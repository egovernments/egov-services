package org.egov.lams.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.TestConfiguration;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.service.AgreementService;
import org.egov.lams.service.PaymentService;
import org.egov.lams.util.FileUtils;
import org.egov.lams.web.contract.BillReceiptInfoReq;
import org.egov.lams.web.contract.ReceiptAmountInfo;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentController.class)
@Import(TestConfiguration.class)
public class PaymentControllerTest {
	
	
	@MockBean
	private PaymentService paymentService;
	
	@MockBean
	private AgreementService agreementService;
	
	@MockBean
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void test_Should_Create_Bill() throws Exception{
		
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(new Agreement());
		String s="xml";
		when(agreementService.searchAgreement(any(AgreementCriteria.class),any(RequestInfo.class))).thenReturn(agreements);
		when(paymentService.generateBillXml(any(Agreement.class),any(RequestInfo.class))).thenReturn(s);

		mockMvc.perform(post("/payment/_create")
				.param("tenantId", "ap.kurnool")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("requestinfowrapper.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.ALL_VALUE))
	                .andExpect(content().string(getFileContents("billresponse.json")));
	                		
	                		
	                		//json(getFileContents("billresponse.json")));
	}
	
	@Test
	public void test_Should_Update_Bill() throws Exception{
		
		ReceiptAmountInfo receiptAmountInfo = new ReceiptAmountInfo();
		ResponseEntity<ReceiptAmountInfo> responseEntity = new ResponseEntity<>(receiptAmountInfo,HttpStatus.OK);
		when(paymentService.updateDemand(any(BillReceiptInfoReq.class))).thenReturn(responseEntity);

		mockMvc.perform(post("/payment/_update")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("billreceiptinforeq.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("billupdateresponse.json")));
	}
	
	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
}
