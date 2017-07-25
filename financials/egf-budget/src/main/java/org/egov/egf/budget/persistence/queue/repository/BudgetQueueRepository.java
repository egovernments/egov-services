package org.egov.egf.budget.persistence.queue.repository;

import java.util.HashMap;

import org.egov.egf.budget.persistence.queue.FinancialProducer;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BudgetQueueRepository {

	private FinancialProducer financialProducer;

	private String validatedTopic;

	private String validatedKey;

	private String completedTopic;

	private String completedKey;

	@Autowired
	public BudgetQueueRepository(FinancialProducer financialProducer,
			@Value("${kafka.topics.egf.budget.service.validated.topic}") String validatedTopic,
			@Value("${kafka.topics.egf.budget.service.validated.key}") String validatedKey,
			@Value("${kafka.topics.egf.budget.service.completed.topic}") String completedTopic,
			@Value("${kafka.topics.egf.budget.service.completed.key}") String completedKey) {

		this.financialProducer = financialProducer;
		this.validatedTopic = validatedTopic;
		this.validatedKey = validatedKey;
		this.completedTopic = completedTopic;
		this.completedKey = completedKey;
	}

	public void addToQue(BudgetRequest request) {
		HashMap<String, Object> topicMap = new HashMap<String, Object>();

		switch (request.getRequestInfo().getAction().toLowerCase()) {

		case "create":
			topicMap.put("budget_create", request);
			System.out.println("push create topic" + request);
			break;
		case "update":
			topicMap.put("budget_update", request);
			break;

		}
		financialProducer.sendMessage(validatedTopic, validatedKey, topicMap);
	}

	public void addToSearchQue(BudgetRequest request) {

		HashMap<String, Object> topicMap = new HashMap<String, Object>();

		if (!request.getBudgets().isEmpty()) {

			topicMap.put("budget_completed", request);

			System.out.println("push search topic" + request);

		}

		financialProducer.sendMessage(completedTopic, completedKey, topicMap);

	}
}
