package org.egov.propertyIndexer.indexerConsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.propertyIndexer.config.PropertiesManager;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;

/*
 * Consumer class will use for listing  property object from kafka server to insert data in elastic server
 * @author:narendra
 */

@Configuration
@EnableKafka
@Service
@Slf4j
public class Consumer {

    // TODO Hey there need to read topic name from application properties via environment
	@Autowired
	PropertiesManager propertiesManager;

    @Autowired
    JestClient client = null;

    // public static final String topic=getTopic();

    /*
     * This method for getting consumer configuration bean
     */
    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> consumerProperties = new HashMap<String, Object>();
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, propertiesManager.getConsumerOffset());
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesManager.getBootstrapServer());
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, propertiesManager.getConsumerGroup());
        return consumerProperties;
    }

    /*
     * This method will return the consumer factory bean based on consumer configuration
     */
    @Bean
    public ConsumerFactory<String, PropertyRequest> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
                new JsonDeserializer<>(PropertyRequest.class));

    }

    /*
     * This bean will return kafka listner object based on consumer factory
     */

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PropertyRequest> factory = new ConcurrentKafkaListenerContainerFactory<String, PropertyRequest>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    /*
     * This method will build and return jest client bean
     */

    @Bean
    public JestClient getClient() {
        String url = "http://" + propertiesManager.getEsHost() + ":" + propertiesManager.getEsPort();
        if (this.client == null) {
            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig.Builder(url)
                    .multiThreaded(Boolean.valueOf(propertiesManager.getIsMultiThread()))
                    .readTimeout(Integer.valueOf(propertiesManager.getTimeout()))
                    .build());
            this.client = factory.getObject();
        }

        return this.client;
    }

    /*
     * This method will listen when ever data pushed to indexer topic and insert data in elastic search
     */

    @KafkaListener(topics = { "#{propertiesManager.getCreateWorkflow()}",
            "#{propertiesManager.getUpdateWorkflow()}",
            "#{propertiesManager.getApproveWorkflow()}" })
    public void receive(ConsumerRecord<String, PropertyRequest> consumerRecord) throws IOException {
        log.info("consumer topic value is: " + consumerRecord.topic() + " consumer value is" + consumerRecord.value());
        for (Property property : consumerRecord.value().getProperties()) {
            String propertyData = new ObjectMapper().writeValueAsString(property);
            client.execute(
                    new Index.Builder(propertyData)
                            .index(propertiesManager.getPropertyIndex())
                            .type(propertiesManager.getPropertyIndexType())
                            .build());
        }
        client.shutdownClient();

    }
}
