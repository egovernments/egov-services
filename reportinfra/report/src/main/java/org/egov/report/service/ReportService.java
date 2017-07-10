package org.egov.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.domain.model.MetaDataRequest;
import org.egov.domain.model.ReportDefinitions;
import org.egov.domain.model.ReportYamlMetaData;
import org.egov.domain.model.ReportYamlMetaData.sourceColumns;
import org.egov.domain.model.Response;
import org.egov.report.repository.ReportRepository;
import org.egov.swagger.model.ColumnDetail;
import org.egov.swagger.model.ColumnDetail.TypeEnum;
import org.egov.swagger.model.MetadataResponse;
import org.egov.swagger.model.ReportDefinition;
import org.egov.swagger.model.ReportMetadata;
import org.egov.swagger.model.ReportRequest;
import org.egov.swagger.model.ReportResponse;
import org.egov.swagger.model.SearchColumn;
import org.egov.swagger.model.SourceColumn;
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
	
	@Autowired
	private IntegrationService integrationService;

	public static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

	public MetadataResponse getMetaData(MetaDataRequest metaDataRequest) {
		MetadataResponse metadataResponse = new MetadataResponse();
		ReportDefinition reportDefinition = new ReportDefinition();
        System.out.println("Report Definitions from service "+reportDefinitions.getReportDefinitions());
		for (ReportDefinition rDefinition : reportDefinitions.getReportDefinitions()) {
			if (rDefinition.getReportName().equals(metaDataRequest.getReportName())) {

				reportDefinition = rDefinition;
			}
		}
		ReportMetadata rmt = new ReportMetadata();
		rmt.setReportName(reportDefinition.getReportName());

		List<ColumnDetail> reportHeaders = new ArrayList<>();
		List<ColumnDetail> searchParams = new ArrayList<>();
		for (SourceColumn cd : reportDefinition.getSourceColumns()) {
			ColumnDetail reportheader = new ColumnDetail();
			reportheader.setLabel(cd.getLabel());
			reportheader.setName(cd.getName());
			TypeEnum te = getType(cd.getType().toString());
			reportheader.setType(te);
			reportHeaders.add(reportheader);
            
		}
		for (SearchColumn cd : reportDefinition.getSearchParams()) {
			ColumnDetail searchParam = new ColumnDetail();
			searchParam.setLabel(cd.getLabel());
			searchParam.setName(cd.getName());
			TypeEnum te = getType(cd.getType().toString());
			searchParam.setType(te);
			searchParams.add(searchParam);

		}
		rmt.setReportHeader(reportHeaders);
		rmt.setSearchParams(searchParams);
		metadataResponse.setReportDetails(rmt);
		metadataResponse.setTenantId(metaDataRequest.getTenantId());
		integrationService.getData(reportDefinition, metadataResponse, metaDataRequest.getRequestInfo());
		return metadataResponse;
	}

	public TypeEnum getType(String type) {
		if (type.equals("string")) {
			return ColumnDetail.TypeEnum.STRING;
		}
		if (type.equals("number")) {
			return ColumnDetail.TypeEnum.NUMBER;
		}
		if (type.equals("epoch")) {
			return ColumnDetail.TypeEnum.EPOCH;
		}
		if (type.equals("singlevaluelist")) {
			return ColumnDetail.TypeEnum.SINGLEVALUELIST;
		}
		
		return null;
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

	public ReportResponse getReportData(ReportRequest reportRequest) {
		
		List<ReportDefinition> listReportDefinitions  = reportDefinitions.getReportDefinitions();
		 
		ReportDefinition reportDefinition = listReportDefinitions.stream()
				.filter(t -> t.getReportName().equals(reportRequest.getReportName())).findFirst().orElse(null);
		LOGGER.info("reportYamlMetaData::" + reportDefinition);
		List<Map<String, Object>> maps = reportRepository.getData(reportRequest, reportDefinition);
		List<SourceColumn> columns = reportDefinition.getSourceColumns();
		LOGGER.info("columns::" + columns);
		LOGGER.info("maps::" + maps);

		ReportResponse reportResponse = new ReportResponse();
		populateData(columns, maps, reportResponse);
		populateReportHeader(reportDefinition, reportResponse);

		return reportResponse;
	}

	private void populateData(List<SourceColumn> columns, List<Map<String, Object>> maps,
			ReportResponse reportResponse) {

		List<List<Object>> lists = new ArrayList<>();

		for (int i = 0; i < maps.size(); i++) {
			List<Object> objects = new ArrayList<>();
			Map<String, Object> map = maps.get(i);
			for (SourceColumn sourceColm : columns) {
				
				objects.add(map.get(sourceColm.getName()));
			}
			lists.add(objects);
		}
		reportResponse.setReportData(lists);
	}
	private void populateReportHeader(ReportDefinition reportDefinition, ReportResponse reportResponse) {
		List<SourceColumn> columns = reportDefinition.getSourceColumns();
		List<ColumnDetail> columnDetails = columns.stream()
				.map(p -> new ColumnDetail(p.getLabel(), p.getType(),p.getName()))
				.collect(Collectors.toList());
		
		reportResponse.setReportHeader(columnDetails);
	}
}
