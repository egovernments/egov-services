package org.egov.asset.consumers;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.service.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SaveAssetConsumer {
	
public static final Logger LOGGER = LoggerFactory.getLogger(SaveAssetConsumer.class);
	
	@Autowired
	private AssetService assetService;
	
	/*@KafkaListener(containerFactory="kafkaListenerContainerFactory",topics ="save-asset-db")
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("key:"+ record.key() +":"+ "value:" +record.value()+ "thread:"+Thread.currentThread());
	    if (record.topic().equals("save-asset-db")) {
			ObjectMapper objectMapper=new ObjectMapper();
			try {
				LOGGER.info("SaveAssetConsumer save-assetcategory-db assetCategoryService:"+assetService);
				assetService.create(objectMapper.readValue(record.value(),AssetRequest.class));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
}
