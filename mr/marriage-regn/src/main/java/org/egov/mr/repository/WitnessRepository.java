package org.egov.mr.repository;

import org.egov.mr.model.Witness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WitnessRepository {	

	public static final String INSERT_WITNESS_QUERY = "INSERT INTO egmr_marriageregn_witness("
	       +"id, applicationnumber, tenantid, witnessno, name, relation, relatedto, dob, address, relationshipwithapplicants, occupation, aadhaar, mobileno, email, photo)"
	       +" VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?);";
	
	public static final String UPDATE_WITNESS_QUERY = "UPDATE egmr_marriageregn_witness"
			+" SET(name, relation, relatedto, dob, address, relationshipwithapplicants, occupation, aadhaar, mobileno, email, photo)"
			+" = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
			+" WHERE witnessno = ? AND applicationnumber = ? AND tenantid = ?";
	
	public static final String DELETE_WITNESS_QUERY = "DELETE FROM egmr_marriageregn_witness"
			+" WHERE witnessno = ? AND applicationnumber = ? AND tenantid = ?";	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(String applicationNumber, String tenantId, Witness witness) {
		Object[] obj = new Object[]{witness.getId(),
				applicationNumber, tenantId, witness.getWitnessNo(), witness.getName(), witness.getRelationForIdentification(), 
				witness.getRelatedTo().toString(), witness.getDob(),
				witness.getAddress(), witness.getRelationshipWithApplicants(), witness.getOccupation(), witness.getAadhaar(), 
				witness.getMobileNo(), witness.getEmail()
		};
		jdbcTemplate.update(INSERT_WITNESS_QUERY, obj);
	}

	public void update(String applicationNumber, String tenantId, Witness witness) {
		Object[] obj = new Object[]{
				witness.getName(), witness.getRelationForIdentification(), witness.getRelatedTo().toString(),
				witness.getDob(), witness.getAddress(), witness.getRelationshipWithApplicants(),
				witness.getOccupation(), witness.getAadhaar(),
				witness.getMobileNo(), witness.getEmail(), witness.getWitnessNo(), applicationNumber, tenantId
		};
		jdbcTemplate.update(UPDATE_WITNESS_QUERY, obj);
	}

	public void delete(String applicationNumber, String tenantId) {
		jdbcTemplate.update(DELETE_WITNESS_QUERY, applicationNumber, tenantId);
	}
	
	public String getNextId(){
		return "SELECT NEXTVAL('seq_marriageregn_witness');";
	}
}
