package org.egov.pgr.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.pgr.domain.model.OTPConfig;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class OTPConfigRowMapper implements RowMapper<OTPConfig> {
	@Override
	public OTPConfig mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		OTPConfig config = new OTPConfig();
		config.setTenantId(rs.getString("tenantid"));
		config.setOtpConfigEnabled(rs.getString("enabled").equals("Y")? true : false); 
		return config;
	}

}
