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

package org.egov.eis.repository;

import org.egov.eis.model.Attendance;
import org.egov.eis.model.AttendanceType;
import org.egov.eis.repository.builder.AttendanceQueryBuilder;
import org.egov.eis.repository.rowmapper.AttendanceReportRowMapper;
import org.egov.eis.repository.rowmapper.AttendanceRowMapper;
import org.egov.eis.repository.rowmapper.AttendanceTypeRowMapper;
import org.egov.eis.web.contract.AttendanceGetRequest;
import org.egov.eis.web.contract.AttendanceReportRequest;
import org.egov.eis.web.contract.AttendanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AttendanceRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(AttendanceRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AttendanceRowMapper attendanceRowMapper;

    @Autowired
    private AttendanceTypeRowMapper attendanceTypeRowMapper;

    @Autowired
    private AttendanceReportRowMapper attendanceReportRowMapper;

    @Autowired
    private AttendanceQueryBuilder attendanceQueryBuilder;

    public List<Attendance> findForCriteria(final AttendanceGetRequest attendanceGetRequest) throws ParseException {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = attendanceQueryBuilder.getQuery(attendanceGetRequest, preparedStatementValues);
        LOGGER.info("search attendance Query ::" + queryStr);

        final List<Attendance> attendances = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                attendanceRowMapper);
        return attendances;
    }

    public List<Attendance> findReportQuery(final AttendanceReportRequest attendanceReportRequest, Long noofdays, Long noOfWorkingDays) throws ParseException {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = attendanceQueryBuilder.getAttendanceReportQuery(attendanceReportRequest, noofdays, noOfWorkingDays, preparedStatementValues);
        LOGGER.info("search attendance Query ::" + queryStr);

        final List<Attendance> attendances = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
                attendanceReportRowMapper);
        return attendances;
    }

    public List<Attendance> findByCriteria(final AttendanceGetRequest attendanceGetRequest, final List<java.util.Date> holidayDates) throws ParseException {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final List<Attendance> attendances = attendanceQueryBuilder.getAttendanceQuery(attendanceGetRequest, holidayDates, preparedStatementValues);
        LOGGER.info("search attendance Query ::" + attendances);
        return attendances;
    }

    @SuppressWarnings("static-access")
    public AttendanceType findAttendanceTypeByCode(final String code, final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(code.toUpperCase());
        preparedStatementValues.add(tenantId);
        final String query = AttendanceQueryBuilder.selectTypeByCodeQuery();
        final List<AttendanceType> attendanceTypes = jdbcTemplate.query(query, preparedStatementValues.toArray(),
                attendanceTypeRowMapper);
        if (!attendanceTypes.isEmpty())
            return attendanceTypes.get(0);

        return null;
    }

    public boolean checkAttendanceByEmployeeAndDate(final Long employee, final java.util.Date attendanceDate,
                                                    final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        preparedStatementValues.add(employee);
        preparedStatementValues.add(attendanceDate);
        preparedStatementValues.add(tenantId);
        final String query = AttendanceQueryBuilder.selectAttendanceByEmployeeAndDateQuery();
        final List<Map<String, Object>> attendanceIds = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());
        if (!attendanceIds.isEmpty())
            return true;

        return false;
    }

    public AttendanceRequest saveAttendance(final AttendanceRequest attendanceRequest) {

        LOGGER.info("AttendanceDao attendanceRequest::" + attendanceRequest);

        createAttendance(attendanceRequest);
        updateAttendance(attendanceRequest);

        return attendanceRequest;
    }

    private void createAttendance(final AttendanceRequest attendanceRequest) {
        final String attendanceinsert = AttendanceQueryBuilder.insertAttendanceQuery();
        LOGGER.info("Create Query for attendanceRequest::" + attendanceinsert);

        try {
            jdbcTemplate.batchUpdate(attendanceinsert, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Attendance attendance = ((List<Attendance>) attendanceRequest.getNewAttendances()).get(i);
                    AttendanceType type = findAttendanceTypeByCode(attendance.getType().getCode(), attendance.getTenantId());
                    if (type == null)
                        type = findAttendanceTypeByCode("A", attendance.getTenantId());
                    ps.setDate(1, new Date(attendance.getAttendanceDate().getTime()));
                    ps.setLong(2, attendance.getEmployee());
                    ps.setInt(3, attendance.getMonth());
                    ps.setString(4, attendance.getYear());
                    ps.setLong(5, type.getId());
                    ps.setString(6, attendance.getRemarks());
                    ps.setLong(7, Long.valueOf(attendanceRequest.getRequestInfo().getMsgId()));
                    ps.setDate(8, new Date(new java.util.Date().getTime()));
                    ps.setLong(9, Long.valueOf(attendanceRequest.getRequestInfo().getMsgId()));
                    ps.setDate(10, new Date(new java.util.Date().getTime()));
                    ps.setString(11, attendance.getTenantId());
                }

                @Override
                public int getBatchSize() {
                    return attendanceRequest.getNewAttendances().size();
                }
            });
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void updateAttendance(final AttendanceRequest attendanceRequest) {
        final String attendanceUpdate = AttendanceQueryBuilder.updateAttendanceQuery();
        LOGGER.info("Update Query for attendanceRequest::" + attendanceUpdate);

        try {
            jdbcTemplate.batchUpdate(attendanceUpdate, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Attendance attendance = ((List<Attendance>) attendanceRequest.getSavedAttendances()).get(i);
                    AttendanceType type = findAttendanceTypeByCode(attendance.getType().getCode(), attendance.getTenantId());
                    if (type == null)
                        type = findAttendanceTypeByCode("A", attendance.getTenantId());
                    ps.setDate(1, new Date(attendance.getAttendanceDate().getTime()));
                    ps.setLong(2, attendance.getEmployee());
                    ps.setInt(3, attendance.getMonth());
                    ps.setString(4, attendance.getYear());
                    ps.setLong(5, type.getId());
                    ps.setString(6, attendance.getRemarks());
                    ps.setLong(7, Long.valueOf(attendanceRequest.getRequestInfo().getMsgId()));
                    ps.setDate(8, new Date(new java.util.Date().getTime()));
                    ps.setString(9, attendance.getTenantId());
                    ps.setLong(10, attendance.getId());
                }

                @Override
                public int getBatchSize() {
                    return attendanceRequest.getSavedAttendances().size();
                }
            });
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    public List<AttendanceType> findAllAttendanceTypes(final String tenantId) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String query = AttendanceQueryBuilder.selectAttendanceTypeQuery();
        preparedStatementValues.add(tenantId);
        return jdbcTemplate.query(query, preparedStatementValues.toArray(),
                attendanceTypeRowMapper);
    }
}