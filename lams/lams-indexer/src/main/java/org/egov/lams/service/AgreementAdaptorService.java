package org.egov.lams.service;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.*;
import org.egov.lams.model.*;
import org.egov.lams.repository.*;
import org.egov.lams.repository.helper.InstallmetHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

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
	
	@Autowired
	private InstallmetHelper installmetHelper;
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
		Demand agreementDemand = null;
		
		Asset asset = assetRepository.getAsset(agreement.getAsset().getId(),agreement.getTenantId());
		Allottee allottee = allotteeRepository.getAllottee(agreement.getAllottee().getId(),agreement.getTenantId(),requestInfo);
		City city = tenantRepository.fetchTenantByCode(agreement.getTenantId());
		if(agreement.getDemands() != null){
		agreementDemand = demandRepository.getDemandBySearch(agreement.getDemands().get(0), agreement.getTenantId());
			agreementDetailsEs.setDemandDetails(getDemandDetails(agreementDemand.getDemandDetails()));
		}
		agreementDetailsEs.setAsset(asset);
		agreementDetailsEs.setAgreement(agreement);
		agreementDetailsEs.setAllottee(allottee);
		agreementDetailsEs.setCity(city);
		agreementDetailsEs.setBoundaryDetails(asset.getLocationDetails(), boundaryRepository.getBoundariesById(agreement,asset));
		if(agreementDemand != null){
		logger.info("setting rent details");
		agreementDetailsEs.setRent(agreementDemand.getDemandDetails(),getCurrentInstallment(agreementRequest),propertiesManager.getDemandReasonRent());
		logger.info("rent details are added to indexer");
		}
		agreementIndex.setAgreementDetails(agreementDetailsEs);
		
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
	
	private Installment getCurrentInstallment(AgreementRequest agreementRequest) {
		InstallmentResponse installmentResponse;
		Installment installment = new Installment();
		String installmentUrl = propertiesManager.getDemandServiceHostName()
				+ propertiesManager.getDemandInstallmentSearchPath()
				+ installmetHelper.getInstallmentUrlParams(agreementRequest);

		try {
			logger.info(" intsallment search url :: " + installmentUrl);
			installmentResponse = restTemplate.postForObject(installmentUrl, agreementRequest.getRequestInfo(),
					InstallmentResponse.class);

		} catch (Exception e) {

			logger.info("the exception in intsallment search :: " + e);
			throw new RuntimeException("exception in fetching current installment :" + e.getMessage());
		}
		if (!installmentResponse.getInstallments().isEmpty()) {
			installment = installmentResponse.getInstallments().get(0);
			logger.info("current installment is :" + installment.getDescription());
		}
		return installment;
	}
}