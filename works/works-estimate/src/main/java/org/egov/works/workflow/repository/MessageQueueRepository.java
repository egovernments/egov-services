package org.egov.works.workflow.repository;

import java.util.HashMap;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueRepository {

	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public MessageQueueRepository(
			LogAwareKafkaTemplate<String, Object> kafkaTemplate) {

		this.kafkaTemplate = kafkaTemplate;
	}

	public void save(String topicName, HashMap<String, Object> requestMap) {

		kafkaTemplate.send(topicName, requestMap);

	}
}
