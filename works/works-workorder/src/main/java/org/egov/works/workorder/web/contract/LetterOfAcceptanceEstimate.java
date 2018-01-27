package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds the basic data of Letter Of Acceptance Estimate
 */
@ApiModel(description = "An Object that holds the basic data of Letter Of Acceptance Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-01T06:18:18.668Z")

public class LetterOfAcceptanceEstimate {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptance")
    private String letterOfAcceptance = null;

    @JsonProperty("detailedEstimate")
    private DetailedEstimate detailedEstimate = null;

    @JsonProperty("workCompletionDate")
    private Long workCompletionDate = null;

    @JsonProperty("estimateLOAAmount")
    private BigDecimal estimateLOAAmount = null;

    @JsonProperty("assetForLOAs")
    private List<AssetsForLOA> assetForLOAs = null;

    @JsonProperty("loaActivities")
    private List<LOAActivity> loaActivities = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    @JsonProperty("backUpdateDE")
    private Boolean backUpdateDE = false;

    public LetterOfAcceptanceEstimate id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Letter Of Acceptance Estimate.
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Letter Of Acceptance Estimate.")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LetterOfAcceptanceEstimate tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Letter Of Acceptance Estimate
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Letter Of Acceptance Estimate")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public LetterOfAcceptanceEstimate letterOfAcceptance(String letterOfAcceptance) {
        this.letterOfAcceptance = letterOfAcceptance;
        return this;
    }

    /**
     * reference of 'LetterOfAcceptance'. Pimary key is reference here.
     *
     * @return letterOfAcceptance
     **/
    @ApiModelProperty(value = "reference of 'LetterOfAcceptance'. Pimary key is reference here.")


    public String getLetterOfAcceptance() {
        return letterOfAcceptance;
    }

    public void setLetterOfAcceptance(String letterOfAcceptance) {
        this.letterOfAcceptance = letterOfAcceptance;
    }

    public LetterOfAcceptanceEstimate detailedEstimate(DetailedEstimate detailedEstimate) {
        this.detailedEstimate = detailedEstimate;
        return this;
    }

    /**
     * Detailed Estimate reference.
     *
     * @return detailedEstimate
     **/
    @ApiModelProperty(required = true, value = "Detailed Estimate reference.")
    @NotNull

//    @Valid

    public DetailedEstimate getDetailedEstimate() {
        return detailedEstimate;
    }

    public void setDetailedEstimate(DetailedEstimate detailedEstimate) {
        this.detailedEstimate = detailedEstimate;
    }

    public LetterOfAcceptanceEstimate workCompletionDate(Long workCompletionDate) {
        this.workCompletionDate = workCompletionDate;
        return this;
    }

    /**
     * Work Completion Date of Letter Of Acceptance Estimate
     *
     * @return workCompletionDate
     **/
    @ApiModelProperty(value = "Work Completion Date of Letter Of Acceptance Estimate")


    public Long getWorkCompletionDate() {
        return workCompletionDate;
    }

    public void setWorkCompletionDate(Long workCompletionDate) {
        this.workCompletionDate = workCompletionDate;
    }

    public LetterOfAcceptanceEstimate estimateLOAAmount(BigDecimal estimateLOAAmount) {
        this.estimateLOAAmount = estimateLOAAmount;
        return this;
    }

    /**
     * Estimate wise LOA amount of the Letter Of Acceptance
     *
     * @return estimateLOAAmount
     **/
    @ApiModelProperty(value = "Estimate wise LOA amount of the Letter Of Acceptance")

    @Valid

    public BigDecimal getEstimateLOAAmount() {
        return estimateLOAAmount;
    }

    public void setEstimateLOAAmount(BigDecimal estimateLOAAmount) {
        this.estimateLOAAmount = estimateLOAAmount;
    }

    public LetterOfAcceptanceEstimate assetForLOAs(List<AssetsForLOA> assetForLOAs) {
        this.assetForLOAs = assetForLOAs;
        return this;
    }

    public LetterOfAcceptanceEstimate addAssetForLOAsItem(AssetsForLOA assetForLOAsItem) {
        if (this.assetForLOAs == null) {
            this.assetForLOAs = new ArrayList<AssetsForLOA>();
        }
        this.assetForLOAs.add(assetForLOAsItem);
        return this;
    }

    /**
     * Array of Asset for LOA Details
     *
     * @return assetForLOAs
     **/
    @ApiModelProperty(value = "Array of Asset for LOA Details")

    @Valid

    public List<AssetsForLOA> getAssetForLOAs() {
        return assetForLOAs;
    }

    public void setAssetForLOAs(List<AssetsForLOA> assetForLOAs) {
        this.assetForLOAs = assetForLOAs;
    }

    public LetterOfAcceptanceEstimate loaActivities(List<LOAActivity> loaActivities) {
        this.loaActivities = loaActivities;
        return this;
    }

    public LetterOfAcceptanceEstimate addLoaActivitiesItem(LOAActivity loaActivitiesItem) {
        if (this.loaActivities == null) {
            this.loaActivities = new ArrayList<LOAActivity>();
        }
        this.loaActivities.add(loaActivitiesItem);
        return this;
    }

    /**
     * Array of LOA Activity Details
     *
     * @return loaActivities
     **/
    @ApiModelProperty(value = "Array of LOA Activity Details")

