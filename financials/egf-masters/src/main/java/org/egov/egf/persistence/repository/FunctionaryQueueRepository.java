package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.FunctionaryContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FunctionaryQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String functionaryValidatedTopic;

	@Value("${kafka.topics.egf.masters.functionary.validated.key}")
	private String functionaryValidatedKey;

	public void push(FunctionaryContractRequest functionaryContractRequest) {
		HashMap<String, Object> functionaryContractRequestMap = new HashMap<String, Object>();
		if (functionaryContractRequest.getFunctionaries() != null
				&& !functionaryContractRequest.getFunctionaries().isEmpty())
			functionaryContractRequestMap.put("FunctionaryCreate", functionaryContractRequest);
		else if (functionaryContractRequest.getFunctionary() != null
				&& functionaryContractRequest.getFunctionary().getId() != null)
			functionaryContractRequestMap.put("FunctionaryUpdate", functionaryContractRequest);
		financialProducer.sendMessage(functionaryValidatedKey, functionaryValidatedKey,
				functionaryContractRequestMap);
	}
}
