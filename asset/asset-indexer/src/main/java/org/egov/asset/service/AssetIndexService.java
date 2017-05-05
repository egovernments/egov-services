package org.egov.asset.service;

import java.util.Map;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetIndex;
import org.egov.asset.model.Boundary;
import org.egov.asset.model.Location;
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

	public void postAsset(AssetRequest assetRequest) {
		AssetIndex assetIndex = prepareAssetIndex(assetRequest);
		LOGGER.info("the logged value of assetIndex ::"+assetIndex);
		assetRepository.saveAsset(assetIndex);
		}
	
	public void putAsset(AssetRequest assetRequest) {
		AssetIndex assetIndex = prepareAssetIndex(assetRequest);
		LOGGER.info("the logged value of assetIndex in update ::"+assetIndex);
		assetRepository.updateAsset(assetIndex);
		}

	public AssetIndex prepareAssetIndex(AssetRequest assetRequest) {
		AssetIndex assetIndex = new AssetIndex();
		Asset asset = assetRequest.getAsset();
		Location location = asset.getLocationDetails();
		assetIndex.setAssetData(asset, assetIndex);
		Map<Long,Boundary>	locationMap = assetRepository.getlocationsById(asset);	
		assetIndex.setAssetLocation(location, locationMap);
		return assetIndex;
	}
}
