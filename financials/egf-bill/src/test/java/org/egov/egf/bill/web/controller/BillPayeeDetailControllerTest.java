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
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.bill.domain.service.BillPayeeDetailService;
import org.egov.egf.bill.utils.RequestJsonReader;
import org.egov.egf.bill.web.requests.BillPayeeDetailRequest;
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
@WebMvcTest(BillPayeeDetailController.class)
@Import(BillTestConfiguration.class)
public class BillPayeeDetailControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BillPayeeDetailService billPayeeDetailService;

	@Captor
	private ArgumentCaptor<BillPayeeDetailRequest> captor;

	private RequestJsonReader resources = new RequestJsonReader();

	@Test
	public void test_create() throws IOException, Exception {

		when(billPayeeDetailService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getBillPayeeDetails());

		mockMvc.perform(post("/billpayeedetails/_create").content(resources.readRequest("billpayeedetail/billpayeedetail_create_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("billpayeedetail/billpayeedetail_create_valid_response.json")));
	}

	@Test
	public void test_create_error() throws IOException, Exception {

		when(billPayeeDetailService.create(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(getBillPayeeDetails());

		mockMvc.perform(post("/billpayeedetails/_create").content(resources.readRequest("billpayeedetail/billpayeedetail_create_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}

	@Test
	public void test_update() throws IOException, Exception {

		List<BillPayeeDetail> billPayeeDetails = getBillPayeeDetails();
		billPayeeDetails.get(0).setAmount(new BigDecimal(12345.00));

		when(billPayeeDetailService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn(billPayeeDetails);

		mockMvc.perform(post("/billpayeedetails/_update").content(resources.readRequest("billpayeedetail/billpayeedetail_update_valid_request.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(201)).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json(resources.readResponse("billpayeedetail/billpayeedetail_update_valid_response.json")));
	}

	@Test
	public void test_update_error() throws IOException, Exception {

		when(billPayeeDetailService.update(any(List.class),any(BindingResult.class), any(RequestInfo.class))).thenReturn((getBillPayeeDetails()));

		mockMvc.perform(post("/billpayeedetails/_update").content(resources.readRequest("billpayeedetail/billpayeedetail_update_invalid_field_value.json"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError());
	}
	
	private List<BillPayeeDetail> getBillPayeeDetails() {

		List<BillPayeeDetail> billPayeeDetails = new ArrayList<BillPayeeDetail>();
		
		BillPayeeDetail billPayeeDetail = BillPayeeDetail.builder().id("6").amount(new BigDecimal(1234.00)).build();
		billPayeeDetail.setTenantId("default");
		
		billPayeeDetails.add(billPayeeDetail);
		return billPayeeDetails;
	}

}
