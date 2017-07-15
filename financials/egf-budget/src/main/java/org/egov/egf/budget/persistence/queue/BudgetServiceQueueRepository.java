package org.egov.egf.budget.persistence.queue;

import java.util.HashMap;

import org.egov.common.web.contract.CommonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceQueueRepository {

	private FinancialProducer financialProducer;

	private String validatedTopic;

	private String validatedKey;

	@Autowired
	public BudgetServiceQueueRepository(FinancialProducer financialProducer,
			@Value("${kafka.topics.egf.budget.service.validated.topic}") String validatedTopic,
			@Value("${kafka.topics.egf.budget.service.validated.key}") String validatedKey) {
		this.financialProducer = financialProducer;
		this.validatedTopic = validatedTopic;
		this.validatedKey = validatedKey;
	}

	public void addToQue(CommonRequest<?> request) {
		String masterName = "";
		HashMap<String, CommonRequest<?>> topicMap = new HashMap<String, CommonRequest<?>>();
		if (!request.getData().isEmpty())
			masterName = request.getData().get(0).getClass().getSimpleName();

		System.out.println("got insert for " + masterName);

		switch (request.getRequestInfo().getAction().toLowerCase()) {

		case "create":
			topicMap.put(masterName.toLowerCase() + "_create", request);
			System.out.println("push create topic" + request);
			break;
		case "update":
			topicMap.put(masterName.toLowerCase() + "_update", request);
			break;

		}
		financialProducer.sendMessage(validatedTopic, validatedKey, topicMap);
	}
}
