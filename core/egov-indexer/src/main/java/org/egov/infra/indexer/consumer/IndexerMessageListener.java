package org.egov.infra.indexer.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.infra.indexer.custom.pgr.models.PGRCustomDecorator;
import org.egov.infra.indexer.custom.pgr.models.PGRIndexObject;
import org.egov.infra.indexer.custom.pgr.models.ServiceResponse;
import org.egov.infra.indexer.service.IndexerService;
import org.egov.infra.indexer.service.LegacyIndexService;
import org.egov.infra.indexer.service.ReindexService;
import org.egov.infra.indexer.util.IndexerUtils;
import org.egov.infra.indexer.web.contract.LegacyIndexRequest;
import org.egov.infra.indexer.web.contract.ReindexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexerMessageListener implements MessageListener<String, String> {

	@Autowired
	private IndexerService indexerService;

	@Autowired
	private IndexerUtils indexerUtils;
	
	@Autowired
	private ReindexService reindexService;
	
	@Autowired
	private LegacyIndexService legacyIndexService;
	
	@Autowired
	private PGRCustomDecorator pgrCustomDecorator;

	@Value("${egov.core.reindex.topic.name}")
	private String reindexTopic;
	
	@Value("${egov.core.legacyindex.topic.name}")
	private String legacyIndexTopic;

	@Value("${egov.indexer.pgr.create.topic.name}")
	private String pgrCreateTopic;

	@Value("${egov.indexer.pgr.update.topic.name}")
	private String pgrUpdateTopic;
	
	@Value("${egov.indexer.pgr.legacyindex.topic.name}")
	private String pgrLegacyTopic;
	


	@Override
	public void onMessage(ConsumerRecord<String, String> data) {
		log.info("Topic: " + data.topic());		
		ObjectMapper mapper = indexerUtils.getObjectMapper();
		if(data.topic().equals("update-pt-property-index")) {
			log.info("Value: "+data.value());
		}
		if (data.topic().equals(reindexTopic)) {
			try {
				ReindexRequest reindexRequest = mapper.readValue(data.value(), ReindexRequest.class);
				reindexService.beginReindex(reindexRequest);
			} catch (Exception e) {
				log.error("Couldn't parse reindex request: ", e);
			}
		}else if(data.topic().equals(legacyIndexTopic)) {
			try {
				LegacyIndexRequest legacyIndexRequest = mapper.readValue(data.value(), LegacyIndexRequest.class);
				legacyIndexService.beginLegacyIndex(legacyIndexRequest);
			}catch(Exception e) {
				log.error("Couldn't parse legacyindex request: ", e);
			}
			
		}else if(data.topic().equals(pgrCreateTopic) || data.topic().equals(pgrUpdateTopic)) {
			try {
				ServiceResponse serviceResponse = mapper.readValue(data.value(), ServiceResponse.class);
				PGRIndexObject indexObject = pgrCustomDecorator.dataTransformationForPGR(serviceResponse);
				indexerService.elasticIndexer(data.topic(), mapper.writeValueAsString(indexObject));
			} catch (Exception e) {
				log.error("Couldn't parse pgrindex request: ", e);
			}
		}else {
			try {
				indexerService.elasticIndexer(data.topic(), data.value());
			} catch (Exception e) {
				log.error("error while indexing: ", e);
			}
		}
	}

}
