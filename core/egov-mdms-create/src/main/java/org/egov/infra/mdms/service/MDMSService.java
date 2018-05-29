package org.egov.infra.mdms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.common.contract.request.RequestInfo;
import org.egov.infra.mdms.producer.GitPushProcessProducer;
import org.egov.infra.mdms.repository.MDMSCreateRepository;
import org.egov.infra.mdms.utils.GitPushProcessWrapper;
import org.egov.infra.mdms.utils.MDMSConstants;
import org.egov.infra.mdms.utils.MDMSUtils;
import org.egov.mdms.model.MDMSCreateRequest;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@Service
public class MDMSService {

	public static final Logger logger = LoggerFactory.getLogger(MDMSService.class);

	@Value("${egov.github.user}")
	private String userName;

	@Value("${egov.github.pass}")
	private String password;

	@Value("${egov.data.root.folder}")
	private String dataRootFolder;

	@Value("${git.repo.path}")
	private String gitRepoPath;

	@Value("${reload.enabled}")
	private Boolean isReloadEnabled;

	@Value("${filepath.reload}")
	private Boolean isFilePathReload;

	@Value("${cache.fetch.enabled}")
	private Boolean cacheFetch;

	@Value("${egov.repo.owner}")
	private String egovRepoOwner;

	@Value("${egov.repo}")
	private String egovRepo;

	@Autowired
	private GitPushProcessProducer gitPushProcessProducer;

	@Autowired
	private MDMSCreateRepository mDMSCreateRepository;

	@Autowired
	private MDMSUtils mDMSUtils;

