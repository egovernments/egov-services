package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.Indent;
import io.swagger.model.Store;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class TransferOutward   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("store")
  private Store store = null;

  @JsonProperty("issueDate")
  private Long issueDate = null;

  @JsonProperty("issuedToEmployee")
  private String issuedToEmployee = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("outwardNoteNumber")
  private String outwardNoteNumber = null;

  /**
   * outward note status of the TransferOutward 
   */
  public enum OutwardNoteStatusEnum {
    CREATED("CREATED"),
    
    APPROVED("APPROVED"),
    
    REJECTED("REJECTED"),
    
    CANCELED("CANCELED");

    private String value;

    OutwardNoteStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OutwardNoteStatusEnum fromValue(String text) {
      for (OutwardNoteStatusEnum b : OutwardNoteStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("outwardNoteStatus")
  private OutwardNoteStatusEnum outwardNoteStatus = null;

  @JsonProperty("indent")
  private Indent indent = null;

  @JsonProperty("stateId")
  private Long stateId = null;

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("designation")
  private String designation = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public TransferOutward id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the TransferOutward 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the TransferOutward ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TransferOutward tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Transfer Indent Note
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Transfer Indent Note")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public TransferOutward store(Store store) {
    this.store = store;
    return this;
  }

   /**
   * Get store
   * @return store
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public TransferOutward issueDate(Long issueDate) {
    this.issueDate = issueDate;
    return this;
  }

   /**
   * issue date of the TransferOutward 
   * @return issueDate
  **/
  @ApiModelProperty(required = true, value = "issue date of the TransferOutward ")
  @NotNull


  public Long getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Long issueDate) {
    this.issueDate = issueDate;
  }

  public TransferOutward issuedToEmployee(String issuedToEmployee) {
    this.issuedToEmployee = issuedToEmployee;
    return this;
  }

   /**
   * issued to employee of the TransferOutward 
   * @return issuedToEmployee
  **/
  @ApiModelProperty(value = "issued to employee of the TransferOutward ")


  public String getIssuedToEmployee() {
    return issuedToEmployee;
  }

  public void setIssuedToEmployee(String issuedToEmployee) {
    this.issuedToEmployee = issuedToEmployee;
  }

  public TransferOutward description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the TransferOutward 
   * @return description
  **/
  @ApiModelProperty(value = "description of the TransferOutward ")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TransferOutward outwardNoteNumber(String outwardNoteNumber) {
    this.outwardNoteNumber = outwardNoteNumber;
    return this;
  }

   /**
   * Auto generated number, read only 
   * @return outwardNoteNumber
  **/
  @ApiModelProperty(readOnly = true, value = "Auto generated number, read only ")

 @Size(max=100)
  public String getOutwardNoteNumber() {
    return outwardNoteNumber;
  }

  public void setOutwardNoteNumber(String outwardNoteNumber) {
    this.outwardNoteNumber = outwardNoteNumber;
  }

  public TransferOutward outwardNoteStatus(OutwardNoteStatusEnum outwardNoteStatus) {
    this.outwardNoteStatus = outwardNoteStatus;
    return this;
  }

   /**
   * outward note status of the TransferOutward 
   * @return outwardNoteStatus
  **/
  @ApiModelProperty(value = "outward note status of the TransferOutward ")

 @Size(min=5,max=50)
  public OutwardNoteStatusEnum getOutwardNoteStatus() {
    return outwardNoteStatus;
  }

  public void setOutwardNoteStatus(OutwardNoteStatusEnum outwardNoteStatus) {
    this.outwardNoteStatus = outwardNoteStatus;
  }

  public TransferOutward indent(Indent indent) {
    this.indent = indent;
    return this;
  }

   /**
   * Get indent
   * @return indent
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Indent getIndent() {
    return indent;
  }

  public void setIndent(Indent indent) {
    this.indent = indent;
  }

  public TransferOutward stateId(Long stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * state id of the TransferOutward 
   * @return stateId
  **/
  @ApiModelProperty(value = "state id of the TransferOutward ")


  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public TransferOutward fileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

   /**
   * fileStoreId  File Store id of the Transfer Outward Note 
   * @return fileStoreId
  **/
  @ApiModelProperty(value = "fileStoreId  File Store id of the Transfer Outward Note ")


  public String getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public TransferOutward designation(String designation) {
    this.designation = designation;
    return this;
  }

   /**
   * Designation of the of the created by user at the time of Purchase Order creation.
   * @return designation
  **/
  @ApiModelProperty(value = "Designation of the of the created by user at the time of Purchase Order creation.")


  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public TransferOutward auditDetails(AuditDetails auditDetails) {
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
    TransferOutward transferOutward = (TransferOutward) o;
    return Objects.equals(this.id, transferOutward.id) &&
        Objects.equals(this.tenantId, transferOutward.tenantId) &&
        Objects.equals(this.store, transferOutward.store) &&
        Objects.equals(this.issueDate, transferOutward.issueDate) &&
        Objects.equals(this.issuedToEmployee, transferOutward.issuedToEmployee) &&
        Objects.equals(this.description, transferOutward.description) &&
        Objects.equals(this.outwardNoteNumber, transferOutward.outwardNoteNumber) &&
        Objects.equals(this.outwardNoteStatus, transferOutward.outwardNoteStatus) &&
        Objects.equals(this.indent, transferOutward.indent) &&
        Objects.equals(this.stateId, transferOutward.stateId) &&
        Objects.equals(this.fileStoreId, transferOutward.fileStoreId) &&
        Objects.equals(this.designation, transferOutward.designation) &&
        Objects.equals(this.auditDetails, transferOutward.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, store, issueDate, issuedToEmployee, description, outwardNoteNumber, outwardNoteStatus, indent, stateId, fileStoreId, designation, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferOutward {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    store: ").append(toIndentedString(store)).append("\n");
    sb.append("    issueDate: ").append(toIndentedString(issueDate)).append("\n");
    sb.append("    issuedToEmployee: ").append(toIndentedString(issuedToEmployee)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    outwardNoteNumber: ").append(toIndentedString(outwardNoteNumber)).append("\n");
    sb.append("    outwardNoteStatus: ").append(toIndentedString(outwardNoteStatus)).append("\n");
    sb.append("    indent: ").append(toIndentedString(indent)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    fileStoreId: ").append(toIndentedString(fileStoreId)).append("\n");
    sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
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

