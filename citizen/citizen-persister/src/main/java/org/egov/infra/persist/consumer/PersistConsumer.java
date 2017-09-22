package org.egov.infra.persist.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.infra.persist.service.PersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersistConsumer {
	
	@Autowired
	private org.egov.infra.persist.web.contract.Service service;
	
	@Autowired
	private PersistService persistService;

	@KafkaListener(topics = {"${kafka.topics.save.bill}","${egov.propertytax.property.create.validated}","${kafka.topics.update.reissueappl}","${kafka.topics.create.reissueappl}","${kafka.topics.create.message}","${kafka.topics.update.fee}","${kafka.topics.create.fee}","${kafka.topics.save.demand}","${kafka.topics.update.marriagedocumenttype}","${kafka.topics.create.marriagedocumenttype}","${kafka.topics.create.registrationunit}","${kafka.topics.update.registrationunit}","${kafka.topics.create.marriageregn}","${kafka.topics.update.marriageregn}","${kafka.topics.save.servicereq}","${kafka.topics.update.servicereq}"})
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		//log.info("topic:" + record.topic() + ":" + "value:" + record.value());
		log.info("topic:" + topic+ ":" + "value:" + consumerRecord);
		System.out.println("ObjectCollection consumerRecord:"+consumerRecord);
		System.out.println("ObjectCollection service:"+service);
		ObjectMapper objectMapper = new ObjectMapper();
		String rcvData = null;
		
		try {
			rcvData = objectMapper.writeValueAsString(consumerRecord);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		persistService.persist(topic,rcvData);
		
	}
	
}
