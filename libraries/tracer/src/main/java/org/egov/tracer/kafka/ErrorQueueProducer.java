package org.egov.tracer.kafka;

import org.egov.tracer.model.ErrorQueueContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ErrorQueueProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@Value(("${error.queue.topic:egov.error}"))
	private String topic;
	
	public void sendMessage(ErrorQueueContract errorQueueContract) {
		kafkaTemplate.send(topic, errorQueueContract);
	}
	
}
