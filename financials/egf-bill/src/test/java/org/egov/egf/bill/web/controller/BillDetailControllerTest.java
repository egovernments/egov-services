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
import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.service.BillDetailService;
import org.egov.egf.bill.utils.RequestJsonReader;
import org.egov.egf.bill.web.requests.BillDetailRequest;
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
@WebMvcTest(BillDetailController.class)
@Import(BillTestConfiguration.class)
public class BillDetailControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BillDetailService billDetailService;

	@Captor
	private ArgumentCaptor<BillDetailRequest> captor;

	private RequestJsonReader resources = new RequestJsonReader();

	@Test
	public void test_create() throws IOException, Exception {

		when(billDetailService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getBillDetails());

		mockMvc.perform(post("/billdetails/_create").content(resources.readRequest("billdetail/billdetail_create_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("billdetail/billdetail_create_valid_response.json")));
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(billDetailService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getBillDetails());

		mockMvc.perform(post("/billdetails/_create").content(resources.readRequest("billdetail/billdetail_create_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}

	@Test
	public void test_update() throws IOException, Exception {

		List<BillDetail> billDetails = getBillDetails();
		billDetails.get(0).setGlcode("billdetailglcodeu");

		when(billDetailService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(billDetails);

		mockMvc.perform(post("/billdetails/_update").content(resources.readRequest("billdetail/billdetail_update_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("billdetail/billdetail_update_valid_response.json")));
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(billDetailService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn((getBillDetails()));

		mockMvc.perform(post("/billdetails/_update").content(resources.readRequest("billdetail/billdetail_update_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}
	
	private List<BillDetail> getBillDetails() {

		List<BillDetail> billDetails = new ArrayList<BillDetail>();
		
		BillDetail billDetail = BillDetail.builder().id("9").orderId(1234).glcode("billdetailglcode").debitAmount(new BigDecimal(10000)).creditAmount(new BigDecimal(10000))
								.build();
		billDetail.setTenantId("default");
		
		billDetails.add(billDetail);
		return billDetails;
	}

}
