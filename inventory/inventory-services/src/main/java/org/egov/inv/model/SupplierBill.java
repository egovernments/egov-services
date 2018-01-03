package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds the basic data for a supplier Bill
 */
@ApiModel(description = "An Object that holds the basic data for a supplier Bill")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-02T07:38:14.139Z")

public class SupplierBill extends BillRegister {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("store")
    private Store store = null;

    @JsonProperty("invoiceNumber")
    private String invoiceNumber = null;

    @JsonProperty("invoiceDate")
    private Long invoiceDate = null;

    @JsonProperty("approvedDate")
    private Long approvedDate = null;

    @JsonProperty("approvedBy")
    private User approvedBy = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    @JsonProperty("supplierBillReceipts")
    private List<SupplierBillReceipt> supplierBillReceipts = null;

    @JsonProperty("supplierBillAdvanceAdjustments")
    private List<SupplierBillAdvanceAdjustment> supplierBillAdvanceAdjustments = null;

    @JsonProperty("billId")
    private String billId = null;

    @JsonProperty("supplierBillStatus")
    private String supplierBillStatus = null;

    @JsonProperty("workFlowDetails")
    private WorkFlowDetails workFlowDetails = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public SupplierBill id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Supplier Bill
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Supplier Bill")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SupplierBill tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Supplier Bill
     *
     * @return tenantId
     **/
    @ApiModelProperty(value = "Tenant id of the Supplier Bill")
    @NotNull
    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public SupplierBill store(Store store) {
        this.store = store;
        return this;
    }

    /**
     * Get store
     *
     * @return store
     **/
    @ApiModelProperty(value = "")

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public SupplierBill invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    /**
     * Invoice number of the Bill
     *
     * @return invoiceNumber
     **/
    @ApiModelProperty(value = "Invoice number of the Bill")

    @Pattern(regexp = "[a-zA-Z0-9-/]+")
    @Size(max = 5)
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public SupplierBill invoiceDate(Long invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    /**
     * Epoch time of invoice date
     *
     * @return invoiceDate
     **/
    @ApiModelProperty(value = "Epoch time of invoice date")


    public Long getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Long invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public SupplierBill approvedDate(Long approvedDate) {
        this.approvedDate = approvedDate;
        return this;
    }

    /**
     * Epoch time of when the Bill approved
     *
     * @return approvedDate
     **/
    @ApiModelProperty(value = "Epoch time of when the Bill approved")


    public Long getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Long approvedDate) {
        this.approvedDate = approvedDate;
    }

    public SupplierBill approvedBy(User approvedBy) {
        this.approvedBy = approvedBy;
        return this;
    }

