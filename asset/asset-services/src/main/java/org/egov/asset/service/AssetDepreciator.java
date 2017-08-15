package org.egov.asset.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.egov.asset.contract.DepreciationRequest;
import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.domain.CalculationCurrentValue;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.DepreciationDetail;
import org.springframework.stereotype.Component;

@Component
public interface AssetDepreciator {

    public void depreciateAsset(DepreciationRequest depreciationRequest,
            List<CalculationAssetDetails> calculationAssetDetailList,
            Map<Long, CalculationCurrentValue> calculationCurrentValues, Map<Long, BigDecimal> depreciationSumMap,
            List<AssetCurrentValue> assetCurrentValues, Map<Long, DepreciationDetail> depreciationDetailsMap);

}
