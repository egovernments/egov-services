package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AN Object which holds the basic data for AbstractEstimate
 */
@ApiModel(description = "AN Object which holds the basic data for AbstractEstimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class AbstractEstimate   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("dateOfProposal")
  private Long dateOfProposal = null;

  @JsonProperty("abstractEstimateNumber")
  private String abstractEstimateNumber = null;

  @JsonProperty("subject")
  private String subject = null;

  @JsonProperty("referenceType")
  private ReferenceType referenceType = null;

  @JsonProperty("referenceNumber")
  private String referenceNumber = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("department")
  private Department department = null;

  @JsonProperty("status")
  private AbstractEstimateStatus status = null;

  @JsonProperty("beneficiary")
  private Beneficiary beneficiary = null;

  @JsonProperty("modeOfAllotment")
  private ModeOfAllotment modeOfAllotment = null;

  @JsonProperty("typeOfWork")
  private TypeOfWork typeOfWork = null;

  @JsonProperty("subTypeOfWork")
  private TypeOfWork subTypeOfWork = null;

  @JsonProperty("natureOfWork")
  private NatureOfWork natureOfWork = null;

  @JsonProperty("ward")
  private Boundary ward = null;

  @JsonProperty("locality")
  private Boundary locality = null;

  @JsonProperty("workCategory")
  private WorkCategory workCategory = null;

  @JsonProperty("councilResolutionNumber")
  private String councilResolutionNumber = null;

  @JsonProperty("councilResolutionDate")
  private Long councilResolutionDate = null;

  @JsonProperty("spillOverFlag")
  private Boolean spillOverFlag = false;

  @JsonProperty("detailedEstimateCreated")
  private Boolean detailedEstimateCreated = false;

  @JsonProperty("workOrderCreated")
  private Boolean workOrderCreated = false;

  @JsonProperty("billsCreated")
  private Boolean billsCreated = false;

  @JsonProperty("cancellationReason")
  private String cancellationReason = null;

  @JsonProperty("cancellationRemarks")
  private String cancellationRemarks = null;

  @JsonProperty("adminSanctionNumber")
  private String adminSanctionNumber = null;

  @JsonProperty("adminSanctionDate")
  private Long adminSanctionDate = null;

  @JsonProperty("adminSanctionBy")
  private User adminSanctionBy = null;

  @JsonProperty("financialSanctionNumber")
  private String financialSanctionNumber = null;

  @JsonProperty("financialSanctionDate")
  private Long financialSanctionDate = null;

  @JsonProperty("financialSanctionBy")
  private User financialSanctionBy = null;

  @JsonProperty("workFlowDetails")
  private WorkFlowDetails workFlowDetails = null;

  @JsonProperty("stateId")
  private String stateId = null;

  @JsonProperty("implementationPeriod")
  private Integer implementationPeriod = null;

  @JsonProperty("fund")
  private Fund fund = null;

  @JsonProperty("function")
  private Function function = null;

  @JsonProperty("budgetGroup")
  private BudgetGroup budgetGroup = null;

  @JsonProperty("accountCode")
  private String accountCode = null;

  @JsonProperty("scheme")
  private Scheme scheme = null;

  @JsonProperty("subScheme")
  private SubScheme subScheme = null;

  @JsonProperty("fundAvailable")
  private Boolean fundAvailable = false;

  @JsonProperty("fundSanctioningAuthority")
  private String fundSanctioningAuthority = null;

  @JsonProperty("pmcRequired")
  private Boolean pmcRequired = false;

  @JsonProperty("pmcType")
  private String pmcType = null;

  @JsonProperty("pmcName")
  private String pmcName = null;

  @JsonProperty("workProposedAsPerDP")
  private Boolean workProposedAsPerDP = false;

  @JsonProperty("dpRemarks")
  private String dpRemarks = null;

  @JsonProperty("landAssetRequired")
  private Boolean landAssetRequired = false;

  @JsonProperty("noOfLands")
  private Integer noOfLands = null;

  @JsonProperty("otherAssetSpecificationRemarks")
  private String otherAssetSpecificationRemarks = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("abstractEstimateDetails")
  private List<AbstractEstimateDetails> abstractEstimateDetails = new ArrayList<AbstractEstimateDetails>();

  @JsonProperty("sanctionDetails")
  private List<AbstractEstimateSanctionDetail> sanctionDetails = null;

  @JsonProperty("assetDetails")
  private List<AbstractEstimateAssetDetail> assetDetails = null;

  @JsonProperty("documentDetails")
  private List<DocumentDetail> documentDetails = null;

  public AbstractEstimate id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Abstract Estimate
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Abstract Estimate")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AbstractEstimate tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Abstract Estimate
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Abstract Estimate")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public AbstractEstimate dateOfProposal(Long dateOfProposal) {
    this.dateOfProposal = dateOfProposal;
    return this;
  }

   /**
   * Epoch time of the Date of Proposal. Default the current date for new estimates and do not allow to modify. This field is user entered for spillover works. Allowed to edit during rejected status or drafts for Spillover Estimates. Future date is not allowed to enter.
   * @return dateOfProposal
  **/
  @ApiModelProperty(required = true, value = "Epoch time of the Date of Proposal. Default the current date for new estimates and do not allow to modify. This field is user entered for spillover works. Allowed to edit during rejected status or drafts for Spillover Estimates. Future date is not allowed to enter.")
  @NotNull


  public Long getDateOfProposal() {
    return dateOfProposal;
  }

  public void setDateOfProposal(Long dateOfProposal) {
    this.dateOfProposal = dateOfProposal;
  }

  public AbstractEstimate abstractEstimateNumber(String abstractEstimateNumber) {
    this.abstractEstimateNumber = abstractEstimateNumber;
    return this;
  }

   /**
   * Unique number for the Abstract Estimate. If the work is spillover then the Abstract Estimate number is user entered. Otherwise it is auto generated. This field is allowed to edit during rejected status or drafts for Spillover Estimates.
   * @return abstractEstimateNumber
  **/
  @ApiModelProperty(value = "Unique number for the Abstract Estimate. If the work is spillover then the Abstract Estimate number is user entered. Otherwise it is auto generated. This field is allowed to edit during rejected status or drafts for Spillover Estimates.")

 @Pattern(regexp="[a-zA-Z0-9-/]+") @Size(max=50)
  public String getAbstractEstimateNumber() {
    return abstractEstimateNumber;
  }

  public void setAbstractEstimateNumber(String abstractEstimateNumber) {
    this.abstractEstimateNumber = abstractEstimateNumber;
  }

  public AbstractEstimate subject(String subject) {
    this.subject = subject;
    return this;
  }

   /**
   * Subject of Abstract Estimate
   * @return subject
  **/
  @ApiModelProperty(required = true, value = "Subject of Abstract Estimate")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=256)
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public AbstractEstimate referenceType(ReferenceType referenceType) {
    this.referenceType = referenceType;
    return this;
  }

   /**
   * Reference Type of the Abstract Estimate
   * @return referenceType
  **/
  @ApiModelProperty(required = true, value = "Reference Type of the Abstract Estimate")
  @NotNull

