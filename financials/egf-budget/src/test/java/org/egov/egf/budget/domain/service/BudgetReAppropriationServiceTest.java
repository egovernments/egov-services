package org.egov.egf.budget.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.TestConfiguration;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.egov.egf.budget.domain.repository.BudgetReAppropriationRepository;
import org.egov.egf.master.web.repository.FinancialYearContractRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@Import(TestConfiguration.class)
@RunWith(SpringRunner.class)
public class BudgetReAppropriationServiceTest {

	@InjectMocks
	private BudgetReAppropriationService budgetReAppropriationService;

	@Mock
	private SmartValidator validator;

	@Mock
	private BudgetReAppropriationRepository budgetReAppropriationRepository;

	@Mock
	private BudgetDetailRepository budgetDetailRepository;

	@Mock
	private FinancialYearContractRepository financialYearContractRepository;

	private BindingResult errors = new BeanPropertyBindingResult(null, null);

	@Test
	public final void test_save_for_create() {

		List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

		List<BudgetReAppropriation> actualResult = budgetReAppropriationService.save(expextedResult, errors, "create");

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_save_for_update() {

		List<BudgetReAppropriation> expextedResult = getBudgetReAppropriations();

		List<BudgetReAppropriation> actualResult = budgetReAppropriationService.save(expextedResult, errors, "update");

		assertEquals(expextedResult, actualResult);

	}

	@Test
	public final void test_search() {

		List<BudgetReAppropriation> budgets = getBudgetReAppropriations();
		BudgetReAppropriationSearch budgetSearch = new BudgetReAppropriationSearch();
		Pagination<BudgetReAppropriation> expextedResult = new Pagination<>();

		expextedResult.setPagedData(budgets);

		when(budgetReAppropriationRepository.search(budgetSearch)).thenReturn(expextedResult);

		Pagination<BudgetReAppropriation> actualResult = budgetReAppropriationService.search(budgetSearch);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_save() {

		BudgetReAppropriation expextedResult = getBudgetReAppropriations().get(0);

		when(budgetReAppropriationRepository.save(any(BudgetReAppropriation.class))).thenReturn(expextedResult);

		BudgetReAppropriation actualResult = budgetReAppropriationService.save(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_update() {

		BudgetReAppropriation expextedResult = getBudgetReAppropriations().get(0);

		when(budgetReAppropriationRepository.update(any(BudgetReAppropriation.class))).thenReturn(expextedResult);

		BudgetReAppropriation actualResult = budgetReAppropriationService.update(expextedResult);

		assertEquals(expextedResult, actualResult);
	}

	@Test
	public final void test_fetch_budget_detail() {

		List<BudgetReAppropriation> budgets = getBudgetReAppropriations();

		BudgetDetail expextedResult = BudgetDetail.builder().id("1").build();
		expextedResult.setTenantId("tenantId");

		budgets.get(0).setBudgetDetail(expextedResult);

		when(budgetDetailRepository.findById(any(BudgetDetail.class))).thenReturn(expextedResult);

		List<BudgetReAppropriation> actualResult = budgetReAppropriationService.fetchRelated(budgets);

		assertEquals(expextedResult, actualResult.get(0).getBudgetDetail());
	}

	@Test(expected = InvalidDataException.class)
	public final void test_fetch_null_for_budget_detail() {

		List<BudgetReAppropriation> budgets = getBudgetReAppropriations();

		BudgetDetail expextedResult = BudgetDetail.builder().id("1").build();
		expextedResult.setTenantId("tenantId");

		budgets.get(0).setBudgetDetail(expextedResult);

		when(budgetDetailRepository.findById(null)).thenReturn(expextedResult);

		budgetReAppropriationService.fetchRelated(budgets);

	}

	private List<BudgetReAppropriation> getBudgetReAppropriations() {

		List<BudgetReAppropriation> budgetReAppropriations = new ArrayList<BudgetReAppropriation>();

		BudgetReAppropriation budgetReAppropriation = BudgetReAppropriation.builder()
				.budgetDetail(BudgetDetail.builder().id("1").build()).additionAmount(BigDecimal.TEN)
				.deductionAmount(BigDecimal.TEN).originalAdditionAmount(BigDecimal.TEN)
				.originalDeductionAmount(BigDecimal.TEN).anticipatoryAmount(BigDecimal.TEN).build();

		budgetReAppropriation.setTenantId("default");
		budgetReAppropriations.add(budgetReAppropriation);

		return budgetReAppropriations;
	}

}
