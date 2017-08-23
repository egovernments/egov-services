package org.egov.citizen.repository;

import org.egov.citizen.config.ApplicationProperties;
import org.egov.citizen.model.BillResponse;
import org.egov.citizen.model.BillingServiceRequestWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Repository
public class BillingServiceRepository {
	
	public static final Logger LOGGER = LoggerFactory
			.getLogger(BillingServiceRepository.class);
	
	@Autowired
	public RestTemplate restTemplate;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	
	public BillResponse getBillOnBillNumber(BillingServiceRequestWrapper billingServiceRequestWrapper) {
		LOGGER.info("Search bill from Billing Service");
		StringBuilder uri = new StringBuilder();
		String searchCriteria = "?billId="+billingServiceRequestWrapper.getBillNumber()
								+"&tenantId="+billingServiceRequestWrapper.getTenantId();
		uri.append(applicationProperties.getBillingServiceHostName())
		   .append(applicationProperties.getSearchBill())
		   .append(searchCriteria);
		LOGGER.info("URI for search bill in Billing Service: "
				+ uri.toString());
		BillResponse response = null;
		response = restTemplate.postForObject(uri.toString(),
					billingServiceRequestWrapper, BillResponse.class);
		LOGGER.info("Response from billing service: " + response);
		return response;
	}
	
	public BillResponse generateBillForDemand(RequestInfo requestInfo, String tenantId,
			String mobileNumber){
		LOGGER.info("Generating bill from Billing Service");
		StringBuilder uri = new StringBuilder();
		String generateCriteria ="?mobileNumber="+mobileNumber;
		uri.append(applicationProperties.getBillingServiceHostName())
		   .append(applicationProperties.getGenerateBill())
		   .append(generateCriteria);
		LOGGER.info("URI for Generating bill from Billing Service: "
				+ uri.toString());
		BillResponse response = null;
		response = restTemplate.postForObject(uri.toString(),
				requestInfo, BillResponse.class);
		LOGGER.info("Response from billing service: " + response);
		return response;
		
	}

}
