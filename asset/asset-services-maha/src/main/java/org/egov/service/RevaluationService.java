package org.egov.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.AssetCurrentValueRequest;
import org.egov.contract.RevaluationRequest;
import org.egov.contract.RevaluationResponse;
import org.egov.model.CurrentValue;
import org.egov.model.Revaluation;
import org.egov.model.criteria.RevaluationCriteria;
import org.egov.model.enums.KafkaTopicName;
import org.egov.model.enums.Sequence;
import org.egov.model.enums.TransactionType;
import org.egov.repository.RevaluationRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.web.wrapperfactory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RevaluationService {

    @Autowired
    private RevaluationRepository revaluationRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private AssetService assetService;

    //@Autowired private AssetConfigurationService assetConfigurationService;

    @Autowired
    private AssetCommonService assetCommonService;

   // @Autowired private CurrentValueService currentValueService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    
    public RevaluationResponse createAsync(final RevaluationRequest revaluationRequest) {
    	
         Revaluation revaluation = revaluationRequest.getRevaluation();
         RequestInfo requestInfo = revaluationRequest.getRequestInfo();

        revaluation.setId(assetCommonService.getNextId(Sequence.REVALUATIONSEQUENCE));
        // revaluation.setStatus(getRevaluationStatus(AssetStatusObjectName.REVALUATION, Status.APPROVED,revaluation.getTenantId());
        if (revaluation.getAuditDetails() == null)
            revaluation.setAuditDetails(assetCommonService.getAuditDetails(requestInfo));
        //todo voucher generation
        /*String revaluationVoucher= voucherService.revaluationVoucherGenerator(revaluationRequest);
        revaluation.setVoucherReference(revaluationVoucher);*/
        
        logAwareKafkaTemplate.send(applicationProperties.getRevaluationSaveTopic(), revaluationRequest);
        CurrentValue currentValue = CurrentValue.builder().
        		id(new Long(assetCommonService.getCode(Sequence.CURRENTVALUESEQUENCE))).assetId(revaluation.getAssetId()).assetTranType(TransactionType.REVALUATION).
        		 transactionDate(revaluation.getRevaluationDate()).auditDetails(assetCommonService.getAuditDetails(revaluationRequest.getRequestInfo())).
        		 tenantId(revaluation.getTenantId()).currentAmount(revaluation.getValueAfterRevaluation()).build();
        List<CurrentValue> assetCurrentValueList = new ArrayList<>();
        assetCurrentValueList.add(currentValue);
		AssetCurrentValueRequest assetCurrentValueRequest = AssetCurrentValueRequest.builder().assetCurrentValue(assetCurrentValueList).
				requestInfo(revaluationRequest.getRequestInfo()).build();
		
		logAwareKafkaTemplate.send(applicationProperties.getSaveCurrentvalueTopic(), assetCurrentValueRequest);
		
		// todo workflow integration
       /* logAwareKafkaTemplate.send(applicationProperties.getCreateWorkflowTopicName(), revaluationRequest);*/
        final List<Revaluation> revaluations = new ArrayList<Revaluation>();
        revaluations.add(revaluation);
        return getRevaluationResponse(revaluations, requestInfo);
    }

   /* public void saveRevaluationAmountToCurrentAmount(final RevaluationRequest revaluationRequest) {

        final Revaluation revaluation = revaluationRequest.getRevaluation();
        final List<AssetCurrentValue> assetCurrentValues = new ArrayList<AssetCurrentValue>();
        final AssetCurrentValue assetCurrentValue = new AssetCurrentValue();
        assetCurrentValue.setAssetId(revaluation.getAssetId());
        assetCurrentValue.setAssetTranType(TransactionType.REVALUATION);
        assetCurrentValue.setCurrentAmount(revaluation.getValueAfterRevaluation());
        assetCurrentValue.setTenantId(revaluation.getTenantId());
        assetCurrentValues.add(assetCurrentValue);
        final AssetCurrentValueRequest assetCurrentValueRequest = new AssetCurrentValueRequest();
        assetCurrentValueRequest.setRequestInfo(revaluationRequest.getRequestInfo());
        assetCurrentValueRequest.setAssetCurrentValues(assetCurrentValues);
        currentValueService.createCurrentValueAsync(assetCurrentValueRequest);
    }*/

    public RevaluationResponse search(final RevaluationCriteria revaluationCriteria, final RequestInfo requestInfo) {
        List<Revaluation> revaluations = new ArrayList<Revaluation>();
     
        revaluations = revaluationRepository.search(revaluationCriteria);
 
        return getRevaluationResponse(revaluations, requestInfo);
    }

	public RevaluationResponse updateAsync(RevaluationRequest revaluationRequest) {
		final Revaluation revaluation = revaluationRequest.getRevaluation();
        log.debug("assetRequest updateAsync::" + revaluationRequest);
        if (revaluation.getAuditDetails() == null)
            revaluation.setAuditDetails(assetCommonService.getAuditDetails(revaluationRequest.getRequestInfo()));
        
       /* logAwareKafkaTemplate.send(applicationProperties.getUpdateWorkflowTopicNmae(), revaluationRequest);*/
        logAwareKafkaTemplate.send(applicationProperties.getRevaluationUpdateTopic(),
				KafkaTopicName.UPDATEASSET.toString(), revaluationRequest);

		final List<Revaluation> revaluations = new ArrayList<>();
		revaluations.add(revaluation);
		return getRevaluationResponse(revaluations, revaluationRequest.getRequestInfo());
	}
 
    private RevaluationResponse getRevaluationResponse(final List<Revaluation> revaluations,
            final RequestInfo requestInfo) {
        final RevaluationResponse revaluationResponse = new RevaluationResponse();
        revaluationResponse.setRevaluations(revaluations);
        revaluationResponse.setResposneInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
        return revaluationResponse;
    }


}
