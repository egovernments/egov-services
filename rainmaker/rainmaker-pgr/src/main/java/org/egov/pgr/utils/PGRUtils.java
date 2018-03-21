package org.egov.pgr.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.pgr.contract.AuditDetails;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.v3.contract.ActionHistory;
import org.egov.pgr.v3.contract.ActionInfo;
import org.egov.pgr.v3.contract.Service;
import org.egov.pgr.v3.contract.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

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
	
	@Autowired
	private ResponseInfoFactory factory;

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
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.SEARCHER_SRSEARCH_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
	/**
	 * Prepares request and uri for count search 
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
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.SEARCHER_COUNT_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
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
	
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareHistoryRequest(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.SEARCHER_SRHISTORY_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
	
	
	
/*................................V3 Utils.........................................................................*/	
	
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareSearchRequestSpecific(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V3_SEARCHER_SRSEARCH_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
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
	public SearcherRequest prepareActionSearchRequest(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V3_SEARCHER_ACTIONSEARCH_GENERAL_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
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
	public SearcherRequest prepareSearchRequestAssignedTo(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V3_SEARCHER_SRSEARCH_ASSIGNEDTO_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
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
	public SearcherRequest prepareCountRequestGeneral(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V2_SEARCHER_COUNT_GENERAL_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
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
	public SearcherRequest prepareCountRequestAssignedTo(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V3_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V2_SEARCHER_COUNT_ASSIGNED_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
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
	 * Util method to check if the media,comment,assignee,statuses are present in the searcher response
	 * based on this, the formatting logic is applied. Searcher response is such that, either all of them or none of them is present
	 * so the check in this method is applied on only media.
	 * 
	 * @param mapper
	 * @param response
	 * @return
	 */
	public boolean isSecondaryInfoAvailable(ObjectMapper mapper, Object response) {
		boolean isSecondaryInfoAvailable = true;
		try {
			JsonPath.read(mapper.writeValueAsString(response), PGRConstants.V2_MEDIA_JSONPATH);
		}catch(Exception e) {
			isSecondaryInfoAvailable = false;
		}
		return isSecondaryInfoAvailable;
	}
	
	
	
	
	
	
	
/*................................V2 Utils.........................................................................*/
	
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 *//*
	public SearcherRequest prepareSearchRequestSpecific(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V2_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V2_SEARCHER_SRSEARCH_SPECIFIC_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
	*//**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 *//*
	public SearcherRequest prepareSearchRequestGeneral(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V2_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V2_SEARCHER_SRSEARCH_GENERAL_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
	*//**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 *//*
	public SearcherRequest prepareSearchRequestAssignedTo(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V2_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V2_SEARCHER_SRSEARCH_ASSIGNEDTO_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
	*//**
	 * Prepares request and uri for count search for general criteria
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 *//*
	public SearcherRequest prepareCountRequestGeneral(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V2_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V2_SEARCHER_COUNT_GENERAL_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
	*//**
	 * Prepares request and uri for count search on assined to
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 *//*
	public SearcherRequest prepareCountRequestAssignedTo(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.V2_SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.V2_SEARCHER_COUNT_ASSIGNED_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
	*//**
	 * Formats the external service response to ServiceResponse
	 * 
	 * @param response
	 * @param requestInfo
	 * @return ServiceResponse
	 *//*
	public ServiceResponse getServiceResponse(Object response, RequestInfo requestInfo) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);        
		try {
			ActionHistory actionHistory = null;
			Service service = null;
			List<Service> services = new ArrayList<>();
			if(isSecondaryInfoAvailable(mapper, response)) {
				actionHistory = ActionHistory.builder()
						.media(JsonPath.read(mapper.writeValueAsString(response), PGRConstants.V2_MEDIA_JSONPATH))
						.comments(JsonPath.read(mapper.writeValueAsString(response), PGRConstants.V2_COMMENT_JSONPATH))
						.assignees(JsonPath.read(mapper.writeValueAsString(response), PGRConstants.V2_ASSIGNEE_JSONPATH))
						.statuses(JsonPath.read(mapper.writeValueAsString(response), PGRConstants.V2_STATUS_JSONPATH)).build();
				
				service = mapper.convertValue(JsonPath.read(mapper.writeValueAsString(response), PGRConstants.V2_SERVICES_JSONPATH), Service.class);
				services.add(service);
			}else {
				actionHistory = new ActionHistory();
				services = (List<Service>) JsonPath.read(mapper.writeValueAsString(response), PGRConstants.V2_SERVICES_PARENT_JSONPATH);
			}
			List<ActionHistory> actionHistories = new ArrayList<>();
			actionHistories.add(actionHistory);
			
			ServiceResponse serviceResponse = ServiceResponse.builder()
					.responseInfo(factory.createResponseInfoFromRequestInfo(requestInfo, true))
					.services(services).actionHistory(actionHistories).build();
			
			log.info("Result: "+serviceResponse);
			
			return serviceResponse;

		}catch(Exception e) {
			log.error("Exception while formatting response: ",e);
		}
		
		return new ServiceResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
				new ArrayList<Service>(), new ArrayList<ActionHistory>());
		
	}
	
	*//**
	 * Default response is responseInfo with error status and empty lists
	 * 
	 * @param requestInfo
	 * @return ServiceResponse
	 *//*
	public ServiceResponse getDefaultServiceResponse(RequestInfo requestInfo) {
		return new ServiceResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
				new ArrayList<Service>(), new ArrayList<ActionHistory>());
	}
	
	*//**
	 * Util method to check if the media,comment,assignee,statuses are present in the searcher response
	 * based on this, the formatting logic is applied. Searcher response is such that, either all of them or none of them is present
	 * so the check in this method is applied on only media.
	 * 
	 * @param mapper
	 * @param response
	 * @return
	 *//*
	public boolean isSecondaryInfoAvailable(ObjectMapper mapper, Object response) {
		boolean isSecondaryInfoAvailable = true;
		try {
			JsonPath.read(mapper.writeValueAsString(response), PGRConstants.V2_MEDIA_JSONPATH);
		}catch(Exception e) {
			isSecondaryInfoAvailable = false;
		}
		return isSecondaryInfoAvailable;
	}
*/
}
