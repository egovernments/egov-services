package org.egov.eis.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.Probation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProbationRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(ProbationRepository.class);

	// FIXME Probation Sequence, Employee ID
	public static final String INSERT_PROBATION_QUERY = "INSERT INTO egeis_probation"
			+ " (id, employeeId, designationId, declaredOn, orderNo, orderDate, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (NEXTVAL('seq_egeis_probation'),?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(Employee employee) {
		List<Probation> probations = employee.getProbation();

		jdbcTemplate.batchUpdate(INSERT_PROBATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Probation probation = probations.get(i);
				ps.setLong(1, employee.getId());
				ps.setLong(2, probation.getDesignation());
				ps.setDate(3, new Date(probation.getDeclaredOn().getTime()));
				ps.setString(4, probation.getOrderNo());
				ps.setDate(5, new Date(probation.getOrderDate().getTime()));
				ps.setString(6, probation.getRemarks());
				ps.setLong(7, probation.getCreatedBy());
				ps.setDate(8, new Date(probation.getCreatedDate().getTime()));
				ps.setLong(9, probation.getLastModifiedBy());
				ps.setDate(10, new Date(probation.getLastModifiedDate().getTime()));
				ps.setString(11, employee.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return probations.size();
			}
		});
	}
}