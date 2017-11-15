package org.egov.dataupload.repository;

import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;



@Repository
public class DataUploadRepository {
	
	@Autowired
	private RestTemplate restTemplate;
			
	public static final Logger LOGGER = LoggerFactory.getLogger(DataUploadRepository.class);
		
	public Object doApiCall(Object request, String url) {
		Object response = null;
		try{
			response = restTemplate.postForObject(url, request, Map.class);
		}catch(Exception e){
			LOGGER.error("Exception while hitting url: "+url, e);
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), 
					"Exception while hitting url: "+url);
		}
		return response;
	}

}
