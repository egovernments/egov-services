package org.egov.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.domain.model.ReportDefinitions;
import org.egov.domain.model.ReportYamlMetaData;
import org.egov.domain.model.ReportYamlMetaData.searchParams;
import org.egov.domain.model.ReportYamlMetaData.sourceColumns;
import org.egov.domain.model.Response;
import org.egov.report.repository.ReportRepository;
import org.egov.swagger.model.ColumnDetail;
import org.egov.swagger.model.ColumnDetail.TypeEnum;
import org.egov.swagger.model.MetadataResponse;
import org.egov.swagger.model.ReportMetadata;
import org.egov.swagger.model.ReportRequest;
import org.egov.swagger.model.ReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
	
	@Autowired
	public ReportDefinitions reportDefinitions;
	
	@Autowired
	private ReportRepository reportRepository;
	

	@Autowired
	private Response responseInfoFactory;
	
public MetadataResponse getMetaData(String reportName){
		MetadataResponse metadataResponse = new MetadataResponse();
		ReportYamlMetaData reportYamlMetaData = new ReportYamlMetaData();
		System.out.println("Report Definition is" +reportDefinitions.getReportDefinitions());
		for(ReportYamlMetaData reportYaml : reportDefinitions.getReportDefinitions()) {
			if(reportYaml.getReportName().equals(reportName)){
			
				reportYamlMetaData = reportYaml;
			}
		}
		ReportMetadata rmt = new ReportMetadata();
		rmt.setReportName(reportYamlMetaData.getReportName());
		List<ColumnDetail> reportHeaders = new ArrayList<>();
		List<ColumnDetail> searchParams = new ArrayList<>();
		for(sourceColumns cd : reportYamlMetaData.getSourceColumns()){
			ColumnDetail reportheader = new ColumnDetail();
			reportheader.setLabel(cd.getLabel());
			reportheader.setName(cd.getName());
			TypeEnum te = getType(cd.getType());
			reportheader.setType(te);
			reportHeaders.add(reportheader);

		}
		for(searchParams cd : reportYamlMetaData.getSearchParams()){
			ColumnDetail searchParam = new ColumnDetail();
			searchParam.setLabel(cd.getLabel());
			searchParam.setName(cd.getName());
			TypeEnum te = getType(cd.getType());
			searchParam.setType(te);
			searchParams.add(searchParam);

		}
		rmt.setReportHeader(reportHeaders);
		rmt.setSearchParams(searchParams);
		metadataResponse.setReportDetails(rmt);
		return metadataResponse;
	}
	public TypeEnum getType(String type)
	{
		if(type.equals("string")){
			return ColumnDetail.TypeEnum.STRING;
		}
        if(type.equals("number")){
        	return ColumnDetail.TypeEnum.NUMBER;
		}
        if(type.equals("epoch")){
        	return ColumnDetail.TypeEnum.EPOCH;
		}
        if(type.equals("singlevaluelist")){
        	return ColumnDetail.TypeEnum.SINGLEVALUELIST;
		}
        
		return null;
	}
	public ResponseEntity<?> getSuccessResponse(final MetadataResponse metadataResponse, final RequestInfo requestInfo,String tenantID) {
		final MetadataResponse metadataResponses = new MetadataResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		metadataResponses.setRequestInfo(responseInfo);
		metadataResponses.setTenantId(tenantID);
		metadataResponses.setReportDetails(metadataResponse.getReportDetails());
		return new ResponseEntity<>(metadataResponses, HttpStatus.OK);

	}	

	public static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);


	public ReportResponse getReportData(ReportRequest reportRequest){
		List<ReportYamlMetaData> reportYamlMetaDatas = reportDefinitions.getReportDefinitions();
		ReportYamlMetaData reportYamlMetaData = reportYamlMetaDatas.stream().
				filter(t -> t.getReportName().equals(reportRequest.getReportName())).findFirst().orElse(null);
		LOGGER.info("reportYamlMetaData::"+reportYamlMetaData);
		List<Map<String, Object>> maps = reportRepository.getData(reportRequest, reportYamlMetaData);
		List<sourceColumns> columns = reportYamlMetaData.getSourceColumns();
		LOGGER.info("columns::"+columns);
		LOGGER.info("maps::"+maps);
		
		ReportResponse reportResponse = new ReportResponse();
		populateData(columns, maps, reportResponse);
		populateReportHeader(reportYamlMetaData, reportResponse);
		
		return reportResponse;
	}
	
	private void populateData(List<sourceColumns> columns, List<Map<String, Object>> maps, ReportResponse reportResponse){
		
		List<List<Object>> lists = new ArrayList<>();
		
		for(int i=0; i<maps.size(); i++){
			List<Object> objects = new ArrayList<>();
			Map<String, Object> map = maps.get(i);
			for(sourceColumns sourceColm : columns){
				objects.add(map.get(sourceColm.getName()));
			}
			lists.add(objects);
		}
		reportResponse.setReportData(lists);
	}
	
	private void populateReportHeader(ReportYamlMetaData reportYamlMetaData, ReportResponse reportResponse){
		List<sourceColumns>  columns = reportYamlMetaData.getSourceColumns();
		List<ColumnDetail> columnDetails = columns.stream().map(p -> new ColumnDetail(p.getLabel(), p.getName(), TypeEnum.fromValue(p.getType()))).collect(Collectors.toList());
		reportResponse.setReportHeader(columnDetails);
	}
}
