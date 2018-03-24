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
	public static final String SEARCHER_SRHISTORY_DEF_NAME = "history";
	public static final String SEARCHER_PGR_MOD_NAME = "rainmaker-pgr";
	public static final String MDMS_PGR_MOD_NAME = "RAINMAKER-PGR";
	public static final String MDMS_SERVICETYPE_MASTER_NAME = "ServiceDefs";
	public static final String SERVICE_CODES = "serviceCode";
	public static final String JSONPATH_SERVICE_CODES = "$.MdmsRes.RAINMAKER-PGR.ServiceDefs";
	public static final String SERVICE_NAME = "serviceName";
	public static final String DEFAULT_COMPLAINT_TYPE = "resolution";
	
	//Notification
	public static final String TEMPLATE_COMPLAINT_EMAIL = "./src/main/resources/email-templates/velocityEmailNotifSample.vm";
	
	
	
	//V3 Constants
	public static final String V3_MDMS_JSONPATH_SERVICECODES = "$.*.serviceCodes";
	public static final String V3_PG_JSONPATH_COUNT = "$.count[0].count";
	public static final String V3_SEARCHER_SRSEARCH_DEF_NAME = "serviceSearch";
	public static final String V3_SEARCHER_ACTIONSEARCH_GENERAL_DEF_NAME = "actionSearch";
	public static final String V3_SEARCHER_SRSEARCH_ASSIGNEDTO_DEF_NAME = "serviceSearchOnAssignedTo";
	public static final String V3_SEARCHER_COUNT_DEF_NAME = "count";
	public static final String V3_SEARCHER_COUNT_ASSIGNED_DEF_NAME = "countOnAssignedTo";
	public static final String V3_SEARCHER_PGR_MOD_NAME = "rainmaker-pgr-V3";
	public static final String V3_MDMS_PGR_MOD_NAME = "RAINMAKER-PGR";
	public static final String V3_MDMS_SERVICETYPE_MASTER_NAME = "ServiceDefs";
	public static final String V3_SERVICE_CODES = "serviceCode";
	public static final String V3_JSONPATH_SERVICE_CODES = "$.MdmsRes.PGR.ServiceDefs";
	public static final String V3_SERVICE_NAME = "serviceName";
	public static final String V3_DEFAULT_COMPLAINT_TYPE = "resolution";
	public static final String V3_SERVICE_JSONPATH = "$.services";
	public static final String V3_ACTION_JSONPATH = "$.actionHistory";
	public static final String V3_SERVICES_PARENT_JSONPATH = "$.services";
	public static final String V3_RESPONSEINFO_JSONPATH = "$.ResponseInfo";
	public static final String V3_EMPLOYEE_DEPTCODE_JSONPATH = "$.Employee[0].assignments[0].department";
	public static final String V3_DEPARTMENTNAME_EMPLOYEE_JSONPATH = "$.departmentList[0].name";

	
}
