package org.egov.infra.indexer.service;

import java.util.Map;

import org.egov.infra.indexer.IndexerApplicationRunnerImpl;
import org.egov.infra.indexer.IndexerInfraApplication;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.web.contract.CustomJsonMapping;
import org.egov.infra.indexer.web.contract.FieldMapping;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.UriMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;


@Service
public class IndexerService {

	public static final Logger logger = LoggerFactory.getLogger(IndexerService.class);

		
	@Autowired
	private BulkIndexer bulkIndexer;
	
	@Autowired
	private IndexerApplicationRunnerImpl runner;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;
	
	public void elasticIndexer(String topic, String kafkaJson){
		Map<String, Mapping> mappingsMap = runner.getMappingMaps();
		logger.info("MappingsMap: "+mappingsMap);
		if(null != mappingsMap.get(topic)){
			Mapping mapping = mappingsMap.get(topic);
			logger.info("Mapping to be used: "+mapping);
			try{
				for(Index index: mapping.getIndexes()){
					indexCurrentValue(index, kafkaJson,
							(index.getIsBulk() == null || index.getIsBulk() == false) ? false : true);
				}
			}catch(Exception e){
				logger.error("Exception while indexing, Uncaught at the indexer level: ", e);
			}
		}else{
			logger.error("No mappings found for the service to which the following topic belongs: "+topic);
		}
	}
	
	public void indexCurrentValue(Index index, String kafkaJson, boolean isBulk) {
		StringBuilder url = new StringBuilder();
		url.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/")
		   .append("_bulk");	
        
        logger.info("Index Metadata: "+index);
        logger.info("kafkaJson: "+kafkaJson);
		
	    if (index.getJsonPath() != null) {
				logger.info("Indexing IndexNode JSON to elasticsearch " + kafkaJson);
				String finalJson = buildIndexJsonWithJsonpath(index, kafkaJson, isBulk);
				if(null == finalJson){
					logger.info("Indexing will not be done, please modify the data and retry.");
					logger.info("Advice: Looks like isBulk = true in the config yaml but the record sent on the queue is a json object and not an array of objects. In that case, change either of them.");
				}else{
					if(finalJson.startsWith("{ \"index\""))
						bulkIndexer.indexJsonOntoES(url.toString(), finalJson);
					else{
						indexWithESId(index, finalJson);
					}			
				}
		} else if(!(null == index.getCustomJsonMapping())){
			    logger.info("Building custom json using the mapping: "+index.getCustomJsonMapping());
			    StringBuilder urlForMap = new StringBuilder();
			    urlForMap.append(esHostUrl).append(index.getName()).append("/").append("_mapping").append("/").append(index.getType());	
				String finalJson = buildCustomJsonForBulk(index, kafkaJson, urlForMap.toString(), isBulk);
				if(null == finalJson){
					logger.info("Indexing will not be done, please modify the data and retry.");
					logger.info("Advice: Looks like isBulk = true in the config yaml but the record sent on the queue is a json object and not an array of objects. In that case, change either of them.");
				}else{
					if(finalJson.startsWith("{ \"index\""))
						bulkIndexer.indexJsonOntoES(url.toString(), finalJson);
					else{
						indexWithESId(index, finalJson);
					}		
				}		
		}else {
				logger.info("Indexing entire request JSON to elasticsearch" + kafkaJson);
				String finalJson = buildIndexJsonWithoutJsonpath(index, kafkaJson, isBulk);
				if(null == finalJson){
					logger.info("Indexing will not be done, please modify the data and retry.");
				    logger.info("Advice: Looks like isBulk = true in the config yaml but the record sent on the queue is a json object and not an array of objects. In that case, change either of them.");

				}else{
					if(finalJson.startsWith("{ \"index\""))
						bulkIndexer.indexJsonOntoES(url.toString(), finalJson);
					else{
						indexWithESId(index, finalJson);
					}
				}
		}
	}
	
	public void indexWithESId(Index index, String finalJson){
		logger.info("Non bulk indexing...");
		StringBuilder urlForNonBulk = new StringBuilder();
		urlForNonBulk.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/").append("_index");
		bulkIndexer.indexJsonOntoES(urlForNonBulk.toString(), finalJson);
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
		logger.info("string for jsonArray: "+jsonArray.toString());
		
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
		logger.info("string constructed from JSONObject: "+jsonArray.toString());
		return jsonArray.toString();		
	}
	
