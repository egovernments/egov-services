package org.egov.lams.service;

import java.util.List;

import org.egov.lams.model.RentIncrementType;
import org.egov.lams.repository.rowmapper.RentIncrementRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GetRentIncrementService {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<RentIncrementType> getRentIncrements() {
		String query = "select rentincrement.id,rentincrement.type from eglams_rentincrementtype rentincrement";
		List<RentIncrementType> rentIncrements = null;
		try {
			rentIncrements = jdbcTemplate.query(query, new RentIncrementRowMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("No record found");
		}

		return rentIncrements;

	}

}
