package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.FinancialYearContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FinancialYearQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String financialYearValidatedTopic;

	@Value("${kafka.topics.egf.masters.financialyear.validated.key}")
	private String financialYearValidatedKey;

	public void push(FinancialYearContractRequest financialYearContractRequest) {
		HashMap<String, Object> financialYearContractRequestMap = new HashMap<String, Object>();
		if ("create".equalsIgnoreCase(financialYearContractRequest.getRequestInfo().getAction()))
			financialYearContractRequestMap.put("FinancialYearCreate", financialYearContractRequest);
		else if ("updateAll".equalsIgnoreCase(financialYearContractRequest.getRequestInfo().getAction()))
			financialYearContractRequestMap.put("FinancialYearUpdateAll", financialYearContractRequest);
		else if ("update".equalsIgnoreCase(financialYearContractRequest.getRequestInfo().getAction()))
			financialYearContractRequestMap.put("FinancialYearUpdate", financialYearContractRequest);
		
		financialProducer.sendMessage(financialYearValidatedTopic, financialYearValidatedKey,
				financialYearContractRequestMap);
	}
}
