package org.egov.lams.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementDetailsEs;
import org.egov.lams.contract.AgreementIndex;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.DemandDetailsEs;
import org.egov.lams.contract.Installment;
import org.egov.lams.contract.InstallmentResponse;
import org.egov.lams.contract.InstallmentSearchCriteria;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.City;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.repository.AllotteeRepository;
import org.egov.lams.repository.AssetRepository;
import org.egov.lams.repository.BoundaryRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AgreementAdaptorService {
	public static final Logger logger = LoggerFactory.getLogger(AgreementAdaptorService.class);
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private AllotteeRepository allotteeRepository;
	
	@Autowired
	private BoundaryRepository boundaryRepository;
	
	@Autowired
	private DemandRepository demandRepository;
	
	@Autowired
	private TenantRepository tenantRepository;
	/***
	 * method to create agreementdetails object and populate values
	 * 
	 * @param agreement
	 * @return AgreementDetails
	 */
	public AgreementIndex indexOnCreate(AgreementRequest agreementRequest) {
		
		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		AgreementDetailsEs agreementDetailsEs = new AgreementDetailsEs();
		AgreementIndex agreementIndex = new AgreementIndex();
		
		Asset asset = assetRepository.getAsset(agreement.getAsset().getId(),agreement.getTenantId());
		Allottee allottee = allotteeRepository.getAllottee(agreement.getAllottee().getId(),agreement.getTenantId(),requestInfo);
		City city = tenantRepository.fetchTenantByCode(agreement.getTenantId());
		Demand agreementDemand = demandRepository.getDemandBySearch(agreement.getDemands().get(0), agreement.getTenantId());
				
		agreementDetailsEs.setAsset(asset);
		agreementDetailsEs.setAgreement(agreement);
		agreementDetailsEs.setAllottee(allottee);
		agreementDetailsEs.setCity(city);
		agreementDetailsEs.setBoundaryDetails(asset.getLocationDetails(), boundaryRepository.getBoundariesById(agreement,asset));
		agreementDetailsEs.setRent(agreementDemand.getDemandDetails(),getCurrentInstallment(agreement),propertiesManager.getDemandReasonRent());
		agreementIndex.setAgreementDetails(agreementDetailsEs);
		if(agreementDemand != null)
		agreementIndex.setDemandDetails(getDemandDetails(agreementDemand.getDemandDetails()));
		//boundaryRepository.getBoundary(); FIXME make call to city controller
		return agreementIndex;
	}
	
	private List<DemandDetailsEs> getDemandDetails(List<DemandDetails> demandDetails) {
		
		return demandDetails.stream().map(demandDetail -> {
			DemandDetailsEs demandDetailsEs = new DemandDetailsEs();

			demandDetailsEs.setTaxAmount(demandDetail.getTaxAmount());
			demandDetailsEs.setCollectionAmount(demandDetail.getCollectionAmount());
			demandDetailsEs.setRebateAmount(demandDetail.getRebateAmount());
			demandDetailsEs.setTaxPeriod(demandDetail.getTaxPeriod());
			demandDetailsEs.setTaxReason(demandDetail.getTaxReason());
			demandDetailsEs.setGlCode(demandDetail.getGlCode());
			demandDetailsEs.setIsActualDemand(demandDetail.getIsActualDemand());
			demandDetailsEs.setPeriodStartDate(demandDetail.getPeriodStartDate());
			demandDetailsEs.setPeriodEndDate(demandDetail.getPeriodEndDate());
			
			return demandDetailsEs;
	}).collect(Collectors.toList());
  }
	
	private Installment getCurrentInstallment(Agreement agreement) {
		InstallmentResponse installmentResponse;
		Installment installment = new Installment();
		InstallmentSearchCriteria installmentSearchCriteria = new InstallmentSearchCriteria();
		String installmentUrl = propertiesManager.getDemandServiceHostName()
				+ propertiesManager.getDemandInstallmentSearchPath();
		installmentSearchCriteria.setCurrentDate(new Date());
		installmentSearchCriteria.setTenantId(agreement.getTenantId());
		installmentSearchCriteria.setInstallmentType(agreement.getPaymentCycle().toString());
		installmentSearchCriteria.setModule(propertiesManager.getModuleName());

		try {
			logger.info(" intsallment search url :: " + installmentUrl);
			installmentResponse = restTemplate.postForObject(installmentUrl, installmentSearchCriteria,
					InstallmentResponse.class);

		} catch (Exception e) {

			logger.info("the exception in intsallment search :: " + e);
			throw new RuntimeException("exception in fetching current installment :" + e.getMessage());
		}
		if (!installmentResponse.getInstallments().isEmpty()) {
			installment = installmentResponse.getInstallments().get(0);
		}
		return installment;
	}
}