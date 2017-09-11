package org.egov.tl.indexer.consumer;

import java.io.IOException;
import java.util.Map;

import org.egov.tl.commons.web.contract.TradeLicenseIndexerContract;
import org.egov.tl.commons.web.requests.TradeLicenseIndexerRequest;
import org.egov.tl.indexer.client.JestClientEs;
import org.egov.tl.indexer.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;

/*
 * Consumer class to listen kafka topic and insert data in elastic server
 * 
 * @author Shubham prata singh
 */

@Configuration
@EnableKafka
@Service
@Slf4j
public class TradeLicenseConsumer {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	JestClientEs client;

	@Autowired
	ObjectMapper mapper;

	@KafkaListener(topics = { "#{propertiesManager.getTradeLicensePersistedTopic()}" })
	public void receive(Map<String, Object> tradeLicenseMap) throws IOException {

		if (tradeLicenseMap.get("tradelicense-persisted") != null) {

			TradeLicenseIndexerRequest request = mapper.convertValue(tradeLicenseMap.get("tradelicense-persisted"),
					TradeLicenseIndexerRequest.class);

			for (TradeLicenseIndexerContract tlContract : request.getLicenses()) {

				String tradeLicenseData = mapper.writeValueAsString(tlContract);
				DocumentResult DocumentResult = client.getClient()
						.execute(new Index.Builder(tradeLicenseData).index(propertiesManager.getEsIndex())
								.type(propertiesManager.getEsIndexType()).id(String.valueOf(tlContract.getId()))
								.build());
				if (DocumentResult.isSucceeded()) {
					log.info("inserted data successfully to elasticsearch with Topic : "
							+ propertiesManager.getTradeLicensePersistedTopic());
				}
			}
		}
	}
}