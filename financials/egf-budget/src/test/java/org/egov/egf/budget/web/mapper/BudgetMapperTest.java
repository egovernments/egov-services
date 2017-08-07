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

import static org.junit.Assert.assertEquals;

import org.egov.common.contract.request.User;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
import org.egov.egf.budget.web.contract.EstimationTypeContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetMapperTest {

    @InjectMocks
    private BudgetMapper budgetMapper;

    @Before
    public void setup() {
        budgetMapper = new BudgetMapper();
    }

    @Test
    public void test_to_domain() {

        final Budget expectedDomain = budgetMapper.toDomain(contract());

        assertEquals(expectedDomain, domain());

    }

    @Test
    public void test_to_contract() {

        final BudgetContract expectedContract = budgetMapper.toContract(domain());

        assertEquals(expectedContract.getId(), contract().getId());
        assertEquals(expectedContract.getName(), contract().getName());
        assertEquals(expectedContract.getFinancialYear().getId(), contract().getFinancialYear().getId());
        assertEquals(expectedContract.getEstimationType(), contract().getEstimationType());
        assertEquals(expectedContract.getParent().getId(), contract().getParent().getId());
        assertEquals(expectedContract.getReferenceBudget().getId(), contract().getReferenceBudget().getId());
        assertEquals(expectedContract.getDescription(), contract().getDescription());
        assertEquals(expectedContract.getActive(), contract().getActive());
        assertEquals(expectedContract.getPrimaryBudget(), contract().getPrimaryBudget());
        assertEquals(expectedContract.getMaterializedPath(), contract().getMaterializedPath());
        assertEquals(expectedContract.getDocumentNumber(), contract().getDocumentNumber());
        assertEquals(expectedContract.getStatus().getId(), contract().getStatus().getId());
        assertEquals(expectedContract.getCreatedBy().getId(), contract().getCreatedBy().getId());
        assertEquals(expectedContract.getLastModifiedBy().getId(), contract().getLastModifiedBy().getId());
        assertEquals(expectedContract.getTenantId(), contract().getTenantId());

    }

    @Test
    public void test_to_search_domain() {

        final BudgetSearch expectedSearchDomain = budgetMapper.toSearchDomain(searchContract());

        assertEquals(expectedSearchDomain, searchDomain());

    }

    @Test
    public void test_to_search_contract() {

        final BudgetSearchContract expectedSearchContract = budgetMapper.toSearchContract(searchDomain());

        assertEquals(expectedSearchContract.getId(), searchContract().getId());
        assertEquals(expectedSearchContract.getName(), searchContract().getName());
        assertEquals(expectedSearchContract.getFinancialYear().getId(), searchContract().getFinancialYear().getId());
        assertEquals(expectedSearchContract.getEstimationType(), searchContract().getEstimationType());
        assertEquals(expectedSearchContract.getParent().getId(), searchContract().getParent().getId());
        assertEquals(expectedSearchContract.getReferenceBudget().getId(),
                searchContract().getReferenceBudget().getId());
        assertEquals(expectedSearchContract.getDescription(), searchContract().getDescription());
        assertEquals(expectedSearchContract.getActive(), searchContract().getActive());
        assertEquals(expectedSearchContract.getPrimaryBudget(), searchContract().getPrimaryBudget());
        assertEquals(expectedSearchContract.getMaterializedPath(), searchContract().getMaterializedPath());
        assertEquals(expectedSearchContract.getDocumentNumber(), searchContract().getDocumentNumber());
        assertEquals(expectedSearchContract.getStatus().getId(), searchContract().getStatus().getId());
        assertEquals(expectedSearchContract.getCreatedBy().getId(), searchContract().getCreatedBy().getId());
        assertEquals(expectedSearchContract.getLastModifiedBy().getId(), searchContract().getLastModifiedBy().getId());
        assertEquals(expectedSearchContract.getTenantId(), searchContract().getTenantId());
        assertEquals(expectedSearchContract.getPageSize(), searchContract().getPageSize());
        assertEquals(expectedSearchContract.getOffset(), searchContract().getOffset());

    }

    public Budget domain() {

        final Budget budget = new Budget();

        budget.setId("id");
        budget.setName("name");
        budget.setFinancialYear(FinancialYearContract.builder().id("id").build());
        budget.setEstimationType(EstimationType.BE);
        budget.setParent(Budget.builder().id("parent").build());
        budget.setDescription("description");
        budget.setActive(true);
        budget.setPrimaryBudget(true);
        budget.setMaterializedPath("materializedPath");
        budget.setReferenceBudget(Budget.builder().id("referenceBudget").build());
        budget.setDocumentNumber("documentNumber");
        budget.setStatus(FinancialStatusContract.builder().id("status").code("code").description("description")
                .moduleType("moduleType").build());
        budget.setCreatedBy(User.builder().id(1l).build());
        budget.setLastModifiedBy(User.builder().id(1l).build());
        budget.setTenantId("tenantId");

        return budget;
    }

    public BudgetContract contract() {

        final BudgetContract contract = new BudgetContract();

        contract.setId("id");
        contract.setName("name");
        contract.setFinancialYear(FinancialYearContract.builder().id("id").build());
        contract.setEstimationType(EstimationTypeContract.BE);
        contract.setParent(BudgetContract.builder().id("parent").build());
        contract.setDescription("description");
        contract.setActive(true);
        contract.setPrimaryBudget(true);
        contract.setMaterializedPath("materializedPath");
        contract.setReferenceBudget(BudgetContract.builder().id("referenceBudget").build());
        contract.setDocumentNumber("documentNumber");
        contract.setStatus(FinancialStatusContract.builder().id("status").code("code").description("description")
                .moduleType("moduleType").build());
        contract.setCreatedBy(User.builder().id(1l).build());
        contract.setLastModifiedBy(User.builder().id(1l).build());
        contract.setTenantId("tenantId");

        return contract;
    }

    public BudgetSearch searchDomain() {

        final BudgetSearch budgetSearch = new BudgetSearch();

        budgetSearch.setId("id");
        budgetSearch.setName("name");
        budgetSearch.setFinancialYear(FinancialYearContract.builder().id("id").build());
        budgetSearch.setEstimationType(EstimationType.BE);
        budgetSearch.setParent(Budget.builder().id("parent").build());
        budgetSearch.setDescription("description");
        budgetSearch.setActive(true);
        budgetSearch.setPrimaryBudget(true);
        budgetSearch.setMaterializedPath("materializedPath");
        budgetSearch.setReferenceBudget(Budget.builder().id("referenceBudget").build());
        budgetSearch.setDocumentNumber("documentNumber");
        budgetSearch.setStatus(FinancialStatusContract.builder().id("status").build());
        budgetSearch.setCreatedBy(User.builder().id(1l).build());
        budgetSearch.setLastModifiedBy(User.builder().id(1l).build());
        budgetSearch.setTenantId("tenantId");
        budgetSearch.setPageSize(1);
        budgetSearch.setOffset(1);

        return budgetSearch;
    }

    public BudgetSearchContract searchContract() {

        final BudgetSearchContract contract = new BudgetSearchContract();

        contract.setId("id");
        contract.setName("name");
        contract.setFinancialYear(FinancialYearContract.builder().id("id").build());
        contract.setEstimationType(EstimationTypeContract.BE);
        contract.setParent(BudgetContract.builder().id("parent").build());
        contract.setDescription("description");
        contract.setActive(true);
        contract.setPrimaryBudget(true);
        contract.setMaterializedPath("materializedPath");
        contract.setReferenceBudget(BudgetContract.builder().id("referenceBudget").build());
        contract.setDocumentNumber("documentNumber");
        contract.setStatus(FinancialStatusContract.builder().id("status").build());
        contract.setCreatedBy(User.builder().id(1l).build());
        contract.setLastModifiedBy(User.builder().id(1l).build());
        contract.setTenantId("tenantId");
        contract.setPageSize(1);
        contract.setOffset(1);

        return contract;
    }

}
