package org.egov.mr.repository;

import org.egov.mr.model.Witness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WitnessRepository {	

	public static final String INSERT_WITNESS_QUERY = "INSERT INTO egmr_marriageregn_witness("
	       +" applicationnumber, tenantid, name, relation, age, address, realtionship, occupation, aadhaar)"
	       +" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	public static final String UPDATE_WITNESS_QUERY = "UPDATE egmr_marriageregn_witness"
			+" SET(name, relation, age, address, realtionship, occupation, aadhaar)"
			+" = (?, ?, ?, ?, ?, ?, ?)"
			+" WHERE applicationnumber = ? AND tenantid = ?";
	
	public static final String DELETE_WITNESS_QUERY = "DELETE FROM egmr_marriageregn_witness"
			+" WHERE applicationnumber = ? AND tenantid = ?";	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(String applicationNumber, String tenantId, Witness witness) {
		Object[] obj = new Object[]{
				applicationNumber, tenantId, witness.getName(), witness.getRelationForIdentification(), witness.getAge(),
				witness.getAddress(), witness.getRelationship(), witness.getOccupation(), witness.getAadhaar()
		};
		jdbcTemplate.update(INSERT_WITNESS_QUERY, obj);
	}

	public void update(String applicationNumber, String tenantId, Witness witness) {
		Object[] obj = new Object[]{
				witness.getName(), witness.getRelationForIdentification(), witness.getAge(),
				witness.getAddress(), witness.getRelationship(), witness.getOccupation(), witness.getAadhaar(),
				applicationNumber, tenantId
		};
		jdbcTemplate.update(UPDATE_WITNESS_QUERY, obj);
	}

	public void delete(String applicationNumber, String tenantId) {
		jdbcTemplate.update(DELETE_WITNESS_QUERY, applicationNumber, tenantId);
	}
}
