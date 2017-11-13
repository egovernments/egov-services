package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.models.Case;
import org.egov.lcms.repository.builder.AdvocateBuilders;
import org.egov.lcms.repository.rowmapper.AdvocateRowMapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AdvocateRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	AdvocateRowMapper advocateRowMapper;

	public List<Advocate> search(AdvocateSearchCriteria advocateSearchCriteria) {

		if (advocateSearchCriteria.getPageNumber() == null || advocateSearchCriteria.getPageNumber() == 0)
			advocateSearchCriteria.setPageNumber(Integer.valueOf(propertiesManager.getDefaultPageNumber().trim()));

		if (advocateSearchCriteria.getPageSize() == null)
			advocateSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String selectQuery = AdvocateBuilders.getSearchQuery(advocateSearchCriteria, preparedStatementValues);

		List<Advocate> advocates = new ArrayList<Advocate>();
		try {
			advocates = jdbcTemplate.query(selectQuery, preparedStatementValues.toArray(), advocateRowMapper);
		} catch (final Exception exception) {
			log.info("the exception in advocate search :" + exception);
			throw new CustomException(propertiesManager.getAdvocateErrorCode(),
					propertiesManager.getAdvocateErrorMsg());
		}

		return advocates;
	}
	
	/**
	 * This will give the case codes for the given advocateName
	 * 
	 * @param advocateCode
	 * @return {@link String} AdvocateName
	 */
	public List<String> getcaseCodeByAdvocateCode(String advocateCode) {
		String searchQuery = AdvocateBuilders.SEARCH_CASE_CODE_BY_ADVOCATE;
		List<String> caseCodes = jdbcTemplate.queryForList(searchQuery, new Object[] { advocateCode }, String.class);
		return caseCodes;

	}

}
