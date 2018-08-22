package org.egov.lams.repository;

import org.apache.log4j.Logger;
import org.egov.lams.config.ApplicationProperties;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.exceptions.CollectionExceedException;
import org.egov.lams.web.contract.BillInfo;
import org.egov.lams.web.contract.BillRequest;
import org.egov.lams.web.contract.BillResponse;
import org.egov.lams.web.contract.BillSearchCriteria;
import org.egov.lams.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class BillRepository {

	private static final Logger LOGGER = Logger.getLogger(BillRepository.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	ApplicationProperties applicationProperties;

	public String createBillAndGetXml(List<BillInfo> billInfos, RequestInfo requestInfo) {

		BillRequest billRequest = new BillRequest();
		billRequest.setRequestInfo(requestInfo);
		billRequest.setBillInfos(billInfos);

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getDemandBillCreateService();
		LOGGER.info("billRequest url ++++++++++++ " + url);
		BillResponse billResponse = restTemplate.postForObject(url, billRequest, BillResponse.class);
		LOGGER.info("billResponse>>>>>>>>>>" + billResponse.getBillXmls());
		if (billResponse.getBillXmls().isEmpty()) {
			LOGGER.info("exception occured while getting billInfo");
			throw new CollectionExceedException();
		} else
			return billResponse.getBillXmls().get(0);
	}


	public BillResponse updateBill(List<BillInfo> billInfos, RequestInfo requestInfo) {

		BillRequest billRequest = new BillRequest();
		billRequest.setRequestInfo(requestInfo);
		billRequest.setBillInfos(billInfos);

		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getDemandBillUpdateService();
		LOGGER.info("the url for update demand API call is  ::: " + url);

		BillResponse billResponse = null;
		try {
			billResponse = restTemplate.postForObject(url, billRequest, BillResponse.class);
		} catch (Exception e) {

			LOGGER.info("the exception raised during update demand API call ::: " + e);
		}
		LOGGER.info("the exception raised during update demand API call ::: " + billResponse);
		return billResponse;
	}


	public BillInfo searchBill(BillSearchCriteria billSearchCriteria, RequestInfo requestInfo) {
		String url = propertiesManager.getDemandServiceHostName() + propertiesManager.getDemandBillSearchService()
				+ "?billId=" + billSearchCriteria.getBillId();

		LOGGER.info("The url for search bill API ::: " + url);
		BillResponse billResponse = null;
		if (requestInfo == null) {
			// FIXME remove this when application is running good
			LOGGER.info("requestInfo ::: is null ");
			requestInfo = new RequestInfo();
			requestInfo.setApiId("apiid");
			requestInfo.setVer("ver");
			requestInfo.setTs(new Date());
		}

		try {
			billResponse = restTemplate.postForObject(url, requestInfo, BillResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception from billsearch API call ::: " + e);
		}
		LOGGER.info("the response for bill search API call ::: " + billResponse.getBillInfos().get(0));

		return billResponse.getBillInfos().get(0);
	}

	public Map getPurpose(String tenantId) {

		Map purpose = null;
		try {
			String url = applicationProperties.getHostNameForMonolith(tenantId) + propertiesManager.getPurposeService();
			LOGGER.info("url>>>>>>>>>>" + url);
			purpose = restTemplate.getForObject(url, Map.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		LOGGER.info("Purpose>>>>>>>>>>" + purpose);

		return purpose;
	}
}