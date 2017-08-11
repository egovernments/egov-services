package org.egov.asset.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.DepreciationDetail;
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
	private ApplicationProperties applicationProperties;

	@Override
	public void depreciateAsset(DepreciationRequest depreciationRequest,List<CalculationAssetDetails> calculationAssetDetailList,
			Map<Long, CalculationCurrentValue> calculationCurrentValues, Map<Long, BigDecimal> depreciationSumMap,
			List<AssetCurrentValue> assetCurrentValues, List<DepreciationDetail> depreciationDetails) {

		BigDecimal depreciationSum = BigDecimal.ZERO;
		
		for (CalculationAssetDetails calculationAssetDetail : calculationAssetDetailList) {
			log.info("inside the loop for calculation asset details");
			Long assetId = calculationAssetDetail.getAssetId();
			
		//  if written down value method add sum of deprecition to calculation---a-Plus-B-Plus-c
		/*	if (calculationAssetDetail.getDepreciationMethod().equals(DepreciationMethod.STRAIGHT_LINE_METHOD)) 
				log.info("the depreciation method is " + DepreciationMethod.STRAIGHT_LINE_METHOD);*/
			
			Double depreciationRate = getDepreciationRate(calculationAssetDetail);
			if (depreciationRate == null) {
				depreciationDetails.add(DepreciationDetail.builder().assetId(assetId).status(DepreciationStatus.FAIL)
						.reasonForFailure(ReasonForFailure.DEPRECIATION_RATE_NOT_FOUND).build());
				continue;
			}
			
			CalculationCurrentValue calculationCurrentValue = calculationCurrentValues.get(assetId);
	   //	if (calculationAssetDetail.getGrossValue() != null) 
			DepreciationDetail depreciationDetail = computeDepreciationDetail(calculationAssetDetail,
					calculationCurrentValue, depreciationRate, depreciationSum);	
			depreciationDetail.setAssetId(assetId);
			if(depreciationDetail.getStatus().equals(DepreciationStatus.SUCCESS)){
			depreciationDetail.setDepreciationRate(depreciationRate);
			depreciationDetail.setStatus(DepreciationStatus.SUCCESS);	
			assetCurrentValues.add(AssetCurrentValue.builder().assetId(assetId).assetTranType(
					TransactionType.DEPRECIATION).currentAmount(depreciationDetail.getValueAfterDepreciation()).build());
			}
			depreciationDetails.add(depreciationDetail);
		}
	}

	/**
	 * The primary aim of this method is to compute 3 values namely currVal, newCurrVal 
	 * and amountToBedepreciated. These 3 values are set into a DepreciationDetail object and returned.
	 * This method can be overridden with different formula implementations when the need arises.
	 * @param calculationAssetDetail
	 * @param calculationCurrentValue
	 * @param depreciationRate
	 * @param depreciationSum 
	 * @return
	 */
	private DepreciationDetail computeDepreciationDetail(CalculationAssetDetails calculationAssetDetail,
			CalculationCurrentValue calculationCurrentValue, Double depreciationRate, BigDecimal depreciationSum) {
		BigDecimal newCurrVal = null;
		BigDecimal currVal = null;
		BigDecimal amountToBedepreciated = null;
		BigDecimal aPlusB = null;
		BigDecimal c = null;
		
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
		
		Double minValue = Double.parseDouble(applicationProperties.getDepreciaitionMinimumValue());
		if (currVal.doubleValue() == Double.parseDouble(applicationProperties.getDepreciaitionCapitalizedValue())) {
			return DepreciationDetail.builder().status(DepreciationStatus.FAIL)
					.reasonForFailure(ReasonForFailure.CAPITALISED_VALUE_IS_ALREADY_MINIMUM).build();
		} else if (minValue != null && currVal.doubleValue() < minValue) {
				return DepreciationDetail.builder().status(DepreciationStatus.FAIL)
						.reasonForFailure(ReasonForFailure.ASSET_IS_FULLY_DEPRECIATED_IN_YEAR_OF_PURCHASE).build();
		}
		c = currVal.subtract(aPlusB);
		double cFactor = Double.parseDouble(applicationProperties.getDepreciaitionFactorForC());
		amountToBedepreciated = aPlusB.add(c.multiply(BigDecimal.valueOf(cFactor))).add(depreciationSum)
				.multiply(BigDecimal.valueOf(depreciationRate / 100));
		newCurrVal = currVal.subtract(amountToBedepreciated);
		return DepreciationDetail.builder().valueBeforeDepreciation(currVal).depreciationValue(amountToBedepreciated)
				.valueAfterDepreciation(newCurrVal).status(DepreciationStatus.SUCCESS).build();
	}

	private Double getDepreciationRate(CalculationAssetDetails calculationAssetDetail) {
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
