package org.egov.search.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.SearchApplicationRunnerImpl;
import org.egov.search.controller.SearchController;
import org.egov.search.model.Definition;
import org.egov.search.model.Params;
import org.egov.search.model.SearchDefinition;
import org.egov.search.model.SearchParams;
import org.egov.search.model.SearchRequest;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class SearchReqValidator {
	
	public static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private SearchApplicationRunnerImpl runner;
	
	@Autowired
	private SearchUtils searchUtils;
	
	public void validate(SearchRequest searchRequest, String moduleName, String searchName) {
		Map<String, SearchDefinition> searchDefinitionMap = runner.getSearchDefinitionMap();
		Definition searchDefinition = null;
		try{
			searchDefinition = searchUtils.getSearchDefinition(searchDefinitionMap, moduleName, searchName);
		}catch(CustomException e){
			throw e;
		}
		logger.info("Definition being used for process: "+searchDefinition);
		validateSearchDefAgainstReq(searchDefinition, searchRequest);
	}
	
	public void validateSearchDefAgainstReq(Definition searchDefinition, SearchRequest searchRequest) {
		SearchParams searchParams = searchDefinition.getSearchParams();
		Map<String, String> errorMap = new HashMap<>();
		if(null == searchParams) {
			errorMap.put("400", "Missiing Configurations for: "+searchDefinition.getName());
		}
		ObjectMapper mapper = new ObjectMapper();
		List<Params> params = searchParams.getParams().parallelStream()
				.filter(param -> param.getIsMandatory())
				.collect(Collectors.toList());
		
		for(Params param: params) {
			try {
				Object paramValue = JsonPath.read(mapper.writeValueAsString(searchRequest), param.getJsonPath());
				if(null == paramValue)
					errorMap.put("400", "Missiing Mandatory Property: "+param.getJsonPath());
			}catch(Exception e) {
				errorMap.put("400", "Missiing Mandatory Property: "+param.getJsonPath());
			}
		}
		
		throw new CustomException(errorMap);
		
		
	}
}
