package org.egov.eis.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.egov.eis.model.EducationalQualification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class EducationalQualificationRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(EducationalQualificationRepository.class);

	// FIXME Educational Qualification Sequence, Employee ID
	public static final String INSERT_EDUCATIONAL_QUALIFICATION_QUERY = "INSERT INTO egeis_educationalQualification"
			+ " (id, employeeId, qualification, majorSubject, yearOfPassing, university,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (NEXTVAL('seq_egeis_educationalQualification'),?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(Long employeeId, List<EducationalQualification> educationalQualifications) {
		jdbcTemplate.batchUpdate(INSERT_EDUCATIONAL_QUALIFICATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				EducationalQualification educationalQualification = educationalQualifications.get(i);
				ps.setLong(1, employeeId);
				ps.setString(2, educationalQualification.getQualification());
				ps.setString(3, educationalQualification.getMajorSubject());
				ps.setInt(4, educationalQualification.getYearOfPassing());
				ps.setString(5, educationalQualification.getUniversity());
				ps.setLong(6, educationalQualification.getCreatedBy());
				ps.setDate(7, new Date(educationalQualification.getCreatedDate().getTime()));
				ps.setLong(8, educationalQualification.getLastModifiedBy());
				ps.setDate(9, new Date(educationalQualification.getLastModifiedDate().getTime()));
				ps.setString(10, "1");
			}

			@Override
			public int getBatchSize() {
				return educationalQualifications.size();
			}
		});
	}
}