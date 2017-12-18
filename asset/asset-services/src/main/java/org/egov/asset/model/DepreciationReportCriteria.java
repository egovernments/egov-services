package org.egov.asset.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class DepreciationReportCriteria {

    private Long id;

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("assetCategoryType")
    private String assetCategoryType;

    private Long parent;

    private String departmentName;
    private Long department;

    @JsonProperty("assetCategoryName")
    private String assetCategoryName;

    @JsonProperty("assetName")
    private String assetName;

    @JsonProperty("assetCode")
    private String assetCode;

    private String financialYear;

    private Long assetId;

    private BigDecimal grossValue;

    private BigDecimal originalValue;

    @JsonProperty("valueAfterDepreciation")
    private BigDecimal valueAfterDepreciation;
    private Long assetCategory;

    private BigDecimal depreciationValue;

    private Double depreciationRate;

    private BigDecimal currentValue;

}
