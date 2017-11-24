package org.egov.infra.mdms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.infra.mdms.repository.MDMSCreateRepository;
import org.egov.infra.mdms.utils.MDMSConstants;
import org.egov.mdms.model.MDMSCreateRequest;
import org.egov.mdms.model.MasterDetail;
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
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Service
@Slf4j
public class MDMSService {
	
	public static final Logger logger = LoggerFactory.getLogger(MDMSService.class);
	
	@Value("${egov.github.user}")
	private String userName;
	
	@Value("${egov.github.pass}")
	private String password;
	
	@Autowired
	private MDMSCreateRepository mDMSCreateRepository;


	public Map<String, Map<String, JSONArray>> getMaster(MdmsCriteriaReq mdmsCriteriaReq) {
		Map<String, List<Object>> tenantIdMap = MDMSApplicationRunnerImpl.getTenantMap();
		List<Object> list = tenantIdMap.get(mdmsCriteriaReq.getMdmsCriteria().getTenantId());
		
		if(list == null) 
		throw new CustomException("Invalid_tenantId.MdmsCriteria.tenantId","Invalid Tenant Id");
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<ModuleDetail> moduleDetails = mdmsCriteriaReq.getMdmsCriteria().getModuleDetails();
		// String masterDataJson = null;
		String tenantJsonList = null;
		Map<String, Map<String, JSONArray>> moduleMap = new HashMap<>(); 
		
		try {
			// masterDataJson = objectMapper.writeValueAsString(tenantIdMap);
			tenantJsonList = objectMapper.writeValueAsString(list);
			log.info("MDMSService tenantJsonList:" + tenantJsonList);
			
			
			for(ModuleDetail moduleDetail : moduleDetails) {
				//List<Map<String, JSONArray>> response = new ArrayList<>();
				String moduleFilterJsonPath = "$[?(@.moduleName==".concat("\"").
						concat(moduleDetail.getModuleName()).concat("\"").concat(")]");
				log.info("moduleFilterJsonPath:"+ moduleFilterJsonPath);
				JSONArray mastersOfModule = JsonPath.read(tenantJsonList, moduleFilterJsonPath);
				log.info("getMaster moduleDetail:"+moduleDetail);
				Map<String, JSONArray> resMap = new HashMap<String, JSONArray>();
				for(MasterDetail masterDetail : moduleDetail.getMasterDetails()) {
					//String moduleJson = objectMapper.writeValueAsString(list);
					JSONArray masters = JsonPath.read(mastersOfModule, "$.*".concat(masterDetail.getName()).concat(".*"));
					log.info("masters:"+masters);
					if(masterDetail.getFilter() != null) 
						masters = filterMaster(masters, masterDetail.getFilter());
					
					resMap.put(masterDetail.getName(), masters);
					//response.add(resMap);
				}
				
				moduleMap.put(moduleDetail.getModuleName(), resMap);
				System.out.println("moduleMap:"+moduleMap);
				//response.clear();
			}
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
		return moduleMap;
	}
	
	public JSONArray  filterMaster(JSONArray masters, String filterExp) {
		JSONArray filteredMasters = JsonPath.read(masters, filterExp);
		System.out.println("filteredMasters: "+filteredMasters);
		return filteredMasters;
	}
	
	public String gitPush(MDMSCreateRequest mDMSCreateRequest) throws JsonProcessingException{
		Map<String, List<Object>> tenantIdMap = MDMSApplicationRunnerImpl.getTenantMap();
		Map<String, String> filePathMap = MDMSApplicationRunnerImpl.getFilePathMap();
		List<Object> tenantSpecificModuleData = tenantIdMap.get(mDMSCreateRequest.getMasterMetaData().getTenantId());
		if(null == tenantSpecificModuleData) 
			throw new CustomException("500","Invalid Tenant Id");
		Integer index = 0;
		String content = preProcessor(tenantSpecificModuleData, mDMSCreateRequest, tenantIdMap, index);
    	String filePath = getFilePath(filePathMap, mDMSCreateRequest);
    	
		//get the head of the branch
		logger.info("Step 1: Getting branch head......");
		String branchHeadSHA = getBranchHead();
		logger.info("Step 1 COMPLETED SUCCESSFULLY!");
		
		//get the latest commit to that branch and save its sha
		logger.info("Step 2: Getting Base Tree......");
		String baseTreeSHA = getBaseTree(branchHeadSHA);
		logger.info("Step 2 COMPLETED SUCCESSFULLY!");
		
		//create a tree with base_tree as last commit and contents to be written
		logger.info("Step 3: Creating a New Tree......");
		String newTreeSHA = createTree(baseTreeSHA, filePath, content);
		logger.info("Step 3 COMPLETED SUCCESSFULLY!");
		
		//create a commit for this tree
		logger.info("Step 4: Creating a New Commit......");
		String commitMessage = "commit by "+userName+" at epoch time: "+new Date().getTime();
		String newCommitSHA = createCommit(branchHeadSHA, newTreeSHA, commitMessage);
		logger.info("Step 4 COMPLETED SUCCESSFULLY!");
		
		//push the contents
		logger.info("Step 5: Pushing the Contents to git......");
		pushTheContents(newCommitSHA);
		logger.info("Step 5 COMPLETED SUCCESSFULLY!");
		
		logger.info("Updating cache......");
		updateCache(tenantSpecificModuleData, content, index, tenantIdMap, mDMSCreateRequest.getMasterMetaData().getTenantId());
		logger.info("Cache Update COMPLETE!");

		logger.info("Find your changes at: "+ MDMSConstants.FINAL_FILE_PATH_APPEND + filePath);
    	DocumentContext documentContext = JsonPath.parse(MDMSConstants.SUCCESS_RES);
    	documentContext.put("$", "file", MDMSConstants.FINAL_FILE_PATH_APPEND + filePath);

		return documentContext.jsonString().toString();
		
	}
	
	public String preProcessor(List<Object> tenantSpecificModuleData, 
			MDMSCreateRequest mDMSCreateRequest, Map<String, List<Object>> tenantIdMap, Integer index) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		Object moduleContent = null;
		for(Object moduleData: tenantSpecificModuleData){
			if(moduleData.toString().contains("moduleName="+mDMSCreateRequest.getMasterMetaData().getModuleName())){
				moduleContent = moduleData;
				break;
			}
			index++;
		}
		logger.info("Module content: "+moduleContent);
		String moduleContentJson = mapper.writeValueAsString(moduleContent);
    	DocumentContext documentContext = JsonPath.parse(moduleContentJson);
    	documentContext.put("$", mDMSCreateRequest.getMasterMetaData().getMasterName(),
    			mDMSCreateRequest.getMasterMetaData().getMasterData());
    	moduleContentJson = documentContext.jsonString().toString();
    	logger.info("Updated contents: "+moduleContentJson);
    	
    	return moduleContentJson;
	}
	
