package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.AssetStatusObjectName;
import org.egov.asset.model.enums.KafkaTopicName;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.DisposalRepository;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisposalService {

    public static final Logger LOGGER = LoggerFactory.getLogger(DisposalService.class);

    @Autowired
    private DisposalRepository disposalRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private AssetCurrentAmountService assetCurrentAmountService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetMasterService assetMasterService;

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    public DisposalResponse search(final DisposalCriteria disposalCriteria, final RequestInfo requestInfo) {
        List<Disposal> disposals = null;

        try {
            disposals = disposalRepository.search(disposalCriteria);
        } catch (final Exception ex) {
            LOGGER.info("DisposalService:", ex);
            throw new RuntimeException(ex);
        }
        return getResponse(disposals, requestInfo);
    }

    public void create(final DisposalRequest disposalRequest) {
        disposalRepository.create(disposalRequest);
        setStatusOfAssetToDisposed(disposalRequest);
    }

    public void setStatusOfAssetToDisposed(final DisposalRequest disposalRequest) {
        final Asset asset = assetCurrentAmountService.getAsset(disposalRequest.getDisposal().getAssetId(),
                disposalRequest.getDisposal().getTenantId(), disposalRequest.getRequestInfo());
        final List<AssetStatus> assetStatuses = assetMasterService.getStatuses(AssetStatusObjectName.ASSETMASTER,
                Status.DISPOSED, disposalRequest.getDisposal().getTenantId());
        asset.setStatus(assetStatuses.get(0).getStatusValues().get(0).getCode());
        final AssetRequest assetRequest = AssetRequest.builder().asset(asset)
                .requestInfo(disposalRequest.getRequestInfo()).build();
        assetService.update(assetRequest);
    }

    public DisposalResponse createAsync(final DisposalRequest disposalRequest) {

        disposalRequest.getDisposal().setId(Long.valueOf(disposalRepository.getNextDisposalId().longValue()));

        if (disposalRequest.getDisposal().getAuditDetails() == null)
            disposalRequest.getDisposal()
                    .setAuditDetails(assetCurrentAmountService.getAuditDetails(disposalRequest.getRequestInfo()));
        if (assetConfigurationService.getEnabledVoucherGeneration(AssetConfigurationKeys.ENABLEVOUCHERGENERATION,
                disposalRequest.getDisposal().getTenantId()))
            try {
                LOGGER.info("Commencing Voucher Generation for Asset Sale/Disposal");
                final Long voucherId = createVoucherForDisposal(disposalRequest);
                if (voucherId != null)
                    disposalRequest.getDisposal().setVoucherReference(voucherId);
            } catch (final Exception e) {
                throw new RuntimeException("Voucher Generation is failed due to :" + e.getMessage());
            }

        logAwareKafkaTemplate.send(applicationProperties.getCreateAssetDisposalTopicName(),
                KafkaTopicName.SAVEDISPOSAL.toString(), disposalRequest);
        final List<Disposal> disposals = new ArrayList<Disposal>();
        disposals.add(disposalRequest.getDisposal());
        return getResponse(disposals, disposalRequest.getRequestInfo());
    }

    private Long createVoucherForDisposal(final DisposalRequest disposalRequest) {
        final Asset asset = assetCurrentAmountService.getAsset(disposalRequest.getDisposal().getAssetId(),
                disposalRequest.getDisposal().getTenantId(), disposalRequest.getRequestInfo());

        final AssetCategory assetCategory = asset.getAssetCategory();

        final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = voucherService.getSubledgerDetails(
                disposalRequest.getRequestInfo(), disposalRequest.getDisposal().getTenantId(),
                assetCategory.getAssetAccount());
        final List<ChartOfAccountDetailContract> subledgerDetailsForAssetSaleAccount = voucherService
                .getSubledgerDetails(disposalRequest.getRequestInfo(), disposalRequest.getDisposal().getTenantId(),
                        disposalRequest.getDisposal().getAssetSaleAccount());

        if (subledgerDetailsForAssetAccount != null && subledgerDetailsForAssetSaleAccount != null
                && !subledgerDetailsForAssetAccount.isEmpty() && !subledgerDetailsForAssetSaleAccount.isEmpty())
            throw new RuntimeException("Subledger Details Should not be present for Chart Of Accounts");
        else {
            final List<VouchercreateAccountCodeDetails> accountCodeDetails = getAccountDetails(disposalRequest,
                    assetCategory);
            LOGGER.debug("Voucher Create Account Code Details :: " + accountCodeDetails);

            final VoucherRequest voucherRequest = voucherService.createVoucherRequestForDisposal(disposalRequest, asset,
                    accountCodeDetails);

            LOGGER.debug("Voucher Request for Disposal :: " + voucherRequest);

            return voucherService.createVoucher(voucherRequest, disposalRequest.getDisposal().getTenantId());
        }

    }

    private List<VouchercreateAccountCodeDetails> getAccountDetails(final DisposalRequest disposalRequest,
            final AssetCategory assetCategory) {
        final List<VouchercreateAccountCodeDetails> accountCodeDetails = new ArrayList<VouchercreateAccountCodeDetails>();
        accountCodeDetails.add(voucherService.getGlCodes(disposalRequest.getRequestInfo(),
                disposalRequest.getDisposal().getTenantId(), disposalRequest.getDisposal().getAssetSaleAccount(),
                disposalRequest.getDisposal().getSaleValue(), disposalRequest.getDisposal().getFunction(), false,
                true));
        accountCodeDetails.add(
                voucherService.getGlCodes(disposalRequest.getRequestInfo(), disposalRequest.getDisposal().getTenantId(),
                        assetCategory.getAssetAccount(), disposalRequest.getDisposal().getSaleValue(),
                        disposalRequest.getDisposal().getFunction(), true, false));
        return accountCodeDetails;
    }

    public DisposalResponse getResponse(final List<Disposal> disposals, final RequestInfo requestInfo) {
        final DisposalResponse disposalResponse = new DisposalResponse();
        disposalResponse.setDisposals(disposals);

        return disposalResponse;
    }

}
