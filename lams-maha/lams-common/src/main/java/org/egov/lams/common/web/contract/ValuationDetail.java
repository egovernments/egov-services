package org.egov.lams.common.web.contract;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Valuation details of land acquisition
 */
@ApiModel(description = "Valuation details of land acquisition")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:54:53.690Z")

public class ValuationDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("valuationYear")
  private BigDecimal valuationYear = null;

  @JsonProperty("valuationRate")
  private Double valuationRate = null;

  @JsonProperty("valuationAmount")
  private Double valuationAmount = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("landAcquisition")
  private LandAcquisition landAcquisition = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public ValuationDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the land valuation
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the land valuation")

 @Size(max=64)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ValuationDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the Land Acquisition
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the Land Acquisition")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ValuationDetail valuationYear(BigDecimal valuationYear) {
    this.valuationYear = valuationYear;
    return this;
  }

   /**
   * Valuation year.
   * @return valuationYear
  **/
  @ApiModelProperty(required = true, value = "Valuation year.")
  @NotNull

  @Valid

  public BigDecimal getValuationYear() {
    return valuationYear;
  }

  public void setValuationYear(BigDecimal valuationYear) {
    this.valuationYear = valuationYear;
  }

  public ValuationDetail valuationRate(Double valuationRate) {
    this.valuationRate = valuationRate;
    return this;
  }

   /**
   * valuation rate
   * @return valuationRate
  **/
  @ApiModelProperty(required = true, value = "valuation rate")
  @NotNull


  public Double getValuationRate() {
    return valuationRate;
  }

  public void setValuationRate(Double valuationRate) {
    this.valuationRate = valuationRate;
  }

  public ValuationDetail valuationAmount(Double valuationAmount) {
    this.valuationAmount = valuationAmount;
    return this;
  }

   /**
   * valuation amount
   * @return valuationAmount
  **/
  @ApiModelProperty(required = true, value = "valuation amount")
  @NotNull


  public Double getValuationAmount() {
    return valuationAmount;
  }

  public void setValuationAmount(Double valuationAmount) {
    this.valuationAmount = valuationAmount;
  }

  public ValuationDetail remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * remarks about land acquisition
   * @return remarks
  **/
  @ApiModelProperty(value = "remarks about land acquisition")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]") @Size(max=512)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public ValuationDetail landAcquisition(LandAcquisition landAcquisition) {
    this.landAcquisition = landAcquisition;
    return this;
  }

   /**
   * refer id of landacquisition
   * @return landAcquisition
  **/
  @ApiModelProperty(required = true, value = "refer id of landacquisition")
  @NotNull

  @Valid

  public LandAcquisition getLandAcquisition() {
    return landAcquisition;
  }

  public void setLandAcquisition(LandAcquisition landAcquisition) {
    this.landAcquisition = landAcquisition;
  }

  public ValuationDetail auditDetails(AuditDetails auditDetails) {
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
    ValuationDetail valuationDetail = (ValuationDetail) o;
    return Objects.equals(this.id, valuationDetail.id) &&
        Objects.equals(this.tenantId, valuationDetail.tenantId) &&
        Objects.equals(this.valuationYear, valuationDetail.valuationYear) &&
        Objects.equals(this.valuationRate, valuationDetail.valuationRate) &&
        Objects.equals(this.valuationAmount, valuationDetail.valuationAmount) &&
        Objects.equals(this.remarks, valuationDetail.remarks) &&
        Objects.equals(this.landAcquisition, valuationDetail.landAcquisition) &&
        Objects.equals(this.auditDetails, valuationDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, valuationYear, valuationRate, valuationAmount, remarks, landAcquisition, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValuationDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    valuationYear: ").append(toIndentedString(valuationYear)).append("\n");
    sb.append("    valuationRate: ").append(toIndentedString(valuationRate)).append("\n");
    sb.append("    valuationAmount: ").append(toIndentedString(valuationAmount)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    landAcquisition: ").append(toIndentedString(landAcquisition)).append("\n");
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

