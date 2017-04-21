package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.egov.eis.model.DepartmentalTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DepartmentalTestTableRowMapper implements RowMapper<DepartmentalTest> {

	@Override
	public DepartmentalTest mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		DepartmentalTest test = new DepartmentalTest();
		test.setId(rs.getLong("id"));
		test.setTest(rs.getString("test"));
		test.setYearOfPassing(rs.getInt("yearOfPassing"));
		test.setRemarks(rs.getString("remarks"));
		test.setCreatedBy(rs.getLong("createdBy"));
		test.setLastModifiedBy(rs.getLong("lastModifiedBy"));
		test.setTenantId(rs.getString("tenantId"));
		try {
			test.setCreatedDate(sdf.parse(sdf.format(rs.getDate("createdDate"))));
			test.setLastModifiedDate(sdf.parse(sdf.format(rs.getDate("lastModifiedDate"))));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}
		
		return test;
	}
}
