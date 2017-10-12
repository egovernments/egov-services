package org.egov.tracer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaConsumerErrorHandller extends LoggingErrorHandler {

	@Autowired
	private ExceptionAdvise exceptionAdvise;
	
	@Override
	public void handle(Exception thrownException, ConsumerRecord<?, ?> record) {
		log.error("Error while processing1: " + ObjectUtils.nullSafeToString(record), thrownException);
		ObjectMapper objectMapper = new ObjectMapper();
	    String body = null;
		try {
			body = objectMapper.writeValueAsString(record.value());
		} catch (Exception ex) {
			log.error("KafkaConsumerErrorHandller Kafka consumer can not parse json data");
			ex.printStackTrace();
		}
		exceptionAdvise.sendErrorMessage(body, thrownException, record.topic(), null);
	}
	
	
}
