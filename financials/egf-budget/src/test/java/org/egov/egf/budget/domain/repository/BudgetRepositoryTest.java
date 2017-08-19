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
package org.egov.egf.budget.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.queue.repository.BudgetQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetRequest;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.egov.egf.master.web.repository.FinancialConfigurationContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetRepositoryTest {

    @Mock
    private BudgetJdbcRepository budgetJdbcRepository;

    private BudgetRepository budgetRepositoryWithKafka;

    private BudgetRepository budgetRepositoryWithOutKafka;

    @Mock
    private BudgetQueueRepository budgetQueueRepository;

    @Captor
    private ArgumentCaptor<BudgetRequest> captor;

    private final RequestInfo requestInfo = new RequestInfo();

    @Mock
    private FinancialConfigurationContractRepository financialConfigurationContractRepository;

    @Mock
    private BudgetESRepository budgetESRepository;

    @Before
    public void setup() {
        budgetRepositoryWithKafka = new BudgetRepository(budgetJdbcRepository, budgetQueueRepository, "yes",
                financialConfigurationContractRepository,budgetESRepository);

        budgetRepositoryWithOutKafka = new BudgetRepository(budgetJdbcRepository, budgetQueueRepository, "no",
                financialConfigurationContractRepository,budgetESRepository);
    }

    @Test
    public void test_find_by_id() {
        final BudgetEntity entity = getBudgetEntity();
        final Budget expectedResult = entity.toDomain();

        when(budgetJdbcRepository.findById(any(BudgetEntity.class))).thenReturn(entity);

        final Budget actualResult = budgetRepositoryWithKafka.findById(getBudgetDomin());

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_find_by_id_return_null() {
        final BudgetEntity entity = getBudgetEntity();

        when(budgetJdbcRepository.findById(null)).thenReturn(entity);

        final Budget actualResult = budgetRepositoryWithKafka.findById(getBudgetDomin());

        assertEquals(null, actualResult);
    }

    @Test
    public void test_save_with_kafka() {

        final List<Budget> expectedResult = getBudgets();

        budgetRepositoryWithKafka.save(expectedResult, requestInfo);

        verify(budgetQueueRepository).addToQue(captor.capture());

        final BudgetRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getName(), actualRequest.getBudgets().get(0).getName());
        assertEquals(expectedResult.get(0).getFinancialYear().getId(),
                actualRequest.getBudgets().get(0).getFinancialYear().getId());
        assertEquals(expectedResult.get(0).getPrimaryBudget(), actualRequest.getBudgets().get(0).getPrimaryBudget());
        assertEquals(expectedResult.get(0).getEstimationType().name(),
                actualRequest.getBudgets().get(0).getEstimationType().name());
        assertEquals(expectedResult.get(0).getTenantId(), actualRequest.getBudgets().get(0).getTenantId());
    }

    @Test
    public void test_save_with_out_kafka() {

        final List<Budget> expectedResult = getBudgets();

        final BudgetEntity entity = new BudgetEntity().toEntity(expectedResult.get(0));

        when(budgetJdbcRepository.create(any(BudgetEntity.class))).thenReturn(entity);

        budgetRepositoryWithOutKafka.save(expectedResult, requestInfo);

        verify(budgetQueueRepository).addToSearchQue(captor.capture());

        final BudgetRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getName(), actualRequest.getBudgets().get(0).getName());
        assertEquals(expectedResult.get(0).getFinancialYear().getId(),
                actualRequest.getBudgets().get(0).getFinancialYear().getId());
        assertEquals(expectedResult.get(0).getPrimaryBudget(), actualRequest.getBudgets().get(0).getPrimaryBudget());
        assertEquals(expectedResult.get(0).getEstimationType().name(),
                actualRequest.getBudgets().get(0).getEstimationType().name());
        assertEquals(expectedResult.get(0).getTenantId(), actualRequest.getBudgets().get(0).getTenantId());
    }

    @Test
    public void test_update_with_kafka() {

        final List<Budget> expectedResult = getBudgets();

        budgetRepositoryWithKafka.update(expectedResult, requestInfo);

        verify(budgetQueueRepository).addToQue(captor.capture());

        final BudgetRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getName(), actualRequest.getBudgets().get(0).getName());
        assertEquals(expectedResult.get(0).getFinancialYear().getId(),
                actualRequest.getBudgets().get(0).getFinancialYear().getId());
        assertEquals(expectedResult.get(0).getPrimaryBudget(), actualRequest.getBudgets().get(0).getPrimaryBudget());
        assertEquals(expectedResult.get(0).getEstimationType().name(),
                actualRequest.getBudgets().get(0).getEstimationType().name());
        assertEquals(expectedResult.get(0).getTenantId(), actualRequest.getBudgets().get(0).getTenantId());
    }
    
    @Test
    public void test_delete_with_kafka() {

        final List<Budget> expectedResult = getBudgets();

        budgetRepositoryWithKafka.delete(expectedResult, requestInfo);

        verify(budgetQueueRepository).addToQue(captor.capture());

        final BudgetRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getName(), actualRequest.getBudgets().get(0).getName());
        assertEquals(expectedResult.get(0).getFinancialYear().getId(),
                actualRequest.getBudgets().get(0).getFinancialYear().getId());
        assertEquals(expectedResult.get(0).getPrimaryBudget(), actualRequest.getBudgets().get(0).getPrimaryBudget());
        assertEquals(expectedResult.get(0).getEstimationType().name(),
                actualRequest.getBudgets().get(0).getEstimationType().name());
        assertEquals(expectedResult.get(0).getTenantId(), actualRequest.getBudgets().get(0).getTenantId());
    }

    @Test
    public void test_update_with_out_kafka() {

        final List<Budget> expectedResult = getBudgets();

        final BudgetEntity entity = new BudgetEntity().toEntity(expectedResult.get(0));

        when(budgetJdbcRepository.update(any(BudgetEntity.class))).thenReturn(entity);

        budgetRepositoryWithOutKafka.update(expectedResult, requestInfo);

        verify(budgetQueueRepository).addToSearchQue(captor.capture());

        final BudgetRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getName(), actualRequest.getBudgets().get(0).getName());
        assertEquals(expectedResult.get(0).getFinancialYear().getId(),
                actualRequest.getBudgets().get(0).getFinancialYear().getId());
        assertEquals(expectedResult.get(0).getPrimaryBudget(), actualRequest.getBudgets().get(0).getPrimaryBudget());
        assertEquals(expectedResult.get(0).getEstimationType().name(),
                actualRequest.getBudgets().get(0).getEstimationType().name());
        assertEquals(expectedResult.get(0).getTenantId(), actualRequest.getBudgets().get(0).getTenantId());
    }
    
    @Test
    public void test_delete_with_out_kafka() {

        final List<Budget> expectedResult = getBudgets();

        final BudgetEntity entity = new BudgetEntity().toEntity(expectedResult.get(0));

        when(budgetJdbcRepository.delete(any(BudgetEntity.class))).thenReturn(entity);

        budgetRepositoryWithOutKafka.delete(expectedResult, requestInfo);

        verify(budgetQueueRepository).addToSearchQue(captor.capture());

        final BudgetRequest actualRequest = captor.getValue();

        assertEquals(expectedResult.get(0).getName(), actualRequest.getBudgets().get(0).getName());
        assertEquals(expectedResult.get(0).getFinancialYear().getId(),
                actualRequest.getBudgets().get(0).getFinancialYear().getId());
        assertEquals(expectedResult.get(0).getPrimaryBudget(), actualRequest.getBudgets().get(0).getPrimaryBudget());
        assertEquals(expectedResult.get(0).getEstimationType().name(),
                actualRequest.getBudgets().get(0).getEstimationType().name());
        assertEquals(expectedResult.get(0).getTenantId(), actualRequest.getBudgets().get(0).getTenantId());
    }

    @Test
    public void test_save() {

        final BudgetEntity entity = getBudgetEntity();
        final Budget expectedResult = entity.toDomain();

        when(budgetJdbcRepository.create(any(BudgetEntity.class))).thenReturn(entity);

        final Budget actualResult = budgetRepositoryWithKafka.save(getBudgetDomin());

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void test_update() {

        final BudgetEntity entity = getBudgetEntity();
        final Budget expectedResult = entity.toDomain();

        when(budgetJdbcRepository.update(any(BudgetEntity.class))).thenReturn(entity);

        final Budget actualResult = budgetRepositoryWithKafka.update(getBudgetDomin());

        assertEquals(expectedResult, actualResult);

    }
    
    @Test
    public void test_delete() {

        final BudgetEntity entity = getBudgetEntity();
        final Budget expectedResult = entity.toDomain();

        when(budgetJdbcRepository.delete(any(BudgetEntity.class))).thenReturn(entity);

        final Budget actualResult = budgetRepositoryWithKafka.delete(getBudgetDomin());

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void test_search() {

        final Pagination<Budget> expectedResult = new Pagination<>();
        expectedResult.setPageSize(500);
        expectedResult.setOffset(0);
        when(budgetJdbcRepository.search(any(BudgetSearch.class))).thenReturn(expectedResult);
        final Pagination<Budget> actualResult = budgetRepositoryWithKafka.search(getBudgetSearch());
        assertEquals(expectedResult, actualResult);

    }

    private Budget getBudgetDomin() {
        final Budget budget = new Budget();
        budget.setName("name");
        budget.setActive(true);
        budget.setEstimationType(EstimationType.BE);
        budget.setTenantId("default");
        return budget;
    }

    private BudgetEntity getBudgetEntity() {
        final BudgetEntity entity = new BudgetEntity();
        entity.setName("name");
        entity.setActive(true);
        entity.setEstimationType(EstimationType.BE.name());
        entity.setTenantId("default");
        return entity;
    }

    private BudgetSearch getBudgetSearch() {
        final BudgetSearch budgetSearch = new BudgetSearch();
        budgetSearch.setPageSize(500);
        budgetSearch.setOffset(0);
        return budgetSearch;

    }

    private List<Budget> getBudgets() {
        final List<Budget> budgets = new ArrayList<Budget>();
        final Budget budget = Budget.builder().name("test").financialYear(FinancialYearContract.builder().id("id").build())
                .estimationType(EstimationType.BE).primaryBudget(false).build();
        budget.setTenantId("default");
        budgets.add(budget);
        return budgets;
    }
}
