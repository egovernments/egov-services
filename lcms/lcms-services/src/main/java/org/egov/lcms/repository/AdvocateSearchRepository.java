package org.egov.lcms.repository;
import java.util.List;
import org.egov.lcms.repository.builder.AdvocateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Prasad
 *
 */
@Repository
public class AdvocateSearchRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * This will give the case codes for the given advocateName
	 * 
	 * @param advocateName
	 * @return {@link String} AdvocateName
	 */
	public List<String> getcaseCodeByAdvocateName(String advocateName) {
		String searchQuery = AdvocateBuilder.SEARCH_CASE_CODE_BY_ADVOCATE;
		List<String> caseCodes = jdbcTemplate.queryForList(searchQuery, new Object[] { advocateName }, String.class);
		return caseCodes;

	}

}
