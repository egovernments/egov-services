package org.egov.asset.model;

import java.math.BigDecimal;

import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.model.enums.ReasonForFailure;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DepreciationDetail {

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("assetId")
    private Long assetId = null;

    @JsonProperty("status")
    private DepreciationStatus status = null;

    @JsonProperty("depreciationRate")
    private Double depreciationRate = null;

    @JsonProperty("valueBeforeDepreciation")
    private BigDecimal valueBeforeDepreciation = null;

    @JsonProperty("depreciationValue")
    private BigDecimal depreciationValue = null;

    @JsonProperty("valueAfterDepreciation")
    private BigDecimal valueAfterDepreciation = null;

    private String voucherReference;

    private ReasonForFailure reasonForFailure;
}
