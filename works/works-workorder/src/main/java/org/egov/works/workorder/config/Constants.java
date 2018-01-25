package org.egov.works.workorder.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static final String LOA_NUMBER_GENERATION_ERROR = "LOA numebr can not be generated";
    public static final String WORKORDER_NUMBER_GENERATION_ERROR = "Work order numebr can not be generated";
    public static final String NOTICE_NUMBER_GENERATION_ERROR = "Notice numebr can not be generated";

    public static final String APPROVE = "Approve";
    public static final String SUBMIT = "Submit";
    public static final String REJECT = "Reject";
    public static final String FORWARD = "Forward";
    public static final String CANCEL = "Cancel";
    public static final String SAVE = "Save";
    public static final String LETTEROFACCEPTANCE_OBJECT = "LetterOfAcceptance";
    public static final String STATUS_APPROVED = "APPROVED";
    
    public static final String WORKORDER_OFFLINE="WorkOrderOffline";
    public static final String LETTEROFACCEPTANCE_OFFLINE="LetterOfAcceptanceOffline";
    public static final String DETAILEDESTIMATE_OFFLINE="DetailedEstimateOffline";

    public static final String STATUS_MILESTONE_APPROVED = "APPROVED";
    public static final String STATUS_MILESTONE_CANCELLED = "CANCELLED";

    public static final String STATUS_TRACKMILESTONE_NOT_YET_STARTED = "NOT_YET_STARTED";
    public static final String STATUS_TRACKMILESTONE_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_TRACKMILESTONE_COMPLETED = "COMPLETED";

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
    
    public static final String KEY_NOTICE_WO_NOT_APPROVED = "works.notice.workorder.not.approved";
    public static final String MESSAGE_NOTICE_WO_NOT_APPROVED = "Work Order should be in approved status before generating Notices";

    public static final String KEY_MILESTONE_LOA_ESTIMATE_ID_REQUIRED = "WORKS.MILESTONE.LOA.ESTIMATE.ID.REQUIRED";
    public static final String MESSAGE_MILESTONE_LOA_ESTIMATE_ID_REQUIRED = "LOA Estimate id is required";

    public static final String KEY_MILESTONE_LOA_ESTIMATE_ID_INVALID = "WORKS.MILESTONE.LOA.ESTIMATE.ID.INVALID";
    public static final String MESSAGE_MILESTONE_LOA_ESTIMATE_ID_INVALID = "Given LOA Estimate id is invalid:";

    public static final String KEY_MILESTONE_WORKORDER_SHOULD_BE_APPROVED_STATE = "WORKS.MILESTONE.WORKORDER.SHOULD.BE.APPROVED.STATE";
    public static final String MESSAGE_MILESTONE_WORKORDER_SHOULD_BE_APPROVED_STATE = "Work order should be in approved state to create a milestone";

    public static final String KEY_MILESTONE_ACTIVITY_TOTALPERCENTAGE_SHOULDBE_100 = "WORKS.MILESTONE.ACTIVITY.TOTALPERCENTAGE.SHOULDBE.100.CROSS100";
    public static final String MESSAGE_MILESTONE_ACTIVITY_TOTALPERCENTAGE_SHOULDBE_100 = "Milestone Activity total percentage should be 100";

    public static final String KEY_MILESTONE_ACTIVITY_TOTALPERCENTAGE_SHOULDNOT_CROSS100 = "WORKS.MILESTONE.ACTIVITY.TOTALPERCENTAGE.SHOULDNOT.CROSS100";
    public static final String MESSAGE_MILESTONE_ACTIVITY_TOTALPERCENTAGE_SHOULDNOT_CROSS100 = "Milestone Activity total percentage should not cross 100";

    public static final String KEY_MILESTONE_ACTIVITY_SSD_CANNOT_BEFORE_WOD= "WORKS.MILESTONE.ACTIVITY.SSD.CANNOT.BEFORE.WOD";
    public static final String MESSAGE_MILESTONE_ACTIVITY_SSD_CANNOT_BEFORE_WOD = "Milestone schedule start date cannot be before work order approved date";

    public static final String KEY_MILESTONE_ACTIVITY_SED_CANNOT_BEFORE_SSD= "WORKS.MILESTONE.ACTIVITY.SED.CANNOT.BEFORE.SSD";
    public static final String MESSAGE_MILESTONE_ACTIVITY_SED_CANNOT_BEFORE_SSD = "Milestone schedule end date cannot be before schedule start date";

    public static final String KEY_MILESTONE_ACTIVITY_SSD_SED_CANNOTBE_FUTURE= "WORKS.MILESTONE.ACTIVITY.SSD.SED.CANNOTBE.FUTURE";
    public static final String MESSAGE_MILESTONE_ACTIVITY_SSD_SED_CANNOTBE_FUTURE = "Milestone schedule start or end date cannot be future date";

    public static final String KEY_WORKS_LOA_DE_EXISTS = "works.loa.de.created";
    public static final String MESSAGE_WORKS_LOA_DE_EXISTS = "Letter of Acceptance already create for the given Detailed Estimate";
    
    public static final String KEY_TRACKMILESTONE_MILESTONEID_IS_MANDATORY= "WORKS.TRACKMILESTONE.MILESTONEID.IS.MANDATORY";
    public static final String MESSAGE_TRACKMILESTONE_MILESTONEID_IS_MANDATORY = "Milestone is mandatory";

    public static final String KEY_TRACKMILESTONE_MILESTONEID_INVALID= "WORKS.TRACKMILESTONE.MILESTONEID.INVALID";
    public static final String MESSAGE_TRACKMILESTONE_MILESTONEID_INVALID = "Given Milestone is invalid:";

    public static final String KEY_TRACKMILESTONE_COMPLETED= "WORKS.TRACKMILESTONE.COMPLETED";
    public static final String MESSAGE_TRACKMILESTONE_COMPLETED = "This milestone is completed hence total percentage should be 100";

    public static final String KEY_TRACKMILESTONE_NOT_STARTED= "WORKS.TRACKMILESTONE.NOT.STARTED";
    public static final String MESSAGE_TRACKMILESTONE_NOT_STARTED = "This milestone is not yet started hence total percentage cannot be more than zero";

    public static final String KEY_TRACKMILESTONE_IN_PROGRESS= "WORKS.TRACKMILESTONE.IN.PROGRESS";
    public static final String MESSAGE_TRACKMILESTONE_IN_PROGRESS = "This milestone is in progress hence total percentage should be more than zero and less than 100";

    public static final String KEY_TRACKMILESTONE_TENANTID_IS_MANDATORY= "WORKS.TRACKMILESTONE.TENANTID.IS.MANDATORY";
    public static final String MESSAGE_TRACKMILESTONE_TENANTID_IS_MANDATORY = "Track Milestone tenantid is mandatory";

    public static final String KEY_TRACKMILESTONE_ACTIVITY_ASD_AED_CANNOTBE_FUTURE= "WORKS.TRACKMILESTONE.ACTIVITY.ASD.AED.CANNOTBE.FUTURE";
    public static final String MESSAGE_TRACKMILESTONE_ACTIVITY_ASD_AED_CANNOTBE_FUTURE = "Track Milestone actual start or end date cannot be future date";

    public static final String KEY_TRACKMILESTONE_ACTIVITY_AED_CANNOT_BEFORE_ASD= "WORKS.TRACKMILESTONE.ACTIVITY.AED.CANNOT.BEFORE.ASD";
    public static final String MESSAGE_TRACKMILESTONE_ACTIVITY_AED_CANNOT_BEFORE_ASD = "Track Milestone actual end date cannot be before actual start date";

    public static final String KEY_TRACKMILESTONE_MILESTONEACTIVITYID_IS_MANDATORY= "WORKS.TRACKMILESTONE.MILESTONEACTIVITYID.IS.MANDATORY";
    public static final String MESSAGE_TRACKMILESTONE_MILESTONEACTIVITYID_IS_MANDATORY = "Milestone Activity is mandatory";

    public static final String KEY_TRACKMILESTONE_MILESTONEACTIVITYID_INVALID= "WORKS.TRACKMILESTONE.MILESTONEACTIVITYID.INVALID";
    public static final String MESSAGE_TRACKMILESTONE_MILESTONEACTIVITYID_INVALID = "Given Milestone Activity is invalid:";
    
    public static final String KEY_WORKORDER_STATUS_REQUIRED= "WORKORDER_STATUS_REQUIRED";
    public static final String MESSAGE_WORKORDER_STATUS_REQUIRED = "Work order status is required";

    public static final String KEY_WORKORDER_STATUS_INVALID= "WORKORDER_STATUS_INVALID";
    public static final String MESSAGE_WORKORDER_STATUS_INVALID = "Work order status is invalid";
    
    public static final String KEY_LOA_ENGINEERINCHARGE_INVALID= "works.loa.engineerincharge.notexist";
    public static final String MESSAGE_LOA_ENGINEERINCHARGE_INVALID = "Engineer incharge does not exist in system";
    
    public static final String KEY_LOA_ENGINEERINCHARGE_NULL= "works.loa.engineerincharge.null";
    public static final String MESSAGE_LOA_ENGINEERINCHARGE_NULL = "Please pass data for Engineer incharge";

    public static final String KEY_WORKORDER_REMARKS_INVALID= "WORKORDER_REMARKS_INVALID";
    public static final String MESSAGE_WORKORDER_REMARKS_INVALID = "Invalid data for workorder remarks";

    public static final String KEY_NOTICE_REMARKS_INVALID= "NOTICE_REMARKS_INVALID";
    public static final String MESSAGE_NOTICE_REMARKS_INVALID = "Invalid data for notice remarks";
    
    public static final String KEY_CONTRACTORADVANCE_LOA_NULL = "works.contractoradvance.loa.null";
    public static final String MESSAGE_CONTRACTORADVANCE_LOA_NULL = "Please pass the Letter Of Acceptance";
    
    public static final String KEY_CONTRACTORADVANCE_LOA_NOTEXISTS = "works.contractoradvance.loa.notexists";
    public static final String MESSAGE_CONTRACTORADVANCE_LOA_NOTEXISTS = "LOA not exists for given LOA Number";

    public static final String KEY_CONTRACTORADVANCE_EXISTS = "works.contractoradvance.exists";
    public static final String MESSAGE_CONTRACTORADVANCE_EXISTS = "Contractor Advance already exists for given LOA";

    
}
