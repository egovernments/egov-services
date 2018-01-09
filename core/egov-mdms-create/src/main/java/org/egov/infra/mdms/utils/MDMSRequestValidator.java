package org.egov.infra.mdms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.infra.mdms.service.MDMSService;
import org.egov.mdms.model.MDMSCreateRequest;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

@Service
public class MDMSRequestValidator {

	public static final Logger logger = LoggerFactory.getLogger(MDMSRequestValidator.class);

	@Autowired
	private MDMSUtils mDMSUtils;

	@Autowired
	private MDMSService mDMSService;

	@Autowired
	private ObjectMapper mapper;

	@Value("${cache.fetch.enabled}")
	private Boolean cacheFetch;

	@SuppressWarnings("unchecked")
	public ArrayList<Object> validateRequest(MDMSCreateRequest mDMSCreateRequest, List<String> keys, Boolean isCreate,
			List<String> isRequestPayload) throws Exception {
		Map<String, Map<String, Object>> masterConfigMap = MDMSApplicationRunnerImpl.getMasterConfigMap();

		ArrayList<Object> duplicateList = validateRequestPayload(mDMSCreateRequest, masterConfigMap);
		if (!duplicateList.isEmpty()) {
			isRequestPayload.add("true");
			StringBuilder expression = new StringBuilder();
			List<String> uniqueKeys = mDMSUtils.getUniqueKeys(masterConfigMap, mDMSCreateRequest);
			for (String key : uniqueKeys) {
				keys.add(mDMSUtils.getJsonPathKey(key, expression));
			}
			return duplicateList;
		}

		verifyMasterIsState(mDMSCreateRequest, masterConfigMap);

		ArrayList<Object> result = new ArrayList<>();
		Map<String, String> filePathMap = MDMSApplicationRunnerImpl.getFilePathMap();
		Object fileContents = null;
		Map<String, Object> masterContentFromCache = null;
		List<Object> masterData = new ArrayList<>();
		Object masterDataFromCache = null;
		if (cacheFetch) {
			masterContentFromCache = mDMSService.getContentFromCache(filePathMap, mDMSCreateRequest);
			if (masterContentFromCache != null) {
				masterDataFromCache = mapper.writeValueAsString(masterContentFromCache);
				try {
					masterDataFromCache = JsonPath.read(masterDataFromCache.toString(),
							"$.MdmsRes." + mDMSCreateRequest.getMasterMetaData().getModuleName());
				} catch (com.jayway.jsonpath.PathNotFoundException e) {
					throw new CustomException("400",
							"Module doesn't exist: " + mDMSCreateRequest.getMasterMetaData().getModuleName());
				}
			}
			if (null == masterContentFromCache) {
				logger.info("Failed to get content from cache, fall back to fetch from git triggered....");
			} else if (masterDataFromCache != null && masterDataFromCache.toString().equals("{}")) {
				fileContents = new Object();
			} else
				fileContents = mDMSService.getFileContents(filePathMap, mDMSCreateRequest);

		} else {
			fileContents = mDMSService.getFileContents(filePathMap, mDMSCreateRequest);
		}
		if (null == fileContents) {
			throw new CustomException("400", "Invalid Tenant Id");
		}

		if (null != masterContentFromCache && masterDataFromCache != null
				&& !masterDataFromCache.toString().equals("{}")) {
			fileContents = mapper.writeValueAsString(fileContents);
			Object masterFromCache = mapper.writeValueAsString(masterContentFromCache);
			logger.info("masterContentsFromCache: " + masterContentFromCache);
			masterFromCache = JsonPath.read(masterFromCache.toString(),
					"$.MdmsRes." + mDMSCreateRequest.getMasterMetaData().getModuleName() + "."
							+ mDMSCreateRequest.getMasterMetaData().getMasterName());

			logger.info("masterData fetched from cache: " + masterFromCache);

			DocumentContext documentContext = JsonPath.parse(fileContents.toString());
			documentContext.put("$", mDMSCreateRequest.getMasterMetaData().getMasterName(), masterFromCache);
			fileContents = documentContext.jsonString().toString();
			Map<String, Object> allMasters = mapper.readValue(fileContents.toString(), Map.class);
			masterData = (List<Object>) allMasters.get(mDMSCreateRequest.getMasterMetaData().getMasterName());
		}
		Object requestMasterData = mDMSCreateRequest.getMasterMetaData().getMasterData();
		JSONArray masterDataArray = new JSONArray(mapper.writeValueAsString(requestMasterData));

		if (masterDataFromCache.toString().equals("{}") && masterData.isEmpty()) {
			return result;
		}

		logger.info(" the map before passing for get uniquekeys : " + masterConfigMap);
		List<String> uniqueKeys = mDMSUtils.getUniqueKeys(masterConfigMap, mDMSCreateRequest);
		if (null == uniqueKeys) {
			throw new CustomException("400", "There are duplicate mdms-configs for this master: "
					+ mDMSCreateRequest.getMasterMetaData().getMasterName());
		} else if (uniqueKeys.isEmpty()) {
			logger.info("Skipping Validation....");
			return result;
		} else {
			if (null == masterData) {
				logger.info("Config available for this master nut no data");
				return result;
			}
			logger.info("uniqueKeys: " + uniqueKeys);
			StringBuilder expression = new StringBuilder();
			for (String key : uniqueKeys) {
				keys.add(mDMSUtils.getJsonPathKey(key, expression));
			}
			List<Object> masters = masterData;
			for (int i = 0; i < masterDataArray.length(); i++) {
				List<Object> filterResult = new ArrayList<>();
				masterData = masters;
				for (String key : uniqueKeys) {
					// TODO ERROR IS BEING THROWN IF KEYS FIELDS ARE MISSING IN
					// INCOMING JSON
					Object value;
					value = JsonPath.read(masterDataArray.get(i).toString(), key.toString());
					filterResult = mDMSUtils.filter(masterData, key, value);
					masterData = filterResult;
					logger.info("filterResult: " + filterResult);

				}
				logger.info("filterResult: " + filterResult);

				if (isCreate) {
					if (!filterResult.isEmpty())
						result.add(masterDataArray.get(i));
				} else {
					if (filterResult.isEmpty())
						result.add(masterDataArray.get(i));
				}

			}
			logger.info("Validation result, List of error objects: " + result);
			return result;
		}
	}

