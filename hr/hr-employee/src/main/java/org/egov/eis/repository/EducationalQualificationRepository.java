package org.egov.eis.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.egov.eis.model.EducationalQualification;
import org.egov.eis.web.contract.EmployeeRequest;
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
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(EmployeeRequest employeeRequest) {
		List<EducationalQualification> educationalQualifications = employeeRequest.getEmployee().getEducation();

		jdbcTemplate.batchUpdate(INSERT_EDUCATIONAL_QUALIFICATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				EducationalQualification educationalQualification = educationalQualifications.get(i);
				ps.setLong(1, educationalQualification.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setString(3, educationalQualification.getQualification());
				ps.setString(4, educationalQualification.getMajorSubject());
				ps.setInt(5, educationalQualification.getYearOfPassing());
				ps.setString(6, educationalQualification.getUniversity());
				ps.setLong(7, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(9, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
				ps.setString(11, employeeRequest.getEmployee().getTenantId());
			}

			@Override
			public int getBatchSize() {
				return educationalQualifications.size();
			}
		});
	}
}