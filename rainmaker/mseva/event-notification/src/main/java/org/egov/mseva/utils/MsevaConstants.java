package org.egov.mseva.utils;

import org.springframework.stereotype.Component;

@Component
public class MsevaConstants {
	
	public static final String MEN_MDMS_MODULE_CODE = "mseva";	
	public static final String MEN_MDMS_EVENTMASTER_CODE = "EventTypes";
	public static final String MEN_MDMS_EVENTMASTER_FILTER = "$.[?(@.active==true)].code";
	public static final String MEN_MDMS_EVENTMASTER_CODES_JSONPATH = "$.MdmsRes." + MEN_MDMS_MODULE_CODE + "." + MEN_MDMS_EVENTMASTER_CODE;



}
