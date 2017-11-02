package org.egov.works.estimate.web.contract;

import java.math.BigDecimal;

import org.egov.works.commons.domain.enums.Beneficiary;
import org.egov.works.commons.domain.enums.WorkCategory;
import org.egov.works.commons.domain.model.TypeOfWork;
import org.egov.works.estimate.web.model.BudgetGroup;
import org.egov.works.estimate.web.model.Department;
import org.egov.works.estimate.web.model.Function;
import org.egov.works.estimate.web.model.Functionary;
import org.egov.works.estimate.web.model.Fund;
import org.egov.works.estimate.web.model.Scheme;
import org.egov.works.estimate.web.model.SubScheme;
import org.egov.works.estimate.web.model.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

/**
 * An Object holds the basic data for a Detailed Estimate
 */
@ApiModel(description = "An Object holds the basic data for a Detailed Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T10:26:20.111Z")

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
    private String status = null;

    @JsonProperty("workValue")
    private BigDecimal workValue = null;

    @JsonProperty("estimateValue")
    private BigDecimal estimateValue = null;

    @JsonProperty("projectCode")
    private String projectCode = null;

    @JsonProperty("parent")
    private String parent = null;

    @JsonProperty("copiedFrom")
    private String copiedFrom = null;

    @JsonProperty("approvedDate")
    private Long approvedDate = null;

    @JsonProperty("approvedBy")
    private String approvedBy = null;

    @JsonProperty("copiedEstimate")
    private Boolean copiedEstimate = null;

    @JsonProperty("beneficiary")
    private String beneficiary = null;

    @JsonProperty("modeOfAllotment")
    private String modeOfAllotment = null;

    @JsonProperty("worksType")
    private String worksType = null;

    @JsonProperty("worksSubtype")
    private String worksSubtype = null;

    @JsonProperty("natureOfWork")
    private String natureOfWork = null;

    @JsonProperty("ward")
    private String ward = null;

    @JsonProperty("location")
    private String location = null;

    @JsonProperty("latitude")
    private Double latitude = null;

    @JsonProperty("longitude")
    private Double longitude = null;

    @JsonProperty("workCategory")
    private WorkCategory workCategory = null;

    @JsonProperty("locality")
    private String locality = null;

    @JsonProperty("councilResolutionNumber")
    private String councilResolutionNumber = null;

    @JsonProperty("councilResolutionDate")
    private Long councilResolutionDate = null;

    @JsonProperty("workOrderCreated")
    private Boolean workOrderCreated = null;

    @JsonProperty("billsCreated")
    private Boolean billsCreated = null;

    @JsonProperty("spillOverFlag")
    private Boolean spillOverFlag = null;

    @JsonProperty("grossAmountBilled")
    private BigDecimal grossAmountBilled = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    @JsonProperty("totalIncludingRE")
    private BigDecimal totalIncludingRE = null;

    @JsonProperty("abstractEstimateDetail")
    private String abstractEstimateDetail = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("fund")
    private String fund = null;

    @JsonProperty("function")
    private String function = null;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public Long getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Long estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getNameOfWork() {
		return nameOfWork;
	}

	public void setNameOfWork(String nameOfWork) {
		this.nameOfWork = nameOfWork;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getAdminSanctionNumber() {
		return adminSanctionNumber;
	}

	public void setAdminSanctionNumber(String adminSanctionNumber) {
		this.adminSanctionNumber = adminSanctionNumber;
	}

	public Long getAdminSanctionDate() {
		return adminSanctionDate;
	}

	public void setAdminSanctionDate(Long adminSanctionDate) {
		this.adminSanctionDate = adminSanctionDate;
	}

	public String getAdminSanctionBy() {
		return adminSanctionBy;
	}

	public void setAdminSanctionBy(String adminSanctionBy) {
		this.adminSanctionBy = adminSanctionBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getWorkValue() {
		return workValue;
	}

	public void setWorkValue(BigDecimal workValue) {
		this.workValue = workValue;
	}

	public BigDecimal getEstimateValue() {
		return estimateValue;
	}

	public void setEstimateValue(BigDecimal estimateValue) {
		this.estimateValue = estimateValue;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getCopiedFrom() {
		return copiedFrom;
	}

	public void setCopiedFrom(String copiedFrom) {
		this.copiedFrom = copiedFrom;
	}

	public Long getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Long approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Boolean getCopiedEstimate() {
		return copiedEstimate;
	}

	public void setCopiedEstimate(Boolean copiedEstimate) {
		this.copiedEstimate = copiedEstimate;
	}

	public String getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}

	public String getModeOfAllotment() {
		return modeOfAllotment;
	}

	public void setModeOfAllotment(String modeOfAllotment) {
		this.modeOfAllotment = modeOfAllotment;
	}

	public String getWorksType() {
		return worksType;
	}

	public void setWorksType(String worksType) {
		this.worksType = worksType;
	}

	public String getWorksSubtype() {
		return worksSubtype;
	}

	public void setWorksSubtype(String worksSubtype) {
		this.worksSubtype = worksSubtype;
	}

	public String getNatureOfWork() {
		return natureOfWork;
	}

	public void setNatureOfWork(String natureOfWork) {
		this.natureOfWork = natureOfWork;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public WorkCategory getWorkCategory() {
		return workCategory;
	}

	public void setWorkCategory(WorkCategory workCategory) {
		this.workCategory = workCategory;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getCouncilResolutionNumber() {
		return councilResolutionNumber;
	}

	public void setCouncilResolutionNumber(String councilResolutionNumber) {
		this.councilResolutionNumber = councilResolutionNumber;
	}

	public Long getCouncilResolutionDate() {
		return councilResolutionDate;
	}

	public void setCouncilResolutionDate(Long councilResolutionDate) {
		this.councilResolutionDate = councilResolutionDate;
	}

	public Boolean getWorkOrderCreated() {
		return workOrderCreated;
	}

	public void setWorkOrderCreated(Boolean workOrderCreated) {
		this.workOrderCreated = workOrderCreated;
	}

	public Boolean getBillsCreated() {
		return billsCreated;
	}

	public void setBillsCreated(Boolean billsCreated) {
		this.billsCreated = billsCreated;
	}

	public Boolean getSpillOverFlag() {
		return spillOverFlag;
	}

	public void setSpillOverFlag(Boolean spillOverFlag) {
		this.spillOverFlag = spillOverFlag;
	}

	public BigDecimal getGrossAmountBilled() {
		return grossAmountBilled;
	}

	public void setGrossAmountBilled(BigDecimal grossAmountBilled) {
		this.grossAmountBilled = grossAmountBilled;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getCancellationRemarks() {
		return cancellationRemarks;
	}

	public void setCancellationRemarks(String cancellationRemarks) {
		this.cancellationRemarks = cancellationRemarks;
	}

	public BigDecimal getTotalIncludingRE() {
		return totalIncludingRE;
	}

	public void setTotalIncludingRE(BigDecimal totalIncludingRE) {
		this.totalIncludingRE = totalIncludingRE;
	}

	public String getAbstractEstimateDetail() {
		return abstractEstimateDetail;
	}

	public void setAbstractEstimateDetail(String abstractEstimateDetail) {
		this.abstractEstimateDetail = abstractEstimateDetail;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getFund() {
		return fund;
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getFunctionary() {
		return functionary;
	}

	public void setFunctionary(String functionary) {
		this.functionary = functionary;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getSubScheme() {
		return subScheme;
	}

	public void setSubScheme(String subScheme) {
		this.subScheme = subScheme;
	}

	public String getBudgetGroup() {
		return budgetGroup;
	}

	public void setBudgetGroup(String budgetGroup) {
		this.budgetGroup = budgetGroup;
	}

	@JsonProperty("functionary")
    private String functionary = null;

    @JsonProperty("scheme")
    private String scheme = null;

    @JsonProperty("subScheme")
    private String subScheme = null;

    @JsonProperty("budgetGroup")
    private String budgetGroup = null;
    
    public org.egov.works.estimate.web.model.DetailedEstimate toDomain(final DetailedEstimate estimate) {

		org.egov.works.estimate.web.model.DetailedEstimate detailedEstimate = new org.egov.works.estimate.web.model.DetailedEstimate();
		detailedEstimate.setAdminSanctionDate(estimate.getAdminSanctionDate());
		final User user = new User();
		user.setUserName(estimate.getAdminSanctionBy());
		detailedEstimate.setAdminSanctionBy(user);
		detailedEstimate.setAdminSanctionNumber(estimate.getAdminSanctionNumber());
		user.setUserName(estimate.getApprovedBy());
		detailedEstimate.setApprovedBy(user);
		detailedEstimate.setApprovedDate(estimate.getApprovedDate());
		detailedEstimate.setBeneficiary(Beneficiary.valueOf(estimate.getBeneficiary()));
		detailedEstimate.setBillsCreated(estimate.getBillsCreated());
		BudgetGroup budgetGroup = new BudgetGroup();
		budgetGroup.setName(estimate.getBudgetGroup());
		detailedEstimate.setBudgetGroup(budgetGroup);
		detailedEstimate.setCancellationReason(estimate.getCancellationReason());
		detailedEstimate.setCancellationRemarks(estimate.getCancellationRemarks());
		detailedEstimate.setCopiedEstimate(estimate.getCopiedEstimate());
		detailedEstimate.setCouncilResolutionDate(estimate.getCouncilResolutionDate());
		detailedEstimate.setCouncilResolutionNumber(estimate.getCouncilResolutionNumber());
		Department department = new Department();
		department.setCode(estimate.getDepartment());
		detailedEstimate.setDepartment(department);
		detailedEstimate.setDescription(estimate.getDescription());
		detailedEstimate.setEstimateDate(estimate.getEstimateDate());
		detailedEstimate.setEstimateNumber(this.estimateNumber);
		detailedEstimate.setEstimateValue(this.estimateValue);
		detailedEstimate.setCopiedEstimate(this.copiedEstimate);
		detailedEstimate.setGrossAmountBilled(this.grossAmountBilled);
		detailedEstimate.setId(this.id);
		detailedEstimate.setTenantId(this.tenantId);
		detailedEstimate.setTotalIncludingRE(this.totalIncludingRE);
		TypeOfWork typeOfWork = new TypeOfWork();
		typeOfWork.setCode(this.worksType);
		detailedEstimate.setWorksType(typeOfWork);
		typeOfWork.setCode(this.worksSubtype);
		detailedEstimate.setWorksSubtype(typeOfWork);
		detailedEstimate.setWorkOrderCreated(this.workOrderCreated);
		detailedEstimate.setSpillOverFlag(this.spillOverFlag);
		
		Function function = new Function();
		function.setCode(this.function);
		Fund fund = new Fund();
		fund.setCode(this.fund);
		Functionary functionary = new Functionary();
		functionary.setCode(this.functionary);
		Scheme scheme = new Scheme();
		scheme.setCode(this.scheme);
		SubScheme subScheme = new SubScheme();
		subScheme.setCode(this.subScheme);
		
		detailedEstimate.setFunction(function);
		detailedEstimate.setFunctionary(functionary);
		detailedEstimate.setFund(fund);
		detailedEstimate.setScheme(scheme);
		detailedEstimate.setSubScheme(subScheme);
		
		return detailedEstimate;

	}

}
