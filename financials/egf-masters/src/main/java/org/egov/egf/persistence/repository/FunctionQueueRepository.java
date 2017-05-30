package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.FunctionContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FunctionQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String functionValidatedTopic;

	@Value("${kafka.topics.egf.masters.function.validated.key}")
	private String functionValidatedKey;

	public void push(FunctionContractRequest functionContractRequest) {
		HashMap<String, Object> functionContractRequestMap = new HashMap<String, Object>();

		if ("create".equalsIgnoreCase(functionContractRequest.getRequestInfo().getAction()))
			functionContractRequestMap.put("FunctionCreate", functionContractRequest);
		else if ("updateAll".equalsIgnoreCase(functionContractRequest.getRequestInfo().getAction()))
			functionContractRequestMap.put("FunctionUpdateAll", functionContractRequest);
		else if ("update".equalsIgnoreCase(functionContractRequest.getRequestInfo().getAction()))
			functionContractRequestMap.put("FunctionUpdate", functionContractRequest);

		financialProducer.sendMessage(functionValidatedTopic, functionValidatedKey, functionContractRequestMap);
	}
}
