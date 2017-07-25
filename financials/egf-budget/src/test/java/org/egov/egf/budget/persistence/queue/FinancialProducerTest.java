package org.egov.egf.budget.persistence.queue;

import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.egov.egf.budget.web.contract.BudgetRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FinancialProducerTest {

	private static final String TOPIC_NAME = "topic";

	private static final String KEY_NAME = "key";

	@Mock
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@InjectMocks
	private FinancialProducer financialProducer;

	@Before
	public void setup() {
		financialProducer = new FinancialProducer(kafkaTemplate);
	}

	@Test
	public void test_send_message() {

		BudgetRequest request = new BudgetRequest();

		Map<String, Object> message = new HashMap<>();
		message.put("budgetcontract_create", request);

		financialProducer.sendMessage(TOPIC_NAME, KEY_NAME, message);

		verify(kafkaTemplate).send(TOPIC_NAME, KEY_NAME, message);

	}

}
