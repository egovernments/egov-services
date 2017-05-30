package org.egov.wcms.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.wcms.util.FileUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.service.PropertyCategoryService;
import org.egov.wcms.web.contract.PropertyCategoryGetRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypesRes;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.controller.PropertyCategoryController;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PropertyCategoryController.class)
@WebAppConfiguration
public class PropertyCategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ApplicationProperties applicationProperties;
	
	@MockBean
	private PropertyCategoryService propertyCategoryService;
	
	@MockBean
    private ErrorHandler errHandler;
	
    @MockBean
    private ResponseInfoFactory responseInfoFactory;
    
  /*  @MockBean
    private ResponseInfo responseInfo; */
    
    @InjectMocks
    private PropertyCategoryController propertyCategoryController;

	
	
	@Test(expected = Exception.class)
	public void test_Should_Search_PropertyCategory() throws Exception{
		
		List<PropertyTypeCategoryType> propertyCategories = new ArrayList<>();
		PropertyTypeCategoryTypesRes propertyCategoryResponse = new PropertyTypeCategoryTypesRes();
		RequestInfo requestInfo = new RequestInfo();
        ResponseInfo responseInfo = new ResponseInfo();
		PropertyTypeCategoryType propertyCategory = new PropertyTypeCategoryType();
		propertyCategory.setActive(true);
		propertyCategory.setCategoryTypeName("category");
		propertyCategory.setPropertyTypeName("property");
		propertyCategory.setTenantId("1");
		
		propertyCategories.add(propertyCategory);
		
		PropertyCategoryGetRequest propertyCategoryGetRequest = Mockito.mock(PropertyCategoryGetRequest.class);
		
		
		when(propertyCategoryService.getPropertyCategories(propertyCategoryGetRequest)).thenReturn(propertyCategoryResponse); 
		when(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true)).thenReturn(responseInfo);
		
		mockMvc.perform(post("/property/category/_search")
	        		.param("tenantId", "1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("requestinfowrapper.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("propertycategoryresponse.json")));
	}
	
	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
}
