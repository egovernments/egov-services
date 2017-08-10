package org.egov.asset.consumers;

import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.model.Depreciation;
import org.egov.asset.service.AssetIndexService;
import org.egov.asset.service.CurrentValueIndexService;
import org.egov.asset.service.DepreciationIndexService;
import org.egov.asset.service.DisposalIndexService;
import org.egov.asset.service.RevaluationIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetConsumers {

	@Autowired
	private AssetIndexService assetIndexService;

	@Autowired
	private RevaluationIndexService revaluationIndexService;

	@Autowired
	private DisposalIndexService disposalIndexService;

	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private DepreciationIndexService depreciationIndexService;
	
	@Autowired
	private CurrentValueIndexService currentValueIndexService;
	
	

	@KafkaListener(topics = { "${kafka.topics.save.asset}", "${kafka.topics.save.depreciation}",
			"${kafka.topics.save.currentvalue}", "${kafka.topics.update.asset}", "${kafka.topics.save.revaluation}",
			"${kafka.topics.update.revaluation}", "${kafka.topics.save.disposal}", "${kafka.topics.update.disposal}" })
	public void listen(final Map<String, Object> consumerRecord,
			@Header(KafkaHeaders.RECEIVED_TOPIC) final String topic) {

		log.info("topic:" + topic + ":" + "value:" + consumerRecord + "thread:" + Thread.currentThread());
		final ObjectMapper objectMapper = new ObjectMapper();
		if (topic.equals(applicationProperties.getCreateAssetTopicName()))
			assetIndexService.postAsset(objectMapper.convertValue(consumerRecord, AssetRequest.class));
		else if (topic.equals(applicationProperties.getUpdateAssetTopicName()))
			assetIndexService.putAsset(objectMapper.convertValue(consumerRecord, AssetRequest.class));
		else if (topic.equals(applicationProperties.getCreateAssetRevaluationTopicName())
				|| topic.equals(applicationProperties.getUpdateAssetRevaluationTopicName())) {
			revaluationIndexService.postAssetRevaluation(
					objectMapper.convertValue(consumerRecord, RevaluationRequest.class));
		} else if (topic.equals(applicationProperties.getCreateAssetDisposalTopicName())
				|| topic.equals(applicationProperties.getUpdateAssetDisposalTopicName())) {
			disposalIndexService.postAssetDisposal(objectMapper.convertValue(consumerRecord, DisposalRequest.class));
		}else if(topic.equals(applicationProperties.getSaveCurrentvalueTopic())){
        	currentValueIndexService.indexCurrentValue(objectMapper.convertValue(consumerRecord, AssetCurrentValueRequest.class));
        }else if(topic.equals(applicationProperties.getSaveDepreciationTopic())){
        	depreciationIndexService.indexDepreciaiton(objectMapper.convertValue(consumerRecord, Depreciation.class));
        }

	}
}