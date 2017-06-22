package org.egov.collection.consumer;


import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.web.contract.ReceiptInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CollectionConsumer {
	
	public static final Logger logger = LoggerFactory.getLogger(CollectionConsumer.class);
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired 
	private ReceiptService recieptService;
	
	
	@KafkaListener(topics = {"${kafka.topics.receipt.create.name}" })
	
	public void listen(final String record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			if (topic.equals(applicationProperties.getCreateReceiptTopicName())) {
				logger.info("Consuming create ReceiptDetails request");
				recieptService.create(objectMapper.readValue(record, ReceiptInfo.class));
			}
		} catch (final Exception e) {
			logger.error("Error while listening to value: "+record+" on topic: "+topic+": ", e.getMessage());
		}
	}

}
