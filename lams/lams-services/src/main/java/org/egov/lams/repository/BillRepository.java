package org.egov.lams.repository;

import java.util.List;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.RequestInfo;
import org.egov.lams.web.contract.BillInfo;
import org.egov.lams.web.contract.BillRequest;
import org.egov.lams.web.contract.BillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class BillRepository {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	public String createBillAndGetXml(List<BillInfo> billInfos, RequestInfo requestInfo){
		
		BillRequest billRequest = new org.egov.lams.web.contract.BillRequest();
		billRequest.setRequestInfo(requestInfo);
		billRequest.setBillInfos(billInfos);
		
		String url = propertiesManager.getDemandServiceHostName()
				+propertiesManager.getDemandBillCreateService();
		
		BillResponse billResponse = restTemplate.postForObject(url, billRequest, BillResponse.class);
		return billResponse.getBillXmls().get(0);
	}
}
