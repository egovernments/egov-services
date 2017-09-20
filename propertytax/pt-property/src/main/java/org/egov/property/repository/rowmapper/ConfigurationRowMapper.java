package org.egov.property.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.models.AppConfiguration;
import org.egov.models.AuditDetails;
import org.springframework.jdbc.core.RowMapper;

public class ConfigurationRowMapper implements RowMapper<AppConfiguration> {

	@Override
	public AppConfiguration mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub

		AppConfiguration appConfiguration = new AppConfiguration();
		AuditDetails auditDetails = new AuditDetails();
		List<String> values = new ArrayList<String>();
		try {
			appConfiguration.setId(rs.getLong("id"));
			appConfiguration.setTenantId(rs.getString("tenantId"));
			appConfiguration.setKeyName(rs.getString("keyName"));
			appConfiguration.setDescription(rs.getString("description"));

			values.add(rs.getString("value"));

			appConfiguration.setValues(values);
			appConfiguration.setEffectiveFrom(rs.getString("effectiveFrom"));
			auditDetails.setCreatedBy(rs.getString("createdBy"));
			auditDetails.setCreatedTime(rs.getLong("createdTime"));
			auditDetails.setLastModifiedBy(rs.getString("lastModifiedBy"));
			auditDetails.setLastModifiedTime(rs.getLong("lastModifiedTime"));
			appConfiguration.setAuditDetails(auditDetails);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return appConfiguration;
	}
}