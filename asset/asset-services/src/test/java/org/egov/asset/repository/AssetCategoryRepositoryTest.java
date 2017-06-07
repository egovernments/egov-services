package org.egov.asset.repository;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import static org.junit.Assert.assertTrue;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.repository.builder.AssetCategoryQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetCategoryRowMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AssetCategoryRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@Mock
	private AssetCategoryQueryBuilder assetCategoryQueryBuilder;
	
	@InjectMocks
	private AssetCategoryRepository assetCategoryRepository;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Test
	public void testSearch(){
		
		List<AssetCategory> assetCategories = new ArrayList<>();
		AssetCategory assetCategory = getAssetCategory();
		assetCategories.add(assetCategory);
		
		when(assetCategoryQueryBuilder.getQuery(any(AssetCategoryCriteria.class),any(List.class))).thenReturn("");
		when(jdbcTemplate.query(any(String.class),any(Object[].class),any(AssetCategoryRowMapper.class))).thenReturn(assetCategories);
		
		assertTrue(assetCategories.equals(assetCategoryRepository.search(new AssetCategoryCriteria())));
	}
	
	@Test
	public void testCreate(){
		
		AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
		RequestInfo requestInfo = new RequestInfo();
		User user = new User();
		user.setId(1l);
		requestInfo.setUserInfo(user);
		assetCategoryRequest.setRequestInfo(requestInfo);
		AssetCategory assetCategory = getAssetCategory();
		assetCategoryRequest.setAssetCategory(assetCategory);
		
		when(assetCategoryQueryBuilder.getInsertQuery()).thenReturn("");
		when(jdbcTemplate.update(any(String.class),any(Object[].class))).thenReturn(1);
		
		assertTrue(assetCategory.equals(assetCategoryRepository.create(assetCategoryRequest)));
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
