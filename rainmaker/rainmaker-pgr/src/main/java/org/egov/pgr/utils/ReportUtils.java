package org.egov.pgr.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.egov.pgr.contract.ColumnDetail;
import org.egov.pgr.contract.ReportRequest;
import org.egov.pgr.contract.ReportResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReportUtils {
	
	@Autowired
	private ResponseInfoFactory factory;

	/**
	 * Formats the DB Response according to the existing Report Framework response for the ease of UI intgeration
	 * 
	 * @param reportRequest
	 * @param dbResponse
	 * @return ReportResponse
	 */
	
	public ReportResponse formatDBResponse(ReportRequest reportRequest, List<Map<String, Object>> dbResponse) {
		List<ColumnDetail> reportHeader = new ArrayList<ColumnDetail>();
		List<List<Object>> reportData = new ArrayList<List<Object>>();
		
		//preparing report header
		Map<String, Object> firstTuple = dbResponse.get(0);
		for(String key: firstTuple.keySet()) {
			ColumnDetail coloumnDetail = null;
			if(null != firstTuple.get(key)) {
				if(firstTuple.get(key).getClass().getTypeName().contains("String"))
					coloumnDetail = ColumnDetail.builder().name(key.replaceAll("[_]", " ")).type("string").label("reports.rainmaker-pgr."+key.replaceAll("[_]", " ")).showColumn(true).build();
				else 
					coloumnDetail = ColumnDetail.builder().name(key.replaceAll("[_]", " ")).type("number").label("reports.rainmaker-pgr."+key.replaceAll("[_]", " ")).showColumn(true).build();
			}else {
				coloumnDetail = ColumnDetail.builder().name(key.replaceAll("[_]", " ")).type("number").label("reports.rainmaker-pgr."+key.replaceAll("[_]", " ")).showColumn(true).build();
			}
			reportHeader.add(coloumnDetail);
		}
		
		//preparing report data
		for(Map<String, Object> tuple: dbResponse) {
			log.info("value: "+tuple.values());
			List<Object> data = new ArrayList(tuple.values());
			reportData.add(data);
		}
		
		return ReportResponse.builder().responseInfo(factory.createResponseInfoFromRequestInfo(reportRequest.getRequestInfo(), true))
				.reportHeader(reportHeader).reportData(reportData).build();

	}
	
	
	public ReportResponse getDefaultResponse(ReportRequest reportRequest) {
		String[] supersetOfKeys = {"Total Complaints Recieved", "Total Complaints Closed", "Total Open Complaints", "Within SLA", "Outside SLA", "Avg. Citizen Rating"};
		List<ColumnDetail> reportHeader = new ArrayList<ColumnDetail>();
		ColumnDetail columnDetail = ColumnDetail.builder().name(ReportConstants.reportCoulmnKeyMap.get(reportRequest.getReportName()))
				.label("reports.rainmaker-pgr."+ReportConstants.reportCoulmnKeyMap.get(reportRequest.getReportName()))
				.type("string").showColumn(true).build();
		reportHeader.add(columnDetail);
		
		for(String key: Arrays.asList(supersetOfKeys)) {
			ColumnDetail colmDetail = ColumnDetail.builder().name(key).label("reports.rainmaker-pgr."+key).type("string").showColumn(true).build();
			reportHeader.add(colmDetail);
		}
		
		return ReportResponse.builder().responseInfo(factory.createResponseInfoFromRequestInfo(reportRequest.getRequestInfo(), true))
				.reportHeader(reportHeader).reportData(new ArrayList<List<Object>>()).build();
	}
	
	
	
	public void validateReportRequest(ReportRequest reportRequest) {
		log.info("report list: "+Arrays.asList(ReportConstants.REPORT_LIST));
		if(!Arrays.asList(ReportConstants.REPORT_LIST).contains(reportRequest.getReportName()))
			throw new CustomException("INVALID_REPORT_REQUEST", "There's no definition of this report");
	}
	
	
	/**
	 * Splits any camelCase to human readable string
	 * @param String
	 * @return String
	 */
	public String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}
}
