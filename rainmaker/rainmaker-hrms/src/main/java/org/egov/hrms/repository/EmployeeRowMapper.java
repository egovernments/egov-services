package org.egov.hrms.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.hrms.model.AuditDetails;
import org.egov.hrms.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EmployeeRowMapper implements ResultSetExtractor<List<Employee>> {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<Employee> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, Employee> employeeMap = new HashMap<>();
		while(rs.next()) {
			String currentid = rs.getString("employee_uuid");
			Employee currentEmployee = employeeMap.get(currentid);
			if(null == currentEmployee) {
				AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("employee_createdby")).createdDate(rs.getLong("employee_createddate"))
						.lastModifiedBy(rs.getString("employee_lastmodifiedby")).lastModifiedDate(rs.getLong("employee_lastmodifieddate")).build();
				currentEmployee = Employee.builder().id(rs.getLong("employee_id")).uuid(rs.getString("employee_uuid")).tenantId(rs.getString("employee_tenantid"))
						.code(rs.getString("code")).dateOfAppointment(rs.getLong("employee_doa")).active(rs.getBoolean("employee_active"))
						.employeeStatus(rs.getString("employee_employeestatus")).employeeType(rs.getString("employeetype")).auditDetails(auditDetails)
						.build();
			}
		}
		
		return new ArrayList<>(employeeMap.values());

	}

}
