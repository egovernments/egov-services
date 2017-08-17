package org.egov.asset.domain;

import java.math.BigDecimal;

import org.egov.asset.model.enums.DepreciationMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculationAssetDetails {

    private Long assetId;

    private BigDecimal grossValue;

    private BigDecimal accumulatedDepreciation;

    private Boolean enableYearWiseDepreciation;

    private Long assetCategoryId;

    private Long departmentId;

    private DepreciationMethod depreciationMethod;

    private Long assetReference;

    private String assetCategoryName;

    private Double assetDepreciationRate;

    private Double assetCategoryDepreciationRate;

    private Double yearwisedepreciationrate;

    private String financialyear;

    private Long accumulatedDepreciationAccount;

    private Long depreciationExpenseAccount;
}
