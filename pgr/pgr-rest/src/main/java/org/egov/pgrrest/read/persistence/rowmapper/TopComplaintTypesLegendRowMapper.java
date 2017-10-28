package org.egov.pgrrest.read.persistence.rowmapper;

import org.egov.pgrrest.read.domain.model.ComplaintTypeLegend;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TopComplaintTypesLegendRowMapper implements RowMapper<ComplaintTypeLegend> {
    @Override
    public ComplaintTypeLegend mapRow(ResultSet resultSet, int i) throws SQLException {

        return ComplaintTypeLegend.builder()
                .complaintTypeName(resultSet.getString("complainttypename"))
                .build();

    }
}
