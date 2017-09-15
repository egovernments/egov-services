package org.egov.wcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.wcms.model.UsageType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class SubUsageTypeRowMapper implements RowMapper<UsageType> {

    @Override
    public UsageType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final UsageType usageType = new UsageType();
        usageType.setId(rs.getLong("ut_id"));
        usageType.setCode(rs.getString("ut_code"));
        usageType.setActive((Boolean) rs.getObject("ut_active"));
        usageType.setDescription(rs.getString("ut_description"));
        usageType.setName(rs.getString("ut_name"));
        usageType.setParentName(rs.getString("uts_name"));
        usageType.setParent(rs.getString("ut_parent"));
        usageType.setTenantId(rs.getString("ut_tenantid"));
        usageType.setCreatedBy((Long) rs.getObject("ut_createdby"));
        usageType.setCreatedDate((Long) rs.getObject("ut_createddate"));
        usageType.setLastModifiedBy((Long) rs.getObject("ut_lastmodifiedby"));
        usageType.setLastModifiedDate((Long) rs.getObject("ut_lastmodifieddate"));
        return usageType;
    }

}
