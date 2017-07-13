package org.egov.notificationConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

@Service
/**
 * 
 * @author Yosadhara
 *
 */
public class Producer {
	@Autowired
	private Environment envorniment;

	/**
	 * This method will return map object for producer configuration
	 */
	@Bean
	public Map<String, Object> producerConfig() {
		Map<String, Object> producerProperties = new HashMap<String, Object>();
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, envorniment.getProperty("kafka.config.bootstrap_server_config"));
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return producerProperties;
	}

	/**
	 * This method will return producer factory bean based on producer
	 * configuration
	 */
	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfig());
	}

	/**
	 * This method will return kafka template bean based on producer factory
	 * bean
	 */
	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
