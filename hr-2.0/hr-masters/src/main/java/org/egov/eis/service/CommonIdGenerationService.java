package org.egov.eis.service;

import org.egov.eis.model.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CommonIdGenerationService {
	
	  @Autowired
	    private JdbcTemplate jdbcTemplate;
	 public Long getNextId(final Sequence sequence) {

	        final String query = "SELECT nextval('" + sequence.toString() + "')";
	        Integer result = null;
	        try {
	            result = jdbcTemplate.queryForObject(query, Integer.class);
	            log.debug("result:" + result);
	            return result.longValue();
	        } catch (final Exception ex) {
	            throw new RuntimeException("Next id is not generated.");
	        }
	    }

}
