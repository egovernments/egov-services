package org.egov.eis.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.egov.eis.model.Regularisation;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RegularisationRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(RegularisationRepository.class);

	// FIXME Regularisation Sequence, Employee ID
	public static final String INSERT_REGULARISATION_QUERY = "INSERT INTO egeis_regularisation"
			+ " (id, employeeId, designationId, declaredOn, orderNo, orderDate, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(EmployeeRequest employeeRequest) {
		List<Regularisation> regularisations = employeeRequest.getEmployee().getRegularisation();

		jdbcTemplate.batchUpdate(INSERT_REGULARISATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Regularisation regularisation = regularisations.get(i);
				ps.setLong(1, regularisation.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setLong(3, regularisation.getDesignation());
				ps.setDate(4, new Date(regularisation.getDeclaredOn().getTime()));
				ps.setString(5, regularisation.getOrderNo());
				ps.setDate(6, new Date(regularisation.getOrderDate().getTime()));
				ps.setString(7, regularisation.getRemarks());
				ps.setLong(8, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(10, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
				ps.setString(12, employeeRequest.getEmployee().getTenantId());
			}

			@Override
			public int getBatchSize() {
				return regularisations.size();
			}
		});
	}
}