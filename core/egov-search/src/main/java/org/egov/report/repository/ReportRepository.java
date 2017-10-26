package org.egov.report.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.swagger.model.ReportDefinition;
import org.egov.swagger.model.ReportRequest;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.minidev.json.JSONObject;

@Repository
public class ReportRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ReportQueryBuilder reportQueryBuilder;
	
	@Value("${max.sql.execution.time.millisec:45000}")
	private Long maxExecutionTime;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ReportRepository.class);
	
	public List<String> getData(ReportRequest reportRequest, ReportDefinition reportDefinition) {
		String query = reportQueryBuilder.buildQuery(reportRequest.getSearchParams(),reportRequest.getTenantId(),reportDefinition);
		Long startTime = new Date().getTime();
		LOGGER.info("final query:"+query);
		List<PGobject> maps = jdbcTemplate.queryForList(query,PGobject.class);
		Long endTime = new Date().getTime();
		Long totalExecutionTime = endTime-startTime;
		LOGGER.info("total query execution time taken in millisecount:"+totalExecutionTime);
		if(endTime-startTime>maxExecutionTime)
			LOGGER.error("Sql query is taking time query:"+query);
	
		List<String> queryJson = new ArrayList<>();
		for(PGobject obj: maps){
			LOGGER.info("obj:::"+obj);
			queryJson.add(obj.getValue().replaceAll("\\", ""));
						
			/*LOGGER.info("maps after converting to object : "+obj.toString());	*/
		}
		
		
		return queryJson;
	}

}
