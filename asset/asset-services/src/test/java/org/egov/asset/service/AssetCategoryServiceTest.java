package org.egov.asset.service;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.AssetCategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AssetCategoryServiceTest {
	
	@Mock
	private AssetCategoryRepository assetCategoryRepository;
	
	@Mock
	private AssetProducer assetProducer;
	
	@Mock
	private ApplicationProperties applicationProperties;
	
	@InjectMocks
	private AssetCategoryService assetCategoryService;
	
	@Mock
	private ObjectMapper objectMapper;

	@Test
	public void testSearch() {
		List<AssetCategory> assetCategories = new ArrayList<>();
		assetCategories.add(getAssetCategory());
		
		when(assetCategoryRepository.search(any(AssetCategoryCriteria.class))).thenReturn(assetCategories);
		
		assertTrue(assetCategories.equals(assetCategoryService.search(any(AssetCategoryCriteria.class))));
	}
	
	/*@Test
	public void testCreate() {
		
		AssetCategory assetCategory = getAssetCategory();
		AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
		assetCategoryRequest.setAssetCategory(assetCategory);
		
		List<AssetCategory> assetCategories = new ArrayList<>();
		assetCategories.add(assetCategory);
		AssetCategoryResponse assetCategoryResponse = new AssetCategoryResponse();
		assetCategoryResponse.setResponseInfo(null);
		assetCategoryResponse.setAssetCategory(assetCategories);
		
		when(assetCategoryRepository.create(any(AssetCategoryRequest.class))).thenReturn(assetCategory);
		
		assertTrue(assetCategoryResponse.equals(assetCategoryService.create(assetCategoryRequest)));
	}*/
	
	@Test
	public void testCreateAsync() {
		
		AssetCategory assetCategory = getAssetCategory();
		AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
		assetCategoryRequest.setAssetCategory(assetCategory);
		
		List<AssetCategory> assetCategories = new ArrayList<>();
		assetCategories.add(assetCategory);
		AssetCategoryResponse assetCategoryResponse = new AssetCategoryResponse();
		assetCategoryResponse.setResponseInfo(null);
		assetCategoryResponse.setAssetCategory(assetCategories);
		
		assertTrue(assetCategoryResponse.equals(assetCategoryService.createAsync(assetCategoryRequest)));
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
