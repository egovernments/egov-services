package org.egov.consumer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetWorkFlowConsumer {


	//@Autowired	private ApplicationProperties applicationProperties;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = { "${kafka.topics.receipt.update.collecteReceipt}","${kafka.topics.updateMIS.demand}","${kafka.topics.save.bill}", "${kafka.topics.update.bill}", "${kafka.topics.save.demand}",
			"${kafka.topics.update.demand}" , "${kafka.topics.save.taxHeadMaster}","${kafka.topics.update.taxHeadMaster}",
			"${kafka.topics.create.taxperiod.name}", "${kafka.topics.update.taxperiod.name}","${kafka.topics.save.glCodeMaster}",
			"${kafka.topics.update.glCodeMaster}","${kafka.topics.receipt.update.demand}",
			"${kafka.topics.create.businessservicedetail.name}", "${kafka.topics.update.businessservicedetail.name}"})
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);
		
		
	}
}
