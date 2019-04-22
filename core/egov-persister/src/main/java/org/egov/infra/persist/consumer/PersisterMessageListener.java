package org.egov.infra.persist.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.infra.persist.service.PersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersisterMessageListener implements MessageListener<String, Object> {
	
	@Autowired
	private PersistService persistService;
	
	@Override
	public void onMessage(ConsumerRecord<String, Object> data) {
        log.info("Topic: "+data.topic());
        log.info("Value: "+data.value());
        ObjectMapper objectMapper = null;
		String rcvData = null;
		
		try {
			rcvData = objectMapper.writeValueAsString(data.value());
			persistService.persist(data.topic(),rcvData);    
		} catch (JsonProcessingException e) {
			persistService.postToErrorQueue(data.value(), e);
			e.printStackTrace();
		} catch (Exception e) {
			persistService.postToErrorQueue(data.value(), e);
			log.error("Exception: ", e);
		}
	}

}
