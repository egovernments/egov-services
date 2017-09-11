package org.egov.asset.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.model.enums.ReasonForFailure;
import org.egov.asset.model.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AssetDepreciatorImpl implements AssetDepreciator {

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    @Override
    public void depreciateAsset(final DepreciationRequest depreciationRequest,
            final List<CalculationAssetDetails> calculationAssetDetailList,
            final Map<Long, CalculationCurrentValue> calculationCurrentValues,
            final Map<Long, BigDecimal> depreciationSumMap, final List<AssetCurrentValue> assetCurrentValues,
            final Map<Long, DepreciationDetail> depreciationDetailsMap) {

        final BigDecimal depreciationSum = BigDecimal.ZERO;

        for (final CalculationAssetDetails calculationAssetDetail : calculationAssetDetailList) {
            log.info("inside the loop for calculation asset details");
            final Long assetId = calculationAssetDetail.getAssetId();

            // if written down value method add sum of deprecition to
            // calculation---a-Plus-B-Plus-c
            /*
             * if (calculationAssetDetail.getDepreciationMethod().equals(
             * DepreciationMethod.STRAIGHT_LINE_METHOD)) log.info(
             * "the depreciation method is " +
             * DepreciationMethod.STRAIGHT_LINE_METHOD);
             */

            final Double depreciationRate = getDepreciationRate(calculationAssetDetail);
            if (depreciationRate == null) {
                depreciationDetailsMap.put(assetId,
                        DepreciationDetail.builder().assetId(assetId).status(DepreciationStatus.FAIL)
                                .reasonForFailure(ReasonForFailure.DEPRECIATION_RATE_NOT_FOUND).build());
                continue;
            }

            final CalculationCurrentValue calculationCurrentValue = calculationCurrentValues.get(assetId);
            // if (calculationAssetDetail.getGrossValue() != null)
            final DepreciationDetail depreciationDetail = computeDepreciationDetail(calculationAssetDetail,
                    calculationCurrentValue, depreciationRate, depreciationSum);
            depreciationDetail.setAssetId(assetId);
            if (depreciationDetail.getStatus().equals(DepreciationStatus.SUCCESS)) {
                depreciationDetail.setDepreciationRate(depreciationRate);
                depreciationDetail.setStatus(DepreciationStatus.SUCCESS);
                assetCurrentValues
                        .add(AssetCurrentValue.builder().assetId(assetId).assetTranType(TransactionType.DEPRECIATION)
                                .currentAmount(depreciationDetail.getValueAfterDepreciation()).build());
            }
            depreciationDetailsMap.put(assetId, depreciationDetail);
        }
    }

    /**
     * The primary aim of this method is to compute 3 values namely currVal,
     * newCurrVal and amountToBedepreciated. These 3 values are set into a
     * DepreciationDetail object and returned. This method can be overridden
     * with different formula implementations when the need arises.
     *
     * @param calculationAssetDetail
     * @param calculationCurrentValue
     * @param depreciationRate
     * @param depreciationSum
     * @return
     */
    public DepreciationDetail computeDepreciationDetail(final CalculationAssetDetails calculationAssetDetail,
            final CalculationCurrentValue calculationCurrentValue, final Double depreciationRate,
            final BigDecimal depreciationSum) {
        final String tenantId = calculationCurrentValue.getTenantId();
        BigDecimal newCurrVal = null;
        BigDecimal currVal = null;
        BigDecimal amountToBedepreciated = null;
        BigDecimal aPlusB = null;
        BigDecimal c = null;

        if (calculationAssetDetail.getAccumulatedDepreciation() != null)
            currVal = aPlusB = calculationAssetDetail.getGrossValue()
                    .subtract(calculationAssetDetail.getAccumulatedDepreciation());
        else
            currVal = aPlusB = calculationAssetDetail.getGrossValue();
        if (calculationCurrentValue != null) {

            if (calculationCurrentValue.getCurrentAmountBeforeSeptember() != null)
                currVal = aPlusB = calculationCurrentValue.getCurrentAmountBeforeSeptember();
            if (calculationCurrentValue.getCurrentAmountAfterSeptember() != null)
                currVal = calculationCurrentValue.getCurrentAmountAfterSeptember();
        }

        final Double minValue = Double.parseDouble(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETMINIMUMVALUE, tenantId));
        
        if (currVal.doubleValue() == Double.parseDouble(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETDEFAULTCAPITALIZEDVALUE, tenantId)))
            
            return DepreciationDetail.builder().status(DepreciationStatus.FAIL).valueAfterDepreciation(currVal)
                    .reasonForFailure(ReasonForFailure.CAPITALISED_VALUE_IS_ALREADY_MINIMUM).build();
        
        else if (minValue != null && currVal.doubleValue() < minValue)
            return DepreciationDetail.builder().status(DepreciationStatus.FAIL).valueAfterDepreciation(currVal)
                    .reasonForFailure(ReasonForFailure.ASSET_IS_FULLY_DEPRECIATED_IN_YEAR_OF_PURCHASE).build();
        
        c = currVal.subtract(aPlusB);
        final double cFactor = Double.parseDouble(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.DEPRECIATIONCFACTOR, tenantId));
        
        amountToBedepreciated = aPlusB.add(c.multiply(BigDecimal.valueOf(cFactor))).add(depreciationSum)
                .multiply(BigDecimal.valueOf(depreciationRate / 100));
        
        newCurrVal = currVal.subtract(amountToBedepreciated);
        return DepreciationDetail.builder().valueBeforeDepreciation(currVal).depreciationValue(amountToBedepreciated)
                .valueAfterDepreciation(newCurrVal).status(DepreciationStatus.SUCCESS).build();
    }

    public Double getDepreciationRate(final CalculationAssetDetails calculationAssetDetail) {
        Double depreciationRate = null;
        if (calculationAssetDetail.getYearwisedepreciationrate() != null)
            depreciationRate = calculationAssetDetail.getYearwisedepreciationrate();
        else if (calculationAssetDetail.getAssetDepreciationRate() != null)
            depreciationRate = calculationAssetDetail.getAssetDepreciationRate();
        else if (calculationAssetDetail.getAssetCategoryDepreciationRate() != null)
            depreciationRate = calculationAssetDetail.getAssetCategoryDepreciationRate();
        return depreciationRate;
    }
}
