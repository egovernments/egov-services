package org.egov.tradelicense.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.egov.enums.FeeTypeEnum;
import org.egov.enums.RateTypeEnum;
import org.egov.models.AuditDetails;
import org.egov.models.Category;
import org.egov.models.CategoryDetail;
import org.egov.models.CategoryRequest;
import org.egov.models.CategoryResponse;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.models.UOM;
import org.egov.models.UOMRequest;
import org.egov.models.UOMResponse;
import org.egov.models.UserInfo;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.consumers.CategoryConsumer;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.services.CategoryService;
import org.egov.tradelicense.domain.services.UOMService;
import org.egov.tradelicense.persistence.repository.CategoryRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { TradeLicenseApplication.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext
@SuppressWarnings("rawtypes")
public class CategoryServiceTest {

	@Autowired
	CategoryService categoryService;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	UOMService uomRepository;

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	CategoryConsumer categoryConsumer;
	 
	 @ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "category-create-validated","category-update-validated");
	
	 @Autowired
	  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	public static Long categoryId = 1l;
	public Integer parentId = null;
	public String tenantId = "default";
	public String name = "Flammables";
	public String code = "Flammables";
	public String active = "True";
	public String type = "CATEGORY";
	public String updatedName = "Flammables v1.1 name updated";
	public String updatedCode = "Flammables v1.1 code updated";
	public String subCatName = "Flammables2";
	public String subCatCode = "Flammables2";
	public static UOMResponse uomResponse;
	public static Long uomId = 0L;
	public static Integer searchCategoryId = 1;
	
	 @Before
	  public void setUp() throws Exception {
	    // wait until the partitions are assigned
	    for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
	        .getListenerContainers()) {
	      ContainerTestUtils.waitForAssignment(messageListenerContainer,
	          embeddedKafka.getPartitionsPerTopic());
	    }
	  }
	@SuppressWarnings("unchecked")
	public void insertvalues() {
		try {
			UOM uom = new UOM();
			uom.setTenantId("default");
			uom.setName("shubham");
			uom.setCode("nitin");
			uom.setActive(true);
			long createdTime = new Date().getTime();

			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("1");
			auditDetails.setLastModifiedBy("1");
			auditDetails.setCreatedTime(createdTime);
			auditDetails.setLastModifiedTime(createdTime);

			uom.setAuditDetails(auditDetails);
			RequestInfo requestInfo = getRequestInfoObject();
			UOMRequest uomRequest = new UOMRequest();
			List uoms = new ArrayList<UOM>();
			uoms.add(uom);
			uomRequest.setUoms(uoms);
			uomRequest.setRequestInfo(requestInfo);
			uomResponse = uomRepository.createUomMaster(uomRequest);
			uomId = uomResponse.getUoms().get(0).getId();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Description : test method for service createCategory master
	 */
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
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
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
			
		    categoryConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
		    if( categoryConsumer.getLatch().getCount() != 0){
		    	assertTrue(false);
		    }else{
				Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
				Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
				 categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId,
						null, name, code, active, type, null, pageSize, offset);

				if (categoryResponse.getCategories().size() == 0) {
					assertTrue(false);
				}else{
					categoryId = categoryResponse.getCategories().get(0).getId();
					assertTrue(true);
				}
//		    	assertTrue(true);
		    }
		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : test method for service searchCategory Master
	 * 
	 */
	@Test
	public void testAsearchCategory() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId,
					new Integer[] { categoryId.intValue() }, name, code, active, type, parentId, pageSize, offset);

			if (categoryResponse.getCategories().size() == 0) {
				assertTrue(false);
			}

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : test method for service createCategory master to check
	 * DuplicateRecord check
	 */
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
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
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

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	/**
	 * Description : test method for service createCategory Details Master
	 * 
	 */
	@Test
	public void testBcreateSubCategory() {
		try {
			this.insertvalues();
			RequestInfo requestInfo = getRequestInfoObject();
			List<Category> categories = new ArrayList<>();

			Category category = new Category();
			category.setTenantId(tenantId);
			category.setName(subCatName);
			category.setCode(subCatCode);
			category.setParentId(categoryId);

			CategoryDetail details = new CategoryDetail();
			details.setId(Long.valueOf(1));
			details.setCategoryId(categoryId);
			details.setFeeType(FeeTypeEnum.fromValue("License"));
			details.setRateType(RateTypeEnum.fromValue("Flat_By_Percentage"));
			details.setUomId(uomId);

			List<CategoryDetail> catDetails = new ArrayList<CategoryDetail>();
			catDetails.add(details);

			category.setDetails(catDetails);
			long createdTime = new Date().getTime();

			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy("1");
			auditDetails.setLastModifiedBy("1");
			auditDetails.setCreatedTime(createdTime);
			auditDetails.setLastModifiedTime(createdTime);

			category.setAuditDetails(auditDetails);
			categories.add(category);

			CategoryRequest categoryRequest = new CategoryRequest();
			categoryRequest.setCategories(categories);
			categoryRequest.setRequestInfo(requestInfo);
			
			categoryConsumer.resetCountDown();
			
			CategoryResponse categoryResponse = categoryService.createCategoryMaster(categoryRequest);
			if (categoryResponse.getCategories().size() == 0) {
				assertTrue(false);
			}
			
			categoryConsumer.getLatch().await();
		    if( categoryConsumer.getLatch().getCount() != 0){
		    	assertTrue(false);
		    }else{
		    	categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId,
						null, null, null, null, "SUBCATEGORY", null, null, null);

				if (categoryResponse.getCategories().size() == 0) {
					assertTrue(false);
				}else{
					 categoryResponse.getCategories().get(0).getId();
					assertTrue(true);
				}
		    	
		    }


		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	/**
	 * Description : test method for service searchCategory Details Master
	 * 
	 */
	@Test
	public void testBsearchSubCategory() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId, null,
					subCatName, subCatCode, active, "SUBCATEGORY", null, pageSize, offset);
			if (categoryResponse.getCategories().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : test method for service createCategory Details Master
	 * 
	 */
	@Test
	public void testBcreateSubCategoryDuplicate() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setTenantId(tenantId);
		category.setName(subCatName);
		category.setCode(subCatCode);
		category.setParentId(categoryId);

		CategoryDetail details = new CategoryDetail();
		details.setId(Long.valueOf(1));
		details.setCategoryId(categoryId);
		details.setFeeType(FeeTypeEnum.fromValue("License"));
		details.setRateType(RateTypeEnum.fromValue("Flat_By_Percentage"));
		details.setUomId(uomId);

		List<CategoryDetail> catDetails = new ArrayList<CategoryDetail>();
		catDetails.add(details);

		category.setDetails(catDetails);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
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

			assertTrue(true);

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	/**
	 * Description : test method for service UpdateCategory Details Master to
	 * check modify name
	 * 
	 */
	@Test
	public void testCmodifyCategoryName() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setId(categoryId);
		category.setTenantId(tenantId);
		category.setName(updatedName);
		category.setCode(code);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		category.setAuditDetails(auditDetails);
		categories.add(category);

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setCategories(categories);
		categoryRequest.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.updateCategoryMaster(categoryRequest);

			if (categoryResponse.getCategories().size() == 0) {
				assertTrue(false);
			}

			categoryConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
		    if( categoryConsumer.getLatch().getCount() != 0){
		    	assertTrue(false);
		    }else{
				assertTrue(true);
		    	
		    }

		} catch (Exception e) {
			if (e.getClass().isInstance(new DuplicateIdException())) {
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}

	}

	/**
	 * Description : test method for service updateCategory Details Master to
	 * check modify name
	 */
	@Test
	public void testCsearchUpdatedCategoryName() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId,
					null, updatedName, null, active, null, parentId, pageSize,
					offset);
			if (categoryResponse.getCategories().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : test method for service updateCategory Details Master to
	 * check modify code
	 */
	@Test
	public void testDmodifyCategoryCode() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setId(categoryId);
		category.setTenantId(tenantId);
		category.setName(updatedName);
		category.setCode(updatedCode);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);

		category.setAuditDetails(auditDetails);
		categories.add(category);

		CategoryRequest categoryRequest = new CategoryRequest();
		categoryRequest.setCategories(categories);
		categoryRequest.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.updateCategoryMaster(categoryRequest);

			if (categoryResponse.getCategories().size() == 0) {
				assertTrue(false);
			}

			categoryConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
		    if( categoryConsumer.getLatch().getCount() != 0){
		    	assertTrue(false);
		    }else{
				assertTrue(true);
		    	
		    }
			

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : test method for service updateCategory Details Master to
	 * check modify code
	 */
	@Test
	public void testDmodifyDuplicateCategoryCode() {
		RequestInfo requestInfo = getRequestInfoObject();
		List<Category> categories = new ArrayList<>();

		Category category = new Category();
		category.setId(categoryId);
		category.setTenantId(tenantId);
		category.setName(updatedName);
		category.setCode(updatedCode);
		long createdTime = new Date().getTime();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy("1");
		auditDetails.setLastModifiedBy("1");
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

	/**
	 * Description : test method for service updateCategory Details Master to
	 * check modify code
	 */
	@Test
	public void testDsearchUpdatedCategoryCode() {

		Integer pageSize = Integer.valueOf(propertiesManager.getDefaultPageSize());
		Integer offset = Integer.valueOf(propertiesManager.getDefaultOffset());
		RequestInfo requestInfo = getRequestInfoObject();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		try {
			CategoryResponse categoryResponse = categoryService.getCategoryMaster(requestInfo, tenantId,
					new Integer[] { categoryId.intValue() }, updatedName, updatedCode, active, type, parentId, pageSize,
					offset);
			if (categoryResponse.getCategories().size() == 0)
				assertTrue(false);

			assertTrue(true);

		} catch (Exception e) {
			assertTrue(false);
		}

	}

	/**
	 * Description : method to create requestInfo Object
	 */
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
		Integer userId = 1;
		userInfo.setUsername(username);
		userInfo.setId(userId);
		requestInfo.setUserInfo(userInfo);

		return requestInfo;
	}
}