	public Map<String, Map<String, JSONArray>> gitPush(MDMSCreateRequest mDMSCreateRequest, Boolean isCreate)
			throws Exception {

		Map<String, String> filePathMap = MDMSApplicationRunnerImpl.getFilePathMap();
		Map<String, Map<String, Object>> masterConfigMap = MDMSApplicationRunnerImpl.getMasterConfigMap();
		ObjectMapper mapper = new ObjectMapper();
		Long startTime = null;
		Long endTime = null;
		Map<String, Object> masterContentFromCache = null;
		Object masterDataFromCache = null;
		Object fileContents = null;
		startTime = new Date().getTime();
		if (cacheFetch) {
			masterContentFromCache = getContentFromCache(filePathMap, mDMSCreateRequest);
			if (masterContentFromCache != null) {
				masterDataFromCache = mapper.writeValueAsString(masterContentFromCache);
				masterDataFromCache = JsonPath.read(masterDataFromCache.toString(),
						"$.MdmsRes." + mDMSCreateRequest.getMasterMetaData().getModuleName());
			}
			if (null == masterContentFromCache) {
				logger.info("Failed to get content from cache, fall back to fetch from git triggered....");
			} else if (masterDataFromCache != null && masterDataFromCache.toString().equals("{}")) {
				fileContents = new Object();
			} else {
				fileContents = getFileContents(filePathMap, mDMSCreateRequest);
			}
		} else {
			fileContents = getFileContents(filePathMap, mDMSCreateRequest);
		}
		endTime = new Date().getTime();
		logger.info("Time taken for this step: " + (endTime - startTime) + "ms");
		if (null == fileContents)
			throw new CustomException("400", "Invalid Tenant Id");
		if (null != masterContentFromCache && !masterDataFromCache.toString().equals("{}")) {
			Object masterFromCache = mapper.writeValueAsString(masterContentFromCache);
			logger.debug("masterContentsFromCache: " + masterContentFromCache);
			masterFromCache = JsonPath.read(masterFromCache.toString(),
					"$.MdmsRes." + mDMSCreateRequest.getMasterMetaData().getModuleName() + "."
							+ mDMSCreateRequest.getMasterMetaData().getMasterName());
			logger.debug("masterData fetched from cache: " + masterFromCache);
			fileContents = mapper.writeValueAsString(fileContents);
			DocumentContext documentContext = JsonPath.parse(fileContents.toString());
			documentContext.put("$", mDMSCreateRequest.getMasterMetaData().getMasterName(), masterFromCache);
			fileContents = mapper.readValue(documentContext.jsonString().toString(), Map.class);
		}
		startTime = new Date().getTime();
		String content = "";
		if (masterDataFromCache.toString().equals("{}")) {
			Map<String,Object> myLinkedHashMap = new LinkedHashMap<String, Object>();
			myLinkedHashMap.put("tenantId", mDMSCreateRequest.getMasterMetaData().getTenantId());
			myLinkedHashMap.put("moduleName", mDMSCreateRequest.getMasterMetaData().getModuleName());
			myLinkedHashMap.put(mDMSCreateRequest.getMasterMetaData().getMasterName(), mDMSCreateRequest.getMasterMetaData().getMasterData());
			Gson gson = new Gson();
			content = gson.toJson(myLinkedHashMap, LinkedHashMap.class);
		} else {
			content = getContentForPush(fileContents, mDMSCreateRequest, isCreate);
		}
		endTime = new Date().getTime();
		logger.info("Time taken for this step: " + (endTime - startTime) + "ms");
		String filePath = null;
		String tenantId = mDMSCreateRequest.getMasterMetaData().getTenantId();
		if (tenantId.contains(".")) {
			String moduleName = mDMSCreateRequest.getMasterMetaData().getModuleName();
			String masterName = mDMSCreateRequest.getMasterMetaData().getMasterName();
			Map<String, Object> moduleMap = masterConfigMap.get(moduleName);
			Object masterData = null;
			Boolean isStateLevel = false;
			if (null != moduleMap)
				masterData = moduleMap.get(masterName);

			try {
				if (masterData != null)
					isStateLevel = (Boolean) JsonPath.read(mapper.writeValueAsString(masterData),
							MDMSConstants.STATE_LEVEL_JSONPATH);
			} catch (Exception e) {
				isStateLevel = false;
			}
			if (masterData != null && isStateLevel) {
				filePath = getStateLevelFilePath(filePathMap, mDMSCreateRequest);
			} else {
				filePath = getFilePath(filePathMap, mDMSCreateRequest);
			}
		} else {
			filePath = getFilePath(filePathMap, mDMSCreateRequest);
		}

		GitPushProcessWrapper gitPushObject = GitPushProcessWrapper.builder().filePath(filePath).content(content)
				.isCreate(isCreate).mdmsCreateRequest(mDMSCreateRequest).build();
		gitPushProcessProducer.producer(gitPushObject);

		if (isReloadEnabled) {
			if (isFilePathReload) {
				logger.info("Filepath reload....");
				startTime = new Date().getTime();
				updateCache(gitRepoPath + filePath, mDMSCreateRequest.getMasterMetaData().getTenantId(),
						mDMSCreateRequest.getRequestInfo());
				endTime = new Date().getTime();
				logger.info("Time taken for this step: " + (endTime - startTime) + "ms");
			} else {
				logger.info("Object reload....");
				Map<String, Object> map = mapper.readValue(content, Map.class);
				DocumentContext docContext = JsonPath.parse(MDMSConstants.MDMS_RELOAD_RES);
				docContext.put("$", "RequestInfo", mDMSCreateRequest.getRequestInfo());
				docContext.put("$", "MdmsReq", map);
				startTime = new Date().getTime();
				updateCache(docContext.jsonString().toString());
				endTime = new Date().getTime();
				logger.info("Time taken for this step: " + (endTime - startTime) + "ms");
			}
		}
		String FINAL_FILE_PATH_APPEND = "https://github.com/" + egovRepoOwner + "/" + egovRepo + "/blob/master/";
		logger.info("Find your changes at: " + FINAL_FILE_PATH_APPEND + filePath);

		Map<String, Map<String, JSONArray>> response = new HashMap<>();
		Map<String, JSONArray> entry = new HashMap<>();
		JSONArray data = new JSONArray();
		List<Object> list = JsonPath.read(mapper.writeValueAsString(mDMSCreateRequest.getMasterMetaData()),
				MDMSConstants.MASTERDATA_JSONPATH);
		for (Object obj : list) {
			data.add(obj);
		}
		entry.put(mDMSCreateRequest.getMasterMetaData().getMasterName(), data);
		response.put(mDMSCreateRequest.getMasterMetaData().getModuleName(), entry);

		return response;

	}

