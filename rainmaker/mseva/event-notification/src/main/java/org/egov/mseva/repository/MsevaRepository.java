package org.egov.mseva.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.mseva.web.contract.EventSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import org.egov.mseva.web.contract.Event;


import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MsevaRepository {
	
	@Autowired
	private MsevaEventsQueryBuilder queryBuilder;
	
	@Autowired
	private MsevaEventRowMapper rowMapper;
	
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	public List<Event> fetchEvents(EventSearchCriteria criteria){
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String query = queryBuilder.getSearchQuery(criteria, preparedStatementValues);
		List<Event> events = new ArrayList<>();
		try {
			events = namedParameterJdbcTemplate.query(query, preparedStatementValues, rowMapper);
		}catch(Exception e) {
			log.error("Error while fetching results from db: ", e);
		}
		
		return events;
	}

}
