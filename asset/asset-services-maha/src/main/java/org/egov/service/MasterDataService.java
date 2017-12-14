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
import org.egov.contract.FinancialYear;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.Department;
import org.egov.model.FundSource;
import org.egov.model.ModeOfAcquisition;
import org.egov.repository.MasterDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
			tenantId = tenantId.split("\\.")[0];
		return mDRepo.getMastersByListParams(getStateWideParams(assets), requestInfo, tenantId);
	}

	public Map<String, Map<String, JSONArray>> getUlbWideMastersByListParams(
			Map<String, Map<String, Map<String, String>>> moduleMap, RequestInfo requestInfo, String tenantId) {
		return mDRepo.getMastersByListParams(moduleMap, requestInfo, tenantId);
	}

	public Map<String,Map<String,Map<String,String>>> getStateWideParams(List<Asset> assets){
		
		Set<Long> asCatSet = assets.stream().filter(a -> a.getAssetCategory().getId() != null)
				.map(asset -> asset.getAssetCategory().getId()).collect(Collectors.toSet());
		Set<String> deptSet = assets.stream().filter(a -> a.getDepartment().getCode() != null)
				.map(asset -> asset.getDepartment().getCode()).collect(Collectors.toSet());
		Set<String> modeOfAcSet = assets.stream().filter(a -> a.getModeOfAcquisition().getCode() != null)
				.map(asset -> asset.getModeOfAcquisition().getCode()).collect(Collectors.toSet());
		/*Set<String> fundSet = assets.stream().filter(a -> a.getFundSource().getCode() != null)
				.map(a -> a.getFundSource().getCode()).collect(Collectors.toSet());*/
		  Set<String> fundSet = assets.stream().filter(a -> {
			if (a.getFundSource() != null && a.getFundSource().getCode() != null)
				return true;
			else
				return false;
		}).map(a -> a.getFundSource().getCode()).collect(Collectors.toSet());
		
		
		/**/
		
		// module Map
		Map<String, Map<String, Map<String, String>>> moduleMapInput = new HashMap<>();
		// masters Map
		Map<String, Map<String, String>> assetmasterMap = new HashMap<>();
		Map<String, Map<String, String>> commonMastersMap = new HashMap<>();
		Map<String, Map<String, String>> egfMastersMap = new HashMap<>();
		
		//masters map
		Map<String, String> assetCategoryFieldMap = new HashMap<>();
		Map<String, String> modeofAcquisitionFeildMap = new HashMap<>();
		Map<String, String> departmentFieldMap = new HashMap<>();
		Map<String, String> fundSourceFieldMap = new HashMap<>();
		
		
		// AssetCategory
		if (!asCatSet.isEmpty())
			assetCategoryFieldMap.put("id", assetCommonService.getIdQuery(asCatSet));
			assetmasterMap.put("AssetCategory", assetCategoryFieldMap);

		// modeOfAcquisition
		if (!modeOfAcSet.isEmpty())
			modeofAcquisitionFeildMap.put("code", assetCommonService.getIdQueryFromString(modeOfAcSet));
			assetmasterMap.put("ModeOfAcquisition", modeofAcquisitionFeildMap);
			moduleMapInput.put("ASSET", assetmasterMap);

		// department and common masters
		if (!deptSet.isEmpty())
			departmentFieldMap.put("code", assetCommonService.getIdQueryFromString(deptSet));
			commonMastersMap.put("Department", departmentFieldMap);
			moduleMapInput.put("common-masters", commonMastersMap);

		// egf-master and fundsource
		if (!fundSet.isEmpty())
			fundSourceFieldMap.put("code", assetCommonService.getIdQueryFromString(fundSet));
			egfMastersMap.put("funds", fundSourceFieldMap);
			moduleMapInput.put("egf-master", egfMastersMap);
		
		return moduleMapInput;
	}

	public Map<Long, AssetCategory> getAssetCategoryMapFromJSONArray(JSONArray jsonArray ) {
		
		if(CollectionUtils.isEmpty(jsonArray))
			return new HashMap<Long,AssetCategory>();
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
	
	public Map<String, Department> getDepartmentMapFromJSONArray(JSONArray jsonArray ) {

		if(CollectionUtils.isEmpty(jsonArray))
			return new HashMap<String,Department>();
		
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
		return Departments.stream().collect(Collectors.toMap(Department::getCode, Function.identity()));
	}
	
	public Map<String, FundSource> getFundSourceMapFromJSONArray(JSONArray jsonArray ) {

		if(CollectionUtils.isEmpty(jsonArray))
			return new HashMap<String,FundSource>();
		List<FundSource> Fundsources = new ArrayList<>();
		try {
			Fundsources = Arrays
					.asList(mapper.readValue(jsonArray.toJSONString(), FundSource[].class));
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
		return Fundsources.stream().collect(Collectors.toMap(FundSource::getCode, Function.identity()));
	}

	public FinancialYear getFinancialYear(Long toDate, RequestInfo requestInfo, String tenantId) {

		return mDRepo.getFinancialYears(toDate, requestInfo, tenantId);
	}

	public Map<String, ModeOfAcquisition> getModeOfAcquisitionMapFromJSONArray(JSONArray jsonArray ) {

		if(CollectionUtils.isEmpty(jsonArray))
			return new HashMap<String, ModeOfAcquisition>();
		List<ModeOfAcquisition> ModeOfAcquisitions = new ArrayList<>();
		try {
			ModeOfAcquisitions = Arrays
					.asList(mapper.readValue(jsonArray.toJSONString(), ModeOfAcquisition[].class));
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
		return ModeOfAcquisitions.stream().collect(Collectors.toMap(ModeOfAcquisition::getCode, Function.identity()));
	}
}