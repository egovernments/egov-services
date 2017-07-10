/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.budget.web.contract;

import java.math.BigDecimal;

import org.egov.common.master.web.contract.BoundaryContract;
import org.egov.common.master.web.contract.DepartmentContract;
import org.egov.common.web.contract.AuditableContract;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.EgfStatus;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "budgetGroup", "budget", "originalAmount", "approvedAmount", "budgetAvailable",
		"anticipatoryAmount", "usingDepartment", "executingDepartment", "function", "scheme", "fund", "subScheme",
		"functionary", "boundary", "materializedPath", "documentNumber", "uniqueNo", "planningPercent", "status" })
public class BudgetDetailContract extends AuditableContract {

	/*
	 * id of the budgetDeatil representing the unique value of each record
	 * getting saved.
	 */
	private String id;

	/*
	 * budgetGroup is reference to the budget group which defines the set of COA
	 * or a COA at major, minor or detailed level with its account type and
	 * budgeting type.
	 */
	private BudgetGroupContract budgetGroup;

	/*
	 * budget is node reference given for budget in the budget tree structure
	 * defined.
	 */
	private BudgetContract budget;

	/*
	 * originalAmount is the budget amount for the given combination of budget
	 * detail
	 */
	private BigDecimal originalAmount = new BigDecimal("0.0");

	/*
	 * approvedAmount final approved budget (post workflow) amount which is
	 * considered for budget checking.
	 */
	private BigDecimal approvedAmount = new BigDecimal("0.0");

	/* budgetAvailable is the running balance of the budget line item */
	private BigDecimal budgetAvailable = new BigDecimal("0.0");

	/*
	 * anticipatoryAmount is the anticipated amount while processing the
	 * re-appropriations.
	 */
	private BigDecimal anticipatoryAmount = new BigDecimal("0.0");

	/*
	 * usingDepartment is the department for which the budget line item is
	 * actually budgeted for.
	 */
	private DepartmentContract usingDepartment;

	/*
	 * executingDepartment is the department which uses the budget to execute
	 * the work which is defined in the budget of using department.
	 */
	private DepartmentContract executingDepartment;

	/*
	 * function is the MIS or financial transaction attribute which contributes
	 * the budget details definition.
	 */
	private FunctionContract function;

	/*
	 * scheme is the MIS or financial transaction attribute which contributes
	 * the budget details definition. Scheme can be optional
	 */
	private SchemeContract scheme;

	/*
	 * fund is the MIS or financial transaction attribute which contributes the
	 * budget details definition
	 */
	private FundContract fund;

	/*
	 * subScheme is the MIS or financial transaction attribute which contributes
	 * the budget details definition. sub Scheme can be optional
	 */
	private SubSchemeContract subScheme;

	/*
	 * functionary is the MIS or financial transaction attribute which
	 * contributes the budget details definition
	 */
	private FunctionContract functionary;

	/*
	 * boundary is also the MIS attribute which is used incase the budget need
	 * to be defined and idetified by boundaries of the organization.
	 */
	private BoundaryContract boundary;

	/**
	 * materializedPath is unique data by hierarchy level.This data is created
	 * internally
	 */
	private String materializedPath;

	/*
	 * budgetReAppropriations is the reference to the re appropriations made for
	 * the given budget line item
	 */
	// private Set<BudgetReAppropriation> budgetReAppropriations = new
	// HashSet<BudgetReAppropriation>(0);

	/*
	 * documentNumber is the reference number to identify the attachments made
	 * to the budget definition.
	 */
	private Long documentNumber;

	/*
	 * unique number is unique ref number to identify the budget line item. ie
	 * the combination of MIS attributes used for defining budget and budget
	 * group. Ex - Fund, Function, dept and Group. This data is internal to the
	 * system.
	 */
	private String uniqueNo;

	/*
	 * planningPercent is the percentage which gives the inflated budget used in
	 * certain business modules to have the budget check based on the inflated
	 * budget values. Ex: 150% in planning percentage for a budget line item is
	 * the 50% extra budget on approved budget used to do the budget checking in
	 * works module.
	 */
	private BigDecimal planningPercent;

	/*
	 * status gives the current status of the budget line item. (detailed level)
	 */
	private EgfStatusContract status;

