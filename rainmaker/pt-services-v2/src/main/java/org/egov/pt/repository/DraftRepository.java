package org.egov.pt.repository;

import java.util.List;

import org.egov.pt.repository.builder.DraftsQueryBuilder;
import org.egov.pt.repository.rowmapper.DraftsRowMapper;
import org.egov.pt.web.models.Draft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DraftRepository {
	
	@Autowired
	private DraftsQueryBuilder draftsQueryBuilder;
	
	@Autowired
	private DraftsRowMapper draftsRowMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Draft> getDrafts(String userId, String tenantId){
		String query = draftsQueryBuilder.getDraftsSearchQuery(tenantId, userId);
		log.info("Query: "+query);
		return jdbcTemplate.query(query, draftsRowMapper);
	}
}
