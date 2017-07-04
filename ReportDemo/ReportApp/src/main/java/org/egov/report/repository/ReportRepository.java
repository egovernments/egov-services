package org.egov.report.repository;

import java.util.List;
import java.util.Map;

import org.egov.domain.model.ReportYamlMetaData;
import org.egov.report.repository.builder.ReportQueryBuilder;
import org.egov.swagger.model.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ReportQueryBuilder reportQueryBuilder;
	
	public List<Map<String, Object>> getData(ReportRequest reportRequest, ReportYamlMetaData reportYamlMetaData) {
		String query = reportQueryBuilder.buildQuery(reportRequest.getSearchParams(),reportRequest.getTenantId(),reportYamlMetaData);
		List<Map<String, Object>> maps = jdbcTemplate.queryForList(query);
		System.out.println("maps : "+maps);
		return maps;
	}

}
