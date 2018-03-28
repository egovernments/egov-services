package org.egov.pgr.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.egov.common.contract.request.RequestInfo;
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

		Map<String, String> errorMap = new HashMap<>();
		userInfoCheck(serviceRequest, errorMap);

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	public void validateUpdate(ServiceRequest serviceRequest) {

		Map<String, String> errorMap = new HashMap<>();
		
		userInfoCheck(serviceRequest, errorMap);

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
		
		ServiceReqSearchCriteria service = ServiceReqSearchCriteria.builder()
				.tenantId(serviceRequest.getServices().get(0).getTenantId()).serviceRequestId(serviceRequest
						.getServices().stream().map(Service::getServiceRequestId).collect(Collectors.toList()))
				.build();

		Map<String, Service> map = ((ServiceResponse) requestService
				.getServiceRequests(serviceRequest.getRequestInfo(), service)).getServices().stream()
						.collect(Collectors.toMap(Service::getServiceRequestId, Function.identity()));

		List<String> errorList = new ArrayList<>();
		serviceRequest.getServices().forEach(a -> {

			if (map.get(a.getServiceRequestId()) == null)
				errorList.add(a.getServiceRequestId());
		});

		if (!CollectionUtils.isEmpty(errorList))
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
	}

	public void validateSearch(ServiceReqSearchCriteria criteria, RequestInfo requestInfo) {
		log.info("Validating search request...");
		Map<String, String> errorMap = new HashMap<>();
		validateUserRBACProxy(errorMap, criteria, requestInfo);
		if((criteria.getStartDate()!=null && criteria.getStartDate()>new Date().getTime()) || (criteria.getEndDate()!=null && criteria.getEndDate()>new Date().getTime())) {
			errorMap.put("400", "startDate or endDate cannot be greater than currentDate");
			throw new CustomException("400","startDate or endDate cannot be greater than currentDate");
		}
		if ((criteria.getStartDate() != null && criteria.getEndDate() != null)
				&& criteria.getStartDate().compareTo(criteria.getEndDate()) > 0) {
			errorMap.put("400", "startDate cannot be greater than endDate");
		}
		
		if(!errorMap.isEmpty())
			throw new CustomException(errorMap);
		
		log.info("All Validations passed!");
	}
	
	public void validateUserRBACProxy(Map<String, String> errorMap, ServiceReqSearchCriteria criteria, RequestInfo requestInfo) {
		
		if(null != requestInfo.getUserInfo()) {
			if(null == requestInfo.getUserInfo().getId() || 
					(null == requestInfo.getUserInfo().getRoles() || requestInfo.getUserInfo().getRoles().isEmpty())) {
				errorMap.put("401","Unauthenticated user, userId and Roles missing in the request.");
				return;
			}
		}else {
			errorMap.put("401","Unauthenticated user, userInfo missing in the request.");
			return;
		}
		
	/*	if(requestInfo.getUserInfo().getRoles().get(0).getName().equals("CITIZEN") && requestInfo.getUserInfo().getRoles().size() == 1) {
			if(null != criteria.getAccountId() && !criteria.getAccountId().isEmpty()) {
				if(!(criteria.getAccountId().equals(requestInfo.getUserInfo().getId().toString())))
					errorMap.put("403", "User not authorized to access this information");
			}else {
				errorMap.put("403", "User not authorized, accountId missing");
			}
		}
*/		
	/*	if(requestInfo.getUserInfo().getRoles().get(0).getName().equals("DGRO") && requestInfo.getUserInfo().getRoles().size() == 1) {
			if(null == criteria.getGroup() || criteria.getGroup().isEmpty())
				errorMap.put("400", "Department/group of the DGRO is mandatory");
		}*/

    }
}