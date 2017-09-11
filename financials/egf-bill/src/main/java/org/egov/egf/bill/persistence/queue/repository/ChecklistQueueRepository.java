package org.egov.egf.bill.persistence.queue.repository;

import java.util.Map;

import org.egov.egf.bill.persistence.queue.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChecklistQueueRepository {

	private FinancialProducer financialChecklistProducer;

	private String validatedTopic;

	private String checklistValidatedKey;

	private String completedTopic;

	private String checklistCompletedKey;

	@Autowired
	public ChecklistQueueRepository(
			final FinancialProducer financialChecklistProducer,
			@Value("${kafka.topics.egf.bill.validated.topic}") String validatedTopic,
			@Value("${kafka.topics.egf.bill.bill.checklist.validated.key}") String checklistValidatedKey,
			@Value("${kafka.topics.egf.bill.completed.topic}") String completedTopic,
			@Value("${kafka.topics.egf.bill.bill.checklist.completed.key}") String checklistCompletedKey) {

		this.financialChecklistProducer = financialChecklistProducer;
		this.validatedTopic = validatedTopic;
		this.checklistValidatedKey = checklistValidatedKey;
		this.completedTopic = completedTopic;
		this.checklistCompletedKey = checklistCompletedKey;
	}

	public void addToQue(final Map<String, Object> topicMap) {

		financialChecklistProducer.sendMessage(validatedTopic,
				checklistValidatedKey, topicMap);
	}

	public void addToSearchQue(final Map<String, Object> topicMap) {

		financialChecklistProducer.sendMessage(completedTopic,
				checklistCompletedKey, topicMap);

	}

}
