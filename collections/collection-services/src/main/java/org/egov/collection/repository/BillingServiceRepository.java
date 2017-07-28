package org.egov.collection.repository;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.web.contract.Bill;
import org.egov.collection.web.contract.BillRequest;
import org.egov.collection.web.contract.BillResponse;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BillingServiceRepository {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(BillingServiceRepository.class);
	@Autowired
	public RestTemplate restTemplate;
	@Autowired
	private ApplicationProperties applicationProperties;

	public BillResponse getApportionListFromBillingService(
			RequestInfo requestInfo, Bill apportionBill) {
		LOGGER.info("Apportion Paid Amount in Billing Service");
		StringBuilder uriForApportion = new StringBuilder();
		uriForApportion
				.append(applicationProperties.getBillingServiceHostName())
				.append(applicationProperties.getBillingServiceApportion())
				.append("&tenantId=").append(apportionBill.getTenantId());
		LOGGER.info("URI For Apportioning Paid Amount in Billing Service: "
				+ uriForApportion.toString());
		BillRequest billRequest = new BillRequest();
		billRequest.setRequestInfo(requestInfo);
		billRequest.getBills().add(apportionBill);
		BillResponse response = null;
		try {
			response = restTemplate.postForObject(uriForApportion.toString(),
					billRequest, BillResponse.class);
		} catch (Exception e) {
			LOGGER.error("Error while apportioning paid amount from billing service. "
					+ e);
		}
		LOGGER.info("Response from coll-master: " + response);
		return response;
	}
}
