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

package org.egov.eis.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.EmployeePayscale;
import org.egov.eis.repository.EmployeePayscaleRepository;
import org.egov.eis.repository.PayscaleRepository;
import org.egov.eis.web.contract.EmployeePayscaleGetRequest;
import org.egov.eis.web.contract.EmployeePayscaleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class EmployeePayscaleService {

    @Autowired
    private EmployeePayscaleRepository employeePayscaleRepository;

    @Autowired
    private PayscaleRepository payscaleRepository;

    public List<EmployeePayscale> create(EmployeePayscaleRequest employeePayscaleRequest) {
        List<EmployeePayscale> employeePayscales = employeePayscaleRequest.getEmployeePayscale();
        RequestInfo requestInfo = employeePayscaleRequest.getRequestInfo();
        Long requesterId = requestInfo.getUserInfo().getId();
        populateDataForUpdate(employeePayscales, requesterId);
        updateEmpPayscale(employeePayscales);
        return employeePayscales;
    }

    private void populateDataForUpdate(List<EmployeePayscale> employeePayscales, Long requesterId) {
        employeePayscales.forEach((employeePayscale) -> {
            if (isEmpty(employeePayscale.getId())) {
                employeePayscale.setId(payscaleRepository.generateSequence("seq_egeis_employeepayscale"));
                employeePayscale.setCreatedDate(new Date());
                employeePayscale.setLastModifiedDate(new Date());
                employeePayscale.setCreatedBy(requesterId);
                employeePayscale.setLastModifiedBy(requesterId);
            }
        });
    }

    public List<EmployeePayscale> getEmpPayscale(final EmployeePayscaleGetRequest employeePayscaleGetRequest,
                                                 final RequestInfo requestInfo) {
        return employeePayscaleRepository.findForCriteria(employeePayscaleGetRequest, requestInfo);
    }

    public void updateEmpPayscale(List<EmployeePayscale> employeePayscale) {
        EmployeePayscaleGetRequest employeePayscaleGetRequest = new EmployeePayscaleGetRequest();
        employeePayscaleGetRequest.setEmployee(employeePayscale.get(0).getEmployee().getId());
        employeePayscaleGetRequest.setTenantId(employeePayscale.get(0).getTenantId());
        List<EmployeePayscale> employeePayscaleFromDB = employeePayscaleRepository.findForCriteria(employeePayscaleGetRequest, new RequestInfo());
        employeePayscale.forEach((empPayscale) -> {
            if (insertEmpPayscale(empPayscale, employeePayscaleFromDB)) {
                employeePayscaleRepository.saveEmployeePayscale(empPayscale);
            } else if (updateEmpPay(empPayscale, employeePayscaleFromDB)) {
                employeePayscaleRepository.updateEmpPayscale(empPayscale);
            }
        });
        deleteEmpPayscaleInDBThatAreNotInInput(employeePayscale, employeePayscaleFromDB, employeePayscale.get(0).getEmployee().getId(),
                employeePayscale.get(0).getTenantId());
    }

    private boolean insertEmpPayscale(EmployeePayscale employeePayscale, List<EmployeePayscale> employeePayscaleList) {
        for (EmployeePayscale oldEmpPay : employeePayscaleList)
            if (employeePayscale.getId().equals(oldEmpPay.getId()))
                return false;
        return true;
    }

    private boolean updateEmpPay(EmployeePayscale employeePayscale, List<EmployeePayscale> employeePayscaleList) {
        for (EmployeePayscale oldEmpPay : employeePayscaleList)
            if (employeePayscale.equals(oldEmpPay))
                return false;
        return true;
    }


    private List<EmployeePayscale> getListOfEmpPayscalesTDelete(List<EmployeePayscale> inputEmpPayscale,
                                                                List<EmployeePayscale> empPayscaleFromDb) {
        List<EmployeePayscale> empPayscalesToDelete = new ArrayList<>();
        for (EmployeePayscale empPayscaleInDb : empPayscaleFromDb) {
            boolean found = false;
            if (!isEmpty(inputEmpPayscale)) {
                for (EmployeePayscale inputEmployeePayscale : inputEmpPayscale)
                    if (inputEmployeePayscale.getId().equals(empPayscaleInDb.getId())) {
                        found = true;
                        break;
                    }
            }
            if (!found)
                empPayscalesToDelete.add(empPayscaleInDb);
        }
        return empPayscalesToDelete;
    }

    private void deleteEmpPayscaleInDBThatAreNotInInput(List<EmployeePayscale> inputEmpPayscale,
                                                        List<EmployeePayscale> empPayscaleFromDb, Long employeeId, String tenantId) {
        List<EmployeePayscale> empPayscaleToDelete = getListOfEmpPayscalesTDelete(inputEmpPayscale, empPayscaleFromDb);
        List<Long> empPayscaleIdsToDelete = empPayscaleToDelete.stream().map(EmployeePayscale::getId)
                .collect(Collectors.toList());
        if (!empPayscaleIdsToDelete.isEmpty()) {
            employeePayscaleRepository.deleteEmpPayscale(empPayscaleIdsToDelete, employeeId, tenantId);
        }
    }
}
