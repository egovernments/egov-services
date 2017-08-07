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
package org.egov.egf.budget.web.mapper;

import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.web.contract.Boundary;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetDetailSearchContract;
import org.egov.egf.budget.web.contract.Department;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;

public class BudgetDetailMapper {

    public BudgetDetail toDomain(final BudgetDetailContract contract) {

        final BudgetDetail budgetDetail = new BudgetDetail();

        budgetDetail.setId(contract.getId());
        budgetDetail.setBudgetGroup(contract.getBudgetGroup());
        final Budget budget = Budget.builder().id(contract.getBudget() != null ? contract.getBudget().getId() : null).build();
        budget.setTenantId(contract.getBudget() != null ? contract.getBudget().getTenantId() : null);
        budgetDetail.setBudget(budget);
        budgetDetail.setOriginalAmount(contract.getOriginalAmount());
        budgetDetail.setApprovedAmount(contract.getApprovedAmount());
        budgetDetail.setBudgetAvailable(contract.getBudgetAvailable());
        budgetDetail.setAnticipatoryAmount(contract.getAnticipatoryAmount());
        budgetDetail.setUsingDepartment(contract.getUsingDepartment());
        budgetDetail.setExecutingDepartment(contract.getUsingDepartment());
        budgetDetail.setFunction(contract.getFunction());
        budgetDetail.setScheme(contract.getScheme());
        budgetDetail.setFund(contract.getFund());
        budgetDetail.setSubScheme(contract.getSubScheme());
        budgetDetail.setFunctionary(contract.getFunctionary());
        budgetDetail.setBoundary(contract.getBoundary());
        budgetDetail.setMaterializedPath(contract.getMaterializedPath());
        budgetDetail.setDocumentNumber(contract.getDocumentNumber());
        budgetDetail.setUniqueNo(contract.getUniqueNo());
        budgetDetail.setPlanningPercent(contract.getPlanningPercent());
        budgetDetail.setStatus(FinancialStatusContract.builder()
                .id(contract.getStatus() != null ? contract.getStatus().getId() : null).build());
        budgetDetail.setCreatedBy(contract.getCreatedBy());
        budgetDetail.setCreatedDate(contract.getCreatedDate());
        budgetDetail.setLastModifiedBy(contract.getLastModifiedBy());
        budgetDetail.setLastModifiedDate(contract.getLastModifiedDate());
        budgetDetail.setTenantId(contract.getTenantId());

        return budgetDetail;
    }

    public BudgetDetailContract toContract(final BudgetDetail budgetDetail) {

        final BudgetDetailContract contract = new BudgetDetailContract();

        contract.setId(budgetDetail.getId());
        contract.setBudgetGroup(budgetDetail.getBudgetGroup() != null ? budgetDetail.getBudgetGroup() : null);
        if (budgetDetail.getBudget() != null) {
            final BudgetMapper bMapper = new BudgetMapper();
            contract.setBudget(bMapper.toContract(budgetDetail.getBudget()));
        }
        contract.setOriginalAmount(budgetDetail.getOriginalAmount());
        contract.setApprovedAmount(budgetDetail.getApprovedAmount());
        contract.setBudgetAvailable(budgetDetail.getBudgetAvailable());
        contract.setAnticipatoryAmount(budgetDetail.getAnticipatoryAmount());
        contract.setUsingDepartment(budgetDetail.getUsingDepartment());
        contract.setExecutingDepartment(budgetDetail.getExecutingDepartment());
        contract.setFunction(budgetDetail.getFunction());
        contract.setScheme(budgetDetail.getScheme());
        contract.setFund(budgetDetail.getFund());
        contract.setSubScheme(budgetDetail.getSubScheme());
        contract.setFunctionary(budgetDetail.getFunctionary());
        contract.setBoundary(budgetDetail.getBoundary());
        contract.setMaterializedPath(budgetDetail.getMaterializedPath());
        contract.setDocumentNumber(budgetDetail.getDocumentNumber());
        contract.setUniqueNo(budgetDetail.getUniqueNo());
        contract.setPlanningPercent(budgetDetail.getPlanningPercent());
        contract.setStatus(FinancialStatusContract.builder()
                .id(budgetDetail.getStatus() != null ? budgetDetail.getStatus().getId() : null).build());
        contract.setCreatedBy(budgetDetail.getCreatedBy());
        contract.setCreatedDate(budgetDetail.getCreatedDate());
        contract.setLastModifiedBy(budgetDetail.getLastModifiedBy());
        contract.setLastModifiedDate(budgetDetail.getLastModifiedDate());
        contract.setTenantId(budgetDetail.getTenantId());

        return contract;
    }

