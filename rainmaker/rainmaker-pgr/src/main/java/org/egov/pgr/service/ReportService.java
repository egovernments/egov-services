package org.egov.pgr.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.egov.pgr.contract.ReportRequest;
import org.egov.pgr.contract.ReportResponse;
import org.egov.pgr.repository.ReportRepository;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.ReportConstants;
import org.egov.pgr.utils.ReportUtils;
import org.egov.pgr.utils.ResponseInfoFactory;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportService {
	
	@Autowired
	private ReportRepository repository;
	
	@Autowired
	private ReportUtils reportUtils;
	
	public ReportResponse getReports(ReportRequest reportRequest) {
		reportUtils.validateReportRequest(reportRequest);
		List<Map<String, Object>> dbResponse = repository.getDataFromDb(reportRequest);
		if(CollectionUtils.isEmpty(dbResponse)) {
			return reportUtils.getDefaultResponse(reportRequest);
		}
		return enrichAndFormatResponse(reportRequest, dbResponse);
	}
	
	public ReportResponse enrichAndFormatResponse(ReportRequest reportRequest, List<Map<String, Object>> dbResponse) {
		ReportResponse reportResponse = null;
		if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.COMPLAINT_TYPE_REPORT)) {
			enrichComplaintTypeWiseReport(reportRequest, dbResponse);
			return reportUtils.formatDBResponse(reportRequest, dbResponse);
		}
		return reportResponse;
	}
	
	public void enrichComplaintTypeWiseReport(ReportRequest reportRequest, List<Map<String, Object>> dbResponse) {
		for(Map<String, Object> tuple: dbResponse) {
			tuple.put("servicecode", reportUtils.splitCamelCase(tuple.get("servicecode").toString()));
		}
	}
	

}
