package org.egov.collection.consumer;


import java.util.HashMap;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.service.WorkflowService;
import org.egov.collection.web.contract.ReceiptReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class CollectionConsumer {
	
	public static final Logger logger = LoggerFactory.getLogger(CollectionConsumer.class);
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired 
	private ReceiptService recieptService;	
	
	@KafkaListener(topics = {"${kafka.topics.receipt.create.name}", "${kafka.topics.receipt.cancel.name}",
			"${kafka.topics.stateId.update.name}","${kafka.topics.receipt.update.name}"})
	
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
	logger.info("Record: "+record.toString());
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
             if(topic.equals(applicationProperties.getCancelReceiptTopicName())){
				logger.info("Consuming cancel Receipt request");
				recieptService.cancelReceiptBeforeRemittance(objectMapper.convertValue(record, ReceiptReq.class));
			}else if(topic.equals(applicationProperties.getKafkaUpdateStateIdTopic())){
				logger.info("Consuming updateStateId request");
				recieptService.updateStateId(objectMapper.convertValue(record, WorkflowDetails.class));
			}else if(topic.equals(applicationProperties.getUpdateReceiptTopicName())){
				logger.info("Consuming update Receipt request");
				recieptService.updateReceipt(objectMapper.convertValue(record, ReceiptReq.class));
			}
			
		} catch (final Exception e) {
			logger.error("Error while listening to value: "+record+" on topic: "+topic+": ", e.getMessage());
		}
	}

}