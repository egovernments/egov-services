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
import org.egov.swagger.model.ReportDefinition;
import org.egov.swagger.model.ReportMetadata;
import org.egov.swagger.model.ReportRequest;
import org.egov.swagger.model.SearchColumn;
import org.egov.swagger.model.SearchParam;
import org.egov.swagger.model.SourceColumn;
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

	private static final String UTF_8 = "UTF-8";
	
	
	
	public ReportDefinitions reportDefinitions;
	@Autowired
	public ReportController(ReportDefinitions reportDefinitions) {
		// TODO Auto-generated constructor stub
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
	public void generateSQL(@RequestBody @Valid  ReportRequest reportRequest, 
			final BindingResult errors) {
		
		ReportDefinition rdef = createReportDefinition();  //Once the Report Definition is integrated, this line to be removed
		
		reportRequest = reportRequestCreate();  	// Once the request is received in integrated, this line to be removed 
		StringBuilder sourceColumnsList = new StringBuilder();
		List<SourceColumn> sourceColumns = rdef.getSourceColumns();
		
		//Appending Source Columns as Comma Separated Values using String Builder Class
		if(sourceColumns.size() > 0 ){
			for(int i =0 ; i < sourceColumns.size() ; i ++ ){
				if((i == 0 && i == sourceColumns.size()-1) || (i == sourceColumns.size()-1)){
					sourceColumnsList.append(sourceColumns.get(i).getColName());
				} else { 
					sourceColumnsList.append(sourceColumns.get(i).getColName() + ",");
				}
			}
		}
		
		// Replacing * in the Query with the Source Columns
		String query = rdef.getQuery();
		query  = query.replaceAll("\\*", sourceColumnsList.toString());
		String queryBeforeParamReplacing = query;
		// System.out.println(queryBeforeParamReplacing); // Printing Query with columns replaced. Before replacing parameters
		
		List<SearchParam> listFromRequest = reportRequest.getSearchParams();
		List<SearchColumn> listFromDef = rdef.getSearchParams();
		if(listFromDef.size() == listFromRequest.size()){
			for(int i = 0 ; i < listFromDef.size() ; i++) {
				String nameToFind = listFromDef.get(i).getName();
				for(int j = 0 ; j < listFromRequest.size() ; j++) {
					if(nameToFind.equals(listFromRequest.get(j).getName())){
						String replaceString = "\\{"+nameToFind+"\\}";
						query = query.replaceAll(replaceString, listFromRequest.get(j).getValue());
					}
				}
			}
		}
		String queryAfterReplacing = query;
		// System.out.println(queryAfterReplacing); // Printing Query after replacing the Query Params 
		
		
	}
	
	// This method is used to assume that there is an available ReportDefinition
	// This has to be removed. In real-time, this gets replaced
		private static ReportDefinition createReportDefinition() { 
			ReportDefinition rdef = new ReportDefinition();
			rdef.setSummary("TestReportSummary");
			rdef.setReportName("TestReportName");
			rdef.setQuery("SELECT * FROM table_name WHERE username = {userName} AND dateofbirth = {dateOfBirth} AND phone = {phone} AND email = {email}");
			SearchColumn scol1 = new SearchColumn();
			scol1.setColName("username");
			scol1.setLabel("User Name");
			scol1.setName("userName");
			SearchColumn scol2 = new SearchColumn();
			scol2.setColName("dateofbirth");
			scol2.setLabel("Date Of Birth");
			scol2.setName("dateOfBirth");
			SearchColumn scol3 = new SearchColumn();
			scol3.setColName("phone");
			scol3.setLabel("Phone Number");
			scol3.setName("phone");
			SearchColumn scol4 = new SearchColumn();
			scol4.setColName("email");
			scol4.setLabel("Email ID");
			scol4.setName("email");
			List<SearchColumn> searchParams = new ArrayList<>();
			searchParams.add(scol1);
			searchParams.add(scol2);
			searchParams.add(scol3);
			searchParams.add(scol4);
			
			SourceColumn sourceCol1 = new SourceColumn();
			sourceCol1.setColName("userName");
			SourceColumn sourceCol2 = new SourceColumn();
			sourceCol2.setColName("firstName");
			SourceColumn sourceCol3 = new SourceColumn();
			sourceCol3.setColName("lastName");
			SourceColumn sourceCol4 = new SourceColumn();
			sourceCol4.setColName("employeeCode");
			SourceColumn sourceCol5 = new SourceColumn();
			sourceCol5.setColName("designation");
			SourceColumn sourceCol6 = new SourceColumn();
			sourceCol6.setColName("department");
			SourceColumn sourceCol7 = new SourceColumn();
			sourceCol7.setColName("company");
			List<SourceColumn> sourceColumns = new ArrayList<>();
			sourceColumns.add(sourceCol1);
			sourceColumns.add(sourceCol2);
			sourceColumns.add(sourceCol3);
			sourceColumns.add(sourceCol4);
			sourceColumns.add(sourceCol5);
			sourceColumns.add(sourceCol6);
			sourceColumns.add(sourceCol7);
			rdef.setSearchParams(searchParams);
			rdef.setSourceColumns(sourceColumns);
			return rdef;
		}
	
	// This method is to mimick the request. Once the real request is received, this method can be removed
	private ReportRequest reportRequestCreate()
	{
		ReportRequest reportRequest = new ReportRequest();
		SearchParam sParam = new SearchParam();
		sParam.setName("userName");
		sParam.setValue("MyUserName");
		SearchParam sParam1 = new SearchParam();
		sParam1.setName("dateOfBirth");
		sParam1.setValue("07-OCT-1990");
		SearchParam sParam2 = new SearchParam();
		sParam2.setName("phone");
		sParam2.setValue("9778977899");
		SearchParam sParam3 = new SearchParam();
		sParam3.setName("email");
		sParam3.setValue("myemail@email.com");
		List<SearchParam> searchParams = new ArrayList<>();
		searchParams.add(sParam);
		searchParams.add(sParam1);
		searchParams.add(sParam2);
		searchParams.add(sParam3);
		reportRequest.setSearchParams(searchParams);
		return reportRequest;
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