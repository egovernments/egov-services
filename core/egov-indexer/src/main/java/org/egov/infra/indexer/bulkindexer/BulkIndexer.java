package org.egov.infra.indexer.bulkindexer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.indexer.util.IndexerUtils;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class BulkIndexer {
	
	public static final Logger logger = LoggerFactory.getLogger(BulkIndexer.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IndexerUtils indexerUtils;
			
	public void indexJsonOntoES(String url, String indexJson, Index index) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		try{
			logger.debug("Record being indexed: "+indexJson);
			final HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        final HttpEntity<String> entity = new HttpEntity<>(indexJson, headers);
			Object response = restTemplate.postForObject(url.toString(), entity, Map.class);
			if(url.contains("_bulk")){
				if(JsonPath.read(mapper.writeValueAsString(response), "$.errors").equals(true)){
					logger.info("Indexing FAILED!!!!");
					logger.info("Response from ES: "+response);
				}else{
					logger.info("Indexing SUCCESSFULL!");
				}
			}else{
				logger.info("Indexing SUCCESSFULL!");
			}
		}catch(final ResourceAccessException e){
			logger.error("ES is DOWN, Pausing kafka listener.......");
			indexerUtils.orchestrateListenerOnESHealth();
		}catch(Exception e){
			logger.error("Exception while trying to index to ES. Note: ES is not Down.",e);
			throw new CustomException("500", "Exception while trying to index to ES. Note: ES is not Down.");
		}
	}
	
	
	public Object getIndexMappingfromES(String url){
		Object response = null;
		try{
			logger.info("URI: "+url.toString());
			response = restTemplate.getForObject(url.toString(), Map.class);
		}catch(final ResourceAccessException e){
			logger.error("ES is DOWN, Pausing kafka listener.......");
			indexerUtils.orchestrateListenerOnESHealth();
		}catch(Exception e){
			logger.error("Exception while trying to fetch index mapping from ES. Note: ES is not Down.",e);
			return response;
		}
		logger.info("Mapping from ES: "+response);
		return response;

	}
	
	public Object getESResponse(String url) {
		Object response = null;
		try {
			response = restTemplate.getForObject(url, Map.class);
		}catch(Exception e) {
			logger.error("Exception while fetching from es: "+e);
		}
		return response;
	}

}
