package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.contract.CountResponse;
import org.egov.pgr.contract.IdResponse;
import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceRequest;
import org.egov.pgr.contract.ServiceRequestDetails;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.model.ActionHistory;
import org.egov.pgr.model.ActionInfo;
import org.egov.pgr.model.AuditDetails;
import org.egov.pgr.model.Service;
import org.egov.pgr.model.Service.StatusEnum;
import org.egov.pgr.model.user.Citizen;
import org.egov.pgr.model.user.CreateUserRequest;
import org.egov.pgr.model.user.UserResponse;
import org.egov.pgr.model.user.UserSearchRequest;
import org.egov.pgr.model.user.UserType;
import org.egov.pgr.producer.PGRProducer;
import org.egov.pgr.repository.FileStoreRepo;
import org.egov.pgr.repository.IdGenRepo;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.ErrorConstants;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.ResponseInfoFactory;
import org.egov.pgr.utils.WorkFlowConfigs;
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
	
	@Value("${egov.hr.employee.v2.host}")
	private String hrEmployeeHost;

	@Value("${egov.hr.employee.v2.search.endpoint}")
	private String hrEmployeeV2SearchEndpoint;
		
	@Value("${egov.user.host}")
	private String userBasePath;
	
	@Value("${egov.user.search.endpoint}")
	private String userSearchEndPoint;
	
	@Value("${egov.user.create.endpoint}")
	private String userCreateEndPoint;

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

	/***
	 * Asynchronous method performs business logic if any and adds the data to
	 * persister queue on create topic
	 * 
	 * @param request
	 */
	public ServiceResponse create(ServiceRequest request) {
		log.info("Service layer for createss");
		enrichserviceRequestForcreate(request);
		pGRProducer.push(saveTopic, request);
		return getServiceResponse(request);
	}

	/**
	 * Asynchronous method performs business logic if any and adds the data to
	 * persister queue on update topic
	 * 
	 * @param request
	 */
	public ServiceResponse update(ServiceRequest request) {
		enrichServiceRequestForUpdate(request);
		if (null == request.getActionInfo())
			request.setActionInfo(new ArrayList<ActionInfo>());
		pGRProducer.push(updateTopic, request);
		return getServiceResponse(request);
	}
	
	/**
	 * private method to enrich request with Ids and action infos for create
	 * 
	 * @param serviceRequest
	 */
	private void enrichserviceRequestForcreate(ServiceRequest serviceRequest) {
		log.info("enriching service request create.");
		Map<String, String> actionStatusMap = WorkFlowConfigs.getActionStatusMap();
		RequestInfo requestInfo = serviceRequest.getRequestInfo();
		List<Service> serviceReqs = serviceRequest.getServices();
		String tenantId = serviceReqs.get(0).getTenantId();
		overRideCitizenAccountId(serviceRequest);
		validateAndCreateUser(serviceRequest);
		List<String> servReqIdList = getIdList(requestInfo, tenantId, serviceReqs.size(), PGRConstants.SERV_REQ_ID_NAME,
				PGRConstants.SERV_REQ_ID_FORMAT);
		AuditDetails auditDetails = pGRUtils.getAuditDetails(String.valueOf(requestInfo.getUserInfo().getId()), true);
		String by = auditDetails.getCreatedBy() + ":" + requestInfo.getUserInfo().getRoles().get(0).getName();
		List<ActionInfo> actionInfos = new LinkedList<>();
		for (int servReqCount = 0; servReqCount < serviceReqs.size(); servReqCount++) {
			Service servReq = serviceReqs.get(servReqCount);
			String currentId = servReqIdList.get(servReqCount);
			servReq.setAuditDetails(auditDetails); servReq.setServiceRequestId(currentId);
			servReq.setStatus(StatusEnum.OPEN);servReq.setFeedback(null);servReq.setRating(null);
			ActionInfo actionInfo = ActionInfo.builder().uuid(UUID.randomUUID().toString()).businessKey(currentId)
					.action(WorkFlowConfigs.ACTION_OPEN).assignee(null).by(by).when(auditDetails.getCreatedTime()).tenantId(tenantId)
					.status(actionStatusMap.get(WorkFlowConfigs.ACTION_OPEN)).build();
			actionInfos.add(actionInfo);
		}
		serviceRequest.setActionInfo(actionInfos);
	}
	
	/**
	 * Override the accountId of every request with the user id
	 * @param serviceRequest
	 */
	private void overRideCitizenAccountId(ServiceRequest serviceRequest) {
		User user = serviceRequest.getRequestInfo().getUserInfo();
		List<String> codes = user.getRoles().parallelStream().map(Role::getName).collect(Collectors.toList());
		if (codes.contains(PGRConstants.ROLE_CITIZEN) || codes.contains(PGRConstants.ROLE_NAME_CITIZEN))
			serviceRequest.getServices().forEach(service -> service.setAccountId(String.valueOf(user.getId())));
	}
	
	/**
	 * Checks if the user is present for the given citizen object.
	 * 
	 * @param citizen
	 * @param requestInfo
	 * @param tenantId
	 * @return
	 */
	private String isUserPresent(Citizen citizen, RequestInfo requestInfo, String tenantId) {
		ObjectMapper mapper = pGRUtils.getObjectMapper();
		UserSearchRequest searchRequest = UserSearchRequest.builder().userName(citizen.getMobileNumber())
				.tenantId(tenantId).userType(PGRConstants.ROLE_CITIZEN).requestInfo(requestInfo).build();
		StringBuilder url = new StringBuilder(userBasePath+userSearchEndPoint); 
		UserResponse res = mapper.convertValue(serviceRequestRepository.fetchResult(url, searchRequest), UserResponse.class);
		if(CollectionUtils.isEmpty(res.getUser())) {
			return null;
		}
		return res.getUser().get(0).getId().toString();
	}
	
	/**
	 * When CSR files a complaint, this method captures the user information if the user exists otherwise creates the user.
	 * 
	 * @param serviceRequest
	 * @param errorMap
	 */
	private void validateAndCreateUser(ServiceRequest serviceRequest) {
		RequestInfo requestInfo = serviceRequest.getRequestInfo();
		List<String> roleNames = requestInfo.getUserInfo().getRoles().parallelStream().map(Role::getName)
				.collect(Collectors.toList());
		if(roleNames.contains(PGRConstants.ROLE_NAME_CSR) || roleNames.contains(PGRConstants.ROLE_CSR)) {
			serviceRequest.getServices().parallelStream().forEach(request -> {
				String accId = null;
				if (null != request.getCitizen()) {
					accId = isUserPresent(request.getCitizen(),requestInfo,request.getTenantId());
					if (StringUtils.isEmpty(accId)) {
						accId = createUser(request.getCitizen(),requestInfo,request.getTenantId());
					}
					request.setAccountId(accId);
				}
			});	
		}
	}
	
	private String createUser(Citizen citizen, RequestInfo requestInfo, String tenantId) {
		ObjectMapper mapper = pGRUtils.getObjectMapper();
		citizen.setUserName(citizen.getMobileNumber());
		citizen.setActive(true);
		citizen.setTenantId(tenantId);
		citizen.setType(UserType.CITIZEN);
		citizen.setRoles(Arrays.asList(org.egov.pgr.model.user.Role.builder().code(PGRConstants.ROLE_CITIZEN).build()));
		StringBuilder url = new StringBuilder(userBasePath+userCreateEndPoint); 
		CreateUserRequest req = CreateUserRequest.builder().citizen(citizen).requestInfo(requestInfo).build();
		UserResponse res = mapper.convertValue(serviceRequestRepository.fetchResult(url, req), UserResponse.class);
		return res.getUser().get(0).getId().toString();
	}

	/**
	 * Util method for the update to enrich the actions in the request 
	 * 
	 * @param request
	 */
	private void enrichServiceRequestForUpdate(ServiceRequest request) {
		Map<String, String> actionStatusMap = WorkFlowConfigs.getActionStatusMap(); 
		Map<String, List<String>> errorMap = new HashMap<>();
		RequestInfo requestInfo = request.getRequestInfo();
		List<Service> serviceReqs = request.getServices();
		List<ActionInfo> actionInfos = request.getActionInfo();
		final AuditDetails auditDetails = pGRUtils.getAuditDetails(String.valueOf(requestInfo.getUserInfo().getId()),false);
		for (int index = 0; index < serviceReqs.size(); index++) {
			Service service = serviceReqs.get(index);
			ActionInfo actionInfo = actionInfos.get(index);
			service.setAuditDetails(auditDetails);
			service.setStatus(StatusEnum.fromValue(actionStatusMap.get(actionInfo.getAction())));
			String role = pGRUtils.getPrecedentRole(requestInfo.getUserInfo().getRoles().parallelStream().map(Role::getName)
					.collect(Collectors.toList()));
			if(StringUtils.isEmpty(role))
				role = requestInfo.getUserInfo().getRoles().get(0).getName();
			actionInfo.setUuid(UUID.randomUUID().toString()); actionInfo.setBusinessKey(service.getServiceRequestId()); 
			actionInfo.setBy(auditDetails.getLastModifiedBy() + ":" + role); actionInfo.setWhen(auditDetails.getLastModifiedTime());
			actionInfo.setTenantId(service.getTenantId()); actionInfo.status(actionInfo.getAction()); 
			actionInfo.setStatus(actionStatusMap.get(actionInfo.getAction()));			
		}
		if (!errorMap.isEmpty()) {
			Map<String, String> newMap = new HashMap<>();
			errorMap.keySet().forEach(key -> newMap.put(key, errorMap.get(key).toString()));
			throw new CustomException(newMap);
		}
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
	 * returns ServiceResponse fetched from database/built based on the given
	 * ServiceRequest
	 * 
	 * @param serviceReqRequest
	 * @return serviceReqResponse
	 */
	public ServiceResponse getServiceResponse(ServiceRequest serviceReqRequest) {
			return ServiceResponse.builder()
					.responseInfo(factory.createResponseInfoFromRequestInfo(serviceReqRequest.getRequestInfo(), true))
					.services(serviceReqRequest.getServices())
					.actionHistory(convertActionInfosToHistorys(serviceReqRequest.getActionInfo())).build();
	}

	/**
	 * helper method to convert list of actioninfos to list of actionHistorys
	 * 
	 * @param actionInfos
	 * @return
	 */
	private List<ActionHistory> convertActionInfosToHistorys(List<ActionInfo> actionInfos) {
		List<ActionHistory> historys = new ArrayList<>();
		if (!CollectionUtils.isEmpty(actionInfos))
			actionInfos.forEach(a -> {
				List<ActionInfo> infos = new ArrayList<>();
				infos.add(a);
				historys.add(new ActionHistory(infos));
			});
		return historys;
	}

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
			if (e.getMessage().equals(ErrorConstants.NO_DATA_MSG))
				return pGRUtils.getDefaultServiceResponse(requestInfo);
			else
				throw e;
		}
		searcherRequest = pGRUtils.prepareSearchRequestWithDetails(uri, serviceReqSearchCriteria, requestInfo);
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
		log.debug(PGRConstants.SEARCHER_RESPONSE_TEXT + response);
		if (null == response)
			return pGRUtils.getDefaultServiceResponse(requestInfo);
		ServiceResponse serviceResponse = prepareResult(response, requestInfo);
		if(CollectionUtils.isEmpty(serviceResponse.getServices()))
			return serviceResponse;
		else
			return enrichSearchResWithUserInfo(requestInfo, serviceResponse);
	}

	/**
	 * Method to enrich the request for search based on roles.
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 */
	public void enrichRequest(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
		log.info("Enriching request for search");
		String precedentRole = pGRUtils.getPrecedentRole(requestInfo.getUserInfo().getRoles().parallelStream().map(Role::getName)
				.collect(Collectors.toList()));
		if (requestInfo.getUserInfo().getType().equalsIgnoreCase(PGRConstants.ROLE_CITIZEN)) {
			serviceReqSearchCriteria.setAccountId(requestInfo.getUserInfo().getId().toString());
		    serviceReqSearchCriteria.setTenantId(serviceReqSearchCriteria.getTenantId().split("[.]")[0]); //citizen can search his complaints across state.
		} else if (requestInfo.getUserInfo().getType().equalsIgnoreCase(PGRConstants.ROLE_EMPLOYEE)) {
			if(StringUtils.isEmpty(precedentRole)) {
				throw new CustomException(ErrorConstants.UNAUTHORIZED_USER_KEY, ErrorConstants.UNAUTHORIZED_USER_MSG);
			}
			/**
			 * DGRO belongs to a department and that department takes care of certain complaint types.
			 * A DGRO can address/see only the complaints belonging to those complaint types.
			 */
			if (precedentRole.equalsIgnoreCase(PGRConstants.ROLE_NAME_DGRO)) { 
				Object response = fetchServiceDefs(requestInfo, serviceReqSearchCriteria.getTenantId(), 
						getDepartment(serviceReqSearchCriteria, requestInfo, getDepartmentCode(serviceReqSearchCriteria, requestInfo)));
				if (null == response) {
					throw new CustomException(ErrorConstants.NO_DATA_KEY, ErrorConstants.NO_DATA_MSG);
				}
				try {
					List<String> serviceCodes = JsonPath.read(response, PGRConstants.JSONPATH_SERVICE_CODES);
					if(serviceCodes.isEmpty())
						throw new CustomException(ErrorConstants.NO_DATA_KEY, ErrorConstants.NO_DATA_MSG);
					serviceReqSearchCriteria.setServiceCodes(serviceCodes);
				} catch (Exception e) {
					throw new CustomException(ErrorConstants.NO_DATA_KEY, ErrorConstants.NO_DATA_MSG);
				}
				/**
				 * An Employee can by default search only the complaints assigned to him.
				 */
			} else if (precedentRole.equalsIgnoreCase(PGRConstants.ROLE_NAME_EMPLOYEE)) {
				if (StringUtils.isEmpty(serviceReqSearchCriteria.getAssignedTo()) && CollectionUtils.isEmpty(serviceReqSearchCriteria.getServiceRequestId())) {
					serviceReqSearchCriteria.setAssignedTo(requestInfo.getUserInfo().getId().toString());
				}
				/**
				 * CSR can search complaints across the state.
				 */
			} else if (precedentRole.equalsIgnoreCase(PGRConstants.ROLE_NAME_CSR)) {
				serviceReqSearchCriteria.setTenantId(serviceReqSearchCriteria.getTenantId().split("[.]")[0]); //state-level
			}
		}
		if (!StringUtils.isEmpty(serviceReqSearchCriteria.getAssignedTo())) {
			List<String> serviceRequestIds = getServiceRequestIdsOnAssignedTo(requestInfo, serviceReqSearchCriteria);
			if (serviceRequestIds.isEmpty())
				throw new CustomException(ErrorConstants.NO_DATA_KEY, ErrorConstants.NO_DATA_MSG);
			serviceReqSearchCriteria.setServiceRequestId(serviceRequestIds);
		}
		if(!StringUtils.isEmpty(serviceReqSearchCriteria.getGroup()) && CollectionUtils.isEmpty(serviceReqSearchCriteria.getServiceCodes())) {
			List<String> departments = new ArrayList<>(); departments.add(serviceReqSearchCriteria.getGroup());
			Object response = fetchServiceDefs(requestInfo, serviceReqSearchCriteria.getTenantId(), departments);
			if (null == response) {
				throw new CustomException(ErrorConstants.NO_DATA_KEY, ErrorConstants.NO_DATA_MSG);
			}
			try {
				List<String> serviceCodes = JsonPath.read(response, PGRConstants.JSONPATH_SERVICE_CODES);
				if(serviceCodes.isEmpty())
					throw new CustomException(ErrorConstants.NO_DATA_KEY, ErrorConstants.NO_DATA_MSG);
				serviceReqSearchCriteria.setServiceCodes(serviceCodes);
			} catch (Exception e) {
				throw new CustomException(ErrorConstants.NO_DATA_KEY, ErrorConstants.NO_DATA_MSG);
			}
		}
	}

	/**
	 * This method fetches all the codes of departments that the employee belongs to.
	 * 
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return
	 */
	public List<String> getDepartmentCode(ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		RequestInfoWrapper requestInfoWrapper = pGRUtils.prepareRequestForEmployeeSearch(uri, requestInfo,
				serviceReqSearchCriteria);
		Object response = null;
		log.debug("Employee: " + response);
		List<String> departmenCodes = null;
		try {
			response = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
			if (null == response) {
				throw new CustomException(ErrorConstants.UNAUTHORIZED_EMPLOYEE_TENANT_KEY,
						ErrorConstants.UNAUTHORIZED_EMPLOYEE_TENANT_MSG);
			}
			log.debug("Employee: " + response);
			departmenCodes = JsonPath.read(response, PGRConstants.EMPLOYEE_DEPTCODES_JSONPATH);
		} catch (Exception e) {
			log.error("Exception: " + e);
			throw new CustomException(ErrorConstants.UNAUTHORIZED_EMPLOYEE_TENANT_KEY,
					ErrorConstants.UNAUTHORIZED_EMPLOYEE_TENANT_MSG);
		}
		return departmenCodes;
	}	

	/**
	 * Get department on department code
	 * 
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @param departmentCode
	 * @return String
	 */
	public List<String> getDepartment(ServiceReqSearchCriteria serviceReqSearchCriteria, RequestInfo requestInfo, List<String> departmentCodes) {
		StringBuilder deptUri = new StringBuilder();
		List<String> departments = null;
		Object response = null;
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareMdMsRequestForDept(deptUri, serviceReqSearchCriteria.getTenantId(), departmentCodes, requestInfo);
		try {
			response = serviceRequestRepository.fetchResult(deptUri, mdmsCriteriaReq);
			if (null == response) {
				throw new CustomException(ErrorConstants.INVALID_DEPARTMENT_TENANT_KEY, ErrorConstants.INVALID_DEPARTMENT_TENANT_MSG);
			}
			departments = JsonPath.read(response, PGRConstants.JSONPATH_DEPARTMENTS);
			if(CollectionUtils.isEmpty(departments)) {
				throw new CustomException(ErrorConstants.INVALID_DEPARTMENT_TENANT_KEY, ErrorConstants.INVALID_DEPARTMENT_TENANT_MSG);
			}
		} catch (Exception e) {
			log.error("Exception: " + e);
			throw new CustomException(ErrorConstants.INVALID_DEPARTMENT_TENANT_KEY,
					ErrorConstants.INVALID_DEPARTMENT_TENANT_MSG);
		}
		return departments;
	}

	/**
	 * method to fetch service defs from mdms based on dept
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param department
	 * @return Object
	 * @author vishal
	 */
	public Object fetchServiceDefs(RequestInfo requestInfo, String tenantId, List<String> departments) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareSearchRequestForServiceCodes(uri, tenantId, departments,
				requestInfo);
		Object response = null;
		try {
			response = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
		} catch (Exception e) {
			log.error("Exception while fetching serviceCodes: " + e);
		}
		return response;

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
			log.debug("Searcher response: " + response);
			if (null == response)
				return serviceRequestIds;
			serviceRequestIds = JsonPath.read(response, PGRConstants.SRID_ASSIGNEDTO_JSONPATH);
		} catch (Exception e) {
			log.error("Exception while parsing SRid search on AssignedTo result: " + e);
			return serviceRequestIds;
		}
		log.debug("serviceRequestIds: " + serviceRequestIds);

		return serviceRequestIds;

	}

	/**
	 * This method formats the search result according to the contract.
	 * 
	 * @param response
	 * @param requestInfo
	 * @return
	 */
	public ServiceResponse prepareResult(Object response, RequestInfo requestInfo) {
		ObjectMapper mapper = pGRUtils.getObjectMapper();
		List<Service> services = new ArrayList<>();
		List<ActionHistory> actionHistory = new ArrayList<>();
		List<ServiceRequestDetails> result = new ArrayList<>();
		List<Object> list = JsonPath.read(response, "$.services");
		list.parallelStream().forEach(entry -> result.add(mapper.convertValue(entry, ServiceRequestDetails.class)));
		result.parallelStream().forEach(obj -> {
			ActionHistory actionHis = new ActionHistory();
			actionHis.setActions(obj.getActionhistory());
			actionHistory.add(actionHis);
			obj.setActionhistory(null);
			services.add(obj.getServices());
		});
		replaceIdsWithUrls(actionHistory);

		return ServiceResponse.builder().responseInfo(factory.createResponseInfoFromRequestInfo(requestInfo, true))
				.services(services).actionHistory(actionHistory).build();
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
		try {
			enrichRequest(requestInfo, serviceReqSearchCriteria);
		} catch (CustomException e) {
			if (e.getMessage().equals(ErrorConstants.NO_DATA_MSG))
				return pGRUtils.getDefaultCountResponse(requestInfo);
			else
				throw e;
		}
		searcherRequest = pGRUtils.prepareCountRequestWithDetails(uri, serviceReqSearchCriteria, requestInfo);
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
		log.info("Searcher response: " + response);
		if (null == response)
			return pGRUtils.getDefaultServiceResponse(requestInfo);
		Double count = JsonPath.read(response, PGRConstants.PG_JSONPATH_COUNT);
		return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, true), count);
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
		try {
			String tenantId = historyList.get(0).getActions().get(0).getTenantId();
			List<String> fileStoreIds = new ArrayList<>();
			historyList.forEach(history -> history.getActions().forEach(action -> {
				List<String> media = action.getMedia();
				if (!CollectionUtils.isEmpty(media))
					fileStoreIds.addAll(media);
			}));
			Map<String, String> computeUriIdMap = new HashMap<>();
			try {
				computeUriIdMap = fileStoreRepo.getUrlMaps(tenantId.split("\\.")[0], fileStoreIds);
			} catch (Exception e) {
				log.error(" exception while connecting to filestore : " + e);
			}
			final Map<String, String> urlIdMap = computeUriIdMap;
			if(!CollectionUtils.isEmpty(urlIdMap.keySet())) {
				historyList.parallelStream().forEach(history -> {
					history.getActions().parallelStream().forEach(action -> {
						List<String> mediaList = new ArrayList<>();
						action.getMedia().forEach(media -> {
							media = StringUtils.isEmpty(urlIdMap.get(media)) ? media : urlIdMap.get(media);
							mediaList.add(media);
						});
						action.setMedia(mediaList);
					});
				});
			}
		} catch (Exception e) {
			log.error("Exception while replacing s3 links: " + e);
		}
	}
	
	/**
	 * This method populates timeline information of the service request with user details of the actors on the complaint
	 * 
	 * @param requestInfo
	 * @param response
	 * @return
	 */
	public ServiceResponse enrichSearchResWithUserInfo(RequestInfo requestInfo, ServiceResponse response) {
		List<Long> userIds = response.getServices().parallelStream()
				.map(a -> {
					try {
						return Long.parseLong(a.getAccountId());
					}catch(Exception e) {
						return null;
					}
				}).collect(Collectors.toList());
		String tenantId = response.getServices().get(0).getTenantId().split("[.]")[0]; //citizen is state-level no point in sending ulb level tenant.
		UserResponse userResponse = getUsers(requestInfo, tenantId, userIds);
		if(null != userResponse) {
			Map<Long, Citizen> userResponseMap = userResponse.getUser().parallelStream()
					.collect(Collectors.toMap(Citizen :: getId, Function.identity()));
			for(Service service: response.getServices()) {
				Long id = null;
				try {
					id = Long.parseLong(service.getAccountId());
				}catch(Exception e) {}
				service.setCitizen(userResponseMap.get(id));
			}
		}
		return response;
	}
	
	/**
	 * Fetches Users to be populated in the response
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param userIds
	 * @return
	 */
	public UserResponse getUsers(RequestInfo requestInfo, String tenantId, List<Long> userIds) {
		ObjectMapper mapper = pGRUtils.getObjectMapper();
		UserSearchRequest searchRequest = UserSearchRequest.builder().id(userIds).tenantId(tenantId)
				.userType(PGRConstants.ROLE_CITIZEN).requestInfo(requestInfo).build();
		StringBuilder url = new StringBuilder();
		url.append(userBasePath).append(userSearchEndPoint);
		try {
			UserResponse res = mapper.convertValue(serviceRequestRepository.fetchResult(url, searchRequest), UserResponse.class);
			if(CollectionUtils.isEmpty(res.getUser())) {
				return null;
			}else {
				return res;
			}
		}catch(Exception e) {
			return null;
		}
		
	}

}
