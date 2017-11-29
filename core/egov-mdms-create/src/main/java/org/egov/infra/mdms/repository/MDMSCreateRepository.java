package org.egov.infra.mdms.repository;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.tomcat.util.codec.binary.Base64;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class MDMSCreateRepository {
	
	public static final Logger logger = LoggerFactory.getLogger(MDMSCreateRepository.class);
	
	@Autowired
	private RestTemplate restTemplate;
		
	public Object get(String uri, String userName, String password){
		Object result = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = userName + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64( 
           auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        headers.set( "Authorization", authHeader );
        logger.info("Generated authHeader: "+authHeader);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> res = null;
		try{
			res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		}catch(Exception e){
			logger.error("Couldn't fetch data from git: ",e);
			throw new CustomException("500", "Couln't fetch data from git");
		}
		result = res.getBody();
		return result;
	}
	
	public Object post(String uri, String body, String userName, String password){
		Object result = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = userName + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64( 
           auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        headers.set( "Authorization", authHeader );
        logger.info("Generated authHeader: "+authHeader);
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<String> res = null;
		try{
			res = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		}catch(Exception e){
			logger.error("Couldn't post to git: ",e);
			throw new CustomException("500", "Couln't post file contents from git");
		}
		result = res.getBody();
		return result;
	}
	
	public Map<String, Object> getFileContents(String filePath) throws Exception{
		logger.info("Reading....: "+filePath);
		URL yamlFile = new URL(filePath);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = new WeakHashMap<>();
		try{
			result = mapper.readValue(new InputStreamReader(yamlFile.openStream()), Map.class);
		} catch(Exception e) {
			logger.error("Exception while fetching data for: "+filePath+" = ",e);
			throw new CustomException("400", "No data avaialble for this master");
		}
		logger.info("Parsed to object: "+result);
		
		return result;
	}
	
	public void updateCache(String filePath){
		try{
			//make rest call to update cache.
		}catch(Exception e){
			
		}
	}

}
