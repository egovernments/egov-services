package org.egov.works.estimate.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static final String ABSTRACT_ESTIMATE_NUMBER_GENERATION_ERROR = "Abstract estimate numebr can not be generated";
    public static final String DETAILED_ESTIMATE_NUMBER_GENERATION_ERROR = "Detailed estimate numebr can not be generated";
    public static final String WORK_IDENTIFICATION_NUMBER_GENERATION_ERROR = "Work identification numebr can not be generated";

    public static final String TYPEOFWORK_OBJECT = "TypeOfWork";
    public static final String FUND_OBJECT = "Fund";
    public static final String FUNCTION_OBJECT = "Function";
    public static final String SUBTYPEOFWORK_OBJECT = "SubTypeOfWork";
    public static final String SCHEME_OBJECT = "Scheme";
    public static final String SUBSCHEME_OBJECT = "SubScheme";
    public static final String BUDGETGROUP_OBJECT = "BudgetGroup";
    public static final String DEPARTMENT_OBJECT = "Department";
    public static final String WORKS_MODULE_CODE = "Works";
    public static final String EGF_MODULE_CODE = "egf-master";
    public static final String COMMON_MASTERS_MODULE_CODE = "common-masters";
    public static final String OVERHEAD_OBJECT = "Overhead";
    public static final String UOM_OBJECT = "Uom";
    public static final String MODEOFALLOTMENT_OBJECT = "ModeOfAllotment";
    public static final String NATUREOFWORK_OBJECT = "NatureOfWork";

    public static final String BOUNDARY_OBJECT = "Boundary";
    public static final String APPCONFIGURATION_OBJECT = "AppConfiguration";
    public static final String GIS_INTEGRATION_APPCONFIG = "GIS_INTEGRATION";
    public static final String ASSET_DETAILES_REQUIRED_APPCONFIG = "ASSET_MANDATORY";
    public static final String FINANCIAL_INTEGRATION_KEY = "Financial_Integration_Required";
    
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

    public static final String KEY_DUPLICATE_MULTIYEAR_ESTIMATE = "DUPLICATE_MULTIYEAR_ESTIMATE";
    public static final String MESSAGE_DUPLICATE_MULTIYEAR_ESTIMATE = "Duplicate multiyear estimates";

    public static final String KEY_PERCENTAGE_MULTIYEAR_ESTIMATE = "INVALID_PERCANTAGE_MULTIYEAR_ESTIMATE";
    public static final String MESSAGE_PERCENTAGE_MULTIYEAR_ESTIMATE = "Percentage should not  be greater than 100 for multiyear estimates";
    
    public static final String KEY_NULL_ABSTRACTESTIMATE_NUMBER = "INVALID_ABSTRACTESTIMATE_NUMBER";
    public static final String MESSAGE_NULL_ABSTRACTESTIMATE_NUMBER = "Abstract Estimate Number should be entered";

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

    public static final String KEY_FUTUREDATE_ESTIMATEDATE_SPILLOVER = "INVALID_ESTIMATE_DATE_SPILLOVER";
    public static final String MESSAGE_FUTUREDATE_ESTIMATEDATE_SPILLOVER = "Estimate Date cannot be future date";

    public static final String KEY_INVALID_ESTIMATNUMBER_SPILLOVER = "INVALID_ESTIMATNUMBER_SPILLOVER";
    public static final String MESSAGE_INVALID_ESTIMATNUMBER_SPILLOVER = "Duplicate estimate number";
    
    public static final String KEY_NULL_TENANTID = "INVALID_TENANTID";
    public static final String MESSAGE_NULL_TENANTID = "Tenantid of should be entered";

    public static final String KEY_FUND_INVALID = "INVALID_FUND";
    public static final String MESSAGE_FUND_INVALID = "Plese provide valid data for fund";
    
    public static final String KEY_FUNCTION_INVALID = "INVALID_FUNCTION";
    public static final String MESSAGE_FUNCTION_INVALID = "Plese provide valid data for function";
    
    public static final String KEY_TYPEOFWORK_INVALID = "INVALID_TYPEOFWORK";
    public static final String MESSAGE_TYPEOFWORK_INVALID = "Plese provide valid data for Type Of Work";
    
    public static final String KEY_SUBTYPEOFWORK_INVALID = "INVALID_SUBTYPEOFWORK";
    public static final String MESSAGE_SUBTYPEOFWORK_INVALID = "Plese provide valid data for Sub Type Of Work";

    public static final String KEY_DEPARTMENT_INVALID = "INVALID_DEPARTMENT";
    public static final String MESSAGE_DEPARTMENT_INVALID = "Plese provide valid data for Department";

    public static final String KEY_UOM_INVALID = "UOM_INVALID";
    public static final String MESSAGE_UOM_INVALID = "Plese provide valid data for UOM";

    public static final String KEY_MODEOFALLOTMENT_INVALID = "MODEOFALLOTMENT_INVALID";
    public static final String MESSAGE_MODEOFALLOTMENT_INVALID = "Plese provide valid data for Mode of Allotment";

    public static final String KEY_NATUREOFWORK_INVALID = "UOM_INVALID";
    public static final String MESSAGE_NATUREOFWORK_INVALID = "Plese provide valid data for Nature of work";

    
    public static final String KEY_SCHEME_INVALID = "INVALID_SCHEME";
    public static final String MESSAGE_SCHEME_INVALID = "Plese provide valid data for Scheme";

    public static final String KEY_SUBSCHEME_INVALID = "INVALID_SUBSCHEME";
    public static final String MESSAGE_SUBSCHEME_INVALID = "Plese provide valid data for Sub Scheme";

    public static final String KEY_BUDGETGROUP_INVALID = "INVALID_BUDGETGROUP";
    public static final String MESSAGE_BUDGETGROUP_INVALID = "Plese provide valid data for Budget Group";

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

    public static final String KEY_UNIQUE_ESTIMATENUMBER = "UNIQUE_ESTIMATENUMBER";
    public static final String MESSAGE_UNIQUE_ESTIMATENUMBER = "Estimate Number should be Unique";

    public static final String KEY_INVALID_ESTIMATEAMOUNT = "UNIQUE_ESTIMATEAMOUNT";
    public static final String MESSAGE_INVALID_ESTIMATEAMOUNT = "Estimate Amount should be greater then 0";
    
    public static final String KEY_UNIQUE_WORKIDENTIFICATIONNUMBER = "UNIQUE_WORKIDENTIFICATIONNUMBER";
    public static final String MESSAGE_UNIQUE_WORKIDENTIFICATIONNUMBER = "Work Identification Number should be entered";

    public static final String KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE= "INVALID_ESTIMATE_ACTIVITY_SCHEDULEOFRATE";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE = "Duplicate data for activity schedule of rates";

    public static final String KEY_ESTIMATE_ACTIVITY_QUANTITY= "INVALID_ESTIMATE_ACTIVITY_QUANTITY";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_QUANTITY = "Activity quantity should be greater than zero";

    public static final String KEY_ESTIMATE_ACTIVITY_INVALID= "INVALID_ESTIMATE_ACTIVITY";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_INVALID = "Invalid data for estimate activity";

    public static final String KEY_ESTIMATE_ACTIVITY_REQUIRED= "ESTIMATE_ACTIVITY_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_REQUIRED = "Atleast one activity is required to create an estimate";

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
    public static final String MESSAGE_INVALID_TECHNICALSANCTION_NUMBER= "Duplicate technical sanction number";

    public static final String KEY_TECHNICAL_SANCTION_DATE_FUTUREDATE = "TECHNICAL_SANCTION_DATE_FUTUREDATE";
    public static final String MESSAGE_TECHNICAL_SANCTION_DATE_FUTUREDATE= "Estimate technical sanction date cannot be a future date";

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


    public static final String KEY_ESTIMATE_OVERHEAD_CODE_INVALID= "ESTIMATE_OVERHEAD_INVALID";
    public static final String MESSAGE_ESTIMATE_OVERHEAD_CODE_INVALID= "Invalid data for esimate overhead code";

    public static final String KEY_ESTIMATE_ASSET_REQUIRED= "ESTIMATE_ASSET_CODE_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ASSET_REQUIRED= "Invalid data for esimate asset code";

    public static final String KEY_ESTIMATE_LAND_ASSET_REQUIRED= "ESTIMATE_LAND_ASSET_REQUIRED";
    public static final String MESSAGE_ESTIMATE_LAND_ASSET_REQUIRED= "Esimate land asset is required";

    public static final String KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED= "ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED= "Estimate rate is required for activity";

    public static final String KEY_ESTIMATE_ACTIVITY_UOM_REQUIRED= "ESTIMATE_ACTIVITY_UOM_REQUIRED";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_UOM_REQUIRED= "UOM is required for activity";

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

    public static final String KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID= "ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID= "Estimate measurement sheet quantity should be greater than zero";

    public static final String KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_CODE_INVALID= "ESTIMATE_ACTIVITY_SCHEDULEOFRATE_CODE_INVALID";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_CODE_INVALID= "Estimate activity schedule of rate is invalid";

    public static final String KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE= "ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE";
    public static final String MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE= "Duplicate value for Estimate activity schedule of rate";

    public static final String KEY_WORKS_ESTIMATE_ASSET_CODE_INVALID= "WORKS_ESTIMATE_ASSET_CODE_INVALID";
    public static final String MESSAGE_WORKS_ESTIMATE_ASSET_CODE_INVALID= "Invalid data for estimate asset code";

    public static final String ESTIMATE_NAMEOFWORK_NEW= "New";
    public static final String ESTIMATE_NAMEOFWORK_REPAIRS= "Repairs";
    public static final String ESTIMATE_NAMEOFWORK_ADDITION= "Addition";



}
