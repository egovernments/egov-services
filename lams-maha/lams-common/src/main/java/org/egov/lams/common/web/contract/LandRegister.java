package org.egov.lams.common.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A Object holds the data for a Land
 */
@ApiModel(description = "A Object holds the data for a Land")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class LandRegister   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("oldLandNumber")
  private String oldLandNumber = null;

  @JsonProperty("landNumber")
  private String landNumber = null;

  /**
   * Land acquisition type.
   */
  public enum AcquisitionTypeEnum {
    NEW("NEW"),
    
    EXISTING("EXISTING");

    private String value;

    AcquisitionTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AcquisitionTypeEnum fromValue(String text) {
      for (AcquisitionTypeEnum b : AcquisitionTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("acquisitionType")
  private AcquisitionTypeEnum acquisitionType = null;

  @JsonProperty("acquisitionNo")
  private String acquisitionNo = null;

  @JsonProperty("register")
  private Register register = null;

  @JsonProperty("subRegister")
  private SubRegister subRegister = null;

  @JsonProperty("regionalOffice")
  private Boundary regionalOffice = null;

  @JsonProperty("location")
  private Boundary location = null;

  @JsonProperty("surveyNo")
  private String surveyNo = null;

  @JsonProperty("gattNo")
  private String gattNo = null;

  @JsonProperty("oldOwnerName")
  private String oldOwnerName = null;

  @JsonProperty("purpose")
  private Purpose purpose = null;

  @JsonProperty("width")
  private Double width = null;

  @JsonProperty("roadType")
  private RoadType roadType = null;

  @JsonProperty("landType")
  private AssetCategory landType = null;

  @JsonProperty("reservationArea")
  private Double reservationArea = null;

  @JsonProperty("landArea")
  private Double landArea = null;

  @JsonProperty("areaAsPerRegister")
  private Double areaAsPerRegister = null;

  @JsonProperty("possessionDate")
  private Long possessionDate = null;

  @JsonProperty("modeOfAcquisition")
  private ModeOfAcquisition modeOfAcquisition = null;

  @JsonProperty("typeOfHolding")
  private HoldingType typeOfHolding = null;

  @JsonProperty("modeOfCompensation")
  private CompensationMode modeOfCompensation = null;

  @JsonProperty("costOfAcquisition")
  private Double costOfAcquisition = null;

  @JsonProperty("dateOfPayment")
  private Long dateOfPayment = null;

  @JsonProperty("buildingReference")
  private String buildingReference = null;

  @JsonProperty("typeOfPlanning")
  private PlaningType typeOfPlanning = null;

  @JsonProperty("codeOfReservation")
  private String codeOfReservation = null;

  /**
   * enumeration of land register statuses.
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    WORKFLOW("WORKFLOW"),
    
    INACTIVE("INACTIVE");

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

  /**
   * source from which Land Register is being persisted.
   */
  public enum SourceEnum {
    SYSTEM("SYSTEM"),
    
    DATE_ENTRY("DATE_ENTRY"),
    
    MIGRATION("MIGRATION"),
    
    CFC("CFC");

    private String value;

    SourceEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SourceEnum fromValue(String text) {
      for (SourceEnum b : SourceEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("source")
  private SourceEnum source = null;

  @JsonProperty("documents")
  private List<LandDocs> documents = null;

  @JsonProperty("stateId")
  private String stateId = null;

  @JsonProperty("workFlowDetails")
  private WorkFlowDetails workFlowDetails = null;

  @JsonProperty("comments")
  private String comments = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public LandRegister id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the LandRegister.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the LandRegister.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LandRegister tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique Identifier of the tenant
   * @return tenantId
  **/
  @ApiModelProperty(required = true, readOnly = true, value = "Unique Identifier of the tenant")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public LandRegister oldLandNumber(String oldLandNumber) {
    this.oldLandNumber = oldLandNumber;
    return this;
  }

   /**
   * Old land ref no, this is mandatory when source is DATA_ENTRY or MIGRATION
   * @return oldLandNumber
  **/
  @ApiModelProperty(value = "Old land ref no, this is mandatory when source is DATA_ENTRY or MIGRATION")

 @Size(max=64)
  public String getOldLandNumber() {
    return oldLandNumber;
  }

  public void setOldLandNumber(String oldLandNumber) {
    this.oldLandNumber = oldLandNumber;
  }

  public LandRegister landNumber(String landNumber) {
    this.landNumber = landNumber;
    return this;
  }

   /**
   * Unique Land ref. no
   * @return landNumber
  **/
  @ApiModelProperty(value = "Unique Land ref. no")

 @Size(max=64)
  public String getLandNumber() {
    return landNumber;
  }

  public void setLandNumber(String landNumber) {
    this.landNumber = landNumber;
  }

  public LandRegister acquisitionType(AcquisitionTypeEnum acquisitionType) {
    this.acquisitionType = acquisitionType;
    return this;
  }

   /**
   * Land acquisition type.
   * @return acquisitionType
  **/
  @ApiModelProperty(required = true, value = "Land acquisition type.")
  @NotNull


  public AcquisitionTypeEnum getAcquisitionType() {
    return acquisitionType;
  }

  public void setAcquisitionType(AcquisitionTypeEnum acquisitionType) {
    this.acquisitionType = acquisitionType;
  }

  public LandRegister acquisitionNo(String acquisitionNo) {
    this.acquisitionNo = acquisitionNo;
    return this;
  }

   /**
   * Land acquisition no.
   * @return acquisitionNo
  **/
  @ApiModelProperty(required = true, value = "Land acquisition no.")
  @NotNull

 @Size(min=1,max=64)
  public String getAcquisitionNo() {
    return acquisitionNo;
  }

  public void setAcquisitionNo(String acquisitionNo) {
    this.acquisitionNo = acquisitionNo;
  }

  public LandRegister register(Register register) {
    this.register = register;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return register
  **/
  @ApiModelProperty(required = true, value = "code of the master stored here as a ref.")
  @NotNull

  @Valid

  public Register getRegister() {
    return register;
  }

  public void setRegister(Register register) {
    this.register = register;
  }

  public LandRegister subRegister(SubRegister subRegister) {
    this.subRegister = subRegister;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return subRegister
  **/
  @ApiModelProperty(value = "code of the master stored here as a ref.")

  @Valid

  public SubRegister getSubRegister() {
    return subRegister;
  }

  public void setSubRegister(SubRegister subRegister) {
    this.subRegister = subRegister;
  }

  public LandRegister regionalOffice(Boundary regionalOffice) {
    this.regionalOffice = regionalOffice;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return regionalOffice
  **/
  @ApiModelProperty(required = true, value = "code of the master stored here as a ref.")
  @NotNull

  @Valid

  public Boundary getRegionalOffice() {
    return regionalOffice;
  }

  public void setRegionalOffice(Boundary regionalOffice) {
    this.regionalOffice = regionalOffice;
  }

  public LandRegister location(Boundary location) {
    this.location = location;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return location
  **/
  @ApiModelProperty(required = true, value = "code of the master stored here as a ref.")
  @NotNull

  @Valid

  public Boundary getLocation() {
    return location;
  }

  public void setLocation(Boundary location) {
    this.location = location;
  }

  public LandRegister surveyNo(String surveyNo) {
    this.surveyNo = surveyNo;
    return this;
  }

   /**
   * Land Survey no
   * @return surveyNo
  **/
  @ApiModelProperty(required = true, value = "Land Survey no")
  @NotNull

 @Size(min=1,max=64)
  public String getSurveyNo() {
    return surveyNo;
  }

  public void setSurveyNo(String surveyNo) {
    this.surveyNo = surveyNo;
  }

  public LandRegister gattNo(String gattNo) {
    this.gattNo = gattNo;
    return this;
  }

   /**
   * Gatt or CTS no for a Land
   * @return gattNo
  **/
  @ApiModelProperty(value = "Gatt or CTS no for a Land")

 @Size(min=1,max=64)
  public String getGattNo() {
    return gattNo;
  }

  public void setGattNo(String gattNo) {
    this.gattNo = gattNo;
  }

  public LandRegister oldOwnerName(String oldOwnerName) {
    this.oldOwnerName = oldOwnerName;
    return this;
  }

   /**
   * Old owner name of a Land
   * @return oldOwnerName
  **/
  @ApiModelProperty(required = true, value = "Old owner name of a Land")
  @NotNull

 @Size(min=1,max=256)
  public String getOldOwnerName() {
    return oldOwnerName;
  }

  public void setOldOwnerName(String oldOwnerName) {
    this.oldOwnerName = oldOwnerName;
  }

  public LandRegister purpose(Purpose purpose) {
    this.purpose = purpose;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return purpose
  **/
  @ApiModelProperty(required = true, value = "code of the master stored here as a ref.")
  @NotNull

  @Valid

  public Purpose getPurpose() {
    return purpose;
  }

  public void setPurpose(Purpose purpose) {
    this.purpose = purpose;
  }

  public LandRegister width(Double width) {
    this.width = width;
    return this;
  }

   /**
   * width of the land
   * @return width
  **/
  @ApiModelProperty(required = true, value = "width of the land")
  @NotNull


  public Double getWidth() {
    return width;
  }

  public void setWidth(Double width) {
    this.width = width;
  }

  public LandRegister roadType(RoadType roadType) {
    this.roadType = roadType;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return roadType
  **/
  @ApiModelProperty(value = "code of the master stored here as a ref.")

  @Valid

  public RoadType getRoadType() {
    return roadType;
  }

  public void setRoadType(RoadType roadType) {
    this.roadType = roadType;
  }

  public LandRegister landType(AssetCategory landType) {
    this.landType = landType;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return landType
  **/
  @ApiModelProperty(required = true, value = "code of the master stored here as a ref.")
  @NotNull

  @Valid

  public AssetCategory getLandType() {
    return landType;
  }

  public void setLandType(AssetCategory landType) {
    this.landType = landType;
  }

  public LandRegister reservationArea(Double reservationArea) {
    this.reservationArea = reservationArea;
    return this;
  }

   /**
   * Reservation Area of the land
   * @return reservationArea
  **/
  @ApiModelProperty(value = "Reservation Area of the land")


  public Double getReservationArea() {
    return reservationArea;
  }

  public void setReservationArea(Double reservationArea) {
    this.reservationArea = reservationArea;
  }

  public LandRegister landArea(Double landArea) {
    this.landArea = landArea;
    return this;
  }

   /**
   * Land Area
   * @return landArea
  **/
  @ApiModelProperty(required = true, value = "Land Area")
  @NotNull


  public Double getLandArea() {
    return landArea;
  }

  public void setLandArea(Double landArea) {
    this.landArea = landArea;
  }

  public LandRegister areaAsPerRegister(Double areaAsPerRegister) {
    this.areaAsPerRegister = areaAsPerRegister;
    return this;
  }

   /**
   * Land Area as per register
   * @return areaAsPerRegister
  **/
  @ApiModelProperty(required = true, value = "Land Area as per register")
  @NotNull


  public Double getAreaAsPerRegister() {
    return areaAsPerRegister;
  }

  public void setAreaAsPerRegister(Double areaAsPerRegister) {
    this.areaAsPerRegister = areaAsPerRegister;
  }

  public LandRegister possessionDate(Long possessionDate) {
    this.possessionDate = possessionDate;
    return this;
  }

   /**
   * possession date of a land, Date is inclduing timestamp, date in epoch
   * @return possessionDate
  **/
  @ApiModelProperty(required = true, value = "possession date of a land, Date is inclduing timestamp, date in epoch")
  @NotNull


  public Long getPossessionDate() {
    return possessionDate;
  }

  public void setPossessionDate(Long possessionDate) {
    this.possessionDate = possessionDate;
  }

  public LandRegister modeOfAcquisition(ModeOfAcquisition modeOfAcquisition) {
    this.modeOfAcquisition = modeOfAcquisition;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return modeOfAcquisition
  **/
  @ApiModelProperty(required = true, value = "code of the master stored here as a ref.")
  @NotNull

  @Valid

  public ModeOfAcquisition getModeOfAcquisition() {
    return modeOfAcquisition;
  }

  public void setModeOfAcquisition(ModeOfAcquisition modeOfAcquisition) {
    this.modeOfAcquisition = modeOfAcquisition;
  }

  public LandRegister typeOfHolding(HoldingType typeOfHolding) {
    this.typeOfHolding = typeOfHolding;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return typeOfHolding
  **/
  @ApiModelProperty(required = true, value = "code of the master stored here as a ref.")
  @NotNull

  @Valid

  public HoldingType getTypeOfHolding() {
    return typeOfHolding;
  }

  public void setTypeOfHolding(HoldingType typeOfHolding) {
    this.typeOfHolding = typeOfHolding;
  }

  public LandRegister modeOfCompensation(CompensationMode modeOfCompensation) {
    this.modeOfCompensation = modeOfCompensation;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return modeOfCompensation
  **/
  @ApiModelProperty(value = "code of the master stored here as a ref.")

  @Valid

  public CompensationMode getModeOfCompensation() {
    return modeOfCompensation;
  }

  public void setModeOfCompensation(CompensationMode modeOfCompensation) {
    this.modeOfCompensation = modeOfCompensation;
  }

  public LandRegister costOfAcquisition(Double costOfAcquisition) {
    this.costOfAcquisition = costOfAcquisition;
    return this;
  }

   /**
   * cost of acquisition for a land
   * @return costOfAcquisition
  **/
  @ApiModelProperty(required = true, value = "cost of acquisition for a land")
  @NotNull


  public Double getCostOfAcquisition() {
    return costOfAcquisition;
  }

  public void setCostOfAcquisition(Double costOfAcquisition) {
    this.costOfAcquisition = costOfAcquisition;
  }

  public LandRegister dateOfPayment(Long dateOfPayment) {
    this.dateOfPayment = dateOfPayment;
    return this;
  }

   /**
   * payment date of a land, Date is inclduing timestamp, date in epoch
   * @return dateOfPayment
  **/
  @ApiModelProperty(value = "payment date of a land, Date is inclduing timestamp, date in epoch")


  public Long getDateOfPayment() {
    return dateOfPayment;
  }

  public void setDateOfPayment(Long dateOfPayment) {
    this.dateOfPayment = dateOfPayment;
  }

  public LandRegister buildingReference(String buildingReference) {
    this.buildingReference = buildingReference;
    return this;
  }

   /**
   * Building Reference for a land
   * @return buildingReference
  **/
  @ApiModelProperty(value = "Building Reference for a land")

 @Size(min=1,max=64)
  public String getBuildingReference() {
    return buildingReference;
  }

  public void setBuildingReference(String buildingReference) {
    this.buildingReference = buildingReference;
  }

  public LandRegister typeOfPlanning(PlaningType typeOfPlanning) {
    this.typeOfPlanning = typeOfPlanning;
    return this;
  }

   /**
   * Get typeOfPlanning
   * @return typeOfPlanning
  **/
  @ApiModelProperty(value = "")

  @Valid

  public PlaningType getTypeOfPlanning() {
    return typeOfPlanning;
  }

  public void setTypeOfPlanning(PlaningType typeOfPlanning) {
    this.typeOfPlanning = typeOfPlanning;
  }

  public LandRegister codeOfReservation(String codeOfReservation) {
    this.codeOfReservation = codeOfReservation;
    return this;
  }

   /**
   * Code Of Reservation for a land
   * @return codeOfReservation
  **/
  @ApiModelProperty(value = "Code Of Reservation for a land")

 @Size(min=1,max=64)
  public String getCodeOfReservation() {
    return codeOfReservation;
  }

  public void setCodeOfReservation(String codeOfReservation) {
    this.codeOfReservation = codeOfReservation;
  }

  public LandRegister status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * enumeration of land register statuses.
   * @return status
  **/
  @ApiModelProperty(value = "enumeration of land register statuses.")


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public LandRegister source(SourceEnum source) {
    this.source = source;
    return this;
  }

   /**
   * source from which Land Register is being persisted.
   * @return source
  **/
  @ApiModelProperty(value = "source from which Land Register is being persisted.")


  public SourceEnum getSource() {
    return source;
  }

  public void setSource(SourceEnum source) {
    this.source = source;
  }

  public LandRegister documents(List<LandDocs> documents) {
    this.documents = documents;
    return this;
  }

  public LandRegister addDocumentsItem(LandDocs documentsItem) {
    if (this.documents == null) {
      this.documents = new ArrayList<LandDocs>();
    }
    this.documents.add(documentsItem);
    return this;
  }

   /**
   * Get documents
   * @return documents
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<LandDocs> getDocuments() {
    return documents;
  }

  public void setDocuments(List<LandDocs> documents) {
    this.documents = documents;
  }

  public LandRegister stateId(String stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * Work flow ref id.
   * @return stateId
  **/
  @ApiModelProperty(value = "Work flow ref id.")


  public String getStateId() {
    return stateId;
  }

  public void setStateId(String stateId) {
    this.stateId = stateId;
  }

  public LandRegister workFlowDetails(WorkFlowDetails workFlowDetails) {
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

  public LandRegister comments(String comments) {
    this.comments = comments;
    return this;
  }

   /**
   * description or comments for a land
   * @return comments
  **/
  @ApiModelProperty(value = "description or comments for a land")

 @Size(min=1,max=1024)
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public LandRegister auditDetails(AuditDetails auditDetails) {
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
    LandRegister landRegister = (LandRegister) o;
    return Objects.equals(this.id, landRegister.id) &&
        Objects.equals(this.tenantId, landRegister.tenantId) &&
        Objects.equals(this.oldLandNumber, landRegister.oldLandNumber) &&
        Objects.equals(this.landNumber, landRegister.landNumber) &&
        Objects.equals(this.acquisitionType, landRegister.acquisitionType) &&
        Objects.equals(this.acquisitionNo, landRegister.acquisitionNo) &&
        Objects.equals(this.register, landRegister.register) &&
        Objects.equals(this.subRegister, landRegister.subRegister) &&
        Objects.equals(this.regionalOffice, landRegister.regionalOffice) &&
        Objects.equals(this.location, landRegister.location) &&
        Objects.equals(this.surveyNo, landRegister.surveyNo) &&
        Objects.equals(this.gattNo, landRegister.gattNo) &&
        Objects.equals(this.oldOwnerName, landRegister.oldOwnerName) &&
        Objects.equals(this.purpose, landRegister.purpose) &&
        Objects.equals(this.width, landRegister.width) &&
        Objects.equals(this.roadType, landRegister.roadType) &&
        Objects.equals(this.landType, landRegister.landType) &&
        Objects.equals(this.reservationArea, landRegister.reservationArea) &&
        Objects.equals(this.landArea, landRegister.landArea) &&
        Objects.equals(this.areaAsPerRegister, landRegister.areaAsPerRegister) &&
        Objects.equals(this.possessionDate, landRegister.possessionDate) &&
        Objects.equals(this.modeOfAcquisition, landRegister.modeOfAcquisition) &&
        Objects.equals(this.typeOfHolding, landRegister.typeOfHolding) &&
        Objects.equals(this.modeOfCompensation, landRegister.modeOfCompensation) &&
        Objects.equals(this.costOfAcquisition, landRegister.costOfAcquisition) &&
        Objects.equals(this.dateOfPayment, landRegister.dateOfPayment) &&
        Objects.equals(this.buildingReference, landRegister.buildingReference) &&
        Objects.equals(this.typeOfPlanning, landRegister.typeOfPlanning) &&
        Objects.equals(this.codeOfReservation, landRegister.codeOfReservation) &&
        Objects.equals(this.status, landRegister.status) &&
        Objects.equals(this.source, landRegister.source) &&
        Objects.equals(this.documents, landRegister.documents) &&
        Objects.equals(this.stateId, landRegister.stateId) &&
        Objects.equals(this.workFlowDetails, landRegister.workFlowDetails) &&
        Objects.equals(this.comments, landRegister.comments) &&
        Objects.equals(this.auditDetails, landRegister.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, oldLandNumber, landNumber, acquisitionType, acquisitionNo, register, subRegister, regionalOffice, location, surveyNo, gattNo, oldOwnerName, purpose, width, roadType, landType, reservationArea, landArea, areaAsPerRegister, possessionDate, modeOfAcquisition, typeOfHolding, modeOfCompensation, costOfAcquisition, dateOfPayment, buildingReference, typeOfPlanning, codeOfReservation, status, source, documents, stateId, workFlowDetails, comments, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandRegister {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    oldLandNumber: ").append(toIndentedString(oldLandNumber)).append("\n");
    sb.append("    landNumber: ").append(toIndentedString(landNumber)).append("\n");
    sb.append("    acquisitionType: ").append(toIndentedString(acquisitionType)).append("\n");
    sb.append("    acquisitionNo: ").append(toIndentedString(acquisitionNo)).append("\n");
    sb.append("    register: ").append(toIndentedString(register)).append("\n");
    sb.append("    subRegister: ").append(toIndentedString(subRegister)).append("\n");
    sb.append("    regionalOffice: ").append(toIndentedString(regionalOffice)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    surveyNo: ").append(toIndentedString(surveyNo)).append("\n");
    sb.append("    gattNo: ").append(toIndentedString(gattNo)).append("\n");
    sb.append("    oldOwnerName: ").append(toIndentedString(oldOwnerName)).append("\n");
    sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    roadType: ").append(toIndentedString(roadType)).append("\n");
    sb.append("    landType: ").append(toIndentedString(landType)).append("\n");
    sb.append("    reservationArea: ").append(toIndentedString(reservationArea)).append("\n");
    sb.append("    landArea: ").append(toIndentedString(landArea)).append("\n");
    sb.append("    areaAsPerRegister: ").append(toIndentedString(areaAsPerRegister)).append("\n");
    sb.append("    possessionDate: ").append(toIndentedString(possessionDate)).append("\n");
    sb.append("    modeOfAcquisition: ").append(toIndentedString(modeOfAcquisition)).append("\n");
    sb.append("    typeOfHolding: ").append(toIndentedString(typeOfHolding)).append("\n");
    sb.append("    modeOfCompensation: ").append(toIndentedString(modeOfCompensation)).append("\n");
    sb.append("    costOfAcquisition: ").append(toIndentedString(costOfAcquisition)).append("\n");
    sb.append("    dateOfPayment: ").append(toIndentedString(dateOfPayment)).append("\n");
    sb.append("    buildingReference: ").append(toIndentedString(buildingReference)).append("\n");
    sb.append("    typeOfPlanning: ").append(toIndentedString(typeOfPlanning)).append("\n");
    sb.append("    codeOfReservation: ").append(toIndentedString(codeOfReservation)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
    sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
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

