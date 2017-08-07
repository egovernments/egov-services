/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.budget.persistence.entity;

import java.math.BigDecimal;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.web.contract.Boundary;
import org.egov.egf.budget.web.contract.Department;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class BudgetDetailEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_budgetdetail";
    private String id;
    private String budgetId;
    private String budgetGroupId;
    private String usingDepartmentId;
    private String executingDepartmentId;
    private String fundId;
    private String functionId;
    private String schemeId;
    private String subSchemeId;
    private String functionaryId;
    private String boundaryId;
    private BigDecimal anticipatoryAmount;
    private BigDecimal originalAmount;
    private BigDecimal approvedAmount;
    private BigDecimal planningPercent;
    private BigDecimal budgetAvailable;
    private String statusId;
    private String documentNumber;
    private String uniqueNo;
    private String materializedPath;

    public BudgetDetail toDomain() {
        final BudgetDetail budgetDetail = new BudgetDetail();
        super.toDomain(budgetDetail);
        budgetDetail.setId(id);
        budgetDetail.setBudget(Budget.builder().id(budgetId).build());
        budgetDetail.setBudgetGroup(BudgetGroupContract.builder().id(budgetGroupId).build());
        budgetDetail.setUsingDepartment(Department.builder().id(usingDepartmentId).build());
        budgetDetail.setExecutingDepartment(Department.builder().id(executingDepartmentId).build());
        budgetDetail.setFund(FundContract.builder().id(fundId).build());
        budgetDetail.setFunction(FunctionContract.builder().id(functionId).build());
        budgetDetail.setScheme(SchemeContract.builder().id(schemeId).build());
        budgetDetail.setSubScheme(SubSchemeContract.builder().id(subSchemeId).build());
        budgetDetail.setFunctionary(FunctionaryContract.builder().id(functionaryId).build());
        budgetDetail.setBoundary(Boundary.builder().id(boundaryId).build());
        budgetDetail.setAnticipatoryAmount(anticipatoryAmount);
        budgetDetail.setOriginalAmount(originalAmount);
        budgetDetail.setApprovedAmount(approvedAmount);
        budgetDetail.setPlanningPercent(planningPercent);
        budgetDetail.setBudgetAvailable(budgetAvailable);
        budgetDetail.setStatus(FinancialStatusContract.builder().id(statusId).build());
        budgetDetail.setDocumentNumber(documentNumber);
        budgetDetail.setUniqueNo(uniqueNo);
        budgetDetail.setMaterializedPath(materializedPath);
        return budgetDetail;
    }

    public BudgetDetailEntity toEntity(final BudgetDetail budgetDetail) {
        super.toEntity(budgetDetail);
        id = budgetDetail.getId();
        budgetId = budgetDetail.getBudget() != null ? budgetDetail.getBudget().getId() : null;
        budgetGroupId = budgetDetail.getBudgetGroup() != null ? budgetDetail.getBudgetGroup().getId() : null;
        usingDepartmentId = budgetDetail.getUsingDepartment() != null ? budgetDetail.getUsingDepartment().getId()
                : null;
        executingDepartmentId = budgetDetail.getExecutingDepartment() != null
                ? budgetDetail.getExecutingDepartment().getId() : null;
        fundId = budgetDetail.getFund() != null ? budgetDetail.getFund().getId() : null;
        functionId = budgetDetail.getFunction() != null ? budgetDetail.getFunction().getId() : null;
        schemeId = budgetDetail.getScheme() != null ? budgetDetail.getScheme().getId() : null;
        subSchemeId = budgetDetail.getSubScheme() != null ? budgetDetail.getSubScheme().getId() : null;
        functionaryId = budgetDetail.getFunctionary() != null ? budgetDetail.getFunctionary().getId() : null;
        boundaryId = budgetDetail.getBoundary() != null ? budgetDetail.getBoundary().getId() : null;
        anticipatoryAmount = budgetDetail.getAnticipatoryAmount();
        originalAmount = budgetDetail.getOriginalAmount();
        approvedAmount = budgetDetail.getApprovedAmount();
        planningPercent = budgetDetail.getPlanningPercent();
        budgetAvailable = budgetDetail.getBudgetAvailable();
        statusId = budgetDetail.getStatus() != null ? budgetDetail.getStatus().getId() : null;
        documentNumber = budgetDetail.getDocumentNumber();
        uniqueNo = budgetDetail.getUniqueNo();
        materializedPath = budgetDetail.getMaterializedPath();
        return this;
    }

}
