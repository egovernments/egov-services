package org.egov.marriagefee.consumer;


import java.util.Map;

import org.egov.marriagefee.config.PropertiesManager;
import org.egov.marriagefee.service.DemandService;
import org.egov.marriagefee.web.contract.MarriageRegnRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class MarriageFeeConsumer {
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private DemandService demandService;
	
	@KafkaListener(topics = { "#{propertiesManager.getCreateMarriageFeeGenerated()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {
		log.info(" kakfaConsumer ");
		ObjectMapper objectMapper = new ObjectMapper();
		MarriageRegnRequest marriageregnRequest = objectMapper.convertValue(consumerRecord, MarriageRegnRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is" + consumerRecord);
		if (topic.equals(propertiesManager.getCreateMarriageFeeGenerated())) {
			try {
				log.info(" create  demand marriagefeeconsumer");
				demandService.createMarriageRegnDemand(marriageregnRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		
	}
	
	

}
