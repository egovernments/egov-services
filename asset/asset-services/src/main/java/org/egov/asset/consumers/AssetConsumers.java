/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

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