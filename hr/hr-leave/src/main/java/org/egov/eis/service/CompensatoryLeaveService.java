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
import org.egov.eis.model.CompensatoryLeave;
import org.egov.eis.model.LeaveApplication;
import org.egov.eis.model.enums.LeaveStatus;
import org.egov.eis.repository.AttendanceRepository;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.repository.LeaveApplicationRepository;
import org.egov.eis.web.contract.AttendanceResponse;
import org.egov.eis.web.contract.LeaveSearchRequest;
import org.egov.eis.web.contract.EmployeeInfo;
import org.egov.eis.web.contract.EmployeeInfoResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompensatoryLeaveService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HRConfigurationService hrConfigurationService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private HRStatusService hrStatusService;


    public Date getValidFromDate(String tenantId, RequestInfo requestInfo) {
        Date date = new Date();
        String validityDays = hrConfigurationService.getCompensatoryValidityDays(tenantId, requestInfo);
        return StringUtils.isEmpty(validityDays) ? null : new DateTime(date).minusDays(Integer.parseInt(validityDays)).toDate();
    }


    public List<CompensatoryLeave> getCompensatoryLeavesForEmployees(final LeaveSearchRequest leaveSearchRequest, final RequestInfo requestInfo) {


        Date validFromDate = getValidFromDate(leaveSearchRequest.getTenantId(), requestInfo);
        EmployeeInfoResponse employeeResponse;
        List<Long> employeeIds = new ArrayList<>();
        if (!StringUtils.isEmpty(leaveSearchRequest.getCode()) ||
                leaveSearchRequest.getDepartmentId() != null ||
                leaveSearchRequest.getDesignationId() != null ||
                leaveSearchRequest.getEmployeeType() != null) {

            employeeResponse = employeeRepository
                    .getEmployeesForLeaveRequest(leaveSearchRequest, requestInfo);

            employeeIds = employeeResponse.getEmployees().stream().map(employeeInfo -> employeeInfo.getId())
                    .collect(Collectors.toList());
        } else {
            employeeResponse = employeeRepository
                    .getEmployees(requestInfo, leaveSearchRequest.getTenantId());
        }
        List<CompensatoryLeave> compensatoryLeaves = new ArrayList<>();


        Long statusId = hrStatusService.getHRStatuses(LeaveStatus.REJECTED.toString(), leaveSearchRequest.getTenantId(),
                requestInfo).get(0).getId();

        AttendanceResponse attendanceResponse = attendanceRepository.getAttendance(leaveSearchRequest.getTenantId(), validFromDate, employeeIds, requestInfo);
        attendanceResponse.getAttendances().stream().forEach(attendance -> {
            LeaveApplication leaveApplication = leaveApplicationRepository.getLeaveApplicationForDate(attendance.getEmployee(), attendance.getAttendanceDate(), attendance.getTenantId());
            if (leaveApplication == null || (leaveApplication != null && leaveApplication.getStatus() == statusId)) {
                List<EmployeeInfo> employeesInfo = employeeResponse.getEmployees().stream().filter(employeeDetail -> employeeDetail.getId().equals(attendance.getEmployee()))
                        .collect(Collectors.toList());
                CompensatoryLeave compensatoryLeave = new CompensatoryLeave();
                if (employeesInfo.size() > 0) {
                    compensatoryLeave.setEmployeeId(attendance.getEmployee());
                    compensatoryLeave.setTenantId(leaveSearchRequest.getTenantId());
                    compensatoryLeave.setCode(employeesInfo.get(0).getCode());
                    compensatoryLeave.setName(employeesInfo.get(0).getName());
                    compensatoryLeave.setWorkedDate(attendance.getAttendanceDate());
                    if (leaveApplication != null && leaveApplication.getStatus().equals(statusId)) {
                        compensatoryLeave.setStatus("REJECTED");
                    }
                    compensatoryLeaves.add(compensatoryLeave);
                }
            }

        });

        return compensatoryLeaves;
    }

}