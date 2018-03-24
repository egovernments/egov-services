package org.egov.pgr.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.contract.ActionHistory;
import org.egov.pgr.contract.ActionInfo;
import org.egov.pgr.contract.AuditDetails;
import org.egov.pgr.contract.IdResponse;
import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.Service;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceRequest;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.producer.PGRProducer;
import org.egov.pgr.repository.IdGenRepo;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.ResponseInfoFactory;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class GrievanceService {

	@Value("${kafka.topics.save.service}")
	private String saveTopic;

	@Value("${kafka.topics.update.service}")
	private String updateTopic;

	@Value("${kafka.topics.notification.complaint}")
	private String complaintTopic;

	@Value("${indexer.grievance.create}")
	private String indexerCreateTopic;

	@Value("${indexer.grievance.update}")
	private String indexerUpdateTopic;

	@Autowired
	private ResponseInfoFactory factory;

	@Autowired
	private IdGenRepo idGenRepo;

	@Autowired
	private PGRUtils pGRUtils;

	@Autowired
	private PGRProducer pGRProducer;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	private static final String MODULE_NAME = "PGR:";

	/***
	 * Asynchronous method performs business logic if any and adds the data to
	 * persister queue on create topic
	 * 
	 * @param request
	 */
	public ServiceResponse create(ServiceRequest request) {

		log.debug(" the incoming request obj in service : {}", request);

		RequestInfo requestInfo = request.getRequestInfo();
		List<Service> serviceReqs = request.getServices();
		List<ActionInfo> actionInfos = request.getActionInfo();
		String tenantId = serviceReqs.get(0).getTenantId();
		Integer servReqLen = serviceReqs.size();

		AuditDetails auditDetails = pGRUtils.getAuditDetails(String.valueOf(requestInfo.getUserInfo().getId()));

		String by = auditDetails.getCreatedBy() + ":" + requestInfo.getUserInfo().getRoles().get(0).getName();

		List<String> servReqIdList = getIdList(requestInfo, tenantId, servReqLen, PGRConstants.SERV_REQ_ID_NAME,
				PGRConstants.SERV_REQ_ID_FORMAT);

		for (int servReqCount = 0; servReqCount < servReqLen; servReqCount++) {

			Service servReq = serviceReqs.get(servReqCount);
			ActionInfo actionInfo = actionInfos.get(servReqCount);
			String currentId = servReqIdList.get(servReqCount);
			servReq.setAuditDetails(auditDetails);
			servReq.setServiceRequestId(currentId);
			actionInfo.setBusinessKey(currentId);
			actionInfo.setBy(by);
			actionInfo.setWhen(auditDetails.getCreatedTime());
			actionInfo.setTenantId(tenantId);
			actionInfo.setStatus(actionInfo.getAction());
			// FIXME TODO should be module name and currentid in future
		}

		pGRProducer.push(saveTopic, request);
		pGRProducer.push(complaintTopic, request);
		pGRProducer.push(indexerCreateTopic, request);

		return getServiceResponse(request);
	}

	/**
	 * Asynchronous method performs business logic if any and adds the data to
	 * persister queue on update topic
	 * 
	 * @param request
	 */
	public ServiceResponse update(ServiceRequest request) {

		log.debug(" the incoming request obj in service : {}", request);

		RequestInfo requestInfo = request.getRequestInfo();
		List<Service> serviceReqs = request.getServices();
		List<ActionInfo> actionInfos = request.getActionInfo();
		String tenantId = serviceReqs.get(0).getTenantId();

		final AuditDetails auditDetails = pGRUtils
				.getAuditDetails(String.valueOf(request.getRequestInfo().getUserInfo().getId()));
		String by = auditDetails.getCreatedBy() + ":" + requestInfo.getUserInfo().getRoles().get(0).getName();

		int serviceLen = serviceReqs.size();
		for (int index = 0; index < serviceLen; index++) {

			Service servReq = serviceReqs.get(index);
			ActionInfo actionInfo = actionInfos.get(index);
			servReq.setAuditDetails(auditDetails);
			actionInfo.setBusinessKey(servReq.getServiceRequestId());
			actionInfo.setBy(by);
			actionInfo.setWhen(auditDetails.getCreatedTime());
			actionInfo.setTenantId(tenantId);
			actionInfo.setStatus(actionInfo.getAction());
		}

		pGRProducer.push(updateTopic, request);
		pGRProducer.push(complaintTopic, request);
		pGRProducer.push(indexerUpdateTopic, request);

		return getServiceResponse(request);
	}

       /**
		 * method to parse the IdGenResponse from IdgenRepo to List of String ids
		 * required by the respective methods
		 * 
		 * @param requestInfo
		 * @param tenantId
		 * @param length
		 * @param idKey
		 * @param idformat
		 * 
		 */
		private List<String> getIdList(RequestInfo requestInfo, String tenantId, Integer length, String idKey,
				String idformat) {

			return idGenRepo.getId(requestInfo, tenantId, length, idKey, idformat).getIdResponses().stream()
					.map(IdResponse::getId).collect(Collectors.toList());
		}

		/**
		 * returns ServiceResponse built based on the given ServiceRequest
		 * 
		 * @param serviceReqRequest
		 * @return serviceReqResponse
		 */
		public ServiceResponse getServiceResponse(ServiceRequest serviceReqRequest) {

			return ServiceResponse.builder()
					.responseInfo(factory.createResponseInfoFromRequestInfo(serviceReqRequest.getRequestInfo(), true))
					.services(serviceReqRequest.getServices()).build();
		}
		
		/**
		 * Method to return service requests received from the repo to the controller in
		 * the reqd format
		 * 
		 * @param requestInfo
		 * @param serviceReqSearchCriteria
		 * @return ServiceReqResponse
		 * @author vishal
		 */
		public Object getServiceRequests(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
			ObjectMapper mapper = pGRUtils.getObjectMapper();
			StringBuilder uri = new StringBuilder();
			SearcherRequest searcherRequest = null;
			enrichRequest(requestInfo, serviceReqSearchCriteria);
			log.info("Enriched request: "+serviceReqSearchCriteria);
			if(null != serviceReqSearchCriteria.getServiceRequestId() && serviceReqSearchCriteria.getServiceRequestId().size() == 1) {
				return getServiceRequestWithDetails(requestInfo, serviceReqSearchCriteria);
			}
			searcherRequest = prepareSearcherRequest(requestInfo, serviceReqSearchCriteria, uri);
			if(null == searcherRequest)
				return pGRUtils.getDefaultServiceResponse(requestInfo);
			Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
			log.info("Searcher response: "+response);
			if (null == response) 
				return pGRUtils.getDefaultServiceResponse(requestInfo);
			ServiceResponse serviceResponse = mapper.convertValue(response, ServiceResponse.class);
			return serviceResponse;
		}
		
		
		/**
		 * method to fetch service codes from mdms based on dept
		 * 
		 * @param requestInfo
		 * @param tenantId
		 * @param department
		 * @return Object
		 * @author vishal
		 */
		public Object fetchServiceCodes(RequestInfo requestInfo,
				String tenantId, String department) {
			StringBuilder uri = new StringBuilder();
			MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareSearchRequestForServiceCodes(uri, tenantId, department, requestInfo);
			return serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			
		}
		
		/**
		 * Prepares request for searcher service based on the criteria
		 * 
		 * @param requestInfo
		 * @param serviceReqSearchCriteria
		 * @param uri
		 * @return SearcherRequest
		 */
		public SearcherRequest prepareSearcherRequest(RequestInfo requestInfo,
				ServiceReqSearchCriteria serviceReqSearchCriteria, StringBuilder uri) {
			SearcherRequest searcherRequest = null;
            if(null != serviceReqSearchCriteria.getAssignedTo() && !serviceReqSearchCriteria.getAssignedTo().isEmpty()) {
				searcherRequest = pGRUtils.prepareSearchRequestAssignedTo(uri, serviceReqSearchCriteria, requestInfo);
			}else {
				if(null != serviceReqSearchCriteria.getGroup() && !serviceReqSearchCriteria.getGroup().isEmpty()){
						Object response = fetchServiceCodes(requestInfo, serviceReqSearchCriteria.getTenantId(), serviceReqSearchCriteria.getGroup());
						List<String> serviceCodes = null;
						if(null == response) {
							log.info("Searcher returned zero serviceCodes!");
							return null;
						}
						try {
							serviceCodes = (List<String>) JsonPath.read(response, PGRConstants.JSONPATH_SERVICE_CODES);
						}catch(Exception e) {
							log.error("Exception while parsing serviceCodes: ",e);
							return null;
						}
						serviceReqSearchCriteria.setServiceCodes(serviceCodes);
				}
				searcherRequest = pGRUtils.prepareSearchRequest(uri, serviceReqSearchCriteria, requestInfo);
			}
			
			return searcherRequest;
		}
		
		/**
		 * Prepares search request when the search is on only one service request id.
		 * 
		 * @param requestInfo
		 * @param serviceReqSearchCriteria
		 * @return
		 */
		public ServiceResponse getServiceRequestWithDetails(RequestInfo requestInfo,
				ServiceReqSearchCriteria serviceReqSearchCriteria) {
			
			ObjectMapper mapper = pGRUtils.getObjectMapper();
			StringBuilder uri = new StringBuilder();
			SearcherRequest searcherRequest = null;
			searcherRequest = pGRUtils.prepareSearchRequest(uri, serviceReqSearchCriteria, requestInfo);
			if(null == searcherRequest)
				return pGRUtils.getDefaultServiceResponse(requestInfo);
			Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
			if(null == response)
				return pGRUtils.getDefaultServiceResponse(requestInfo);
			log.info("Service: "+response);
			
			StringBuilder url = new StringBuilder();
			searcherRequest = pGRUtils.prepareActionSearchRequest(url, serviceReqSearchCriteria, requestInfo);
			List<ActionInfo> actions = null;
			if(null != searcherRequest) {
				Object res = serviceRequestRepository.fetchResult(url, searcherRequest);
				log.info("Actions: "+res);
				if(null != res) {
					actions = (List<ActionInfo>) JsonPath.read(res, PGRConstants.V3_ACTION_JSONPATH);
				}
			}
			ActionHistory actionHistory = ActionHistory.builder().actions(actions).build();
			List<ActionHistory> actionHistories = new ArrayList<>();
			actionHistories.add(actionHistory);
			ServiceResponse serviceResponse = mapper.convertValue(response, ServiceResponse.class);
			serviceResponse.setActionHistory(actionHistories);
			return serviceResponse;
			
		}
		
		public void enrichRequest(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
			log.info("Enriching request.........: "+serviceReqSearchCriteria);
			if(requestInfo.getUserInfo().getRoles().get(0).getName().equals("DGRO") && requestInfo.getUserInfo().getRoles().size() == 1) {
				StringBuilder uri = new StringBuilder();
				RequestInfoWrapper requestInfoWrapper = pGRUtils.prepareRequestForEmployeeSearch(uri, requestInfo, serviceReqSearchCriteria);
				Object response = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
				if(null == response) {
					throw new CustomException("401", "Unauthorized Employee");
				}
				log.info("Employee: "+response);
				Long departmenCode = null;
				try {
					departmenCode = JsonPath.read(response, PGRConstants.V3_EMPLOYEE_DEPTCODE_JSONPATH);
				}catch(Exception e) {
					log.error("Exception: "+e);
					throw new CustomException("401", "Unauthorized Employee");
				}
				
				StringBuilder deptUri = new StringBuilder();
				requestInfoWrapper = pGRUtils.prepareRequestForDeptSearch(deptUri, requestInfo, departmenCode);
				response = serviceRequestRepository.fetchResult(deptUri, requestInfoWrapper);
				if(null == response) {
					throw new CustomException("401", "Invalid department");
				}
				log.info("Department: "+response);
				String department = JsonPath.read(response, PGRConstants.V3_DEPARTMENTNAME_EMPLOYEE_JSONPATH);
				serviceReqSearchCriteria.setGroup(department);
			}else if(requestInfo.getUserInfo().getRoles().get(0).getName().equals("CITIZEN") && requestInfo.getUserInfo().getRoles().size() == 1) {
				serviceReqSearchCriteria.setAccountId(requestInfo.getUserInfo().getId().toString());
			}			
		}
		
		
/*		*//**
		 * Fetches count of service requests and returns in the reqd format.
		 * 
		 * @param requestInfo
		 * @param serviceReqSearchCriteria
		 * @return Object
		 * @author vishal
		 *//*
		public Object getCount(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
			ObjectMapper mapper = pGRUtils.getObjectMapper();
			StringBuilder uri = new StringBuilder();
			SearcherRequest searcherRequest = null;
		    if(null != serviceReqSearchCriteria.getAssignedTo() && !serviceReqSearchCriteria.getAssignedTo().isEmpty()) {
				searcherRequest = pGRUtils.prepareCountRequestAssignedTo(uri, serviceReqSearchCriteria, requestInfo);
			}else {
				searcherRequest = pGRUtils.prepareCountRequestGeneral(uri, serviceReqSearchCriteria, requestInfo);
			}
			Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);		
			log.info("Searcher response: ", response);
			if (null == response) {
				return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false), 0D);
			}
			Double count = JsonPath.read(response, PGRConstants.PG_JSONPATH_COUNT);
			return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false), count);
		}*/
		
}
