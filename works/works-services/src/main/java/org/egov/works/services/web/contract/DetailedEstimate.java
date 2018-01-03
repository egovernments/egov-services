package org.egov.works.services.web.contract;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedEstimate {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("estimateNumber")
    private String estimateNumber = null;

    @JsonProperty("estimateDate")
    private Long estimateDate = null;

    @JsonProperty("nameOfWork")
    private String nameOfWork = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("adminSanctionNumber")
    private String adminSanctionNumber = null;

    @JsonProperty("adminSanctionDate")
    private Long adminSanctionDate = null;

    @JsonProperty("workValue")
    private BigDecimal workValue = null;

    @JsonProperty("estimateValue")
    private BigDecimal estimateValue = null;

    @JsonProperty("parent")
    private String parent = null;

    @JsonProperty("copiedFrom")
    private String copiedFrom = null;

    @JsonProperty("approvedDate")
    private Long approvedDate = null;

    @JsonProperty("copiedEstimate")
    private Boolean copiedEstimate = false;

    @JsonProperty("location")
    private String location = null;

    @JsonProperty("latitude")
    private Double latitude = null;

    @JsonProperty("longitude")
    private Double longitude = null;

    @JsonProperty("councilResolutionNumber")
    private String councilResolutionNumber = null;

    @JsonProperty("councilResolutionDate")
    private Long councilResolutionDate = null;

    @JsonProperty("workOrderCreated")
    private Boolean workOrderCreated = false;

    @JsonProperty("billsCreated")
    private Boolean billsCreated = false;

    @JsonProperty("spillOverFlag")
    private Boolean spillOverFlag = false;

    @JsonProperty("grossAmountBilled")
    private BigDecimal grossAmountBilled = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    @JsonProperty("totalIncludingRE")
    private BigDecimal totalIncludingRE = null;

    @JsonProperty("documentDetails")
    private List<DocumentDetail> documentDetails = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

}
