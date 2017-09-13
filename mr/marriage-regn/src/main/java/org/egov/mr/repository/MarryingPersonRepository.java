package org.egov.mr.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.egov.mr.model.MarryingPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class MarryingPersonRepository {
	
	public static final String INSERT_MARRYING_PERSON_QUERY = "INSERT INTO egmr_marrying_person("
	       +" id, name, parentname, street, locality, city, dob, status, aadhaar, mobileno, email, religion," 
		   +" religionpractice, education, occupation, handicapped, residenceaddress, photo, nationality, tenantid, officeaddress)"
	       +" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	public static final String UPDATE_MARRYING_PERSON_QUERY = "UPDATE egmr_marrying_person"
		       +" SET(name, parentname, street, locality, city, dob, status, aadhaar, mobileno, email, religion," 
			   +" religionpractice, education, occupation, handicapped, residenceaddress, photo, nationality, officeaddress)"
		       +" = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
			   +" WHERE id = ? AND tenantid = ?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(MarryingPerson marryingPerson, String tenantId) {
//		Object[] obj = new Object[] {
//				marryingPerson.getId(), marryingPerson.getName(), marryingPerson.getParentName(), marryingPerson.getStreet(),
//				marryingPerson.getLocality(), marryingPerson.getCity(), marryingPerson.getDob(), marryingPerson.getStatus().toString(),
//				marryingPerson.getAadhaar(), marryingPerson.getMobileNo(), marryingPerson.getEmail(), marryingPerson.getReligion(),
//				marryingPerson.getReligionPractice(), marryingPerson.getEducation(), marryingPerson.getOccupation(),
//				marryingPerson.getHandicapped(), marryingPerson.getResidenceAddress(), marryingPerson.getPhoto(),
//				marryingPerson.getNationality(),tenantId,marryingPerson.getOfficeAddress()
//		};
//		
//		jdbcTemplate.update(INSERT_MARRYING_PERSON_QUERY, obj);
		
		System.err.println("ps values ::marryingPerson::::::::"+marryingPerson);
		
		System.err.println("ps values ::office address::::::::"+marryingPerson.getOfficeAddress());
		jdbcTemplate.update(INSERT_MARRYING_PERSON_QUERY, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, marryingPerson.getId());
				ps.setString(2, marryingPerson.getName());
				ps.setString(3, marryingPerson.getParentName());
				ps.setString(4,marryingPerson.getStreet());
				ps.setString(5,marryingPerson.getLocality());
				ps.setString(6,marryingPerson.getCity());
				ps.setLong(7,marryingPerson.getDob());
				ps.setString(8,marryingPerson.getStatus().toString());
				ps.setString(9,marryingPerson.getAadhaar());
				ps.setString(10,marryingPerson.getMobileNo());
				ps.setString(11,marryingPerson.getEmail());
				ps.setLong(12,marryingPerson.getReligion());
				ps.setString(13,marryingPerson.getReligionPractice());
				ps.setString(14,marryingPerson.getEducation());
				ps.setString(15,marryingPerson.getOccupation());
				ps.setString(16,marryingPerson.getHandicapped());
				ps.setString(17,marryingPerson.getResidenceAddress());
				ps.setString(18,marryingPerson.getPhoto());
				ps.setString(19,marryingPerson.getNationality());
				ps.setString(20,tenantId);
				ps.setString(21,marryingPerson.getOfficeAddress());
				
				}
	});
	}

	public void update(MarryingPerson marryingPerson, String tenantId) {
		Object[] obj = new Object[] {
				marryingPerson.getName(), marryingPerson.getParentName(), marryingPerson.getStreet(),
				marryingPerson.getLocality(), marryingPerson.getCity(), marryingPerson.getDob(), marryingPerson.getStatus().toString(),
				marryingPerson.getAadhaar(), marryingPerson.getMobileNo(), marryingPerson.getEmail(), marryingPerson.getReligion(),
				marryingPerson.getReligionPractice(), marryingPerson.getEducation(), marryingPerson.getOccupation(),
				marryingPerson.getHandicapped(), marryingPerson.getResidenceAddress(), marryingPerson.getPhoto(),
				marryingPerson.getNationality(), marryingPerson.getOfficeAddress(), marryingPerson.getId(), tenantId
				};
		jdbcTemplate.update(UPDATE_MARRYING_PERSON_QUERY, obj);
	}
}
