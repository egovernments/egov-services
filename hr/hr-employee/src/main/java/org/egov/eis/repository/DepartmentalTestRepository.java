package org.egov.eis.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DepartmentalTestRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(DepartmentalTestRepository.class);

	// FIXME Departmental Test Sequence, Employee ID
	public static final String INSERT_DEPARTMENTAL_TEST_QUERY = "INSERT INTO egeis_departmentalTest"
			+ "(id, employeeId, test, yearOfPassing, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(EmployeeRequest employeeRequest) {
		List<DepartmentalTest> departmentalTests = employeeRequest.getEmployee().getTest();

		jdbcTemplate.batchUpdate(INSERT_DEPARTMENTAL_TEST_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DepartmentalTest departmentalTest = departmentalTests.get(i);
				ps.setLong(1, departmentalTest.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setString(3, departmentalTest.getTest());
				ps.setInt(4, departmentalTest.getYearOfPassing());
				ps.setString(5, departmentalTest.getRemarks());
				ps.setLong(6, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(7, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(8, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				ps.setString(10, employeeRequest.getEmployee().getTenantId());
			}

			@Override
			public int getBatchSize() {
				return departmentalTests.size();
			}
		});
	}
}