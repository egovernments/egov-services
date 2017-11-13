package org.egov.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.model.DepreciationInputs;
import org.egov.model.criteria.DepreciationCriteria;
import org.egov.repository.querybuilder.DepreciationQueryBuilder;
import org.egov.repository.rowmapper.DepreciationInputRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DepreciationRepository {
	
	@Autowired
	private DepreciationQueryBuilder depreciationQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DepreciationInputRowMapper depreciationInputRowMapper;
	
	public List<DepreciationInputs> getDepreciationInputs(DepreciationCriteria depreciationCriteria) {
	
		List<Object> preparedStatementValues = new ArrayList<>();
		String sql = depreciationQueryBuilder.getDepreciationQuery(depreciationCriteria, preparedStatementValues);
		log.info("the query for depreciation inputs : "+sql);
		log.info("prepared stsmt values : "+preparedStatementValues);
		return jdbcTemplate.query(sql, preparedStatementValues.toArray(), depreciationInputRowMapper);
	}
}