//  @Valid

  public ReferenceType getReferenceType() {
    return referenceType;
  }

  public void setReferenceType(ReferenceType referenceType) {
    this.referenceType = referenceType;
  }

  public AbstractEstimate referenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
    return this;
  }

   /**
   * Letter Reference Number of the Abstract Estimate
   * @return referenceNumber
  **/
  @ApiModelProperty(required = true, value = "Letter Reference Number of the Abstract Estimate")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-/]+") @Size(min=1,max=100)
  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public AbstractEstimate description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description for the Abstract Estimate
   * @return description
  **/
  @ApiModelProperty(required = true, value = "description for the Abstract Estimate")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AbstractEstimate department(Department department) {
    this.department = department;
    return this;
  }

   /**
   * Department for which Abstract Estimate belongs to
   * @return department
  **/
  @ApiModelProperty(required = true, value = "Department for which Abstract Estimate belongs to")
  @NotNull

//  @Valid

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public AbstractEstimate status(AbstractEstimateStatus status) {
    this.status = status;
    return this;
  }

   /**
   * Status of the Abstract Estimate
   * @return status
  **/
  @ApiModelProperty(value = "Status of the Abstract Estimate")

//  @Valid

  public AbstractEstimateStatus getStatus() {
    return status;
  }

  public void setStatus(AbstractEstimateStatus status) {
    this.status = status;
  }

  public AbstractEstimate beneficiary(Beneficiary beneficiary) {
    this.beneficiary = beneficiary;
    return this;
  }

   /**
   * The Beneficiary of this work
   * @return beneficiary
  **/
  @ApiModelProperty(required = true, value = "The Beneficiary of this work")
  @NotNull

//  @Valid

  public Beneficiary getBeneficiary() {
    return beneficiary;
  }

  public void setBeneficiary(Beneficiary beneficiary) {
    this.beneficiary = beneficiary;
  }

  public AbstractEstimate modeOfAllotment(ModeOfAllotment modeOfAllotment) {
    this.modeOfAllotment = modeOfAllotment;
    return this;
  }

   /**
   * The Recommended Mode of Allotment of the work
   * @return modeOfAllotment
  **/
  @ApiModelProperty(required = true, value = "The Recommended Mode of Allotment of the work")
  @NotNull

//  @Valid

  public ModeOfAllotment getModeOfAllotment() {
    return modeOfAllotment;
  }

  public void setModeOfAllotment(ModeOfAllotment modeOfAllotment) {
    this.modeOfAllotment = modeOfAllotment;
  }

  public AbstractEstimate typeOfWork(TypeOfWork typeOfWork) {
    this.typeOfWork = typeOfWork;
    return this;
  }

   /**
   * The Type of work for which this Abstract Estimate belongs to
   * @return typeOfWork
  **/
  @ApiModelProperty(required = true, value = "The Type of work for which this Abstract Estimate belongs to")
  @NotNull

//  @Valid

  public TypeOfWork getTypeOfWork() {
    return typeOfWork;
  }

  public void setTypeOfWork(TypeOfWork typeOfWork) {
    this.typeOfWork = typeOfWork;
  }

  public AbstractEstimate subTypeOfWork(TypeOfWork subTypeOfWork) {
    this.subTypeOfWork = subTypeOfWork;
    return this;
  }

   /**
   * The Sub Type of work for which this Abstract Estimate belongs to
   * @return subTypeOfWork
  **/
  @ApiModelProperty(required = true, value = "The Sub Type of work for which this Abstract Estimate belongs to")
  @NotNull

//  @Valid

  public TypeOfWork getSubTypeOfWork() {
    return subTypeOfWork;
  }

  public void setSubTypeOfWork(TypeOfWork subTypeOfWork) {
    this.subTypeOfWork = subTypeOfWork;
  }

  public AbstractEstimate natureOfWork(NatureOfWork natureOfWork) {
    this.natureOfWork = natureOfWork;
    return this;
  }

   /**
   * The Nature of work for which this Abstract Estimate belongs to
   * @return natureOfWork
  **/
  @ApiModelProperty(required = true, value = "The Nature of work for which this Abstract Estimate belongs to")
  @NotNull

//  @Valid

  public NatureOfWork getNatureOfWork() {
    return natureOfWork;
  }

  public void setNatureOfWork(NatureOfWork natureOfWork) {
    this.natureOfWork = natureOfWork;
  }

  public AbstractEstimate ward(Boundary ward) {
    this.ward = ward;
    return this;
  }

   /**
   * The Admin Ward for which this Abstract Estimate belongs to
   * @return ward
  **/
  @ApiModelProperty(required = true, value = "The Admin Ward for which this Abstract Estimate belongs to")
  @NotNull

