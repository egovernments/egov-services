package org.egov.eis.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.egov.eis.model.DepartmentalTest;
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
			+ " VALUES (NEXTVAL('seq_egeis_departmentalTest'),?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(Long employeeId, List<DepartmentalTest> departmentalTests) {
		jdbcTemplate.batchUpdate(INSERT_DEPARTMENTAL_TEST_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DepartmentalTest departmentalTest = departmentalTests.get(i);
				ps.setLong(1, employeeId);
				ps.setString(2, departmentalTest.getTest());
				ps.setInt(3, departmentalTest.getYearOfPassing());
				ps.setString(4, departmentalTest.getRemarks());
				ps.setLong(5, departmentalTest.getCreatedBy());
				ps.setDate(6, new Date(departmentalTest.getCreatedDate().getTime()));
				ps.setLong(7, departmentalTest.getLastModifiedBy());
				ps.setDate(8, new Date(departmentalTest.getLastModifiedDate().getTime()));
				ps.setString(9, "1");
			}

			@Override
			public int getBatchSize() {
				return departmentalTests.size();
			}
		});
	}
}