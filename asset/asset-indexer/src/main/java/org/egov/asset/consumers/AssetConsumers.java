package org.egov.asset.consumers;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.service.AssetIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AssetConsumers {

	public static final Logger LOGGER = LoggerFactory.getLogger(AssetConsumers.class);

	@Autowired
	private AssetIndexService assetIndexService;

	@Autowired
	private ApplicationProperties applicationProperties;

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = { "${kafka.topics.save.asset}",
			"${kafka.topics.update.asset}" })
	public void listen1(ConsumerRecord<String, String> record) {

		LOGGER.info("topic:" + record.topic() + ":" + "value:" + record.value() + "thread:" + Thread.currentThread());
		ObjectMapper objectMapper = new ObjectMapper();
		AssetRequest assetRequest = null;
		try {
			assetRequest = objectMapper.readValue(record.value(), AssetRequest.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (record.topic().equals(applicationProperties.getCreateAssetTopicName())) {
			assetIndexService.postAsset(assetRequest);
		} else if (record.topic().equals(applicationProperties.getUpdateAssetTopicName())) {
			assetIndexService.putAsset(assetRequest);
		}

	}
}