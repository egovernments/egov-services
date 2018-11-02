package org.egov.pgr.validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.pgr.contract.ReportRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceRequest;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.model.ActionHistory;
import org.egov.pgr.model.ActionInfo;
import org.egov.pgr.model.Service;
import org.egov.pgr.service.GrievanceService;
import org.egov.pgr.service.ReportService;
import org.egov.pgr.utils.ErrorConstants;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.WorkFlowConfigs;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class PGRRequestValidator {

	@Autowired
	private GrievanceService requestService;

	@Autowired
	private PGRUtils pgrUtils;
	
	@Autowired
	private ReportService reportService;

	@Value("${egov.mdms.host}")
	private String mdmsHost;

	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;
	
	@Value("${egov.user.host}")
	private String userBasePath;
	
	@Value("${egov.user.create.endpoint}")
	private String userCreateEndPoint;
	
	@Value("${egov.user.search.endpoint}")
	private String userSearchEndPoint;
	
	
	/**
	 * validates the create Request based on the following cirtera:
	 * 
	 * 1. Checks if the length of actionInfo and services are same.
	 * 2. Overrides accountId field of the request with the id of the logged in user.
	 * 3. Services codes mentioned in the request are validated against the mdms records.
	 * 4. Validates address. 
	 * 
	 * @param serviceRequest
	 */
	public void validateCreate(ServiceRequest serviceRequest) {
		log.info("Validating create request");
		Map<String, String> errorMap = new HashMap<>();
		validateUserRBACProxy(errorMap, serviceRequest.getRequestInfo());
		validateIfArraysEqual(serviceRequest, errorMap);
		vaidateServiceCodes(serviceRequest, errorMap);
		//validateAddressCombo(serviceRequest, errorMap);
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	/**
	 * validates the update Request based on the following criteria:
	 * 
	 * 1. Checks if the length of actionInfo and services are same.
	 * 2. Overrides accountId field of the request with the id of the logged in user.
	 * 3. Services codes mentioned in the request are validated against the mdms records.
	 * 4. Validates assignment of service requests based on various roles.
	 * 5. Validates the action performed on the service request based on the role.
	 * 6. Checks if the service being updated does exist.
	 *
	 * @param serviceRequest
	 */
	public void validateUpdate(ServiceRequest serviceRequest) {
		log.info("Validating update request");
		Map<String, String> errorMap = new HashMap<>();
		validateIfArraysEqual(serviceRequest, errorMap);
		vaidateServiceCodes(serviceRequest, errorMap);
		validateAssignments(serviceRequest, errorMap);
		validateAction(serviceRequest, errorMap);
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	/**
	 * checks the length of both service and actionInfo array to avoid mismatches in
	 * between them
	 * 
	 * @param serviceRequest
	 * @param errorMap
	 */
	private void validateIfArraysEqual(ServiceRequest serviceRequest, Map<String, String> errorMap) {
		log.info("Array equal check");
		if (null != serviceRequest.getActionInfo()
				&& serviceRequest.getServices().size() != serviceRequest.getActionInfo().size())
			errorMap.put(ErrorConstants.UNEQUAL_REQUEST_SIZE_KEY, ErrorConstants.UNEQUAL_REQUEST_SIZE_MSG);
	}

	/**
	 * validates the presence of any of the combination of address
	 * parameters(atleast one of the parameters is mandatory)
	 * 
	 * @param serviceRequest
	 * @param errorMap
	 */
/*	private void validateAddressCombo(ServiceRequest serviceRequest, Map<String, String> errorMap) {

		if (serviceRequest.getServices().size() != serviceRequest.getServices().parallelStream()
				.filter(a -> (a.getAddress() != null || a.getAddressId() != null
						|| (a.getLat() != null && a.getLongitutde() != null)))
				.map(Service::getServiceRequestId).collect(Collectors.toList()).size())
			errorMap.put(ErrorConstants.CREATE_ADDRESS_COMBO_ERROR_KEY, ErrorConstants.CREATE_ADDRESS_COMBO_ERROR_MSG);
	}*/

	/**
	 * validates if the given service codes in the request objects are admissible
	 * 
	 * @param serviceRequest
	 * @param errorMap
	 */
	private void vaidateServiceCodes(ServiceRequest serviceRequest, Map<String, String> errorMap) {
		log.info("Service code check");
		String tenantId = serviceRequest.getServices().get(0).getTenantId();
		List<String> serviceCodes = pgrUtils.getServiceCodes(tenantId,
				serviceRequest.getServices().parallelStream().map(Service::getServiceCode).collect(Collectors.toSet()),
				serviceRequest.getRequestInfo());
		List<String> errorList = new ArrayList<>();
		log.info("serviceCodes: "+serviceCodes);
		serviceRequest.getServices().forEach(a -> {
			if (!serviceCodes.contains(a.getServiceCode()))
				errorList.add(a.getServiceCode());
		});
		if (!errorList.isEmpty())
			errorMap.put(ErrorConstants.INVALID_SERVICECODE_CODE, ErrorConstants.INVALID_SERVICECODE_MSG + errorList);
	}


	/**
	 * validates if the assignee has been given for proper action and not provided
	 * for inappropriate actions
	 * 
	 * @param serviceRequest
	 * @param errorMap
	 */
	private void validateAssignments(ServiceRequest serviceRequest, Map<String, String> errorMap) {

		List<String> errorMsgForActionAssign = new ArrayList<>();
		List<Service> services = serviceRequest.getServices();
		List<ActionInfo> infos = serviceRequest.getActionInfo();

		if (null != infos)
			for (int i = 0; i <= infos.size() - 1; i++) {
				ActionInfo info = infos.get(i);
				if (null != info && null != info.getAction())
					if ((WorkFlowConfigs.ACTION_ASSIGN.equalsIgnoreCase(info.getAction())
							|| WorkFlowConfigs.ACTION_REASSIGN.equalsIgnoreCase(info.getAction()))) {
						if(StringUtils.isEmpty(info.getAssignee())) {
							errorMsgForActionAssign.add(services.get(i).getServiceRequestId());
						}else {
							ReportRequest request = ReportRequest.builder().requestInfo(serviceRequest.getRequestInfo())
									.tenantId(serviceRequest.getServices().get(0).getTenantId()).build();
							List<Long> employeeIds = new ArrayList<>();
							try {
								employeeIds.add(Long.valueOf(info.getAssignee()));
							}catch(Exception e) {
								errorMsgForActionAssign.add(services.get(i).getServiceRequestId());
							}
							Map<Long, String> result = reportService.getEmployeeDetails(request, employeeIds);
							if(CollectionUtils.isEmpty(result.keySet())) {
								errorMsgForActionAssign.add(services.get(i).getServiceRequestId());
							}
						}
					}
					else if (!WorkFlowConfigs.ACTION_ASSIGN.equalsIgnoreCase(info.getAction())
							&& !WorkFlowConfigs.ACTION_REASSIGN.equalsIgnoreCase(info.getAction())
							&& null != info.getAssignee())
						info.setAssignee(null);
			}

		if (!errorMsgForActionAssign.isEmpty())
			errorMap.put(ErrorConstants.ASSIGNEE_MISSING_FOR_ACTION_ASSIGN_REASSIGN_KEY,
					ErrorConstants.ASSIGNEE_MISSING_FOR_ACTION_ASSIGN_REASSIGN_MSG + errorMsgForActionAssign);
	}

	/**
	 * validates the legality of the search criteria given
	 * 
	 * @param criteria
	 * @param requestInfo
	 */
	public void validateSearch(ServiceReqSearchCriteria criteria, RequestInfo requestInfo) {
		log.info("Validating search request...");
		Map<String, String> errorMap = new HashMap<>();
		validateUserRBACProxy(errorMap, requestInfo);
		if ((criteria.getStartDate() != null && criteria.getStartDate() > new Date().getTime())
				|| (criteria.getEndDate() != null && criteria.getEndDate() > new Date().getTime())) {
			errorMap.put(ErrorConstants.INVALID_START_END_DATE_CODE, ErrorConstants.INVALID_START_END_DATE_MSG);
		}
		if ((criteria.getStartDate() != null && criteria.getEndDate() != null)
				&& criteria.getStartDate().compareTo(criteria.getEndDate()) > 0) {
			errorMap.put(ErrorConstants.INVALID_START_DATE_CODE, ErrorConstants.INVALID_START_DATE_MSG);
		}

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);

		log.info("All Validations passed!");
	}

	public void validateUserRBACProxy(Map<String, String> errorMap, RequestInfo requestInfo) {

		if (null != requestInfo.getUserInfo()) {
			if (null == requestInfo.getUserInfo().getType() || requestInfo.getUserInfo().getType().isEmpty()) {
				errorMap.put(ErrorConstants.MISSING_USERTYPE_CODE, ErrorConstants.MISSING_USERTYPE_MSG);
				return;
			}
			if (null == requestInfo.getUserInfo().getId() || (null == requestInfo.getUserInfo().getRoles()
					|| requestInfo.getUserInfo().getRoles().isEmpty())) {
				errorMap.put(ErrorConstants.MISSING_ROLE_USERID_CODE, ErrorConstants.MISSING_ROLE_USERID_MSG);
				return;
			}
		} else {
			errorMap.put(ErrorConstants.MISSING_USERINFO_CODE, ErrorConstants.MISSING_USERINFO_MSG);
			return;
		}

	}

	/**
	 * validates of the action as follows:
	 * 1. Does the user trying to perform the action have rights of that action.
	 * 2. Is the action being performed valid for the current status.
	 * 
	 * This method also checks if the service requests being updated are available in the system. Since it is already a part of the action validation flow.
	 * 
	 * @param serviceRequest
	 * @param errorMap
	 */
	public void validateAction(ServiceRequest serviceRequest, Map<String, String> errorMap) {
		Map<String, List<String>> roleActionMap = WorkFlowConfigs.getRoleActionMap();
		List<String> roles = serviceRequest.getRequestInfo().getUserInfo().getRoles().parallelStream()
				.map(Role::getName).collect(Collectors.toList());
		List<String> actions = null;
		if (roles.contains(PGRConstants.ROLE_NAME_CITIZEN))
			actions = roleActionMap.get(PGRConstants.ROLE_NAME_CITIZEN);
		else
			actions = roleActionMap.get(pgrUtils.getPrecedentRole(serviceRequest.getRequestInfo().getUserInfo()
					.getRoles().parallelStream().map(Role::getName).collect(Collectors.toList())));
		final List<String> actionsAllowedForTheRole = actions;
		log.info("actions: " + actionsAllowedForTheRole);
		if (!CollectionUtils.isEmpty(actions)) {
			List<ActionInfo> infos = serviceRequest.getActionInfo();
			if (!CollectionUtils.isEmpty(infos)) {
				infos.parallelStream().forEach(action -> {
					if(!StringUtils.isEmpty(action.getAction())) {
						if(!actionsAllowedForTheRole.contains(action.getAction())) {
							String errorMsg = ErrorConstants.INVALID_ACTION_FOR_ROLE_MSG;
							errorMsg = errorMsg.replace("$action", action.getAction()).replace("$role", serviceRequest.getRequestInfo().getUserInfo().getRoles().get(0).getName());
							errorMap.put(ErrorConstants.INVALID_ACTION_FOR_ROLE_CODE, errorMsg + "for serviceRequest: "+action.getBusinessKey());
						}else {
							validateActionsOnCurrentStatus(serviceRequest, errorMap) ;
						}
					}
				});
			}
		} else {
			errorMap.put(ErrorConstants.INVALID_ROLE_CODE, ErrorConstants.INVALID_ROLE_MSG
					+ serviceRequest.getRequestInfo().getUserInfo().getRoles().get(0).getName().toUpperCase());
		}
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
	
	 /**
	  * This method valiates if the action being performed is valid on the current status of the sevice request.
	  * NOTE - It also checks if the service requests being validated are available in the system. As part of the action validation.
	  * 
	  * @param serviceRequest
	  * @param errorMap
	  */
	public void validateActionsOnCurrentStatus(ServiceRequest serviceRequest, Map<String, String> errorMap) {
		Map<String, List<String>> actioncurrentStatusMap = WorkFlowConfigs.getActionCurrentStatusMap();
		ServiceResponse serviceResponse = getServiceRequests(serviceRequest, errorMap);
		if (!errorMap.isEmpty())
			return;
		List<ActionHistory> historys = serviceResponse.getActionHistory();
		Map<String, ActionHistory> historyMap = new HashMap<>();
		historys.forEach(a -> historyMap.put(a.getActions().get(0).getBusinessKey(), a));
		for (int index = 0; index < serviceResponse.getServices().size(); index++) {
			Service service = serviceRequest.getServices().get(index);
			ActionHistory history = historyMap.get(service.getServiceRequestId());
			ActionInfo actionInfo = serviceRequest.getActionInfo().get(index);
			String currentStatus = pgrUtils.getCurrentStatus(history);
			List<String> validStatusList = actioncurrentStatusMap.get(actionInfo.getAction());
			if (!StringUtils.isEmpty(currentStatus) && !validStatusList.contains(currentStatus)) {
				String errorMsg = ErrorConstants.INVALID_ACTION_ON_STATUS_MSG;
				errorMsg = errorMsg.replace("$action", actionInfo.getAction()).replace("$status", currentStatus);
				errorMap.put(ErrorConstants.INVALID_ACTION_ON_STATUS_CODE, errorMsg + "for serviceRequest: "+service.getServiceRequestId());
			}
			if (!WorkFlowConfigs.ACTION_CLOSE.equals(actionInfo.getAction())
					&& (!StringUtils.isEmpty(service.getFeedback()) || !StringUtils.isEmpty(service.getRating()))) {
				errorMap.put(ErrorConstants.UPDATE_FEEDBACK_ERROR_KEY, ErrorConstants.UPDATE_FEEDBACK_ERROR_MSG);
			}
		}		

	}
	
	/**
	 * validates the services by fetching the service ids from the database
	 * 
	 * @param serviceRequest
	 * @param errorMap
	 */
	private ServiceResponse getServiceRequests(ServiceRequest serviceRequest, Map<String, String> errorMap) {
		log.info("Validating if servicerequests exist");
		ObjectMapper mapper = pgrUtils.getObjectMapper();
		ServiceReqSearchCriteria serviceReqSearchCriteria = ServiceReqSearchCriteria.builder()
				.tenantId(serviceRequest.getServices().get(0).getTenantId()).serviceRequestId(serviceRequest
						.getServices().stream().map(Service::getServiceRequestId).collect(Collectors.toList()))
				.build();
		ServiceResponse serviceResponse = mapper.convertValue(requestService
				.getServiceRequestDetails(serviceRequest.getRequestInfo(), serviceReqSearchCriteria), ServiceResponse.class);
		Map<String, Service> map = serviceResponse.getServices().stream().collect(Collectors.toMap(Service::getServiceRequestId, Function.identity()));
		List<String> errorList = new ArrayList<>();
		serviceRequest.getServices().forEach(a -> {
			if (map.get(a.getServiceRequestId()) == null)
				errorList.add(a.getServiceRequestId());
		});
		if (!errorList.isEmpty())
			errorMap.put(ErrorConstants.INVALID_SERVICEREQUESTID_CODE, ErrorConstants.INVALID_SERVICEREQUESTID_MSG + errorList);
		
		return serviceResponse;
	}
}
