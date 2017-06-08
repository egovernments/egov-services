package org.egov.egf.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.persistence.queue.contract.BankBranchGetRequest;
import org.egov.egf.persistence.service.BankBranchService;
import org.egov.egf.utils.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(BankBranchController.class)
public class BankBranchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BankBranchService bankBranchService;

	private Resources resources = new Resources();

	@Test
	public void testCreate() throws IOException, Exception {

		mockMvc.perform(post("/bankbranches/_create")
				.content(resources.getFileContents("bankbranch/bankBranchContractCreateRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.getFileContents("bankbranch/bankBranchContractCreateResponse.json")));
		final ArgumentCaptor<BankBranchContractRequest> argumentCaptor = ArgumentCaptor
				.forClass(BankBranchContractRequest.class);

		verify(bankBranchService).validate(argumentCaptor.capture(), any(String.class), any(BindingResult.class));
		verify(bankBranchService).fetchRelatedContracts(argumentCaptor.capture());
		verify(bankBranchService).push(argumentCaptor.capture());

		final BankBranchContractRequest actualRequest = argumentCaptor.getValue();
		assertEquals("name", actualRequest.getBankBranches().get(0).getName());
		assertEquals("code", actualRequest.getBankBranches().get(0).getCode());
		assertEquals(Long.valueOf(1), actualRequest.getBankBranches().get(0).getBank().getId());
		assertEquals("default", actualRequest.getBankBranches().get(0).getTenantId());
	}

	@Test
	public void testUpdate() throws IOException, Exception {

		mockMvc.perform(post("/bankbranches/1/_update")
				.content(resources.getFileContents("bankbranch/bankBranchContractUpdateRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.getFileContents("bankbranch/bankBranchContractUpdateResponse.json")));

		final ArgumentCaptor<BankBranchContractRequest> argumentCaptor = ArgumentCaptor
				.forClass(BankBranchContractRequest.class);

		verify(bankBranchService).validate(argumentCaptor.capture(), any(String.class), any(BindingResult.class));
		verify(bankBranchService).fetchRelatedContracts(argumentCaptor.capture());
		verify(bankBranchService).push(argumentCaptor.capture());

		final BankBranchContractRequest actualRequest = argumentCaptor.getValue();
		assertEquals("nameU", actualRequest.getBankBranch().getName());
		assertEquals("codeU", actualRequest.getBankBranch().getCode());
		assertEquals(Long.valueOf(1), actualRequest.getBankBranch().getBank().getId());
		assertEquals("default", actualRequest.getBankBranch().getTenantId());
	}

	@Test
	public void testSearch() throws IOException, Exception {

		final ArgumentCaptor<BankBranchGetRequest> argumentCaptor = ArgumentCaptor.forClass(BankBranchGetRequest.class);

		final ArgumentCaptor<BankBranchContractRequest> argumentCaptor1 = ArgumentCaptor
				.forClass(BankBranchContractRequest.class);

		when(bankBranchService.getBankBranches(argumentCaptor.capture())).thenReturn(getBankBranch());
		mockMvc.perform(post("/bankbranches/_search?name=bankBranchName")
				.content(resources.getFileContents("bankbranch/bankBranchContractSearchRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().json(resources.getFileContents("bankbranch/bankBranchContractSearchResponse.json")));

		verify(bankBranchService).validate(argumentCaptor1.capture(), any(String.class), any(BindingResult.class));
		verify(bankBranchService).fetchRelatedContracts(argumentCaptor1.capture());

		final BankBranchGetRequest actualRequest = argumentCaptor.getValue();
		assertEquals("bankBranchName", actualRequest.getName());

		final BankBranchContractRequest actualRequest1 = argumentCaptor1.getValue();
		assertEquals("action", actualRequest1.getRequestInfo().getAction());
		assertEquals("did", actualRequest1.getRequestInfo().getDid());
		assertEquals("msgId", actualRequest1.getRequestInfo().getMsgId());
		assertEquals("requesterId", actualRequest1.getRequestInfo().getRequesterId());
	}

	@Test
	public void testUpdateAll() throws IOException, Exception {

		mockMvc.perform(post("/bankbranches/_update")
				.content(resources.getFileContents("bankbranch/bankBranchContractUpdateAllRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.getFileContents("bankbranch/bankBranchContractUpdateAllResponse.json")));
		final ArgumentCaptor<BankBranchContractRequest> argumentCaptor = ArgumentCaptor
				.forClass(BankBranchContractRequest.class);

		verify(bankBranchService).validate(argumentCaptor.capture(), any(String.class), any(BindingResult.class));
		verify(bankBranchService).fetchRelatedContracts(argumentCaptor.capture());
		verify(bankBranchService).push(argumentCaptor.capture());

		final BankBranchContractRequest actualRequest = argumentCaptor.getValue();
		assertEquals("nameU", actualRequest.getBankBranches().get(0).getName());
		assertEquals("codeU", actualRequest.getBankBranches().get(0).getCode());
		assertEquals(Long.valueOf(1), actualRequest.getBankBranches().get(0).getId());
		assertEquals(Long.valueOf(1), actualRequest.getBankBranches().get(0).getBank().getId());
		assertEquals("default", actualRequest.getBankBranches().get(0).getTenantId());
	}

	private List<BankBranchContract> getBankBranch() {
		List<BankBranchContract> bankBranchList = new ArrayList<BankBranchContract>();
		BankBranchContract bankBranch = new BankBranchContract();
		bankBranch.setName("bankBranchName");
		bankBranch.setCode("bankBranchCode");
		bankBranchList.add(bankBranch);
		return bankBranchList;
	}

}