//  @Valid

  public Boundary getWard() {
    return ward;
  }

  public void setWard(Boundary ward) {
    this.ward = ward;
  }

  public AbstractEstimate locality(Boundary locality) {
    this.locality = locality;
    return this;
  }

   /**
   * The Locality in which the Abstract Estimate belongs to
   * @return locality
  **/
  @ApiModelProperty(required = true, value = "The Locality in which the Abstract Estimate belongs to")
  @NotNull

//  @Valid

  public Boundary getLocality() {
    return locality;
  }

  public void setLocality(Boundary locality) {
    this.locality = locality;
  }

  public AbstractEstimate workCategory(WorkCategory workCategory) {
    this.workCategory = workCategory;
    return this;
  }

   /**
   * The Work Category of the Abstract Estimate
   * @return workCategory
  **/
  @ApiModelProperty(required = true, value = "The Work Category of the Abstract Estimate")
  @NotNull

//  @Valid

  public WorkCategory getWorkCategory() {
    return workCategory;
  }

  public void setWorkCategory(WorkCategory workCategory) {
    this.workCategory = workCategory;
  }

  public AbstractEstimate councilResolutionNumber(String councilResolutionNumber) {
    this.councilResolutionNumber = councilResolutionNumber;
    return this;
  }

   /**
   * Council resolution number of the Abstract Estimate
   * @return councilResolutionNumber
  **/
  @ApiModelProperty(value = "Council resolution number of the Abstract Estimate")

 @Pattern(regexp="[a-zA-Z0-9-/]+")
  public String getCouncilResolutionNumber() {
    return councilResolutionNumber;
  }

  public void setCouncilResolutionNumber(String councilResolutionNumber) {
    this.councilResolutionNumber = councilResolutionNumber;
  }

  public AbstractEstimate councilResolutionDate(Long councilResolutionDate) {
    this.councilResolutionDate = councilResolutionDate;
    return this;
  }

   /**
   * Epoch time of the council resolution date
   * @return councilResolutionDate
  **/
  @ApiModelProperty(value = "Epoch time of the council resolution date")


  public Long getCouncilResolutionDate() {
    return councilResolutionDate;
  }

  public void setCouncilResolutionDate(Long councilResolutionDate) {
    this.councilResolutionDate = councilResolutionDate;
  }

  public AbstractEstimate spillOverFlag(Boolean spillOverFlag) {
    this.spillOverFlag = spillOverFlag;
    return this;
  }

   /**
   * Boolean value if spill over or not. Required only if Abstract Estimate is Spill Over.
   * @return spillOverFlag
  **/
  @ApiModelProperty(value = "Boolean value if spill over or not. Required only if Abstract Estimate is Spill Over.")


  public Boolean getSpillOverFlag() {
    return spillOverFlag;
  }

  public void setSpillOverFlag(Boolean spillOverFlag) {
    this.spillOverFlag = spillOverFlag;
  }

  public AbstractEstimate detailedEstimateCreated(Boolean detailedEstimateCreated) {
    this.detailedEstimateCreated = detailedEstimateCreated;
    return this;
  }

   /**
   * Boolean value if the detailed estimate created or not. Required only if Abstract Estimate is Spill Over.
   * @return detailedEstimateCreated
  **/
  @ApiModelProperty(value = "Boolean value if the detailed estimate created or not. Required only if Abstract Estimate is Spill Over.")


  public Boolean getDetailedEstimateCreated() {
    return detailedEstimateCreated;
  }

  public void setDetailedEstimateCreated(Boolean detailedEstimateCreated) {
    this.detailedEstimateCreated = detailedEstimateCreated;
  }

  public AbstractEstimate workOrderCreated(Boolean workOrderCreated) {
    this.workOrderCreated = workOrderCreated;
    return this;
  }

   /**
   * Boolean value if work order created or not, Required only if Abstract Estimate is Spill Over.
   * @return workOrderCreated
  **/
  @ApiModelProperty(value = "Boolean value if work order created or not, Required only if Abstract Estimate is Spill Over.")


  public Boolean getWorkOrderCreated() {
    return workOrderCreated;
  }

  public void setWorkOrderCreated(Boolean workOrderCreated) {
    this.workOrderCreated = workOrderCreated;
  }

  public AbstractEstimate billsCreated(Boolean billsCreated) {
    this.billsCreated = billsCreated;
    return this;
  }

   /**
   * Boolean value if bills created or not. Required only if Abstract Estimate is Spill Over. The grossAmountBilled field needs to be shown in Works detail tab if  the billsCreated flag is true for Abstract Estimate.
   * @return billsCreated
  **/
  @ApiModelProperty(value = "Boolean value if bills created or not. Required only if Abstract Estimate is Spill Over. The grossAmountBilled field needs to be shown in Works detail tab if  the billsCreated flag is true for Abstract Estimate.")


  public Boolean getBillsCreated() {
    return billsCreated;
  }

  public void setBillsCreated(Boolean billsCreated) {
    this.billsCreated = billsCreated;
  }

  public AbstractEstimate cancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
    return this;
  }

   /**
   * Reason for cancellation of the Abstract Estimate, Required only while cancelling Abstract Estimate
   * @return cancellationReason
  **/
  @ApiModelProperty(value = "Reason for cancellation of the Abstract Estimate, Required only while cancelling Abstract Estimate")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=100)
  public String getCancellationReason() {
    return cancellationReason;
  }

  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  public AbstractEstimate cancellationRemarks(String cancellationRemarks) {
    this.cancellationRemarks = cancellationRemarks;
    return this;
  }

   /**
   * Remarks for cancellation of the Abstract Estimate, Required only while cancelling Abstract Estimate
   * @return cancellationRemarks
  **/
  @ApiModelProperty(value = "Remarks for cancellation of the Abstract Estimate, Required only while cancelling Abstract Estimate")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=512)
  public String getCancellationRemarks() {
    return cancellationRemarks;
  }

  public void setCancellationRemarks(String cancellationRemarks) {
    this.cancellationRemarks = cancellationRemarks;
  }

  public AbstractEstimate adminSanctionNumber(String adminSanctionNumber) {
    this.adminSanctionNumber = adminSanctionNumber;
    return this;
  }

   /**
   * Unique number of admin sanction for the Abstract Estimate
   * @return adminSanctionNumber
  **/
  @ApiModelProperty(value = "Unique number of admin sanction for the Abstract Estimate")

 @Pattern(regexp="[a-zA-Z0-9-/]+") @Size(min=1,max=50)
  public String getAdminSanctionNumber() {
    return adminSanctionNumber;
  }

  public void setAdminSanctionNumber(String adminSanctionNumber) {
    this.adminSanctionNumber = adminSanctionNumber;
  }

  public AbstractEstimate adminSanctionDate(Long adminSanctionDate) {
    this.adminSanctionDate = adminSanctionDate;
    return this;
  }

   /**
   * Epoch time of when the admin santion done. Future date is not allowed to enter in this field. Admin sanction date should be on or after the Date of Proposal.
   * @return adminSanctionDate
  **/
  @ApiModelProperty(value = "Epoch time of when the admin santion done. Future date is not allowed to enter in this field. Admin sanction date should be on or after the Date of Proposal.")


  public Long getAdminSanctionDate() {
    return adminSanctionDate;
  }

  public void setAdminSanctionDate(Long adminSanctionDate) {
    this.adminSanctionDate = adminSanctionDate;
  }

  public AbstractEstimate adminSanctionBy(User adminSanctionBy) {
    this.adminSanctionBy = adminSanctionBy;
    return this;
  }

   /**
   * User who admin sanctioned
   * @return adminSanctionBy
  **/
  @ApiModelProperty(value = "User who admin sanctioned")