	/***
	 * TO turn the tenantId to stateLevel if the data is present in
	 * stateWideMaster list
	 * 
	 * @param mDMSCreateRequest
	 * @param stateLevelMastermap
	 */
	@SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
	private void verifyMasterIsState(MDMSCreateRequest mDMSCreateRequest,
			Map<String, Map<String, Object>> masterConfigMap) throws Exception {

		String tenantId = mDMSCreateRequest.getMasterMetaData().getTenantId();
		String moduleName = mDMSCreateRequest.getMasterMetaData().getModuleName();
		String masterName = mDMSCreateRequest.getMasterMetaData().getMasterName();
		Map<String, Object> masterData = null;
		Boolean isStateLevel = false;

		if (tenantId.contains(".")) {
			Map<String, Object> moduleData = masterConfigMap.get(moduleName);
			if (null != moduleData)
				masterData = (Map<String, Object>) moduleData.get(masterName);
			if (masterData.get(MDMSConstants.STATE_LEVEL_KEY) == null)
				isStateLevel = false;
			else if (masterData.get(MDMSConstants.STATE_LEVEL_KEY).equals(true))
				isStateLevel = true;
			if (masterData != null && isStateLevel) {
				List<Object> dataList = mDMSCreateRequest.getMasterMetaData().getMasterData();
				String updatedtenantId = tenantId.split("\\.")[0];
				mDMSCreateRequest.getMasterMetaData().setTenantId(updatedtenantId);
				dataList.forEach(a -> {
					Map<String, Object> map = (Map<String, Object>) a;
					if (map.get(MDMSConstants.tenantId_KEY) != null)
						map.put(MDMSConstants.tenantId_KEY, updatedtenantId);
					else if (map.get(MDMSConstants.tenantId_KEY_IMPROPER) != null)
						map.put(MDMSConstants.tenantId_KEY_IMPROPER, updatedtenantId);
				});
			}
			logger.info("After changing the tenantId In Request: " + mDMSCreateRequest);
		}
	}

	/**
	 * Validate The Request Payload based on masterConfigMap
	 * 
	 * @param mDMSCreateRequest
	 * @param masterConfigMap
	 * @throws Exception
	 */
	private ArrayList<Object> validateRequestPayload(MDMSCreateRequest mDMSCreateRequest,
			Map<String, Map<String, Object>> masterConfigMap) throws Exception {
		String moduleName = mDMSCreateRequest.getMasterMetaData().getModuleName();
		String mastername = mDMSCreateRequest.getMasterMetaData().getMasterName();
		Map<String, Object> map = masterConfigMap.get(moduleName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> duplicateMap = new HashMap<String, Object>();
		ArrayList<Object> list = new ArrayList<Object>();
		if (map != null && map.isEmpty()) {
			throw new CustomException("400",
					"Module doesn't have mdms-config : " + mDMSCreateRequest.getMasterMetaData().getModuleName());
		} else if (map != null && !map.isEmpty()) {
			Object obj = map.get(mastername);
			if (null == obj) {
				throw new CustomException("400",
						"Master doesn't have mdms-config : " + mDMSCreateRequest.getMasterMetaData().getMasterName());
			} else if (null != obj) {
				List<String> uniqueKeys = mDMSUtils.getUniqueKeys(masterConfigMap, mDMSCreateRequest);
				Object requestMasterData = mDMSCreateRequest.getMasterMetaData().getMasterData();
				JSONArray masterDataArray = new JSONArray(mapper.writeValueAsString(requestMasterData));
				for (int i = 0; i < masterDataArray.length(); i++) {
					String key = null;
					for (String uniqueQue : uniqueKeys) {
						Object value =null;
						try{ 
						value = JsonPath.read(masterDataArray.get(i).toString(), uniqueQue.toString());
						}catch(PathNotFoundException e){
							throw new CustomException("400",
									"Required Fields doesn't exist In Request : " + e.getMessage().split("\\$")[1]);
						}
						if (null == key)
							key = value.toString();
						else
							key = key + "_" + value;
					}

					if (!dataMap.containsKey(key))
						dataMap.put(key, masterDataArray.get(i).toString());
					else
						duplicateMap.put(key, masterDataArray.get(i).toString());
				}
				if (!duplicateMap.isEmpty()) {
					Set<String> set = duplicateMap.keySet();
					for (String key : set) {
						list.add(duplicateMap.get(key));
					}
				}
			}

		}
		return list;
	}
}
