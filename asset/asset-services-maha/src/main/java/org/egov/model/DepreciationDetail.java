package org.egov.model;

import java.math.BigDecimal;

import org.egov.model.enums.DepreciationStatus;
import org.egov.model.enums.ReasonForFailure;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepreciationDetail {

    @JsonProperty("id")
    private Long id = null;
    
    private String assetCode;

    @JsonProperty("assetId")
    private Long assetId = null;
    
    @JsonProperty("fromDate")
    private Long fromDate;

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
