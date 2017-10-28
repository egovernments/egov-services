package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.IndentDetail;
import io.swagger.model.IndentIssue;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class IndentIssueDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("indentIssue")
  private IndentIssue indentIssue = null;

  @JsonProperty("indentDetail")
  private IndentDetail indentDetail = null;

  @JsonProperty("quantityIssued")
  private BigDecimal quantityIssued = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public IndentIssueDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Indent Issue Details 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Indent Issue Details ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public IndentIssueDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Indent Issue Details
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Indent Issue Details")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public IndentIssueDetail indentIssue(IndentIssue indentIssue) {
    this.indentIssue = indentIssue;
    return this;
  }

   /**
   * Get indentIssue
   * @return indentIssue
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public IndentIssue getIndentIssue() {
    return indentIssue;
  }

  public void setIndentIssue(IndentIssue indentIssue) {
    this.indentIssue = indentIssue;
  }

  public IndentIssueDetail indentDetail(IndentDetail indentDetail) {
    this.indentDetail = indentDetail;
    return this;
  }

   /**
   * Get indentDetail
   * @return indentDetail
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public IndentDetail getIndentDetail() {
    return indentDetail;
  }

  public void setIndentDetail(IndentDetail indentDetail) {
    this.indentDetail = indentDetail;
  }

  public IndentIssueDetail quantityIssued(BigDecimal quantityIssued) {
    this.quantityIssued = quantityIssued;
    return this;
  }

   /**
   * quantity issued of the IndentIssueDetail 
   * @return quantityIssued
  **/
  @ApiModelProperty(required = true, value = "quantity issued of the IndentIssueDetail ")
  @NotNull

  @Valid

  public BigDecimal getQuantityIssued() {
    return quantityIssued;
  }

  public void setQuantityIssued(BigDecimal quantityIssued) {
    this.quantityIssued = quantityIssued;
  }

  public IndentIssueDetail description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the IndentIssueDetail 
   * @return description
  **/
  @ApiModelProperty(value = "description of the IndentIssueDetail ")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public IndentIssueDetail auditDetails(AuditDetails auditDetails) {
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
    IndentIssueDetail indentIssueDetail = (IndentIssueDetail) o;
    return Objects.equals(this.id, indentIssueDetail.id) &&
        Objects.equals(this.tenantId, indentIssueDetail.tenantId) &&
        Objects.equals(this.indentIssue, indentIssueDetail.indentIssue) &&
        Objects.equals(this.indentDetail, indentIssueDetail.indentDetail) &&
        Objects.equals(this.quantityIssued, indentIssueDetail.quantityIssued) &&
        Objects.equals(this.description, indentIssueDetail.description) &&
        Objects.equals(this.auditDetails, indentIssueDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, indentIssue, indentDetail, quantityIssued, description, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndentIssueDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    indentIssue: ").append(toIndentedString(indentIssue)).append("\n");
    sb.append("    indentDetail: ").append(toIndentedString(indentDetail)).append("\n");
    sb.append("    quantityIssued: ").append(toIndentedString(quantityIssued)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

