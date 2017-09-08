package org.egov.egf.bill.persistence.queue.repository;

import java.util.Map;

import org.egov.egf.bill.persistence.queue.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillDetailQueueRepository {
	
	private FinancialProducer financialBillDetailProducer;

	private String validatedTopic;

	private String billDetailValidatedKey;

	private String completedTopic;

	private String billDetailCompletedKey;

	@Autowired
	public BillDetailQueueRepository(
			final FinancialProducer financialBillDetailProducer,
			@Value("${kafka.topics.egf.bill.validated.topic}") String validatedTopic,
			@Value("${kafka.topics.egf.bill.bill.detail.validated.key}") String billDetailValidatedKey,
			@Value("${kafka.topics.egf.bill.completed.topic}") String completedTopic,
			@Value("${kafka.topics.egf.bill.bill.detail.completed.key}") String billDetailCompletedKey) {

		this.financialBillDetailProducer = financialBillDetailProducer;
		this.validatedTopic = validatedTopic;
		this.billDetailValidatedKey = billDetailValidatedKey;
		this.completedTopic = completedTopic;
		this.billDetailCompletedKey = billDetailCompletedKey;
	}

	public void addToQue(final Map<String, Object> topicMap) {

		financialBillDetailProducer.sendMessage(validatedTopic,
				billDetailValidatedKey, topicMap);
	}

	public void addToSearchQue(final Map<String, Object> topicMap) {

		financialBillDetailProducer.sendMessage(completedTopic,
				billDetailCompletedKey, topicMap);

	}
}
