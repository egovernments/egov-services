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
import org.egov.repository.MasterDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class MasterDataService {

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private MasterDataRepository mDRepo;

	
	public Map<Long,AssetCategory> getAssetCategoryMap(Set<Long> idSet,RequestInfo requestInfo,String tenantId){
		
		Map<String, String> argsMap = new HashMap<>();
		argsMap.put(appProps.getMdMsMasterAssetCategory(), getIdQuery(idSet));
		
		if(!tenantId.equals("default"))
			tenantId = tenantId.split(".")[0];
		JSONArray jsonArray = mDRepo.getAssetMastersById(argsMap, requestInfo, tenantId)
				.get(appProps.getMdMsMasterAssetCategory());
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
		
		return assetCategorys.stream().collect(Collectors.toMap(AssetCategory::getId, Function.identity()));
	}
	
	private String getIdQuery(final Set<Long> idSet) {
		StringBuilder query = null;
		Long[] arr = new Long[idSet.size()];
		arr = idSet.toArray(arr);
		query = new StringBuilder(arr[0].toString());
		for (int i = 1; i < arr.length; i++)
			query.append("," + arr[i]);
		return query.toString();
	}
}
