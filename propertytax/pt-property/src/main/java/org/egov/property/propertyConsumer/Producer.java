package org.egov.property.propertyConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.egov.models.PropertyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Configuration
@Service
public class Producer {

	@Autowired
	private Environment environment;

	@Autowired
	KafkaTemplate<String, PropertyRequest> kafkaTemplate;



	@Bean
	public Map<String,Object> producerConfig(){
		Map<String,Object> producerProperties=new HashMap<String,Object>();
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("bootstrap.servers"));
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return producerProperties;
	}

	@Bean
	public ProducerFactory<String, PropertyRequest> producerFactory(){
		return new DefaultKafkaProducerFactory<>(producerConfig());
	}

	@Bean
	public KafkaTemplate<String, PropertyRequest> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean 
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	public void send(String topic,PropertyRequest propertyRequest){
		kafkaTemplate.send(topic, propertyRequest);
	}
}
