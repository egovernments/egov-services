package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.BudgetGroupContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BudgetGroupQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String budgetGroupValidatedTopic;

	@Value("${kafka.topics.egf.masters.budgetgroup.validated.key}")
	private String budgetGroupValidatedKey;

	public void push(BudgetGroupContractRequest budgetGroupContractRequest) {
		HashMap<String, Object> budgetGroupContractRequestMap = new HashMap<String, Object>();

		if ("create".equalsIgnoreCase(budgetGroupContractRequest.getRequestInfo().getAction()))
			budgetGroupContractRequestMap.put("BudgetGroupCreate", budgetGroupContractRequest);
		else if ("updateAll".equalsIgnoreCase(budgetGroupContractRequest.getRequestInfo().getAction()))
			budgetGroupContractRequestMap.put("BudgetGroupUpdateAll", budgetGroupContractRequest);
		else if ("update".equalsIgnoreCase(budgetGroupContractRequest.getRequestInfo().getAction()))
			budgetGroupContractRequestMap.put("BudgetGroupUpdate", budgetGroupContractRequest);

		financialProducer.sendMessage(budgetGroupValidatedTopic, budgetGroupValidatedKey,
				budgetGroupContractRequestMap);
	}
}
