package org.egov.egf.master.web.controller;

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

import org.egov.common.domain.model.Pagination;
import org.egov.common.utils.RequestJsonReader;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.TestConfiguration;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.FundSearch;
import org.egov.egf.master.domain.service.FundService;
import org.egov.egf.master.web.contract.FundContract;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(FundController.class)
@Import(TestConfiguration.class)
public class FundControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FundService fundService;

	private RequestJsonReader resources = new RequestJsonReader();

	@Captor
	private ArgumentCaptor<CommonRequest<FundContract>> captor;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() throws IOException, Exception {

		when(fundService.add(any(List.class), any(BindingResult.class))).thenReturn((getFunds()));

		mockMvc.perform(post("/funds/_create").content(resources.readRequest("fund/fund_create_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("fund/fund_create_valid_response.json")));

		verify(fundService).addToQue(captor.capture());

		final CommonRequest<FundContract> actualRequest = captor.getValue();
		assertEquals("MunicipalFund", actualRequest.getData().get(0).getName());
		assertEquals("001", actualRequest.getData().get(0).getCode());
		assertEquals(Character.valueOf('M'), actualRequest.getData().get(0).getIdentifier());
		assertEquals("default", actualRequest.getData().get(0).getTenantId());
	}

	@Test
	public void testCreate_Error() throws IOException, Exception {

		when(fundService.add(any(List.class), any(BindingResult.class))).thenReturn((getFunds()));

		mockMvc.perform(
				post("/funds/_create").content(resources.readRequest("fund/fund_create_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is5xxServerError());

	}

	/*
	 * @Test public void testCreate_400() throws IOException, Exception {
	 * 
	 * /*
	 * 
	 * @Test public void testCreate_400() throws IOException, Exception {
	 * 
	 * BindingResult errors = new BeanPropertyBindingResult(null, null);
	 * errors.addError(new ObjectError("sample", "sample"));
	 * 
	 * CustomBindException customBindException = new
	 * CustomBindException(errors); when(fundService.add(any(List.class),
	 * any(BindingResult.class))).thenThrow(customBindException);
	 * 
	 * mockMvc.perform(post("/funds/_create").content(resources.readRequest(
	 * "fund/fund_create_valid_request.json"))
	 * .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().
	 * is4xxClientError())
	 * .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	 * .andExpect(content().json(resources.readErrorResponse(
	 * "common/error_response.json")));
	 * 
	 * }
	 */

	@Test
	public void test_update() throws IOException, Exception {
		when(fundService.update(any(List.class), any(BindingResult.class))).thenReturn((getUpdateFunds()));

		mockMvc.perform(post("/funds/_update").content(resources.readRequest("fund/fund_update_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("fund/fund_update_valid_response.json")));

		verify(fundService).addToQue(captor.capture());

		final CommonRequest<FundContract> actualRequest = captor.getValue();
		assertEquals("1", actualRequest.getData().get(0).getId());
		assertEquals("MunicipalFundUpdate", actualRequest.getData().get(0).getName());
		assertEquals("001", actualRequest.getData().get(0).getCode());
		assertEquals(Character.valueOf('U'), actualRequest.getData().get(0).getIdentifier());
		assertEquals("default", actualRequest.getData().get(0).getTenantId());
	}

	@Test
	public void testUpdate_Error() throws IOException, Exception {

		when(fundService.add(any(List.class), any(BindingResult.class))).thenReturn((getUpdateFunds()));

		mockMvc.perform(
				post("/funds/_update").content(resources.readRequest("fund/fund_create_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is5xxServerError());

	}

	@Test
	public void test_search() throws IOException, Exception {

		Pagination<Fund> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getFunds());
		page.getPagedData().get(0).setId("1");

		when(fundService.search(any(FundSearch.class))).thenReturn(page);

		mockMvc.perform(
				post("/funds/_search").content(resources.getRequestInfo()).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(200)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("fund/fund_search_valid_response.json")));

	}

	private List<Fund> getFunds() {
		List<Fund> funds = new ArrayList<Fund>();
		Fund fund = Fund.builder().name("MunicipalFund").code("001").identifier('M').active(true).build();
		fund.setTenantId("default");
		funds.add(fund);
		return funds;
	}

	private List<Fund> getUpdateFunds() {
		List<Fund> funds = new ArrayList<Fund>();
		Fund fund = Fund.builder().name("MunicipalFundUpdate").code("001").identifier('U').active(true).id("1")
				.level(1l).build();
		fund.setTenantId("default");
		funds.add(fund);
		return funds;
	}

}
