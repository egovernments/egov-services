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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.eis.model.Attendance;
import org.egov.eis.model.AttendanceType;
import org.egov.eis.producers.AttendanceProducer;
import org.egov.eis.repository.AttendanceRepository;
import org.egov.eis.web.contract.AttendanceGetRequest;
import org.egov.eis.web.contract.AttendanceRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class AttendanceService {
    public static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceProducer attendanceProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HolidayService holidayService;

    public List<Attendance> getAttendances(final AttendanceGetRequest attendanceGetRequest) throws ParseException {
        return attendanceRepository.findForCriteria(attendanceGetRequest);
    }

    public List<Attendance> findAttendanceForHolidays(final AttendanceGetRequest attendanceGetRequest, RequestInfo requestInfo) throws ParseException {
        List<Date> holidayDates = getHolidayDates(attendanceGetRequest, requestInfo);
        return attendanceRepository.findByCriteria(attendanceGetRequest, holidayDates);
    }

    private List<Date> getHolidayDates(AttendanceGetRequest attendanceGetRequest, RequestInfo requestInfo) {
        return holidayService.getHolidaysForDate(attendanceGetRequest.getTenantId(), requestInfo, attendanceGetRequest.getFromDate());
    }

    /*
     * This method is used to create new attendance
     * @return Attendance, return the attendance details with current status
     * @param attendance, hold attendance details
     */

    public List<Attendance> createAsync(final AttendanceRequest attendanceRequest) {
        String attendanceValue = null;
        try {
            logger.info("createAttendance service::" + attendanceRequest);
            attendanceValue = objectMapper.writeValueAsString(attendanceRequest);
            logger.info("attendanceValue::" + attendanceValue);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            attendanceProducer.sendMessage("egov-hr-attendance", "save-attendance", attendanceValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return attendanceRequest.getAttendances();
    }

    public boolean getByEmployeeAndDate(final Long employee, final Date attendanceDate, final String tenantId) {
        return attendanceRepository.checkAttendanceByEmployeeAndDate(employee, attendanceDate, tenantId);
    }

    public AttendanceRequest create(final AttendanceRequest attendanceRequest) {
        return attendanceRepository.saveAttendance(attendanceRequest);
    }

    public AttendanceType getAttendanceTypeByCode(final String code, final String tenantId) {
        return attendanceRepository.findAttendanceTypeByCode(code, tenantId);
    }

    public List<AttendanceType> getAllAttendanceTypes(final String tenantId) {
        return attendanceRepository.findAllAttendanceTypes(tenantId);
    }
}