package org.egov.egf.budget.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.persistence.entity.BudgetReAppropriationEntity;
import org.egov.egf.budget.persistence.queue.repository.BudgetReAppropriationQueueRepository;
import org.egov.egf.budget.persistence.repository.BudgetReAppropriationJdbcRepository;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetReAppropriationRepositoryTest {

	@Mock
	private BudgetReAppropriationJdbcRepository budgetReAppropriationJdbcRepository;

	private BudgetReAppropriationRepository budgetReAppropriationRepositoryWithKafka;

	private BudgetReAppropriationRepository budgetReAppropriationRepositoryWithOutKafka;

	@Mock
	private BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository;

	@Captor
	private ArgumentCaptor<BudgetReAppropriationRequest> captor;

	private RequestInfo requestInfo = new RequestInfo();

	@Before
	public void setup() {
		budgetReAppropriationRepositoryWithKafka = new BudgetReAppropriationRepository(
				budgetReAppropriationJdbcRepository, budgetReAppropriationQueueRepository, "yes");

		budgetReAppropriationRepositoryWithOutKafka = new BudgetReAppropriationRepository(
				budgetReAppropriationJdbcRepository, budgetReAppropriationQueueRepository, "no");
	}

	@Test
	public void test_find_by_id() {
		BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();
		BudgetReAppropriation expectedResult = entity.toDomain();

		when(budgetReAppropriationJdbcRepository.findById(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

		BudgetReAppropriation actualResult = budgetReAppropriationRepositoryWithKafka
				.findById(getBudgetReAppropriationDomin());

		assertEquals(expectedResult.getAnticipatoryAmount(), actualResult.getAnticipatoryAmount());
		assertEquals(expectedResult.getAdditionAmount(), actualResult.getAdditionAmount());
		assertEquals(expectedResult.getDeductionAmount(), actualResult.getDeductionAmount());
		assertEquals(expectedResult.getOriginalAdditionAmount(), actualResult.getOriginalAdditionAmount());
		assertEquals(expectedResult.getOriginalDeductionAmount(), actualResult.getOriginalDeductionAmount());
	}

	@Test
	public void test_find_by_id_return_null() {
		BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();

		when(budgetReAppropriationJdbcRepository.findById(null)).thenReturn(entity);

		BudgetReAppropriation actualResult = budgetReAppropriationRepositoryWithKafka
				.findById(getBudgetReAppropriationDomin());

		assertEquals(null, actualResult);
	}

	@Test
	public void test_save_with_kafka() {

		List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

		budgetReAppropriationRepositoryWithKafka.save(expectedResult, requestInfo);

		verify(budgetReAppropriationQueueRepository).addToQue(captor.capture());

		final BudgetReAppropriationRequest actualRequest = captor.getValue();

		assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
		assertEquals(expectedResult.get(0).getAdditionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
		assertEquals(expectedResult.get(0).getDeductionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
		assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
		assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

	}

	@Test
	public void test_save_with_out_kafka() {

		List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

		BudgetReAppropriationEntity entity = new BudgetReAppropriationEntity().toEntity(expectedResult.get(0));

		when(budgetReAppropriationJdbcRepository.create(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

		budgetReAppropriationRepositoryWithOutKafka.save(expectedResult, requestInfo);

		verify(budgetReAppropriationQueueRepository).addToSearchQue(captor.capture());

		final BudgetReAppropriationRequest actualRequest = captor.getValue();

		assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
		assertEquals(expectedResult.get(0).getAdditionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
		assertEquals(expectedResult.get(0).getDeductionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
		assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
		assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

	}

	@Test
	public void test_update_with_kafka() {

		List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

		budgetReAppropriationRepositoryWithKafka.update(expectedResult, requestInfo);

		verify(budgetReAppropriationQueueRepository).addToQue(captor.capture());

		final BudgetReAppropriationRequest actualRequest = captor.getValue();

		assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
		assertEquals(expectedResult.get(0).getAdditionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
		assertEquals(expectedResult.get(0).getDeductionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
		assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
		assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

	}

	@Test
	public void test_update_with_out_kafka() {

		List<BudgetReAppropriation> expectedResult = getBudgetReAppropriations();

		BudgetReAppropriationEntity entity = new BudgetReAppropriationEntity().toEntity(expectedResult.get(0));

		when(budgetReAppropriationJdbcRepository.update(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

		budgetReAppropriationRepositoryWithOutKafka.update(expectedResult, requestInfo);

		verify(budgetReAppropriationQueueRepository).addToSearchQue(captor.capture());

		final BudgetReAppropriationRequest actualRequest = captor.getValue();

		assertEquals(expectedResult.get(0).getAnticipatoryAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
		assertEquals(expectedResult.get(0).getAdditionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
		assertEquals(expectedResult.get(0).getDeductionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
		assertEquals(expectedResult.get(0).getOriginalAdditionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
		assertEquals(expectedResult.get(0).getOriginalDeductionAmount(),
				actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());

	}

	@Test
	public void test_save() {

		BudgetReAppropriationEntity entity = getBudgetReAppropriationEntity();
		BudgetReAppropriation expectedResult = entity.toDomain();

		when(budgetReAppropriationJdbcRepository.create(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

		BudgetReAppropriation actualResult = budgetReAppropriationRepositoryWithKafka
				.save(getBudgetReAppropriationDomin());

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

		when(budgetReAppropriationJdbcRepository.update(any(BudgetReAppropriationEntity.class))).thenReturn(entity);

		BudgetReAppropriation actualResult = budgetReAppropriationRepositoryWithKafka
				.update(getBudgetReAppropriationDomin());

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

		when(budgetReAppropriationJdbcRepository.search(any(BudgetReAppropriationSearch.class)))
				.thenReturn(expectedResult);

		Pagination<BudgetReAppropriation> actualResult = budgetReAppropriationRepositoryWithKafka
				.search(getBudgetReAppropriationSearch());

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
