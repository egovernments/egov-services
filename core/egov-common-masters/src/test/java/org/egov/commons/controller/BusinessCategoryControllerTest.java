package org.egov.commons.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.commons.model.AuthenticatedUser;
import org.egov.commons.model.BusinessCategory;
import org.egov.commons.model.BusinessCategoryCriteria;
import org.egov.commons.service.BusinessCategoryService;
import org.egov.commons.web.contract.factory.ResponseInfoFactory;
import org.egov.commons.web.controller.BusinessCategoryController;
import org.egov.commons.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(BusinessCategoryController.class)
public class BusinessCategoryControllerTest {

	@MockBean
	private BusinessCategoryService serviceCategoryService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ErrorHandler errHandler;

	@MockBean
	private ResponseInfoFactory responseInfoFactory;

	@Test
	public void test_should_create_business_category() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(getRequestInfo(), true))
				.thenReturn(getResponseInfo());
		when(serviceCategoryService.getBusinessCategoryByNameAndTenantId("Collection", "default")).thenReturn(true);
		when(serviceCategoryService.getBusinessCategoryByCodeAndTenantId("CL", "default")).thenReturn(true);
		when(serviceCategoryService.create(getBusinessCategoryModel(), getAuthenticatedUser()))
				.thenReturn(getBusinessCategoryModel());

		mockMvc.perform(post("/businessCategory/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("businessCategoryRequestCreate.json"))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("businessCategoryResponseCreate.json")));

	}

	@Test
	public void test_should_update_business_category() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(getRequestInfo(), true))
				.thenReturn(getResponseInfo());
		when(serviceCategoryService.getBusinessCategoryByNameAndTenantId("Collection", "default")).thenReturn(true);
		when(serviceCategoryService.getBusinessCategoryByCodeAndTenantId("CL", "default")).thenReturn(true);
		when(serviceCategoryService.update("CLL", getBusinessCategoryModel(), getAuthenticatedUser()))
				.thenReturn(getBusinessCategoryModel());

		mockMvc.perform(post("/businessCategory/{businessCategoryCode}/_update", "CLL")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("businessCategoryRequestUpdate.json"))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("businessCategoryResponseUpdate.json")));
	}

	@Test
	public void test_should_get_all_business_categories_with_the_params_criteria() throws Exception {
		when(responseInfoFactory.createResponseInfoFromRequestInfo(getRequestInfo(), true))
				.thenReturn(getResponseInfo());
		when(serviceCategoryService.getForCriteria(getBusinessCriteria()))
				.thenReturn(getListOfModelBusinessCategories());

		mockMvc.perform(
				post("/businessCategory/_search?active=true&tenantId=default&ids=1,2,3&sortBy=code&sortOrder=desc")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(getFileContents("businessCategoryRequest.json")))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("businessCategoryResponse.json")));
	}

	private List<BusinessCategory> getListOfModelBusinessCategories() {
		BusinessCategory category1 = BusinessCategory.builder().id(3L).code("TL").name("Trade Licence").isactive(true)
				.tenantId("default").build();
		BusinessCategory category2 = BusinessCategory.builder().id(2L).code("MR").name("Marriage Registration")
				.isactive(true).tenantId("default").build();
		BusinessCategory category3 = BusinessCategory.builder().id(1L).code("CL").name("Collection").isactive(true)
				.tenantId("default").build();
		return Arrays.asList(category1, category2, category3);
	}

	private BusinessCategoryCriteria getBusinessCriteria() {
		return BusinessCategoryCriteria.builder().ids(Arrays.asList(1L, 2L, 3L)).active(true).sortBy("code")
				.sortOrder("desc").tenantId("default").build();
	}

	private RequestInfo getRequestInfo() {
		User userInfo = User.builder().id(1L).name("ram").emailId("ram@gmail.com").mobileNumber("73878921")
				.build();
		return RequestInfo.builder().apiId("org.egov.collection").ver("1.0").action("POST").did("4354648646").key("xyz")
				.msgId("654654").requesterId("61").authToken("345678f").userInfo(userInfo).build();
	}

	private AuthenticatedUser getAuthenticatedUser() {

		return AuthenticatedUser.builder().id(1L).name("ram").anonymousUser(false).emailId("ram@gmail.com")
				.mobileNumber("73878921").build();

	}

	private ResponseInfo getResponseInfo() {
		return ResponseInfo.builder().apiId("org.egov.collection").ver("1.0").resMsgId("uief87324").msgId("654654")
				.status("successful").build();
	}

	private BusinessCategory getBusinessCategoryModel() {
		BusinessCategory category = BusinessCategory.builder().id(1L).code("CL").name("Collection").isactive(true)
				.tenantId("default").build();
		return category;
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
