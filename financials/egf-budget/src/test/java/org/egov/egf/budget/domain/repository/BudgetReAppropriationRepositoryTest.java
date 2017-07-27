package org.egov.egf.budget.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.budget.persistence.repository.BudgetReAppropriationJdbcRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetReAppropriationRepositoryTest {

	@InjectMocks
	private BudgetReAppropriationRepository budgetRepository;

	@Mock
	private BudgetReAppropriationJdbcRepository budgetJdbcRepository;

	@Test
	public void test_find_by_id() {
		BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();
		BudgetReAppropriation expectedResult = entity.toDomain();

		when(budgetJdbcRepository.findById(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

		BudgetReAppropriation actualResult = budgetRepository.findById(getBudgetReAppropriationDomin());

		assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
		assertEquals(expectedResult.getAdditionAmount(), actualResult.getAdditionAmount());
		assertEquals(expectedResult.getDeductionAmount(), actualResult.getDeductionAmount());
		assertEquals(expectedResult.getOriginalAdditionAmount(), actualResult.getOriginalAdditionAmount());
		assertEquals(expectedResult.getOriginalDeductionAmount(), actualResult.getOriginalDeductionAmount());
	}

	@Test
	public void test_find_by_id_return_null() {
		BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();

		when(budgetJdbcRepository.findById(null)).thenReturn(entity);

		BudgetReAppropriation actualResult = budgetRepository.findById(getBudgetReAppropriationDomin());

		assertEquals(null, actualResult);
	}

	@Test
	public void test_save() {

		BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();
		BudgetReAppropriation expectedResult = entity.toDomain();

		when(budgetJdbcRepository.create(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

		BudgetReAppropriation actualResult = budgetRepository.save(getBudgetReAppropriationDomin());

		assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
		assertEquals(expectedResult.getAdditionAmount(), actualResult.getAdditionAmount());
		assertEquals(expectedResult.getDeductionAmount(), actualResult.getDeductionAmount());
		assertEquals(expectedResult.getOriginalAdditionAmount(), actualResult.getOriginalAdditionAmount());
		assertEquals(expectedResult.getOriginalDeductionAmount(), actualResult.getOriginalDeductionAmount());

	}

	@Test
	public void test_update() {

		BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();
		BudgetReAppropriation expectedResult = entity.toDomain();

		when(budgetJdbcRepository.update(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

		BudgetReAppropriation actualResult = budgetRepository.update(getBudgetReAppropriationDomin());

		assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
		assertEquals(expectedResult.getAdditionAmount(), actualResult.getAdditionAmount());
		assertEquals(expectedResult.getDeductionAmount(), actualResult.getDeductionAmount());
		assertEquals(expectedResult.getOriginalAdditionAmount(), actualResult.getOriginalAdditionAmount());
		assertEquals(expectedResult.getOriginalDeductionAmount(), actualResult.getOriginalDeductionAmount());

	}

	@Test
	public void test_search() {

		Pagination<BudgetReAppropriation> expectedResult = new Pagination<>();
		expectedResult.setPageSize(500);
		expectedResult.setOffset(0);

		when(budgetJdbcRepository.search(any(BudgetReAppropriationSearch.class))).thenReturn(expectedResult);

		Pagination<BudgetReAppropriation> actualResult = budgetRepository.search(getBudgetReAppropriationSearch());

		assertEquals(expectedResult, actualResult);

	}

	private BudgetReAppropriation getBudgetReAppropriationDomin() {
		BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation();
		budgetReAppropriation.setAnticipatoryAmount(BigDecimal.ONE);
		budgetReAppropriation.setAdditionAmount(BigDecimal.ONE);
		budgetReAppropriation.setDeductionAmount(BigDecimal.ONE);
		budgetReAppropriation.setOriginalAdditionAmount(BigDecimal.ONE);
		budgetReAppropriation.setOriginalDeductionAmount(BigDecimal.valueOf(1500));
		budgetReAppropriation.setTenantId("default");
		return budgetReAppropriation;
	}

	private BudgetReAppropriationEntity getBudgetReAppropriationEntity() {
		BudgetReAppropriationEntity entity = new BudgetReAppropriationEntity();
		entity.setAnticipatoryAmount(BigDecimal.ONE);
		entity.setAdditionAmount(BigDecimal.ONE);
		entity.setDeductionAmount(BigDecimal.ONE);
		entity.setOriginalAdditionAmount(BigDecimal.ONE);
		entity.setOriginalDeductionAmount(BigDecimal.valueOf(1500));
		entity.setTenantId("default");
		return entity;
	}

	private BudgetReAppropriationSearch getBudgetReAppropriationSearch() {
		BudgetReAppropriationSearch budgetSearch = new BudgetReAppropriationSearch();
		budgetSearch.setPageSize(500);
		budgetSearch.setOffset(0);
		return budgetSearch;

	}

}
