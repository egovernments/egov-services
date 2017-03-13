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
public class EmployeeLanguageRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeLanguageRepository.class);

	// FIXME Educational Qualification Sequence, Employee ID
	public static final String INSERT_EMPLOYEE_LANGUAGE_QUERY = "INSERT INTO egeis_employeeLanguages"
			+ " (id, employeeId, languageId, tenantId)"
			+ " VALUES (NEXTVAL('seq_egeis_employeeLanguages'),?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(Long employeeId, List<Long> employeeLanguages) {
		jdbcTemplate.batchUpdate(INSERT_EMPLOYEE_LANGUAGE_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Long languageId = employeeLanguages.get(i);
				ps.setLong(1, employeeId);
				ps.setLong(2, languageId);
				ps.setString(3, "1");
			}

			@Override
			public int getBatchSize() {
				return employeeLanguages.size();
			}
		});
	}
}