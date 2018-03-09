package org.egov.pgr.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
		
		serviceReqRequest.getServiceReq().forEach( a -> {
			
			if(map.get(a.getServiceRequestId()) == null)
				errorMap.put("EG_PGR_UPDATE ","request object does not exist for given id : "+a.getServiceRequestId());
		});
		
		if(!errorMap.isEmpty()) {
			throw new CustomException(errorMap);
		}
	}
	
	public void validateSearch(ServiceReqSearchCriteria criteria) {
		
		if((criteria.getStartDate()!=null && criteria.getStartDate()>new Date().getTime()) || (criteria.getEndDate()!=null && criteria.getEndDate()>new Date().getTime()))
			throw new CustomException("400","startDate or endDate cannot be greater than currentDate");
		
		if ((criteria.getStartDate() != null && criteria.getEndDate() != null)
				&& criteria.getStartDate().compareTo(criteria.getEndDate()) > 0)
			throw new CustomException("400", "startDate cannot be greater than endDate");
	}

}