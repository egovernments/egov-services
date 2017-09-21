/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:messages/messages.properties",
        "classpath:messages/errors.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class WcmsConstants {

    @Autowired
    private Environment environment;

    public static final String INVALID_REQUEST_MESSAGE = "Request is invalid";
    public static final String INVALID_USAGETYPE_REQUEST_MESSAGE = "UsageType is invalid";
    public static final String INVALID_METER_COST_REQUEST_MESSAGE = "MeterCost Request is invalid";
    public static final String INVALID_METER_STATUS_REQUEST_MESSAGE = "MeterStatus Request is invalid";
    public static final String INVALID_PIPESIZE_REQUEST_MESSAGE = "PipeSize is invalid";
    public static final String INVALID_DONATION_REQUEST_MESSAGE = "Donation Request is invalid";
    public static final String INVALID_DOCUMENTTYPE_REQUEST_MESSAGE = "Document Type Request is Invalid";
    public static final String INVALID_DOCTYPE_APPLICATION_TYPE_REQUEST_MESSAGE = "DocumentTypeApplicationType is invalid";
    public static final String INVALID_WATERSOURCETYPE_REQUEST_MESSAGE = "Water Source Type is invalid";
    public static final String INVALID_STORAGE_RESERVOIR_REQUEST_MESSAGE = "Storage Reservoir is invalid";
    public static final String INVALID_TREATMENT_PLANT_REQUEST_MESSAGE = "Treatment Plant is invalid";
    public static final String INVALID_METER_WATER_RATES_REQUEST_MESSAGE = "Meter Water Rates is invalid";
    public static final String INVALID_SUPPLY_TYPE_REQUEST_MESSAGE = "Supply Type is invalid";
    public static final String INVALID_NON_METER_WATER_RATES_REQUEST_MESSAGE = "Non Meter Water Rates is invalid";
    public static final String INVALID_SERVICE_CHARGE_REQUEST_MESSAGE = "Service Charge is invalid";
    public static final String INVALID_GAPCODE_REQUEST_MESSAGE = "GapCode is invalid";

    public static final String USAGETYPE_NAME_UNIQUE_CODE = "wcms.0001";
    public static final String USAGETYPE_NAME_UNQ_FIELD_NAME = "name";
    public static final String USAGETYPE_UNQ_ERROR_MESSAGE = "Entered Usage Type already exist";

    public static final String USAGETYPE_NAME_MANDATORY_CODE = "wcms.0002";
    public static final String USAGETYPE_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE = "Usage Type is required";


    public static final String TENANTID_MANDATORY_CODE = "wcms.0003";
    public static final String TENANTID_MANADATORY_FIELD_NAME = "tenantId";
    public static final String TENANTID_MANADATORY_ERROR_MESSAGE = "Tenant Id is required";

    public static final String ACTIVE_MANDATORY_CODE = "wcms.0004";
    public static final String ACTIVE_MANADATORY_FIELD_NAME = "active";
    public static final String ACTIVE_MANADATORY_ERROR_MESSAGE = "Active is required";

    public static final String PIPESIZE_SIZEINMM_MANDATORY_CODE = "wcms.0007";
    public static final String PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME = "sizeInMilimeter";
    public static final String PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE = "H.S.C Pipe Size(mm) is required";

    public static final String PIPESIZE_SIZEINMM_UNIQUE_CODE = "wcms.0008";
    public static final String PIPESIZE_SIZEINMM__UNQ_FIELD_NAME = "sizeInMilimeter";
    public static final String PIPESIZE_SIZEINMM_UNQ_ERROR_MESSAGE = "Entered H.S.C Pipe Size(mm) already exist.";

    public static final String DONATION_MANDATORY_CODE = "wcms.0012";
    public static final String DONATION_MANDATORY_FIELD_NAME = "donationAmount";
    public static final String DONATION_MANDATORY_ERROR_MESSAGE = "Donation Amount is required";

    public static final String FROMTO_MANDATORY_CODE = "wcms.0013";
    public static final String FROMTO_MANDATORY_FIELD_NAME = "fromDate,toDate";
    public static final String FROMTO_MANDATORY_ERROR_MESSAGE = "From and To Date both are required";

    public static final String CODE_MANDATORY_CODE = "wcms.0013";
    public static final String CODE_MANDATORY_FIELD_NAME = "code";
    public static final String CODE_MANDATORY_ERROR_MESSAGE = "Code is required";

    public static final String NAME_MANDATORY_CODE = "wcms.0014";
    public static final String NAME_MANDATORY_FIELD_NAME = "name";
    public static final String NAME_MANDATORY_ERROR_MESSAGE = "Name is required";

    public static final String CODE_TENANT_UNIQUE_CODE = "wcms.0015";
    public static final String CODE_TENANT_UNQ_FIELD_NAME = "code";
    public static final String CODE_TENANT_UNQ_ERROR_MESSAGE = "Entered combination of Document Code and Tenant ID already exists. Please check!";


    public static final String APPLICATION_TYPE_INVALID_CODE = "wcms.0020";
    public static final String APPLICATION_TYPE_INVALID_FIELD_NAME = "applicationType";
    public static final String APPLICATION_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Application Type";

    public static final String BILLING_TYPE_INVALID_CODE = "wcms.0021";
    public static final String BILLING_TYPE_INVALID_FIELD_NAME = "billingType";
    public static final String BILLING_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Billing Type";

    public static final String CONNECTION_TYPE_INVALID_CODE = "wcms.0022";
    public static final String CONNECTION_TYPE_INVALID_FIELD_NAME = "connectionType";
    public static final String CONNECTION_INVALID_ERROR_MESSAGE = "Please provide valid Connection Type";

    public static final String DOCUMENTS_INVALID_CODE = "wcms.0023";
    public static final String DOCUMENTS_INVALID_FIELD_NAME = "documents";
    public static final String DOCUMENTS_INVALID_ERROR_MESSAGE = "Please provide valid Documents";

    public static final String NO_OF_TAPS_INVALID_CODE = "wcms.0024";
    public static final String NO_OF_TAPS_INVALID_FIELD_NAME = "numberOfTaps";
    public static final String NO_OF_TAPS_INVALID_ERROR_MESSAGE = "Please provide valid info for Number of Taps";

    public static final String SOURCE_TYPE_INVALID_CODE = "wcms.0025";
    public static final String SOURCE_TYPE_INVALID_FIELD_NAME = "sourceType";
    public static final String SOURCE_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Source Type";

    public static final String SUMP_CAPACITY_INVALID_CODE = "wcms.0026";
    public static final String SUMP_CAPACITY_INVALID_FIELD_NAME = "sumpCapacity";
    public static final String SUMP_CAPACITY_INVALID_ERROR_MESSAGE = "Please provide valid Sump Capacity";

    public static final String SUPPLY_TYPE_INVALID_CODE = "wcms.0027";
    public static final String SUPPLY_TYPE_INVALID_FIELD_NAME = "supplyType";
    public static final String SUPPLY_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Supply Type";

    public static final String DOCTYPE_MANDATORY_CODE = "wcms.0028";
    public static final String DOCTYPE_MANADATORY_FIELD_NAME = "documentType";
    public static final String DOCTYPE_MANADATORY_ERROR_MESSAGE = "documentType is required";

    public static final String VALID_APPLICATION_TYPE_MANDATORY_CODE = "wcms.0029";
    public static final String VALID_APPLICATION_TYPE_MANADATORY_FIELD_NAME = "applicationType";
    public static final String VALID_APPLICATION_TYPE_ERROR_MESSAGE = "Please provide valid Application type";

    public static final String APPLICATION_TYPE_MANDATORY_CODE = "wcms.0030";
    public static final String APPLICATION_TYPE_MANADATORY_FIELD_NAME = "applicationType";
    public static final String APPLICATION_TYPE_ERROR_MESSAGE = "applicationType is required";

    public static final String DOCTYPE_APPLICATIONTYPE_UNIQUE_CODE = "wcms.0031";
    public static final String DOCTYPE_APPLICATIONTYPE_UNQ_FIELD_NAME = "documentType and applicationType";
    public static final String DOCTYPE_APPLICATIONTYPE_UNQ_ERROR_MESSAGE = "Entered DocumentType and ApplicationType Combination already exist.";

    public static final String DOCUMENTTYPE_NAME_MANDATORY_CODE = "wcms.0032";
    public static final String DOCUMENTTYPE_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String DOCUMENTTYPE_NAME_MANADATORY_ERROR_MESSAGE = "Document Type is required";

    public static final String DOCUMENTTYPE_NAME_UNIQUE_CODE = "wcms.0033";
    public static final String DOCUMENTTYPE_NAME_UNQ_FIELD_NAME = "name";
    public static final String DOCUMENTTYPE_UNQ_ERROR_MESSAGE = "Entered Document Type already exist";

    public static final String DOCUMENT_APPLICATION_INVALID_CODE = "wcms.0036";
    public static final String DOCUMENT_APPLICATION_INVALID_FIELD_NAME = "documentType - applicationType";
    public static final String DOCUMENT_APPLICATION_INVALID_ERROR_MESSAGE = "Document Type - Application Type values don't match.";

    public static final String STATIC_INVALID_CODE = "wcms.0037";
    public static final String STATIC_INVALID_FIELD_NAME = "enums";
    public static final String STATIC_INVALID_ERROR_MESSAGE = "Please check: connection, supply, source or billing type.";

    public static final String BPL_INVALID_CODE = "wcms.0038";
    public static final String BPL_INVALID_FIELD_NAME = "bplCardHolderName";
    public static final String BPL_INVALID_ERROR_MESSAGE = "BPL Card Holder Name is invalid!";

    public static final String DONATION_INVALID_CODE = "wcms.0039";
    public static final String DONATION_INVALID_FIELD_NAME = "donationAmount";
    public static final String DONATION_INVALID_ERROR_MESSAGE = "Donation Amount couldn't be generated, Please check data";

    public static final String WATERSOURCETYPE_NAME_UNIQUE_CODE = "wcms.0034";
    public static final String WATERSOURCETYPE_NAME_UNQ_FIELD_NAME = "name";
    public static final String WATERSOURCETYPE_UNQ_ERROR_MESSAGE = "Entered Water Source Type  already exist";

    public static final String WATERSOURCETYPE_NAME_MANDATORY_CODE = "wcms.0035";
    public static final String WATERSOURCETYPE_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String WATERSOURCETYPE_NAME_MANADATORY_ERROR_MESSAGE = "Water Source Type is required";

    public static final String SUPPLYTYPE_NAME_UNIQUE_CODE = "wcms.0036";
    public static final String SUPPLYTYPE_NAME_UNQ_FIELD_NAME = "name";
    public static final String SUPPLYTYPE_UNQ_ERROR_MESSAGE = "Entered Supply Type already exist";

    public static final String SUPPLYTYPE_NAME_MANDATORY_CODE = "wcms.0037";
    public static final String SUPPLYTYPE_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String SUPPLYTYPE_NAME_MANADATORY_ERROR_MESSAGE = "Supply Type is required";

    public static final String CATEGORY_INVALID_CODE = "wcms.0038";
    public static final String CATEGORY_INVALID_FIELD_NAME = "CategoryType";
    public static final String CATEGORY_INVALID_ERROR_MESSAGE = "CategoryType name doesn't Exist";

    public static final String PIPESIZE_INVALID_CODE = "wcms.0039";
    public static final String PIPESIZE_INVALID_FIELD_NAME = "hscPipeSizeType.sizeininch";
    public static final String PIPESIZE_INVALID_ERROR_MESSAGE = "Pipesize InInch doesn't Exist";

    public static final String SOURCETYPE_INVALID_CODE = "wcms.0040";
    public static final String SOURCETYPE_INVALID_FIELD_NAME = "sourceType.name";
    public static final String SOURCETYPE_INVALID_ERROR_MESSAGE = "SourceType name doesn't Exist";

    public static final String SUPPLYTYPE_INVALID_CODE = "wcms.0041";
    public static final String SUPPLYTYPE_INVALID_FIELD_NAME = "supplyType.name";
    public static final String SUPPLYTYPE_INVALID_ERROR_MESSAGE = "SupplyType name doesn't Exist";


    public static final String USAGETYPE_INVALID_CODE = "wcms.0043";
    public static final String USAGETYPE_INVALID_ERROR_MESSAGE = "Please provide valid Usage Type Code";
    public static final String USAGETYPE_INVALID_FIELD_NAME = "usageTypeCode";

    public static final String DONATION_PIPESIZE_MAX_INVALID_CODE = "wcms.0044";
    public static final String DONATION_PIPESIZE_MAX_INVALID_FIELD_NAME = "maxPipeSize";
    public static final String DONATION_PIPESIZE_MAX_INVALID_ERROR_MESSAGE = "Please provide valid Max Pipe Size(mm)";

    public static final String DONATION_PIPESIZE_MIN_INVALID_CODE = "wcms.0045";
    public static final String DONATION_PIPESIZE_MIN_INVALID_FIELD_NAME = "minPipeSize";
    public static final String DONATION_PIPESIZE_MIN_INVALID_ERROR_MESSAGE = "Please provide valid Min Pipe Size(mm)";

    public static final String DOCUMENTTYPE_INVALID_CODE = "wcms.0046";
    public static final String DOCUMENTTYPE_INVALID_FIELD_NAME = "documentType";
    public static final String DOCUMENTTYPE_INVALID_ERROR_MESSAGE = "Please provide valid DocumentType";

    public static final String STORAGE_RESERVOIR_NAME_MANDATORY_CODE = "wcms.0047";
    public static final String STORAGE_RESERVOIR_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String STORAGE_RESERVOIR_MANADATORY_ERROR_MESSAGE = "Storage Reservoir Name is required";

    public static final String STORAGE_RESERVOIR_RESERVOIR_TYPE_MANDATORY_CODE = "wcms.0048";
    public static final String STORAGE_RESERVOIR_RESERVOIR_TYPE_MANADATORY_FIELD_NAME = "reservoirType";
    public static final String STORAGE_RESERVOIR_RESERVOIR_TYPE_MANADATORY_ERROR_MESSAGE = " Reservoir Type is required";

    public static final String CAPACITY_MANDATORY_CODE = "wcms.0049";
    public static final String CAPACITY_MANADATORY_FIELD_NAME = "Capacity";
    public static final String CAPACITY_MANADATORY_ERROR_MESSAGE = "Capacity is required";

    public static final String BOUNDARY_ZONE_INVALID_CODE = "wcms.0050";
    public static final String BOUNDARY_ZONE_INVALID_ERROR_MESSAGE = "Please provide valid Zone";
    public static final String BOUNDARY_ZONE_INVALID_FIELD_NAME = "zone";

    public static final String RESERVOIR_TYPE_INVALID_CODE = "wcms.0051";
    public static final String RESERVOIR_TYPE_INVALID_FIELD_NAME = "reservoirType";
    public static final String RESERVOIR_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Reservoir type";

    public static final String BOUNDARY_WARD_INVALID_CODE = "wcms.0052";
    public static final String BOUNDARY_WARD_INVALID_ERROR_MESSAGE = "Please provide valid Ward";
    public static final String BOUNDARY_WARD_INVALID_FIELD_NAME = "ward";

    public static final String BOUNDARY_LOCATION_INVALID_CODE = "wcms.0053";
    public static final String BOUNDARY_LOCATION_INVALID_ERROR_MESSAGE = "Please provide valid Location";
    public static final String BOUNDARY_LOCATION_INVALID_FIELD_NAME = "location";

    public static final String STORAGERESERVOIR_NAME_UNIQUE_CODE = "wcms.0054";
    public static final String STORAGERESERVOIR_NAME_UNQ_FIELD_NAME = "name";
    public static final String STORAGERESERVOIR_UNQ_ERROR_MESSAGE = "Entered Storage Reservoir Name already exist";

    public static final String TREATMENT_PLANT_NAME_MANDATORY_CODE = "wcms.0055";
    public static final String TREATMENT_PLANT_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String TREATMENT_PLANT_MANADATORY_ERROR_MESSAGE = "Treatment Plant Name is required";

    public static final String TREATMENT_PLANT_TYPE_MANDATORY_CODE = "wcms.0056";
    public static final String TREATMENT_PLANT_TYPE_MANADATORY_FIELD_NAME = "plantType";
    public static final String TREATMENT_PLANT_TYPE_MANADATORY_ERROR_MESSAGE = " Plant Type is required";

    public static final String TREATMENT_PLANT_NAME_UNIQUE_CODE = "wcms.0057";
    public static final String TREATMENT_PLANT_NAME_UNQ_FIELD_NAME = "name";
    public static final String TREATMENTPLANT_UNQ_ERROR_MESSAGE = "Entered Treatment Plant Name already exist";

    public static final String STORAGE_RESERVOIR_NAME_INVALID_CODE = "wcms.0058";
    public static final String STORAGE_RESERVOIR_NAME_INVALID_FIELD_NAME = "storageReservoirName";
    public static final String STORAGE_RESERVOIR_NAME_INVALID_ERROR_MESSAGE = "Please provide valid Storage Reservoir Name";

    public static final String TREATMENT_STORAGERESERVOIR_NAME_MANDATORY_CODE = "wcms.0059";
    public static final String TREATMENT_STORAGERESERVOIR_NAME_MANADATORY_FIELD_NAME = "storageReservoirName";
    public static final String TREATMENT_STORAGERESERVOIR_NAME_MANADATORY_ERROR_MESSAGE = " Storage Reservoir Name is required";

    public static final String PLANT_TYPE_INVALID_CODE = "wcms.0060";
    public static final String PLANT_TYPE_INVALID_FIELD_NAME = "plantType";
    public static final String PLANT_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Plant type";

    public static final String CONNECTION_TYPE_MANDATORY_CODE = "wcms.0061";
    public static final String CONNECTION_TYPE_MANADATORY_FIELD_NAME = "connectionType";
    public static final String CONNECTION_TYPE_MANADATORY_ERROR_MESSAGE = "connectionType is required";

    public static final String SOURCETYPE_NAME_MANDATORY_CODE = "wcms.0062";
    public static final String SOURCETYPE_NAME_MANADATORY_FIELD_NAME = "sourceTypeName";
    public static final String SOURCETYPE_NAME_MANADATORY_ERROR_MESSAGE = "SourceTypeName is required";

    public static final String SOURCE_TYPE_NAME_INVALID_CODE = "wcms.0063";
    public static final String SOURCE_TYPE_NAME_INVALID_ERROR_MESSAGE = "Please provide valid Source Type";
    public static final String SOURCE_TYPE_NAME_INVALID_FIELD_NAME = "source Type";

    public static final String METER_WATER_RATES_UNIQUE_CODE = "wcms.0064";
    public static final String METER_WATER_RATES_UNQ_FIELD_NAME = "usageTypeCode,subUsageTypeCode,sourceTypeName and pipeSize";
    public static final String METER_WATER_RATES_UNQ_ERROR_MESSAGE = "Entered combination of UsageTypeCode ,SubUsageTypeCode,SourceType and PipeSize Inmm has already been mapped";

    public static final String PIPESIZE_INMM_INVALID_CODE = "wcms.0065";
    public static final String PIPESIZE_INMM_FIELD_NAME = "pipeSize";
    public static final String PIPESIZE_INMM_INVALID_ERROR_MESSAGE = "Please provide valid Pipe Size(mm)";

    public static final String DONATION_MINPIPESIZE_MAXPIPESIZE_CODE = "wcms.0067";
    public static final String DONATION_MINPIPESIZE_MAXPIPESIZE_FIELD_NAME = "minPipeSize,maxPipeSize";
    public static final String DONATION_MINPIPESIZE_MAXPIPESIZE_ERROR_MESSAGE = "Minimum PipeSize should not be greater than the maximum PipeSize";

    public static final String DONATION_MINPIPESIZE_MAXPIPESIZE_EQUAL_CODE = "wcms.0068";
    public static final String DONATION_MINPIPESIZE_MAXPIPESIZE__EQUALFIELD_NAME = "minPipeSize,maxPipeSize";
    public static final String DONATION_MINPIPESIZE_MAXPIPESIZE__EQUAL_ERROR_MESSAGE = "Minimum PipeSize should not be same as maximum PipeSize";

    public static final String PIPESIZE_MANDATORY_CODE = "wcms.0069";
    public static final String PIPESIZE_MANDATORY_FIELD_NAME = "pipeSizeId";
    public static final String PIPESIZE_MANDATORY_ERROR_MESSAGE = "PipeSizeId Is Required";

    public static final String METERMAKE_MANDATORY_CODE = "wcms.0070";
    public static final String METERMAKE_MANDATORY_FIELD_NAME = "meterMake";
    public static final String METERMAKE_MANDATORY_ERROR_MESSAGE = "MeterMake Is Required";

    public static final String AMOUNT_MANDATORY_CODE = "wcms.0071";
    public static final String AMOUNT_MANDATORY_FIELD_NAME = "amount";
    public static final String AMOUNT_MANDATORY_ERROR_MESSAGE = "Amount is required";

    public static final String CODETENANTID_UNIQUE_CODE = "wcms.0072";
    public static final String CODETENANTID_UNIQUE_FIELD_NAME = "code,tenantId";
    public static final String CODETENANTID_UNIQUE_ERROR_MESSAGE = "Entered Code and tenantId combination already exists";

    public static final String NAMETENANTID_UNIQUE_CODE = "wcms.0073";
    public static final String NAMETENANTID_UNIQUE_FIELD_NAME = "metermake,amount,tenantId";
    public static final String NAMETENANTID_UNIQUE_ERROR_MESSAGE = "Entered MeterMake,MeterCost and tenantId combination already exists";

    public static final String METERSTATUS_MANDATORY_CODE = "wcms.0074";
    public static final String METERSTATUS_MANDATORY_FIELD_NAME = "meterStatus";
    public static final String METERSTATUS_MANDATORY_ERROR_MESSAGE = "meterStatus Is Required";

    public static final String NON_METER_WATER_RATES_UNIQUE_CODE = "wcms.0075";
    public static final String NON_METER_WATER_RATES_UNQ_FIELD_NAME = "connectionType ,usageTypeCode , subUsageTypeCode, sourceTypeName ,pipeSize and fromDate ";
    public static final String NON_METER_WATER_RATES_UNQ_ERROR_MESSAGE = "Entered combination of ConnectionType,UsageType , SubUsageType, SourceType ,PipeSize and fromDate has already been mapped";

    public static final String DONATION_UNIQUE_CODE = "wcms.0076";
    public static final String DONATION_UNQ_FIELD_NAME = "usageTypeCode,subUsageTypeCode,maxPipeSize and minPipeSize";
    public static final String DONATION_UNQ_ERROR_MESSAGE = "Entered combination of UsageTypeCode ,SubUsageTypeCode ,MaxPipeSize and MinPipeSize Inmm has already been mapped";

    public static final String SERVICETYPE_MANDATORY_CODE = "wcms.0076";
    public static final String SERVICETYPE_MANADATORY_FIELD_NAME = "serviceType";
    public static final String SERVICETYPE_MANADATORY_ERROR_MESSAGE = "serviceType is required";

    public static final String SERVICECHARGEAPPLICABLE_MANDATORY_CODE = "wcms.0077";
    public static final String SERVICECHARGEAPPLICABLE_MANADATORY_FIELD_NAME = "serviceChargeApplicable";
    public static final String SERVICECHARGEAPPLICABLE_MANADATORY_ERROR_MESSAGE = "serviceChargeApplicable is required";

    public static final String SERVICECHARGETYPE_MANDATORY_CODE = "wcms.0078";
    public static final String SERVICECHARGETYPE_MANADATORY_FIELD_NAME = "serviceChargeType";
    public static final String SERVICECHARGETYPE_MANADATORY_ERROR_MESSAGE = "serviceChargeType is Required";

    public static final String SERVICECHARGEEFFECTIVEFROM_MANDATORY_CODE = "wcms.0079";
    public static final String SERVICECHARGEEFFECTIVEFROM_MANADATORY_FIELD_NAME = "effectiveFrom";
    public static final String SERVICECHARGEEFFECTIVEFROM_MANADATORY_ERROR_MESSAGE = "effectiveFrom is Required";

    public static final String SERVICECHARGEEFFECTIVETO_MANDATORY_CODE = "wcms.0080";
    public static final String SERVICECHARGEEFFECTIVETO_MANADATORY_FIELD_NAME = "effectiveTo";
    public static final String SERVICECHARGEEFFECTIVETO_MANADATORY_ERROR_MESSAGE = "effectiveTo is Required";

    public static final String SUB_USAGETYPE_INVALID_CODE = "wcms.0081";
    public static final String SUB_USAGETYPE_INVALID_ERROR_MESSAGE = "Please provide valid Sub Usage Type Code";
    public static final String SUB_USAGETYPE_INVALID_FIELD_NAME = "subUsageTypeCode";

    public static final String GAPCODE_NAME_MANDATORY_CODE = "wcms.0082";
    public static final String GAPCODE_NAME_MANDATORY_FIELD_NAME = "name";
    public static final String GAPCODE_NAME_MANDATORY_ERROR_MESSAGE = "name is required";

    public static final String NO_OF_MONTHS_MANDATORY_CODE = "wcms.0083";
    public static final String NO_OF_MONTHS_MANADATORY_FIELD_NAME = "noOfMonths";
    public static final String NO_OF_MONTHS_MANADATORY_ERROR_MESSAGE = "noOfMonths is required";

    public static final String LOGIC_MANDATORY_CODE = "wcms.0084";
    public static final String LOGIC_MANADATORY_FIELD_NAME = "logic";
    public static final String LOGIC_MANADATORY_ERROR_MESSAGE = "logic is required";
    
    public static final String USGTYPE_MANDATORY_CODE = "wcms.0085";
    public static final String USGTYPE_MANADATORY_FIELD_NAME = "name,tenantId";
    public static final String USGTYPE_MANADATORY_ERROR_MESSAGE = "Entered UsageType name and tenantId combination already exists";

    public static final String ZONE = "Zone";
    public static final String REVENUE = "REVENUE";
    public static final String WARD = "Ward";
    public static final String LOCALITY = "Locality";
    public static final String LOCATION = "LOCATION";
    
    public static final String LOCATION_MANDATORY_CODE = "wcms.0086";
    public static final String LOCATION_MANADATORY_FIELD_NAME = "location";
    public static final String LOCATION_MANADATORY_ERROR_MESSAGE = "location is required";
    
    public static final String SUBUSAGETYPE_CODE_MANDATORY_CODE = "wcms.0087";
    public static final String SUBUSAGETYPE_CODE_MANADATORY_FIELD_NAME = "subUsageTypeCode";
    public static final String SUBUSAGETYPE_CODE_MANADATORY_ERROR_MESSAGE = "Sub Usage Type Code is required";
    
    public static final String USAGETYPECODE_CODE_MANDATORY_CODE = "wcms.0088";
    public static final String USAGETYPE_CODE_MANADATORY_FIELD_NAME = "usageTypeCode";
    public static final String USAGETYPE_CODE_MANADATORY_ERROR_MESSAGE = " Usage Type Code is required";
    
    public static final String FROMDATE_TODATE_CODE = "wcms.089";
    public static final String FROMDATE_TODATE_FIELD_NAME = "fromDate,toDate";
    public static final String FROMDATE_TODATE_ERROR_MESSAGE = "From Date should not be greater than the to Date ";
    
    public static final String FROMDATE_CODE = "wcms.0090";
    public static final String FROMDATE_FIELD_NAME = "fromDate";
    public static final String FROMDATE_ERROR_MESSAGE = "From Date is required ";

    public String getErrorMessage(final String property) {
        return environment.getProperty(property);
    }

}
