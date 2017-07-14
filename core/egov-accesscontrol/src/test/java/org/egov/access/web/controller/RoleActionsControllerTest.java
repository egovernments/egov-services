package org.egov.access.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.egov.access.Resources;
import org.egov.access.TestConfiguration;
import org.egov.access.domain.model.RoleAction;
import org.egov.access.domain.service.RoleActionService;
import org.egov.access.web.contract.action.RoleActionsRequest;
import org.egov.access.web.contract.factory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
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
@WebMvcTest(RoleActionController.class)
@Import(TestConfiguration.class)
public class RoleActionsControllerTest {

	@MockBean
	private RoleActionService roleActionService;

	@MockBean
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void createRoleActions() throws Exception {

		List<RoleAction> roleActionList = new ArrayList<RoleAction>();

		when(roleActionService.createRoleActions(any(RoleActionsRequest.class))).thenReturn(roleActionList);

		ResponseInfo responseInfo = ResponseInfo.builder().build();

		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
				.thenReturn(responseInfo);

		mockMvc.perform(post("/v1/role-actions/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new Resources().getFileContents("roleActionRequest.json")))
		        .andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(new Resources().getFileContents("roleActionResponse.json")));

	}

	@Test
	public void testShouldNotcreateRoleActionsWithoutTenant() throws Exception {

		List<RoleAction> roleActionList = new ArrayList<RoleAction>();

		when(roleActionService.createRoleActions(any(RoleActionsRequest.class))).thenReturn(roleActionList);

		ResponseInfo responseInfo = ResponseInfo.builder().build();

		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
				.thenReturn(responseInfo);

		mockMvc.perform(post("/v1/role-actions/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new Resources().getFileContents("roleActionRequestWithoutTenant.json")))
		        .andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(new Resources().getFileContents("roleActionResponseWithoutTenant.json")));

	}
	
	
	@Test
	public void testShouldNotcreateRoleActionsWithoutActions() throws Exception {

		List<RoleAction> roleActionList = new ArrayList<RoleAction>();

		when(roleActionService.createRoleActions(any(RoleActionsRequest.class))).thenReturn(roleActionList);

		ResponseInfo responseInfo = ResponseInfo.builder().build();

		when(responseInfoFactory.createResponseInfoFromRequestInfo(any(RequestInfo.class), any(Boolean.class)))
				.thenReturn(responseInfo);

		mockMvc.perform(post("/v1/role-actions/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new Resources().getFileContents("roleActionRequestWithoutActions.json")))
		        .andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(new Resources().getFileContents("roleActionResponseWithoutActions.json")));

	}
	
}
