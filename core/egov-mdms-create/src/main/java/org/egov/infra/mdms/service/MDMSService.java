package org.egov.infra.mdms.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.egov.MDMSApplicationRunnerImpl;
import org.egov.infra.mdms.repository.MDMSCreateRepository;
import org.egov.infra.mdms.utils.MDMSConstants;
import org.egov.infra.mdms.utils.MDMSUtils;
import org.egov.mdms.model.MDMSCreateRequest;
import org.egov.tracer.model.CustomException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	@Autowired
	private MDMSCreateRepository mDMSCreateRepository;
	
	@Autowired
	private MDMSUtils mDMSUtils;
	
	public Map<String, Map<String, JSONArray>> gitPush(MDMSCreateRequest mDMSCreateRequest, Boolean isCreate) throws Exception{
		Map<String, String> filePathMap = MDMSApplicationRunnerImpl.getFilePathMap();
		ObjectMapper mapper = new ObjectMapper();
		Long startTime = null;
		Long endTime = null;
		
		startTime = new Date().getTime();
		Object fileContents = getFileContents(filePathMap, mDMSCreateRequest);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: "+(endTime - startTime)+"ms");
		if(null == fileContents) 
			throw new CustomException("400","Invalid Tenant Id");
		
		startTime = new Date().getTime();
		String content = getContentForPush(fileContents, mDMSCreateRequest, isCreate);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: "+(endTime - startTime)+"ms");
    	String filePath = getFilePath(filePathMap, mDMSCreateRequest);
    	
		logger.info("Step 1: Getting branch head......");
		startTime = new Date().getTime();
		String branchHeadSHA = getBranchHead();
		endTime = new Date().getTime();
		logger.info("Time taken for this step: "+(endTime - startTime)+"ms");
		
		logger.info("Step 2: Getting Base Tree......");
		startTime = new Date().getTime();
		String baseTreeSHA = getBaseTree(branchHeadSHA);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: "+(endTime - startTime)+"ms");
		
		logger.info("Step 3: Creating a New Tree......");
		startTime = new Date().getTime();
		String newTreeSHA = createTree(baseTreeSHA, filePath, content);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: "+(endTime - startTime)+"ms");
		
		logger.info("Step 4: Creating a New Commit......");
		startTime = new Date().getTime();
		String commitMessage = "commit by "+userName+" at epoch time: "+new Date().getTime();
		String newCommitSHA = createCommit(branchHeadSHA, newTreeSHA, commitMessage);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: "+(endTime - startTime)+"ms");
		
		logger.info("Step 5: Pushing the Contents to git......");
		startTime = new Date().getTime();
		pushTheContents(newCommitSHA);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: "+(endTime - startTime)+"ms");
		
		startTime = new Date().getTime();
		updateCache(MDMSConstants.READ_FILE_PATH_APPEND + filePath);
		endTime = new Date().getTime();
		logger.info("Time taken for this step: "+(endTime - startTime)+"ms");

		logger.info("Find your changes at: "+ MDMSConstants.FINAL_FILE_PATH_APPEND + filePath);
		
		Map<String, Map<String, JSONArray>> response = new HashMap<>();
		Map<String, JSONArray> entry = new HashMap<>();
		JSONArray data = new JSONArray();
		data.add(JsonPath.read(mapper.writeValueAsString(mDMSCreateRequest.getMasterMetaData()), 
				MDMSConstants.MASTERDATA_JSONPATH));
		entry.put(mDMSCreateRequest.getMasterMetaData().getMasterName(), data);
		response.put(mDMSCreateRequest.getMasterMetaData().getModuleName(), entry);

		return response;
		
	}
	
	public Object getFileContents(Map<String, String> filePathMap, MDMSCreateRequest mDMSCreateRequest) throws Exception{
		String filePath = getFilePath(filePathMap, mDMSCreateRequest);
		filePath = MDMSConstants.READ_FILE_PATH_APPEND + filePath;
		Object fileContents = mDMSCreateRepository.getFileContents(filePath);
		return fileContents;
	}
	
	public void updateCache(String filePath){
		logger.info("Updating cache......");
		mDMSCreateRepository.updateCache(filePath);
	}
	
	public String getContentForPush(Object fileContents, 
			MDMSCreateRequest mDMSCreateRequest, Boolean isCreate) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Object moduleContent = fileContents;
		if(null == moduleContent){
			throw new CustomException("400", "There is no master data available for this module: "+mDMSCreateRequest.getMasterMetaData().getModuleName());
		}
		String moduleContentString = mapper.writeValueAsString(moduleContent);
		Map<String, Object> moduleDataMap = mapper.readValue(moduleContentString, Map.class);	
		List<Object> masterData = new ArrayList<>();
		try{
		     masterData = (List<Object>) moduleDataMap.get(mDMSCreateRequest.getMasterMetaData().getMasterName());
		}catch(Exception e){
			throw new CustomException("500","Couldn't fetch master data due to a parse exception");
		}
		String moduleContentJson = mapper.writeValueAsString(moduleContent);
		if(isCreate){
			masterData.addAll(mDMSCreateRequest.getMasterMetaData().getMasterData());
            logger.info("moduleContentJson: "+moduleContentJson);
	    	DocumentContext documentContext = JsonPath.parse(moduleContentJson);
	    	try{
		    	documentContext.put("$", mDMSCreateRequest.getMasterMetaData().getMasterName(),
		    			masterData);
	    	}catch(Exception e){
				throw new CustomException("400", "There is no master data available for this master: "+mDMSCreateRequest.getMasterMetaData().getMasterName());
	    	}
	    	moduleContentJson = documentContext.jsonString().toString();
	    	logger.info("Updated contents: "+moduleContentJson);
		}else{
			List<Object> configs = getConfigs(mDMSCreateRequest.getMasterMetaData().getTenantId(),
					mDMSCreateRequest.getMasterMetaData().getModuleName(), mDMSCreateRequest.getMasterMetaData().getMasterName());
			if(!configs.isEmpty()){
				List<String> keys = JsonPath.read(configs.get(0).toString(), MDMSConstants.UNIQUEKEYS_JSONPATH);
				logger.info("keys: "+keys.toString());
				if(null != keys){
					Map<String, Object> conditionMap = new HashMap<>();
					for(String key: keys){
						conditionMap.put(key, JsonPath.read(mDMSCreateRequest.getMasterMetaData().getMasterData().get(0),
								key));
					}
					logger.info("conditionMap: "+conditionMap);
		            ListIterator<Object> iterator = masterData.listIterator();
		            int counter = 0;
		            int j = 0;
		            while(iterator.hasNext()){
		            	Object master = iterator.next();
		            	counter = 0;
						for(int i = 0; i < keys.size(); i++){
							if(JsonPath.read(master, keys.get(i).toString()).equals(conditionMap.get(keys.get(i)))){
								counter++;
							}else{
								break;
							}
						  }
						if(counter == keys.size()){
							iterator.remove();
							iterator.add(mapper.writeValueAsString(mDMSCreateRequest.getMasterMetaData().getMasterData().get(j)));
				            if(j < mDMSCreateRequest.getMasterMetaData().getMasterData().size())
				            	j++;
							break;
						}
		            }
		            if(counter != keys.size()){
		            	throw new CustomException("400", "Invalid Request");
		            }
				}
			}
            logger.info("moduleContentJson: "+moduleContentJson);
	    	DocumentContext documentContext = JsonPath.parse(moduleContentJson);
	    	try{
		    	documentContext.put("$", mDMSCreateRequest.getMasterMetaData().getMasterName(),
		    			masterData);
	    	}catch(Exception e){
	    		logger.error("master data couldn't be added to the master list: ",e);
				throw new CustomException("400", "There is no master data available for this master: "+mDMSCreateRequest.getMasterMetaData().getMasterName());
	    	}
	    	moduleContentJson = documentContext.jsonString().toString();
	    	logger.info("Updated contents: "+moduleContentJson);    
		}
    	
    	return moduleContentJson;
	}
		
	public String getFilePath(Map<String, String> filePathMap, MDMSCreateRequest mDMSCreateRequest){
		String fileName = filePathMap.get(mDMSCreateRequest.getMasterMetaData().getTenantId() +"-"+ mDMSCreateRequest.getMasterMetaData().getModuleName());
		if(null == fileName){
			throw new CustomException("400", "No data available for this master");
		}
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
		logger.debug("baseTreeSHA: "+branchHeadSHA);
		
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

		Object createTreeResponse = mDMSCreateRepository.
				post(getCreateTreeUri.toString(), body, userName, password);
		
		String newTreeSHA = JsonPath.read(createTreeResponse.toString(), MDMSConstants.CREATETREESHA_JSONPATH);
		logger.debug("newTreeSHA: "+newTreeSHA);
		
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

		Object createCommitResponse = mDMSCreateRepository.
				post(getCreateTreeUri.toString(), body, userName, password);
		
		String newCommitSHA = JsonPath.read(createCommitResponse.toString(), MDMSConstants.CREATECOMMITSHA_JSONPATH);
		logger.debug("newCommitSHA: "+newCommitSHA);
		
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
	
	
	@SuppressWarnings("unchecked")
	public List<Object> getConfigs(String tenantId, String module, String master) throws JsonProcessingException{
		Map<String, Map<String, Object>> validationMap = MDMSApplicationRunnerImpl.getValidationMap();
		Map<String, Object> allMasters = 
				validationMap.get(tenantId+"-"+module);
		if(null == allMasters){
			throw new CustomException("400", "No data avaialble for this module and for this tenant");
		}
		List<Object> allmasterConfigs = new ArrayList<>();
		allmasterConfigs = (List<Object>) allMasters.get(MDMSConstants.CONFIG_ARRAY_KEY);
		if(null == allmasterConfigs || allmasterConfigs.isEmpty()){
			logger.info("There is no mdms-config for this module: "+master);
			return new ArrayList<>();
		}
		if(null != master){
			List<Object> masterConfig = mDMSUtils.filter(allmasterConfigs, MDMSConstants.MASTERNAME_JSONPATH, master);
			return masterConfig;
		}else
			return allmasterConfigs;
	}

}
