
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
	
	@Autowired
	private ObjectMapper objectMapper;

	public List<AssetCategory> search(AssetCategoryCriteria assetCategoryCriteria) {
		return assetCategoryRepository.search(assetCategoryCriteria);
	}

	public void create(AssetCategoryRequest assetCategoryRequest) {

		try {
				assetCategoryRepository.create(assetCategoryRequest);
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.info("AssetCategoryService create");
		}
	}

	public AssetCategoryResponse createAsync(AssetCategoryRequest assetCategoryRequest) {

		assetCategoryRequest.getAssetCategory().setCode(assetCategoryRepository.getAssetCategoryCode());
		logger.info("AssetCategoryService createAsync" + assetCategoryRequest);
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

	public void update(AssetCategoryRequest assetCategoryRequest) {

		try {
			assetCategoryRepository.update(assetCategoryRequest);
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.info("AssetCategoryService update"+ex.getMessage());
		}
	}
	
	public AssetCategoryResponse updateAsync(AssetCategoryRequest assetCategoryRequest) {
		
		logger.info("AssetCategoryService updateAsync" + assetCategoryRequest);
		String value = null;
		try {
			value = objectMapper.writeValueAsString(assetCategoryRequest);
		} catch (JsonProcessingException e) {
			logger.info("the exception in assetcategory service  updateasync method : " + e);
		}
		try {
			assetProducer.sendMessage(applicationProperties.getUpdateAssetCategoryTopicName(), "update-aasetcategory",
					value);
		} catch (Exception ex) {
			logger.info("the exception in assetcategory service updateasync method : " + ex);
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
