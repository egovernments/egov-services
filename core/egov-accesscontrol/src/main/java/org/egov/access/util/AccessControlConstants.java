package org.egov.access.util;

public class AccessControlConstants {

	public static final String INVALID_ACTION_REQUEST_MESSAGE = "Action is invalid";
	
	
	public static final String TENANTID_MANDATORY_CODE = "accesscontrol.0001";
	public static final String TENANTID_MANADATORY_FIELD_NAME = "tenantId";
	public static final String TENANTID_MANADATORY_ERROR_MESSAGE = "Tenant Id is required";
	
	public static final String ACTION_NAME_MANDATORY_CODE = "accesscontrol.0002";
	public static final String ACTION_NAME_MANADATORY_FIELD_NAME = "name";
	public static final String ACTION_NAME_MANADATORY_ERROR_MESSAGE = "Action Name is required";
	
	public static final String ACTION_NAME_DUPLICATE_CODE = "accesscontrol.0003";
	public static final String ACTION_NAME_DUPLICATEFIELD_NAME = "name";
	public static final String ACTION_NAME_DUPLICATE_ERROR_MESSAGE = "Action Name Already exist.";
	
	public static final String ACTION_NAME_INVALID_CODE = "accesscontrol.0002";
	public static final String ACTION_NAME_INVALID_FIELD_NAME = "name";
	public static final String ACTION_NAME_INVALID_ERROR_MESSAGE = "Action Name is required";

	public static final String ACTION_NAME_DOESNOT_EXIT_CODE = "accesscontrol.0002";
	public static final String ACTION_NAME_DOESNOT_EXIT_FIELD_NAME = "name";
	public static final String ACTION_NAME_DOESNOT_EXIT_ERROR_MESSAGE = "Action Name Does Not Exit";
	
	
	public static final String ACTION_URL_QUERYPARAMS_UNIQUE_CODE = "accesscontrol.0002";
	public static final String ACTION_URL_QUERYPARAMS_UNIQUE_ERROR_MESSAGE = "url and queryParams";
	public static final String ACTION_URL_QUERYPARAMS_UNIQUE_FIELD_NAME = "Action url And queryParams combination already Exist";
	
	public static final String ROLE_NAME_MANDATORY_CODE = "accesscontrol.0002";
	public static final String ROLE_NAME_MANADATORY_FIELD_NAME = "name";
	public static final String ROLE_NAME_MANADATORY_ERROR_MESSAGE = "Role Name is required";
	
	
	public static final String ROLE_NAME_DUPLICATE_CODE = "accesscontrol.0003";
	public static final String ROLE_NAME_DUPLICATEFIELD_NAME = "name";
	public static final String ROLE_NAME_DUPLICATE_ERROR_MESSAGE = "Role Name Already exist.";
}
