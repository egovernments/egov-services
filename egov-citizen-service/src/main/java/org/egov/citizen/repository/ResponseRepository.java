package org.egov.citizen.repository;

import java.util.List;

import org.egov.citizen.model.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

@Repository
public class ResponseRepository {
	
	public static final Logger LOGGER = LoggerFactory
			.getLogger(CollectionRepository.class);
	
	@Autowired
	public RestTemplate restTemplate;
	
	public Object generateResponseObject(String url, RequestInfoWrapper requestInfo, List<String> results){
		
		if (url != "") {
			Object response = restTemplate.postForObject(url, requestInfo, Object.class);
			
			if (results != null && results.size() != 0) {
				Object responseObj = JsonPath.read(response, results.get(0));
				Object responseObj1 = JsonPath.read(responseObj, "$..Demands[0]");
				if (responseObj1 != "") {
					Object res = JsonPath.read(response, results.get(1));
					return res;
				}
				return responseObj;
			}
		}
	   return "";	
	}

}
