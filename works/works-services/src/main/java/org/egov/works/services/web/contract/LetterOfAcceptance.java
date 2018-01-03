package org.egov.works.services.web.contract;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetterOfAcceptance {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("loaDate")
    private Long loaDate = null;

    @JsonProperty("loaNumber")
    private String loaNumber = null;

    @JsonProperty("contractPeriod")
    private BigDecimal contractPeriod = null;

    @JsonProperty("emdAmountDeposited")
    private BigDecimal emdAmountDeposited = null;

    @JsonProperty("stampPaperAmount")
    private BigDecimal stampPaperAmount = null;

    @JsonProperty("engineerIncharge")
    private String engineerIncharge = null;

    @JsonProperty("defectLiabilityPeriod")
    private Double defectLiabilityPeriod = null;

    @JsonProperty("loaAmount")
    private BigDecimal loaAmount = null;

    @JsonProperty("tenderFinalizedPercentage")
    private Double tenderFinalizedPercentage = null;

    @JsonProperty("approvedDate")
    private Long approvedDate = null;

    @JsonProperty("fileNumber")
    private String fileNumber = null;

    @JsonProperty("fileDate")
    private Long fileDate = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    @JsonProperty("councilResolutionNumber")
    private String councilResolutionNumber = null;

    @JsonProperty("councilResolutionDate")
    private Long councilResolutionDate = null;

    @JsonProperty("spillOverFlag")
    private Boolean spillOverFlag = false;

}
