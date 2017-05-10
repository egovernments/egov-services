
package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.AssetCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AssetCategoryService {

	private static final Logger logger = LoggerFactory.getLogger(AssetCategoryService.class);

	@Autowired
	private AssetCategoryRepository assetCategoryRepository;

	@Autowired
	private AssetProducer assetProducer;

	@Autowired
	private ApplicationProperties applicationProperties;

	public List<AssetCategory> search(AssetCategoryCriteria assetCategoryCriteria) {
		return assetCategoryRepository.search(assetCategoryCriteria);
	}

	public AssetCategoryResponse create(AssetCategoryRequest assetCategoryRequest) {

		assetCategoryRepository.create(assetCategoryRequest);

		List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();
		assetCategories.add(assetCategoryRequest.getAssetCategory());
		AssetCategoryResponse assetCategoryResponse = getAssetCategoryResponse(assetCategories);

		return assetCategoryResponse;
	}

	public AssetCategoryResponse createAsync(AssetCategoryRequest assetCategoryRequest) {

		assetCategoryRequest.getAssetCategory().setCode(assetCategoryRepository.getAssetCategoryCode());
		logger.info("AssetCategoryService createAsync" + assetCategoryRequest);
		ObjectMapper objectMapper = new ObjectMapper();
		String value = null;
		try {
			value = objectMapper.writeValueAsString(assetCategoryRequest);
		} catch (JsonProcessingException e) {
			logger.info("the exception in assetcategory service create async method : " + e);
		}
		try {
			assetProducer.sendMessage(applicationProperties.getCreateAssetCategoryTopicName(), "save-aasetcategory",
					value);
		} catch (Exception ex) {
			logger.info("the exception in assetcategory service create async method : " + ex);
		}
		List<AssetCategory> assetCategories = new ArrayList<>();
		assetCategories.add(assetCategoryRequest.getAssetCategory());

		return getAssetCategoryResponse(assetCategories);
	}

	private AssetCategoryResponse getAssetCategoryResponse(List<AssetCategory> assetCategories) {
		AssetCategoryResponse assetCategoryResponse = new AssetCategoryResponse();
		assetCategoryResponse.setAssetCategory(assetCategories);

		return assetCategoryResponse;
	}
}
