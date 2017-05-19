package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BankBranchQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String bankBranchValidatedTopic;

	@Value("${kafka.topics.egf.masters.bankbranch.validated.key}")
	private String bankBranchValidatedKey;

	public void push(BankBranchContractRequest bankBranchContractRequest) {
		HashMap<String, Object> bankBranchContractRequestMap = new HashMap<String, Object>();
		if (bankBranchContractRequest.getBankBranches() != null
				&& !bankBranchContractRequest.getBankBranches().isEmpty())
			bankBranchContractRequestMap.put("BankBranchCreate", bankBranchContractRequest);
		else if (bankBranchContractRequest.getBankBranch() != null
				&& bankBranchContractRequest.getBankBranch().getId() != null)
			bankBranchContractRequestMap.put("BankBranchUpdate", bankBranchContractRequest);
		financialProducer.sendMessage(bankBranchValidatedTopic, bankBranchValidatedKey, bankBranchContractRequestMap);
	}
}
