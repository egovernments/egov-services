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

package org.egov.eis.web.validator;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeePayscale;
import org.egov.eis.model.PayscaleHeader;
import org.egov.eis.repository.PayscaleRepository;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.PayscaleService;
import org.egov.eis.web.contract.EmployeePayscaleRequest;
import org.egov.eis.web.contract.PayscaleGetRequest;
import org.egov.eis.web.errorhandler.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PayscaleValidator {

    @Autowired
    private PayscaleRepository payscaleRepository;

    @Autowired
    private PayscaleService payscaleService;

    @Autowired
    private EmployeeService employeeService;

    public void validatePayscaleRequest(PayscaleHeader payscaleHeader, Errors errors) {
        if (payscaleRepository.checkIfPayscaleExists(payscaleHeader.getId(), payscaleHeader.getPayscale(), payscaleHeader.getTenantId()))
            errors.rejectValue("payscaleHeader.payscale", "invalid",
                    "Payscale already exists.");
    }

    public void validateEmpPayscaleReuest(List<EmployeePayscale> employeePayscale, RequestInfo requestInfo, Errors errors) {
        for (int index = 0; index < employeePayscale.size(); index++) {

            PayscaleGetRequest payscaleGetRequest = new PayscaleGetRequest();
            payscaleGetRequest.setId(new ArrayList<>(Arrays.asList(employeePayscale.get(index).getPayscaleHeader().getId())));
            payscaleGetRequest.setTenantId(employeePayscale.get(index).getTenantId());

            PayscaleHeader payscaleHeader = payscaleService.getPayscaleHeaders(payscaleGetRequest, requestInfo).get(0);

            Employee employee = employeeService.getEmployee(employeePayscale.get(index).getEmployee().getId(), employeePayscale.get(index).getTenantId(), requestInfo);

            if ((employeePayscale.get(index).getBasicAmount() < payscaleHeader.getAmountFrom()) || (employeePayscale.get(index).getBasicAmount() > payscaleHeader.getAmountTo()))
                errors.rejectValue("employeePayscale[" + index + "]", "invalid",
                        "Basic Amount should be between " + payscaleHeader.getAmountFrom() + " to " + payscaleHeader.getAmountTo());

            if (employee.getDateOfAppointment() != null && !employee.getDateOfAppointment().equals("") && employeePayscale.get(index).getEffectiveFrom().before(employee.getDateOfAppointment()))
                errors.rejectValue("employeePayscale[" + index + "]", "invalid",
                        "Effective from should be greater than employee appointment date");

        }
    }
}
