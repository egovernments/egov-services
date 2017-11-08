package org.egov.lcms.repository;

import java.util.List;

import org.egov.lcms.models.Case;
import org.egov.lcms.repository.builder.SummonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Prasad
 *
 */
@Repository
public class SummonRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * This Will Delete the Advocate Details ,while updating the assign
	 * advocates,user might delete some assigned advocates,
	 * 
	 * @param cases
	 * @return List<>
	 */
	@Autowired(required = false)
	public List<String> getAdvocateCodes(Case caseObj) throws Exception {

		List<String> advocateCodes = getAdvocateCodesForCase(caseObj);
		return advocateCodes;
	}

	/**
	 * This API will delete the given advocateCodes
	 * 
	 * @param advocateCodes
	 */
	public void deleteAdvocateDetails(List<String> advocateCodes) {

		jdbcTemplate.update(SummonBuilder.deleteAdvocates(advocateCodes));

	}

	/**
	 * This Will get the advocate codes for the given case
	 * 
	 * @param caseObj
	 * @return {@link String}
	 */
	private List<String> getAdvocateCodesForCase(Case caseObj) {

		List<String> advocateCodes = jdbcTemplate.queryForList(SummonBuilder.GET_ADVOCATECODE_BY_CASECODE,
				new Object[] { caseObj.getCode() }, String.class);

		return advocateCodes;

	}

}
