package org.egov.egf.budget.persistence.queue;

import java.util.HashMap;

import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialBudgetListener {

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

	@KafkaListener(id = "${kafka.topics.egf.budget.service.validated.id}", topics = "${kafka.topics.egf.budget.service.validated.topic}", group = "${kafka.topics.egf.budget.service.validated.group}")
	public void process(HashMap<String, CommonRequest<?>> mastersMap) {
		
		if (mastersMap.get("budgetcontract_create") != null) {

			CommonRequest<BudgetContract> request = objectMapper.convertValue(mastersMap.get("budgetcontract_create"),
					new TypeReference<CommonRequest<BudgetContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (BudgetContract budgetContract : request.getData()) {
				Budget domain = mapper.map(budgetContract, Budget.class);
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

			ModelMapper mapper = new ModelMapper();
			for (BudgetContract budgetContract : request.getData()) {
				Budget domain = mapper.map(budgetContract, Budget.class);
				budgetRepository.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

	}

}
