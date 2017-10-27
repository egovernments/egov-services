package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * PurchaseIndent  Single purchase order can be mapped to multiple indent
 */
@ApiModel(description = "PurchaseIndent  Single purchase order can be mapped to multiple indent ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class PurchaseIndent {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("purchaseOrder")
    private PurchaseOrder purchaseOrder = null;

    @JsonProperty("indent")
    private Indent indent = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public PurchaseIndent id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Purchase Indent
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Purchase Indent ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PurchaseIndent tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Purchase Indent
     *
     * @return tenantId
     **/
    @ApiModelProperty(value = "Tenant id of the Purchase Indent")

    @Size(min = 4, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public PurchaseIndent purchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
        return this;
    }

    /**
     * Get purchaseOrder
     *
     * @return purchaseOrder
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseIndent indent(Indent indent) {
        this.indent = indent;
        return this;
    }

    /**
     * Get indent
     *
     * @return indent
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public Indent getIndent() {
        return indent;
    }

    public void setIndent(Indent indent) {
        this.indent = indent;
    }

    public PurchaseIndent auditDetails(AuditDetails auditDetails) {
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
        PurchaseIndent purchaseIndent = (PurchaseIndent) o;
        return Objects.equals(this.id, purchaseIndent.id) &&
                Objects.equals(this.tenantId, purchaseIndent.tenantId) &&
                Objects.equals(this.purchaseOrder, purchaseIndent.purchaseOrder) &&
                Objects.equals(this.indent, purchaseIndent.indent) &&
                Objects.equals(this.auditDetails, purchaseIndent.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, purchaseOrder, indent, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PurchaseIndent {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    purchaseOrder: ").append(toIndentedString(purchaseOrder)).append("\n");
        sb.append("    indent: ").append(toIndentedString(indent)).append("\n");
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

