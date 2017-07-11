package org.egov.egf.budget.persistence.queue;

import java.util.HashMap;

import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.egov.egf.budget.domain.repository.BudgetReAppropriationRepository;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
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
	private BudgetRepository budgetRepository;

	@Autowired
	private BudgetDetailRepository budgetDetailRepository;

	@Autowired
	private BudgetReAppropriationRepository budgetReAppropriationRepository;

	@KafkaListener(id = "${kafka.topics.egf.budget.service.validated.id}", topics = "${kafka.topics.egf.budget.service.validated.topic}", group = "${kafka.topics.egf.budget.service.validated.group}")
	public void process(HashMap<String, CommonRequest<?>> mastersMap) {

		if (mastersMap.get("budgetcontract_create") != null) {

			CommonRequest<BudgetContract> request = objectMapper.convertValue(mastersMap.get("budgetcontract_create"),
					new TypeReference<CommonRequest<BudgetContract>>() {
					});

			for (BudgetContract budgetContract : request.getData()) {
				Budget domain = budgetContract.toDomain();
				budgetRepository.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("budgetcontract_update") != null)

		{

			CommonRequest<BudgetContract> request = objectMapper.convertValue(mastersMap.get("budgetcontract_update"),
					new TypeReference<CommonRequest<BudgetContract>>() {
					});

			for (BudgetContract budgetContract : request.getData()) {
				Budget domain = budgetContract.toDomain();
				budgetRepository.update(domain);
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
				BudgetDetail domain = budgetDetailContract.toDomain();
				budgetDetailRepository.save(domain);
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
				BudgetDetail domain = budgetDetailContract.toDomain();
				budgetDetailRepository.update(domain);
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
				BudgetReAppropriation domain = budgetReAppropriationContract.toDomain();
				budgetReAppropriationRepository.save(domain);
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
				BudgetReAppropriation domain = budgetReAppropriationContract.toDomain();
				budgetReAppropriationRepository.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetreappropriationcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

	}

}