	public BudgetDetail toDomain() {
		BudgetDetail budgetDetail = new BudgetDetail();
		budgetDetail.setId(this.id);
		budgetDetail.setBudgetGroupId(budgetGroup);
		budgetDetail.setBudgetId(Budget.builder().id(budget != null ? budget.getId() : null).build());
		budgetDetail.setOriginalAmount(this.originalAmount);
		budgetDetail.setApprovedAmount(this.approvedAmount);
		budgetDetail.setBudgetAvailable(this.budgetAvailable);
		budgetDetail.setAnticipatoryAmount(this.anticipatoryAmount);
		budgetDetail.setUsingDepartmentId(usingDepartment);
		budgetDetail.setExecutingDepartmentId(usingDepartment);
		budgetDetail.setFunctionId(function);
		budgetDetail.setSchemeId(scheme);
		budgetDetail.setFundId(fund);
		budgetDetail.setSubSchemeId(subScheme);
		budgetDetail.setFunctionaryId(functionary);
		budgetDetail.setBoundaryId(boundary);
		budgetDetail.setMaterializedPath(this.materializedPath);
		budgetDetail.setDocumentNumber(this.documentNumber);
		budgetDetail.setUniqueNo(this.uniqueNo);
		budgetDetail.setPlanningPercent(this.planningPercent);
		budgetDetail.setStatusId(EgfStatus.builder().id(status != null ? status.getId() : null).build());
		budgetDetail.setCreatedBy(this.createdBy);
		budgetDetail.setCreatedDate(this.createdDate);
		budgetDetail.setLastModifiedBy(this.lastModifiedBy);
		budgetDetail.setLastModifiedDate(this.lastModifiedDate);
		budgetDetail.setTenantId(this.tenantId);
		return budgetDetail;
	}

	public void toContract(BudgetDetail budgetDetail) {
		this.id = budgetDetail.getId();
		this.budgetGroup = budgetDetail.getBudgetGroupId() != null ? budgetDetail.getBudgetGroupId() : null;
		if (budgetDetail.getBudgetId() != null) {
			BudgetContract bContract = new BudgetContract();
			bContract.toContract(budgetDetail.getBudgetId());
			this.budget = bContract;
		}
		this.originalAmount = budgetDetail.getOriginalAmount();
		this.approvedAmount = budgetDetail.getApprovedAmount();
		this.budgetAvailable = budgetDetail.getBudgetAvailable();
		this.anticipatoryAmount = budgetDetail.getAnticipatoryAmount();
		this.usingDepartment = budgetDetail.getUsingDepartmentId() != null ? budgetDetail.getUsingDepartmentId() : null;
		this.executingDepartment = budgetDetail.getExecutingDepartmentId() != null
				? budgetDetail.getExecutingDepartmentId() : null;
		this.function = budgetDetail.getFunctionId() != null ? budgetDetail.getFunctionId() : null;
		this.scheme = budgetDetail.getSchemeId() != null ? budgetDetail.getSchemeId() : null;
		this.fund = budgetDetail.getFundId() != null ? budgetDetail.getFundId() : null;
		this.subScheme = budgetDetail.getSubSchemeId() != null ? budgetDetail.getSubSchemeId() : null;
		this.functionary = budgetDetail.getFunctionaryId() != null ? budgetDetail.getFunctionaryId() : null;
		this.boundary = budgetDetail.getBoundaryId() != null ? budgetDetail.getBoundaryId() : null;
		this.materializedPath = budgetDetail.getMaterializedPath();
		this.documentNumber = budgetDetail.getDocumentNumber();
		this.uniqueNo = budgetDetail.getUniqueNo();
		this.planningPercent = budgetDetail.getPlanningPercent();
		this.status = EgfStatusContract.builder()
				.id(budgetDetail.getStatusId() != null ? budgetDetail.getStatusId().getId() : null).build();
		this.setCreatedBy(budgetDetail.getCreatedBy());
		this.setCreatedDate(budgetDetail.getCreatedDate());
		this.setLastModifiedBy(budgetDetail.getLastModifiedBy());
		this.setLastModifiedDate(budgetDetail.getLastModifiedDate());
		this.setTenantId(budgetDetail.getTenantId());
	}

}