    public BudgetDetailSearch toSearchDomain(final BudgetDetailSearchContract contract) {

        final BudgetDetailSearch budgetDetailSearch = new BudgetDetailSearch();

        budgetDetailSearch.setId(contract.getId());
        budgetDetailSearch.setBudgetGroup(BudgetGroupContract.builder()
                .id(contract.getBudgetGroup() != null ? contract.getBudgetGroup().getId() : null).build());
        budgetDetailSearch.setBudget(
                Budget.builder().id(contract.getBudget() != null ? contract.getBudget().getId() : null).build());
        budgetDetailSearch.setOriginalAmount(contract.getOriginalAmount());
        budgetDetailSearch.setApprovedAmount(contract.getApprovedAmount());
        budgetDetailSearch.setBudgetAvailable(contract.getBudgetAvailable());
        budgetDetailSearch.setAnticipatoryAmount(contract.getAnticipatoryAmount());
        budgetDetailSearch.setUsingDepartment(Department.builder()
                .id(contract.getUsingDepartment() != null ? contract.getUsingDepartment().getId() : null).build());
        budgetDetailSearch.setExecutingDepartment(Department.builder()
                .id(contract.getExecutingDepartment() != null ? contract.getExecutingDepartment().getId() : null)
                .build());
        budgetDetailSearch.setFunction(FunctionContract.builder()
                .id(contract.getFunction() != null ? contract.getFunction().getId() : null).build());
        budgetDetailSearch.setScheme(SchemeContract.builder()
                .id(contract.getScheme() != null ? contract.getScheme().getId() : null).build());
        budgetDetailSearch.setFund(
                FundContract.builder().id(contract.getFund() != null ? contract.getFund().getId() : null).build());
        budgetDetailSearch.setSubScheme(SubSchemeContract.builder()
                .id(contract.getSubScheme() != null ? contract.getSubScheme().getId() : null).build());
        budgetDetailSearch.setFunctionary(FunctionaryContract.builder()
                .id(contract.getFunctionary() != null ? contract.getFunctionary().getId() : null).build());
        budgetDetailSearch.setBoundary(
                Boundary.builder().id(contract.getBoundary() != null ? contract.getBoundary().getId() : null).build());
        budgetDetailSearch.setMaterializedPath(contract.getMaterializedPath());
        budgetDetailSearch.setDocumentNumber(contract.getDocumentNumber());
        budgetDetailSearch.setUniqueNo(contract.getUniqueNo());
        budgetDetailSearch.setPlanningPercent(contract.getPlanningPercent());
        budgetDetailSearch.setStatus(FinancialStatusContract.builder()
                .id(contract.getStatus() != null ? contract.getStatus().getId() : null).build());
        budgetDetailSearch.setCreatedBy(contract.getCreatedBy());
        budgetDetailSearch.setCreatedDate(contract.getCreatedDate());
        budgetDetailSearch.setLastModifiedBy(contract.getLastModifiedBy());
        budgetDetailSearch.setLastModifiedDate(contract.getLastModifiedDate());
        budgetDetailSearch.setTenantId(contract.getTenantId());
        budgetDetailSearch.setPageSize(contract.getPageSize());
        budgetDetailSearch.setOffset(contract.getOffset());

        return budgetDetailSearch;
    }

    public BudgetDetailSearchContract toSearchContract(final BudgetDetailSearch budgetDetailSearch) {

        final BudgetDetailSearchContract contract = new BudgetDetailSearchContract();

        contract.setId(budgetDetailSearch.getId());
        contract.setBudgetGroup(
                budgetDetailSearch.getBudgetGroup() != null ? budgetDetailSearch.getBudgetGroup() : null);
        contract.setBudget(BudgetContract.builder()
                .id(budgetDetailSearch.getBudget() != null ? budgetDetailSearch.getBudget().getId() : null).build());
        contract.setOriginalAmount(budgetDetailSearch.getOriginalAmount());
        contract.setApprovedAmount(budgetDetailSearch.getApprovedAmount());
        contract.setBudgetAvailable(budgetDetailSearch.getBudgetAvailable());
        contract.setAnticipatoryAmount(budgetDetailSearch.getAnticipatoryAmount());
        contract.setUsingDepartment(
                budgetDetailSearch.getUsingDepartment() != null ? budgetDetailSearch.getUsingDepartment() : null);
        contract.setExecutingDepartment(budgetDetailSearch.getExecutingDepartment() != null
                ? budgetDetailSearch.getExecutingDepartment() : null);
        contract.setFunction(budgetDetailSearch.getFunction() != null ? budgetDetailSearch.getFunction() : null);
        contract.setScheme(budgetDetailSearch.getScheme() != null ? budgetDetailSearch.getScheme() : null);
        contract.setFund(budgetDetailSearch.getFund() != null ? budgetDetailSearch.getFund() : null);
        contract.setSubScheme(budgetDetailSearch.getSubScheme() != null ? budgetDetailSearch.getSubScheme() : null);
        contract.setFunctionary(
                budgetDetailSearch.getFunctionary() != null ? budgetDetailSearch.getFunctionary() : null);
        contract.setBoundary(budgetDetailSearch.getBoundary() != null ? budgetDetailSearch.getBoundary() : null);
        contract.setMaterializedPath(budgetDetailSearch.getMaterializedPath());
        contract.setDocumentNumber(budgetDetailSearch.getDocumentNumber());
        contract.setUniqueNo(budgetDetailSearch.getUniqueNo());
        contract.setPlanningPercent(budgetDetailSearch.getPlanningPercent());
        contract.setStatus(FinancialStatusContract.builder()
                .id(budgetDetailSearch.getStatus() != null ? budgetDetailSearch.getStatus().getId() : null).build());
        contract.setCreatedBy(budgetDetailSearch.getCreatedBy());
        contract.setCreatedDate(budgetDetailSearch.getCreatedDate());
        contract.setLastModifiedBy(budgetDetailSearch.getLastModifiedBy());
        contract.setLastModifiedDate(budgetDetailSearch.getLastModifiedDate());
        contract.setTenantId(budgetDetailSearch.getTenantId());
        contract.setPageSize(budgetDetailSearch.getPageSize());
        contract.setOffset(budgetDetailSearch.getOffset());

        return contract;
    }

}
