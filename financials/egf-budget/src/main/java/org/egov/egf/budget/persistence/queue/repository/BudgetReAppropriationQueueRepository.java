package org.egov.egf.budget.persistence.queue.repository;

import java.util.HashMap;

import org.egov.egf.budget.persistence.queue.FinancialProducer;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BudgetReAppropriationQueueRepository {

	private FinancialProducer financialProducer;

	private String validatedTopic;

	private String budgetReAppValidatedKey;

	private String completedTopic;

	private String budgetReAppCompletedKey;

	@Autowired
	public BudgetReAppropriationQueueRepository(FinancialProducer financialProducer,
			@Value("${kafka.topics.egf.budget.service.validated.topic}") String validatedTopic,
			@Value("${kafka.topics.egf.budget.budgetreapp.validated.key}") String budgetReAppValidatedKey,
			@Value("${kafka.topics.egf.budget.service.completed.topic}") String completedTopic,
			@Value("${kafka.topics.egf.budget.budgetreapp.completed.key}") String budgetReAppCompletedKey) {

		this.financialProducer = financialProducer;
		this.validatedTopic = validatedTopic;
		this.budgetReAppValidatedKey = budgetReAppValidatedKey;
		this.completedTopic = completedTopic;
		this.budgetReAppCompletedKey = budgetReAppCompletedKey;
	}

	public void addToQue(BudgetReAppropriationRequest request) {
		HashMap<String, Object> topicMap = new HashMap<String, Object>();

		switch (request.getRequestInfo().getAction().toLowerCase()) {

		case "create":
			topicMap.put("budgetreappropriation_create", request);
			System.out.println("push create topic" + request);
			break;
		case "update":
			topicMap.put("budgetreappropriation_update", request);
			break;

		}
		financialProducer.sendMessage(validatedTopic, budgetReAppValidatedKey, topicMap);
	}

	public void addToSearchQue(BudgetReAppropriationRequest request) {

		HashMap<String, Object> topicMap = new HashMap<String, Object>();

		if (!request.getBudgetReAppropriations().isEmpty()) {

			topicMap.put("budgetreappropriation_persisted", request);

			System.out.println("push search topic" + request);

		}

		financialProducer.sendMessage(completedTopic, budgetReAppCompletedKey, topicMap);

	}
}
