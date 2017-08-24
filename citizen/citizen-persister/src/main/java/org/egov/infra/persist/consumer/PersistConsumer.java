package org.egov.infra.persist.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.infra.persist.service.PersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersistConsumer {
	
	@Autowired
	private org.egov.infra.persist.web.contract.Service service;
	
	@Autowired
	private PersistService persistService;

	@KafkaListener(topics = {"${kafka.topics.save.servicereq}","${kafka.topics.update.servicereq}"})
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		//log.info("topic:" + record.topic() + ":" + "value:" + record.value());
		log.info("topic:" + topic+ ":" + "value:" + consumerRecord);
		System.out.println("ObjectCollection objectCollection:"+service);
		
		//persistService.persist(record.topic(), record.value());
		
	}
	
}
