package org.egov.egf.budget.persistence.queue.repository;

import java.util.HashMap;

import org.egov.egf.budget.persistence.queue.FinancialProducer;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BudgetQueueRepository {

    private final FinancialProducer financialProducer;

    private final String validatedTopic;

    private final String budgetValidatedKey;

    private final String completedTopic;

    private final String budgetCompletedKey;

    @Autowired
    public BudgetQueueRepository(final FinancialProducer financialProducer,
            @Value("${kafka.topics.egf.budget.service.validated.topic}") final String validatedTopic,
            @Value("${kafka.topics.egf.budget.budget.validated.key}") final String budgetValidatedKey,
            @Value("${kafka.topics.egf.budget.service.completed.topic}") final String completedTopic,
            @Value("${kafka.topics.egf.budget.budget.completed.key}") final String budgetCompletedKey) {

        this.financialProducer = financialProducer;
        this.validatedTopic = validatedTopic;
        this.budgetValidatedKey = budgetValidatedKey;
        this.completedTopic = completedTopic;
        this.budgetCompletedKey = budgetCompletedKey;
    }

    public void addToQue(final BudgetRequest request) {
        final HashMap<String, Object> topicMap = new HashMap<String, Object>();

        switch (request.getRequestInfo().getAction().toLowerCase()) {

        case "create":
            topicMap.put("budget_create", request);
            System.out.println("push create topic" + request);
            break;
        case "update":
            topicMap.put("budget_update", request);
            break;

        }
        financialProducer.sendMessage(validatedTopic, budgetValidatedKey, topicMap);
    }

    public void addToSearchQue(final BudgetRequest request) {

        final HashMap<String, Object> topicMap = new HashMap<String, Object>();

        if (!request.getBudgets().isEmpty()) {

            topicMap.put("budget_persisted", request);

            System.out.println("push search topic" + request);

        }

        financialProducer.sendMessage(completedTopic, budgetCompletedKey, topicMap);

    }
}
