package org.egov.win.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.egov.tracer.model.CustomException;
import org.egov.win.model.SearcherRequest;
import org.egov.win.repository.CronRepository;
import org.egov.win.utils.CronUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExternalAPIService {
	
	@Autowired
	private CronRepository repository;

	@Autowired
	private CronUtils utils;
	
	public List<Map<String, Object>> getRainmakerData(String defName) {
		StringBuilder uri = new StringBuilder();
		ObjectMapper mapper = utils.getObjectMapper();
		List<Map<String, Object>> data = new ArrayList<>();
		SearcherRequest request = utils.preparePlainSearchReq(uri, defName);
		Optional<Object> response = repository.fetchResult(uri, request);
		try {
			if(response.isPresent()) {
				Object parsedResponse = mapper.convertValue(response.get(), Map.class);
				List<Object> dataParsedToList = mapper.convertValue(JsonPath.read(parsedResponse, "$.data"), List.class);
				for (Object record : dataParsedToList) {
					data.add(mapper.convertValue(record, Map.class));
				}
			}

		} catch (Exception e) {
			throw new CustomException("EMAILER_DATA_RETREIVAL_FAILED", "Failed to retrieve data from the db");
		}

		return data;

	}
	
	
	public List<Map<String, Object>> getWSData() {
		StringBuilder uri = new StringBuilder();
		ObjectMapper mapper = utils.getObjectMapper();
		List<Map<String, Object>> data = new ArrayList<>();
		utils.prepareWSSearchReq(uri);
		Object request = "{}";
		Optional<Object> response = repository.fetchResult(uri, request);
		try {
			if(response.isPresent()) {
				List<Object> dataParsedToList = mapper.convertValue(response.get(), List.class);
				for (Object record : dataParsedToList) {
					data.add(mapper.convertValue(record, Map.class));
				}
			}
		} catch (Exception e) {
			throw new CustomException("EMAILER_DATA_RETREIVAL_FAILED", "Failed to retrieve data from WS module");
		}

		return data;

	}

}
