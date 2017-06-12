package org.egov.lams.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.IOException;

import org.egov.lams.repository.RentIncrementRepository;
import org.egov.lams.util.FileUtils;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(LamsMasterController.class)
public class LamsMasterControllerTest {
	
	@Autowired 
	private MockMvc mockMvc;
	
	@MockBean
	private ResponseInfoFactory responseInfoFactory;
	
	@MockBean
	private RentIncrementRepository rentIncrementService;

	@Test
	public void test_Should_Return_Status() throws Exception{
	       
	        mockMvc.perform(get("/getstatus"))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("status.json")));
	}
	
	@Test
	public void test_Should_Return_PaymentCycle() throws Exception{
	       
	        mockMvc.perform(get("/getpaymentcycle"))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("paymentcycle.json")));
	}
	
	@Test
	public void test_Should_Return_NatureOfAllotment() throws Exception{
	       
	        mockMvc.perform(get("/getnatureofallotment"))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("natureofallotment.json")));
	}

	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
}
