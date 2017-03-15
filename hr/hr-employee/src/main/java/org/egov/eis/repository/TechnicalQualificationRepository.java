package org.egov.eis.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.TechnicalQualification;
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
			+ " VALUES (NEXTVAL('seq_egeis_technicalQualification'),?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(Employee employee) {
		List<TechnicalQualification> technicalQualifications = employee.getTechnical();

		jdbcTemplate.batchUpdate(INSERT_TECHNICAL_QUALIFICATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TechnicalQualification technicalQualification = technicalQualifications.get(i);
				ps.setLong(1, employee.getId());
				ps.setString(2, technicalQualification.getSkill());
				ps.setString(3, technicalQualification.getGrade());
				ps.setInt(4, technicalQualification.getYearOfPassing());
				ps.setString(5, technicalQualification.getRemarks());
				ps.setLong(6, technicalQualification.getCreatedBy());
				ps.setDate(7, new Date(technicalQualification.getCreatedDate().getTime()));
				ps.setLong(8, technicalQualification.getLastModifiedBy());
				ps.setDate(9, new Date(technicalQualification.getLastModifiedDate().getTime()));
				ps.setString(10, employee.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return technicalQualifications.size();
			}
		});
	}
}