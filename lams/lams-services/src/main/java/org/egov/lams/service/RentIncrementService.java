package org.egov.lams.service;

import java.util.List;

import org.egov.lams.model.RentIncrementType;
import org.egov.lams.repository.builder.AgreementQueryBuilder;
import org.egov.lams.repository.rowmapper.RentIncrementRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RentIncrementService {
	
	public static final Logger logger = LoggerFactory.getLogger(AgreementService.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<RentIncrementType> getRentIncrements() {
		String query = "select rentincrement.id,rentincrement.type from eglams_rentincrementtype rentincrement";
		List<RentIncrementType> rentIncrements = null;
		try {
			rentIncrements = jdbcTemplate.query(query, new RentIncrementRowMapper());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("No records found for given criteria");
		}

		return rentIncrements;

	}
	
	public RentIncrementType getRentIncrementById(Long rentID) {

		String rentIncrementTypeqQuery = AgreementQueryBuilder.findRentIncrementTypeQuery();
		Object[] rentObj = new Object[] { rentID };
		RentIncrementType rentIncrementType = null;

		try {
			rentIncrementType = jdbcTemplate.queryForObject(rentIncrementTypeqQuery, rentObj,
					new RentIncrementRowMapper());
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
			// FIXME put apt exception
		}
		return rentIncrementType;

	}

}
