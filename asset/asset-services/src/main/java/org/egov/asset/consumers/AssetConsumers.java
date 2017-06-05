package org.egov.asset.consumers;

import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.service.AssetService;
import org.egov.asset.service.DisposalService;
import org.egov.asset.service.RevaluationService;
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
	
	@Autowired
	private RevaluationService revaluationService;
	
	@Autowired
	private DisposalService disposalService;

	@KafkaListener(containerFactory="kafkaListenerContainerFactory",
					topics = {"${kafka.topics.save.assetcategory}","${kafka.topics.update.assetcategory}",
							  "${kafka.topics.save.asset}","${kafka.topics.update.asset}",
							  "${kafka.topics.save.revaluation}","${kafka.topics.update.revaluation}",
							  "${kafka.topics.save.disposal}","${kafka.topics.update.disposal}"})
	public void listen(ConsumerRecord<String, String> record) {
		
		LOGGER.info("topic:"+ record.topic() +":"+ "value:" +record.value()+"thread:"+Thread.currentThread());
	   
			ObjectMapper objectMapper=new ObjectMapper();
			try {
				if (record.topic().equals(applicationProperties.getCreateAssetTopicName()))
					assetService.create(objectMapper.readValue(record.value(),AssetRequest.class));	
				else if(record.topic().equals(applicationProperties.getUpdateAssetTopicName()))
					assetService.update(objectMapper.readValue(record.value(),AssetRequest.class));
				else if (record.topic().equals(applicationProperties.getCreateAssetCategoryTopicName())) 
					assetCategoryService.create(objectMapper.readValue(record.value(),AssetCategoryRequest.class));	
				else if(record.topic().equals(applicationProperties.getUpdateAssetCategoryTopicName()))
					assetCategoryService.update(objectMapper.readValue(record.value(),AssetCategoryRequest.class));
				else if (record.topic().equals(applicationProperties.getCreateAssetRevaluationTopicName()))
					revaluationService.create(objectMapper.readValue(record.value(),RevaluationRequest.class));	
				/*else if(record.topic().equals(applicationProperties.getUpdateAssetTopicName()))
					revaluationService.update(record.value(),RevaluationRequest.class);*/
				else if (record.topic().equals(applicationProperties.getCreateAssetDisposalTopicName()))
					disposalService.create(objectMapper.readValue(record.value(),DisposalRequest.class));	
			} catch (IOException e) {
				LOGGER.info("AssetConsumers ",e);
				throw new RuntimeException(e);
			}
			
			
	}
	
	/*@KafkaListener(containerFactory="kafkaListenerContainerFactory",
				   topics = {"${kafka.topics.save.asset}","${kafka.topics.update.asset}"})
	public void listen1(ConsumerRecord<String, String> record) {
		
		LOGGER.info("listen1 topic:"+ record.topic() +":"+ "value:" +record.value()+ "thread:"+Thread.currentThread());
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
			else if(record.topic().equals(applicationProperties.getUpdateAssetTopicName())){
				assetService.update(assetRequest);
			}
	}
	
	@KafkaListener(containerFactory="kafkaListenerContainerFactory",
			   topics = {"${kafka.topics.save.revaluation}","${kafka.topics.update.revaluation}"})
	public void revaluation(ConsumerRecord<String, String> record) {
	
		LOGGER.info("revaluation topic:"+ record.topic() +":"+ "value:" +record.value()+ "thread:"+Thread.currentThread());
		ObjectMapper objectMapper = new ObjectMapper();
		RevaluationRequest revaluationRequest = null;
		try {
			revaluationRequest=objectMapper.readValue(record.value(),RevaluationRequest.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}	

		if (record.topic().equals(applicationProperties.getCreateAssetRevaluationTopicName())) {
			revaluationService.create(revaluationRequest);	
		}
		else if(record.topic().equals(applicationProperties.getUpdateAssetTopicName())){
			//revaluationService.update(revaluationRequest);
		}
	}*/
}