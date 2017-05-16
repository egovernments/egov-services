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

	@Value("${kafka.topics.egf.master.validated.topic}")
	private String bankValidatedTopic;

	@Value("${kafka.topics.egf.master.bank.validated.key}")
	private String bankValidatedKey;

	public void push(BankContractRequest bankContractRequest) {
		HashMap<String, Object> bankContractRequestMap = new HashMap<String, Object>();
		bankContractRequestMap.put("Bank", bankContractRequest);
		financialProducer.sendMessage(bankValidatedTopic, bankValidatedKey, bankContractRequestMap);
	}
}
