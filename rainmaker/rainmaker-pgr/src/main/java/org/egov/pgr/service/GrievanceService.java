package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.PGRApp;
import org.egov.pgr.contract.ActionHistory;
import org.egov.pgr.contract.ActionInfo;
import org.egov.pgr.contract.AuditDetails;
import org.egov.pgr.contract.CountResponse;
import org.egov.pgr.contract.IdResponse;
import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.Service;
import org.egov.pgr.contract.Service.StatusEnum;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceRequest;
import org.egov.pgr.contract.ServiceRequestDetails;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.producer.PGRProducer;
import org.egov.pgr.repository.FileStoreRepo;
import org.egov.pgr.repository.IdGenRepo;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.ResponseInfoFactory;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

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
	private FileStoreRepo fileStoreRepo;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	private static final String UPDATE_ERROR_KEY = "EG_PGR_INVALID_ACTION_UPDATE"; 

	private static final String MODULE_NAME = "PGR:";

	/***
	 * Asynchronous method performs business logic if any and adds the data to
	 * persister queue on create topic
	 * 
	 * @param request
	 */
	public ServiceResponse create(ServiceRequest request) {

		Map<String, String> actionStatusMap = PGRApp.getActionStatusMap();

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
			if (null == actionInfo)
				actionInfo = new ActionInfo();
			String currentId = servReqIdList.get(servReqCount);
			servReq.setAuditDetails(auditDetails);
			servReq.setServiceRequestId(currentId);
			servReq.setStatus(StatusEnum.OPEN);
			// FIXME TODO business key should be module name and currentid in future
			actionInfo.setBusinessKey(currentId);
			actionInfo.setBy(by);
			actionInfo.setWhen(auditDetails.getCreatedTime());
			actionInfo.setTenantId(tenantId);
			actionInfo.setStatus(actionStatusMap.get("open"));
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

		Map<String, List<String>> actioncurrentStatusMap = PGRApp.getActionCurrentStatusMap();
		Map<String, String> actionStatusMap = PGRApp.getActionStatusMap();
		Map<String, List<String>> errorMap = new HashMap<>();

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
			ActionInfo actionInfo = null;
			if (!CollectionUtils.isEmpty(actionInfos))
				actionInfo = actionInfos.get(index);
			servReq.setAuditDetails(auditDetails);
			System.err.println(" teh action info : "+ actionInfo);
			// FIXME TODO business key should be module name and currentid in future
			if (null != actionInfo) {
				actionInfo.setBusinessKey(servReq.getServiceRequestId());
				actionInfo.setBy(by);
				actionInfo.setWhen(auditDetails.getCreatedTime());
				actionInfo.setTenantId(tenantId);
				actionInfo.setStatus(actionInfo.getAction());
				if (null != actionInfo.getAction()
						&& isUpdateValid(requestInfo, actionInfo, actioncurrentStatusMap.get(actionInfo.getAction())))
					actionInfo.setStatus(actionStatusMap.get(actionInfo.getAction()));
				else {

					String errorMsg = " The Given Action " + actionInfo.getAction()
							+ "cannot be applied for the Current status of the Grievance with ServiceRequestId "
							+ servReq.getServiceRequestId();
					List<String> errors = errorMap.get(UPDATE_ERROR_KEY);
					if (null == errors) {
						errors = Arrays.asList(errorMsg);
						errorMap.put(UPDATE_ERROR_KEY, errors);
					} else
						errors.add(errorMsg);
				}

			}
		}
		if(!errorMap.isEmpty()) {
			Map<String, String> newMap = new HashMap<>();
			newMap.put(UPDATE_ERROR_KEY, errorMap.get(UPDATE_ERROR_KEY).toString());
			throw new CustomException(newMap);
		}

		pGRProducer.push(updateTopic, request);
		pGRProducer.push(complaintTopic, request);
		pGRProducer.push(indexerUpdateTopic, request);

		return getServiceResponse(request);
	}

	private boolean isUpdateValid(RequestInfo requestInfo, ActionInfo actionInfo, List<String> currentStatusList) {

		System.err.println(" the current list possible : "+ currentStatusList);
		ServiceReqSearchCriteria serviceReqSearchCriteria = ServiceReqSearchCriteria.builder()
				.tenantId(actionInfo.getTenantId()).serviceRequestId(Arrays.asList(actionInfo.getBusinessKey()))
				.build();

		List<ActionInfo> infos = ((ServiceResponse) getServiceRequestDetails(requestInfo, serviceReqSearchCriteria))
				.getActionHistory().get(0).getActions();
		
		
		for (int i = infos.size() - 1; i >= 0; i--) {
				String status = infos.get(i).getStatus();
				System.err.println(" the status is : "+ status);
				if (null != status) {
					System.err.println(" is it true : "+currentStatusList.contains(status));
					if (currentStatusList.contains(status))
						return true;
					else
						return false;
				}
			}
		return false;
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
		log.info("Enriched request: " + serviceReqSearchCriteria);
		if (!CollectionUtils.isEmpty(serviceReqSearchCriteria.getServiceRequestId())
				&& serviceReqSearchCriteria.getServiceRequestId().size() == 1) {
			return getServiceRequestWithDetails(requestInfo, serviceReqSearchCriteria);
		}
		searcherRequest = prepareSearcherRequest(requestInfo, serviceReqSearchCriteria, uri);
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
		log.info("Searcher response: " + response);
		if (null == response)
			return pGRUtils.getDefaultServiceResponse(requestInfo);
		ServiceResponse serviceResponse = mapper.convertValue(response, ServiceResponse.class);
		return serviceResponse;
	}

	/* ...................................................V5-START................................................................*/

	/**
	 * Method to return service requests along with details acc to V5 design
	 * received from the repo to the controller in the reqd format
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return ServiceReqResponse
	 * @author vishal
	 */
	public Object getServiceRequestDetails(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
		StringBuilder uri = new StringBuilder();
		SearcherRequest searcherRequest = null;
		try {
			enrichRequest(requestInfo, serviceReqSearchCriteria);
		} catch (CustomException e) {
			if (e.getMessage().equals("No Data"))
				return pGRUtils.getDefaultServiceResponse(requestInfo);
			else
				throw e;
		}
		searcherRequest = pGRUtils.prepareSearchRequestWithDetails(uri, serviceReqSearchCriteria, requestInfo);
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
		log.info("Searcher response: " + response);
		if (null == response)
			return pGRUtils.getDefaultServiceResponse(requestInfo);
		return prepareResult(response, requestInfo);

	}

	/**
	 * Method to return service requests ids based on the assignedTo
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return List<String>
	 * @author vishal
	 */
	public List<String> getServiceRequestIdsOnAssignedTo(RequestInfo requestInfo,
			ServiceReqSearchCriteria serviceReqSearchCriteria) {
		StringBuilder uri = new StringBuilder();
		List<String> serviceRequestIds = new ArrayList<>();
		SearcherRequest searcherRequest = pGRUtils.prepareSearchRequestForAssignedTo(uri, serviceReqSearchCriteria,
				requestInfo);
		try {
			Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
			log.info("Searcher response: " + response);
			if (null == response)
				return serviceRequestIds;
			serviceRequestIds = (List<String>) JsonPath.read(response, PGRConstants.V2_SRID_ASSIGNEDTO_JSONPATH);
		} catch (Exception e) {
			log.error("Exception while parsing SRid search on AssignedTo result: " + e);
			return serviceRequestIds;
		}

		log.info("serviceRequestIds: " + serviceRequestIds);

		return serviceRequestIds;

	}

	public ServiceResponse prepareResult(Object response, RequestInfo requestInfo) {
		ObjectMapper mapper = pGRUtils.getObjectMapper();
		List<Service> services = new ArrayList<Service>();
		List<ActionHistory> actionHistory = new ArrayList<ActionHistory>();

		List<ServiceRequestDetails> result = new ArrayList<>();
		List<Object> list = (List<Object>) JsonPath.read(response, "$.services");
		log.info("Objects: " + list);
		for (Object entry : list) {
			ServiceRequestDetails object = mapper.convertValue(entry, ServiceRequestDetails.class);
			result.add(object);
			log.info("Object: " + object);
		}

		for (ServiceRequestDetails obj : result) {
			List<ActionInfo> action = obj.getActionhistory();
			ActionHistory actionHis = new ActionHistory();
			actionHis.setActions(action);
			actionHistory.add(actionHis);

			obj.setActionhistory(null);
			services.add(obj.getServices());

		}

		replaceIdsWithUrls(actionHistory);

		ServiceResponse serviceResponse = ServiceResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(requestInfo, true)).services(services)
				.actionHistory(actionHistory).build();

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
	public Object fetchServiceCodes(RequestInfo requestInfo, String tenantId, String department) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareSearchRequestForServiceCodes(uri, tenantId, department,
				requestInfo);
		Object response = null;
		try {
			response = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
		} catch (Exception e) {
			log.error("Exception while fetching serviceCodes: " + e);
		}
		return response;

	}

	public void enrichRequest(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
		log.info("Enriching request.........: " + serviceReqSearchCriteria);
		if (requestInfo.getUserInfo().getRoles().get(0).getName().equals("DGRO")
				&& requestInfo.getUserInfo().getRoles().size() == 1) {
			Integer departmenCode = getDepartmentCode(serviceReqSearchCriteria, requestInfo);
			String department = getDepartment(serviceReqSearchCriteria, requestInfo, departmenCode);
			Object response = fetchServiceCodes(requestInfo, serviceReqSearchCriteria.getTenantId(), department);
			if (null == response) {
				log.info("Searcher returned zero serviceCodes for dept: " + department);
				throw new CustomException("400", "No Data");
			}
			try {
				List<String> serviceCodes = (List<String>) JsonPath.read(response, PGRConstants.JSONPATH_SERVICE_CODES);
				serviceReqSearchCriteria.setServiceCodes(serviceCodes);
			} catch (Exception e) {
				log.error("Exception while parsing serviceCodes: ", e);
				throw new CustomException("400", "No Data");
			}
		} else if (requestInfo.getUserInfo().getRoles().get(0).getName().equals("CITIZEN")
				&& requestInfo.getUserInfo().getRoles().size() == 1) {
			serviceReqSearchCriteria.setAccountId(requestInfo.getUserInfo().getId().toString());
			String[] tenant = serviceReqSearchCriteria.getTenantId().split("[.]");
			if (tenant.length > 1)
				serviceReqSearchCriteria.setTenantId(tenant[0]);
		}
		if (requestInfo.getUserInfo().getRoles().get(0).getName().equals("EMPLOYEE")
				&& requestInfo.getUserInfo().getRoles().size() == 1) {
			serviceReqSearchCriteria.setAssignedTo(requestInfo.getUserInfo().getId().toString());
		}
		if (null != serviceReqSearchCriteria.getAssignedTo() && !serviceReqSearchCriteria.getAssignedTo().isEmpty()) {
			List<String> serviceRequestIds = getServiceRequestIdsOnAssignedTo(requestInfo, serviceReqSearchCriteria);
			if (serviceRequestIds.isEmpty())
				throw new CustomException("400", "No Data");
			serviceReqSearchCriteria.setServiceRequestId(serviceRequestIds);
		}
		
		log.info("Enriched request: " + serviceReqSearchCriteria);

	}

	public Integer getDepartmentCode(ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		RequestInfoWrapper requestInfoWrapper = pGRUtils.prepareRequestForEmployeeSearch(uri, requestInfo,
				serviceReqSearchCriteria);
		Object response = null;
		log.info("Employee: " + response);
		Integer departmenCode = null;
		try {
			response = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
			if (null == response) {
				throw new CustomException("401", "Unauthorized Employee for this tenant.");
			}
			log.info("Employee: " + response);
			departmenCode = JsonPath.read(response, PGRConstants.V3_EMPLOYEE_DEPTCODE_JSONPATH);
		} catch (Exception e) {
			log.error("Exception: " + e);
			throw new CustomException("401", "Unauthorized Employee for this tenant");
		}

		return departmenCode;
	}

	public String getDepartment(ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo,
			Integer departmentCode) {
		StringBuilder deptUri = new StringBuilder();
		String department = null;
		Object response = null;
		RequestInfoWrapper requestInfoWrapper = pGRUtils.prepareRequestForDeptSearch(deptUri, requestInfo,
				departmentCode, serviceReqSearchCriteria.getTenantId());
		try {
			response = serviceRequestRepository.fetchResult(deptUri, requestInfoWrapper);
			if (null == response) {
				throw new CustomException("401", "Invalid department for this tenant");
			}
			department = JsonPath.read(response, PGRConstants.V3_DEPARTMENTNAME_EMPLOYEE_JSONPATH);
		} catch (Exception e) {
			log.error("Exception: " + e);
			throw new CustomException("401", "Invalid department for this tenant");
		}

		return department;
	}

