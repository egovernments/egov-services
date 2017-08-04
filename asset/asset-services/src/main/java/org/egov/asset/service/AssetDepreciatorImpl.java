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

			CalculationCurrentValue currentValue = calculationCurrentValues.get(assetId);

			if (status != DepreciationStatus.FAIL && calculationAssetDetail.getGrossValue()!=null) {
				
				BigDecimal aPlusB = calculationAssetDetail.getGrossValue();
				BigDecimal c = BigDecimal.ZERO;
				
				if (currentValue != null) {
						if (currentValue.getCurrentAmountBeforeSeptember() != null) {
								aPlusB = currentValue.getCurrentAmountBeforeSeptember();
								currVal = currentValue.getCurrentAmountBeforeSeptember();
						}if (currentValue.getCurrentAmountAfterSeptember() != null) {
								currVal = currentValue.getCurrentAmountAfterSeptember();
								c = currentValue.getCurrentAmountAfterSeptember().subtract(aPlusB);
					}
				} else {
						if (calculationAssetDetail.getAccumulatedDepreciation() != null)
								currVal = calculationAssetDetail.getGrossValue()
										.subtract(calculationAssetDetail.getAccumulatedDepreciation());
						else
								currVal = calculationAssetDetail.getGrossValue();
				}
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
