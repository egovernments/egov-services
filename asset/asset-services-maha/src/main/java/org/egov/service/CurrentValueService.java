package org.egov.service;

import java.util.List;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.AssetCurrentValueRequest;
import org.egov.contract.AssetCurrentValueResponse;
import org.egov.model.CurrentValue;
import org.egov.model.AuditDetails;
import org.egov.model.enums.Sequence;
import org.egov.repository.CurrentValueRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.web.wrapperfactory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
    
    public List<Long> getNonTransactedCurrentValues(final Set<Long> assetIds, final String tenantId,
            final RequestInfo requestInfo) {

        return  currentValueRepository.getNonTransactedCurrentValues(assetIds, tenantId);
    }


    public AssetCurrentValueResponse createCurrentValueAsync(final AssetCurrentValueRequest assetCurrentValueRequest) {
        log.info(" CurrentValueService  assetCurrentValueRequest"+assetCurrentValueRequest);
        final RequestInfo requestInfo = assetCurrentValueRequest.getRequestInfo();
        final List<CurrentValue> assetCurrentValues = assetCurrentValueRequest.getAssetCurrentValue();
        final AuditDetails auditDetails = assetCommonService.getAuditDetails(requestInfo);

        final List<Long> idList = sequenceGenService.getIds(assetCurrentValues.size(), Sequence.CURRENTVALUESEQUENCE.toString());
        int i = 0;
        for (final CurrentValue assetCurrentValue : assetCurrentValues) {
            assetCurrentValue.setAuditDetails(auditDetails);
            assetCurrentValue.setId(idList.get(i++));
        }
        kafkaTemplate.send(applicationProperties.getSaveCurrentvalueTopic(), assetCurrentValueRequest);
        log.info("response in service  "+ new AssetCurrentValueResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
                assetCurrentValues));
        return new AssetCurrentValueResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
                assetCurrentValues);
    }


}