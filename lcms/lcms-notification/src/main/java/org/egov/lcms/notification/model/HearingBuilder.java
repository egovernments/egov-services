package org.egov.lcms.notification.model;

public class HearingBuilder {
	
	public static final String GET_CASE_CODES_BY_CASE_STATUS = "select casecode from egov_lcms_hearing_details where caseStatus->>'code'=?";

}
