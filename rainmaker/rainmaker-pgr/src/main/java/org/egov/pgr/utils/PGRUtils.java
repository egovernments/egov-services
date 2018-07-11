package org.egov.pgr.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.pgr.contract.CountResponse;
import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.model.ActionHistory;
import org.egov.pgr.model.AuditDetails;
import org.egov.pgr.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PGRUtils {

	private static Map<Integer, String> employeeRolesPrecedenceMap = prepareEmployeeRolesPrecedenceMap();

	@Value("${egov.infra.searcher.host}")
	private String searcherHost;

	@Value("${egov.infra.searcher.endpoint}")
	private String searcherEndpoint;

	@Value("${egov.mdms.host}")
	private String mdmsHost;

	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;

	@Value("${egov.hr.employee.host}")
	private String hrEmployeeHost;

	@Value("${egov.hr.employee.v2.host}")
	private String hrEmployeeV2Host;

	@Value("${egov.hr.employee.search.endpoint}")
	private String hrEmployeeSearchEndpoint;

	@Value("${egov.common.masters.host}")
	private String commonMasterHost;

	@Value("${egov.common.masters.search.endpoint}")
	private String commonMasterSearchEndpoint;

	@Value("${egov.localization.host}")
	private String localizationHost;

	@Value("${egov.localization.search.endpoint}")
	private String localizationSearchEndpoint;

	@Value("${egov.user.host}")
	private String egovUserHost;

	@Value("${egov.user.search.endpoint}")
	private String egovUserSearchEndpoint;

	@Autowired
	private ResponseInfoFactory factory;

	private static final String MODULE_NAME = "{moduleName}";

	private static final String SEARCH_NAME = "{searchName}";

	/**
	 * Prepares request and uri for service code search from MDMS
	 * 
	 * @param uri
	 * @param tenantId
	 * @param department
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	public MdmsCriteriaReq prepareSearchRequestForServiceCodes(StringBuilder uri, String tenantId, String department,
			RequestInfo requestInfo) {

		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME)
				.filter("[?(@.department=='" + department + "')]." + PGRConstants.SERVICE_CODES).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_PGR_MOD_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	/**
	 * Util method to return Auditdetails for create and update processes
	 * 
	 * @param by
	 * @param isCreate
	 * @return
	 */
	public AuditDetails getAuditDetails(String by, Boolean isCreate) {

		Long dt = new Date().getTime();
		if (isCreate)
			return AuditDetails.builder().createdBy(by).createdTime(dt).lastModifiedBy(by).lastModifiedTime(dt).build();
		else
			return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(dt).build();
	}

	/**
	 * Prepares request and uri for service type search from MDMS
	 * 
	 * @param uri
	 * @param tenantId
	 * @param department
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	public MdmsCriteriaReq prepareSearchRequestForServiceType(StringBuilder uri, String tenantId, String serviceCode,
			RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME)
				.filter("[?(@.serviceCode=='" + serviceCode + "')]." + PGRConstants.SERVICE_CODES).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_PGR_MOD_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	/**
	 * Prepares request and uri for service type search from MDMS
	 * 
	 * @param uri
	 * @param tenantId
	 * @param department
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	public MdmsCriteriaReq prepareMdMsRequest(String tenantId, String fieldName, String values,
			RequestInfo requestInfo) {

		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME)
				.filter("[?(@." + fieldName + " IN " + values + ")]." + PGRConstants.SERVICE_CODES).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_PGR_MOD_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	public MdmsCriteriaReq prepareMdMsRequestForDept(StringBuilder uri, String tenantId, String code,
			RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_DEPT_MASTERS_MASTER_NAME).filter("[?(@.code=='" + code + "')].name").build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_COMMON_MASTERS_MODULE_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	public MdmsCriteriaReq prepareMdMsRequestForDesignation(StringBuilder uri, String tenantId, String code,
			RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_DESIGNATION_MASTERS_MASTER_NAME).filter("[?(@.code=='" + code + "')].name")
				.build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_COMMON_MASTERS_MODULE_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareSearchRequestWithDetails(StringBuilder uri,
			ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.SEARCHER_PGR_MOD_NAME).replace(SEARCH_NAME,
				PGRConstants.SEARCHER_SRSEARCH_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}

	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareSearchRequestForAssignedTo(StringBuilder uri,
			ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.SEARCHER_PGR_MOD_NAME).replace(SEARCH_NAME,
				PGRConstants.SEARCHER_SRID_ASSIGNEDTO_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}

	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareCountRequestWithDetails(StringBuilder uri,
			ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.SEARCHER_PGR_MOD_NAME).replace(SEARCH_NAME,
				PGRConstants.SEARCHER_COUNT_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}
	
	public MdmsCriteriaReq prepareServiceDefSearchMdmsRequest(StringBuilder uri, String tenantId, RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_PGR_MOD_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

	public RequestInfoWrapper prepareRequestForEmployeeSearch(StringBuilder uri, RequestInfo requestInfo,
			ServiceReqSearchCriteria serviceReqSearchCriteria) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(hrEmployeeV2Host).append(hrEmployeeSearchEndpoint).append("?id=" + requestInfo.getUserInfo().getId())
				.append("&tenantId=" + serviceReqSearchCriteria.getTenantId());

		return requestInfoWrapper;
	}
	
	public RequestInfoWrapper prepareRequestForLocalization(StringBuilder uri, RequestInfo requestInfo, String locale,
			String tenantId, String module) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(localizationHost).append(localizationSearchEndpoint).append("?tenantId=" + tenantId)
				.append("&module=" + module).append("&locale=" + locale);

		return requestInfoWrapper;
	}

	public Map<String, Object> prepareRequestForUserSearch(StringBuilder uri, RequestInfo requestInfo, String userId,
			String tenantId) {
		Map<String, Object> userServiceRequest = new HashMap();
		String[] userIds = { userId };
		userServiceRequest.put("RequestInfo", requestInfo);
		userServiceRequest.put("tenantId", tenantId);
		userServiceRequest.put("id", Arrays.asList(userIds));

		uri.append(egovUserHost).append(egovUserSearchEndpoint);

		return userServiceRequest;
	}

	/**
	 * Default response is responseInfo with error status and empty lists
	 * 
	 * @param requestInfo
	 * @return ServiceResponse
	 */
	public ServiceResponse getDefaultServiceResponse(RequestInfo requestInfo) {
		return new ServiceResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
				new ArrayList<Service>(), new ArrayList<ActionHistory>());
	}

	/**
	 * Default response is responseInfo with error status and zero count
	 * 
	 * @param requestInfo
	 * @return CountResponse
	 */
	public CountResponse getDefaultCountResponse(RequestInfo requestInfo) {
		return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false), 0D);
	}

	/**
	 * Returns mapper with all the appropriate properties reqd in our
	 * functionalities.
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
	 * prepares and returns a map with integer keys starting from zero and values as
	 * role codes the integer values decides the precedence by which actions should
	 * be applied among there roles
	 */
	private static Map<Integer, String> prepareEmployeeRolesPrecedenceMap() {

		Map<Integer, String> map = new TreeMap<>();

		map.put(3, PGRConstants.ROLE_EMPLOYEE);
		map.put(2, PGRConstants.ROLE_DGRO);
		map.put(1, PGRConstants.ROLE_GRO);
		map.put(0, PGRConstants.ROLE_CSR);

		return map;
	}

	/**
	 * @return employeeRolesPrecedenceMap
	 */
	public static Map<Integer, String> getEmployeeRolesPrecedenceMap() {
		return employeeRolesPrecedenceMap;
	}

	/**
	 * Helper method which returns the precedent role among all the given roles
	 * 
	 * The employee precedent map is a tree map which will have the roles ordered
	 * based on their keys precedence
	 * 
	 * The method will fail if the list of roles is null, so the parameter must be
	 * null checked
	 * 
	 * If the none of roles in the precedence map has a match in roles object then
	 * the method will return null
	 */
	public String getPrecedentRole(List<String> roles) {

		for (Entry<Integer, String> entry : PGRUtils.getEmployeeRolesPrecedenceMap().entrySet()) {

			String currentValue = entry.getValue();
			if (roles.contains(currentValue))
				return currentValue;
		}
		return null;
	}

	/**
	 * Returns the roles that need to receive notification at this status and action.
	 * 
	 * @param status
	 * @param action
	 * @return Set
	 */
	public Set<String> getReceptorsOfNotification(String status, String action){
		Set<String> setOfRoles = new HashSet<>();
		setOfRoles.addAll(WorkFlowConfigs.getMapOfStatusAndReceptors().get(status));
		if(!StringUtils.isEmpty(action) && (action.equals(WorkFlowConfigs.ACTION_REASSIGN) || action.equals(WorkFlowConfigs.ACTION_REOPEN))) {
			setOfRoles.clear();
			setOfRoles.addAll(WorkFlowConfigs.getMapOfActionAndReceptors().get(action));
		}		
		return setOfRoles;
	}
	/**
	 * Splits any camelCase to human readable string
	 * @param String
	 * @return String
	 */
	public static String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}
	
	public Long convertToMilliSec(Integer hours) {
		Long milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(hours));
		log.info("SLA in ms: "+milliseconds);
		return milliseconds;
	}

}
