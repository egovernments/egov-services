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

	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {
		budgetService = new BudgetService(validator, budgetRepository, financialYearContractRepository);
	}

	@Test
	public final void test_save_with_out_kafka() {

		List<Budget> expextedResult = getBudgets();

		when(budgetRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<Budget> actualResult = budgetService.create(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test(expected = CustomBindException.class)
	public final void test_save_with_out_kafka_and_with_null_req() {

		List<Budget> expextedResult = getBudgets();

		when(budgetRepository.save(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<Budget> actualResult = budgetService.create(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_update_with_out_kafka() {

		List<Budget> expextedResult = getBudgets();

		when(budgetRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<Budget> actualResult = budgetService.update(expextedResult, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test(expected = CustomBindException.class)
	public final void test_update_with_out_kafka_and_with_null_req() {

		List<Budget> expextedResult = getBudgets();

		when(budgetRepository.update(any(List.class), any(RequestInfo.class))).thenReturn(expextedResult);

		List<Budget> actualResult = budgetService.update(null, errors, requestInfo);

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_search() {

		List<Budget> budgets = getBudgets();
		BudgetSearch budgetSearch = new BudgetSearch();
		Pagination<Budget> expextedResult = new Pagination<>();

		expextedResult.setPagedData(budgets);

		when(budgetRepository.search(budgetSearch)).thenReturn(expextedResult);

		Pagination<Budget> actualResult = budgetService.search(budgetSearch);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_save() {

		Budget expextedResult = getBudgets().get(0);

		when(budgetRepository.save(any(Budget.class))).thenReturn(expextedResult);

		Budget actualResult = budgetService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_update() {

		Budget expextedResult = getBudgets().get(0);

		when(budgetRepository.update(any(Budget.class))).thenReturn(expextedResult);

		Budget actualResult = budgetService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_fetch_financialyear() {

		List<Budget> budgets = getBudgets();

		budgets.get(0).getFinancialYear().setId("1");
		budgets.get(0).getFinancialYear().setTenantId("tenantId");
		FinancialYearContract expextedResult = FinancialYearContract.builder().finYearRange("2017-18").id("1").build();

		when(financialYearContractRepository.findById(any(FinancialYearSearchContract.class)))
				.thenReturn(expextedResult);

		List<Budget> actualResult = budgetService.fetchRelated(budgets);

		assertEquals(expextedResult, actualResult.get(0).getFinancialYear());
	}

	@Test
	public final void test_fetch_parent() {

		List<Budget> budgets = getBudgets();

		Budget expextedResult = Budget.builder().id("1").build();
		expextedResult.setTenantId("tenantId");

		budgets.get(0).setParent(expextedResult);

		when(budgetRepository.findById(any(Budget.class))).thenReturn(expextedResult);

		List<Budget> actualResult = budgetService.fetchRelated(budgets);

		assertEquals(expextedResult, actualResult.get(0).getParent());
	}

	@Test
	public final void test_fetch_reference_budget() {

		List<Budget> budgets = getBudgets();

		Budget expextedResult = Budget.builder().id("1").build();
		expextedResult.setTenantId("tenantId");

		budgets.get(0).setReferenceBudget(expextedResult);

		when(budgetRepository.findById(any(Budget.class))).thenReturn(expextedResult);

		List<Budget> actualResult = budgetService.fetchRelated(budgets);

		assertEquals(expextedResult, actualResult.get(0).getReferenceBudget());
	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_financialyear_null() {

		List<Budget> budgets = getBudgets();

		budgets.get(0).getFinancialYear().setId("1");
		budgets.get(0).getFinancialYear().setTenantId("tenantId");
		FinancialYearContract expextedResult = FinancialYearContract.builder().finYearRange("2017-18").id("1").build();

		when(financialYearContractRepository.findById(new FinancialYearSearchContract())).thenReturn(expextedResult);

		budgetService.fetchRelated(budgets);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_parent_null() {

		List<Budget> budgets = getBudgets();

		Budget expextedResult = Budget.builder().id("1").build();
		expextedResult.setTenantId("tenantId");

		budgets.get(0).setParent(expextedResult);

		when(budgetRepository.findById(new Budget())).thenReturn(expextedResult);

		budgetService.fetchRelated(budgets);

	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_reference_budget_null() {

		List<Budget> budgets = getBudgets();

		Budget expextedResult = Budget.builder().id("1").build();
		expextedResult.setTenantId("tenantId");

		budgets.get(0).setReferenceBudget(expextedResult);

		when(budgetRepository.findById(new Budget())).thenReturn(expextedResult);

		budgetService.fetchRelated(budgets);

	}

	private List<Budget> getBudgets() {
		List<Budget> budgets = new ArrayList<Budget>();
		Budget budget = Budget.builder().name("test")
				.financialYear(FinancialYearContract.builder().finYearRange("2017-18").build())
				.estimationType(EstimationType.BE).primaryBudget(false).build();
		budget.setTenantId("default");
		budgets.add(budget);
		return budgets;
	}

}
