package org.egov.web.indexer.consumer;

import java.util.Map;

import org.egov.web.indexer.config.PropertiesManager;
import org.egov.web.indexer.contract.ConnectionIndex;
import org.egov.web.indexer.contract.WaterConnectionReq;
import org.egov.web.indexer.repository.ElasticSearchRepository;
import org.egov.web.indexer.service.ConnectionAdaptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexerListener {

	@Autowired
    private ElasticSearchRepository elasticSearchRepository;
	
	@Autowired
    private ObjectMapper objectMapper;
	 
	@Autowired
	private ConnectionAdaptorService connectionAdaptorService; 
	
	@Autowired
	private PropertiesManager propertiesManager;

    @Autowired
    public IndexerListener(ElasticSearchRepository elasticSearchRepository,
                           ObjectMapper objectMapper) {
        this.elasticSearchRepository = elasticSearchRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {"${kafka.topics.newconnection.create.name}","${kafka.topics.newconnection.update.name}"})
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);
		ConnectionIndex connectionIndex = new ConnectionIndex();
		try {
			log.info("SaveAgreementConsumer agreement-save-db :" + elasticSearchRepository);
			connectionIndex = connectionAdaptorService.indexOnCreate(objectMapper.convertValue(consumerRecord, WaterConnectionReq.class));
		} catch (Exception exception) {
			log.debug("processMessage:" + exception);
			throw exception;
		}
		
		if (topic.equals(propertiesManager.getNewConnectionTopicName())) {
			elasticSearchRepository.saveConnection(connectionIndex);
		}
		else if (topic.equals(propertiesManager.getUpdateConnectionTopicName())) {
			elasticSearchRepository.updateConnection(connectionIndex);
		}

	}
}
