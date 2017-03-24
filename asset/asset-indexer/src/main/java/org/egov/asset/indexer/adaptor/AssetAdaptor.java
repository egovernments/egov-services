package org.egov.asset.indexer.adaptor;

import org.egov.asset.contract.AssetDetails;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.indexer.repository.AssetCategoryRepository;
import org.egov.asset.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssetAdaptor {

	@Autowired
	private AssetCategoryRepository assetCategoryRepository;

	/***
	 * method to create assetdetails object and populate values
	 * 
	 * @param asset
	 * @return assetdetails
	 */
	public AssetDetails getIndexRecord(Asset asset, RequestInfo requestInfo) {

		AssetDetails assetDetails = new AssetDetails();
		assetDetails = setAsset(asset, assetDetails);
		assetDetails = setAssetCategory(assetDetails, asset.getAssetCategory().getCode(), requestInfo);

		return assetDetails;
	}

	/***
	 * method to populate all asset related values assetDetails object
	 * 
	 * @param asset,assetDetails
	 * @return assetDetails
	 */

	private AssetDetails setAsset(Asset asset, AssetDetails assetDetails) {

		assetDetails.setAsset(asset);
		return assetDetails;
	}

	/***
	 * method to populate all Asset related values in assetDetails object
	 * 
	 * @param assetDetails,assetCategoryCode
	 * @return assetDetails
	 */
	private AssetDetails setAssetCategory(AssetDetails assetDetails, String assetCategoryCode,
			RequestInfo requestInfo) {
		return assetCategoryRepository.setAssetCategory(assetDetails, assetCategoryCode, requestInfo);
	}

}
