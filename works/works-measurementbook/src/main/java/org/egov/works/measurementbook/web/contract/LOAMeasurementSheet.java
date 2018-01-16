package org.egov.works.measurementbook.web.contract;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds the basic data for LOA Measurement Sheet
 */
@ApiModel(description = "An Object that holds the basic data for LOA Measurement Sheet")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-16T09:56:01.690Z")

public class LOAMeasurementSheet   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("number")
  private BigDecimal number = null;

  @JsonProperty("multiplier")
  private BigDecimal multiplier = null;

  @JsonProperty("length")
  private BigDecimal length = null;

  @JsonProperty("width")
  private BigDecimal width = null;

  @JsonProperty("depthOrHeight")
  private BigDecimal depthOrHeight = null;

  @JsonProperty("quantity")
  private BigDecimal quantity = null;

  @JsonProperty("loaActivity")
  private String loaActivity = null;

  @JsonProperty("estimateMeasurementSheet")
  private String estimateMeasurementSheet = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  public LOAMeasurementSheet id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Measurement Sheet
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Measurement Sheet")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LOAMeasurementSheet tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Measurement Sheet
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Measurement Sheet")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public LOAMeasurementSheet number(BigDecimal number) {
    this.number = number;
    return this;
  }

   /**
   * No. of the Measurement sheet
   * @return number
  **/
  @ApiModelProperty(value = "No. of the Measurement sheet")

  @Valid

  public BigDecimal getNumber() {
    return number;
  }

  public void setNumber(BigDecimal number) {
    this.number = number;
  }

  public LOAMeasurementSheet multiplier(BigDecimal multiplier) {
    this.multiplier = multiplier;
    return this;
  }

   /**
   * Multiplication factor for the number. If the multiplier is entered then number will be multiplied to number.
   * @return multiplier
  **/
  @ApiModelProperty(value = "Multiplication factor for the number. If the multiplier is entered then number will be multiplied to number.")

  @Valid

  public BigDecimal getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(BigDecimal multiplier) {
    this.multiplier = multiplier;
  }

  public LOAMeasurementSheet length(BigDecimal length) {
    this.length = length;
    return this;
  }

   /**
   * Length of the Measurement sheet
   * @return length
  **/
  @ApiModelProperty(value = "Length of the Measurement sheet")

  @Valid

  public BigDecimal getLength() {
    return length;
  }

  public void setLength(BigDecimal length) {
    this.length = length;
  }

  public LOAMeasurementSheet width(BigDecimal width) {
    this.width = width;
    return this;
  }

   /**
   * Width of the Measurement sheet
   * @return width
  **/
  @ApiModelProperty(value = "Width of the Measurement sheet")

  @Valid

  public BigDecimal getWidth() {
    return width;
  }

  public void setWidth(BigDecimal width) {
    this.width = width;
  }

  public LOAMeasurementSheet depthOrHeight(BigDecimal depthOrHeight) {
    this.depthOrHeight = depthOrHeight;
    return this;
  }

   /**
   * Depth or Height of the Measurement sheet
   * @return depthOrHeight
  **/
  @ApiModelProperty(value = "Depth or Height of the Measurement sheet")

  @Valid

  public BigDecimal getDepthOrHeight() {
    return depthOrHeight;
  }

  public void setDepthOrHeight(BigDecimal depthOrHeight) {
    this.depthOrHeight = depthOrHeight;
  }

  public LOAMeasurementSheet quantity(BigDecimal quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Quantity of the Measurement sheet
   * @return quantity
  **/
  @ApiModelProperty(required = true, value = "Quantity of the Measurement sheet")
  @NotNull

  @Valid

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public LOAMeasurementSheet loaActivity(String loaActivity) {
    this.loaActivity = loaActivity;
    return this;
  }

   /**
   * LOA activity reference, primary key is ref here.
   * @return loaActivity
  **/
  @ApiModelProperty(required = true, value = "LOA activity reference, primary key is ref here.")
  @NotNull


  public String getLoaActivity() {
    return loaActivity;
  }

  public void setLoaActivity(String loaActivity) {
    this.loaActivity = loaActivity;
  }

  public LOAMeasurementSheet estimateMeasurementSheet(String estimateMeasurementSheet) {
    this.estimateMeasurementSheet = estimateMeasurementSheet;
    return this;
  }

   /**
   * Estimate Measurement sheet reference
   * @return estimateMeasurementSheet
  **/
  @ApiModelProperty(required = true, value = "Estimate Measurement sheet reference")
  @NotNull


  public String getEstimateMeasurementSheet() {
    return estimateMeasurementSheet;
  }

  public void setEstimateMeasurementSheet(String estimateMeasurementSheet) {
    this.estimateMeasurementSheet = estimateMeasurementSheet;
  }

  public LOAMeasurementSheet auditDetails(AuditDetails auditDetails) {
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

  public LOAMeasurementSheet deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * Boolean value to identify whether the object is deleted or not from UI.
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
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LOAMeasurementSheet loAMeasurementSheet = (LOAMeasurementSheet) o;
    return Objects.equals(this.id, loAMeasurementSheet.id) &&
        Objects.equals(this.tenantId, loAMeasurementSheet.tenantId) &&
        Objects.equals(this.number, loAMeasurementSheet.number) &&
        Objects.equals(this.multiplier, loAMeasurementSheet.multiplier) &&
        Objects.equals(this.length, loAMeasurementSheet.length) &&
        Objects.equals(this.width, loAMeasurementSheet.width) &&
        Objects.equals(this.depthOrHeight, loAMeasurementSheet.depthOrHeight) &&
        Objects.equals(this.quantity, loAMeasurementSheet.quantity) &&
        Objects.equals(this.loaActivity, loAMeasurementSheet.loaActivity) &&
        Objects.equals(this.estimateMeasurementSheet, loAMeasurementSheet.estimateMeasurementSheet) &&
        Objects.equals(this.auditDetails, loAMeasurementSheet.auditDetails) &&
        Objects.equals(this.deleted, loAMeasurementSheet.deleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, number, multiplier, length, width, depthOrHeight, quantity, loaActivity, estimateMeasurementSheet, auditDetails, deleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LOAMeasurementSheet {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    multiplier: ").append(toIndentedString(multiplier)).append("\n");
    sb.append("    length: ").append(toIndentedString(length)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    depthOrHeight: ").append(toIndentedString(depthOrHeight)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    loaActivity: ").append(toIndentedString(loaActivity)).append("\n");
    sb.append("    estimateMeasurementSheet: ").append(toIndentedString(estimateMeasurementSheet)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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

