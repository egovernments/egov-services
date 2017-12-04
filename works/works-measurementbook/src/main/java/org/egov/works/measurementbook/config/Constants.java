package org.egov.works.measurementbook.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static final String APPROVE = "Approve";
    public static final String SUBMIT = "Submit";
    public static final String REJECT = "Reject";
    public static final String FORWARD = "Forward";
    public static final String CANCEL = "Cancel";

    //Error messages
    public static final String KEY_COMMON_ERROR_CODE = "SOMETHING_WENT_WRONG";
    public static final String MESSAGE_RE_COMMON_ERROR_CODE = "Something went wrong while persisting Revision Detailed Estimate";
    public static final String MESSAGE_LOA_COMMON_ERROR_CODE = "Something went wrong while persisting Revision LOA";
}
