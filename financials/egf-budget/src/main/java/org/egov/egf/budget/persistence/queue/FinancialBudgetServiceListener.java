package org.egov.egf.budget.persistence.queue;

import java.util.HashMap;

import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.service.BudgetDetailService;
import org.egov.egf.budget.domain.service.BudgetReAppropriationService;
import org.egov.egf.budget.domain.service.BudgetService;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.mapper.BudgetDetailMapper;
import org.egov.egf.budget.web.mapper.BudgetMapper;
import org.egov.egf.budget.web.mapper.BudgetReAppropriationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialBudgetServiceListener {

	@Value("${kafka.topics.egf.budget.service.completed.topic}")
	private String completedTopic;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private FinancialProducer financialProducer;

	@Autowired
	private BudgetService budgetService;

	@Autowired
	private BudgetDetailService budgetDetailService;

	@Autowired
	private BudgetReAppropriationService budgetReAppropriationService;

	@KafkaListener(id = "${kafka.topics.egf.budget.service.validated.id}", topics = "${kafka.topics.egf.budget.service.validated.topic}", group = "${kafka.topics.egf.budget.service.validated.group}")
	public void process(HashMap<String, CommonRequest<?>> mastersMap) {

		BudgetMapper budgetMapper = new BudgetMapper();
		BudgetDetailMapper budgetDetailMapper = new BudgetDetailMapper();
		BudgetReAppropriationMapper budgetReAppropriationMapper = new BudgetReAppropriationMapper();

		if (mastersMap.get("budgetcontract_create") != null) {

			CommonRequest<BudgetContract> request = objectMapper.convertValue(mastersMap.get("budgetcontract_create"),
					new TypeReference<CommonRequest<BudgetContract>>() {
					});

			for (BudgetContract budgetContract : request.getData()) {
				Budget domain = budgetMapper.toDomain(budgetContract);
				budgetService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("budgetcontract_update") != null) {

			CommonRequest<BudgetContract> request = objectMapper.convertValue(mastersMap.get("budgetcontract_update"),
					new TypeReference<CommonRequest<BudgetContract>>() {
					});

			for (BudgetContract budgetContract : request.getData()) {
				Budget domain = budgetMapper.toDomain(budgetContract);
				budgetService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("budgetdetailcontract_create") != null) {

			CommonRequest<BudgetDetailContract> request = objectMapper.convertValue(
					mastersMap.get("budgetdetailcontract_create"),
					new TypeReference<CommonRequest<BudgetDetailContract>>() {
					});

			for (BudgetDetailContract budgetDetailContract : request.getData()) {
				BudgetDetail domain = budgetDetailMapper.toDomain(budgetDetailContract);
				budgetDetailService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetdetailcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("budgetdetailcontract_update") != null)

		{

			CommonRequest<BudgetDetailContract> request = objectMapper.convertValue(
					mastersMap.get("budgetdetailcontract_update"),
					new TypeReference<CommonRequest<BudgetDetailContract>>() {
					});

			for (BudgetDetailContract budgetDetailContract : request.getData()) {
				BudgetDetail domain = budgetDetailMapper.toDomain(budgetDetailContract);
				budgetDetailService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetdetailcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("budgetreappropriationcontract_create") != null) {

			CommonRequest<BudgetReAppropriationContract> request = objectMapper.convertValue(
					mastersMap.get("budgetreappropriationcontract_create"),
					new TypeReference<CommonRequest<BudgetReAppropriationContract>>() {
					});

			for (BudgetReAppropriationContract budgetReAppropriationContract : request.getData()) {
				BudgetReAppropriation domain = budgetReAppropriationMapper.toDomain(budgetReAppropriationContract);
				budgetReAppropriationService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetreappropriationcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("budgetreappropriationcontract_update") != null)

		{

			CommonRequest<BudgetReAppropriationContract> request = objectMapper.convertValue(
					mastersMap.get("budgetreappropriationcontract_update"),
					new TypeReference<CommonRequest<BudgetReAppropriationContract>>() {
					});

			for (BudgetReAppropriationContract budgetReAppropriationContract : request.getData()) {
				BudgetReAppropriation domain = budgetReAppropriationMapper.toDomain(budgetReAppropriationContract);
				budgetReAppropriationService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetreappropriationcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

	}

}
