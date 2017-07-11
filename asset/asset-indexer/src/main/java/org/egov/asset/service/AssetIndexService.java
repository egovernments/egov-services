package org.egov.asset.service;

import java.util.Map;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetIndex;
import org.egov.asset.model.Boundary;
import org.egov.asset.model.Location;
import org.egov.asset.model.Tenant;
import org.egov.asset.repository.AssetIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetIndexService {

	public static final Logger LOGGER = LoggerFactory.getLogger(AssetIndexService.class);

	@Autowired
	private AssetIndexRepository assetRepository;

	@Autowired
	private AssetIndexCommonService assetIndexCommonService;

	public void postAsset(final AssetRequest assetRequest) {
		final AssetIndex assetIndex = prepareAssetIndex(assetRequest);
		LOGGER.info("the logged value of assetIndex ::" + assetIndex);
		assetRepository.saveAsset(assetIndex);
	}

	public void putAsset(final AssetRequest assetRequest) {
		final AssetIndex assetIndex = prepareAssetIndex(assetRequest);
		LOGGER.info("the logged value of assetIndex in update ::" + assetIndex);
		assetRepository.updateAsset(assetIndex);
	}

	public AssetIndex prepareAssetIndex(final AssetRequest assetRequest) {
		final AssetIndex assetIndex = new AssetIndex();
		final Asset asset = assetRequest.getAsset();
		final Location location = asset.getLocationDetails();
		assetIndex.setAssetData(asset);
		final Map<Long, Boundary> locationMap = assetRepository.getlocationsById(asset);
		assetIndex.setAssetLocation(location, locationMap);
		setTenantProperties(assetIndex, asset.getTenantId());
		return assetIndex;
	}

	private void setTenantProperties(final AssetIndex assetIndex, final String code) {
		final Tenant tenant = assetIndexCommonService.getTenantData(code).get(0);
		assetIndex.setCityName(tenant.getCity().getName());
		assetIndex.setUlbGrade(tenant.getCity().getUlbGrade());
		assetIndex.setLocalName(tenant.getCity().getLocalName());
		assetIndex.setDistrictCode(tenant.getCity().getDistrictCode());
		assetIndex.setDistrictName(tenant.getCity().getDistrictName());
		assetIndex.setRegionName(tenant.getCity().getRegionName());
	}
}
