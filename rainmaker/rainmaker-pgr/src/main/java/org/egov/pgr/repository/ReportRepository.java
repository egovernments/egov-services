package org.egov.pgr.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.pgr.contract.ReportRequest;
import org.egov.pgr.utils.ReportConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ReportRepository {
	
	@Autowired
	private ReportQueryBuilder reportQueryBuilder;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Map<String, Object>> getDataFromDb(ReportRequest reportRequest) {
		String query = null;
		List<Map<String, Object>> result = new ArrayList<>();
		if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.COMPLAINT_TYPE_REPORT)) {
			query = reportQueryBuilder.getComplaintWiseReportQuery(reportRequest);
		}else if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.AO_REPORT)) {
			query = reportQueryBuilder.getAOWiseReportQuery(reportRequest);
		}else if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.DEPARTMENT_REPORT)) {
			query = reportQueryBuilder.getDepartmentWiseReportQuery(reportRequest);
		}else if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.SOURCE_REPORT)) {
			query = reportQueryBuilder.getSourceWiseReportQuery(reportRequest);
		}else if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.ULBEMPLOYEE_REPORT)) {
			query = reportQueryBuilder.getFunctionaryWiseReportQuert(reportRequest);
		}
		try {
			result = jdbcTemplate.queryForList(query);
		}catch(Exception e) {
			log.error("Exception while executing query: "+e);
		}
		log.info("dbResponse: "+result);
		return result;
	}
	
	public void createOrDropViewDb(ReportRequest reportRequest, Boolean shouldbeDropped) {
		String query = null;
		if(!shouldbeDropped) {
			query = reportQueryBuilder.getCreateViewQuery(reportRequest);
		}else {
			query = reportQueryBuilder.getDropViewQuery();
		}
		try {
			   jdbcTemplate.execute(query);
		}catch(Exception e) {
			log.error("View creation failed: "+e);
		}
	}

}
