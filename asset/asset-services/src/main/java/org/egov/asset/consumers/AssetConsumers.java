package org.egov.asset.consumers;

import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.service.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AssetConsumers {

	public static final Logger LOGGER = LoggerFactory.getLogger(AssetConsumers.class);
	
	@Autowired
	private AssetCategoryService assetCategoryService;
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
	private ApplicationProperties applicationProperties;

	@KafkaListener(containerFactory="kafkaListenerContainerFactory",topics = "${kafka.topics.save.assetcategory}")
	public void listen(ConsumerRecord<String, String> record) {
		
		LOGGER.info("topic:"+ record.topic() +":"+ "value:" +record.value()+"thread:"+Thread.currentThread());
	   
			ObjectMapper objectMapper=new ObjectMapper();
			try {
				LOGGER.info("SaveAssetConsumer save-assetcategory-db assetCategoryService:"+assetCategoryService);
				assetCategoryService.create(objectMapper.readValue(record.value(),AssetCategoryRequest.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	@KafkaListener(containerFactory="kafkaListenerContainerFactory",
				   topics = {"${kafka.topics.save.asset}","${kafka.topics.update.asset}"})
	public void listen1(ConsumerRecord<String, String> record) {
		
		LOGGER.info("topic:"+ record.topic() +":"+ "value:" +record.value()+ "thread:"+Thread.currentThread());
		ObjectMapper objectMapper = new ObjectMapper();
		AssetRequest assetRequest = null;
		try {
			 assetRequest=objectMapper.readValue(record.value(),AssetRequest.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	
		if (record.topic().equals(applicationProperties.getCreateAssetTopicName())) {
			assetService.create(assetRequest);	
		}
		if(record.topic().equals(applicationProperties.getUpdateAssetTopicName())){
			assetService.update(assetRequest);
		}
	}
}