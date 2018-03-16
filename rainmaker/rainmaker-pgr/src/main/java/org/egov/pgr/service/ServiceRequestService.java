package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.contract.AuditDetails;
import org.egov.pgr.contract.Comment;
import org.egov.pgr.contract.CountResponse;
import org.egov.pgr.contract.IdResponse;
import org.egov.pgr.contract.Media;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.ServiceReq;
import org.egov.pgr.contract.ServiceReqRequest;
import org.egov.pgr.contract.ServiceReqResponse;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.producer.PGRProducer;
import org.egov.pgr.repository.IdGenRepo;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceRequestService {

	@Value("${kafka.topics.save.servicereq}")
	private String saveTopic;

	@Value("${kafka.topics.update.servicereq}")
	private String updateTopic;
	
	@Value("${kafka.topics.notification.complaint}")
	private String complaintTopic;

	@Autowired
	private ResponseInfoFactory factory;

	@Autowired
	private IdGenRepo idGenRepo;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
		
	@Autowired
	private PGRUtils pGRUtils;
	
	@Autowired
	private PGRProducer pGRProducer;

	/***
	 * Asynchronous method performs business logic if any and adds the data to
	 * persister queue on create topic
	 * 
	 * @param request
	 */
	public ServiceReqResponse create(ServiceReqRequest request) {

		log.debug(" the incoming request obj in service : {}", request);

		RequestInfo requestInfo = request.getRequestInfo();
		List<ServiceReq> serviceReqs = request.getServiceReq();
		String tenantId = serviceReqs.get(0).getTenantId();
		Integer servReqLen = serviceReqs.size();
		int servReqCount = 0;

		List<String> servReqIdList = getIdList(requestInfo, tenantId, servReqLen, PGRConstants.SERV_REQ_ID_NAME,
				PGRConstants.SERV_REQ_ID_FORMAT);
		AuditDetails auditDetails = pGRUtils.getAuditDetails(requestInfo.getUserInfo().getId()+"");
		for (ServiceReq servReq : serviceReqs) {
			servReq.setAuditDetails(auditDetails);
			servReq.setServiceRequestId(servReqIdList.get(servReqCount++));
			setIdsForSubList(servReq.getMedia(), servReq.getComments(), true, requestInfo);
		}
		pGRProducer.push(saveTopic, request);
		pGRProducer.push(complaintTopic, request);
		return getServiceReqResponse(request);
	}

	/***
	 * Asynchronous method performs business logic if any and adds the data to
	 * persister queue on update topic
	 * 
	 * @param request
	 */
	public ServiceReqResponse update(ServiceReqRequest request) {

		log.debug(" the incoming request obj in service : {}", request);

		List<ServiceReq> serviceReqs = request.getServiceReq();

		AuditDetails auditDetails = pGRUtils.getAuditDetails(request.getRequestInfo().getUserInfo().getId()+"");
		for (ServiceReq servReq : serviceReqs) {
			servReq.setAuditDetails(auditDetails);
			setIdsForSubList(servReq.getMedia(), servReq.getComments(), false, request.getRequestInfo());
		}

		pGRProducer.push(updateTopic, request);
		pGRProducer.push(complaintTopic, request);
		return getServiceReqResponse(request);
	}

	/**
	 * to filter the sublist object for idgeneration if they are null
	 * 
	 * @param mediaList
	 * @param commentsList
	 */
	private void setIdsForSubList(List<Media> mediaList, List<Comment> commentsList, Boolean isCreate, RequestInfo requestInfo) {

		User user = requestInfo.getUserInfo();
		Role role = user.getRoles().get(0);

		String by = user.getId() + "-" + role.getName();

		if (null != mediaList)
			mediaList.forEach(media -> {
				if (null == media.getId() || isCreate) {
					media.setId(UUID.randomUUID().toString());
					media.setBy(by);
				}
			});

		if (null != commentsList)
			commentsList.forEach(comment -> {
				if (null == comment.getId() || isCreate) {
					comment.setId(UUID.randomUUID().toString());
					comment.setBy(by);
				}
			});
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
	 * returns ServiceReqResponse built based on the given ServiceReqRequest
	 * 
	 * @param serviceReqRequest
	 * @return serviceReqResponse
	 */
	public ServiceReqResponse getServiceReqResponse(ServiceReqRequest serviceReqRequest) {

		return ServiceReqResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(serviceReqRequest.getRequestInfo(), true))
				.serviceReq(serviceReqRequest.getServiceReq()).build();
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
	public ServiceReqResponse getServiceRequests(RequestInfo requestInfo,
			ServiceReqSearchCriteria serviceReqSearchCriteria) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		ServiceReqResponse serviceReqResponse = null;
		if(null != serviceReqSearchCriteria.getGroup() && !serviceReqSearchCriteria.getGroup().isEmpty()){
				Object response = fetchServiceCodes(requestInfo, serviceReqSearchCriteria.getTenantId(), serviceReqSearchCriteria.getGroup());
				List<String> serviceCodes = null;
				if(null == response)
					return new ServiceReqResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
							new ArrayList<ServiceReq>());
				try {
					serviceCodes = (List<String>) JsonPath.read(response, PGRConstants.JSONPATH_SERVICE_CODES);
				}catch(Exception e) {
					return new ServiceReqResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
							new ArrayList<ServiceReq>());
				}
				serviceReqSearchCriteria.setServiceCodes(serviceCodes);
		}
		StringBuilder uri = new StringBuilder();
		SearcherRequest searcherRequest = pGRUtils.prepareSearchRequest(uri, serviceReqSearchCriteria, requestInfo);
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
		log.info("Searcher response: ", response);
		if (null == response) {
			return new ServiceReqResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
					new ArrayList<ServiceReq>());
		}
		serviceReqResponse = mapper.convertValue(response, ServiceReqResponse.class);
		return serviceReqResponse;
	}

	/**
	 * Fetches count of service requests and returns in the reqd format.
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return Object
	 * @author vishal
	 */
	public Object getCount(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		StringBuilder uri = new StringBuilder();
		SearcherRequest searcherRequest = pGRUtils.prepareCountRequest(uri, serviceReqSearchCriteria, requestInfo);
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);		
		log.info("Searcher response: ", response);
		if (null == response) {
			return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false), 0D);
		}
		Double count = JsonPath.read(response, PGRConstants.PG_JSONPATH_COUNT);
		return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false), count);
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
	
	/*	*//**
	 * method to set Id generated by system using respective sequences to the media
	 * and comment list
	 * 
	 * @param requestInfo
	 * @param mediaList
	 * @param commentList
	 * @param tenantId
	 *//*
	private void setIdsForSubLists(RequestInfo requestInfo, List<Media> mediaList, List<Comment> commentList,
			String tenantId) {

		int mediaLen = mediaList.size();
		int commentLen = commentList.size();

		int count = 0;
		if (mediaLen > 0) {
			List<String> mediaIdList = getIdList(requestInfo, tenantId, mediaLen, PGRConstants.MEDIA_ID_NAME,
					PGRConstants.MEDIA_ID_FORMAT);
			for (Media media : mediaList)
				media.setId(mediaIdList.get(count++));
		}

		count = 0;
		if (commentLen > 0) {
			List<String> commentIdList = getIdList(requestInfo, tenantId, commentLen, PGRConstants.COMMENT_ID_NAME,
					PGRConstants.COMMENT_ID_FORMAT);
			for (Comment comment : commentList)
				comment.setId(commentIdList.get(count++));
		}
	}
*/
	
	/**
	 * Method to return history of service request received from the repo to the controller in
	 * the reqd format
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return ServiceReqResponse
	 * @author vishal
	 */
	public ServiceReqResponse getHistory(RequestInfo requestInfo,
			ServiceReqSearchCriteria serviceReqSearchCriteria) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		ServiceReqResponse serviceReqResponse = null;
		StringBuilder uri = new StringBuilder();
		SearcherRequest searcherRequest = pGRUtils.prepareHistoryRequest(uri, serviceReqSearchCriteria, requestInfo);
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
		log.info("Searcher response: ", response);
		if (null == response) {
			return new ServiceReqResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
					new ArrayList<ServiceReq>());
		}
		serviceReqResponse = mapper.convertValue(response, ServiceReqResponse.class);
		return serviceReqResponse;
	}

}