	public void gitPushProcess(GitPushProcessWrapper gitPush) throws JsonProcessingException {

		Long startTime = null;
		Long endTime = null;
		logger.info("Step 1: Getting the branch head......");
		startTime = new Date().getTime();
		String branchHeadSHA = getBranchHead();
		endTime = new Date().getTime();
		logger.info("Time taken for this step: " + (endTime - startTime) + "ms");

		logger.info("Step 2: Getting Base Tree......");
		startTime = new Date().getTime();
		String baseTreeSHA = getBaseTree(branchHeadSHA);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: " + (endTime - startTime) + "ms");

		logger.info("Step 3: Creating a New Tree......");
		startTime = new Date().getTime();
		String newTreeSHA = createTree(baseTreeSHA, gitPush.getFilePath(), gitPush.getContent());
		endTime = new Date().getTime();
		logger.info("Time taken for this step: " + (endTime - startTime) + "ms");

		logger.info("Step 4: Creating a New Commit......");
		startTime = new Date().getTime();
		String commitMessage = mDMSUtils.buildCommitMessage(gitPush.getMdmsCreateRequest(), gitPush.isCreate(),
				userName);
		String newCommitSHA = createCommit(branchHeadSHA, newTreeSHA, commitMessage);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: " + (endTime - startTime) + "ms");

		logger.info("Step 5: Pushing the Contents to git......");
		startTime = new Date().getTime();
		pushTheContents(newCommitSHA);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: " + (endTime - startTime) + "ms");
	}

	public Object getFileContents(Map<String, String> filePathMap, MDMSCreateRequest mDMSCreateRequest)
			throws Exception {
		logger.info("Getting file contents from git repo.....");
		Map<String, Map<String, Object>> masterConfigMap = MDMSApplicationRunnerImpl.getMasterConfigMap();
		String filePath = "";
		if (mDMSCreateRequest.getMasterMetaData().getTenantId().contains(".")) {
			String moduleName = mDMSCreateRequest.getMasterMetaData().getModuleName();
			String masterName = mDMSCreateRequest.getMasterMetaData().getMasterName();
			Map<String, Object> moduleData = masterConfigMap.get(moduleName);
			Boolean isStateLevel = false;
			Object masterData = null;
			ObjectMapper mapper = new ObjectMapper();
			if (moduleData != null)
				masterData = moduleData.get(masterName);
			try {
				if (masterData != null)
					isStateLevel = (Boolean) JsonPath.read(mapper.writeValueAsString(masterData),
							MDMSConstants.STATE_LEVEL_JSONPATH);
			} catch (Exception e) {
				isStateLevel = false;
			}
			if (masterData != null && isStateLevel) {
				filePath = getStateLevelFilePath(filePathMap, mDMSCreateRequest);
			} else {
				filePath = getFilePath(filePathMap, mDMSCreateRequest);
			}
		} else {
			filePath = getFilePath(filePathMap, mDMSCreateRequest);
		}
		filePath = gitRepoPath + filePath;
		Object fileContents = mDMSCreateRepository.getFileContents(filePath);
		return fileContents;
	}

	public Map<String, Object> getContentFromCache(Map<String, String> filePathMap, MDMSCreateRequest mDMSCreateRequest)
			throws Exception {
		logger.info("Getting file contents from the mdms cache.....");

		MdmsCriteriaReq mdmsCriteriaReq = new MdmsCriteriaReq();
		MdmsCriteria mdmsCriteria = new MdmsCriteria();

		MasterDetail masterDetail = new MasterDetail();
		masterDetail.setName(mDMSCreateRequest.getMasterMetaData().getMasterName());
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);

