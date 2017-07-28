package org.egov.propertyUser.userConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.egov.propertyUser.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * This class will use for sending property object to kafka server
 * 
 * @author: S Anilkumar
 * 
 */
@Service
@Slf4j
public class Producer {
  
    @Autowired
	PropertiesManager propertiesManager;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * This method will return map object for producer configuration
     */
    @Bean
    public Map<String, Object> producerConfig() {
    	
        Map<String, Object> producerProperties = new HashMap<String, Object>();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,propertiesManager.getBootstrapServer());
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        return producerProperties;
    }

    /**
     * This method will return producer factory bean based on producer configuration
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    /**
     * This method will return kafka template bean based on producer factory bean
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * This method will send property request to kakfa producer
     */
    public void send(String topic, Object consumerRecord) {
        log.info("topic name is : " + topic + "  producerecord is: " + consumerRecord);
        kafkaTemplate.send(topic, consumerRecord);
    }
}
