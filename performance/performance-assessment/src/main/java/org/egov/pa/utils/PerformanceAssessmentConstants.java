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
package org.egov.pa.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:messages/messages.properties",
        "classpath:messages/errors.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class PerformanceAssessmentConstants {
    
    @Autowired
    private Environment environment;
    
    public static final String PERIODICITY_DEFAULT = "MONTHLY";
    public static final List<String> MONTHLIST = Arrays.asList("4","5","6","7","8","9","10","11","12","1","2","3");  

    
    public static final String PERSIST_FAILURE = "Something went wrong. Please try again later!" ; 
    
    public static final String INVALID_REQUEST_MESSAGE = "Request is invalid";
    public static final String INVALID_DOCUMENT_REQUEST_MESSAGE = "Document Request is invalid"; 
    
    public static final String TENANTID_MANDATORY_CODE = "pa.0001";
    public static final String TENANTID_MANADATORY_FIELD_NAME = "tenantId";
    public static final String TENANTID_MANADATORY_ERROR_MESSAGE = "Tenant Id is required";
    
    public static final String KPIID_UNIQUE_CODE = "pa.0002";
    public static final String KPIID_UNIQUE_FIELD_NAME = "id";
    public static final String KPIID_UNIQUE_ERROR_MESSAGE = "KPI ID should be 0 in the case of new KPI";
    
    public static final String KPINAME_MANDATORY_CODE = "pa.0003";
    public static final String KPINAME_MANDATORY_FIELD_NAME = "name";
    public static final String KPINAME_MANDATORY_ERROR_MESSAGE = "KPI Name is mandatory";
    
    public static final String KPICODE_MANDATORY_CODE = "pa.0004";
    public static final String KPICODE_MANDATORY_FIELD_NAME = "code";
    public static final String KPICODE_MANDATORY_ERROR_MESSAGE = "KPI Code is mandatory";
    
    public static final String FINYEAR_MANDATORY_CODE = "pa.0005";
    public static final String FINYEAR_MANDATORY_FIELD_NAME = "finYear";
    public static final String FINYEAR_MANDATORY_ERROR_MESSAGE = "Financial Year is mandatory";
    
    public static final String DOCNAME_MANDATORY_CODE = "pa.0006";
    public static final String DOCNAME_MANDATORY_FIELD_NAME = "name";
    public static final String DOCNAME_MANDATORY_ERROR_MESSAGE = "Document Name is mandatory";
    
    public static final String DOCCODE_MANDATORY_CODE = "pa.0007";
    public static final String DOCCODE_MANDATORY_FIELD_NAME = "code";
    public static final String DOCCODE_MANDATORY_ERROR_MESSAGE = "Document Code is mandatory";
    
    public static final String DOCDESC_MANDATORY_CODE = "pa.0008";
    public static final String DOCDESC_MANDATORY_FIELD_NAME = "description";
    public static final String DOCDESC_MANDATORY_ERROR_MESSAGE = "Document Description is mandatory";
    
    public static final String DOCACTIVE_MANDATORY_CODE = "pa.0010";
    public static final String DOCACTIVE_MANDATORY_FIELD_NAME = "active";
    public static final String DOCACTIVE_MANDATORY_ERROR_MESSAGE = "Document Active Status is mandatory";
    
    public static final String TARGETDESC_MANDATORY_CODE = "pa.0011";
    public static final String TARGETDESC_MANDATORY_FIELD_NAME = "targetValue";
    public static final String TARGETDESC_MANDATORY_ERROR_MESSAGE = "Target Value is mandatory for the KPI!";
    
    public static final String TARGETINSTRUCTIONS_MANDATORY_CODE = "pa.0012";
    public static final String TARGETINSTRUCTIONS_MANDATORY_FIELD_NAME = "instructions";
    public static final String TARGETINSTRUCTIONS_MANDATORY_ERROR_MESSAGE = "Target Instructions are mandatory if Target Value is specified";
    
    public static final String NAMECODE_UNIQUE_CODE = "pa.0013";
    public static final String NAMECODE_UNIQUE_FIELD_NAME = "name";
    public static final String NAMECODE_UNIQUE_ERROR_MESSAGE = "KPI Name or Code already exists. Please check!";
    
    public static final String ACTUALVALUE_MANDATORY_CODE = "pa.0014";
    public static final String ACTUALVALUE_MANDATORY_FIELD_NAME = "actualValue";
    public static final String ACTUALVALUE_MANDATORY_ERROR_MESSAGE = "Actual Value is Mandatory";
    
    public static final String KPICODE_INVALID_CODE = "pa.0015";
    public static final String KPICODE_INVALID_FIELD_NAME = "kpiCode";
    public static final String KPICODE_INVALID_ERROR_MESSAGE = "KPI Code is not valid. Please check!";
    
    public static final String KPIVALUES_MANDATORY_CODE = "pa.0016";
    public static final String KPIVALUES_MANDATORY_FIELD_NAME = "kpiValues";
    public static final String KPIVALUES_MANDATORY_ERROR_MESSAGE = "KPI Values are mandatory";

    public static final String TENANT_INVALID_CODE = "pa.0016";
    public static final String TENANT_INVALID_FIELD_NAME = "tenantId";
    public static final String TENANT_INVALID_ERROR_MESSAGE = "Tenant ID is not valid. Please check!";
    
    public static final String TARGET_UNAVAILABLE_CODE = "pa.0017";
    public static final String TARGET_UNAVAILABLE_FIELD_NAME = "targetValue";
    public static final String TARGET_UNAVAILABLE_ERROR_MESSAGE = "Target has not been added for this KPI. Please contact Admin";
    
    public static final String CODE_TENANT_UNIQUE_CODE = "pa.0018";
    public static final String CODE_TENANT_UNIQUE_FIELD_NAME = "kpiCode";
    public static final String CODE_TENANT_UNIQUE_ERROR_MESSAGE = "Actual Value for this Tenant and KPI Code is already available. Try updating";
    
    public static final String SEARCH_PARAMETERS_INVALID_CODE = "pa.0019";
    public static final String SEARCH_PARAMETERS_INVALID_FIELD_NAME = "parameters";
    public static final String SEARCH_PARAMETERS_INVALID_ERROR_MESSAGE = "Search cannot be performed with these parameter range";
    
    public static final String DEPARTMENT_MANDATORY_CODE = "pa.0020";
    public static final String DEPARTMENT_MANDATORY_FIELD_NAME = "department";
    public static final String DEPARTMENT_MANDATORY_ERROR_MESSAGE = "Department is Mandatory!";
    
    public static final String DEPARTMENT_CODE_MANDATORY_CODE = "pa.0021";
    public static final String DEPARTMENT_CODE_MANDATORY_FIELD_NAME = "code";
    public static final String DEPARTMENT_CODE_MANDATORY_ERROR_MESSAGE = "Department Code is Mandatory!";
    
    public static final String DEPARTMENT_ID_MANDATORY_CODE = "pa.0022";
    public static final String DEPARTMENT_ID_MANDATORY_FIELD_NAME = "id";
    public static final String DEPARTMENT_ID_MANDATORY_ERROR_MESSAGE = "Department ID is Mandatory!";
    
    public static final String KPICODE_SEARCH_MANDATORY_CODE = "pa.0023";
    public static final String KPICODE_SEARCH_MANDATORY_FIELD_NAME = "kpiCodes";
    public static final String KPICODE_SEARCH_MANDATORY_ERROR_MESSAGE = "KPI Codes List is Mandatory!";
    
    public static final String FINYEAR_SEARCH_MANDATORY_CODE = "pa.0024";
    public static final String FINYEAR_SEARCH_MANDATORY_FIELD_NAME = "finYear";
    public static final String FINYEAR_SEARCH_MANDATORY_ERROR_MESSAGE = "Financial Year List is Mandatory!";
    
    public static final String TENANTID_SEARCH_MANDATORY_CODE = "pa.0025";
    public static final String TENANTID_SEARCH_MANDATORY_FIELD_NAME = "tenantId";
    public static final String TENANTID_SEARCH_MANDATORY_ERROR_MESSAGE = "Tenant ID List is Mandatory!";
    
    public static final String ACTUAL_VALUE_ALREADY_EXISTS_CODE = "pa.0026";
    public static final String ACTUAL_VALUE_ALREADY_EXISTS_FIELD_NAME = "resultValue";
    public static final String ACTUAL_VALUE_ALREADY_EXISTS_ERROR_MESSAGE = "Actual Value for this KPI already exisits. Use update screen to update the same!";
    
    public static final String MANDATORY_DOCS_REQUIRED_CODE = "pa.0027";
    public static final String MANDATORY_DOCS_REQUIRED_FIELD_NAME = "documents";
    public static final String MANDATORY_DOCS_REQUIRED_ERROR_MESSAGE = "Mandatory Documents are missing for the KPI Value Entry. Please upload and try again!";
    
    public static final String VALUE_LIST_REQUIRED_CODE = "pa.0028";
    public static final String VALUE_LIST_REQUIRED_FIELD_NAME = "valueList";
    public static final String VALUE_LIST_REQUIRED_ERROR_MESSAGE = "KPI Value List is mandatory";
    
    public static final String VALUE_DETAIL_INVALID_CODE = "pa.0029";
    public static final String VALUE_DETAIL_INVALID_FIELD_NAME = "valueid";
    public static final String VALUE_DETAIL_INVALID_ERROR_MESSAGE = "Value Detail is not valid";
    
    public static final String VALUE_PERIOD_INVALID_CODE = "pa.0030";
    public static final String VALUE_PERIOD_INVALID_FIELD_NAME = "period";
    public static final String VALUE_PERIOD_INVALID_ERROR_MESSAGE = "Value Period is not valid";
    
    public static final String TARGET_EXISTS_CODE = "pa.0031";
    public static final String TARGET_EXISTS_FIELD_NAME = "targetType";
    public static final String TARGET_EXISTS_ERROR_MESSAGE = "Target Type cannot be updated as the Target Value has already been added for this KPI";
    
    public static final String TARGETFINYEAR_UNAVAILABLE_CODE = "pa.0032";
    public static final String TARGETFINYEAR_UNAVAILABLE_FIELD_NAME = "finYear";
    public static final String TARGETFINYEAR_UNAVAILABLE_ERROR_MESSAGE = "Target Financial Year is mandatory for KPI Target Record";
    
    public static final String TARGETVALUE_INVALID_CODE = "pa.0033";
    public static final String TARGETVALUE_INVALID_FIELD_NAME = "targetValue";
    public static final String TARGETVALUE_INVALID_ERROR_MESSAGE = "Target Value cannot be set as ZERO!";
    
    public static final String TARGETUPDATE_INVALID_CODE = "pa.0034";
    public static final String TARGETUPDATE_INVALID_FIELD_NAME = "targetValue";
    public static final String TARGETUPDATE_INVALID_ERROR_MESSAGE = "Actual Values entry has already been started for this KPI. Cannot update the Target Now!!!";
        
    public String getErrorMessage(final String property) {
        return environment.getProperty(property);
    }

}
