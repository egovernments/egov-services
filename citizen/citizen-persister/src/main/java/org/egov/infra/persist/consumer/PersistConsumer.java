package org.egov.infra.persist.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.infra.persist.service.PersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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
	public void processMessage(ConsumerRecord<String, String> record) {
		log.info("topic:" + record.topic() + ":" + "value:" + record.value());
		System.out.println("ObjectCollection objectCollection:"+service);
		persistService.persist(record.topic(), record.value());
		
	}
	
}
