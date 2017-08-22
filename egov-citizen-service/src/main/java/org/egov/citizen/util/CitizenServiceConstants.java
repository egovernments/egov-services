package org.egov.citizen.util;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CitizenServiceConstants {
	
	public static final String BILL_INVALID_MSG = "Bill couln't be fetched";
	public static final String BILL_INVALID_DESC = "Billing service didn't return generated bill for the payment";
	
	public static final String RCPT_INVALID_MSG = "Receipt couldn't be created";
	public static final String RCPT_INVALID_DESC = "Receipt creation failed at the collection service, receipt couldn't be returned";
	
	public static final String FAIL_STATUS_MSG = "Transaction failed";
	public static final String FAIL_STATUS_DESC = "Payment gateway returned a failed status for your transaction, "
					+ "receipt will not be generated";


}
