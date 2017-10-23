package org.egov.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.model.Disposal;
import org.egov.model.criteria.DisposalCriteria;
import org.egov.repository.querybuilder.DisposalQueryBuilder;
import org.egov.repository.rowmapper.DisposalRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DisposalRepository {

	@Autowired
	private DisposalQueryBuilder disposalQueryBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DisposalRowMapper disposalRowMapper;

	public List<Disposal> search(final DisposalCriteria disposalCriteria) {

		final List<Object> preparedStatementValues = new ArrayList<>();
		final String queryStr = disposalQueryBuilder.getQuery(disposalCriteria, preparedStatementValues);
		return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), disposalRowMapper);
	}

}
