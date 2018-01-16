package org.egov.works.measurementbook.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds the basic data for a Contractor Bill
 */
@ApiModel(description = "An Object that holds the basic data for a Contractor Bill")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-16T10:26:42.085Z")

public class ContractorBill extends BillRegister  {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("billSequenceNumber")
  private String billSequenceNumber = null;

  @JsonProperty("approvedDate")
  private Long approvedDate = null;

  @JsonProperty("approvedBy")
  private User approvedBy = null;

  @JsonProperty("cancellationReason")
  private String cancellationReason = null;

  @JsonProperty("cancellationRemarks")
  private String cancellationRemarks = null;

  @JsonProperty("assets")
  private List<AssetForBill> assets = null;

  @JsonProperty("mbForContractorBill")
  private List<MeasurementBookForContractorBill> mbForContractorBill = null;

  @JsonProperty("letterOfAcceptanceEstimate")
  private LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = null;

  @JsonProperty("workFlowDetails")
  private WorkFlowDetails workFlowDetails = null;

  @JsonProperty("stateId")
  private String stateId = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("spillOver")
  private Boolean spillOver = false;

  @JsonProperty("workCompletionDate")
  private Long workCompletionDate = null;

  public ContractorBill id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Contractor Bill
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Contractor Bill")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ContractorBill tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Contractor Bill
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Contractor Bill")

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ContractorBill billSequenceNumber(String billSequenceNumber) {
    this.billSequenceNumber = billSequenceNumber;
    return this;
  }

   /**
   * Bill sequence number of the Bill
   * @return billSequenceNumber
  **/
  @ApiModelProperty(value = "Bill sequence number of the Bill")

 @Pattern(regexp="[a-zA-Z0-9-/]+") @Size(max=50)
  public String getBillSequenceNumber() {
    return billSequenceNumber;
  }

  public void setBillSequenceNumber(String billSequenceNumber) {
    this.billSequenceNumber = billSequenceNumber;
  }

  public ContractorBill approvedDate(Long approvedDate) {
    this.approvedDate = approvedDate;
    return this;
  }

   /**
   * Epoch time of when the Bill approved
   * @return approvedDate
  **/
  @ApiModelProperty(value = "Epoch time of when the Bill approved")


  public Long getApprovedDate() {
    return approvedDate;
  }

  public void setApprovedDate(Long approvedDate) {
    this.approvedDate = approvedDate;
  }

  public ContractorBill approvedBy(User approvedBy) {
    this.approvedBy = approvedBy;
    return this;
  }

   /**
   * User name of the User who approved the Bill
   * @return approvedBy
  **/
  @ApiModelProperty(value = "User name of the User who approved the Bill")

  @Valid

  public User getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(User approvedBy) {
    this.approvedBy = approvedBy;
  }

