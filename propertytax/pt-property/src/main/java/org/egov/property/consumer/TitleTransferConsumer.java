package org.egov.property.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.PropertyRequest;
import org.egov.models.TitleTransferRequest;
import org.egov.property.services.PersisterService;
import org.egov.property.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableKafka
@Slf4j
public class TitleTransferConsumer {
	@Autowired
	Environment environment;

	@Autowired
	PropertyService propertyService;
	
	@Autowired
	PersisterService persisterService;

	@Autowired
	Producer producer;

	/*
	 * This method for creating rest template
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * This method for getting consumer configuration bean
	 */
	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> consumerProperties = new HashMap<String, Object>();
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
				environment.getProperty("auto.offset.reset.config"));
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				environment.getProperty("kafka.config.bootstrap_server_config"));
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "propertytitletransfer");
		return consumerProperties;
	}

	/**
	 * This method will return the consumer factory bean based on consumer
	 * configuration
	 */
	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
				new JsonDeserializer<>(Object.class));

	}

	/**
	 * This bean will return kafka listner object based on consumer factory
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	/**
	 * This method will listen property object from producer and check user
	 * authentication Updating auth token in UserAuthResponseInfo Search user
	 * Create user
	 */
	@KafkaListener(topics = { "#{environment.getProperty('egov.propertytax.property.titletransfer.workflow.created')}",
			"#{environment.getProperty('egov.propertytax.property.titletransfer.approved')}",
			"#{environment.getProperty('egov.propertytax.property.titletransfer.workflow.updated')}",
			"#{environment.getProperty('egov.propertytax.property.titletransfer.db.saved')}" })
	public void receive(ConsumerRecord<String, Object> consumerRecord) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		TitleTransferRequest titleTransferRequest = objectMapper.convertValue(consumerRecord.value(),
				TitleTransferRequest.class);
		log.info("consumer topic value is: " + consumerRecord.topic() + " consumer value is" + consumerRecord);

		if (consumerRecord.topic().equalsIgnoreCase(
			 environment.getProperty("egov.propertytax.property.titletransfer.workflow.created"))) {
			persisterService.addTitleTransfer(titleTransferRequest);
		} else if (consumerRecord.topic().equalsIgnoreCase(
				environment.getProperty("egov.propertytax.property.titletransfer.workflow.updated"))) {
			persisterService.updateTitleTransfer(titleTransferRequest);
		} else {
			PropertyRequest propertyRequest = propertyService.savePropertyHistoryandUpdateProperty(titleTransferRequest);

			producer.send(environment.getProperty("egov.propertytax.property.titletransfer.db.saved"), propertyRequest);

		}
	}

}
