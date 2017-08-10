package org.egov.propertyIndexer.indexerConsumer;

import java.io.IOException;
import java.util.Map;

import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.propertyIndexer.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafka
@Service
@Slf4j
public class TitleTransferConsumer {

	@Autowired
	JestClient client = null;

	@Autowired
	PropertiesManager propertiesManager;

	@KafkaListener(topics = { "#{propertiesManager.getCreateTitleTranfer()}",
			"#{propertiesManager.getUpdateTitleTransfer()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws IOException {
		TitleTransferRequest titleTransferRequest = new ObjectMapper().convertValue(consumerRecord,
				TitleTransferRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is: " + titleTransferRequest);
		TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
		String titleTransferData = new ObjectMapper().writeValueAsString(titleTransfer);
		client.execute(new Index.Builder(titleTransferData).index(propertiesManager.getTitleTransferIndex())
				.type(propertiesManager.getTitleTransferType()).id(titleTransfer.getApplicationNo()).build());
	}
}
