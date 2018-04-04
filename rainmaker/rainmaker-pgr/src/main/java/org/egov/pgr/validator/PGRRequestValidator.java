package org.egov.pgr.validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.PGRApp;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceRequest;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.model.ActionInfo;
import org.egov.pgr.model.Service;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.service.GrievanceService;
import org.egov.pgr.utils.ErrorConstants;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class PGRRequestValidator {

	@Autowired
	private GrievanceService requestService;
	
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	@Autowired
	private PGRUtils pgrUtils;
	
	@Value("${egov.mdms.host}")
	private String mdmsHost;
	
	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;

	public void validateCreate(ServiceRequest serviceRequest) {
		
		String tenantId = serviceRequest.getServices().get(0).getTenantId();
		List<String> serviceCodeList = getServiceCodes(tenantId,
				serviceRequest.getServices().parallelStream().map(Service::getServiceCode).collect(Collectors.toSet()), serviceRequest.getRequestInfo());
					
		Map<String, String> errorMap = new HashMap<>();
		userInfoCheck(serviceRequest, errorMap);
		//employeeCreateCheck(serviceRequest.getRequestInfo(),errorMap); //FIXME not needed as of now
		overRideCitizenAccountId(serviceRequest);
		vaidateServiceCodes(serviceRequest, errorMap, serviceCodeList);
		validateAddressCombo(serviceRequest, errorMap);
		
		if(!errorMap.isEmpty())
			throw new CustomException(errorMap);
		
	}
	
	private void validateAddressCombo(ServiceRequest serviceRequest, Map<String, String> errorMap) {

		if(serviceRequest.getServices().size() != serviceRequest.getServices().parallelStream()
				.filter(a -> (a.getAddress() != null || a.getAddressId() != null
						|| (a.getLat() != null && a.getLong() != null)))
				.map(Service::getServiceRequestId).collect(Collectors.toList()).size())
			errorMap.put(ErrorConstants.CREATE_ADDRESS_COMBO_ERROR_KEY, ErrorConstants.CREATE_ADDRESS_COMBO_ERROR_MSG);
	}

	public List<String> getServiceCodes(String tenantId, Set<String> inputCodes, RequestInfo requestInfo) {

		StringBuilder uri = new StringBuilder(mdmsHost).append(mdmsEndpoint);
		MdmsCriteriaReq criteriaReq = pgrUtils.prepareMdMsRequest(tenantId.split("\\.")[0], PGRConstants.SERVICE_CODES,
				inputCodes.toString(), requestInfo);
		try {
			Object result = serviceRequestRepository.fetchResult(uri, criteriaReq);
			return JsonPath.read(result, PGRConstants.JSONPATH_SERVICE_CODES);
		} catch (Exception e) {
			throw new CustomException(ErrorConstants.INVALID_TENANT_ID_MDMS_SERVICE_CODE_KEY,
					ErrorConstants.INVALID_TENANT_ID_MDMS_SERVICE_CODE_MSG);
		}
	}

	private void overRideCitizenAccountId(ServiceRequest serviceRequest) {

		User user = serviceRequest.getRequestInfo().getUserInfo();
		List<String> names = user.getRoles().parallelStream().map(Role::getName).collect(Collectors.toList());
		boolean isUserCitizen = names.contains("CITIZEN") || names.contains("Citizen");
		if (isUserCitizen)
			serviceRequest.getServices().forEach(service -> service.setAccountId(String.valueOf(user.getId())));
	}

	private void employeeCreateCheck(RequestInfo requestInfo, Map<String, String> errorMap) {

		List<String> roleNames = requestInfo.getUserInfo().getRoles().parallelStream().map(Role::getName)
				.collect(Collectors.toList());

		if (roleNames.contains("EMPLOYEE") || roleNames.contains("Employee"))
			errorMap.put("EG_PGR_EMPLOYEE_ERROR", " An Employee cannot register a grievance");
		if (!org.springframework.util.CollectionUtils.isEmpty(errorMap))
			throw new CustomException(errorMap);
	}

	public void validateUpdate(ServiceRequest serviceRequest) {

		Map<String, String> errorMap = new HashMap<>();
		userInfoCheck(serviceRequest, errorMap);
		overRideCitizenAccountId(serviceRequest);
		validateAssignments(serviceRequest, errorMap);
		validateAction(serviceRequest, errorMap);
		
		
		ServiceReqSearchCriteria serviceReqSearchCriteria = ServiceReqSearchCriteria.builder()
				.tenantId(serviceRequest.getServices().get(0).getTenantId()).serviceRequestId(serviceRequest
						.getServices().stream().map(Service::getServiceRequestId).collect(Collectors.toList()))
				.build();

		Map<String, Service> map = ((ServiceResponse) requestService
				.getServiceRequestDetails(serviceRequest.getRequestInfo(), serviceReqSearchCriteria)).getServices()
						.stream().collect(Collectors.toMap(Service::getServiceRequestId, Function.identity()));

		List<String> errorList = new ArrayList<>();
		serviceRequest.getServices().forEach(a -> {

			if (map.get(a.getServiceRequestId()) == null)
				errorList.add(a.getServiceRequestId());
		});

		if (!errorList.isEmpty())
			errorMap.put("EG_PGR_UPDATE_SERVICEREQUESTID",
					"request object does not exist for the given id's : " + errorList);

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	private void validateAssignments(ServiceRequest serviceRequest, Map<String, String> errorMap) {
		
		final String errorCode = "EG_PGR_UPDATE_ASSIGN";
		List<String> errorMsg = new ArrayList<>();
		List<Service> services = serviceRequest.getServices();

		List<ActionInfo> infos = serviceRequest.getActionInfo();
		
		if (null != infos)
			for (int i = 0; i <= infos.size() - 1; i++) {
				ActionInfo info = infos.get(i);
				if (null != info && null != info.getAction() && (info.getAction().equalsIgnoreCase("assign")
						|| info.getAction().equalsIgnoreCase("reassign")) && info.getAssignee() == null)
					errorMsg.add(services.get(i).getServiceRequestId());
			}

		if(!errorMsg.isEmpty())
			errorMap.put(errorCode, " The assignees are missing for the assign actions of services with ids : "+errorMsg);
	}

	public void userInfoCheck(ServiceRequest serviceRequest, Map<String, String> errorMap) {
		log.info("Validating userInfo......."+serviceRequest.getRequestInfo().getUserInfo());

		if (null == serviceRequest.getRequestInfo()) {
			errorMap.put("EG_PGR_REQUESTINFO", "Request info is mandatory for serviceRequest");
			
		}else if (null == serviceRequest.getRequestInfo().getUserInfo()) {
			errorMap.put("EG_PGR_REQUESTINFO_USERINFO", "UserInfo info is mandatory for serviceRequest");
			
		}else if (CollectionUtils.isEmpty(serviceRequest.getRequestInfo().getUserInfo().getRoles())) {
			errorMap.put("EG_PGR_REQUESTINFO_USERINFO_ROLES", "Roles cannot be empty for serviceRequest");
		}
		
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
	
	
	public void vaidateServiceCodes(ServiceRequest serviceRequest, Map<String, String> errorMap, List<String> serviceCodes) {

		
		List<String> errorList = new ArrayList<>();
		serviceRequest.getServices().forEach(a -> {

			if (!serviceCodes.contains(a.getServiceCode()))
				errorList.add(a.getServiceCode());
		});
				
		if (!errorList.isEmpty())
			errorMap.put("EG_PGR_INVALID_SERVICECODE",
					"Following Service codes are invalid: " + errorList);
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}


	public void validateSearch(ServiceReqSearchCriteria criteria, RequestInfo requestInfo) {
		log.info("Validating search request...");
		Map<String, String> errorMap = new HashMap<>();
		validateUserRBACProxy(errorMap, requestInfo);
		if ((criteria.getStartDate() != null && criteria.getStartDate() > new Date().getTime())
				|| (criteria.getEndDate() != null && criteria.getEndDate() > new Date().getTime())) {
			errorMap.put("400", "startDate or endDate cannot be greater than currentDate");
			throw new CustomException("400", "startDate or endDate cannot be greater than currentDate");
		}
		if ((criteria.getStartDate() != null && criteria.getEndDate() != null)
				&& criteria.getStartDate().compareTo(criteria.getEndDate()) > 0) {
			errorMap.put("400", "startDate cannot be greater than endDate");
		}

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);

		log.info("All Validations passed!");
	}

	public void validateUserRBACProxy(Map<String, String> errorMap,
			RequestInfo requestInfo) {

		if (null != requestInfo.getUserInfo()) {
			if (null == requestInfo.getUserInfo().getId() || (null == requestInfo.getUserInfo().getRoles()
					|| requestInfo.getUserInfo().getRoles().isEmpty())) {
				errorMap.put("401", "Unauthenticated user, userId and Roles missing in the request.");
				return;
			}
		} else {
			errorMap.put("401", "Unauthenticated user, userInfo missing in the request.");
			return;
		}

	}
	
	public void validateAction(ServiceRequest serviceRequest, Map<String, String> errorMap) {
		Map<String, List<String>> roleActionMap = PGRApp.getRoleActionMap();
		final String errorCode = "EG_PGR_UPDATE_INVALID_ACTION";
		List<String> actions = roleActionMap
				.get(serviceRequest.getRequestInfo().getUserInfo().getRoles().get(0).getName().toUpperCase());
		log.info("actions: " + actions);
		if (null != actions && !actions.isEmpty()) {
			List<ActionInfo> infos = serviceRequest.getActionInfo();
			if(!CollectionUtils.isEmpty(infos))
			for (int i = 0; i <= infos.size() - 1; i++) {
				ActionInfo info = infos.get(i);
				if (null!=info && null != info.getAction() && !info.getAction().isEmpty() && !actions.contains(info.getAction())) {
					errorMap.put(errorCode, "Invalid Action: " + info.getAction() + " " + "for Role: "
							+ serviceRequest.getRequestInfo().getUserInfo().getRoles().get(0).getName().toUpperCase());
				}
			}
		} else {
			errorMap.put("401", "Invalid Role: "
					+ serviceRequest.getRequestInfo().getUserInfo().getRoles().get(0).getName().toUpperCase());
		}

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
}
