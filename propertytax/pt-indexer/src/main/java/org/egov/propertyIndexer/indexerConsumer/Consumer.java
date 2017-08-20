package org.egov.propertyIndexer.indexerConsumer;

import java.io.IOException;
import java.util.Map;

import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.propertyIndexer.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;

/*
 * Consumer class will use for listing  property object from kafka server to insert data in elastic server
 * @author:narendra
 */

@Configuration
@EnableKafka
@Service
@Slf4j
public class Consumer {

	// TODO Hey there need to read topic name from application properties via
	// environment
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	JestClient client = null;

	// public static final String topic=getTopic();

	/*
	 * This method will build and return jest client bean
	 */

	@Bean
	public JestClient getClient() {
		String url = "http://" + propertiesManager.getEsHost() + ":" + propertiesManager.getEsPort();
		if (this.client == null) {
			JestClientFactory factory = new JestClientFactory();
			factory.setHttpClientConfig(new HttpClientConfig.Builder(url)
					.multiThreaded(Boolean.valueOf(propertiesManager.getIsMultiThread()))
					.readTimeout(Integer.valueOf(propertiesManager.getTimeout())).build());
			this.client = factory.getObject();
		}

		return this.client;
	}

	/*
	 * This method will listen when ever data pushed to indexer topic and insert
	 * data in elastic search
	 */

	@KafkaListener(topics = { "#{propertiesManager.getCreateWorkflow()}", "#{propertiesManager.getUpdateWorkflow()}",
			"#{propertiesManager.getApproveWorkflow()}", "#{propertiesManager.getApproveTitleTransfer()}",
			"#{propertiesManager.getCreateTitleTranfer()}", "#{propertiesManager.getUpdateTitleTransfer()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws IOException {

		if (topic.equalsIgnoreCase(propertiesManager.getCreateWorkflow())
				|| topic.equalsIgnoreCase(propertiesManager.getUpdateWorkflow())
				|| topic.equalsIgnoreCase(propertiesManager.getApproveWorkflow())
				|| topic.equalsIgnoreCase(propertiesManager.getApproveTitleTransfer())) {
			PropertyRequest propertyRequest = new ObjectMapper().convertValue(consumerRecord, PropertyRequest.class);
			log.info("consumer topic value is: " + topic + " consumer value is" + propertyRequest);
			for (Property property : propertyRequest.getProperties()) {
				String propertyData = new ObjectMapper().writeValueAsString(property);
				client.execute(new Index.Builder(propertyData).index(propertiesManager.getPropertyIndex())
						.type(propertiesManager.getPropertyIndexType())
						.id(property.getPropertyDetail().getApplicationNo()).build());
			}
		}

		else if (topic.equalsIgnoreCase(propertiesManager.getCreateTitleTranfer())
				|| topic.equalsIgnoreCase(propertiesManager.getUpdateTitleTransfer())) {
			TitleTransferRequest titleTransferRequest = new ObjectMapper().convertValue(consumerRecord,
					TitleTransferRequest.class);

			TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
			String titleTransferData = new ObjectMapper().writeValueAsString(titleTransfer);
			client.execute(new Index.Builder(titleTransferData).index(propertiesManager.getTitleTransferIndex())
					.type(propertiesManager.getTitleTransferType()).id(titleTransfer.getApplicationNo()).build());
		}

	}

}
