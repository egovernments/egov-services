/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = {"classpath:messages/messages.properties",
        "classpath:messages/errors.properties"}, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationConstants {

    public static final String MSG_LEAVETYPE_PRESENT = "leaveapplication.leavetype.present";
    public static final String MSG_LEAVETYPE_NOTPRESENT = "leaveapplication.leavetype.notpresent";
    public static final String MSG_FROMDATE_TODATE = "leaveapplication.fromdate.todate";
    public static final String MSG_FROMDATE_CUTOFFDATE = "leaveapplication.fromdate.cutoffdate";
    public static final String MSG_ALREADY_PRESENT = "leaveapplication.already.present";
    public static final String MSG_DATE_HOLIDAY = "leaveapplication.date.holiday";
    public static final String MSG_APPOINTMENT_DATE = "leaveapplication.appointment.date";
    public static final String MSG_RETIREMENT_DATE = "leaveapplication.retirement.date";
    public static final String MSG_COMPENSATORYDATE_PRESENT = "leaveapplication.compensatorydate.present";
    public static final String MSG_ASSIGNMENT_TODATE = "leaveapplication.assignment.todate";
    public static final String MSG_LEAVETYPE_MAXDAYS = "leaveapplication.leavetype.maxdays";
    public static final String MSG_LEAVEALLOTMENT_EXISTS = "leaveallotment.present";
    public static final String MSG_LEAVEAPPLICATION_ENCASHABLE = "leaveapplication.leavetype.encashable";


    @Autowired
    private Environment environment;

    public String getErrorMessage(final String property) {
        return environment.getProperty(property);
    }
}