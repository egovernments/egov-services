package org.egov.report.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.egov.report.repository.builder.SearchQueryBuilder;
import org.egov.swagger.model.SearchDefinition;
import org.egov.swagger.model.ReportRequest;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;

@Repository
public class SearchRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SearchQueryBuilder reportQueryBuilder;
	
	@Value("${max.sql.execution.time.millisec:45000}")
	private Long maxExecutionTime;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(SearchRepository.class);
	
	public List<String> getData(ReportRequest reportRequest, SearchDefinition reportDefinition) {
		ObjectMapper mapper = new ObjectMapper();
		String query = reportQueryBuilder.buildQuery(reportRequest.getSearchParams(),reportRequest.getTenantId(),reportDefinition);
		Long startTime = new Date().getTime();
		LOGGER.info("final query:"+query);
		List<PGobject> maps = jdbcTemplate.queryForList(query,PGobject.class);
		Long endTime = new Date().getTime();
		Long totalExecutionTime = endTime-startTime;
		LOGGER.info("total query execution time taken in millisecount:"+totalExecutionTime);
		if(endTime-startTime>maxExecutionTime)
			LOGGER.error("Sql query is taking unusually more time, query: "+query);
		List<String> queryJson = new ArrayList<>();
		for(PGobject obj: maps){
			LOGGER.info("obj:::"+obj);
			try{
				queryJson.add(obj.getValue());
			}catch(Exception e){
				LOGGER.error("Exception while parsing", e);
			}
		}
		 LOGGER.info("queryJson: "+queryJson);
		
		return queryJson;
	}

}
