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

	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {
		budgetRepositoryWithKafka = new BudgetRepository(budgetJdbcRepository, budgetQueueRepository, "yes");

		budgetRepositoryWithOutKafka = new BudgetRepository(budgetJdbcRepository, budgetQueueRepository, "no");
	}

	@Test
	public void test_find_by_id() {
		BudgetEntity entity = getBudgetEntity();
		Budget expectedResult = entity.toDomain();

		when(budgetJdbcRepository.findById(any(BudgetEntity.class))).thenReturn(entity);

		Budget actualResult = budgetRepositoryWithKafka.findById(getBudgetDomin());

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void test_find_by_id_return_null() {
		BudgetEntity entity = getBudgetEntity();

		when(budgetJdbcRepository.findById(null)).thenReturn(entity);

		Budget actualResult = budgetRepositoryWithKafka.findById(getBudgetDomin());

		assertEquals(null, actualResult);
	}

	@Test
	public void test_save_with_kafka() {

		List<Budget> expectedResult = getBudgets();

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

		List<Budget> expectedResult = getBudgets();

		BudgetEntity entity = new BudgetEntity().toEntity(expectedResult.get(0));

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

		List<Budget> expectedResult = getBudgets();

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
	public void test_update_with_out_kafka() {

		List<Budget> expectedResult = getBudgets();

		BudgetEntity entity = new BudgetEntity().toEntity(expectedResult.get(0));

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
	public void test_save() {

		BudgetEntity entity = getBudgetEntity();
		Budget expectedResult = entity.toDomain();

		when(budgetJdbcRepository.create(any(BudgetEntity.class))).thenReturn(entity);

		Budget actualResult = budgetRepositoryWithKafka.save(getBudgetDomin());

		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void test_update() {

		BudgetEntity entity = getBudgetEntity();
		Budget expectedResult = entity.toDomain();

		when(budgetJdbcRepository.update(any(BudgetEntity.class))).thenReturn(entity);

		Budget actualResult = budgetRepositoryWithKafka.update(getBudgetDomin());

		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void test_search() {

		Pagination<Budget> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);
		when(budgetJdbcRepository.search(any(BudgetSearch.class))).thenReturn(expectedResult);
		Pagination<Budget> actualResult = budgetRepositoryWithKafka.search(getBudgetSearch());
		assertEquals(expectedResult, actualResult);

	}

	private Budget getBudgetDomin() {
		Budget budget = new Budget();
		budget.setName("name");
		budget.setActive(true);
		budget.setEstimationType(EstimationType.BE);
		budget.setTenantId("default");
		return budget;
	}

	private BudgetEntity getBudgetEntity() {
		BudgetEntity entity = new BudgetEntity();
		entity.setName("name");
		entity.setActive(true);
		entity.setEstimationType(EstimationType.BE.name());
		entity.setTenantId("default");
		return entity;
	}

	private BudgetSearch getBudgetSearch() {
		BudgetSearch budgetSearch = new BudgetSearch();
		budgetSearch.setPageSize(500);
		budgetSearch.setOffset(0);
		return budgetSearch;

	}

	private List<Budget> getBudgets() {
		List<Budget> budgets = new ArrayList<Budget>();
		Budget budget = Budget.builder().name("test").financialYear(FinancialYearContract.builder().id("id").build())
				.estimationType(EstimationType.BE).primaryBudget(false).build();
		budget.setTenantId("default");
		budgets.add(budget);
		return budgets;
	}
}
