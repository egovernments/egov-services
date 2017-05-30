package org.egov.collection.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;

import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.egov.collection.domain.model.ServiceCategorySearchCriteria;
import org.egov.collection.domain.service.ServiceCategoryService;
import org.egov.collection.persistence.entity.ServiceCategory;

import org.egov.collection.web.controller.BusinessCategoryController;
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
	private ServiceCategoryService serviceCategoryService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void test_should_create_business_category_when_data_is_specified() throws Exception {
		when(serviceCategoryService.create(getServiceCategory())).thenReturn(getServiceCategoryModel());

		mockMvc.perform(post("/businessCategory/_create").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("businessCategoryRequestCreate.json"))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("businessCategoryResponseCreate.json")));

	}

	@Test
	public void test_should_update_business_category_when_data_is_specified() throws Exception {
		when(serviceCategoryService.findByCodeAndTenantId("serviceCode123", "default"))
				.thenReturn(getOldServiceCategoryEntity());
		when(serviceCategoryService.update(getServiceCategory())).thenReturn(getServiceCategoryModel());

		mockMvc.perform(post("/businessCategory/{businessCategoryCode}/_update", "serviceCode123")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(getFileContents("businessCategoryRequestUpdate.json"))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("businessCategoryResponseUpdate.json")));

	}

	@Test
	public void test_should_get_all_business_categories_with_the_params_criteria() throws Exception {
		when(serviceCategoryService.findAll(getServiceCategorySearchCriteria(), "code:desc"))
				.thenReturn(getModelServiceCategoriesForSearch());
		mockMvc.perform(
				post("/businessCategory/_search?isactive=true&tenantId=default&ids=1,2,3&sort=code:desc&fields=code,tenantId")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(getFileContents("businessCategoryRequest.json")))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("businessCategoryResponse.json")));

	}

	private List<org.egov.collection.domain.model.ServiceCategory> getModelServiceCategoriesForSearch() {
		org.egov.collection.domain.model.ServiceCategory service1 = org.egov.collection.domain.model.ServiceCategory
				.builder().id(2L).code("TL").name("Trade Licence").isactive(true).tenantId("default").build();
		org.egov.collection.domain.model.ServiceCategory service2 = org.egov.collection.domain.model.ServiceCategory
				.builder().id(3L).code("STAX").name("Sewarage Tax").isactive(true).tenantId("default").build();
		org.egov.collection.domain.model.ServiceCategory service3 = org.egov.collection.domain.model.ServiceCategory
				.builder().id(1L).code("ADTAX").name("Advertisement Tax").isactive(true).tenantId("default").build();
		return Arrays.asList(service1, service2, service3);
	}

	private ServiceCategorySearchCriteria getServiceCategorySearchCriteria() {
		List<Long> Ids = Arrays.asList(1L, 2L, 3L);
		return ServiceCategorySearchCriteria.builder().isactive(true).tenantId("default").ids(Ids).build();

	}

	private ServiceCategory getOldServiceCategoryEntity() {
		return ServiceCategory.builder().code("serviceCode123").isactive(true).name("serviceName123")
				.tenantId("default").build();

	}

	private org.egov.collection.domain.model.ServiceCategory getServiceCategoryModel() {
		return org.egov.collection.domain.model.ServiceCategory.builder().code("serviceCode").name("serviceName")
				.isactive(true).tenantId("default").build();

	}

	private ServiceCategory getServiceCategory() {
		ServiceCategory serviceCategory = ServiceCategory.builder().code("serviceCode").name("serviceName")
				.isactive(true).tenantId("default").build();
		serviceCategory.setCreatedBy(1L);
		serviceCategory.setCreatedDate(new Date());
		serviceCategory.setLastModifiedBy(1L);
		serviceCategory.setLastModifiedDate(new Date());
		return serviceCategory;
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
