package org.egov.works.workorder.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static final String LOA_NUMBER_GENERATION_ERROR = "LOA numebr can not be generated";
    public static final String WORKORDER_NUMBER_GENERATION_ERROR = "Work order numebr can not be generated";

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
    
    public static final String KEY_INVALID_LOA_DE_EXISTS = "INVALID_LOA_DE_EXISTS";
    public static final String MESSAGE_INVALID_LOA_DE_EXISTS = "LOA with detailed estimate number already exist in the system";
    
    public static final String KEY_INVALID_LOA_WORKORDER = "INVALID_LOA_WORKORDER";
    public static final String MESSAGE_INVALID_LOA_WORKORDER = "Workorder already created for given LOA";

    public static final String KEY_INVALID_LOADATE_DATE = "LOA_DATE_INVALID";
    public static final String MESSAGE_INVALID_LOADATE_DATE= "LOA Date cannot be prior to Detailed Estimate Approved Date.";

    public static final String KEY_LOA_CONRACTOR_REQUIRED = "works.loa.contractor.required";
    public static final String MESSAGE_LOA_CONRACTOR_REQUIRED = "Please provide valid data for contractor";

    public static final String KEY_LOA_CONRACTOR_INACTIVE = "works.loa.contractor.inactive";
    public static final String MESSAGE_LOA_CONRACTOR_INACTIVE = "The contractor is inactive, Please pass the active contractor";

    public static final String KEY_WORKORDER_LOAAMOUNT_INPROPER = "works.loa.loaamount.invalid";
    public static final String MESSAGE_WORKORDER_LOAAMOUNT_INPROPER = "LOA Amount should be equal to total amount of LOA activity";

    public static final String KEY_WORKORDER_LOAAMOUNT_WORKVALUE_INPROPER = "works.loa.loaamount.workvalue.invalid";
    public static final String MESSAGE_WORKORDER_LOAAMOUNT_WORKVALUE_INPROPER = "LOA Amount should be equal to work value of detailed estimate";

    public static final String KEY_WORKORDER_DLP_ZERO = "works.loa.dlp.invalid";
    public static final String MESSAGE_WORKORDER_DLP_ZERO = "Defect liability should be greater then 0";

    public static final String KEY_WORKORDER_CP_ZERO = "works.loa.cp.invalid";
    public static final String MESSAGE_WORKORDER_CP_ZERO = "Contract Period should be greater then 0";

    public static final String KEY_NULL_COUNCILRESOLUTIONDATE = "works.loa.councilresolutiondate.null";
    public static final String MESSAGE_NULL_COUNCILRESOLUTIONDATE = "Council Resolution Date should be entered";
    
    public static final String KEY_NULL_COUNCILRESOLUTIONNUMBER = "works.loa.councilresolutionnumber.null";
    public static final String MESSAGE_NULL_COUNCILRESOLUTIONNUMBER = "Council Resolution Number should be entered";
    
    public static final String KEY_WORKORDER_LOAAMOUNT_LOAACTIVITYAMOUNT_INVALID = "works.loa.loaamount.loaactivityamount.invalid";
    public static final String MESSAGE_WORKORDER_LOAAMOUNT_LOAACTIVITYAMOUNT_INVALID = "Sum of LOA activity amount after aplying Tender Finalized Percentage Should be equal to LOA Amount";
 
    public static final String KEY_OFFLINESTATUS_WORKORDERDATE_INVALID = "works.worksorder.statusdate.invalid";
    public static final String MESSAGE_OFFLINESTATUS_WORKORDERDATE_INVALID = "Workorder Date cannot be less then Agreement Order Signed date";

    public static final String KEY_WORKORDER_REMARKS_EDITABLE = "works.worksorder.remarks.editable";
    public static final String MESSAGE_WORKORDER_REMARKS_EDITABLE = "Remarks is not allowed to modify";

    public static final String KEY_WORKORDER_REMARKS_INVALID_DATA = "works.worksorder.remarks.invalid.data";
    public static final String MESSAGE_WORKORDER_REMARKS_INVALID_DATA = "Please send the proper data for Work Order Details";
    
    public static final String KEY_FUTUREDATE_LOADATE_DETAILEDESTIMATE = "works.offlinestatus.loadate.invalid";
    public static final String MESSAGE_FUTUREDATE_LOADATE_DETAILEDESTIMATE = "LOA Date cannot be prior to detailed estimate approved rate";

}
