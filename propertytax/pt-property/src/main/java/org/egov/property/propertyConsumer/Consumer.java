
package org.egov.property.propertyConsumer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.PropertyRequest;
import org.egov.property.services.PersisterService;
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


@Service
public class Consumer {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Environment env;

    @Autowired
     KafkaTemplate<String, PropertyRequest> kafkaTemplate;
    
    @Autowired
    PersisterService persisterService;
    

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> consumerProperties = new HashMap<String, Object>();
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, env.getProperty("autoOffsetResetConfig"));
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("bootstrap.servers"));
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "boundary");
        return consumerProperties;
    }

    @Bean
    public ConsumerFactory<String, PropertyRequest> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
                new JsonDeserializer<>(PropertyRequest.class));

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> factory = new ConcurrentKafkaListenerContainerFactory<String, PropertyRequest>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @KafkaListener(topics = "#{environment.getProperty('validate.user')}")
    public void receive(PropertyRequest propertyRequest) throws SQLException {
            
      persisterService.addProperty(propertyRequest.getProperties());

    }
}
