package org.egov.infra.indexer.bulkindexer;

import java.util.ArrayList;
import java.util.Map;

import org.egov.infra.indexer.web.contract.Mapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class BulkIndexer {
	
	public static final Logger logger = LoggerFactory.getLogger(BulkIndexer.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${egov.services.infra.indexer.host}")
	private String esHostUrl;
	
	@Value("${egov.services.infra.indexer.name}")
	private String esIndexName;

public void indexCurrentValue(Mapping mapping, String kafkaJson, boolean isBulk) {

		StringBuilder url = new StringBuilder();
        StringBuilder jsonTobeIndexed = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        
        logger.info("Mappings pulled out of the yaml: "+mapping);
        logger.info("kafkaJson: "+kafkaJson);
        
	/*	logger.info("insert or update on service request id for index id = " +mapping.getIndexID()+ " and json path json = "
				+ JsonPath.read(kafkaJson, mapping.getIndexID())); */

		if(isBulk){
			logger.info("Triggering the bulk indexing flow......");
	        final String format = "{ \"index\" : {\"_id\" : \"%s\" } }%n ";
			url.append(esHostUrl)
			   .append("/")
			   .append(mapping.getIndexName())
			   .append("/")
			   .append(mapping.getIndexType())
			   .append("/")
			   .append("_bulk");
			
			logger.info("index id value is null so going to normal url path " + url);
			
			if (mapping.getJsonPath() != null) {
				logger.info("Indexing IndexNode JSON to elasticsearch " + jsonTobeIndexed);
				try {
					String jsonArray = pullArrayOutOfString(kafkaJson);
					if(!(jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
						logger.info("Invalid request for a json array!");
						return;
					}
					JSONArray kafkaJsonArray = new JSONArray(jsonArray);
					JSONArray indexingJsonArray = new JSONArray();
					for(int i = 0; i < kafkaJsonArray.length() ; i++){
						Object obj = kafkaJsonArray.get(i);
						String indexJsonObj = JsonPath.read(buildString(kafkaJsonArray.get(i)), mapping.getJsonPath());
						if(null != JsonPath.read(obj, mapping.getIndexID())){
							logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(buildString(kafkaJsonArray.get(i)), mapping.getIndexID()));
				            final String actionMetaData = String.format(format, "" + JsonPath.read(buildString(kafkaJsonArray.get(i)), mapping.getIndexID()));
				            jsonTobeIndexed.append(actionMetaData)
	         			                   .append(indexJsonObj);
						}
						jsonTobeIndexed.append(indexJsonObj);
			            indexingJsonArray.put(i, jsonTobeIndexed);	
					}
					logger.info("Json being indexed to elasticsearch: "+indexingJsonArray.toString());
					final HttpHeaders headers = new HttpHeaders();
			        headers.setContentType(MediaType.APPLICATION_JSON);
			        final HttpEntity<String> entity = new HttpEntity<>(indexingJsonArray.toString(), headers);
					restTemplate.postForObject(url.toString(), entity, Map.class);
				} catch (final JSONException e) {
					logger.error("Exception while trying parse kafkaJson to jsonArray: ",e);
				} catch (final Exception e) {
					logger.error("Indexing on elasticsearch failed: ",e);
				}	
			} else {
				// kafkaJson
				logger.info("Indexing entire request JSON to elasticsearch" + kafkaJson);
				try {
					String jsonArray = pullArrayOutOfString(kafkaJson);
					if(!(jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
						logger.info("Invalid request for a json array!");
						return;
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
					//String finalJSON = mapper.writeValueAsString(indexingJsonArray);
					logger.info("Json being indexed: "+jsonTobeIndexed.toString());
					final HttpHeaders headers = new HttpHeaders();
			        headers.setContentType(MediaType.APPLICATION_JSON);
			        final HttpEntity<String> entity = new HttpEntity<>(jsonTobeIndexed.toString(), headers);
					restTemplate.postForObject(url.toString(), entity, Map.class);
				} catch (final JSONException e) {
					logger.error("Exception while trying parse kafkaJson to jsonArray: ",e);
				} catch (final Exception e) {
					logger.error("Indexing on elasticsearch failed: ",e);
				}
			}
		}
		
		else{
			if (mapping.getIndexID() != null) { 
				if (JsonPath.read(kafkaJson, mapping.getIndexID()) != null) {
					url.append(esHostUrl)
					   .append("/")
					   .append(mapping.getIndexName())
					   .append("/")
					   .append(mapping.getIndexType())
					   .append("/")
					   .append(JsonPath.read(kafkaJson, mapping.getIndexID()).toString());
				} else {
					url.append(esHostUrl)
					   .append("/")
					   .append(mapping.getIndexName())
					   .append("/")
					   .append(mapping.getIndexType());
					
					logger.info("index id value is null so going to normal url path " + url);
				}
			}else{
				url.append(esHostUrl)
				   .append("/")
				   .append(mapping.getIndexName())
				   .append("/")
				   .append(mapping.getIndexType());			
				
				logger.info("index id json path is null in yml so going to normal url path " + url);
			}
			
			if (mapping.getJsonPath() != null) {
				String indexJson = JsonPath.read(kafkaJson, mapping.getJsonPath());
				logger.info("Indexing IndexNode JSON to elasticsearch" + indexJson);
				try {
					final HttpHeaders headers = new HttpHeaders();
			        headers.setContentType(MediaType.APPLICATION_JSON);
			        final HttpEntity<String> entity = new HttpEntity<>(indexJson.toString(), headers);
					restTemplate.postForObject(url.toString(), entity, Map.class);
				} catch (final Exception e) {
					logger.error("Indexing on elasticsearch failed: ",e);
				}
			} else {
				// kafkaJson
				try {
					final HttpHeaders headers = new HttpHeaders();
			        headers.setContentType(MediaType.APPLICATION_JSON);
			        final HttpEntity<String> entity = new HttpEntity<>(kafkaJson.toString(), headers);
					logger.info("Indexing entire request JSON to elasticsearch" + kafkaJson);

					restTemplate.postForObject(url.toString(), kafkaJson, Map.class);
				} catch (final Exception e) {
					logger.error("Indexing on elasticsearch failed: ",e);
				}

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

}
