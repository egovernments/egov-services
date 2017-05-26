package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BankQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String bankValidatedTopic;

	@Value("${kafka.topics.egf.masters.bank.validated.key}")
	private String bankValidatedKey;

	public void push(BankContractRequest bankContractRequest) {
		HashMap<String, Object> bankContractRequestMap = new HashMap<String, Object>();

		if ("create".equalsIgnoreCase(bankContractRequest.getRequestInfo().getAction()))
			bankContractRequestMap.put("BankCreate", bankContractRequest);
		else if ("updateAll".equalsIgnoreCase(bankContractRequest.getRequestInfo().getAction()))
			bankContractRequestMap.put("BankUpdateAll", bankContractRequest);
		else if ("update".equalsIgnoreCase(bankContractRequest.getRequestInfo().getAction()))
			bankContractRequestMap.put("BankUpdate", bankContractRequest);

		financialProducer.sendMessage(bankValidatedTopic, bankValidatedKey, bankContractRequestMap);
	}
}
