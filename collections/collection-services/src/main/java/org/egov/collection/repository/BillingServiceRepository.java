package org.egov.collection.repository;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.BillingServiceRequestInfo;
import org.egov.collection.model.BillingServiceRequestWrapper;
import org.egov.collection.web.contract.Bill;
import org.egov.collection.web.contract.BillDetail;
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
	private RestTemplate restTemplate;
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
					, e);
		}
		LOGGER.info("Response from billing service: " + response);
		return response;
	}

	public BillResponse getBillForBillId(final RequestInfo requestInfo, final Bill bill, final BillDetail billDetail) {
		LOGGER.info("Search bill from Billing Service");
		StringBuilder uri = new StringBuilder();
		String searchCriteria = "?billId=" + bill.getId() + "&tenantId="
				+ bill.getTenantId() + "&consumerCode=" + billDetail.getConsumerCode() + "&service=" + billDetail.getBusinessService();
		uri.append(applicationProperties.getBillingServiceHostName())
				.append(applicationProperties.getSearchBill())
				.append(searchCriteria);
		LOGGER.info("URI for search bill in Billing Service: " + uri.toString());
		BillResponse response = null;
		try {
			response = restTemplate.postForObject(uri.toString(),
					prepareBillRequestWrapper(requestInfo), BillResponse.class);
		} catch (Exception e) {
			LOGGER.error("Error while searching bill from billing service. "
					, e);
		}
		LOGGER.info("Response from billing service: " + response);
		return response;
	}

	private BillingServiceRequestWrapper prepareBillRequestWrapper(
			RequestInfo requestInfo) {
		BillingServiceRequestWrapper billingServiceRequestWrapper = new BillingServiceRequestWrapper();
		BillingServiceRequestInfo billingServiceRequestInfo = new BillingServiceRequestInfo();

		// Because Billing Svc uses a slightly different form of requestInfo

		billingServiceRequestInfo.setAction(requestInfo.getAction());
		billingServiceRequestInfo.setApiId(requestInfo.getApiId());
		billingServiceRequestInfo.setAuthToken(requestInfo.getAuthToken());
		billingServiceRequestInfo.setCorrelationId(requestInfo
				.getCorrelationId());
		billingServiceRequestInfo.setDid(requestInfo.getDid());
		billingServiceRequestInfo.setKey(requestInfo.getKey());
		billingServiceRequestInfo.setMsgId(requestInfo.getMsgId());
		// billingServiceRequestInfo.setRequesterId(requestInfo.getRequesterId());
		billingServiceRequestInfo.setTs(requestInfo.getTs().getTime()); // this
																// is
																// the
																// difference
		billingServiceRequestInfo.setUserInfo(requestInfo.getUserInfo());
		billingServiceRequestInfo.setVer(requestInfo.getVer());

		billingServiceRequestWrapper
				.setBillingServiceRequestInfo(billingServiceRequestInfo);
		return billingServiceRequestWrapper;
	}
}
