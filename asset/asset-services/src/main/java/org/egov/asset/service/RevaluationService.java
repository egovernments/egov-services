package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.contract.VoucherRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.ChartOfAccountDetailContract;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.VouchercreateAccountCodeDetails;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.RevaluationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RevaluationService {

    @Autowired
    private RevaluationRepository revaluationRepository;

    @Autowired
    private AssetProducer assetProducer;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AssetCurrentAmountService assetCurrentAmountService;

    @Autowired
    private VoucherService voucherService;

    private static final Logger logger = LoggerFactory.getLogger(RevaluationService.class);

    public RevaluationResponse createAsync(final RevaluationRequest revaluationRequest) {

        logger.debug("RevaluationService createAsync revaluationRequest:" + revaluationRequest);

        revaluationRequest.getRevaluation()
                .setId(Long.valueOf(revaluationRepository.getNextRevaluationId().longValue()));

        if (revaluationRequest.getRevaluation().getAuditDetails() == null)
            revaluationRequest.getRevaluation()
                    .setAuditDetails(assetCurrentAmountService.getAuditDetails(revaluationRequest.getRequestInfo()));

        // FIXME uncomment the code once voucher services are up in micro-dev
        // environment.
        if (applicationProperties.getEnableVoucherGenration())
            try {
                logger.info("Commencing Voucher Generation for Asset Revaluation");
                final Long voucherId = createVoucherForRevaluation(revaluationRequest);
                if (voucherId != null)
                    revaluationRequest.getRevaluation().setVoucherReference(voucherId);
            } catch (final Exception e) {
                throw new RuntimeException("Voucher Generation is failed due to :" + e.getMessage());
            }
        String json = null;

        try {
            json = objectMapper.writeValueAsString(revaluationRequest);
        } catch (final JsonProcessingException ex) {
            logger.info("RevaluationService createAsync catch block:" + ex.getMessage());
        }

        // Send data to kafka for persist into db
        try {
            if (json != null)
                assetProducer.sendMessage(applicationProperties.getCreateAssetRevaluationTopicName(),
                        "save-revaluation", json);
        } catch (final Exception ex) {
            logger.info("RevaluationService send kafka createAsync:" + ex.getMessage());
        }

        final List<Revaluation> revaluations = new ArrayList<Revaluation>();
        revaluations.add(revaluationRequest.getRevaluation());
        return getRevaluationResponse(revaluations);
    }

    public void create(final RevaluationRequest revaluationRequest) {
        revaluationRepository.create(revaluationRequest);
    }

    public RevaluationResponse search(final RevaluationCriteria revaluationCriteria) {
        List<Revaluation> revaluations = null;
        try {
            revaluations = revaluationRepository.search(revaluationCriteria);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return getRevaluationResponse(revaluations);
    }

    private Long createVoucherForRevaluation(final RevaluationRequest revaluationRequest) {
        final Asset asset = assetCurrentAmountService.getAsset(revaluationRequest.getRevaluation().getAssetId(),
                revaluationRequest.getRevaluation().getTenantId(), revaluationRequest.getRequestInfo());

        final AssetCategory assetCategory = asset.getAssetCategory();

        if (revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.INCREASED)) {
            final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = voucherService
                    .getSubledgerDetails(revaluationRequest.getRequestInfo(),
                            revaluationRequest.getRevaluation().getTenantId(), assetCategory.getAssetAccount());
            final List<ChartOfAccountDetailContract> subledgerDetailsForRevaluationReserverAccount = voucherService
                    .getSubledgerDetails(revaluationRequest.getRequestInfo(),
                            revaluationRequest.getRevaluation().getTenantId(),
                            assetCategory.getRevaluationReserveAccount());

            if (subledgerDetailsForAssetAccount != null && subledgerDetailsForRevaluationReserverAccount != null
                    && !subledgerDetailsForAssetAccount.isEmpty()
                    && !subledgerDetailsForRevaluationReserverAccount.isEmpty())
                throw new RuntimeException("Subledger Details Should not be present for Chart Of Accounts");

        } else if (revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.DECREASED)) {
            final List<ChartOfAccountDetailContract> subledgerDetailsForAssetAccount = voucherService
                    .getSubledgerDetails(revaluationRequest.getRequestInfo(),
                            revaluationRequest.getRevaluation().getTenantId(), assetCategory.getAssetAccount());
            final List<ChartOfAccountDetailContract> subledgerDetailsForFixedAssetWrittenOffAccount = voucherService
                    .getSubledgerDetails(revaluationRequest.getRequestInfo(),
                            revaluationRequest.getRevaluation().getTenantId(),
                            revaluationRequest.getRevaluation().getFixedAssetsWrittenOffAccount());

            if (subledgerDetailsForAssetAccount != null && subledgerDetailsForFixedAssetWrittenOffAccount != null
                    && !subledgerDetailsForAssetAccount.isEmpty()
                    && !subledgerDetailsForFixedAssetWrittenOffAccount.isEmpty())
                throw new RuntimeException("Subledger Details Should not be present for Chart Of Accounts");
        }
        final List<VouchercreateAccountCodeDetails> accountCodeDetails = getAccountDetails(revaluationRequest,
                assetCategory);
        
        logger.debug("Voucher Create Account Code Details :: " + accountCodeDetails);

        final VoucherRequest voucherRequest = voucherService.createVoucherRequestForRevalaution(revaluationRequest,
                asset, accountCodeDetails);
        logger.debug("Voucher Request for Revaluation :: " + voucherRequest);

        return voucherService.createVoucher(voucherRequest, revaluationRequest.getRevaluation().getTenantId());

    }

    private List<VouchercreateAccountCodeDetails> getAccountDetails(final RevaluationRequest revaluationRequest,
            final AssetCategory assetCategory) {
        final List<VouchercreateAccountCodeDetails> accountCodeDetails = new ArrayList<VouchercreateAccountCodeDetails>();
        if (assetCategory != null
                && revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.INCREASED)) {
            accountCodeDetails.add(voucherService.getGlCodes(revaluationRequest.getRequestInfo(),
                    revaluationRequest.getRevaluation().getTenantId(), assetCategory.getAssetAccount(),
                    revaluationRequest.getRevaluation().getRevaluationAmount(),
                    revaluationRequest.getRevaluation().getFunction(), false, true));
            accountCodeDetails.add(voucherService.getGlCodes(revaluationRequest.getRequestInfo(),
                    revaluationRequest.getRevaluation().getTenantId(), assetCategory.getRevaluationReserveAccount(),
                    revaluationRequest.getRevaluation().getRevaluationAmount(),
                    revaluationRequest.getRevaluation().getFunction(), true, false));
        } else if (assetCategory != null
                && revaluationRequest.getRevaluation().getTypeOfChange().equals(TypeOfChangeEnum.DECREASED)) {
            accountCodeDetails.add(voucherService.getGlCodes(revaluationRequest.getRequestInfo(),
                    revaluationRequest.getRevaluation().getTenantId(),
                    revaluationRequest.getRevaluation().getFixedAssetsWrittenOffAccount(),
                    revaluationRequest.getRevaluation().getRevaluationAmount(),
                    revaluationRequest.getRevaluation().getFunction(), false, true));
            accountCodeDetails.add(voucherService.getGlCodes(revaluationRequest.getRequestInfo(),
                    revaluationRequest.getRevaluation().getTenantId(), assetCategory.getAssetAccount(),
                    revaluationRequest.getRevaluation().getRevaluationAmount(),
                    revaluationRequest.getRevaluation().getFunction(), true, false));

        }
        return accountCodeDetails;
    }

    private RevaluationResponse getRevaluationResponse(final List<Revaluation> revaluations) {
        final RevaluationResponse revaluationResponse = new RevaluationResponse();
        revaluationResponse.setRevaluations(revaluations);
        return revaluationResponse;
    }

}
