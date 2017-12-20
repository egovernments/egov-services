package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object that holds the basic data of Work Order Details
 */
@ApiModel(description = "An Object that holds the basic data of Work Order Details")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class WorkOrderDetail {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("workOrder")
    private String workOrder = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("editable")
    private Boolean editable = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public WorkOrderDetail id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Work Order Details
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Work Order Details")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorkOrderDetail tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * tenant id of the Work Order Details
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "tenant id of the Work Order Details")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public WorkOrderDetail workOrder(String workOrder) {
        this.workOrder = workOrder;
        return this;
    }

    /**
     * Reference of Work Order
     *
     * @return workOrder
     **/
    @ApiModelProperty(value = "Reference of Work Order")


    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }

    public WorkOrderDetail remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    /**
     * Remarks for the Work Order
     *
     * @return remarks
     **/
    @ApiModelProperty(required = true, value = "Remarks for the Work Order")
    @NotNull

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(min = 1, max = 1024)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public WorkOrderDetail editable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    /**
     * True for default
     *
     * @return editable
     **/
    @ApiModelProperty(required = true, value = "True for default")
    @NotNull


    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public WorkOrderDetail auditDetails(AuditDetails auditDetails) {
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

    public WorkOrderDetail deleted(Boolean deleted) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkOrderDetail workOrderDetail = (WorkOrderDetail) o;
        return Objects.equals(this.id, workOrderDetail.id) &&
                Objects.equals(this.tenantId, workOrderDetail.tenantId) &&
                Objects.equals(this.workOrder, workOrderDetail.workOrder) &&
                Objects.equals(this.remarks, workOrderDetail.remarks) &&
                Objects.equals(this.editable, workOrderDetail.editable) &&
                Objects.equals(this.auditDetails, workOrderDetail.auditDetails) &&
                Objects.equals(this.deleted, workOrderDetail.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, workOrder, remarks, editable, auditDetails, deleted);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class WorkOrderDetail {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    workOrder: ").append(toIndentedString(workOrder)).append("\n");
        sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
        sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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

