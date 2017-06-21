package org.egov.demand.consumer;

import java.util.Map;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.repository.BillRepository;
import org.egov.demand.service.DemandService;
import org.egov.demand.web.contract.BillRequest;
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
	
	@Autowired
	private BillRepository billRepository;

	@KafkaListener(topics = { "${kafka.topics.save.bill}", "${kafka.topics.update.bill}", "${kafka.topics.save.demand}",
			"${kafka.topics.update.demand}" })
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("key:" + topic + ":" + "value:" + consumerRecord);

		try {
			
			if (applicationProperties.getCreateDemandTopic().equals(topic))
				demandService.save(objectMapper.convertValue(consumerRecord, DemandRequest.class));
			else if (applicationProperties.getUpdateDemandTopic().equals(topic))
				demandService.update(objectMapper.convertValue(consumerRecord, DemandRequest.class));
			else if (topic.equals(applicationProperties.getCreateBillTopic()))
				billRepository.saveBill(objectMapper.convertValue(consumerRecord, BillRequest.class));
			
		} catch (Exception exception) {
			log.info("processMessage:" + exception);
			throw exception;
		}
	}
}
