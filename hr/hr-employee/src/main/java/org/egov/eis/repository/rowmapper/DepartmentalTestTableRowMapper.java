package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.eis.model.DepartmentalTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DepartmentalTestTableRowMapper implements RowMapper<DepartmentalTest> {

	@Override
	public DepartmentalTest mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		DepartmentalTest test = new DepartmentalTest();
		test.setId(rs.getLong("id"));
		test.setTest(rs.getString("test"));
		test.setYearOfPassing(rs.getInt("yearOfPassing"));
		test.setRemarks(rs.getString("remarks"));
		test.setCreatedBy(rs.getLong("createdBy"));
		test.setCreatedDate(rs.getDate("createdDate"));
		test.setLastModifiedBy(rs.getLong("lastModifiedBy"));
		test.setLastModifiedDate(rs.getDate("lastModifiedDate"));
		test.setTenantId(rs.getString("tenantId"));
		
		return test;
	}
}
