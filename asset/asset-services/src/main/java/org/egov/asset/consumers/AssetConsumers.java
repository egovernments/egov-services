package org.egov.asset.consumers;

import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.model.Depreciation;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.service.AssetService;
import org.egov.asset.service.CurrentValueService;
import org.egov.asset.service.DepreciationService;
import org.egov.asset.service.DisposalService;
import org.egov.asset.service.RevaluationService;
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
    private AssetCategoryService assetCategoryService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RevaluationService revaluationService;

    @Autowired
    private DisposalService disposalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrentValueService currentValueService;

    @Autowired
    private DepreciationService depreciationService;

    @KafkaListener(topics = { "${kafka.topics.save.assetcategory}", "${kafka.topics.update.assetcategory}",
            "${kafka.topics.save.asset}", "${kafka.topics.update.asset}", "${kafka.topics.save.revaluation}",
            "${kafka.topics.save.disposal}", "${kafka.topics.save.depreciation}", "${kafka.topics.save.currentvalue}" })
    public void listen(final Map<String, Object> consumerRecord,
            @Header(KafkaHeaders.RECEIVED_TOPIC) final String topic) {

        log.debug("key:" + topic + ":" + "value:" + consumerRecord);

        if (topic.equals(applicationProperties.getCreateAssetTopicName()))
            assetService.create(objectMapper.convertValue(consumerRecord, AssetRequest.class));
        else if (topic.equals(applicationProperties.getUpdateAssetTopicName()))
            assetService.update(objectMapper.convertValue(consumerRecord, AssetRequest.class));
        else if (topic.equals(applicationProperties.getCreateAssetCategoryTopicName()))
            assetCategoryService.create(objectMapper.convertValue(consumerRecord, AssetCategoryRequest.class));
        else if (topic.equals(applicationProperties.getUpdateAssetCategoryTopicName()))
            assetCategoryService.update(objectMapper.convertValue(consumerRecord, AssetCategoryRequest.class));
        else if (topic.equals(applicationProperties.getCreateAssetRevaluationTopicName()))
            revaluationService.create(objectMapper.convertValue(consumerRecord, RevaluationRequest.class));
        else if (topic.equals(applicationProperties.getCreateAssetDisposalTopicName()))
            disposalService.create(objectMapper.convertValue(consumerRecord, DisposalRequest.class));
        else if (topic.equals(applicationProperties.getSaveCurrentvalueTopic()))
            currentValueService
                    .saveCurrentValue(objectMapper.convertValue(consumerRecord, AssetCurrentValueRequest.class));
        else if (topic.equals(applicationProperties.getSaveDepreciationTopic()))
            depreciationService.save(objectMapper.convertValue(consumerRecord, Depreciation.class));

    }
}