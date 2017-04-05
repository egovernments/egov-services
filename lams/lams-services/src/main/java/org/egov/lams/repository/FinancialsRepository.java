package org.egov.lams.repository;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.web.contract.ChartOfAccountContract;
import org.egov.lams.web.contract.ChartOfAccountContractResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class FinancialsRepository {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public String getChartOfAccountGlcodeById(ChartOfAccountContract chartOfAccountContract,
			RequestInfo requestInfo) {

		String url = propertiesManager.getFinancialServiceHostName()
				+ propertiesManager.getFinancialGetChartOfAccountsService();
		System.out.println("chartOfAccountContract url ++++++++++++ "+url);
		ChartOfAccountContractResponse chartOfAccountContractResponse =  restTemplate.postForObject(url, requestInfo,
				ChartOfAccountContractResponse.class,chartOfAccountContract);
		System.out.println("chartOfAccountContract>>>>>>>>>>"+chartOfAccountContractResponse.getChartOfAccounts().get(0).getGlcode());

		return chartOfAccountContractResponse.getChartOfAccounts().get(0).getGlcode();
	}
}
