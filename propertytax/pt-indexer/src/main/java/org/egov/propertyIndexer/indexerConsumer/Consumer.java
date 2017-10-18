package org.egov.propertyIndexer.indexerConsumer;

import java.io.IOException;
import java.util.Map;

import org.egov.models.Demolition;
import org.egov.models.DemolitionRequest;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.propertyIndexer.config.PropertiesManager;
import org.egov.propertyIndexer.model.PropertyES;
import org.egov.propertyIndexer.repository.IndexerRepository;
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

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	JestClient client = null;

	@Autowired
	IndexerRepository indexerRepository;

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
			"#{propertiesManager.getCreateTitleTranfer()}", "#{propertiesManager.getUpdateTitleTransfer()}",
			"#{propertiesManager.getApproveWorkflow()}", "#{propertiesManager.getApproveTitleTransfer()}",
			"#{propertiesManager.getApproveDemolition()}", "#{propertiesManager.getCreateDemolitionWorkflow()}",
			"#{propertiesManager.getUpdateDemolitionWorkflow()" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws IOException, Exception {

		if (topic.equalsIgnoreCase(propertiesManager.getCreateWorkflow())
				|| topic.equalsIgnoreCase(propertiesManager.getUpdateWorkflow())
				|| topic.equalsIgnoreCase(propertiesManager.getApproveWorkflow())
				|| topic.equalsIgnoreCase(propertiesManager.getSavePropertyTitletransfer())) {
			PropertyRequest propertyRequest = new ObjectMapper().convertValue(consumerRecord, PropertyRequest.class);
			log.info("consumer topic value is: " + topic + " consumer value is" + propertyRequest);
			for (Property property : propertyRequest.getProperties()) {

				PropertyES propertyData = indexerRepository.addMasterData(property, propertyRequest.getRequestInfo());
				client.execute(new Index.Builder(propertyData).index(propertiesManager.getPropertyIndex())
						.type(propertiesManager.getPropertyIndexType())
						.id(property.getPropertyDetail().getApplicationNo()).build());
			}
		}

		else if (topic.equalsIgnoreCase(propertiesManager.getCreateTitleTranfer())
				|| topic.equalsIgnoreCase(propertiesManager.getUpdateTitleTransfer())
				|| topic.equalsIgnoreCase(propertiesManager.getApproveTitleTransfer())) {
			TitleTransferRequest titleTransferRequest = new ObjectMapper().convertValue(consumerRecord,
					TitleTransferRequest.class);

			TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
			String titleTransferData = new ObjectMapper().writeValueAsString(titleTransfer);
			client.execute(new Index.Builder(titleTransferData).index(propertiesManager.getTitleTransferIndex())
					.type(propertiesManager.getTitleTransferType()).id(titleTransfer.getApplicationNo()).build());
		} 
		else if (topic.equalsIgnoreCase(propertiesManager.getCreateDemolitionWorkflow())
				|| topic.equalsIgnoreCase(propertiesManager.getApproveDemolition())
				|| topic.equalsIgnoreCase(propertiesManager.getUpdateDemolitionWorkflow())) {

			DemolitionRequest demolitionRequest = new ObjectMapper().convertValue(consumerRecord,
					DemolitionRequest.class);

			Demolition demolition = demolitionRequest.getDemolition();
			client.execute(new Index.Builder(demolition).index(propertiesManager.getDemolitionIndex())
					.type(propertiesManager.getDemolitionIndexType()).id(demolition.getApplicationNo()).build());

		}

	}

}
