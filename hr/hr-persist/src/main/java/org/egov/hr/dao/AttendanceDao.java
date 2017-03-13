package org.egov.hr.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.egov.hr.model.Attendance;
import org.egov.hr.model.AttendanceRequest;
import org.egov.hr.querybuilder.AttendanceQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AttendanceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final Logger LOGGER = LoggerFactory.getLogger(AttendanceDao.class);

    public void saveAttendance(final AttendanceRequest attendanceRequest) {

        LOGGER.info("AgreementDao agreement::" + attendanceRequest);

        final String attendanceinsert = AttendanceQueryBuilder.insertAttendanceQuery();

        final List<Attendance> attendances = attendanceRequest.getAttendances();

        try {
            jdbcTemplate.batchUpdate(attendanceinsert, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Attendance attendance = attendances.get(i);
                    ps.setDate(1, new Date(attendance.getDate().getTime()));
                    ps.setLong(2, attendance.getEmployee());
                    ps.setString(3, attendance.getMonth());
                    ps.setString(4, attendance.getYear());
                    ps.setLong(5, attendance.getType().getId());
                    ps.setString(6, attendance.getRemarks());
                    ps.setLong(7, Long.valueOf(attendanceRequest.getRequestInfo().getMsgId()));
                    ps.setDate(8, new Date(new java.util.Date().getTime()));
                    ps.setLong(9, Long.valueOf(attendanceRequest.getRequestInfo().getMsgId()));
                    ps.setDate(10, new Date(new java.util.Date().getTime()));
                    ps.setString(11, attendance.getTenantId());
                }

                @Override
                public int getBatchSize() {
                    return attendances.size();
                }
            });
        } catch (final DataAccessException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }
}
