package org.egov.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
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
	private MasterDataRepository mDRepo;

	public Map<String, Map<String, JSONArray>> getStateWideMastersByListParams(
			Map<String, Map<String, Map<String, String>>> moduleMap, RequestInfo requestInfo, String tenantId) {

		if (!tenantId.equals("default"))
			tenantId = tenantId.split(".")[0];
		return mDRepo.getMastersByListParams(moduleMap, requestInfo, tenantId);
	}

	public Map<String, Map<String, JSONArray>> getUlbWideMastersByListParams(
			Map<String, Map<String, Map<String, String>>> moduleMap, RequestInfo requestInfo, String tenantId) {
		return mDRepo.getMastersByListParams(moduleMap, requestInfo, tenantId);
	}

	public String getIdQuery(final Set<Long> idSet) {
		StringBuilder query = null;
		Long[] arr = new Long[idSet.size()];
		arr = idSet.toArray(arr);
		query = new StringBuilder(arr[0].toString());
		for (int i = 1; i < arr.length; i++)
			query.append("," + arr[i]);
		return query.toString();
	}
	
	public Map<Long, AssetCategory> getAssetCategoryMapFromJSONArray(Map<String, JSONArray> moduleMap) {

		JSONArray jsonArray = moduleMap.get("AssetCategory");
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
}
