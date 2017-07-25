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
import org.egov.egf.master.TestConfiguration;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.FunctionSearch;
import org.egov.egf.master.domain.service.FunctionService;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.requests.FunctionRequest;
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
@WebMvcTest(FunctionController.class)
@Import(TestConfiguration.class)
public class FunctionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FunctionService functionService;

	private RequestJsonReader resources = new RequestJsonReader();

	@Captor
	private ArgumentCaptor<FunctionRequest> captor;

	@Test
	public void testCreate() throws IOException, Exception {

		when(functionService.add(any(List.class), any(BindingResult.class))).thenReturn((getFunctions()));

		mockMvc.perform(
				post("/functions/_create").content(resources.readRequest("function/function_create_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("function/function_create_valid_response.json")));

		verify(functionService).addToQue(captor.capture());

		final FunctionRequest actualRequest = captor.getValue();
		assertEquals("function", actualRequest.getFunctions().get(0).getName());
		assertEquals("002", actualRequest.getFunctions().get(0).getCode());
		assertEquals(Integer.valueOf(1), actualRequest.getFunctions().get(0).getLevel());
		assertEquals("default", actualRequest.getFunctions().get(0).getTenantId());
	}

	@Test
	public void testCreate_Error() throws IOException, Exception {

		when(functionService.add(any(List.class), any(BindingResult.class))).thenReturn((getFunctions()));

		mockMvc.perform(post("/functions/_create")
				.content(resources.readRequest("function/function_create_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_update() throws IOException, Exception {
		when(functionService.update(any(List.class), any(BindingResult.class))).thenReturn((updateFunctions()));

		mockMvc.perform(
				post("/functions/_update").content(resources.readRequest("function/function_update_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("function/function_update_valid_response.json")));

		verify(functionService).addToQue(captor.capture());

		final FunctionRequest actualRequest = captor.getValue();
		assertEquals("2", actualRequest.getFunctions().get(0).getId());
		assertEquals("functionU", actualRequest.getFunctions().get(0).getName());
		assertEquals("003", actualRequest.getFunctions().get(0).getCode());
		assertEquals("default", actualRequest.getFunctions().get(0).getTenantId());
		assertEquals("1", actualRequest.getFunctions().get(0).getParentId().getId());
	}

	@Test
	public void testUpdate_Error() throws IOException, Exception {

		when(functionService.add(any(List.class), any(BindingResult.class))).thenReturn((updateFunctions()));

		mockMvc.perform(post("/functions/_update")
				.content(resources.readRequest("function/function_update_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_search() throws IOException, Exception {

		Pagination<Function> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getFunctions());
		page.getPagedData().get(0).setId("1");

		when(functionService.search(any(FunctionSearch.class))).thenReturn(page);

		mockMvc.perform(post("/functions/_search").content(resources.getRequestInfo())
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(200))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("function/function_search_valid_response.json")));

	}

	private List<Function> getFunctions() {
		List<Function> functions = new ArrayList<Function>();
		Function function = Function.builder().name("function").code("002").level(1).active(true).isParent(false)
				.parentId(parentFunction()).build();
		function.setTenantId("default");
		functions.add(function);
		return functions;
	}

	private List<Function> updateFunctions() {
		List<Function> functions = new ArrayList<Function>();
		Function function = Function.builder().name("functionU").code("003").level(1).active(true).isParent(false)
				.parentId(parentFunction()).id("2").build();
		function.setTenantId("default");
		functions.add(function);
		return functions;
	}

	private Function parentFunction() {
		Function function = Function.builder().name("parent").code("001").level(0).active(true).isParent(true).id("1")
				.build();
		return function;
	}
}
