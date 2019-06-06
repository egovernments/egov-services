package org.egov.search.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.egov.custom.mapper.billing.impl.BillRowMapper;
import org.egov.search.model.Definition;
import org.egov.search.model.SearchRequest;
import org.egov.search.utils.SearchUtils;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.DefaultPropertiesPersister;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SearchRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${max.sql.execution.time.millisec:45000}")
	private Long maxExecutionTime;

	@Autowired
	private SearchUtils searchUtils;
	
	@Autowired
	public static ResourceLoader resourceLoader;

	@Autowired
	private BillRowMapper rowMapper;
	
	@Autowired
	private Environment env;

	public List<String> searchData(SearchRequest searchRequest, Definition definition) {
		List<String> result = new ArrayList<>();
		String query = null;
		try {
			query = searchUtils.buildQuery(searchRequest, definition.getSearchParams(), definition.getQuery());
		} catch (CustomException e) {
			throw e;
		}
		Long startTime = new Date().getTime();
		List<PGobject> maps = jdbcTemplate.queryForList(query, PGobject.class);
		Long endTime = new Date().getTime();
		Long totalExecutionTime = endTime - startTime;
		log.info("Query execution time in millisec: " + totalExecutionTime);
		if ((endTime - startTime) > maxExecutionTime) {
			log.error("Json query is taking unusually more time, query: " + query);
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
					"Query Execution Timeout! Json query is taking more time than the max exec time, query: " + query);
		}
		result = searchUtils.convertPGOBjects(maps);
		return result;
	}

	public Object fetchWithCustomMapper(SearchRequest searchRequest, Definition searchDefinition) {
		String query = searchUtils.buildQuery(searchRequest, searchDefinition.getSearchParams(),
				searchDefinition.getQuery());
		try {
			Long startTime = new Date().getTime();
			Object result = jdbcTemplate.queryForList(query, BillRowMapper.class);
			Long endTime = new Date().getTime();
			Long totalExecutionTime = endTime - startTime;
			log.info("Query execution time in millisec: " + totalExecutionTime);
			return result;
		} catch (CustomException e) {
			throw e;
		}
	}

/*	private void setProperties(String value) {
		try {
			Properties props = new Properties();
			props.setProperty("egov.searcher.rowmapper", value);
			Resource resource = resourceLoader.getResource("classpath:application.properties");
			File file = resource.getFile();
			OutputStream out = new FileOutputStream(file);
			// write into it
			DefaultPropertiesPersister p = new DefaultPropertiesPersister();
			p.store(props, out, "Header Comment");
			out.close();
		} catch (Exception e) {
			log.error("Exception while setting property: ", e);
		}
		log.info("Properties updated!");
	}*/

}
