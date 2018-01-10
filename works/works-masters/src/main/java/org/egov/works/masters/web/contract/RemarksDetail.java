package org.egov.works.masters.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds Remarks Details for a given Remarks Master
 */
@ApiModel(description = "An Object that holds Remarks Details for a given Remarks Master")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-10T13:23:45.489Z")

public class RemarksDetail {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("remarksDescription")
    private String remarksDescription = null;

    @JsonProperty("editable")
    private Boolean editable = true;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public RemarksDetail id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Remarks Detail
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Remarks Detail")

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RemarksDetail tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Remarks Detail
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Remarks Detail")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public RemarksDetail remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    /**
     * Reference of remarks.
     * @return remarks
     **/
    @ApiModelProperty(required = true, value = "Reference of remarks.")
    @NotNull

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public RemarksDetail remarksDescription(String remarksDescription) {
        this.remarksDescription = remarksDescription;
        return this;
    }

    /**
     * Detailed description for given remarks master
     * @return remarksDescription
     **/
    @ApiModelProperty(required = true, value = "Detailed description for given remarks master")
    @NotNull

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 1024)
    public String getRemarksDescription() {
        return remarksDescription;
    }

    public void setRemarksDescription(String remarksDescription) {
        this.remarksDescription = remarksDescription;
    }

    public RemarksDetail editable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    /**
     * True By default
     * @return editable
     **/
    @ApiModelProperty(required = true, value = "True By default")
    @NotNull

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public RemarksDetail deleted(Boolean deleted) {
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

    public RemarksDetail auditDetails(AuditDetails auditDetails) {
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

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RemarksDetail remarksDetail = (RemarksDetail) o;
        return Objects.equals(this.id, remarksDetail.id) &&
                Objects.equals(this.tenantId, remarksDetail.tenantId) &&
                Objects.equals(this.remarks, remarksDetail.remarks) &&
                Objects.equals(this.remarksDescription, remarksDetail.remarksDescription) &&
                Objects.equals(this.editable, remarksDetail.editable) &&
                Objects.equals(this.deleted, remarksDetail.deleted) &&
                Objects.equals(this.auditDetails, remarksDetail.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, remarks, remarksDescription, editable, deleted, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RemarksDetail {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
        sb.append("    remarksDescription: ").append(toIndentedString(remarksDescription)).append("\n");
        sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
