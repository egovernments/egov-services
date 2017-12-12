package org.egov.infra.mdms.utils;

import org.springframework.stereotype.Component;

@Component
public class MDMSConstants {

	//GitHub host components
	public static final String GITHUB_HOST = "https://api.github.com/repos/";
	public static final String EGOV_REPO_OWNER = "egovernments";
	public static final String EGOV_REPO = "egov-services";
	public static final String EGOV_REPO_PATH = EGOV_REPO_OWNER +"/"+EGOV_REPO +"/";
	public static final String EGOV_REF_PATH = "git/refs/heads/master";
	public static final String EGOV_TREE_PATH = "commits/";
	public static final String EGOV_CREATE_TREE_PATH = "git/trees";
	public static final String EGOV_CREATE_COMMIT_PATH = "git/commits";
	public static final String FINAL_FILE_PATH_APPEND = "https://github.com/egovernments/egov-services/blob/master/";
	
	
	public static final String GIT_BLOB_MODE = "100644";
	//public static final String DATA_ROOT_FOLDER = "test-data";
	public static final String CONFIG_ARRAY_KEY= "mdms-config";




	
	
	
	//JsonPaths required to process git response
	public static final String BRANCHHEADSHA_JSONPATH = "$.object.sha";
	public static final String BASETREESHA_JSONPATH = "$.commit.tree.sha";
	public static final String CREATETREESHA_JSONPATH = "$.sha";
	public static final String CREATECOMMITSHA_JSONPATH = "$.sha";
	public static final String UNIQUEKEYS_JSONPATH = "$.uniqueKeys";
	public static final String MASTERNAME_JSONPATH = "$.masterName";
	public static final String MASTERDATA_JSONPATH = "$.masterData";


	

	
	
	
	//Requests to git rest apis
	public static final String CREATE_TREE_REQ = "{\"base_tree\": \"\",\"tree\": [{\"path\": \"\",\"mode\": \"\",\"content\":\"\"}]}";
	public static final String CREATE_COMMIT_REQ = "{\"message\": \"\",\"parents\": [\":sha\"],\"tree\": \"\"}";
	public static final String PUSH_CONTENT_REQ = "{\"sha\": \"\",\"force\": true}";
	public static final String SUCCESS_RES = "{\"status\": \"SUCCESS\",\"file\": \"\"}";
	public static final String MDMS_RELOAD_RES = "{\"RequestInfo\": {},\"MdmsReq\": {}}";





	
	


}
