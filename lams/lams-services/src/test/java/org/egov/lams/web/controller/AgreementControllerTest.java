package org.egov.lams.web.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.service.AgreementService;
import org.egov.lams.util.FileUtils;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.ResponseInfo;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.egov.lams.web.validator.AgreementValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AgreementController.class)
public class AgreementControllerTest {
	
	
	@MockBean
	private AgreementService agreementService;

	@MockBean
	private ResponseInfoFactory responseInfoFactory;
	
	@MockBean
	private AgreementValidator agreementValidator;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	public void test_Should_Search_Agreements() throws Exception{
		
		List<Agreement> agreements = new ArrayList<>();
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		agreements.add(agreement);
		ResponseInfo responseInfo = new ResponseInfo();
		AgreementCriteria agreementCriteria = new AgreementCriteria();
		agreementCriteria.setAcknowledgementNumber("ack");
		when(agreementService.searchAgreement(any(AgreementCriteria.class))).thenReturn(agreements); 
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),any(Boolean.class))).thenReturn(responseInfo);

		mockMvc.perform(post("/agreements/_search")
	        		.param("acknowledgementNumber","ack")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("requestinfowrapper.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}
	
	@Test
	public void test_Should_Create_Agreement() throws Exception{
		
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		ResponseInfo responseInfo = new ResponseInfo();
		when(agreementService.createAgreement(any(AgreementRequest.class))).thenReturn(agreement); 
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),any(Boolean.class))).thenReturn(responseInfo);
	       
	        mockMvc.perform(post("/agreements/_create")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("agreementrequest.json")))
	                .andExpect(status().isCreated())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}
	
	@Test
	public void test_Should_Update_Agreement() throws Exception{
		
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		ResponseInfo responseInfo = new ResponseInfo();
		when(agreementService.updateAgreement(any(AgreementRequest.class))).thenReturn(agreement); 
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),any(Boolean.class))).thenReturn(responseInfo);
	       
	        mockMvc.perform(post("/agreements/_update/{code}","00085-2017-RH")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("agreementrequest.json")))
	                .andExpect(status().isCreated())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}
	
	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
}
