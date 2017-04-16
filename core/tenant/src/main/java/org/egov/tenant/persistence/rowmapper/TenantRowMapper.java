package org.egov.tenant.persistence.rowmapper;


import org.egov.tenant.persistence.entity.Tenant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TenantRowMapper implements RowMapper<Tenant> {

    @Override
    public Tenant mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Tenant tenant = Tenant.builder()
                .id(rs.getLong(Tenant.Fields.ID.getValue()))
                .code(rs.getString(Tenant.Fields.CODE.getValue()))
                .description(rs.getString(Tenant.Fields.DESCRIPTION.getValue()))
                .domainUrl(rs.getString(Tenant.Fields.DOMAIN_URL.getValue()))
                .logoId(rs.getString(Tenant.Fields.LOGO_ID.getValue()))
                .imageId(rs.getString(Tenant.Fields.IMAGE_ID.getValue()))
                .build();

        return tenant;
    }
}
