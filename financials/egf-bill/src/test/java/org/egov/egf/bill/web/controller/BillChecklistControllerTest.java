package org.egov.egf.bill.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.BillTestConfiguration;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.service.BillChecklistService;
import org.egov.egf.bill.utils.RequestJsonReader;
import org.egov.egf.bill.web.requests.BillChecklistRequest;
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
@WebMvcTest(BillChecklistController.class)
@Import(BillTestConfiguration.class)
public class BillChecklistControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BillChecklistService billChecklistService;

	@Captor
	private ArgumentCaptor<BillChecklistRequest> captor;

	private RequestJsonReader resources = new RequestJsonReader();

	@Test
	public void test_create() throws IOException, Exception {

		when(billChecklistService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getBillChecklists());

		mockMvc.perform(post("/billchecklists/_create").content(resources.readRequest("billchecklist/billchecklist_create_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("billchecklist/billchecklist_create_valid_response.json")));
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(billChecklistService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getBillChecklists());

		mockMvc.perform(post("/billchecklists/_create").content(resources.readRequest("billchecklist/billchecklist_create_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}

	@Test
	public void test_update() throws IOException, Exception {

		List<BillChecklist> billChecklists = getBillChecklists();
		billChecklists.get(0).setChecklistValue("newValueup");

		when(billChecklistService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(billChecklists);

		mockMvc.perform(post("/billchecklists/_update").content(resources.readRequest("billchecklist/billchecklist_update_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("billchecklist/billchecklist_update_valid_response.json")));
	}
	
	@Test
	public void test_search() throws IOException, Exception {

		Pagination<BillChecklist> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getBillChecklists());
		page.getPagedData().get(0).setId("6");

		when(billChecklistService.search(any(BillChecklistSearch.class), any(BindingResult.class)))
				.thenReturn(page);

		mockMvc.perform(post("/billchecklists/_search").content(resources.getRequestInfo()).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(200)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("billchecklist/billchecklist_search_valid_response.json")));
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(billChecklistService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn((getBillChecklists()));

		mockMvc.perform(post("/billchecklists/_update").content(resources.readRequest("billchecklist/billchecklist_update_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}
	
	private List<BillChecklist> getBillChecklists() {

		List<BillChecklist> billChecklists = new ArrayList<BillChecklist>();
		
		BillChecklist billChecklist = BillChecklist.builder().id("6").checklistValue("newValue").build();
		billChecklist.setBill(BillRegister.builder().id("29").build());
		billChecklist.setChecklist(Checklist.builder().id("4").build());
		billChecklist.setTenantId("default");
		
		billChecklists.add(billChecklist);
		return billChecklists;
	}

}
