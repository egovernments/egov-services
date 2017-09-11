package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.VoucherAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.KafkaTopicName;
import org.egov.asset.model.enums.Sequence;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.AssetRepository;
import org.egov.asset.repository.DisposalRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetMasterService assetMasterService;

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    @Autowired
    private AssetCommonService assetCommonService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    public DisposalResponse search(final DisposalCriteria disposalCriteria, final RequestInfo requestInfo) {
        List<Disposal> disposals = null;

        try {
            disposals = disposalRepository.search(disposalCriteria);
        } catch (final Exception ex) {
            log.info("DisposalService:", ex);
            throw new RuntimeException(ex);
        }
        return getResponse(disposals, requestInfo);
    }

    public void create(final DisposalRequest disposalRequest) {
        disposalRepository.create(disposalRequest);
        setStatusOfAssetToDisposed(disposalRequest);
    }

    public void setStatusOfAssetToDisposed(final DisposalRequest disposalRequest) {
        final Disposal disposal = disposalRequest.getDisposal();
        final String tenantId = disposal.getTenantId();
        final Asset asset = assetService.getAsset(tenantId, disposal.getAssetId(), disposalRequest.getRequestInfo());
        final List<AssetStatus> assetStatuses = assetMasterService.getStatuses(AssetStatusObjectName.ASSETMASTER,
                Status.DISPOSED, tenantId);
        asset.setStatus(assetStatuses.get(0).getStatusValues().get(0).getCode());
        final AssetRequest assetRequest = AssetRequest.builder().asset(asset)
                .requestInfo(disposalRequest.getRequestInfo()).build();
        assetService.update(assetRequest);
    }

    public DisposalResponse createAsync(final DisposalRequest disposalRequest, final HttpHeaders headers) {
        final Disposal disposal = disposalRequest.getDisposal();

        disposal.setId(assetCommonService.getNextId(Sequence.DISPOSALSEQUENCE));

        if (disposal.getAuditDetails() == null)
            disposal.setAuditDetails(assetCommonService.getAuditDetails(disposalRequest.getRequestInfo()));
        if (assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                disposal.getTenantId()))
            try {
                log.info("Commencing Voucher Generation for Asset Sale/Disposal");
                final String voucherNumber = createVoucherForDisposal(disposalRequest, headers);
                if (StringUtils.isNotBlank(voucherNumber))
                    disposal.setProfitLossVoucherReference(voucherNumber);
            } catch (final Exception e) {
                throw new RuntimeException("Voucher Generation is failed due to :" + e.getMessage());
            }

        logAwareKafkaTemplate.send(applicationProperties.getCreateAssetDisposalTopicName(),
                KafkaTopicName.SAVEDISPOSAL.toString(), disposalRequest);

        final List<Disposal> disposals = new ArrayList<Disposal>();
        disposals.add(disposal);
        return getResponse(disposals, disposalRequest.getRequestInfo());
    }

    public String createVoucherForDisposal(final DisposalRequest disposalRequest, final HttpHeaders headers) {
        final Disposal disposal = disposalRequest.getDisposal();
        final RequestInfo requestInfo = disposalRequest.getRequestInfo();
        final List<Long> assetIds = new ArrayList<>();
        final String tenantId = disposal.getTenantId();
        assetIds.add(disposal.getAssetId());
        final Asset asset = assetRepository
                .findForCriteria(AssetCriteria.builder().tenantId(tenantId).id(assetIds).build()).get(0);

        final AssetCategory assetCategory = asset.getAssetCategory();

        final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = voucherService
                .getSubledgerDetails(requestInfo, tenantId, assetCategory.getAssetAccount());
        final List<ChartOfAccountDetailContract> subledgerDetailsForAssetSaleAccount = voucherService
                .getSubledgerDetails(requestInfo, tenantId, disposal.getAssetSaleAccount());
        voucherService.validateSubLedgerDetails(subledgerDetailsForAssetAccount, subledgerDetailsForAssetSaleAccount);

        final List<VoucherAccountCodeDetails> accountCodeDetails = getAccountDetails(disposal, assetCategory,
                requestInfo);
        log.debug("Voucher Create Account Code Details :: " + accountCodeDetails);

        final VoucherRequest voucherRequest = voucherService.createDisposalVoucherRequest(disposal, asset.getId(),
                asset.getDepartment().getId(), accountCodeDetails, headers);

        log.debug("Voucher Request for Disposal :: " + voucherRequest);

        return voucherService.createVoucher(voucherRequest, tenantId, headers);

    }

    private List<VoucherAccountCodeDetails> getAccountDetails(final Disposal disposal,
            final AssetCategory assetCategory, final RequestInfo requestInfo) {
        final List<VoucherAccountCodeDetails> accountCodeDetails = new ArrayList<VoucherAccountCodeDetails>();
        final String tenantId = disposal.getTenantId();
        accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, disposal.getAssetSaleAccount(),
                disposal.getSaleValue(), false, true));
        accountCodeDetails.add(voucherService.getGlCodes(requestInfo, tenantId, assetCategory.getAssetAccount(),
                disposal.getSaleValue(), true, false));
        return accountCodeDetails;
    }

    public DisposalResponse getResponse(final List<Disposal> disposals, final RequestInfo requestInfo) {
        final DisposalResponse disposalResponse = new DisposalResponse();
        disposalResponse.setDisposals(disposals);
        disposalResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
        return disposalResponse;
    }

}
