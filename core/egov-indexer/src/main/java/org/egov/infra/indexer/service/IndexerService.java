package org.egov.infra.indexer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.egov.IndexerApplicationRunnerImpl;
import org.egov.common.contract.request.RequestInfo;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.models.IndexJob;
import org.egov.infra.indexer.models.IndexJob.StatusEnum;
import org.egov.infra.indexer.models.IndexJobWrapper;
import org.egov.infra.indexer.testproducer.IndexerProducer;
import org.egov.infra.indexer.util.IndexerConstants;
import org.egov.infra.indexer.util.IndexerUtils;
import org.egov.infra.indexer.util.ResponseInfoFactory;
import org.egov.infra.indexer.web.contract.CustomJsonMapping;
import org.egov.infra.indexer.web.contract.FieldMapping;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.infra.indexer.web.contract.LegacyIndexRequest;
import org.egov.infra.indexer.web.contract.LegacyIndexResponse;
import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.Mapping.ConfigKeyEnum;
import org.egov.infra.indexer.web.contract.ReindexRequest;
import org.egov.infra.indexer.web.contract.ReindexResponse;
import org.egov.infra.indexer.web.contract.UriMapping;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
	
	@Autowired
	private IndexerUtils indexerUtils;
	
	@Autowired
	private ResponseInfoFactory factory;
	
	@Autowired
	private IndexerProducer indexerProducer;
	
	@Value("${egov.core.reindex.topic.name}")
	private String reindexTopic;
	
	@Value("${egov.core.legacyindex.topic.name}")
	private String legacyIndexTopic;
	
	@Value("${egov.indexer.persister.create.topic}")
	private String persisterCreate;
	
	@Value("${egov.indexer.persister.update.topic}")
	private String persisterUpdate;
	
	@Value("${reindex.pagination.size.default}")
	private Integer defaultPageSizeForReindex;
	
	@Value("${legacyindex.pagination.size.default}")
	private Integer defaultPageSizeForLegacyindex;
	
	@Value("${egov.service.host}")
	private String serviceHost;
	
	
	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;
		
	public void elasticIndexer(String topic, String kafkaJson) throws Exception{
		logger.info("elasticIndexer.....");
		logger.debug("kafka Data: "+kafkaJson);
		Map<String, Mapping> mappingsMap = runner.getMappingMaps();
		if(null != mappingsMap.get(topic)){
			Mapping mapping = mappingsMap.get(topic);
			logger.debug("Mapping to be used: "+mapping);
			try{				
				for(Index index: mapping.getIndexes()){
					indexProccessor(index, kafkaJson,
							(index.getIsBulk() == null || !index.getIsBulk()) ? false : true);
				}
			}catch(Exception e){
				logger.error("Exception while indexing, Uncaught at the indexer level: ", e);
			}
		}else{
			logger.error("No mappings found for the service to which the following topic belongs: "+topic);
		}
	}
	
	public void indexProccessor(Index index, String kafkaJson, boolean isBulk) throws Exception {
        Long startTime = null;
        logger.debug("index: "+index.getCustomJsonMapping());
		StringBuilder url = new StringBuilder();
		url.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/")
		   .append("_bulk");	
        startTime = new Date().getTime();
        if(!StringUtils.isEmpty(index.getJsonPath())) {
        	if(null != index.getCustomJsonMapping()) {
			    StringBuilder urlForMap = new StringBuilder();
			    urlForMap.append(esHostUrl).append(index.getName()).append("/").append("_mapping").append("/").append(index.getType());	
			    indexerUtils.validateAndIndex(buildCustomJsonForBulk(index, kafkaJson, urlForMap.toString(), isBulk), url.toString(), index);
        	}else {
				indexerUtils.validateAndIndex(buildIndexJsonWithJsonpath(index, kafkaJson, isBulk), url.toString(), index);
        	}
        }else if(null != index.getCustomJsonMapping()) {
		    StringBuilder urlForMap = new StringBuilder();
		    urlForMap.append(esHostUrl).append(index.getName()).append("/").append("_mapping").append("/").append(index.getType());	
		    indexerUtils.validateAndIndex(buildCustomJsonForBulk(index, kafkaJson, urlForMap.toString(), isBulk), url.toString(), index);
        }else{
				indexerUtils.validateAndIndex(buildIndexJsonWithoutJsonpath(index, kafkaJson, isBulk), url.toString(), index);
		}
		logger.info("Total time taken: "+((new Date().getTime()) - startTime)+"ms");
	}
	
	public String buildIndexJsonWithJsonpath(Index index, String kafkaJson, boolean isBulk) throws Exception{
        StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        JSONArray kafkaJsonArray = null;
        try {
        	kafkaJsonArray = indexerUtils.constructArrayForBulkIndex(kafkaJson, index, isBulk);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				if(null != kafkaJsonArray.get(i)) {
					String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
					if(!StringUtils.isEmpty(index.getId())){
						String id = indexerUtils.buildIndexId(index, stringifiedObject);
						if(StringUtils.isEmpty(id)) {
							continue;
						}else {
				            final String actionMetaData = String.format(IndexerConstants.ES_INDEX_HEADER_FORMAT, "" + id);
				            jsonTobeIndexed.append(actionMetaData).append(stringifiedObject).append("\n");
						}
					}else{
						jsonTobeIndexed.append(stringifiedObject);
					}
				}else {
					logger.info("buildIndexJsonWithJsonpath, null json in kafkajsonarrya, index: "+i);
					continue;
				}
			}
			result = jsonTobeIndexed.toString();
	    }catch(Exception e){
	    	logger.error("Error while building jsonstring for indexing", e);
	    }

		return result;
  }
	
	public String buildCustomJsonForBulk(Index index, String kafkaJson, String urlForMap, boolean isBulk) throws Exception{
		StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        JSONArray kafkaJsonArray = null;
        try {
        	kafkaJsonArray = indexerUtils.constructArrayForBulkIndex(kafkaJson.toString(), index, isBulk);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				if(null != kafkaJsonArray.get(i)) {
					String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
					String customIndexJson = buildCustomJsonForIndex(index.getCustomJsonMapping(), stringifiedObject, urlForMap);
					if(null != index.getId()){
						String id = indexerUtils.buildIndexId(index, stringifiedObject);
						logger.debug("Inserting id to the json being indexed, id = " + id);
			            final String actionMetaData = String.format(IndexerConstants.ES_INDEX_HEADER_FORMAT, "" + id);
			            jsonTobeIndexed.append(actionMetaData).append(customIndexJson).append("\n");
					}else{
						jsonTobeIndexed.append(customIndexJson);
					}
				}else {
					logger.info("buildIndexJsonWithJsonpath, null json in kafkajsonarrya, index: "+i);
					continue;
				}
			}
			result = jsonTobeIndexed.toString();
	    }catch(Exception e){
	    	logger.error("Error while building jsonstring for indexing", e);
	    }

		return result;
  }
	
	public String buildCustomJsonForIndex(CustomJsonMapping customJsonMappings, String kafkaJson, String urlForMap){
		Object indexMap = null;
		ObjectMapper mapper = new ObjectMapper();
		if(null != customJsonMappings.getIndexMapping()){
			indexMap = customJsonMappings.getIndexMapping();
		}else{
			indexMap = bulkIndexer.getIndexMappingfromES(urlForMap);
		}
    	DocumentContext documentContext = JsonPath.parse(indexMap);
    	if(!CollectionUtils.isEmpty(customJsonMappings.getFieldMapping())){
			for(FieldMapping fieldMapping: customJsonMappings.getFieldMapping()){
				String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
				String expression = indexerUtils.getProcessedJsonPath(fieldMapping.getOutJsonPath());
				try {
					documentContext.put(expression, expressionArray[expressionArray.length - 1], JsonPath.read(kafkaJson, fieldMapping.getInjsonpath()));
				}catch(Exception e) {
					continue;
				}
			
			}
    	}
    	/**
    	 * This block denormalizes data from external services
    	 */
		if(!CollectionUtils.isEmpty(customJsonMappings.getExternalUriMapping())){
			for(UriMapping uriMapping: customJsonMappings.getExternalUriMapping()){
				Object response = null;
				String uri = null;
				try{
					uri = indexerUtils.buildUri(uriMapping, kafkaJson);
					response = restTemplate.postForObject(uri, uriMapping.getRequest(), Map.class);
					if(null == response) continue;
				}catch(Exception e){
					logger.error("Exception while trying to hit: "+uri, e);
					continue;
				}
				logger.debug("Response: "+response+" from the URI: "+uriMapping.getPath());
				for(FieldMapping fieldMapping: uriMapping.getUriResponseMapping()){
					String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
					String expression = indexerUtils.getProcessedJsonPath(fieldMapping.getOutJsonPath());
					try{
						Object value = JsonPath.read(mapper.writeValueAsString(response), fieldMapping.getInjsonpath());
						documentContext.put(expression, expressionArray[expressionArray.length - 1], value);					
					}catch(Exception e){
						logger.error("Value: "+fieldMapping.getInjsonpath()+" is not found!");
						//logger.info("Request: "+uriMapping.getRequest());
						logger.info("Response: "+response);
						continue;
					}
				}
					
			}
		}
		
		/**
		 * This block denormalizes data from MDMS.
		 */
		if(!CollectionUtils.isEmpty(customJsonMappings.getMdmsMapping())) {
			for(UriMapping uriMapping: customJsonMappings.getMdmsMapping()){
				Object response = null;
				StringBuilder uri = new StringBuilder();
				try{
					Object request = indexerUtils.prepareMDMSSearchReq(uri, new RequestInfo(), kafkaJson, uriMapping);
					response = restTemplate.postForObject(uri.toString(), request, Map.class);
					if(null == response) continue;
				}catch(Exception e){
					logger.error("Exception while trying to hit: "+uri, e);
					continue;
				}
				logger.debug("Response: "+response+" from the URI: "+uriMapping.getPath());
				for(FieldMapping fieldMapping: uriMapping.getUriResponseMapping()){
					String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
					String expression = indexerUtils.getProcessedJsonPath(fieldMapping.getOutJsonPath());
					try{
						Object value = JsonPath.read(mapper.writeValueAsString(response), fieldMapping.getInjsonpath());
						if(value instanceof List) {
							if(((List) value).size() == 1) {
								value = ((List) value).get(0);
							}
						}
						documentContext.put(expression, expressionArray[expressionArray.length - 1], value);
					}catch(Exception e){
						logger.error("Value: "+fieldMapping.getInjsonpath()+" is not found!");
						//logger.info("Request: "+uriMapping.getRequest());
						logger.info("Response: "+response);						
						continue;
					}
				}
					
			}
		}
		
		return documentContext.jsonString().toString(); //jsonString has to be converted to string
	}
	
	public String buildIndexJsonWithoutJsonpath(Index index, String kafkaJson, boolean isBulk) throws Exception{
		StringBuilder jsonTobeIndexed = new StringBuilder();
        String result = null;
        JSONArray kafkaJsonArray = null;
        try {
        	kafkaJsonArray = indexerUtils.constructArrayForBulkIndex(kafkaJson, index, isBulk);
			for(int i = 0; i < kafkaJsonArray.length() ; i++){
				if(null != kafkaJsonArray.get(i)) {
					String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
					if(null != index.getId()){
						String id = indexerUtils.buildIndexId(index, stringifiedObject);
						logger.debug("Inserting id to the json being indexed, id = " + id);
			            final String actionMetaData = String.format(IndexerConstants.ES_INDEX_HEADER_FORMAT, "" + id);
			            jsonTobeIndexed.append(actionMetaData).append(stringifiedObject).append("\n");
					}else{
						jsonTobeIndexed.append(stringifiedObject);
					}
				}else {
					logger.info("buildIndexJsonWithJsonpath, null json in kafkajsonarrya, index: "+i);
					continue;
				}
			}
			result = jsonTobeIndexed.toString();
	    }catch(Exception e){
	    	logger.error("Error while building jsonstring for indexing", e);
	    }
		
		return result;
  }
		
	public ReindexResponse createReindexJob(ReindexRequest reindexRequest) {
		Map<String, Mapping> mappingsMap = runner.getMappingMaps();
		ReindexResponse reindexResponse = null;
		String uri = indexerUtils.getESSearchURL(reindexRequest);
		Object response = bulkIndexer.getESResponse(uri, null, null);
		Integer total = JsonPath.read(response, "$.hits.total");
		StringBuilder url = new StringBuilder();
		Index index = mappingsMap.get(reindexRequest.getReindexTopic()).getIndexes().get(0);
		url.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/_search");
		reindexResponse = ReindexResponse.builder().totalRecordsToBeIndexed(total).estimatedTime(indexerUtils.fetchEstimatedTime(total))
				.message("Please hit the 'url' for the newly indexed data after the mentioned 'estimated time'.")
				.url(url.toString())
				.responseInfo(factory.createResponseInfoFromRequestInfo(reindexRequest.getRequestInfo(), true))
				.build();
		IndexJob job = IndexJob.builder().jobId(UUID.randomUUID().toString()).jobStatus(StatusEnum.INPROGRESS).typeOfJob(ConfigKeyEnum.REINDEX)
				.oldIndex(reindexRequest.getIndex() + "/" + reindexRequest.getType())
				.requesterId(reindexRequest.getRequestInfo().getUserInfo().getUuid()).newIndex(index.getName() + "/" + index.getType())
				.totalTimeTakenInMS(0L).tenantId(reindexRequest.getTenantId()).recordsToBeIndexed(total).totalRecordsIndexed(0)
				.auditDetails(indexerUtils.getAuditDetails(reindexRequest.getRequestInfo().getUserInfo().getUuid(), true)).build();
		reindexRequest.setJobId(job.getJobId()); reindexRequest.setStartTime(new Date().getTime()); reindexRequest.setTotalRecords(total);
		IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(reindexRequest.getRequestInfo()).job(job).build();
		indexerProducer.producer(reindexTopic, reindexRequest);
		indexerProducer.producer(persisterCreate, wrapper);
		reindexResponse.setJobId(job.getJobId());
				
		return reindexResponse;
	}
	
	public LegacyIndexResponse createLegacyindexJob(LegacyIndexRequest legacyindexRequest) {
		Map<String, Mapping> mappingsMap = runner.getMappingMaps();
		LegacyIndexResponse legacyindexResponse = null;
		StringBuilder url = new StringBuilder();
		Index index = mappingsMap.get(legacyindexRequest.getLegacyIndexTopic()).getIndexes().get(0);
		url.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/_search");
		legacyindexResponse = LegacyIndexResponse.builder().message("Please hit the 'url' after the legacy index job is complete.")
				.url(url.toString()).responseInfo(factory.createResponseInfoFromRequestInfo(legacyindexRequest.getRequestInfo(), true)).build();
		IndexJob job = IndexJob.builder().jobId(UUID.randomUUID().toString()).jobStatus(StatusEnum.INPROGRESS).typeOfJob(ConfigKeyEnum.LEGACYINDEX)
				.requesterId(legacyindexRequest.getRequestInfo().getUserInfo().getUuid()).newIndex(index.getName() + "/" + index.getType())
				.tenantId(legacyindexRequest.getTenantId()).totalRecordsIndexed(0).totalTimeTakenInMS(0L)
				.auditDetails(indexerUtils.getAuditDetails(legacyindexRequest.getRequestInfo().getUserInfo().getUuid(), true)).build();
		legacyindexRequest.setJobId(job.getJobId()); legacyindexRequest.setStartTime(new Date().getTime());
		IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(legacyindexRequest.getRequestInfo()).job(job).build();
		indexerProducer.producer(legacyIndexTopic, legacyindexRequest);
		logger.info("Job created!");
		indexerProducer.producer(persisterCreate, wrapper);
		legacyindexResponse.setJobId(job.getJobId());
				
		return legacyindexResponse;
	}
	
	public void reindexInPages(ReindexRequest reindexRequest) {
		increaseMaxResultWindow(reindexRequest, reindexRequest.getTotalRecords());
		String uri = indexerUtils.getESSearchURL(reindexRequest);
		Integer from = 0; Integer size = defaultPageSizeForReindex;
		while(true) {
			Object request = indexerUtils.getESSearchBody(from, size);
			Object response = bulkIndexer.getESResponse(uri, request, "POST");
			if(null != response) {
				List<Object> hits = JsonPath.read(response, "$.hits.hits");
				if(CollectionUtils.isEmpty(hits))
					break;
				else {
					List<Object> modifiedHits = new ArrayList<>();
					hits.parallelStream().forEach(hit -> {
						if(!isHitAMetaData(JsonPath.read(hit, "$._source"))) {
							modifiedHits.add(JsonPath.read(hit, "$._source"));
						}
					});
					Map<String, Object> requestToReindex = new HashMap<>();
					requestToReindex.put("hits", modifiedHits);
					indexerProducer.producer(reindexRequest.getReindexTopic(), requestToReindex);
					from += defaultPageSizeForReindex;
				}
			}else {
				IndexJob job = IndexJob.builder().jobId(reindexRequest.getJobId())
						.auditDetails(indexerUtils.getAuditDetails(reindexRequest.getRequestInfo().getUserInfo().getUuid(), false)).totalRecordsIndexed(from)
						.totalTimeTakenInMS(new Date().getTime() - reindexRequest.getStartTime()).jobStatus(StatusEnum.FAILED).build();
				IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(reindexRequest.getRequestInfo()).job(job).build();
				indexerProducer.producer(persisterUpdate, wrapper);
				logger.info("Porcess failed! for data from: "+from+ "and size: "+size);
				return;
			}
			IndexJob job = IndexJob.builder().jobId(reindexRequest.getJobId())
					.auditDetails(indexerUtils.getAuditDetails(reindexRequest.getRequestInfo().getUserInfo().getUuid(), false))
					.totalTimeTakenInMS(new Date().getTime() - reindexRequest.getStartTime()).jobStatus(StatusEnum.INPROGRESS).totalRecordsIndexed(from).build();
			IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(reindexRequest.getRequestInfo()).job(job).build();
			indexerProducer.producer(persisterUpdate, wrapper);
		}
		IndexJob job = IndexJob.builder().jobId(reindexRequest.getJobId())
				.auditDetails(indexerUtils.getAuditDetails(reindexRequest.getRequestInfo().getUserInfo().getUuid(), false)).totalRecordsIndexed(reindexRequest.getTotalRecords())
				.totalTimeTakenInMS(new Date().getTime() - reindexRequest.getStartTime()).jobStatus(StatusEnum.COMPLETED).build();
		IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(reindexRequest.getRequestInfo()).job(job).build();
		indexerProducer.producer(persisterUpdate, wrapper);
		
		logger.info("Process completed successfully!");
		
	}
	
	public void legacyIndexInPages(LegacyIndexRequest legacyIndexRequest) {
		logger.info("Operation for: "+legacyIndexRequest.getJobId());
		Integer offset = 0;
		Integer count = 0;
		Integer presentCount = 0;
		Integer size = null != legacyIndexRequest.getApiDetails().getPaginationDetails().getMaxPageSize() ? 
				legacyIndexRequest.getApiDetails().getPaginationDetails().getMaxPageSize() : defaultPageSizeForLegacyindex;
		Boolean isProccessDone = false;
		while (!isProccessDone) {
			String uri = indexerUtils.buildPagedUriForLegacyIndex(legacyIndexRequest.getApiDetails(), offset, size);
			Object request= null;
			try {
				request = legacyIndexRequest.getApiDetails().getRequest();
				if(null == legacyIndexRequest.getApiDetails().getRequest()) {
					HashMap<String, Object> map = new HashMap<>();
					map.put("RequestInfo", legacyIndexRequest.getRequestInfo());
					request = map;
				}
				Object response = restTemplate.postForObject(uri, request, Map.class);
				if(null == response) {
					//logger.info("Response: "+response);
					logger.info("Request: "+request);
					logger.info("URI: "+uri);
					IndexJob job = IndexJob.builder().jobId(legacyIndexRequest.getJobId())
							.auditDetails(indexerUtils.getAuditDetails(legacyIndexRequest.getRequestInfo().getUserInfo().getUuid(), false)).totalRecordsIndexed(count)
							.totalTimeTakenInMS(new Date().getTime() - legacyIndexRequest.getStartTime()).jobStatus(StatusEnum.FAILED).build();
					IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(legacyIndexRequest.getRequestInfo()).job(job).build();
					indexerProducer.producer(persisterUpdate, wrapper);
					break;
				}else {
					List<Object> searchResponse = JsonPath.read(response, legacyIndexRequest.getApiDetails().getResponseJsonPath());
					if(!CollectionUtils.isEmpty(searchResponse)) {
						indexerProducer.producer(legacyIndexRequest.getLegacyIndexTopic(), response);
						presentCount = searchResponse.size();
						count+=size; 
						logger.info("Size of res: "+searchResponse.size()+" and Count: "+count+" and offset: "+offset);
					}else {
						count = (count - size) + presentCount;
						logger.info("Size Count FINAL: "+count);
						isProccessDone = true;
						break;
					}
				}
			}catch(Exception e) {
				logger.info("Request: "+request);
				logger.info("URI: "+uri);
				logger.error("Exception: ",e);
				IndexJob job = IndexJob.builder().jobId(legacyIndexRequest.getJobId())
						.auditDetails(indexerUtils.getAuditDetails(legacyIndexRequest.getRequestInfo().getUserInfo().getUuid(), false)).totalRecordsIndexed(count)
						.totalTimeTakenInMS(new Date().getTime() - legacyIndexRequest.getStartTime()).jobStatus(StatusEnum.FAILED).build();
				IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(legacyIndexRequest.getRequestInfo()).job(job).build();
				indexerProducer.producer(persisterUpdate, wrapper);
				break;
			}
/*			IndexJob job = IndexJob.builder().jobId(legacyIndexRequest.getJobId())
					.auditDetails(indexerUtils.getAuditDetails(legacyIndexRequest.getRequestInfo().getUserInfo().getUuid(), false))
					.totalTimeTakenInMS(new Date().getTime() - legacyIndexRequest.getStartTime()).jobStatus(StatusEnum.INPROGRESS).totalRecordsIndexed(count).build();
			IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(legacyIndexRequest.getRequestInfo()).job(job).build();
			indexerProducer.producer(persisterUpdate, wrapper);*/
			
			offset+=size;
		}
		if(isProccessDone) {
			IndexJob job = IndexJob.builder().jobId(legacyIndexRequest.getJobId())
					.auditDetails(indexerUtils.getAuditDetails(legacyIndexRequest.getRequestInfo().getUserInfo().getUuid(), false)).totalRecordsIndexed(count)
					.totalTimeTakenInMS(new Date().getTime() - legacyIndexRequest.getStartTime()).jobStatus(StatusEnum.COMPLETED).build();
			IndexJobWrapper wrapper = IndexJobWrapper.builder().requestInfo(legacyIndexRequest.getRequestInfo()).job(job).build();
			indexerProducer.producer(persisterUpdate, wrapper);
		}

	}
	
	public boolean isHitAMetaData(Object hit) {
		ObjectMapper mapper = indexerUtils.getObjectMapper();
		boolean isMetaData = false;
		Map<String, Object> map = mapper.convertValue(hit, Map.class);
		Set<String> keySet = map.keySet();
		if(keySet.size() == 2) {
			if(keySet.contains("from") && keySet.contains("size"))
				isMetaData = true;
			else {
				isMetaData = false;
			}
		}else {
			isMetaData = false;
		}
		return isMetaData;
	}
	
	public void increaseMaxResultWindow(ReindexRequest reindexRequest, Integer totalRecords) {
		String uri = indexerUtils.getESSettingsURL(reindexRequest);
		Object body = indexerUtils.getESSettingsBody(totalRecords);
		Object response = bulkIndexer.getESResponse(uri, body, "PUT");
		if(response.toString().equals("OK")) {
			logger.info("Max window set to "+(totalRecords + 50000)+" for index: "+reindexRequest.getIndex());
		}
	}
	
	
	
	
}