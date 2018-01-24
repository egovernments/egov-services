package org.egov.infra.mdms.repository;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.tomcat.util.codec.binary.Base64;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${reload.path.host}")
	private String reloadPathHost;
			
	@Value("${reload.path.endpoint}")
	private String reloadPathEndpoint;
	
	@Value("${reloadobj.path.endpoint}")
	private String reloadobjPathEndpoint;
	
	@Value("${search.path.endpoint}")
	private String searchEndpoint;

	
	public Object get(String uri, String userName, String password){
		Object result = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = userName + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64( 
           auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        headers.set( "Authorization", authHeader );
        logger.debug("Generated authHeader: "+authHeader);
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
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String auth = userName + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64( 
           auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        headers.set( "Authorization", authHeader );
        logger.debug("Generated authHeader: "+authHeader);
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
		logger.debug("Parsed to object: "+result);
		
		return result;
	}
	
	public Map<String, Object> getContentFromCache(MdmsCriteriaReq mdmsCriteriaReq) throws Exception{
		StringBuilder uri = new StringBuilder();
		Map<String, Object> result = new HashMap<>();
		uri.append(reloadPathHost)
		   .append(searchEndpoint);
		logger.info("URI: "+uri.toString());
		try{
			result = restTemplate.postForObject(uri.toString(), mdmsCriteriaReq, Map.class);
		}catch(Exception e){
			logger.error("Exception while getting content from cache cache for request: "+mdmsCriteriaReq+" = ",e);
			return null;
		}
		
		return result;
	}
	
	public void updateCache(String filePath, String tenantId, RequestInfo requestInfo){
		StringBuilder uri = new StringBuilder();
		uri.append(reloadPathHost)
		   .append(reloadPathEndpoint)
		   .append("?filePath="+filePath)
		   .append("&tenantId="+tenantId);
		logger.info("URI: "+uri.toString());
		try{
			 restTemplate.postForObject(uri.toString(), requestInfo, String.class);
		}catch(Exception e){
			logger.error("Exception while updating cache for: "+filePath+" = ",e);
		}
	}
	
	public void updateCache(String reloadReq){
		StringBuilder uri = new StringBuilder();
		uri.append(reloadPathHost)
		   .append(reloadobjPathEndpoint);
		logger.info("URI: "+uri.toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(reloadReq, headers);
		try{
			restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, String.class);
		}catch(Exception e){
			logger.error("Exception while updating cache for data: "+reloadReq+" = ",e);
		}
	}

}
