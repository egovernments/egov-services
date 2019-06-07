package org.egov.search.controller;

import java.lang.reflect.Type;
import java.util.Map;

import javax.validation.Valid;

import org.egov.search.model.SearchRequest;
import org.egov.search.service.SearchService;
import org.egov.search.utils.SearchReqValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
public class SearchController {
		
	@Autowired
	private SearchService searchService;
	
	@Autowired
	private SearchReqValidator searchReqValidator;
		
	@Autowired
    public static ResourceLoader resourceLoader;
	
	public static final Logger logger = LoggerFactory.getLogger(SearchController.class);

	@PostMapping("/{moduleName}/{searchName}/_get")
	@ResponseBody
	public ResponseEntity<?> getReportData(@PathVariable("moduleName") String moduleName,
			@PathVariable("searchName") String searchName,
			@RequestBody @Valid final SearchRequest searchRequest) {		
		Object searchResult = null;
		try {
			searchReqValidator.validate(searchRequest, moduleName, searchName);
			searchResult = searchService.searchData(searchRequest,moduleName,searchName);
		    Type type = new TypeToken<Map<String, Object>>() {}.getType();
			Gson gson = new Gson();
			Map<String, Object> data = gson.fromJson(searchResult.toString(), type);
			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch(Exception e){
			if(null != searchResult)
				return new ResponseEntity<>(searchResult, HttpStatus.OK);
			else
				throw e;
		}
	}

		
}