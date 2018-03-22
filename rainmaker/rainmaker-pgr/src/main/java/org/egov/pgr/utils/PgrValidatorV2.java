package org.egov.pgr.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.service.GrievanceService;
import org.egov.pgr.v3.contract.Service;
import org.egov.pgr.v3.contract.ServiceRequest;
import org.egov.pgr.v3.contract.ServiceResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PgrValidatorV2 {
	
	@Autowired
	private GrievanceService grievanceService;
	
	public void validateUpdate(ServiceRequest serviceRequest){
		
		Map<String, String> errorMap = new HashMap<>();
		
		ServiceReqSearchCriteria service = ServiceReqSearchCriteria
				.builder().tenantId(serviceRequest.getServices().get(0).getTenantId()).serviceRequestId(serviceRequest
						.getServices().stream().map(Service::getServiceRequestId).collect(Collectors.toList()))
				.build();
		
		Map<String, Service> map = ((ServiceResponse)grievanceService.getServiceRequestsV3(serviceRequest.getRequestInfo(),service)).getServices().stream().collect(Collectors.toMap(Service::getServiceRequestId,Function.identity()));
		
		System.err.println(" the map : "+map);
		List<String> errorList = new ArrayList<>();
		serviceRequest.getServices().forEach( a -> {
			
			if(map.get(a.getServiceRequestId()) == null)
				errorList.add(a.getServiceRequestId());
		});
		
		if(!CollectionUtils.isEmpty(errorList))
		errorMap.put("EG_PGR_UPDATE_SERVICEREQUESTID","request object does not exist for the given id's : "+ errorList);
		
		if(!errorMap.isEmpty()) {
			throw new CustomException(errorMap);
		}
	}

}