  public ContractorBill cancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
    return this;
  }

   /**
   * Reason for cancellation of the Bill, Required only while cancelling Contractor Bill
   * @return cancellationReason
  **/
  @ApiModelProperty(value = "Reason for cancellation of the Bill, Required only while cancelling Contractor Bill")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=100)
  public String getCancellationReason() {
    return cancellationReason;
  }

  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  public ContractorBill cancellationRemarks(String cancellationRemarks) {
    this.cancellationRemarks = cancellationRemarks;
    return this;
  }

   /**
   * Remarks for cancellation of the Bill, Required only while cancelling Bill
   * @return cancellationRemarks
  **/
  @ApiModelProperty(value = "Remarks for cancellation of the Bill, Required only while cancelling Bill")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=512)
  public String getCancellationRemarks() {
    return cancellationRemarks;
  }

  public void setCancellationRemarks(String cancellationRemarks) {
    this.cancellationRemarks = cancellationRemarks;
  }

  public ContractorBill assets(List<AssetForBill> assets) {
    this.assets = assets;
    return this;
  }

  public ContractorBill addAssetsItem(AssetForBill assetsItem) {
    if (this.assets == null) {
      this.assets = new ArrayList<AssetForBill>();
    }
    this.assets.add(assetsItem);
    return this;
  }

   /**
   * Array of Asset details for the Bill
   * @return assets
  **/
  @ApiModelProperty(value = "Array of Asset details for the Bill")

  @Valid

  public List<AssetForBill> getAssets() {
    return assets;
  }

  public void setAssets(List<AssetForBill> assets) {
    this.assets = assets;
  }

  public ContractorBill mbForContractorBill(List<MeasurementBookForContractorBill> mbForContractorBill) {
    this.mbForContractorBill = mbForContractorBill;
    return this;
  }

  public ContractorBill addMbForContractorBillItem(MeasurementBookForContractorBill mbForContractorBillItem) {
    if (this.mbForContractorBill == null) {
      this.mbForContractorBill = new ArrayList<MeasurementBookForContractorBill>();
    }
    this.mbForContractorBill.add(mbForContractorBillItem);
    return this;
  }

   /**
   * Array of Measurement Book for the Bill
   * @return mbForContractorBill
  **/
  @ApiModelProperty(value = "Array of Measurement Book for the Bill")

  @Valid

  public List<MeasurementBookForContractorBill> getMbForContractorBill() {
    return mbForContractorBill;
  }

  public void setMbForContractorBill(List<MeasurementBookForContractorBill> mbForContractorBill) {
    this.mbForContractorBill = mbForContractorBill;
  }

  public ContractorBill letterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
    this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
    return this;
  }

   /**
   * LOA and Estimate reference for the Bill
   * @return letterOfAcceptanceEstimate
  **/
  @ApiModelProperty(value = "LOA and Estimate reference for the Bill")

  @Valid

  public LetterOfAcceptanceEstimate getLetterOfAcceptanceEstimate() {
    return letterOfAcceptanceEstimate;
  }

  public void setLetterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
    this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
  }

  public ContractorBill workFlowDetails(WorkFlowDetails workFlowDetails) {
    this.workFlowDetails = workFlowDetails;
    return this;
  }

   /**
   * Get workFlowDetails
   * @return workFlowDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public WorkFlowDetails getWorkFlowDetails() {
    return workFlowDetails;
  }

  public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
    this.workFlowDetails = workFlowDetails;
  }

  public ContractorBill stateId(String stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * State id of the workflow
   * @return stateId
  **/
  @ApiModelProperty(value = "State id of the workflow")


  public String getStateId() {
    return stateId;
  }

  public void setStateId(String stateId) {
    this.stateId = stateId;
  }

  public ContractorBill auditDetails(AuditDetails auditDetails) {
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

  public ContractorBill deleted(Boolean deleted) {
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

  public ContractorBill spillOver(Boolean spillOver) {
    this.spillOver = spillOver;
    return this;
  }

   /**
   * Boolean value to identify whether the bills are spillover or current.
   * @return spillOver
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the bills are spillover or current.")


  public Boolean getSpillOver() {
    return spillOver;
  }

  public void setSpillOver(Boolean spillOver) {
    this.spillOver = spillOver;
  }

  public ContractorBill workCompletionDate(Long workCompletionDate) {
    this.workCompletionDate = workCompletionDate;
    return this;
  }

   /**
   * Epoch time of when the Final Bill Completed
   * @return workCompletionDate
  **/
  @ApiModelProperty(value = "Epoch time of when the Final Bill Completed")


  public Long getWorkCompletionDate() {
    return workCompletionDate;
  }

  public void setWorkCompletionDate(Long workCompletionDate) {
    this.workCompletionDate = workCompletionDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractorBill contractorBill = (ContractorBill) o;
    return Objects.equals(this.id, contractorBill.id) &&
        Objects.equals(this.tenantId, contractorBill.tenantId) &&
        Objects.equals(this.billSequenceNumber, contractorBill.billSequenceNumber) &&
        Objects.equals(this.approvedDate, contractorBill.approvedDate) &&
        Objects.equals(this.approvedBy, contractorBill.approvedBy) &&
        Objects.equals(this.cancellationReason, contractorBill.cancellationReason) &&
        Objects.equals(this.cancellationRemarks, contractorBill.cancellationRemarks) &&
        Objects.equals(this.assets, contractorBill.assets) &&
        Objects.equals(this.mbForContractorBill, contractorBill.mbForContractorBill) &&
        Objects.equals(this.letterOfAcceptanceEstimate, contractorBill.letterOfAcceptanceEstimate) &&
        Objects.equals(this.workFlowDetails, contractorBill.workFlowDetails) &&
        Objects.equals(this.stateId, contractorBill.stateId) &&
        Objects.equals(this.auditDetails, contractorBill.auditDetails) &&
        Objects.equals(this.deleted, contractorBill.deleted) &&
        Objects.equals(this.spillOver, contractorBill.spillOver) &&
        Objects.equals(this.workCompletionDate, contractorBill.workCompletionDate) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, billSequenceNumber, approvedDate, approvedBy, cancellationReason, cancellationRemarks, assets, mbForContractorBill, letterOfAcceptanceEstimate, workFlowDetails, stateId, auditDetails, deleted, spillOver, workCompletionDate, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractorBill {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    billSequenceNumber: ").append(toIndentedString(billSequenceNumber)).append("\n");
    sb.append("    approvedDate: ").append(toIndentedString(approvedDate)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    cancellationReason: ").append(toIndentedString(cancellationReason)).append("\n");
    sb.append("    cancellationRemarks: ").append(toIndentedString(cancellationRemarks)).append("\n");
    sb.append("    assets: ").append(toIndentedString(assets)).append("\n");
    sb.append("    mbForContractorBill: ").append(toIndentedString(mbForContractorBill)).append("\n");
    sb.append("    letterOfAcceptanceEstimate: ").append(toIndentedString(letterOfAcceptanceEstimate)).append("\n");
    sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    spillOver: ").append(toIndentedString(spillOver)).append("\n");
    sb.append("    workCompletionDate: ").append(toIndentedString(workCompletionDate)).append("\n");
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
