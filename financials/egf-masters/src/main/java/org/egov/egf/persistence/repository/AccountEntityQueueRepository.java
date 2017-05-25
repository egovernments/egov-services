package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.AccountEntityContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccountEntityQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String accountEntityValidatedTopic;

	@Value("${kafka.topics.egf.masters.accountentity.validated.key}")
	private String accountEntityValidatedKey;

	public void push(AccountEntityContractRequest accountEntityContractRequest) {
		HashMap<String, Object> accountEntityContractRequestMap = new HashMap<String, Object>();

		if ("create".equalsIgnoreCase(accountEntityContractRequest.getRequestInfo().getAction()))
			accountEntityContractRequestMap.put("AccountEntityCreate", accountEntityContractRequest);
		else if ("updateAll".equalsIgnoreCase(accountEntityContractRequest.getRequestInfo().getAction()))
			accountEntityContractRequestMap.put("AccountEntityUpdateAll", accountEntityContractRequest);
		else if ("update".equalsIgnoreCase(accountEntityContractRequest.getRequestInfo().getAction()))
			accountEntityContractRequestMap.put("AccountEntityUpdate", accountEntityContractRequest);

		financialProducer.sendMessage(accountEntityValidatedTopic, accountEntityValidatedKey,
				accountEntityContractRequestMap);
	}
}
