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
                    .reasonForFailure(ReasonForFailure.ASSET_IS_FULLY_DEPRECIATED_TO_MINIMUN_VALUE).build();
        
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
