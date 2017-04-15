package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.eis.model.EmployeeDocument;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDocumentsUpdateTableRowMapper implements RowMapper<EmployeeDocument> {

	@Override
	public EmployeeDocument mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {

		EmployeeDocument employeeDocument = new EmployeeDocument();

		employeeDocument.setDocument(rs.getString("document"));
		employeeDocument.setEmployeeId(rs.getLong("employeeId"));
		employeeDocument.setReferenceType(rs.getString("referencetype"));
		employeeDocument.setReferenceId(rs.getLong("referenceId"));

		return employeeDocument;

	}

}
