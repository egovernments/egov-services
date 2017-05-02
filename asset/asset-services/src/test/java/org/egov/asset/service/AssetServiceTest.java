package org.egov.asset.service;

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
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.AssetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssetServiceTest {

	@Mock
	private AssetRepository assetRepository;

	@Mock
	private AssetProducer assetProducer;

	@Mock
	private ApplicationProperties applicationProperties;

	@InjectMocks
	private AssetService assetService;

	@Test
	public void testSearch() {
		List<Asset> assets = new ArrayList<>();
		assets.add(getAsset());

		when(assetRepository.findForCriteria(any(AssetCriteria.class))).thenReturn(assets);

		assertTrue(assets.equals(assetService.getAssets(any(AssetCriteria.class))));
	}

	@Test
	public void testCreate() {

		Asset asset = getAsset();
		AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		when(assetRepository.create(any(AssetRequest.class))).thenReturn(asset);

		assertTrue(assetResponse.equals(assetService.create(assetRequest)));
	}

	@Test
	public void testCreateAsync() {

		Asset asset = getAsset();
		AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		assertTrue(assetResponse.equals(assetService.createAsync(assetRequest)));
	}

	@Test
	public void testUpdate() {

		Asset asset = getAsset();
		AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		when(assetRepository.update(any(AssetRequest.class))).thenReturn(asset);

		assertTrue(assetResponse.equals(assetService.update(assetRequest)));
	}

	@Test
	public void testUpdateAsync() {

		Asset asset = getAsset();
		AssetRequest assetRequest = new AssetRequest();
		assetRequest.setAsset(asset);

		List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		AssetResponse assetResponse = new AssetResponse();
		assetResponse.setResponseInfo(null);
		assetResponse.setAssets(assets);

		assertTrue(assetResponse.equals(assetService.updateAsync(assetRequest)));
	}

	private Asset getAsset() {
		Asset asset = new Asset();
		asset.setTenantId("ap.kurnool");
		asset.setName("asset name");
		asset.setStatus(Status.CWIP);
		asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);

		Location location = new Location();
		location.setLocality(4l);
		location.setDoorNo("door no");

		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setId(1l);
		assetCategory.setName("category name");

		asset.setLocationDetails(location);
		asset.setAssetCategory(assetCategory);
		return asset;
	}

}
