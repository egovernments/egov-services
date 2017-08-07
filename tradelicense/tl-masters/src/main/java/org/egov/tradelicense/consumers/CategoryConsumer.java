package org.egov.tradelicense.consumers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.CategoryRequest;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Consumer class will use for listening category object from kafka server to
 * insert data in postgres database
 * 
 * @author: Pavan Kumar Kamma
 */
@Service
@Configuration
@EnableKafka
public class CategoryConsumer {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	CategoryService categoryService;

	 @Autowired
	 private ObjectMapper objectMapper;
	 
	 private CountDownLatch latch = new CountDownLatch(1);

	 public void resetCountDown(){
		 this.latch = new CountDownLatch(1);
	 }
	 
	  public CountDownLatch getLatch() {
	    return latch;
	  }
	  
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
		consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "category");
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
	 * receive method
	 * 
	 * @param Object
	 *            This method is listened whenever category is created and
	 *            updated
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreateCategoryValidated()}",
			"#{propertiesManager.getUpdateCategoryValidated()}" })
	public void receive(ConsumerRecord<String, Object> consumerRecord) throws Exception {
	    

		CategoryRequest objectReceived = objectMapper.convertValue(consumerRecord.value(), CategoryRequest.class);
		
		if (consumerRecord.topic().equalsIgnoreCase(propertiesManager.getCreateCategoryValidated())) {
			categoryService.createCategory(objectReceived);
		}

		else {
			categoryService.updateCategory(objectReceived);
		}
		latch.countDown();
	}
	@Bean
	public CategoryConsumer getReceiver(){
		return this;
	}
}