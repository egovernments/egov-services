package org.egov.pgrrest.read.persistence.rowmapper;

import org.egov.pgrrest.read.domain.model.TopComplaintTypesResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class WardWiseRowMapper implements RowMapper<TopComplaintTypesResponse> {
    @Override
    public TopComplaintTypesResponse mapRow(ResultSet resultSet, int i) throws SQLException {

        return TopComplaintTypesResponse.builder()
            .count(resultSet.getInt("count"))
            .boundary(resultSet.getInt("boundary"))
            .boundaryName(resultSet.getString("boundaryname"))
            .build();
    }
}
