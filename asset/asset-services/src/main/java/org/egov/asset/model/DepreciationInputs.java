package org.egov.asset.model;

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
public class DepreciationInputs {

    private String assetCode;
    
    private String assetName;

    private Long assetId;

    private Long lastDepreciationDate;

    private String tenantId;

    private BigDecimal grossValue;

    private BigDecimal originalValue;

    private BigDecimal accumulatedDepreciation;

    private Long assetCategory;
    
    private String assetCategoryName;

    private Double depreciationRate;

    private BigDecimal currentValue;

    private BigDecimal depreciationSum;

    private Long dateOfCreation;

    private String assetaccount;

    private Long accumulatedDepreciationAccount;

    private Long depreciationExpenseAccount;

    private String revaluationreserveaccount;

    private Boolean enableYearwiseDepreciation;

    private Double yearwiseDepreciationRate;

    private DepreciationMethod depreciationMethod;
    
    private String financialyear;
    
    private Long department;
}
