package org.egov.egf.budget.persistence.queue;

import java.util.HashMap;

import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.service.BudgetDetailService;
import org.egov.egf.budget.domain.service.BudgetReAppropriationService;
import org.egov.egf.budget.domain.service.BudgetService;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.egov.egf.budget.web.mapper.BudgetDetailMapper;
import org.egov.egf.budget.web.mapper.BudgetMapper;
import org.egov.egf.budget.web.mapper.BudgetReAppropriationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialBudgetServiceListener {

    @Value("${kafka.topics.egf.budget.service.completed.topic}")
    private String completedTopic;

    @Value("${kafka.topics.egf.budget.budget.completed.key}")
    private String budgetCompletedKey;

    @Value("${kafka.topics.egf.budget.budgetdetail.completed.key}")
    private String budgetDetailCompletedKey;

    @Value("${kafka.topics.egf.budget.budgetreapp.completed.key}")
    private String budgetReAppropriationCompletedKey;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectMapperFactory objectMapperFactory;

    @Autowired
    private FinancialProducer financialProducer;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetDetailService budgetDetailService;

    @Autowired
    private BudgetReAppropriationService budgetReAppropriationService;

    @KafkaListener(id = "${kafka.topics.egf.budget.service.validated.id}", topics = "${kafka.topics.egf.budget.service.validated.topic}", group = "${kafka.topics.egf.budget.service.validated.group}")
    public void process(final HashMap<String, Object> mastersMap) {

        objectMapperFactory = new ObjectMapperFactory(objectMapper);

        final BudgetMapper budgetMapper = new BudgetMapper();
        final BudgetDetailMapper budgetDetailMapper = new BudgetDetailMapper();
        final BudgetReAppropriationMapper budgetReAppropriationMapper = new BudgetReAppropriationMapper();

        if (mastersMap.get("budget_create") != null) {

            final BudgetRequest request = objectMapperFactory.create().convertValue(mastersMap.get("budget_create"),
                    BudgetRequest.class);

            for (final BudgetContract budgetContract : request.getBudgets()) {
                final Budget domain = budgetMapper.toDomain(budgetContract);
                budgetService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("budget_persisted", request);
            financialProducer.sendMessage(completedTopic, budgetCompletedKey, mastersMap);
        }

        if (mastersMap.get("budget_update") != null) {

            final BudgetRequest request = objectMapperFactory.create().convertValue(mastersMap.get("budget_update"),
                    BudgetRequest.class);

            for (final BudgetContract budgetContract : request.getBudgets()) {
                final Budget domain = budgetMapper.toDomain(budgetContract);
                budgetService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("budget_persisted", request);
            financialProducer.sendMessage(completedTopic, budgetCompletedKey, mastersMap);
        }

        if (mastersMap.get("budgetdetail_create") != null) {

            final BudgetDetailRequest request = objectMapperFactory.create()
                    .convertValue(mastersMap.get("budgetdetail_create"), BudgetDetailRequest.class);

            for (final BudgetDetailContract budgetDetailContract : request.getBudgetDetails()) {
                final BudgetDetail domain = budgetDetailMapper.toDomain(budgetDetailContract);
                budgetDetailService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("budgetdetail_persisted", request);
            financialProducer.sendMessage(completedTopic, budgetDetailCompletedKey, mastersMap);
        }

        if (mastersMap.get("budgetdetail_update") != null)

        {

            final BudgetDetailRequest request = objectMapperFactory.create()
                    .convertValue(mastersMap.get("budgetdetail_update"), BudgetDetailRequest.class);

            for (final BudgetDetailContract budgetDetailContract : request.getBudgetDetails()) {
                final BudgetDetail domain = budgetDetailMapper.toDomain(budgetDetailContract);
                budgetDetailService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("budgetdetail_persisted", request);
            financialProducer.sendMessage(completedTopic, budgetDetailCompletedKey, mastersMap);
        }

        if (mastersMap.get("budgetreappropriation_create") != null) {

            final BudgetReAppropriationRequest request = objectMapperFactory.create().convertValue(
                    mastersMap.get("budgetreappropriation_create"), BudgetReAppropriationRequest.class);

            for (final BudgetReAppropriationContract budgetReAppropriationContract : request.getBudgetReAppropriations()) {
                final BudgetReAppropriation domain = budgetReAppropriationMapper.toDomain(budgetReAppropriationContract);
                budgetReAppropriationService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("budgetreappropriation_persisted", request);
            financialProducer.sendMessage(completedTopic, budgetReAppropriationCompletedKey, mastersMap);
        }
        if (mastersMap.get("budgetreappropriation_update") != null)

        {
            final BudgetReAppropriationRequest request = objectMapperFactory.create().convertValue(
                    mastersMap.get("budgetreappropriation_update"), BudgetReAppropriationRequest.class);

            for (final BudgetReAppropriationContract budgetReAppropriationContract : request.getBudgetReAppropriations()) {
                final BudgetReAppropriation domain = budgetReAppropriationMapper.toDomain(budgetReAppropriationContract);
                budgetReAppropriationService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("budgetreappropriation_persisted", request);
            financialProducer.sendMessage(completedTopic, budgetReAppropriationCompletedKey, mastersMap);
        }

    }

}
