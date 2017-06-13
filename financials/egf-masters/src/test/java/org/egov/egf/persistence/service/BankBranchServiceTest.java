package org.egov.egf.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.persistence.queue.contract.BankBranchGetRequest;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.queue.contract.User;
import org.egov.egf.persistence.repository.BankBranchQueueRepository;
import org.egov.egf.persistence.repository.BankBranchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

@RunWith(MockitoJUnitRunner.class)
public class BankBranchServiceTest {

	@Mock
	private SmartValidator validator;

	@Mock
	private BankService bankService;

	private BankBranchService bankBranchService;

	@Mock
	private BankBranchRepository bankBranchRepository;

	@Mock
	private BankBranchQueueRepository bankBranchQueueRepository;

	private final String BANKNAME = "bankName";
	private final String BANKCODE = "bankCode";
	private final String BRANCHNAME = "branchName";
	private final String BRANCHCODE = "branchCode";
	private final String TENANT_ID = "tenantId";
	private final String BANKBRANCHCREATE = "BankBranchCreate";
	private final String BANKBRANCHUPDATE = "BankBranchUpdate";

	@Before
	public void before() {
		bankBranchService = new BankBranchService(bankBranchRepository, bankBranchQueueRepository,bankService,validator);
	}

	@Test
	public void test_bankbranch_service_should_call_queue_repository_to_push_the_request() {

		BankBranchContractRequest bankBranchContractRequest = new BankBranchContractRequest();
		bankBranchService.push(bankBranchContractRequest);
		verify(bankBranchQueueRepository).push(bankBranchContractRequest);

	}

	@Test
	public void test_should_create_a_valid_list_of_bankbranch() {

		HashMap<String, Object> financialContractRequestMap = new HashMap<String, Object>();
		BankBranchContractRequest request = new BankBranchContractRequest();
		request.setRequestInfo(getRequestInfo());
		request.getBankBranches().add(getBankBranchContract());
		BankBranchContractRequest expectedResponse = new BankBranchContractRequest();
		expectedResponse.setRequestInfo(getRequestInfo());
		BankBranchContract contract = getBankBranchContract();
		contract.setId(1L);
		contract.getBank().setId(1L);
		expectedResponse.getBankBranches().add(contract);
		financialContractRequestMap.put(BANKBRANCHCREATE, request);

		when(bankBranchRepository.create(request)).thenReturn(expectedResponse);

		BankBranchContractResponse actualResponse = bankBranchService.create(financialContractRequestMap);

		assertEquals(getResponseInfo(request.getRequestInfo()).getTenantId(),
				actualResponse.getResponseInfo().getTenantId());

		assertEquals(expectedResponse.getBankBranches(), actualResponse.getBankBranches());
	}

	@Test
	public void test_should_create_a_valid_single_bankbranch() {

		HashMap<String, Object> financialContractRequestMap = new HashMap<String, Object>();
		BankBranchContractRequest request = new BankBranchContractRequest();
		request.setRequestInfo(getRequestInfo());
		request.setBankBranch(getBankBranchContract());
		BankBranchContractRequest expectedResponse = new BankBranchContractRequest();
		expectedResponse.setRequestInfo(getRequestInfo());
		BankBranchContract contract = getBankBranchContract();
		contract.setId(1L);
		contract.getBank().setId(1L);
		expectedResponse.setBankBranch(contract);
		financialContractRequestMap.put(BANKBRANCHCREATE, request);

		when(bankBranchRepository.create(request)).thenReturn(expectedResponse);

		BankBranchContractResponse actualResponse = bankBranchService.create(financialContractRequestMap);

		assertEquals(getResponseInfo(request.getRequestInfo()).getTenantId(),
				actualResponse.getResponseInfo().getTenantId());

		assertEquals(expectedResponse.getBankBranch(), actualResponse.getBankBranch());
	}

	@Test
	public void test_should_update_a_valid_list_of_bankbranch() {

		HashMap<String, Object> financialContractRequestMap = new HashMap<String, Object>();
		BankBranchContractRequest request = new BankBranchContractRequest();
		request.setRequestInfo(getRequestInfo());
		BankBranchContract contract = getBankBranchContract();
		contract.setId(1L);
		contract.getBank().setId(1L);
		request.getBankBranches().add(contract);
		BankBranchContractRequest expectedResponse = new BankBranchContractRequest();
		expectedResponse.setRequestInfo(getRequestInfo());
		expectedResponse.getBankBranches().add(contract);
		financialContractRequestMap.put(BANKBRANCHUPDATE, request);

		when(bankBranchRepository.update(request)).thenReturn(expectedResponse);

		BankBranchContractResponse actualResponse = bankBranchService.update(financialContractRequestMap);

		assertEquals(getResponseInfo(request.getRequestInfo()).getTenantId(),
				actualResponse.getResponseInfo().getTenantId());

		assertEquals(expectedResponse.getBankBranches(), actualResponse.getBankBranches());
	}

