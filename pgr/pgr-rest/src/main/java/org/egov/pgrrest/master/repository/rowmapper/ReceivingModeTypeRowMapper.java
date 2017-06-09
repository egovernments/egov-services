package org.egov.pgrrest.master.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.pgrrest.master.model.ReceivingModeType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReceivingModeTypeRowMapper implements RowMapper<ReceivingModeType> {
	@Override
	public ReceivingModeType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final ReceivingModeType modeType = new ReceivingModeType();
		modeType.setId(rs.getLong("id"));
		modeType.setCode(rs.getString("code"));
		modeType.setName(rs.getString("name"));
		modeType.setDescription(rs.getString("description"));
		modeType.setActive(rs.getBoolean("active"));
		modeType.setTenantId(rs.getString("tenantId"));
		modeType.setActive(rs.getBoolean("active"));
		modeType.setVisible(rs.getBoolean("visible"));
		
		return modeType;
	}
}
