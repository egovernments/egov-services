package org.egov.works.estimate.persistence.helper;

import org.egov.works.estimate.web.contract.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AbstractEstimateHelper {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("abstractEstimateNumber")
	private String abstractEstimateNumber = null;

	@JsonProperty("subject")
	private String subject = null;

	@JsonProperty("referenceType")
	private String referenceType = null;

	@JsonProperty("referenceNumber")
	private String referenceNumber = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("fund")
	private String fund = null;

	@JsonProperty("function")
	private String function = null;

	@JsonProperty("budgetHead")
	private String budgetHead = null;
	
	@JsonProperty("accountCode")
	private String accountCode = null;

	@JsonProperty("scheme")
	private String scheme = null;

	@JsonProperty("subScheme")
	private String subScheme = null;

	@JsonProperty("dateOfProposal")
	private Long dateOfProposal = null;

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

	@JsonProperty("beneficiary")
	private String beneficiary = null;

	@JsonProperty("modeOfAllotment")
	private String modeOfAllotment = null;

	@JsonProperty("typeOfWork")
	private String typeOfWork = null;

	@JsonProperty("subTypeOfWork")
	private String subTypeOfWork = null;

	@JsonProperty("natureOfWork")
	private String natureOfWork = null;

	@JsonProperty("ward")
	private String ward = null;

	@JsonProperty("financialSanctionNumber")
	private String financialSanctionNumber = null;

	@JsonProperty("financialSanctionDate")
	private Long financialSanctionDate = null;

	@JsonProperty("financialSanctionBy")
	private String financialSanctionBy = null;

	@JsonProperty("locality")
	private String locality = null;

	@JsonProperty("workCategory")
	private String workCategory = null;

	@JsonProperty("councilResolutionNumber")
	private String councilResolutionNumber = null;

	@JsonProperty("councilResolutionDate")
	private Long councilResolutionDate = null;

	@JsonProperty("spillOverFlag")
	private Boolean spillOverFlag = null;

	@JsonProperty("detailedEstimateCreated")
	private Boolean detailedEstimateCreated = null;

	@JsonProperty("workOrderCreated")
	private Boolean workOrderCreated = null;

	@JsonProperty("billsCreated")
	private Boolean billsCreated = null;

	@JsonProperty("cancellationReason")
	private String cancellationReason = null;

	@JsonProperty("cancellationRemarks")
	private String cancellationRemarks = null;

	@JsonProperty("stateId")
	private String stateId = null;

	@JsonProperty("implementationPeriod")
	private Integer implementationPeriod = null;

	@JsonProperty("fundAvailable")
	private Boolean fundAvailable = null;

	@JsonProperty("fundSanctioningAuthority")
	private String fundSanctioningAuthority = null;

	@JsonProperty("pmcRequired")
	private Boolean pmcRequired = null;

	@JsonProperty("pmcType")
	private String pmcType = null;

	@JsonProperty("pmcName")
	private String pmcName = null;
	
	@JsonProperty("workProposedAsPerDP")
	private Boolean workProposedAsPerDP = null;

	@JsonProperty("dpRemarks")
	private String dpRemarks = null;

	@JsonProperty("landAssetRequired")
	private Boolean landAssetRequired = null;

	@JsonProperty("noOfLands")
	private Integer noOfLands = null;

	@JsonProperty("otherAssetSpecificationRemarks")
	private String otherAssetSpecificationRemarks = null;

	@JsonProperty("createdBy")
	private String createdBy = null;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;

	public AbstractEstimate toDomain() {
		AbstractEstimate abstractEstimate = new AbstractEstimate();
		abstractEstimate.setId(this.id);
		abstractEstimate.setTenantId(this.tenantId);
		abstractEstimate.setAbstractEstimateNumber(this.abstractEstimateNumber);
		abstractEstimate.setAdminSanctionBy(new User());
		abstractEstimate.getAdminSanctionBy().setUserName(this.adminSanctionBy);
		abstractEstimate.setAdminSanctionDate(this.adminSanctionDate);
		abstractEstimate.setAdminSanctionNumber(this.adminSanctionNumber);
		abstractEstimate.setAuditDetails(new AuditDetails());
		abstractEstimate.getAuditDetails().setCreatedBy(this.createdBy);
		abstractEstimate.getAuditDetails().setCreatedTime(this.createdTime);
		abstractEstimate.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
		abstractEstimate.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
		abstractEstimate.setBeneficiary(Beneficiary.valueOf(this.beneficiary));
		abstractEstimate.setBillsCreated(this.billsCreated);
		abstractEstimate.setBudgetGroup(new BudgetGroup());
		abstractEstimate.getBudgetGroup().setName(this.budgetHead);
		abstractEstimate.setCancellationReason(this.cancellationReason);
		abstractEstimate.setCancellationRemarks(this.cancellationRemarks);
		abstractEstimate.setCouncilResolutionDate(this.councilResolutionDate);
		abstractEstimate.setCouncilResolutionNumber(this.councilResolutionNumber);
		abstractEstimate.setDateOfProposal(this.dateOfProposal);
		abstractEstimate.setDepartment(new Department());
		abstractEstimate.getDepartment().setCode(this.department);
		abstractEstimate.setDescription(this.description);
		abstractEstimate.setDetailedEstimateCreated(this.detailedEstimateCreated);
		abstractEstimate.setFunction(new Function());
		abstractEstimate.getFunction().setCode(this.function);
		abstractEstimate.setFund(new Fund());
		abstractEstimate.getFund().setCode(this.fund);
		abstractEstimate.setFundAvailable(this.fundAvailable);
		abstractEstimate.setFundSanctioningAuthority(this.fundSanctioningAuthority);
		abstractEstimate.setImplementationPeriod(this.implementationPeriod);
		abstractEstimate.setLocality(new Boundary());
		abstractEstimate.getLocality().setCode(this.locality);
		abstractEstimate.setModeOfAllotment(new ModeOfAllotment());
		abstractEstimate.getModeOfAllotment().setCode(this.modeOfAllotment);
		abstractEstimate.setNatureOfWork(new NatureOfWork());
		abstractEstimate.getNatureOfWork().setCode(this.natureOfWork);
		Contractor contractor = new Contractor();
		contractor.setCode(this.pmcName);
		abstractEstimate.setPmcName(contractor);
		abstractEstimate.setPmcRequired(this.pmcRequired);
		abstractEstimate.setPmcType(this.pmcType);
		abstractEstimate.setReferenceNumber(this.referenceNumber);
		abstractEstimate.setReferenceType(new ReferenceType());
		abstractEstimate.getReferenceType().setCode(this.referenceType);
		abstractEstimate.setScheme(new Scheme());
		abstractEstimate.getScheme().setCode(this.scheme);
		abstractEstimate.setSpillOverFlag(this.spillOverFlag);
		abstractEstimate.setStateId(this.stateId);
        WorksStatus worksStatus = new WorksStatus();
        worksStatus.setCode(this.status);
		abstractEstimate.setStatus(worksStatus);
		abstractEstimate.setSubject(this.subject);
		abstractEstimate.setSubScheme(new SubScheme());
		abstractEstimate.getSubScheme().setCode(this.subScheme);
		abstractEstimate.setSubTypeOfWork(new TypeOfWork());
		abstractEstimate.getSubTypeOfWork().setCode(this.subTypeOfWork);
		abstractEstimate.setTypeOfWork(new TypeOfWork());
		abstractEstimate.getTypeOfWork().setCode(this.typeOfWork);
		abstractEstimate.setWard(new Boundary());
		abstractEstimate.getWard().setCode(this.ward);
		abstractEstimate.setWorkCategory(WorkCategory.valueOf(this.workCategory));
		abstractEstimate.setWorkOrderCreated(this.workOrderCreated);
		abstractEstimate.setFinancialSanctionNumber(this.financialSanctionNumber);
		abstractEstimate.setFinancialSanctionDate(this.financialSanctionDate);
		abstractEstimate.setFinancialSanctionBy(new User());
		abstractEstimate.getFinancialSanctionBy().setUserName(this.financialSanctionBy);
		abstractEstimate.setWorkProposedAsPerDP(this.workProposedAsPerDP);
		abstractEstimate.setDpRemarks(this.dpRemarks);
		abstractEstimate.setLandAssetRequired(this.landAssetRequired);
		abstractEstimate.setNoOfLands(this.noOfLands);
		abstractEstimate.setAccountCode(this.accountCode);
		BudgetGroup budgetGroup = new BudgetGroup();
		budgetGroup.setName(this.budgetHead);
		abstractEstimate.setBudgetGroup(budgetGroup);
		abstractEstimate.setWorkFlowDetails(new WorkFlowDetails());
		return abstractEstimate;
	}
}
