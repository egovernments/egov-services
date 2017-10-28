package org.egov.pgrrest.read.persistence.rowmapper;

import org.egov.pgrrest.read.domain.model.AgeingResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AgeingRowMapper implements RowMapper<AgeingResponse> {
    @Override
    public AgeingResponse mapRow(ResultSet resultSet, int i) throws SQLException {

        return AgeingResponse.builder()
            .interval1(resultSet.getString("interval1"))
            .interval2(resultSet.getString("interval2"))
            .interval3(resultSet.getString("interval3"))
            .interval4(resultSet.getString("interval4"))
            .interval5(resultSet.getString("interval5"))
            .build();
    }
}
