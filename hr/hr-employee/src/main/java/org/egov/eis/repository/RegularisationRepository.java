package org.egov.eis.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.egov.eis.model.Regularisation;
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
			+ " VALUES (NEXTVAL('seq_egeis_regularisation'),?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(Long employeeId, List<Regularisation> regularisations) {
		jdbcTemplate.batchUpdate(INSERT_REGULARISATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Regularisation regularisation = regularisations.get(i);
				ps.setLong(1, employeeId);
				ps.setLong(2, regularisation.getDesignation());
				ps.setDate(3, new Date(regularisation.getDeclaredOn().getTime()));
				ps.setString(4, regularisation.getOrderNo());
				ps.setDate(5, new Date(regularisation.getOrderDate().getTime()));
				ps.setString(6, regularisation.getRemarks());
				ps.setLong(7, regularisation.getCreatedBy());
				ps.setDate(8, new Date(regularisation.getCreatedDate().getTime()));
				ps.setLong(9, regularisation.getLastModifiedBy());
				ps.setDate(10, new Date(regularisation.getLastModifiedDate().getTime()));
				ps.setString(11, "1");
			}

			@Override
			public int getBatchSize() {
				return regularisations.size();
			}
		});
	}
}