package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.repository.builder.CaseBuilder;
import org.egov.lcms.repository.rowmapper.CaseRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Prasad
 *
 */
@Repository
@Slf4j
public class CaseSearchRepository {
	
	
	@Autowired
	CaseBuilder caseBuilder;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	CaseRowMapper caseRowMapper;
	
	/**
	 * This will search the cases based on the given casesearchCriteria
	 * @param caseSearchCriteria
	 * @return
	 */
	public List<Case> searchCases(CaseSearchCriteria caseSearchCriteria){

        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = caseBuilder.getQuery(caseSearchCriteria, preparedStatementValues);

        List<Case> cases = new ArrayList<Case>();
        try {
        	cases = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),caseRowMapper);
        } catch (final Exception exception) {
            log.info("the exception in case search :" + exception);
        }
        
		
		return cases;
	}

}