//  @Valid

  public User getAdminSanctionBy() {
    return adminSanctionBy;
  }

  public void setAdminSanctionBy(User adminSanctionBy) {
    this.adminSanctionBy = adminSanctionBy;
  }

  public AbstractEstimate financialSanctionNumber(String financialSanctionNumber) {
    this.financialSanctionNumber = financialSanctionNumber;
    return this;
  }

   /**
   * Financial sanction number of the Abstract Estimate
   * @return financialSanctionNumber
  **/
  @ApiModelProperty(value = "Financial sanction number of the Abstract Estimate")

 @Pattern(regexp="[a-zA-Z0-9-/]+") @Size(max=50)
  public String getFinancialSanctionNumber() {
    return financialSanctionNumber;
  }

  public void setFinancialSanctionNumber(String financialSanctionNumber) {
    this.financialSanctionNumber = financialSanctionNumber;
  }

  public AbstractEstimate financialSanctionDate(Long financialSanctionDate) {
    this.financialSanctionDate = financialSanctionDate;
    return this;
  }

   /**
   * Epoch time of when Financial sanctioned. Future date is not allowed to enter in this field.
   * @return financialSanctionDate
  **/
  @ApiModelProperty(value = "Epoch time of when Financial sanctioned. Future date is not allowed to enter in this field.")


  public Long getFinancialSanctionDate() {
    return financialSanctionDate;
  }

  public void setFinancialSanctionDate(Long financialSanctionDate) {
    this.financialSanctionDate = financialSanctionDate;
  }

  public AbstractEstimate financialSanctionBy(User financialSanctionBy) {
    this.financialSanctionBy = financialSanctionBy;
    return this;
  }

   /**
   * The user who Financial sanctioned the Abstract Estimate
   * @return financialSanctionBy
  **/
  @ApiModelProperty(value = "The user who Financial sanctioned the Abstract Estimate")

//  @Valid

  public User getFinancialSanctionBy() {
    return financialSanctionBy;
  }

  public void setFinancialSanctionBy(User financialSanctionBy) {
    this.financialSanctionBy = financialSanctionBy;
  }

  public AbstractEstimate workFlowDetails(WorkFlowDetails workFlowDetails) {
    this.workFlowDetails = workFlowDetails;
    return this;
  }

   /**
   * Get workFlowDetails
   * @return workFlowDetails
  **/
  @ApiModelProperty(value = "")