	public void updateCache(List<Object> tenantSpecificModuleData, String moduleContentJson
			,Integer index, Map<String, List<Object>> tenantIdMap, String tenantId){
    	logger.info("index: "+index);
    	tenantSpecificModuleData.add(index, moduleContentJson);
    	tenantIdMap.put(tenantId, tenantSpecificModuleData);
    	
    	MDMSApplicationRunnerImpl.setTenantMap(tenantIdMap);
	}
	
	public String getFilePath(Map<String, String> filePathMap, MDMSCreateRequest mDMSCreateRequest){
		String fileName = filePathMap.get(mDMSCreateRequest.getMasterMetaData().getTenantId() +"-"+ mDMSCreateRequest.getMasterMetaData().getModuleName());
		StringBuilder filePath = new StringBuilder();
		filePath.append(MDMSConstants.DATA_ROOT_FOLDER);
		String[] tenantArray = mDMSCreateRequest.getMasterMetaData().getTenantId().split("[.]");
		StringBuilder folderPath = new StringBuilder();
		for(int i = 0; i < tenantArray.length; i++){
			folderPath.append(tenantArray[i]).append("/");
		}
		filePath.append("/").append(folderPath.toString()).append(fileName);
		logger.info("filePath: "+filePath.toString());
		
		return filePath.toString();
		
	}
	
	public String getBranchHead(){
		StringBuilder getBranchHeadUri = new StringBuilder();
		getBranchHeadUri.append(MDMSConstants.GITHUB_HOST).append(MDMSConstants.EGOV_REPO_PATH)
		                .append(MDMSConstants.EGOV_REF_PATH);
		logger.info("URI: "+getBranchHeadUri.toString());
		Object branchHeadResponse = mDMSCreateRepository.
				get(getBranchHeadUri.toString(), userName, password);
		
		String branchHeadSHA = JsonPath.read(branchHeadResponse.toString(), MDMSConstants.BRANCHHEADSHA_JSONPATH);
		logger.info("branchHeadSHA: "+branchHeadSHA);
		
		return branchHeadSHA;
		
	}
	
