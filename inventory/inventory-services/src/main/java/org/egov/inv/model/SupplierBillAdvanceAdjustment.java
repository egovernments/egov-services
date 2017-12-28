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
 * Asset information for the Contractor Bill
 */
@ApiModel(description = "Asset information for the Contractor Bill")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-28T09:20:06.607Z")

public class SupplierBillAdvanceAdjustment {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("supplierBill")
    private String supplierBill = null;

    @JsonProperty("supplierAdvanceRequisition")
    private SupplierAdvanceRequisition supplierAdvanceRequisition = null;

    @JsonProperty("advanceAdjustedAmount")
    private BigDecimal advanceAdjustedAmount = null;

    @JsonProperty("advanceFullyAdjustedInBill")
    private Boolean advanceFullyAdjustedInBill = false;

    public SupplierBillAdvanceAdjustment id(String id) {
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

    public SupplierBillAdvanceAdjustment tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Supplier Bill
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Supplier Bill")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public SupplierBillAdvanceAdjustment supplierBill(String supplierBill) {
        this.supplierBill = supplierBill;
        return this;
    }

    /**
     * supplier bill number id as reference
     *
     * @return supplierBill
     **/
    @ApiModelProperty(required = true, value = "supplier bill number id as reference")
    @NotNull


    public String getSupplierBill() {
        return supplierBill;
    }

    public void setSupplierBill(String supplierBill) {
        this.supplierBill = supplierBill;
    }

    public SupplierBillAdvanceAdjustment supplierAdvanceRequisition(SupplierAdvanceRequisition supplierAdvanceRequisition) {
        this.supplierAdvanceRequisition = supplierAdvanceRequisition;
        return this;
    }

    /**
     * SupplierAdvanceRequisition  reference
     *
     * @return supplierAdvanceRequisition
     **/
    @ApiModelProperty(value = "SupplierAdvanceRequisition  reference")

    @Valid

    public SupplierAdvanceRequisition getSupplierAdvanceRequisition() {
        return supplierAdvanceRequisition;
    }

    public void setSupplierAdvanceRequisition(SupplierAdvanceRequisition supplierAdvanceRequisition) {
        this.supplierAdvanceRequisition = supplierAdvanceRequisition;
    }

    public SupplierBillAdvanceAdjustment advanceAdjustedAmount(BigDecimal advanceAdjustedAmount) {
        this.advanceAdjustedAmount = advanceAdjustedAmount;
        return this;
    }

    /**
     * advance adjested amount in each advance requisition object.
     *
     * @return advanceAdjustedAmount
     **/
    @ApiModelProperty(required = true, value = "advance adjested amount in each advance requisition object. ")
    @NotNull

    @Valid

    public BigDecimal getAdvanceAdjustedAmount() {
        return advanceAdjustedAmount;
    }

    public void setAdvanceAdjustedAmount(BigDecimal advanceAdjustedAmount) {
        this.advanceAdjustedAmount = advanceAdjustedAmount;
    }

    public SupplierBillAdvanceAdjustment advanceFullyAdjustedInBill(Boolean advanceFullyAdjustedInBill) {
        this.advanceFullyAdjustedInBill = advanceFullyAdjustedInBill;
        return this;
    }

    /**
     * Boolean value to identify whether the advance fully adjusted in supplier bill.
     *
     * @return advanceFullyAdjustedInBill
     **/
    @ApiModelProperty(value = "Boolean value to identify whether the advance fully adjusted in supplier bill.")


    public Boolean getAdvanceFullyAdjustedInBill() {
        return advanceFullyAdjustedInBill;
    }

    public void setAdvanceFullyAdjustedInBill(Boolean advanceFullyAdjustedInBill) {
        this.advanceFullyAdjustedInBill = advanceFullyAdjustedInBill;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SupplierBillAdvanceAdjustment supplierBillAdvanceAdjustment = (SupplierBillAdvanceAdjustment) o;
        return Objects.equals(this.id, supplierBillAdvanceAdjustment.id) &&
                Objects.equals(this.tenantId, supplierBillAdvanceAdjustment.tenantId) &&
                Objects.equals(this.supplierBill, supplierBillAdvanceAdjustment.supplierBill) &&
                Objects.equals(this.supplierAdvanceRequisition, supplierBillAdvanceAdjustment.supplierAdvanceRequisition) &&
                Objects.equals(this.advanceAdjustedAmount, supplierBillAdvanceAdjustment.advanceAdjustedAmount) &&
                Objects.equals(this.advanceFullyAdjustedInBill, supplierBillAdvanceAdjustment.advanceFullyAdjustedInBill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, supplierBill, supplierAdvanceRequisition, advanceAdjustedAmount, advanceFullyAdjustedInBill);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SupplierBillAdvanceAdjustment {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    supplierBill: ").append(toIndentedString(supplierBill)).append("\n");
        sb.append("    supplierAdvanceRequisition: ").append(toIndentedString(supplierAdvanceRequisition)).append("\n");
        sb.append("    advanceAdjustedAmount: ").append(toIndentedString(advanceAdjustedAmount)).append("\n");
        sb.append("    advanceFullyAdjustedInBill: ").append(toIndentedString(advanceFullyAdjustedInBill)).append("\n");
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

