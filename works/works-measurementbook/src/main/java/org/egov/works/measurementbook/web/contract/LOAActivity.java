package org.egov.works.measurementbook.web.contract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds the basic data for Letter Of Acceptance Activity
 */
@ApiModel(description = "An Object that holds the basic data for Letter Of Acceptance Activity")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-16T09:56:01.690Z")

public class LOAActivity   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("letterOfAcceptanceEstimate")
  private String letterOfAcceptanceEstimate = null;

  @JsonProperty("estimateActivity")
  private EstimateActivity estimateActivity = null;

  @JsonProperty("parent")
  private String parent = null;

  @JsonProperty("approvedRate")
  private BigDecimal approvedRate = null;

  @JsonProperty("approvedQuantity")
  private BigDecimal approvedQuantity = null;

  @JsonProperty("approvedAmount")
  private BigDecimal approvedAmount = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("loaMeasurements")
  private List<LOAMeasurementSheet> loaMeasurements = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public LOAActivity id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Letter Of Acceptance Activity
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Letter Of Acceptance Activity")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LOAActivity tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Letter Of Acceptance Activity
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Letter Of Acceptance Activity")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public LOAActivity letterOfAcceptanceEstimate(String letterOfAcceptanceEstimate) {
    this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
    return this;
  }

   /**
   * Letter of acceptance reference. Primary key is ref here.
   * @return letterOfAcceptanceEstimate
  **/
  @ApiModelProperty(required = true, value = "Letter of acceptance reference. Primary key is ref here.")
  @NotNull

 @Size(min=1,max=100)
  public String getLetterOfAcceptanceEstimate() {
    return letterOfAcceptanceEstimate;
  }

  public void setLetterOfAcceptanceEstimate(String letterOfAcceptanceEstimate) {
    this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
  }

  public LOAActivity estimateActivity(EstimateActivity estimateActivity) {
    this.estimateActivity = estimateActivity;
    return this;
  }

   /**
   * Reference of Estimate Activity, primary key is ref here.
   * @return estimateActivity
  **/
  @ApiModelProperty(required = true, value = "Reference of Estimate Activity, primary key is ref here.")
  @NotNull

  @Valid

  public EstimateActivity getEstimateActivity() {
    return estimateActivity;
  }

  public void setEstimateActivity(EstimateActivity estimateActivity) {
    this.estimateActivity = estimateActivity;
  }

  public LOAActivity parent(String parent) {
    this.parent = parent;
    return this;
  }

   /**
   * Parent of the Letter Of Acceptance Activity. This is required for Revision Workrder parent activity.
   * @return parent
  **/
  @ApiModelProperty(value = "Parent of the Letter Of Acceptance Activity. This is required for Revision Workrder parent activity.")


  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public LOAActivity approvedRate(BigDecimal approvedRate) {
    this.approvedRate = approvedRate;
    return this;
  }

   /**
   * Approved Rate for the LOA activity.
   * @return approvedRate
  **/
  @ApiModelProperty(required = true, value = "Approved Rate for the LOA activity.")
  @NotNull

  @Valid

  public BigDecimal getApprovedRate() {
    return approvedRate;
  }

  public void setApprovedRate(BigDecimal approvedRate) {
    this.approvedRate = approvedRate;
  }

  public LOAActivity approvedQuantity(BigDecimal approvedQuantity) {
    this.approvedQuantity = approvedQuantity;
    return this;
  }

   /**
   * Approved Quantity for the LOA activity.
   * @return approvedQuantity
  **/
  @ApiModelProperty(required = true, value = "Approved Quantity for the LOA activity.")
  @NotNull

  @Valid

  public BigDecimal getApprovedQuantity() {
    return approvedQuantity;
  }

  public void setApprovedQuantity(BigDecimal approvedQuantity) {
    this.approvedQuantity = approvedQuantity;
  }

  public LOAActivity approvedAmount(BigDecimal approvedAmount) {
    this.approvedAmount = approvedAmount;
    return this;
  }

   /**
   * Approved Amount for the LOA activity.
   * @return approvedAmount
  **/
  @ApiModelProperty(required = true, value = "Approved Amount for the LOA activity.")
  @NotNull

  @Valid

  public BigDecimal getApprovedAmount() {
    return approvedAmount;
  }

  public void setApprovedAmount(BigDecimal approvedAmount) {
    this.approvedAmount = approvedAmount;
  }

  public LOAActivity remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * Remarks for the LOA Activity
   * @return remarks
  **/
  @ApiModelProperty(value = "Remarks for the LOA Activity")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=1024)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public LOAActivity loaMeasurements(List<LOAMeasurementSheet> loaMeasurements) {
    this.loaMeasurements = loaMeasurements;
    return this;
  }

  public LOAActivity addLoaMeasurementsItem(LOAMeasurementSheet loaMeasurementsItem) {
    if (this.loaMeasurements == null) {
      this.loaMeasurements = new ArrayList<LOAMeasurementSheet>();
    }
    this.loaMeasurements.add(loaMeasurementsItem);
    return this;
  }

   /**
   * Array of LOA measurement sheet
   * @return loaMeasurements
  **/
  @ApiModelProperty(value = "Array of LOA measurement sheet")

  @Valid

  public List<LOAMeasurementSheet> getLoaMeasurements() {
    return loaMeasurements;
  }

  public void setLoaMeasurements(List<LOAMeasurementSheet> loaMeasurements) {
    this.loaMeasurements = loaMeasurements;
  }

  public LOAActivity deleted(Boolean deleted) {
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

  public LOAActivity auditDetails(AuditDetails auditDetails) {
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
    LOAActivity loAActivity = (LOAActivity) o;
    return Objects.equals(this.id, loAActivity.id) &&
        Objects.equals(this.tenantId, loAActivity.tenantId) &&
        Objects.equals(this.letterOfAcceptanceEstimate, loAActivity.letterOfAcceptanceEstimate) &&
        Objects.equals(this.estimateActivity, loAActivity.estimateActivity) &&
        Objects.equals(this.parent, loAActivity.parent) &&
        Objects.equals(this.approvedRate, loAActivity.approvedRate) &&
        Objects.equals(this.approvedQuantity, loAActivity.approvedQuantity) &&
        Objects.equals(this.approvedAmount, loAActivity.approvedAmount) &&
        Objects.equals(this.remarks, loAActivity.remarks) &&
        Objects.equals(this.loaMeasurements, loAActivity.loaMeasurements) &&
        Objects.equals(this.deleted, loAActivity.deleted) &&
        Objects.equals(this.auditDetails, loAActivity.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, letterOfAcceptanceEstimate, estimateActivity, parent, approvedRate, approvedQuantity, approvedAmount, remarks, loaMeasurements, deleted, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LOAActivity {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    letterOfAcceptanceEstimate: ").append(toIndentedString(letterOfAcceptanceEstimate)).append("\n");
    sb.append("    estimateActivity: ").append(toIndentedString(estimateActivity)).append("\n");
    sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
    sb.append("    approvedRate: ").append(toIndentedString(approvedRate)).append("\n");
    sb.append("    approvedQuantity: ").append(toIndentedString(approvedQuantity)).append("\n");
    sb.append("    approvedAmount: ").append(toIndentedString(approvedAmount)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    loaMeasurements: ").append(toIndentedString(loaMeasurements)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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

