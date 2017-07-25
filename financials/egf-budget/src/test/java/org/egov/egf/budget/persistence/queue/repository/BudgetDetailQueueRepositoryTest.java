package org.egov.egf.budget.persistence.queue.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.common.web.contract.RequestInfo;
import org.egov.egf.budget.persistence.queue.FinancialProducer;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.budget.web.contract.BudgetDetailRequest;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetDetailQueueRepositoryTest {

	@Mock
	private BudgetDetailQueueRepository budgetDetailQueueRepository;

	@Mock
	private FinancialProducer financialProducer;

	private static final String TOPIC_NAME = "topic";

	private static final String KEY_NAME = "key";

	@Before
	public void setup() {
		budgetDetailQueueRepository = new BudgetDetailQueueRepository(financialProducer, TOPIC_NAME, KEY_NAME,
				TOPIC_NAME, KEY_NAME);
	}

	@Test
	public void test_add_to_queue_while_create() {

		BudgetDetailRequest request = new BudgetDetailRequest();

		request.setBudgetDetails(getBudgetDetails());
		request.setRequestInfo(new RequestInfo());
		request.getRequestInfo().setAction("create");

		budgetDetailQueueRepository.addToQue(request);

		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

		assertEquals(request, actualRequest.get("budgetdetail_create"));

	}

	@Test
	public void test_add_to_queue_while_update() {

		BudgetDetailRequest request = new BudgetDetailRequest();

		request.setBudgetDetails(getBudgetDetails());
		request.setRequestInfo(new RequestInfo());
		request.getRequestInfo().setAction("update");

		budgetDetailQueueRepository.addToQue(request);

		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

		assertEquals(request, actualRequest.get("budgetdetail_update"));

	}

	@Test
	public void test_add_to_search_queue() {

		BudgetDetailRequest request = new BudgetDetailRequest();

		request.setBudgetDetails(getBudgetDetails());

		budgetDetailQueueRepository.addToSearchQue(request);

		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

		assertEquals(request, actualRequest.get("budgetdetail_completed"));

	}

	private List<BudgetDetailContract> getBudgetDetails() {

		List<BudgetDetailContract> budgetDetails = new ArrayList<BudgetDetailContract>();

		BudgetDetailContract budgetDetail = BudgetDetailContract.builder()
				.budget(BudgetContract.builder().id("1").build())
				.budgetGroup(BudgetGroupContract.builder().id("1").build()).anticipatoryAmount(BigDecimal.TEN)
				.originalAmount(BigDecimal.TEN).approvedAmount(BigDecimal.TEN).budgetAvailable(BigDecimal.TEN)
				.planningPercent(BigDecimal.valueOf(1500)).build();

		budgetDetail.setTenantId("default");
		budgetDetails.add(budgetDetail);

		return budgetDetails;
	}

}
