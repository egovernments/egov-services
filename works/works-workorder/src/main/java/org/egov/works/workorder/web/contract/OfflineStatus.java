package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object that holds the basic data of Offline Status
 */
@ApiModel(description = "An Object that holds the basic data of Offline Status")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-26T08:48:07.947Z")

public class OfflineStatus   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("objectType")
  private String objectType = null;

  @JsonProperty("objectNumber")
  private String objectNumber = null;

  @JsonProperty("objectDate")
  private Long objectDate = null;

  @JsonProperty("status")
  private WorksStatus status = null;

  @JsonProperty("statusDate")
  private Long statusDate = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("referenceNumber")
  private String referenceNumber = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public OfflineStatus id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Offline Status
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Offline Status")

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OfflineStatus tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Offline Status
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Offline Status")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public OfflineStatus objectType(String objectType) {
    this.objectType = objectType;
    return this;
  }

   /**
   * Object type for the offline status. For example DetailedEstimate, LetterOfAcceptance and WorkOrder.
   * @return objectType
  **/
  @ApiModelProperty(required = true, value = "Object type for the offline status. For example DetailedEstimate, LetterOfAcceptance and WorkOrder.")
  @NotNull

  public String getObjectType() {
    return objectType;
  }

  public void setObjectType(String objectType) {
    this.objectType = objectType;
  }

  public OfflineStatus objectNumber(String objectNumber) {
    this.objectNumber = objectNumber;
    return this;
  }

   /**
   * Unique Identifier for the specified obect type. For example Detailed Estimate Number, LOA Number and Work Order number.
   * @return objectNumber
  **/
  @ApiModelProperty(required = true, value = "Unique Identifier for the specified obect type. For example Detailed Estimate Number, LOA Number and Work Order number.")
  @NotNull

  public String getObjectNumber() {
    return objectNumber;
  }

  public void setObjectNumber(String objectNumber) {
    this.objectNumber = objectNumber;
  }

  public OfflineStatus objectDate(Long objectDate) {
    this.objectDate = objectDate;
    return this;
  }

   /**
     * In case of Detailed estimate object date will be the date on which detatiled estimate is technical sanctioned and in case
     * of Work Order/LOA the object date will be the date on which Work Order/LOA is approved.
   * @return objectDate
  **/
  @ApiModelProperty(value = "In case of Detailed estimate object date will be the date on which detatiled estimate is technical sanctioned and in case of Work Order/LOA the object date will be the date on which Work Order/LOA is approved.")

  public Long getObjectDate() {
    return objectDate;
  }

  public void setObjectDate(Long objectDate) {
    this.objectDate = objectDate;
  }

  public OfflineStatus status(WorksStatus status) {
    this.status = status;
    return this;
  }

   /**
   * Offline status for the object type.
   * @return status
  **/
  @ApiModelProperty(required = true, value = "Offline status for the object type.")
  @NotNull

  @Valid

  public WorksStatus getStatus() {
    return status;
  }

  public void setStatus(WorksStatus status) {
    this.status = status;
  }

  public OfflineStatus statusDate(Long statusDate) {
    this.statusDate = statusDate;
    return this;
  }

   /**
     * Epoch time of Offline Status Date. The status and dates order is maintained. Future date is not allowed to enter in this
     * field. The current row date should be on or after the previous row date of the offline status.
   * @return statusDate
  **/
  @ApiModelProperty(required = true, value = "Epoch time of Offline Status Date. The status and dates order is maintained.  Future date is not allowed to enter in this field. The current row date should be on or after the previous row date of the offline status.")
  @NotNull

  public Long getStatusDate() {
    return statusDate;
  }

  public void setStatusDate(Long statusDate) {
    this.statusDate = statusDate;
  }

  public OfflineStatus remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * Remarks for the object type.
   * @return remarks
  **/
  @ApiModelProperty(value = "Remarks for the object type.")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public OfflineStatus referenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
    return this;
  }

   /**
   * Referenece number for the particular object. For example the Tender number needs to be captures for Tender Notice document release, Agreement number for Agreement order signed status.
   * @return referenceNumber
  **/
  @ApiModelProperty(value = "Referenece number for the particular object. For example the Tender number needs to be captures for Tender Notice document release, Agreement number for Agreement order signed status.")

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+")
  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public OfflineStatus deleted(Boolean deleted) {
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

  public OfflineStatus auditDetails(AuditDetails auditDetails) {
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
    OfflineStatus offlineStatus = (OfflineStatus) o;
    return Objects.equals(this.id, offlineStatus.id) &&
        Objects.equals(this.tenantId, offlineStatus.tenantId) &&
        Objects.equals(this.objectType, offlineStatus.objectType) &&
        Objects.equals(this.objectNumber, offlineStatus.objectNumber) &&
        Objects.equals(this.objectDate, offlineStatus.objectDate) &&
        Objects.equals(this.status, offlineStatus.status) &&
        Objects.equals(this.statusDate, offlineStatus.statusDate) &&
        Objects.equals(this.remarks, offlineStatus.remarks) &&
        Objects.equals(this.referenceNumber, offlineStatus.referenceNumber) &&
        Objects.equals(this.deleted, offlineStatus.deleted) &&
        Objects.equals(this.auditDetails, offlineStatus.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, objectType, objectNumber, objectDate, status, statusDate, remarks, referenceNumber, deleted, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OfflineStatus {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    objectType: ").append(toIndentedString(objectType)).append("\n");
    sb.append("    objectNumber: ").append(toIndentedString(objectNumber)).append("\n");
    sb.append("    objectDate: ").append(toIndentedString(objectDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    statusDate: ").append(toIndentedString(statusDate)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    referenceNumber: ").append(toIndentedString(referenceNumber)).append("\n");
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

