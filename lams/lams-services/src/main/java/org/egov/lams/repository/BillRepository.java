package org.egov.lams.repository;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.web.contract.BillInfo;
import org.egov.lams.web.contract.BillRequest;
import org.egov.lams.web.contract.BillResponse;
import org.egov.lams.web.contract.BillSearchCriteria;
import org.egov.lams.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Repository
public class BillRepository {
	
	private static final Logger LOGGER = Logger.getLogger(BillRepository.class);

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
		System.out.println("billRequest url ++++++++++++ "+url);
		BillResponse billResponse = restTemplate.postForObject(url,
				billRequest, BillResponse.class);
		System.out.println("billResponse>>>>>>>>>>"+billResponse.getBillXmls());

		return billResponse.getBillXmls().get(0);
	}

	public BillInfo searchBill(BillSearchCriteria billSearchCriteria, RequestInfo requestInfo) {
		String url = propertiesManager.getDemandServiceHostName()
				+ propertiesManager.getDemandBillSearchService()
				+ "?billId="+billSearchCriteria.getBillId();
			
		LOGGER.info("The url for search bill API ::: "+url);
		BillResponse billResponse = null;
		
		try{
			billResponse = restTemplate.postForObject(url, requestInfo, BillResponse.class);
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("the exception from billsearch API call ::: "+e);
		}
		LOGGER.info("the response for bill search API call ::: "+billResponse);
		return billResponse.getBillInfos().get(0);
	}
	
	
	public Map getPurpose()
	{
		String url = propertiesManager.getPurposeHostName()
				+ propertiesManager.getPurposeService();
		Map purpose = null;
		try {
		System.out.println("url>>>>>>>>>>"+url);
			purpose = restTemplate.getForObject(url, Map.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		System.out.println("Purpose>>>>>>>>>>"+purpose);

		return purpose;
	}
}
