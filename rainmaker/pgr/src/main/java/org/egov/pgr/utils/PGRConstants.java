package org.egov.pgr.utils;

import org.springframework.stereotype.Component;


@Component
public class PGRConstants {
	
	private PGRConstants() {}

	public static final String SERV_REQ_ID_NAME = "pgr.servicerequestid";
	public static final String SERV_REQ_ID_FORMAT = "[cy:dd]/[cy:MM]/[cy:yyyy]/[SEQ_EG_PGR_SERVICEREQUESTID]";
	

	public static final String MDMS_JSONPATH_SERVICECODES = "$.*.serviceCodes";
	public static final String PG_JSONPATH_COUNT = "$.count[0].count";
	public static final String SEARCHER_SRSEARCH_DEF_NAME = "serviceRequestSearchNew";
	public static final String SEARCHER_COUNT_DEF_NAME = "count";
	public static final String SEARCHER_PGR_MOD_NAME = "rainmaker-pgr";
	public static final String MDMS_PGR_MOD_NAME = "rainmaker-pgr";
	public static final String MDMS_SERVICETYPE_MASTER_NAME = "serviceDefinitions";
	public static final String SERVICE_CODES = "serviceCode";
	
	//Notification
	public static final String TEMPLATE_COMPLAINT_EMAIL = "./src/main/resources/email-templates/velocityEmailNotifSample.vm";

}
