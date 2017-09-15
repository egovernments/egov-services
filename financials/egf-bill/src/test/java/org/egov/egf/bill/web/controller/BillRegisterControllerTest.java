package org.egov.egf.bill.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.BillTestConfiguration;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;
import org.egov.egf.bill.domain.service.BillRegisterService;
import org.egov.egf.bill.utils.RequestJsonReader;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
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
@WebMvcTest(BillRegisterController.class)
@Import(BillTestConfiguration.class)
public class BillRegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BillRegisterService billRegisterService;
	
	@Captor
	private ArgumentCaptor<BillRegisterRequest> captor;

	private RequestJsonReader resources = new RequestJsonReader();
	
	@Test
	public void test_create() throws IOException, Exception {

		when(billRegisterService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getBillRegisters());
		mockMvc.perform(post("/billregisters/_create").content(resources.readRequest("billregisters/billregister_create_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("billregisters/billregister_create_valid_response.json")));
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(billRegisterService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getBillRegisters());

		mockMvc.perform(post("/billregisters/_create").content(resources.readRequest("billregisters/billregister_create_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}

	@Test
	public void test_update() throws IOException, Exception {

		List<BillRegister> billRegisters = getBillRegisters();
		billRegisters.get(0).setBillAmount(new BigDecimal(43215));

		when(billRegisterService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(billRegisters);

		mockMvc.perform(post("/billregisters/_update").content(resources.readRequest("billregisters/billregister_update_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("billregisters/billregister_update_valid_response.json")));
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(billRegisterService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn((getBillRegisters()));

		mockMvc.perform(post("/billregisters/_update").content(resources.readRequest("billregisters/billregister_update_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}

	@Test
	public void test_search() throws IOException, Exception {

		Pagination<BillRegister> page = new Pagination<>();
		page.setTotalPages(1);
		page.setTotalResults(1);
		page.setCurrentPage(0);
		page.setPagedData(getBillRegisters());
		page.getPagedData().get(0).setId("1");

		when(billRegisterService.search(any(BillRegisterSearch.class), any(BindingResult.class)))
				.thenReturn(page);

		mockMvc.perform(post("/billregisters/_search").content(resources.getRequestInfo()).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(200)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(resources.readResponse("billregisters/billregister_search_valid_response.json")));
	}
	
	private List<BillRegister> getBillRegisters() {

		List<BillRegister> billRegisters = new ArrayList<BillRegister>();
		
		BillRegister billRegister = BillRegister.builder().id("30").billType("billtype4321").billAmount(new BigDecimal(4321)).build();
//		BillPayeeDetail billPayeeDetail = BillPayeeDetail.builder().id("5").build();
//		BillDetail billDetail = BillDetail.builder().id("29").orderId(4321).glcode("billdetailglcode4321").debitAmount(new BigDecimal(10000))
//				.creditAmount(new BigDecimal(10000)).build();
//		billDetail.setTenantId("default");
//		billPayeeDetail.setTenantId("default");
		billRegister.setTenantId("default");
		billRegisters.add(billRegister);
		return billRegisters;
	}

}
