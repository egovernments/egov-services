package org.egov.commons.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.model.Module;
import org.egov.commons.repository.builder.ModuleQueryBuilder;
import org.egov.commons.repository.rowmapper.ModuleRowMapper;
import org.egov.commons.web.contract.ModuleGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ModuleRowMapper moduleRowMapper;

	@Autowired
	private ModuleQueryBuilder moduleQueryBuilder;

	public List<Module> findForCriteria(ModuleGetRequest moduleGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = moduleQueryBuilder.getQuery(moduleGetRequest, preparedStatementValues);
		List<Module> modules = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), moduleRowMapper);
		return modules;
	}
}
