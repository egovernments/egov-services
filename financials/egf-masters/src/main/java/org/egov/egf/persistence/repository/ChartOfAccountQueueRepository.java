package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.ChartOfAccountContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChartOfAccountQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String chartOfAccountValidatedTopic;

	@Value("${kafka.topics.egf.masters.chartofaccount.validated.key}")
	private String chartOfAccountValidatedKey;

	public void push(ChartOfAccountContractRequest chartOfAccountContractRequest) {
		HashMap<String, Object> chartOfAccountContractRequestMap = new HashMap<String, Object>();

		if ("create".equalsIgnoreCase(chartOfAccountContractRequest.getRequestInfo().getAction()))
			chartOfAccountContractRequestMap.put("ChartOfAccountCreate", chartOfAccountContractRequest);
		else if ("updateAll".equalsIgnoreCase(chartOfAccountContractRequest.getRequestInfo().getAction()))
			chartOfAccountContractRequestMap.put("ChartOfAccountUpdateAll", chartOfAccountContractRequest);
		else if ("update".equalsIgnoreCase(chartOfAccountContractRequest.getRequestInfo().getAction()))
			chartOfAccountContractRequestMap.put("ChartOfAccountUpdate", chartOfAccountContractRequest);

		financialProducer.sendMessage(chartOfAccountValidatedTopic, chartOfAccountValidatedKey,
				chartOfAccountContractRequestMap);
	}
}
