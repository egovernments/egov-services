package org.egov.common.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author mani
 *  List of Domain Error codes with message and detailed description
 *  Donot auto format this file 
 */
public enum ErrorCode {
	
	KAFKA_TIMEOUT_ERROR(
			"org.egov.service.kafka.timeout",
			"time out while waiting for kafka",
			"Some required service is down. Please contact Administrator"), 
	INTERNAL_SERVER_ERROR(
			"org.egov.service.internal.error",
			"Internal Server error",
			"Some required service is down. Please contact Administrator"),
	NON_UNIQUE_VALUE(
					"non.unique.value",
					"the field {0} must be unique in the system",
					"The  value  {1} for the field {0} already exists in the system. Please provide different value"),
	REPEATED_VALUE(
			"repeated.value",
			"the field {0} must be unique ",
			"The  value  {1} for the field {0} {2}  repeated"),
	NULL_VALUE("null.value",
			  "the field {0} must be not be null",
			  "The  value  {1} for the field {0} not allowed in the system. Please provide correct value"),
	MANDATORY_VALUE_MISSING("mandatory.value.missing",
			  "the field {0} must be not be null or empty",
			  "the field {0} is Mandatory .It cannot be not be null or empty.Please provide correct value"),
	NOT_NULL("NotNull",
			  "the field {0} must be not be null",
			  "The  value  {1} for the field {0} not allowed in the system. Please provide correct value"),
	INVALID_REF_VALUE("invalid.ref.value",
			  "the field {0} should have a valid value which exists in the system. ",
			  "The  value  {1} for the field {0} does exist in system. Please provide correct value"), 
	DATE_LE_CURRENTDATE("date.should.be.le.currentdate",
			  "the field {0} should be Less than or Equal to Current Date. ",
			  "The  value  {1} for the field {0} should be Less than or Equal to Current Date."), 
	DATE_GE_CURRENTDATE("date.should.be.ge.currentdate",
					  "the field {0} should be Greater than or Equal to Current Date. ",
					  "The  value  {1} for the field {0} should be Greater than or Equal to Current Date."),
	DATE1_LE_DATE2("date1.should.be.le.date2",
			  "the date {0} should be less than or Equal to {1} Date. ",
			  "The  value  {1} for the field {0} should be Greater than or Equal to Current Date."),
	DATE1_GT_DATE2("date2.should.be.gt.date1",
			  "the date {0} should be greater than {1} Date. ",
			  "The  value  {2} for the field {0} should be Greater than value  {3} for the field {1} ."),
	DATE1_GE_DATE2("date2.should.be.ge.date1",
			  "the date {0} should be greater than {1} Date. ",
			  "The  value  {2} for the field {0} should be Greater than value  {3} for the field {1} ."),
	MANDATORY_BASED_ON("value2.manadatory.if.value1",
			  "the field {0} is mandatory if  {1}  is selected. ",
			  "The field {0} should be provided if the {1} is selected {2}."),
	FIN_YEAR_NOT_EXIST("fin.year.not.exist",
			  "Financial Year does not exist for the date {0} ",
			  "Financial Year does not exist for the date {0}."),
	CITY_CODE_NOT_AVAILABLE("city.code.not.available",
			  "City code is not available for tenant {0} ",
			  "City code is not available for tenant  {0}.")  
	; 

	
	private final String code;
	private final String message;
	private final String description;
	private static final Map<String, ErrorCode> errorMap = new HashMap<String, ErrorCode>();

	static {
		for (ErrorCode error : ErrorCode.values()) {
			errorMap.put(error.code, error);
		}
	}

	ErrorCode(final String code, final String message, final String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}

	public static ErrorCode getError(String code) {
		return errorMap.get(code);
	}

	// add getters and setters here:
	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public String getDescription() {
		return this.description;
	}

}
