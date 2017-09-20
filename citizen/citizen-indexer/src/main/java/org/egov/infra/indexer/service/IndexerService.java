package org.egov.infra.indexer.service;

import java.util.List;

import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.web.contract.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IndexerService {

	public static final Logger logger = LoggerFactory.getLogger(IndexerService.class);

	
	@Autowired
	private org.egov.infra.indexer.web.contract.Service service;
	
	@Autowired
	private BulkIndexer bulkIndexer;
	
	public void elasticIndexer(String topic, String kafkaJson){
		List<Mapping> mappings = service.getServiceMaps().getMappings();
		for(Mapping mapping : mappings){
			if(mapping.getFromTopicSave().equals(topic) || mapping.getFromTopicUpdate().equals(topic)){
				logger.info("save topic = " + mapping.getFromTopicSave());
				logger.info("Update topic = " + mapping.getFromTopicUpdate());
				logger.info("Received topic = " + topic);
				try{
					bulkIndexer.indexCurrentValue(mapping, kafkaJson,
							(mapping.getIsBulk() == null || mapping.getIsBulk() == false) ? false : true);
				}catch(Exception e){
					logger.error("Exception while indexing, Uncaught at the indexer level: ", e);
				}
			}
		}
		
	}

	private String modifyKafkaJson(Mapping mapping, String kafkaJson) {
		// TODO Auto-generated method stub
		String modifiedKafkaJson = null;
		
		
		return modifiedKafkaJson;
	}
}