	public String buildIndexJsonWithJsonpath(Index index, String kafkaJson, boolean isBulk){
        StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        String jsonArray = null;
        ObjectMapper mapper = new ObjectMapper();
        final String format = "{ \"index\" : {\"_id\" : \"%s\" } }%n ";
        try {
        	if(isBulk){
        		//Validating if the request is a valid json array.
				jsonArray = pullArrayOutOfString(kafkaJson);
				if(!(jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
					logger.info("Invalid request for a json array!");
					return null;
		        }
            }else{
            	jsonArray = "[" + kafkaJson + "]";
            	logger.info("constructed json array: "+jsonArray);
            }
			JSONArray kafkaJsonArray = new JSONArray(jsonArray);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				logger.info("Object - "+(i+1)+" : "+kafkaJsonArray.get(i));
				Object indexJsonObj = JsonPath.read(buildString(kafkaJsonArray.get(i)), index.getJsonPath());
				String indexJson = mapper.writeValueAsString(indexJsonObj);
				logger.info("Index json: "+indexJson);
				String stringifiedObject = buildString(kafkaJsonArray.get(i));
				if(null != JsonPath.read(stringifiedObject, index.getId())){
					logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(stringifiedObject, index.getId()));
		            final String actionMetaData = String.format(format, "" + JsonPath.read(stringifiedObject, index.getId()));
		            jsonTobeIndexed.append(actionMetaData)
     			                   .append(indexJson)
		            			   .append("\n");
				}else{
					logger.info("Index id not provided for the document, Allowing ES to generate the id.");
					jsonTobeIndexed.append(indexJson);
				}
			}
			result = jsonTobeIndexed.toString();
	    }catch(JSONException e){
	    	logger.error("Error while parsing the JSONArray", e);
	    }catch(Exception e){
	    	logger.error("Error while building jsonstring for indexing", e);
	    }
		logger.info("Json being indexed: "+result.toString());

