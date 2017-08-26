package org.egov.infra.indexer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.infra.indexer.service.IndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

/*
 * Indexer Consumer to read from kafka topic
 * @author ranjeetvimal
 *
 */
public class IndexerConsumer {
	@Autowired
	private org.egov.infra.indexer.web.contract.Service service;
	
	@Autowired
	private IndexerService indexService;
	
	
	@KafkaListener(topics = {"${kafka.topics.save.servicereq}","${kafka.topics.update.servicereq}"})
	public void processMessage(ConsumerRecord<String, String> record) {
		//log.info("topic:" + record.topic() + ":" + "value:" + record.value());
		//System.out.println("ObjectCollection objectCollection:"+ service);
		indexService.ElasticIndexer(record.topic(), record.value());
		
	}

}
