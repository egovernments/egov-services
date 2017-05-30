package org.egov.lams.repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.DemandReason;
import org.egov.lams.repository.helper.DemandHelper;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.DemandReasonResponse;
import org.egov.lams.web.contract.DemandRequest;
import org.egov.lams.web.contract.DemandResponse;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.UserErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class DemandRepository {

	private static final Logger LOGGER = Logger.getLogger(DemandRepository.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	DemandHelper demandHelper;

	public List<DemandReason> getDemandReason(AgreementRequest agreementRequest) {
		
		List<DemandReason> demandReasons = new ArrayList<>();
		String taxReason = propertiesManager.getTaxReasonAdvanceTax();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(agreementRequest.getAgreement().getCommencementDate());
		calendar.add(Calendar.MONTH,1);
		Date date = calendar.getTime();
		for(int i=0;i<3;i++){

		if(i == 1)
			taxReason = propertiesManager.getTaxReasonGoodWillAmount();
		else if (i == 2){
			taxReason = propertiesManager.getTaxReasonRent();
			date = agreementRequest.getAgreement().getExpiryDate();
		}
		
		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getDemandReasonSearchPath()
				+ demandHelper.getDemandReasonUrlParams(agreementRequest,taxReason,date);

		System.out.println("DemandRepository getDemandReason url:" + url);
		DemandReasonResponse demandReasonResponse = null;
		try {
			demandReasonResponse = restTemplate.postForObject(url, agreementRequest.getRequestInfo(),
					DemandReasonResponse.class);
			LOGGER.info(demandReasonResponse);
		} catch (HttpClientErrorException e) {
			String errorResponseBody = e.getResponseBodyAsString();
			LOGGER.info("Following exception occurred: " + e.getResponseBodyAsString());
			UserErrorResponse userErrorResponse = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				userErrorResponse = mapper.readValue(errorResponseBody, UserErrorResponse.class);
			} catch (JsonMappingException jme) {
				LOGGER.error("Following Exception Occurred While Mapping JSON Response From User Service : "
						+ jme.getMessage());
				jme.printStackTrace();
			} catch (JsonProcessingException jpe) {
				LOGGER.error("Following Exception Occurred While Processing JSON Response From User Service : "
						+ jpe.getMessage());
				jpe.printStackTrace();
			} catch (IOException ioe) {
				LOGGER.error("Following Exception Occurred Calling User Service : " + ioe.getMessage());
				ioe.printStackTrace();
			}
			// return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
			LOGGER.error("the exception from user module inside first catch block ::"
					+ userErrorResponse.getError().toString());
		} catch (Exception e) {
			LOGGER.error("Following Exception Occurred While Calling User Service : " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("demandReasonResponse:" + demandReasonResponse);
		demandReasons.addAll(demandReasonResponse.getDemandReasons());
		}
		return demandReasons;
	}

	public List<Demand> getDemandList(AgreementRequest agreementRequest, List<DemandReason> demandReasons) {

		Agreement agreement = agreementRequest.getAgreement();
		List<Demand> demands = new ArrayList<>();
		List<DemandDetails> demandDetails = new ArrayList<>();
		Demand demand = new Demand();
		demand.setTenantId(agreement.getTenantId());
		demand.setInstallment(demandReasons.get(0).getTaxPeriod());
		demand.setModuleName("Leases And Agreements");

		DemandDetails demandDetail = null;
		for (DemandReason demandReason : demandReasons) {
			
			demandDetail = new DemandDetails();
			LOGGER.info("the demand reason object in the loop : "+ demandReason);
			if ("RENT".equalsIgnoreCase(demandReason.getName())) {
				demandDetail.setTaxAmount(BigDecimal.valueOf(agreement.getRent()));
			} else if ("GOODWILL_AMOUNT".equalsIgnoreCase(demandReason.getName()) ) {
				demandDetail.setTaxAmount(BigDecimal.valueOf(agreement.getGoodWillAmount()));
			} else if ("ADVANCE_TAX".equalsIgnoreCase(demandReason.getName())) {
				demandDetail.setTaxAmount(BigDecimal.valueOf(agreement.getSecurityDeposit()));
			}
			demandDetail.setCollectionAmount(BigDecimal.ZERO);
			demandDetail.setRebateAmount(BigDecimal.ZERO);
			demandDetail.setTaxReason(demandReason.getName());
			demandDetail.setTaxPeriod(demandReason.getTaxPeriod());

			demandDetails.add(demandDetail);
		}
		demand.setDemandDetails(demandDetails);
		demands.add(demand);
		System.err.println(demands);

		return demands;
	}

	public DemandResponse createDemand(List<Demand> demands, RequestInfo requestInfo) {
		System.out.println("DemandRepository createDemand demands:" + demands.toString());
		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemand(demands);

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getCreateDemandSevice();

		return restTemplate.postForObject(url, demandRequest, DemandResponse.class);
	}

	public DemandResponse getDemandBySearch(DemandSearchCriteria demandSearchCriteria, RequestInfo requestInfo) {

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getDemandSearchServicepath()
				+ "?demandId=" + demandSearchCriteria.getDemandId();
		LOGGER.info("the url of demand search API call ::: is " + url);

		if (requestInfo == null) {
			// FIXME remove this when application is running good
			LOGGER.info("requestInfo ::: is null ");
			requestInfo = new RequestInfo();
			requestInfo.setApiId("apiid");
			requestInfo.setVer("ver");
			requestInfo.setTs(new Date());
		}

		DemandResponse demandResponse = null;
		try {
			demandResponse = restTemplate.postForObject(url, requestInfo, DemandResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception thrown from demand search api call ::: " + e);
		}
		LOGGER.info("the response form demand search api call ::: " + demandResponse);
		return demandResponse;
	}

	public DemandResponse updateDemandForCollection(List<Demand> demands, RequestInfo requestInfo) {

		System.out.println("DemandRepository createDemand demands:" + demands.toString());
		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemand(demands);

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getUpdateDemandBasePath()
				+ demands.get(0).getId() + "/" + propertiesManager.getUpdateDemandService();
		LOGGER.info("the url for update demand API call is  ::: " + url);

		DemandResponse demandResponse = null;
		try {
			demandResponse = restTemplate.postForObject(url, demandRequest, DemandResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception raised during update demand API call ::: " + e);
		}
		LOGGER.info("the exception raised during update demand API call ::: " + demandResponse);
		return demandResponse;
	}

	public DemandResponse updateDemand(List<Demand> demands, RequestInfo requestInfo) {

		System.out.println("DemandRepository createDemand demands:" + demands.toString());
		DemandRequest demandRequest = new DemandRequest();
		demandRequest.setRequestInfo(requestInfo);
		demandRequest.setDemand(demands);

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getUpdateDemandBasePath()
				+ propertiesManager.getUpdateDemandService();

		LOGGER.info("the url for update demand API call is  ::: " + url);

		DemandResponse demandResponse = null;
		try {
			demandResponse = restTemplate.postForObject(url, demandRequest, DemandResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception raised during update demand API call ::: " + e);
		}
		LOGGER.info("the exception raised during update demand API call ::: " + demandResponse);
		return demandResponse;
	}
}
