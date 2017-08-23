package org.egov.infra.indexer.service;

import java.util.List;

import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.indexMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexerService {

	@Autowired
	
	private org.egov.infra.indexer.web.contract.Service service;
	@Autowired
	private BulkIndexer bulkindexer;
	
	public void ElasticIndexer(String topic, String kafkaJson){
		
		String esIndex = null;
		String kfkTopic = null;
		String version = null;
		String indexNode = null;
		

		List<Mapping> mappings = service.getServiceMaps().getMappings();
			
		for(Mapping mapping : mappings){
			//String newKafkaJson = modifyKafkaJson(mapping, kafkaJson);
			if(mapping.getFromTopic().equals(topic)){
				
				bulkindexer.indexCurrentValue(mapping, kafkaJson);
			}
		}
		
	}

	private String modifyKafkaJson(Mapping mapping, String kafkaJson) {
		// TODO Auto-generated method stub
		String modifiedKafkaJson = null;
		
		
		return modifiedKafkaJson;
	}
}