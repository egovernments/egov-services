package org.egov.demand.consumer;

import java.util.Map;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.service.DemandService;
import org.egov.demand.web.contract.DemandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemandConsumer {

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private DemandService demandService;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = { "${kafka.topics.save.bill}", "${kafka.topics.update.bill}", "${kafka.topics.save.demand}",
			"${kafka.topics.update.demand}" })
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("key:" + topic + ":" + "value:" + consumerRecord);

		DemandRequest demandRequest = null;
		try {
			demandRequest = objectMapper.convertValue(consumerRecord, DemandRequest.class);
		} catch (IllegalArgumentException e) {
			log.error("error while converting map to demandrequest");
			throw e;
		}
		if (applicationProperties.getCreateDemandTopic().equals(topic))
			demandService.save(demandRequest);
		else if (applicationProperties.getUpdateDemandTopic().equals(topic))
			demandService.update(demandRequest);
	}
}
