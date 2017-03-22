package org.egov.eis.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.egov.eis.model.Probation;
import org.egov.eis.web.contract.EmployeeRequest;
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
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(EmployeeRequest employeeRequest) {
		List<Probation> probations = employeeRequest.getEmployee().getProbation();

		jdbcTemplate.batchUpdate(INSERT_PROBATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Probation probation = probations.get(i);
				ps.setLong(1, probation.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setLong(3, probation.getDesignation());
				ps.setDate(4, new Date(probation.getDeclaredOn().getTime()));
				ps.setString(5, probation.getOrderNo());
				ps.setDate(6, new Date(probation.getOrderDate().getTime()));
				ps.setString(7, probation.getRemarks());
				ps.setLong(8, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(10, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
				ps.setString(12, employeeRequest.getEmployee().getTenantId());
			}

			@Override
			public int getBatchSize() {
				return probations.size();
			}
		});
	}
}