package org.egov.hrms.repository.rowmapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmployeeJurisdictionMapper implements RowMapper<Long> {

	@Override
	public Long mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		return (Long) rs.getObject("jurisdictionid");
	}
}
