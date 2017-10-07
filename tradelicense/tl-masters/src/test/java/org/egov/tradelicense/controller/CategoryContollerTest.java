package org.egov.tradelicense.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.Category;
import org.egov.tl.commons.web.contract.CategoryDetail;
import org.egov.tl.commons.web.contract.CategoryDetailSearch;
import org.egov.tl.commons.web.contract.CategorySearch;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.enums.FeeTypeEnum;
import org.egov.tl.commons.web.contract.enums.RateTypeEnum;
import org.egov.tl.commons.web.requests.CategoryRequest;
import org.egov.tl.commons.web.response.CategoryResponse;
import org.egov.tl.commons.web.response.CategorySearchResponse;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.services.CategoryService;
import org.egov.tradelicense.web.controller.CategoryController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
@ContextConfiguration(classes = { TradeLicenseApplication.class })
public class CategoryContollerTest {

	@MockBean
	private CategoryService categoryService;

	@MockBean
	private PropertiesManager propertiesManager;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	KafkaTemplate kafkaTemplate;

	/**
	 * Description : Test method for createCategory() method
	 * 
	 */
	@Test
	public void testCreateCategory() throws Exception {

		List<Category> categories = new ArrayList<>();
		Category category = new Category();
		category.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		category.setAuditDetails(auditDetails);

		CategoryResponse categoryResponse = new CategoryResponse();
		categories.add(category);

		categoryResponse.setResponseInfo(new ResponseInfo());
		categoryResponse.setCategories(categories);

		try {

			when(categoryService.createCategoryMaster(any(CategoryRequest.class),any(String.class))).thenReturn(categoryResponse);

			mockMvc.perform(post("/category/v1/_create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("categoryCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("categoryCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description : Test method for createCategoryDetails() method
	 * 
	 */
	@Test
	public void testCreateCategoryDetails() throws Exception {

		List<Category> categories = new ArrayList<>();
		Category category = new Category();
		category.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		category.setAuditDetails(auditDetails);

		CategoryDetail categoryDetail = new CategoryDetail();

		List<CategoryDetail> categoryDetails = new ArrayList<>();
		categoryDetails.add(categoryDetail);
		category.setDetails(categoryDetails);
		categories.add(category);

		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setResponseInfo(new ResponseInfo());
		categoryResponse.setCategories(categories);

		try {

			when(categoryService.createCategoryMaster(any(CategoryRequest.class),any(String.class))).thenReturn(categoryResponse);

			mockMvc.perform(
					post("/category/v1/_create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("categoryDetailsCreateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("categoryDetailsCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description : Test method for updateCategory() method
	 */
	@Test
	public void testUpdateCategory() throws Exception {

		CategoryResponse categoryResponse = new CategoryResponse();
		List<Category> categories = new ArrayList<>();
		Category category = new Category();
		category.setTenantId("default");
		category.setParent("test");

		AuditDetails auditDetails = new AuditDetails();
		category.setAuditDetails(auditDetails);
		category.setName("Flammables");

		categories.add(category);

		categoryResponse.setResponseInfo(new ResponseInfo());
		categoryResponse.setCategories(categories);

		try {

			when(categoryService.updateCategoryMaster(any(CategoryRequest.class),any(String.class))).thenReturn(categoryResponse);
			mockMvc.perform(post("/category/v1/_update")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("categoryUpdateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("categoryUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description : Test method for updateCategoryDetials() method
	 */
	@Test
	public void testUpdateCategoryDetails() throws Exception {

		CategoryResponse categoryResponse = new CategoryResponse();
		List<Category> categories = new ArrayList<>();
		Category category = new Category();
		category.setTenantId("default");
		category.setParent("test");

		AuditDetails auditDetails = new AuditDetails();
		category.setAuditDetails(auditDetails);
		category.setName("Flammables");

		CategoryDetail details = new CategoryDetail();
		details.setId(Long.valueOf(5));
		details.setCategory("10");
		details.setFeeType(FeeTypeEnum.fromValue("License"));
		details.setRateType(RateTypeEnum.fromValue("Flat_By_Percentage"));
		details.setUom("1");

		List<CategoryDetail> catDetails = new ArrayList<CategoryDetail>();
		catDetails.add(details);

		category.setDetails(catDetails);

		categories.add(category);

		categoryResponse.setResponseInfo(new ResponseInfo());
		categoryResponse.setCategories(categories);

		try {

			when(categoryService.updateCategoryMaster(any(CategoryRequest.class),any(String.class))).thenReturn(categoryResponse);
			mockMvc.perform(post("/category/v1/_update").param("tenantId", "default")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("categoryUpdateRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("categoryUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description : Test method for searchCategory() method
	 */
	@Test
	public void testSearchCategory() throws Exception {

		CategorySearchResponse categoryResponse = new CategorySearchResponse();
		List<CategorySearch> categories = new ArrayList<>();
		CategorySearch category = new CategorySearch();
		category.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		category.setAuditDetails(auditDetails);

		categories.add(category);

		categoryResponse.setResponseInfo(new ResponseInfo());
		categoryResponse.setCategories(categories);

		try {

			when(categoryService.getCategoryMaster(any(RequestInfo.class), any(String.class), any(Integer[].class), any(String[].class),
					any(String.class), any(String.class), any(String.class), any(String.class), any(String.class),
					any(String.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class))).thenReturn(categoryResponse);

			mockMvc.perform(post("/category/v1/_search").param("tenantId", "default").param("type", "SUBCATEGORY")
					.contentType(MediaType.APPLICATION_JSON).content(getFileContents("categorySearchRequest.json")))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("categorySearchResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description : Test method for searchCategoryDetails() method
	 */
	@Test
	public void testSearchCategoryDetails() throws Exception {

		CategorySearchResponse categoryResponse = new CategorySearchResponse();
		List<CategorySearch> categories = new ArrayList<>();
		CategorySearch category = new CategorySearch();
		category.setTenantId("default");
		category.setParent("test");

		AuditDetails auditDetails = new AuditDetails();
		category.setAuditDetails(auditDetails);

		categories.add(category);
		CategoryDetailSearch details = new CategoryDetailSearch();
		details.setId(Long.valueOf(5));

		List<CategoryDetailSearch> catDetails = new ArrayList<CategoryDetailSearch>();
		catDetails.add(details);
		category.setDetails(catDetails);
		categoryResponse.setResponseInfo(new ResponseInfo());
		categoryResponse.setCategories(categories);

		try {

			when(categoryService.getCategoryMaster(any(RequestInfo.class), any(String.class), any(Integer[].class), any(String[].class),
					any(String.class), any(String.class), any(String.class), any(String.class), any(String.class),
					any(String.class), any(String.class), any(String.class), any(Integer.class), any(Integer.class))).thenReturn(categoryResponse);

			mockMvc.perform(post("/category/v1/_search").param("tenantId", "default").param("type", "SUBCATEGORY")
					.contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("categoryDetailsSearchRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("categoryDetailsSearchResponse.json")));

		} catch (Exception e) {
			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}
		assertTrue(Boolean.TRUE);

	}

	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}
}