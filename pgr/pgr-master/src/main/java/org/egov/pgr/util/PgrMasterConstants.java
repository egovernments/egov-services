
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

package org.egov.pgr.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = {"classpath:messages/messages.properties",
        "classpath:messages/errors.properties"}, ignoreResourceNotFound = true)
@Order(0)
public class PgrMasterConstants {

    @Autowired
    private Environment environment;

    public static final String INVALID_REQUEST_MESSAGE = "Request is invalid";
    public static final String INVALID_SERVICEGROUP_REQUEST_MESSAGE = "Service Group is invalid";
    public static final String INVALID_RECEIVING_CENTERTYPE_REQUEST_MESSAGE = "ReceivingCenter is Invalid.";
    public static final String INVALID_RECEIVING_MODETYPE_REQUEST_MESSAGE = "ReceivingMode is Invalid.";
    public static final String INVALID_SERVICETYPE_REQUEST_MESSAGE = "Service Type Message is Invalid";
    public static final String INVALID_ESCALATIONTIMETYPE_REQUEST_MESSAGE = "Escalation Time Type is Invalid";
    public static final String INVALID_ESCALATIONHIERARCHY_REQUEST_MESSAGE = "Escalation Hierarchy is Invalid";


    public static final String TENANTID_MANDATORY_CODE = "pgr.0011";
    public static final String TENANTID_MANADATORY_FIELD_NAME = "tenantId";
    public static final String TENANTID_MANADATORY_ERROR_MESSAGE = "Tenant Id is required";

    public static final String SERVICEGROUP_NAME_MANDATORY_CODE = "pgr.0055";
    public static final String SERVICEGROUP_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String SERVICEGROUP_NAME_MANADATORY_ERROR_MESSAGE = "Service Group name is required";

    public static final String SERVICEGROUP_CODE_MANDATORY_CODE = "pgr.0056";
    public static final String SERVICEGROUP_CODE_MANADATORY_FIELD_NAME = "code";
    public static final String SERVICEGROUP_CODE_MANADATORY_ERROR_MESSAGE = "Service Group code is required";


    public static final String ROUTER_SERVICE_MANDATORY_CODE = "pgr.0003";
    public static final String ROUTER_SERVICE_MANADATORY_FIELD_NAME = "service";
    public static final String ROUTER_SERVICE_MANADATORY_ERROR_MESSAGE = "Service required";

    public static final String ROUTER_POSITION_MANDATORY_CODE = "pgr.0004";
    public static final String ROUTER_POSITION_MANADATORY_FIELD_NAME = "position";
    public static final String ROUTER_POSITION_MANADATORY_ERROR_MESSAGE = "Position is required";

    public static final String ROUTER_BOUNDARY_MANDATORY_CODE = "pgr.0005";
    public static final String ROUTER_BOUNDARY_MANADATORY_FIELD_NAME = "boundary";
    public static final String ROUTER_BOUNDARY_MANADATORY_ERROR_MESSAGE = "Boundary is required";

    public static final String RECEIVINGCENTER_NAME_MANDATORY_CODE = "pgr.0037";
    public static final String RECEIVINGCENTER_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String RECEIVINGCENTER_NAME_MANADATORY_ERROR_MESSAGE = "Receiving Center name is required";

    public static final String RECEIVINGCENTER_CODE_MANDATORY_CODE = "pgr.0038";
    public static final String RECEIVINGCENTER_CODE_MANADATORY_FIELD_NAME = "name";
    public static final String RECEIVINGCENTER_CODE_MANADATORY_ERROR_MESSAGE = "Receiving Center code is required";

    public static final String RECEIVINGCENTER_CODE_EXISTS_CODE = "pgr.0039";
    public static final String RECEIVINGCENTER_CODE_EXISTS_FIELD_NAME = "code";
    public static final String RECEIVINGCENTER_CODE_EXISTS_ERROR_MESSAGE = "Receiving Center code already exists";

    public static final String RECEIVINGMODE_NAME_MANDATORY_CODE = "pgr.0032";
    public static final String RECEIVINGMODE_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String RECEIVINGMODE_NAME_MANADATORY_ERROR_MESSAGE = "Receiving Mode name is required";

    public static final String GRIEVANCETYPE_NAME_MANDATORY_CODE = "pgr.0006";
    public static final String GRIEVANCETYPE_NAME_MANADATORY_FIELD_NAME = "serviceName";
    public static final String GRIEVANCETYPE_NAME_MANADATORY_ERROR_MESSAGE = "Service Type Name is required";

    public static final String GRIEVANCETYPE_CODE_MANDATORY_CODE = "pgr.0054";
    public static final String GRIEVANCETYPE_CODE_MANADATORY_FIELD_NAME = "serviceCode";
    public static final String GRIEVANCETYPE_CODE_MANADATORY_ERROR_MESSAGE = "Service Type Code is required";

    public static final String GRIEVANCETYPE_ID_MANDATORY_CODE = "pgr.0046";
    public static final String GRIEVANCETYPE_ID_MANDATORY_FIELD_NAME = "serviceId";
    public static final String GRIEVANCETYPE_ID_MANDATORY_ERROR_MESSAGE = "Service Type/ Grievance Type Id is required";

    public static final String NO_0F_HOURS_MANDATORY_CODE = "pgr.0047";
    public static final String NO_0F_HOURS_MANADATORY_FIELD_NAME = "noOfHours";
    public static final String NO_0F_HOURS_MANADATORY_ERROR_MESSAGE = "No of hours is required";

