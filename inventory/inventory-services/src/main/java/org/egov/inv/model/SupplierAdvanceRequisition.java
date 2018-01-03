package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object gives information about advance issued againest purchase order. There are multiple times advance requisition possible for selected purchase order. But it should not cross alloted advance amount or advance percentage defined in purchase order.
 */
@ApiModel(description = "An Object gives information about advance issued againest purchase order. There are multiple times advance requisition possible for selected purchase order. But it should not cross alloted advance amount or advance percentage defined in purchase order. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-02T08:20:46.403Z")

public class SupplierAdvanceRequisition extends AdvanceRequisition {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("supplier")
    private Supplier supplier = null;

    @JsonProperty("purchaseOrder")
    private PurchaseOrder purchaseOrder = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("advanceAdjustedAmount")
    private BigDecimal advanceAdjustedAmount = null;

    @JsonProperty("advanceFullyAdjustedInBill")
    private Boolean advanceFullyAdjustedInBill = false;

    @JsonProperty("sarStatus")
    private String sarStatus = null;

    @JsonProperty("workFlowDetails")
    private WorkFlowDetails workFlowDetails = null;

    @JsonProperty("stateId")
    private String stateId = null;

    public SupplierAdvanceRequisition id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Supplier Bill
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Supplier Bill")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SupplierAdvanceRequisition tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * tenant id of the Contractor Advance Requisition Form
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "tenant id of the Contractor Advance Requisition Form")
    @NotNull

    @Size(min=2,max=128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public SupplierAdvanceRequisition supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    /**
     * Supplier reference
     * @return supplier
     **/
    @ApiModelProperty(value = "Supplier reference")

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public SupplierAdvanceRequisition purchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
        return this;
    }

    /**
     * Purchase order reference
     * @return purchaseOrder
     **/
    @ApiModelProperty(required = true, value = "Purchase order reference")
    @NotNull

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public SupplierAdvanceRequisition auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     * @return auditDetails
     **/
    @ApiModelProperty(value = "")

    public AuditDetails getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
    }

    public SupplierAdvanceRequisition advanceAdjustedAmount(BigDecimal advanceAdjustedAmount) {
        this.advanceAdjustedAmount = advanceAdjustedAmount;
        return this;
    }

    /**
     * Total Advance adjested amount in each supplier bill
     * @return advanceAdjustedAmount
     **/
    @ApiModelProperty(value = "Total Advance adjested amount in each supplier bill")

    @Valid

    public BigDecimal getAdvanceAdjustedAmount() {
        return advanceAdjustedAmount;
    }

    public void setAdvanceAdjustedAmount(BigDecimal advanceAdjustedAmount) {
        this.advanceAdjustedAmount = advanceAdjustedAmount;
    }

    public SupplierAdvanceRequisition advanceFullyAdjustedInBill(Boolean advanceFullyAdjustedInBill) {
        this.advanceFullyAdjustedInBill = advanceFullyAdjustedInBill;
        return this;
    }

    /**
     * Boolean value to identify whether the advance fully adjusted in supplier bill.
     * @return advanceFullyAdjustedInBill
     **/
    @ApiModelProperty(value = "Boolean value to identify whether the advance fully adjusted in supplier bill.")


    public Boolean getAdvanceFullyAdjustedInBill() {
        return advanceFullyAdjustedInBill;
    }

    public void setAdvanceFullyAdjustedInBill(Boolean advanceFullyAdjustedInBill) {
        this.advanceFullyAdjustedInBill = advanceFullyAdjustedInBill;
    }

    public SupplierAdvanceRequisition sarStatus(String sarStatus) {
        this.sarStatus = sarStatus;
        return this;
    }

    /**
     * status of supplier advance requisition
     * @return sarStatus
     **/
    @ApiModelProperty(value = "status of supplier advance requisition")


    public String getSarStatus() {
        return sarStatus;
    }

    public void setSarStatus(String sarStatus) {
        this.sarStatus = sarStatus;
    }

    public SupplierAdvanceRequisition workFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
        return this;
    }

    /**
     * Get workFlowDetails
     * @return workFlowDetails
     **/
    @ApiModelProperty(value = "")

    @Valid

    public WorkFlowDetails getWorkFlowDetails() {
        return workFlowDetails;
    }

    public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
    }

    public SupplierAdvanceRequisition stateId(String stateId) {
        this.stateId = stateId;
        return this;
    }

    /**
     * State id of the workflow
     * @return stateId
     **/
    @ApiModelProperty(value = "State id of the workflow")


    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SupplierAdvanceRequisition supplierAdvanceRequisition = (SupplierAdvanceRequisition) o;
        return Objects.equals(this.id, supplierAdvanceRequisition.id) &&
                Objects.equals(this.tenantId, supplierAdvanceRequisition.tenantId) &&
                Objects.equals(this.supplier, supplierAdvanceRequisition.supplier) &&
                Objects.equals(this.purchaseOrder, supplierAdvanceRequisition.purchaseOrder) &&
                Objects.equals(this.auditDetails, supplierAdvanceRequisition.auditDetails) &&
                Objects.equals(this.advanceAdjustedAmount, supplierAdvanceRequisition.advanceAdjustedAmount) &&
                Objects.equals(this.advanceFullyAdjustedInBill, supplierAdvanceRequisition.advanceFullyAdjustedInBill) &&
                Objects.equals(this.sarStatus, supplierAdvanceRequisition.sarStatus) &&
                Objects.equals(this.workFlowDetails, supplierAdvanceRequisition.workFlowDetails) &&
                Objects.equals(this.stateId, supplierAdvanceRequisition.stateId) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, supplier, purchaseOrder, auditDetails, advanceAdjustedAmount, advanceFullyAdjustedInBill, sarStatus, workFlowDetails, stateId, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SupplierAdvanceRequisition {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    supplier: ").append(toIndentedString(supplier)).append("\n");
        sb.append("    purchaseOrder: ").append(toIndentedString(purchaseOrder)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("    advanceAdjustedAmount: ").append(toIndentedString(advanceAdjustedAmount)).append("\n");
        sb.append("    advanceFullyAdjustedInBill: ").append(toIndentedString(advanceFullyAdjustedInBill)).append("\n");
        sb.append("    sarStatus: ").append(toIndentedString(sarStatus)).append("\n");
        sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
        sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
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
