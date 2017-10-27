package org.egov.controller;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.SearchApp;
import org.egov.common.contract.request.RequestInfo;
import org.egov.domain.model.MetaDataRequest;
import org.egov.domain.model.ReportDefinitions;
import org.egov.domain.model.Response;
import org.egov.domain.model.SearchResponse;
import org.egov.report.service.SearchService;
import org.egov.swagger.model.MetadataResponse;
import org.egov.swagger.model.ReportDataResponse;
import org.egov.swagger.model.ReportRequest;
import org.egov.swagger.model.ReportResponse;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.minidev.json.JSONObject;

@RestController
public class SearchController {

	public ReportDefinitions reportDefinitions;
	
	@Autowired
	public SearchController(ReportDefinitions reportDefinitions) {
		this.reportDefinitions = reportDefinitions;
	}
	
	@Autowired
	private SearchService reportService;
	
	@Autowired
	private Response responseInfoFactory;
	
	@Autowired
    public static ResourceLoader resourceLoader;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

	@PostMapping("/{moduleName}/metadata/_get")
	@ResponseBody
	public ResponseEntity<?> create(@PathVariable("moduleName") String moduleName,@RequestBody @Valid final MetaDataRequest metaDataRequest,
			final BindingResult errors) {
		try{
		System.out.println("The Module Name from the URI is :"+moduleName);
		MetadataResponse mdr = reportService.getMetaData(metaDataRequest,moduleName);
		return reportService.getSuccessResponse(mdr, metaDataRequest.getRequestInfo(),metaDataRequest.getTenantId());
		} catch(NullPointerException e){
			return reportService.getFailureResponse(metaDataRequest.getRequestInfo(),metaDataRequest.getTenantId());
		}
	}
	
	@PostMapping("/{moduleName}/_get")
	@ResponseBody
	public ResponseEntity<?> getReportData(@PathVariable("moduleName") String moduleName,@RequestBody @Valid final ReportRequest reportRequest,
			final BindingResult errors) {
		SearchResponse searchResponse = new SearchResponse();
		try {
			List<String> reportData = reportService.getReportData(reportRequest,moduleName,reportRequest.getReportName());
		    Type type = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
			Gson gson = new Gson();
			List<Map<String, Object>> data = gson.fromJson(reportData.toString(), type);
			LOGGER.info("service response: "+data);
			searchResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(reportRequest.getRequestInfo(), true));
			searchResponse.setData(data);
			return new ResponseEntity<>(searchResponse, HttpStatus.OK);
		} catch(NullPointerException e){
			e.printStackTrace();
			return reportService.getFailureResponse(reportRequest.getRequestInfo(),reportRequest.getTenantId());
		}
	}
	

	@PostMapping("/_reload")
	@ResponseBody
	public ResponseEntity<?> reloadYamlData(@RequestBody @Valid final MetaDataRequest reportRequest,
			final BindingResult errors) {
		try {
		SearchApp.loadYaml("common");
		} catch(Exception e){
			return reportService.getFailureResponse(reportRequest.getRequestInfo(),reportRequest.getTenantId(),e);
		}
		return reportService.reloadResponse(reportRequest.getRequestInfo(),null);

	}

		
}