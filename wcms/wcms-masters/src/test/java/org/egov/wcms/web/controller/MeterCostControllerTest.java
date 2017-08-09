package org.egov.wcms.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.TestConfiguration;
import org.egov.wcms.model.MeterCostCriteria;
import org.egov.wcms.model.MeterCost;
import org.egov.wcms.service.MeterCostService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.MeterCostReq;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MeterCostController.class)
@Import(TestConfiguration.class)
public class MeterCostControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MeterCostService meterCostService;

	@InjectMocks
	private MeterCostController meterCostController;

	@MockBean
	private ResponseInfoFactory responseInfoFactory;

	@MockBean
	private ValidatorUtils validatorUtils;

	@MockBean
	private ErrorHandler errHandler;

	@Test
	public void test_should_create_meter_cost() throws Exception {
		List<ErrorResponse> errorResponses = new ArrayList<>();
		when(validatorUtils.validateMeterCostRequest(getMeterCostRequest())).thenReturn(errorResponses);
		when(meterCostService.createMeterCostPushToQueue(any(MeterCostReq.class))).thenReturn(getListOfMeterCosts());
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
				.thenReturn(getSuccessRequestInfo());
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
				.thenReturn(getFailureRequestInfo());
		mockMvc.perform(post("/metercosts/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("MeterCostRequestCreate.json"))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("MeterCostResponseCreate.json")));
	}

	@Test
	public void test_should_update_meter_cost() throws Exception {
		List<ErrorResponse> errorResponses = new ArrayList<>();
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
				.thenReturn(getSuccessRequestInfo());
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
				.thenReturn(getFailureRequestInfo());
		when(validatorUtils.validateMeterCostRequest(getMeterCostRequestForUpdate())).thenReturn(errorResponses);
		when(meterCostService.updateMeterCostPushToQueue(getMeterCostRequestForUpdate()))
				.thenReturn(getListOfUpdatedMeterCosts());
		mockMvc.perform(post("/metercosts/_update").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("MeterCostRequestUpdate.json"))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("MeterCostResponseUpdate.json")));

	}

	@Test
	public void test_should_search_meter_cost() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(true)))
				.thenReturn(getSuccessRequestInfo());
		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), eq(false)))
				.thenReturn(getFailureRequestInfo());
		when(meterCostService.getMeterCostByCriteria(getMeterCostCriteria())).thenReturn(getSearchResult());
		mockMvc.perform(post("/metercosts/_search?ids=1,2&active=true&tenantId=default&sortBy=code&sortOrder=desc")
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(getFileContents("MeterCostRequest.json")))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("MeterCostResponse.json")));

	}

	private List<MeterCost> getSearchResult() {
		MeterCost meterCost1 = MeterCost.builder().id(1L).code("Meter").pipeSizeId(1L).meterMake("meterMake123")
				.amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
		MeterCost meterCost2 = MeterCost.builder().id(2L).code("Weter").pipeSizeId(2L).meterMake("meterMake234")
				.amount(5000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
		return Arrays.asList(meterCost2, meterCost1);
	}

	private MeterCostCriteria getMeterCostCriteria() {
		return MeterCostCriteria.builder().active(true).ids(Arrays.asList(1L, 2L)).tenantId("default").sortby("code")
				.sortOrder("desc").build();
	}

	private List<MeterCost> getListOfUpdatedMeterCosts() {
		MeterCost meterCost1 = MeterCost.builder().id(1L).code("MeterCost123").pipeSizeId(1L)
				.meterMake("meterMakeUpdated1").amount(3000.0).active(true).createdBy(1L).lastModifiedBy(1L)
				.tenantId("default").build();
		MeterCost meterCost2 = MeterCost.builder().id(2L).code("MeterCost234").pipeSizeId(2L)
				.meterMake("meterMakeUpdated2").amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L)
				.tenantId("default").build();
		return Arrays.asList(meterCost1, meterCost2);
	}

	private MeterCostReq getMeterCostRequestForUpdate() {
		User userInfo = User.builder().id(1L).build();
		RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
				.did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
		MeterCost meterCost1 = MeterCost.builder().id(1L).code("MeterCost123").pipeSizeId(1L)
				.meterMake("meterMakeUpdated1").amount(3000.0).active(true).createdBy(1L).lastModifiedBy(1L)
				.tenantId("default").build();
		MeterCost meterCost2 = MeterCost.builder().id(2L).code("MeterCost234").pipeSizeId(2L)
				.meterMake("meterMakeUpdated2").amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L)
				.tenantId("default").build();
		return MeterCostReq.builder().requestInfo(requestInfo).meterCost(Arrays.asList(meterCost1, meterCost2)).build();

	}

	private ResponseInfo getFailureRequestInfo() {
		return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
				.status("failed").build();
	}

	private ResponseInfo getSuccessRequestInfo() {
		return ResponseInfo.builder().apiId("org.egov.wcms").ver("1.0").resMsgId("uief87324").msgId("654654")
				.status("successful").build();
	}

	private List<MeterCost> getListOfMeterCosts() {
		MeterCost meterCost1 = MeterCost.builder().id(1L).code("MeterCost123").pipeSizeId(1L).meterMake("meterMake123")
				.amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
		MeterCost meterCost2 = MeterCost.builder().id(2L).code("MeterCost234").pipeSizeId(2L).meterMake("meterMake234")
				.amount(5000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
		return Arrays.asList(meterCost1, meterCost2);
	}

	private MeterCostReq getMeterCostRequest() {
		User userInfo = User.builder().id(1L).build();
		RequestInfo requestInfo = RequestInfo.builder().apiId("org.egov.wcms").ver("1.0").action("POST")
				.did("4354648646").key("xyz").msgId("654654").authToken("345678f").userInfo(userInfo).build();
		MeterCost meterCost1 = MeterCost.builder().id(1L).code("MeterCost123").pipeSizeId(1L).meterMake("meterMake123")
				.amount(4000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
		MeterCost meterCost2 = MeterCost.builder().id(2L).code("MeterCost234").pipeSizeId(2L).meterMake("meterMake234")
				.amount(5000.0).active(true).createdBy(1L).lastModifiedBy(1L).tenantId("default").build();
		return MeterCostReq.builder().requestInfo(requestInfo).meterCost(Arrays.asList(meterCost1, meterCost2)).build();

	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
