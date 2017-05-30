package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccountCodePurposeQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String accountCodePurposeValidatedTopic;

	@Value("${kafka.topics.egf.masters.accountcodepurpose.validated.key}")
	private String accountCodePurposeValidatedKey;

	public void push(AccountCodePurposeContractRequest accountCodePurposeContractRequest) {
		HashMap<String, Object> accountCodePurposeContractRequestMap = new HashMap<String, Object>();
		if ("create".equalsIgnoreCase(accountCodePurposeContractRequest.getRequestInfo().getAction()))
			accountCodePurposeContractRequestMap.put("AccountCodePurposeCreate", accountCodePurposeContractRequest);
		else if ("updateAll".equalsIgnoreCase(accountCodePurposeContractRequest.getRequestInfo().getAction())
				|| "update".equalsIgnoreCase(accountCodePurposeContractRequest.getRequestInfo().getAction()))
			accountCodePurposeContractRequestMap.put("AccountCodePurposeUpdate", accountCodePurposeContractRequest);

		financialProducer.sendMessage(accountCodePurposeValidatedTopic, accountCodePurposeValidatedKey,
				accountCodePurposeContractRequestMap);
	}
}
