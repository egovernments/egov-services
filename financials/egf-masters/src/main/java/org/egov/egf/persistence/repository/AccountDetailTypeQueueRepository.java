package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.AccountDetailTypeContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailTypeQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String accountDetailTypeValidatedTopic;

	@Value("${kafka.topics.egf.masters.accountdetailtype.validated.key}")
	private String accountDetailTypeValidatedKey;

	public void push(AccountDetailTypeContractRequest accountDetailTypeContractRequest) {
		HashMap<String, Object> accountDetailTypeContractRequestMap = new HashMap<String, Object>();
		if (accountDetailTypeContractRequest.getAccountDetailTypes() != null
				&& !accountDetailTypeContractRequest.getAccountDetailTypes().isEmpty())
			accountDetailTypeContractRequestMap.put("AccountDetailTypeCreate", accountDetailTypeContractRequest);
		else if (accountDetailTypeContractRequest.getAccountDetailType() != null
				&& accountDetailTypeContractRequest.getAccountDetailType().getId() != null)
			accountDetailTypeContractRequestMap.put("AccountDetailTypeUpdate", accountDetailTypeContractRequest);

		financialProducer.sendMessage(accountDetailTypeValidatedTopic, accountDetailTypeValidatedKey,
				accountDetailTypeContractRequestMap);
	}
}
