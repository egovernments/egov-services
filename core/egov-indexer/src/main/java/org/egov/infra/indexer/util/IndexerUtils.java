package org.egov.infra.indexer.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.consumer.KafkaConsumerConfig;
import org.egov.infra.indexer.models.AuditDetails;
import org.egov.infra.indexer.web.contract.APIDetails;
import org.egov.infra.indexer.web.contract.FilterMapping;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.infra.indexer.web.contract.ReindexRequest;
import org.egov.infra.indexer.web.contract.UriMapping;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexerUtils {
		
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
	
	@Value("${egov.mdms.host}")
	private String mdmsHost;

	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;
	
	@Value("${egov.service.host}")
	private String serviceHost;
	
    private final ScheduledExecutorService scheduler =
    	       Executors.newScheduledThreadPool(1);
	
	
	public void orchestrateListenerOnESHealth(){
		kafkaConsumerConfig.pauseContainer();
		log.info("Polling ES....");
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
        				log.error("ES is DOWN..");
        			}
        			if(response != null){
        				log.info("ES is UP!");
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
						if(queryParamExpression[1].trim().contains("$.")) {
							queryParam = JsonPath.read(kafkaJson, queryParamExpression[1].trim());
						}else {
							queryParam = queryParamExpression[1].trim();
						}
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
	
	/**
	 * A common method that builds MDMS request for searching master data.
	 * 
	 * @param uri
	 * @param tenantId
	 * @param module
	 * @param master
	 * @param filter
	 * @param requestInfo
	 * @return
	 */
	public MdmsCriteriaReq prepareMDMSSearchReq(StringBuilder uri, RequestInfo requestInfo, String kafkaJson, UriMapping mdmsMppings) {

		uri.append(mdmsHost).append(mdmsEndpoint);
		String filter = buildFilter(mdmsMppings.getFilter(), mdmsMppings, kafkaJson);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder().name(mdmsMppings.getMasterName()).filter(filter).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder().moduleName(mdmsMppings.getModuleName()).masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(mdmsMppings.getTenantId()).moduleDetails(moduleDetails).build();
		return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}
	
	public String buildFilter(String filter, UriMapping mdmsMppings, String kafkaJson) {
		String modifiedFilter = mdmsMppings.getFilter();
		log.debug("buildfilter, kafkaJson: "+kafkaJson);
		for(FilterMapping mdmsMapping: mdmsMppings.getFilterMapping()) {
			Object value = JsonPath.read(kafkaJson, mdmsMapping.getValueJsonpath());
			if(null == value) {
				log.info("MDMS filter, No value found at: "+ mdmsMapping.getValueJsonpath());
				continue;
			}else if(value.toString().startsWith("[") && value.toString().endsWith("]")) {
				value = value.toString().substring(1, value.toString().length() - 1);
			}
			modifiedFilter = modifiedFilter.replace(mdmsMapping.getVariable(), "'"+value.toString()+"'");
		}
		return modifiedFilter;
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
			log.error("No id found at the given jsonpath: ", e);
			return null;
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
		    		if(JsonPath.read(kafkaJson.toString(), index.getJsonPath()) instanceof net.minidev.json.JSONArray){
		    			String inputArray = mapper.writeValueAsString(JsonPath.read(kafkaJson, index.getJsonPath()));
		    			kafkaJsonArray = new JSONArray(inputArray);
		    		}
	    		}else if((jsonArray.startsWith("[") && jsonArray.endsWith("]"))){
	    			kafkaJsonArray = new JSONArray(jsonArray);
		        }else{
					log.info("Invalid request for a json array!");
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
        	log.error("Exception while constructing json array for bulk index: ", e);
        	throw e;
        }
    	return addTimeStamp(index, kafkaJsonArray);
	}
	
	public void validateAndIndex(String finalJson, String url, Index index) throws Exception{
		if(!StringUtils.isEmpty(finalJson)){
			doIndexing(finalJson, url.toString(), index);
		}else{
			log.info("Indexing will not be done, please modify the data and retry.");
		}
	}
	
	public void doIndexing(String finalJson, String url, Index index) throws Exception{
		if(finalJson.startsWith("{ \"index\""))
			bulkIndexer.indexJsonOntoES(url.toString(), finalJson, index);
		else{
			indexWithESId(index, finalJson);
		}
	}
	
	public void indexWithESId(Index index, String finalJson) throws Exception{
		StringBuilder urlForNonBulk = new StringBuilder();
		urlForNonBulk.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/").append("_index");
		bulkIndexer.indexJsonOntoES(urlForNonBulk.toString(), finalJson, index);
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
	
	public String getESSearchURL(ReindexRequest reindexRequest) {
		StringBuilder uri = new StringBuilder();
		uri.append(esHostUrl).append(reindexRequest.getIndex()).append("/"+reindexRequest.getType()).append("/_search");
		return uri.toString();
	}
	
	public String getESSettingsURL(ReindexRequest reindexRequest) {
		StringBuilder uri = new StringBuilder();
		uri.append(esHostUrl).append(reindexRequest.getIndex()).append("/_settings");
		return uri.toString();
	}
	
	public Object getESSearchBody(Integer from, Integer size) {
		Map<String, Integer> searchBody = new HashMap<>();
		searchBody.put("from", from); searchBody.put("size", size); 
		return searchBody;
	}
	
	public Object getESSettingsBody(Integer totalRecords) {
		Map<String, Map<String, Long>> settingsBody = new HashMap<>();
		Map<String, Long> innerBody = new HashMap<>();
		Long window = Long.valueOf(totalRecords.toString()) + 50000L;
		innerBody.put("max_result_window", window);
		settingsBody.put("index", innerBody);
		return settingsBody;
	}
	
	
	public String setDynamicMapping(Index index) {
		String requestTwo = "{ \"settings\": {\"index.mapping.ignore_malformed\": true}}";
		StringBuilder uriForUpdateMapping = new StringBuilder();
		uriForUpdateMapping.append(esHostUrl).append(index.getName()).append("/_settings");
		try {
			restTemplate.put(uriForUpdateMapping.toString(), requestTwo, Map.class);
			return "OK";
		}catch(Exception e) {
			log.error("Updating mapping failed for index: "+index.getName()+" and type: "+index.getType());
			log.error("Trace: ", e);
			return null;
		}
		
	}
	
	public JSONArray addTimeStamp(Index index, JSONArray kafkaJsonArray) {
		JSONArray tranformedArray = new JSONArray();
		ObjectMapper mapper = getObjectMapper();
		for(int i = 0; i < kafkaJsonArray.length();  i++) {
			try {
				if(null != kafkaJsonArray.get(i)) {
					DocumentContext context = null;
					try {
						String epochValue = mapper.writeValueAsString(JsonPath.read(kafkaJsonArray.get(i).toString(), index.getTimeStampField()));
						Date date = new Date(Long.valueOf(epochValue));
						SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US); 
						formatter.setTimeZone(TimeZone.getTimeZone("UTC"));	
						context = JsonPath.parse(kafkaJsonArray.get(i).toString());
						context.put("$","@timestamp", formatter.format(date));
						tranformedArray.put(context.jsonString());
					}catch(Exception e) {
						log.error("Exception while adding timestamp: ", e);
						log.info("kafkaJsonArray.get(i): "+kafkaJsonArray.get(i));
						continue;
					}
				}else {
					log.info("null json in kafkaJsonArray, index: "+i);
					continue;
				}
			}catch(Exception e) {
				log.error("Exception while adding timestamp: ", e);
				continue;
			}
		}
		if(tranformedArray.length() != kafkaJsonArray.length()) {
			return kafkaJsonArray;
		}
		return tranformedArray;
	}
	
	/**
	 * Returns mapper with all the appropriate properties reqd in our
	 * functionalities.
	 * 
	 * @return ObjectMapper
	 */
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		return mapper;
	}
	
	/**
	 * Util method to return Auditdetails for create and update processes
	 * 
	 * @param by
	 * @param isCreate
	 * @return
	 */
	public AuditDetails getAuditDetails(String by, Boolean isCreate) {
		Long dt = new Date().getTime();
		if (isCreate)
			return AuditDetails.builder().createdBy(by).createdTime(dt).lastModifiedBy(by).lastModifiedTime(dt).build();
		else
			return AuditDetails.builder().lastModifiedBy(by).lastModifiedTime(dt).build();
	}
	
	public String fetchEstimatedTime(Integer totalRecords) {
		StringBuilder estimatedTime = new StringBuilder();
		Double actualTime = totalRecords * 0.000250; //on an avg one record gets reindexed in 0.000125s.
		if(actualTime > 60) {
			Double mins = actualTime / 60 ;
			Double seconds = actualTime % 60;
			estimatedTime.append(mins).append("mins ").append(seconds).append("secs");
		}else if(actualTime < 1) {
			estimatedTime.append("less than a second");
		}else {
			estimatedTime.append(actualTime).append("secs");
		}
		return estimatedTime.toString();
	}
	
	public String buildPagedUriForLegacyIndex(APIDetails apiDetails, Integer offset, Integer size) {
		StringBuilder url = new StringBuilder();
		if(apiDetails.getUri().contains("http://") || apiDetails.getUri().contains("https://"))
			url.append(apiDetails.getUri());
		else
			url.append(serviceHost).append(apiDetails.getUri());
			
		String offsetKey = null; String sizeKey = null;
		offsetKey = null != apiDetails.getPaginationDetails().getOffsetKey() ? apiDetails.getPaginationDetails().getOffsetKey() : "offset";
		sizeKey = null != apiDetails.getPaginationDetails().getSizeKey() ? apiDetails.getPaginationDetails().getSizeKey() : "size";
		url.append("?tenantId=").append(apiDetails.getTenantIdForOpenSearch()).append("&"+offsetKey+"="+offset).append("&"+sizeKey+"="+size);
		
		return url.toString();
	}
	
	public String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}
	

}