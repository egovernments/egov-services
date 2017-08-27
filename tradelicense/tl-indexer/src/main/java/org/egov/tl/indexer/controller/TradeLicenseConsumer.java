package org.egov.tl.indexer.controller;

import java.io.IOException;
import java.util.Map;

import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.indexer.client.JestClientEs;
import org.egov.tl.indexer.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;

/*
 * Consumer class will use for listing  property object from kafka server to insert data in elastic server
 * @author Shubham prata singh
 */

@Configuration
@EnableKafka
@Service
@Slf4j
public class TradeLicenseConsumer {

	// TODO Hey there need to read topic name from application properties via
	// environment
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	JestClientEs client;

	@Autowired
	ObjectMapper mapper;

	@KafkaListener(topics = { "#{propertiesManager.getCreateLegacyTradeValidated()}",
			"#{propertiesManager.getUpdateLegacyTradeValidated()}" })
	public void receive(Map<String, TradeLicenseRequest> consumerRecord,
			@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws IOException {

		if (topic.equalsIgnoreCase(propertiesManager.getCreateLegacyTradeValidated())) {

			TradeLicenseRequest request = mapper.convertValue(
					(consumerRecord.get(propertiesManager.getCreateLegacyTradeValidated())), TradeLicenseRequest.class);
			log.info("consumer topic value is: " + topic + " consumer value is" + request);

			for (TradeLicenseContract tlContract : request.getLicenses()) {

				String tradeLicenseData = mapper.writeValueAsString(tlContract);
				DocumentResult DocumentResult = client.getClient()
						.execute(new Index.Builder(tradeLicenseData)
								.index(propertiesManager.getEsIndex())
								.type(propertiesManager.getEsIndexType())
								.id(String.valueOf(tlContract.getId()))
								.build());
				if (DocumentResult.isSucceeded()){
					log.info("inserted data successfully to elasticsearch with Topic : "
							+ propertiesManager.getCreateLegacyTradeValidated());
				}	
			}
		} else if(topic.equalsIgnoreCase(propertiesManager.getUpdateLegacyTradeValidated())){
			
			TradeLicenseRequest request = mapper.convertValue(
					(consumerRecord.get(propertiesManager.getUpdateLegacyTradeValidated())), TradeLicenseRequest.class);
			log.info("consumer topic value is: " + topic + " consumer value is" + request);
			for (TradeLicenseContract tlContract : request.getLicenses()) {

				String tradeLicenseData = mapper.writeValueAsString(tlContract);
				DocumentResult DocumentResult = client.getClient()
						.execute(new Index.Builder(tradeLicenseData)
								.index(propertiesManager.getEsIndex())
								.type(propertiesManager.getEsIndexType())
								.id(String.valueOf(tlContract.getId()))
								.build());
				if (DocumentResult.isSucceeded()){
					log.info("inserted data successfully to elasticsearch with Topic : "
							+ propertiesManager.getCreateLegacyTradeValidated());
				}	
			}
		}

	}

}
