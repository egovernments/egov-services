package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.eis.model.EmployeeDocument;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
@Component
public class EmployeeDocumentsTableRowMapper implements RowMapper<EmployeeDocument> {
	
	
	
	@Override
	public EmployeeDocument mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		
		EmployeeDocument employeeDocument = new EmployeeDocument();
		
		employeeDocument.setId(rs.getLong("id"));
		employeeDocument.setDocument(rs.getString("document"));
		employeeDocument.setReferenceType(rs.getString("referencetype"));
		employeeDocument.setReferenceId(rs.getLong("referenceid"));
		employeeDocument.setTenantId(rs.getString("tenantid"));
		
		return employeeDocument;
		
	}
	
	

}
