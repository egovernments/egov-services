package org.egov.lams.repository;

import java.util.List;
import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.BillInfo;
import org.egov.lams.web.contract.BillRequest;
import org.egov.lams.web.contract.BillResponse;
import org.egov.lams.web.contract.BillSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class BillRepository {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public String createBillAndGetXml(List<BillInfo> billInfos,
			RequestInfo requestInfo) {

		BillRequest billRequest = new BillRequest();
		billRequest.setRequestInfo(requestInfo);
		billRequest.setBillInfos(billInfos);

		String url = propertiesManager.getDemandServiceHostName()
				+ propertiesManager.getDemandBillCreateService();

		BillResponse billResponse = restTemplate.postForObject(url,
				billRequest, BillResponse.class);
		return billResponse.getBillXmls().get(0);
	}

	public BillInfo searchBill(BillSearchCriteria billSearchCriteria, RequestInfo requestInfo) {
		String url = propertiesManager.getDemandServiceHostName()
				+ propertiesManager.getDemandBillSearchService();

		BillResponse billResponse = restTemplate.postForObject(url, requestInfo,
				 BillResponse.class,billSearchCriteria);
		return billResponse.getBillInfos().get(0);
	}
	
	
	public Map getPurpose()
	{
		String url = propertiesManager.getPurposeHostName()
				+ propertiesManager.getPurposeService();
		Map purpose = restTemplate.getForObject(url, Map.class);
		return purpose;
	}
}
