package org.egov.egf.budget.persistence.queue;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.common.web.contract.CommonRequest;
import org.egov.common.web.contract.RequestInfo;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BudgetServiceQueueRepositoryTest {

	@Mock
	private BudgetServiceQueueRepository budgetServiceQueueRepository;

	@Mock
	private FinancialProducer financialProducer;

	private static final String TOPIC_NAME = "topic";

	private static final String KEY_NAME = "key";

	@Before
	public void setup() {
		budgetServiceQueueRepository = new BudgetServiceQueueRepository(financialProducer, TOPIC_NAME, KEY_NAME,
				TOPIC_NAME, KEY_NAME);
	}

	@Test
	public void test_add_to_queue_while_create() {

		CommonRequest<BudgetContract> request = new CommonRequest<BudgetContract>();

		request.setData(getBudgetContracts());
		request.setRequestInfo(new RequestInfo());
		request.getRequestInfo().setAction("create");

		budgetServiceQueueRepository.addToQue(request);

		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

		assertEquals(request, actualRequest.get("budgetcontract_create"));

	}

	@Test
	public void test_add_to_queue_while_update() {

		CommonRequest<BudgetContract> request = new CommonRequest<BudgetContract>();

		request.setData(getBudgetContracts());
		request.setRequestInfo(new RequestInfo());
		request.getRequestInfo().setAction("update");

		budgetServiceQueueRepository.addToQue(request);

		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

		assertEquals(request, actualRequest.get("budgetcontract_update"));

	}

	@Test
	public void test_add_to_search_queue() {

		CommonRequest<BudgetContract> request = new CommonRequest<BudgetContract>();

		request.setData(getBudgetContracts());

		budgetServiceQueueRepository.addToSearchQue(request);

		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);

		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());

		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();

		assertEquals(request, actualRequest.get("budgetcontract_completed"));

	}

	private List<BudgetContract> getBudgetContracts() {
		List<BudgetContract> budgetContracts = new ArrayList<BudgetContract>();
		BudgetContract budgetContract = BudgetContract.builder().name("test")
				.financialYear(FinancialYearContract.builder().finYearRange("2017-18").build())
				.estimationType(EstimationType.BE).primaryBudget(false).build();
		budgetContract.setTenantId("default");
		budgetContracts.add(budgetContract);
		return budgetContracts;
	}

}
