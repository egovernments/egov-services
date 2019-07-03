package org.egov.mseva.consumer;

import java.util.HashMap;

import org.egov.mseva.config.PropertiesManager;
import org.egov.mseva.service.MsevaService;
import org.egov.mseva.web.contract.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MsevaEventsConsumer {
	
	
    @Autowired
    private ObjectMapper objectMapper;
    
	@Autowired
	private MsevaService service;
	
	@Autowired
	private PropertiesManager props;
	
	
	/**
	 * Kafka consumer
	 * 
	 * @param record
	 * @param topic
	 */
    @KafkaListener(topics = { "${kafka.topics.save.events}", "${kafka.topics.update.events}" })
	public void listen(HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		try {
			EventRequest eventReq = objectMapper.convertValue(record, EventRequest.class);
			if(topic.equals(props.getSaveEventsTopic())) {
				service.createEvents(eventReq);
			}else if(topic.equals(props.getUpdateEventsTopic())) {
				service.updateEvents(eventReq);
			}
		}catch(Exception e) {
			log.error("Exception while reading from the queue: ", e);
		}
	}

}
