
package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.producers.AssetProducer;
import org.egov.asset.repository.AssetCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AssetCategoryService {
	
	@Autowired
	private AssetCategoryRepository assetCategoryRepository;
	
	@Autowired
	private AssetProducer assetProducer;
	
	public List<AssetCategory> search(AssetCategoryCriteria assetCategoryCriteria){
	  return assetCategoryRepository.search(assetCategoryCriteria);	
	}
	
	public AssetCategoryResponse create(AssetCategoryRequest assetCategoryRequest){
		
		assetCategoryRepository.create(assetCategoryRequest);
		
		List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();
		assetCategories.add(assetCategoryRequest.getAssetCategory());
		AssetCategoryResponse assetCategoryResponse = getAssetCategoryResponse(assetCategories);
		
	  return assetCategoryResponse;	
	}
	
	public AssetCategoryResponse createAsync(String topic,String key,AssetCategoryRequest assetCategoryRequest){
		
		assetCategoryRequest.getAssetCategory().setCode(assetCategoryRepository.getAssetCategoryCode());
		System.out.println("AssetCategoryService createAsync"+assetCategoryRequest);
		ObjectMapper objectMapper=new ObjectMapper();
		String value=null;
		try {
			value = objectMapper.writeValueAsString(assetCategoryRequest);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		assetProducer.sendMessage(topic, key, value);
		
		List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();
		assetCategories.add(assetCategoryRequest.getAssetCategory());
		AssetCategoryResponse assetCategoryResponse = getAssetCategoryResponse(assetCategories);
		
	   return assetCategoryResponse;	
	}
	
	private AssetCategoryResponse getAssetCategoryResponse(List<AssetCategory> assetCategories){
		AssetCategoryResponse  assetCategoryResponse=new AssetCategoryResponse();
		assetCategoryResponse.setAssetCategory(assetCategories);
		
	  return assetCategoryResponse;
	}
}
