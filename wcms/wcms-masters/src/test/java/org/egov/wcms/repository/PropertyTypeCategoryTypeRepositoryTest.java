package org.egov.wcms.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.repository.builder.PropertyTypeCategoryTypeQueryBuilder;
import org.egov.wcms.repository.rowmapper.PropertyCategoryRowMapper;
import org.egov.wcms.service.PropertyCategoryService;
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypeReq;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.controller.PropertyCategoryController;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PropertyCategoryController.class)
@WebAppConfiguration
public class PropertyTypeCategoryTypeRepositoryTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private PropertyTypeCategoryTypeQueryBuilder propertyCategoryueryBuilder;
	
	@Mock
    private PropertyCategoryRowMapper propertyCategoryRowMapper;
	
	@MockBean
	private PropertyCategoryService propertyCAtegoryService;
	
	@MockBean
    private ErrorHandler errHandler;
	
	@MockBean
	private ApplicationProperties applicationProperties;
	
    @MockBean
    private ResponseInfoFactory responseInfoFactory;
	    
	@InjectMocks
	private PropertyTypeCategoryTypeRepository propertyCategoryRepository;

	
	
	@Test
	public void test_Should_Find_PropertyCategory() throws Exception{
        List<Object> preparedStatementValues = new ArrayList<Object>();
		List<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
		PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
		propertyCategory.setActive(true);
		propertyCategory.setCategoryTypeName("category");
		propertyCategory.setPropertyTypeName("property");
		propertyCategory.setTenantId("1");
		
		propertyCategories.add(propertyCategory);
        
        PropertyCategoryGetRequest propertyCategoryRequest = Mockito.mock(PropertyCategoryGetRequest.class);
        when(propertyCategoryueryBuilder.getQuery(propertyCategoryRequest, preparedStatementValues)).thenReturn("query");
        when(jdbcTemplate.query("query", preparedStatementValues.toArray(), propertyCategoryRowMapper)).thenReturn(propertyCategories);
                
		assertNotNull(propertyCategoryRepository.findForCriteria(propertyCategoryRequest));
	}
	
	@Test
	public void test_Inavalid_Find_PropertyCategory() throws Exception{
        List<Object> preparedStatementValues = new ArrayList<Object>();
		List<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
		PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
		propertyCategory.setActive(true);
		propertyCategory.setCategoryTypeName("category");
		propertyCategory.setPropertyTypeName("property");
		propertyCategory.setTenantId("1");
		
		propertyCategories.add(propertyCategory);
        
        PropertyCategoryGetRequest propertyCategoryRequest = Mockito.mock(PropertyCategoryGetRequest.class);
        when(propertyCategoryueryBuilder.getQuery(propertyCategoryRequest, preparedStatementValues)).thenReturn(null);
        when(jdbcTemplate.query("query", preparedStatementValues.toArray(), propertyCategoryRowMapper)).thenReturn(propertyCategories);
                
		assertTrue(!propertyCategories.equals(propertyCategoryRepository.findForCriteria(propertyCategoryRequest)));
	}
	
	@Test(expected = Exception.class) //check
	public void test_Should_Create_PropertyCategory() throws Exception{
		List<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
		PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
		propertyCategory.setActive(true);
		propertyCategory.setCategoryTypeName("category");
		propertyCategory.setPropertyTypeName("property");
		propertyCategory.setTenantId("1");
		
		propertyCategories.add(propertyCategory);
		
		Object[] obj = new Object[] {};
        
        PropertyTypeCategoryTypeReq propertyCategoryRequest = Mockito.mock(PropertyTypeCategoryTypeReq.class);
        when(jdbcTemplate.update("query", obj)).thenReturn(1);
                
		assertNotNull(propertyCategoryRepository.persistCreatePropertyCategory(propertyCategoryRequest));
	}
	
	    @Test(expected = Exception.class)
	    public void test_Should_Update_PropertyCategory() throws Exception {
	        final List<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
	        final PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
	        propertyCategory.setActive(true);
	        propertyCategory.setCategoryTypeName("category");
	        propertyCategory.setPropertyTypeName("property");
	        propertyCategory.setTenantId("1");

	        propertyCategories.add(propertyCategory);

	        final Object[] obj = new Object[] {};

	        final PropertyTypeCategoryTypeReq propertyCategoryRequest = Mockito.mock(PropertyTypeCategoryTypeReq.class);
	        when(jdbcTemplate.update("query", obj)).thenReturn(1);

	        assertNotNull(propertyCategoryRepository.persistUpdatePropertyCategory(propertyCategoryRequest));
	    }

}