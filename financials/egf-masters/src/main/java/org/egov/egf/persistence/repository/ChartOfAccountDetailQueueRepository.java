package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChartOfAccountDetailQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String chartOfAccountDetailValidatedTopic;

	@Value("${kafka.topics.egf.masters.chartofaccount.validated.key}")
	private String chartOfAccountDetailValidatedKey;

	public void push(ChartOfAccountDetailContractRequest chartOfAccountDetailContractRequest) {
		HashMap<String, Object> chartOfAccountDetailContractRequestMap = new HashMap<String, Object>();
		
		if ("create".equalsIgnoreCase(chartOfAccountDetailContractRequest.getRequestInfo().getAction()))
			chartOfAccountDetailContractRequestMap.put("ChartOfAccountDetailCreate",
					chartOfAccountDetailContractRequest);
		else if ("updateAll".equalsIgnoreCase(chartOfAccountDetailContractRequest.getRequestInfo().getAction()))
			chartOfAccountDetailContractRequestMap.put("ChartOfAccountDetailUpdateAll",
					chartOfAccountDetailContractRequest);
		else if ("update".equalsIgnoreCase(chartOfAccountDetailContractRequest.getRequestInfo().getAction()))
			chartOfAccountDetailContractRequestMap.put("ChartOfAccountDetailUpdate",
					chartOfAccountDetailContractRequest);
		
		financialProducer.sendMessage(chartOfAccountDetailValidatedTopic, chartOfAccountDetailValidatedKey,
				chartOfAccountDetailContractRequestMap);
	}
}
