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
package org.egov.wcms.transaction.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:messages/messages.properties",
        "classpath:messages/errors.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class WcmsConnectionConstants {
    
    @Autowired
    private Environment environment;

    
    public static final String SPECIALDEPOSITECHARGEDEMANDREASON="SPLDEPOSITECHARGE";
    public static final String WATERCONNECTIONDEPOSITETAXHEADREASON="WATERCONNECTIONDEPOSITE";

    public static final String SPECIALSECURITYCHARGEDEMANDREASON="SECURITYDEPOSITECHARGE";
    public static final String ROADCUTCHARGEDEMANDREASON="ROADCUTCHARGE";
    public static final String ESIMATIONCHARGEDEMANDREASON="ESTIMATIONCHARGES";
    public static final String SUPERVISIONCHARGEREASON="SUPERVISIONCHARGE";
    public static final String WATERDEMANDREASONNAME="Water Charge";
    public static final String DONATIONCHARGEANDREASON = "DONATIONCHARGES"; 
    public static final String BUSINESSSERVICE_COLLECTION="WC";
    
    static Map<String, String> reasonMap = new HashMap<>();
    public static Map<Integer, Integer> monthFinMonthMap = new HashMap<Integer, Integer>() {{
        put(1,10);put(2,11);put(3,12);put(4,1);put(5,2);put(6,3);put(7,4);put(8,5);
        put(9,6);put(10,7);put(11,8);put(12,9);
    }};
    
    public static int[] quarters = {0,3,6,9,12};
    public static final int TOTALMONTHS = 12; 
    public static final int HALFYEARMONTHS = 6; 
    public static final int ADDMONTH = 1;
 
    
    
    public static final String CONNECTION_PERSIST_FAILURE = "Something went wrong. Please try again later!" ; 
    
    public static final String NUMBEROFPERSONSDEVIDELOGIC="4";
    
    public static final String NUMBEROFPERSONSADDLOGIC="1";

    public static final String INVALID_REQUEST_MESSAGE = "Request is invalid";
    public static final String INVALID_CONNECTION_REQUEST_MESSAGE = "Connection Request is invalid";
    public static final String INVALID_CONNECTION_DOCUMENT_REQUEST_MESSAGE = "Connection Document Request is invalid"; 
    
    public static final String TENANTID_MANDATORY_CODE = "wcms.0001";
    public static final String TENANTID_MANADATORY_FIELD_NAME = "tenantId";
    public static final String TENANTID_MANADATORY_ERROR_MESSAGE = "Tenant Id is required";

    public static final String ACTIVE_MANDATORY_CODE = "wcms.0002";
    public static final String ACTIVE_MANADATORY_FIELD_NAME = "active";
    public static final String ACTIVE_MANADATORY_ERROR_MESSAGE = "Active is required";
    
    
    public static final String PIPESIZE_HSCSIZEINMM_MANDATORY_CODE = "wcms.003";
    public static final String PIPESIZE_HSCSIZEINMM_MANADATORY_FIELD_NAME = "hscPipeSizeType";
    public static final String PIPESIZE_HSCSIZEINMM_MANADATORY_ERROR_MESSAGE = "H.S.C Pipe Size(mm) is required";


    public static final String PIPESIZE_HSCSIZEINMM_INVALID_CODE = "wcms.0004";
    public static final String PIPESIZE_HSCSIZEINMM_INVALID_FIELD_NAME = "sizeInMilimeter";
    public static final String PIPESIZE_HSCSIZEINMM_INVALID_ERROR_MESSAGE = "Please provide valid H.S.C Pipe Size(mm)";

    public static final String APPLICATION_TYPE_INVALID_CODE = "wcms.0005";
    public static final String APPLICATION_TYPE_INVALID_FIELD_NAME = "applicationType";
    public static final String APPLICATION_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Application Type";

    public static final String BILLING_TYPE_INVALID_CODE = "wcms.0006";
    public static final String BILLING_TYPE_INVALID_FIELD_NAME = "billingType";
    public static final String BILLING_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Billing Type";

    public static final String CONNECTION_TYPE_INVALID_CODE = "wcms.0007";
    public static final String CONNECTION_TYPE_INVALID_FIELD_NAME = "connectionType";
    public static final String CONNECTION_INVALID_ERROR_MESSAGE = "Please provide valid Connection Type";

    public static final String DOCUMENTS_INVALID_CODE = "wcms.0008";
    public static final String DOCUMENTS_INVALID_FIELD_NAME = "documents";
    public static final String DOCUMENTS_INVALID_ERROR_MESSAGE = "Please provide valid Documents";

    public static final String NO_OF_TAPS_INVALID_CODE = "wcms.0009";
    public static final String NO_OF_TAPS_INVALID_FIELD_NAME = "numberOfTaps";
    public static final String NO_OF_TAPS_INVALID_ERROR_MESSAGE = "Please provide valid info for Number of Taps";

    public static final String SOURCE_TYPE_INVALID_CODE = "wcms.0010";
    public static final String SOURCE_TYPE_INVALID_FIELD_NAME = "sourceType";
    public static final String SOURCE_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Source Type";

    public static final String SUMP_CAPACITY_INVALID_CODE = "wcms.0011";
    public static final String SUMP_CAPACITY_INVALID_FIELD_NAME = "sumpCapacity";
    public static final String SUMP_CAPACITY_INVALID_ERROR_MESSAGE = "Please provide valid Sump Capacity";

    public static final String SUPPLY_TYPE_INVALID_CODE = "wcms.0012";
    public static final String SUPPLY_TYPE_INVALID_FIELD_NAME = "supplyType";
    public static final String SUPPLY_TYPE_INVALID_ERROR_MESSAGE = "Please provide valid Supply Type";


    public static final String VALID_APPLICATION_TYPE_MANDATORY_CODE = "wcms.0013";
    public static final String VALID_APPLICATION_TYPE_MANADATORY_FIELD_NAME = "applicationType";
    public static final String VALID_APPLICATION_TYPE_ERROR_MESSAGE = "Please provide valid Application Type";

    public static final String APPLICATION_TYPE_MANDATORY_CODE = "wcms.0014";
    public static final String APPLICATION_TYPE_MANADATORY_FIELD_NAME = "applicationType";
    public static final String APPLICATION_TYPE_ERROR_MESSAGE = "applicationType is required";


    public static final String PROPERTY_INVALID_CODE = "wcms.0015";
    public static final String PROPERTY_INVALID_FIELD_NAME = "propertyIdentifier";
    public static final String PROPERTY_INVALID_ERROR_MESSAGE = "PropertyIdentifier doesnt exist  in Property";
  

    public static final String STATIC_INVALID_CODE = "wcms.0017";
    public static final String STATIC_INVALID_FIELD_NAME = "enums";
    public static final String STATIC_INVALID_ERROR_MESSAGE = "Please check: connection, supply, source or billing type.";


    public static final String DONATION_INVALID_CODE = "wcms.0019";
    public static final String DONATION_INVALID_FIELD_NAME = "donationAmount";
    public static final String DONATION_INVALID_ERROR_MESSAGE = "Donation Amount couldn't be generated, Please check data";

    
    public static final String PROPERTY_IDENTIFIER_CODE = "wcms.0020";
    public static final String PROPERTY_IDENTIFIERFIELD_NAME = "propertyIdentifier";
    public static final String PROPERTY_IDENTIFIER_ERROR_MESSAGE = "Property Identifier Doesnt Exist";
    
    public static final String PIPESIZE_INVALID_CODE = "wcms.0021";
    public static final String PIPESIZE_INVALID_FIELD_NAME = "hscPipeSizeType";
    public static final String PIPESIZE_INVALID_ERROR_MESSAGE = "HscPipeSizeType doesn't Exist";
    
    
    
    public static final String SOURCETYPE_INVALID_CODE = "wcms.0022";
    public static final String SOURCETYPE_INVALID_FIELD_NAME = "sourceType.name";
    public static final String SOURCETYPE_INVALID_ERROR_MESSAGE = "SourceType name doesn't Exist";
    
    public static final String TREATPLANT_INVALID_CODE = "wcms.0023";
    public static final String TREATPLANT_INVALID_FIELD_NAME = "waterTreatment";
    public static final String TREATPLANT_INVALID_ERROR_MESSAGE = "Water Treatment name doesn't Exist";
    
    
    
    public static final String SUPPLYTYPE_INVALID_CODE = "wcms.0024";
    public static final String SUPPLYTYPE_INVALID_FIELD_NAME = "supplyType.name";
    public static final String SUPPLYTYPE_INVALID_ERROR_MESSAGE = "SupplyType name doesn't Exist";
    
    
    public static final String CONNECTION_METERED_INVALID_CODE = "wcms.0025";
    public static final String CONNECTION_METERED_INVALID_FIELD_NAME = "billingType";
    public static final String CONNECTION_METERED_INVALID_ERROR_MESSAGE = "Please provide Meter Deatils for Metered Connection";
    
    public static final String LEGACY_EXECUTIONDATE_INVALID_CODE = "wcms.0028";
    public static final String LEGACY_EXECUTIONDATE_INVALID_FIELD_NAME = "executionDate";
    public static final String LEGACY_EXECUTIONDATE_INVALID_ERROR_MESSAGE = "Please provide executionDate";
    
    public static final String LEGACY_CONNECTION_INVALID_CODE = "wcms.0029";
    public static final String LEGACY_CONNECTION_INVALID_FIELD_NAME = "legacyConsumerNumber";
    public static final String LEGACY_CONNECTION_INVALID_ERROR_MESSAGE = "Legacy ConsumerNumber already Exist";
    
    
    public static final String SUBUSAGETYPE_INVALID_CODE = "wcms.0030";
    public static final String SUBUSAGETYPE_INVALID_FIELD_NAME = "SubusageType";
    public static final String SUBUSAGETYPE_INVALID_ERROR_MESSAGE = "Sub Usage Type don't exist ";
    
    public static final String BOUNDARY_ZONE_INVALID_CODE = "wcms.0031";
    public static final String BOUNDARY_ZONE_INVALID_ERROR_MESSAGE = "Please provide valid Revenue Boundary";
    public static final String BOUNDARY_ZONE_INVALID_FIELD_NAME = "revenueBoundary.code";

    public static final String BOUNDARY_WARD_INVALID_CODE = "wcms.0032";
    public static final String BOUNDARY_WARD_INVALID_ERROR_MESSAGE = "Please provide valid Admin Boundary";
    public static final String BOUNDARY_WARD_INVALID_FIELD_NAME = "adminBoundary.code";

    public static final String BOUNDARY_LOCATION_INVALID_CODE = "wcms.0033";
    public static final String BOUNDARY_LOCATION_INVALID_ERROR_MESSAGE = "Please provide valid Location Boundary";
    public static final String BOUNDARY_LOCATION_INVALID_FIELD_NAME = "locationBoundary.code";
    
    public static final String CONNECTION_METERED_OWNER_INVALID_CODE = "wcms.0034";
    public static final String CONNECTION_METERED_OWNER_INVALID_ERROR_MESSAGE = "Required meter owner";
    public static final String CONNECTION_METERED_OWNER_INVALID_FIELD_NAME = "meterOwner";
    
    public static final String CONNECTION_METERED_NUMBER_INVALID_CODE = "wcms.0035";
    public static final String CONNECTION_METERED_NUMBER_INVALID_ERROR_MESSAGE = "Required meter serial number";
    public static final String CONNECTION_METERED_NUMBER_INVALID_FIELD_NAME = "meterSlNo";
    
    public static final String CONNECTION_METERED_MAXMETERREADING_INVALID_CODE = "wcms.0036";
    public static final String CONNECTION_METERED_MAXMETERREADING_INVALID_ERROR_MESSAGE = "Required maximum meter reading";
    public static final String CONNECTION_METERED_MAXMETERREADING_INVALID_FIELD_NAME = "maximumMeterReading";
    
    public static final String CONNECTION_METERED_METERREADING_INVALID_CODE = "wcms.0037";
    public static final String CONNECTION_METERED_METERREADING_INVALID_ERROR_MESSAGE = "Required valid meter reading";
    public static final String CONNECTION_METERED_METERREADING_INVALID_FIELD_NAME = "reading";
    
    public static final String CONNECTION_METERED_METERREADINGDATE_INVALID_CODE = "wcms.0038";
    public static final String CONNECTION_METERED_METERREADINGDATE_INVALID_ERROR_MESSAGE = "Required valid meter ReadingDate";
    public static final String CONNECTION_METERED_METERREADINGDATE_INVALID_FIELD_NAME = "readingDate";
    
    public static final String CONNECTION_METERED_METERREADINGDETAILS_INVALID_CODE = "wcms.0039";
    public static final String CONNECTION_METERED_METERREADINGDETAILS_INVALID_ERROR_MESSAGE = "Required meterReadings Deatils for Metered Connection";
    public static final String CONNECTION_METERED_METERREADINGDETAILS_INVALID_FIELD_NAME = "meterReadings";
    
    public static final String CONNECTION_METERED_MODEL_INVALID_CODE = "wcms.0040";
    public static final String CONNECTION_METERED_MODEL_INVALID_ERROR_MESSAGE = "Required meter model";
    public static final String CONNECTION_METERED_MODEL_INVALID_FIELD_NAME = "meterModel";
    
    public static final String CONNECTION_METERED_OWNER_WRONG_CODE = "wcms.0041";
    public static final String CONNECTION_METERED_OWNER_WRONG_ERROR_MESSAGE = "Wrong value for Meter Owner";
    public static final String CONNECTION_METERED_OWNER_WRONG_FIELD_NAME = "meterOwner";
    
    public static final String CONNECTION_METERED_MODEL_WRONG_CODE = "wcms.0042";
    public static final String CONNECTION_METERED_MODEL_WRONG_ERROR_MESSAGE = "Wrong value for Meter Model";
    public static final String CONNECTION_METERED_MODEL_WRONG_FIELD_NAME = "meterModel";
    
    public static final String STORAGERESERVOIR_MANDATORY_CODE = "wcms.0043";
    public static final String STORAGERESERVOIR_MANDATORY_ERROR_MESSAGE = "Storage Reservoir Name is required";
    public static final String STORAGERESERVOIR_MANDATORY_FIELD_NAME = "storageReservoir";
    
    public static final String AADHRA_MANDATORY_CODE = "wcms.0044";
    public static final String AADHRA_MANADATORY_FIELD_NAME = "aadhra";
    public static final String AADHRA_MANADATORY_ERROR_MESSAGE = "Aadhra is required";
    
    public static final String STORAGERESERVOIR_INVALID_CODE = "wcms.0045";
    public static final String STORAGERESERVOIR_INVALID_ERROR_MESSAGE = "Storage Reservoir Name does't Exist";
    public static final String STORAGERESERVOIR_INVALID_FIELD_NAME = "storageReservoir";
    
    public static final String CONECTIONTYPE_MANDATORY_CODE = "wcms.0046";
    public static final String CONNECTIONTYPE_MANADATORY_FIELD_NAME = "connectionType";
    public static final String CONNECTIONTYPE_MANADATORY_ERROR_MESSAGE = "ConnectionType is required";
    
    public static final String USAGETYPECODE_MANDATORY_CODE = "wcms.0047";
    public static final String USAGETYPECODE_MANADATORY_FIELD_NAME = "usageType";
    public static final String USAGETYPECODE_MANADATORY_ERROR_MESSAGE = "UsageType is required";
    
    public static final String SUBUSAGETYPECODE_MANDATORY_CODE = "wcms.0048";
    public static final String SUBUSAGETYPECODE_MANADATORY_FIELD_NAME = "subUsageType";
    public static final String SUBUSAGETYPECODE_MANADATORY_ERROR_MESSAGE = "SubUsageType is required";
    
    public static final String USAGETYPE_INVALID_CODE = "wcms.0049";
    public static final String USAGETYPE_INVALID_FIELD_NAME = "usageType";
    public static final String USAGETYPE_INVALID_ERROR_MESSAGE = "Usage Type don't exist ";


    public static final String WORKFLOWTYPES_ACTION_CODE= "wcms.0050";
    public static final String  WORKFLOWTYPES_ACTION_NAME = "action";
    public static final String  WORKFLOWTYPES_ACTION_MESSAGE = "Action is required";


    public static final String WORKFLOWTYPES_DEPARTMENT_CODE= "wcms.0051";
    public static final String  WORKFLOWTYPES_DEPARTMENT_NAME = "department";
    public static final String  WORKFLOWTYPES_DEPARTMENT_MESSAGE = "Department  is required";

    public static final String WORKFLOWTYPES_DESIGNATION_CODE= "wcms.0052";
    public static final String  WORKFLOWTYPES_DESIGNATION_NAME = "designation";
    public static final String  WORKFLOWTYPES_DESIGNATION_MESSAGE = "Designation is required";
    
    public static final String WORKFLOWTYPES_STATUS_CODE= "wcms.0053";
    public static final String  WORKFLOWTYPES_STATUS_NAME = "status";
    public static final String  WORKFLOWTYPES_STATUS_MESSAGE = "Status is required";
    

    
    public static final String WORKFLOWTYPES_ASSIGNEE_CODE= "wcms.0054";
    public static final String  WORKFLOWTYPES_ASSIGNEE_NAME = "assinee";
    public static final String  WORKFLOWTYPES_ASSIGNEE_MESSAGE = "Assignee is required";

    

    
    public static final String CONNECTION_ACKNOWLEDGEMENT_NUMBER_CODE= "wcms.0055";
    public static final String  CONNECTION_ACKNOWLEDGEMENT_NUMBER_MESSAGE = "acknowledgementNumber";
    public static final String  CONNECTION_ACKNOWLEDGEMENT_NUMBER_NAME = "acknowledgementNumber is required";

    
    public static final String DOCUMENTTYPE_REQUIRED_CODE = "wcms.0056";
    public static final String DOCUMENTTYPE_REQUIRED_FIELD_NAME = "documentType";
    public static final String DOCUMENTTYPE_REQUIRED_ERROR_MESSAGE = "documentType is required";
          
    public static final String FILESTOREID_REQUIRED_CODE = "wcms.0057";
    public static final String FILESTOREID_REQUIRED_FIELD_NAME = "fileStoreId";
    public static final String FILESTOREID_REQUIRED_ERROR_MESSAGE = "fileStoreId is required";
          
    public static final String CONNECTION_ID_CODE = "wcms.0058";
    public static final String CONNECTION_ID_NAME = "connectionId";
    public static final String CONNECTION_ID_MESSAGE = "connectionId is Required";
          
    public static final String CONNECTION_REFERENCE_NUMBER_CODE ="wcms.0059";
    public static final String CONNECTION_REFERENCE_NUMBER_MESSAGE = "referenceNumber is Required";
    public static final String CONNECTION_REFERENCE_NUMBER_NAME = "referenceNumber";
    
    public static final String ZONE = "Zone";
    public static final String REVENUE = "REVENUE";
    public static final String WARD = "Ward";
    public static final String LOCALITY = "Locality";
    public static final String LOCATION = "LOCATION";
    public static final String ADMINSTARTION = "ADMINSTRATION";
    
    public static final String CONNECTIONSTATUSACTIVE="ACTIVE";
    public static final String CONNECTIONSTATUSINPROGRESS="INPROGRESS";
    public static final String CONNECTIONSTATUSCREAED="CREATED";
    
    public static final String WORKFLOW_REQUIRED_CONFIG_KEY="WORKFLOW_REQUIRED_OR_NOT";
    
    public static final String HIERACHYTYPEFORWC_CONFIG_KEY="HIERACHYTYPEFORWC";

    private  static final String WATERCHARGETAXHEADCODE="WATERCHARGE";
    
    private  static final String WATERCHARGEPENALTYTAXHEADCODE="WATERCHARGEPENALTY";
    public  static final String WATERCHARGEADVANCE="ADVANCE";
    
    public static final String AADHARNUMBER_REQUIRED="AADHRANUMBER";

    public static final HashMap<String, String> DEMAND_REASON_ORDER_MAP_WITHOUTAVANCE = new HashMap<String, String>() {
        private static final long serialVersionUID = -376251525790947906L;

        {
            put(WATERCHARGETAXHEADCODE,WATERCHARGETAXHEADCODE);
            put(WATERCHARGEPENALTYTAXHEADCODE, WATERCHARGEPENALTYTAXHEADCODE);
        }
};
    
    public static final HashMap<String, String> DEMAND_REASON_ORDER_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = -376251525790947906L;

        {
            put(WATERCHARGETAXHEADCODE,WATERCHARGETAXHEADCODE);
            put(WATERCHARGEPENALTYTAXHEADCODE, WATERCHARGEPENALTYTAXHEADCODE);
            put(WATERCHARGEADVANCE, WATERCHARGEADVANCE);
        }
};

    
    public String getErrorMessage(final String property) {
        return environment.getProperty(property);
    }
    
    public static Map<String, String> getChargeReasonToDisplay() {
    	reasonMap.clear();
    	reasonMap.put(SPECIALDEPOSITECHARGEDEMANDREASON, "Special Deposit Charge and Reason"); 
    	reasonMap.put(SPECIALSECURITYCHARGEDEMANDREASON, "Security Deposit Charge and Reason");
    	reasonMap.put(ROADCUTCHARGEDEMANDREASON,"Road Cut Charge and Reason");
    	reasonMap.put(ESIMATIONCHARGEDEMANDREASON,"Estimation Charges");
    	reasonMap.put(SUPERVISIONCHARGEREASON,"Supervision Charges");
    	reasonMap.put(DONATIONCHARGEANDREASON, "Donation Charges");
    	return reasonMap;
    	
    	
    }

    public static final String ESTIMATION_NOTICE_HTML_CODE = "<p class=\"western\" lang=\"en-IN\"><br />"+
    		"</p><dl><dd><table width=\"620\" cellspacing=\"0\" cellpadding=\"7\" border=2><tbody><tr valign=\"top\"><td width=\"81\"><p lang=\"en-IN\" align=\"center\">"+
    		"<strong>[ulbLogo]</strong></p></td><td width=\"420\"><p lang=\"en-IN\" align=\"center\"><strong>[ulbName]</strong></p>"+
    		"<p lang=\"en-IN\" align=\"center\"><span style=\"font-family: 'Courier New', serif;\"><strong>Water Department /</strong> "+
    		"</span><span style=\"font-family: 'Courier New';\"><span style=\"font-size: small;\"><span lang=\"ar-SA\"><span style=\"color: #212121;\">"+
    		"<span style=\"font-family: Mangal;\"><span lang=\"mr-IN\">???? ?????</span></span></span></span></span></span></p></td><td width=\"75\">"+
    		"<p lang=\"en-IN\" align=\"center\"><strong>[mahLogo]</strong></p></td></tr><tr><td colspan=\"3\" valign=\"top\" width=\"604\">"+
    		"<p lang=\"en-IN\" align=\"center\"><span style=\"font-family: 'Courier New', serif;\"><span style=\"font-size: small;\">"+
    		"<span lang=\"en-US\"><strong>Letter of Intimation/ </strong></span></span></span><span style=\"font-family: 'Courier New';\">"+
    		"<span style=\"font-size: small;\"><span lang=\"ar-SA\"><span style=\"color: #212121;\"><span style=\"font-family: Mangal;\">"+
    		"<span lang=\"mr-IN\">????? ????</span></span></span></span></span></span></p><p lang=\"en-IN\" align=\"right\"><span style=\"font-size: small;\">"+
    		"<span lang=\"en-US\"><strong>Date / </strong></span></span><span style=\"font-size: small;\"><span lang=\"ar-SA\"><span style=\"font-family: Kokila;\"><span style=\"font-size: small;\"><span lang=\"mr-IN\">??????</span></span></span></span></span><span style=\"font-size: small;\">"+
    		"<span lang=\"en-US\"><strong>:  [dateOfLetter]</strong></span></span></p><p lang=\"en-IN\" align=\"right\"><span style=\"font-size: small;\"><span lang=\"en-US\"><strong>No./ </strong></span></span><span style=\"font-size: small;\"><span lang=\"ar-SA\"><span style=\"font-family: Kokila;\"><span style=\"font-size: small;\"><span lang=\"mr-IN\">???????</span></span></span></span></span><span style=\"font-size: small;\"><span lang=\"en-US\"><strong>:  [letterNumber]</strong></span></span></p><p lang=\"en-IN\"><span style=\"font-size: small;\"><span lang=\"en-US\"><strong>To,[letterTo]</strong></span></span></p><p lang=\"en-IN\"><span style=\"font-size: small;\"><span lang=\"en-US\"><strong>Applicant</strong></span></span></p><p> </p><p lang=\"en-IN\"><span style=\"font-size: small;\"><span lang=\"en-US\">"+
    		"Subject : Letter of Intimation for [letterIntimationSubject]</span></span></p><p lang=\"en-IN\"><span style=\"font-size: small;\"><span lang=\"en-US\">Reference : Application No:  [applicationNumber] and Application Date  [applicationDate]"+
    		"</span></span></p><p lang=\"en-IN\"><span style=\"font-size: small;\"><span lang=\"en-US\">Sir/Madam</span></span><span lang=\"en-US\"><strong>[applicantName] has applied for </strong></span><span style=\"font-size: small;\"><span lang=\"en-US\">[serviceName]</span></span><span lang=\"en-US\"><strong> for Water No [waterNo]. Requested to </strong></span><span style=\"font-size: small;\"><span lang=\"en-US\">[serviceName]</span></span><span lang=\"en-US\"><strong>has been approved. Kindly pay the charges which are mentioned below within [slaDays] days.</strong></span></p><p lang=\"en-IN\"><span lang=\"en-US\"><strong>If not paid Application will be rejected or Penalty will be levied.</strong></span></p><p lang=\"en-IN\"><span lang=\"en-US\"><strong>[penaltyChargeDescription1]</strong></span></p><p lang=\"en-IN\">"+
    		"<span lang=\"en-US\"><strong>[penaltyChargeDescription2]</strong></span></p><p lang=\"en-IN\" align=\"right\">"+
    		"<span style=\"font-size: medium;\"><span lang=\"en-US\"><strong>Signing Authority </strong></span></span></p><p lang=\"en-IN\" align=\"right\"><span style=\"font-size: small;\"><span lang=\"ar-SA\"><span style=\"font-size: medium;\"><span lang=\"mr-IN\">???????</span></span></span></span>"+
    		"<span style=\"font-size: medium;\"><span lang=\"en-US\"><strong>,</strong></span></span></p><p lang=\"en-IN\" align=\"right\">"+
    		"<span style=\"font-size: medium;\"><span lang=\"en-US\"><strong>[ulbName]</strong></span></span></p></td></tr></tbody></table>"+
    		"</dd></dl><p class=\"western\" lang=\"en-IN\"><br /></p>";
	
	
	


}
