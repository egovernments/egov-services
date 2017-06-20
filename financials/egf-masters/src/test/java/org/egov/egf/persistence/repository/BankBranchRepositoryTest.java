package org.egov.egf.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.persistence.queue.contract.BankBranchGetRequest;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.User;
import org.egov.egf.persistence.repository.builder.BankBranchQueryBuilder;
import org.egov.egf.persistence.repository.rowmapper.BankBranchRowMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class BankBranchRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private BankBranchRowMapper bankBranchRowMapper;

	@Mock
	private BankBranchQueryBuilder bankBranchQueryBuilder;

	@Mock
	private BankBranchRepository bankBranchRepository;

	private final String BANKNAME = "bankName";
	private final String BANKCODE = "bankCode";
	private final String BRANCHNAME = "branchName";
	private final String BRANCHCODE = "branchCode";
	private final String TENANT_ID = "tenantId";

	@Before
	public void before() {
		bankBranchRepository = new BankBranchRepository(jdbcTemplate, bankBranchRowMapper, bankBranchQueryBuilder);
	}

	@Test
	public void test_should_create_bankbranch_for_single_object() {

		String seq_query = "SELECT nextval (?) as nextval";
		final List<Object> preparedStatementValues = new ArrayList<>();
		preparedStatementValues.add("seq_egf_bankbranch");
		BankBranchContractRequest bankBranchContractRequest = new BankBranchContractRequest();
		bankBranchContractRequest.setRequestInfo(getRequestInfo());
		bankBranchContractRequest.getRequestInfo().setAction("create");
		bankBranchContractRequest.setBankBranch(getBankBranchContract());

		when(jdbcTemplate.queryForObject(seq_query, preparedStatementValues.toArray(), Long.class)).thenReturn(1l);

		BankBranchContractRequest response = bankBranchRepository.create(bankBranchContractRequest);

		verify(jdbcTemplate).batchUpdate(any(String.class), any(List.class));

		assertEquals(Long.valueOf(1), response.getBankBranch().getId());
		assertEquals(BRANCHNAME, response.getBankBranch().getName());
		assertEquals(BRANCHCODE, response.getBankBranch().getCode());

	}

	@Test
	public void test_should_create_bankbranch_for_list_of_objects() {

		String seq_query = "SELECT nextval (?) as nextval";
		final List<Object> preparedStatementValues = new ArrayList<>();
		preparedStatementValues.add("seq_egf_bankbranch");
		BankBranchContractRequest bankBranchContractRequest = new BankBranchContractRequest();
		bankBranchContractRequest.setRequestInfo(getRequestInfo());
		bankBranchContractRequest.getRequestInfo().setAction("create");
		bankBranchContractRequest.getBankBranches().add((getBankBranchContract()));

		when(jdbcTemplate.queryForObject(seq_query, preparedStatementValues.toArray(), Long.class)).thenReturn(1l);

		BankBranchContractRequest response = bankBranchRepository.create(bankBranchContractRequest);

		verify(jdbcTemplate).batchUpdate(any(String.class), any(List.class));

		assertEquals(Long.valueOf(1), response.getBankBranches().get(0).getId());
		assertEquals(BRANCHNAME, response.getBankBranches().get(0).getName());
		assertEquals(BRANCHCODE, response.getBankBranches().get(0).getCode());

	}

	@Test
	public void test_should_update_bankbranch_for_single_object() {

		BankBranchContractRequest bankBranchContractRequest = new BankBranchContractRequest();
		bankBranchContractRequest.setRequestInfo(getRequestInfo());
		bankBranchContractRequest.getRequestInfo().setAction("update");
		bankBranchContractRequest.setBankBranch(getBankBranchContract());
		bankBranchContractRequest.getBankBranch().setId(1l);

		BankBranchContractRequest response = bankBranchRepository.update(bankBranchContractRequest);

		verify(jdbcTemplate).batchUpdate(any(String.class), any(List.class));

		assertEquals(Long.valueOf(1), response.getBankBranch().getId());
		assertEquals(BRANCHNAME, response.getBankBranch().getName());
		assertEquals(BRANCHCODE, response.getBankBranch().getCode());

	}

	@Test
	public void test_should_update_bankbranch_for_list_of_objects() {

		BankBranchContractRequest bankBranchContractRequest = new BankBranchContractRequest();
		bankBranchContractRequest.setRequestInfo(getRequestInfo());
		bankBranchContractRequest.getRequestInfo().setAction("update");
		bankBranchContractRequest.getBankBranches().add((getBankBranchContract()));
		bankBranchContractRequest.getBankBranches().get(0).setId(1l);

		BankBranchContractRequest response = bankBranchRepository.update(bankBranchContractRequest);

		verify(jdbcTemplate).batchUpdate(any(String.class), any(List.class));

		assertEquals(Long.valueOf(1), response.getBankBranches().get(0).getId());
		assertEquals(BRANCHNAME, response.getBankBranches().get(0).getName());
		assertEquals(BRANCHCODE, response.getBankBranches().get(0).getCode());

	}

	@Test
	public void test_should_return_bankbranchs_for_criteria() {

		List<BankBranchContract> expectedResponse = new ArrayList<BankBranchContract>();
		expectedResponse.add(getBankBranchContract());
		BankBranchGetRequest bankBranchGetRequest = new BankBranchGetRequest();
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = "";
		when(bankBranchQueryBuilder.getQuery(bankBranchGetRequest, preparedStatementValues)).thenReturn(queryStr);
		when(jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), bankBranchRowMapper))
				.thenReturn(expectedResponse);
		List<BankBranchContract> actualResponse = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				bankBranchRowMapper);

		assertEquals(actualResponse.size(), expectedResponse.size());

	}

	private RequestInfo getRequestInfo() {
		return RequestInfo.builder().action("create").apiId("apiId").did("did").key("key").msgId("msgId")
				.requesterId("requesterId").tenantId(TENANT_ID).ts(new Date())
				.userInfo(User.builder().id(1l).userName("userName").build()).build();
	}

	private BankBranchContract getBankBranchContract() {
		return BankBranchContract.builder().name(BRANCHNAME).code(BRANCHCODE).active(true)
				.bank(BankContract.builder().name(BANKNAME).code(BANKCODE).active(true).build()).build();
	}

}