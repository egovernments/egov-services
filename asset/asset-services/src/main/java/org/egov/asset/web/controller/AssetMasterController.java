package org.egov.asset.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetMasterController {

	@GetMapping("GET_STATUS")
	public Map<Status, Status> getAssetStatus() {
		Map<Status, Status> map = new HashMap<>();
		for (Status key : Status.values()) {
			map.put(key, Status.valueOf(key.toString()));
		}
		return map;
	}

	@GetMapping("GET_ASSET_CATEGORY_TYPE")
	public Map<AssetCategoryType, AssetCategoryType> getAssetCategoryTypes() {

		Map<AssetCategoryType, AssetCategoryType> map = new HashMap<>();
		for (AssetCategoryType key : AssetCategoryType.values()) {
			map.put(key, AssetCategoryType.valueOf(key.toString()));
		}

		return map;
	}

	@GetMapping("GET_DEPRECIATION_METHOD")
	public Map<DepreciationMethod, DepreciationMethod> getDepreciationMethod() {

		Map<DepreciationMethod, DepreciationMethod> map = new HashMap<>();
		for (DepreciationMethod key : DepreciationMethod.values()) {
			map.put(key, DepreciationMethod.valueOf(key.toString()));
		}

		return map;
	}

	@GetMapping("GET_MODE_OF_ACQUISITION")
	public Map<ModeOfAcquisition, ModeOfAcquisition> getModeOfAcquisition() {

		Map<ModeOfAcquisition, ModeOfAcquisition> map = new HashMap<>();
		for (ModeOfAcquisition key : ModeOfAcquisition.values()) {
			map.put(key, ModeOfAcquisition.valueOf(key.toString()));
		}

		return map;
	}

}
