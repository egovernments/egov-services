package org.egov.infra.indexer.bulkindexer;

import java.util.Map;

import org.egov.infra.indexer.web.contract.Mapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        
        logger.info("Mappings pulled out of the yaml: "+mapping);
        logger.info("kafkaJson: "+kafkaJson);
        
	/*	logger.info("insert or update on service request id for index id = " +mapping.getIndexID()+ " and json path json = "
				+ JsonPath.read(kafkaJson, mapping.getIndexID())); */

		if(isBulk){
			logger.info("Triggering the bulk indexing flow......");
	        final String format = "{ \"index\" : { \"_id\" : \"%s\" } }%n";
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
					if(!(jsonArray.substring(0, 1).equals("[") && 
							jsonArray.charAt(jsonArray.length() - 1) == ']')){
						logger.info("Invalid request for a json array!");
						return;
					}
					JSONArray kafkaJsonArray = new JSONArray(jsonArray);
					JSONArray indexingJsonArray = new JSONArray();
					for(int i = 0; i < kafkaJsonArray.length() ; i++){
						Object obj = kafkaJsonArray.get(i);
						String indexJsonObj = JsonPath.read(obj, mapping.getJsonPath());
						if(null != JsonPath.read(obj, mapping.getIndexID())){
							logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(obj, mapping.getIndexID()));
				            final String actionMetaData = String.format(format, "" + JsonPath.read(obj, mapping.getIndexID()));
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
					if(!(jsonArray.substring(0, 1).equals("[") && 
							jsonArray.charAt(jsonArray.length() - 1) == ']')){
						logger.info("Invalid request for a json array!");
						return;
					}
					JSONArray kafkaJsonArray = new JSONArray(jsonArray);
					JSONArray indexingJsonArray = new JSONArray();
					for(int i = 0; i < kafkaJsonArray.length() ; i++){
						GsonBuilder builder = new GsonBuilder();
						Gson gson = builder.create();
						String obj = gson.toJson(kafkaJsonArray.get(i)).toString();
						logger.info("Obj: "+obj);
						obj = obj.replace("{\"map\":", "").replace("{\"myArrayList\":", "");
						if(null != JsonPath.read(obj, mapping.getIndexID())){
							logger.info("Inserting id to the json being indexed, id = " + JsonPath.read(kafkaJsonArray.get(i), mapping.getIndexID()));
				            final String actionMetaData = String.format(format, "" + JsonPath.read(kafkaJsonArray.get(i), mapping.getIndexID()));
				            jsonTobeIndexed.append(actionMetaData)
	         			                   .append(kafkaJsonArray.get(i));
						}
						jsonTobeIndexed.append(kafkaJsonArray.get(i));
			            indexingJsonArray.put(i, jsonTobeIndexed);	
					}
					final HttpHeaders headers = new HttpHeaders();
			        headers.setContentType(MediaType.APPLICATION_JSON);
			        final HttpEntity<String> entity = new HttpEntity<>(indexingJsonArray.toString(), headers);
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

}
