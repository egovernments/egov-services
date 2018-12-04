package org.egov.infra.indexer.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.IndexerApplicationRunnerImpl;
import org.egov.common.contract.request.RequestInfo;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.producer.IndexerProducer;
import org.egov.infra.indexer.util.IndexerConstants;
import org.egov.infra.indexer.util.IndexerUtils;
import org.egov.infra.indexer.util.ResponseInfoFactory;
import org.egov.infra.indexer.web.contract.CustomJsonMapping;
import org.egov.infra.indexer.web.contract.FieldMapping;
import org.egov.infra.indexer.web.contract.Index;
import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.UriMapping;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexerService {

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

	public void elasticIndexer(String topic, String kafkaJson) throws Exception {
		log.debug("kafka Data: " + kafkaJson);
		Map<String, Mapping> mappingsMap = runner.getMappingMaps();
		if (null != mappingsMap.get(topic)) {
			Mapping mapping = mappingsMap.get(topic);
			log.debug("Mapping to be used: " + mapping);
			try {
				for (Index index : mapping.getIndexes()) {
					indexProccessor(index, kafkaJson, (index.getIsBulk() == null || !index.getIsBulk()) ? false : true);
				}
			} catch (Exception e) {
				log.error("Exception while indexing, Uncaught at the indexer level: ", e);
			}
		} else {
			log.error("No mappings found for the service to which the following topic belongs: " + topic);
		}
	}

	public void indexProccessor(Index index, String kafkaJson, boolean isBulk) throws Exception {
		Long startTime = null;
		log.debug("index: " + index.getCustomJsonMapping());
		StringBuilder url = new StringBuilder();
		url.append(esHostUrl).append(index.getName()).append("/").append(index.getType()).append("/").append("_bulk");
		startTime = new Date().getTime();
		if (!StringUtils.isEmpty(index.getJsonPath())) {
			if (null != index.getCustomJsonMapping()) {
				StringBuilder urlForMap = new StringBuilder();
				urlForMap.append(esHostUrl).append(index.getName()).append("/").append("_mapping").append("/")
						.append(index.getType());
				indexerUtils.validateAndIndex(buildCustomJsonForBulk(index, kafkaJson, urlForMap.toString(), isBulk),
						url.toString(), index);
			} else {
				indexerUtils.validateAndIndex(buildIndexJsonWithJsonpath(index, kafkaJson, isBulk), url.toString(),
						index);
			}
		} else if (null != index.getCustomJsonMapping()) {
			StringBuilder urlForMap = new StringBuilder();
			urlForMap.append(esHostUrl).append(index.getName()).append("/").append("_mapping").append("/")
					.append(index.getType());
			indexerUtils.validateAndIndex(buildCustomJsonForBulk(index, kafkaJson, urlForMap.toString(), isBulk),
					url.toString(), index);
		} else {
			indexerUtils.validateAndIndex(buildIndexJsonWithoutJsonpath(index, kafkaJson, isBulk), url.toString(),
					index);
		}
		log.info("Total time taken: " + ((new Date().getTime()) - startTime) + "ms");
	}

	public String buildIndexJsonWithJsonpath(Index index, String kafkaJson, boolean isBulk) throws Exception {
		StringBuilder jsonTobeIndexed = new StringBuilder();
		String result = null;
		JSONArray kafkaJsonArray = null;
		try {
			kafkaJsonArray = indexerUtils.constructArrayForBulkIndex(kafkaJson, index, isBulk);
			for (int i = 0; i < kafkaJsonArray.length(); i++) {
				if (null != kafkaJsonArray.get(i)) {
					String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
					if (!StringUtils.isEmpty(index.getId())) {
						String id = indexerUtils.buildIndexId(index, stringifiedObject);
						if (StringUtils.isEmpty(id)) {
							continue;
						} else {
							final String actionMetaData = String.format(IndexerConstants.ES_INDEX_HEADER_FORMAT,
									"" + id);
							jsonTobeIndexed.append(actionMetaData).append(stringifiedObject).append("\n");
						}
					} else {
						jsonTobeIndexed.append(stringifiedObject);
					}
				} else {
					log.info("buildIndexJsonWithJsonpath, null json in kafkajsonarrya, index: " + i);
					continue;
				}
			}
			result = jsonTobeIndexed.toString();
		} catch (Exception e) {
			log.error("Error while building jsonstring for indexing", e);
		}

		return result;
	}

	public String buildCustomJsonForBulk(Index index, String kafkaJson, String urlForMap, boolean isBulk)
			throws Exception {
		StringBuilder jsonTobeIndexed = new StringBuilder();
		String result = null;
		JSONArray kafkaJsonArray = null;
		try {
			kafkaJsonArray = indexerUtils.constructArrayForBulkIndex(kafkaJson.toString(), index, isBulk);
			for (int i = 0; i < kafkaJsonArray.length(); i++) {
				if (null != kafkaJsonArray.get(i)) {
					String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
					String customIndexJson = buildCustomJsonForIndex(index.getCustomJsonMapping(), stringifiedObject,
							urlForMap);
					if (null != index.getId()) {
						String id = indexerUtils.buildIndexId(index, stringifiedObject);
						log.debug("Inserting id to the json being indexed, id = " + id);
						final String actionMetaData = String.format(IndexerConstants.ES_INDEX_HEADER_FORMAT, "" + id);
						jsonTobeIndexed.append(actionMetaData).append(customIndexJson).append("\n");
					} else {
						jsonTobeIndexed.append(customIndexJson);
					}
				} else {
					log.info("buildIndexJsonWithJsonpath, null json in kafkajsonarrya, index: " + i);
					continue;
				}
			}
			result = jsonTobeIndexed.toString();
		} catch (Exception e) {
			log.error("Error while building jsonstring for indexing", e);
		}

		return result;
	}

	public String buildCustomJsonForIndex(CustomJsonMapping customJsonMappings, String kafkaJson, String urlForMap) {
		Object indexMap = null;
		ObjectMapper mapper = new ObjectMapper();
		if (null != customJsonMappings.getIndexMapping()) {
			indexMap = customJsonMappings.getIndexMapping();
		} else {
			indexMap = bulkIndexer.getIndexMappingfromES(urlForMap);
		}
		DocumentContext documentContext = JsonPath.parse(indexMap);
		if (!CollectionUtils.isEmpty(customJsonMappings.getFieldMapping())) {
			for (FieldMapping fieldMapping : customJsonMappings.getFieldMapping()) {
				String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
				String expression = indexerUtils.getProcessedJsonPath(fieldMapping.getOutJsonPath());
				try {
					documentContext.put(expression, expressionArray[expressionArray.length - 1],
							JsonPath.read(kafkaJson, fieldMapping.getInjsonpath()));
				} catch (Exception e) {
					continue;
				}

			}
		}
		/**
		 * This block denormalizes data from external services
		 */
		if (!CollectionUtils.isEmpty(customJsonMappings.getExternalUriMapping())) {
			for (UriMapping uriMapping : customJsonMappings.getExternalUriMapping()) {
				Object response = null;
				String uri = null;
				try {
					uri = indexerUtils.buildUri(uriMapping, kafkaJson);
					response = restTemplate.postForObject(uri, uriMapping.getRequest(), Map.class);
					if (null == response)
						continue;
				} catch (Exception e) {
					log.error("Exception while trying to hit: " + uri, e);
					continue;
				}
				log.debug("Response: " + response + " from the URI: " + uriMapping.getPath());
				for (FieldMapping fieldMapping : uriMapping.getUriResponseMapping()) {
					String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
					String expression = indexerUtils.getProcessedJsonPath(fieldMapping.getOutJsonPath());
					try {
						Object value = JsonPath.read(mapper.writeValueAsString(response), fieldMapping.getInjsonpath());
						documentContext.put(expression, expressionArray[expressionArray.length - 1], value);
					} catch (Exception e) {
						log.error("Value: " + fieldMapping.getInjsonpath() + " is not found!");
						log.info("URI: " + uri);
						log.info("context: " + documentContext.jsonString().toString());
						continue;
					}
				}

			}
		}

		/**
		 * This block denormalizes data from MDMS.
		 */
		if (!CollectionUtils.isEmpty(customJsonMappings.getMdmsMapping())) {
			for (UriMapping uriMapping : customJsonMappings.getMdmsMapping()) {
				Object response = null;
				StringBuilder uri = new StringBuilder();
				uri.append(uriMapping.getPath());
				Object request = null;
				try {
					request = indexerUtils.prepareMDMSSearchReq(uri, new RequestInfo(), kafkaJson, uriMapping);
					response = restTemplate.postForObject(uri.toString(), request, Map.class);
					if (null == response)
						continue;
				} catch (Exception e) {
					log.error("Exception while trying to hit: " + uri, e);
					log.info("Request: " + uriMapping.getRequest());
					continue;
				}
				log.debug("Response: " + response + " from the URI: " + uriMapping.getPath());
				for (FieldMapping fieldMapping : uriMapping.getUriResponseMapping()) {
					String[] expressionArray = (fieldMapping.getOutJsonPath()).split("[.]");
					String expression = indexerUtils.getProcessedJsonPath(fieldMapping.getOutJsonPath());
					try {
						Object value = JsonPath.read(mapper.writeValueAsString(response), fieldMapping.getInjsonpath());
						if (value instanceof List) {
							if (((List) value).size() == 1) {
								value = ((List) value).get(0);
							}
						}
						documentContext.put(expression, expressionArray[expressionArray.length - 1], value);
					} catch (Exception e) {
						log.error("Value: " + fieldMapping.getInjsonpath() + " is not found!");
						log.info("MDMS Request: " + uriMapping.getRequest());
						continue;
					}
				}

			}
		}

		return documentContext.jsonString().toString(); // jsonString has to be converted to string
	}

	public String buildIndexJsonWithoutJsonpath(Index index, String kafkaJson, boolean isBulk) throws Exception {
		StringBuilder jsonTobeIndexed = new StringBuilder();
		String result = null;
		JSONArray kafkaJsonArray = null;
		try {
			kafkaJsonArray = indexerUtils.constructArrayForBulkIndex(kafkaJson, index, isBulk);
			for (int i = 0; i < kafkaJsonArray.length(); i++) {
				if (null != kafkaJsonArray.get(i)) {
					String stringifiedObject = indexerUtils.buildString(kafkaJsonArray.get(i));
					if (null != index.getId()) {
						String id = indexerUtils.buildIndexId(index, stringifiedObject);
						log.debug("Inserting id to the json being indexed, id = " + id);
						final String actionMetaData = String.format(IndexerConstants.ES_INDEX_HEADER_FORMAT, "" + id);
						jsonTobeIndexed.append(actionMetaData).append(stringifiedObject).append("\n");
					} else {
						jsonTobeIndexed.append(stringifiedObject);
					}
				} else {
					log.info("buildIndexJsonWithJsonpath, null json in kafkajsonarrya, index: " + i);
					continue;
				}
			}
			result = jsonTobeIndexed.toString();
		} catch (Exception e) {
			log.error("Error while building jsonstring for indexing", e);
		}

		return result;
	}

}