	@Test
	public void test_should_update_a_valid_single_bankbranch() {

		HashMap<String, Object> financialContractRequestMap = new HashMap<String, Object>();
		BankBranchContractRequest request = new BankBranchContractRequest();
		request.setRequestInfo(getRequestInfo());
		BankBranchContract contract = getBankBranchContract();
		contract.setId(1L);
		contract.getBank().setId(1L);
		request.setBankBranch(contract);
		BankBranchContractRequest expectedResponse = new BankBranchContractRequest();
		expectedResponse.setRequestInfo(getRequestInfo());
		expectedResponse.setBankBranch(contract);
		financialContractRequestMap.put(BANKBRANCHUPDATE, request);

		when(bankBranchRepository.update(request)).thenReturn(expectedResponse);

		BankBranchContractResponse actualResponse = bankBranchService.update(financialContractRequestMap);

		assertEquals(getResponseInfo(request.getRequestInfo()).getTenantId(),
				actualResponse.getResponseInfo().getTenantId());

		assertEquals(expectedResponse.getBankBranch(), actualResponse.getBankBranch());
	}

	@Test
	public void test_should_get_bank_branches_for_given_criteria() {

		BankBranchGetRequest request = BankBranchGetRequest.builder().tenantId(TENANT_ID).build();
		List<BankBranchContract> expectedResponse = new ArrayList<>();
		expectedResponse.add(getBankBranchContract());

		when(bankBranchRepository.findForCriteria(request)).thenReturn(expectedResponse);

		List<BankBranchContract> actualResponse = bankBranchService.getBankBranches(request);

		assertEquals(expectedResponse.size(), actualResponse.size());
		assertEquals(expectedResponse.get(0), actualResponse.get(0));
	}

	@Test
	public void test_should_fail_while_create_for_invalid_input() {

		BankBranchContractRequest request = new BankBranchContractRequest();
		request.setRequestInfo(getRequestInfo());
		request.setBankBranches(null);
		BindingResult errors = new BeanPropertyBindingResult(request, "BankBranchContractRequest");
		bankBranchService.validate(request, "create", errors);
		assertEquals(errors.getAllErrors().get(0).getDefaultMessage(), "BankBranches to create must not be null");
	}
	
	@Test
	public void test_should_fail_while_update_for_invalid_input() {

		BankBranchContractRequest request = new BankBranchContractRequest();
		request.setRequestInfo(getRequestInfo());
		request.setBankBranch(null);
		BindingResult errors = new BeanPropertyBindingResult(request, "BankBranchContractRequest");
		bankBranchService.validate(request, "update", errors);
		assertEquals(errors.getAllErrors().get(0).getDefaultMessage(), "BankBranch to edit must not be null");
	}

	@Test
	public void test_should_fail_while_updateall_for_invalid_input() {

		BankBranchContractRequest request = new BankBranchContractRequest();
		request.setRequestInfo(getRequestInfo());
		request.setBankBranches(null);
		BindingResult errors = new BeanPropertyBindingResult(request, "BankBranchContractRequest");
		bankBranchService.validate(request, "updateAll", errors);
		assertEquals(errors.getAllErrors().get(0).getDefaultMessage(), "BankBranches to update must not be null");
	}

	@Test
	public void test_should_fetch_related_contracts_for_given_request() {

		BankBranchContractRequest request = new BankBranchContractRequest();
		request.setRequestInfo(getRequestInfo());
		BankBranchContract contract = getBankBranchContract();
		contract.setId(1L);
		contract.getBank().setId(1L);
		request.setBankBranch(contract);
		request.getBankBranches().add(contract);
		Bank expectedResponse = getBank();

		when(bankService.findOne(1L)).thenReturn(expectedResponse);

		BankBranchContractRequest actualResponse = bankBranchService.fetchRelatedContracts(request);

		assertEquals(actualResponse.getBankBranch().getBank().getName(), BANKNAME);
		assertEquals(actualResponse.getBankBranch().getBank().getCode(), BANKCODE);
		assertEquals(actualResponse.getBankBranches().get(0).getBank().getName(), BANKNAME);
		assertEquals(actualResponse.getBankBranches().get(0).getBank().getCode(), BANKCODE);

	}

	private Bank getBank() {
		return Bank.builder().id(1L).name(BANKNAME).code(BANKCODE).active(true).build();
	}

	private RequestInfo getRequestInfo() {
		return RequestInfo.builder().action("create").apiId("apiId").did("did").key("key").msgId("msgId")
				.requesterId("requesterId").tenantId(TENANT_ID).ts(new Date())
				.userInfo(User.builder().userName("userName").build()).build();
	}

	private BankBranchContract getBankBranchContract() {
		return BankBranchContract.builder().name(BRANCHNAME).code(BRANCHCODE).active(true)
				.bank(BankContract.builder().name(BANKNAME).code(BANKCODE).active(true).build()).build();
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}