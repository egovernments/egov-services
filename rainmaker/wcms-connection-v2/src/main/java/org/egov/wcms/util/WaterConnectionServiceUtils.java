package org.egov.wcms.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.config.WaterConnectionConfig;
import org.egov.wcms.mdms.MDMSConstants;
import org.egov.wcms.web.models.AuditDetails;
import org.egov.wcms.web.models.SearcherRequest;
import org.egov.wcms.web.models.WaterConnectionRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WaterConnectionServiceUtils {
	
	@Autowired
	private WaterConnectionConfig mainConfiguration;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
		
	private final String searchNamePlaceholder = "{searchName}";
	
	/**
	 * Common method to fetch egov-searcher request for any search definition within this moudule.
	 * 
	 * @param uri
	 * @param request
	 * @param requestInfo
	 * @param searchDefName
	 * @return SearcherRequest
	 */
	public SearcherRequest getSearcherRequest(StringBuilder uri, Object request, RequestInfo requestInfo, String searchDefName) {
		uri.append(mainConfiguration.getEgovSeacherHost());
		String endPoint = mainConfiguration.getEgovSearcherEndpoint().replace(searchNamePlaceholder, searchDefName);
		uri.append(endPoint);
		
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(request).build();
		
	}
	
	/**
	 * Method to fetch request for user search from egov-user. 
	 * Return type is hashmap as we're experimenting to come up with one single method that can return inter-svc call request for any use-case.
	 * 
	 * @param uri
	 * @param requestInfo
	 * @param tenantId
	 * @param phone
	 * @return HashMap
	 */
	public HashMap<String, Object> getUserSearchRequest(StringBuilder uri, RequestInfo requestInfo, String tenantId, String phone) {
		uri.append(mainConfiguration.getUserSvcHost()).append(mainConfiguration.getUserSearchEndpoint());
		
		HashMap<String, Object> request = new HashMap<>();
		request.put("RequestInfo", requestInfo);
		request.put("tenantId", tenantId);
		request.put("phone", phone);
		
		return request;
		
	}
	
	/**
	 * Method to return auditDetails for create/update flows
	 * 
	 * @param by
	 * @param isCreate
	 * @return AuditDetails
	 */
	public AuditDetails getAuditDetails(String by, Boolean isCreate) {
		Long time = new Date().getTime();
		if(isCreate)
			return AuditDetails.builder().createdBy(by).lastModifiedBy(by).createdTime(time).lastModifiedTime(time).build();
		else
			return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(time).build();
	}
	
	/**
	 * Method to fetch valid codes from mdms for a given master.
	 * This will be used to validate the codes (references) that are sent in the request during create/update.
	 * 
	 * @param masterName
	 * @return List
	 */
	public List<String> getValidCodes(String masterName, Map<String, List<Map<String, Object>>> mastersMap){
		List<String> codes = new ArrayList<>();
		if(!CollectionUtils.isEmpty(mastersMap.get(masterName))) {
			codes = JsonPath.read(mastersMap.get(masterName), MDMSConstants.WCMS_MDMS_RES_CODE_JSONPATH);
		}
		log.debug("Valid Codes for "+masterName+" master: "+codes);
		return codes;
	}
	
	/**
	 * For a given ApplicationType, there is a specific set of documentTypes allowed. This method returns such documentTypes for the applicationType.
	 * 
	 * @param applicationType
	 * @return List
	 */
	public List<String> getValidDocumentTypesForApplicationType(String applicationType, Map<String, List<Map<String, Object>>> mastersMap){
		List<String> documentTypes = new ArrayList<>();
		if(!CollectionUtils.isEmpty(mastersMap.get(MDMSConstants.APPLICATIONTYPE_MASTER_NAME))) {
			documentTypes = JsonPath.read(mastersMap.get(MDMSConstants.APPLICATIONTYPE_MASTER_NAME), 
					MDMSConstants.WCMS_MDMS_RES_VALIDDOCTYPE_JSONPATH.replace("{$}", applicationType));
		}
		log.debug("Valid document types for "+applicationType+": "+documentTypes);
		return documentTypes;
	}
	
	/**
	 * Returns mapper with all the appropriate properties reqd in our functionalities.
	 * 
	 * @return ObjectMapper
	 */
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        return mapper;
	}
	
	/**
	 * Default response is responseInfo with error status and empty lists
	 * 
	 * @param requestInfo
	 * @return ServiceResponse
	 */
	public WaterConnectionRes getDefaultWaterConnectionResponse(RequestInfo requestInfo) {
		return WaterConnectionRes.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true))
		.connections(new ArrayList<>()).actionHistory(new ArrayList<>()).build();
	}
	
	/**
	 * Mock method to return connectionNumber, this will be replaced by idGen integration.
	 * 
	 * @return String
	 */
	public String generateConnectonNumber() {		
		return new StringBuilder().append("WCMS/").append(UUID.randomUUID().toString()).toString();
	}
}
