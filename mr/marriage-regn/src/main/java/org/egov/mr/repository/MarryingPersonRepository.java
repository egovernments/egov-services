package org.egov.mr.repository;

import org.egov.mr.model.MarryingPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MarryingPersonRepository {
	
	public static final String INSERT_MARRYING_PERSON_QUERY = "INSERT INTO egmr_marrying_person("
	       +" id, name, parentname, street, locality, city, dob, status, aadhaar, mobileno, email, religion," 
		   +" religionpractice, education, occupation, handicapped, residenceaddress, photo, nationality, tenantid)"
	       +" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	public static final String UPDATE_MARRYING_PERSON_QUERY = "UPDATE egmr_marrying_person"
		       +" SET(name, parentname, street, locality, city, dob, status, aadhaar, mobileno, email, religion," 
			   +" religionpractice, education, occupation, handicapped, residenceaddress, photo, nationality)"
		       +" = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
			   +" WHERE id = ? AND tenantid = ?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(MarryingPerson marryingPerson, String tenantId) {
		Object[] obj = new Object[] {
				marryingPerson.getId(), marryingPerson.getName(), marryingPerson.getParentName(), marryingPerson.getStreet(),
				marryingPerson.getLocality(), marryingPerson.getCity(), marryingPerson.getDob(), marryingPerson.getStatus().toString(),
				marryingPerson.getAadhaar(), marryingPerson.getMobileNo(), marryingPerson.getEmail(), marryingPerson.getReligion(),
				marryingPerson.getReligionPractice(), marryingPerson.getEducation(), marryingPerson.getOccupation(),
				marryingPerson.getHandicapped(), marryingPerson.getResidenceAddress(), marryingPerson.getPhoto(),
				marryingPerson.getNationality(),tenantId
		};
		jdbcTemplate.update(INSERT_MARRYING_PERSON_QUERY, obj);
	}

	public void update(MarryingPerson marryingPerson, String tenantId) {
		Object[] obj = new Object[] {
				marryingPerson.getName(), marryingPerson.getParentName(), marryingPerson.getStreet(),
				marryingPerson.getLocality(), marryingPerson.getCity(), marryingPerson.getDob(), marryingPerson.getStatus().toString(),
				marryingPerson.getAadhaar(), marryingPerson.getMobileNo(), marryingPerson.getEmail(), marryingPerson.getReligion(),
				marryingPerson.getReligionPractice(), marryingPerson.getEducation(), marryingPerson.getOccupation(),
				marryingPerson.getHandicapped(), marryingPerson.getResidenceAddress(), marryingPerson.getPhoto(),
				marryingPerson.getNationality(), marryingPerson.getId(), tenantId
				};
		jdbcTemplate.update(UPDATE_MARRYING_PERSON_QUERY, obj);
	}
}
