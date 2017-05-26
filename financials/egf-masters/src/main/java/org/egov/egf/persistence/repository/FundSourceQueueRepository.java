package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.FundsourceContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FundSourceQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String fundSourceValidatedTopic;

	@Value("${kafka.topics.egf.masters.fundsource.validated.key}")
	private String fundSourceValidatedKey;

	public void push(FundsourceContractRequest fundSourceContractRequest) {
		HashMap<String, Object> fundSourceContractRequestMap = new HashMap<String, Object>();

		if ("create".equalsIgnoreCase(fundSourceContractRequest.getRequestInfo().getAction()))
			fundSourceContractRequestMap.put("FundSourceCreate", fundSourceContractRequest);
		else if ("updateAll".equalsIgnoreCase(fundSourceContractRequest.getRequestInfo().getAction()))
			fundSourceContractRequestMap.put("FundSourceUpdateAll", fundSourceContractRequest);
		else if ("update".equalsIgnoreCase(fundSourceContractRequest.getRequestInfo().getAction()))
			fundSourceContractRequestMap.put("FundSourceUpdateAll", fundSourceContractRequest);

		financialProducer.sendMessage(fundSourceValidatedTopic, fundSourceValidatedKey, fundSourceContractRequestMap);
	}
}
