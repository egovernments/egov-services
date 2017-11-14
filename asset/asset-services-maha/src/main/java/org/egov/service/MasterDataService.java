package org.egov.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.contract.FinancialYear;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.Department;
import org.egov.model.Fundsource;
import org.egov.repository.MasterDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Service
@Slf4j
public class MasterDataService {
	
	@Autowired
	private AssetCommonService assetCommonService;
	
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MasterDataRepository mDRepo;

	public Map<String, Map<String, JSONArray>> getStateWideMastersByListParams(List<Asset> assets, RequestInfo requestInfo, String tenantId) {

		if (!tenantId.equals("default"))
			tenantId = tenantId.split(".")[0];
		return mDRepo.getMastersByListParams(getStateWideParams(assets), requestInfo, tenantId);
	}

	public Map<String, Map<String, JSONArray>> getUlbWideMastersByListParams(
			Map<String, Map<String, Map<String, String>>> moduleMap, RequestInfo requestInfo, String tenantId) {
		return mDRepo.getMastersByListParams(moduleMap, requestInfo, tenantId);
	}

	public Map<String,Map<String,Map<String,String>>> getStateWideParams(List<Asset> assets){
		
		String assetCategoryParam = assetCommonService.getIdQuery(assets.stream().map(asset -> asset.getAssetCategory().getId()).collect(Collectors.toSet()));
		String departmentParam = assetCommonService.getIdQueryFromString(assets.stream().map(asset -> asset.getDepartment().getCode()).collect(Collectors.toSet()));
		String fundSourceParam = assetCommonService.getIdQueryFromString(assets.stream().map(asset -> asset.getFundSource().getCode()).collect(Collectors.toSet()));
		
		Map<String, Map<String, Map<String, String>>> moduleMapInput = new HashMap<>();
		
		// AssetMaster and assetcategory
		Map<String, Map<String, String>> assetmasterMap = new HashMap<>();
		Map<String, String> assetCategoryFieldMap = new HashMap<>();
		assetCategoryFieldMap.put("id", assetCategoryParam);
		assetmasterMap.put("AssetCategory", assetCategoryFieldMap);
		moduleMapInput.put("ASSET", assetmasterMap);

		// department and common masters
		Map<String, Map<String, String>> commonMastersMap = new HashMap<>();
		Map<String, String> departmentFieldMap = new HashMap<>();
		departmentFieldMap.put("code", departmentParam);
		commonMastersMap.put("Department", departmentFieldMap);
		moduleMapInput.put("common-masters", commonMastersMap);

		// egf-master and fundsource
		Map<String, Map<String, String>> egfMastersMap = new HashMap<>();
		Map<String, String> fundSourceFieldMap = new HashMap<>();
		fundSourceFieldMap.put("code", fundSourceParam);
		egfMastersMap.put("funds", fundSourceFieldMap);
		moduleMapInput.put("egf-master", egfMastersMap);
		
		return moduleMapInput;
	}

	public Map<Long, AssetCategory> getAssetCategoryMapFromJSONArray(Map<String, JSONArray> moduleMap) {

		JSONArray jsonArray = moduleMap.get("AssetCategory");
		List<AssetCategory> assetCategorys = new ArrayList<>();
		try {
			assetCategorys = Arrays
					.asList(mapper.readValue(jsonArray.toJSONString(), AssetCategory[].class));
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
	
	public Map<String, Department> getDepartmentMapFromJSONArray(Map<String, JSONArray> moduleMap) {

		JSONArray jsonArray = moduleMap.get("Department");
		List<Department> Departments = new ArrayList<>();
		
		try {
			Departments = Arrays
					.asList(mapper.readValue(jsonArray.toJSONString(), Department[].class));
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
		return Departments.stream().collect(Collectors.toMap(Department::getId, Function.identity()));
	}
	
	public Map<String, Fundsource> getFundSourceMapFromJSONArray(Map<String, JSONArray> moduleMap) {

		JSONArray jsonArray = moduleMap.get("funds");
		List<Fundsource> Fundsources = new ArrayList<>();
		try {
			Fundsources = Arrays
					.asList(mapper.readValue(jsonArray.toJSONString(), Fundsource[].class));
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
		return Fundsources.stream().collect(Collectors.toMap(Fundsource::getId, Function.identity()));
	}

	public FinancialYear getFinancialYear(Long toDate, RequestInfo requestInfo, String tenantId) {

		return mDRepo.getFinancialYears(toDate, requestInfo, tenantId);
	}
}
