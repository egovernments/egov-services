package org.egov.mseva.utils;

import org.springframework.stereotype.Component;

@Component
public class MsevaConstants {
	
	public static final String MEN_MDMS_MODULE_CODE = "mseva-event-notification";	
	public static final String MEN_MDMS_EVENTMASTER_CODE = "EventTypes";
	public static final String MEN_MDMS_EVENTMASTER_FILTER = "$.code";
	public static final String MEN_MDMS_EVENTMASTER_CODES_JSONPATH = "$.MdmsRes.mseva-event-notification.EventTypes";



}
