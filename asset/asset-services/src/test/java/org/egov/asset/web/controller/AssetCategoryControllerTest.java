package org.egov.asset.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.validator.AssetCategoryValidator;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetCategoryController.class)
public class AssetCategoryControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AssetCategoryService  assetCategoryService;
	
	@MockBean
	private ApplicationProperties applicationProperties;
	
	@MockBean
	private AssetCategoryValidator assetCategoryValidator;

	@Test
	public void test_Should_Search_AssetCategory() throws Exception{
		
		List<AssetCategory> assetCategories = new ArrayList<>();
		assetCategories.add(getAssetCategory());
		
		when(assetCategoryService.search(any(AssetCategoryCriteria.class))).thenReturn(assetCategories); 

		mockMvc.perform(post("/assetCategories/_search")
	        		.param("tenantId", "ap.kurnool")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("requestinfowrapper.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("assetcategorysearchresponse.json")));
	}
	
	@Test
	public void test_Should_Create_AssetCategory() throws Exception{
		
		List<AssetCategory> assetCategories = new ArrayList<>();
		assetCategories.add(getAssetCategory());
		AssetCategoryResponse assetCategoryResponse = new AssetCategoryResponse();
		assetCategoryResponse.setAssetCategory(assetCategories);
		assetCategoryResponse.setResponseInfo(new ResponseInfo());
		
		when(assetCategoryService.createAsync(any(AssetCategoryRequest.class))).thenReturn(assetCategoryResponse); 
		when(applicationProperties.getAssetCategoryAsync()).thenReturn(true);

		mockMvc.perform(post("/assetCategories/_create")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("assetcategorycreaterequest.json")))
	                .andExpect(status().isCreated())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("assetcategorysearchresponse.json")));
	}

	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
	
	private AssetCategory getAssetCategory() {
		
		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setTenantId("ap.kurnool");
		assetCategory.setVersion("v1");
		assetCategory.setName("assetcategory3");
		assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
		assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
		assetCategory.setIsAssetAllow(true);
		assetCategory.setAssetAccount(2l);
		
		return assetCategory;
	}

}
