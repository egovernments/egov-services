package org.egov.service;

import java.util.List;

import org.egov.contract.AssetCategoryRequest;
import org.egov.contract.AssetCategoryResponse;
import org.egov.model.AssetCategory;
import org.egov.model.AssetCategoryCriteria;
import org.egov.repository.AssetCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetCategoryService {
	
	@Autowired
	 AssetCategoryRepository assetCategoryRepository;
	
	public List<AssetCategory> search(final AssetCategoryCriteria assetCategoryCriteria) {
		log.info("AssetCategoryService search "+assetCategoryRepository.search(assetCategoryCriteria));
        return assetCategoryRepository.search(assetCategoryCriteria);
    }

	public AssetCategoryResponse createAsync(AssetCategoryRequest assetCategoryRequest) {
		// TODO Auto-generated method stub
		return null;
	}
}


