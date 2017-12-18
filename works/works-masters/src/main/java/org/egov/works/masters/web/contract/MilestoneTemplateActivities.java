package org.egov.works.masters.web.contract;

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
 * An Object that holds Activities for a given Milestone template
 */
@ApiModel(description = "An Object that holds Activities for a given Milestone template")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-18T04:55:33.424Z")

public class MilestoneTemplateActivities   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("milestoneTemplate")
    private String milestoneTemplate = null;

    @JsonProperty("stageOrderNumber")
    private BigDecimal stageOrderNumber = null;

    @JsonProperty("stageDescription")
    private String stageDescription = null;

    @JsonProperty("percentage")
    private Double percentage = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public MilestoneTemplateActivities id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Milestone Template Activitie
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Milestone Template Activitie")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MilestoneTemplateActivities tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Milestone Template Activitie
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Milestone Template Activitie")
    @NotNull

    @Size(min=2,max=128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public MilestoneTemplateActivities milestoneTemplate(String milestoneTemplate) {
        this.milestoneTemplate = milestoneTemplate;
        return this;
    }

    /**
     * The milestone template of the Template activity. Unique reference from 'MilestoneTemplate'. Primary key is ref. here.
     * @return milestoneTemplate
     **/
    @ApiModelProperty(value = "The milestone template of the Template activity. Unique reference from 'MilestoneTemplate'. Primary key is ref. here.")


    public String getMilestoneTemplate() {
        return milestoneTemplate;
    }

    public void setMilestoneTemplate(String milestoneTemplate) {
        this.milestoneTemplate = milestoneTemplate;
    }

    public MilestoneTemplateActivities stageOrderNumber(BigDecimal stageOrderNumber) {
        this.stageOrderNumber = stageOrderNumber;
        return this;
    }

    /**
     * Stage Order Number for the Milestone Template Activitie
     * @return stageOrderNumber
     **/
    @ApiModelProperty(required = true, value = "Stage Order Number for the Milestone Template Activitie")
    @NotNull

    @Valid

    public BigDecimal getStageOrderNumber() {
        return stageOrderNumber;
    }

    public void setStageOrderNumber(BigDecimal stageOrderNumber) {
        this.stageOrderNumber = stageOrderNumber;
    }

    public MilestoneTemplateActivities stageDescription(String stageDescription) {
        this.stageDescription = stageDescription;
        return this;
    }

    /**
     * Description for the Milestone Template Activitie
     * @return stageDescription
     **/
    @ApiModelProperty(required = true, value = "Description for the Milestone Template Activitie")
    @NotNull

    @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
    public String getStageDescription() {
        return stageDescription;
    }

    public void setStageDescription(String stageDescription) {
        this.stageDescription = stageDescription;
    }

    public MilestoneTemplateActivities percentage(Double percentage) {
        this.percentage = percentage;
        return this;
    }

    /**
     * Percentage to be applied for the given Milestone Template Activitie
     * @return percentage
     **/
    @ApiModelProperty(required = true, value = "Percentage to be applied for the given Milestone Template Activitie")
    @NotNull


    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public MilestoneTemplateActivities auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
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

    public MilestoneTemplateActivities deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    /**
     * Boolean value to identify whether the object is deleted or not from UI.
     * @return deleted
     **/
    @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")


    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MilestoneTemplateActivities milestoneTemplateActivities = (MilestoneTemplateActivities) o;
        return Objects.equals(this.id, milestoneTemplateActivities.id) &&
                Objects.equals(this.tenantId, milestoneTemplateActivities.tenantId) &&
                Objects.equals(this.milestoneTemplate, milestoneTemplateActivities.milestoneTemplate) &&
                Objects.equals(this.stageOrderNumber, milestoneTemplateActivities.stageOrderNumber) &&
                Objects.equals(this.stageDescription, milestoneTemplateActivities.stageDescription) &&
                Objects.equals(this.percentage, milestoneTemplateActivities.percentage) &&
                Objects.equals(this.auditDetails, milestoneTemplateActivities.auditDetails) &&
                Objects.equals(this.deleted, milestoneTemplateActivities.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, milestoneTemplate, stageOrderNumber, stageDescription, percentage, auditDetails, deleted);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MilestoneTemplateActivities {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    milestoneTemplate: ").append(toIndentedString(milestoneTemplate)).append("\n");
        sb.append("    stageOrderNumber: ").append(toIndentedString(stageOrderNumber)).append("\n");
        sb.append("    stageDescription: ").append(toIndentedString(stageDescription)).append("\n");
        sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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
