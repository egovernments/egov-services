package org.egov.pgr.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.pgr.contract.ColumnDetail;
import org.egov.pgr.contract.ReportRequest;
import org.egov.pgr.contract.ReportResponse;
import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReportUtils {
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Value("${egov.hr.employee.v2.host}")
	private String hrEmployeeV2Host;

	@Value("${egov.hr.employee.v2.search.endpoint}")
	private String hrEmployeeV2SearchEndpoint;
	
	@Value("${egov.mdms.host}")
	private String mdmsHost;

	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;

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
			List<Object> data = new ArrayList(tuple.values());
			reportData.add(data);
		}
		
		return ReportResponse.builder().responseInfo(factory.createResponseInfoFromRequestInfo(reportRequest.getRequestInfo(), true))
				.reportHeader(reportHeader).reportData(reportData).build();

	}
	
	
	public ReportResponse getDefaultResponse(ReportRequest reportRequest) {
		String[] supersetOfKeys = {"total complaints recieved", "total complaints closed", "total open complaints", "within SLA", "outside SLA", "avg. Citizen Rating"};
		String[] supersetOfKeysOfAOReport = {"total Complaints Received", "complaints assigned", "compliants rejected", "compliants unassigned"};
		String[] supersetOfKeysOfSourceWiseReport = {"citizen mobile app", "citizen web app", "counter desktop"};

		List<ColumnDetail> reportHeader = new ArrayList<ColumnDetail>();
		ColumnDetail columnDetail = ColumnDetail.builder().name(ReportConstants.reportCoulmnKeyMap.get(reportRequest.getReportName()))
				.label("reports.rainmaker-pgr."+ReportConstants.reportCoulmnKeyMap.get(reportRequest.getReportName()))
				.type("string").showColumn(true).build();
		reportHeader.add(columnDetail);
		if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.AO_REPORT)) {
			for(String key: Arrays.asList(supersetOfKeysOfAOReport)) {
				ColumnDetail colmDetail = ColumnDetail.builder().name(key).label("reports.rainmaker-pgr."+key).type("string").showColumn(true).build();
				reportHeader.add(colmDetail);
			}
		}else if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.SOURCE_REPORT)) {
			for(String key: Arrays.asList(supersetOfKeysOfSourceWiseReport)) {
				ColumnDetail colmDetail = ColumnDetail.builder().name(key).label("reports.rainmaker-pgr."+key).type("string").showColumn(true).build();
				reportHeader.add(colmDetail);
			}
		}else {
			for(String key: Arrays.asList(supersetOfKeys)) {
				ColumnDetail colmDetail = ColumnDetail.builder().name(key).label("reports.rainmaker-pgr."+key).type("string").showColumn(true).build();
				reportHeader.add(colmDetail);
			}
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
	 * Returns uri and request for searching Employees based on id
	 * 
	 * @param uri
	 * @param GROids
	 * @param reportRequest
	 * @return RequestInfoWrapper
	 */
	public RequestInfoWrapper getGROSearchRequest(StringBuilder uri, List<Long> GROids, ReportRequest reportRequest) {
		RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(reportRequest.getRequestInfo()).build();
		uri.append(hrEmployeeV2Host).append(hrEmployeeV2SearchEndpoint)
		.append("?tenantId=").append(reportRequest.getTenantId())
		.append("&id=").append(GROids.toString().substring(1, GROids.toString().length() - 1));
		
		return requestInfoWrapper;
	}
	
	/**
	 * Prepares request and uri for service type search from MDMS
	 * 
	 * @param uri
	 * @param tenantId
	 * @param department
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	public MdmsCriteriaReq getRequestForServiceDefsSearch(StringBuilder uri, String tenantId, RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(PGRConstants.MDMS_PGR_MOD_NAME)
				.masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
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
	
	public String getPercentage(Object totalValue, Object percentValue) {
		Long total = Long.valueOf(totalValue.toString());
		Long fraction = Long.valueOf(percentValue.toString());
		Double percentage = Double.valueOf((fraction * 100) / total);
		if(Long.valueOf(percentage.toString() .split("[.]")[1]) > 0) {
			return percentValue + " (" + percentage.toString()+ "%" + ")";
		}else {
			return percentValue + " (" + percentage.toString() .split("[.]")[0]+ "%" + ")";
		}
		
	}
	
	public String getAvgRating(Object avgRating) {
		String value = null;
		if(null != avgRating) {
			value = new DecimalFormat("#.##").format(avgRating) + "/" + "5";
		}else {
			value =  "5/5";
		}
		
		return value;
	}
}
