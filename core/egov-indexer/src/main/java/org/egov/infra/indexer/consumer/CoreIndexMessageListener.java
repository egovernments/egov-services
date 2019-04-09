package org.egov.infra.indexer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.infra.indexer.service.IndexerService;
import org.egov.infra.indexer.util.IndexerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoreIndexMessageListener implements MessageListener<String, String> {

	@Autowired
	private IndexerService indexerService;
	
	@Autowired
	private IndexerUtils utils;

	@Override
	/**
	 * Messages listener which acts as consumer. This message listener is injected
	 * inside a kafkaContainer. This consumer is a start point to the following
	 * index jobs: 1. Core indexing
	 */
	public void onMessage(ConsumerRecord<String, String> data) {
		log.info("Topic: " + data.topic());
		try {
			indexerService.esIndexer(data.topic(), data.value());
		} catch (Exception e) {
			utils.postToErrorQueue(data.value(), e);
			log.error("error while indexing: ", e);
		}
	}

}
