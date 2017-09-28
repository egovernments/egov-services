package org.egov.infra.indexer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.infra.indexer.service.IndexerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IndexerMessageListener implements MessageListener<String, String> {
	
	public static final Logger logger = LoggerFactory.getLogger(IndexerMessageListener.class);

	@Autowired
	private IndexerService indexerService;
	
	@Override
	public void onMessage(ConsumerRecord<String, String> data) {
        logger.info("record received: "+data);
        logger.info("Topic: "+data.topic());
        logger.info("Value: "+data.value());
		ObjectMapper mapper = new ObjectMapper();
	//	IndexerService indexerService = new IndexerService();
		String recordString = null;
		try{
			recordString = mapper.writeValueAsString(data.value());
		}catch(Exception e){
			logger.error("Exception while parsing map to json", e);
		}
		logger.info("recordString: "+recordString);
		indexerService.elasticIndexer(data.topic(), data.value());     
	}

}
