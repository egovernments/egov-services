package org.egov.egf.instrument.web.controller;

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
import org.egov.egf.instrument.TestConfiguration;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.SurrenderReasonSearch;
import org.egov.egf.instrument.domain.service.SurrenderReasonService;
import org.egov.egf.instrument.persistence.queue.repository.SurrenderReasonQueueRepository;
import org.egov.egf.instrument.utils.RequestJsonReader;
import org.egov.egf.instrument.web.requests.SurrenderReasonRequest;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(SurrenderReasonController.class)
@Import(TestConfiguration.class)
public class SurrenderReasonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SurrenderReasonService surrenderReasonService;

	@MockBean
	private SurrenderReasonQueueRepository surrenderReasonQueueRepository;

	@Captor
	private ArgumentCaptor<SurrenderReasonRequest> captor;

	private RequestJsonReader resources = new RequestJsonReader();

	@Test
	public void test_create_with_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(SurrenderReasonController.class, "persistThroughKafka", "yes");

		when(surrenderReasonService.fetchAndValidate(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn(getSurrenderReasons());

		mockMvc.perform(post("/surrenderreasons/_create")
				.content(resources.readRequest("surrenderreason/surrenderreason_create_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("surrenderreason/surrenderreason_create_valid_response.json")));

		verify(surrenderReasonQueueRepository).addToQue(captor.capture());

		final SurrenderReasonRequest actualRequest = captor.getValue();

		assertEquals("name", actualRequest.getSurrenderReasons().get(0).getName());
		assertEquals("description", actualRequest.getSurrenderReasons().get(0).getDescription());
		assertEquals("default", actualRequest.getSurrenderReasons().get(0).getTenantId());
	}

	@Test
	public void test_create_without_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(SurrenderReasonController.class, "persistThroughKafka", "no");

		when(surrenderReasonService.save(any(List.class), any(BindingResult.class))).thenReturn(getSurrenderReasons());

		mockMvc.perform(post("/surrenderreasons/_create")
				.content(resources.readRequest("surrenderreason/surrenderreason_create_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("surrenderreason/surrenderreason_create_valid_response.json")));

		verify(surrenderReasonQueueRepository).addToSearchQue(captor.capture());

		final SurrenderReasonRequest actualRequest = captor.getValue();

		assertEquals("name", actualRequest.getSurrenderReasons().get(0).getName());
		assertEquals("description", actualRequest.getSurrenderReasons().get(0).getDescription());
		assertEquals("default", actualRequest.getSurrenderReasons().get(0).getTenantId());
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(surrenderReasonService.fetchAndValidate(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn((getSurrenderReasons()));

		mockMvc.perform(post("/surrenderreasons/_create")
				.content(resources.readRequest("surrenderreason/surrenderreason_create_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_update_with_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(SurrenderReasonController.class, "persistThroughKafka", "yes");

		List<SurrenderReason> surrenderReasons = getSurrenderReasons();
		surrenderReasons.get(0).setId("1");

		when(surrenderReasonService.fetchAndValidate(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn(surrenderReasons);

		mockMvc.perform(post("/surrenderreasons/_update")
				.content(resources.readRequest("surrenderreason/surrenderreason_update_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("surrenderreason/surrenderreason_update_valid_response.json")));

		verify(surrenderReasonQueueRepository).addToQue(captor.capture());

		final SurrenderReasonRequest actualRequest = captor.getValue();

		assertEquals("name", actualRequest.getSurrenderReasons().get(0).getName());
		assertEquals("description", actualRequest.getSurrenderReasons().get(0).getDescription());
		assertEquals("default", actualRequest.getSurrenderReasons().get(0).getTenantId());
	}

	@Test
	public void test_update_without_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(SurrenderReasonController.class, "persistThroughKafka", "no");

		List<SurrenderReason> surrenderReasons = getSurrenderReasons();
		surrenderReasons.get(0).setId("1");

		when(surrenderReasonService.update(any(List.class), any(BindingResult.class))).thenReturn(surrenderReasons);

		mockMvc.perform(post("/surrenderreasons/_update")
				.content(resources.readRequest("surrenderreason/surrenderreason_update_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("surrenderreason/surrenderreason_update_valid_response.json")));

		verify(surrenderReasonQueueRepository).addToSearchQue(captor.capture());

		final SurrenderReasonRequest actualRequest = captor.getValue();

		assertEquals("name", actualRequest.getSurrenderReasons().get(0).getName());
		assertEquals("description", actualRequest.getSurrenderReasons().get(0).getDescription());
		assertEquals("default", actualRequest.getSurrenderReasons().get(0).getTenantId());
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(surrenderReasonService.fetchAndValidate(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn((getSurrenderReasons()));

		mockMvc.perform(post("/surrenderreasons/_update")
				.content(resources.readRequest("surrenderreason/surrenderreason_create_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_search() throws IOException, Exception {

		Pagination<SurrenderReason> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getSurrenderReasons());
		page.getPagedData().get(0).setId("1");

		when(surrenderReasonService.search(any(SurrenderReasonSearch.class))).thenReturn(page);

		mockMvc.perform(post("/surrenderreasons/_search").content(resources.getRequestInfo())
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(200))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("surrenderreason/surrenderreason_search_valid_response.json")));

	}

	private List<SurrenderReason> getSurrenderReasons() {
		List<SurrenderReason> surrenderReasons = new ArrayList<SurrenderReason>();
		SurrenderReason surrenderReason = SurrenderReason.builder().name("name").description("description").build();
		surrenderReason.setTenantId("default");
		surrenderReasons.add(surrenderReason);
		return surrenderReasons;
	}

}