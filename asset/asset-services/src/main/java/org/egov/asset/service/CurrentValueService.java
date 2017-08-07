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

    public void getAssetDepreciation(final Long assetId, final String tenantId) {
        // TODO
    }

    /*
     * public AssetCurrentValueResponse getCurrentAmount(final Long assetId,
     * final String tenantId, final RequestInfo requestInfo) { BigDecimal
     * currentValue = null; final Asset asset = getAsset(assetId, tenantId,
     * requestInfo); final Revaluation revaluation = getRevaluateAsset(assetId,
     * tenantId); currentValue = asset.getGrossValue(); if (revaluation != null)
     * { currentValue = revaluation.getCurrentCapitalizedValue(); if
     * (revaluation.getTypeOfChange().toString().equals(TypeOfChangeEnum.
     * INCREASED.toString())) currentValue =
     * currentValue.add(revaluation.getRevaluationAmount()); else if
     * (revaluation.getTypeOfChange().toString().equals(TypeOfChangeEnum.
     * DECREASED.toString())) currentValue =
     * currentValue.subtract(revaluation.getRevaluationAmount()); } return
     * getResponse(currentValue, tenantId, assetId); }
     */

    /*
     * public Revaluation getRevaluateAsset(final Long assetId, final String
     * tenantId) { log.info("AssetCurrentAmountService getRevaluateAsset");
     * final List<Long> assetIds = new ArrayList<>(); assetIds.add(assetId);
     * final RevaluationCriteria revaluationCriteria =
     * RevaluationCriteria.builder().assetId(assetIds)
     * .tenantId(tenantId).status("ACTIVE").build(); final RevaluationResponse
     * revaluationResponse = revaluationService.search(revaluationCriteria);
     * Revaluation revaluation = null; if
     * (!revaluationResponse.getRevaluations().isEmpty()) revaluation =
     * revaluationResponse.getRevaluations().get(0); return revaluation; }
     */

    /*
     * public Asset getAsset(final Long assetId, final String tenantId, final
     * RequestInfo requestInfo) { log.info("AssetCurrentAmountService getAsset"
     * ); final AssetCriteria assetCriteria = new AssetCriteria(); final
     * List<Long> assetIds = new ArrayList<>(); assetIds.add(assetId);
     * assetCriteria.setId(assetIds); assetCriteria.setTenantId(tenantId); final
     * AssetResponse assetResponse = assetService.getAssets(assetCriteria,
     * requestInfo); Asset asset = null; if (assetResponse.getAssets().size() !=
     * 0) asset = assetResponse.getAssets().get(0); if (asset == null) throw new
     * RuntimeException("Invalid Asset"); return asset; }
     */
}
