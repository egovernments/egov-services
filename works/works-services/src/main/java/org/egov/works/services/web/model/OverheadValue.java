package org.egov.works.services.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object holds the basic data for a Over Head Value
 */
@ApiModel(description = "An Object holds the basic data for a Over Head Value")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-25T07:37:26.972Z")

public class OverheadValue   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("overhead")
  private Overhead overhead = null;

  @JsonProperty("amount")
  private Integer amount = null;

  @JsonProperty("detailedEstimate")
  private DetailedEstimate detailedEstimate = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public OverheadValue id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Overhead Value
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Overhead Value")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OverheadValue tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Overhead Value
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Overhead Value")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public OverheadValue overhead(Overhead overhead) {
    this.overhead = overhead;
    return this;
  }

   /**
   * Get overhead
   * @return overhead
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Overhead getOverhead() {
    return overhead;
  }

  public void setOverhead(Overhead overhead) {
    this.overhead = overhead;
  }

  public OverheadValue amount(Integer amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Overhead amount
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "Overhead amount")
  @NotNull


  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public OverheadValue detailedEstimate(DetailedEstimate detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
    return this;
  }

   /**
   * Get detailedEstimate
   * @return detailedEstimate
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public DetailedEstimate getDetailedEstimate() {
    return detailedEstimate;
  }

  public void setDetailedEstimate(DetailedEstimate detailedEstimate) {
    this.detailedEstimate = detailedEstimate;
  }

  public OverheadValue auditDetails(AuditDetails auditDetails) {
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
    OverheadValue overheadValue = (OverheadValue) o;
    return Objects.equals(this.id, overheadValue.id) &&
        Objects.equals(this.tenantId, overheadValue.tenantId) &&
        Objects.equals(this.overhead, overheadValue.overhead) &&
        Objects.equals(this.amount, overheadValue.amount) &&
        Objects.equals(this.detailedEstimate, overheadValue.detailedEstimate) &&
        Objects.equals(this.auditDetails, overheadValue.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, overhead, amount, detailedEstimate, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OverheadValue {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    overhead: ").append(toIndentedString(overhead)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
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

