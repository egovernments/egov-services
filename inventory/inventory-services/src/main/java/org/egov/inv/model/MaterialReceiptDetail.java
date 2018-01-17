package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hold the material receipt note material level information. This will show which material is received based on which purchase order.
 */
@ApiModel(description = "Hold the material receipt note material level information. This will show which material is received based on which purchase order.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-12-27T10:36:36.253Z")

public class MaterialReceiptDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("material")
  private Material material = null;

  @JsonProperty("mrnNumber")
  private String mrnNumber = null;

  @JsonProperty("orderNumber")
  private BigDecimal orderNumber = null;

  @JsonProperty("uom")
  private Uom uom = null;

  @JsonProperty("purchaseOrderDetail")
  private PurchaseOrderDetail purchaseOrderDetail = null;

  @JsonProperty("userReceivedQty")
  private BigDecimal userReceivedQty = null;

  @JsonProperty("receivedQty")
  private BigDecimal receivedQty = null;

  @JsonProperty("userAcceptedQty")
  private BigDecimal userAcceptedQty = null;

  @JsonProperty("acceptedQty")
  private BigDecimal acceptedQty = null;

  @JsonProperty("unitRate")
  private BigDecimal unitRate = null;

  @JsonProperty("openingRate")
  private BigDecimal openingRate = null;

  @JsonProperty("asset")
  private Asset asset = null;

  @JsonProperty("voucherHeader")
  private String voucherHeader = null;

  @JsonProperty("rejectionRemark")
  private String rejectionRemark = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("isScrapItem")
  private Boolean isScrapItem = false;

  @JsonProperty("receiptDetailsAddnInfo")
  private List<MaterialReceiptDetailAddnlinfo> receiptDetailsAddnInfo = null;

  public MaterialReceiptDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * The unique identifier for the receipt details.
   * @return id
  **/
  @ApiModelProperty(required = true, value = "The unique identifier for the receipt details.")
  @NotNull


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MaterialReceiptDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Material Receipt Header.
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Material Receipt Header.")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
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

  public MaterialReceiptDetail mrnNumber(String mrnNumber) {
    this.mrnNumber = mrnNumber;
    return this;
  }

   /**
   * mrn number reference from material receipt.
   * @return mrnNumber
  **/
  @ApiModelProperty(readOnly = true, value = "mrn number reference from material receipt.")


  public String getMrnNumber() {
    return mrnNumber;
  }

  public void setMrnNumber(String mrnNumber) {
    this.mrnNumber = mrnNumber;
  }

  public MaterialReceiptDetail orderNumber(BigDecimal orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

   /**
   * Order of items received.
   * @return orderNumber
  **/
  @ApiModelProperty(value = "Order of items received.")

  

  public BigDecimal getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(BigDecimal orderNumber) {
    this.orderNumber = orderNumber;
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

  

  public Uom getUom() {
    return uom;
  }

  public void setUom(Uom uom) {
    this.uom = uom;
  }

  public MaterialReceiptDetail purchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
    this.purchaseOrderDetail = purchaseOrderDetail;
    return this;
  }

   /**
   * po line mandatory if receipt type is purchase receipt.
   * @return purchaseOrderDetail
  **/
  @ApiModelProperty(value = "po line mandatory if receipt type is purchase receipt.")

  

  public PurchaseOrderDetail getPurchaseOrderDetail() {
    return purchaseOrderDetail;
  }

  public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
    this.purchaseOrderDetail = purchaseOrderDetail;
  }

  public MaterialReceiptDetail userReceivedQty(BigDecimal userReceivedQty) {
    this.userReceivedQty = userReceivedQty;
    return this;
  }

   /**
   * The quantity of the material received for this particular purchase order or inter store  transfer.
   * @return userReceivedQty
  **/
  @ApiModelProperty(required = true, value = "The quantity of the material received for this particular purchase order or inter store  transfer.")
  @NotNull

  

  public BigDecimal getUserReceivedQty() {
    return userReceivedQty;
  }

  public void setUserReceivedQty(BigDecimal userReceivedQty) {
    this.userReceivedQty = userReceivedQty;
  }

  public MaterialReceiptDetail receivedQty(BigDecimal receivedQty) {
    this.receivedQty = receivedQty;
    return this;
  }

   /**
   * The quantity of the material received for this particular purchase order or inter store  transfer in the based UOM.
   * @return receivedQty
  **/
  @ApiModelProperty(value = "The quantity of the material received for this particular purchase order or inter store  transfer in the based UOM.")

  

  public BigDecimal getReceivedQty() {
    return receivedQty;
  }

  public void setReceivedQty(BigDecimal receivedQty) {
    this.receivedQty = receivedQty;
  }

  public MaterialReceiptDetail userAcceptedQty(BigDecimal userAcceptedQty) {
    this.userAcceptedQty = userAcceptedQty;
    return this;
  }

   /**
   * The quantity of the material accepted from the total quantity of materials received.  This field also contains Opening Quantity when the receipt purpose is OPENING BALANCE.
   * @return userAcceptedQty
  **/
  @ApiModelProperty(required = true, value = "The quantity of the material accepted from the total quantity of materials received.  This field also contains Opening Quantity when the receipt purpose is OPENING BALANCE.")
  @NotNull

  

  public BigDecimal getUserAcceptedQty() {
    return userAcceptedQty;
  }

  public void setUserAcceptedQty(BigDecimal userAcceptedQty) {
    this.userAcceptedQty = userAcceptedQty;
  }

  public MaterialReceiptDetail acceptedQty(BigDecimal acceptedQty) {
    this.acceptedQty = acceptedQty;
    return this;
  }

   /**
   * The quantity of the material accepted from the total quantity of materials received in Base UOM. Accepted quantity cannot be greater than the received quantity. This field also contains Opening Quantity when the receipt purpose is OPENING BALANCE.
   * @return acceptedQty
  **/
  @ApiModelProperty(value = "The quantity of the material accepted from the total quantity of materials received in Base UOM. Accepted quantity cannot be greater than the received quantity. This field also contains Opening Quantity when the receipt purpose is OPENING BALANCE.")

  

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

  

  public BigDecimal getUnitRate() {
    return unitRate;
  }

  public void setUnitRate(BigDecimal unitRate) {
    this.unitRate = unitRate;
  }

  public MaterialReceiptDetail openingRate(BigDecimal openingRate) {
    this.openingRate = openingRate;
    return this;
  }

   /**
   * Opening rate, applicable for Material Opening Balance Entry.
   * @return openingRate
  **/
  @ApiModelProperty(value = "Opening rate, applicable for Material Opening Balance Entry.")

  

  public BigDecimal getOpeningRate() {
    return openingRate;
  }

  public void setOpeningRate(BigDecimal openingRate) {
    this.openingRate = openingRate;
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

  public MaterialReceiptDetail isScrapItem(Boolean isScrapItem) {
    this.isScrapItem = isScrapItem;
    return this;
  }

   /**
   * is scrap item flag. Donot use this data to issue material and show in any stock ledger report.
   * @return isScrapItem
  **/
  @ApiModelProperty(value = "is scrap item flag. Donot use this data to issue material and show in any stock ledger report.")


  public Boolean getIsScrapItem() {
    return isScrapItem;
  }

  public void setIsScrapItem(Boolean isScrapItem) {
    this.isScrapItem = isScrapItem;
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
        Objects.equals(this.material, materialReceiptDetail.material) &&
        Objects.equals(this.mrnNumber, materialReceiptDetail.mrnNumber) &&
        Objects.equals(this.orderNumber, materialReceiptDetail.orderNumber) &&
        Objects.equals(this.uom, materialReceiptDetail.uom) &&
        Objects.equals(this.purchaseOrderDetail, materialReceiptDetail.purchaseOrderDetail) &&
        Objects.equals(this.userReceivedQty, materialReceiptDetail.userReceivedQty) &&
        Objects.equals(this.receivedQty, materialReceiptDetail.receivedQty) &&
        Objects.equals(this.userAcceptedQty, materialReceiptDetail.userAcceptedQty) &&
        Objects.equals(this.acceptedQty, materialReceiptDetail.acceptedQty) &&
        Objects.equals(this.unitRate, materialReceiptDetail.unitRate) &&
        Objects.equals(this.openingRate, materialReceiptDetail.openingRate) &&
        Objects.equals(this.asset, materialReceiptDetail.asset) &&
        Objects.equals(this.voucherHeader, materialReceiptDetail.voucherHeader) &&
        Objects.equals(this.rejectionRemark, materialReceiptDetail.rejectionRemark) &&
        Objects.equals(this.remarks, materialReceiptDetail.remarks) &&
        Objects.equals(this.isScrapItem, materialReceiptDetail.isScrapItem) &&
        Objects.equals(this.receiptDetailsAddnInfo, materialReceiptDetail.receiptDetailsAddnInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, material, mrnNumber, orderNumber, uom, purchaseOrderDetail, userReceivedQty, receivedQty, userAcceptedQty, acceptedQty, unitRate, openingRate, asset, voucherHeader, rejectionRemark, remarks, isScrapItem, receiptDetailsAddnInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialReceiptDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    mrnNumber: ").append(toIndentedString(mrnNumber)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    purchaseOrderDetail: ").append(toIndentedString(purchaseOrderDetail)).append("\n");
    sb.append("    userReceivedQty: ").append(toIndentedString(userReceivedQty)).append("\n");
    sb.append("    receivedQty: ").append(toIndentedString(receivedQty)).append("\n");
    sb.append("    userAcceptedQty: ").append(toIndentedString(userAcceptedQty)).append("\n");
    sb.append("    acceptedQty: ").append(toIndentedString(acceptedQty)).append("\n");
    sb.append("    unitRate: ").append(toIndentedString(unitRate)).append("\n");
    sb.append("    openingRate: ").append(toIndentedString(openingRate)).append("\n");
    sb.append("    asset: ").append(toIndentedString(asset)).append("\n");
    sb.append("    voucherHeader: ").append(toIndentedString(voucherHeader)).append("\n");
    sb.append("    rejectionRemark: ").append(toIndentedString(rejectionRemark)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    isScrapItem: ").append(toIndentedString(isScrapItem)).append("\n");
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

