package org.egov.infra.indexer.service;


import org.egov.infra.indexer.IndexerInfraApplication;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
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
	
	@Value("${egov.services.infra.indexer.host}")
	private String esHostUrl;
	
	public void elasticIndexer(String topic, String kafkaJson){
		logger.info("ServiceMap: "+IndexerInfraApplication.getServiceMaps());
		for(Mapping mapping : IndexerInfraApplication.getServiceMaps().get("CitizenService").getServiceMaps().getMappings()){
			if(mapping.getFromTopicSave().equals(topic) || mapping.getFromTopicUpdate().equals(topic)){
				logger.info("save topic = " + mapping.getFromTopicSave());
				logger.info("Update topic = " + mapping.getFromTopicUpdate());
				logger.info("Received topic = " + topic);	
				try{
					indexCurrentValue(mapping, kafkaJson,
							(mapping.getIsBulk() == null || mapping.getIsBulk() == false) ? false : true);
				}catch(Exception e){
					logger.error("Exception while indexing, Uncaught at the indexer level: ", e);
				}
			}
		}
		
	}
	
	public void indexCurrentValue(Mapping mapping, String kafkaJson, boolean isBulk) {
		StringBuilder url = new StringBuilder();
		url.append(esHostUrl)
		   .append("/")
		   .append(mapping.getIndexName())
		   .append("/")
		   .append(mapping.getIndexType());
        
        logger.info("Mappings pulled out of the yaml: "+mapping);
        logger.info("kafkaJson: "+kafkaJson);

		if(isBulk){
			logger.info("Triggering the bulk indexing flow......");
			url.append("/")
			   .append("_bulk");
			if (mapping.getJsonPath() != null) {
				logger.info("Indexing IndexNode JSON to elasticsearch " + kafkaJson);
				bulkIndexer.indexJsonOntoES(url.toString(), buildIndexJsonWithJsonpath(mapping, kafkaJson));
			} else {
				logger.info("Indexing entire request JSON to elasticsearch" + kafkaJson);
				bulkIndexer.indexJsonOntoES(url.toString(), buildIndexJsonWithoutJsonpath(mapping, kafkaJson));
			}
		}
		else{
			if (mapping.getIndexID() != null) { 
				if (JsonPath.read(kafkaJson, mapping.getIndexID()) != null) {
					url.append("/")
					   .append(JsonPath.read(kafkaJson, mapping.getIndexID()).toString());
				}else				
					logger.info("index id value is null so going to normal url path " + url);
			}else				
				logger.info("index id json path is null in yml so going to normal url path " + url);
			if (mapping.getJsonPath() != null) {
				try{
					String indexJson = JsonPath.read(kafkaJson, mapping.getJsonPath());
					bulkIndexer.indexJsonOntoES(url.toString(), indexJson);
				}catch(Exception e){
					logger.error("Exception while trying to pull json to be indexed from the request based on jsonpath", e);
				}
			} else {
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
	
	public String buildIndexJsonWithJsonpath(Mapping mapping, String kafkaJson){
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
				String indexJsonObj = JsonPath.read(buildString(kafkaJsonArray.get(i)), mapping.getJsonPath());
				String stringifiedObject = buildString(kafkaJsonArray.get(i));
				if(null != JsonPath.read(stringifiedObject, mapping.getIndexID())){
					logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(stringifiedObject, mapping.getIndexID()));
		            final String actionMetaData = String.format(format, "" + JsonPath.read(stringifiedObject, mapping.getIndexID()));
		            jsonTobeIndexed.append(actionMetaData)
     			                   .append(mapper.writeValueAsString(indexJsonObj))
		            			   .append("\n");
				}else{
					jsonTobeIndexed.append(mapper.writeValueAsString(indexJsonObj))
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
	
	public String buildIndexJsonWithoutJsonpath(Mapping mapping, String kafkaJson){
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
				if(null != JsonPath.read(stringifiedObject, mapping.getIndexID())){
					logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(stringifiedObject, mapping.getIndexID()));
		            final String actionMetaData = String.format(format, "" + JsonPath.read(stringifiedObject, mapping.getIndexID()));
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
}