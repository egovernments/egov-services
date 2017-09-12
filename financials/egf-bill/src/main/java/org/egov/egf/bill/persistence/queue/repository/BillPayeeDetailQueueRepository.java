package org.egov.egf.bill.persistence.queue.repository;

import java.util.Map;

import org.egov.egf.bill.persistence.queue.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillPayeeDetailQueueRepository {

	private FinancialProducer financialBillPayeeDetailProducer;

	private String validatedTopic;

	private String billPayeeDetailValidatedKey;

	private String completedTopic;

	private String billPayeeDetailCompletedKey;

	@Autowired
	public BillPayeeDetailQueueRepository(
			final FinancialProducer financialBillPayeeDetailProducer,
			@Value("${kafka.topics.egf.bill.validated.topic}") String validatedTopic,
			@Value("${kafka.topics.egf.bill.bill.payeedetail.validated.key}") String billPayeeDetailValidatedKey,
			@Value("${kafka.topics.egf.bill.completed.topic}") String completedTopic,
			@Value("${kafka.topics.egf.bill.bill.payeedetail.completed.key}") String billPayeeDetailCompletedKey) {

		this.financialBillPayeeDetailProducer = financialBillPayeeDetailProducer;
		this.validatedTopic = validatedTopic;
		this.billPayeeDetailValidatedKey = billPayeeDetailValidatedKey;
		this.completedTopic = completedTopic;
		this.billPayeeDetailCompletedKey = billPayeeDetailCompletedKey;
	}

	public void addToQue(final Map<String, Object> topicMap) {

		financialBillPayeeDetailProducer.sendMessage(validatedTopic,
				billPayeeDetailValidatedKey, topicMap);
	}

	public void addToSearchQue(final Map<String, Object> topicMap) {

		financialBillPayeeDetailProducer.sendMessage(completedTopic,
				billPayeeDetailCompletedKey, topicMap);

	}

}
