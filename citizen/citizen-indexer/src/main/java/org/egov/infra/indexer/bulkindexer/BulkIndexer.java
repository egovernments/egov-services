package org.egov.infra.indexer.bulkindexer;

import java.util.List;
import java.util.Map;

import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.indexMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class BulkIndexer {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${egov.infra.indexer.host}")
	private String esHostUrl;

	// this is not bulk operation for time being
	
	public void indexCurrentValue(Mapping mapping, String kafkaJson) {
		
		String kfkTopic = mapping.getFromTopic();
		String esIndex = mapping.getIndexName(); 
		String version = mapping.getVersion();
		String indexNode = mapping.getJsonPath();
		String indexType = mapping.getIndexType();
		String indexId = mapping.getIndexID();
		
		// these will be not required once we map with original kafkaJson
		
		List<indexMap> indexMap = mapping.getIndexMap();
		List<String> omitPaths = mapping.getOmitPaths();
		List<String> maskPaths = mapping.getHashPaths();
		List<String> hashPaths = mapping.getHashPaths();
		
		String url = "";
		
		if (indexId != null) {
			
			url = esHostUrl + "/" + esIndex + "/" +indexType + "/" + JsonPath.read(kafkaJson, indexId);
			
		} else {
			
			url = esHostUrl + "/" + esIndex + "/" +indexType;
		}

		
		
		if (indexNode != null) {

			log.info("Indexing to elasticsearch when IndexNode is not empty " + url);
			
			String indexJson = JsonPath.read(kafkaJson, indexNode);
			
			try {
				restTemplate.postForObject(url, indexJson, Map.class);
			} catch (final Exception e) {
				log.error(e.toString());
				throw e;
			}
			
		} else {
			// kafkaJson

			log.info("Indexing to elasticsearch when IndexNode is empty " + url);
			try {
				restTemplate.postForObject(url, kafkaJson, Map.class);
			} catch (final Exception e) {
				log.error(e.toString());
				throw e;
			}

		}

	}

}
