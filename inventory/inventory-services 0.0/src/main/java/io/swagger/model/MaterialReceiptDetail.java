package io.swagger.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

/**
 * Hold the material receipt note material level information. This will show which material is received based on which purchase order.
 */
@ApiModel(description = "Hold the material receipt note material level information. This will show which material is received based on which purchase order.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-08T06:17:26.594Z")
@Builder
public class MaterialReceiptDetail   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("mrnNumber")
  private MaterialReceipt mrnNumber = null;

  @JsonProperty("material")
  private Material material = null;

  @JsonProperty("uom")
  private Uom uom = null;

  @JsonProperty("poline")
  private PurchaseOrder poline = null;

  @JsonProperty("receivedQty")
  private BigDecimal receivedQty = null;

  @JsonProperty("acceptedQty")
  private BigDecimal acceptedQty = null;

  @JsonProperty("unitRate")
  private BigDecimal unitRate = null;

  @JsonProperty("asset")
  private Asset asset = null;

  @JsonProperty("voucherHeader")
  private String voucherHeader = null;

  @JsonProperty("rejectionRemark")
  private String rejectionRemark = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("receiptDetailsAddnInfo")
  private List<MaterialReceiptDetailAddnlinfo> receiptDetailsAddnInfo = null;

  public MaterialReceiptDetail id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * The unique identifier for the receipt details.
   * @return id
  **/
  @ApiModelProperty(required = true, value = "The unique identifier for the receipt details.")
  @NotNull


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public MaterialReceiptDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Material Receipt Header
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Material Receipt Header")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MaterialReceiptDetail mrnNumber(MaterialReceipt mrnNumber) {
    this.mrnNumber = mrnNumber;
    return this;
  }

   /**
   * Unique number to identify the maretial receipt note.
   * @return mrnNumber
  **/
  @ApiModelProperty(required = true, value = "Unique number to identify the maretial receipt note.")
  @NotNull


  public MaterialReceipt getMrnNumber() {
    return mrnNumber;
  }

  public void setMrnNumber(MaterialReceipt mrnNumber) {
    this.mrnNumber = mrnNumber;
  }

  public MaterialReceiptDetail material(Material material) {
    this.material = material;
    return this;
  }

   /**
   * Get material
   * @return material
  **/
  @ApiModelProperty(value = "")

  

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public MaterialReceiptDetail uom(Uom uom) {
    this.uom = uom;
    return this;
  }

   /**
   * Auto populate stocking unit of measure of selected material.
   * @return uom
  **/
  @ApiModelProperty(value = "Auto populate stocking unit of measure of selected material.")

  @Valid

  public Uom getUom() {
    return uom;
  }

  public void setUom(Uom uom) {
    this.uom = uom;
  }

  public MaterialReceiptDetail poline(PurchaseOrder poline) {
    this.poline = poline;
    return this;
  }

   /**
   * po line mandatory if receipt type is purchase receipt.
   * @return poline
  **/
  @ApiModelProperty(value = "po line mandatory if receipt type is purchase receipt.")

  @Valid

  public PurchaseOrder getPoline() {
    return poline;
  }

  public void setPoline(PurchaseOrder poline) {
    this.poline = poline;
  }

  public MaterialReceiptDetail receivedQty(BigDecimal receivedQty) {
    this.receivedQty = receivedQty;
    return this;
  }

   /**
   * The quantity of the material received for this particular purchase order or inter store  transfer. All the quantity needs to be saved in the database in the based UOM.
   * @return receivedQty
  **/
  @ApiModelProperty(required = true, value = "The quantity of the material received for this particular purchase order or inter store  transfer. All the quantity needs to be saved in the database in the based UOM.")
  @NotNull

  @Valid

  public BigDecimal getReceivedQty() {
    return receivedQty;
  }

  public void setReceivedQty(BigDecimal receivedQty) {
    this.receivedQty = receivedQty;
  }

  public MaterialReceiptDetail acceptedQty(BigDecimal acceptedQty) {
    this.acceptedQty = acceptedQty;
    return this;
  }

   /**
   * The quantity of the material accepted from the total quantity of materials received. All the quantity needs to be saved in the database in the based UOM. Accepted quantity cannot be greater than the received quantity.
   * @return acceptedQty
  **/
  @ApiModelProperty(value = "The quantity of the material accepted from the total quantity of materials received. All the quantity needs to be saved in the database in the based UOM. Accepted quantity cannot be greater than the received quantity.")

  @Valid

  public BigDecimal getAcceptedQty() {
    return acceptedQty;
  }

  public void setAcceptedQty(BigDecimal acceptedQty) {
    this.acceptedQty = acceptedQty;
  }

  public MaterialReceiptDetail unitRate(BigDecimal unitRate) {
    this.unitRate = unitRate;
    return this;
  }

   /**
   * Per unit rate of the material as per the Purchase Order.
   * @return unitRate
  **/
  @ApiModelProperty(value = "Per unit rate of the material as per the Purchase Order.")

  @Valid

  public BigDecimal getUnitRate() {
    return unitRate;
  }

  public void setUnitRate(BigDecimal unitRate) {
    this.unitRate = unitRate;
  }

  public MaterialReceiptDetail asset(Asset asset) {
    this.asset = asset;
    return this;
  }

   /**
   * asset code if asset received. Create asset in asset module and save the reference.
   * @return asset
  **/
  @ApiModelProperty(value = "asset code if asset received. Create asset in asset module and save the reference.")

  @Valid

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }

  public MaterialReceiptDetail voucherHeader(String voucherHeader) {
    this.voucherHeader = voucherHeader;
    return this;
  }

   /**
   * financial voucher passed id map
   * @return voucherHeader
  **/
  @ApiModelProperty(value = "financial voucher passed id map")


  public String getVoucherHeader() {
    return voucherHeader;
  }

  public void setVoucherHeader(String voucherHeader) {
    this.voucherHeader = voucherHeader;
  }

  public MaterialReceiptDetail rejectionRemark(String rejectionRemark) {
    this.rejectionRemark = rejectionRemark;
    return this;
  }

   /**
   * The reason why certain quantities of materials were rejected.
   * @return rejectionRemark
  **/
  @ApiModelProperty(value = "The reason why certain quantities of materials were rejected.")


  public String getRejectionRemark() {
    return rejectionRemark;
  }

  public void setRejectionRemark(String rejectionRemark) {
    this.rejectionRemark = rejectionRemark;
  }

  public MaterialReceiptDetail remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * General description about the materials.
   * @return remarks
  **/
  @ApiModelProperty(value = "General description about the materials.")

 @Size(max=512)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public MaterialReceiptDetail receiptDetailsAddnInfo(List<MaterialReceiptDetailAddnlinfo> receiptDetailsAddnInfo) {
    this.receiptDetailsAddnInfo = receiptDetailsAddnInfo;
    return this;
  }

  public MaterialReceiptDetail addReceiptDetailsAddnInfoItem(MaterialReceiptDetailAddnlinfo receiptDetailsAddnInfoItem) {
    if (this.receiptDetailsAddnInfo == null) {
      this.receiptDetailsAddnInfo = new ArrayList<MaterialReceiptDetailAddnlinfo>();
    }
    this.receiptDetailsAddnInfo.add(receiptDetailsAddnInfoItem);
    return this;
  }

   /**
   * Material detail additional information referred
   * @return receiptDetailsAddnInfo
  **/
  @ApiModelProperty(value = "Material detail additional information referred")

  @Valid

  public List<MaterialReceiptDetailAddnlinfo> getReceiptDetailsAddnInfo() {
    return receiptDetailsAddnInfo;
  }

  public void setReceiptDetailsAddnInfo(List<MaterialReceiptDetailAddnlinfo> receiptDetailsAddnInfo) {
    this.receiptDetailsAddnInfo = receiptDetailsAddnInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialReceiptDetail materialReceiptDetail = (MaterialReceiptDetail) o;
    return Objects.equals(this.id, materialReceiptDetail.id) &&
        Objects.equals(this.tenantId, materialReceiptDetail.tenantId) &&
        Objects.equals(this.mrnNumber, materialReceiptDetail.mrnNumber) &&
        Objects.equals(this.material, materialReceiptDetail.material) &&
        Objects.equals(this.uom, materialReceiptDetail.uom) &&
        Objects.equals(this.poline, materialReceiptDetail.poline) &&
        Objects.equals(this.receivedQty, materialReceiptDetail.receivedQty) &&
        Objects.equals(this.acceptedQty, materialReceiptDetail.acceptedQty) &&
        Objects.equals(this.unitRate, materialReceiptDetail.unitRate) &&
        Objects.equals(this.asset, materialReceiptDetail.asset) &&
        Objects.equals(this.voucherHeader, materialReceiptDetail.voucherHeader) &&
        Objects.equals(this.rejectionRemark, materialReceiptDetail.rejectionRemark) &&
        Objects.equals(this.remarks, materialReceiptDetail.remarks) &&
        Objects.equals(this.receiptDetailsAddnInfo, materialReceiptDetail.receiptDetailsAddnInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, mrnNumber, material, uom, poline, receivedQty, acceptedQty, unitRate, asset, voucherHeader, rejectionRemark, remarks, receiptDetailsAddnInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialReceiptDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    mrnNumber: ").append(toIndentedString(mrnNumber)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    poline: ").append(toIndentedString(poline)).append("\n");
    sb.append("    receivedQty: ").append(toIndentedString(receivedQty)).append("\n");
    sb.append("    acceptedQty: ").append(toIndentedString(acceptedQty)).append("\n");
    sb.append("    unitRate: ").append(toIndentedString(unitRate)).append("\n");
    sb.append("    asset: ").append(toIndentedString(asset)).append("\n");
    sb.append("    voucherHeader: ").append(toIndentedString(voucherHeader)).append("\n");
    sb.append("    rejectionRemark: ").append(toIndentedString(rejectionRemark)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    receiptDetailsAddnInfo: ").append(toIndentedString(receiptDetailsAddnInfo)).append("\n");
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

