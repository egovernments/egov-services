package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.AccountDetailKeyContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailKeyQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String accountDetailKeyValidatedTopic;

	@Value("${kafka.topics.egf.masters.accountdetailkey.validated.key}")
	private String accountDetailKeyValidatedKey;

	public void push(AccountDetailKeyContractRequest accountDetailKeyContractRequest) {
		HashMap<String, Object> accountDetailKeyContractRequestMap = new HashMap<String, Object>();
		if ("create".equalsIgnoreCase(accountDetailKeyContractRequest.getRequestInfo().getAction()))
			accountDetailKeyContractRequestMap.put("AccountDetailKeyCreate", accountDetailKeyContractRequest);
		else if ("updateAll".equalsIgnoreCase(accountDetailKeyContractRequest.getRequestInfo().getAction()))
			accountDetailKeyContractRequestMap.put("AccountDetailKeyUpdateAll", accountDetailKeyContractRequest);
		else if ("update".equalsIgnoreCase(accountDetailKeyContractRequest.getRequestInfo().getAction()))
			accountDetailKeyContractRequestMap.put("AccountDetailKeyUpdate", accountDetailKeyContractRequest);
		
		financialProducer.sendMessage(accountDetailKeyValidatedTopic, accountDetailKeyValidatedKey,
				accountDetailKeyContractRequestMap);
	}
}
