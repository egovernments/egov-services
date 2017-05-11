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

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.upperCase;

@Configuration
@PropertySource(value = { "classpath:messages/messages.properties",
            "classpath:messages/errors.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class WcmsConstants {


    public static final String INVALID_USAGETYPE_REQUEST_MESSAGE = "UsageType is invalid";
    public static final String INVALID_CATEGORY_REQUEST_MESSAGE = "Category is invalid";

    public static final String USAGETYPE_NAME_UNIQUE_CODE = "wcms.0001";
    public static final String USAGETYPE_NAME_FIELD_NAME = "name";
    public static final String USAGETYPE_UNQ_ERROR_MESSAGE = "Entered Usage Type already exist";

    public static final String USAGETYPE_NAME_MANDATORY_CODE = "wcms.0002";
    public static final String USAGETYPE_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE = "Usage Type is required";

    public static final String TEANANTID_MANDATORY_CODE = "wcms.0003";
    public static final String TEANANTID_MANADATORY_FIELD_NAME = "tenantId";
    public static final String TEANANTID_MANADATORY_ERROR_MESSAGE = "Tenant Id is required";

    public static final String ACTIVE_MANDATORY_CODE = "wcms.0004";
    public static final String ACTIVE_MANADATORY_FIELD_NAME = "active";
    public static final String ACTIVE_MANADATORY_ERROR_MESSAGE = "Active is required";

    public static final String CATEGORY_NAME_UNIQUE_CODE = "wcms.0005";
    public static final String CATEGORY_NAME_FIELD_NAME = "name";
    public static final String CATEGORY_UNQ_ERROR_MESSAGE = "Entered Category Type already exist";

    public static final String CATEGORY_NAME_MANDATORY_CODE = "wcms.0006";
    public static final String CATEGORY_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String CATEGORY_NAME_MANADATORY_ERROR_MESSAGE = "Category Type is required";


    @Autowired
        private Environment environment;

        public String getErrorMessage(final String property) {
            return environment.getProperty(property);
        }


}
