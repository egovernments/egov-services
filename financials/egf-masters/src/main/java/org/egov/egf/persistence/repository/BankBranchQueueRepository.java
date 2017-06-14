package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BankBranchQueueRepository {

	private FinancialProducer financialProducer;

	private String bankBranchValidatedTopic;

	private String bankBranchValidatedKey;

	@Autowired
	public BankBranchQueueRepository(FinancialProducer financialProducer,
			@Value("${kafka.topics.egf.masters.validated.topic}") String bankBranchValidatedTopic,
			@Value("${kafka.topics.egf.masters.bankbranch.validated.key}") String bankBranchValidatedKey) {
		this.financialProducer = financialProducer;
		this.bankBranchValidatedTopic = bankBranchValidatedTopic;
		this.bankBranchValidatedKey = bankBranchValidatedKey;
	}

	public void push(BankBranchContractRequest bankBranchContractRequest) {
		HashMap<String, Object> bankBranchContractRequestMap = new HashMap<String, Object>();

		if ("create".equalsIgnoreCase(bankBranchContractRequest.getRequestInfo().getAction()))
			bankBranchContractRequestMap.put("BankBranchCreate", bankBranchContractRequest);
		else if ("updateAll".equalsIgnoreCase(bankBranchContractRequest.getRequestInfo().getAction())
				|| "update".equalsIgnoreCase(bankBranchContractRequest.getRequestInfo().getAction()))
			bankBranchContractRequestMap.put("BankBranchUpdate", bankBranchContractRequest);

		financialProducer.sendMessage(bankBranchValidatedTopic, bankBranchValidatedKey, bankBranchContractRequestMap);
	}
}
