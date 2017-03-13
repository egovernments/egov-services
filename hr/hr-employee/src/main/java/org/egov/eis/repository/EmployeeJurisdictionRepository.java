package org.egov.eis.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmployeeJurisdictionRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeJurisdictionRepository.class);

	// FIXME Educational Qualification Sequence, Employee ID
	public static final String INSERT_EMPLOYEE_JURISDICTION_QUERY = "INSERT INTO egeis_employeeJurisdictions"
			+ " (id, employeeId, jurisdictionId, tenantId)"
			+ " VALUES (NEXTVAL('seq_egeis_employeeJurisdictions'),?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(Long employeeId, List<Long> employeeJurisdictions) {
		jdbcTemplate.batchUpdate(INSERT_EMPLOYEE_JURISDICTION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Long jurisdictionId = employeeJurisdictions.get(i);
				ps.setLong(1, employeeId);
				ps.setLong(2, jurisdictionId);
				ps.setString(3, "1");
			}

			@Override
			public int getBatchSize() {
				return employeeJurisdictions.size();
			}
		});
	}
}