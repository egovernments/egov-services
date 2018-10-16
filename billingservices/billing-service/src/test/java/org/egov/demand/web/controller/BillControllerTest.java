package org.egov.demand.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.TestConfiguration;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillSearchCriteria;
import org.egov.demand.model.GenerateBillCriteria;
import org.egov.demand.service.BillService;
import org.egov.demand.util.FileUtils;
import org.egov.demand.web.contract.BillRequest;
import org.egov.demand.web.contract.BillResponse;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.demand.web.validator.BillValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(BillController.class)
@Import(TestConfiguration.class)
@ActiveProfiles("test")
public class BillControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BillService billService;
	
	@MockBean
	private BillValidator billValidator;
	
	@MockBean
	private ResponseFactory responseFactory;
	
	@Test
	public void test_Should_Search_Bill() throws IOException, Exception{
		List<Bill> bills = new ArrayList<>();
		bills.add(getBills());
		BillResponse billResponse=new BillResponse();
		billResponse.setBill(bills);
		billResponse.setResposneInfo(new ResponseInfo());
		
		when(billService.searchBill(Matchers.any(BillSearchCriteria.class), Matchers.any(RequestInfo.class)))
		.thenReturn(billResponse);

		mockMvc.perform(post("/bill/_search").param("tenantId", "ap.kurnool")
		.contentType(MediaType.APPLICATION_JSON)
		.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().json(getFileContents("billSearchResponse.json")));

	}
	
	@Test
	public void test_Should_Create_Bill() throws IOException, Exception {
	 List<Bill> bills = new ArrayList<>();
	 bills.add(getBills());
	 BillResponse billResponse=new BillResponse();
		billResponse.setBill(bills);
		billResponse.setResposneInfo(new ResponseInfo());
		
		when(billService.createAsync(any(BillRequest.class))).thenReturn(billResponse);

		mockMvc.perform(post("/bill/_create").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("billCreateRequest.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("billCreateResponse.json")));
		
	}
	
	@Test
	public void test_Should_Generate_Bill() throws IOException, Exception {
	 List<Bill> bills = new ArrayList<>();
	 bills.add(getBills());
	 BillResponse billResponse=new BillResponse();
		billResponse.setBill(bills);
		billResponse.setResposneInfo(new ResponseInfo());
		
		when(billService.generateBill(Matchers.any(GenerateBillCriteria.class), Matchers.any(RequestInfo.class)))
		.thenReturn(billResponse);

		mockMvc.perform(post("/bill/_generate").param("tenantId", "ap.kurnool")
		.contentType(MediaType.APPLICATION_JSON)
		.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isCreated())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(content().json(getFileContents("billSearchResponse.json")));

	}	
	
	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
	
	private Bill getBills(){
		Bill bill=new Bill();
		bill.setId("12");
		bill.setIsActive(true);
		bill.setIsCancelled(true);
		bill.setPayeeAddress("bangalore");
		bill.setPayeeEmail("abc@xyz.com");
		bill.setPayeeName("abcd");
		bill.setTenantId("ap.kurnool");
		bill.setBillDetails(null);
		
		return bill;
	}

}
