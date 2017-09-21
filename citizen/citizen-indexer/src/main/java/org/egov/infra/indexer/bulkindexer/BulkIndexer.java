package org.egov.infra.indexer.bulkindexer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BulkIndexer {
	
	public static final Logger logger = LoggerFactory.getLogger(BulkIndexer.class);

	@Autowired
	private RestTemplate restTemplate;
		
	public void indexJsonOntoES(String url, String indexJson){
		try {
			final HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        final HttpEntity<String> entity = new HttpEntity<>(indexJson, headers);
			logger.info("Indexing request JSON to elasticsearch: " + indexJson);
			restTemplate.postForObject(url.toString(), entity, Map.class);
		} catch (final Exception e) {
			logger.error("Indexing on elasticsearch failed: ",e);
		}
	}

}
