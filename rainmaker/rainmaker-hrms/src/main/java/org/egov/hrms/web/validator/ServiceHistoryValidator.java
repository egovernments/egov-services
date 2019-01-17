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

package org.egov.hrms.web.validator;

import org.egov.hrms.model.Employee;
import org.egov.hrms.model.ServiceHistory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ServiceHistoryValidator implements Validator {

    /**
     * This Validator validates *just* Employee instances
     */
    @Override
    public boolean supports(Class<?> paramClass) {
        return Employee.class.equals(paramClass);
    }

    @Override
    public void validate(Object targetObject, Errors errors) {
        if (!(targetObject instanceof Employee)) {

            return;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd MMMMM, yyyy");

        Employee employee = (Employee) targetObject;
        List<ServiceHistory> serviceHistories = employee.getServiceHistory();

		for (int index = 0; index < serviceHistories.size(); index++) {
                if (serviceHistories.get(index).getServiceFrom().compareTo(serviceHistories.get(index).getServiceTo()) > 0) {
                    errors.rejectValue("employee.serviceHistory[" + index + "]", "invalid",
                            "Service History From Date " + dateFormat.format(serviceHistories.get(index).getServiceFrom())
                                    + " Can Not Be Greater Than To Date "
                                    + dateFormat.format(serviceHistories.get(index).getServiceTo())
                                    + ". Please Enter Correct Dates");
                }
            }


            // check if assignmentDates are overlapping for primary assignments
            for (int i = 0; i < serviceHistories.size(); i++) {
                for (int j = i + 1; j < serviceHistories.size(); j++) {
                    Long ith_fromDate = serviceHistories.get(i).getServiceFrom().getTime();
                    Long jth_fromDate = serviceHistories.get(j).getServiceFrom().getTime();
                    Long ith_toDate = serviceHistories.get(i).getServiceTo().getTime();
                    Long jth_toDate = serviceHistories.get(j).getServiceTo().getTime();

                    if (ith_fromDate >= jth_fromDate && ith_fromDate <= jth_toDate) {
                        errors.rejectValue("employee.serviceHistory[" + i + "].serviceFrom", "invalid",
                                "Service History From Date " + dateFormat.format(new Date(ith_fromDate))
                                        + " Is Overlapping With Dates " + dateFormat.format(new Date(jth_fromDate))
                                        + " & " + dateFormat.format(new Date(jth_toDate))
                                        + " Of Other Service History." + " Please Enter Correct Dates");
                    }

                    if (ith_toDate >= jth_fromDate && ith_toDate <= jth_toDate) {
                        errors.rejectValue("employee.serviceHistory[" + i + "].serviceTo", "invalid",
                                "Service History To Date " + dateFormat.format(new Date(ith_toDate))
                                        + " Is Overlapping With Dates " + dateFormat.format(new Date(jth_fromDate))
                                        + " & " + dateFormat.format(new Date(jth_toDate))
                                        + " Of Other Service History." + " Please Enter Correct Dates");
                    }

                    if (jth_fromDate >= ith_fromDate && jth_fromDate <= ith_toDate) {
                        errors.rejectValue("employee.serviceHistory[" + j + "].serviceFrom", "invalid",
                                "Service History From Date " + dateFormat.format(new Date(jth_fromDate))
                                        + " Is Overlapping With Dates " + dateFormat.format(new Date(ith_fromDate))
                                        + " & " + dateFormat.format(new Date(ith_toDate))
                                        + " Of Other Service History." + " Please Enter Correct Dates");
                    }

                    if (jth_toDate >= ith_fromDate && jth_toDate <= ith_toDate) {
                        errors.rejectValue("employee.serviceHistory[" + j + "].serviceTo", "invalid",
                                "Service History To Date " + dateFormat.format(new Date(jth_toDate))
                                        + " Is Overlapping With Dates " + dateFormat.format(new Date(ith_fromDate))
                                        + " & " + dateFormat.format(new Date(ith_toDate))
                                        + " Of Other Service History." + " Please Enter Correct Dates");
                    }
                }
            }
        }

    }