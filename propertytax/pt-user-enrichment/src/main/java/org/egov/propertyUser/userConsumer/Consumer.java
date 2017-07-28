package org.egov.propertyUser.userConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.User;
import org.egov.propertyUser.config.PropertiesManager;
import org.egov.propertyUser.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumer class will use for listing property object from kafka server. Authenticate the user Search the user Create the user If
 * user exist update the user id otherwise create the user
 * 
 * @author: S Anilkumar
 */

@Service
@Slf4j
public class Consumer {
    
    @Autowired
    PropertiesManager propertiesManager;
    
    @Autowired
    private Producer producer;

    @Autowired
    private UserUtil userUtil;

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
        		propertiesManager.getAutoOffsetReset());
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        		propertiesManager.getBootstrapServer());
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "user");
        return consumerProperties;
    }

    /**
     * This method will return the consumer factory bean based on consumer configuration
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
     * This method will listen property object from producer and check user authentication Updating auth token in
     * UserAuthResponseInfo Search user Create user
     */
    @KafkaListener(topics = { "#{propertiesManager.getCreatePropertyValidator()}",
            "#{propertiesManager.getUpdatePropertyValidator()}" })
    public void receive(ConsumerRecord<String, PropertyRequest> consumerRecord) throws Exception {
        log.info("consumer topic value is: " + consumerRecord.topic() + " consumer value is" + consumerRecord);
        PropertyRequest propertyRequest = consumerRecord.value();
        for (Property property : propertyRequest.getProperties()) {
            for (User user : property.getOwners()) {
                user.setUserName(user.getMobileNumber());

                user = userUtil.getUserId(user, propertyRequest.getRequestInfo());

            }
            if (consumerRecord.topic()
                    .equalsIgnoreCase(propertiesManager.getCreatePropertyValidator())) {

                producer.kafkaTemplate.send(propertiesManager.getCreatePropertyUserValidator(),
                        propertyRequest);

            } else if (consumerRecord.topic()
                    .equalsIgnoreCase(propertiesManager.getUpdatePropertyValidator())) {

                producer.kafkaTemplate.send(propertiesManager.getUpdatePropertyUserValidator(),
                        propertyRequest);

            }
        }
    }

}
