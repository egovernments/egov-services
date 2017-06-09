package org.egov.pgrrest.master.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.pgrrest.master.model.ReceivingCenterType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReceivingCenterTypeRowMapper implements RowMapper<ReceivingCenterType> {
	@Override
	public ReceivingCenterType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final ReceivingCenterType centerType = new ReceivingCenterType();
		centerType.setId(rs.getLong("id"));
		centerType.setCode(rs.getString("code"));
		centerType.setName(rs.getString("name"));
		centerType.setDescription(rs.getString("description"));
		centerType.setActive(rs.getBoolean("active"));
		centerType.setTenantId(rs.getString("tenantId"));
		centerType.setActive(rs.getBoolean("active"));
		centerType.setVisible(rs.getBoolean("visible"));
		
		return centerType;
	}
}
