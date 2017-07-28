package org.egov.egf.instrument.web.controller;

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
import org.egov.egf.instrument.TestConfiguration;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.TransactionType;
import org.egov.egf.instrument.domain.service.InstrumentService;
import org.egov.egf.instrument.persistence.queue.repository.InstrumentQueueRepository;
import org.egov.egf.instrument.utils.RequestJsonReader;
import org.egov.egf.instrument.web.contract.TransactionTypeContract;
import org.egov.egf.instrument.web.requests.InstrumentRequest;
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
@WebMvcTest(InstrumentController.class)
@Import(TestConfiguration.class)
public class InstrumentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private InstrumentService instrumentService;

	@MockBean
	private InstrumentQueueRepository instrumentQueueRepository;

	@Captor
	private ArgumentCaptor<InstrumentRequest> captor;

	private RequestJsonReader resources = new RequestJsonReader();

	@Test
	public void test_create_with_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(InstrumentController.class, "persistThroughKafka", "yes");

		when(instrumentService.fetchAndValidate(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn(getInstruments());

		mockMvc.perform(post("/instruments/_create")
				.content(resources.readRequest("instrument/instrument_create_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("instrument/instrument_create_valid_response.json")));

		verify(instrumentQueueRepository).addToQue(captor.capture());

		final InstrumentRequest actualRequest = captor.getValue();

		assertEquals("instrumenttype", actualRequest.getInstruments().get(0).getInstrumentType().getName());
		assertEquals(true, actualRequest.getInstruments().get(0).getInstrumentType().getActive());
		assertEquals("transactionNumber", actualRequest.getInstruments().get(0).getTransactionNumber());
		assertEquals("serialNo", actualRequest.getInstruments().get(0).getSerialNo());
		assertEquals(BigDecimal.ONE, actualRequest.getInstruments().get(0).getAmount());
		assertEquals(TransactionTypeContract.Credit, actualRequest.getInstruments().get(0).getTransactionType());
		assertEquals("default", actualRequest.getInstruments().get(0).getTenantId());
	}

	@Test
	public void test_create_without_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(InstrumentController.class, "persistThroughKafka", "no");

		when(instrumentService.save(any(List.class), any(BindingResult.class))).thenReturn(getInstruments());

		mockMvc.perform(post("/instruments/_create")
				.content(resources.readRequest("instrument/instrument_create_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("instrument/instrument_create_valid_response.json")));

		verify(instrumentQueueRepository).addToSearchQue(captor.capture());

		final InstrumentRequest actualRequest = captor.getValue();

		assertEquals("instrumenttype", actualRequest.getInstruments().get(0).getInstrumentType().getName());
		assertEquals(true, actualRequest.getInstruments().get(0).getInstrumentType().getActive());
		assertEquals("transactionNumber", actualRequest.getInstruments().get(0).getTransactionNumber());
		assertEquals("serialNo", actualRequest.getInstruments().get(0).getSerialNo());
		assertEquals(BigDecimal.ONE, actualRequest.getInstruments().get(0).getAmount());
		assertEquals(TransactionTypeContract.Credit, actualRequest.getInstruments().get(0).getTransactionType());
		assertEquals("default", actualRequest.getInstruments().get(0).getTenantId());
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(instrumentService.fetchAndValidate(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn((getInstruments()));

		mockMvc.perform(post("/instruments/_create")
				.content(resources.readRequest("instrument/instrument_create_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_update_with_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(InstrumentController.class, "persistThroughKafka", "yes");

		List<Instrument> instruments = getInstruments();
		instruments.get(0).setId("1");

		when(instrumentService.fetchAndValidate(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn(instruments);

		mockMvc.perform(post("/instruments/_update")
				.content(resources.readRequest("instrument/instrument_update_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("instrument/instrument_update_valid_response.json")));

		verify(instrumentQueueRepository).addToQue(captor.capture());

		final InstrumentRequest actualRequest = captor.getValue();

		assertEquals("instrumenttype", actualRequest.getInstruments().get(0).getInstrumentType().getName());
		assertEquals(true, actualRequest.getInstruments().get(0).getInstrumentType().getActive());
		assertEquals("transactionNumber", actualRequest.getInstruments().get(0).getTransactionNumber());
		assertEquals("serialNo", actualRequest.getInstruments().get(0).getSerialNo());
		assertEquals(BigDecimal.ONE, actualRequest.getInstruments().get(0).getAmount());
		assertEquals(TransactionTypeContract.Credit, actualRequest.getInstruments().get(0).getTransactionType());
		assertEquals("default", actualRequest.getInstruments().get(0).getTenantId());
	}

	@Test
	public void test_update_without_kafka() throws IOException, Exception {

		ReflectionTestUtils.setField(InstrumentController.class, "persistThroughKafka", "no");

		List<Instrument> instruments = getInstruments();
		instruments.get(0).setId("1");

		when(instrumentService.update(any(List.class), any(BindingResult.class))).thenReturn(instruments);

		mockMvc.perform(post("/instruments/_update")
				.content(resources.readRequest("instrument/instrument_update_valid_request.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("instrument/instrument_update_valid_response.json")));

		verify(instrumentQueueRepository).addToSearchQue(captor.capture());

		final InstrumentRequest actualRequest = captor.getValue();

		assertEquals("instrumenttype", actualRequest.getInstruments().get(0).getInstrumentType().getName());
		assertEquals(true, actualRequest.getInstruments().get(0).getInstrumentType().getActive());
		assertEquals("transactionNumber", actualRequest.getInstruments().get(0).getTransactionNumber());
		assertEquals("serialNo", actualRequest.getInstruments().get(0).getSerialNo());
		assertEquals(BigDecimal.ONE, actualRequest.getInstruments().get(0).getAmount());
		assertEquals(TransactionTypeContract.Credit, actualRequest.getInstruments().get(0).getTransactionType());
		assertEquals("default", actualRequest.getInstruments().get(0).getTenantId());
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(instrumentService.fetchAndValidate(any(List.class), any(BindingResult.class), any(String.class)))
				.thenReturn((getInstruments()));

		mockMvc.perform(post("/instruments/_update")
				.content(resources.readRequest("instrument/instrument_create_invalid_field_value.json"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());

	}

	@Test
	public void test_search() throws IOException, Exception {

		Pagination<Instrument> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getInstruments());
		page.getPagedData().get(0).setId("1");

		when(instrumentService.search(any(InstrumentSearch.class))).thenReturn(page);

		mockMvc.perform(post("/instruments/_search").content(resources.getRequestInfo())
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(200))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("instrument/instrument_search_valid_response.json")));

	}

	private List<Instrument> getInstruments() {
		List<Instrument> instruments = new ArrayList<Instrument>();
		Instrument instrument = Instrument.builder().transactionNumber("transactionNumber").amount(BigDecimal.ONE)
				.transactionType(TransactionType.Credit).serialNo("serialNo")
				.instrumentType(InstrumentType.builder().active(true).name("instrumenttype").build()).build();
		instrument.setTenantId("default");
		instruments.add(instrument);
		return instruments;
	}

}