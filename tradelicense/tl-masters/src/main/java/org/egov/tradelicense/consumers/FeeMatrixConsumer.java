package org.egov.tradelicense.consumers;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.FeeMatrixRequest;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.services.FeeMatrixService;
import org.egov.tradelicense.persistence.repository.FeeMatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Consumer class will use for listening feematrix object from kafka server to
 * insert data in postgres database
 * 
 * @author: Pavan Kumar Kamma
 */
@Service
@Configuration
@Profile("production")
public class FeeMatrixConsumer {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	FeeMatrixRepository feeMatrixRepository;

	/**
	 * This method for getting consumer configuration bean
	 */
	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> consumerProperties = new HashMap<String, Object>();
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertiesManager.getKafkaOffsetConfig());
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesManager.getKafkaServerConfig());
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "feeMatrix");
		return consumerProperties;
	}

	/**
	 * This method will return the consumer factory bean based on consumer
	 * configuration
	 */
	@Bean
	public ConsumerFactory<String, FeeMatrixRequest> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
				new JsonDeserializer<>(FeeMatrixRequest.class));

	}

	/**
	 * This bean will return kafka listner object based on consumer factory
	 */

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, FeeMatrixRequest> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, FeeMatrixRequest> factory = new ConcurrentKafkaListenerContainerFactory<String, FeeMatrixRequest>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	/**
	 * receive method
	 * 
	 * @param CategoryRequest
	 *            This method is listened whenever category is created and
	 *            updated
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreateFeeMatrixValidated()}",
			"#{propertiesManager.getUpdateFeeMatrixValidated()}" })
	public void receive(ConsumerRecord<String, FeeMatrixRequest> consumerRecord) throws Exception {
		Boolean isNew = (consumerRecord.topic().equalsIgnoreCase(propertiesManager.getCreateFeeMatrixValidated()));
		feeMatrixRepository.persistNewFeeMatrix(consumerRecord.value(), isNew);
	}
}