//  @Valid

  public WorkFlowDetails getWorkFlowDetails() {
    return workFlowDetails;
  }

  public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
    this.workFlowDetails = workFlowDetails;
  }

  public AbstractEstimate stateId(String stateId) {
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

  public AbstractEstimate implementationPeriod(Integer implementationPeriod) {
    this.implementationPeriod = implementationPeriod;
    return this;
  }

   /**
   * Approximate period of implementation of the work
   * @return implementationPeriod
  **/
  @ApiModelProperty(value = "Approximate period of implementation of the work")


  public Integer getImplementationPeriod() {
    return implementationPeriod;
  }

  public void setImplementationPeriod(Integer implementationPeriod) {
    this.implementationPeriod = implementationPeriod;
  }

  public AbstractEstimate fund(Fund fund) {
    this.fund = fund;
    return this;
  }

   /**
   * Fund of the Abstract Estimate. This data comes from financials.
   * @return fund
  **/
  @ApiModelProperty(value = "Fund of the Abstract Estimate. This data comes from financials.")

//  @Valid

  public Fund getFund() {
    return fund;
  }

  public void setFund(Fund fund) {
    this.fund = fund;
  }

  public AbstractEstimate function(Function function) {
    this.function = function;
    return this;
  }

   /**
   * Function of the Abstract Estimate. This data comes from financials.
   * @return function
  **/
  @ApiModelProperty(value = "Function of the Abstract Estimate. This data comes from financials.")

//  @Valid

  public Function getFunction() {
    return function;
  }

  public void setFunction(Function function) {
    this.function = function;
  }

  public AbstractEstimate budgetGroup(BudgetGroup budgetGroup) {
    this.budgetGroup = budgetGroup;
    return this;
  }

   /**
   * Budget head of the Abstract Estimate. This data comes from financials.
   * @return budgetGroup
  **/
  @ApiModelProperty(value = "Budget head of the Abstract Estimate. This data comes from financials.")

//  @Valid

  public BudgetGroup getBudgetGroup() {
    return budgetGroup;
  }

  public void setBudgetGroup(BudgetGroup budgetGroup) {
    this.budgetGroup = budgetGroup;
  }

  public AbstractEstimate accountCode(String accountCode) {
    this.accountCode = accountCode;
    return this;
  }

   /**
   * Account code of the Abstract Estimate. This data comes from financials. This field is required only if Financial Integration required flag is turned on and it should be mandatory.
   * @return accountCode
  **/
  @ApiModelProperty(value = "Account code of the Abstract Estimate. This data comes from financials. This field is required only if Financial Integration required flag is turned on and it should be mandatory.")


  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public AbstractEstimate scheme(Scheme scheme) {
    this.scheme = scheme;
    return this;
  }

   /**
   * Scheme of the Abstract Estimate. This data comes from financials.
   * @return scheme
  **/
  @ApiModelProperty(value = "Scheme of the Abstract Estimate. This data comes from financials.")

//  @Valid

  public Scheme getScheme() {
    return scheme;
  }

  public void setScheme(Scheme scheme) {
    this.scheme = scheme;
  }

  public AbstractEstimate subScheme(SubScheme subScheme) {
    this.subScheme = subScheme;
    return this;
  }

   /**
   * Sub scheme of the Abstract Estimate. This data comes from financials.
   * @return subScheme
  **/
  @ApiModelProperty(value = "Sub scheme of the Abstract Estimate. This data comes from financials.")

//  @Valid

  public SubScheme getSubScheme() {
    return subScheme;
  }

  public void setSubScheme(SubScheme subScheme) {
    this.subScheme = subScheme;
  }

  public AbstractEstimate fundAvailable(Boolean fundAvailable) {
    this.fundAvailable = fundAvailable;
    return this;
  }

   /**
   * Boolean value to capture whether the fund available to execute this work. True if Fund available. False if fund not available.
   * @return fundAvailable
  **/
  @ApiModelProperty(value = "Boolean value to capture whether the fund available to execute this work. True if Fund available. False if fund not available.")


  public Boolean getFundAvailable() {
    return fundAvailable;
  }

  public void setFundAvailable(Boolean fundAvailable) {
    this.fundAvailable = fundAvailable;
  }

  public AbstractEstimate fundSanctioningAuthority(String fundSanctioningAuthority) {
    this.fundSanctioningAuthority = fundSanctioningAuthority;
    return this;
  }

   /**
   * The sanctioning Authority for fund to carry out the work
   * @return fundSanctioningAuthority
  **/
  @ApiModelProperty(value = "The sanctioning Authority for fund to carry out the work")

 @Pattern(regexp="[a-zA-Z0-9\\s\\.,]+") @Size(max=100)
  public String getFundSanctioningAuthority() {
    return fundSanctioningAuthority;
  }

  public void setFundSanctioningAuthority(String fundSanctioningAuthority) {
    this.fundSanctioningAuthority = fundSanctioningAuthority;
  }

  public AbstractEstimate pmcRequired(Boolean pmcRequired) {
    this.pmcRequired = pmcRequired;
    return this;
  }

   /**
   * Boolean vlaue to capture whether the PMC need to appoint or not. True if PMC needs to be appointed. Otherwise the value will be false. The valid values are 'Yes' and 'No'.
   * @return pmcRequired
  **/
  @ApiModelProperty(value = "Boolean vlaue to capture whether the PMC need to appoint or not. True if PMC needs to be appointed. Otherwise the value will be false. The valid values are 'Yes' and 'No'.")


  public Boolean getPmcRequired() {
    return pmcRequired;
  }

  public void setPmcRequired(Boolean pmcRequired) {
    this.pmcRequired = pmcRequired;
  }

  public AbstractEstimate pmcType(String pmcType) {
    this.pmcType = pmcType;
    return this;
  }

   /**
   * If PMC required is yes then to capture PMC is from Panel or New Appointment.
   * @return pmcType
  **/
  @ApiModelProperty(value = "If PMC required is yes then to capture PMC is from Panel or New Appointment.")

 @Size(max=100)
  public String getPmcType() {
    return pmcType;
  }

  public void setPmcType(String pmcType) {
    this.pmcType = pmcType;
  }

  public AbstractEstimate pmcName(String pmcName) {
    this.pmcName = pmcName;
    return this;
  }

   /**
   * Name of the PMC for PMC type Panel. The PMC(Project Management Consultant) data will fetch from Contractor master based on the boolean flag pmc=true. This field is not required if pmcType is 'New Appointment'.
   * @return pmcName
  **/
  @ApiModelProperty(value = "Name of the PMC for PMC type Panel. The PMC(Project Management Consultant) data will fetch from Contractor master based on the boolean flag pmc=true. This field is not required if pmcType is 'New Appointment'.")

 @Size(max=100)
  public String getPmcName() {
    return pmcName;
  }

  public void setPmcName(String pmcName) {
    this.pmcName = pmcName;
  }

  public AbstractEstimate workProposedAsPerDP(Boolean workProposedAsPerDP) {
    this.workProposedAsPerDP = workProposedAsPerDP;
    return this;
  }

   /**
   * Work proposed as per DP. The valid values are 'Yes' and 'No'.
   * @return workProposedAsPerDP
  **/
  @ApiModelProperty(required = true, value = "Work proposed as per DP. The valid values are 'Yes' and 'No'.")
  @NotNull


  public Boolean getWorkProposedAsPerDP() {
    return workProposedAsPerDP;
  }

  public void setWorkProposedAsPerDP(Boolean workProposedAsPerDP) {
    this.workProposedAsPerDP = workProposedAsPerDP;
  }

  public AbstractEstimate dpRemarks(String dpRemarks) {
    this.dpRemarks = dpRemarks;
    return this;
  }

   /**
   * DP Remarks
   * @return dpRemarks
  **/
  @ApiModelProperty(required = true, value = "DP Remarks")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=512)
  public String getDpRemarks() {
    return dpRemarks;
  }

  public void setDpRemarks(String dpRemarks) {
    this.dpRemarks = dpRemarks;
  }

  public AbstractEstimate landAssetRequired(Boolean landAssetRequired) {
    this.landAssetRequired = landAssetRequired;
    return this;
  }

   /**
   * Boolean value to capture whether the Land Asset required. True if Land Asset required. False if the Land Asset value is not required. The values should be shown in UI are 'Yes' and 'No'. This field needs to be shown if Nature of work is 'New'.
   * @return landAssetRequired
  **/
  @ApiModelProperty(value = "Boolean value to capture whether the Land Asset required. True if Land Asset required. False if the Land Asset value is not required. The values should be shown in UI are 'Yes' and 'No'. This field needs to be shown if Nature of work is 'New'.")


  public Boolean getLandAssetRequired() {
    return landAssetRequired;
  }

  public void setLandAssetRequired(Boolean landAssetRequired) {
    this.landAssetRequired = landAssetRequired;
  }

  public AbstractEstimate noOfLands(Integer noOfLands) {
    this.noOfLands = noOfLands;
    return this;
  }

   /**
   * No of Lands. This field needs to be shown if Nature of work is 'New'. If landAssetRequired=Yes then this field is mandatory.
   * @return noOfLands
  **/
  @ApiModelProperty(value = "No of Lands. This field needs to be shown if Nature of work is 'New'. If landAssetRequired=Yes then this field is mandatory.")


  public Integer getNoOfLands() {
    return noOfLands;
  }

  public void setNoOfLands(Integer noOfLands) {
    this.noOfLands = noOfLands;
  }

  public AbstractEstimate otherAssetSpecificationRemarks(String otherAssetSpecificationRemarks) {
    this.otherAssetSpecificationRemarks = otherAssetSpecificationRemarks;
    return this;
  }

   /**
   * Other Specifications Remarks. This field needs to be shown if Nature of work is 'New'.
   * @return otherAssetSpecificationRemarks
  **/
  @ApiModelProperty(value = "Other Specifications Remarks. This field needs to be shown if Nature of work is 'New'.")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=1024)
  public String getOtherAssetSpecificationRemarks() {
    return otherAssetSpecificationRemarks;
  }

  public void setOtherAssetSpecificationRemarks(String otherAssetSpecificationRemarks) {
    this.otherAssetSpecificationRemarks = otherAssetSpecificationRemarks;
  }

  public AbstractEstimate deleted(Boolean deleted) {
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

  public AbstractEstimate auditDetails(AuditDetails auditDetails) {
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

  public AbstractEstimate abstractEstimateDetails(List<AbstractEstimateDetails> abstractEstimateDetails) {
    this.abstractEstimateDetails = abstractEstimateDetails;
    return this;
  }

  public AbstractEstimate addAbstractEstimateDetailsItem(AbstractEstimateDetails abstractEstimateDetailsItem) {
    this.abstractEstimateDetails.add(abstractEstimateDetailsItem);
    return this;
  }

   /**
   * Array of Abstract Estimate Details
   * @return abstractEstimateDetails
  **/
  @ApiModelProperty(required = true, value = "Array of Abstract Estimate Details")
  @NotNull

  @Valid
 @Size(min=1)
  public List<AbstractEstimateDetails> getAbstractEstimateDetails() {
    return abstractEstimateDetails;
  }

  public void setAbstractEstimateDetails(List<AbstractEstimateDetails> abstractEstimateDetails) {
    this.abstractEstimateDetails = abstractEstimateDetails;
  }

  public AbstractEstimate sanctionDetails(List<AbstractEstimateSanctionDetail> sanctionDetails) {
    this.sanctionDetails = sanctionDetails;
    return this;
  }

  public AbstractEstimate addSanctionDetailsItem(AbstractEstimateSanctionDetail sanctionDetailsItem) {
    if (this.sanctionDetails == null) {
      this.sanctionDetails = new ArrayList<AbstractEstimateSanctionDetail>();
    }
    this.sanctionDetails.add(sanctionDetailsItem);
    return this;
  }

   /**
   * Array of Abstract Estimate Sanction Details
   * @return sanctionDetails
  **/
  @ApiModelProperty(value = "Array of Abstract Estimate Sanction Details")

//  @Valid

  public List<AbstractEstimateSanctionDetail> getSanctionDetails() {
    return sanctionDetails;
  }

  public void setSanctionDetails(List<AbstractEstimateSanctionDetail> sanctionDetails) {
    this.sanctionDetails = sanctionDetails;
  }

  public AbstractEstimate assetDetails(List<AbstractEstimateAssetDetail> assetDetails) {
    this.assetDetails = assetDetails;
    return this;
  }

  public AbstractEstimate addAssetDetailsItem(AbstractEstimateAssetDetail assetDetailsItem) {
    if (this.assetDetails == null) {
      this.assetDetails = new ArrayList<AbstractEstimateAssetDetail>();
    }
    this.assetDetails.add(assetDetailsItem);
    return this;
  }

   /**
   * Array of Asset/Land details
   * @return assetDetails
  **/
  @ApiModelProperty(value = "Array of Asset/Land details")

//  @Valid

  public List<AbstractEstimateAssetDetail> getAssetDetails() {
    return assetDetails;
  }

  public void setAssetDetails(List<AbstractEstimateAssetDetail> assetDetails) {
    this.assetDetails = assetDetails;
  }

  public AbstractEstimate documentDetails(List<DocumentDetail> documentDetails) {
    this.documentDetails = documentDetails;
    return this;
  }

  public AbstractEstimate addDocumentDetailsItem(DocumentDetail documentDetailsItem) {
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

//  @Valid

  public List<DocumentDetail> getDocumentDetails() {
    return documentDetails;
  }

  public void setDocumentDetails(List<DocumentDetail> documentDetails) {
    this.documentDetails = documentDetails;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractEstimate abstractEstimate = (AbstractEstimate) o;
    return Objects.equals(this.id, abstractEstimate.id) &&
        Objects.equals(this.tenantId, abstractEstimate.tenantId) &&
        Objects.equals(this.dateOfProposal, abstractEstimate.dateOfProposal) &&
        Objects.equals(this.abstractEstimateNumber, abstractEstimate.abstractEstimateNumber) &&
        Objects.equals(this.subject, abstractEstimate.subject) &&
        Objects.equals(this.referenceType, abstractEstimate.referenceType) &&
        Objects.equals(this.referenceNumber, abstractEstimate.referenceNumber) &&
        Objects.equals(this.description, abstractEstimate.description) &&
        Objects.equals(this.department, abstractEstimate.department) &&
        Objects.equals(this.status, abstractEstimate.status) &&
        Objects.equals(this.beneficiary, abstractEstimate.beneficiary) &&
        Objects.equals(this.modeOfAllotment, abstractEstimate.modeOfAllotment) &&
        Objects.equals(this.typeOfWork, abstractEstimate.typeOfWork) &&
        Objects.equals(this.subTypeOfWork, abstractEstimate.subTypeOfWork) &&
        Objects.equals(this.natureOfWork, abstractEstimate.natureOfWork) &&
        Objects.equals(this.ward, abstractEstimate.ward) &&
        Objects.equals(this.locality, abstractEstimate.locality) &&
        Objects.equals(this.workCategory, abstractEstimate.workCategory) &&
        Objects.equals(this.councilResolutionNumber, abstractEstimate.councilResolutionNumber) &&
        Objects.equals(this.councilResolutionDate, abstractEstimate.councilResolutionDate) &&
        Objects.equals(this.spillOverFlag, abstractEstimate.spillOverFlag) &&
        Objects.equals(this.detailedEstimateCreated, abstractEstimate.detailedEstimateCreated) &&
        Objects.equals(this.workOrderCreated, abstractEstimate.workOrderCreated) &&
        Objects.equals(this.billsCreated, abstractEstimate.billsCreated) &&
        Objects.equals(this.cancellationReason, abstractEstimate.cancellationReason) &&
        Objects.equals(this.cancellationRemarks, abstractEstimate.cancellationRemarks) &&
        Objects.equals(this.adminSanctionNumber, abstractEstimate.adminSanctionNumber) &&
        Objects.equals(this.adminSanctionDate, abstractEstimate.adminSanctionDate) &&
        Objects.equals(this.adminSanctionBy, abstractEstimate.adminSanctionBy) &&
        Objects.equals(this.financialSanctionNumber, abstractEstimate.financialSanctionNumber) &&
        Objects.equals(this.financialSanctionDate, abstractEstimate.financialSanctionDate) &&
        Objects.equals(this.financialSanctionBy, abstractEstimate.financialSanctionBy) &&
        Objects.equals(this.workFlowDetails, abstractEstimate.workFlowDetails) &&
        Objects.equals(this.stateId, abstractEstimate.stateId) &&
        Objects.equals(this.implementationPeriod, abstractEstimate.implementationPeriod) &&
        Objects.equals(this.fund, abstractEstimate.fund) &&
        Objects.equals(this.function, abstractEstimate.function) &&
        Objects.equals(this.budgetGroup, abstractEstimate.budgetGroup) &&
        Objects.equals(this.accountCode, abstractEstimate.accountCode) &&
        Objects.equals(this.scheme, abstractEstimate.scheme) &&
        Objects.equals(this.subScheme, abstractEstimate.subScheme) &&
        Objects.equals(this.fundAvailable, abstractEstimate.fundAvailable) &&
        Objects.equals(this.fundSanctioningAuthority, abstractEstimate.fundSanctioningAuthority) &&
        Objects.equals(this.pmcRequired, abstractEstimate.pmcRequired) &&
        Objects.equals(this.pmcType, abstractEstimate.pmcType) &&
        Objects.equals(this.pmcName, abstractEstimate.pmcName) &&
        Objects.equals(this.workProposedAsPerDP, abstractEstimate.workProposedAsPerDP) &&
        Objects.equals(this.dpRemarks, abstractEstimate.dpRemarks) &&
        Objects.equals(this.landAssetRequired, abstractEstimate.landAssetRequired) &&
        Objects.equals(this.noOfLands, abstractEstimate.noOfLands) &&
        Objects.equals(this.otherAssetSpecificationRemarks, abstractEstimate.otherAssetSpecificationRemarks) &&
        Objects.equals(this.deleted, abstractEstimate.deleted) &&
        Objects.equals(this.auditDetails, abstractEstimate.auditDetails) &&
        Objects.equals(this.abstractEstimateDetails, abstractEstimate.abstractEstimateDetails) &&
        Objects.equals(this.sanctionDetails, abstractEstimate.sanctionDetails) &&
        Objects.equals(this.assetDetails, abstractEstimate.assetDetails) &&
        Objects.equals(this.documentDetails, abstractEstimate.documentDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, dateOfProposal, abstractEstimateNumber, subject, referenceType, referenceNumber, description, department, status, beneficiary, modeOfAllotment, typeOfWork, subTypeOfWork, natureOfWork, ward, locality, workCategory, councilResolutionNumber, councilResolutionDate, spillOverFlag, detailedEstimateCreated, workOrderCreated, billsCreated, cancellationReason, cancellationRemarks, adminSanctionNumber, adminSanctionDate, adminSanctionBy, financialSanctionNumber, financialSanctionDate, financialSanctionBy, workFlowDetails, stateId, implementationPeriod, fund, function, budgetGroup, accountCode, scheme, subScheme, fundAvailable, fundSanctioningAuthority, pmcRequired, pmcType, pmcName, workProposedAsPerDP, dpRemarks, landAssetRequired, noOfLands, otherAssetSpecificationRemarks, deleted, auditDetails, abstractEstimateDetails, sanctionDetails, assetDetails, documentDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbstractEstimate {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    dateOfProposal: ").append(toIndentedString(dateOfProposal)).append("\n");
    sb.append("    abstractEstimateNumber: ").append(toIndentedString(abstractEstimateNumber)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    referenceType: ").append(toIndentedString(referenceType)).append("\n");
    sb.append("    referenceNumber: ").append(toIndentedString(referenceNumber)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    department: ").append(toIndentedString(department)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    beneficiary: ").append(toIndentedString(beneficiary)).append("\n");
    sb.append("    modeOfAllotment: ").append(toIndentedString(modeOfAllotment)).append("\n");
    sb.append("    typeOfWork: ").append(toIndentedString(typeOfWork)).append("\n");
    sb.append("    subTypeOfWork: ").append(toIndentedString(subTypeOfWork)).append("\n");
    sb.append("    natureOfWork: ").append(toIndentedString(natureOfWork)).append("\n");
    sb.append("    ward: ").append(toIndentedString(ward)).append("\n");
    sb.append("    locality: ").append(toIndentedString(locality)).append("\n");
    sb.append("    workCategory: ").append(toIndentedString(workCategory)).append("\n");
    sb.append("    councilResolutionNumber: ").append(toIndentedString(councilResolutionNumber)).append("\n");
    sb.append("    councilResolutionDate: ").append(toIndentedString(councilResolutionDate)).append("\n");
    sb.append("    spillOverFlag: ").append(toIndentedString(spillOverFlag)).append("\n");
    sb.append("    detailedEstimateCreated: ").append(toIndentedString(detailedEstimateCreated)).append("\n");
    sb.append("    workOrderCreated: ").append(toIndentedString(workOrderCreated)).append("\n");
    sb.append("    billsCreated: ").append(toIndentedString(billsCreated)).append("\n");
    sb.append("    cancellationReason: ").append(toIndentedString(cancellationReason)).append("\n");
    sb.append("    cancellationRemarks: ").append(toIndentedString(cancellationRemarks)).append("\n");
    sb.append("    adminSanctionNumber: ").append(toIndentedString(adminSanctionNumber)).append("\n");
    sb.append("    adminSanctionDate: ").append(toIndentedString(adminSanctionDate)).append("\n");
    sb.append("    adminSanctionBy: ").append(toIndentedString(adminSanctionBy)).append("\n");
    sb.append("    financialSanctionNumber: ").append(toIndentedString(financialSanctionNumber)).append("\n");
    sb.append("    financialSanctionDate: ").append(toIndentedString(financialSanctionDate)).append("\n");
    sb.append("    financialSanctionBy: ").append(toIndentedString(financialSanctionBy)).append("\n");
    sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    implementationPeriod: ").append(toIndentedString(implementationPeriod)).append("\n");
    sb.append("    fund: ").append(toIndentedString(fund)).append("\n");
    sb.append("    function: ").append(toIndentedString(function)).append("\n");
    sb.append("    budgetGroup: ").append(toIndentedString(budgetGroup)).append("\n");
    sb.append("    accountCode: ").append(toIndentedString(accountCode)).append("\n");
    sb.append("    scheme: ").append(toIndentedString(scheme)).append("\n");
    sb.append("    subScheme: ").append(toIndentedString(subScheme)).append("\n");
    sb.append("    fundAvailable: ").append(toIndentedString(fundAvailable)).append("\n");
    sb.append("    fundSanctioningAuthority: ").append(toIndentedString(fundSanctioningAuthority)).append("\n");
    sb.append("    pmcRequired: ").append(toIndentedString(pmcRequired)).append("\n");
    sb.append("    pmcType: ").append(toIndentedString(pmcType)).append("\n");
    sb.append("    pmcName: ").append(toIndentedString(pmcName)).append("\n");
    sb.append("    workProposedAsPerDP: ").append(toIndentedString(workProposedAsPerDP)).append("\n");
    sb.append("    dpRemarks: ").append(toIndentedString(dpRemarks)).append("\n");
    sb.append("    landAssetRequired: ").append(toIndentedString(landAssetRequired)).append("\n");
    sb.append("    noOfLands: ").append(toIndentedString(noOfLands)).append("\n");
    sb.append("    otherAssetSpecificationRemarks: ").append(toIndentedString(otherAssetSpecificationRemarks)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("    abstractEstimateDetails: ").append(toIndentedString(abstractEstimateDetails)).append("\n");
    sb.append("    sanctionDetails: ").append(toIndentedString(sanctionDetails)).append("\n");
    sb.append("    assetDetails: ").append(toIndentedString(assetDetails)).append("\n");
    sb.append("    documentDetails: ").append(toIndentedString(documentDetails)).append("\n");
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

