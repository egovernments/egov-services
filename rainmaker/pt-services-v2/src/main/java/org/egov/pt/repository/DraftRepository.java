package org.egov.pt.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.pt.repository.builder.DraftsQueryBuilder;
import org.egov.pt.web.models.DraftSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class DraftRepository {
	
	@Autowired
	private DraftsQueryBuilder draftsQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public String getDrafts(DraftSearchCriteria searchCriteria) {
		List<Object> prprStmtList = new ArrayList<>();
		String query = draftsQueryBuilder.getDraftsSearchQuery(searchCriteria, prprStmtList);
		log.info("Query: "+query);
		return jdbcTemplate.queryForObject(query,prprStmtList.toArray(), String.class);
	}
}
