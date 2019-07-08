package org.egov.userevent.utils;

import org.springframework.stereotype.Component;

@Component
public class UserEventsConstants {
	
	public static final String MEN_MDMS_MODULE_CODE = "mseva";	
	public static final String MEN_MDMS_EVENTMASTER_CODE = "EventTypes";
	public static final String MEN_MDMS_EVENTMASTER_FILTER = "$.[?(@.active==true)].code";
	public static final String MEN_MDMS_EVENTMASTER_CODES_JSONPATH = "$.MdmsRes." + MEN_MDMS_MODULE_CODE + "." + MEN_MDMS_EVENTMASTER_CODE;
	
	public static final String MEN_MDMS_EVENTSONGROUND_CODE = "EVENTSONGROUND";	
	public static final String MEN_MDMS_BROADCAST_CODE = "BROADCAST";	
	
	
	public static final String REGEX_FOR_SPCHARS_EXCEPT_DOT = "[$&+,:;=\\\\\\\\?@#|/'<>^()%!-]";	
	public static final String REGEX_FOR_UUID = "[/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/]";
	public static final String ALL_KEYWORD = "All";	


	
	

}
