package org.egov.egf.budget.persistence.queue.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.egf.budget.persistence.queue.FinancialProducer;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetReAppropriationQueueRepositoryTest {

	@Mock
	private BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository;

	@Mock
	private FinancialProducer financialProducer;

	private static final String TOPIC_NAME = "topic";

	private static final String KEY_NAME = "key";

	@Before
	public void setup() {
		budgetReAppropriationQueueRepository = new BudgetReAppropriationQueueRepository(financialProducer, TOPIC_NAME, KEY_NAME,
				TOPIC_NAME, KEY_NAME);
	}

	@Test
	public void test_add_to_queue_while_create() {

		BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();

		request.setBudgetReAppropriations(getBudgetReAppropriations());
		request.setRequestInfo(new RequestInfo());
		request.getRequestInfo().setAction("create");

		budgetReAppropriationQueueRepository.addToQue(request);

		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

		assertEquals(request, actualRequest.get("budgetreappropriation_create"));

	}

	@Test
	public void test_add_to_queue_while_update() {

		BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();

		request.setBudgetReAppropriations(getBudgetReAppropriations());
		request.setRequestInfo(new RequestInfo());
		request.getRequestInfo().setAction("update");

		budgetReAppropriationQueueRepository.addToQue(request);

		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

		assertEquals(request, actualRequest.get("budgetreappropriation_update"));

	}

	@Test
	public void test_add_to_search_queue() {

		BudgetReAppropriationRequest request = new BudgetReAppropriationRequest();

		request.setBudgetReAppropriations(getBudgetReAppropriations());

		budgetReAppropriationQueueRepository.addToSearchQue(request);

		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

		assertEquals(request, actualRequest.get("budgetreappropriation_persisted"));

	}

	private List<BudgetReAppropriationContract> getBudgetReAppropriations() {

		List<BudgetReAppropriationContract> budgetReAppropriations = new ArrayList<BudgetReAppropriationContract>();

		BudgetReAppropriationContract budgetReAppropriation = BudgetReAppropriationContract.builder()
				.budgetDetail(BudgetDetailContract.builder().id("1").build()).additionAmount(BigDecimal.TEN)
				.deductionAmount(BigDecimal.TEN).originalAdditionAmount(BigDecimal.TEN)
				.originalDeductionAmount(BigDecimal.TEN).anticipatoryAmount(BigDecimal.TEN).build();

		budgetReAppropriation.setTenantId("default");
		budgetReAppropriations.add(budgetReAppropriation);

		return budgetReAppropriations;
	}

}
