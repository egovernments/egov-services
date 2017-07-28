package org.egov.egf.budget.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.budget.persistence.entity.BudgetEntity;
import org.egov.egf.budget.persistence.repository.BudgetJdbcRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetRepositoryTest {

	@InjectMocks
	private BudgetRepository budgetRepository;

	@Mock
	private BudgetJdbcRepository budgetJdbcRepository;

	@Test
	public void test_find_by_id() {
		BudgetEntity entity = getBudgetEntity();
		Budget expectedResult = entity.toDomain();

		when(budgetJdbcRepository.findById(any(BudgetEntity.class))).thenReturn(entity);

		Budget actualResult = budgetRepository.findById(getBudgetDomin());

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void test_find_by_id_return_null() {
		BudgetEntity entity = getBudgetEntity();

		when(budgetJdbcRepository.findById(null)).thenReturn(entity);

		Budget actualResult = budgetRepository.findById(getBudgetDomin());

		assertEquals(null, actualResult);
	}

	@Test
	public void test_save() {

		BudgetEntity entity = getBudgetEntity();
		Budget expectedResult = entity.toDomain();

		when(budgetJdbcRepository.create(any(BudgetEntity.class))).thenReturn(entity);

		Budget actualResult = budgetRepository.save(getBudgetDomin());

		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void test_update() {

		BudgetEntity entity = getBudgetEntity();
		Budget expectedResult = entity.toDomain();

		when(budgetJdbcRepository.update(any(BudgetEntity.class))).thenReturn(entity);

		Budget actualResult = budgetRepository.update(getBudgetDomin());

		assertEquals(expectedResult, actualResult);

	}

	@Test
	public void test_search() {

		Pagination<Budget> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);
		when(budgetJdbcRepository.search(any(BudgetSearch.class))).thenReturn(expectedResult);
		Pagination<Budget> actualResult = budgetRepository.search(getBudgetSearch());
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

}
