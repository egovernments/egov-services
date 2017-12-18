package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds Templates defined for Milestone based on Type of Works and Sub Type of work
 */
@ApiModel(description = "An Object that holds Templates defined for Milestone based on Type of Works and Sub Type of work")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-18T04:55:33.424Z")

public class MilestoneTemplate   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("active")
    private Boolean active = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("typeOfWork")
    private TypeOfWork typeOfWork = null;

    @JsonProperty("subTypeOfWork")
    private TypeOfWork subTypeOfWork = null;

    @JsonProperty("milestoneTemplateActivities")
    private List<MilestoneTemplateActivities> milestoneTemplateActivities = new ArrayList<MilestoneTemplateActivities>();

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public MilestoneTemplate id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Milestone Template
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Milestone Template")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MilestoneTemplate tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Milestone Template
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Milestone Template")
    @NotNull

    @Size(min=2,max=128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public MilestoneTemplate name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the Milestone Template
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Name of the Milestone Template")
    @NotNull

    @Pattern(regexp="[a-zA-Z0-9\\s\\.,]+") @Size(min=1,max=100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MilestoneTemplate code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Code of the Milestone Template
     * @return code
     **/
    @ApiModelProperty(required = true, value = "Code of the Milestone Template")
    @NotNull

    @Pattern(regexp="[a-zA-Z0-9-\\\\]+") @Size(min=1,max=100)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MilestoneTemplate active(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * If status of Type Of Work is TRUE then the Milestone Template is active else it is in-active. The default is active.
     * @return active
     **/
    @ApiModelProperty(required = true, value = "If status of Type Of Work is TRUE then the Milestone Template is active else it is in-active. The default is active.")
    @NotNull


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public MilestoneTemplate description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Description of the Milestone Template
     * @return description
     **/
    @ApiModelProperty(value = "Description of the Milestone Template")

    @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MilestoneTemplate typeOfWork(TypeOfWork typeOfWork) {
        this.typeOfWork = typeOfWork;
        return this;
    }

    /**
     * Type of work for which this Milestone template is belongs to. Unique reference from 'TypeOfWork'.Code is ref. here.
     * @return typeOfWork
     **/
    @ApiModelProperty(required = true, value = "Type of work for which this Milestone template is belongs to. Unique reference from 'TypeOfWork'.Code is ref. here.")
    @NotNull

    public TypeOfWork getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(TypeOfWork typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public MilestoneTemplate subTypeOfWork(TypeOfWork subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
        return this;
    }

    /**
     * Sub Type of work for which this Milestone template is belongs to. Unique reference from 'TypeOfWork'.Code is ref. here.
     * @return subTypeOfWork
     **/
    @ApiModelProperty(value = "Sub Type of work for which this Milestone template is belongs to. Unique reference from 'TypeOfWork'.Code is ref. here.")


    public TypeOfWork getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setSubTypeOfWork(TypeOfWork subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

    public MilestoneTemplate milestoneTemplateActivities(List<MilestoneTemplateActivities> milestoneTemplateActivities) {
        this.milestoneTemplateActivities = milestoneTemplateActivities;
        return this;
    }

    public MilestoneTemplate addMilestoneTemplateActivitiesItem(MilestoneTemplateActivities milestoneTemplateActivitiesItem) {
        this.milestoneTemplateActivities.add(milestoneTemplateActivitiesItem);
        return this;
    }

    /**
     * Array of Milestone Template Activities
     * @return milestoneTemplateActivities
     **/
    @ApiModelProperty(required = true, value = "Array of Milestone Template Activities")
    @NotNull

    @Valid
    @Size(min=1)
    public List<MilestoneTemplateActivities> getMilestoneTemplateActivities() {
        return milestoneTemplateActivities;
    }

    public void setMilestoneTemplateActivities(List<MilestoneTemplateActivities> milestoneTemplateActivities) {
        this.milestoneTemplateActivities = milestoneTemplateActivities;
    }

    public MilestoneTemplate auditDetails(AuditDetails auditDetails) {
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

    public MilestoneTemplate deleted(Boolean deleted) {
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
        MilestoneTemplate milestoneTemplate = (MilestoneTemplate) o;
        return Objects.equals(this.id, milestoneTemplate.id) &&
                Objects.equals(this.tenantId, milestoneTemplate.tenantId) &&
                Objects.equals(this.name, milestoneTemplate.name) &&
                Objects.equals(this.code, milestoneTemplate.code) &&
                Objects.equals(this.active, milestoneTemplate.active) &&
                Objects.equals(this.description, milestoneTemplate.description) &&
                Objects.equals(this.typeOfWork, milestoneTemplate.typeOfWork) &&
                Objects.equals(this.subTypeOfWork, milestoneTemplate.subTypeOfWork) &&
                Objects.equals(this.milestoneTemplateActivities, milestoneTemplate.milestoneTemplateActivities) &&
                Objects.equals(this.auditDetails, milestoneTemplate.auditDetails) &&
                Objects.equals(this.deleted, milestoneTemplate.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, name, code, active, description, typeOfWork, subTypeOfWork, milestoneTemplateActivities, auditDetails, deleted);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MilestoneTemplate {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    active: ").append(toIndentedString(active)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    typeOfWork: ").append(toIndentedString(typeOfWork)).append("\n");
        sb.append("    subTypeOfWork: ").append(toIndentedString(subTypeOfWork)).append("\n");
        sb.append("    milestoneTemplateActivities: ").append(toIndentedString(milestoneTemplateActivities)).append("\n");
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
