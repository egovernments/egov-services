package org.egov.pgrrest.read.persistence.rowmapper;

import org.egov.pgrrest.read.domain.model.DashboardResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DailyCountRowMapper implements RowMapper<DashboardResponse> {
    @Override
    public DashboardResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        return DashboardResponse.builder()
            .openComplaintsCount(resultSet.getInt("regcount"))
            .closedComplaintsCount(resultSet.getInt("closedcount"))
            .day(resultSet.getString("day"))
            .date(resultSet.getString("date"))
            .build();
    }
}
