package org.egov.asset.indexer.repository;

import org.egov.asset.contract.AssetCategory;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.contract.AssetDetails;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.indexer.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AssetCategoryRepository {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public AssetDetails setAssetCategory(AssetDetails assetDetails, String assetCategoryCode, RequestInfo requestInfo) {

		String url = propertiesManager.getAssetCategoryApiHostUrl() + "?code=" + assetCategoryCode;
		AssetCategoryResponse assetCategoryResponse = restTemplate.postForObject(url, requestInfo,
				AssetCategoryResponse.class);
		AssetCategory assetCategoryModel = assetCategoryResponse.getAssetCategory().get(0);
		assetDetails.setAssetCategory(assetCategoryModel);

		return assetDetails;
	}
}
