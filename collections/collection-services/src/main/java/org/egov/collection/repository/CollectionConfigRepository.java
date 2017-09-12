package org.egov.collection.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.collection.repository.querybuilder.CollectionConfigQueryBuilder;
import org.egov.collection.repository.rowmapper.CollectionConfigRowMapper;
import org.egov.collection.web.contract.CollectionConfigGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionConfigRepository {
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CollectionConfigRowMapper collectionConfigRowMapper;

	@Autowired
	private CollectionConfigQueryBuilder collectionConfigQueryBuilder;

	public Map<String, List<String>> findForCriteria(CollectionConfigGetRequest collectionConfigGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<>();
		String queryStr = collectionConfigQueryBuilder.getQuery(collectionConfigGetRequest, preparedStatementValues);
		return jdbcTemplate.query(queryStr,
				preparedStatementValues.toArray(), collectionConfigRowMapper);
	}

}
