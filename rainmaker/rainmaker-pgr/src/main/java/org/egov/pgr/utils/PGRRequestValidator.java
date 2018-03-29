package org.egov.pgr.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.pgr.contract.Service;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceRequest;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.service.GrievanceService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@Slf4j
public class PGRRequestValidator {

	@Autowired
	private GrievanceService requestService;

	public void validateCreate(ServiceRequest serviceRequest) {
		
		List<String> serviceCodeList = Arrays.asList("ADDGC","AOS","AC","DC","MC","BG","BPS","BB","BMW");
		
		Map<String, String> errorMap = new HashMap<>();
		userInfoCheck(serviceRequest, errorMap);
		employeeCreateCheck(serviceRequest.getRequestInfo(),errorMap);
		overRideCitizenAccountId(serviceRequest);
		vaidateServiceCodes(serviceRequest, errorMap, serviceCodeList);
		
	}

	private void overRideCitizenAccountId(ServiceRequest serviceRequest) {

		User user = serviceRequest.getRequestInfo().getUserInfo();
		boolean isUserCitizen = user.getRoles().parallelStream().map(Role::getName).collect(Collectors.toList())
				.contains("CITIZEN");
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
		overRideCitizenAccountId(serviceRequest); // TODO remove the accid field from persiter and remove this method
													// following that action FIXME

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

	public void userInfoCheck(ServiceRequest serviceRequest, Map<String, String> errorMap) {

		if (null == serviceRequest.getRequestInfo()) {
			errorMap.put("EG_PGR_REQUESTINFO", "Request info is mandatory for serviceRequest");
			return;
		}

		if (null == serviceRequest.getRequestInfo().getUserInfo()) {
			errorMap.put("EG_PGR_REQUESTINFO_USERINFO", "UserInfo info is mandatory for serviceRequest");
			return;
		}

		if (CollectionUtils.isEmpty(serviceRequest.getRequestInfo().getUserInfo().getRoles())) {
			errorMap.put("EG_PGR_REQUESTINFO_USERINFO_ROLES", "Roles cannot be empty for serviceRequest");
			return;
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
		validateUserRBACProxy(errorMap, criteria, requestInfo);
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

	public void validateUserRBACProxy(Map<String, String> errorMap, ServiceReqSearchCriteria criteria,
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
}