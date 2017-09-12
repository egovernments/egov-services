package org.egov.asset.consumers;

import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.model.Depreciation;
import org.egov.asset.service.AssetCategoryIndexService;
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

    @Autowired
    private AssetCategoryIndexService assetCategoryIndexService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = { "${kafka.topics.save.asset}", "${kafka.topics.save.depreciation}",
            "${kafka.topics.save.currentvalue}", "${kafka.topics.update.asset}", "${kafka.topics.save.revaluation}",
            "${kafka.topics.save.disposal}", "${kafka.topics.save.assetcategory}",
            "${kafka.topics.update.assetcategory}" })
    public void listen(final Map<String, Object> consumerRecord,
            @Header(KafkaHeaders.RECEIVED_TOPIC) final String topic) {

        log.info("topic:" + topic + ":" + "value:" + consumerRecord + "thread:" + Thread.currentThread());
        if (topic.equals(applicationProperties.getCreateAssetTopicName()))
            assetIndexService.postAsset(objectMapper.convertValue(consumerRecord, AssetRequest.class));
        else if (topic.equals(applicationProperties.getUpdateAssetTopicName()))
            assetIndexService.postAsset(objectMapper.convertValue(consumerRecord, AssetRequest.class));
        else if (topic.equals(applicationProperties.getCreateAssetRevaluationTopicName()))
            revaluationIndexService
                    .postAssetRevaluation(objectMapper.convertValue(consumerRecord, RevaluationRequest.class));
        else if (topic.equals(applicationProperties.getCreateAssetDisposalTopicName()))
            disposalIndexService.postAssetDisposal(objectMapper.convertValue(consumerRecord, DisposalRequest.class));
        else if (topic.equals(applicationProperties.getSaveCurrentvalueTopic()))
            currentValueIndexService
                    .indexCurrentValue(objectMapper.convertValue(consumerRecord, AssetCurrentValueRequest.class));
        else if (topic.equals(applicationProperties.getSaveDepreciationTopic()))
            depreciationIndexService.indexDepreciaiton(objectMapper.convertValue(consumerRecord, Depreciation.class));
        else if (topic.equals(applicationProperties.getSaveassetCategoryTopic()))
            assetCategoryIndexService
                    .postAssetCategory(objectMapper.convertValue(consumerRecord, AssetCategoryRequest.class));
        else if (topic.equals(applicationProperties.getUpdateAssetCategoryTopic()))
            assetCategoryIndexService
                    .postAssetCategory(objectMapper.convertValue(consumerRecord, AssetCategoryRequest.class));

    }
}