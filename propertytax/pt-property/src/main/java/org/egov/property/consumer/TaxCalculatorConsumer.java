
package org.egov.property.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.CalculationRequest;
import org.egov.models.CalculationResponse;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Consumer class will use for listing property object from kafka server to
 * update the property data &send it back to kafka topic
 * 
 * @author: Prasad Khandagale
 */
@Service
public class TaxCalculatorConsumer {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	Environment environment;

	@Autowired
	PropertyProducer producer;

	@Autowired
	KafkaTemplate<String, PropertyRequest> kafkaTemplate;

	/**
	 * This method for getting consumer configuration bean
	 */
	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> consumerProperties = new HashMap<String, Object>();
		consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
				environment.getProperty("auto.offset.reset.config"));
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("kafka.config.bootstrap_server_config"));
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "boundary");
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
	 * This method will create rest template object
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * receive method
	 * 
	 * @param PropertyRequest
	 *            This method is listened whenever property is created and
	 *            updated
	 */
	@KafkaListener(topics = { "#{environment.getProperty('egov.propertytax.property.tax')}", "#{environment.getProperty('egov.propertytax.property.update.tax')}"  })
	public void receive(ConsumerRecord<String, PropertyRequest> consumerRecord) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		Property property = consumerRecord.value().getProperties().get(0);
		CalculationRequest calculationRequest = new CalculationRequest();
		calculationRequest.setRequestInfo(consumerRecord.value().getRequestInfo());
		calculationRequest.setProperty(property);
		String url=environment.getProperty("egov.services.pt_calculator.hostname")+environment.getProperty("egov.services.pt_calculator.calculatorpath");
		CalculationResponse calculationResponse = restTemplate.postForObject(url,
				calculationRequest, CalculationResponse.class);
		String taxCalculations = objectMapper.writeValueAsString(calculationResponse.getTaxes());
		property.getPropertyDetail().setTaxCalculations(taxCalculations);
		producer.send(environment.getProperty("egov.propertytax.property.create.approved"), consumerRecord.value());

	}

}
