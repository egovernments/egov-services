package org.egov.asset.service;

import java.util.List;
import java.util.Set;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetCurrentValueResponse;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.repository.CurrentValueRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentValueService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private CurrentValueRepository currentValueRepository;

    @Autowired
    private SequenceGenService sequenceGenService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private ApplicationProperties applicationProperties;

    public AssetCurrentValueResponse getCurrentValues(final Set<Long> assetIds, final String tenantId,
            final RequestInfo requestInfo) {

        return new AssetCurrentValueResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
                currentValueRepository.getCurrentValues(assetIds, tenantId));
    }

    public AssetCurrentValueResponse createCurrentValueAsync(final AssetCurrentValueRequest assetCurrentValueRequest) {

        final RequestInfo requestInfo = assetCurrentValueRequest.getRequestInfo();
        final List<AssetCurrentValue> assetCurrentValues = assetCurrentValueRequest.getAssetCurrentValues();
        final AuditDetails auditDetails = assetCommonService.getAuditDetails(requestInfo);

        final List<Long> idList = sequenceGenService.getIds(assetCurrentValues.size(),
                Sequence.CURRENTVALUESEQUENCE.toString());
        int i = 0;
        for (final AssetCurrentValue assetCurrentValue : assetCurrentValues) {
            assetCurrentValue.setAuditDetails(auditDetails);
            assetCurrentValue.setId(idList.get(i++));
        }
        kafkaTemplate.send(applicationProperties.getSaveCurrentvalueTopic(), assetCurrentValueRequest);
        return new AssetCurrentValueResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
                assetCurrentValues);
    }

    public void saveCurrentValue(final AssetCurrentValueRequest assetCurrentValueRequest) {

        currentValueRepository.create(assetCurrentValueRequest.getAssetCurrentValues());
    }

}
