package org.egov.eis.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.egov.eis.model.TechnicalQualification;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TechnicalQualificationRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(TechnicalQualificationRepository.class);

	// FIXME Technical Qualification Sequence, Employee ID
	public static final String INSERT_TECHNICAL_QUALIFICATION_QUERY = "INSERT INTO egeis_technicalQualification"
			+ " (id, employeeId, skill, grade, yearOfPassing, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(EmployeeRequest employeeRequest) {
		List<TechnicalQualification> technicalQualifications = employeeRequest.getEmployee().getTechnical();

		jdbcTemplate.batchUpdate(INSERT_TECHNICAL_QUALIFICATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TechnicalQualification technicalQualification = technicalQualifications.get(i);
				ps.setLong(1, technicalQualification.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setString(3, technicalQualification.getSkill());
				ps.setString(4, technicalQualification.getGrade());
				ps.setInt(5, technicalQualification.getYearOfPassing());
				ps.setString(6, technicalQualification.getRemarks());
				ps.setLong(7, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(9, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
				ps.setString(11, employeeRequest.getEmployee().getTenantId());
			}

			@Override
			public int getBatchSize() {
				return technicalQualifications.size();
			}
		});
	}
}