package org.egov.egf.budget.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.TestConfiguration;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.service.BudgetReAppropriationService;
import org.egov.egf.budget.persistence.queue.repository.BudgetReAppropriationQueueRepository;
import org.egov.egf.budget.utils.RequestJsonReader;
import org.egov.egf.budget.web.contract.BudgetReAppropriationRequest;
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
@WebMvcTest(BudgetReAppropriationController.class)
@Import(TestConfiguration.class)
public class BudgetReAppropriationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BudgetReAppropriationService budgetReAppropriationService;

	@MockBean
	private BudgetReAppropriationQueueRepository budgetReAppropriationQueueRepository;

	@Captor
	private ArgumentCaptor<BudgetReAppropriationRequest> captor;

	private RequestJsonReader resources = new RequestJsonReader();

	@Test
	public void test_create_with_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(BudgetReAppropriationController.class, "persistThroughKafka", "yes");

		when(budgetReAppropriationService.fetchAndValidate(any(List.class), any(BindingResult.class),
				any(String.class))).thenReturn(getBudgetReAppropriations());

		mockMvc.perform(post("/budgetreappropriations/_create")
				.content(resources.readRequest("budgetreappropriation/budgetreappropriation_create_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(resources
						.readResponse("budgetreappropriation/budgetreappropriation_create_valid_response.json")));

		verify(budgetReAppropriationQueueRepository).addToQue(captor.capture());

		final BudgetReAppropriationRequest actualRequest = captor.getValue();

		assertEquals("1", actualRequest.getBudgetReAppropriations().get(0).getBudgetDetail().getId());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
		assertEquals("default", actualRequest.getBudgetReAppropriations().get(0).getTenantId());
	}

	@Test
	public void test_create_with_out_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(BudgetReAppropriationController.class, "persistThroughKafka", "no");

		when(budgetReAppropriationService.save(any(List.class), any(BindingResult.class)))
				.thenReturn(getBudgetReAppropriations());

		mockMvc.perform(post("/budgetreappropriations/_create")
				.content(resources.readRequest("budgetreappropriation/budgetreappropriation_create_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(resources
						.readResponse("budgetreappropriation/budgetreappropriation_create_valid_response.json")));

		verify(budgetReAppropriationQueueRepository).addToSearchQue(captor.capture());

		final BudgetReAppropriationRequest actualRequest = captor.getValue();

		assertEquals("1", actualRequest.getBudgetReAppropriations().get(0).getBudgetDetail().getId());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
		assertEquals("default", actualRequest.getBudgetReAppropriations().get(0).getTenantId());
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(budgetReAppropriationService.fetchAndValidate(any(List.class), any(BindingResult.class),
				any(String.class))).thenReturn((getBudgetReAppropriations()));

		mockMvc.perform(post("/budgetreappropriations/_create")
				.content(resources
						.readRequest("budgetreappropriation/budgetreappropriation_create_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_update_with_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(BudgetReAppropriationController.class, "persistThroughKafka", "yes");

		List<BudgetReAppropriation> budgetReAppropriations = getBudgetReAppropriations();
		budgetReAppropriations.get(0).setId("1");

		when(budgetReAppropriationService.fetchAndValidate(any(List.class), any(BindingResult.class),
				any(String.class))).thenReturn(budgetReAppropriations);

		mockMvc.perform(post("/budgetreappropriations/_update")
				.content(resources.readRequest("budgetreappropriation/budgetreappropriation_update_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(resources
						.readResponse("budgetreappropriation/budgetreappropriation_update_valid_response.json")));

		verify(budgetReAppropriationQueueRepository).addToQue(captor.capture());

		final BudgetReAppropriationRequest actualRequest = captor.getValue();

		assertEquals("1", actualRequest.getBudgetReAppropriations().get(0).getBudgetDetail().getId());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
		assertEquals("default", actualRequest.getBudgetReAppropriations().get(0).getTenantId());
	}

	@Test
	public void test_update_with_out_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(BudgetReAppropriationController.class, "persistThroughKafka", "no");

		List<BudgetReAppropriation> budgetReAppropriations = getBudgetReAppropriations();
		budgetReAppropriations.get(0).setId("1");

		when(budgetReAppropriationService.update(any(List.class), any(BindingResult.class)))
				.thenReturn(budgetReAppropriations);

		mockMvc.perform(post("/budgetreappropriations/_update")
				.content(resources.readRequest("budgetreappropriation/budgetreappropriation_update_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(resources
						.readResponse("budgetreappropriation/budgetreappropriation_update_valid_response.json")));

		verify(budgetReAppropriationQueueRepository).addToSearchQue(captor.capture());

		final BudgetReAppropriationRequest actualRequest = captor.getValue();

		assertEquals("1", actualRequest.getBudgetReAppropriations().get(0).getBudgetDetail().getId());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getAdditionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getDeductionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getOriginalAdditionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getOriginalDeductionAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getBudgetReAppropriations().get(0).getAnticipatoryAmount());
		assertEquals("default", actualRequest.getBudgetReAppropriations().get(0).getTenantId());
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(budgetReAppropriationService.fetchAndValidate(any(List.class), any(BindingResult.class),
				any(String.class))).thenReturn((getBudgetReAppropriations()));

		mockMvc.perform(post("/budgetreappropriations/_update")
				.content(resources
						.readRequest("budgetreappropriation/budgetreappropriation_create_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_search() throws IOException, Exception {

		Pagination<BudgetReAppropriation> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getBudgetReAppropriations());
		page.getPagedData().get(0).setId("1");

		when(budgetReAppropriationService.search(any(BudgetReAppropriationSearch.class))).thenReturn(page);

		mockMvc.perform(post("/budgetreappropriations/_search").content(resources.getRequestInfo())
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(200))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(content().json(resources
						.readResponse("budgetreappropriation/budgetreappropriation_search_valid_response.json")));

	}

	private List<BudgetReAppropriation> getBudgetReAppropriations() {

		List<BudgetReAppropriation> budgetReAppropriations = new ArrayList<BudgetReAppropriation>();

		BudgetReAppropriation budgetReAppropriation = BudgetReAppropriation.builder()
				.budgetDetail(BudgetDetail.builder().id("1").build()).additionAmount(BigDecimal.TEN)
				.deductionAmount(BigDecimal.TEN).originalAdditionAmount(BigDecimal.TEN)
				.originalDeductionAmount(BigDecimal.TEN).anticipatoryAmount(BigDecimal.TEN).build();

		budgetReAppropriation.setTenantId("default");
		budgetReAppropriations.add(budgetReAppropriation);

		return budgetReAppropriations;
	}

}