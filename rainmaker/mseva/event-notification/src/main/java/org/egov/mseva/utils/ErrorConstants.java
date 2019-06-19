package org.egov.mseva.utils;

import org.springframework.stereotype.Component;

@Component
public class ErrorConstants {
	
	public static final String MISSING_ROLE_USERID_CODE = "MEN_USERID_ROLECODE_MISSING";
	public static final String MISSING_ROLE_USERID_MSG = "User id, tenantid and rolecode are mandatory in the request info";
	
	public static final String MISSING_REQ_INFO_CODE = "MEN_REQ_INFO_MISSING";
	public static final String MISSING_REQ_INFO_MSG = "RequestInfo is mandatory in the request.";
	
	public static final String EMPTY_RECEPIENT_CODE = "MEN_EMPTY_RECEPIENT";
	public static final String EMPTY_RECEPIENT_MSG = "toRoles and toUsers both cannot be empty, provide atleast one of them. "
			+ "Incase the event is addressed to everyone, send 'All' in toUsers";
	
	public static final String INVALID_EVENT_DATE_CODE = "MEN_INVALID_EVENT_DATE";
	public static final String INVALID_EVENT_DATE_MSG = "Date invalid, fromDate cannot be greater than toDate";
	
	public static final String INVALID_FROM_TO_DATE_CODE = "MEN_INVALID_FROM_TO_DATE";
	public static final String INVALID_FROM_TO_DATE_MSG = "Date invalid, fromDate and toDate cannot be greater than currentDate";

	public static final String MEN_ERROR_FROM_MDMS_CODE = "MEN_ERROR_FROM_MDMS";
	public static final String MEN_ERROR_FROM_MDMS_MSG = "There was an error while fetching event types from MDMS";
	
	public static final String MEN_NO_DATA_MDMS_CODE = "MEN_NO_DATA_MDMS";
	public static final String MEN_NO_DATA_MDMS_MSG = "MDMS data for eventTypes is missing!";
	
	public static final String MEN_INVALID_EVENTTYPE_CODE = "MEN_INVALID_EVENTTYPE";
	public static final String MEN_INVALID_EVENTTYPE_MSG = "The provided eventType is not valid.";
	
	public static final String MEN_UPDATE_MISSING_EVENTS_CODE = "MEN_UPDATE_MISSING_EVENTS";
	public static final String MEN_UPDATE_MISSING_EVENTS_MSG = "The events you're trying update are missing in the system.";
	
	public static final String MEN_INVALID_SEARCH_CRITERIA_CODE = "MEN_INVALID_SEARCH_CRITERIA";
	public static final String MEN_INVALID_SEARCH_CRITERIA_MSG = "Atleast one of the parameters is mandatory for searching events.";
	
	public static final String MEN_INVALID_COUNT_CRITERIA_CODE = "MEN_INVALID_COUNT_CRITERIA";
	public static final String MEN_INVALID_COUNT_CRITERIA_MSG = "Atleast one of the parameters is mandatory for fetching count of unread notifications.";
	
}
