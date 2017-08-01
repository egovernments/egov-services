package org.egov.tradelicense.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.enums.FeeTypeEnum;
import org.egov.enums.RateTypeEnum;
import org.egov.models.AuditDetails;
import org.egov.models.Category;
import org.egov.models.CategoryDetail;
import org.egov.models.CategoryRequest;
import org.egov.models.CategoryResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.UserInfo;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.exception.DuplicateIdException;
import org.egov.tradelicense.services.CategoryService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TradeLicenseApplication.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryServiceTest {

	@Autowired
	CategoryService categoryService;

	@Autowired
	private PropertiesManager propertiesManager;

	public static Long categoryId = 1l;
	public String tenantId = "default";
	public String name = "Flammables";
	public String code = "Flammables";
	public String type = "CATEGORY";
	public String updatedName = "Flammables v1.1 name updated";
	public String updatedCode = "Flammables v1.1 code updated";

	@Test
	public void testAcreateCategory() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setTenantId(tenantId);
		category.setName(name);
		category.setCode(code);
		category.setParentId(null);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		category.setAuditDetails(auditDetails);
		categories.add(category);

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setCategories(categories);
		categoryRequest.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.createCategoryMaster(categoryRequest);
			if (categoryResponse.getCategories().size() == 0) {
				assertTrue(false);
			}
			categoryId = categoryResponse.getCategories().get(0).getId();

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testAsearchCategory() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId,
					new Integer[] { categoryId.intValue() }, name, code, type, pageSize, offset);
			
			if (categoryResponse.getCategories().size() == 0){
					assertTrue(false);
			}
				

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testAcreateDuplicateCategory() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setTenantId(tenantId);
		category.setName(name);
		category.setCode(code);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		category.setAuditDetails(auditDetails);
		categories.add(category);

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setCategories(categories);
		categoryRequest.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.createCategoryMaster(categoryRequest);
			if (categoryResponse.getCategories().size() == 0) {
				assertTrue(false);
			}
			categoryId = categoryResponse.getCategories().get(0).getId();

			assertTrue(true);

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}
	
	
	
	@Test
	public void testzsearchCategoryDetails() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId,
					new Integer[] { categoryId.intValue() }, name, code, "SUBCATEGORY", pageSize, offset);
			if (categoryResponse.getCategories().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	
	@Test
	public void testCycreateCategoryDetails() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setTenantId(tenantId);
		category.setName(name);
		category.setCode(code);
		category.setParentId(categoryId);
		
		
		 CategoryDetail details = new CategoryDetail();
	        details.setId(Long.valueOf(5));
	        details.setCategoryId(categoryId);
	        details.setFeeType(FeeTypeEnum.fromValue("License"));
	        details.setRateType(RateTypeEnum.fromValue("Flat_By_Percentage"));
	        details.setUomId(Long.valueOf(1));
	        
	        List<CategoryDetail> catDetails = new ArrayList<CategoryDetail>();
	        catDetails.add(details);
	        
	        category.setDetails(catDetails);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		category.setAuditDetails(auditDetails);
		categories.add(category);

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setCategories(categories);
		categoryRequest.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.createCategoryMaster(categoryRequest);
			if (categoryResponse.getCategories().size() == 0) {
				assertTrue(false);
			}
			this.categoryId = categoryResponse.getCategories().get(0).getId();

			assertTrue(true);

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}
	
	
	@Test
	public void testCyxcreateduplicateCategoryDetails() {
		RequestInfo requestInfo = getRequestInfoObject();

		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setTenantId(tenantId);
		category.setName(name);
		category.setCode(code);
		category.setParentId(categoryId);
		
		
		 CategoryDetail details = new CategoryDetail();
	        details.setId(Long.valueOf(5));
	        details.setCategoryId(categoryId);
	        details.setFeeType(FeeTypeEnum.fromValue("License"));
	        details.setRateType(RateTypeEnum.fromValue("Flat_By_Percentage"));
	        details.setUomId(Long.valueOf(1));
	        
	        List<CategoryDetail> catDetails = new ArrayList<CategoryDetail>();
	        catDetails.add(details);
	        
	        category.setDetails(catDetails);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		category.setAuditDetails(auditDetails);
		categories.add(category);

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setCategories(categories);
		categoryRequest.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.createCategoryMaster(categoryRequest);
			if (categoryResponse.getCategories().size() == 0) {
				assertTrue(false);
			}
			this.categoryId = categoryResponse.getCategories().get(0).getId();

			assertTrue(true);

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	@Test
	public void testBmodifyCategoryName() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setId(categoryId);
		category.setTenantId(tenantId);
		category.setName(updatedName);
		category.setCode(code);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		category.setAuditDetails(auditDetails);
		categories.add(category);

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setCategories(categories);
		categoryRequest.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.updateCategoryMaster(categoryRequest);

			if (categoryResponse.getCategories().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	@Test
	public void testBsearchUpdatedCategoryName() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId,
					new Integer[] { categoryId.intValue() }, updatedName, code, type, pageSize, offset);
			if (categoryResponse.getCategories().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testCmodifyCategoryCode() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setId(categoryId);
		category.setTenantId(tenantId);
		category.setName(updatedName);
		category.setCode(updatedCode);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		category.setAuditDetails(auditDetails);
		categories.add(category);

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setCategories(categories);
		categoryRequest.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.updateCategoryMaster(categoryRequest);

			if (categoryResponse.getCategories().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	@Test
	public void testCmodifyDuplicateCategoryCode() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setId(categoryId);
		category.setTenantId(tenantId);
		category.setName(updatedName);
		category.setCode(updatedCode);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("pavan");
		auditDetails.setLastModifiedBy("pavan");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		category.setAuditDetails(auditDetails);
		categories.add(category);

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setCategories(categories);
		categoryRequest.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.updateCategoryMaster(categoryRequest);

			if (categoryResponse.getCategories().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	@Test
	public void testCsearchUpdatedCategoryCode() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId,
					new Integer[] { categoryId.intValue() }, updatedName, updatedCode, type, pageSize, offset);
			if (categoryResponse.getCategories().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	private RequestInfo getRequestInfoObject() {
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setApiId("emp");
		requestInfo.setVer("1.0");
		requestInfo.setTs(new Long(122366));
		requestInfo.setDid("1");
		requestInfo.setKey("abcdkey");
		requestInfo.setMsgId("20170310130900");
		requestInfo.setRequesterId("rajesh");
		requestInfo.setAuthToken("b5da31a4-b400-4d6e-aa46-9ebf33cce933");
		UserInfo userInfo = new UserInfo();
		String username = "pavan";
		userInfo.setUsername(username);
		requestInfo.setUserInfo(userInfo);

		return requestInfo;
	}
}