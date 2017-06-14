package org.egov.egf.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.User;
import org.egov.egf.producer.FinancialProducer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BankBranchQueueRepositoryTest {

	@Mock
	private FinancialProducer financialProducer;
	
	private static final String TOPIC_NAME = "topic";
	
	private static final String KEY_NAME = "key";

	@Mock
	private BankBranchQueueRepository bankBranchQueueRepository;

	@Before
	public void before() {
		bankBranchQueueRepository = new BankBranchQueueRepository(financialProducer, TOPIC_NAME,
				KEY_NAME);
	}

	@Test
	public void test_should_call_financialproducer_sendmessage_api_while_create() {

		BankBranchContractRequest bankBranchContractRequest = new BankBranchContractRequest();
		bankBranchContractRequest.setRequestInfo(getRequestInfo());
		bankBranchContractRequest.getRequestInfo().setAction("create");
		bankBranchQueueRepository.push(bankBranchContractRequest);
		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);
		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());
		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();
		assertEquals(bankBranchContractRequest, actualRequest.get("BankBranchCreate"));

	}

	@Test
	public void test_should_call_financialproducer_sendmessage_api_while_update() {

		BankBranchContractRequest bankBranchContractRequest = new BankBranchContractRequest();
		bankBranchContractRequest.setRequestInfo(getRequestInfo());
		bankBranchContractRequest.getRequestInfo().setAction("update");
		bankBranchQueueRepository.push(bankBranchContractRequest);
		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);
		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());
		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();
		assertEquals(bankBranchContractRequest, actualRequest.get("BankBranchUpdate"));

	}

	@Test
	public void test_should_call_financialproducer_sendmessage_api_while_updateall() {

		BankBranchContractRequest bankBranchContractRequest = new BankBranchContractRequest();
		bankBranchContractRequest.setRequestInfo(getRequestInfo());
		bankBranchContractRequest.getRequestInfo().setAction("updateAll");
		bankBranchQueueRepository.push(bankBranchContractRequest);
		final ArgumentCaptor<HashMap> argumentCaptor = ArgumentCaptor.forClass(HashMap.class);
		verify(financialProducer).sendMessage(any(String.class), any(String.class), argumentCaptor.capture());
		final HashMap<String, Object> actualRequest = argumentCaptor.getValue();
		assertEquals(bankBranchContractRequest, actualRequest.get("BankBranchUpdate"));

	}

	private RequestInfo getRequestInfo() {
		return RequestInfo.builder().action("create").apiId("apiId").did("did").key("key").msgId("msgId")
				.requesterId("requesterId").tenantId("tenantId").ts(new Date())
				.userInfo(User.builder().userName("userName").build()).build();
	}

}