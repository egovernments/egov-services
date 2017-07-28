package org.egov.propertyUser.userConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
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
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TitleTransferConsumer {
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
    public ConsumerFactory<String, TitleTransferRequest> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
                new JsonDeserializer<>(TitleTransferRequest.class));

    }

    /**
     * This bean will return kafka listner object based on consumer factory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TitleTransferRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TitleTransferRequest> factory = new ConcurrentKafkaListenerContainerFactory<String, TitleTransferRequest>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    /**
     * This method will listen property object from producer and check user authentication Updating auth token in
     * UserAuthResponseInfo Search user Create user
     */
    @KafkaListener(topics = { "#{propertiesManager.getCreatePropertyTitletransferValidator()}",
            "#{propertiesManager.getUpdatePropertyTitletransferValidator()}" })
    public void receive(ConsumerRecord<String, TitleTransferRequest> consumerRecord) throws Exception {
        log.info("consumer topic value is: " + consumerRecord.topic() + " consumer value is" + consumerRecord);
        TitleTransferRequest titleTransferRequest = consumerRecord.value();
        TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
        for (User user : titleTransfer.getNewOwners()) {
            user.setUserName(user.getMobileNumber());

            user = userUtil.getUserId(user, titleTransferRequest.getRequestInfo());

        }

        if (consumerRecord.topic()
                .equalsIgnoreCase(propertiesManager.getCreatePropertyTitletransferValidator()))
            producer.kafkaTemplate.send(
            		propertiesManager.getCreateTitletransferUserValidator(),
                    titleTransferRequest);

        else if (consumerRecord.topic()
                .equalsIgnoreCase(propertiesManager.getUpdatePropertyTitletransferValidator()))
            producer.kafkaTemplate.send(
            		propertiesManager.getUpdateTitletransferUserValidator(),
                    titleTransferRequest);
    }

}
