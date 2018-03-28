package org.egov.pgr.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.pgr.contract.ActionHistory;
import org.egov.pgr.contract.AuditDetails;
import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.Service;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceResponse;
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
	
	@Value("${egov.hr.employee.search.endpoint}")
	private String hrEmployeeSearchEndpoint;
	
	@Value("${egov.common.masters.host}")
	private String commonMasterHost;
	
	@Value("${egov.common.masters.search.endpoint}")
	private String commonMasterSearchEndpoint;
	
	@Autowired
	private ResponseInfoFactory factory;

	
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
	public MdmsCriteriaReq prepareSearchRequestForServiceCodes(StringBuilder uri, String tenantId, 
			String department, RequestInfo requestInfo) {
		
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME).
				filter("[?(@.group=='"+department+"')]."+PGRConstants.SERVICE_CODES).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder()
				.moduleName(PGRConstants.MDMS_PGR_MOD_NAME).masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
		
		return mdmsCriteriaReq;
	}
	
	
	public AuditDetails getAuditDetails(String by) {
		
		Long date = new Date().getTime();
		return AuditDetails.builder().createdBy(by).createdTime(date).lastModifiedBy(by).lastModifiedTime(date).build();
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
	public MdmsCriteriaReq prepareSearchRequestForServiceType(StringBuilder uri, String tenantId, String serviceCode, RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME).
				filter("[?(@.serviceCode=='"+serviceCode+"')]."+PGRConstants.SERVICE_NAME).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder()
				.moduleName(PGRConstants.MDMS_PGR_MOD_NAME).masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
		
		return mdmsCriteriaReq;
	}
	
	
/*................................V3 Utils.........................................................................*/	
	
	private static final String MODULE_NAME = "{moduleName}";
	
	private static final String SEARCH_NAME = "{searchName}";
	
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareSearchRequest(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria,
			RequestInfo requestInfo) {
		
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace(SEARCH_NAME, PGRConstants.V3_SEARCHER_SRSEARCH_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}
	
	
	
	
	
	
/*................................................V5..............................................................*/	
	
	
	
	
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareSearchRequestWithDetails(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria,
			RequestInfo requestInfo) {
		
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.V2_SEARCHER_PGR_MOD_NAME)
				.replace(SEARCH_NAME, PGRConstants.V2_SEARCHER_DEF_NAME);
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
	public SearcherRequest prepareSearchRequestForAssignedTo(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria,
			RequestInfo requestInfo) {
		
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.V2_SEARCHER_PGR_MOD_NAME)
				.replace(SEARCH_NAME, PGRConstants.V2_SEARCHER_SRID_ASSIGNEDTO_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}
	
	
	public RequestInfoWrapper prepareRequestForEmployeeSearch(StringBuilder uri, RequestInfo requestInfo,
			ServiceReqSearchCriteria serviceReqSearchCriteria) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(hrEmployeeHost).append(hrEmployeeSearchEndpoint)
		.append("?id="+requestInfo.getUserInfo().getId()).append("&tenantId="+serviceReqSearchCriteria.getTenantId());

		return requestInfoWrapper;
	}
	
	public RequestInfoWrapper prepareRequestForDeptSearch(StringBuilder uri, RequestInfo requestInfo, long deptId, String tenantId) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(commonMasterHost).append(commonMasterSearchEndpoint).append("?id="+deptId).append("&tenantId="+tenantId);

		return requestInfoWrapper;
	}
	
	
	
	
	
	
/*................................................V5..............................................................*/	

	
	
	
	
	
	
	
	
	
	
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareActionSearchRequest(StringBuilder uri,
			ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace(SEARCH_NAME, PGRConstants.V3_SEARCHER_ACTIONSEARCH_GENERAL_DEF_NAME);
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
	public SearcherRequest prepareSearchRequestAssignedTo(StringBuilder uri,
			ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace(SEARCH_NAME, PGRConstants.V3_SEARCHER_SRSEARCH_ASSIGNEDTO_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}

	/**
	 * Prepares request and uri for count search for general criteria
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareCountRequest(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria,
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace(SEARCH_NAME, PGRConstants.V3_SEARCHER_COUNT_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
	}

	/**
	 * Prepares request and uri for count search on assined to
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareCountRequestAssignedTo(StringBuilder uri,
			ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace(MODULE_NAME, PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace(SEARCH_NAME, PGRConstants.V3_SEARCHER_COUNT_ASSIGNED_DEF_NAME);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
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
	
	

}
