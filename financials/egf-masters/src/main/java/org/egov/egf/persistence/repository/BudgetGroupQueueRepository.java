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
		if (budgetGroupContractRequest.getBudgetGroups() != null
				&& !budgetGroupContractRequest.getBudgetGroups().isEmpty())
			budgetGroupContractRequestMap.put("BudgetGroupCreate", budgetGroupContractRequest);
		else if (budgetGroupContractRequest.getBudgetGroup() != null
				&& budgetGroupContractRequest.getBudgetGroup().getId() != null)
			budgetGroupContractRequestMap.put("BudgetGroupUpdate", budgetGroupContractRequest);
		financialProducer.sendMessage(budgetGroupValidatedTopic, budgetGroupValidatedKey, budgetGroupContractRequestMap);
	}
}
