package org.egov.works.services.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object that hold Abstract Estimate Detail for a given Abstract Estimate
 */
@ApiModel(description = "An Object that hold Abstract Estimate Detail for a given Abstract Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-25T07:37:26.972Z")

public class AbstractEstimateDetails   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("abstractEstimate")
  private AbstractEstimate abstractEstimate = null;

  @JsonProperty("nameOfWork")
  private String nameOfWork = null;

  @JsonProperty("estimateAmount")
  private BigDecimal estimateAmount = null;

  @JsonProperty("estimateNumber")
  private String estimateNumber = null;

  @JsonProperty("grossAmountBilled")
  private Double grossAmountBilled = null;

  @JsonProperty("projectCode")
  private ProjectCode projectCode = null;

  @JsonProperty("abstractEstimateAppropriations")
  private EstimateAppropriation abstractEstimateAppropriations = null;

  @JsonProperty("estimatePhotographsList")
  private DocumentDetail estimatePhotographsList = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public AbstractEstimateDetails id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Abstract Estimate Detail
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Abstract Estimate Detail")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AbstractEstimateDetails tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Abstract Estimate Details
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Abstract Estimate Details")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AbstractEstimateDetails abstractEstimate(AbstractEstimate abstractEstimate) {
    this.abstractEstimate = abstractEstimate;
    return this;
  }

   /**
   * Get abstractEstimate
   * @return abstractEstimate
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AbstractEstimate getAbstractEstimate() {
    return abstractEstimate;
  }

  public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
    this.abstractEstimate = abstractEstimate;
  }

  public AbstractEstimateDetails nameOfWork(String nameOfWork) {
    this.nameOfWork = nameOfWork;
    return this;
  }

   /**
   * Name Of Work of the Abstract Estimate Details
   * @return nameOfWork
  **/
  @ApiModelProperty(required = true, value = "Name Of Work of the Abstract Estimate Details")
  @NotNull

 @Size(min=1,max=1024)
  public String getNameOfWork() {
    return nameOfWork;
  }

  public void setNameOfWork(String nameOfWork) {
    this.nameOfWork = nameOfWork;
  }

  public AbstractEstimateDetails estimateAmount(BigDecimal estimateAmount) {
    this.estimateAmount = estimateAmount;
    return this;
  }

   /**
   * Estimate amount for the Abstract Estimate Details
   * @return estimateAmount
  **/
  @ApiModelProperty(required = true, value = "Estimate amount for the Abstract Estimate Details")
  @NotNull

  @Valid

  public BigDecimal getEstimateAmount() {
    return estimateAmount;
  }

  public void setEstimateAmount(BigDecimal estimateAmount) {
    this.estimateAmount = estimateAmount;
  }

  public AbstractEstimateDetails estimateNumber(String estimateNumber) {
    this.estimateNumber = estimateNumber;
    return this;
  }

   /**
   * Estimate number of the Abstract Estimate Details.
   * @return estimateNumber
  **/
  @ApiModelProperty(required = true, value = "Estimate number of the Abstract Estimate Details.")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-\\\\]") @Size(min=1,max=50)
  public String getEstimateNumber() {
    return estimateNumber;
  }

  public void setEstimateNumber(String estimateNumber) {
    this.estimateNumber = estimateNumber;
  }

  public AbstractEstimateDetails grossAmountBilled(Double grossAmountBilled) {
    this.grossAmountBilled = grossAmountBilled;
    return this;
  }

   /**
   * Gross Billed amount of the Abstract Estimate Details
   * @return grossAmountBilled
  **/
  @ApiModelProperty(value = "Gross Billed amount of the Abstract Estimate Details")


  public Double getGrossAmountBilled() {
    return grossAmountBilled;
  }

  public void setGrossAmountBilled(Double grossAmountBilled) {
    this.grossAmountBilled = grossAmountBilled;
  }

  public AbstractEstimateDetails projectCode(ProjectCode projectCode) {
    this.projectCode = projectCode;
    return this;
  }

   /**
   * Get projectCode
   * @return projectCode
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ProjectCode getProjectCode() {
    return projectCode;
  }

  public void setProjectCode(ProjectCode projectCode) {
    this.projectCode = projectCode;
  }

  public AbstractEstimateDetails abstractEstimateAppropriations(EstimateAppropriation abstractEstimateAppropriations) {
    this.abstractEstimateAppropriations = abstractEstimateAppropriations;
    return this;
  }

   /**
   * Get abstractEstimateAppropriations
   * @return abstractEstimateAppropriations
  **/
  @ApiModelProperty(value = "")

  @Valid

  public EstimateAppropriation getAbstractEstimateAppropriations() {
    return abstractEstimateAppropriations;
  }

  public void setAbstractEstimateAppropriations(EstimateAppropriation abstractEstimateAppropriations) {
    this.abstractEstimateAppropriations = abstractEstimateAppropriations;
  }

  public AbstractEstimateDetails estimatePhotographsList(DocumentDetail estimatePhotographsList) {
    this.estimatePhotographsList = estimatePhotographsList;
    return this;
  }

   /**
   * Get estimatePhotographsList
   * @return estimatePhotographsList
  **/
  @ApiModelProperty(value = "")

  @Valid

  public DocumentDetail getEstimatePhotographsList() {
    return estimatePhotographsList;
  }

  public void setEstimatePhotographsList(DocumentDetail estimatePhotographsList) {
    this.estimatePhotographsList = estimatePhotographsList;
  }

  public AbstractEstimateDetails auditDetails(AuditDetails auditDetails) {
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
    AbstractEstimateDetails abstractEstimateDetails = (AbstractEstimateDetails) o;
    return Objects.equals(this.id, abstractEstimateDetails.id) &&
        Objects.equals(this.tenantId, abstractEstimateDetails.tenantId) &&
        Objects.equals(this.abstractEstimate, abstractEstimateDetails.abstractEstimate) &&
        Objects.equals(this.nameOfWork, abstractEstimateDetails.nameOfWork) &&
        Objects.equals(this.estimateAmount, abstractEstimateDetails.estimateAmount) &&
        Objects.equals(this.estimateNumber, abstractEstimateDetails.estimateNumber) &&
        Objects.equals(this.grossAmountBilled, abstractEstimateDetails.grossAmountBilled) &&
        Objects.equals(this.projectCode, abstractEstimateDetails.projectCode) &&
        Objects.equals(this.abstractEstimateAppropriations, abstractEstimateDetails.abstractEstimateAppropriations) &&
        Objects.equals(this.estimatePhotographsList, abstractEstimateDetails.estimatePhotographsList) &&
        Objects.equals(this.auditDetails, abstractEstimateDetails.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, abstractEstimate, nameOfWork, estimateAmount, estimateNumber, grossAmountBilled, projectCode, abstractEstimateAppropriations, estimatePhotographsList, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbstractEstimateDetails {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    abstractEstimate: ").append(toIndentedString(abstractEstimate)).append("\n");
    sb.append("    nameOfWork: ").append(toIndentedString(nameOfWork)).append("\n");
    sb.append("    estimateAmount: ").append(toIndentedString(estimateAmount)).append("\n");
    sb.append("    estimateNumber: ").append(toIndentedString(estimateNumber)).append("\n");
    sb.append("    grossAmountBilled: ").append(toIndentedString(grossAmountBilled)).append("\n");
    sb.append("    projectCode: ").append(toIndentedString(projectCode)).append("\n");
    sb.append("    abstractEstimateAppropriations: ").append(toIndentedString(abstractEstimateAppropriations)).append("\n");
    sb.append("    estimatePhotographsList: ").append(toIndentedString(estimatePhotographsList)).append("\n");
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

