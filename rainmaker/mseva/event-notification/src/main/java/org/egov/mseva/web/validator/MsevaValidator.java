package org.egov.mseva.web.validator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mseva.service.MDMSService;
import org.egov.mseva.utils.ErrorConstants;
import org.egov.mseva.web.contract.Event;
import org.egov.mseva.web.contract.EventRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MsevaValidator {
	
	@Autowired
	private MDMSService mdmsService;
	
	public void validateCreateEvent(EventRequest request) {
		log.info("Validating the request......");
		Map<String, String> errorMap = new HashMap<>();
		validateRI(request.getRequestInfo(), errorMap);
		request.getEvents().forEach(event -> {
			validateEventData(request.getRequestInfo(), event, errorMap);
		});
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}
		
	}
	
	public void validateUpdateEvent(EventRequest request) {
		validateCreateEvent(request);
	}
	
	private void validateRI(RequestInfo requestInfo, Map<String, String> errorMap) {
		if(null != requestInfo) {
			if ((null == requestInfo.getUserInfo().getId()) || 
					(CollectionUtils.isEmpty(requestInfo.getUserInfo().getRoles())) || (StringUtils.isEmpty(requestInfo.getUserInfo().getTenantId()))) {
				errorMap.put(ErrorConstants.MISSING_ROLE_USERID_CODE, ErrorConstants.MISSING_ROLE_USERID_MSG);
			}
		}else {
			errorMap.put(ErrorConstants.MISSING_REQ_INFO_CODE, ErrorConstants.MISSING_REQ_INFO_MSG);
		}
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}

	}
	
	private void validateEventData(RequestInfo requestInfo, Event event, Map<String, String> errorMap) {
		if(CollectionUtils.isEmpty(event.getRecepient().getToUsers()) 
				&& CollectionUtils.isEmpty(event.getRecepient().getToRoles())) {
			errorMap.put(ErrorConstants.EMPTY_RECEPIENT_CODE, ErrorConstants.EMPTY_RECEPIENT_MSG);
		}
		if(null != event.getEventDetails()) {
			if(event.getEventDetails().getFromDate() > event.getEventDetails().getToDate()) {
				errorMap.put(ErrorConstants.INVALID_EVENT_DATE_CODE, ErrorConstants.INVALID_EVENT_DATE_MSG);
			}
			if((event.getEventDetails().getFromDate() > new Date().getTime()) || 
								(event.getEventDetails().getFromDate() > new Date().getTime())) {
				errorMap.put(ErrorConstants.INVALID_FROM_TO_DATE_CODE, ErrorConstants.INVALID_FROM_TO_DATE_MSG);
			}
			
		}
		validateMDMSData(requestInfo, event, errorMap);
	}
	
	private void validateMDMSData(RequestInfo requestInfo, Event event, Map<String, String> errorMap) {
		List<String> eventTypes = mdmsService.fetchEventTypes(requestInfo, event.getTenantId());
		if(!CollectionUtils.isEmpty(eventTypes)) {
			if(!eventTypes.contains(event.getEventType()))
				throw new CustomException(ErrorConstants.MEN_INVALID_EVENTTYPE_CODE, ErrorConstants.MEN_INVALID_EVENTTYPE_MSG);
		}else {
			throw new CustomException(ErrorConstants.MEN_NO_DATA_MDMS_CODE, ErrorConstants.MEN_NO_DATA_MDMS_MSG);
		}
	}
	
}
