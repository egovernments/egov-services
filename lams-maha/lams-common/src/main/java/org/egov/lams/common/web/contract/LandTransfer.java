package org.egov.lams.common.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * The Object holds the data of shits
 */
@ApiModel(description = "The Object holds the data of shits")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:54:53.690Z")

public class LandTransfer   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("landAcquisition")
  private LandAcquisition landAcquisition = null;

  @JsonProperty("resolutionNumber")
  private String resolutionNumber = null;

  @JsonProperty("resolutionDate")
  private Long resolutionDate = null;

  @JsonProperty("propertyId")
  private String propertyId = null;

  @JsonProperty("department")
  private Department department = null;

  /**
   * Transfer type of land. Refer master.
   */
  public enum TransferTypeEnum {
    TRANSFERTYPE1("TRANSFERTYPE1"),
    
    TRANSFERTYPE2("TRANSFERTYPE2");

    private String value;

    TransferTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TransferTypeEnum fromValue(String text) {
      for (TransferTypeEnum b : TransferTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("transferType")
  private TransferTypeEnum transferType = null;

  @JsonProperty("transferPurpose")
  private String transferPurpose = null;

  @JsonProperty("transferFrom")
  private String transferFrom = null;

  @JsonProperty("transferTo")
  private String transferTo = null;

  @JsonProperty("transferNumber")
  private String transferNumber = null;

  @JsonProperty("transferedFromDate")
  private Long transferedFromDate = null;

  @JsonProperty("transferedToDate")
  private Long transferedToDate = null;

  @JsonProperty("remarks")
  private String remarks = null;

  /**
   * Unique identifier (code) of the transfer of land Status
   */
  public enum StatusEnum {
    APPROVED("APPROVED"),
    
    REJECTED("REJECTED");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("workFlowDetails")
  private WorkFlowDetails workFlowDetails = null;

  @JsonProperty("stateId")
  private String stateId = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public LandTransfer id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Id of the Transfer of Land
   * @return id
  **/
  @ApiModelProperty(required = true, value = "Id of the Transfer of Land")
  @NotNull

 @Size(max=64)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LandTransfer tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the land transfer
   * @return tenantId
  **/
  @ApiModelProperty(value = "tenant id of the land transfer")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public LandTransfer landAcquisition(LandAcquisition landAcquisition) {
    this.landAcquisition = landAcquisition;
    return this;
  }

   /**
   * use id of land acquisition for reference
   * @return landAcquisition
  **/
  @ApiModelProperty(required = true, value = "use id of land acquisition for reference")
  @NotNull

  @Valid

  public LandAcquisition getLandAcquisition() {
    return landAcquisition;
  }

  public void setLandAcquisition(LandAcquisition landAcquisition) {
    this.landAcquisition = landAcquisition;
  }

  public LandTransfer resolutionNumber(String resolutionNumber) {
    this.resolutionNumber = resolutionNumber;
    return this;
  }

   /**
   * council resolution number.
   * @return resolutionNumber
  **/
  @ApiModelProperty(required = true, value = "council resolution number.")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-\\\\]") @Size(min=1,max=64)
  public String getResolutionNumber() {
    return resolutionNumber;
  }

  public void setResolutionNumber(String resolutionNumber) {
    this.resolutionNumber = resolutionNumber;
  }

  public LandTransfer resolutionDate(Long resolutionDate) {
    this.resolutionDate = resolutionDate;
    return this;
  }

   /**
   * resolution Date in epoch format
   * @return resolutionDate
  **/
  @ApiModelProperty(value = "resolution Date in epoch format")


  public Long getResolutionDate() {
    return resolutionDate;
  }

  public void setResolutionDate(Long resolutionDate) {
    this.resolutionDate = resolutionDate;
  }

  public LandTransfer propertyId(String propertyId) {
    this.propertyId = propertyId;
    return this;
  }

   /**
   * property or land details . Refer external property system.
   * @return propertyId
  **/
  @ApiModelProperty(value = "property or land details . Refer external property system.")


  public String getPropertyId() {
    return propertyId;
  }

  public void setPropertyId(String propertyId) {
    this.propertyId = propertyId;
  }

  public LandTransfer department(Department department) {
    this.department = department;
    return this;
  }

   /**
   * Proposer department.Dropdown list refer department. Use department code.
   * @return department
  **/
  @ApiModelProperty(value = "Proposer department.Dropdown list refer department. Use department code.")

  @Valid

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public LandTransfer transferType(TransferTypeEnum transferType) {
    this.transferType = transferType;
    return this;
  }

   /**
   * Transfer type of land. Refer master.
   * @return transferType
  **/
  @ApiModelProperty(value = "Transfer type of land. Refer master.")


  public TransferTypeEnum getTransferType() {
    return transferType;
  }

  public void setTransferType(TransferTypeEnum transferType) {
    this.transferType = transferType;
  }

  public LandTransfer transferPurpose(String transferPurpose) {
    this.transferPurpose = transferPurpose;
    return this;
  }

   /**
   * Transfer purpose of land.
   * @return transferPurpose
  **/
  @ApiModelProperty(value = "Transfer purpose of land.")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]") @Size(max=128)
  public String getTransferPurpose() {
    return transferPurpose;
  }

  public void setTransferPurpose(String transferPurpose) {
    this.transferPurpose = transferPurpose;
  }

  public LandTransfer transferFrom(String transferFrom) {
    this.transferFrom = transferFrom;
    return this;
  }

   /**
   * Transfered from details.
   * @return transferFrom
  **/
  @ApiModelProperty(value = "Transfered from details.")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]") @Size(max=128)
  public String getTransferFrom() {
    return transferFrom;
  }

  public void setTransferFrom(String transferFrom) {
    this.transferFrom = transferFrom;
  }

  public LandTransfer transferTo(String transferTo) {
    this.transferTo = transferTo;
    return this;
  }

   /**
   * Transfer to details.
   * @return transferTo
  **/
  @ApiModelProperty(value = "Transfer to details.")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]") @Size(max=128)
  public String getTransferTo() {
    return transferTo;
  }

  public void setTransferTo(String transferTo) {
    this.transferTo = transferTo;
  }

  public LandTransfer transferNumber(String transferNumber) {
    this.transferNumber = transferNumber;
    return this;
  }

   /**
   * transfer number. Unique number generated
   * @return transferNumber
  **/
  @ApiModelProperty(required = true, value = "transfer number. Unique number generated")
  @NotNull

 @Size(max=64)
  public String getTransferNumber() {
    return transferNumber;
  }

  public void setTransferNumber(String transferNumber) {
    this.transferNumber = transferNumber;
  }

  public LandTransfer transferedFromDate(Long transferedFromDate) {
    this.transferedFromDate = transferedFromDate;
    return this;
  }

   /**
   * Transfer date -Start date in epoc format
   * @return transferedFromDate
  **/
  @ApiModelProperty(value = "Transfer date -Start date in epoc format")


  public Long getTransferedFromDate() {
    return transferedFromDate;
  }

  public void setTransferedFromDate(Long transferedFromDate) {
    this.transferedFromDate = transferedFromDate;
  }

  public LandTransfer transferedToDate(Long transferedToDate) {
    this.transferedToDate = transferedToDate;
    return this;
  }

   /**
   * Transfer date - end date in epoc format
   * @return transferedToDate
  **/
  @ApiModelProperty(value = "Transfer date - end date in epoc format")


  public Long getTransferedToDate() {
    return transferedToDate;
  }

  public void setTransferedToDate(Long transferedToDate) {
    this.transferedToDate = transferedToDate;
  }

  public LandTransfer remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * Remarks if any
   * @return remarks
  **/
  @ApiModelProperty(value = "Remarks if any")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]") @Size(max=512)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public LandTransfer status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Unique identifier (code) of the transfer of land Status
   * @return status
  **/
  @ApiModelProperty(value = "Unique identifier (code) of the transfer of land Status")


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public LandTransfer workFlowDetails(WorkFlowDetails workFlowDetails) {
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

  public LandTransfer stateId(String stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * state id of the workflow
   * @return stateId
  **/
  @ApiModelProperty(value = "state id of the workflow")

 @Size(max=20)
  public String getStateId() {
    return stateId;
  }

  public void setStateId(String stateId) {
    this.stateId = stateId;
  }

  public LandTransfer auditDetails(AuditDetails auditDetails) {
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
    LandTransfer landTransfer = (LandTransfer) o;
    return Objects.equals(this.id, landTransfer.id) &&
        Objects.equals(this.tenantId, landTransfer.tenantId) &&
        Objects.equals(this.landAcquisition, landTransfer.landAcquisition) &&
        Objects.equals(this.resolutionNumber, landTransfer.resolutionNumber) &&
        Objects.equals(this.resolutionDate, landTransfer.resolutionDate) &&
        Objects.equals(this.propertyId, landTransfer.propertyId) &&
        Objects.equals(this.department, landTransfer.department) &&
        Objects.equals(this.transferType, landTransfer.transferType) &&
        Objects.equals(this.transferPurpose, landTransfer.transferPurpose) &&
        Objects.equals(this.transferFrom, landTransfer.transferFrom) &&
        Objects.equals(this.transferTo, landTransfer.transferTo) &&
        Objects.equals(this.transferNumber, landTransfer.transferNumber) &&
        Objects.equals(this.transferedFromDate, landTransfer.transferedFromDate) &&
        Objects.equals(this.transferedToDate, landTransfer.transferedToDate) &&
        Objects.equals(this.remarks, landTransfer.remarks) &&
        Objects.equals(this.status, landTransfer.status) &&
        Objects.equals(this.workFlowDetails, landTransfer.workFlowDetails) &&
        Objects.equals(this.stateId, landTransfer.stateId) &&
        Objects.equals(this.auditDetails, landTransfer.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, landAcquisition, resolutionNumber, resolutionDate, propertyId, department, transferType, transferPurpose, transferFrom, transferTo, transferNumber, transferedFromDate, transferedToDate, remarks, status, workFlowDetails, stateId, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandTransfer {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    landAcquisition: ").append(toIndentedString(landAcquisition)).append("\n");
    sb.append("    resolutionNumber: ").append(toIndentedString(resolutionNumber)).append("\n");
    sb.append("    resolutionDate: ").append(toIndentedString(resolutionDate)).append("\n");
    sb.append("    propertyId: ").append(toIndentedString(propertyId)).append("\n");
    sb.append("    department: ").append(toIndentedString(department)).append("\n");
    sb.append("    transferType: ").append(toIndentedString(transferType)).append("\n");
    sb.append("    transferPurpose: ").append(toIndentedString(transferPurpose)).append("\n");
    sb.append("    transferFrom: ").append(toIndentedString(transferFrom)).append("\n");
    sb.append("    transferTo: ").append(toIndentedString(transferTo)).append("\n");
    sb.append("    transferNumber: ").append(toIndentedString(transferNumber)).append("\n");
    sb.append("    transferedFromDate: ").append(toIndentedString(transferedFromDate)).append("\n");
    sb.append("    transferedToDate: ").append(toIndentedString(transferedToDate)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
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

