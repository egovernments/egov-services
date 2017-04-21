package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.egov.eis.model.Employee;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeTableRowMapper implements ResultSetExtractor<Employee> {
	
	@Override
	public Employee extractData(ResultSet rs) throws SQLException, DataAccessException {
		if (!rs.next())
			return null;

		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Employee employee = new Employee();
		employee.setId(rs.getLong("id"));
		employee.setCode(rs.getString("code"));
		employee.setEmployeeStatus(rs.getString("employeestatus"));
		employee.setRecruitmentMode(rs.getLong("recruitmentmodeid"));
		employee.setRecruitmentType(rs.getLong("recruitmenttypeid"));
		employee.setRecruitmentQuota(rs.getLong("recruitmentquotaid"));
		employee.setRetirementAge(rs.getShort("retirementage"));
		employee.setEmployeeType(rs.getLong("employeetypeid"));
		employee.setMotherTongue(rs.getLong("mothertongueid"));
		employee.setReligion(rs.getLong("religionid"));
		employee.setCommunity(rs.getLong("communityid"));
		employee.setCategory(rs.getLong("categoryid"));
		employee.setPhysicallyDisabled(rs.getBoolean("physicallydisabled"));
		employee.setMedicalReportProduced(rs.getBoolean("medicalReportproduced"));
		employee.setPassportNo(rs.getString("passportno"));
		employee.setGpfNo(rs.getString("gpfno"));
		employee.setBank(rs.getLong("bankid"));
		employee.setBankBranch(rs.getLong("bankbranchid"));
		employee.setBankAccount(rs.getString("bankaccount"));
		employee.setGroup(rs.getLong("groupid"));
		employee.setPlaceOfBirth(rs.getString("placeofbirth"));
		employee.setTenantId(rs.getString("tenantid"));
		try {
			employee.setDateOfAppointment(sdf.parse(sdf.format(rs.getDate("dateofappointment"))));
			employee.setDateOfJoining(sdf.parse(sdf.format(rs.getDate("dateofjoining"))));
			employee.setDateOfRetirement(sdf.parse(sdf.format(rs.getDate("dateofretirement"))));
			employee.setDateOfResignation(sdf.parse(sdf.format(rs.getDate("dateofresignation"))));
			employee.setDateOfTermination(sdf.parse(sdf.format(rs.getDate("dateoftermination"))));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}

		return employee;
	}
}
	
	
	

