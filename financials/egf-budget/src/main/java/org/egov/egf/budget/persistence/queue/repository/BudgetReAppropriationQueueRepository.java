package org.egov.egf.budget.persistence.queue.repository;

import java.util.HashMap;

import org.egov.egf.budget.persistence.queue.FinancialProducer;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BudgetReAppropriationQueueRepository {

    private final FinancialProducer financialProducer;

    private final String validatedTopic;

    private final String budgetReAppValidatedKey;

    private final String completedTopic;

    private final String budgetReAppCompletedKey;

    @Autowired
    public BudgetReAppropriationQueueRepository(final FinancialProducer financialProducer,
            @Value("${kafka.topics.egf.budget.service.validated.topic}") final String validatedTopic,
            @Value("${kafka.topics.egf.budget.budgetreapp.validated.key}") final String budgetReAppValidatedKey,
            @Value("${kafka.topics.egf.budget.service.completed.topic}") final String completedTopic,
            @Value("${kafka.topics.egf.budget.budgetreapp.completed.key}") final String budgetReAppCompletedKey) {

        this.financialProducer = financialProducer;
        this.validatedTopic = validatedTopic;
        this.budgetReAppValidatedKey = budgetReAppValidatedKey;
        this.completedTopic = completedTopic;
        this.budgetReAppCompletedKey = budgetReAppCompletedKey;
    }

    public void addToQue(final BudgetReAppropriationRequest request) {
        final HashMap<String, Object> topicMap = new HashMap<String, Object>();

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

    public void addToSearchQue(final BudgetReAppropriationRequest request) {

        final HashMap<String, Object> topicMap = new HashMap<String, Object>();

        if (!request.getBudgetReAppropriations().isEmpty()) {

            topicMap.put("budgetreappropriation_persisted", request);

            System.out.println("push search topic" + request);

        }

        financialProducer.sendMessage(completedTopic, budgetReAppCompletedKey, topicMap);

    }
}
