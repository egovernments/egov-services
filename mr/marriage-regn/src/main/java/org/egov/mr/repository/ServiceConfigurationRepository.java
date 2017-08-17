package org.egov.mr.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.mr.repository.querybuilder.ServiceConfigurationQueryBuilder;
import org.egov.mr.repository.rowmapper.ServiceConfigurationKeyValuesRowMapper;
import org.egov.mr.web.contract.ServiceConfigurationSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ServiceConfigurationRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ServiceConfigurationQueryBuilder serviceConfigurationQueryBuilder;

	@Autowired
	private ServiceConfigurationKeyValuesRowMapper serviceConfigurationKeyValuesRowMapper;

	public Map<String, List<String>> findForCriteria(
			ServiceConfigurationSearchCriteria serviceConfigurationSearchCriteria) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String query = serviceConfigurationQueryBuilder.getSelectQuery(serviceConfigurationSearchCriteria,
				preparedStatementValues);

		log.info("ServiceConfigurationRepository query: " + query);
		log.info("preparedStatementValues : " + preparedStatementValues);

		Map<String, List<String>> hrConfigurationKeyValues = jdbcTemplate.query(query,
				preparedStatementValues.toArray(), serviceConfigurationKeyValuesRowMapper);

		return hrConfigurationKeyValues;
	}

}
