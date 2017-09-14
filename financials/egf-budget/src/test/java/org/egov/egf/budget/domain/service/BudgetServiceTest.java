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
package org.egov.egf.budget.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.TestConfiguration;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.egov.egf.master.web.contract.FinancialYearSearchContract;
import org.egov.egf.master.web.repository.FinancialYearContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class BudgetServiceTest {

    private BudgetService budgetService;

    @Mock
    private SmartValidator validator;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private FinancialYearContractRepository financialYearContractRepository;

    private final BindingResult errors = new BeanPropertyBindingResult(null, null);

    private final RequestInfo requestInfo = new RequestInfo();

    @Before
    public void setup() {
        budgetService = new BudgetService(validator, budgetRepository, financialYearContractRepository);
    }

    @Test
    public final void test_save_with_out_kafka() {

        final List<Budget> expextedResult = getBudgets();

        when(budgetRepository.uniqueCheck(any(String.class), any(Budget.class))).thenReturn(true);
        when(budgetRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.create(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=CustomBindException.class)
    public final void test_save_with_out_kafka_uniquefalse() {

        final List<Budget> expextedResult = getBudgets();

        when(budgetRepository.uniqueCheck(any(String.class), any(Budget.class))).thenReturn(false);
        when(budgetRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.create(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test(expected = InvalidDataException.class)
    public final void test_save_with_out_kafka_and_with_null_req() {

        final List<Budget> expextedResult = getBudgets();

        when(budgetRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.create(null, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test
    public final void test_update_with_out_kafka() {

        final List<Budget> expextedResult = getBudgets();

        when(budgetRepository.uniqueCheck(any(String.class), any(Budget.class))).thenReturn(true);
        when(budgetRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.update(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=InvalidDataException.class)
    public final void test_update_with_out_kafka_null_id() {

        final List<Budget> expextedResult = getBudgets();
        expextedResult.get(0).setId(null);

        when(budgetRepository.uniqueCheck(any(String.class), any(Budget.class))).thenReturn(true);
        when(budgetRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.update(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=CustomBindException.class)
    public final void test_update_with_out_kafka_uniquefalse() {

        final List<Budget> expextedResult = getBudgets();

        when(budgetRepository.uniqueCheck(any(String.class), any(Budget.class))).thenReturn(false);
        when(budgetRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.update(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test
    public final void test_delete_with_out_kafka() {

        final List<Budget> expextedResult = getBudgets();

        when(budgetRepository.delete(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.delete(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected=InvalidDataException.class)
    public final void test_delete_with_out_kafka_null_id() {

        final List<Budget> expextedResult = getBudgets();
        expextedResult.get(0).setId(null);

        when(budgetRepository.delete(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.delete(expextedResult, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test(expected = InvalidDataException.class)
    public final void test_update_with_out_kafka_and_with_null_req() {

        final List<Budget> expextedResult = getBudgets();

        when(budgetRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.update(null, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }
    
    @Test(expected = InvalidDataException.class)
    public final void test_delete_with_out_kafka_and_with_null_req() {

        final List<Budget> expextedResult = getBudgets();

        when(budgetRepository.delete(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.delete(null, errors, requestInfo);

        assertEquals(expextedResult, actualResult);

    }

    @Test
    public final void test_search() {

        final List<Budget> budgets = getBudgets();
        final BudgetSearch budgetSearch = new BudgetSearch();
        final Pagination<Budget> expextedResult = new Pagination<>();

        expextedResult.setPagedData(budgets);

        when(budgetRepository.search(budgetSearch)).thenReturn(expextedResult);

        final Pagination<Budget> actualResult = budgetService.search(budgetSearch);

        assertEquals(expextedResult, actualResult);
    }

    @Test
    public final void test_save() {

        final Budget expextedResult = getBudgets().get(0);

        when(budgetRepository.save(any(Budget.class))).thenReturn(expextedResult);

        final Budget actualResult = budgetService.save(expextedResult);

        assertEquals(expextedResult, actualResult);
    }

    @Test
    public final void test_update() {

        final Budget expextedResult = getBudgets().get(0);

        when(budgetRepository.update(any(Budget.class))).thenReturn(expextedResult);

        final Budget actualResult = budgetService.update(expextedResult);

        assertEquals(expextedResult, actualResult);
    }
    
    @Test
    public final void test_delete() {

        final Budget expextedResult = getBudgets().get(0);

        when(budgetRepository.delete(any(Budget.class))).thenReturn(expextedResult);

        final Budget actualResult = budgetService.delete(expextedResult);

        assertEquals(expextedResult, actualResult);
    }

    @Test
    public final void test_fetch_financialyear() {

        final List<Budget> budgets = getBudgets();

        budgets.get(0).getFinancialYear().setId("1");
        budgets.get(0).getFinancialYear().setTenantId("tenantId");
        final FinancialYearContract expextedResult = FinancialYearContract.builder().finYearRange("2017-18").id("1").build();

        when(financialYearContractRepository.findById(any(FinancialYearSearchContract.class), Matchers.anyObject()))
                .thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.fetchRelated(budgets,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getFinancialYear());
    }

    @Test
    public final void test_fetch_parent() {

        final List<Budget> budgets = getBudgets();

        final Budget expextedResult = Budget.builder().id("1").build();
        expextedResult.setTenantId("tenantId");

        budgets.get(0).setParent(expextedResult);

        when(budgetRepository.findById(any(Budget.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.fetchRelated(budgets,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getParent());
    }

    @Test
    public final void test_fetch_reference_budget() {

        final List<Budget> budgets = getBudgets();

        final Budget expextedResult = Budget.builder().id("1").build();
        expextedResult.setTenantId("tenantId");

        budgets.get(0).setReferenceBudget(expextedResult);

        when(budgetRepository.findById(any(Budget.class))).thenReturn(expextedResult);

        final List<Budget> actualResult = budgetService.fetchRelated(budgets,new RequestInfo());

        assertEquals(expextedResult, actualResult.get(0).getReferenceBudget());
    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_financialyear_null() {

        final List<Budget> budgets = getBudgets();

        budgets.get(0).getFinancialYear().setId("1");
        budgets.get(0).getFinancialYear().setTenantId("tenantId");
        final FinancialYearContract expextedResult = FinancialYearContract.builder().finYearRange("2017-18").id("1").build();

        when(financialYearContractRepository.findById(new FinancialYearSearchContract(),new RequestInfo())).thenReturn(expextedResult);

        budgetService.fetchRelated(budgets,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_parent_null() {

        final List<Budget> budgets = getBudgets();

        final Budget expextedResult = Budget.builder().id("1").build();
        expextedResult.setTenantId("tenantId");

        budgets.get(0).setParent(expextedResult);

        when(budgetRepository.findById(new Budget())).thenReturn(expextedResult);

        budgetService.fetchRelated(budgets,new RequestInfo());

    }

    @Test(expected = InvalidDataException.class)
    public final void test_fetch_reference_budget_null() {

        final List<Budget> budgets = getBudgets();

        final Budget expextedResult = Budget.builder().id("1").build();
        expextedResult.setTenantId("tenantId");

        budgets.get(0).setReferenceBudget(expextedResult);

        when(budgetRepository.findById(new Budget())).thenReturn(expextedResult);

        budgetService.fetchRelated(budgets,new RequestInfo());

    }

    private List<Budget> getBudgets() {
        final List<Budget> budgets = new ArrayList<Budget>();
        final Budget budget = Budget.builder().name("test").id("1")
                .financialYear(FinancialYearContract.builder().finYearRange("2017-18").build())
                .estimationType(EstimationType.BE).primaryBudget(false).build();
        budget.setTenantId("default");
        budgets.add(budget);
        return budgets;
    }

}
