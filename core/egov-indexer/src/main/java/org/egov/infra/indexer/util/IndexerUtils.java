package org.egov.infra.indexer.util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.consumer.KafkaConsumerConfig;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.infra.indexer.web.contract.UriMapping;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class IndexerUtils {
	
	public static final Logger logger = LoggerFactory.getLogger(IndexerUtils.class);

	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private KafkaConsumerConfig kafkaConsumerConfig;
	
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;
	
	@Value("${elasticsearch.poll.interval.seconds}")
	private String pollInterval;
	
	@Autowired
	private BulkIndexer bulkIndexer;
	
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
        				kafkaConsumerConfig.startContainer();
        				threadRun = false;
        			}
                  }
                }
            };
         scheduler.scheduleAtFixedRate(esPoller, 0, Long.valueOf(pollInterval), TimeUnit.SECONDS);
	}
	
	public String pullArrayOutOfString(String jsonString){
		String[] array = jsonString.split(":");
		StringBuilder jsonArray = new StringBuilder(); 
		for(int i = 1; i < array.length ; i++ ){
			jsonArray.append(array[i]);
			if(i != array.length - 1)
				jsonArray.append(":");
		}
		jsonArray.deleteCharAt(jsonArray.length() - 1);
		
		return jsonArray.toString();
	}
	
	public String buildString(Object object){
		//JsonPath cannot be applied on the type JSONObject. String has to be built of it and then used.
		String[] array = object.toString().split(":");
		StringBuilder jsonArray = new StringBuilder(); 
		for(int i = 0; i < array.length ; i++ ){
			jsonArray.append(array[i]);
			if(i != array.length - 1)
				jsonArray.append(":");
		}
		return jsonArray.toString();		
	}
	
	public String buildUri(UriMapping uriMapping, String kafkaJson){
		StringBuilder serviceCallUri = new StringBuilder();	
		String uriWithPathParam = null;
		if(!StringUtils.isEmpty(uriMapping.getPath())) {
			uriWithPathParam = uriMapping.getPath();
			if(!StringUtils.isEmpty(uriMapping.getPathParam())) {
				uriWithPathParam = uriWithPathParam.replace("$", JsonPath.read(kafkaJson, uriMapping.getPathParam()).toString());
			}
			serviceCallUri.append(uriWithPathParam);
			if(!StringUtils.isEmpty(uriMapping.getQueryParam())){
				String[] queryParamsArray = uriMapping.getQueryParam().split(",");
				for(int i = 0; i < queryParamsArray.length; i++){
					String[] queryParamExpression = queryParamsArray[i].trim().split("=");
					String queryParam = null;
					try {
						queryParam = JsonPath.read(kafkaJson, queryParamExpression[1].trim());
					}catch(Exception e) {
						continue;
					}
					StringBuilder resolvedParam = new StringBuilder();
					resolvedParam.append(queryParamExpression[0].trim()).append("=").append(queryParam.trim());
					queryParamsArray[i] = resolvedParam.toString().trim();
				}
				StringBuilder queryParams = new StringBuilder();
				for(int i = 0; i < queryParamsArray.length; i++){
					queryParams.append(queryParamsArray[i]);
					if(i != queryParamsArray.length - 1)
						queryParams.append("&");
				}
				serviceCallUri.append("?").append(queryParams.toString());
			}
		}else{
			serviceCallUri.append(uriMapping.getPath());
		}
		return serviceCallUri.toString();
	}
	
	public String buildIndexId(Index index, String stringifiedObject){
		String[] idFormat = index.getId().split("[,]");
		StringBuilder id = new StringBuilder();
		try{
			if(0 == idFormat.length){
				id.append(JsonPath.read(stringifiedObject, index.getId()).toString());
			}else{
				for(int j = 0; j < idFormat.length; j++){
					id.append(JsonPath.read(stringifiedObject, idFormat[j]).toString());
				} 
			}
		}catch(Exception e){
			logger.error("No id found at the given jsonpath: ", e);
			throw e;
		}
		return id.toString();
	}
	
	public JSONArray constructArrayForBulkIndex(String kafkaJson, Index index, boolean isBulk) throws Exception{
        String jsonArray = null;
        JSONArray kafkaJsonArray = null;
        ObjectMapper mapper = new ObjectMapper();
        try{
	    	if(isBulk){
	    		//Validating if the request is a valid json array.
				jsonArray = pullArrayOutOfString(kafkaJson);   
				if(null != index.getJsonPath()){
		    		if(JsonPath.read(kafkaJson, index.getJsonPath()) instanceof net.minidev.json.JSONArray){
		    			String inputArray = mapper.writeValueAsString(JsonPath.read(kafkaJson, index.getJsonPath()));
		    			kafkaJsonArray = new JSONArray(inputArray);
		    		}
	    		}else if((jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
	    			kafkaJsonArray = new JSONArray(jsonArray);
		        }else{
					logger.info("Invalid request for a json array!");
					return null;
		        }
	        }else{
	        	if(null != index.getJsonPath()){
	        		kafkaJson = mapper.writeValueAsString(JsonPath.read(kafkaJson, index.getJsonPath()));
		        	jsonArray = "[" + kafkaJson + "]";
	        	}else{
		        	jsonArray = "[" + kafkaJson + "]";
	        	}
				kafkaJsonArray = new JSONArray(jsonArray);
	        }
        }catch(Exception e){
        	logger.error("Exception while constructing json array for bulk index: ", e);
        	throw e;
        }
    	
    	return kafkaJsonArray;
	}
	
	public void validateAndIndex(String finalJson, String url, Index index) throws Exception{
		if(!StringUtils.isEmpty(finalJson)){
			doIndexing(finalJson, url.toString(), index);
		}else{
			logger.info("Indexing will not be done, please modify the data and retry.");
		}
	}
	
	public void doIndexing(String finalJson, String url, Index index) throws Exception{
		if(finalJson.startsWith("{ \"index\""))
			bulkIndexer.indexJsonOntoES(url.toString(), finalJson);
		else{
			indexWithESId(index, finalJson);
		}
	}
	
	public void indexWithESId(Index index, String finalJson) throws Exception{
		StringBuilder urlForNonBulk = new StringBuilder();
		urlForNonBulk.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/").append("_index");
		bulkIndexer.indexJsonOntoES(urlForNonBulk.toString(), finalJson);
	}
	
	public String getProcessedJsonPath(String jsonPath) {
		String[] expressionArray = (jsonPath).split("[.]");
		StringBuilder expression = new StringBuilder();
		for(int i = 0; i < (expressionArray.length - 1) ; i++ ){
			expression.append(expressionArray[i]);
			if(i != expressionArray.length - 2)
				expression.append(".");
		}
		return expression.toString();
	}

}