package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.TransferOutward;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class TransferInward   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("receiptDate")
  private Long receiptDate = null;

  @JsonProperty("transferOutWard")
  private TransferOutward transferOutWard = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("inwardNoteNumber")
  private String inwardNoteNumber = null;

  /**
   * inward note status of the TransferInward 
   */
  public enum InwardNoteStatusEnum {
    CREATED("CREATED"),
    
    APPROVED("APPROVED"),
    
    REJECTED("REJECTED"),
    
    CANCELED("CANCELED");

    private String value;

    InwardNoteStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static InwardNoteStatusEnum fromValue(String text) {
      for (InwardNoteStatusEnum b : InwardNoteStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("inwardNoteStatus")
  private InwardNoteStatusEnum inwardNoteStatus = null;

  @JsonProperty("stateId")
  private Long stateId = null;

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("designation")
  private String designation = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public TransferInward id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Transfer Inward 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Transfer Inward ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TransferInward tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Transfer Inward
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Transfer Inward")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public TransferInward receiptDate(Long receiptDate) {
    this.receiptDate = receiptDate;
    return this;
  }

   /**
   * receipt date of the TransferInward 
   * @return receiptDate
  **/
  @ApiModelProperty(required = true, value = "receipt date of the TransferInward ")
  @NotNull


  public Long getReceiptDate() {
    return receiptDate;
  }

  public void setReceiptDate(Long receiptDate) {
    this.receiptDate = receiptDate;
  }

  public TransferInward transferOutWard(TransferOutward transferOutWard) {
    this.transferOutWard = transferOutWard;
    return this;
  }

   /**
   * Get transferOutWard
   * @return transferOutWard
  **/
  @ApiModelProperty(value = "")

  @Valid

  public TransferOutward getTransferOutWard() {
    return transferOutWard;
  }

  public void setTransferOutWard(TransferOutward transferOutWard) {
    this.transferOutWard = transferOutWard;
  }

  public TransferInward description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the TransferInward 
   * @return description
  **/
  @ApiModelProperty(value = "description of the TransferInward ")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TransferInward inwardNoteNumber(String inwardNoteNumber) {
    this.inwardNoteNumber = inwardNoteNumber;
    return this;
  }

   /**
   * Auto generated number, read only 
   * @return inwardNoteNumber
  **/
  @ApiModelProperty(readOnly = true, value = "Auto generated number, read only ")

 @Size(max=100)
  public String getInwardNoteNumber() {
    return inwardNoteNumber;
  }

  public void setInwardNoteNumber(String inwardNoteNumber) {
    this.inwardNoteNumber = inwardNoteNumber;
  }

  public TransferInward inwardNoteStatus(InwardNoteStatusEnum inwardNoteStatus) {
    this.inwardNoteStatus = inwardNoteStatus;
    return this;
  }

   /**
   * inward note status of the TransferInward 
   * @return inwardNoteStatus
  **/
  @ApiModelProperty(value = "inward note status of the TransferInward ")

 @Size(min=5,max=50)
  public InwardNoteStatusEnum getInwardNoteStatus() {
    return inwardNoteStatus;
  }

  public void setInwardNoteStatus(InwardNoteStatusEnum inwardNoteStatus) {
    this.inwardNoteStatus = inwardNoteStatus;
  }

  public TransferInward stateId(Long stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * state id of the TransferInward 
   * @return stateId
  **/
  @ApiModelProperty(value = "state id of the TransferInward ")


  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public TransferInward fileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

   /**
   * fileStoreId  File Store id of the Transfer Inward Note 
   * @return fileStoreId
  **/
  @ApiModelProperty(value = "fileStoreId  File Store id of the Transfer Inward Note ")


  public String getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public TransferInward designation(String designation) {
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

  public TransferInward auditDetails(AuditDetails auditDetails) {
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
    TransferInward transferInward = (TransferInward) o;
    return Objects.equals(this.id, transferInward.id) &&
        Objects.equals(this.tenantId, transferInward.tenantId) &&
        Objects.equals(this.receiptDate, transferInward.receiptDate) &&
        Objects.equals(this.transferOutWard, transferInward.transferOutWard) &&
        Objects.equals(this.description, transferInward.description) &&
        Objects.equals(this.inwardNoteNumber, transferInward.inwardNoteNumber) &&
        Objects.equals(this.inwardNoteStatus, transferInward.inwardNoteStatus) &&
        Objects.equals(this.stateId, transferInward.stateId) &&
        Objects.equals(this.fileStoreId, transferInward.fileStoreId) &&
        Objects.equals(this.designation, transferInward.designation) &&
        Objects.equals(this.auditDetails, transferInward.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, receiptDate, transferOutWard, description, inwardNoteNumber, inwardNoteStatus, stateId, fileStoreId, designation, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferInward {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    receiptDate: ").append(toIndentedString(receiptDate)).append("\n");
    sb.append("    transferOutWard: ").append(toIndentedString(transferOutWard)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    inwardNoteNumber: ").append(toIndentedString(inwardNoteNumber)).append("\n");
    sb.append("    inwardNoteStatus: ").append(toIndentedString(inwardNoteStatus)).append("\n");
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

