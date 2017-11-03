package org.egov.works.services.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An Object that hold Estimate Appropriation for a given Abstract Estimate Details
 */
@ApiModel(description = "An Object that hold Estimate Appropriation for a given Abstract Estimate Details")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-03T10:45:32.643Z")

public class EstimateAppropriation   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("objectNumber")
  private String objectNumber = null;

  @JsonProperty("objectType")
  private String objectType = null;

  @JsonProperty("budgetUsage")
  private String budgetUsage = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public EstimateAppropriation id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Estimate Appropriation
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Estimate Appropriation")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EstimateAppropriation tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id for which this object belongs to
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id for which this object belongs to")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public EstimateAppropriation objectNumber(String objectNumber) {
    this.objectNumber = objectNumber;
    return this;
  }

   /**
   * Unique object number to be defined for the object type
   * @return objectNumber
  **/
  @ApiModelProperty(required = true, value = "Unique object number to be defined for the object type")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+") @Size(max=100)
  public String getObjectNumber() {
    return objectNumber;
  }

  public void setObjectNumber(String objectNumber) {
    this.objectNumber = objectNumber;
  }

  public EstimateAppropriation objectType(String objectType) {
    this.objectType = objectType;
    return this;
  }

   /**
   * Object type for the Estimate Appropriation.
   * @return objectType
  **/
  @ApiModelProperty(required = true, value = "Object type for the Estimate Appropriation.")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+") @Size(max=100)
  public String getObjectType() {
    return objectType;
  }

  public void setObjectType(String objectType) {
    this.objectType = objectType;
  }

  public EstimateAppropriation budgetUsage(String budgetUsage) {
    this.budgetUsage = budgetUsage;
    return this;
  }

   /**
   * Refrernce of Budget Usage of the Estimate Appropriation
   * @return budgetUsage
  **/
  @ApiModelProperty(required = true, value = "Refrernce of Budget Usage of the Estimate Appropriation")
  @NotNull

 @Size(min=3,max=100)
  public String getBudgetUsage() {
    return budgetUsage;
  }

  public void setBudgetUsage(String budgetUsage) {
    this.budgetUsage = budgetUsage;
  }

  public EstimateAppropriation auditDetails(AuditDetails auditDetails) {
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
    EstimateAppropriation estimateAppropriation = (EstimateAppropriation) o;
    return Objects.equals(this.id, estimateAppropriation.id) &&
        Objects.equals(this.tenantId, estimateAppropriation.tenantId) &&
        Objects.equals(this.objectNumber, estimateAppropriation.objectNumber) &&
        Objects.equals(this.objectType, estimateAppropriation.objectType) &&
        Objects.equals(this.budgetUsage, estimateAppropriation.budgetUsage) &&
        Objects.equals(this.auditDetails, estimateAppropriation.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, objectNumber, objectType, budgetUsage, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimateAppropriation {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    objectNumber: ").append(toIndentedString(objectNumber)).append("\n");
    sb.append("    objectType: ").append(toIndentedString(objectType)).append("\n");
    sb.append("    budgetUsage: ").append(toIndentedString(budgetUsage)).append("\n");
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

