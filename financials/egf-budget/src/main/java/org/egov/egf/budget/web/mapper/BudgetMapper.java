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
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
import org.egov.egf.budget.web.contract.EstimationTypeContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;

public class BudgetMapper {

    public Budget toDomain(final BudgetContract contract) {

        final Budget budget = new Budget();

        budget.setId(contract.getId());
        budget.setName(contract.getName());
        budget.setFinancialYear(contract.getFinancialYear());
        budget.setEstimationType(
                contract.getEstimationType() != null ? EstimationType.valueOf(contract.getEstimationType().name()) : null);
        budget.setParent(
                Budget.builder().id(contract.getParent() != null ? contract.getParent().getId() : null).build());
        budget.setDescription(contract.getDescription());
        budget.setActive(contract.getActive());
        budget.setPrimaryBudget(contract.getPrimaryBudget());
        budget.setMaterializedPath(contract.getMaterializedPath());
        budget.setReferenceBudget(Budget.builder()
                .id(contract.getReferenceBudget() != null ? contract.getReferenceBudget().getId() : null).build());
        budget.setDocumentNumber(contract.getDocumentNumber());
        budget.setStatus(FinancialStatusContract.builder()
                .id(contract.getStatus() != null ? contract.getStatus().getId() : null).build());
        budget.setCreatedBy(contract.getCreatedBy());
        budget.setCreatedDate(contract.getCreatedDate());
        budget.setLastModifiedBy(contract.getLastModifiedBy());
        budget.setLastModifiedDate(contract.getLastModifiedDate());
        budget.setTenantId(contract.getTenantId());

        return budget;
    }

    public BudgetContract toContract(final Budget budget) {

        final BudgetContract contract = new BudgetContract();
        contract.setId(budget.getId());
        contract.setName(budget.getName());
        if (budget.getFinancialYear() != null)
            contract.setFinancialYear(budget.getFinancialYear());
        contract.setEstimationType(
                budget.getEstimationType() != null ? EstimationTypeContract.valueOf(budget.getEstimationType().name()) : null);
        if (budget.getParent() != null)
            contract.setParent(toContract(budget.getParent()));
        contract.setDescription(budget.getDescription());
        contract.setActive(budget.getActive());
        contract.setPrimaryBudget(budget.getPrimaryBudget());
        contract.setMaterializedPath(budget.getMaterializedPath());
        if (budget.getReferenceBudget() != null)
            contract.setReferenceBudget(toContract(budget.getReferenceBudget()));
        contract.setDocumentNumber(budget.getDocumentNumber());
        if (budget.getStatus() != null)
            contract.setStatus(FinancialStatusContract.builder().id(budget.getStatus().getId())
                    .code(budget.getStatus().getCode()).description(budget.getStatus().getDescription())
                    .moduleType(budget.getStatus().getModuleType()).build());
        contract.setCreatedBy(budget.getCreatedBy());
        contract.setCreatedDate(budget.getCreatedDate());
        contract.setLastModifiedBy(budget.getLastModifiedBy());
        contract.setLastModifiedDate(budget.getLastModifiedDate());
        contract.setTenantId(budget.getTenantId());

        return contract;
    }

    public BudgetSearch toSearchDomain(final BudgetSearchContract contract) {

        final BudgetSearch budgetSearch = new BudgetSearch();

        budgetSearch.setId(contract.getId());
        budgetSearch.setName(contract.getName());
        budgetSearch.setFinancialYear(contract.getFinancialYear());
        budgetSearch.setEstimationType(
                contract.getEstimationType() != null ? EstimationType.valueOf(contract.getEstimationType().name()) : null);
        budgetSearch.setParent(
                Budget.builder().id(contract.getParent() != null ? contract.getParent().getId() : null).build());
        budgetSearch.setDescription(contract.getDescription());
        budgetSearch.setActive(contract.getActive());
        budgetSearch.setPrimaryBudget(contract.getPrimaryBudget());
        budgetSearch.setMaterializedPath(contract.getMaterializedPath());
        budgetSearch.setReferenceBudget(Budget.builder()
                .id(contract.getReferenceBudget() != null ? contract.getReferenceBudget().getId() : null).build());
        budgetSearch.setDocumentNumber(contract.getDocumentNumber());
        budgetSearch.setStatus(FinancialStatusContract.builder()
                .id(contract.getStatus() != null ? contract.getStatus().getId() : null).build());
        budgetSearch.setCreatedBy(contract.getCreatedBy());
        budgetSearch.setCreatedDate(contract.getCreatedDate());
        budgetSearch.setLastModifiedBy(contract.getLastModifiedBy());
        budgetSearch.setLastModifiedDate(contract.getLastModifiedDate());
        budgetSearch.setTenantId(contract.getTenantId());
        budgetSearch.setPageSize(contract.getPageSize());
        budgetSearch.setOffset(contract.getOffset());

        return budgetSearch;
    }

    public BudgetSearchContract toSearchContract(final BudgetSearch budgetSearch) {

        final BudgetSearchContract contract = new BudgetSearchContract();

        contract.setId(budgetSearch.getId());
        contract.setName(budgetSearch.getName());
        contract.setFinancialYear(FinancialYearContract.builder()
                .id(budgetSearch.getFinancialYear() != null ? budgetSearch.getFinancialYear().getId() : null).build());
        contract.setEstimationType(budgetSearch.getEstimationType() != null
                ? EstimationTypeContract.valueOf(budgetSearch.getEstimationType().name()) : null);
        contract.setParent(BudgetContract.builder()
                .id(budgetSearch.getParent() != null ? budgetSearch.getParent().getId() : null).build());
        contract.setDescription(budgetSearch.getDescription());
        contract.setActive(budgetSearch.getActive());
        contract.setPrimaryBudget(budgetSearch.getPrimaryBudget());
        contract.setMaterializedPath(budgetSearch.getMaterializedPath());
        contract.setReferenceBudget(BudgetContract.builder()
                .id(budgetSearch.getReferenceBudget() != null ? budgetSearch.getReferenceBudget().getId() : null)
                .build());
        contract.setDocumentNumber(budgetSearch.getDocumentNumber());
        contract.setStatus(FinancialStatusContract.builder()
                .id(budgetSearch.getStatus() != null ? budgetSearch.getStatus().getId() : null).build());
        contract.setCreatedBy(budgetSearch.getCreatedBy());
        contract.setCreatedDate(budgetSearch.getCreatedDate());
        contract.setLastModifiedBy(budgetSearch.getLastModifiedBy());
        contract.setLastModifiedDate(budgetSearch.getLastModifiedDate());
        contract.setTenantId(budgetSearch.getTenantId());
        contract.setPageSize(budgetSearch.getPageSize());
        contract.setOffset(budgetSearch.getOffset());

        return contract;
    }

}
