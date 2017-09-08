package org.egov.egf.bill.persistence.queue.repository;

import java.util.Map;

import org.egov.egf.bill.persistence.queue.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillRegisterQueueRepository {

	private FinancialProducer financialBillRegisterProducer;

	private String validatedTopic;

	private String billValidatedKey;

	private String completedTopic;

	private String billCompletedKey;

	@Autowired
	public BillRegisterQueueRepository(
			final FinancialProducer financialBillRegisterProducer,
			@Value("${kafka.topics.egf.bill.validated.topic}") String validatedTopic,
			@Value("${kafka.topics.egf.bill.bill.register.validated.key}") String billValidatedKey,
			@Value("${kafka.topics.egf.bill.completed.topic}") String completedTopic,
			@Value("${kafka.topics.egf.bill.bill.register.completed.key}") String billCompletedKey) {

		this.financialBillRegisterProducer = financialBillRegisterProducer;
		this.validatedTopic = validatedTopic;
		this.billValidatedKey = billValidatedKey;
		this.completedTopic = completedTopic;
		this.billCompletedKey = billCompletedKey;
	}

	public void addToQue(final Map<String, Object> topicMap) {

		financialBillRegisterProducer.sendMessage(validatedTopic,
				billValidatedKey, topicMap);
	}

	public void addToSearchQue(final Map<String, Object> topicMap) {

		financialBillRegisterProducer.sendMessage(completedTopic,
				billCompletedKey, topicMap);

	}
}
