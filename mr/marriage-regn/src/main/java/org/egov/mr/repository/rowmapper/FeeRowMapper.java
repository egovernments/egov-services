package org.egov.mr.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.mr.model.Fee;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FeeRowMapper implements RowMapper<Fee> {
	@Override
	public Fee mapRow(ResultSet rs, int rowNum) throws SQLException {

		Fee fee = new Fee();

		fee.setId(rs.getString("id"));
		fee.setTenantId(rs.getString("tenantId"));
		fee.setFee(rs.getBigDecimal("fee"));
		fee.setFeeCriteria(rs.getString("feeCriteria"));
		fee.setFromDate(rs.getLong("fromDate"));
		fee.setToDate(rs.getLong("toDate"));

		return fee;
	}

}
