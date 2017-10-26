package org.egov.works.services.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object holds the basic data for a Detailed Estimate
 */
@ApiModel(description = "An Object holds the basic data for a Detailed Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-25T07:37:26.972Z")

public class DetailedEstimate   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("estimateNumber")
  private String estimateNumber = null;

  @JsonProperty("estimateDate")
  private Long estimateDate = null;

  @JsonProperty("nameOfWork")
  private String nameOfWork = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("department")
  private String department = null;

  @JsonProperty("adminSanctionNumber")
  private String adminSanctionNumber = null;

  @JsonProperty("adminSanctionDate")
  private Long adminSanctionDate = null;

  @JsonProperty("adminSanctionBy")
  private String adminSanctionBy = null;

  @JsonProperty("status")
  private DetailedEstimateStatus status = null;

  @JsonProperty("workValue")
  private BigDecimal workValue = null;

  @JsonProperty("estimateValue")
  private BigDecimal estimateValue = null;

  @JsonProperty("projectCode")
  private ProjectCode projectCode = null;

  @JsonProperty("parent")
  private String parent = null;

  @JsonProperty("copiedFrom")
  private DetailedEstimate copiedFrom = null;

  @JsonProperty("approvedDate")
  private Long approvedDate = null;

  @JsonProperty("approvedBy")
  private String approvedBy = null;

  @JsonProperty("copiedEstimate")
  private Boolean copiedEstimate = null;

  @JsonProperty("beneficiary")
  private Beneficiary beneficiary = null;

  @JsonProperty("modeOfAllotment")
  private ModeOfAllotment modeOfAllotment = null;

  @JsonProperty("worksType")
  private TypeOfWork worksType = null;

  @JsonProperty("worksSubtype")
  private TypeOfWork worksSubtype = null;

  @JsonProperty("natureOfWork")
  private NatureOfWork natureOfWork = null;

  @JsonProperty("ward")
  private String ward = null;

  @JsonProperty("location")
  private String location = null;

  @JsonProperty("latitude")
  private Integer latitude = null;

  @JsonProperty("longitude")
  private Integer longitude = null;

  @JsonProperty("workCategory")
  private WorkCategory workCategory = null;

  @JsonProperty("locality")
  private String locality = null;

  @JsonProperty("councilResolutionNumber")
  private String councilResolutionNumber = null;

  @JsonProperty("councilResolutionDate")
  private String councilResolutionDate = null;

  @JsonProperty("workOrderCreated")
  private Boolean workOrderCreated = null;

  @JsonProperty("billsCreated")
  private Boolean billsCreated = null;

  @JsonProperty("spillOverFlag")
  private Boolean spillOverFlag = null;

  @JsonProperty("grossAmountBilled")
  private Integer grossAmountBilled = null;

  @JsonProperty("cancellationReason")
  private String cancellationReason = null;

  @JsonProperty("cancellationRemarks")
  private String cancellationRemarks = null;

  @JsonProperty("totalIncludingRE")
  private Integer totalIncludingRE = null;

  @JsonProperty("abstractEstimateDetails")
  private List<AbstractEstimateDetails> abstractEstimateDetails = null;

  @JsonProperty("documentDetails")
  private List<DocumentDetail> documentDetails = null;

  @JsonProperty("assetValues")
  private List<AssetsForEstimate> assetValues = null;

  @JsonProperty("overheadValues")
  private List<OverheadValue> overheadValues = null;

  @JsonProperty("estimatePhotographsList")
  private List<DocumentDetail> estimatePhotographsList = null;

  @JsonProperty("workFlowDetails")
  private WorkFlowDetails workFlowDetails = null;

  @JsonProperty("stateId")
  private String stateId = null;

  @JsonProperty("fund")
  private String fund = null;

  @JsonProperty("function")
  private String function = null;

  @JsonProperty("functionary")
  private String functionary = null;

  @JsonProperty("scheme")
  private String scheme = null;

  @JsonProperty("subScheme")
  private String subScheme = null;

  @JsonProperty("budgetGroup")
  private String budgetGroup = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public DetailedEstimate id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Detailed Estimate
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Detailed Estimate")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DetailedEstimate tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Detailed Estimate
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Detailed Estimate")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public DetailedEstimate estimateNumber(String estimateNumber) {
    this.estimateNumber = estimateNumber;
    return this;
  }

   /**
   * Unique number for the Detailed Estimate
   * @return estimateNumber
  **/
  @ApiModelProperty(required = true, value = "Unique number for the Detailed Estimate")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-\\\\]") @Size(min=1,max=50)
  public String getEstimateNumber() {
    return estimateNumber;
  }

  public void setEstimateNumber(String estimateNumber) {
    this.estimateNumber = estimateNumber;
  }

  public DetailedEstimate estimateDate(Long estimateDate) {
    this.estimateDate = estimateDate;
    return this;
  }

   /**
   * Epoch time of the Detailed Estimate Created
   * @return estimateDate
  **/
  @ApiModelProperty(required = true, value = "Epoch time of the Detailed Estimate Created")
  @NotNull


  public Long getEstimateDate() {
    return estimateDate;
  }

  public void setEstimateDate(Long estimateDate) {
    this.estimateDate = estimateDate;
  }

  public DetailedEstimate nameOfWork(String nameOfWork) {
    this.nameOfWork = nameOfWork;
    return this;
  }

   /**
   * name of the work of Detailed Estimate Details
   * @return nameOfWork
  **/
  @ApiModelProperty(required = true, value = "name of the work of Detailed Estimate Details")
  @NotNull

 @Size(min=1,max=1024)
  public String getNameOfWork() {
    return nameOfWork;
  }

  public void setNameOfWork(String nameOfWork) {
    this.nameOfWork = nameOfWork;
  }

  public DetailedEstimate description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description for the Detailed Estimate
   * @return description
  **/
  @ApiModelProperty(required = true, value = "description for the Detailed Estimate")
  @NotNull

 @Size(min=1,max=1024)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DetailedEstimate department(String department) {
    this.department = department;
    return this;
  }

   /**
   * Department for which Detailed Estimate belongs to
   * @return department
  **/
  @ApiModelProperty(required = true, value = "Department for which Detailed Estimate belongs to")
  @NotNull


  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public DetailedEstimate adminSanctionNumber(String adminSanctionNumber) {
    this.adminSanctionNumber = adminSanctionNumber;
    return this;
  }

   /**
   * Unique number after admin sanction for the Detailed Estimate
   * @return adminSanctionNumber
  **/
  @ApiModelProperty(value = "Unique number after admin sanction for the Detailed Estimate")

 @Pattern(regexp="[a-zA-Z0-9-\\\\]")
  public String getAdminSanctionNumber() {
    return adminSanctionNumber;
  }

  public void setAdminSanctionNumber(String adminSanctionNumber) {
    this.adminSanctionNumber = adminSanctionNumber;
  }

  public DetailedEstimate adminSanctionDate(Long adminSanctionDate) {
    this.adminSanctionDate = adminSanctionDate;
    return this;
  }

   /**
   * Epoch time of when the admin santion is done
   * @return adminSanctionDate
  **/
  @ApiModelProperty(value = "Epoch time of when the admin santion is done")


  public Long getAdminSanctionDate() {
    return adminSanctionDate;
  }

  public void setAdminSanctionDate(Long adminSanctionDate) {
    this.adminSanctionDate = adminSanctionDate;
  }

  public DetailedEstimate adminSanctionBy(String adminSanctionBy) {
    this.adminSanctionBy = adminSanctionBy;
    return this;
  }

   /**
   * user who admin sanctioned
   * @return adminSanctionBy
  **/
  @ApiModelProperty(value = "user who admin sanctioned")


  public String getAdminSanctionBy() {
    return adminSanctionBy;
  }

  public void setAdminSanctionBy(String adminSanctionBy) {
    this.adminSanctionBy = adminSanctionBy;
  }

  public DetailedEstimate status(DetailedEstimateStatus status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public DetailedEstimateStatus getStatus() {
    return status;
  }

  public void setStatus(DetailedEstimateStatus status) {
    this.status = status;
  }

  public DetailedEstimate workValue(BigDecimal workValue) {
    this.workValue = workValue;
    return this;
  }

   /**
   * Work value of the Detailed Estimate
   * @return workValue
  **/
  @ApiModelProperty(value = "Work value of the Detailed Estimate")

  @Valid

  public BigDecimal getWorkValue() {
    return workValue;
  }

  public void setWorkValue(BigDecimal workValue) {
    this.workValue = workValue;
  }

  public DetailedEstimate estimateValue(BigDecimal estimateValue) {
    this.estimateValue = estimateValue;
    return this;
  }

   /**
   * Estimate value of the Detailed Estimate
   * @return estimateValue
  **/
  @ApiModelProperty(value = "Estimate value of the Detailed Estimate")

  @Valid

  public BigDecimal getEstimateValue() {
    return estimateValue;
  }

  public void setEstimateValue(BigDecimal estimateValue) {
    this.estimateValue = estimateValue;
  }

  public DetailedEstimate projectCode(ProjectCode projectCode) {
    this.projectCode = projectCode;
    return this;
  }

   /**
   * Get projectCode
   * @return projectCode
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ProjectCode getProjectCode() {
    return projectCode;
  }

  public void setProjectCode(ProjectCode projectCode) {
    this.projectCode = projectCode;
  }

  public DetailedEstimate parent(String parent) {
    this.parent = parent;
    return this;
  }

   /**
   * Parent of the Detailed Estimate
   * @return parent
  **/
  @ApiModelProperty(value = "Parent of the Detailed Estimate")


  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public DetailedEstimate copiedFrom(DetailedEstimate copiedFrom) {
    this.copiedFrom = copiedFrom;
    return this;
  }

   /**
   * Get copiedFrom
   * @return copiedFrom
  **/
  @ApiModelProperty(value = "")

  @Valid

  public DetailedEstimate getCopiedFrom() {
    return copiedFrom;
  }

  public void setCopiedFrom(DetailedEstimate copiedFrom) {
    this.copiedFrom = copiedFrom;
  }

  public DetailedEstimate approvedDate(Long approvedDate) {
    this.approvedDate = approvedDate;
    return this;
  }

   /**
   * Epoch time of when Detailed Estimate created
   * @return approvedDate
  **/
  @ApiModelProperty(value = "Epoch time of when Detailed Estimate created")


  public Long getApprovedDate() {
    return approvedDate;
  }

  public void setApprovedDate(Long approvedDate) {
    this.approvedDate = approvedDate;
  }

  public DetailedEstimate approvedBy(String approvedBy) {
    this.approvedBy = approvedBy;
    return this;
  }

   /**
   * Unique username of the user who approved Detailed Estimate
   * @return approvedBy
  **/
  @ApiModelProperty(value = "Unique username of the user who approved Detailed Estimate")


  public String getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(String approvedBy) {
    this.approvedBy = approvedBy;
  }

  public DetailedEstimate copiedEstimate(Boolean copiedEstimate) {
    this.copiedEstimate = copiedEstimate;
    return this;
  }

   /**
   * Boolean value to identify whether the Detailed Estimate is copied from another estimate
   * @return copiedEstimate
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the Detailed Estimate is copied from another estimate")


  public Boolean getCopiedEstimate() {
    return copiedEstimate;
  }

  public void setCopiedEstimate(Boolean copiedEstimate) {
    this.copiedEstimate = copiedEstimate;
  }

  public DetailedEstimate beneficiary(Beneficiary beneficiary) {
    this.beneficiary = beneficiary;
    return this;
  }

   /**
   * Get beneficiary
   * @return beneficiary
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Beneficiary getBeneficiary() {
    return beneficiary;
  }

  public void setBeneficiary(Beneficiary beneficiary) {
    this.beneficiary = beneficiary;
  }

  public DetailedEstimate modeOfAllotment(ModeOfAllotment modeOfAllotment) {
    this.modeOfAllotment = modeOfAllotment;
    return this;
  }

   /**
   * Get modeOfAllotment
   * @return modeOfAllotment
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ModeOfAllotment getModeOfAllotment() {
    return modeOfAllotment;
  }

  public void setModeOfAllotment(ModeOfAllotment modeOfAllotment) {
    this.modeOfAllotment = modeOfAllotment;
  }

  public DetailedEstimate worksType(TypeOfWork worksType) {
    this.worksType = worksType;
    return this;
  }

   /**
   * Get worksType
   * @return worksType
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public TypeOfWork getWorksType() {
    return worksType;
  }

  public void setWorksType(TypeOfWork worksType) {
    this.worksType = worksType;
  }

  public DetailedEstimate worksSubtype(TypeOfWork worksSubtype) {
    this.worksSubtype = worksSubtype;
    return this;
  }

   /**
   * Get worksSubtype
   * @return worksSubtype
  **/
  @ApiModelProperty(value = "")

  @Valid

  public TypeOfWork getWorksSubtype() {
    return worksSubtype;
  }

  public void setWorksSubtype(TypeOfWork worksSubtype) {
    this.worksSubtype = worksSubtype;
  }

  public DetailedEstimate natureOfWork(NatureOfWork natureOfWork) {
    this.natureOfWork = natureOfWork;
    return this;
  }

   /**
   * Get natureOfWork
   * @return natureOfWork
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public NatureOfWork getNatureOfWork() {
    return natureOfWork;
  }

  public void setNatureOfWork(NatureOfWork natureOfWork) {
    this.natureOfWork = natureOfWork;
  }

  public DetailedEstimate ward(String ward) {
    this.ward = ward;
    return this;
  }

   /**
   * Ward of the Detailed Estimate
   * @return ward
  **/
  @ApiModelProperty(required = true, value = "Ward of the Detailed Estimate")
  @NotNull


  public String getWard() {
    return ward;
  }

  public void setWard(String ward) {
    this.ward = ward;
  }

  public DetailedEstimate location(String location) {
    this.location = location;
    return this;
  }

   /**
   * Location of the Detailed Estimate
   * @return location
  **/
  @ApiModelProperty(value = "Location of the Detailed Estimate")


  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public DetailedEstimate latitude(Integer latitude) {
    this.latitude = latitude;
    return this;
  }

   /**
   * Latitude of the location
   * @return latitude
  **/
  @ApiModelProperty(value = "Latitude of the location")


  public Integer getLatitude() {
    return latitude;
  }

  public void setLatitude(Integer latitude) {
    this.latitude = latitude;
  }

  public DetailedEstimate longitude(Integer longitude) {
    this.longitude = longitude;
    return this;
  }

   /**
   * Longitude of the location
   * @return longitude
  **/
  @ApiModelProperty(value = "Longitude of the location")


  public Integer getLongitude() {
    return longitude;
  }

  public void setLongitude(Integer longitude) {
    this.longitude = longitude;
  }

  public DetailedEstimate workCategory(WorkCategory workCategory) {
    this.workCategory = workCategory;
    return this;
  }

   /**
   * Get workCategory
   * @return workCategory
  **/
  @ApiModelProperty(value = "")

  @Valid

  public WorkCategory getWorkCategory() {
    return workCategory;
  }

  public void setWorkCategory(WorkCategory workCategory) {
    this.workCategory = workCategory;
  }

  public DetailedEstimate locality(String locality) {
    this.locality = locality;
    return this;
  }

   /**
   * Locality of the Detailed Estimate
   * @return locality
  **/
  @ApiModelProperty(value = "Locality of the Detailed Estimate")


  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public DetailedEstimate councilResolutionNumber(String councilResolutionNumber) {
    this.councilResolutionNumber = councilResolutionNumber;
    return this;
  }

   /**
   * council resolution number of the Detailed Estimate
   * @return councilResolutionNumber
  **/
  @ApiModelProperty(value = "council resolution number of the Detailed Estimate")

 @Size(min=3,max=50)
  public String getCouncilResolutionNumber() {
    return councilResolutionNumber;
  }

  public void setCouncilResolutionNumber(String councilResolutionNumber) {
    this.councilResolutionNumber = councilResolutionNumber;
  }

  public DetailedEstimate councilResolutionDate(String councilResolutionDate) {
    this.councilResolutionDate = councilResolutionDate;
    return this;
  }

   /**
   * Epoch time of the council resolution date
   * @return councilResolutionDate
  **/
  @ApiModelProperty(value = "Epoch time of the council resolution date")


  public String getCouncilResolutionDate() {
    return councilResolutionDate;
  }

  public void setCouncilResolutionDate(String councilResolutionDate) {
    this.councilResolutionDate = councilResolutionDate;
  }

  public DetailedEstimate workOrderCreated(Boolean workOrderCreated) {
    this.workOrderCreated = workOrderCreated;
    return this;
  }

   /**
   * Boolean value to identify whether the work order created or not for spillover data
   * @return workOrderCreated
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the work order created or not for spillover data")


  public Boolean getWorkOrderCreated() {
    return workOrderCreated;
  }

  public void setWorkOrderCreated(Boolean workOrderCreated) {
    this.workOrderCreated = workOrderCreated;
  }

  public DetailedEstimate billsCreated(Boolean billsCreated) {
    this.billsCreated = billsCreated;
    return this;
  }

   /**
   * Boolean value to identify whether the bills created or not for spillover data
   * @return billsCreated
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the bills created or not for spillover data")


  public Boolean getBillsCreated() {
    return billsCreated;
  }

  public void setBillsCreated(Boolean billsCreated) {
    this.billsCreated = billsCreated;
  }

  public DetailedEstimate spillOverFlag(Boolean spillOverFlag) {
    this.spillOverFlag = spillOverFlag;
    return this;
  }

   /**
   * Boolean value to identify whether the spill over or not
   * @return spillOverFlag
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the spill over or not")


  public Boolean getSpillOverFlag() {
    return spillOverFlag;
  }

  public void setSpillOverFlag(Boolean spillOverFlag) {
    this.spillOverFlag = spillOverFlag;
  }

  public DetailedEstimate grossAmountBilled(Integer grossAmountBilled) {
    this.grossAmountBilled = grossAmountBilled;
    return this;
  }

   /**
   * Gross Amount Billed if billsCreated flag is true
   * @return grossAmountBilled
  **/
  @ApiModelProperty(value = "Gross Amount Billed if billsCreated flag is true")


  public Integer getGrossAmountBilled() {
    return grossAmountBilled;
  }

  public void setGrossAmountBilled(Integer grossAmountBilled) {
    this.grossAmountBilled = grossAmountBilled;
  }

  public DetailedEstimate cancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
    return this;
  }

   /**
   * Reason for cancellation of the Detailed Estimate, Required only while cancelling Detailed Estimate
   * @return cancellationReason
  **/
  @ApiModelProperty(value = "Reason for cancellation of the Detailed Estimate, Required only while cancelling Detailed Estimate")


  public String getCancellationReason() {
    return cancellationReason;
  }

  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  public DetailedEstimate cancellationRemarks(String cancellationRemarks) {
    this.cancellationRemarks = cancellationRemarks;
    return this;
  }

   /**
   * Remarks for cancellation of the Detailed Estimate, Required only while cancelling Detailed Estimate
   * @return cancellationRemarks
  **/
  @ApiModelProperty(value = "Remarks for cancellation of the Detailed Estimate, Required only while cancelling Detailed Estimate")

 @Size(min=3,max=512)
  public String getCancellationRemarks() {
    return cancellationRemarks;
  }

  public void setCancellationRemarks(String cancellationRemarks) {
    this.cancellationRemarks = cancellationRemarks;
  }

  public DetailedEstimate totalIncludingRE(Integer totalIncludingRE) {
    this.totalIncludingRE = totalIncludingRE;
    return this;
  }

   /**
   * Total Estimate Amount including Revision Estimates
   * @return totalIncludingRE
  **/
  @ApiModelProperty(value = "Total Estimate Amount including Revision Estimates")


  public Integer getTotalIncludingRE() {
    return totalIncludingRE;
  }

  public void setTotalIncludingRE(Integer totalIncludingRE) {
    this.totalIncludingRE = totalIncludingRE;
  }

  public DetailedEstimate abstractEstimateDetails(List<AbstractEstimateDetails> abstractEstimateDetails) {
    this.abstractEstimateDetails = abstractEstimateDetails;
    return this;
  }

  public DetailedEstimate addAbstractEstimateDetailsItem(AbstractEstimateDetails abstractEstimateDetailsItem) {
    if (this.abstractEstimateDetails == null) {
      this.abstractEstimateDetails = new ArrayList<AbstractEstimateDetails>();
    }
    this.abstractEstimateDetails.add(abstractEstimateDetailsItem);
    return this;
  }

   /**
   * Array of Abstract Estimate Details
   * @return abstractEstimateDetails
  **/
  @ApiModelProperty(value = "Array of Abstract Estimate Details")

  @Valid

  public List<AbstractEstimateDetails> getAbstractEstimateDetails() {
    return abstractEstimateDetails;
  }

  public void setAbstractEstimateDetails(List<AbstractEstimateDetails> abstractEstimateDetails) {
    this.abstractEstimateDetails = abstractEstimateDetails;
  }

  public DetailedEstimate documentDetails(List<DocumentDetail> documentDetails) {
    this.documentDetails = documentDetails;
    return this;
  }

  public DetailedEstimate addDocumentDetailsItem(DocumentDetail documentDetailsItem) {
    if (this.documentDetails == null) {
      this.documentDetails = new ArrayList<DocumentDetail>();
    }
    this.documentDetails.add(documentDetailsItem);
    return this;
  }

   /**
   * Array of document details
   * @return documentDetails
  **/
  @ApiModelProperty(value = "Array of document details")

  @Valid

  public List<DocumentDetail> getDocumentDetails() {
    return documentDetails;
  }

  public void setDocumentDetails(List<DocumentDetail> documentDetails) {
    this.documentDetails = documentDetails;
  }

  public DetailedEstimate assetValues(List<AssetsForEstimate> assetValues) {
    this.assetValues = assetValues;
    return this;
  }

  public DetailedEstimate addAssetValuesItem(AssetsForEstimate assetValuesItem) {
    if (this.assetValues == null) {
      this.assetValues = new ArrayList<AssetsForEstimate>();
    }
    this.assetValues.add(assetValuesItem);
    return this;
  }

   /**
   * Asstet Values for Detailed Estimate
   * @return assetValues
  **/
  @ApiModelProperty(value = "Asstet Values for Detailed Estimate")

  @Valid

  public List<AssetsForEstimate> getAssetValues() {
    return assetValues;
  }

  public void setAssetValues(List<AssetsForEstimate> assetValues) {
    this.assetValues = assetValues;
  }

  public DetailedEstimate overheadValues(List<OverheadValue> overheadValues) {
    this.overheadValues = overheadValues;
    return this;
  }

  public DetailedEstimate addOverheadValuesItem(OverheadValue overheadValuesItem) {
    if (this.overheadValues == null) {
      this.overheadValues = new ArrayList<OverheadValue>();
    }
    this.overheadValues.add(overheadValuesItem);
    return this;
  }

   /**
   * Array of Overhead Values
   * @return overheadValues
  **/
  @ApiModelProperty(value = "Array of Overhead Values")

  @Valid

  public List<OverheadValue> getOverheadValues() {
    return overheadValues;
  }

  public void setOverheadValues(List<OverheadValue> overheadValues) {
    this.overheadValues = overheadValues;
  }

  public DetailedEstimate estimatePhotographsList(List<DocumentDetail> estimatePhotographsList) {
    this.estimatePhotographsList = estimatePhotographsList;
    return this;
  }

  public DetailedEstimate addEstimatePhotographsListItem(DocumentDetail estimatePhotographsListItem) {
    if (this.estimatePhotographsList == null) {
      this.estimatePhotographsList = new ArrayList<DocumentDetail>();
    }
    this.estimatePhotographsList.add(estimatePhotographsListItem);
    return this;
  }

   /**
   * Array of Estimate Photographs List
   * @return estimatePhotographsList
  **/
  @ApiModelProperty(value = "Array of Estimate Photographs List")

  @Valid

  public List<DocumentDetail> getEstimatePhotographsList() {
    return estimatePhotographsList;
  }

  public void setEstimatePhotographsList(List<DocumentDetail> estimatePhotographsList) {
    this.estimatePhotographsList = estimatePhotographsList;
  }

  public DetailedEstimate workFlowDetails(WorkFlowDetails workFlowDetails) {
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

  public DetailedEstimate stateId(String stateId) {
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

  public DetailedEstimate fund(String fund) {
    this.fund = fund;
    return this;
  }

   /**
   * Fund of the Detailed Estimate
   * @return fund
  **/
  @ApiModelProperty(value = "Fund of the Detailed Estimate")


  public String getFund() {
    return fund;
  }

  public void setFund(String fund) {
    this.fund = fund;
  }

  public DetailedEstimate function(String function) {
    this.function = function;
    return this;
  }

   /**
   * Function of the Detailed Estimate
   * @return function
  **/
  @ApiModelProperty(value = "Function of the Detailed Estimate")


  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public DetailedEstimate functionary(String functionary) {
    this.functionary = functionary;
    return this;
  }

   /**
   * Functionary of the Detailed Estimate
   * @return functionary
  **/
  @ApiModelProperty(value = "Functionary of the Detailed Estimate")


  public String getFunctionary() {
    return functionary;
  }

  public void setFunctionary(String functionary) {
    this.functionary = functionary;
  }

  public DetailedEstimate scheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

   /**
   * Scheme of the Detailed Estimate
   * @return scheme
  **/
  @ApiModelProperty(value = "Scheme of the Detailed Estimate")


  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public DetailedEstimate subScheme(String subScheme) {
    this.subScheme = subScheme;
    return this;
  }

   /**
   * Sub Scheme of the Detailed Estimate
   * @return subScheme
  **/
  @ApiModelProperty(value = "Sub Scheme of the Detailed Estimate")


  public String getSubScheme() {
    return subScheme;
  }

  public void setSubScheme(String subScheme) {
    this.subScheme = subScheme;
  }

  public DetailedEstimate budgetGroup(String budgetGroup) {
    this.budgetGroup = budgetGroup;
    return this;
  }

   /**
   * Budget Group of the Detailed Estimate
   * @return budgetGroup
  **/
  @ApiModelProperty(value = "Budget Group of the Detailed Estimate")


  public String getBudgetGroup() {
    return budgetGroup;
  }

  public void setBudgetGroup(String budgetGroup) {
    this.budgetGroup = budgetGroup;
  }

  public DetailedEstimate auditDetails(AuditDetails auditDetails) {
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
    DetailedEstimate detailedEstimate = (DetailedEstimate) o;
    return Objects.equals(this.id, detailedEstimate.id) &&
        Objects.equals(this.tenantId, detailedEstimate.tenantId) &&
        Objects.equals(this.estimateNumber, detailedEstimate.estimateNumber) &&
        Objects.equals(this.estimateDate, detailedEstimate.estimateDate) &&
        Objects.equals(this.nameOfWork, detailedEstimate.nameOfWork) &&
        Objects.equals(this.description, detailedEstimate.description) &&
        Objects.equals(this.department, detailedEstimate.department) &&
        Objects.equals(this.adminSanctionNumber, detailedEstimate.adminSanctionNumber) &&
        Objects.equals(this.adminSanctionDate, detailedEstimate.adminSanctionDate) &&
        Objects.equals(this.adminSanctionBy, detailedEstimate.adminSanctionBy) &&
        Objects.equals(this.status, detailedEstimate.status) &&
        Objects.equals(this.workValue, detailedEstimate.workValue) &&
        Objects.equals(this.estimateValue, detailedEstimate.estimateValue) &&
        Objects.equals(this.projectCode, detailedEstimate.projectCode) &&
        Objects.equals(this.parent, detailedEstimate.parent) &&
        Objects.equals(this.copiedFrom, detailedEstimate.copiedFrom) &&
        Objects.equals(this.approvedDate, detailedEstimate.approvedDate) &&
        Objects.equals(this.approvedBy, detailedEstimate.approvedBy) &&
        Objects.equals(this.copiedEstimate, detailedEstimate.copiedEstimate) &&
        Objects.equals(this.beneficiary, detailedEstimate.beneficiary) &&
        Objects.equals(this.modeOfAllotment, detailedEstimate.modeOfAllotment) &&
        Objects.equals(this.worksType, detailedEstimate.worksType) &&
        Objects.equals(this.worksSubtype, detailedEstimate.worksSubtype) &&
        Objects.equals(this.natureOfWork, detailedEstimate.natureOfWork) &&
        Objects.equals(this.ward, detailedEstimate.ward) &&
        Objects.equals(this.location, detailedEstimate.location) &&
        Objects.equals(this.latitude, detailedEstimate.latitude) &&
        Objects.equals(this.longitude, detailedEstimate.longitude) &&
        Objects.equals(this.workCategory, detailedEstimate.workCategory) &&
        Objects.equals(this.locality, detailedEstimate.locality) &&
        Objects.equals(this.councilResolutionNumber, detailedEstimate.councilResolutionNumber) &&
        Objects.equals(this.councilResolutionDate, detailedEstimate.councilResolutionDate) &&
        Objects.equals(this.workOrderCreated, detailedEstimate.workOrderCreated) &&
        Objects.equals(this.billsCreated, detailedEstimate.billsCreated) &&
        Objects.equals(this.spillOverFlag, detailedEstimate.spillOverFlag) &&
        Objects.equals(this.grossAmountBilled, detailedEstimate.grossAmountBilled) &&
        Objects.equals(this.cancellationReason, detailedEstimate.cancellationReason) &&
        Objects.equals(this.cancellationRemarks, detailedEstimate.cancellationRemarks) &&
        Objects.equals(this.totalIncludingRE, detailedEstimate.totalIncludingRE) &&
        Objects.equals(this.abstractEstimateDetails, detailedEstimate.abstractEstimateDetails) &&
        Objects.equals(this.documentDetails, detailedEstimate.documentDetails) &&
        Objects.equals(this.assetValues, detailedEstimate.assetValues) &&
        Objects.equals(this.overheadValues, detailedEstimate.overheadValues) &&
        Objects.equals(this.estimatePhotographsList, detailedEstimate.estimatePhotographsList) &&
        Objects.equals(this.workFlowDetails, detailedEstimate.workFlowDetails) &&
        Objects.equals(this.stateId, detailedEstimate.stateId) &&
        Objects.equals(this.fund, detailedEstimate.fund) &&
        Objects.equals(this.function, detailedEstimate.function) &&
        Objects.equals(this.functionary, detailedEstimate.functionary) &&
        Objects.equals(this.scheme, detailedEstimate.scheme) &&
        Objects.equals(this.subScheme, detailedEstimate.subScheme) &&
        Objects.equals(this.budgetGroup, detailedEstimate.budgetGroup) &&
        Objects.equals(this.auditDetails, detailedEstimate.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, estimateNumber, estimateDate, nameOfWork, description, department, adminSanctionNumber, adminSanctionDate, adminSanctionBy, status, workValue, estimateValue, projectCode, parent, copiedFrom, approvedDate, approvedBy, copiedEstimate, beneficiary, modeOfAllotment, worksType, worksSubtype, natureOfWork, ward, location, latitude, longitude, workCategory, locality, councilResolutionNumber, councilResolutionDate, workOrderCreated, billsCreated, spillOverFlag, grossAmountBilled, cancellationReason, cancellationRemarks, totalIncludingRE, abstractEstimateDetails, documentDetails, assetValues, overheadValues, estimatePhotographsList, workFlowDetails, stateId, fund, function, functionary, scheme, subScheme, budgetGroup, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetailedEstimate {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    estimateNumber: ").append(toIndentedString(estimateNumber)).append("\n");
    sb.append("    estimateDate: ").append(toIndentedString(estimateDate)).append("\n");
    sb.append("    nameOfWork: ").append(toIndentedString(nameOfWork)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    department: ").append(toIndentedString(department)).append("\n");
    sb.append("    adminSanctionNumber: ").append(toIndentedString(adminSanctionNumber)).append("\n");
    sb.append("    adminSanctionDate: ").append(toIndentedString(adminSanctionDate)).append("\n");
    sb.append("    adminSanctionBy: ").append(toIndentedString(adminSanctionBy)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    workValue: ").append(toIndentedString(workValue)).append("\n");
    sb.append("    estimateValue: ").append(toIndentedString(estimateValue)).append("\n");
    sb.append("    projectCode: ").append(toIndentedString(projectCode)).append("\n");
    sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
    sb.append("    copiedFrom: ").append(toIndentedString(copiedFrom)).append("\n");
    sb.append("    approvedDate: ").append(toIndentedString(approvedDate)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    copiedEstimate: ").append(toIndentedString(copiedEstimate)).append("\n");
    sb.append("    beneficiary: ").append(toIndentedString(beneficiary)).append("\n");
    sb.append("    modeOfAllotment: ").append(toIndentedString(modeOfAllotment)).append("\n");
    sb.append("    worksType: ").append(toIndentedString(worksType)).append("\n");
    sb.append("    worksSubtype: ").append(toIndentedString(worksSubtype)).append("\n");
    sb.append("    natureOfWork: ").append(toIndentedString(natureOfWork)).append("\n");
    sb.append("    ward: ").append(toIndentedString(ward)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    workCategory: ").append(toIndentedString(workCategory)).append("\n");
    sb.append("    locality: ").append(toIndentedString(locality)).append("\n");
    sb.append("    councilResolutionNumber: ").append(toIndentedString(councilResolutionNumber)).append("\n");
    sb.append("    councilResolutionDate: ").append(toIndentedString(councilResolutionDate)).append("\n");
    sb.append("    workOrderCreated: ").append(toIndentedString(workOrderCreated)).append("\n");
    sb.append("    billsCreated: ").append(toIndentedString(billsCreated)).append("\n");
    sb.append("    spillOverFlag: ").append(toIndentedString(spillOverFlag)).append("\n");
    sb.append("    grossAmountBilled: ").append(toIndentedString(grossAmountBilled)).append("\n");
    sb.append("    cancellationReason: ").append(toIndentedString(cancellationReason)).append("\n");
    sb.append("    cancellationRemarks: ").append(toIndentedString(cancellationRemarks)).append("\n");
    sb.append("    totalIncludingRE: ").append(toIndentedString(totalIncludingRE)).append("\n");
    sb.append("    abstractEstimateDetails: ").append(toIndentedString(abstractEstimateDetails)).append("\n");
    sb.append("    documentDetails: ").append(toIndentedString(documentDetails)).append("\n");
    sb.append("    assetValues: ").append(toIndentedString(assetValues)).append("\n");
    sb.append("    overheadValues: ").append(toIndentedString(overheadValues)).append("\n");
    sb.append("    estimatePhotographsList: ").append(toIndentedString(estimatePhotographsList)).append("\n");
    sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    fund: ").append(toIndentedString(fund)).append("\n");
    sb.append("    function: ").append(toIndentedString(function)).append("\n");
    sb.append("    functionary: ").append(toIndentedString(functionary)).append("\n");
    sb.append("    scheme: ").append(toIndentedString(scheme)).append("\n");
    sb.append("    subScheme: ").append(toIndentedString(subScheme)).append("\n");
    sb.append("    budgetGroup: ").append(toIndentedString(budgetGroup)).append("\n");
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

