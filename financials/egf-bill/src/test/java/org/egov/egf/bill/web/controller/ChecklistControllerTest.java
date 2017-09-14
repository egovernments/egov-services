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
import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
import org.egov.egf.bill.domain.service.ChecklistService;
import org.egov.egf.bill.utils.RequestJsonReader;
import org.egov.egf.bill.web.requests.ChecklistRequest;
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
@WebMvcTest(ChecklistController.class)
@Import(BillTestConfiguration.class)
public class ChecklistControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ChecklistService checklistService;

	@Captor
	private ArgumentCaptor<ChecklistRequest> captor;

	private RequestJsonReader resources = new RequestJsonReader();

	@Test
	public void test_create() throws IOException, Exception {

		when(checklistService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getChecklists());

		mockMvc.perform(post("/checklists/_create").content(resources.readRequest("checklist/checklist_create_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("checklist/checklist_create_valid_response.json")));
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(checklistService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getChecklists());

		mockMvc.perform(post("/checklists/_create").content(resources.readRequest("checklist/checklist_create_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}

	@Test
	public void test_update() throws IOException, Exception {

		List<Checklist> checklists = getChecklists();
		checklists.get(0).setType("checklisttypeu");

		when(checklistService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(checklists);

		mockMvc.perform(post("/checklists/_update").content(resources.readRequest("checklist/checklist_update_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("checklist/checklist_update_valid_response.json")));
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(checklistService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn((getChecklists()));

		mockMvc.perform(post("/checklists/_update").content(resources.readRequest("checklist/checklist_update_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}

	@Test
	public void test_search() throws IOException, Exception {

		Pagination<Checklist> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getChecklists());
		page.getPagedData().get(0).setId("1");

		when(checklistService.search(any(ChecklistSearch.class), any(BindingResult.class)))
				.thenReturn(page);

		mockMvc.perform(post("/checklists/_search").content(resources.getRequestInfo()).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(200)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("checklist/checklist_search_valid_response.json")));
	}
	
	private List<Checklist> getChecklists() {

		List<Checklist> checklists = new ArrayList<Checklist>();
		
		Checklist checklist = Checklist.builder().id("9").type("checklisttype").
								subType("checklistSubType").key("checklistkey").description("description")
								.build();
		checklist.setTenantId("default");
		
		checklists.add(checklist);
		return checklists;
	}

}
