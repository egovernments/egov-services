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
    
    public static final String KEY_DETAILEDESTIMATE_STATUS = "INVALID_DETAILEDESTIMATE_STATUS";
    public static final String MESSAGE_DETAILEDESTIMATE_STATUS = "Detailed Estimate status should be Technical sanctioned";

    public static final String KEY_DETAILEDESTIMATE_OFFLINE_STATUS = "INVALID_DETAILEDESTIMATE_OFFLINE_STATUS";
    public static final String MESSAGE_DETAILEDESTIMATE_OFFLINE_STATUS = "Offline Status for Detailed Estimate should be L1 TENDER FINALIZED";
    
    public static final String KEY_FUTUREDATE_LOADATE_OFFLINESTATUS = "INVALID_LOADATE_OFFLINESTATUS";
    public static final String MESSAGE_FUTUREDATE_LOADATE_OFFLINESTATUS = "LOA Date cannot greater then L1 TENDER FINALIZED date";
    
    public static final String KEY_DETAILEDESTIMATE_EXIST = "INVALID_DETAILEDESTIMATE_EXIST";
    public static final String MESSAGE_DETAILEDESTIMATE_EXIST = "Please provide valid detailed estimate";
    
    public static final String KEY_WORKORDER_LOANUMBER_REQUIRED= "INVALID_WORKORDER_LOANUMBER";
    public static final String MESSAGE_WORKORDER_LOANUMBER_REQUIRED= "LOA Number is required when Detailed Estimate is spillover";

    public static final String KEY_INVALID_LOANUMBER = "INVALID_LOANUMBER";
    public static final String MESSAGE_INVALID_LOANUMBER = "Please enter a valid LOA number";

    public static final String KEY_INVALID_WORKORDERDATE = "INVALID_WORKORDERDATE";
    public static final String MESSAGE_INVALID_WORKORDERDATE = "Workorder Date cannot be greater then LOA date";

    public static final String KEY_LOA_OFFLINE_STATUS = "INVALID_LOA_OFFLINE_STATUS";
    public static final String MESSAGE_LOA_OFFLINE_STATUS = "Offline Status for LOA should be Agreement Order Signed";

    public static final String KEY_INVALID_LOA = "INVALID_LOA";
    public static final String MESSAGE_INVALID_LOA = "LOA does not exist with given data";

    public static final String KEY_INVALID_LOA_EXISTS = "INVALID_LOA_EXISTS";
    public static final String MESSAGE_INVALID_LOA_EXISTS = "LOA with LOA Number already exist in the system";

    public static final String KEY_INVALID_WORKORDER = "INVALID_WORKORDER";
    public static final String MESSAGE_INVALID_WORKORDER = "Workorder does not exist with given data";

    public static final String KEY_INVALID_WORKORDER_EXISTS = "INVALID_WORKORDER_EXISTS";
    public static final String MESSAGE_INVALID_WORKORDER_EXISTS = "Workorder with work order Number already exist in the system";

    public static final String KEY_WORKORDER_WORKORDERNUMBER_REQUIRED= "INVALID_WORKORDER_WORKORDER";
    public static final String MESSAGE_WORKORDER_WORKORDERNUMBER_REQUIRED= "Work order number is required when LOA is spillover";




}
