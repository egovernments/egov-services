package org.egov.works.workorder.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static final String LOA_NUMBER_GENERATION_ERROR = "LOA numebr can not be generated";

    public static final String APPROVE = "Approve";
    public static final String SUBMIT = "Submit";
    public static final String REJECT = "Reject";
    public static final String FORWARD = "Forward";
    public static final String CANCEL = "Cancel";

    public static final String KEY_FUTUREDATE_LOADATE = "INVALID_LOADATE";
    public static final String MESSAGE_FUTUREDATE_LOADATE = "LOA Date cannot be future date";

    public static final String KEY_FUTUREDATE_L1DATE = "INVALID_L1DATE";
    public static final String MESSAGE_FUTUREDATE_L1DATE = "LOA Date cannot be L1 Tendor finalized date";

    public static final String KEY_FUTUREDATE_FILEDATE = "INVALID_FILEDATE";
    public static final String MESSAGE_FUTUREDATE_FILEDATE = "File Date cannot be future date";

}
