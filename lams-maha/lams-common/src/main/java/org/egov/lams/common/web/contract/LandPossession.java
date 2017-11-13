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
 * The Object Holds the possession details of land acquisition
 */
@ApiModel(description = "The Object Holds the possession details of land acquisition")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:54:53.690Z")

public class LandPossession   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("possessionNumber")
  private String possessionNumber = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("landType")
  private AssetCategory landType = null;

  @JsonProperty("usage")
  private Usage usage = null;

  @JsonProperty("subUsage")
  private SubUsage subUsage = null;

  @JsonProperty("ctsNumber")
  private String ctsNumber = null;

  @JsonProperty("surveyNumber")
  private String surveyNumber = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("possessionDate")
  private Long possessionDate = null;

  @JsonProperty("tdrCertificate")
  private String tdrCertificate = null;

  @JsonProperty("landAcquisition")
  private LandAcquisition landAcquisition = null;

  /**
   * Unique identifier (code) of the possession Status
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

  public LandPossession id(String id) {
    this.id = id;
    return this;
  }

   /**
   * unique primary key identifier with tenantid.
   * @return id
  **/
  @ApiModelProperty(required = true, value = "unique primary key identifier with tenantid.")
  @NotNull

 @Size(max=64)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LandPossession possessionNumber(String possessionNumber) {
    this.possessionNumber = possessionNumber;
    return this;
  }

   /**
   * Unique Possession number generated in the system
   * @return possessionNumber
  **/
  @ApiModelProperty(value = "Unique Possession number generated in the system")

 @Size(max=64)
  public String getPossessionNumber() {
    return possessionNumber;
  }

  public void setPossessionNumber(String possessionNumber) {
    this.possessionNumber = possessionNumber;
  }

  public LandPossession tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the land possession
   * @return tenantId
  **/
  @ApiModelProperty(value = "tenant id of the land possession")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public LandPossession landType(AssetCategory landType) {
    this.landType = landType;
    return this;
  }

   /**
   * Land type. Refering asset category of type LAND. Get category code and use as land type code.
   * @return landType
  **/
  @ApiModelProperty(value = "Land type. Refering asset category of type LAND. Get category code and use as land type code.")

  @Valid

  public AssetCategory getLandType() {
    return landType;
  }

  public void setLandType(AssetCategory landType) {
    this.landType = landType;
  }

  public LandPossession usage(Usage usage) {
    this.usage = usage;
    return this;
  }

   /**
   * Use Code for usage reference.
   * @return usage
  **/
  @ApiModelProperty(value = "Use Code for usage reference.")

  @Valid

  public Usage getUsage() {
    return usage;
  }

  public void setUsage(Usage usage) {
    this.usage = usage;
  }

  public LandPossession subUsage(SubUsage subUsage) {
    this.subUsage = subUsage;
    return this;
  }

   /**
   * Sub Usage code will be used in land posession
   * @return subUsage
  **/
  @ApiModelProperty(value = "Sub Usage code will be used in land posession")

  @Valid

  public SubUsage getSubUsage() {
    return subUsage;
  }

  public void setSubUsage(SubUsage subUsage) {
    this.subUsage = subUsage;
  }

  public LandPossession ctsNumber(String ctsNumber) {
    this.ctsNumber = ctsNumber;
    return this;
  }

   /**
   * Cts number or gatt number of land
   * @return ctsNumber
  **/
  @ApiModelProperty(value = "Cts number or gatt number of land")

 @Size(max=25)
  public String getCtsNumber() {
    return ctsNumber;
  }

  public void setCtsNumber(String ctsNumber) {
    this.ctsNumber = ctsNumber;
  }

  public LandPossession surveyNumber(String surveyNumber) {
    this.surveyNumber = surveyNumber;
    return this;
  }

   /**
   * survey number of land
   * @return surveyNumber
  **/
  @ApiModelProperty(value = "survey number of land")

 @Size(max=25)
  public String getSurveyNumber() {
    return surveyNumber;
  }

  public void setSurveyNumber(String surveyNumber) {
    this.surveyNumber = surveyNumber;
  }

  public LandPossession remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * Remarks of pocession
   * @return remarks
  **/
  @ApiModelProperty(value = "Remarks of pocession")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]") @Size(max=1024)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public LandPossession possessionDate(Long possessionDate) {
    this.possessionDate = possessionDate;
    return this;
  }

   /**
   * possession Date in epoch forma
   * @return possessionDate
  **/
  @ApiModelProperty(required = true, value = "possession Date in epoch forma")
  @NotNull


  public Long getPossessionDate() {
    return possessionDate;
  }

  public void setPossessionDate(Long possessionDate) {
    this.possessionDate = possessionDate;
  }

  public LandPossession tdrCertificate(String tdrCertificate) {
    this.tdrCertificate = tdrCertificate;
    return this;
  }

   /**
   * filestoreId of the TDR Certificate
   * @return tdrCertificate
  **/
  @ApiModelProperty(value = "filestoreId of the TDR Certificate")


  public String getTdrCertificate() {
    return tdrCertificate;
  }

  public void setTdrCertificate(String tdrCertificate) {
    this.tdrCertificate = tdrCertificate;
  }

  public LandPossession landAcquisition(LandAcquisition landAcquisition) {
    this.landAcquisition = landAcquisition;
    return this;
  }

   /**
   * refer land acquisition id.
   * @return landAcquisition
  **/
  @ApiModelProperty(required = true, value = "refer land acquisition id.")
  @NotNull

  @Valid

  public LandAcquisition getLandAcquisition() {
    return landAcquisition;
  }

  public void setLandAcquisition(LandAcquisition landAcquisition) {
    this.landAcquisition = landAcquisition;
  }

  public LandPossession status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Unique identifier (code) of the possession Status
   * @return status
  **/
  @ApiModelProperty(value = "Unique identifier (code) of the possession Status")


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public LandPossession workFlowDetails(WorkFlowDetails workFlowDetails) {
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

  public LandPossession stateId(String stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * state id of the workflow
   * @return stateId
  **/
  @ApiModelProperty(value = "state id of the workflow")


  public String getStateId() {
    return stateId;
  }

  public void setStateId(String stateId) {
    this.stateId = stateId;
  }

  public LandPossession auditDetails(AuditDetails auditDetails) {
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
    LandPossession landPossession = (LandPossession) o;
    return Objects.equals(this.id, landPossession.id) &&
        Objects.equals(this.possessionNumber, landPossession.possessionNumber) &&
        Objects.equals(this.tenantId, landPossession.tenantId) &&
        Objects.equals(this.landType, landPossession.landType) &&
        Objects.equals(this.usage, landPossession.usage) &&
        Objects.equals(this.subUsage, landPossession.subUsage) &&
        Objects.equals(this.ctsNumber, landPossession.ctsNumber) &&
        Objects.equals(this.surveyNumber, landPossession.surveyNumber) &&
        Objects.equals(this.remarks, landPossession.remarks) &&
        Objects.equals(this.possessionDate, landPossession.possessionDate) &&
        Objects.equals(this.tdrCertificate, landPossession.tdrCertificate) &&
        Objects.equals(this.landAcquisition, landPossession.landAcquisition) &&
        Objects.equals(this.status, landPossession.status) &&
        Objects.equals(this.workFlowDetails, landPossession.workFlowDetails) &&
        Objects.equals(this.stateId, landPossession.stateId) &&
        Objects.equals(this.auditDetails, landPossession.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, possessionNumber, tenantId, landType, usage, subUsage, ctsNumber, surveyNumber, remarks, possessionDate, tdrCertificate, landAcquisition, status, workFlowDetails, stateId, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandPossession {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    possessionNumber: ").append(toIndentedString(possessionNumber)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    landType: ").append(toIndentedString(landType)).append("\n");
    sb.append("    usage: ").append(toIndentedString(usage)).append("\n");
    sb.append("    subUsage: ").append(toIndentedString(subUsage)).append("\n");
    sb.append("    ctsNumber: ").append(toIndentedString(ctsNumber)).append("\n");
    sb.append("    surveyNumber: ").append(toIndentedString(surveyNumber)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    possessionDate: ").append(toIndentedString(possessionDate)).append("\n");
    sb.append("    tdrCertificate: ").append(toIndentedString(tdrCertificate)).append("\n");
    sb.append("    landAcquisition: ").append(toIndentedString(landAcquisition)).append("\n");
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

