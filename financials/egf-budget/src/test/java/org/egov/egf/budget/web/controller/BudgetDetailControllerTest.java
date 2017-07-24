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
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.budget.TestConfiguration;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.domain.service.BudgetDetailService;
import org.egov.egf.budget.persistence.queue.BudgetServiceQueueRepository;
import org.egov.egf.budget.utils.RequestJsonReader;
import org.egov.egf.budget.web.contract.BudgetDetailContract;
import org.egov.egf.master.web.contract.BudgetGroupContract;
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
@WebMvcTest(BudgetDetailController.class)
@Import(TestConfiguration.class)
public class BudgetDetailControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BudgetDetailService budgetDetailService;

	@MockBean
	private BudgetServiceQueueRepository budgetServiceQueueRepository;

	@Captor
	private ArgumentCaptor<CommonRequest<BudgetDetailContract>> captor;

	private RequestJsonReader resources = new RequestJsonReader();

	@Test
	public void test_create_with_kafka() throws IOException, Exception {
		
		ReflectionTestUtils.setField(BudgetDetailController.class, "persistThroughKafka", "yes");

		when(budgetDetailService.save(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn(getBudgetDetails());

		mockMvc.perform(post("/budgetdetails/_create")
				.content(resources.readRequest("budgetdetail/budgetdetail_create_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
						content().json(resources.readResponse("budgetdetail/budgetdetail_create_valid_response.json")));

		verify(budgetServiceQueueRepository).addToQue(captor.capture());

		final CommonRequest<BudgetDetailContract> actualRequest = captor.getValue();

		assertEquals("1", actualRequest.getData().get(0).getBudget().getId());
		assertEquals("1", actualRequest.getData().get(0).getBudgetGroup().getId());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getAnticipatoryAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getOriginalAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getApprovedAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getBudgetAvailable());
		assertEquals("default", actualRequest.getData().get(0).getTenantId());
	}
	
	@Test
	public void test_create_with_out_kafka() throws IOException, Exception {
		
		ReflectionTestUtils.setField(BudgetDetailController.class, "persistThroughKafka", "no");

		when(budgetDetailService.save(any(List.class), any(BindingResult.class)))
				.thenReturn(getBudgetDetails());

		mockMvc.perform(post("/budgetdetails/_create")
				.content(resources.readRequest("budgetdetail/budgetdetail_create_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
						content().json(resources.readResponse("budgetdetail/budgetdetail_create_valid_response.json")));

		verify(budgetServiceQueueRepository).addToSearchQue(captor.capture());

		final CommonRequest<BudgetDetailContract> actualRequest = captor.getValue();

		assertEquals("1", actualRequest.getData().get(0).getBudget().getId());
		assertEquals("1", actualRequest.getData().get(0).getBudgetGroup().getId());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getAnticipatoryAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getOriginalAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getApprovedAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getBudgetAvailable());
		assertEquals("default", actualRequest.getData().get(0).getTenantId());
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(budgetDetailService.save(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn((getBudgetDetails()));

		mockMvc.perform(post("/budgetdetails/_create")
				.content(resources.readRequest("budgetdetail/budgetdetail_create_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_update_with_kafka() throws IOException, Exception {
		
		ReflectionTestUtils.setField(BudgetDetailController.class, "persistThroughKafka", "yes");

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setId("1");

		when(budgetDetailService.save(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn(budgetDetails);

		mockMvc.perform(post("/budgetdetails/_update")
				.content(resources.readRequest("budgetdetail/budgetdetail_update_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
						content().json(resources.readResponse("budgetdetail/budgetdetail_update_valid_response.json")));

		verify(budgetServiceQueueRepository).addToQue(captor.capture());

		final CommonRequest<BudgetDetailContract> actualRequest = captor.getValue();

		assertEquals("1", actualRequest.getData().get(0).getBudget().getId());
		assertEquals("1", actualRequest.getData().get(0).getBudgetGroup().getId());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getAnticipatoryAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getOriginalAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getApprovedAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getBudgetAvailable());
		assertEquals("default", actualRequest.getData().get(0).getTenantId());
	}
	
	@Test
	public void test_update_with_out_kafka() throws IOException, Exception {
		
		ReflectionTestUtils.setField(BudgetDetailController.class, "persistThroughKafka", "no");

		List<BudgetDetail> budgetDetails = getBudgetDetails();
		budgetDetails.get(0).setId("1");

		when(budgetDetailService.update(any(List.class), any(BindingResult.class)))
				.thenReturn(budgetDetails);

		mockMvc.perform(post("/budgetdetails/_update")
				.content(resources.readRequest("budgetdetail/budgetdetail_update_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
						content().json(resources.readResponse("budgetdetail/budgetdetail_update_valid_response.json")));

		verify(budgetServiceQueueRepository).addToSearchQue(captor.capture());

		final CommonRequest<BudgetDetailContract> actualRequest = captor.getValue();

		assertEquals("1", actualRequest.getData().get(0).getBudget().getId());
		assertEquals("1", actualRequest.getData().get(0).getBudgetGroup().getId());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getAnticipatoryAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getOriginalAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getApprovedAmount());
		assertEquals(BigDecimal.TEN, actualRequest.getData().get(0).getBudgetAvailable());
		assertEquals("default", actualRequest.getData().get(0).getTenantId());
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(budgetDetailService.save(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn((getBudgetDetails()));

		mockMvc.perform(post("/budgetdetails/_update")
				.content(resources.readRequest("budgetdetail/budgetdetail_create_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_search() throws IOException, Exception {

		Pagination<BudgetDetail> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getBudgetDetails());
		page.getPagedData().get(0).setId("1");

		when(budgetDetailService.search(any(BudgetDetailSearch.class))).thenReturn(page);

		mockMvc.perform(post("/budgetdetails/_search").content(resources.getRequestInfo())
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(200))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(
						content().json(resources.readResponse("budgetdetail/budgetdetail_search_valid_response.json")));

	}

	private List<BudgetDetail> getBudgetDetails() {

		List<BudgetDetail> budgetDetails = new ArrayList<BudgetDetail>();

		BudgetDetail budgetDetail = BudgetDetail.builder().budget(Budget.builder().id("1").build())
				.budgetGroup(BudgetGroupContract.builder().id("1").build()).anticipatoryAmount(BigDecimal.TEN)
				.originalAmount(BigDecimal.TEN).approvedAmount(BigDecimal.TEN).budgetAvailable(BigDecimal.TEN)
				.planningPercent(BigDecimal.valueOf(1500)).build();

		budgetDetail.setTenantId("default");
		budgetDetails.add(budgetDetail);

		return budgetDetails;
	}

}