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

	@Value("${kafka.topics.egf.budget.service.completed.key}")
	private String completedKey;

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
	public void process(HashMap<String, Object> mastersMap) {

		objectMapperFactory = new ObjectMapperFactory(objectMapper);

		BudgetMapper budgetMapper = new BudgetMapper();
		BudgetDetailMapper budgetDetailMapper = new BudgetDetailMapper();
		BudgetReAppropriationMapper budgetReAppropriationMapper = new BudgetReAppropriationMapper();

		if (mastersMap.get("budget_create") != null) {

			BudgetRequest request = objectMapperFactory.create().convertValue(mastersMap.get("budget_create"),
					BudgetRequest.class);

			for (BudgetContract budgetContract : request.getBudgets()) {
				Budget domain = budgetMapper.toDomain(budgetContract);
				budgetService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("budget_completed", request);
			financialProducer.sendMessage(completedTopic, completedKey, mastersMap);
		}

		if (mastersMap.get("budget_update") != null) {

			BudgetRequest request = objectMapperFactory.create().convertValue(mastersMap.get("budget_update"),
					BudgetRequest.class);

			for (BudgetContract budgetContract : request.getBudgets()) {
				Budget domain = budgetMapper.toDomain(budgetContract);
				budgetService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("budget_completed", request);
			financialProducer.sendMessage(completedTopic, completedKey, mastersMap);
		}

		if (mastersMap.get("budgetdetail_create") != null) {

			BudgetDetailRequest request = objectMapperFactory.create()
					.convertValue(mastersMap.get("budgetdetail_create"), BudgetDetailRequest.class);

			for (BudgetDetailContract budgetDetailContract : request.getBudgetDetails()) {
				BudgetDetail domain = budgetDetailMapper.toDomain(budgetDetailContract);
				budgetDetailService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetdetail_completed", request);
			financialProducer.sendMessage(completedTopic, completedKey, mastersMap);
		}

		if (mastersMap.get("budgetdetail_update") != null)

		{

			BudgetDetailRequest request = objectMapperFactory.create()
					.convertValue(mastersMap.get("budgetdetail_update"), BudgetDetailRequest.class);

			for (BudgetDetailContract budgetDetailContract : request.getBudgetDetails()) {
				BudgetDetail domain = budgetDetailMapper.toDomain(budgetDetailContract);
				budgetDetailService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetdetail_completed", request);
			financialProducer.sendMessage(completedTopic, completedKey, mastersMap);
		}

		if (mastersMap.get("budgetreappropriation_create") != null) {

			BudgetReAppropriationRequest request = objectMapperFactory.create().convertValue(
					mastersMap.get("budgetreappropriation_create"), BudgetReAppropriationRequest.class);

			for (BudgetReAppropriationContract budgetReAppropriationContract : request.getBudgetReAppropriations()) {
				BudgetReAppropriation domain = budgetReAppropriationMapper.toDomain(budgetReAppropriationContract);
				budgetReAppropriationService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetreappropriation_completed", request);
			financialProducer.sendMessage(completedTopic, completedKey, mastersMap);
		}
		if (mastersMap.get("budgetreappropriation_update") != null)

		{
			BudgetReAppropriationRequest request = objectMapperFactory.create().convertValue(
					mastersMap.get("budgetreappropriation_update"), BudgetReAppropriationRequest.class);

			for (BudgetReAppropriationContract budgetReAppropriationContract : request.getBudgetReAppropriations()) {
				BudgetReAppropriation domain = budgetReAppropriationMapper.toDomain(budgetReAppropriationContract);
				budgetReAppropriationService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetreappropriation_completed", request);
			financialProducer.sendMessage(completedTopic, completedKey, mastersMap);
		}

	}

}
