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
            .lessThan15(resultSet.getString("less15"))
            .lessThan45(resultSet.getString("btw15to45"))
            .lessThan90(resultSet.getString("btw45to90"))
            .greaterThan90(resultSet.getString("greaterthan90"))
            .build();
    }
}
