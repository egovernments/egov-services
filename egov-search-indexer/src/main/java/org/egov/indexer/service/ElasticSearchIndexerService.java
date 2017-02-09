package org.egov.indexer.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticSearchIndexerService {

	//TODO : need to add it as configurational param
	public static final String ELASTICSEARCH_INDEX_URL = "http://localhost:9200/";

	public void indexObject(String indexName, Object indexObject) throws Exception {
		restTempl(indexName, indexObject);
	}

	/**
	 * @param indexName
	 * @param indexObject
	 */
	public void restTempl(String indexName, Object indexObject) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(indexObject, headers);
		try {
			// Creating index object
			String indexId = getIndexId(indexObject);
			String url = ELASTICSEARCH_INDEX_URL + indexName + "/" + indexName + "/" + indexId;
			restTemplate.postForObject(url, entity, Map.class);
		} catch (Exception ex) {
			throw new Exception("Error Indexing Object in Elastic Search!!" + ex.getMessage());
		}
	}

	/**
	 * @description Generates elasticsearch index id
	 * @param indexObject
	 * @return
	 */
	public String getIndexId(Object indexObject) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> objectMap = objectMapper.convertValue(indexObject, Map.class);
		// TODO : Need to decide on the id format.
		if (!objectMap.isEmpty())
			return objectMap.get("id").toString();
		else
			throw new Exception("Elastic Search : Id is null!!");
	}
}
