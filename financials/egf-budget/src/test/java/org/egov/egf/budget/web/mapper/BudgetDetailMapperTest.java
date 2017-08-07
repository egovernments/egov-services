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

import java.math.BigDecimal;

import org.egov.common.contract.request.User;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetDetailMapperTest {

    @InjectMocks
    private BudgetDetailMapper budgetDetailMapper;

    @Before
    public void setup() {
        budgetDetailMapper = new BudgetDetailMapper();
    }

    @Test
    public void test_to_domain() {

        final BudgetDetail expectedDomain = budgetDetailMapper.toDomain(contract());

        assertEquals(expectedDomain.getId(), domain().getId());
        assertEquals(expectedDomain.getBudgetGroup().getId(), domain().getBudgetGroup().getId());
        assertEquals(expectedDomain.getBudget().getId(), domain().getBudget().getId());
        assertEquals(expectedDomain.getOriginalAmount(), domain().getOriginalAmount());
        assertEquals(expectedDomain.getApprovedAmount(), domain().getApprovedAmount());
        assertEquals(expectedDomain.getBudgetAvailable(), domain().getBudgetAvailable());
        assertEquals(expectedDomain.getAnticipatoryAmount(), domain().getAnticipatoryAmount());
        assertEquals(expectedDomain.getUsingDepartment().getId(), domain().getUsingDepartment().getId());
        assertEquals(expectedDomain.getExecutingDepartment().getId(), domain().getExecutingDepartment().getId());
        assertEquals(expectedDomain.getFunction().getId(), domain().getFunction().getId());
        assertEquals(expectedDomain.getScheme().getId(), domain().getScheme().getId());
        assertEquals(expectedDomain.getFund().getId(), domain().getFund().getId());
        assertEquals(expectedDomain.getSubScheme().getId(), domain().getSubScheme().getId());
        assertEquals(expectedDomain.getFunctionary().getId(), domain().getFunctionary().getId());
        assertEquals(expectedDomain.getBoundary().getId(), domain().getBoundary().getId());
        assertEquals(expectedDomain.getMaterializedPath(), domain().getMaterializedPath());
        assertEquals(expectedDomain.getDocumentNumber(), domain().getDocumentNumber());
        assertEquals(expectedDomain.getUniqueNo(), domain().getUniqueNo());
        assertEquals(expectedDomain.getPlanningPercent(), domain().getPlanningPercent());
        assertEquals(expectedDomain.getStatus().getId(), domain().getStatus().getId());
        assertEquals(expectedDomain.getCreatedBy().getId(), domain().getCreatedBy().getId());
        assertEquals(expectedDomain.getLastModifiedBy().getId(), domain().getLastModifiedBy().getId());
        assertEquals(expectedDomain.getTenantId(), domain().getTenantId());

    }

    @Test
    public void test_to_contract() {

        final BudgetDetailContract expectedContract = budgetDetailMapper.toContract(domain());

        assertEquals(expectedContract.getId(), contract().getId());
        assertEquals(expectedContract.getBudgetGroup().getId(), contract().getBudgetGroup().getId());
        assertEquals(expectedContract.getBudget().getId(), contract().getBudget().getId());
        assertEquals(expectedContract.getOriginalAmount(), contract().getOriginalAmount());
        assertEquals(expectedContract.getApprovedAmount(), contract().getApprovedAmount());
        assertEquals(expectedContract.getBudgetAvailable(), contract().getBudgetAvailable());
        assertEquals(expectedContract.getAnticipatoryAmount(), contract().getAnticipatoryAmount());
        assertEquals(expectedContract.getUsingDepartment().getId(), contract().getUsingDepartment().getId());
        assertEquals(expectedContract.getExecutingDepartment().getId(), contract().getExecutingDepartment().getId());
        assertEquals(expectedContract.getFunction().getId(), contract().getFunction().getId());
        assertEquals(expectedContract.getScheme().getId(), contract().getScheme().getId());
        assertEquals(expectedContract.getFund().getId(), contract().getFund().getId());
        assertEquals(expectedContract.getSubScheme().getId(), contract().getSubScheme().getId());
        assertEquals(expectedContract.getFunctionary().getId(), contract().getFunctionary().getId());
        assertEquals(expectedContract.getBoundary().getId(), contract().getBoundary().getId());
        assertEquals(expectedContract.getMaterializedPath(), contract().getMaterializedPath());
        assertEquals(expectedContract.getDocumentNumber(), contract().getDocumentNumber());
        assertEquals(expectedContract.getUniqueNo(), contract().getUniqueNo());
        assertEquals(expectedContract.getPlanningPercent(), contract().getPlanningPercent());
        assertEquals(expectedContract.getStatus().getId(), contract().getStatus().getId());
        assertEquals(expectedContract.getCreatedBy().getId(), contract().getCreatedBy().getId());
        assertEquals(expectedContract.getLastModifiedBy().getId(), contract().getLastModifiedBy().getId());
        assertEquals(expectedContract.getTenantId(), contract().getTenantId());

    }

    @Test
    public void test_to_search_domain() {

        final BudgetDetailSearch expectedSearchDomain = budgetDetailMapper.toSearchDomain(searchContract());

        assertEquals(expectedSearchDomain.getId(), searchContract().getId());
        assertEquals(expectedSearchDomain.getBudgetGroup().getId(), searchContract().getBudgetGroup().getId());
        assertEquals(expectedSearchDomain.getBudget().getId(), searchContract().getBudget().getId());
        assertEquals(expectedSearchDomain.getOriginalAmount(), searchContract().getOriginalAmount());
        assertEquals(expectedSearchDomain.getApprovedAmount(), searchContract().getApprovedAmount());
        assertEquals(expectedSearchDomain.getBudgetAvailable(), searchContract().getBudgetAvailable());
        assertEquals(expectedSearchDomain.getAnticipatoryAmount(), searchContract().getAnticipatoryAmount());
        assertEquals(expectedSearchDomain.getUsingDepartment().getId(), searchContract().getUsingDepartment().getId());
        assertEquals(expectedSearchDomain.getExecutingDepartment().getId(),
                searchContract().getExecutingDepartment().getId());
        assertEquals(expectedSearchDomain.getFunction().getId(), searchContract().getFunction().getId());
        assertEquals(expectedSearchDomain.getScheme().getId(), searchContract().getScheme().getId());
        assertEquals(expectedSearchDomain.getFund().getId(), searchContract().getFund().getId());
        assertEquals(expectedSearchDomain.getSubScheme().getId(), searchContract().getSubScheme().getId());
        assertEquals(expectedSearchDomain.getFunctionary().getId(), searchContract().getFunctionary().getId());
        assertEquals(expectedSearchDomain.getBoundary().getId(), searchContract().getBoundary().getId());
        assertEquals(expectedSearchDomain.getMaterializedPath(), searchContract().getMaterializedPath());
        assertEquals(expectedSearchDomain.getDocumentNumber(), searchContract().getDocumentNumber());
        assertEquals(expectedSearchDomain.getUniqueNo(), searchContract().getUniqueNo());
        assertEquals(expectedSearchDomain.getPlanningPercent(), searchContract().getPlanningPercent());
        assertEquals(expectedSearchDomain.getStatus().getId(), searchContract().getStatus().getId());
        assertEquals(expectedSearchDomain.getCreatedBy().getId(), searchContract().getCreatedBy().getId());
        assertEquals(expectedSearchDomain.getLastModifiedBy().getId(), searchContract().getLastModifiedBy().getId());
        assertEquals(expectedSearchDomain.getTenantId(), searchContract().getTenantId());
        assertEquals(expectedSearchDomain.getPageSize(), searchContract().getPageSize());
        assertEquals(expectedSearchDomain.getOffset(), searchContract().getOffset());

    }

    @Test
    public void test_to_search_contract() {

        final BudgetDetailSearchContract expectedSearchContract = budgetDetailMapper.toSearchContract(searchDomain());

        assertEquals(expectedSearchContract.getId(), searchContract().getId());
        assertEquals(expectedSearchContract.getBudgetGroup().getId(), searchContract().getBudgetGroup().getId());
        assertEquals(expectedSearchContract.getBudget().getId(), searchContract().getBudget().getId());
        assertEquals(expectedSearchContract.getOriginalAmount(), searchContract().getOriginalAmount());
        assertEquals(expectedSearchContract.getApprovedAmount(), searchContract().getApprovedAmount());
        assertEquals(expectedSearchContract.getBudgetAvailable(), searchContract().getBudgetAvailable());
        assertEquals(expectedSearchContract.getAnticipatoryAmount(), searchContract().getAnticipatoryAmount());
        assertEquals(expectedSearchContract.getUsingDepartment().getId(),
                searchContract().getUsingDepartment().getId());
        assertEquals(expectedSearchContract.getExecutingDepartment().getId(),
                searchContract().getExecutingDepartment().getId());
        assertEquals(expectedSearchContract.getFunction().getId(), searchContract().getFunction().getId());
        assertEquals(expectedSearchContract.getScheme().getId(), searchContract().getScheme().getId());
        assertEquals(expectedSearchContract.getFund().getId(), searchContract().getFund().getId());
        assertEquals(expectedSearchContract.getSubScheme().getId(), searchContract().getSubScheme().getId());
        assertEquals(expectedSearchContract.getFunctionary().getId(), searchContract().getFunctionary().getId());
        assertEquals(expectedSearchContract.getBoundary().getId(), searchContract().getBoundary().getId());
        assertEquals(expectedSearchContract.getMaterializedPath(), searchContract().getMaterializedPath());
        assertEquals(expectedSearchContract.getDocumentNumber(), searchContract().getDocumentNumber());
        assertEquals(expectedSearchContract.getUniqueNo(), searchContract().getUniqueNo());
        assertEquals(expectedSearchContract.getPlanningPercent(), searchContract().getPlanningPercent());
        assertEquals(expectedSearchContract.getStatus().getId(), searchContract().getStatus().getId());
        assertEquals(expectedSearchContract.getCreatedBy().getId(), searchContract().getCreatedBy().getId());
        assertEquals(expectedSearchContract.getLastModifiedBy().getId(), searchContract().getLastModifiedBy().getId());
        assertEquals(expectedSearchContract.getTenantId(), searchContract().getTenantId());
        assertEquals(expectedSearchContract.getPageSize(), searchContract().getPageSize());
        assertEquals(expectedSearchContract.getOffset(), searchContract().getOffset());

    }

    public BudgetDetail domain() {

        final BudgetDetail budgetDetail = new BudgetDetail();

        budgetDetail.setId("id");
        budgetDetail.setBudgetGroup(BudgetGroupContract.builder().id("id").build());
        final Budget budget = Budget.builder().id("id").build();
        budget.setTenantId("tenantId");
        budgetDetail.setBudget(budget);
        budgetDetail.setOriginalAmount(BigDecimal.ONE);
        budgetDetail.setApprovedAmount(BigDecimal.ONE);
        budgetDetail.setBudgetAvailable(BigDecimal.ONE);
        budgetDetail.setAnticipatoryAmount(BigDecimal.ONE);
        budgetDetail.setUsingDepartment(Department.builder().id("id").build());
        budgetDetail.setExecutingDepartment(Department.builder().id("id").build());
        budgetDetail.setFunction(FunctionContract.builder().id("id").build());
        budgetDetail.setScheme(SchemeContract.builder().id("id").build());
        budgetDetail.setFund(FundContract.builder().id("id").build());
        budgetDetail.setSubScheme(SubSchemeContract.builder().id("id").build());
        budgetDetail.setFunctionary(FunctionaryContract.builder().id("id").build());
        budgetDetail.setBoundary(Boundary.builder().id("id").build());
        budgetDetail.setMaterializedPath("materializedPath");
        budgetDetail.setDocumentNumber("documentNumber");
        budgetDetail.setUniqueNo("uniqueNo");
        budgetDetail.setPlanningPercent(BigDecimal.ONE);
        budgetDetail.setStatus(FinancialStatusContract.builder().id("id").build());
        budgetDetail.setCreatedBy(User.builder().id(1l).build());
        budgetDetail.setLastModifiedBy(User.builder().id(1l).build());
        budgetDetail.setTenantId("tenantId");

        return budgetDetail;
    }

    public BudgetDetailContract contract() {

        final BudgetDetailContract contract = new BudgetDetailContract();

        contract.setId("id");
        contract.setBudgetGroup(BudgetGroupContract.builder().id("id").build());
        final BudgetContract budget = BudgetContract.builder().id("id").build();
        budget.setTenantId("tenantId");
        contract.setBudget(budget);
        contract.setOriginalAmount(BigDecimal.ONE);
        contract.setApprovedAmount(BigDecimal.ONE);
        contract.setBudgetAvailable(BigDecimal.ONE);
        contract.setAnticipatoryAmount(BigDecimal.ONE);
        contract.setUsingDepartment(Department.builder().id("id").build());
        contract.setExecutingDepartment(Department.builder().id("id").build());
        contract.setFunction(FunctionContract.builder().id("id").build());
        contract.setScheme(SchemeContract.builder().id("id").build());
        contract.setFund(FundContract.builder().id("id").build());
        contract.setSubScheme(SubSchemeContract.builder().id("id").build());
        contract.setFunctionary(FunctionaryContract.builder().id("id").build());
        contract.setBoundary(Boundary.builder().id("id").build());
        contract.setMaterializedPath("materializedPath");
        contract.setDocumentNumber("documentNumber");
        contract.setUniqueNo("uniqueNo");
        contract.setPlanningPercent(BigDecimal.ONE);
        contract.setStatus(FinancialStatusContract.builder().id("id").build());
        contract.setCreatedBy(User.builder().id(1l).build());
        contract.setLastModifiedBy(User.builder().id(1l).build());
        contract.setTenantId("tenantId");

        return contract;
    }

    public BudgetDetailSearch searchDomain() {

        final BudgetDetailSearch budgetDetailSearch = new BudgetDetailSearch();
        budgetDetailSearch.setId("id");
        budgetDetailSearch.setBudgetGroup(BudgetGroupContract.builder().id("id").build());
        final Budget budget = Budget.builder().id("id").build();
        budget.setTenantId("tenantId");
        budgetDetailSearch.setBudget(budget);
        budgetDetailSearch.setOriginalAmount(BigDecimal.ONE);
        budgetDetailSearch.setApprovedAmount(BigDecimal.ONE);
        budgetDetailSearch.setBudgetAvailable(BigDecimal.ONE);
        budgetDetailSearch.setAnticipatoryAmount(BigDecimal.ONE);
        budgetDetailSearch.setUsingDepartment(Department.builder().id("id").build());
        budgetDetailSearch.setExecutingDepartment(Department.builder().id("id").build());
        budgetDetailSearch.setFunction(FunctionContract.builder().id("id").build());
        budgetDetailSearch.setScheme(SchemeContract.builder().id("id").build());
        budgetDetailSearch.setFund(FundContract.builder().id("id").build());
        budgetDetailSearch.setSubScheme(SubSchemeContract.builder().id("id").build());
        budgetDetailSearch.setFunctionary(FunctionaryContract.builder().id("id").build());
        budgetDetailSearch.setBoundary(Boundary.builder().id("id").build());
        budgetDetailSearch.setMaterializedPath("materializedPath");
        budgetDetailSearch.setDocumentNumber("documentNumber");
        budgetDetailSearch.setUniqueNo("uniqueNo");
        budgetDetailSearch.setPlanningPercent(BigDecimal.ONE);
        budgetDetailSearch.setStatus(FinancialStatusContract.builder().id("id").build());
        budgetDetailSearch.setCreatedBy(User.builder().id(1l).build());
        budgetDetailSearch.setLastModifiedBy(User.builder().id(1l).build());
        budgetDetailSearch.setTenantId("tenantId");
        budgetDetailSearch.setPageSize(1);
        budgetDetailSearch.setOffset(1);

        return budgetDetailSearch;
    }

    public BudgetDetailSearchContract searchContract() {

        final BudgetDetailSearchContract contract = new BudgetDetailSearchContract();

        contract.setId("id");
        contract.setBudgetGroup(BudgetGroupContract.builder().id("id").build());
        final BudgetContract budget = BudgetContract.builder().id("id").build();
        budget.setTenantId("tenantId");
        contract.setBudget(budget);
        contract.setOriginalAmount(BigDecimal.ONE);
        contract.setApprovedAmount(BigDecimal.ONE);
        contract.setBudgetAvailable(BigDecimal.ONE);
        contract.setAnticipatoryAmount(BigDecimal.ONE);
        contract.setUsingDepartment(Department.builder().id("id").build());
        contract.setExecutingDepartment(Department.builder().id("id").build());
        contract.setFunction(FunctionContract.builder().id("id").build());
        contract.setScheme(SchemeContract.builder().id("id").build());
        contract.setFund(FundContract.builder().id("id").build());
        contract.setSubScheme(SubSchemeContract.builder().id("id").build());
        contract.setFunctionary(FunctionaryContract.builder().id("id").build());
        contract.setBoundary(Boundary.builder().id("id").build());
        contract.setMaterializedPath("materializedPath");
        contract.setDocumentNumber("documentNumber");
        contract.setUniqueNo("uniqueNo");
        contract.setPlanningPercent(BigDecimal.ONE);
        contract.setStatus(FinancialStatusContract.builder().id("id").build());
        contract.setCreatedBy(User.builder().id(1l).build());
        contract.setLastModifiedBy(User.builder().id(1l).build());
        contract.setTenantId("tenantId");
        contract.setPageSize(1);
        contract.setOffset(1);

        return contract;
    }

}
