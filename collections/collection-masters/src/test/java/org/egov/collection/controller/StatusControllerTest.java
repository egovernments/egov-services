package org.egov.collection.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.collection.domain.model.Status;
import org.egov.collection.domain.model.StatusCriteria;
import org.egov.collection.domain.service.StatusService;
import org.egov.collection.web.contract.RequestInfo;
import org.egov.collection.web.contract.ResponseInfo;
import org.egov.collection.web.contract.UserInfo;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.collection.web.controller.StatusController;
import org.egov.collection.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(StatusController.class)
public class StatusControllerTest {

	@MockBean
	ResponseInfoFactory responseInfoFactory;
	@MockBean
	StatusService statusService;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ErrorHandler errHandler;

	@Test
	public void test_should_search_for_collection_status_by_criteria() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(getRequestInfo(), true))
				.thenReturn(getResponseInfo());
		when(statusService.getStatuses(getStatusCriteria())).thenReturn(getStatusModel());
		mockMvc.perform(post("/collectionStatus/_search?code=SUBMITTED&tenantId=default&objectType=ReceiptHeader")
				.contentType(MediaType.APPLICATION_JSON_UTF8).content(getFileContents("StatusRequest.json")))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("StatusResponse.json")));
	}

	private List<Status> getStatusModel() {
		Status status = Status.builder().id(1L).code("SUBMITTED").description("Submitted").objectType("ReceiptHeader")
				.tenantId("default").createdBy(1L).lastModifiedBy(1L).build();
		return Arrays.asList(status);
	}

	private StatusCriteria getStatusCriteria() {
		return StatusCriteria.builder().code("SUBMITTED").objectType("ReceiptHeader").tenantId("default").build();
	}

	private RequestInfo getRequestInfo() {
		UserInfo userInfo = UserInfo.builder().id(1L).build();
		return RequestInfo.builder().apiId("org.egov.collection").ver("1.0").action("POST").did("4354648646").key("xyz")
				.msgId("654654").requesterId("61").userInfo(userInfo).authToken("345678f").build();

	}

	private ResponseInfo getResponseInfo() {
		return ResponseInfo.builder().apiId("org.egov.collection").ver("1.0").resMsgId("uief87324").msgId("654654")
				.status("successful").build();
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