/*...................................................V5-END.....................................................*/


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
		if (null != serviceReqSearchCriteria.getAssignedTo() && !serviceReqSearchCriteria.getAssignedTo().isEmpty()) {
			searcherRequest = pGRUtils.prepareSearchRequestAssignedTo(uri, serviceReqSearchCriteria, requestInfo);
		} else {
			if (null != serviceReqSearchCriteria.getGroup() && !serviceReqSearchCriteria.getGroup().isEmpty()) {
				Object response = fetchServiceCodes(requestInfo, serviceReqSearchCriteria.getTenantId(),
						serviceReqSearchCriteria.getGroup());
				List<String> serviceCodes = null;
				if (null == response) {
					log.info("Searcher returned zero serviceCodes!");
					return null;
				}
				try {
					serviceCodes = (List<String>) JsonPath.read(response, PGRConstants.JSONPATH_SERVICE_CODES);
				} catch (Exception e) {
					log.error("Exception while parsing serviceCodes: ", e);
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
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
		if (null == response)
			return pGRUtils.getDefaultServiceResponse(requestInfo);
		log.info("Service: " + response);

		StringBuilder url = new StringBuilder();
		searcherRequest = pGRUtils.prepareActionSearchRequest(url, serviceReqSearchCriteria, requestInfo);
		List<ActionInfo> actions = null;
		Object res = serviceRequestRepository.fetchResult(url, searcherRequest);
		log.info("Actions: " + res);
		if (null != res) {
			actions = (List<ActionInfo>) JsonPath.read(res, PGRConstants.V3_ACTION_JSONPATH);
		}
		ActionHistory actionHistory = ActionHistory.builder().actions(actions).build();
		List<ActionHistory> actionHistories = new ArrayList<>();
		actionHistories.add(actionHistory);
		replaceIdsWithUrls(actionHistories);

		ServiceResponse serviceResponse = mapper.convertValue(response, ServiceResponse.class);
		serviceResponse.setActionHistory(actionHistories);
		return serviceResponse;

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
		StringBuilder uri = new StringBuilder();
		SearcherRequest searcherRequest = null;
		if (null != serviceReqSearchCriteria.getAssignedTo() && !serviceReqSearchCriteria.getAssignedTo().isEmpty()) {
			searcherRequest = pGRUtils.prepareCountRequestAssignedTo(uri, serviceReqSearchCriteria, requestInfo);
		} else {
			searcherRequest = prepareSearcherRequestForCount(requestInfo, serviceReqSearchCriteria, uri);
		}
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
		log.info("Searcher response: ", response);
		if (null == response) {
			return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false), 0D);
		}
		Double count = JsonPath.read(response, PGRConstants.PG_JSONPATH_COUNT);
		return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, true), count);
	}

	/**
	 * Prepares request for searcher service based on the criteria
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @param uri
	 * @return SearcherRequest
	 */
	public SearcherRequest prepareSearcherRequestForCount(RequestInfo requestInfo,
			ServiceReqSearchCriteria serviceReqSearchCriteria, StringBuilder uri) {
		SearcherRequest searcherRequest = null;
		if (null != serviceReqSearchCriteria.getGroup() && !serviceReqSearchCriteria.getGroup().isEmpty()) {
			Object response = fetchServiceCodes(requestInfo, serviceReqSearchCriteria.getTenantId(),
					serviceReqSearchCriteria.getGroup());
			List<String> serviceCodes = null;
			if (null == response) {
				log.info("Searcher returned zero serviceCodes!");
				return null;
			}
			try {
				serviceCodes = (List<String>) JsonPath.read(response, PGRConstants.JSONPATH_SERVICE_CODES);
			} catch (Exception e) {
				log.error("Exception while parsing serviceCodes: ", e);
				return null;
			}
			serviceReqSearchCriteria.setServiceCodes(serviceCodes);
		}
		searcherRequest = pGRUtils.prepareCountRequest(uri, serviceReqSearchCriteria, requestInfo);

		return searcherRequest;
	}

	/**
	 * method to replace the fileStoreIds with the respective urls acquired from
	 * filestore service
	 * 
	 * @param historyList
	 */
	private void replaceIdsWithUrls(List<ActionHistory> historyList) {

		if (CollectionUtils.isEmpty(historyList))
			return;
		String tenantId = historyList.get(0).getActions().get(0).getTenantId();
		List<String> fileStoreIds = new ArrayList<>();

		historyList.forEach(history -> history.getActions().forEach(action -> {
			List<String> media = action.getMedia();
			if (!CollectionUtils.isEmpty(media))
				fileStoreIds.addAll(media);
		}));

		Map<String, String> urlIdMap = null;
		try {
		urlIdMap = fileStoreRepo.getUrlMaps(tenantId, fileStoreIds);
		}catch (Exception e) {
			log.error(" exception while connecting to filestore : "+e);
		}

		log.info("urlIdMap: " + urlIdMap);
		if (null != urlIdMap) {
			for (int i = 0; i < historyList.size(); i++) {
				ActionHistory history = historyList.get(i);
				for (int j = 0; j < history.getActions().size(); j++) {
					List<ActionInfo> actionList = history.getActions();
					ActionInfo info = actionList.get(j);
					List<String> mediaList = new ArrayList<>();
					for (int k = 0; k < info.getMedia().size(); k++) {
						List<String> oldMedia = info.getMedia();
						String fileStoreId = oldMedia.get(k);
						String url = urlIdMap.get(fileStoreId);
						if (null != url)
							mediaList.add(url);
						else
							mediaList.add(fileStoreId);
					}
					info.setMedia(mediaList);
				}
			}
		}
	}
}
