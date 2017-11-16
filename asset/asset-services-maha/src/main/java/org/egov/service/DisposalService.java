package org.egov.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.AssetCurrentValueRequest;
import org.egov.contract.AssetRequest;
import org.egov.contract.DisposalRequest;
import org.egov.contract.DisposalResponse;
import org.egov.model.Asset;
import org.egov.model.CurrentValue;
import org.egov.model.Disposal;
import org.egov.model.criteria.DisposalCriteria;
import org.egov.model.enums.Sequence;
import org.egov.model.enums.TransactionType;
import org.egov.repository.DisposalRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.web.wrapperfactory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DisposalService {

    @Autowired
    private DisposalRepository disposalRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    //@Autowired private VoucherService voucherService;

    @Autowired
    private AssetService assetService;

    //@Autowired private AssetMasterService assetMasterService;

    //@Autowired private AssetConfigurationService assetConfigurationService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    public DisposalResponse search(final DisposalCriteria disposalCriteria, final RequestInfo requestInfo) {
        return getResponse(disposalRepository.search(disposalCriteria), requestInfo);
    }

  
    public void setStatusOfAssetToDisposed(final DisposalRequest disposalRequest) {
    	
         Disposal disposal = disposalRequest.getDisposal();
         String tenantId = disposal.getTenantId();
         Asset asset = assetService.getAsset(tenantId, disposal.getAssetId(), disposalRequest.getRequestInfo());
         // final List<AssetStatus> assetStatuses = assetMasterService.getStatuses(AssetStatusObjectName.ASSETMASTER,Status.DISPOSED, tenantId);
         asset.setStatus("DISPOSED"); // TODO remove hard coding get data from mdms
         AssetRequest assetRequest = AssetRequest.builder().asset(asset)
                .requestInfo(disposalRequest.getRequestInfo()).build();
        assetService.updateAsync(assetRequest);
    }

    public DisposalResponse createAsync(final DisposalRequest disposalRequest) {
        final Disposal disposal = disposalRequest.getDisposal();
        disposal.setTransactionType(TransactionType.DISPOSAL);
        disposal.setId(assetCommonService.getNextId(Sequence.DISPOSALSEQUENCE));

        if (disposal.getAuditDetails() == null)
            disposal.setAuditDetails(assetCommonService.getAuditDetails(disposalRequest.getRequestInfo()));

        logAwareKafkaTemplate.send(applicationProperties.getDisposalSaveTopicName(), disposalRequest);
       /* CurrentValue currentValue = CurrentValue.builder().
        		id(new Long(assetCommonService.getCode(Sequence.CURRENTVALUESEQUENCE))).assetId(disposal.getAssetId()).assetTranType(TransactionType.DISPOSAL).
        		 transactionDate(disposal.getDisposalDate()).auditDetails(assetCommonService.getAuditDetails(disposalRequest.getRequestInfo())).
        		 tenantId(disposal.getTenantId()).currentAmount(disposal.getSaleValue()).build();
        List<CurrentValue> assetCurrentValueList = new ArrayList<>();
        assetCurrentValueList.add(currentValue);
		AssetCurrentValueRequest assetCurrentValueRequest = AssetCurrentValueRequest.builder().assetCurrentValue(assetCurrentValueList).
				requestInfo(disposalRequest.getRequestInfo()).build();
		logAwareKafkaTemplate.send(applicationProperties.getSaveCurrentvalueTopic(), assetCurrentValueRequest);*/
		//todo workflow
		
        setStatusOfAssetToDisposed(disposalRequest);
        final List<Disposal> disposals = new ArrayList<Disposal>();
        disposals.add(disposal);
        return getResponse(disposals, disposalRequest.getRequestInfo());
    }

    public DisposalResponse getResponse(final List<Disposal> disposals, final RequestInfo requestInfo) {
        final DisposalResponse disposalResponse = new DisposalResponse();
        disposalResponse.setDisposals(disposals);
        disposalResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
        return disposalResponse;
    }

}
