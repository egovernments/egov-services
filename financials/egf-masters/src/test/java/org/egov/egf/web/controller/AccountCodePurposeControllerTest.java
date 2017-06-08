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

import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractRequest;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeGetRequest;
import org.egov.egf.persistence.service.AccountCodePurposeService;
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
@WebMvcTest(AccountCodePurposeController.class)
public class AccountCodePurposeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountCodePurposeService accountCodePurposeService;

	private Resources resources = new Resources();

	@Test
	public void testCreate() throws IOException, Exception {

		mockMvc.perform(post("/accountcodepurposes/_create")
				.content(resources.getFileContents("accountcodepurpose/accountCodePurposeContractCreateRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.getFileContents("accountcodepurpose/accountCodePurposeContractCreateResponse.json")));
		final ArgumentCaptor<AccountCodePurposeContractRequest> argumentCaptor = ArgumentCaptor
				.forClass(AccountCodePurposeContractRequest.class);

		verify(accountCodePurposeService).validate(argumentCaptor.capture(), any(String.class),
				any(BindingResult.class));
		verify(accountCodePurposeService).fetchRelatedContracts(argumentCaptor.capture());
		verify(accountCodePurposeService).push(argumentCaptor.capture());

		final AccountCodePurposeContractRequest actualRequest = argumentCaptor.getValue();
		assertEquals("accountCodePurpose", actualRequest.getAccountCodePurposes().get(0).getName());
		assertEquals("default", actualRequest.getAccountCodePurposes().get(0).getTenantId());
	}

	@Test
	public void testUpdate() throws IOException, Exception {

		mockMvc.perform(post("/accountcodepurposes/1/_update")
				.content(resources.getFileContents("accountcodepurpose/accountCodePurposeContractUpdateRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.getFileContents("accountcodepurpose/accountCodePurposeContractUpdateResponse.json")));

		final ArgumentCaptor<AccountCodePurposeContractRequest> argumentCaptor = ArgumentCaptor
				.forClass(AccountCodePurposeContractRequest.class);

		verify(accountCodePurposeService).validate(argumentCaptor.capture(), any(String.class),
				any(BindingResult.class));
		verify(accountCodePurposeService).fetchRelatedContracts(argumentCaptor.capture());
		verify(accountCodePurposeService).push(argumentCaptor.capture());

		final AccountCodePurposeContractRequest actualRequest = argumentCaptor.getValue();
		assertEquals("accountCodePurposeU", actualRequest.getAccountCodePurpose().getName());
		assertEquals("default", actualRequest.getAccountCodePurpose().getTenantId());
	}

	@Test
	public void testSearch() throws IOException, Exception {

		final ArgumentCaptor<AccountCodePurposeGetRequest> argumentCaptor = ArgumentCaptor
				.forClass(AccountCodePurposeGetRequest.class);

		final ArgumentCaptor<AccountCodePurposeContractRequest> argumentCaptor1 = ArgumentCaptor
				.forClass(AccountCodePurposeContractRequest.class);

		when(accountCodePurposeService.getAccountCodePurposes(argumentCaptor.capture()))
				.thenReturn(getAccountCodePurpose());
		mockMvc.perform(post("/accountcodepurposes/_search?name=test")
				.content(resources.getFileContents("accountcodepurpose/accountCodePurposeContractSearchRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().json(resources.getFileContents("accountcodepurpose/accountCodePurposeContractSearchResponse.json")));

		verify(accountCodePurposeService).validate(argumentCaptor1.capture(), any(String.class),
				any(BindingResult.class));
		verify(accountCodePurposeService).fetchRelatedContracts(argumentCaptor1.capture());

		final AccountCodePurposeGetRequest actualRequest = argumentCaptor.getValue();
		assertEquals("test", actualRequest.getName());

		final AccountCodePurposeContractRequest actualRequest1 = argumentCaptor1.getValue();
		assertEquals("action", actualRequest1.getRequestInfo().getAction());
		assertEquals("did", actualRequest1.getRequestInfo().getDid());
		assertEquals("msgId", actualRequest1.getRequestInfo().getMsgId());
		assertEquals("requesterId", actualRequest1.getRequestInfo().getRequesterId());
	}

	@Test
	public void testUpdateAll() throws IOException, Exception {

		mockMvc.perform(post("/accountcodepurposes/_update")
				.content(resources.getFileContents("accountcodepurpose/accountCodePurposeContractUpdateAllRequest.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
						content().json(resources.getFileContents("accountcodepurpose/accountCodePurposeContractUpdateAllResponse.json")));
		final ArgumentCaptor<AccountCodePurposeContractRequest> argumentCaptor = ArgumentCaptor
				.forClass(AccountCodePurposeContractRequest.class);

		verify(accountCodePurposeService).validate(argumentCaptor.capture(), any(String.class),
				any(BindingResult.class));
		verify(accountCodePurposeService).fetchRelatedContracts(argumentCaptor.capture());
		verify(accountCodePurposeService).push(argumentCaptor.capture());

		final AccountCodePurposeContractRequest actualRequest = argumentCaptor.getValue();
		assertEquals("accountCodePurposeUU", actualRequest.getAccountCodePurposes().get(0).getName());
		assertEquals("default", actualRequest.getAccountCodePurposes().get(0).getTenantId());
	}

	private List<AccountCodePurpose> getAccountCodePurpose() {
		List<AccountCodePurpose> accountCodePurposeList = new ArrayList<AccountCodePurpose>();
		AccountCodePurpose accountCodePurpose = new AccountCodePurpose();
		accountCodePurpose.setName("accountCodePurpose1");
		accountCodePurposeList.add(accountCodePurpose);
		return accountCodePurposeList;
	}

}