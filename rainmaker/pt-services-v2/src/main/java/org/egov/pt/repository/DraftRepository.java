package org.egov.pt.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.pt.repository.builder.DraftsQueryBuilder;
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
	private JdbcTemplate jdbcTemplate;

	public String getDrafts(String userId, String tenantId){
		List<Object> prprStmtList = new ArrayList<>();
		String query = draftsQueryBuilder.getDraftsSearchQuery(tenantId, userId, prprStmtList);
		log.info("Query: "+query);
		return jdbcTemplate.queryForObject(query,prprStmtList.toArray(), String.class);
	}
}
