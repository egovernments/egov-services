package org.egov.demand.consumer;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TaxHeadMasterConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(TaxHeadMasterConsumer.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private TaxHeadMasterService taxHeadMasterservice;
	
	@KafkaListener(containerFactory="kafkaListenerContainerFactory",
			topics = {"${kafka.topics.save.taxHeadMaster}","${kafka.topics.update.taxHeadMaster}"})
	public void listen(ConsumerRecord<String, String> record) {
		System.out.println(":::In consumer:::");
		LOGGER.info("topic:"+ record.topic() +":"+ "value:" +record.value()+"thread:"+Thread.currentThread());
		
		try {
		
			if (record.topic().equals(applicationProperties.getCreateTaxHeadMasterTopicName()))
			taxHeadMasterservice.create(objectMapper.readValue(record.value(),TaxHeadMasterRequest.class));
//			else if(record.topic().equals(applicationProperties.getUpdateTaxHeadMasterTopicName()))
//				taxHeadMasterservice.update(objectMapper.readValue(record.value(),TaxHeadMasterRequest.class));
		} catch (IOException e) {
			LOGGER.info("TaxHeadMasterConsumer ",e);
			throw new RuntimeException(e);
		}
	}
}
