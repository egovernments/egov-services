package org.egov.infra.indexer.util;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.egov.infra.indexer.consumer.KafkaConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IndexerUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(IndexerUtils.class);

	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private KafkaConsumerConfig kafkaConsumerConfig;
	
	@Value("${egov.services.infra.indexer.host}")
	private String esHostUrl;
	
	@Value("${elasticsearch.poll.interval.seconds}")
	private String pollInterval;
	
    private final ScheduledExecutorService scheduler =
    	       Executors.newScheduledThreadPool(1);
	
	
	public void orchestrateListenerOnESHealth(){
		kafkaConsumerConfig.pauseContainer();
		logger.info("Polling ES....");
        final Runnable esPoller = new Runnable() {
    		boolean threadRun = true;
                public void run() {
                	if(threadRun){
        	        Object response = null;
        			try{
        				StringBuilder url = new StringBuilder();
        				url.append(esHostUrl)
        					.append("/_search");
        				response = restTemplate.getForObject(url.toString(), Map.class);
        			}catch(Exception e){
        				logger.error("ES is DOWN..");
        			}
        			if(response != null){
        				logger.info("ES is UP!");
        				kafkaConsumerConfig.resumeContainer();
        				threadRun = false;
        			}
                  }
                }
            };
         scheduler.scheduleAtFixedRate(esPoller, 0, Long.valueOf(pollInterval), TimeUnit.SECONDS);
	}

}
