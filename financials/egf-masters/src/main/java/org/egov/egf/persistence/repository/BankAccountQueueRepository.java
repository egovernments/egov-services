package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.BankAccountContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BankAccountQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String bankAccountValidatedTopic;

	@Value("${kafka.topics.egf.masters.bankaccount.validated.key}")
	private String bankAccountValidatedKey;

	public void push(BankAccountContractRequest bankAccountContractRequest) {
		HashMap<String, Object> bankAccountContractRequestMap = new HashMap<String, Object>();

		if ("create".equalsIgnoreCase(bankAccountContractRequest.getRequestInfo().getAction()))
			bankAccountContractRequestMap.put("BankAccountCreate", bankAccountContractRequest);
		else if ("updateAll".equalsIgnoreCase(bankAccountContractRequest.getRequestInfo().getAction()))
			bankAccountContractRequestMap.put("BankAccountUpdateAll", bankAccountContractRequest);
		else if ("update".equalsIgnoreCase(bankAccountContractRequest.getRequestInfo().getAction()))
			bankAccountContractRequestMap.put("BankAccountUpdate", bankAccountContractRequest);

		financialProducer.sendMessage(bankAccountValidatedTopic, bankAccountValidatedKey,
				bankAccountContractRequestMap);
	}
}
