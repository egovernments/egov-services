package org.egov.wcms.service;


import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.producers.PropertyCategoryProducer;
import org.egov.wcms.repository.PropertyCategoryRepository;
import org.egov.wcms.service.PropertyCategoryService;
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.egov.wcms.web.contract.PropertyCategoryRequest;
import org.egov.wcms.web.contract.PropertyCategoryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(PropertyCategoryService.class)
@WebAppConfiguration
public class PropertyCategoryServiceTest {
	
	
	@Mock
	private ApplicationProperties applicationProperties;
    
	@Mock
	private PropertyCategoryRepository propertyCategoryRepository;
    
	@Mock
	private PropertyCategoryProducer propertyCategoryProducer;
	
	
    @InjectMocks
	private PropertyCategoryService propertyCategoryService;
    
	@Test
	public void test_Should_Find_PropertyCategory() throws Exception{
        		
        PropertyCategoryRequest propertyCategoryRequest = new PropertyCategoryRequest();
        doReturn(propertyCategoryRequest).when(propertyCategoryRepository).persistCreatePropertyCategory(propertyCategoryRequest);
                
		assertTrue(propertyCategoryRequest.equals(propertyCategoryService.create(propertyCategoryRequest)));
	}
    
    @Test(expected = Exception.class)
	public void test_throwException_Find_PropertyCategory() throws Exception{
        		
        PropertyCategoryRequest propertyCategoryRequest = Mockito.mock(PropertyCategoryRequest.class);
        when(propertyCategoryRepository.persistCreatePropertyCategory(propertyCategoryRequest)).thenThrow(Exception.class);
                
		assertTrue(propertyCategoryRequest.equals(propertyCategoryService.create(propertyCategoryRequest)));
	}
    
	@Test
	public void test_Should_Get_PropertyCategories() throws Exception{
        
		PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
		PropertyCategoryResponse propertyCategoryResponse = new PropertyCategoryResponse();
		propertyCategory.setPropertyTypeName("propertyType");
		propertyCategory.setCategoryTypeName("categoryType");
		propertyCategory.setActive(true);
		propertyCategory.setTenantId("1");
		
		ArrayList<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
		propertyCategories.add(propertyCategory);
		
        PropertyCategoryGetRequest propertyCategoryGetRequest = Mockito.mock(PropertyCategoryGetRequest.class);
        doReturn(propertyCategoryResponse).when(propertyCategoryRepository).findForCriteria(propertyCategoryGetRequest);
                
		assertNotNull(propertyCategoryService.getPropertyCategories(propertyCategoryGetRequest));
	}
    
    @Test(expected = Exception.class)
	public void test_throwException_Get_PropertyCategories() throws Exception{
        
    	PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
		propertyCategory.setPropertyTypeName("propertyType");
		propertyCategory.setCategoryTypeName("categoryType");
		propertyCategory.setActive(true);
		propertyCategory.setTenantId("1");
		
		ArrayList<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
		propertyCategories.add(propertyCategory);
		
        PropertyCategoryGetRequest propertyCategoryGetRequest = Mockito.mock(PropertyCategoryGetRequest.class);
        when(propertyCategoryRepository.findForCriteria(propertyCategoryGetRequest)).thenThrow(Exception.class);
                
		assertTrue(propertyCategories.equals(propertyCategoryService.getPropertyCategories(propertyCategoryGetRequest)));
	}
    
  /*  @Test
	public void test_Should_Create_PropertyCategory() throws Exception{
        
    	PropertyCategoryProducer propertyCategoryProducer = new PropertyCategoryProducer();
    	PropertyCategoryProducer spy = Mockito.spy(propertyCategoryProducer);
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    	PropertyCategoryRequest propertyCategoryRequest = Mockito.mock(PropertyCategoryRequest.class);
        
    	doNothing().when(spy).sendMessage("topic", "key", "propertyCategory");
    	
		assertTrue(propertyCategoryRequest.equals(propertyCategoryService.createPropertyCategory("topic", "key", propertyCategoryRequest)));
	}
    
    @Test(expected = Exception.class)
	public void test_throwException_Create_PropertyCategory() throws Exception{
	 	ObjectMapper mapper = new ObjectMapper();
    	PropertyCategoryRequest propertyCategoryRequest = Mockito.mock(PropertyCategoryRequest.class);
        
    	when(mapper.writeValueAsString(propertyCategoryRequest)).thenReturn("propertyCategory");
    	doNothing().when(propertyCategoryProducer).sendMessage("topic", "key", "propertyCategory");
    	
		assertTrue(propertyCategoryRequest.equals(propertyCategoryService.createPropertyCategory("topic", "key", propertyCategoryRequest)));

    	
    	                
	} */
    

}
