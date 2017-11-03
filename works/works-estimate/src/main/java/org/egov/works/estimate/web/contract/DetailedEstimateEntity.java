package org.egov.works.estimate.web.contract;

import java.math.BigDecimal;

import org.egov.works.commons.domain.enums.Beneficiary;
import org.egov.works.commons.domain.enums.WorkCategory;
import org.egov.works.commons.domain.model.TypeOfWork;
import org.egov.works.estimate.persistence.entity.AbstractEstimateDetailsEntity;
import org.egov.works.estimate.web.model.AbstractEstimateDetails;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An Object holds the basic data for a Detailed Estimate
 */
@ApiModel(description = "An Object holds the basic data for a Detailed Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T10:26:20.111Z")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedEstimateEntity   {
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

	@JsonProperty("functionary")
    private String functionary = null;

    @JsonProperty("scheme")
    private String scheme = null;

    @JsonProperty("subScheme")
    private String subScheme = null;

    @JsonProperty("budgetGroup")
    private String budgetGroup = null;
    
    public org.egov.works.estimate.web.model.DetailedEstimate toDomain(final DetailedEstimateEntity estimate) {

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