    /**
     * User name of the User who approved the Bill
     *
     * @return approvedBy
     **/
    @ApiModelProperty(value = "User name of the User who approved the Bill")

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public SupplierBill cancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
        return this;
    }

    /**
     * Reason for cancellation of the Bill, Required only while cancelling Supplier Bill
     *
     * @return cancellationReason
     **/
    @ApiModelProperty(value = "Reason for cancellation of the Bill, Required only while cancelling Supplier Bill")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 100)
    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public SupplierBill cancellationRemarks(String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
        return this;
    }

    /**
     * Remarks for cancellation of the Bill, Required only while cancelling Bill
     *
     * @return cancellationRemarks
     **/
    @ApiModelProperty(value = "Remarks for cancellation of the Bill, Required only while cancelling Bill")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 512)
    public String getCancellationRemarks() {
        return cancellationRemarks;
    }

    public void setCancellationRemarks(String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
    }

    public SupplierBill supplierBillReceipts(List<SupplierBillReceipt> supplierBillReceipts) {
        this.supplierBillReceipts = supplierBillReceipts;
        return this;
    }

    public SupplierBill addSupplierBillReceiptsItem(SupplierBillReceipt supplierBillReceiptsItem) {
        if (this.supplierBillReceipts == null) {
            this.supplierBillReceipts = new ArrayList<SupplierBillReceipt>();
        }
        this.supplierBillReceipts.add(supplierBillReceiptsItem);
        return this;
    }

    /**
     * Array of Material receipts used to create supplier bill. Only approved receipt will be used and there is no partial payment possible from each receipt.
     *
     * @return supplierBillReceipts
     **/
    @ApiModelProperty(value = "Array of Material receipts used to create supplier bill. Only approved receipt will be used and there is no partial payment possible from each receipt.")

    public List<SupplierBillReceipt> getSupplierBillReceipts() {
        return supplierBillReceipts;
    }

    public void setSupplierBillReceipts(List<SupplierBillReceipt> supplierBillReceipts) {
        this.supplierBillReceipts = supplierBillReceipts;
    }

    public SupplierBill supplierBillAdvanceAdjustments(List<SupplierBillAdvanceAdjustment> supplierBillAdvanceAdjustments) {
        this.supplierBillAdvanceAdjustments = supplierBillAdvanceAdjustments;
        return this;
    }

    public SupplierBill addSupplierBillAdvanceAdjustmentsItem(SupplierBillAdvanceAdjustment supplierBillAdvanceAdjustmentsItem) {
        if (this.supplierBillAdvanceAdjustments == null) {
            this.supplierBillAdvanceAdjustments = new ArrayList<SupplierBillAdvanceAdjustment>();
        }
        this.supplierBillAdvanceAdjustments.add(supplierBillAdvanceAdjustmentsItem);
        return this;
    }

    /**
     * Array of Material receipts used to create supplier bill. Only approved receipt will be used and there is no partial payment possible from each receipt.
     *
     * @return supplierBillAdvanceAdjustments
     **/
    @ApiModelProperty(value = "Array of Material receipts used to create supplier bill. Only approved receipt will be used and there is no partial payment possible from each receipt.")

    public List<SupplierBillAdvanceAdjustment> getSupplierBillAdvanceAdjustments() {
        return supplierBillAdvanceAdjustments;
    }

    public void setSupplierBillAdvanceAdjustments(List<SupplierBillAdvanceAdjustment> supplierBillAdvanceAdjustments) {
        this.supplierBillAdvanceAdjustments = supplierBillAdvanceAdjustments;
    }

    public SupplierBill billId(String billId) {
        this.billId = billId;
        return this;
    }

    /**
     * bill id reference
     *
     * @return billId
     **/
    @ApiModelProperty(value = "bill id reference")


    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public SupplierBill supplierBillStatus(String supplierBillStatus) {
        this.supplierBillStatus = supplierBillStatus;
        return this;
    }

    /**
     * status of supplier bill
     *
     * @return supplierBillStatus
     **/
    @ApiModelProperty(value = "status of supplier bill")


    public String getSupplierBillStatus() {
        return supplierBillStatus;
    }

    public void setSupplierBillStatus(String supplierBillStatus) {
        this.supplierBillStatus = supplierBillStatus;
    }

    public SupplierBill workFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
        return this;
    }

    /**
     * Get workFlowDetails
     *
     * @return workFlowDetails
     **/
    @ApiModelProperty(value = "")

    public WorkFlowDetails getWorkFlowDetails() {
        return workFlowDetails;
    }

    public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
    }

    public SupplierBill stateId(String stateId) {
        this.stateId = stateId;
        return this;
    }

    /**
     * State id of the workflow
     *
     * @return stateId
     **/
    @ApiModelProperty(value = "State id of the workflow")


    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public SupplierBill auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     *
     * @return auditDetails
     **/
    @ApiModelProperty(value = "")

    public AuditDetails getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
    }

    public SupplierBill deleted(Boolean deleted) {
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
        SupplierBill supplierBill = (SupplierBill) o;
        return Objects.equals(this.id, supplierBill.id) &&
                Objects.equals(this.tenantId, supplierBill.tenantId) &&
                Objects.equals(this.store, supplierBill.store) &&
                Objects.equals(this.invoiceNumber, supplierBill.invoiceNumber) &&
                Objects.equals(this.invoiceDate, supplierBill.invoiceDate) &&
                Objects.equals(this.approvedDate, supplierBill.approvedDate) &&
                Objects.equals(this.approvedBy, supplierBill.approvedBy) &&
                Objects.equals(this.cancellationReason, supplierBill.cancellationReason) &&
                Objects.equals(this.cancellationRemarks, supplierBill.cancellationRemarks) &&
                Objects.equals(this.supplierBillReceipts, supplierBill.supplierBillReceipts) &&
                Objects.equals(this.supplierBillAdvanceAdjustments, supplierBill.supplierBillAdvanceAdjustments) &&
                Objects.equals(this.billId, supplierBill.billId) &&
                Objects.equals(this.supplierBillStatus, supplierBill.supplierBillStatus) &&
                Objects.equals(this.workFlowDetails, supplierBill.workFlowDetails) &&
                Objects.equals(this.stateId, supplierBill.stateId) &&
                Objects.equals(this.auditDetails, supplierBill.auditDetails) &&
                Objects.equals(this.deleted, supplierBill.deleted) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, store, invoiceNumber, invoiceDate, approvedDate, approvedBy, cancellationReason, cancellationRemarks, supplierBillReceipts, supplierBillAdvanceAdjustments, billId, supplierBillStatus, workFlowDetails, stateId, auditDetails, deleted, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SupplierBill {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    store: ").append(toIndentedString(store)).append("\n");
        sb.append("    invoiceNumber: ").append(toIndentedString(invoiceNumber)).append("\n");
        sb.append("    invoiceDate: ").append(toIndentedString(invoiceDate)).append("\n");
        sb.append("    approvedDate: ").append(toIndentedString(approvedDate)).append("\n");
        sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
        sb.append("    cancellationReason: ").append(toIndentedString(cancellationReason)).append("\n");
        sb.append("    cancellationRemarks: ").append(toIndentedString(cancellationRemarks)).append("\n");
        sb.append("    supplierBillReceipts: ").append(toIndentedString(supplierBillReceipts)).append("\n");
        sb.append("    supplierBillAdvanceAdjustments: ").append(toIndentedString(supplierBillAdvanceAdjustments)).append("\n");
        sb.append("    billId: ").append(toIndentedString(billId)).append("\n");
        sb.append("    supplierBillStatus: ").append(toIndentedString(supplierBillStatus)).append("\n");
        sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
        sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
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
