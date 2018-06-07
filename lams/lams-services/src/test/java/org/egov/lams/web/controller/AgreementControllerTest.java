package org.egov.lams.web.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.TestConfiguration;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.Demand;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AgreementController.class)
@Import(TestConfiguration.class)
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
		agreement.setAcknowledgementNumber("ack");
		agreements.add(agreement);
		ResponseInfo responseInfo = new ResponseInfo();
		when(agreementService.searchAgreement(any(AgreementCriteria.class),any(RequestInfo.class))).thenReturn(agreements); 
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class),any(Boolean.class))).thenReturn(responseInfo);

		mockMvc.perform(post("/agreements/_search")
	        		.param("acknowledgementNumber","ack")
	        		.param("tenantId","ap.kurnool")
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
		agreement.setAction(Action.CREATE);
		agreement.setAcknowledgementNumber("ack");
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
		agreement.setAcknowledgementNumber("ack");
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

	@Test
	public void test_Should_Renew_Agreement() throws  Exception {
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		agreement.setAction(Action.RENEWAL);
		agreement.setAcknowledgementNumber("ack");
		ResponseInfo responseInfo = new ResponseInfo();
		when(agreementService.createRenewal(any(AgreementRequest.class))).thenReturn(agreement);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(responseInfo);
		mockMvc.perform(post("/agreements/_renew")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("agreementrequest.json")))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}

	@Test
	public void test_Should_Evict_Agreement() throws  Exception {
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		agreement.setAction(Action.EVICTION);
		agreement.setAcknowledgementNumber("ack");
		ResponseInfo responseInfo = new ResponseInfo();
		when(agreementService.createEviction(any(AgreementRequest.class))).thenReturn(agreement);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), (any(Boolean.class)))).thenReturn(responseInfo);
		mockMvc.perform(post("/agreements/_eviction")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("agreementrequest.json")))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}

	@Test
	public void test_Should_Cancel_Agreement() throws Exception {
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		agreement.setAction(Action.CANCELLATION);
		agreement.setAcknowledgementNumber("ack");
		ResponseInfo responseInfo = new ResponseInfo();
		when(agreementService.createCancellation(any(AgreementRequest.class))).thenReturn(agreement);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(responseInfo);
		mockMvc.perform(post("/agreements/_cancel")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("agreementrequest.json")))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}

	@Test
	public void test_Object_On_Agreement() throws Exception {
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		agreement.setAction(Action.OBJECTION);
		agreement.setAcknowledgementNumber("ack");
		ResponseInfo responseInfo = new ResponseInfo();
		when(agreementService.createObjection(any(AgreementRequest.class))).thenReturn(agreement);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(responseInfo);
		mockMvc.perform(post("/agreements/_objection")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("agreementrequest.json")))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}

	@Test
	public void test_Judgement_On_Agreement() throws Exception {
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		agreement.setAction(Action.JUDGEMENT);
		agreement.setAcknowledgementNumber("ack");
		ResponseInfo responseInfo = new ResponseInfo();
		when(agreementService.createJudgement(any(AgreementRequest.class))).thenReturn(agreement);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(responseInfo);
		mockMvc.perform(post("/agreements/_courtjudgement")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("agreementrequest.json")))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}
	
	@Test
	public void test_Remission_On_Agreement() throws Exception {
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		agreement.setAction(Action.REMISSION);
		agreement.setAcknowledgementNumber("ack");
		ResponseInfo responseInfo = new ResponseInfo();
		when(agreementService.createRemission(any(AgreementRequest.class))).thenReturn(agreement);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
				.thenReturn(responseInfo);
		mockMvc.perform(post("/agreements/_remission").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("agreementrequest.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}

	@Test
	public void test_should_prepare_demands() throws Exception {
		List<Agreement> agreements = new ArrayList<>();
		Agreement agreement = new Agreement();
		agreement.setTenantId("1");
		agreement.setAcknowledgementNumber("ack");
		agreements.add(agreement);

		List<Demand> demands = new ArrayList<>();
		Demand demand = new Demand();
		demand.setTenantId("1");
		demand.setTaxAmount(BigDecimal.TEN);
		demand.setCollectionAmount(BigDecimal.TEN);

		ResponseInfo responseInfo = new ResponseInfo();

		when(agreementService.searchAgreement(any(AgreementCriteria.class), any(RequestInfo.class))).thenReturn(agreements);
		when(agreementService.prepareLegacyDemands(any(AgreementRequest.class))).thenReturn(demands);
		when(agreementService.prepareDemands(any(AgreementRequest.class))).thenReturn(demands);
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class))).thenReturn(responseInfo);

		mockMvc.perform(post("/agreements/demands/_prepare")
				.param("agreementNumber", "1").param("tenantId", "1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("requestinfowrapper.json")))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("agreementsearchresponse.json")));
	}

	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
}
