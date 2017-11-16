package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds the materail issue and receipts used for material issue. This table will be used in FIFO logic and total items issued againest a particular receipt. 
 */
@ApiModel(description = "This object holds the materail issue and receipts used for material issue. This table will be used in FIFO logic and total items issued againest a particular receipt. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-13T08:36:20.118Z")

public class MaterialIssuedFromReceipt   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("materialReceiptId")
  private String materialReceiptId = null;

  @JsonProperty("materialReceiptDetail")
  private MaterialReceiptDetail materialReceiptDetail = null;

  @JsonProperty("materialReceiptDetailAddnlinfoId")
  private String materialReceiptDetailAddnlinfoId = null;

  @JsonProperty("quantity")
  private BigDecimal quantity = null;

  @JsonProperty("status")
  private Boolean status = true;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public MaterialIssuedFromReceipt id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Material Issue from receipt. 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Material Issue from receipt. ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MaterialIssuedFromReceipt tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Material Issue from receipt.
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Material Issue from receipt.")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MaterialIssuedFromReceipt materialReceiptId(String materialReceiptId) {
    this.materialReceiptId = materialReceiptId;
    return this;
  }

   /**
   * material receipt id
   * @return materialReceiptId
  **/
  @ApiModelProperty(value = "material receipt id")

 @Size(max=128)
  public String getMaterialReceiptId() {
    return materialReceiptId;
  }

  public void setMaterialReceiptId(String materialReceiptId) {
    this.materialReceiptId = materialReceiptId;
  }

  public MaterialIssuedFromReceipt materialReceiptDetail(MaterialReceiptDetail materialReceiptDetail) {
    this.materialReceiptDetail = materialReceiptDetail;
    return this;
  }

   /**
   * In case of return of material purpose type, select initial receipt to keep account. Do not use fifo logic in case of return to supplier. Supplier will received the same goods which are identified by lot number, expiry date, serial number or manufacture number or receive date wise.  For other purpose use FIFO logic to issue item.
   * @return materialReceiptDetail
  **/
  @ApiModelProperty(value = "In case of return of material purpose type, select initial receipt to keep account. Do not use fifo logic in case of return to supplier. Supplier will received the same goods which are identified by lot number, expiry date, serial number or manufacture number or receive date wise.  For other purpose use FIFO logic to issue item.")

  @Valid

  public MaterialReceiptDetail getMaterialReceiptDetail() {
    return materialReceiptDetail;
  }

  public void setMaterialReceiptDetail(MaterialReceiptDetail materialReceiptDetail) {
    this.materialReceiptDetail = materialReceiptDetail;
  }

  public MaterialIssuedFromReceipt materialReceiptDetailAddnlinfoId(String materialReceiptDetailAddnlinfoId) {
    this.materialReceiptDetailAddnlinfoId = materialReceiptDetailAddnlinfoId;
    return this;
  }

   /**
   * if material has lot number, expiry date, serial number or manufacture number then save receipt additional info id also.
   * @return materialReceiptDetailAddnlinfoId
  **/
  @ApiModelProperty(value = "if material has lot number, expiry date, serial number or manufacture number then save receipt additional info id also.")

 @Size(max=128)
  public String getMaterialReceiptDetailAddnlinfoId() {
    return materialReceiptDetailAddnlinfoId;
  }

  public void setMaterialReceiptDetailAddnlinfoId(String materialReceiptDetailAddnlinfoId) {
    this.materialReceiptDetailAddnlinfoId = materialReceiptDetailAddnlinfoId;
  }

  public MaterialIssuedFromReceipt quantity(BigDecimal quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Quantity issued of the Material Issue Detail. 
   * @return quantity
  **/
  @ApiModelProperty(value = "Quantity issued of the Material Issue Detail. ")

  @Valid

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public MaterialIssuedFromReceipt status(Boolean status) {
    this.status = status;
    return this;
  }

   /**
   * If material issue cancelled or rejected in workflow, then use status as false. 
   * @return status
  **/
  @ApiModelProperty(value = "If material issue cancelled or rejected in workflow, then use status as false. ")


  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public MaterialIssuedFromReceipt auditDetails(AuditDetails auditDetails) {
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
    MaterialIssuedFromReceipt materialIssuedFromReceipt = (MaterialIssuedFromReceipt) o;
    return Objects.equals(this.id, materialIssuedFromReceipt.id) &&
        Objects.equals(this.tenantId, materialIssuedFromReceipt.tenantId) &&
        Objects.equals(this.materialReceiptId, materialIssuedFromReceipt.materialReceiptId) &&
        Objects.equals(this.materialReceiptDetail, materialIssuedFromReceipt.materialReceiptDetail) &&
        Objects.equals(this.materialReceiptDetailAddnlinfoId, materialIssuedFromReceipt.materialReceiptDetailAddnlinfoId) &&
        Objects.equals(this.quantity, materialIssuedFromReceipt.quantity) &&
        Objects.equals(this.status, materialIssuedFromReceipt.status) &&
        Objects.equals(this.auditDetails, materialIssuedFromReceipt.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, materialReceiptId, materialReceiptDetail, materialReceiptDetailAddnlinfoId, quantity, status, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialIssuedFromReceipt {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    materialReceiptId: ").append(toIndentedString(materialReceiptId)).append("\n");
    sb.append("    materialReceiptDetail: ").append(toIndentedString(materialReceiptDetail)).append("\n");
    sb.append("    materialReceiptDetailAddnlinfoId: ").append(toIndentedString(materialReceiptDetailAddnlinfoId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

