package org.egov.mseva.web.validator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mseva.model.enums.Status;
import org.egov.mseva.service.MDMSService;
import org.egov.mseva.service.MsevaService;
import org.egov.mseva.utils.ErrorConstants;
import org.egov.mseva.utils.MsevaConstants;
import org.egov.mseva.web.contract.Event;
import org.egov.mseva.web.contract.EventRequest;
import org.egov.mseva.web.contract.EventSearchCriteria;
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

	@Autowired
	private MsevaService service;

	public void validateCreateEvent(EventRequest request) {
		log.info("Validating the request......");
		Map<String, String> errorMap = new HashMap<>();
		validateRI(request.getRequestInfo(), errorMap);
		request.getEvents().forEach(event -> {
			validateEventData(request.getRequestInfo(), event, errorMap);
		});
		if (!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}

	}

	public void validateUpdateEvent(EventRequest request) {
		Map<String, String> errorMap = new HashMap<>();
		validateForUpdate(request, errorMap);
		validateCreateEvent(request);
	}

	public void validateSearch(RequestInfo requestInfo, EventSearchCriteria criteria) {
		Map<String, String> errorMap = new HashMap<>();
		validateRI(requestInfo, errorMap);
		if (!requestInfo.getUserInfo().getType().equals("CITIZEN")) {
			if (null != criteria) {
				if (criteria.isEmpty(criteria)) {
					errorMap.put(ErrorConstants.MEN_INVALID_COUNT_CRITERIA_CODE,
							ErrorConstants.MEN_INVALID_COUNT_CRITERIA_MSG);
				}
			} else {
				errorMap.put(ErrorConstants.MEN_INVALID_COUNT_CRITERIA_CODE,
						ErrorConstants.MEN_INVALID_COUNT_CRITERIA_MSG);
			}
		}
		if (!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}
	}

	private void validateForUpdate(EventRequest request, Map<String, String> errorMap) {
		EventSearchCriteria criteria = new EventSearchCriteria();
		List<String> ids = request.getEvents().stream().map(Event::getId).collect(Collectors.toList());
		criteria.setIds(ids);
		List<Event> responseFromDB = service.searchEvents(request.getRequestInfo(), criteria, true).getEvents();
		if (responseFromDB.size() != request.getEvents().size()) {
			errorMap.put(ErrorConstants.MEN_UPDATE_MISSING_EVENTS_CODE, ErrorConstants.MEN_UPDATE_MISSING_EVENTS_MSG);
		}
		for (Event event : request.getEvents()) {
			if (!StringUtils.isEmpty(event.getReferenceId()))
				errorMap.put(ErrorConstants.MEN_UPDATE_COUNTEREVENT_CODE, ErrorConstants.MEN_UPDATE_COUNTEREVENT_MSG);
			if(null == event.getStatus()) {
				errorMap.put(ErrorConstants.MEN_UPDATE_STATUS_NOTNULL_CODE, ErrorConstants.MEN_UPDATE_STATUS_NOTNULL_MSG);
			}
		}
		if (!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}
		validateActions(request.getEvents(), responseFromDB, errorMap);

	}
	
	private void validateActions(List<Event> reqEvents, List<Event> dbEvents, Map<String, String> errorMap) {
		Map<String, Status> mapOfIdAndCurrentState = dbEvents.stream().collect(Collectors.toMap(Event :: getId, Event :: getStatus));
		reqEvents.forEach(event -> {
			if(mapOfIdAndCurrentState.get(event.getId()).equals(Status.CANCELLED)) {
				if(event.getStatus().equals(Status.INACTIVE) || event.getStatus().equals(Status.ACTIVE)) {
					errorMap.put(ErrorConstants.MEN_INVALID_ACTION_CANCEL_CODE, ErrorConstants.MEN_INVALID_ACTION_CANCEL_MSG);
				}else if(event.getStatus().equals(Status.CANCELLED)) {
					event.setGenerateCounterEvent(false);
				}
			}
			if(mapOfIdAndCurrentState.get(event.getId()).equals(Status.INACTIVE)) {
				if(event.getStatus().equals(Status.INACTIVE)) {
					event.setGenerateCounterEvent(false);
				}
				
			}
		});
	}

	private void validateRI(RequestInfo requestInfo, Map<String, String> errorMap) {
		if (null != requestInfo) {
			if ((StringUtils.isEmpty(requestInfo.getUserInfo().getUuid()))
					|| (CollectionUtils.isEmpty(requestInfo.getUserInfo().getRoles()))
					|| (StringUtils.isEmpty(requestInfo.getUserInfo().getTenantId()))) {
				errorMap.put(ErrorConstants.MISSING_ROLE_USERID_CODE, ErrorConstants.MISSING_ROLE_USERID_MSG);
			}
		} else {
			errorMap.put(ErrorConstants.MISSING_REQ_INFO_CODE, ErrorConstants.MISSING_REQ_INFO_MSG);
		}
		if (!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}

	}

	private void validateEventData(RequestInfo requestInfo, Event event, Map<String, String> errorMap) {
		if (null != event.getEventDetails()) {
			if (event.getEventDetails().getFromDate() > event.getEventDetails().getToDate()) {
				errorMap.put(ErrorConstants.INVALID_EVENT_DATE_CODE, ErrorConstants.INVALID_EVENT_DATE_MSG);
			}
			if ((event.getEventDetails().getFromDate() > new Date().getTime())
					|| (event.getEventDetails().getFromDate() > new Date().getTime())) {
				errorMap.put(ErrorConstants.INVALID_FROM_TO_DATE_CODE, ErrorConstants.INVALID_FROM_TO_DATE_MSG);
			}

		}
		if (!CollectionUtils.isEmpty(event.getRecepient().getToRoles())) {
			if(event.getRecepient().getToRoles().contains(MsevaConstants.ALL_KEYWORD) && (event.getRecepient().getToRoles().size() > 1)) {
				if((event.getRecepient().getToRoles().size() > 1) || !CollectionUtils.isEmpty(event.getRecepient().getToUsers())) {
					errorMap.put(ErrorConstants.MEN_INVALID_TOROLE_ALL_CODE, ErrorConstants.MEN_INVALID_TOROLE_ALL_MSG);
				}
			}
			event.getRecepient().getToRoles().forEach(role -> {
				Pattern p = Pattern.compile(MsevaConstants.REGEX_FOR_SPCHARS_EXCEPT_DOT);
				Matcher m = p.matcher(role);
				if (m.find()) {
					errorMap.put(ErrorConstants.MEN_INVALID_TOROLE_CODE, ErrorConstants.MEN_INVALID_TOROLE_MSG);
				}
			});
		}
		
		if (!CollectionUtils.isEmpty(event.getRecepient().getToUsers())) {
			event.getRecepient().getToUsers().forEach(user -> {
				try{
						UUID.fromString(user);
				}catch(Exception e) {
					errorMap.put(ErrorConstants.MEN_INVALID_TOUSER_CODE, ErrorConstants.MEN_INVALID_TOUSER_MSG);
				}
			});
		}
		
		// validateMDMSData(requestInfo, event, errorMap);
	}

	private void validateMDMSData(RequestInfo requestInfo, Event event, Map<String, String> errorMap) {
		List<String> eventTypes = mdmsService.fetchEventTypes(requestInfo, event.getTenantId());
		if (!CollectionUtils.isEmpty(eventTypes)) {
			if (!eventTypes.contains(event.getEventType()))
				errorMap.put(ErrorConstants.MEN_INVALID_EVENTTYPE_CODE, ErrorConstants.MEN_INVALID_EVENTTYPE_MSG);
			else {
				if (event.getEventType().equals(MsevaConstants.MEN_MDMS_EVENTSONGROUND_CODE)) {
					if (null == event.getEventDetails()) {
						errorMap.put(ErrorConstants.MEN_UPDATE_EVENTDETAILS_MANDATORY_CODE,
								ErrorConstants.MEN_UPDATE_EVENTDETAILS_MANDATORY_MSG);
					} else if (event.getEventDetails().isEmpty(event.getEventDetails())) {
						errorMap.put(ErrorConstants.MEN_UPDATE_EVENTDETAILS_MANDATORY_CODE,
								ErrorConstants.MEN_UPDATE_EVENTDETAILS_MANDATORY_MSG);
					}
					if (StringUtils.isEmpty(event.getName())) {
						errorMap.put(ErrorConstants.MEN_CREATE_NAMEMANDATORY_CODE,
								ErrorConstants.MEN_CREATE_NAMEMANDATOR_MSG);
					}
				}

				if (event.getEventType().equals(MsevaConstants.MEN_MDMS_BROADCAST_CODE)) {
					if (null != event.getEventDetails()) {
						errorMap.put(ErrorConstants.MEN_CREATE_BROADCAST_CODE, ErrorConstants.MEN_CREATE_BROADCAST_MSG);
					}
				}

			}
		} else {
			throw new CustomException(ErrorConstants.MEN_NO_DATA_MDMS_CODE, ErrorConstants.MEN_NO_DATA_MDMS_MSG);
		}
	}

}
