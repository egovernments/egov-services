package org.egov.citizen.repository;

import org.egov.citizen.config.ApplicationProperties;
import org.egov.citizen.model.BillResponse;
import org.egov.citizen.model.BillingServiceRequestWrapper;
import org.egov.citizen.model.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class BillingServiceRepository {
	
	public static final Logger LOGGER = LoggerFactory
			.getLogger(BillingServiceRepository.class);
	
	@Autowired
	public RestTemplate restTemplate;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	
	public BillResponse getBill(BillingServiceRequestWrapper billingServiceRequestWrapper) {
		LOGGER.info("Search bill from Billing Service");
		StringBuilder uri = new StringBuilder();
		String searchCriteriaOnBillId = "?billId="+billingServiceRequestWrapper.getBillNumber()
								+"&tenantId="+billingServiceRequestWrapper.getTenantId();
		String searchCriteriaOnConsumerCode = "?consumerCode="+billingServiceRequestWrapper.getConsumerNumber()
		                                      +"&tenantId="+billingServiceRequestWrapper.getTenantId();
		if(null != billingServiceRequestWrapper.getConsumerNumber() || !billingServiceRequestWrapper.getConsumerNumber().isEmpty()){
			uri.append(applicationProperties.getBillingServiceHostName())
			   .append(applicationProperties.getSearchBill())
			   .append(searchCriteriaOnConsumerCode);
		}else{
			uri.append(applicationProperties.getBillingServiceHostName())
			   .append(applicationProperties.getSearchBill())
			   .append(searchCriteriaOnBillId);
		}
		LOGGER.info("URI for search bill in Billing Service: "
				+ uri.toString());
		BillResponse response = null;
		response = restTemplate.postForObject(uri.toString(),
					billingServiceRequestWrapper, BillResponse.class);
		LOGGER.info("Response from billing service: " + response);
		return response;
	}
	
	public Object getDemand(BillingServiceRequestWrapper billingServiceRequestWrapper) {
		LOGGER.info("Search bill from Billing Service");
		StringBuilder uri = new StringBuilder();
		String searchCriteriaOnConsumerCode = "?consumerCode="+billingServiceRequestWrapper.getConsumerNumber()
		                                      +"&tenantId="+billingServiceRequestWrapper.getTenantId()
		                                      +"&businessService="+billingServiceRequestWrapper.getBuisnessService();
			uri.append(applicationProperties.getBillingServiceHostName())
			   .append(applicationProperties.getSearchDues())
			   .append(searchCriteriaOnConsumerCode);

		LOGGER.info("URI for search bill in Billing Service: "
				+ uri.toString());
		Object response = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(billingServiceRequestWrapper.getBillingServiceRequestInfo());
		response = restTemplate.postForObject(uri.toString(),
				requestInfoWrapper, Object.class);
		LOGGER.info("Response from billing service: " + response);
		return response;
	}
	
	public BillResponse generateBillForDemand(RequestInfoWrapper requestInfoWrapper, String tenantId,
			String consumerCode, String buisnessService){
		LOGGER.info("Generating bill from Billing Service");
		StringBuilder uri = new StringBuilder();
		String generateCriteria ="?consumerCode="+consumerCode+"&tenantId="+tenantId+"&businessService="+buisnessService;
		uri.append(applicationProperties.getBillingServiceHostName())
		   .append(applicationProperties.getGenerateBill())
		   .append(generateCriteria);
		LOGGER.info("URI for Generating bill from Billing Service: "
				+ uri.toString());
		BillResponse response = null;
		response = restTemplate.postForObject(uri.toString(),
				requestInfoWrapper, BillResponse.class);
		LOGGER.info("Response from billing service: " + response);
		return response;
		
	}

}
