package org.egov.pgr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.contract.ReportRequest;
import org.egov.pgr.contract.ReportResponse;
import org.egov.pgr.repository.ReportRepository;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.ReportConstants;
import org.egov.pgr.utils.ReportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportService {

	@Autowired
	private ReportRepository repository;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private ReportUtils reportUtils;

	@Autowired
	private PGRUtils pgrUtils;

	public ReportResponse getReports(ReportRequest reportRequest) {
		reportUtils.validateReportRequest(reportRequest);
		createViewForSLA(false);
		List<Map<String, Object>> dbResponse = repository.getDataFromDb(reportRequest);
		if (CollectionUtils.isEmpty(dbResponse)) {
			return reportUtils.getDefaultResponse(reportRequest);
		}
		createViewForSLA(true);
		return enrichAndFormatResponse(reportRequest, dbResponse);
	}

	public void createViewForSLA(Boolean shouldViewBeDropped) {
		repository.createOrDropViewDb(shouldViewBeDropped);
	}

	public ReportResponse enrichAndFormatResponse(ReportRequest reportRequest, List<Map<String, Object>> dbResponse) {
		if (reportRequest.getReportName().equalsIgnoreCase(ReportConstants.COMPLAINT_TYPE_REPORT)) {
			enrichComplaintTypeWiseReport(reportRequest, dbResponse);
		} else if (reportRequest.getReportName().equalsIgnoreCase(ReportConstants.AO_REPORT)) {
			enrichAOWiseReport(reportRequest, dbResponse);
		} else if (reportRequest.getReportName().equalsIgnoreCase(ReportConstants.DEPARTMENT_REPORT)) {
			enrichDepartmentWiseReport(reportRequest, dbResponse);
		} else if (reportRequest.getReportName().equalsIgnoreCase(ReportConstants.SOURCE_REPORT)) {
			enrichSourceWiseReport(reportRequest, dbResponse);
		} else if (reportRequest.getReportName().equalsIgnoreCase(ReportConstants.FUNCTIONARY_REPORT)) {
			enrichFunctionaryWiseReport(reportRequest, dbResponse);
		}
		return reportUtils.formatDBResponse(reportRequest, dbResponse);
	}

	public void enrichComplaintTypeWiseReport(ReportRequest reportRequest, List<Map<String, Object>> dbResponse) {
		for (Map<String, Object> tuple : dbResponse) {
			tuple.put("complaint_type", reportUtils.splitCamelCase(tuple.get("complaint_type").toString()));
		}
	}

	public void enrichAOWiseReport(ReportRequest reportRequest, List<Map<String, Object>> dbResponse) {
		List<Long> GROids = dbResponse.parallelStream().map(obj -> {
			return Long.valueOf(obj.get("ao_name").toString().split("[:]")[0]);
		}).collect(Collectors.toList());
		
		Map<Long, String> mapOfIdAndName = getEmployeeDetails(reportRequest, GROids);
		
		for (Map<String, Object> tuple : dbResponse) {
			String name = mapOfIdAndName.get(Long.valueOf(tuple.get("ao_name").toString().split("[:]")[0]));
			tuple.put("ao_name", 
				 !StringUtils.isEmpty(name) ? name : tuple.get("ao_name").toString().split("[:]")[0]);
			tuple.put("complaints_unassigned",
					((Long.valueOf(tuple.get("total_complaints_received").toString()))
							- ((Long.valueOf(tuple.get("complaints_assigned").toString()))
									+ (Long.valueOf(tuple.get("complaints_rejected").toString())))));
		}

	}
	
	public void enrichDepartmentWiseReport(ReportRequest reportRequest, List<Map<String, Object>> dbResponse) {
		Map<String, String> mapOfServiceCodesAndDepts = getServiceDefsData(reportRequest, false);
		for (Map<String, Object> tuple : dbResponse) {
			tuple.put("department_name", mapOfServiceCodesAndDepts.get(tuple.get("department_name")));
		}
	}
	
	public void enrichSourceWiseReport(ReportRequest reportRequest, List<Map<String, Object>> dbResponse) {
		//do nothing for now, later if there's a requirement, this method can be used.
	}
	
	public void enrichFunctionaryWiseReport(ReportRequest reportRequest, List<Map<String, Object>> dbResponse) {
		List<Long> employeeIds = dbResponse.parallelStream().map(obj -> {
			return Long.valueOf(StringUtils.isEmpty(obj.get("employee_name").toString()) ? "0" : obj.get("employee_name").toString());
		}).collect(Collectors.toList());
		
		Map<Long, String> mapOfIdAndName = getEmployeeDetails(reportRequest, employeeIds);
		
		for (Map<String, Object> tuple : dbResponse) {
			String name = mapOfIdAndName.get(Long.valueOf(tuple.get("employee_name").toString()));
			tuple.put("employee_name", 
				 !StringUtils.isEmpty(name) ? name : tuple.get("employee_name").toString());
		}

	}

	public Map<Long, String> getEmployeeDetails(ReportRequest reportRequest, List<Long> employeeIds){
		Map<Long, String> mapOfIdAndName = new HashMap<>();
		ObjectMapper mapper = pgrUtils.getObjectMapper();
		StringBuilder uri = new StringBuilder();
		Object request = reportUtils.getGROSearchRequest(uri, employeeIds, reportRequest);
		Object response = serviceRequestRepository.fetchResult(uri, request);
		if(null != response) {
			List<Map<String, Object>> resultCast = mapper.convertValue(JsonPath.read(response, "$.Employee"), List.class);
			for(Map<String, Object> employee: resultCast) {
				mapOfIdAndName.put(Long.parseLong(employee.get("id").toString()), employee.get("name").toString());
			}
		}
		log.info("mapOfIdAndName: "+mapOfIdAndName);
		return mapOfIdAndName;
	}
	
	public Map<String, String> getServiceDefsData(ReportRequest reportRequest, Boolean iWantSlahours){
		Map<String, String> mapOfServiceCodesAndDepts = new HashMap<>();
		Map<String, String> mapOfServiceCodesAndSLA = new HashMap<>();
		ObjectMapper mapper = pgrUtils.getObjectMapper();
		StringBuilder uri = new StringBuilder();
		Object request = reportUtils.getRequestForServiceDefsSearch(uri, reportRequest.getTenantId(), reportRequest.getRequestInfo());
		Object response = serviceRequestRepository.fetchResult(uri, request);
		if(null != response) {
			List<Map<String, Object>> resultCast = mapper.convertValue(JsonPath.read(response, "$.MdmsRes.RAINMAKER-PGR.ServiceDefs"), List.class);
			for(Map<String, Object> serviceDef: resultCast) {
				mapOfServiceCodesAndDepts.put(serviceDef.get("serviceCode").toString(), serviceDef.get("department").toString());
				mapOfServiceCodesAndSLA.put(serviceDef.get("serviceCode").toString(), serviceDef.get("slaHours").toString());
			}
		}
		log.info("mapOfServiceCodesAndDepts: "+mapOfServiceCodesAndDepts);
		if(iWantSlahours)
			return mapOfServiceCodesAndSLA;
		else
			return mapOfServiceCodesAndDepts;
	}

}
