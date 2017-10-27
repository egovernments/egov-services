package org.egov.report.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.SearchApp;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.domain.model.MetaDataRequest;
import org.egov.domain.model.ReportDefinitions;
import org.egov.domain.model.Response;
import org.egov.swagger.model.CustomJsonMapping;
import org.egov.swagger.model.FieldMapping;
import org.egov.report.repository.SearchRepository;
import org.egov.swagger.model.ColumnDetail;
import org.egov.swagger.model.ColumnDetail.TypeEnum;
import org.egov.swagger.model.MetadataResponse;
import org.egov.swagger.model.ReportDataResponse;
import org.egov.swagger.model.SearchDefinition;
import org.egov.swagger.model.ReportMetadata;
import org.egov.swagger.model.ReportRequest;
import org.egov.swagger.model.ReportResponse;
import org.egov.swagger.model.SearchColumn;
import org.egov.swagger.model.SourceColumn;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONObject;

@Service
public class SearchService {

	@Autowired
	private SearchRepository reportRepository;

	@Autowired
	private Response responseInfoFactory;
	
	@Autowired
	private IntegrationService integrationService;
	
	

	public static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);

	public MetadataResponse getMetaData(MetaDataRequest metaDataRequest, String moduleName) {
		MetadataResponse metadataResponse = new MetadataResponse();
		ReportDefinitions rds = SearchApp.getReportDefs();
		SearchDefinition reportDefinition = new SearchDefinition();
		//LOGGER.info("updated repot defs " + ReportApp.getReportDefs() + "\n\n\n");
		reportDefinition = rds.getReportDefinition(moduleName+" "+metaDataRequest.getReportName());
		ReportMetadata rmt = new ReportMetadata();
		rmt.setReportName(reportDefinition.getReportName());
        rmt.setSummary(reportDefinition.getSummary());
        rmt.setSearchFilter(reportDefinition.isSearchFilter());
        rmt.setSorting(reportDefinition.isSorting());
		List<ColumnDetail> reportHeaders = new ArrayList<>();
		List<ColumnDetail> searchParams = new ArrayList<>();
		
		for (SearchColumn cd : reportDefinition.getSearchParams()) {
			
			ColumnDetail sc = new ColumnDetail();
			
			TypeEnum te = TypeEnum.valueOf(cd.getType().toString().toUpperCase());
			sc.setType(te);
			sc.setLabel(cd.getLabel());
			sc.setName(cd.getName());
            sc.setShowColumn(cd.getShowColumn());
			sc.setDefaultValue(cd.getPattern());
			sc.setIsMandatory(cd.getIsMandatory());

            sc.setColumnTotal(cd.getColumnTotal());
            sc.setRowTotal(cd.getRowTotal());

			searchParams.add(sc);

		}
		rmt.setReportHeader(reportHeaders);
		rmt.setSearchParams(searchParams);
		metadataResponse.setReportDetails(rmt);
		metadataResponse.setTenantId(metaDataRequest.getTenantId());
		try {
			integrationService.getData(reportDefinition, metadataResponse, metaDataRequest.getRequestInfo(),moduleName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return metadataResponse;
	}

	

	public ResponseEntity<?> getSuccessResponse(final MetadataResponse metadataResponse, final RequestInfo requestInfo,
			String tenantID) {
		final MetadataResponse metadataResponses = new MetadataResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		metadataResponses.setRequestInfo(responseInfo);
		metadataResponses.setTenantId(tenantID);
		metadataResponses.setReportDetails(metadataResponse.getReportDetails());
		return new ResponseEntity<>(metadataResponses, HttpStatus.OK);

	}
	public ResponseEntity<?> getReportDataSuccessResponse(final List<ReportResponse> reportResponse, final RequestInfo requestInfo
			,String tenantId) {
		final ReportDataResponse reportDataResponse = new ReportDataResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		reportDataResponse.setResponseInfo(responseInfo);
		reportDataResponse.setTenantId(tenantId);
		reportDataResponse.setReportResponses(reportResponse);
		return new ResponseEntity<>(reportDataResponse, HttpStatus.OK);

	}
	public ResponseEntity<?> getFailureResponse( final RequestInfo requestInfo,
			String tenantID) {
		final MetadataResponse metadataResponses = new MetadataResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);
		responseInfo.setResMsgId("Report Defintion not found");
		metadataResponses.setRequestInfo(responseInfo);
		metadataResponses.setTenantId(tenantID);
		return new ResponseEntity<>(metadataResponses, HttpStatus.NOT_FOUND);

	}

	public ResponseEntity<?> getFailureResponse( final RequestInfo requestInfo,
			String tenantID, Exception e) {
		final MetadataResponse metadataResponses = new MetadataResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);
		responseInfo.setResMsgId(e.getMessage());
		metadataResponses.setRequestInfo(responseInfo);
		metadataResponses.setTenantId(tenantID);
		return new ResponseEntity<>(metadataResponses, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	

	public ResponseEntity<?> reloadResponse( final RequestInfo requestInfo, Exception e) {

		final MetadataResponse metadataResponses = new MetadataResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		if(e != null) {
			responseInfo.setResMsgId("Report reloaded partially with Errors");
		}
		responseInfo.setResMsgId("Report reloaded successfully");
		metadataResponses.setRequestInfo(responseInfo);
		return new ResponseEntity<>(metadataResponses, HttpStatus.OK);

	}

	/*public List<ReportResponse> getAllReportData(ReportRequest reportRequest,String moduleName) {
		List<ReportResponse> reportResponse = new ArrayList<ReportResponse>();
		ReportDataResponse rdr = new ReportDataResponse();
		ReportResponse rResponse = new ReportResponse();
		ReportDefinitions rds = SearchApp.getReportDefs();
		List<String> subReportNames = new ArrayList<>();
		ReportDefinition reportDefinition = rds.getReportDefinition(moduleName+ " "+reportRequest.getReportName());
		if(reportDefinition.isSubReport()) {
			rResponse = getReportData(reportRequest, moduleName, reportRequest.getReportName());
			reportResponse.add(rResponse);
			subReportNames = reportDefinition.getSubReportNames();
			for(String sr : subReportNames) {
				rResponse = getReportData(reportRequest,moduleName,sr);
				reportResponse.add(rResponse);				
			}
		}else {
			rResponse = getReportData(reportRequest,moduleName,reportRequest.getReportName());
			reportResponse.add(rResponse);
		}
		rdr.setReportResponses(reportResponse);
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(reportRequest.getRequestInfo(), false);
		rdr.setResponseInfo(responseInfo);
		return reportResponse;
	}*/



	public List<String> getReportData(ReportRequest reportRequest,String moduleName,String reportName) {
		
	
		ReportDefinitions reportDefinitions = SearchApp.getReportDefs();
		SearchDefinition reportDefinition = reportDefinitions.getReportDefinition(moduleName+ " "+reportName);
		
		List<Object> convertedMaps = new ArrayList<>();
		
		
		LOGGER.info("Incoming Report Name is "+reportName);
		List<String> maps = reportRepository.getData(reportRequest, reportDefinition);
		//List<Map<String, Object>> maps = null;
		Object indexMap = null;
		CustomJsonMapping customJsonMappings = null;
	   /* for(Object test:maps ) 
	    {  
	        customJsonMappings = reportDefinition.getCustomMapping();
	    	indexMap = customJsonMappings.getIndexMapping();
	    	DocumentContext documentContext = JsonPath.parse(indexMap);
	    	System.out.println("Custom Mapping "+customJsonMappings);
	    	if(null != customJsonMappings.getFieldMapping() || !customJsonMappings.getFieldMapping().isEmpty()){
				for(FieldMapping fieldMapping: customJsonMappings.getFieldMapping()){
					String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
					StringBuilder expression = new StringBuilder();
					for(int i = 0; i < (expressionArray.length - 1) ; i++ ){
						expression.append(expressionArray[i]);
						if(i != expressionArray.length - 2)
							expression.append(".");
					}
					documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1],
							JsonPath.read(test, fieldMapping.getInjsonpath()));	
					
					
				}
				System.out.println("The updated document context is " +documentContext.jsonString());
				
				
				convertedMaps.add(documentContext.jsonString());
	    	}else{
	    		System.out.println("field mapping list is empty");
	    	}
	    }
		*/
		ReportResponse reportResponse = new ReportResponse();
		//populateData(columns, maps, reportResponse);
		populateReportHeader(reportDefinition, reportResponse);

		return maps;
	}

	private void populateData(List<SourceColumn> columns, List<Object> maps,
			ReportResponse reportResponse) {

		List<List<Object>> lists = new ArrayList<>();

		for (int i = 0; i < maps.size(); i++) {
			List<Object> objects = new ArrayList<>();
			
			/*Map<String, Object> map = maps.get(i);
			for (SourceColumn sourceColm : columns) {
				
				objects.add(map.get(sourceColm.getName()));
			}*/
			
		}
		/*lists.add(objects);*/
		lists.add(maps);
		
		reportResponse.setReportData(lists);
	}
	private void populateReportHeader(SearchDefinition reportDefinition, ReportResponse reportResponse) {
		
		//Let's check whether there's a linked report, we will set the default value in header columns according to that
				
				String pattern = null;
				String defaultValue = null;
		
	}
}
