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
import org.egov.pgr.contract.ServiceReq;
import org.egov.pgr.contract.ServiceReqRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.service.ServiceRequestService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PGRRequestValidator {
	
	@Autowired
	private ServiceRequestService requestService ;
	
	public void validateServiceRequest(ServiceReqRequest serviceReqRequest) {
		
		
	}
	
	public void validateUpdate(ServiceReqRequest serviceReqRequest) {
		
		Map<String, String> errorMap = new HashMap<>();
		
		ServiceReqSearchCriteria service = ServiceReqSearchCriteria
				.builder().tenantId(serviceReqRequest.getServiceReq().get(0).getTenantId()).serviceRequestId(serviceReqRequest
						.getServiceReq().stream().map(ServiceReq::getServiceRequestId).collect(Collectors.toList()))
				.build();
		
		Map<String, ServiceReq> map = requestService.getServiceRequests(serviceReqRequest.getRequestInfo(),service).getServiceReq().stream().collect(Collectors.toMap(ServiceReq::getServiceRequestId,Function.identity()));
		
		List<String> errorList = new ArrayList<>();
		serviceReqRequest.getServiceReq().forEach( a -> {
			
			if(map.get(a.getServiceRequestId()) == null)
				errorList.add(a.getServiceRequestId());
		});
		
		if(!CollectionUtils.isEmpty(errorList))
		errorMap.put("EG_PGR_UPDATE_SERVICEREQUESTID","request object does not exist for the given id's : "+ errorList);
		
		if(!errorMap.isEmpty()) {
			throw new CustomException(errorMap);
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
		
		if(requestInfo.getUserInfo().getRoles().get(0).getName().equals("CITIZEN") && requestInfo.getUserInfo().getRoles().size() == 1) {
			if(null != criteria.getAccountId() && !criteria.getAccountId().isEmpty()) {
				if(!(criteria.getAccountId().equals(requestInfo.getUserInfo().getId().toString())))
					errorMap.put("403", "User not authorized to access this information");
			}else {
				errorMap.put("403", "User not authorized, accountId missing");
			}
		}
	}

}