package org.egov.propertytax.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.PropertyRequest;
import org.egov.propertytax.config.PropertiesManager;
import org.egov.propertytax.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Consumer {

    @Autowired
	PropertiesManager propertiesManager;

    @Autowired
    private Producer producer;

    @Autowired
    private DemandService demandService;

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
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "boundary");
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
     * receive method
     *
     * @param PropertyRequest This method is listened whenever property is created and updated
     */
    @KafkaListener(topics = { "#{propertiesManager.getCreatePropertyTaxCalculated()}" })
    public void receive(ConsumerRecord<String, PropertyRequest> consumerRecord) throws Exception {
        log.info("consumer topic value is: " + consumerRecord.topic() + " consumer value is" + consumerRecord);
        demandService.createDemand(consumerRecord.value());
        log.info("demand generated >>>> \n next topic ----->> " + propertiesManager.getCreatePropertyTaxGenerated()
                + " \n Property >>>>> " + consumerRecord.value());
        producer.send(propertiesManager.getCreatePropertyTaxGenerated(), consumerRecord.value());

    }
}
