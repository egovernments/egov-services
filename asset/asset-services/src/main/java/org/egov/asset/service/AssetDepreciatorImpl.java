package org.egov.asset.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.model.enums.ReasonForFailure;
import org.egov.asset.model.enums.TransactionType;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AssetDepreciatorImpl implements AssetDepreciator {

	@Override
	public void depreciateAsset(DepreciationRequest depreciationRequest,List<CalculationAssetDetails> calculationAssetDetailList,
			Map<Long, CalculationCurrentValue> calculationCurrentValues, Map<Long, BigDecimal> depreciationSumMap,
			List<AssetCurrentValue> assetCurrentValues, List<DepreciationDetail> depreciationDetails) {

		BigDecimal depreciationSum = BigDecimal.ZERO;
		DepreciationStatus status = DepreciationStatus.SUCCESS;
		Double depreciationRate = null;
		BigDecimal newCurrVal = null;
		BigDecimal currVal = BigDecimal.ONE;
		BigDecimal amountToBedepreciated = BigDecimal.ZERO;
		boolean addCurrentValue = false;
		ReasonForFailure reasonForFailure = null;
		BigDecimal aPlusB = null;
		BigDecimal c = null;
		
		
		for (CalculationAssetDetails calculationAssetDetail : calculationAssetDetailList) {

			Long assetId = calculationAssetDetail.getAssetId();
			
		/*	if (calculationAssetDetail.getDepreciationMethod().equals(DepreciationMethod.STRAIGHT_LINE_METHOD)) 
				log.info("the depreciation method is " + DepreciationMethod.STRAIGHT_LINE_METHOD);
			// if written down value method add sum of deprecition to calculation---a-Plus-B-Plus-c
*/
			if (calculationAssetDetail.getYearwisedepreciationrate() != null)
				depreciationRate = calculationAssetDetail.getYearwisedepreciationrate();
			else if (calculationAssetDetail.getAssetDepreciationRate() != null)
				depreciationRate = calculationAssetDetail.getAssetDepreciationRate();
			else if (calculationAssetDetail.getAssetCategoryDepreciationRate() != null)
				depreciationRate = calculationAssetDetail.getAssetCategoryDepreciationRate();
			else {
				status = DepreciationStatus.FAIL;
				reasonForFailure = ReasonForFailure.DEPRECIATION_RATE_NOT_FOUND;
			}

			CalculationCurrentValue calculationCurrentValue = calculationCurrentValues.get(assetId);

			if (status != DepreciationStatus.FAIL && calculationAssetDetail.getGrossValue()!=null) {
				
				if (calculationAssetDetail.getAccumulatedDepreciation() != null) {
					currVal = aPlusB = calculationAssetDetail.getGrossValue()
							.subtract(calculationAssetDetail.getAccumulatedDepreciation());
				} else {
					currVal = aPlusB = calculationAssetDetail.getGrossValue();
				}
				if (calculationCurrentValue != null) {

					if (calculationCurrentValue.getCurrentAmountBeforeSeptember() != null) {
						currVal = aPlusB = calculationCurrentValue.getCurrentAmountBeforeSeptember();
					}
					if (calculationCurrentValue.getCurrentAmountAfterSeptember() != null) {
						currVal = calculationCurrentValue.getCurrentAmountAfterSeptember();
					}
				}
				c = currVal.subtract(aPlusB);
				// TODO get 0.5 from app.props
				amountToBedepreciated = aPlusB.add(c.multiply(BigDecimal.valueOf(0.5))).add(depreciationSum)
						.multiply(BigDecimal.valueOf(depreciationRate / 100));
				newCurrVal = currVal.subtract(amountToBedepreciated);
				addCurrentValue=true;
			}
			depreciationDetails.add(DepreciationDetail.builder().assetId(assetId).depreciationRate(depreciationRate)
					.status(status).depreciationValue(amountToBedepreciated).valueAfterDepreciation(newCurrVal)
					.valueBeforeDepreciation(currVal).reasonForFailure(reasonForFailure).build());
			if (addCurrentValue) {
				assetCurrentValues.add(AssetCurrentValue.builder().assetId(assetId)
						.assetTranType(TransactionType.DEPRECIATION).currentAmount(newCurrVal).build());
				addCurrentValue = false;
			}
		}
	}
}
