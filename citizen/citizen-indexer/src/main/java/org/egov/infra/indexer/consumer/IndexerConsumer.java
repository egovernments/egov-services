package org.egov.infra.indexer.consumer;

import java.util.HashMap;

import org.egov.infra.indexer.service.IndexerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Indexer Consumer to read from kafka topic
 * @author ranjeetvimal
 *
 */

@Service
public class IndexerConsumer {
	
	public static final Logger logger = LoggerFactory.getLogger(IndexerConsumer.class);
	
	@Autowired
	private IndexerService indexService;
	
	@KafkaListener(topics = {"${kafka.topics.save.servicereq}","${kafka.topics.update.servicereq}"})
	
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {		
		logger.info("Record receieved at the Index Consumer: "+record);
		logger.info("Topic receieved at the Index Consumer: "+topic);
		ObjectMapper mapper = new ObjectMapper();
		String recordString = null;
		try{
			recordString = mapper.writeValueAsString(record);
		}catch(Exception e){
			logger.error("Exception while parsing map to json", e);
		}
		logger.info("recordString: "+recordString);
	    indexService.ElasticIndexer(topic, recordString);
	}

}
