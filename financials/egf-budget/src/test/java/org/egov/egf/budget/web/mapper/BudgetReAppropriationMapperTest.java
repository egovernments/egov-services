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
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationSearchContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetReAppropriationMapperTest {

    @InjectMocks
    private BudgetReAppropriationMapper budgetDetailMapper;

    @Before
    public void setup() {
        budgetDetailMapper = new BudgetReAppropriationMapper();
    }

    @Test
    public void test_to_domain() {

        final BudgetReAppropriation expectedDomain = budgetDetailMapper.toDomain(contract());

        assertEquals(expectedDomain.getId(), domain().getId());
        assertEquals(expectedDomain.getBudgetDetail().getId(), domain().getBudgetDetail().getId());
        assertEquals(expectedDomain.getAdditionAmount(), domain().getAdditionAmount());
        assertEquals(expectedDomain.getDeductionAmount(), domain().getDeductionAmount());
        assertEquals(expectedDomain.getOriginalAdditionAmount(), domain().getOriginalAdditionAmount());
        assertEquals(expectedDomain.getOriginalDeductionAmount(), domain().getOriginalDeductionAmount());
        assertEquals(expectedDomain.getAnticipatoryAmount(), domain().getAnticipatoryAmount());
        assertEquals(expectedDomain.getStatus().getId(), domain().getStatus().getId());
        assertEquals(expectedDomain.getCreatedBy().getId(), domain().getCreatedBy().getId());
        assertEquals(expectedDomain.getLastModifiedBy().getId(), domain().getLastModifiedBy().getId());
        assertEquals(expectedDomain.getTenantId(), domain().getTenantId());

    }

    @Test
    public void test_to_contract() {

        final BudgetReAppropriationContract expectedContract = budgetDetailMapper.toContract(domain());

        assertEquals(expectedContract.getId(), contract().getId());
        assertEquals(expectedContract.getBudgetDetail().getId(), contract().getBudgetDetail().getId());
        assertEquals(expectedContract.getAdditionAmount(), contract().getAdditionAmount());
        assertEquals(expectedContract.getDeductionAmount(), contract().getDeductionAmount());
        assertEquals(expectedContract.getOriginalAdditionAmount(), contract().getOriginalAdditionAmount());
        assertEquals(expectedContract.getOriginalDeductionAmount(), contract().getOriginalDeductionAmount());
        assertEquals(expectedContract.getAnticipatoryAmount(), contract().getAnticipatoryAmount());
        assertEquals(expectedContract.getStatus().getId(), contract().getStatus().getId());
        assertEquals(expectedContract.getCreatedBy().getId(), contract().getCreatedBy().getId());
        assertEquals(expectedContract.getLastModifiedBy().getId(), contract().getLastModifiedBy().getId());
        assertEquals(expectedContract.getTenantId(), contract().getTenantId());

    }

    @Test
    public void test_to_search_domain() {

        final BudgetReAppropriationSearch expectedSearchDomain = budgetDetailMapper.toSearchDomain(searchContract());

        assertEquals(expectedSearchDomain.getId(), searchDomain().getId());
        assertEquals(expectedSearchDomain.getBudgetDetail().getId(), searchDomain().getBudgetDetail().getId());
        assertEquals(expectedSearchDomain.getAdditionAmount(), searchDomain().getAdditionAmount());
        assertEquals(expectedSearchDomain.getDeductionAmount(), searchDomain().getDeductionAmount());
        assertEquals(expectedSearchDomain.getOriginalAdditionAmount(), searchDomain().getOriginalAdditionAmount());
        assertEquals(expectedSearchDomain.getOriginalDeductionAmount(), searchDomain().getOriginalDeductionAmount());
        assertEquals(expectedSearchDomain.getAnticipatoryAmount(), searchDomain().getAnticipatoryAmount());
        assertEquals(expectedSearchDomain.getStatus().getId(), searchDomain().getStatus().getId());
        assertEquals(expectedSearchDomain.getCreatedBy().getId(), searchDomain().getCreatedBy().getId());
        assertEquals(expectedSearchDomain.getLastModifiedBy().getId(), searchDomain().getLastModifiedBy().getId());
        assertEquals(expectedSearchDomain.getTenantId(), searchDomain().getTenantId());

    }

    @Test
    public void test_to_search_contract() {

        final BudgetReAppropriationSearchContract expectedSearchContract = budgetDetailMapper
                .toSearchContract(searchDomain());

        assertEquals(expectedSearchContract.getId(), searchContract().getId());
        assertEquals(expectedSearchContract.getBudgetDetail().getId(), searchContract().getBudgetDetail().getId());
        assertEquals(expectedSearchContract.getAdditionAmount(), searchContract().getAdditionAmount());
        assertEquals(expectedSearchContract.getDeductionAmount(), searchContract().getDeductionAmount());
        assertEquals(expectedSearchContract.getOriginalAdditionAmount(), searchContract().getOriginalAdditionAmount());
        assertEquals(expectedSearchContract.getOriginalDeductionAmount(),
                searchContract().getOriginalDeductionAmount());
        assertEquals(expectedSearchContract.getAnticipatoryAmount(), searchContract().getAnticipatoryAmount());
        assertEquals(expectedSearchContract.getStatus().getId(), searchContract().getStatus().getId());
        assertEquals(expectedSearchContract.getCreatedBy().getId(), searchContract().getCreatedBy().getId());
        assertEquals(expectedSearchContract.getLastModifiedBy().getId(), searchContract().getLastModifiedBy().getId());
        assertEquals(expectedSearchContract.getTenantId(), searchContract().getTenantId());

    }

    public BudgetReAppropriation domain() {

        final BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation();

        budgetReAppropriation.setId("id");
        budgetReAppropriation.setBudgetDetail(BudgetDetail.builder().id("id").build());
        budgetReAppropriation.setAdditionAmount(BigDecimal.ONE);
        budgetReAppropriation.setDeductionAmount(BigDecimal.ONE);
        budgetReAppropriation.setOriginalAdditionAmount(BigDecimal.ONE);
        budgetReAppropriation.setOriginalDeductionAmount(BigDecimal.ONE);
        budgetReAppropriation.setAnticipatoryAmount(BigDecimal.ONE);
        budgetReAppropriation.setStatus(FinancialStatusContract.builder().id("id").build());
        budgetReAppropriation.setCreatedBy(User.builder().id(1l).build());
        budgetReAppropriation.setLastModifiedBy(User.builder().id(1l).build());
        budgetReAppropriation.setTenantId("tenantId");

        return budgetReAppropriation;
    }

    public BudgetReAppropriationContract contract() {

        final BudgetReAppropriationContract contract = new BudgetReAppropriationContract();

        contract.setId("id");
        contract.setBudgetDetail(BudgetDetailContract.builder().id("id").build());
        contract.setAdditionAmount(BigDecimal.ONE);
        contract.setDeductionAmount(BigDecimal.ONE);
        contract.setOriginalAdditionAmount(BigDecimal.ONE);
        contract.setOriginalDeductionAmount(BigDecimal.ONE);
        contract.setAnticipatoryAmount(BigDecimal.ONE);
        contract.setStatus(FinancialStatusContract.builder().id("id").build());
        contract.setCreatedBy(User.builder().id(1l).build());
        contract.setLastModifiedBy(User.builder().id(1l).build());
        contract.setTenantId("tenantId");

        return contract;
    }

    public BudgetReAppropriationSearch searchDomain() {

        final BudgetReAppropriationSearch budgetReAppropriationSearch = new BudgetReAppropriationSearch();

        budgetReAppropriationSearch.setId("id");
        budgetReAppropriationSearch.setBudgetDetail(BudgetDetail.builder().id("id").build());
        budgetReAppropriationSearch.setAdditionAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setDeductionAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setOriginalAdditionAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setOriginalDeductionAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setAnticipatoryAmount(BigDecimal.ONE);
        budgetReAppropriationSearch.setStatus(FinancialStatusContract.builder().id("id").build());
        budgetReAppropriationSearch.setCreatedBy(User.builder().id(1l).build());
        budgetReAppropriationSearch.setLastModifiedBy(User.builder().id(1l).build());
        budgetReAppropriationSearch.setTenantId("tenantId");
        budgetReAppropriationSearch.setPageSize(1);
        budgetReAppropriationSearch.setOffset(1);

        return budgetReAppropriationSearch;
    }

    public BudgetReAppropriationSearchContract searchContract() {

        final BudgetReAppropriationSearchContract contract = new BudgetReAppropriationSearchContract();

        contract.setId("id");
        contract.setBudgetDetail(BudgetDetailContract.builder().id("id").build());
        contract.setAdditionAmount(BigDecimal.ONE);
        contract.setDeductionAmount(BigDecimal.ONE);
        contract.setOriginalAdditionAmount(BigDecimal.ONE);
        contract.setOriginalDeductionAmount(BigDecimal.ONE);
        contract.setAnticipatoryAmount(BigDecimal.ONE);
        contract.setStatus(FinancialStatusContract.builder().id("id").build());
        contract.setCreatedBy(User.builder().id(1l).build());
        contract.setLastModifiedBy(User.builder().id(1l).build());
        contract.setTenantId("tenantId");
        contract.setPageSize(1);
        contract.setOffset(1);

        return contract;
    }

}
