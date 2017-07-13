package org.egov.notificationConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.PropertyRequest;
import org.egov.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

@Service
/**
 * This is Consumer class
 * 
 * @author Yosadhara
 *
 */
public class Consumer {
	@Autowired
	Environment environment;

	@Autowired
	NotificationService notificationService;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	Producer producer;

	/**
	 * This method for getting consumer configuration bean
	 */
	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> consumerProperties = new HashMap<String, Object>();
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("notification.Offset"));
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("kafka.config.bootstrap_server_config"));
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("notification.groupName"));
		return consumerProperties;
	}

	/**
	 * This method will return the consumer factory bean based on consumer
	 * configuration
	 */
	@Bean
	public ConsumerFactory<String, PropertyRequest> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
				new JsonDeserializer<>(PropertyRequest.class));
	}

	/**
	 * This bean will return kafka listner object based on consumer factory
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> factory = new ConcurrentKafkaListenerContainerFactory<String, PropertyRequest>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	/**
	 * This is receive method for consuming record from Kafka server
	 * 
	 * @param consumerRecord
	 */
	@KafkaListener(topics = { "#{environment.getProperty('demand.acknowledgement')}",
			"#{environment.getProperty('demand.approve')}", "#{environment.getProperty('demand.transferfee')}",
			"#{environment.getProperty('demand.reject')}", "#{environment.getProperty('property.acknowledgement')}",
			"#{environment.getProperty('property.approve')}", "#{environment.getProperty('property.reject')}",
			"#{environment.getProperty('revision.petition.acknowledgement')}",
			"#{environment.getProperty('revision.petition.hearing')}",
			"#{environment.getProperty('revision.petition.endorsement')}" })
	public void receive(ConsumerRecord<String, PropertyRequest> consumerRecord) {

		if (consumerRecord.topic().equalsIgnoreCase(environment.getProperty("demand.acknowledgement"))) {

			notificationService.demandAcknowledgement(consumerRecord.value());
		} else if (consumerRecord.topic().equalsIgnoreCase(environment.getProperty("demand.approve"))) {

			notificationService.demandApprove(consumerRecord.value());
		} else if (consumerRecord.topic().equalsIgnoreCase(environment.getProperty("demand.transferfee"))) {

			notificationService.demandTransferFee(consumerRecord.value());
		} else if (consumerRecord.topic().equalsIgnoreCase(environment.getProperty("demand.reject"))) {

			notificationService.demandReject(consumerRecord.value());
		} else if (consumerRecord.topic().equalsIgnoreCase(environment.getProperty("property.acknowledgement"))) {

			notificationService.propertyAcknowledgement(consumerRecord.value().getProperties());
		} else if (consumerRecord.topic().equalsIgnoreCase(environment.getProperty("property.approve"))) {

			notificationService.propertyApprove(consumerRecord.value().getProperties());
		} else if (consumerRecord.topic().equalsIgnoreCase(environment.getProperty("property.reject"))) {

			notificationService.propertyReject(consumerRecord.value().getProperties());
		} else if (consumerRecord.topic()
				.equalsIgnoreCase(environment.getProperty("revision.petition.acknowledgement"))) {

			notificationService.revisionPetitionAcknowldgement(consumerRecord.value().getProperties());
		} else if (consumerRecord.topic().equalsIgnoreCase(environment.getProperty("revision.petition.hearing"))) {

			notificationService.revisionPetitionHearing(consumerRecord.value().getProperties());
		} else if (consumerRecord.topic().equalsIgnoreCase(environment.getProperty("revision.petition.endorsement"))) {

			notificationService.revisionPetitionEndorsement(consumerRecord.value().getProperties());
		}
	}
}
