package org.egov.lams.common.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A Object holds the basic data for a Land Aquisition
 */
@ApiModel(description = "A Object holds the basic data for a Land Aquisition")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:54:53.690Z")

public class LandAcquisition   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("landAcquisitionNumber")
  private String landAcquisitionNumber = null;

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

  @JsonProperty("plotNumber")
  private String plotNumber = null;

  @JsonProperty("plotArea")
  private String plotArea = null;

  @JsonProperty("plotAddress")
  private String plotAddress = null;

  @JsonProperty("location")
  private Boundary location = null;

  @JsonProperty("pinCode")
  private String pinCode = null;

  @JsonProperty("reservationCode")
  private String reservationCode = null;

  @JsonProperty("resolutionNumber")
  private String resolutionNumber = null;

  @JsonProperty("resolutionDate")
  private Long resolutionDate = null;

  @JsonProperty("latitude")
  private Double latitude = null;

  @JsonProperty("longitude")
  private Double longitude = null;

  @JsonProperty("landAcquisitionOfficerName")
  private String landAcquisitionOfficerName = null;

  @JsonProperty("landAcquisitionOfficerRemark")
  private String landAcquisitionOfficerRemark = null;

  @JsonProperty("paperNoticeNumber")
  private String paperNoticeNumber = null;

  @JsonProperty("advocateName")
  private String advocateName = null;

  @JsonProperty("panelAppointmentDate")
  private Long panelAppointmentDate = null;

  /**
   * Unique identifier (code) of the land acquisition Status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    CANCELED("CANCELED"),
    
    WORKFLOW("WORKFLOW");

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

  @JsonProperty("possessionOfLand")
  private List<LandPossession> possessionOfLand = null;

  @JsonProperty("landTransfer")
  private List<LandTransfer> landTransfer = null;

  @JsonProperty("valuationDetails")
  private List<ValuationDetail> valuationDetails = new ArrayList<ValuationDetail>();

  @JsonProperty("proposalDetails")
  private ProposalDetails proposalDetails = null;

  @JsonProperty("documents")
  private List<LandAcquisitionDocuments> documents = null;

  public LandAcquisition id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Land Acquisition
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Land Acquisition")

 @Size(max=64)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LandAcquisition tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the Land Acquisition
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the Land Acquisition")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public LandAcquisition landAcquisitionNumber(String landAcquisitionNumber) {
    this.landAcquisitionNumber = landAcquisitionNumber;
    return this;
  }

   /**
   * Unique Number of the Land Acquisition. This is  unique in system for a tenant. This is mandatory but always be generated on the creation. Hide this field if source is DATAENTRY
   * @return landAcquisitionNumber
  **/
  @ApiModelProperty(value = "Unique Number of the Land Acquisition. This is  unique in system for a tenant. This is mandatory but always be generated on the creation. Hide this field if source is DATAENTRY")

 @Size(max=20)
  public String getLandAcquisitionNumber() {
    return landAcquisitionNumber;
  }

  public void setLandAcquisitionNumber(String landAcquisitionNumber) {
    this.landAcquisitionNumber = landAcquisitionNumber;
  }

  public LandAcquisition landType(AssetCategory landType) {
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

  public LandAcquisition usage(Usage usage) {
    this.usage = usage;
    return this;
  }

   /**
   * Usage code will be used in land acquisition
   * @return usage
  **/
  @ApiModelProperty(value = "Usage code will be used in land acquisition")

  @Valid

  public Usage getUsage() {
    return usage;
  }

  public void setUsage(Usage usage) {
    this.usage = usage;
  }

  public LandAcquisition subUsage(SubUsage subUsage) {
    this.subUsage = subUsage;
    return this;
  }

   /**
   * Sub Usage code will be used in land acquisition
   * @return subUsage
  **/
  @ApiModelProperty(value = "Sub Usage code will be used in land acquisition")

  @Valid

  public SubUsage getSubUsage() {
    return subUsage;
  }

  public void setSubUsage(SubUsage subUsage) {
    this.subUsage = subUsage;
  }

  public LandAcquisition ctsNumber(String ctsNumber) {
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

  public LandAcquisition surveyNumber(String surveyNumber) {
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

  public LandAcquisition plotNumber(String plotNumber) {
    this.plotNumber = plotNumber;
    return this;
  }

   /**
   * Plot number of land
   * @return plotNumber
  **/
  @ApiModelProperty(value = "Plot number of land")

 @Size(max=25)
  public String getPlotNumber() {
    return plotNumber;
  }

  public void setPlotNumber(String plotNumber) {
    this.plotNumber = plotNumber;
  }

  public LandAcquisition plotArea(String plotArea) {
    this.plotArea = plotArea;
    return this;
  }

   /**
   * plot area of land. Unit of measure not selected as seperate field. User can enter area with description of unit in the same field
   * @return plotArea
  **/
  @ApiModelProperty(value = "plot area of land. Unit of measure not selected as seperate field. User can enter area with description of unit in the same field")

 @Size(max=64)
  public String getPlotArea() {
    return plotArea;
  }

  public void setPlotArea(String plotArea) {
    this.plotArea = plotArea;
  }

  public LandAcquisition plotAddress(String plotAddress) {
    this.plotAddress = plotAddress;
    return this;
  }

   /**
   * Address of land
   * @return plotAddress
  **/
  @ApiModelProperty(value = "Address of land")

 @Size(max=256)
  public String getPlotAddress() {
    return plotAddress;
  }

  public void setPlotAddress(String plotAddress) {
    this.plotAddress = plotAddress;
  }

  public LandAcquisition location(Boundary location) {
    this.location = location;
    return this;
  }

   /**
   * The boundary location where the land is located. Refer code of boundary.
   * @return location
  **/
  @ApiModelProperty(value = "The boundary location where the land is located. Refer code of boundary.")

  @Valid

  public Boundary getLocation() {
    return location;
  }

  public void setLocation(Boundary location) {
    this.location = location;
  }

  public LandAcquisition pinCode(String pinCode) {
    this.pinCode = pinCode;
    return this;
  }

   /**
   * pin code of the land location
   * @return pinCode
  **/
  @ApiModelProperty(value = "pin code of the land location")

 @Size(min=6,max=6)
  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(String pinCode) {
    this.pinCode = pinCode;
  }

  public LandAcquisition reservationCode(String reservationCode) {
    this.reservationCode = reservationCode;
    return this;
  }

   /**
   * Reservation code
   * @return reservationCode
  **/
  @ApiModelProperty(value = "Reservation code")

 @Pattern(regexp="[a-zA-Z0-9-\\\\]") @Size(max=32)
  public String getReservationCode() {
    return reservationCode;
  }

  public void setReservationCode(String reservationCode) {
    this.reservationCode = reservationCode;
  }

  public LandAcquisition resolutionNumber(String resolutionNumber) {
    this.resolutionNumber = resolutionNumber;
    return this;
  }

   /**
   * Resolution number of council
   * @return resolutionNumber
  **/
  @ApiModelProperty(value = "Resolution number of council")

 @Pattern(regexp="[a-zA-Z0-9-\\\\]") @Size(max=32)
  public String getResolutionNumber() {
    return resolutionNumber;
  }

  public void setResolutionNumber(String resolutionNumber) {
    this.resolutionNumber = resolutionNumber;
  }

  public LandAcquisition resolutionDate(Long resolutionDate) {
    this.resolutionDate = resolutionDate;
    return this;
  }

   /**
   * Resolution date as epoch
   * @return resolutionDate
  **/
  @ApiModelProperty(value = "Resolution date as epoch")


  public Long getResolutionDate() {
    return resolutionDate;
  }

  public void setResolutionDate(Long resolutionDate) {
    this.resolutionDate = resolutionDate;
  }

  public LandAcquisition latitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

   /**
   * latitude of the land (A location latitude)
   * @return latitude
  **/
  @ApiModelProperty(value = "latitude of the land (A location latitude)")


  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public LandAcquisition longitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

   /**
   * longitude of the land (A location longitude)
   * @return longitude
  **/
  @ApiModelProperty(value = "longitude of the land (A location longitude)")


  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public LandAcquisition landAcquisitionOfficerName(String landAcquisitionOfficerName) {
    this.landAcquisitionOfficerName = landAcquisitionOfficerName;
    return this;
  }

   /**
   * Special land aquisition officer name
   * @return landAcquisitionOfficerName
  **/
  @ApiModelProperty(required = true, value = "Special land aquisition officer name")
  @NotNull

 @Size(max=128)
  public String getLandAcquisitionOfficerName() {
    return landAcquisitionOfficerName;
  }

  public void setLandAcquisitionOfficerName(String landAcquisitionOfficerName) {
    this.landAcquisitionOfficerName = landAcquisitionOfficerName;
  }

  public LandAcquisition landAcquisitionOfficerRemark(String landAcquisitionOfficerRemark) {
    this.landAcquisitionOfficerRemark = landAcquisitionOfficerRemark;
    return this;
  }

   /**
   * Special land aquisition officer remarks
   * @return landAcquisitionOfficerRemark
  **/
  @ApiModelProperty(value = "Special land aquisition officer remarks")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]") @Size(max=512)
  public String getLandAcquisitionOfficerRemark() {
    return landAcquisitionOfficerRemark;
  }

  public void setLandAcquisitionOfficerRemark(String landAcquisitionOfficerRemark) {
    this.landAcquisitionOfficerRemark = landAcquisitionOfficerRemark;
  }

  public LandAcquisition paperNoticeNumber(String paperNoticeNumber) {
    this.paperNoticeNumber = paperNoticeNumber;
    return this;
  }

   /**
   * Published paper notice number
   * @return paperNoticeNumber
  **/
  @ApiModelProperty(required = true, value = "Published paper notice number")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-\\\\]") @Size(max=64)
  public String getPaperNoticeNumber() {
    return paperNoticeNumber;
  }

  public void setPaperNoticeNumber(String paperNoticeNumber) {
    this.paperNoticeNumber = paperNoticeNumber;
  }

  public LandAcquisition advocateName(String advocateName) {
    this.advocateName = advocateName;
    return this;
  }

   /**
   * Advocate Name
   * @return advocateName
  **/
  @ApiModelProperty(required = true, value = "Advocate Name")
  @NotNull

 @Size(max=128)
  public String getAdvocateName() {
    return advocateName;
  }

  public void setAdvocateName(String advocateName) {
    this.advocateName = advocateName;
  }

  public LandAcquisition panelAppointmentDate(Long panelAppointmentDate) {
    this.panelAppointmentDate = panelAppointmentDate;
    return this;
  }

   /**
   * Panal appointment date
   * @return panelAppointmentDate
  **/
  @ApiModelProperty(value = "Panal appointment date")


  public Long getPanelAppointmentDate() {
    return panelAppointmentDate;
  }

  public void setPanelAppointmentDate(Long panelAppointmentDate) {
    this.panelAppointmentDate = panelAppointmentDate;
  }

  public LandAcquisition status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Unique identifier (code) of the land acquisition Status
   * @return status
  **/
  @ApiModelProperty(value = "Unique identifier (code) of the land acquisition Status")


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public LandAcquisition workFlowDetails(WorkFlowDetails workFlowDetails) {
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

  public LandAcquisition stateId(String stateId) {
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

  public LandAcquisition auditDetails(AuditDetails auditDetails) {
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

  public LandAcquisition possessionOfLand(List<LandPossession> possessionOfLand) {
    this.possessionOfLand = possessionOfLand;
    return this;
  }

  public LandAcquisition addPossessionOfLandItem(LandPossession possessionOfLandItem) {
    if (this.possessionOfLand == null) {
      this.possessionOfLand = new ArrayList<LandPossession>();
    }
    this.possessionOfLand.add(possessionOfLandItem);
    return this;
  }

   /**
   * List of possession of land detail.
   * @return possessionOfLand
  **/
  @ApiModelProperty(value = "List of possession of land detail.")

  @Valid

  public List<LandPossession> getPossessionOfLand() {
    return possessionOfLand;
  }

  public void setPossessionOfLand(List<LandPossession> possessionOfLand) {
    this.possessionOfLand = possessionOfLand;
  }

  public LandAcquisition landTransfer(List<LandTransfer> landTransfer) {
    this.landTransfer = landTransfer;
    return this;
  }

  public LandAcquisition addLandTransferItem(LandTransfer landTransferItem) {
    if (this.landTransfer == null) {
      this.landTransfer = new ArrayList<LandTransfer>();
    }
    this.landTransfer.add(landTransferItem);
    return this;
  }

   /**
   * List of transfer of land detail.
   * @return landTransfer
  **/
  @ApiModelProperty(value = "List of transfer of land detail.")

  @Valid

  public List<LandTransfer> getLandTransfer() {
    return landTransfer;
  }

  public void setLandTransfer(List<LandTransfer> landTransfer) {
    this.landTransfer = landTransfer;
  }

  public LandAcquisition valuationDetails(List<ValuationDetail> valuationDetails) {
    this.valuationDetails = valuationDetails;
    return this;
  }

  public LandAcquisition addValuationDetailsItem(ValuationDetail valuationDetailsItem) {
    this.valuationDetails.add(valuationDetailsItem);
    return this;
  }

   /**
   * List of land valuation details.
   * @return valuationDetails
  **/
  @ApiModelProperty(required = true, value = "List of land valuation details.")
  @NotNull

  @Valid

  public List<ValuationDetail> getValuationDetails() {
    return valuationDetails;
  }

  public void setValuationDetails(List<ValuationDetail> valuationDetails) {
    this.valuationDetails = valuationDetails;
  }

  public LandAcquisition proposalDetails(ProposalDetails proposalDetails) {
    this.proposalDetails = proposalDetails;
    return this;
  }

   /**
   * refer id of proposal details for reference.
   * @return proposalDetails
  **/
  @ApiModelProperty(required = true, value = "refer id of proposal details for reference.")
  @NotNull

  @Valid

  public ProposalDetails getProposalDetails() {
    return proposalDetails;
  }

  public void setProposalDetails(ProposalDetails proposalDetails) {
    this.proposalDetails = proposalDetails;
  }

  public LandAcquisition documents(List<LandAcquisitionDocuments> documents) {
    this.documents = documents;
    return this;
  }

  public LandAcquisition addDocumentsItem(LandAcquisitionDocuments documentsItem) {
    if (this.documents == null) {
      this.documents = new ArrayList<LandAcquisitionDocuments>();
    }
    this.documents.add(documentsItem);
    return this;
  }

   /**
   * List of Support Documents for land acquisition
   * @return documents
  **/
  @ApiModelProperty(value = "List of Support Documents for land acquisition")

  @Valid

  public List<LandAcquisitionDocuments> getDocuments() {
    return documents;
  }

  public void setDocuments(List<LandAcquisitionDocuments> documents) {
    this.documents = documents;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LandAcquisition landAcquisition = (LandAcquisition) o;
    return Objects.equals(this.id, landAcquisition.id) &&
        Objects.equals(this.tenantId, landAcquisition.tenantId) &&
        Objects.equals(this.landAcquisitionNumber, landAcquisition.landAcquisitionNumber) &&
        Objects.equals(this.landType, landAcquisition.landType) &&
        Objects.equals(this.usage, landAcquisition.usage) &&
        Objects.equals(this.subUsage, landAcquisition.subUsage) &&
        Objects.equals(this.ctsNumber, landAcquisition.ctsNumber) &&
        Objects.equals(this.surveyNumber, landAcquisition.surveyNumber) &&
        Objects.equals(this.plotNumber, landAcquisition.plotNumber) &&
        Objects.equals(this.plotArea, landAcquisition.plotArea) &&
        Objects.equals(this.plotAddress, landAcquisition.plotAddress) &&
        Objects.equals(this.location, landAcquisition.location) &&
        Objects.equals(this.pinCode, landAcquisition.pinCode) &&
        Objects.equals(this.reservationCode, landAcquisition.reservationCode) &&
        Objects.equals(this.resolutionNumber, landAcquisition.resolutionNumber) &&
        Objects.equals(this.resolutionDate, landAcquisition.resolutionDate) &&
        Objects.equals(this.latitude, landAcquisition.latitude) &&
        Objects.equals(this.longitude, landAcquisition.longitude) &&
        Objects.equals(this.landAcquisitionOfficerName, landAcquisition.landAcquisitionOfficerName) &&
        Objects.equals(this.landAcquisitionOfficerRemark, landAcquisition.landAcquisitionOfficerRemark) &&
        Objects.equals(this.paperNoticeNumber, landAcquisition.paperNoticeNumber) &&
        Objects.equals(this.advocateName, landAcquisition.advocateName) &&
        Objects.equals(this.panelAppointmentDate, landAcquisition.panelAppointmentDate) &&
        Objects.equals(this.status, landAcquisition.status) &&
        Objects.equals(this.workFlowDetails, landAcquisition.workFlowDetails) &&
        Objects.equals(this.stateId, landAcquisition.stateId) &&
        Objects.equals(this.auditDetails, landAcquisition.auditDetails) &&
        Objects.equals(this.possessionOfLand, landAcquisition.possessionOfLand) &&
        Objects.equals(this.landTransfer, landAcquisition.landTransfer) &&
        Objects.equals(this.valuationDetails, landAcquisition.valuationDetails) &&
        Objects.equals(this.proposalDetails, landAcquisition.proposalDetails) &&
        Objects.equals(this.documents, landAcquisition.documents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, landAcquisitionNumber, landType, usage, subUsage, ctsNumber, surveyNumber, plotNumber, plotArea, plotAddress, location, pinCode, reservationCode, resolutionNumber, resolutionDate, latitude, longitude, landAcquisitionOfficerName, landAcquisitionOfficerRemark, paperNoticeNumber, advocateName, panelAppointmentDate, status, workFlowDetails, stateId, auditDetails, possessionOfLand, landTransfer, valuationDetails, proposalDetails, documents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandAcquisition {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    landAcquisitionNumber: ").append(toIndentedString(landAcquisitionNumber)).append("\n");
    sb.append("    landType: ").append(toIndentedString(landType)).append("\n");
    sb.append("    usage: ").append(toIndentedString(usage)).append("\n");
    sb.append("    subUsage: ").append(toIndentedString(subUsage)).append("\n");
    sb.append("    ctsNumber: ").append(toIndentedString(ctsNumber)).append("\n");
    sb.append("    surveyNumber: ").append(toIndentedString(surveyNumber)).append("\n");
    sb.append("    plotNumber: ").append(toIndentedString(plotNumber)).append("\n");
    sb.append("    plotArea: ").append(toIndentedString(plotArea)).append("\n");
    sb.append("    plotAddress: ").append(toIndentedString(plotAddress)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    pinCode: ").append(toIndentedString(pinCode)).append("\n");
    sb.append("    reservationCode: ").append(toIndentedString(reservationCode)).append("\n");
    sb.append("    resolutionNumber: ").append(toIndentedString(resolutionNumber)).append("\n");
    sb.append("    resolutionDate: ").append(toIndentedString(resolutionDate)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    landAcquisitionOfficerName: ").append(toIndentedString(landAcquisitionOfficerName)).append("\n");
    sb.append("    landAcquisitionOfficerRemark: ").append(toIndentedString(landAcquisitionOfficerRemark)).append("\n");
    sb.append("    paperNoticeNumber: ").append(toIndentedString(paperNoticeNumber)).append("\n");
    sb.append("    advocateName: ").append(toIndentedString(advocateName)).append("\n");
    sb.append("    panelAppointmentDate: ").append(toIndentedString(panelAppointmentDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("    possessionOfLand: ").append(toIndentedString(possessionOfLand)).append("\n");
    sb.append("    landTransfer: ").append(toIndentedString(landTransfer)).append("\n");
    sb.append("    valuationDetails: ").append(toIndentedString(valuationDetails)).append("\n");
    sb.append("    proposalDetails: ").append(toIndentedString(proposalDetails)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
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

