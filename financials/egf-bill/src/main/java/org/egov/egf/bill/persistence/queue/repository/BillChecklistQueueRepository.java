package org.egov.egf.bill.persistence.queue.repository;

import java.util.Map;

import org.egov.egf.bill.persistence.queue.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillChecklistQueueRepository {

	private FinancialProducer financialBillChecklistProducer;

	private String validatedTopic;

	private String billChecklistValidatedKey;

	private String completedTopic;

	private String billChecklistCompletedKey;

	@Autowired
	public BillChecklistQueueRepository(
			final FinancialProducer financialBillChecklistProducer,
			@Value("${kafka.topics.egf.bill.validated.topic}") String validatedTopic,
			@Value("${kafka.topics.egf.bill.billchecklist.validated.key}") String billChecklistValidatedKey,
			@Value("${kafka.topics.egf.bill.completed.topic}") String completedTopic,
			@Value("${kafka.topics.egf.bill.billchecklist.completed.key}") String billChecklistCompletedKey) {

		this.financialBillChecklistProducer = financialBillChecklistProducer;
		this.validatedTopic = validatedTopic;
		this.billChecklistValidatedKey = billChecklistValidatedKey;
		this.completedTopic = completedTopic;
		this.billChecklistCompletedKey = billChecklistCompletedKey;
	}

	public void addToQue(final Map<String, Object> topicMap) {

		financialBillChecklistProducer.sendMessage(validatedTopic,
				billChecklistValidatedKey, topicMap);
	}

	public void addToSearchQue(final Map<String, Object> topicMap) {

		financialBillChecklistProducer.sendMessage(completedTopic,
				billChecklistCompletedKey, topicMap);

	}

}
