package org.egov.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.model.AssetCategory;
import org.egov.model.CurrentValue;
import org.egov.model.Depreciation;
import org.egov.model.DepreciationDetail;
import org.egov.model.DepreciationInputs;
import org.egov.model.criteria.DepreciationCriteria;
import org.egov.repository.DepreciationRepository;
import org.egov.repository.MasterDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class DepreciationService {

	@Autowired
	private AssetService assetService;
	
	@Autowired
	private DepreciationRepository depreciationRepository;
	
	@Autowired
	private ApplicationProperties appProps;
	
	@Autowired
	private MasterDataRepository mDRepo;
	
	
	public Depreciation depreciateAssets(DepreciationCriteria depreciationCriteria,RequestInfo requestInfo) {
		
		//TODO validation and filling financila year data in criteria
		List<DepreciationInputs> depreciationInputsList = depreciationRepository.getDepreciationInputs(depreciationCriteria);
		enrichDepreciationCriteria(depreciationInputsList,requestInfo,depreciationCriteria.getTenantId());
		
		List<DepreciationDetail> depreciationDetailList = new ArrayList<>();
		List<CurrentValue> currentValues = new ArrayList<>();
		
		//TODO find depreciation value per day and depreciate assets based on it
		//TODO loop through the input list, find depreciation and current vlaue for each asset , put them in  respective list,
		// put both current value and depreciation in respective topics
		
		return null;
	}


	private void enrichDepreciationCriteria(List<DepreciationInputs> depreciationInputsList,RequestInfo requestInfo,String tenantId) {
		//TODO getAssetCategories list
		Set<Long> assetCategoryIds = depreciationInputsList.stream().map(dil -> dil.getAssetCategory()).collect(Collectors.toSet());
		
		Map<String, String> argsMap = new HashMap<>();
		argsMap.put(appProps.getMdMsMasterAssetCategory(), getIdQuery(assetCategoryIds));

		JSONArray jsonArray = mDRepo.getAssetMastersById(argsMap, requestInfo,tenantId).get(appProps.getMdMsMasterAssetCategory());
		List<AssetCategory> assetCategorys = new ArrayList<>();
		try {
			assetCategorys = Arrays
					.asList(new ObjectMapper().readValue(jsonArray.toJSONString(), AssetCategory[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Map<Long, AssetCategory> assetCatMap = assetCategorys.stream()
				.collect(Collectors.toMap(AssetCategory::getId, Function.identity()));
		depreciationInputsList.forEach(a -> a.setDepreciationRate(assetCatMap.get(a.getAssetCategory()).getDepreciationRate()));
	}
	
	private static String getIdQuery(final Set<Long> idSet) {
		StringBuilder query = null;
		Long[] arr = new Long[idSet.size()];
		arr = idSet.toArray(arr);
		query = new StringBuilder(arr[0].toString());
		for (int i = 1; i < arr.length; i++)
			query.append("," + arr[i]);
		return query.toString();
	}	
}