	public String getBaseTree(String branchHeadSHA){
		StringBuilder getBaseTreeUri = new StringBuilder();
		getBaseTreeUri.append(MDMSConstants.GITHUB_HOST).append(MDMSConstants.EGOV_REPO_PATH)
		                .append(MDMSConstants.EGOV_TREE_PATH).append(branchHeadSHA);
		logger.info("URI: "+getBaseTreeUri.toString());
		Object baseTreeResponse = mDMSCreateRepository.
				get(getBaseTreeUri.toString(), userName, password);
		
		String baseTreeSHA = JsonPath.read(baseTreeResponse.toString(), MDMSConstants.BASETREESHA_JSONPATH);
		logger.info("baseTreeSHA: "+branchHeadSHA);
		
		return baseTreeSHA;
		
	}
	
	public String createTree(String baseTreeSHA, String filePath, String contents){
		StringBuilder getCreateTreeUri = new StringBuilder();
		getCreateTreeUri.append(MDMSConstants.GITHUB_HOST).append(MDMSConstants.EGOV_REPO_PATH)
		                .append(MDMSConstants.EGOV_CREATE_TREE_PATH);
		logger.info("URI: "+getCreateTreeUri.toString());
    	DocumentContext documentContext = JsonPath.parse(MDMSConstants.CREATE_TREE_REQ);
    	documentContext.put("$", "base_tree", baseTreeSHA);
    	documentContext.put("$.tree.*", "path", filePath);
    	documentContext.put("$.tree.*", "mode", MDMSConstants.GIT_BLOB_MODE);
    	documentContext.put("$.tree.*", "content", contents);

    	String body = documentContext.jsonString().toString();
    	logger.info("Body: "+body);

		Object createTreeResponse = mDMSCreateRepository.
				post(getCreateTreeUri.toString(), body, userName, password);
		
		String newTreeSHA = JsonPath.read(createTreeResponse.toString(), MDMSConstants.CREATETREESHA_JSONPATH);
		logger.info("newTreeSHA: "+newTreeSHA);
		
		return newTreeSHA;
		
	}
	
	public String createCommit(String branchHeadSHA, String newTreeSHA, String message) throws JsonProcessingException{
		StringBuilder getCreateTreeUri = new StringBuilder();
		getCreateTreeUri.append(MDMSConstants.GITHUB_HOST).append(MDMSConstants.EGOV_REPO_PATH)
		                .append(MDMSConstants.EGOV_CREATE_COMMIT_PATH);
		logger.info("URI: "+getCreateTreeUri.toString());
    	DocumentContext documentContext = JsonPath.parse(MDMSConstants.CREATE_COMMIT_REQ);
    	String[] parents = new String[1];
    	parents[0] = branchHeadSHA;
    	documentContext.put("$", "message", message);
    	documentContext.put("$", "tree", newTreeSHA);
    	String body = documentContext.jsonString().toString().replace(":sha", branchHeadSHA);
    	logger.info("Body: "+body);

		Object createCommitResponse = mDMSCreateRepository.
				post(getCreateTreeUri.toString(), body, userName, password);
		
		String newCommitSHA = JsonPath.read(createCommitResponse.toString(), MDMSConstants.CREATECOMMITSHA_JSONPATH);
		logger.info("newCommitSHA: "+newCommitSHA);
		
		return newCommitSHA;
		
	}
	
	public String pushTheContents(String newCommitSHA){
		StringBuilder getPushUri = new StringBuilder();
		getPushUri.append(MDMSConstants.GITHUB_HOST).append(MDMSConstants.EGOV_REPO_PATH)
		                .append(MDMSConstants.EGOV_REF_PATH);
		logger.info("URI: "+getPushUri.toString());
    	DocumentContext documentContext = JsonPath.parse(MDMSConstants.PUSH_CONTENT_REQ);
    	documentContext.put("$", "sha", newCommitSHA);
    	String body = documentContext.jsonString().toString();
    	logger.info("Body: "+body);

		Object pushResponse = mDMSCreateRepository.
				post(getPushUri.toString(), body, userName, password);
		
		logger.info("pushResponse: "+pushResponse.toString());
		
		return pushResponse.toString();
	}

}
