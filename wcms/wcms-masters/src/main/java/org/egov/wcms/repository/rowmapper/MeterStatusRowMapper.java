package org.egov.wcms.repository.rowmapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.wcms.model.MeterStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MeterStatusRowMapper implements RowMapper<MeterStatus> {

	@Override
	public MeterStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
		MeterStatus meterStatus = new MeterStatus();
		meterStatus.setId(rs.getLong("ms_id"));
		meterStatus.setCode(rs.getString("ms_code"));
		meterStatus.setMeterStatus(rs.getString("ms_status"));
		meterStatus.setDescription(rs.getString("ms_description"));
		meterStatus.setCreatedBy((Long)rs.getObject("ms_createdby"));
		meterStatus.setCreatedDate((Long)rs.getObject("ms_createddate"));
		meterStatus.setLastModifiedBy((Long)rs.getObject("ms_lastmodifiedby"));
		meterStatus.setLastModifiedDate((Long)rs.getObject("ms_lastmodifieddate"));
		meterStatus.setTenantId(rs.getString("ms_tenantid"));
		return meterStatus;
	}

}