    public static final String RECEIVINGCENTER_NAME_UNIQUE_CODE = "pgr.0040";
    public static final String RECEIVINGCENTER_UNQ_ERROR_MESSAGE = "Receiving Center name already exist.";
    public static final String RECEIVINGCENTER_NAME_UNQ_FIELD_NAME = "name";

    public static final String RECEIVINGMODE_CODE_UNIQUE_CODE = "pgr.0034";
    public static final String RECEIVINGMODE_UNQ_ERROR_MESSAGE = "Receiving Mode code already exist.";
    public static final String RECEIVINGMODE_CODE_UNQ_FIELD_NAME = "code";

    public static final String RECEIVINGMODE_CODE_MANDATORY_CODE = "pgr.0031";
    public static final String RECEIVINGMODE_CODE_MANADATORY_FIELD_NAME = "code";
    public static final String RECEIVINGMODE_CODE_MANADATORY_ERROR_MESSAGE = "Receiving Mode code is required";

    public static final String ATTRIBUTE_DETAILS_MANDATORY_CODE = "pgr.0023";
    public static final String ATTRIBUTE_DETAILS_MANADATORY_FIELD_NAME = "attributes";
    public static final String ATTRIBUTE_DETAILS_MANADATORY_ERROR_MESSAGE = "Attributes are mandatory if Metadata is True";

    public static final String SERVICETYPE_TENANTID_NAME_UNIQUE_CODE = "pgr.0024";
    public static final String SERVICETYPE_TENANTID_NAME_UNIQUE_FIELD_NAME = "code";
    public static final String SERVICETYPE_TENANTID_NAME_UNIQUE_ERROR_MESSAGE = "Grievance Type Code already exists";

    public static final String SERVICETYPE_DESCRIPTION_LENGTH_CODE = "pgr.0050";
    public static final String SERVICETYPE_DESCRIPTION_LENGTH_FIELD_NAME = "description";
    public static final String SERVICETYPE_DESCRIPTION_LENGTH_ERROR_MESSAGE = "Description length is greater than 250 characters";

    public static final String CATEGORY_ID_MANDATORY_CODE = "pgr.0025";
    public static final String CATEGORY_ID_MANDATORY_FIELD_NAME = "code";
    public static final String CATEGORY_ID_MANDATORY_ERROR_MESSAGE = "Category Code is mandatory";

    public static final String SLA_HOURS_MANDATORY_CODE = "pgr.0020";
    public static final String SLA_HOURS_MANDATORY_FIELD_NAME = "slaHours";
    public static final String SLA_HOURS_MANDATORY_ERROR_MESSAGE = "SLA Hours is mandatory";

    public static final String RECEIVINGMODE_CHANNEL_VALID_CODE = "pgr.0036";
    public static final String RECEIVINGMODE_CHANNEL_VALID__FIELD_NAME = "channels";
    public static final String RECEIVINGMODE_CHANNEL_VALID_ERROR_MESSAGE = "Channels should be WEB or MOBILE.";

    public static final String RECEIVINGMODE_CHANNEL_MANDATORY_CODE = "pgr.0035";
    public static final String RECEIVINGMODE_CHANNEL_MANADATORY_ERROR_MESSAGE = "Channels Are Required.";
    public static final String RECEIVINGMODE_CHANNEL_MANADATORY_FIELD_NAME = "channels;";

    public static final String FROMPOSITION_MANDATORY_CODE = "pgr.0051";
    public static final String FROMPOSITION_MANADATORY_ERROR_MESSAGE = "From Position is required.";
    public static final String FROMPOSITION_MANADATORY_FIELD_NAME = "fromPosition";

    public static final String TOPOSITION_MANDATORY_CODE = "pgr.0052";
    public static final String TOPOSITION_MANADATORY_ERROR_MESSAGE = "To Position is required.";
    public static final String TOPOSITION_MANADATORY_FIELD_NAME = "toPosition";

    public static final String SERVICEGROUP_CODE_UNIQUE_CODE = "pgr.0058";
    public static final String SERVICEGROUP_CODE_ERROR_MESSAGE = "Service Group Code  already exists";
    public static final String SERVICEGROUP_CODE_FIELD_NAME = "name";

    public static final String SERVICEGROUP_NAME_UNIQUE_CODE = "pgr.0059";
    public static final String SERVICEGROUP_NAME_ERROR_MESSAGE = "Service Group Name already exists";
    public static final String SERVICEGROUP_NAME_FIELD_NAME = "name";

    public static final String GRIEVANCETYPE_NAME_UNIQUE_CODE = "pgr.0027";
    public static final String GRIEVANCETYPE_NAME_UNIQUE_FIELD_NAME = "serviceName";
    public static final String GRIEVANCETYPE_NAME_UNIQUE_ERROR_MESSAGE = "Grievance Type Name already exists";

    public static final String RECEIVINGMODE_NAME_UNIQUE_CODE = "pgr.0033";
    public static final String RECEIVINGMODE_NAME_UNIQUE_ERROR_MESSAGE = "Receiving Mode Name Already Exist.";
    public static final String RECEIVINGMODE_NAME_UNIQUE_FIELD_NAME = "name";

    public static final String FROMTOPOSITION_UNIQUE_CODE = "pgr.0053";
    public static final String FROMTOPOSITION_UNIQUE_ERROR_MESSAGE = "From Position and To Position cannot be same";
    public static final String FROMTOPOSITION_UNIQUE_FIELD_NAME = "toPosition";

    public static final String CODENAME_UNIQUE_CODE = "pgr.0019";
    public static final String CODENAME_UNIQUE_ERROR_MESSAGE = "Combination of code and name already exists";
    public static final String CODENAME_UNIQUE_FIELD_NAME = "code,name";

    public String getErrorMessage(final String property) {
        return environment.getProperty(property);
    }

}