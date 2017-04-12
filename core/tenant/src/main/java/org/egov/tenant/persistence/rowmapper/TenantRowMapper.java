package org.egov.tenant.persistence.rowmapper;


import org.egov.tenant.domain.model.Tenant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TenantRowMapper implements RowMapper<Tenant> {

    @Override
    public Tenant mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Tenant tenant = Tenant.builder().id(rs.getLong("t_id"))
                .name(rs.getString("t_name"))
                .code(rs.getString("t_code"))
                .description(rs.getString("t_description"))
                .latitude(rs.getDouble("t_longitude"))
                .localName(rs.getString("t_localname"))
                .longitude(rs.getDouble("t_latitude"))
                .regionName(rs.getString("t_regionName"))
                .active(rs.getBoolean("t_active"))
                .districtName(rs.getString("t_districtName"))
                .districtCode(rs.getString("t_districtCode"))
                .grade(rs.getString("t_grade"))
                .build();

        return tenant;

    }
}
