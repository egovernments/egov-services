package org.egov.tradelicense.notification.web.repository;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.notification.config.PropertiesManager;
import org.egov.tradelicense.notification.web.responses.ReceiptRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CollectionServiceRepository {

	private RestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	public CollectionServiceRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;

	}

	public String findRecieptNumber(String tenantId, String applicationNumber, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		String hostUrl = propertiesManager.getCollectionServiceHostName()
				+ propertiesManager.getCollectionServiceBasePath();
		String searchUrl = propertiesManager.getCollectionSearchPath();

		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (tenantId != null) {
			content.append("tenantId=" + tenantId);
		}

		if (applicationNumber != null) {
			content.append("&consumerCode=" + applicationNumber);
		}

		content.append("&businessCode=TRADELICENSE");

		url = url + content.toString();
		ReceiptRes receiptResponse = null;

		try {

			receiptResponse = restTemplate.postForObject(url, requestInfoWrapper, ReceiptRes.class);

		} catch (Exception e) {

			log.error("Error connecting to collection service end point :" + url);
		}

		if (receiptResponse != null && receiptResponse.getReceipts() != null && receiptResponse.getReceipts().size() > 0
				&& receiptResponse.getReceipts().get(0).getBill() != null
				&& !receiptResponse.getReceipts().get(0).getBill().isEmpty()
				&& receiptResponse.getReceipts().get(0).getBill().get(0).getBillDetails() != null
				&& !receiptResponse.getReceipts().get(0).getBill().get(0).getBillDetails().isEmpty()) {

			System.out.println("Reciept Response Details:" + receiptResponse.getReceipts().get(0).getBill().get(0).getBillDetails().toString());
			return receiptResponse.getReceipts().get(0).getBill().get(0).getBillDetails().get(0).getReceiptNumber();

		} else {

			return null;
		}
	}
}