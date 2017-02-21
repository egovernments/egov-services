package org.egov.web.indexer.service;

import java.util.Map;

import org.egov.web.indexer.config.IndexerPropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticSearchIndexerService {

	@Autowired
	private IndexerPropertiesManager propertiesManager;

	public void indexObject(String indexName, String indexId, Object indexObject) throws Exception {
		restTempl(indexName, indexId, indexObject);
	}

	/**
	 * @param indexName
	 * @param indexObject
	 */
	public void restTempl(String indexName, String indexId, Object indexObject) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(indexObject, headers);
		try {
			String url = propertiesManager.getElasticSearchHost() + indexName + "/" + indexName + "/" + indexId;
			restTemplate.postForObject(url, entity, Map.class);
		} catch (Exception ex) {
			throw new Exception("Error Indexing Object in Elastic Search!!" + ex.getMessage());
		}
	}
}
