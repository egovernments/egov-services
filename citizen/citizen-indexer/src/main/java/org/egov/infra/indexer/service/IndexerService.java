package org.egov.infra.indexer.service;


import java.util.Map;

import org.egov.infra.indexer.IndexerInfraApplication;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.infra.indexer.web.contract.Mapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;


@Service
public class IndexerService {

	public static final Logger logger = LoggerFactory.getLogger(IndexerService.class);

		
	@Autowired
	private BulkIndexer bulkIndexer;
	
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;
	
	public void elasticIndexer(String topic, String kafkaJson){
		Map<String, Mapping> mappingsMap = IndexerInfraApplication.getMappingMaps();
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
		ObjectMapper mapper = new ObjectMapper();
		url.append(esHostUrl)
		   .append("/")
		   .append(index.getName())
		   .append("/")
		   .append(index.getType());
        
        logger.info("Index Metadata: "+index);
        logger.info("kafkaJson: "+kafkaJson);

		if(isBulk){
			logger.info("Triggering the bulk indexing flow......");
			url.append("/")
			   .append("_bulk");
			if (index.getJsonPath() != null) {
				logger.info("Indexing IndexNode JSON to elasticsearch " + kafkaJson);
				bulkIndexer.indexJsonOntoES(url.toString(), buildIndexJsonWithJsonpath(index, kafkaJson));
			} else {
				logger.info("Indexing entire request JSON to elasticsearch" + kafkaJson);
				bulkIndexer.indexJsonOntoES(url.toString(), buildIndexJsonWithoutJsonpath(index, kafkaJson));
			}
		}
		else{
			if (index.getId() != null) { 
				if (JsonPath.read(kafkaJson, index.getId()) != null) {
					url.append("/")
					   .append(JsonPath.read(kafkaJson, index.getId()).toString());
				}else				
					logger.info("index id value is null so going to normal url path " + url);
			}else				
				logger.info("index id json path is null in yml so going to normal url path " + url);
			if (index.getJsonPath() != null) {
				try{
					logger.info("JSON Node: "+index.getJsonPath());
					Object indexJsonObj = JsonPath.read(kafkaJson, index.getJsonPath());
					String indexJson = mapper.writeValueAsString(indexJsonObj);
					logger.info("Index json: "+indexJson);
					bulkIndexer.indexJsonOntoES(url.toString(), indexJson);
				}catch(Exception e){
					logger.error("Exception while trying to pull json to be indexed from the request based on jsonpath", e);
				}
			} else {
				logger.info("Indexing entire request JSON to elasticsearch" + kafkaJson);
				bulkIndexer.indexJsonOntoES(url.toString(), kafkaJson);
			}
				
		}

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
	
	public String buildIndexJsonWithJsonpath(Index index, String kafkaJson){
        StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        final String format = "{ \"index\" : {\"_id\" : \"%s\" } }%n ";
        try {
        	if(!(kafkaJson.startsWith("[") && kafkaJson.endsWith("]"))){
				String jsonArray = pullArrayOutOfString(kafkaJson);
				if(!(jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
					logger.info("Invalid request for a json array!");
					return null;
				}
	        }
			JSONArray kafkaJsonArray = new JSONArray(kafkaJson);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				Object indexJsonObj = JsonPath.read(buildString(kafkaJsonArray.get(i)), index.getJsonPath());
				String indexJson = mapper.writeValueAsString(indexJsonObj);
				logger.info("Index json: "+indexJson);
				String stringifiedObject = buildString(kafkaJsonArray.get(i));
				if(null != JsonPath.read(stringifiedObject, index.getId())){
					logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(stringifiedObject, index.getId()));
		            final String actionMetaData = String.format(format, "" + JsonPath.read(stringifiedObject, index.getId()));
		            jsonTobeIndexed.append(actionMetaData)
     			                   .append(mapper.writeValueAsString(indexJson))
		            			   .append("\n");
				}else{
					jsonTobeIndexed.append(mapper.writeValueAsString(indexJson))
        			   .append("\n");
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
	
	public String buildIndexJsonWithoutJsonpath(Index index, String kafkaJson){
        StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        ObjectMapper mapper = new ObjectMapper();
        final String format = "{ \"index\" : {\"_id\" : \"%s\" } }%n ";
        try {
			String jsonArray = pullArrayOutOfString(kafkaJson);
			if(!(jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
				logger.info("Invalid request for a json array!");
				return null;
			}
			JSONArray kafkaJsonArray = new JSONArray(jsonArray);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				String stringifiedObject = buildString(kafkaJsonArray.get(i));
				if(null != JsonPath.read(stringifiedObject, index.getId())){
					logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(stringifiedObject, index.getId()));
		            final String actionMetaData = String.format(format, "" + JsonPath.read(stringifiedObject, index.getId()));
		            jsonTobeIndexed.append(actionMetaData)
     			                   .append(mapper.writeValueAsString(stringifiedObject))
		            			   .append("\n");
				}else{
					jsonTobeIndexed.append(mapper.writeValueAsString(stringifiedObject))
        			   .append("\n");
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
	
	/*public String modifyJson(Mapping mapping, String json){
		String result = null;
		if(null != mapping.getOmitPaths() || !mapping.getOmitPaths().isEmpty()){
			for(String path: mapping.getOmitPaths()){
				
			}
		}
		return result;
	} */
}