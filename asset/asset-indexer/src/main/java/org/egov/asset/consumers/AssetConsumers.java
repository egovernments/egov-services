package org.egov.asset.consumers;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.service.AssetIndexService;
import org.egov.asset.service.DisposalIndexService;
import org.egov.asset.service.RevaluationIndexService;
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
	private RevaluationIndexService revaluationIndexService;

	@Autowired
	private DisposalIndexService disposalIndexService;

	@Autowired
	private ApplicationProperties applicationProperties;

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = { "${kafka.topics.save.asset}",
			"${kafka.topics.update.asset}", "${kafka.topics.save.revaluation}", "${kafka.topics.update.revaluation}",
			"${kafka.topics.save.disposal}", "${kafka.topics.update.disposal}" })
	public void listen1(final ConsumerRecord<String, String> record) {

		LOGGER.info("topic:" + record.topic() + ":" + "value:" + record.value() + "thread:" + Thread.currentThread());
		final ObjectMapper objectMapper = new ObjectMapper();
		AssetRequest assetRequest = null;

		try {
			assetRequest = objectMapper.readValue(record.value(), AssetRequest.class);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		if (record.topic().equals(applicationProperties.getCreateAssetTopicName()))
			assetIndexService.postAsset(assetRequest);
		else if (record.topic().equals(applicationProperties.getUpdateAssetTopicName()))
			assetIndexService.putAsset(assetRequest);
		else if (record.topic().equals(applicationProperties.getCreateAssetRevaluationTopicName())
				|| record.topic().equals(applicationProperties.getUpdateAssetRevaluationTopicName())) {
			RevaluationRequest revaluationRequest = null;
			try {
				revaluationRequest = objectMapper.readValue(record.value(), RevaluationRequest.class);
			} catch (final IOException e) {
				LOGGER.info("An exception occurred in fetching of Revaluation Object ::" + e.toString());
				e.printStackTrace();
			}
			revaluationIndexService.postAssetRevaluation(revaluationRequest);
		} else if (record.topic().equals(applicationProperties.getCreateAssetDisposalTopicName())
				|| record.topic().equals(applicationProperties.getUpdateAssetDisposalTopicName())) {
			DisposalRequest disposalRequest = null;
			try {
				disposalRequest = objectMapper.readValue(record.value(), DisposalRequest.class);
			} catch (final IOException e) {
				LOGGER.info("An exception occurred in fetching of Disposal Object ::" + e.toString());
				e.printStackTrace();
			}
			disposalIndexService.postAssetDisposal(disposalRequest);
		}

	}
}