		ModuleDetail moduleDetail = new ModuleDetail();
		moduleDetail.setModuleName(mDMSCreateRequest.getMasterMetaData().getModuleName());
		moduleDetail.setMasterDetails(masterDetails);
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);

		mdmsCriteria.setModuleDetails(moduleDetails);
		mdmsCriteria.setTenantId(mDMSCreateRequest.getMasterMetaData().getTenantId());

		mdmsCriteriaReq.setMdmsCriteria(mdmsCriteria);
		mdmsCriteriaReq.setRequestInfo(mDMSCreateRequest.getRequestInfo());

		Map<String, Object> fileContents = mDMSCreateRepository.getContentFromCache(mdmsCriteriaReq);

		return fileContents;
	}

	public void updateCache(String filePath, String tenantId, RequestInfo requestInfo) {
		logger.info("Updating cache......");
		mDMSCreateRepository.updateCache(filePath, tenantId, requestInfo);
	}

	public void updateCache(String reloadReq) {
		logger.info("Updating cache......");
		logger.debug("ReloadReq: " + reloadReq);
		mDMSCreateRepository.updateCache(reloadReq);
	}

	public String getContentForPush(Object fileContents, MDMSCreateRequest mDMSCreateRequest, Boolean isCreate)
			throws Exception {

		Map<String, Map<String, Object>> masterConfigMap = MDMSApplicationRunnerImpl.getMasterConfigMap();
		logger.info("Building content for git push.....");
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		Object moduleContent = fileContents;
		if (null == moduleContent) {
			throw new CustomException("400", "There is no master data available for this module: "
					+ mDMSCreateRequest.getMasterMetaData().getModuleName());
		}
		String moduleContentString = mapper.writeValueAsString(moduleContent);
		Map<String, Object> moduleDataMap = mapper.readValue(moduleContentString, Map.class);
		List<Object> masterData = new ArrayList<>();
		try {
			masterData = (List<Object>) moduleDataMap.get(mDMSCreateRequest.getMasterMetaData().getMasterName());
		} catch (Exception e) {
			throw new CustomException("500", "Couldn't fetch master data due to a parse exception");
		}
		String moduleContentJson = mapper.writeValueAsString(moduleContent);
		if (isCreate) {
			if (null == masterData) {
				logger.info("Master doesn't exist, Posting this master data to the module's file.....");
				List<Object> newMaster = new ArrayList<>();
				newMaster.addAll(mDMSCreateRequest.getMasterMetaData().getMasterData());
				moduleDataMap.put(mDMSCreateRequest.getMasterMetaData().getMasterName(), newMaster);
				moduleContentJson = mapper.writeValueAsString(moduleDataMap);

				return moduleContentJson;
			}
			masterData.addAll(0, mDMSCreateRequest.getMasterMetaData().getMasterData());			
			result = buildPushContent(moduleContentJson, mDMSCreateRequest, masterData);
		} else {
			if (null == masterData) {
				throw new CustomException("400", "No master data available for this master");
			}

			List<String> keys = mDMSUtils.getUniqueKeys(masterConfigMap, mDMSCreateRequest);
			if (null == keys) {
				throw new CustomException("400", "There are duplicate mdms-configs for this master: "
						+ mDMSCreateRequest.getMasterMetaData().getMasterName());
			} else if (keys.isEmpty()) {
				logger.info("Skipping Validation.....");
				masterData.addAll(mDMSCreateRequest.getMasterMetaData().getMasterData());
			} else {
				logger.info("keys: " + keys.toString());
				if (null != keys) {
					Map<String, Integer> inputDataMap = new HashMap<>();
					for (int i = 0; i < mDMSCreateRequest.getMasterMetaData().getMasterData().size(); i++) {
						StringBuilder mapKey = new StringBuilder();
						for (String key : keys) {
							String element = mapper
									.writeValueAsString(mDMSCreateRequest.getMasterMetaData().getMasterData().get(i));
							mapKey.append(JsonPath.read(element, key).toString());
						}
						inputDataMap.put(mapKey.toString(), i);
					}
					logger.info("inputDataMap: " + inputDataMap);

					ListIterator<Object> iterator = masterData.listIterator();
					while (iterator.hasNext()) {
						Object master = iterator.next();
						StringBuilder mapKey = new StringBuilder();
						for (String key : keys) {
							String element = mapper.writeValueAsString(master);
							mapKey.append(JsonPath.read(element, key).toString());
						}
						Integer index = inputDataMap.get(mapKey.toString());
						if (null == index) {
							continue;
						} else {
							iterator.remove();
							logger.info("adding master to file: " + mapper.writeValueAsString(
									mDMSCreateRequest.getMasterMetaData().getMasterData().get(index)));
							iterator.add(mDMSCreateRequest.getMasterMetaData().getMasterData().get(index));
						}
					}
				}
			}

			result = buildPushContent(moduleContentJson, mDMSCreateRequest, masterData);
		}

		return result;

	}

	public String buildPushContent(String moduleContentJson, MDMSCreateRequest mDMSCreateRequest,
			List<Object> masterData) {
		logger.debug("moduleContentJson: " + moduleContentJson);
		DocumentContext documentContext = JsonPath.parse(moduleContentJson);
		try {
			documentContext.put("$", mDMSCreateRequest.getMasterMetaData().getMasterName(), masterData);
		} catch (Exception e) {
			logger.error("master data couldn't be added to the master list: ", e);
			throw new CustomException("400", "There is no master data available for this master: "
					+ mDMSCreateRequest.getMasterMetaData().getMasterName());
		}
		moduleContentJson = documentContext.jsonString().toString();
		logger.info("Updated contents: " + moduleContentJson);

		return moduleContentJson;
	}

	public String getFilePath(Map<String, String> filePathMap, MDMSCreateRequest mDMSCreateRequest) {
		String fileName = filePathMap.get(mDMSCreateRequest.getMasterMetaData().getTenantId() + "-"
				+ mDMSCreateRequest.getMasterMetaData().getModuleName() + "-"
				+ mDMSCreateRequest.getMasterMetaData().getMasterName());
		if (null == fileName) {
			/*
			 * throw new CustomException("400",
			 * "No data available for this module. NOTE: Please check if the json file exists for this module."
			 * +
			 * "If it does, please check your spelling of moduleName param, It is case-sensitive."
			 * );
			 */
			fileName = mDMSCreateRequest.getMasterMetaData().getMasterName() + ".json";
		}
		StringBuilder filePath = new StringBuilder();
		filePath.append(dataRootFolder);
		String[] tenantArray = mDMSCreateRequest.getMasterMetaData().getTenantId().split("[.]");
		StringBuilder folderPath = new StringBuilder();
		for (int i = 0; i < tenantArray.length; i++) {
			folderPath.append(tenantArray[i]).append("/");
		}
		filePath.append("/").append(folderPath.toString()).append(mDMSCreateRequest.getMasterMetaData().getModuleName())
				.append("/").append(fileName);
		logger.info("filePath: " + filePath.toString());

		return filePath.toString();
	}

	public String getStateLevelFilePath(Map<String, String> filePathMap, MDMSCreateRequest mDMSCreateRequest) {
		String tenantId = mDMSCreateRequest.getMasterMetaData().getTenantId();
		String[] tenantArray = null;
		if (tenantId.contains(".")) {
			tenantArray = tenantId.split("\\.");
		}
		String fileName = filePathMap.get(tenantArray[0] + "-" + mDMSCreateRequest.getMasterMetaData().getModuleName()
				+ "-" + mDMSCreateRequest.getMasterMetaData().getMasterName());
		if (null == fileName) {
			/*
			 * throw new CustomException("400",
			 * "No data available for this module. NOTE: Please check if the json file exists for this module."
			 * +
			 * "If it does, please check your spelling of moduleName param, It is case-sensitive."
			 * );
			 */
			fileName = mDMSCreateRequest.getMasterMetaData().getMasterName() + ".json";
		}
		StringBuilder filePath = new StringBuilder();
		filePath.append(dataRootFolder);
		StringBuilder folderPath = new StringBuilder();
		folderPath.append(tenantArray[0]).append("/");
		filePath.append("/").append(folderPath.toString()).append(mDMSCreateRequest.getMasterMetaData().getModuleName())
				.append("/").append(fileName);
		logger.info("filePath: " + filePath.toString());

		return filePath.toString();
	}

	public String getBranchHead() {
		StringBuilder getBranchHeadUri = new StringBuilder();
		getBranchHeadUri.append(MDMSConstants.GITHUB_HOST).append(egovRepoOwner + "/" + egovRepo + "/")
				.append(MDMSConstants.EGOV_REF_PATH);
		logger.info("URI: " + getBranchHeadUri.toString());
		Object branchHeadResponse = mDMSCreateRepository.get(getBranchHeadUri.toString(), userName, password);

		String branchHeadSHA = JsonPath.read(branchHeadResponse.toString(), MDMSConstants.BRANCHHEADSHA_JSONPATH);

		return branchHeadSHA;
	}

	public String getBaseTree(String branchHeadSHA) {
		StringBuilder getBaseTreeUri = new StringBuilder();
		getBaseTreeUri.append(MDMSConstants.GITHUB_HOST).append(egovRepoOwner + "/" + egovRepo + "/")
				.append(MDMSConstants.EGOV_TREE_PATH).append(branchHeadSHA);
		logger.info("URI: " + getBaseTreeUri.toString());
		Object baseTreeResponse = mDMSCreateRepository.get(getBaseTreeUri.toString(), userName, password);

		String baseTreeSHA = JsonPath.read(baseTreeResponse.toString(), MDMSConstants.BASETREESHA_JSONPATH);
		logger.debug("baseTreeSHA: " + branchHeadSHA);

		return baseTreeSHA;

	}

	public String createTree(String baseTreeSHA, String filePath, String contents) {
		StringBuilder getCreateTreeUri = new StringBuilder();
		getCreateTreeUri.append(MDMSConstants.GITHUB_HOST).append(egovRepoOwner + "/" + egovRepo + "/")
				.append(MDMSConstants.EGOV_CREATE_TREE_PATH);
		logger.info("URI: " + getCreateTreeUri.toString());
		DocumentContext documentContext = JsonPath.parse(MDMSConstants.CREATE_TREE_REQ);
		documentContext.put("$", "base_tree", baseTreeSHA);
		documentContext.put("$.tree.*", "path", filePath);
		documentContext.put("$.tree.*", "mode", MDMSConstants.GIT_BLOB_MODE);

		String prettyFormatString =toPrettyFormat(contents);
		documentContext.put("$.tree.*", "content", prettyFormatString);
		
		String body = documentContext.jsonString().toString();
		Object createTreeResponse = mDMSCreateRepository.post(getCreateTreeUri.toString(), body, userName, password);
		String newTreeSHA = JsonPath.read(createTreeResponse.toString(), MDMSConstants.CREATETREESHA_JSONPATH);
		logger.debug("newTreeSHA: " + newTreeSHA);

		return newTreeSHA;

	}
	
	/**
	   * Convert a JSON string to pretty print version
	   * @param jsonString
	   * @return
	   */
	  public static String toPrettyFormat(String jsonString) 
	  {
	      JsonParser parser = new JsonParser();
	      JsonObject json = parser.parse(jsonString).getAsJsonObject();

	      Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	      String prettyJson = gson.toJson(json);

	      return prettyJson;
	  }

	public String createCommit(String branchHeadSHA, String newTreeSHA, String message) throws JsonProcessingException {
		StringBuilder getCreateTreeUri = new StringBuilder();
		getCreateTreeUri.append(MDMSConstants.GITHUB_HOST).append(egovRepoOwner + "/" + egovRepo + "/")
				.append(MDMSConstants.EGOV_CREATE_COMMIT_PATH);
		logger.info("URI: " + getCreateTreeUri.toString());
		DocumentContext documentContext = JsonPath.parse(MDMSConstants.CREATE_COMMIT_REQ);
		String[] parents = new String[1];
		parents[0] = branchHeadSHA;
		documentContext.put("$", "message", message);
		documentContext.put("$", "tree", newTreeSHA);
		String body = documentContext.jsonString().toString().replace(":sha", branchHeadSHA);

		Object createCommitResponse = mDMSCreateRepository.post(getCreateTreeUri.toString(), body, userName, password);

		String newCommitSHA = JsonPath.read(createCommitResponse.toString(), MDMSConstants.CREATECOMMITSHA_JSONPATH);
		logger.debug("newCommitSHA: " + newCommitSHA);

		return newCommitSHA;

	}

	public String pushTheContents(String newCommitSHA) {
		StringBuilder getPushUri = new StringBuilder();
		getPushUri.append(MDMSConstants.GITHUB_HOST).append(egovRepoOwner + "/" + egovRepo + "/")
				.append(MDMSConstants.EGOV_REF_PATH);
		logger.info("URI: " + getPushUri.toString());
		DocumentContext documentContext = JsonPath.parse(MDMSConstants.PUSH_CONTENT_REQ);
		documentContext.put("$", "sha", newCommitSHA);
		String body = documentContext.jsonString().toString();
		logger.info("Body: " + body);

		Object pushResponse = mDMSCreateRepository.post(getPushUri.toString(), body, userName, password);

		logger.info("pushResponse: " + pushResponse.toString());

		return pushResponse.toString();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getConfigs(String tenantId, String module, String master) throws Exception {
		Map<String, Map<String, Object>> masterConfigMap = MDMSApplicationRunnerImpl.getMasterConfigMap();
		Map<String, Object> moduleDate = masterConfigMap.get(module);
		if (null == moduleDate) {
			throw new CustomException("400", "Module doesn't have mdms-configs:  " + module);
		}
		Object matserData = moduleDate.get(master);
		List<Object> list = new ArrayList<Object>();
		if (null == matserData) {
			Set<String> keys = moduleDate.keySet();
			for (String key : keys) {
				Object masterData = moduleDate.get(key);
				list.add(masterData);
			}
			return list;
		} else
			list.add(matserData);
		return list;
	}

}
