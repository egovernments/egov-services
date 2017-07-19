package org.egov.egf.budget.persistence.queue;

import java.util.Map;

import org.egov.common.web.contract.CommonRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialProducer {

	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	public FinancialProducer(LogAwareKafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(String topic, String key, Map<String, CommonRequest<?>> message) {
		kafkaTemplate.send(topic, key, message);
	}

}
