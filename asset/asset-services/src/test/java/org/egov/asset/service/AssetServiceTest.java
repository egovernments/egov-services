package org.egov.asset.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.DepreciationReportCriteria;
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.repository.AssetRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AssetServiceTest {

	@Mock
	private AssetRepository assetRepository;

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private AssetService assetService;

	@Mock
	private ResponseInfoFactory responseInfoFactory;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

	@Mock
	private AssetCommonService assetCommonService;

	@Test
	public void testSearch() {
		final List<Asset> assets = new ArrayList<>();
		assets.add(getAsset());
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);

		final AssetCriteria assetCriteria = AssetCriteria.builder().tenantId("ap.kurnool").build();
		when(assetRepository.findForCriteria(any(AssetCriteria.class))).thenReturn(assets);
		assertEquals(assetResponse, assetService.getAssets(assetCriteria, new RequestInfo()));
	}

	@Test
	public void testCreate() {

		final Asset asset = getAsset();
		final AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		when(assetRepository.create(any(AssetRequest.class))).thenReturn(asset);

		assertTrue(assetResponse.equals(assetService.create(assetRequest)));
	}

	@Test
	public void testCreateAsync() {

		final Asset asset = getAsset();
		final AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		when(assetCommonService.getDepreciationRate(any(Double.class))).thenReturn(Double.valueOf("13.17"));

		assertTrue(assetResponse.equals(assetService.createAsync(assetRequest)));
	}

	@Test
	public void testUpdate() {

		final Asset asset = getAsset();
		final AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		when(assetRepository.update(any(AssetRequest.class))).thenReturn(asset);

		assertTrue(assetResponse.equals(assetService.update(assetRequest)));
	}

	@Test
	public void testUpdateAsync() {

		final Asset asset = getAsset();
		final AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);
		final AssetResponse assetResponse = getAssetResponse(asset);

		when(assetCommonService.getDepreciationRate(any(Double.class))).thenReturn(Double.valueOf("13.17"));
		assertTrue(assetResponse.equals(assetService.updateAsync(assetRequest)));
	}

	@Test
	public void testGetAsset() {

		final Asset expectedAsset = getAsset();
		final List<Asset> assets = new ArrayList<>();
		assets.add(expectedAsset);
		when(assetRepository.findForCriteria(any(AssetCriteria.class))).thenReturn(assets);
		final Asset actualAsset = assetService.getAsset("ap.kurnool", Long.valueOf("552"), new RequestInfo());

		assertEquals(expectedAsset, actualAsset);

	}
/*
	@Test
	public void testGetDepreciationReport() {
		final List<Asset> assets = new ArrayList<>();
		assets.add(getAsset());
		final AssetResponse expectedAssetResponse = new AssetResponse();
		expectedAssetResponse.setAssets(assets);

		when(assetRepository.getDepreciatedAsset(any(DepreciationReportCriteria.class))).thenReturn(assets);

		final DepreciationReportCriteria depreciationReportCriteria = DepreciationReportCriteria.builder()
				.assetCategoryType(AssetCategoryType.IMMOVABLE.toString()).assetCategoryName("category name")
				.tenantId("ap.kurnool").build();
		final AssetResponse actualAssetResponse = assetService.getDepreciationReport(new RequestInfo(),
				depreciationReportCriteria);

		assertEquals(expectedAssetResponse, actualAssetResponse);

	}*/

	private AssetResponse getAssetResponse(final Asset asset) {
		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);
		return assetResponse;
	}

	private Asset getAsset() {
		final Asset asset = new Asset();
		asset.setTenantId("ap.kurnool");
		asset.setId(Long.valueOf("552"));
		asset.setName("asset name");
		asset.setStatus(Status.CREATED.toString());
		asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);
		asset.setEnableYearWiseDepreciation(false);
		asset.setDepreciationRate(Double.valueOf("13.17"));

		final Location location = new Location();
		location.setLocality(4l);
		location.setDoorNo("door no");

		final AssetCategory assetCategory = new AssetCategory();
		assetCategory.setId(1l);
		assetCategory.setName("category name");
		assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
		assetCategory.setTenantId("ap.kurnool");

		asset.setLocationDetails(location);
		asset.setAssetCategory(assetCategory);
		return asset;
	}

}
