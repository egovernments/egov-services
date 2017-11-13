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
 * A Object holds the data for a Estate
 */
@ApiModel(description = "A Object holds the data for a Estate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class EstateRegister   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("oldEstateNumber")
  private String oldEstateNumber = null;

  @JsonProperty("estateNumber")
  private String estateNumber = null;

  @JsonProperty("register")
  private Register register = null;

  @JsonProperty("subRegister")
  private SubRegister subRegister = null;

  @JsonProperty("regionalOffice")
  private Boundary regionalOffice = null;

  @JsonProperty("location")
  private Boundary location = null;

  @JsonProperty("propertyName")
  private String propertyName = null;

  @JsonProperty("propertyType")
  private PropertyType propertyType = null;

  @JsonProperty("propertyAddress")
  private String propertyAddress = null;

  @JsonProperty("surveyNo")
  private String surveyNo = null;

  @JsonProperty("gattNo")
  private String gattNo = null;

  @JsonProperty("buildUpArea")
  private Double buildUpArea = null;

  @JsonProperty("purpose")
  private String purpose = null;

  @JsonProperty("modeOfAcquisition")
  private String modeOfAcquisition = null;

  @JsonProperty("carpetArea")
  private Double carpetArea = null;

  @JsonProperty("holdingType")
  private String holdingType = null;

  @JsonProperty("landID")
  private String landID = null;

  @JsonProperty("constructionStartDate")
  private Long constructionStartDate = null;

  @JsonProperty("constructionEndDate")
  private Long constructionEndDate = null;

  @JsonProperty("proposedBuildingBudget")
  private Double proposedBuildingBudget = null;

  @JsonProperty("actualBuildingExpense")
  private Double actualBuildingExpense = null;

  @JsonProperty("latitude")
  private Double latitude = null;

  @JsonProperty("longitude")
  private Double longitude = null;

  @JsonProperty("floors")
  private List<FloorDetail> floors = new ArrayList<FloorDetail>();

  /**
   * enumeration of estate register statuses.
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
   * source from which estate Register is being persisted.
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

  @JsonProperty("stateId")
  private String stateId = null;

  @JsonProperty("workFlowDetails")
  private WorkFlowDetails workFlowDetails = null;

  @JsonProperty("comments")
  private String comments = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public EstateRegister id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the EstateRegister.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the EstateRegister.")

 @Size(min=1,max=256)
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EstateRegister tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique Identifier of the tenant
   * @return tenantId
  **/
  @ApiModelProperty(readOnly = true, value = "Unique Identifier of the tenant")

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public EstateRegister oldEstateNumber(String oldEstateNumber) {
    this.oldEstateNumber = oldEstateNumber;
    return this;
  }

   /**
   * Old estate ref no, this is mandatory when source is DATA_ENTRY or MIGRATION
   * @return oldEstateNumber
  **/
  @ApiModelProperty(value = "Old estate ref no, this is mandatory when source is DATA_ENTRY or MIGRATION")

 @Size(max=64)
  public String getOldEstateNumber() {
    return oldEstateNumber;
  }

  public void setOldEstateNumber(String oldEstateNumber) {
    this.oldEstateNumber = oldEstateNumber;
  }

  public EstateRegister estateNumber(String estateNumber) {
    this.estateNumber = estateNumber;
    return this;
  }

   /**
   * Unique Estate ref. no
   * @return estateNumber
  **/
  @ApiModelProperty(value = "Unique Estate ref. no")

 @Size(max=64)
  public String getEstateNumber() {
    return estateNumber;
  }

  public void setEstateNumber(String estateNumber) {
    this.estateNumber = estateNumber;
  }

  public EstateRegister register(Register register) {
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

  public EstateRegister subRegister(SubRegister subRegister) {
    this.subRegister = subRegister;
    return this;
  }

   /**
   * Get subRegister
   * @return subRegister
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public SubRegister getSubRegister() {
    return subRegister;
  }

  public void setSubRegister(SubRegister subRegister) {
    this.subRegister = subRegister;
  }

  public EstateRegister regionalOffice(Boundary regionalOffice) {
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

  public EstateRegister location(Boundary location) {
    this.location = location;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return location
  **/
  @ApiModelProperty(required = true, value = "code of the master stored here as a ref.")
 // @NotNull

 // @Valid

  public Boundary getLocation() {
    return location;
  }

  public void setLocation(Boundary location) {
    this.location = location;
  }

  public EstateRegister propertyName(String propertyName) {
    this.propertyName = propertyName;
    return this;
  }

   /**
   * Name of the property
   * @return propertyName
  **/
  @ApiModelProperty(value = "Name of the property")

 @Size(max=256)
  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public EstateRegister propertyType(PropertyType propertyType) {
    this.propertyType = propertyType;
    return this;
  }

   /**
   * code of the master stored here as a ref.
   * @return propertyType
  **/
  @ApiModelProperty(required = true, value = "code of the master stored here as a ref.")
  @NotNull

  @Valid

  public PropertyType getPropertyType() {
    return propertyType;
  }

  public void setPropertyType(PropertyType propertyType) {
    this.propertyType = propertyType;
  }

  public EstateRegister propertyAddress(String propertyAddress) {
    this.propertyAddress = propertyAddress;
    return this;
  }

   /**
   * Address of the property
   * @return propertyAddress
  **/
  @ApiModelProperty(required = true, value = "Address of the property")
  @NotNull

 @Size(min=1,max=512)
  public String getPropertyAddress() {
    return propertyAddress;
  }

  public void setPropertyAddress(String propertyAddress) {
    this.propertyAddress = propertyAddress;
  }

  public EstateRegister surveyNo(String surveyNo) {
    this.surveyNo = surveyNo;
    return this;
  }

   /**
   * Estate Survey no
   * @return surveyNo
  **/
  @ApiModelProperty(required = true, value = "Estate Survey no")
  @NotNull

 @Size(min=1,max=64)
  public String getSurveyNo() {
    return surveyNo;
  }

  public void setSurveyNo(String surveyNo) {
    this.surveyNo = surveyNo;
  }

  public EstateRegister gattNo(String gattNo) {
    this.gattNo = gattNo;
    return this;
  }

   /**
   * Gatt or CTS no for a Estate
   * @return gattNo
  **/
  @ApiModelProperty(required = true, value = "Gatt or CTS no for a Estate")
  @NotNull

 @Size(min=1,max=64)
  public String getGattNo() {
    return gattNo;
  }

  public void setGattNo(String gattNo) {
    this.gattNo = gattNo;
  }

  public EstateRegister buildUpArea(Double buildUpArea) {
    this.buildUpArea = buildUpArea;
    return this;
  }

   /**
   * built up area of the Estate
   * @return buildUpArea
  **/
  @ApiModelProperty(value = "built up area of the Estate")


  public Double getBuildUpArea() {
    return buildUpArea;
  }

  public void setBuildUpArea(Double buildUpArea) {
    this.buildUpArea = buildUpArea;
  }

  public EstateRegister purpose(String purpose) {
    this.purpose = purpose;
    return this;
  }

   /**
   * purpose of the Estate
   * @return purpose
  **/
  @ApiModelProperty(required = true, value = "purpose of the Estate")
  @NotNull

 @Size(min=1,max=64)
  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public EstateRegister modeOfAcquisition(String modeOfAcquisition) {
    this.modeOfAcquisition = modeOfAcquisition;
    return this;
  }

   /**
   * mode of acquisition for a Estate
   * @return modeOfAcquisition
  **/
  @ApiModelProperty(required = true, value = "mode of acquisition for a Estate")
  @NotNull

 @Size(max=64)
  public String getModeOfAcquisition() {
    return modeOfAcquisition;
  }

  public void setModeOfAcquisition(String modeOfAcquisition) {
    this.modeOfAcquisition = modeOfAcquisition;
  }

  public EstateRegister carpetArea(Double carpetArea) {
    this.carpetArea = carpetArea;
    return this;
  }

   /**
   * Carpet Area of the Estate
   * @return carpetArea
  **/
  @ApiModelProperty(value = "Carpet Area of the Estate")


  public Double getCarpetArea() {
    return carpetArea;
  }

  public void setCarpetArea(Double carpetArea) {
    this.carpetArea = carpetArea;
  }

  public EstateRegister holdingType(String holdingType) {
    this.holdingType = holdingType;
    return this;
  }

   /**
   * type of holding for a Estate
   * @return holdingType
  **/
  @ApiModelProperty(required = true, value = "type of holding for a Estate")
  @NotNull

 @Size(max=64)
  public String getHoldingType() {
    return holdingType;
  }

  public void setHoldingType(String holdingType) {
    this.holdingType = holdingType;
  }

  public EstateRegister landID(String landID) {
    this.landID = landID;
    return this;
  }

   /**
   * Land id of the Estate
   * @return landID
  **/
  @ApiModelProperty(value = "Land id of the Estate")

 @Size(max=64)
  public String getLandID() {
    return landID;
  }

  public void setLandID(String landID) {
    this.landID = landID;
  }

  public EstateRegister constructionStartDate(Long constructionStartDate) {
    this.constructionStartDate = constructionStartDate;
    return this;
  }

   /**
   * construction start date of a Estate, Date is inclduing timestamp, date in epoch
   * @return constructionStartDate
  **/
  @ApiModelProperty(required = true, value = "construction start date of a Estate, Date is inclduing timestamp, date in epoch")
  @NotNull


  public Long getConstructionStartDate() {
    return constructionStartDate;
  }

  public void setConstructionStartDate(Long constructionStartDate) {
    this.constructionStartDate = constructionStartDate;
  }

  public EstateRegister constructionEndDate(Long constructionEndDate) {
    this.constructionEndDate = constructionEndDate;
    return this;
  }

   /**
   * construction end date of a Estate, Date is inclduing timestamp, date in epoch
   * @return constructionEndDate
  **/
  @ApiModelProperty(required = true, value = "construction end date of a Estate, Date is inclduing timestamp, date in epoch")
  @NotNull


  public Long getConstructionEndDate() {
    return constructionEndDate;
  }

  public void setConstructionEndDate(Long constructionEndDate) {
    this.constructionEndDate = constructionEndDate;
  }

  public EstateRegister proposedBuildingBudget(Double proposedBuildingBudget) {
    this.proposedBuildingBudget = proposedBuildingBudget;
    return this;
  }

   /**
   * Proposed Building Expense of the Estate
   * @return proposedBuildingBudget
  **/
  @ApiModelProperty(value = "Proposed Building Expense of the Estate")


  public Double getProposedBuildingBudget() {
    return proposedBuildingBudget;
  }

  public void setProposedBuildingBudget(Double proposedBuildingBudget) {
    this.proposedBuildingBudget = proposedBuildingBudget;
  }

  public EstateRegister actualBuildingExpense(Double actualBuildingExpense) {
    this.actualBuildingExpense = actualBuildingExpense;
    return this;
  }

   /**
   * Actual Building Expense of the Estate
   * @return actualBuildingExpense
  **/
  @ApiModelProperty(value = "Actual Building Expense of the Estate")


  public Double getActualBuildingExpense() {
    return actualBuildingExpense;
  }

  public void setActualBuildingExpense(Double actualBuildingExpense) {
    this.actualBuildingExpense = actualBuildingExpense;
  }

  public EstateRegister latitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

   /**
   * latitude of the Estate (A location latitude)
   * @return latitude
  **/
  @ApiModelProperty(value = "latitude of the Estate (A location latitude)")


  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public EstateRegister longitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

   /**
   * longitude of the Estate (A location longitude)
   * @return longitude
  **/
  @ApiModelProperty(value = "longitude of the Estate (A location longitude)")


  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public EstateRegister floors(List<FloorDetail> floors) {
    this.floors = floors;
    return this;
  }

  public EstateRegister addFloorsItem(FloorDetail floorsItem) {
    this.floors.add(floorsItem);
    return this;
  }

   /**
   * Get floors
   * @return floors
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<FloorDetail> getFloors() {
    return floors;
  }

  public void setFloors(List<FloorDetail> floors) {
    this.floors = floors;
  }

  public EstateRegister status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * enumeration of estate register statuses.
   * @return status
  **/
  @ApiModelProperty(value = "enumeration of estate register statuses.")


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public EstateRegister source(SourceEnum source) {
    this.source = source;
    return this;
  }

   /**
   * source from which estate Register is being persisted.
   * @return source
  **/
  @ApiModelProperty(value = "source from which estate Register is being persisted.")


  public SourceEnum getSource() {
    return source;
  }

  public void setSource(SourceEnum source) {
    this.source = source;
  }

  public EstateRegister stateId(String stateId) {
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

  public EstateRegister workFlowDetails(WorkFlowDetails workFlowDetails) {
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

  public EstateRegister comments(String comments) {
    this.comments = comments;
    return this;
  }

   /**
   * description or comments for a land
   * @return comments
  **/
  @ApiModelProperty(value = "description or comments for a land")

 @Size(max=1024)
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public EstateRegister auditDetails(AuditDetails auditDetails) {
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
    EstateRegister estateRegister = (EstateRegister) o;
    return Objects.equals(this.id, estateRegister.id) &&
        Objects.equals(this.tenantId, estateRegister.tenantId) &&
        Objects.equals(this.oldEstateNumber, estateRegister.oldEstateNumber) &&
        Objects.equals(this.estateNumber, estateRegister.estateNumber) &&
        Objects.equals(this.register, estateRegister.register) &&
        Objects.equals(this.subRegister, estateRegister.subRegister) &&
        Objects.equals(this.regionalOffice, estateRegister.regionalOffice) &&
        Objects.equals(this.location, estateRegister.location) &&
        Objects.equals(this.propertyName, estateRegister.propertyName) &&
        Objects.equals(this.propertyType, estateRegister.propertyType) &&
        Objects.equals(this.propertyAddress, estateRegister.propertyAddress) &&
        Objects.equals(this.surveyNo, estateRegister.surveyNo) &&
        Objects.equals(this.gattNo, estateRegister.gattNo) &&
        Objects.equals(this.buildUpArea, estateRegister.buildUpArea) &&
        Objects.equals(this.purpose, estateRegister.purpose) &&
        Objects.equals(this.modeOfAcquisition, estateRegister.modeOfAcquisition) &&
        Objects.equals(this.carpetArea, estateRegister.carpetArea) &&
        Objects.equals(this.holdingType, estateRegister.holdingType) &&
        Objects.equals(this.landID, estateRegister.landID) &&
        Objects.equals(this.constructionStartDate, estateRegister.constructionStartDate) &&
        Objects.equals(this.constructionEndDate, estateRegister.constructionEndDate) &&
        Objects.equals(this.proposedBuildingBudget, estateRegister.proposedBuildingBudget) &&
        Objects.equals(this.actualBuildingExpense, estateRegister.actualBuildingExpense) &&
        Objects.equals(this.latitude, estateRegister.latitude) &&
        Objects.equals(this.longitude, estateRegister.longitude) &&
        Objects.equals(this.floors, estateRegister.floors) &&
        Objects.equals(this.status, estateRegister.status) &&
        Objects.equals(this.source, estateRegister.source) &&
        Objects.equals(this.stateId, estateRegister.stateId) &&
        Objects.equals(this.workFlowDetails, estateRegister.workFlowDetails) &&
        Objects.equals(this.comments, estateRegister.comments) &&
        Objects.equals(this.auditDetails, estateRegister.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, oldEstateNumber, estateNumber, register, subRegister, regionalOffice, location, propertyName, propertyType, propertyAddress, surveyNo, gattNo, buildUpArea, purpose, modeOfAcquisition, carpetArea, holdingType, landID, constructionStartDate, constructionEndDate, proposedBuildingBudget, actualBuildingExpense, latitude, longitude, floors, status, source, stateId, workFlowDetails, comments, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstateRegister {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    oldEstateNumber: ").append(toIndentedString(oldEstateNumber)).append("\n");
    sb.append("    estateNumber: ").append(toIndentedString(estateNumber)).append("\n");
    sb.append("    register: ").append(toIndentedString(register)).append("\n");
    sb.append("    subRegister: ").append(toIndentedString(subRegister)).append("\n");
    sb.append("    regionalOffice: ").append(toIndentedString(regionalOffice)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    propertyName: ").append(toIndentedString(propertyName)).append("\n");
    sb.append("    propertyType: ").append(toIndentedString(propertyType)).append("\n");
    sb.append("    propertyAddress: ").append(toIndentedString(propertyAddress)).append("\n");
    sb.append("    surveyNo: ").append(toIndentedString(surveyNo)).append("\n");
    sb.append("    gattNo: ").append(toIndentedString(gattNo)).append("\n");
    sb.append("    buildUpArea: ").append(toIndentedString(buildUpArea)).append("\n");
    sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
    sb.append("    modeOfAcquisition: ").append(toIndentedString(modeOfAcquisition)).append("\n");
    sb.append("    carpetArea: ").append(toIndentedString(carpetArea)).append("\n");
    sb.append("    holdingType: ").append(toIndentedString(holdingType)).append("\n");
    sb.append("    landID: ").append(toIndentedString(landID)).append("\n");
    sb.append("    constructionStartDate: ").append(toIndentedString(constructionStartDate)).append("\n");
    sb.append("    constructionEndDate: ").append(toIndentedString(constructionEndDate)).append("\n");
    sb.append("    proposedBuildingBudget: ").append(toIndentedString(proposedBuildingBudget)).append("\n");
    sb.append("    actualBuildingExpense: ").append(toIndentedString(actualBuildingExpense)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    floors: ").append(toIndentedString(floors)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

