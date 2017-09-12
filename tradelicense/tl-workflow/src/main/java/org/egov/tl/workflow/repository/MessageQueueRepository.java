package org.egov.tl.workflow.repository;

import java.util.HashMap;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueRepository {

	private String topicName;

	private String keyName;

	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public MessageQueueRepository(@Value("${egov.services.tl-services.tradelicense.workflow.populated.topic}") String topicName,
			@Value("${egov.services.tl-services.tradelicense.workflow.populated.key}") String keyName,
			LogAwareKafkaTemplate<String, Object> kafkaTemplate) {

		this.topicName = topicName;

		this.keyName = keyName;

		this.kafkaTemplate = kafkaTemplate;
	}

	public void save(HashMap<String, Object> requestMap) {

		kafkaTemplate.send(topicName, keyName, requestMap);

	}
}
