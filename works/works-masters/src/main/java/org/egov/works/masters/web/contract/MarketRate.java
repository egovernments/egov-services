package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object that hold Market Rate Details for a given SOR
 */
@ApiModel(description = "An Object that hold Market Rate Details for a given SOR")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-04T10:03:03.894Z")

public class MarketRate   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("scheduleOfRate")
  private String scheduleOfRate = null;

  @JsonProperty("fromDate")
  private Long fromDate = null;

  @JsonProperty("toDate")
  private Long toDate = null;

  @JsonProperty("rate")
  private BigDecimal rate = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public MarketRate id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Market Rate Details
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Market Rate Details")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MarketRate tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Market Rate Details
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Market Rate Details")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MarketRate scheduleOfRate(String scheduleOfRate) {
    this.scheduleOfRate = scheduleOfRate;
    return this;
  }

   /**
   * unique reference from 'ScheduleOfRate'. Primary key is ref. here.
   * @return scheduleOfRate
  **/
  @ApiModelProperty(value = "unique reference from 'ScheduleOfRate'. Primary key is ref. here.")


  public String getScheduleOfRate() {
    return scheduleOfRate;
  }

  public void setScheduleOfRate(String scheduleOfRate) {
    this.scheduleOfRate = scheduleOfRate;
  }

  public MarketRate fromDate(Long fromDate) {
    this.fromDate = fromDate;
    return this;
  }

   /**
   * Epoch time of From Date for the Market Rate Detail.
   * @return fromDate
  **/
  @ApiModelProperty(required = true, value = "Epoch time of From Date for the Market Rate Detail.")
  @NotNull


  public Long getFromDate() {
    return fromDate;
  }

  public void setFromDate(Long fromDate) {
    this.fromDate = fromDate;
  }

  public MarketRate toDate(Long toDate) {
    this.toDate = toDate;
    return this;
  }

   /**
   * Epoch time of To date for the Market Rate Details.
   * @return toDate
  **/
  @ApiModelProperty(value = "Epoch time of To date for the Market Rate Details.")


  public Long getToDate() {
    return toDate;
  }

  public void setToDate(Long toDate) {
    this.toDate = toDate;
  }

  public MarketRate rate(BigDecimal rate) {
    this.rate = rate;
    return this;
  }

   /**
   * Market Rate which is applicable for given date range
   * @return rate
  **/
  @ApiModelProperty(required = true, value = "Market Rate which is applicable for given date range")
  @NotNull

  @Valid

  public BigDecimal getRate() {
    return rate;
  }

  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

  public MarketRate deleted(Boolean deleted) {
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

  public MarketRate auditDetails(AuditDetails auditDetails) {
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketRate marketRate = (MarketRate) o;
    return Objects.equals(this.id, marketRate.id) &&
        Objects.equals(this.tenantId, marketRate.tenantId) &&
        Objects.equals(this.scheduleOfRate, marketRate.scheduleOfRate) &&
        Objects.equals(this.fromDate, marketRate.fromDate) &&
        Objects.equals(this.toDate, marketRate.toDate) &&
        Objects.equals(this.rate, marketRate.rate) &&
        Objects.equals(this.deleted, marketRate.deleted) &&
        Objects.equals(this.auditDetails, marketRate.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, scheduleOfRate, fromDate, toDate, rate, deleted, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketRate {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    scheduleOfRate: ").append(toIndentedString(scheduleOfRate)).append("\n");
    sb.append("    fromDate: ").append(toIndentedString(fromDate)).append("\n");
    sb.append("    toDate: ").append(toIndentedString(toDate)).append("\n");
    sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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

