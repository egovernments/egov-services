package org.egov.egf.voucher.domain.exception;

import java.util.HashMap;
import java.util.Map;

public enum VoucherErrorCode {
    
     AMOUNT_IN_DEBIT_AND_CREDIT("org.egov.service.egf.voucher.amount.in.debit.and.credit",
		  "Positive amount is not allowed in both debit and credit amount of {1} of {0},Any time only one can be greater than 0 ",
		  "Positive amount is not allowed in both debit and credit amount of {1} of {0},Any time only one can be greater than 0"),
    ZERO_AMOUNT_IN_DEBIT_AND_CREDIT("org.egov.service.egf.voucher.amount.in.debit.and.credit",
		  "Zero amount is not allowed in both debit and credit amount of {1} of {0},Any time only one can be greater than 0 ",
		  "Zero amount is not allowed in both debit and credit amount of {1} of {0},Any time only one can be greater than 0");
    
    
        private final String code;
	private final String message;
	private final String description;
	private static final Map<String, Object> errorMap = new HashMap<String, Object>();

	static {
		for (VoucherErrorCode error : VoucherErrorCode.values()) {
			errorMap.put(error.code, error);
		}
		 
	}

	VoucherErrorCode(final String code, final String message, final String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}

	public static Object getError(String code) {
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
