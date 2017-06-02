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

    public static final String INVALID_USAGETYPE_REQUEST_MESSAGE = "UsageType is invalid";
    public static final String INVALID_CATEGORY_REQUEST_MESSAGE = "Category is invalid";
    public static final String INVALID_PIPESIZE_REQUEST_MESSAGE = "PipeSize is invalid";
    public static final String INVALID_DONATION_REQUEST_MESSAGE = "Donation Request is invalid";
    public static final String INVALID_PROPERTYUSAGETYPE_REQUEST_MESSAGE = "Property Type or Usage Type is invalid";
    public static final String INVALID_PROPERTY_PIPESIZE_REQUEST_MESSAGE = "PropertyPipeSize is invalid";

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

    public static final String CATEGORY_NAME_UNIQUE_CODE = "wcms.0005";
    public static final String CATEGORY_NAME_UNQ_FIELD_NAME = "name";
    public static final String CATEGORY_UNQ_ERROR_MESSAGE = "Entered Category Type already exist";

    public static final String CATEGORY_NAME_MANDATORY_CODE = "wcms.0006";
    public static final String CATEGORY_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String CATEGORY_NAME_MANADATORY_ERROR_MESSAGE = "Category Type is required";

    public static final String PIPESIZE_SIZEINMM_MANDATORY_CODE = "wcms.0007";
    public static final String PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME = "sizeInMilimeter";
    public static final String PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE = "H.S.C Pipe Size(mm) is required";

    public static final String PIPESIZE_SIZEINMM_UNIQUE_CODE = "wcms.0008";
    public static final String PIPESIZE_SIZEINMM__UNQ_FIELD_NAME = "sizeInMilimeter";
    public static final String PIPESIZE_SIZEINMM_UNQ_ERROR_MESSAGE = "Entered H.S.C Pipe Size(mm) already exist.";

    public static final String PROPERTY_TYPE_MANDATORY_CODE = "wcms.0009";
    public static final String PROPERTY_TYPE_MANDATORY_FIELD_NAME = "name";
    public static final String PROPERTY_TYPE_MANDATORY_ERROR_MESSAGE = "Property Type is required";

    public static final String PROPERTYTYPE_MANDATORY_CODE = "wcms.0010";
    public static final String PROPERTYTYPE_MANDATORY_FIELD_NAME = "propertyType";
    public static final String PROPERTYTYPE_MANDATORY_ERROR_MESSAGE = "Property Type is Required";

    public static final String PROPERTYTYPE_USAGETYPE_UNIQUE_CODE = "wcms.0011";
    public static final String PROPERTYTYPE_USAGETYPE_UNQ_FIELD_NAME = "propertyType";
    public static final String PROPERTYTYPE_USAGETYPE_UNQ_ERROR_MESSAGE = "Entered combination of Property Type and Usage Type has already been mapped";

    public static final String DONATION_MANDATORY_CODE = "wcms.0012";
    public static final String DONATION_MANDATORY_FIELD_NAME = "donationAmount";
    public static final String DONATION_MANDATORY_ERROR_MESSAGE = "Donation Amount is required";

    public static final String FROMTO_MANDATORY_CODE = "wcms.0013";
    public static final String FROMTO_MANDATORY_FIELD_NAME = "fromDate";
    public static final String FROMTO_MANDATORY_ERROR_MESSAGE = "From and To Date are required";

    public static final String PROPERTY_PIPESIZE_PROPERTYTYPE_MANDATORY_CODE = "wcms.0014";
    public static final String PROPERTY_PIPESIZE_PROPERTYTYPE_MANADATORY_ERROR_MESSAGE = "propertyType";
    public static final String PROPERTY_PIPESIZE_PROPERTYTYPE_MANADATORY_FIELD_NAME = "PropertyType is required";

    public static final String PROPERTY_PIPESIZE_HSCSIZEINMM_MANDATORY_CODE = "wcms.0015";
    public static final String PROPERTY_PIPESIZE_HSCSIZEINMM_MANADATORY_FIELD_NAME = "pipeSizeType";
    public static final String PROPERTY_PIPESIZE_HSCSIZEINMM_MANADATORY_ERROR_MESSAGE = "H.S.C Pipe Size(mm) is required";

    public static final String PROPERTY_PIPESIZE_SIZEINMM_UNIQUE_CODE = "wcms.0016";
    public static final String PROPERTY_PIPESIZE_SIZEINMM_UNQ_FIELD_NAME = "pipeSize,propertyType";
    public static final String PROPERTY_PIPESIZE_SIZEINMM_UNQ_ERROR_MESSAGE = "Selected Property Type and PipeSize already exists.";

    public static final String PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_CODE = "wcms.0017";
    public static final String PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_FIELD_NAME = "pipeSize";
    public static final String PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_ERROR_MESSAGE = "Please provide valid H.S.C Pipe Size(mm)";

    @Autowired
    private Environment environment;

    public String getErrorMessage(final String property) {
        return environment.getProperty(property);
    }

}
