package org.egov.infra.indexer.bulkindexer;

import java.util.Map;
import org.egov.infra.indexer.util.IndexerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class BulkIndexer {
	
	public static final Logger logger = LoggerFactory.getLogger(BulkIndexer.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IndexerUtils indexerUtils;
	
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;
	
	@Value("${elasticsearch.poll.interval.seconds}")
	private String pollInterval;
		
	public void indexJsonOntoES(String url, String indexJson){
		try{
			final HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        final HttpEntity<String> entity = new HttpEntity<>(indexJson, headers);
			logger.info("Indexing request JSON to elasticsearch: " + indexJson);
			Object response = restTemplate.postForObject(url.toString(), entity, Map.class);
			logger.info("Indexing SUCCESSFULL!");
			logger.info("Response from ES: "+response);
		}catch(final ResourceAccessException e){
			logger.error("ES is DOWN, Pausing kafka listener.......");
			indexerUtils.orchestrateListenerOnESHealth();
		}catch(Exception e){
			logger.error("Exception while trying to index to ES. Note: ES is not Down.",e);
		}
	}

}
