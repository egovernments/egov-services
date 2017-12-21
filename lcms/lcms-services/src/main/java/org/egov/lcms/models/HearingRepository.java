package org.egov.lcms.models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Veswanth
 *	This object holds information about the Hearing Repository
 */
@Repository
public class HearingRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<String> getCaseCodesByCaseStatus(String caseStatusCode) {

		List<String> caseCodes = jdbcTemplate.queryForList(HearingBuilder.GET_CASE_CODES_BY_CASE_STATUS,
				new Object[] { caseStatusCode }, String.class);
		return caseCodes;

	}
}
