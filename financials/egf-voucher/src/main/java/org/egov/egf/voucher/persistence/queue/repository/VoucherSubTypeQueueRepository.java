package org.egov.egf.voucher.persistence.queue.repository;

import java.util.Map;

import org.egov.egf.voucher.persistence.queue.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VoucherSubTypeQueueRepository {

	private final FinancialProducer financialProducer;

	private final String validatedTopic;

	private final String voucherSubTypeValidatedKey;

	private final String completedTopic;

	private final String voucherSubTypeCompletedKey;

	@Autowired
	public VoucherSubTypeQueueRepository(
			final FinancialProducer financialProducer,
			@Value("${kafka.topics.egf.voucher.validated.topic}") final String validatedTopic,
			@Value("${kafka.topics.egf.voucher.vouchersubtype.validated.key}") final String voucherSubTypeValidatedKey,
			@Value("${kafka.topics.egf.voucher.completed.topic}") final String completedTopic,
			@Value("${kafka.topics.egf.voucher.vouchersubtype.completed.key}") final String voucherSubTypeCompletedKey) {

		this.financialProducer = financialProducer;
		this.validatedTopic = validatedTopic;
		this.voucherSubTypeValidatedKey = voucherSubTypeValidatedKey;
		this.completedTopic = completedTopic;
		this.voucherSubTypeCompletedKey = voucherSubTypeCompletedKey;
	}

	public void addToQue(final Map<String, Object> topicMap) {

		financialProducer.sendMessage(validatedTopic,
				voucherSubTypeValidatedKey, topicMap);
	}

	public void addToSearchQue(final Map<String, Object> topicMap) {

		financialProducer.sendMessage(completedTopic,
				voucherSubTypeCompletedKey, topicMap);

	}
}
