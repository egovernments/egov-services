package org.egov.infra.indexer.bulkindexer;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
    private final ScheduledExecutorService scheduler =
    	       Executors.newScheduledThreadPool(1);
		
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
			logger.error("ES is DOWN, Pausing kafka listener....... ");
			pollESEveryTenSec();
		}catch(Exception e){
			logger.error("Exception while trying to index to ES. Note: ES is not Down.");
		}
	}
	
	public void pollESEveryTenSec(){
        final Runnable beeper = new Runnable() {
                public void run() { 
        			logger.info("Polling to check if ES is up.......");
                }
            };
        final ScheduledFuture<?> pollHandle =
            scheduler.scheduleAtFixedRate(beeper, 0, 10, TimeUnit.SECONDS);
        scheduler.schedule(new Runnable() {
                public void run() { 
                	pollHandle.cancel(true); 
                }
            }, 20 * 20, TimeUnit.SECONDS);
    }

}
