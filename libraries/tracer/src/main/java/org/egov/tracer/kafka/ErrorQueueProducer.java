package org.egov.tracer.kafka;

import org.egov.tracer.model.ErrorQueueContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ErrorQueueProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@Value(("${error.queue.topic:egov.error}"))
	private String topic;
	
	public void sendMessage(ErrorQueueContract errorQueueContract) {
		try {
		kafkaTemplate.send(topic, errorQueueContract);
		} catch (Exception ex) {
			log.info("exception occured while sending exception to error queue");
			ex.printStackTrace();
		}
	}
	
}
