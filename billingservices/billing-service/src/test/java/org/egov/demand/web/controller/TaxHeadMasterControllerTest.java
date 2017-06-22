package org.egov.demand.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.TestConfiguration;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.model.TaxPeriod;
import org.egov.demand.model.enums.Category;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.util.FileUtils;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.egov.demand.web.controller.TaxHeadMasterController;
import org.egov.demand.web.validator.TaxHeadMasterValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(TaxHeadMasterController.class)
@Import(TestConfiguration.class)
public class TaxHeadMasterControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TaxHeadMasterService taxHeadMasterService;
	
	@MockBean
	private TaxHeadMasterValidator taxHeadMasterValidator;
	
	@Test
	public void test_Should_Search_TaxHeadMaster() throws Exception {
		List<TaxHeadMaster> taxHeadMaster = new ArrayList<>();
		taxHeadMaster.add(getTaxHeadMaster());

		TaxHeadMasterResponse taxHeadMasterResponse = new TaxHeadMasterResponse();
		taxHeadMasterResponse.setTaxHeadMasters(taxHeadMaster);
		taxHeadMasterResponse.setResponseInfo(new ResponseInfo());

		when(taxHeadMasterService.getTaxHeads(Matchers.any(TaxHeadMasterCriteria.class), Matchers.any(RequestInfo.class)))
				.thenReturn(taxHeadMasterResponse);

		mockMvc.perform(post("/taxheads/_search").param("tenantId", "ap.kurnool")
				.param("service","string").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("requestinfowrapper.json"))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("taxHeadsSearchResponse.json")));
	}
	
	
	@Test
	public void test_Should_Create_TaxHeadMaster() throws Exception {

		List<TaxHeadMaster> taxHeadMasters = new ArrayList<>();
		TaxHeadMaster taxHeadMaster = getTaxHeadMaster();
		taxHeadMasters.add(taxHeadMaster);
		TaxHeadMasterResponse taxHeadMasterResponse = new TaxHeadMasterResponse();
		taxHeadMasterResponse.setTaxHeadMasters(taxHeadMasters);
		taxHeadMasterResponse.setResponseInfo(new ResponseInfo());

		when(taxHeadMasterService.create(any(TaxHeadMasterRequest.class))).thenReturn(taxHeadMasterResponse);

		mockMvc.perform(post("/taxheads/_create").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("taxHeadsCreateRequest.json"))).andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("taxHeadsCreateResponse.json")));
	}
	
	@Test
	public void test_Should_Update_TaxHeadMaster() throws Exception {

		List<TaxHeadMaster> taxHeadMasters = new ArrayList<>();
		TaxHeadMaster taxHeadMaster = getTaxHeadMaster();
		taxHeadMasters.add(taxHeadMaster);
		TaxHeadMasterResponse taxHeadMasterResponse = new TaxHeadMasterResponse();
		taxHeadMasterResponse.setTaxHeadMasters(taxHeadMasters);
		taxHeadMasterResponse.setResponseInfo(new ResponseInfo());

		when(taxHeadMasterService.update(any(TaxHeadMasterRequest.class))).thenReturn(taxHeadMasterResponse);

		mockMvc.perform(post("/taxheads/_update").contentType(MediaType.APPLICATION_JSON)
				.content(getFileContents("taxHeadsUpdateRequest.json"))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(getFileContents("taxHeadsUpdateResponse.json")));
	}
	
	
	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
	
	private TaxHeadMaster getTaxHeadMaster() {
		TaxHeadMaster taxHeadMaster = new TaxHeadMaster();
		TaxPeriod taxPeriod=new TaxPeriod();
		taxHeadMaster.setId("23");
		taxHeadMaster.setCode("string");
		taxHeadMaster.setTenantId("ap.kurnool");
		taxHeadMaster.setCategory(Category.fromValue("TAX"));
		taxHeadMaster.setService("string");
		taxHeadMaster.setName("string");
		taxHeadMaster.setGlCode("string");
		taxHeadMaster.setIsDebit(true);
		taxHeadMaster.setIsActualDemand(true);
		taxPeriod.setId("string");
		taxPeriod.setCode("string");
		taxPeriod.setFinancialYear("2017-2018");
		taxPeriod.setService("string");
		taxPeriod.setFromDate(123L);
		taxPeriod.setToDate(345L);
		
		taxHeadMaster.setTaxPeriod(taxPeriod);
		
	return taxHeadMaster;
	}


}
