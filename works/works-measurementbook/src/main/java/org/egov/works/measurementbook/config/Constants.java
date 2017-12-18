package org.egov.works.measurementbook.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

	public static final String APPROVE = "Approve";
	public static final String SUBMIT = "Submit";
	public static final String REJECT = "Reject";
	public static final String FORWARD = "Forward";
	public static final String CANCEL = "Cancel";
	public static final String SAVE = "Save";

	// Error messages
	public static final String KEY_COMMON_ERROR_CODE = "SOMETHING_WENT_WRONG";
	public static final String MESSAGE_RE_COMMON_ERROR_CODE = "Something went wrong while persisting Revision Detailed Estimate";
	public static final String MESSAGE_LOA_COMMON_ERROR_CODE = "Something went wrong while persisting Revision LOA";

	public static final String KEY_INVALID_MB_DATE = "INVALID_MB_DATE";
	public static final String MSG_INVALID_MB_DATE = "Measurement Book date cannot be future date";

	public static final String KEY_MB_LOA_DOES_NOT_EXIST = "MB_LOA_DOES_NOT_EXIST";
	public static final String MSG_MB_LOA_DOES_NOT_EXIST = "Letter Of Acceptance does not exist";

        public static final String KEY_MB_WO_DOES_NOT_EXIST = "MB_WO_DOES_NOT_EXIST";
	public static final String MSG_MB_WO_DOES_NOT_EXIST = "Work Order does not exist";

	public static final String KEY_MB_FROM_TO_PAGE_NUMBER = "MB_FROM_TO_PAGE_NUMBER";
	public static final String MSG_MB_FROM_TO_PAGE_NUMBER = "MB From to should be greater than or equal to from page number";

	public static final String KEY_MB_AMOUNT_LESS_THAN_ZERO = "MB_AMOUNT_LESS_THAN_ZERO";
	public static final String MSG_MB_AMOUNT_LESS_THAN_ZERO = "MB Amount should be greater than zero";

	public static final String KEY_MB_LOA_ESTIMATE_NOT_EXISTS = "MB_LOA_ESTIMATE_NOT_EXISTS";
	public static final String MSG_MB_LOA_ESTIMATE_NOT_EXISTS = "Letter Of Acceptance Estimate does not exist";

	public static final String KEY_MB_SPILLOVER_IF_LEGACY = "MB_SPILLOVER_IF_LEGACY";
	public static final String MSG_MB_SPILLOVER_IF_LEGACY = "MB should be Spillover if it is Legacy";

	public static final String KEY_MB_DETAILS_LOA_ACTIVITY = "MB_DETAILS_LOA_ACTIVITY";
	public static final String MSG_MB_DETAILS_LOA_ACTIVITY = "LOA Activity for MB Detail is mandatory";

	public static final String KEY_MB_DETAILS_QUANTITY_ZERO = "MB_DETAILS_QUANTITY_ZERO";
	public static final String MSG_MB_DETAILS_QUANTITY_ZERO = "MB Details quantity should be greater than zero";

	public static final String KEY_MB_DETAILS_RATE_ZERO = "MB_DETAILS_RATE_ZERO";
	public static final String MSG_MB_DETAILS_RATE_ZERO = "MB Details rate should be greater than zero";

	public static final String KEY_MB_DETAILS_AMOUNT_ZERO = "MB_DETAILS_AMOUNT_ZERO";
	public static final String MSG_MB_DETAILS_AMOUNT_ZERO = "MB Details amount should be greater than zero";

	public static final String KEY_MB_DETAILS_PART_REDUCED_RATE = "MB_DETAILS_PART_REDUCED_RATE";
	public static final String MSG_MB_DETAILS_PART_REDUCED_RATE = "Both Part and Reduced rates can not be present for MB Details";

	public static final String KEY_MB_DETAILS_MANDATORY = "MB_DETAILS_MANDATORY";
	public static final String MSG_MB_DETAILS_MANDATORY = "MB Details are mandatory";

	public static final String KEY_MB_DATE_LOA_WO = "MB_DATE_LOA_WO";
	public static final String MSG_MB_DATE_LOA_WO = "MB Date should be greater than LOA and WO Date";

	public static final String KEY_MB_DATE_ISSUE_DATE = "MB_DATE_ISSUE_DATE";
	public static final String MSG_MB_DATE_ISSUE_DATE = "MB Date Issue date can not be greater than MB Date";

	public static final String KEY_MB_IN_WORKFLOW = "MB_IN_WORKFLOW";
	public static final String MSG_MB_IN_WORKFLOW = "MB is in workflow please approve before creating new MB";

	public static final String KEY_MB_DATE_PREVIOUS_DATE = "MB_DATE_PREVIOUS_DATE";
	public static final String MSG_MB_DATE_PREVIOUS_DATE = "MB Date should be greater than previous MB Dates";

	public static final String KEY_MB_AMOUNT_SUM_DETAILS = "MB_AMOUNT_SUM_DETAILS";
	public static final String MSG_MB_AMOUNT_SUM_DETAILS = "MB amount should be equal to Sum of MB details amount after applying tender finalized percentage";

	public static final String KEY_MB_WO_WORK_COMMENCED = "MB_WO_WORK_COMMENCED";
	public static final String MSG_MB_WO_WORK_COMMENCED = "Work Order should be in Work commenced status to create MB";

	public static final String KEY_MB_DATE_WORK_COMMENCED_DATE = "MB_DATE_WORK_COMMENCED_DATE";
	public static final String MSG_MB_DATE_WORK_COMMENCED_DATE = "MB Date should be greater than Work Commenced date";

	public static final String KEY_MB_MEASUREMENTS_MANDATORY_IF_LOA_MEASUREMENTS = "MB_MEASUREMENTS_MANDATORY_IF_LOA_MEASUREMENTS";
	public static final String MSG_MB_MEASUREMENTS_MANDATORY_IF_LOA_MEASUREMENTS = "Measurements are mandatory if Loa measurements present";

	public static final String KEY_MB_MEASUREMENTS_QUANTITY_NOT_EQUAL_DETAIL = "MB_MEASUREMENTS_QUANTITY_NOT_EQUAL_DETAIL";
	public static final String MSG_MB_MEASUREMENTS_QUANTITY_NOT_EQUAL_DETAIL = "Sum (based on identifier Addition(A) and Deletion(D)) of msheet quantity and quantity from MB detail activity should be same";
	
	public static final String KEY_MB_MEASUREMENTS_LOA_NOT_VALID = "MB_MEASUREMENTS_LOA_NOT_VALID";
	public static final String MSG_MB_MEASUREMENTS_LOA_NOT_VALID = "In MB Msheet, reference to LOA msheet should be valid";

}
