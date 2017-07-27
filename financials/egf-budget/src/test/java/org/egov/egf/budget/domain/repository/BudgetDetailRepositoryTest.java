package org.egov.egf.budget.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.persistence.entity.BudgetDetailEntity;
import org.egov.egf.budget.persistence.repository.BudgetDetailJdbcRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetDetailRepositoryTest {

	@InjectMocks
	private BudgetDetailRepository budgetRepository;

	@Mock
	private BudgetDetailJdbcRepository budgetJdbcRepository;

	@Test
	public void test_find_by_id() {
		BudgetDetailEntity entity = getBudgetDetailEntity();
		BudgetDetail expectedResult = entity.toDomain();

		when(budgetJdbcRepository.findById(any(BudgetDetailEntity.class))).thenReturn(entity);

		BudgetDetail actualResult = budgetRepository.findById(getBudgetDetailDomin());

		assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
		assertEquals(expectedResult.getApprovedAmount(), actualResult.getApprovedAmount());
		assertEquals(expectedResult.getBudgetAvailable(), actualResult.getBudgetAvailable());
		assertEquals(expectedResult.getOriginalAmount(), actualResult.getOriginalAmount());
		assertEquals(expectedResult.getPlanningPercent(), actualResult.getPlanningPercent());
	}

	@Test
	public void test_find_by_id_return_null() {
		BudgetDetailEntity entity = getBudgetDetailEntity();

		when(budgetJdbcRepository.findById(null)).thenReturn(entity);

		BudgetDetail actualResult = budgetRepository.findById(getBudgetDetailDomin());

		assertEquals(null, actualResult);
	}

	@Test
	public void test_save() {

		BudgetDetailEntity entity = getBudgetDetailEntity();
		BudgetDetail expectedResult = entity.toDomain();

		when(budgetJdbcRepository.create(any(BudgetDetailEntity.class))).thenReturn(entity);

		BudgetDetail actualResult = budgetRepository.save(getBudgetDetailDomin());

		assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
		assertEquals(expectedResult.getApprovedAmount(), actualResult.getApprovedAmount());
		assertEquals(expectedResult.getBudgetAvailable(), actualResult.getBudgetAvailable());
		assertEquals(expectedResult.getOriginalAmount(), actualResult.getOriginalAmount());
		assertEquals(expectedResult.getPlanningPercent(), actualResult.getPlanningPercent());

	}

	@Test
	public void test_update() {

		BudgetDetailEntity entity = getBudgetDetailEntity();
		BudgetDetail expectedResult = entity.toDomain();

		when(budgetJdbcRepository.update(any(BudgetDetailEntity.class))).thenReturn(entity);

		BudgetDetail actualResult = budgetRepository.update(getBudgetDetailDomin());

		assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
		assertEquals(expectedResult.getApprovedAmount(), actualResult.getApprovedAmount());
		assertEquals(expectedResult.getBudgetAvailable(), actualResult.getBudgetAvailable());
		assertEquals(expectedResult.getOriginalAmount(), actualResult.getOriginalAmount());
		assertEquals(expectedResult.getPlanningPercent(), actualResult.getPlanningPercent());

	}

	@Test
	public void test_search() {

		Pagination<BudgetDetail> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);

		when(budgetJdbcRepository.search(any(BudgetDetailSearch.class))).thenReturn(expectedResult);

		Pagination<BudgetDetail> actualResult = budgetRepository.search(getBudgetDetailSearch());

		assertEquals(expectedResult, actualResult);

	}

	private BudgetDetail getBudgetDetailDomin() {
		BudgetDetail budgetDetail = new BudgetDetail();
		budgetDetail.setApprovedAmount(BigDecimal.ONE);
		budgetDetail.setAnticipatoryAmount(BigDecimal.ONE);
		budgetDetail.setBudgetAvailable(BigDecimal.ONE);
		budgetDetail.setOriginalAmount(BigDecimal.ONE);
		budgetDetail.setPlanningPercent(BigDecimal.valueOf(1500));
		budgetDetail.setTenantId("default");
		return budgetDetail;
	}

	private BudgetDetailEntity getBudgetDetailEntity() {
		BudgetDetailEntity entity = new BudgetDetailEntity();
		entity.setApprovedAmount(BigDecimal.ONE);
		entity.setAnticipatoryAmount(BigDecimal.ONE);
		entity.setBudgetAvailable(BigDecimal.ONE);
		entity.setOriginalAmount(BigDecimal.ONE);
		entity.setPlanningPercent(BigDecimal.valueOf(1500));
		entity.setTenantId("default");
		return entity;
	}

	private BudgetDetailSearch getBudgetDetailSearch() {
		BudgetDetailSearch budgetSearch = new BudgetDetailSearch();
		budgetSearch.setPageSize(500);
		budgetSearch.setOffset(0);
		return budgetSearch;

	}

}
