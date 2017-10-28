package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.PurchaseOrder;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Hold the material receipt note material level information. This will show which material is received based on which purchase order.
 */
@ApiModel(description = "Hold the material receipt note material level information. This will show which material is received based on which purchase order.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class MaterialReceiptDetail   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("mrnNumber")
  private String mrnNumber = null;

  @JsonProperty("poline")
  private PurchaseOrder poline = null;

  @JsonProperty("receivedQty")
  private BigDecimal receivedQty = null;

  @JsonProperty("acceptedQty")
  private BigDecimal acceptedQty = null;

  @JsonProperty("challanQty")
  private BigDecimal challanQty = null;

  @JsonProperty("challanRate")
  private BigDecimal challanRate = null;

  @JsonProperty("unitRate")
  private BigDecimal unitRate = null;

  @JsonProperty("rejectionRemark")
  private String rejectionRemark = null;

  @JsonProperty("lotNo")
  private String lotNo = null;

  @JsonProperty("serialNo")
  private String serialNo = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("manufactureDate")
  private Long manufactureDate = null;

  @JsonProperty("expiryDate")
  private Long expiryDate = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

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

  public MaterialReceiptDetail mrnNumber(String mrnNumber) {
    this.mrnNumber = mrnNumber;
    return this;
  }

   /**
   * Unique number to identify the maretial receipt note.
   * @return mrnNumber
  **/
  @ApiModelProperty(required = true, value = "Unique number to identify the maretial receipt note.")
  @NotNull


  public String getMrnNumber() {
    return mrnNumber;
  }

  public void setMrnNumber(String mrnNumber) {
    this.mrnNumber = mrnNumber;
  }

  public MaterialReceiptDetail poline(PurchaseOrder poline) {
    this.poline = poline;
    return this;
  }

   /**
   * Get poline
   * @return poline
  **/
  @ApiModelProperty(value = "")

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
   * The quantity of the material received for this particular purchase order. All the quantity needs to be saved in the database in the based UOM.
   * @return receivedQty
  **/
  @ApiModelProperty(required = true, value = "The quantity of the material received for this particular purchase order. All the quantity needs to be saved in the database in the based UOM.")
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

  public MaterialReceiptDetail challanQty(BigDecimal challanQty) {
    this.challanQty = challanQty;
    return this;
  }

   /**
   * The quantity of the material as per the challan in the supplier bill.
   * @return challanQty
  **/
  @ApiModelProperty(value = "The quantity of the material as per the challan in the supplier bill.")

  @Valid

  public BigDecimal getChallanQty() {
    return challanQty;
  }

  public void setChallanQty(BigDecimal challanQty) {
    this.challanQty = challanQty;
  }

  public MaterialReceiptDetail challanRate(BigDecimal challanRate) {
    this.challanRate = challanRate;
    return this;
  }

   /**
   * Rate per unit as mentioned in the challan in the supplier bill.
   * @return challanRate
  **/
  @ApiModelProperty(value = "Rate per unit as mentioned in the challan in the supplier bill.")

  @Valid

  public BigDecimal getChallanRate() {
    return challanRate;
  }

  public void setChallanRate(BigDecimal challanRate) {
    this.challanRate = challanRate;
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

  public MaterialReceiptDetail lotNo(String lotNo) {
    this.lotNo = lotNo;
    return this;
  }

   /**
   * LOT number for the materials that are of LOT control.
   * @return lotNo
  **/
  @ApiModelProperty(value = "LOT number for the materials that are of LOT control.")


  public String getLotNo() {
    return lotNo;
  }

  public void setLotNo(String lotNo) {
    this.lotNo = lotNo;
  }

  public MaterialReceiptDetail serialNo(String serialNo) {
    this.serialNo = serialNo;
    return this;
  }

   /**
   * Serial number of the materials that are received.
   * @return serialNo
  **/
  @ApiModelProperty(value = "Serial number of the materials that are received.")


  public String getSerialNo() {
    return serialNo;
  }

  public void setSerialNo(String serialNo) {
    this.serialNo = serialNo;
  }

  public MaterialReceiptDetail description(String description) {
    this.description = description;
    return this;
  }

   /**
   * General description about the materials.
   * @return description
  **/
  @ApiModelProperty(value = "General description about the materials.")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MaterialReceiptDetail manufactureDate(Long manufactureDate) {
    this.manufactureDate = manufactureDate;
    return this;
  }

   /**
   * The date on which the materials are manufactured.
   * @return manufactureDate
  **/
  @ApiModelProperty(required = true, value = "The date on which the materials are manufactured.")
  @NotNull


  public Long getManufactureDate() {
    return manufactureDate;
  }

  public void setManufactureDate(Long manufactureDate) {
    this.manufactureDate = manufactureDate;
  }

  public MaterialReceiptDetail expiryDate(Long expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

   /**
   * The date of expiry of the materials. This is required in case of materials that are of type shelf life control. Date must be greater than the receipt date.
   * @return expiryDate
  **/
  @ApiModelProperty(value = "The date of expiry of the materials. This is required in case of materials that are of type shelf life control. Date must be greater than the receipt date.")


  public Long getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Long expiryDate) {
    this.expiryDate = expiryDate;
  }

  public MaterialReceiptDetail auditDetails(AuditDetails auditDetails) {
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
    MaterialReceiptDetail materialReceiptDetail = (MaterialReceiptDetail) o;
    return Objects.equals(this.id, materialReceiptDetail.id) &&
        Objects.equals(this.tenantId, materialReceiptDetail.tenantId) &&
        Objects.equals(this.mrnNumber, materialReceiptDetail.mrnNumber) &&
        Objects.equals(this.poline, materialReceiptDetail.poline) &&
        Objects.equals(this.receivedQty, materialReceiptDetail.receivedQty) &&
        Objects.equals(this.acceptedQty, materialReceiptDetail.acceptedQty) &&
        Objects.equals(this.challanQty, materialReceiptDetail.challanQty) &&
        Objects.equals(this.challanRate, materialReceiptDetail.challanRate) &&
        Objects.equals(this.unitRate, materialReceiptDetail.unitRate) &&
        Objects.equals(this.rejectionRemark, materialReceiptDetail.rejectionRemark) &&
        Objects.equals(this.lotNo, materialReceiptDetail.lotNo) &&
        Objects.equals(this.serialNo, materialReceiptDetail.serialNo) &&
        Objects.equals(this.description, materialReceiptDetail.description) &&
        Objects.equals(this.manufactureDate, materialReceiptDetail.manufactureDate) &&
        Objects.equals(this.expiryDate, materialReceiptDetail.expiryDate) &&
        Objects.equals(this.auditDetails, materialReceiptDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, mrnNumber, poline, receivedQty, acceptedQty, challanQty, challanRate, unitRate, rejectionRemark, lotNo, serialNo, description, manufactureDate, expiryDate, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialReceiptDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    mrnNumber: ").append(toIndentedString(mrnNumber)).append("\n");
    sb.append("    poline: ").append(toIndentedString(poline)).append("\n");
    sb.append("    receivedQty: ").append(toIndentedString(receivedQty)).append("\n");
    sb.append("    acceptedQty: ").append(toIndentedString(acceptedQty)).append("\n");
    sb.append("    challanQty: ").append(toIndentedString(challanQty)).append("\n");
    sb.append("    challanRate: ").append(toIndentedString(challanRate)).append("\n");
    sb.append("    unitRate: ").append(toIndentedString(unitRate)).append("\n");
    sb.append("    rejectionRemark: ").append(toIndentedString(rejectionRemark)).append("\n");
    sb.append("    lotNo: ").append(toIndentedString(lotNo)).append("\n");
    sb.append("    serialNo: ").append(toIndentedString(serialNo)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    manufactureDate: ").append(toIndentedString(manufactureDate)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
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

