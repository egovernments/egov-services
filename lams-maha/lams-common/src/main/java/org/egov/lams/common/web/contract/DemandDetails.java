package org.egov.lams.common.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A object holds a demand and collection values for a tax head and period.
 */
@ApiModel(description = "A object holds a demand and collection values for a tax head and period.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class DemandDetails   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("taxHead")
  private String taxHead = null;

  @JsonProperty("taxAmount")
  private Double taxAmount = null;

  @JsonProperty("collectionAmount")
  private Double collectionAmount = 0.0d;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  public DemandDetails id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * primary key of a DemandDetails.
   * @return id
  **/
  @ApiModelProperty(value = "primary key of a DemandDetails.")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public DemandDetails taxHead(String taxHead) {
    this.taxHead = taxHead;
    return this;
  }

   /**
   * Tax Head code.
   * @return taxHead
  **/
  @ApiModelProperty(required = true, value = "Tax Head code.")
  @NotNull

 @Size(min=2,max=64)
  public String getTaxHead() {
    return taxHead;
  }

  public void setTaxHead(String taxHead) {
    this.taxHead = taxHead;
  }

  public DemandDetails taxAmount(Double taxAmount) {
    this.taxAmount = taxAmount;
    return this;
  }

   /**
   * demand amount.
   * @return taxAmount
  **/
  @ApiModelProperty(required = true, value = "demand amount.")
  @NotNull


  public Double getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(Double taxAmount) {
    this.taxAmount = taxAmount;
  }

  public DemandDetails collectionAmount(Double collectionAmount) {
    this.collectionAmount = collectionAmount;
    return this;
  }

   /**
   * collection against taxAmount.
   * @return collectionAmount
  **/
  @ApiModelProperty(required = true, value = "collection against taxAmount.")
  @NotNull


  public Double getCollectionAmount() {
    return collectionAmount;
  }

  public void setCollectionAmount(Double collectionAmount) {
    this.collectionAmount = collectionAmount;
  }

  public DemandDetails auditDetails(AuditDetails auditDetails) {
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

  public DemandDetails tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique Identifier of the tenant
   * @return tenantId
  **/
  @ApiModelProperty(required = true, readOnly = true, value = "Unique Identifier of the tenant")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemandDetails demandDetails = (DemandDetails) o;
    return Objects.equals(this.id, demandDetails.id) &&
        Objects.equals(this.taxHead, demandDetails.taxHead) &&
        Objects.equals(this.taxAmount, demandDetails.taxAmount) &&
        Objects.equals(this.collectionAmount, demandDetails.collectionAmount) &&
        Objects.equals(this.auditDetails, demandDetails.auditDetails) &&
        Objects.equals(this.tenantId, demandDetails.tenantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, taxHead, taxAmount, collectionAmount, auditDetails, tenantId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemandDetails {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    taxHead: ").append(toIndentedString(taxHead)).append("\n");
    sb.append("    taxAmount: ").append(toIndentedString(taxAmount)).append("\n");
    sb.append("    collectionAmount: ").append(toIndentedString(collectionAmount)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
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

