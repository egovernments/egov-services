package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object that holds Activity for the given Milestone. At least one data should be given to create an Milestone
 */
@ApiModel(description = "An Object that holds Activity for the given Milestone. At least one data should be given to create an Milestone")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class MilestoneActivity {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("milestone")
    private String milestone = null;

    @JsonProperty("scheduleStartDate")
    private Long scheduleStartDate = null;

    @JsonProperty("scheduleEndDate")
    private Long scheduleEndDate = null;

    @JsonProperty("percentage")
    private BigDecimal percentage = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("stageOrderNumber")
    private String stageOrderNumber = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public MilestoneActivity id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Milestone Activity
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Milestone Activity")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MilestoneActivity tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Milestone Activity
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Milestone Activity")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public MilestoneActivity milestone(String milestone) {
        this.milestone = milestone;
        return this;
    }

    /**
     * Reference of Milestone
     *
     * @return milestone
     **/
    @ApiModelProperty(required = true, value = "Reference of Milestone")
    @NotNull


    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public MilestoneActivity scheduleStartDate(Long scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
        return this;
    }

    /**
     * Epoch time of the Schedule start date for activity
     *
     * @return scheduleStartDate
     **/
    @ApiModelProperty(value = "Epoch time of the Schedule start date for activity")


    public Long getScheduleStartDate() {
        return scheduleStartDate;
    }

    public void setScheduleStartDate(Long scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
    }

    public MilestoneActivity scheduleEndDate(Long scheduleEndDate) {
        this.scheduleEndDate = scheduleEndDate;
        return this;
    }

    /**
     * Epoch time of the Schedule end date for activity
     *
     * @return scheduleEndDate
     **/
    @ApiModelProperty(value = "Epoch time of the Schedule end date for activity")


    public Long getScheduleEndDate() {
        return scheduleEndDate;
    }

    public void setScheduleEndDate(Long scheduleEndDate) {
        this.scheduleEndDate = scheduleEndDate;
    }

    public MilestoneActivity percentage(BigDecimal percentage) {
        this.percentage = percentage;
        return this;
    }

    /**
     * Percentage for the given Milestone Activity.
     *
     * @return percentage
     **/
    @ApiModelProperty(required = true, value = "Percentage for the given Milestone Activity.")
    @NotNull

    @Valid

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public MilestoneActivity description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Descritpion for Milestone Activity.
     *
     * @return description
     **/
    @ApiModelProperty(required = true, value = "Descritpion for Milestone Activity.")
    @NotNull

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(min = 1, max = 512)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MilestoneActivity stageOrderNumber(String stageOrderNumber) {
        this.stageOrderNumber = stageOrderNumber;
        return this;
    }

    /**
     * Stage Order Number for Milestone Activity.
     *
     * @return stageOrderNumber
     **/
    @ApiModelProperty(required = true, value = "Stage Order Number for Milestone Activity.")
    @NotNull

    @Pattern(regexp = "[a-zA-Z0-9-\\\\]+")
    public String getStageOrderNumber() {
        return stageOrderNumber;
    }

    public void setStageOrderNumber(String stageOrderNumber) {
        this.stageOrderNumber = stageOrderNumber;
    }

    public MilestoneActivity deleted(Boolean deleted) {
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

    public MilestoneActivity auditDetails(AuditDetails auditDetails) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MilestoneActivity milestoneActivity = (MilestoneActivity) o;
        return Objects.equals(this.id, milestoneActivity.id) &&
                Objects.equals(this.tenantId, milestoneActivity.tenantId) &&
                Objects.equals(this.milestone, milestoneActivity.milestone) &&
                Objects.equals(this.scheduleStartDate, milestoneActivity.scheduleStartDate) &&
                Objects.equals(this.scheduleEndDate, milestoneActivity.scheduleEndDate) &&
                Objects.equals(this.percentage, milestoneActivity.percentage) &&
                Objects.equals(this.description, milestoneActivity.description) &&
                Objects.equals(this.stageOrderNumber, milestoneActivity.stageOrderNumber) &&
                Objects.equals(this.deleted, milestoneActivity.deleted) &&
                Objects.equals(this.auditDetails, milestoneActivity.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, milestone, scheduleStartDate, scheduleEndDate, percentage, description, stageOrderNumber, deleted, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MilestoneActivity {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    milestone: ").append(toIndentedString(milestone)).append("\n");
        sb.append("    scheduleStartDate: ").append(toIndentedString(scheduleStartDate)).append("\n");
        sb.append("    scheduleEndDate: ").append(toIndentedString(scheduleEndDate)).append("\n");
        sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    stageOrderNumber: ").append(toIndentedString(stageOrderNumber)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