		return result;
  }
	
	public String buildIndexJsonWithoutJsonpath(Index index, String kafkaJson, boolean isBulk){
        StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        String jsonArray = null;
        final String format = "{ \"index\" : {\"_id\" : \"%s\" } }%n ";
        try {
        	if(isBulk){
        		//Validating if the request is a valid json array.
					jsonArray = pullArrayOutOfString(kafkaJson);
					if(!(jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
						logger.info("Invalid request for a json array!");
						return null;
					}
            }else{
            	jsonArray = "[" + kafkaJson + "]";
            	logger.info("constructed json array: "+jsonArray);

            }
			JSONArray kafkaJsonArray = new JSONArray(jsonArray);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				String stringifiedObject = buildString(kafkaJsonArray.get(i));
				if(null != JsonPath.read(stringifiedObject, index.getId())){
					logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(stringifiedObject, index.getId()));
		            final String actionMetaData = String.format(format, "" + JsonPath.read(stringifiedObject, index.getId()));
		            jsonTobeIndexed.append(actionMetaData)
     			                   .append(stringifiedObject)
		            			   .append("\n");
				}else{
					logger.info("Index id not provided for the document, Allowing ES to generate the id.");
					jsonTobeIndexed.append(stringifiedObject);
				}
			}
			result = jsonTobeIndexed.toString();
	    }catch(JSONException e){
	    	logger.error("Error while parsing the JSONArray", e);
	    }catch(Exception e){
	    	logger.error("Error while building jsonstring for indexing", e);
	    }
		logger.info("Json being indexed: "+result.toString());

		return result;
  }
	
	public String buildCustomJsonForBulk(Index index, String kafkaJson, String urlForMap, boolean isBulk){
        StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        String jsonArray = null;
        final String format = "{ \"index\" : {\"_id\" : \"%s\" } }%n ";
        try {
        	if(isBulk){
        		//Validating if the request is a valid json array.
					jsonArray = pullArrayOutOfString(kafkaJson);
					if(!(jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
						logger.info("Invalid request for a json array!");
						return null;
					}
            }else{
            	jsonArray = "[" + kafkaJson + "]";
            	logger.info("constructed json array: "+jsonArray);
            }
			JSONArray kafkaJsonArray = new JSONArray(jsonArray);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				String stringifiedObject = buildString(kafkaJsonArray.get(i));
				String customIndexJson = buildCustomJsonForIndex(index.getCustomJsonMapping(), stringifiedObject, urlForMap);
				if(null != JsonPath.read(stringifiedObject, index.getId())){
					logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(stringifiedObject, index.getId()));
		            final String actionMetaData = String.format(format, "" + JsonPath.read(stringifiedObject, index.getId()));
		            jsonTobeIndexed.append(actionMetaData)
     			                   .append(customIndexJson)
		            			   .append("\n");
				}else{
					logger.info("Index id not provided for the document, Allowing ES to generate the id.");
					jsonTobeIndexed.append(customIndexJson);
				}
			}
			result = jsonTobeIndexed.toString();
	    }catch(JSONException e){
	    	logger.error("Error while parsing the JSONArray", e);
	    }catch(Exception e){
	    	logger.error("Error while building jsonstring for indexing", e);
	    }
		logger.info("Json being indexed: "+result.toString());

		return result;
  }
	
	public String buildCustomJsonForIndex(CustomJsonMapping customJsonMappings, String kafkaJson, String urlForMap){
		Object indexMap = null;
		String customJson = null;
		ObjectMapper mapper = new ObjectMapper();
		if(null != customJsonMappings.getIndexMapping()){
			indexMap = customJsonMappings.getIndexMapping();
		}else{
			logger.info("Index mapping not provided, Fetching it from ES.....");
			indexMap = bulkIndexer.getIndexMappingfromES(urlForMap);
		}
		logger.info("indexMapping: "+indexMap);
    	DocumentContext documentContext = JsonPath.parse(indexMap);
    	
    	if(null != customJsonMappings.getFieldMapping() || !customJsonMappings.getFieldMapping().isEmpty()){
			for(FieldMapping fieldMapping: customJsonMappings.getFieldMapping()){
				String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
				StringBuilder expression = new StringBuilder();
				for(int i = 0; i < (expressionArray.length - 1) ; i++ ){
					expression.append(expressionArray[i]);
					if(i != expressionArray.length - 2)
						expression.append(".");
				}
				documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1],
						JsonPath.read(kafkaJson, fieldMapping.getInjsonpath()));			
			}
    	}else{
    		logger.info("field mapping list is empty");
    	}
		
		if(null != customJsonMappings.getUriMapping() || !customJsonMappings.getUriMapping().isEmpty()){
			for(UriMapping uriMapping: customJsonMappings.getUriMapping()){
				Object response = null;
				try{
					response = restTemplate.postForObject(uriMapping.getPath(), uriMapping.getRequest(), Map.class);
				}catch(Exception e){
					logger.error("Exception while trying to hit: "+uriMapping.getPath(), e);
					continue;
				}
				if(null == response){
					logger.info("Service returned null for the uri: "+uriMapping.getPath());
					continue;
				}
				logger.info("Response: "+response+" from the URI: "+uriMapping.getPath());
				for(FieldMapping fieldMapping: uriMapping.getUriResponseMapping()){
					String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
					StringBuilder expression = new StringBuilder();
					for(int i = 0; i < (expressionArray.length - 1) ; i++ ){
						expression.append(expressionArray[i]);
						if(i != expressionArray.length - 2)
							expression.append(".");
					}
					try{
						documentContext.put(expression.toString(), expressionArray[expressionArray.length - 1],
								JsonPath.read(mapper.writeValueAsString(response), fieldMapping.getInjsonpath()));
					}catch(Exception e){
						logger.error("Value: "+fieldMapping.getInjsonpath()+" is not found in the uri: "+uriMapping.getPath()+" response", e);
					}
				}
					
			}
		}else{
    		logger.info("uri mapping list is empty");
		}
		customJson = documentContext.jsonString(); 
		logger.info("Json to be indexed: "+customJson);
		return customJson.toString(); //jsonString has to be converted to string
	}
}
