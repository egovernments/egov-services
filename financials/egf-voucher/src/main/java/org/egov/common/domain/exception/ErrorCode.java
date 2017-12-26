package org.egov.common.domain.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author mani
 *  List of Domain Error codes with message and detailed description
 *  Donot auto format this file 
 */
public enum ErrorCode {
	
	KAFKA_TIMEOUT_ERROR("org.egov.service.kafka.timeout",
			"time out while waiting for kafka",
			"Some required service is down. Please contact Administrator"),
	NON_UNIQUE_VALUE("org.egov.service.non.unique.value",
			"the field {0} must be unique in the system",
			"The  value  {1} for the field {0} already exists in the system. Please provide different value"),
	NULL_VALUE( "org.egov.service.null.value",
			  "the field {0} must be not be null",
			  "The  value  {1} for the field {0} not allowed in the system. Please provide correct value"),
	MANDATORY_VALUE_MISSING("org.egov.service.mandatory.value.missing",
			  "the field {0} must be not be null or empty",
			  "the field {0} is Mandatory .It cannot be not be null or empty.Please provide correct value"),
	NOT_NULL("org.egov.NotNull",
			  "the field {0} must be not be null",
			  "The  value  {1} for the field {0} not allowed in the system. Please provide correct value"),
	INVALID_REF_VALUE("org.egov.service.invalid.ref.value",
			  "the field {0} should have a valid value which exists in the system. ",
			  "The  value  {1} for the field {0} does exist in system. Please provide correct value"),
	INACTIVE_REF_VALUE("org.egov.service.inactive.ref.value",
		  "the Referenced property {0} should be active to proceed transaction. ",
		  "The  Referenced property  {0} is not active in system. Only active record/data will be used in transaction"),
    
	AMOUNT_IN_DEBIT_AND_CREDIT("org.egov.service.amount.in.debit.and.credit",
		  "Positive amount is not allowed in both debit and credit amount of {1} of {0},Any time only one can be greater than 0 ",
		  "Positive amount is not allowed in both debit and credit amount of {1} of {0},Any time only one can be greater than 0"),
    	ZERO_AMOUNT_IN_DEBIT_AND_CREDIT("org.egov.service.amount.in.debit.and.credit",
		  "Zero amount is not allowed in both debit and credit amount of {1} of {0},Any time only one can be greater than 0 ",
		  "Zero amount is not allowed in both debit and credit amount of {1} of {0},Any time only one can be greater than 0"),
    	SUBLEDGER_DATA_MISSING("org.egov.services.egf.voucher.subledger.data.missing",
    		                "Subledger(ledgerdetails) details are missing for the control account code {0} in {1}",
    		                "When the account code is control code ,the subledger details are mandatory.Subledger(ledgerdetails) details are missing for the control"
    		                + " account code {0} in {1} "), 
    	SUBLEDGER_AMOUNT_MISS_MATCH("org.egov.services.egf.voucher.subledger.amount.miss.match",
	                "Subledger(ledgerdetails) total amount is not matching the control account code {0} in {1}",
	                "When the account code is control code ,the subledger details are mandatory.Subledger(ledgerdetails) details are missing for the control"
	                + " account code {0} in {1} "), 
    	LEDGER_AMOUNT_MISS_MATCH("org.egov.services.egf.voucher.subledger.amount.miss.match",
		                "Ledger total amount is not matching. Sum of debit should be equal to sum of credit ",
		                "Ledger total amount is not matching. Sum of debit should be equal to sum of credit "), 
    	DUPLICATE_ACCOUNT_CODE("org.egov.services.egf.voucher.ledger.duplicate.account.code",
                "Duplicate accountcode with function {0} ",
                "Duplicate accountcode with function {0} ");

	
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
