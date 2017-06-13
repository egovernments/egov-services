package org.egov.asset.service;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.AssetCategoryRepository;
import org.egov.asset.util.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
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

	@Test
	public void testCreate() {
		AssetCategoryResponse assetCategoryResponse = null;
		try {
			assetCategoryResponse = getAssetCategoryResponse("assetcategorycreateresponse.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
		assetCategoryRequest.setRequestInfo(null);
		assetCategoryRequest.setAssetCategory(getAssetCategory());

		when(assetCategoryRepository.create(any(AssetCategoryRequest.class))).thenReturn(getAssetCategory());

		assetCategoryService.create(Matchers.any(AssetCategoryRequest.class));

		assertEquals(assetCategoryResponse.getAssetCategory().get(0).toString(),
				assetCategoryRequest.getAssetCategory().toString());
	}

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

	@Test
	public void testUpdate() {
		AssetCategoryResponse assetCategoryResponse = null;
		try {
			assetCategoryResponse = getAssetCategoryResponse("assetcategoryupdateresponseservice.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
		assetCategoryRequest.setRequestInfo(null);
		assetCategoryRequest.setAssetCategory(getAssetCategory());

		when(assetCategoryRepository.update(any(AssetCategoryRequest.class))).thenReturn(getAssetCategory());

		assetCategoryService.update(Matchers.any(AssetCategoryRequest.class));

		assertEquals(assetCategoryResponse.getAssetCategory().get(0).toString(),
				assetCategoryRequest.getAssetCategory().toString());
	}

	@Test
	public void testUpdateAsync() {

		AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
		assetCategoryRequest.setAssetCategory(getAssetCategoryForUpdateAsync());

		final List<AssetCategoryRequest> insertedAssetCategoryRequest = new ArrayList<>();
		insertedAssetCategoryRequest.add(assetCategoryRequest);
		AssetCategoryResponse assetCategoryResponse = null;
		try {
			assetCategoryResponse = getAssetCategoryResponse("assetcategoryservice.assetcategory1.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		when(applicationProperties.getUpdateAssetCategoryTopicName()).thenReturn("kafka.topics.update.disposal");
		when(assetCategoryRepository.getAssetCategoryCode()).thenReturn("15");
		assertTrue(assetCategoryResponse.getAssetCategory().get(0).getId().equals(Long.valueOf("15")));
		doNothing().when(assetProducer).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.anyObject());
		assetCategoryService.updateAsync(assetCategoryRequest);

		assertEquals(assetCategoryResponse.getAssetCategory().get(0).toString(),
				assetCategoryRequest.getAssetCategory().toString());
	}

	private AssetCategory getAssetCategory() {

		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setTenantId("ap.kurnool");
		assetCategory.setId(null);
		assetCategory.setName("asset3");
		assetCategory.setCode(null);
		assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
		assetCategory.setParent(Long.valueOf("2"));
		assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
		assetCategory.setIsAssetAllow(true);
		assetCategory.setAssetAccount(2l);
		assetCategory.setAccumulatedDepreciationAccount(Long.valueOf("1"));
		assetCategory.setRevaluationReserveAccount(Long.valueOf("5"));
		assetCategory.setDepreciationExpenseAccount(Long.valueOf("3"));
		assetCategory.setUnitOfMeasurement(Long.valueOf("10"));
		assetCategory.setVersion("v1");
		assetCategory.setDepreciationRate(null);

		return assetCategory;
	}

	private AssetCategory getAssetCategoryForUpdateAsync() {

		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setTenantId("ap.kurnool");
		assetCategory.setId(Long.valueOf("15"));
		assetCategory.setName("asset3");
		assetCategory.setCode("15");
		assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
		assetCategory.setParent(Long.valueOf("2"));
		assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
		assetCategory.setIsAssetAllow(true);
		assetCategory.setAssetAccount(2l);
		assetCategory.setAccumulatedDepreciationAccount(Long.valueOf("1"));
		assetCategory.setRevaluationReserveAccount(Long.valueOf("5"));
		assetCategory.setDepreciationExpenseAccount(Long.valueOf("3"));
		assetCategory.setUnitOfMeasurement(Long.valueOf("10"));
		assetCategory.setVersion("v1");
		assetCategory.setDepreciationRate(null);

		return assetCategory;
	}

	private AssetCategoryResponse getAssetCategoryResponse(String filePath) throws IOException {
		String assetJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(assetJson, AssetCategoryResponse.class);
	}

}
