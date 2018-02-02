package org.egov.works.estimate.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static final String ABSTRACT_ESTIMATE_NUMBER_GENERATION_ERROR = "Abstract estimate numebr can not be generated";
    public static final String DETAILED_ESTIMATE_NUMBER_GENERATION_ERROR = "Detailed estimate numebr can not be generated";
    public static final String WORK_IDENTIFICATION_NUMBER_GENERATION_ERROR = "Work identification numebr can not be generated";
    public static final String TECHNICAL_SANCTION_NUMBER_GENERATION_ERROR = "Technical Sanction numebr can not be generated";

    public static final String TYPEOFWORK_OBJECT = "TypeOfWork";
    public static final String FUND_OBJECT = "Fund";
    public static final String FUNCTION_OBJECT = "Function";
    public static final String SUBTYPEOFWORK_OBJECT = "SubTypeOfWork";
    public static final String SCHEME_OBJECT = "Scheme";
    public static final String SUBSCHEME_OBJECT = "SubScheme";
    public static final String BUDGETGROUP_OBJECT = "BudgetGroup";
    public static final String DEPARTMENT_OBJECT = "Department";
    public static final String WARD_OBJECT = "Ward";
    public static final String LOCALITY_OBJECT = "Locality";
    public static final String EGF_MODULE_CODE = "egf-master";
    public static final String OVERHEAD_OBJECT = "Overhead";
    public static final String UOM_OBJECT = "Uom";
    public static final String NATUREOFWORK_OBJECT = "NatureOfWork";
    public static final String REFERENCETYPE_OBJECT = "ReferenceType";
    public static final String MODEOFALLOTMENT_OBJECT = "ModeOfAllotment";
    public static final String ABSTRACT_ESTIMATE_REQUIRED_APPCONFIG = "Abstract_Estimate_Required";
    public static final String PROJECTCODE_OBJECT = "ProjectCode";
    public static final String DETAILEDESTIMATE_OFFLINESTATUS = "L1_TENDER_FINALIZED";
    public static final String DETAILEDESTIMATE_OFFLINE = "DetailedEstimateOffline";

    public static final String BOUNDARY_OBJECT = "Boundary";
    public static final String GIS_INTEGRATION_APPCONFIG = "GIS_INTEGRATION";
    public static final String APPCONFIG_EXCEPTIONALUOMS = "EXCEPTIONALUOMS";
    
    public static final String APPROVE = "Approve";
    public static final String SUBMIT = "Submit";
    public static final String REJECT = "Reject";
    public static final String FORWARD = "Forward";
    public static final String CANCEL = "Cancel";

    //Error messages
    public static final String KEY_ESIMATE_OVERHEAD_CODE = "ESIMATE OVERHEAD_CODE";
    public static final String MESSAGE_ESIMATE_OVERHEAD_CODE = "Overhead code is reqired for estimate";

    public static final String KEY_ESIMATE_OVERHEAD_AMOUNT = "ESIMATE_OVERHEAD_AMOUNT";
    public static final String MESSAGE_ESIMATE_OVERHEAD_AMOUNT = "Overhead amount is required for estimate";

    public static final String KEY_ESIMATE_OVERHEAD_WORKVALUE_AMOUNT = "ESIMATE_OVERHEAD_WORKVALUE_AMOUNT";
    public static final String MESSAGE_ESIMATE_OVERHEAD_WORKVALUE_AMOUNT = "Sum of workvalue and overhead amount should be equal to estimate amount";

    public static final String KEY_ESIMATE_OVERHEAD_UNIQUE = "ESIMATE_OVERHEAD_UNIQUE";
    public static final String MESSAGE_ESIMATE_OVERHEAD_UNIQUE = "Duplicate overheads for estimate";
    
    public static final String KEY_NULL_ABSTRACTESTIMATE_NUMBER = "INVALID_ABSTRACTESTIMATE_NUMBER";
    public static final String MESSAGE_NULL_ABSTRACTESTIMATE_NUMBER = "Abstract Estimate Number should be entered";
    
    public static final String KEY_NULL_WIN_NUMBER = "INVALID_WORK_IDENTIFICATION_NUMBER";
    public static final String MESSAGE_NULL_WIN_NUMBER = "Work Identification Number should be entered";

    public static final String KEY_NULL_DETAILEDESTIMATE_NUMBER = "NULL_DETAILEDESTIMATE_NUMBER";
    public static final String MESSAGE_NULL_DETAILEDESTIMATE_NUMBER = "Detailed Estimate Number should be entered";

    public static final String KEY_INVALID_ABSTRACTESTIMATE_DETAILS = "INVALID_ABSTRACTESTIMATE_DETAILS";
    public static final String MESSAGE_INVALID_ABSTRACTESTIMATE_DETAILS = "Invalid data for abstractEstimate details";

    public static final String KEY_NULL_DETAILEDESTIMATE_DATE = "NULL_DETAILEDESTIMATE_DATE";
    public static final String MESSAGE_NULL_DETAILEDESTIMATE_DATE = "Detailed Estimate date should be entered";

    public static final String KEY_NULL_DATEOFPROPOSAL = "INVALID_DATEOFPROPOSAL";
    public static final String MESSAGE_NULL_DATEOFPROPOSAL = "Date of proposal should be entered";

    public static final String KEY_FUTUREDATE_DATEOFPROPOSAL = "INVALID_DATEOFPROPOSAL";
    public static final String MESSAGE_FUTUREDATE_DATEOFPROPOSAL = "Date of proposal cannot be future date";
    
    public static final String KEY_FUTUREDATE_ADMINSANCTIONDATE = "INVALID_ADMINSANCTIONDATE";
    public static final String MESSAGE_FUTUREDATE_ADMINSANCTIONDATE = "Admin Sanction Date cannot be future date";
    
    public static final String KEY_ADMINSANCTION_PROPOSAL_DATE = "INVALID_ADMINSANCTION_PROPOSAL_DATE";
    public static final String MESSAGE_ADMINSANCTION_PROPOSAL_DATE = "Admin sanction date should be on or after the Date of Proposal";

    public static final String KEY_FUTUREDATE_ESTIMATEDATE = "INVALID_ESTIMATE_DATE";
    public static final String MESSAGE_FUTUREDATE_ESTIMATEDATE = "Estimate Date cannot be future date";

    public static final String KEY_WORK_VALUE_INVALID = "WORK_VALUE_INVALID";
    public static final String MESSAGE_WORK_VALUE_INVALID = "Work value should be greater than zero";

    public static final String KEY_ESTIMATE_VALUE_INVALID = "ESTIMATE_VALUE_INVALID";
    public static final String MESSAGE_ESTIMATE_VALUE_INVALID = "Estimate value should be greater than zero";

    public static final String KEY_WORK_VALUE_GREATERTHAN_ESTIMATE_VALUE = "WORK_VALUE_GREATERTHAN_ESTIMATE_VALUE";
    public static final String MESSAGE_WORK_VALUE_GREATERTHAN_ESTIMATE_VALUE = "Work value should not be greater estimate value";

    public static final String KEY_ESTIMATE_DEDUCTIONS_CHARTOFACCOUNTS_INVALID = "ESTIMATE_DEDUCTIONS_CHARTOFACCOUNTS_INVALID";
    public static final String MESSAGE_ESTIMATE_DEDUCTIONS_CHARTOFACCOUNTS_INVALID = "Chart of accounts is required for an estimate";

    public static final String KEY_ESTIMATE_DEDUCTIONS_AMOUNT_REQUIRED= "ESTIMATE_DEDUCTIONS_AMOUNT_REQUIRED";
    public static final String MESSAGE_ESTIMATE_DEDUCTIONS_AMOUNT_REQUIRED = "Estimate deduction amount is required";

    public static final String KEY_ESTIMATE_DEDUCTIONS_AMOUNT_INVALID= "ESTIMATE_DEDUCTIONS_AMOUNT_INVALID";
    public static final String MESSAGE_ESTIMATE_DEDUCTIONS_AMOUNT_INVALID = "Estimate deduction amount should be greater than zero";

    public static final String KEY_INVALID_ESTIMATNUMBER_SPILLOVER = "INVALID_ESTIMATNUMBER_SPILLOVER";
    public static final String MESSAGE_INVALID_ESTIMATNUMBER_SPILLOVER = "Duplicate estimate number";
    
    public static final String KEY_NULL_TENANTID = "INVALID_TENANTID";
    public static final String MESSAGE_NULL_TENANTID = "Tenantid of should be entered";

    public static final String KEY_FUND_INVALID = "INVALID_FUND";
    public static final String MESSAGE_FUND_INVALID = "Please provide valid data for fund";
    
    public static final String KEY_FUNCTION_INVALID = "INVALID_FUNCTION";
    public static final String MESSAGE_FUNCTION_INVALID = "Please provide valid data for function";
    
    public static final String KEY_TYPEOFWORK_INVALID = "INVALID_TYPEOFWORK";
    public static final String MESSAGE_TYPEOFWORK_INVALID = "Please provide valid data for Type Of Work";
    
    public static final String KEY_SUBTYPEOFWORK_INVALID = "INVALID_SUBTYPEOFWORK";
    public static final String MESSAGE_SUBTYPEOFWORK_INVALID = "Please provide valid data for Sub Type Of Work";

    public static final String KEY_DEPARTMENT_INVALID = "INVALID_DEPARTMENT";
    public static final String MESSAGE_DEPARTMENT_INVALID = "Please provide valid data for Department";
    
    public static final String KEY_WARD_INVALID = "INVALID_WARD";
    public static final String MESSAGE_WARD_INVALID = "Please provide valid data for Ward";
    
    public static final String KEY_LOCALITY_INVALID = "INVALID_LOCALITY";
    public static final String MESSAGE_LOCALITY_INVALID = "Please provide valid data for Locality";

    public static final String KEY_UOM_INVALID = "UOM_INVALID";
    public static final String MESSAGE_UOM_INVALID = "Please provide valid data for UOM";

    public static final String KEY_MODEOFALLOTMENT_INVALID = "MODEOFALLOTMENT_INVALID";
    public static final String MESSAGE_MODEOFALLOTMENT_INVALID = "Please provide valid data for Mode of Allotment";

    public static final String KEY_NATUREOFWORK_INVALID = "NATUREOFWORK_INVALID";
    public static final String MESSAGE_NATUREOFWORK_INVALID = "Please provide valid data for Nature of work";

    
    public static final String KEY_SCHEME_INVALID = "INVALID_SCHEME";
    public static final String MESSAGE_SCHEME_INVALID = "Please provide valid data for Scheme";

    public static final String KEY_SUBSCHEME_INVALID = "INVALID_SUBSCHEME";
    public static final String MESSAGE_SUBSCHEME_INVALID = "Please provide valid data for Sub Scheme";

    public static final String KEY_BUDGETGROUP_INVALID = "INVALID_BUDGETGROUP";
    public static final String MESSAGE_BUDGETGROUP_INVALID = "Please provide valid data for Budget Group";
    
    public static final String KEY_REFERENCETYPE_INVALID = "INVALID_REFERENCETYPE";
    public static final String MESSAGE_REFERENCETYPE_INVALID = "Please provide valid data for Reference Type";
    
    public static final String KEY_UNIQUE_ABSTRACTESTIMATENUMBER = "UNIQUE_ABSTRACTESTIMATENUMBER";
    public static final String MESSAGE_UNIQUE_ABSTRACTESTIMATENUMBER = "Abstract Estimate Number should be Unique";
    
    public static final String KEY_UNIQUE_ADMINSANCTIONNUMBER = "UNIQUE_ADMINSANCTIONNUMBER";
    public static final String MESSAGE_UNIQUE_ADMINSANCTIONNUMBER = "Admin Sanction Number should be Unique";

    public static final String KEY_UNIQUE_COUNCILRESOLUTIONNUMBER = "UNIQUE_COUNCILRESOLUTIONNUMBER";
    public static final String MESSAGE_UNIQUE_COUNCILRESOLUTIONNUMBER = "Council Resolution Number should be Unique";

    public static final String KEY_NULL_COUNCILRESOLUTIONDATE = "INVALID_COUNCILRESOLUTIONDATE";
    public static final String MESSAGE_NULL_COUNCILRESOLUTIONDATE = "Council Resolution Date should be entered";
    
    public static final String KEY_NULL_ADMINSANCTIONNUMBER = "UNIQUE_ADMINSANCTIONNUMBER";
    public static final String MESSAGE_NULL_ADMINSANCTIONNUMBER = "Admin Sanction Number should be entered";
    
    public static final String KEY_NULL_ADMINSANCTIONDATE = "UNIQUE_ADMINSANCTIONDATE";
    public static final String MESSAGE_NULL_ADMINSANCTIONDATE = "Admin Sanction Date should be entered";
    
    public static final String KEY_NULL_ADMINSANCTIONBY = "INVALID_ADMINSANCTIONBY";
    public static final String MESSAGE_NULL_ADMINSANCTIONBY = "Admin Sanction By should be entered";

    public static final String KEY_UNIQUE_ESTIMATENUMBER = "UNIQUE_ESTIMATENUMBER";
    public static final String MESSAGE_UNIQUE_ESTIMATENUMBER = "Estimate Number should be Unique";

    public static final String KEY_INVALID_ESTIMATEAMOUNT = "UNIQUE_ESTIMATEAMOUNT";
    public static final String MESSAGE_INVALID_ESTIMATEAMOUNT = "Estimate Amount should be greater then 0";
    
    public static final String KEY_UNIQUE_WORKIDENTIFICATIONNUMBER = "UNIQUE_WORKIDENTIFICATIONNUMBER";
    public static final String MESSAGE_UNIQUE_WORKIDENTIFICATIONNUMBER = "Unique Work Identification Number should be entered";

    public static final String KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE= "INVALID_ESTIMATE_ACTIVITY_SCHEDULEOFRATE";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE = "Duplicate data for activity schedule of rates";

    public static final String KEY_ESTIMATE_ACTIVITY_QUANTITY= "INVALID_ESTIMATE_ACTIVITY_QUANTITY";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_QUANTITY = "Activity quantity should be greater than zero";

    public static final String KEY_ESTIMATE_ACTIVITY_INVALID= "INVALID_ESTIMATE_ACTIVITY";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_INVALID = "Invalid data for estimate activity";

    public static final String KEY_ESTIMATE_ACTIVITY_REQUIRED= "ESTIMATE_ACTIVITY_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_REQUIRED = "Atleast one sor or nonsor is required to create an estimate";

    public static final String KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE= "INVALID_ESTIMATE_ACTIVITY_ESTIMATE_RATE";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE = "Activity estimate rate should be greater than zero";

    public static final String KEY_ESTIMATE_LOCATION_REQUIRED= "INVALID_ESTIMATE_LOCATION";
    public static final String MESSAGE_ESTIMATE_LOCATION_REQUIRED= "Estimate location detailed required";

    public static final String KEY_ESTIMATE_ASSET_DETAILS_REQUIRED= "INVALID_ESTIMATE_ASSET_DETAILES";
    public static final String MESSAGE_ESTIMATE_ASSET_DETAILS_REQUIRED= "Asset details required for an estimate";

    public static final String KEY_ESTIMATE_TECHNICALSANCTION_DETAILS_REQUIRED= "INVALID_TECHNICALSANCTION_DETAILES";
    public static final String MESSAGE_ESTIMATE_TECHNICALSANCTION_DETAILS_REQUIRED= "Technical Sanction details required for an estimate";

    public static final String KEY_DUPLICATE_ESTIMATE_ASSET_DETAILS= "DUPLICATE_ESTIMATE_ASSET_DETAILES";
    public static final String MESSAGE_DUPLICATE_ESTIMATE_ASSET_DETAILS= "Duplicate Asset details for an estimate";

    public static final String KEY_INVALID_TECHNICALSANCTION_DATE = "TECHNICAL_SANCTION_DATE_INVALID";
    public static final String MESSAGE_INVALID_TECHNICALSANCTION_DATE= "Technical Sanction Date cannot be prior to Abstract/Detailed Estimate Date.";

    public static final String KEY_INVALID_ADMINSANCTION_DATE = "TECHNICAL_SANCTION_DATE_INVALID";
    public static final String MESSAGE_INVALID_ADMINSANCTION_DATE= "Abstract/Detailed Estimate Date cannot be prior to Admin Sanction Date of Estimate";

    public static final String KEY_INVALID_FILESTORE_ID = "INVALID_FILESTORE_ID";
    public static final String MESSAGE_INVALID_FILESTORE_ID= "Invalid data for document filestore id";

    public static final String KEY_INVALID_TECHNICALSANCTION_NUMBER = "INVALID_TECHNICALSANCTION_NUMBER";
    public static final String MESSAGE_INVALID_TECHNICALSANCTION_NUMBER= "Technical sanction number already exists, please provide valid technical sanction number";

    public static final String KEY_ESTIMATE_TECHNICALSANCTION_NUMBER_REQUIRED = "ESTIMATE_TECHNICALSANCTION_NUMBER_REQUIRED";
    public static final String MESSAGE_ESTIMATE_TECHNICALSANCTION_NUMBER_REQUIRED= "Technical sanction number required for an estimate";

    public static final String KEY_ESTIMATE_TECHNICALSANCTION_DATE_REQUIRED = "ESTIMATE_TECHNICALSANCTION_DATE_REQUIRED";
    public static final String MESSAGE_ESTIMATE_TECHNICALSANCTION_DATE_REQUIRED= "Technical sanction date required for an estimate";

    public static final String KEY_TECHNICAL_SANCTION_DATE_FUTUREDATE = "TECHNICAL_SANCTION_DATE_FUTUREDATE";
    public static final String MESSAGE_TECHNICAL_SANCTION_DATE_FUTUREDATE= "Estimate technical sanction date cannot be a future date";
    
    public static final String KEY_ABSTRACTESTIMATE_DETAILS_REQUIRED = "ABSTRACTESTIMATE_DETAILS_REQUIRED";
    public static final String MESSAGE_ABSTRACTESTIMATE_DETAILS_REQUIRED = "Abstract Estimate Details data is required";

    public static final String KEY_FUND_REQUIRED = "FUND_REQUIRED";
    public static final String MESSAGE_FUND_REQUIRED = "Fund data is required";

    public static final String KEY_FUNCTION_REQUIRED= "FUNCTION_REQUIRED";
    public static final String MESSAGE_FUNCTION_REQUIRED= "Function data is required";

    public static final String KEY_SCHEME_REQUIRED= "SCHEME_REQUIRED";
    public static final String MESSAGE_SCHEME_REQUIRED= "Scheme data is required";

    public static final String KEY_SUBSCHEME_REQUIRED= "SUBSCHEME_REQUIRED";
    public static final String MESSAGE_SUBSCHEME_REQUIRED= "SubScheme data is required";

    public static final String KEY_BUDGETGROUP_NAME_REQUIRED= "BUDGET_NAME_REQUIRED";
    public static final String MESSAGE_BUDGETGROUP_NAME_REQUIRED= "Budget group name is required";
    
    public static final String KEY_ACCOUNTCODE_REQUIRED= "ACCOUNTCODE_REQUIRED";
    public static final String MESSAGE_ACCOUNTCODE_REQUIRED= "Account Code is required";

    public static final String KEY_TYPEOFWORK_REQUIRED= "TYPEOFWORK_REQUIRED";
    public static final String MESSAGE_TYPEOFWORK_REQUIRED= "Type of work is required";

    public static final String KEY_SUBTYPEOFWORK_REQUIRED= "SUBTYPEOFWORK_REQUIRED";
    public static final String MESSAGE_SUBTYPEOFWORK_REQUIRED= "SubType of work is required";

    public static final String KEY_DEPARTMENT_CODE_REQUIRED= "DEPARTMENT_CODE_REQUIRED";
    public static final String MESSAGE_DEPARTMENT_CODE_REQUIRED= "Department data is required";
    
    public static final String KEY_WARD_CODE_REQUIRED= "WARD_CODE_REQUIRED";
    public static final String MESSAGE_WARD_CODE_REQUIRED= "Ward data is required";
    
    public static final String KEY_LOCALITY_CODE_REQUIRED= "LOCALITY_CODE_REQUIRED";
    public static final String MESSAGE_LOCALITY_CODE_REQUIRED= "Locality data is required";

    public static final String KEY_LOCALITY_CODE_INVALID= "LOCALITY_CODE_INVALID";
    public static final String MESSAGE_LOCALITY_CODE_INVALID= "Locality data is invali";

    public static final String KEY_ESTIMATE_OVERHEAD_CODE_INVALID= "ESTIMATE_OVERHEAD_INVALID";
    public static final String MESSAGE_ESTIMATE_OVERHEAD_CODE_INVALID= "Invalid data for esimate overhead code";

    public static final String KEY_ESTIMATE_ASSET_REQUIRED= "ESTIMATE_ASSET_CODE_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ASSET_REQUIRED= "Invalid data for esimate asset code";

    public static final String KEY_ESTIMATE_LAND_ASSET_REQUIRED= "ESTIMATE_LAND_ASSET_REQUIRED";
    public static final String MESSAGE_ESTIMATE_LAND_ASSET_REQUIRED= "Esimate land asset is required";

    public static final String KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED= "ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED= "Estimate rate is required for activity";

    public static final String KEY_UOM_REQUIRED= "ESTIMATE_ACTIVITY_UOM_REQUIRED";
    public static final String MESSAGE_UOM_REQUIRED= "UOM is required for activity";

    public static final String KEY_ESTIMATE_ACTIVITY_UOM_CODE_INVALID= "ESTIMATE_ACTIVITY_UOM_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_UOM_CODE_INVALID= "Invalid data for UOM code";

    public static final String KEY_ESTIMATE_ACTIVITY_UNIT_RATE_REQUIRED= "ESTIMATE_ACTIVITY_UNIT_RATE_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_UNIT_RATE_REQUIRED= "Unit rate is required for an estimate";

    public static final String KEY_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID= "ESTIMATE_ACTIVITY_UNIT_RATE_INVALID";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID= "Unit rate should be grater than zero";

    public static final String KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED= "ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED= "Estimate measurement sheet quantity is required";

    public static final String KEY_ESTIMATE_ACTIVITY_MEASUREMENT_IDENTIFIER_REQUIRED= "ESTIMATE_ACTIVITY_MEASUREMENT_IDENTIFIER_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_IDENTIFIER_REQUIRED= "Estimate measurement sheet identifier is required";

    public static final String KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_GREATER= "ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_GREATER";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_GREATER= "Estimate measurement sheet quantity should not be greater than activity quantity";

    public static final String KEY_ACTIVITY_AMOUNT_TOTAL_NOTEQUALSTO_WORKVALUE= "ACTIVITY_AMOUNT_TOTAL_NOTEQUALSTO_WORKVALUE";
    public static final String MESSAGE_ACTIVITY_AMOUNT_TOTAL_NOTEQUALSTO_WORKVALUE= "Total Estimate activity amount should be equal to work value";

    public static final String KEY_ACTIVITY_INVALID_UNITRATE= "ACTIVITY_INVALID_UNITRATE";
    public static final String MESSAGE_ACTIVITY_INVALID_UNITRATE= "Invalid unitrate for estimate activity";

    public static final String KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID= "ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID= "Estimate measurement sheet quantity should be greater than zero";

    public static final String KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATES_INVALID= "ESTIMATE_ACTIVITY_SCHEDULEOFRATES_INVALID";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATES_INVALID= "Estimate activity schedule of rate is invalid";
    
    public static final String KEY_NATUREOFWORK_REQUIRED = "NATUREOFWORK_REQUIRED";
    public static final String MESSAGE_NATUREOFWORK_REQUIRED = "Nature Of Work is required";
    
    public static final String KEY_REFERENCETYPE_REQUIRED = "REFERENCETYPE_REQUIRED";
    public static final String MESSAGE_REFERENCETYPE_REQUIRED = "Reference Type is required";
    
    public static final String KEY_MODEOFALLOTMENT_REQUIRED = "MODEOFALLOTMENT_REQUIRED";
    public static final String MESSAGE_MODEOFALLOTMENT_REQUIRED = "Mode Of Allotment is required";

    public static final String KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE= "ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE= "Duplicate value for Estimate activity schedule of rate";

    public static final String KEY_INVALID_SOR_RATES= "INVALID_SOR_RATES";
    public static final String MESSAGE_INVALID_SOR_RATES= "No valid rates defined for activity";

    public static final String KEY_NONSOR_DESCRIPTION_REQUIRED= "NONSOR_DESCRIPTION_REQUIRED";
    public static final String MESSAGE_NONSOR_DESCRIPTION_REQUIRED= "Non sor description is required";

    public static final String KEY_NONSOR_UOM_CODE_REQUIRED= "NONSOR_UOM_CODE_REQUIRED";
    public static final String MESSAGE_NONSOR_UOM_CODE_REQUIRED= "Non sor uom code is required";


    public static final String KEY_WORKS_ESTIMATE_ASSET_CODE_INVALID= "WORKS_ESTIMATE_ASSET_CODE_INVALID";
    public static final String MESSAGE_WORKS_ESTIMATE_ASSET_CODE_INVALID= "Invalid data for estimate asset code";
    
    public static final String KEY_CANNOT_UPDATE_STATUS_FOR_DETAILED_ESTIMATE = "CANNOT_UPDATE_STATUS_FOR_DETAILED_ESTIMATE";
    public static final String MESSAGE_CANNOT_UPDATE_STATUS_FOR_DETAILED_ESTIMATE = "Status can not be updated for detailed estimate";

    public static final String KEY_INVALID_STATUS_UPDATE_FOR_DETAILED_ESTIMATE = "INVALID_STATUS_UPDATE_FOR_DETAILED_ESTIMATE";
    public static final String MESSAGE_INVALID_STATUS_UPDATE_FOR_DETAILED_ESTIMATE = "Invalid status updated for detailed estimate";

    public static final String KEY_DE_EXISTS_FOR_AE = "DE_EXISTS_FOR_AE";
    public static final String MESSAGE_DE_EXISTS_FOR_AE = "Detailed estimate exists for the given abstract estimate";

    public static final String KEY_ESTIMATE_NOT_EXISTS = "ESTIMATE_NOT_EXISTS";
    public static final String MESSAGE_ESTIMATE_NOT_EXISTS = "Estimate does not exist with the given id";
    
    public static final String KEY_ESTIMATE_NUMBER_MODIFIED = "ESTIMATE_NUMBER_MODIFIED";
    public static final String MESSAGE_ESTIMATE_NUMBER_MODIFIED = "Estimate Number can not be modified";
    
    public static final String KEY_ABSTRACT_ESTIMATE_NUMBER_MODIFIED = "ABSTRACT_ESTIMATE_NUMBER_MODIFIED";
    public static final String MESSAGE_ABSTRACT_ESTIMATE_NUMBER_MODIFIED = "Abstract Estimate Number can not be modified";
    
    public static final String KEY_ABSTRACT_ESTIMATE_DETAIL_MODIFIED = "ABSTRACT_ESTIMATE_DETAIL_MODIFIED";
    public static final String MESSAGE_ABSTRACT_ESTIMATE_DETAIL_MODIFIED = "Abstract Estimate Detail can not be modified";
    
    public static final String ESTIMATE_NAMEOFWORK_NEW= "New";
    public static final String ESTIMATE_NAMEOFWORK_REPAIRS= "Repairs";
    public static final String ESTIMATE_NAMEOFWORK_ADDITION= "Addition";
    
    public static final String KEY_ABSTRACTESTIMATE_DETAILS_GROSSBILLEDAMOUNT_REQUIRED = "ABSTRACTESTIMATE_DETAILS_GROSSBILLEDAMOUNT_REQUIRED";
    public static final String MESSAGE_ABSTRACTESTIMATE_DETAILS_GROSSBILLEDAMOUNT_REQUIRED = "Gross bill amount for Abstract Estimate Details is required";

    public static final String KEY_INVALID_BILLSCREATED_FLAG = "KEY_INVALID_BILLSCREATED_FLAG";
    public static final String MESSAGE_INVALID_BILLSCREATED_FLAG = "When bills creates checked workorder created and detailed estimate created flag is required";

    public static final String KEY_INVALID_WORKORDER_FLAG = "KEY_INVALID_BILLSCREATED_FLAG";
    public static final String MESSAGE_INVALID_WORKORDER_FLAG = "When workorder creates checked detailed estimate created flag is required";

    
    public static final String KEY_INVALID_GROSSBILLEDAMOUNT = "UNIQUE_GROSSBILLEDAMOUNT";
    public static final String MESSAGE_INVALID_GROSSBILLEDAMOUNT = "Gross billed amount should be greater then 0";
 
    public static final String KEY_PMCTYPE_INVALID = "INVALID_PMCTYPE";
    public static final String MESSAGE_PMCTYPE_INVALID = "Please provide valid data for PMC Type";

    public static final String KEY_PMCNAME_INVALID = "INVALID_PMCNAME";
    public static final String MESSAGE_PMCNAME_INVALID = "Please provide valid data for PMC Name";
    
    public static final String KEY_WARDCODE_INVALID = "INVALID_WARDCODE";
    public static final String MESSAGE_WARDCODE_INVALID = "Please provide valid data for Ward";
    
    public static final String KEY_LOCALITYCODE_INVALID = "INVALID_LOCALITY";
    public static final String MESSAGE_LOCALITYCODE_INVALID = "Please provide valid data for Locality";
    
    public static final String KEY_FUTUREDATE_COUNCILRESOLUTIONDATE = "INVALID_COUNCILRESOLUTIONDATE";
    public static final String MESSAGE_FUTUREDATE_COUNCILRESOLUTIONDATE = "Council Resolution Date cannot be future date";
    
    public static final String KEY_COUNCILRESOLUTION_PROPOSAL_DATE = "INVALID_ADMINSANCTION_PROPOSAL_DATE";
    public static final String MESSAGE_COUNCILRESOLUTION_PROPOSAL_DATE = "Council Resolution date should be on or after the Date of Proposal";

    public static final String KEY_DUPLICATE_WINCODES = "KEY_DUPLICATE_WINCODES";
    public static final String MESSAGE_DUPLICATE_WINCODES = "Please provide Unique work identification numbers in abstract estimate details";

    public static final String KEY_WORKS_ESTIMATE_DPREMARKS_REQUIRED = "WORKS_ESTIMATE_DPREMARKS_REQUIRED";
    public static final String MESSAGE_WORKS_ESTIMATE_DPREMARKS_REQUIRED = "Please enter dp remarks for an estimate";

    public static final String KEY_WORKS_ESTIMATE_WORKPROPOSED_AS_PERDPREMARKS_REQUIRED = "WORKS_ESTIMATE_WORKPROPOSED_AS_PERDPREMARKS_REQUIRED";
    public static final String MESSAGE_WORKS_ESTIMATE_WORKPROPOSED_AS_PERDPREMARKS_REQUIRED = "Work proposed as per dp remarks should be true";

    public static final String KEY_DUPLICATE_ABSTRACTESTIMATENUMBERS = "works.abstractestimate.duplicate.abstractestimatenumber";
    public static final String MESSAGE_DUPLICATE_ABSTRACTESTIMATENUMBERS = "Please provide Unique abstract estimate numbers";

    public static final String KEY_ESTIMATEAPPROPRIATION_BUDGET_NOTAVAILABLE = "works.estimateappropriation.budget.notavailable";
    public static final String MESSAGE_ESTIMATEAPPROPRIATION_BUDGET_NOTAVAILABLE = "There is no budget availble for the given combination";

    public static final String KEY_WORKS_ESTIMATE_STATUS_REQUIRED = "WORKS_ESTIMATE_STATUS_REQUIRED";
    public static final String MESSAGE_WORKS_ESTIMATE_STATUS_REQUIRED = "Work estimate status is required";

    public static final String KEY_WORKS_ESTIMATE_STATUS_INVALID = "WORKS_ESTIMATE_STATUS_INVALID";
    public static final String MESSAGE_WORKS_ESTIMATE_STATUS_INVALID = "Work estimate status is invalid";

    public static final String KEY_ESIMATE_OVERHEAD_TENANTID_REQUIRED = "ESIMATE_OVERHEAD_TENANTID_REQUIRED";
    public static final String MESSAGE_ESIMATE_OVERHEAD_TENANTID_REQUIRED = "Tenantid is required for estimate overhead";

    public static final String KEY_ESIMATE_ACTIVITY_TENANTID_REQUIRED = "ESIMATE_ACTIVITY_TENANTID_REQUIRED";
    public static final String MESSAGE_ESIMATE_ACTIVITY_TENANTID_REQUIRED = "Tenantid is required for estimate activity";

    public static final String KEY_ESIMATE_TECHNICALSANCTION_TENANTID_REQUIRED = "ESIMATE_TECHNICALSANCTION_TENANTID_REQUIRED";
    public static final String MESSAGE_ESIMATE_TECHNICALSANCTION_TENANTID_REQUIRED = "Tenantid is required for estimate technical sanction details";

    public static final String KEY_ESIMATE_DOCUMENTDETAILS_TENANTID_REQUIRED = "ESIMATE_DOCUMENTDETAILS_TENANTID_REQUIRED";
    public static final String MESSAGE_ESIMATE_DOCUMENTDETAILS_TENANTID_REQUIRED = "Tenantid is required for estimate document details";

    public static final String KEY_ESIMATE_DEDUCTIONS_TENANTID_REQUIRED = "ESIMATE_DEDUCTIONS_TENANTID_REQUIRED";
    public static final String MESSAGE_ESIMATE_DEDUCTIONS_TENANTID_REQUIRED = "Tenantid is required for estimate deductions";
    
    public static final String KEY_PMC_CONTRACTOR_INVALID = "works.ae.pmc.notexist";
    public static final String MESSAGE_PMC_CONTRACTOR_INVALID = "The given PMC Does not exist in the system";
    
    public static final String KEY_AE_INVALID_GROSSBILLEDAMOUNT = "works.ae.billscreated.mandatory";
    public static final String MESSAGE_AE_INVALID_GROSSBILLEDAMOUNT = "Bills created should be checked if Gross billed amount entered";
}
