package org.egov.pgr.utils;

import org.springframework.stereotype.Component;


@Component
public class PGRConstants {
	
	private PGRConstants() {}

	public static final String SERV_REQ_ID_NAME = "pgr.servicerequestid";
	public static final String SERV_REQ_ID_FORMAT = "[cy:dd]/[cy:MM]/[cy:yyyy]/[SEQ_EG_PGR_SERVICEREQUESTID]";
		
	//Notification
	public static final String TEMPLATE_COMPLAINT_EMAIL = "./src/main/resources/email-templates/velocityEmailNotifSample.vm";
	
	
	public static final String SEARCHER_PGR_MOD_NAME = "rainmaker-pgr-V2";
	public static final String SEARCHER_SRSEARCH_DEF_NAME = "serviceSearchWithDetails";
	public static final String SEARCHER_COUNT_DEF_NAME = "count";
	public static final String PG_JSONPATH_COUNT = "$.count[0].count";
	public static final String SEARCHER_SRID_ASSIGNEDTO_DEF_NAME = "getServiceRequestsOnAssignedTo";
	public static final String SRID_ASSIGNEDTO_JSONPATH = "$.servicesRequestIds.*.businesskey";
	public static final String MDMS_PGR_MOD_NAME = "RAINMAKER-PGR";
	public static final String MDMS_SERVICETYPE_MASTER_NAME = "ServiceDefs";
	public static final String MDMS_COMMON_MASTERS_MASTER_NAME = "common-masters";
	public static final String MDMS_DEPT_MASTERS_MODULE_NAME = "Designation";
	public static final String MDMS_DESIGNATION_MASTERS_MODULE_NAME = "Department";

	public static final String SERVICE_CODES = "serviceCode";
	public static final String JSONPATH_SERVICE_CODES = "$.MdmsRes.RAINMAKER-PGR.ServiceDefs";
	public static final String JSONPATH_DEPARTMENTS = "$.MdmsRes.common-masters.Department";
	public static final String JSONPATH_DESIGNATIONS = "$.MdmsRes.common-masters.Designation";

	public static final String SERVICE_NAME = "serviceName";
	public static final String DEFAULT_COMPLAINT_TYPE = "resolution";
	public static final String EMPLOYEE_DEPTCODE_JSONPATH = "$.Employee[0].assignments[0].department";
	public static final String EMPLOYEE_DESGCODE_JSONPATH = "$.Employee[0].assignments[0].designation";
	public static final String EMPLOYEE_NAME_JSONPATH = "$.Employee[0].name";
	public static final String DEPARTMENTNAME_EMPLOYEE_JSONPATH = "$.Department[0].name";
	
	public static final String SEARCHER_RESPONSE_TEXT = "Searcher response : ";
	
	public static final String WEB_APP_FEEDBACK_PAGE_LINK = "http://letmegetthelink.org";
	
	public static final String SMS_NOTIFICATION_STATUS_KEY = "<status>";
	public static final String SMS_NOTIFICATION_COMPLAINT_TYPE_KEY = "<complaint_type>";
	public static final String SMS_NOTIFICATION_DATE_KEY = "<date>";
	public static final String SMS_NOTIFICATION_ID_KEY = "<id>";
	public static final String SMS_NOTIFICATION_EMP_NAME_KEY = "<emp_name>";
	public static final String SMS_NOTIFICATION_EMP_DEPT_KEY = "<emp_department>";
	public static final String SMS_NOTIFICATION_EMP_DESIGNATION_KEY = "<emp_designation>";
	public static final String SMS_NOTIFICATION_COMMENT_KEY = "<comment>";
	public static final String SMS_NOTIFICATION_REASON_FOR_REOPEN_KEY = "<reason>";
	public static final String SMS_NOTIFICATION_ADDITIONAL_COMMENT_KEY = "<additional_comments>";
	public static final String SMS_NOTIFICATION_APP_LINK_KEY = "<app_link>";




	

	
}
