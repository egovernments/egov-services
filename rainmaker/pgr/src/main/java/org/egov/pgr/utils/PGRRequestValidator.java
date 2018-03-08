package org.egov.pgr.utils;

import java.util.Date;

import org.egov.pgr.contract.ServiceReqRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PGRRequestValidator {
	
	public void validateServiceRequest(ServiceReqRequest serviceReqRequest) {
		
		
	}
	
	public void validateSearch(ServiceReqSearchCriteria criteria) {
		
		
		if(criteria.getStartDate().compareTo(criteria.getEndDate()) > 0)
			throw new CustomException("400","startDate cannot be greater than endDate");
		
		if(criteria.getStartDate()>new Date().getTime() || criteria.getEndDate()>new Date().getTime())
			throw new CustomException("400","startDate or endDate cannot be greater than currentDate");
	}

}
