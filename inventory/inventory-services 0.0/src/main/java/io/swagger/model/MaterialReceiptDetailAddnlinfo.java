package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.MaterialReceiptDetail;
import lombok.Builder;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Hold the material receipt note material level  additional information. This will helpful to maintain/issue material in lot number,expiry date wise, serial number wise and manufacture number wise. This object will be useful in legacy data collection also.
 */
@ApiModel(description = "Hold the material receipt note material level  additional information. This will helpful to maintain/issue material in lot number,expiry date wise, serial number wise and manufacture number wise. This object will be useful in legacy data collection also.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-08T06:17:26.594Z")
@Builder 

public class MaterialReceiptDetailAddnlinfo   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("materialReceiptDetail")
  private MaterialReceiptDetail materialReceiptDetail = null;

  @JsonProperty("lotNo")
  private String lotNo = null;

  @JsonProperty("serialNo")
  private String serialNo = null;

  @JsonProperty("manufactureDate")
  private Long manufactureDate = null;

  @JsonProperty("oldReceiptNumber")
  private String oldReceiptNumber = null;

  @JsonProperty("receivedDate")
  private Long receivedDate = null;

  @JsonProperty("expiryDate")
  private Long expiryDate = null;

  public MaterialReceiptDetailAddnlinfo id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * The unique identifier for the receipt details additional information.
   * @return id
  **/
  @ApiModelProperty(value = "The unique identifier for the receipt details additional information.")


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public MaterialReceiptDetailAddnlinfo tenantId(String tenantId) {
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

  public MaterialReceiptDetailAddnlinfo materialReceiptDetail(MaterialReceiptDetail materialReceiptDetail) {
    this.materialReceiptDetail = materialReceiptDetail;
    return this;
  }

   /**
   * Get materialReceiptDetail
   * @return materialReceiptDetail
  **/
  @ApiModelProperty(value = "")

  @Valid

  public MaterialReceiptDetail getMaterialReceiptDetail() {
    return materialReceiptDetail;
  }

  public void setMaterialReceiptDetail(MaterialReceiptDetail materialReceiptDetail) {
    this.materialReceiptDetail = materialReceiptDetail;
  }

  public MaterialReceiptDetailAddnlinfo lotNo(String lotNo) {
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

  public MaterialReceiptDetailAddnlinfo serialNo(String serialNo) {
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

  public MaterialReceiptDetailAddnlinfo manufactureDate(Long manufactureDate) {
    this.manufactureDate = manufactureDate;
    return this;
  }

   /**
   * The date on which the materials are received into system.
   * @return manufactureDate
  **/
  @ApiModelProperty(value = "The date on which the materials are received into system.")


  public Long getManufactureDate() {
    return manufactureDate;
  }

  public void setManufactureDate(Long manufactureDate) {
    this.manufactureDate = manufactureDate;
  }

  public MaterialReceiptDetailAddnlinfo oldReceiptNumber(String oldReceiptNumber) {
    this.oldReceiptNumber = oldReceiptNumber;
    return this;
  }

   /**
   * old receipt number. Field used in opening balance screen.
   * @return oldReceiptNumber
  **/
  @ApiModelProperty(value = "old receipt number. Field used in opening balance screen.")


  public String getOldReceiptNumber() {
    return oldReceiptNumber;
  }

  public void setOldReceiptNumber(String oldReceiptNumber) {
    this.oldReceiptNumber = oldReceiptNumber;
  }

  public MaterialReceiptDetailAddnlinfo receivedDate(Long receivedDate) {
    this.receivedDate = receivedDate;
    return this;
  }

   /**
   * The date on which the materials are manufactured. Useful in case of opening balance screen
   * @return receivedDate
  **/
  @ApiModelProperty(value = "The date on which the materials are manufactured. Useful in case of opening balance screen")


  public Long getReceivedDate() {
    return receivedDate;
  }

  public void setReceivedDate(Long receivedDate) {
    this.receivedDate = receivedDate;
  }

  public MaterialReceiptDetailAddnlinfo expiryDate(Long expiryDate) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialReceiptDetailAddnlinfo materialReceiptDetailAddnlinfo = (MaterialReceiptDetailAddnlinfo) o;
    return Objects.equals(this.id, materialReceiptDetailAddnlinfo.id) &&
        Objects.equals(this.tenantId, materialReceiptDetailAddnlinfo.tenantId) &&
        Objects.equals(this.materialReceiptDetail, materialReceiptDetailAddnlinfo.materialReceiptDetail) &&
        Objects.equals(this.lotNo, materialReceiptDetailAddnlinfo.lotNo) &&
        Objects.equals(this.serialNo, materialReceiptDetailAddnlinfo.serialNo) &&
        Objects.equals(this.manufactureDate, materialReceiptDetailAddnlinfo.manufactureDate) &&
        Objects.equals(this.oldReceiptNumber, materialReceiptDetailAddnlinfo.oldReceiptNumber) &&
        Objects.equals(this.receivedDate, materialReceiptDetailAddnlinfo.receivedDate) &&
        Objects.equals(this.expiryDate, materialReceiptDetailAddnlinfo.expiryDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, materialReceiptDetail, lotNo, serialNo, manufactureDate, oldReceiptNumber, receivedDate, expiryDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialReceiptDetailAddnlinfo {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    materialReceiptDetail: ").append(toIndentedString(materialReceiptDetail)).append("\n");
    sb.append("    lotNo: ").append(toIndentedString(lotNo)).append("\n");
    sb.append("    serialNo: ").append(toIndentedString(serialNo)).append("\n");
    sb.append("    manufactureDate: ").append(toIndentedString(manufactureDate)).append("\n");
    sb.append("    oldReceiptNumber: ").append(toIndentedString(oldReceiptNumber)).append("\n");
    sb.append("    receivedDate: ").append(toIndentedString(receivedDate)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
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

