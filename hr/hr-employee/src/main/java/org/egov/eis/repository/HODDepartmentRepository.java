package org.egov.eis.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.HODDepartment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class HODDepartmentRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(HODDepartmentRepository.class);

	// FIXME HODDepartment Sequence, Assignment ID
	public static final String INSERT_HOD_DEPARTMENT_QUERY = "INSERT INTO egeis_hodDepartment"
			+ " (id, assignmentId, departmentId, tenantId)"
			+ " VALUES (NEXTVAL('seq_egeis_hodDepartment'),?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(Assignment assignment) {
		List<HODDepartment> hodDepartments = assignment.getHod();

		jdbcTemplate.batchUpdate(INSERT_HOD_DEPARTMENT_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				HODDepartment hodDepartment = hodDepartments.get(i);
				ps.setLong(1, assignment.getId());
				ps.setLong(2, hodDepartment.getDepartment());
				ps.setString(3, assignment.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return hodDepartments.size();
			}
		});
	}
}