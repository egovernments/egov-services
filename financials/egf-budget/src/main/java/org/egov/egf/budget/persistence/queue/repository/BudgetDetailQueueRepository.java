package org.egov.egf.budget.persistence.queue.repository;

import java.util.HashMap;

import org.egov.egf.budget.persistence.queue.FinancialProducer;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BudgetDetailQueueRepository {

    private final FinancialProducer financialProducer;

    private final String validatedTopic;

    private final String budgetDetailValidatedKey;

    private final String completedTopic;

    private final String budgetDetailCompletedKey;

    @Autowired
    public BudgetDetailQueueRepository(final FinancialProducer financialProducer,
            @Value("${kafka.topics.egf.budget.service.validated.topic}") final String validatedTopic,
            @Value("${kafka.topics.egf.budget.budgetdetail.validated.key}") final String budgetDetailValidatedKey,
            @Value("${kafka.topics.egf.budget.service.completed.topic}") final String completedTopic,
            @Value("${kafka.topics.egf.budget.budgetdetail.completed.key}") final String budgetDetailCompletedKey) {

        this.financialProducer = financialProducer;
        this.validatedTopic = validatedTopic;
        this.budgetDetailValidatedKey = budgetDetailValidatedKey;
        this.completedTopic = completedTopic;
        this.budgetDetailCompletedKey = budgetDetailCompletedKey;
    }

    public void addToQue(final BudgetDetailRequest request) {
        final HashMap<String, Object> topicMap = new HashMap<String, Object>();

        switch (request.getRequestInfo().getAction().toLowerCase()) {

        case "create":
            topicMap.put("budgetdetail_create", request);
            System.out.println("push create topic" + request);
            break;
        case "update":
            topicMap.put("budgetdetail_update", request);
            break;

        }
        financialProducer.sendMessage(validatedTopic, budgetDetailValidatedKey, topicMap);
    }

    public void addToSearchQue(final BudgetDetailRequest request) {

        final HashMap<String, Object> topicMap = new HashMap<String, Object>();

        if (!request.getBudgetDetails().isEmpty()) {

            topicMap.put("budgetdetail_persisted", request);

            System.out.println("push search topic" + request);

        }

        financialProducer.sendMessage(completedTopic, budgetDetailCompletedKey, topicMap);

    }
}
