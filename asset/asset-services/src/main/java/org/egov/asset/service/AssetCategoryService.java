
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
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
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
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	public List<AssetCategory> search(final AssetCategoryCriteria assetCategoryCriteria) {
		return assetCategoryRepository.search(assetCategoryCriteria);
	}

	public void create(final AssetCategoryRequest assetCategoryRequest) {

		try {
			assetCategoryRepository.create(assetCategoryRequest);
		} catch (final Exception ex) {
			ex.printStackTrace();
			logger.info("AssetCategoryService create");
		}
	}

	public AssetCategoryResponse createAsync(final AssetCategoryRequest assetCategoryRequest) {

		assetCategoryRequest.getAssetCategory().setCode(assetCategoryRepository.getAssetCategoryCode());
		logger.info("AssetCategoryService createAsync" + assetCategoryRequest);
		String value = null;
		try {
			value = objectMapper.writeValueAsString(assetCategoryRequest);
		} catch (final JsonProcessingException e) {
			logger.info("the exception in assetcategory service create async method : " + e);
		}
		try {
			assetProducer.sendMessage(applicationProperties.getCreateAssetCategoryTopicName(), "save-aasetcategory",
					value);
		} catch (final Exception ex) {
			logger.info("the exception in assetcategory service create async method : " + ex);
		}
		final List<AssetCategory> assetCategories = new ArrayList<>();
		assetCategories.add(assetCategoryRequest.getAssetCategory());

		return getAssetCategoryResponse(assetCategories, assetCategoryRequest.getRequestInfo());
	}

	public AssetCategoryResponse update(final AssetCategoryRequest assetCategoryRequest) {
		final AssetCategory assetCategory = assetCategoryRepository.update(assetCategoryRequest);
		final List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();
		assetCategories.add(assetCategory);
		return getAssetCategoryResponse(assetCategories, new RequestInfo());
	}

	public AssetCategoryResponse updateAsync(final AssetCategoryRequest assetCategoryRequest) {

		logger.info("AssetCategoryService updateAsync" + assetCategoryRequest);
		String value = null;
		try {
			value = objectMapper.writeValueAsString(assetCategoryRequest);
		} catch (final JsonProcessingException e) {
			logger.info("the exception in assetcategory service  updateasync method : " + e);
		}
		try {
			assetProducer.sendMessage(applicationProperties.getUpdateAssetCategoryTopicName(), "update-aasetcategory",
					value);
		} catch (final Exception ex) {
			logger.info("the exception in assetcategory service updateasync method : " + ex);
		}
		final List<AssetCategory> assetCategories = new ArrayList<>();
		assetCategories.add(assetCategoryRequest.getAssetCategory());

		return getAssetCategoryResponse(assetCategories, assetCategoryRequest.getRequestInfo());
	}

	private AssetCategoryResponse getAssetCategoryResponse(final List<AssetCategory> assetCategories,
			final RequestInfo requestInfo) {
		final AssetCategoryResponse assetCategoryResponse = new AssetCategoryResponse();
		assetCategoryResponse.setAssetCategory(assetCategories);
		assetCategoryResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
		return assetCategoryResponse;
	}
}
