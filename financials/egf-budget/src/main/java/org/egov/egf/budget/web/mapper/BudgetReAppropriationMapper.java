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

import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;

public class BudgetReAppropriationMapper {

    public BudgetReAppropriation toDomain(final BudgetReAppropriationContract contract) {

        final BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation();
        final BudgetDetailMapper bdMapper = new BudgetDetailMapper();

        budgetReAppropriation.setId(contract.getId());
        budgetReAppropriation.setBudgetDetail(bdMapper.toDomain(contract.getBudgetDetail()));
        budgetReAppropriation.setAdditionAmount(contract.getAdditionAmount());
        budgetReAppropriation.setDeductionAmount(contract.getDeductionAmount());
        budgetReAppropriation.setOriginalAdditionAmount(contract.getOriginalAdditionAmount());
        budgetReAppropriation.setOriginalDeductionAmount(contract.getOriginalDeductionAmount());
        budgetReAppropriation.setAnticipatoryAmount(contract.getAnticipatoryAmount());
        budgetReAppropriation.setStatus(FinancialStatusContract.builder()
                .id(contract.getStatus() != null ? contract.getStatus().getId() : null).build());
        budgetReAppropriation.setAsOnDate(contract.getAsOnDate());
        budgetReAppropriation.setCreatedBy(contract.getCreatedBy());
        budgetReAppropriation.setCreatedDate(contract.getCreatedDate());
        budgetReAppropriation.setLastModifiedBy(contract.getLastModifiedBy());
        budgetReAppropriation.setLastModifiedDate(contract.getLastModifiedDate());
        budgetReAppropriation.setTenantId(contract.getTenantId());

        return budgetReAppropriation;
    }

    public BudgetReAppropriationContract toContract(final BudgetReAppropriation budgetReAppropriation) {

        final BudgetReAppropriationContract contract = new BudgetReAppropriationContract();

        contract.setId(budgetReAppropriation.getId());
        if (budgetReAppropriation.getBudgetDetail() != null) {
            final BudgetDetailMapper bdMapper = new BudgetDetailMapper();
            contract.setBudgetDetail(bdMapper.toContract(budgetReAppropriation.getBudgetDetail()));
        }
        contract.setAdditionAmount(budgetReAppropriation.getAdditionAmount());
        contract.setDeductionAmount(budgetReAppropriation.getDeductionAmount());
        contract.setOriginalAdditionAmount(budgetReAppropriation.getOriginalAdditionAmount());
        contract.setOriginalDeductionAmount(budgetReAppropriation.getOriginalDeductionAmount());
        contract.setAnticipatoryAmount(budgetReAppropriation.getAnticipatoryAmount());
        if (budgetReAppropriation.getStatus() != null)
            contract.setStatus(FinancialStatusContract.builder().id(budgetReAppropriation.getStatus().getId())
                    .code(budgetReAppropriation.getStatus().getCode())
                    .description(budgetReAppropriation.getStatus().getDescription())
                    .moduleType(budgetReAppropriation.getStatus().getModuleType()).build());
        contract.setAsOnDate(budgetReAppropriation.getAsOnDate());
        contract.setCreatedBy(budgetReAppropriation.getCreatedBy());
        contract.setCreatedDate(budgetReAppropriation.getCreatedDate());
        contract.setLastModifiedBy(budgetReAppropriation.getLastModifiedBy());
        contract.setLastModifiedDate(budgetReAppropriation.getLastModifiedDate());
        contract.setTenantId(budgetReAppropriation.getTenantId());

        return contract;
    }

    public BudgetReAppropriationSearch toSearchDomain(final BudgetReAppropriationSearchContract contract) {

        final BudgetReAppropriationSearch budgetReAppropriationSearch = new BudgetReAppropriationSearch();

        budgetReAppropriationSearch.setId(contract.getId());
        budgetReAppropriationSearch.setBudgetDetail(BudgetDetail.builder()
                .id(contract.getBudgetDetail() != null ? contract.getBudgetDetail().getId() : null).build());
        budgetReAppropriationSearch.setAdditionAmount(contract.getAdditionAmount());
        budgetReAppropriationSearch.setDeductionAmount(contract.getDeductionAmount());
        budgetReAppropriationSearch.setOriginalAdditionAmount(contract.getOriginalAdditionAmount());
        budgetReAppropriationSearch.setOriginalDeductionAmount(contract.getOriginalDeductionAmount());
        budgetReAppropriationSearch.setAnticipatoryAmount(contract.getAnticipatoryAmount());
        budgetReAppropriationSearch.setStatus(FinancialStatusContract.builder()
                .id(contract.getStatus() != null ? contract.getStatus().getId() : null).build());
        budgetReAppropriationSearch.setAsOnDate(contract.getAsOnDate());
        budgetReAppropriationSearch.setCreatedBy(contract.getCreatedBy());
        budgetReAppropriationSearch.setCreatedDate(contract.getCreatedDate());
        budgetReAppropriationSearch.setLastModifiedBy(contract.getLastModifiedBy());
        budgetReAppropriationSearch.setLastModifiedDate(contract.getLastModifiedDate());
        budgetReAppropriationSearch.setTenantId(contract.getTenantId());
        budgetReAppropriationSearch.setPageSize(contract.getPageSize());
        budgetReAppropriationSearch.setOffset(contract.getOffset());

        return budgetReAppropriationSearch;
    }

    public BudgetReAppropriationSearchContract toSearchContract(
            final BudgetReAppropriationSearch budgetReAppropriationSearch) {

        final BudgetReAppropriationSearchContract contract = new BudgetReAppropriationSearchContract();

        contract.setId(budgetReAppropriationSearch.getId());
        contract.setBudgetDetail(BudgetDetailContract.builder().id(budgetReAppropriationSearch.getBudgetDetail() != null
                ? budgetReAppropriationSearch.getBudgetDetail().getId() : null).build());
        contract.setAdditionAmount(budgetReAppropriationSearch.getAdditionAmount());
        contract.setDeductionAmount(budgetReAppropriationSearch.getDeductionAmount());
        contract.setOriginalAdditionAmount(budgetReAppropriationSearch.getOriginalAdditionAmount());
        contract.setOriginalDeductionAmount(budgetReAppropriationSearch.getOriginalDeductionAmount());
        contract.setAnticipatoryAmount(budgetReAppropriationSearch.getAnticipatoryAmount());
        contract.setStatus(FinancialStatusContract.builder().id(budgetReAppropriationSearch.getStatus() != null
                ? budgetReAppropriationSearch.getStatus().getId() : null).build());
        contract.setAsOnDate(budgetReAppropriationSearch.getAsOnDate());
        contract.setCreatedBy(budgetReAppropriationSearch.getCreatedBy());
        contract.setCreatedDate(budgetReAppropriationSearch.getCreatedDate());
        contract.setLastModifiedBy(budgetReAppropriationSearch.getLastModifiedBy());
        contract.setLastModifiedDate(budgetReAppropriationSearch.getLastModifiedDate());
        contract.setTenantId(budgetReAppropriationSearch.getTenantId());
        contract.setPageSize(budgetReAppropriationSearch.getPageSize());
        contract.setOffset(budgetReAppropriationSearch.getOffset());

        return contract;
    }

}
