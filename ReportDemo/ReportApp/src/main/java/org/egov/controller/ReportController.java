package org.egov.controller;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.domain.model.MetaDataRequest;
import org.egov.domain.model.ReportDefinitions;
import org.egov.domain.model.ReportYamlMetaData;
import org.egov.domain.model.ReportYamlMetaData.searchParams;
import org.egov.domain.model.ReportYamlMetaData.sourceColumns;
import org.egov.domain.model.Response;
import org.egov.swagger.model.ColumnDetail;
import org.egov.swagger.model.ColumnDetail.TypeEnum;
import org.egov.swagger.model.MetadataResponse;
import org.egov.swagger.model.ReportMetadata;
import org.egov.swagger.model.ReportRequest;
import org.egov.swagger.model.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

	public ReportDefinitions reportDefinitions;
	public ReportYamlMetaData reportYamlMetaData;
	@Autowired
	public ReportController(ReportDefinitions reportDefinitions) {
		this.reportDefinitions = reportDefinitions;
	}

	@Autowired
	private Response responseInfoFactory;
	


	@PostMapping("/report/metadata/_get")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final MetaDataRequest metaDataRequest,
			final BindingResult errors) {
		MetadataResponse mdr = getMetaData(metaDataRequest.getReportName());
		return getSuccessResponse(mdr, metaDataRequest.getRequestInfo(),metaDataRequest.getTenantId());
		
	}
	
	@PostMapping("/report/generateSQL")
	@ResponseBody
	public String generateSQL(@RequestBody @Valid  ReportRequest reportRequest, 
			final BindingResult errors) {
		
		String query = reportYamlMetaData.getQuery();
		String[] splitQueries = query.split("where"); 
		
		
		
		//Appending Source Columns as Comma Separated Values using String Builder Class
		if(reportYamlMetaData.getSourceColumns().size() > 0 ){
			for(int i =0 ; i < reportYamlMetaData.getSourceColumns().size() ; i ++ ){
				String nameToFind = reportYamlMetaData.getSourceColumns().get(i).getName();
				nameToFind = "\\{" + nameToFind + "\\}"; 
				splitQueries[0] = splitQueries[0].replaceAll(nameToFind, reportYamlMetaData.getSourceColumns().get(i).getName());
			}
		}
		
		List<SearchParam> listFromRequest = reportRequest.getSearchParams();
		if(reportYamlMetaData.getSearchParams().size() == listFromRequest.size()){
			for(int i = 0 ; i < reportYamlMetaData.getSearchParams().size() ; i++) {
				String nameToFind = reportYamlMetaData.getSearchParams().get(i).getName();
				for(int j = 0 ; j < listFromRequest.size() ; j++) {
					if(nameToFind.equals(listFromRequest.get(j).getName())){
						String replaceString = "\\{"+nameToFind+"\\}";
						splitQueries[1] = splitQueries[1].replaceAll(replaceString, listFromRequest.get(j).getValue());
					}
				}
			}
		}
		String queryAfterReplacing = splitQueries[0] + splitQueries[1];
		return queryAfterReplacing;
	}
	
	private ResponseEntity<?> getSuccessResponse(final MetadataResponse metadataResponse, final RequestInfo requestInfo,String tenantID) {
		final MetadataResponse metadataResponses = new MetadataResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		metadataResponses.setRequestInfo(responseInfo);
		metadataResponses.setTenantId(tenantID);
		metadataResponses.setReportDetails(metadataResponse.getReportDetails());
		return new ResponseEntity<>(metadataResponses, HttpStatus.OK);

	}	
	
	public MetadataResponse getMetaData(String reportName){
		MetadataResponse metadataResponse = new MetadataResponse();
		ReportYamlMetaData reportYamlMetaData = new ReportYamlMetaData();
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
	
}