//    @Valid

    public List<LOAActivity> getLoaActivities() {
        return loaActivities;
    }

    public void setLoaActivities(List<LOAActivity> loaActivities) {
        this.loaActivities = loaActivities;
    }

    public LetterOfAcceptanceEstimate auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     *
     * @return auditDetails
     **/
    @ApiModelProperty(value = "")

    @Valid

    public AuditDetails getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
    }

    public LetterOfAcceptanceEstimate deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    /**
     * Boolean value to identify whether the object is deleted or not from UI.
     *
     * @return deleted
     **/
    @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")


    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public LetterOfAcceptanceEstimate backUpdateDE(Boolean backUpdateDE) {
        this.backUpdateDE = backUpdateDE;
        return this;
    }

    /**
     * Boolean value to update true/false for detailedestimate based on LOA create/Cancelled.
     * @return backUpdateDE
     **/
    @ApiModelProperty(value = "Boolean value to update true/false for detailedestimate based on LOA create/Cancelled.")


    public Boolean getBackUpdateDE() {
        return backUpdateDE;
    }

    public void setBackUpdateDE(Boolean backUpdateDE) {
        this.backUpdateDE = backUpdateDE;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = (LetterOfAcceptanceEstimate) o;
        return Objects.equals(this.id, letterOfAcceptanceEstimate.id) &&
                Objects.equals(this.tenantId, letterOfAcceptanceEstimate.tenantId) &&
                Objects.equals(this.letterOfAcceptance, letterOfAcceptanceEstimate.letterOfAcceptance) &&
                Objects.equals(this.detailedEstimate, letterOfAcceptanceEstimate.detailedEstimate) &&
                Objects.equals(this.workCompletionDate, letterOfAcceptanceEstimate.workCompletionDate) &&
                Objects.equals(this.estimateLOAAmount, letterOfAcceptanceEstimate.estimateLOAAmount) &&
                Objects.equals(this.assetForLOAs, letterOfAcceptanceEstimate.assetForLOAs) &&
                Objects.equals(this.loaActivities, letterOfAcceptanceEstimate.loaActivities) &&
                Objects.equals(this.auditDetails, letterOfAcceptanceEstimate.auditDetails) &&
                Objects.equals(this.deleted, letterOfAcceptanceEstimate.deleted) &&
                Objects.equals(this.backUpdateDE, letterOfAcceptanceEstimate.backUpdateDE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, letterOfAcceptance, detailedEstimate, workCompletionDate, estimateLOAAmount, assetForLOAs, loaActivities, auditDetails, deleted, backUpdateDE);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LetterOfAcceptanceEstimate {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    letterOfAcceptance: ").append(toIndentedString(letterOfAcceptance)).append("\n");
        sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
        sb.append("    workCompletionDate: ").append(toIndentedString(workCompletionDate)).append("\n");
        sb.append("    estimateLOAAmount: ").append(toIndentedString(estimateLOAAmount)).append("\n");
        sb.append("    assetForLOAs: ").append(toIndentedString(assetForLOAs)).append("\n");
        sb.append("    loaActivities: ").append(toIndentedString(loaActivities)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
        sb.append("    backUpdateDE: ").append(toIndentedString(backUpdateDE)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

