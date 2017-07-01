package org.egov.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.domain.model.MetaDataRequest;

import org.egov.domain.model.ReportYamlMetaData;
import org.egov.domain.model.ReportYamlMetaData.searchParams;
import org.egov.domain.model.ReportYamlMetaData.sourceColumns;
import org.egov.domain.model.Response;

import org.egov.swagger.model.ColumnDetail;
import org.egov.swagger.model.ColumnDetail.TypeEnum;
import org.egov.swagger.model.MetadataRequest;
import org.egov.swagger.model.MetadataResponse;
import org.egov.swagger.model.ReportMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

	private static final String UTF_8 = "UTF-8";
	
	
	public ReportYamlMetaData reportYamlMetaData;
	@Autowired
	public ReportController(ReportYamlMetaData reportYamlMetaData) {
		// TODO Auto-generated constructor stub
		this.reportYamlMetaData = reportYamlMetaData;
	}

	@Autowired
	private Response responseInfoFactory;
	


	@PostMapping("/report/metadata/_get")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final MetaDataRequest metaDataRequest,
			final BindingResult errors) {
		MetadataResponse mdr = getMetaData();
		return getSuccessResponse(mdr, metaDataRequest.getRequestInfo(),metaDataRequest.getTenantId());
		
        
		
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
	
	public MetadataResponse getMetaData(){
		MetadataResponse metadataResponse = new MetadataResponse();
		
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