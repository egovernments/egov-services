package org.egov.notificationConsumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.models.PropertyRequest;
import org.egov.notification.config.PropertiesManager;
import org.egov.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * This is Consumer class
 * 
 * @author Yosadhara
 *
 */
@Service
@Slf4j
public class Consumer {
    @Autowired
    PropertiesManager propertiesManager;

    @Autowired
    NotificationService notificationService;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    Producer producer;

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
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, propertiesManager.getGroupName());
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
     * This is receive method for consuming record from Kafka server
     * 
     * @param consumerRecord
     */
    @KafkaListener(topics = { "#{propertiesManager.getDemandAcknowledgement()}",
            "#{propertiesManager.getDemandApprove()}",
            "#{propertiesManager.getDemandTransferfee()}",
            "#{propertiesManager.getDemandReject()}",
            "#{propertiesManager.getPropertyAcknowledgement()}",
            "#{propertiesManager.getApproveProperty()}",
            "#{propertiesManager.getRejectProperty()}",
            "#{propertiesManager.getRevisionPetitionAcknowledgement()}",
            "#{propertiesManager.getRevisionPetitionHearing()}",
            "#{propertiesManager.getRevisionPetitionEndorsement()}",
            "#{propertiesManager.getPropertyAlterationAcknowledgement()}",
            "#{propertiesManager.getApprovePropertyAlteration()}",
            "#{propertiesManager.getRejectPropertyAlteration()}" })
    public void receive(ConsumerRecord<String, PropertyRequest> consumerRecord) {
        log.info("consumer topic value is: " + consumerRecord.topic() + " consumer value is" + consumerRecord);
        if (consumerRecord.topic()
                .equalsIgnoreCase(propertiesManager.getDemandAcknowledgement())) {

            notificationService.demandAcknowledgement(consumerRecord.value());
        } else if (consumerRecord.topic()
                .equalsIgnoreCase(propertiesManager.getDemandApprove())) {

            notificationService.demandApprove(consumerRecord.value());
        } else if (consumerRecord.topic()
                .equalsIgnoreCase(propertiesManager.getDemandTransferfee())) {

            notificationService.demandTransferFee(consumerRecord.value());
        } else if (consumerRecord.topic()
                .equalsIgnoreCase(propertiesManager.getDemandReject())) {

            notificationService.demandReject(consumerRecord.value());
        } else if (consumerRecord.topic().equalsIgnoreCase(
        		propertiesManager.getPropertyAcknowledgement())) {

            notificationService.propertyAcknowledgement(consumerRecord.value().getProperties());
        } else if (consumerRecord.topic()
                .equalsIgnoreCase(propertiesManager.getApproveProperty())) {

            notificationService.propertyApprove(consumerRecord.value().getProperties());
        } else if (consumerRecord.topic()
                .equalsIgnoreCase(propertiesManager.getRejectProperty())) {

            notificationService.propertyReject(consumerRecord.value().getProperties());
        } else if (consumerRecord.topic().equalsIgnoreCase(
        		propertiesManager.getRevisionPetitionAcknowledgement())) {

            notificationService.revisionPetitionAcknowldgement(consumerRecord.value().getProperties());
        } else if (consumerRecord.topic().equalsIgnoreCase(
        		propertiesManager.getRevisionPetitionHearing())) {

            notificationService.revisionPetitionHearing(consumerRecord.value().getProperties());
        } else if (consumerRecord.topic().equalsIgnoreCase(
        		propertiesManager.getRevisionPetitionEndorsement())) {

            notificationService.revisionPetitionEndorsement(consumerRecord.value().getProperties());
        }  else if (consumerRecord.topic().equalsIgnoreCase(
        		propertiesManager.getPropertyAlterationAcknowledgement())) {
			
			notificationService.alterationAcknowledgement(consumerRecord.value().getProperties());
		} else if (consumerRecord.topic().equalsIgnoreCase(
				propertiesManager.getApprovePropertyAlteration())) {
			
			notificationService.approvePropertyAlteration(consumerRecord.value().getProperties());
		} else if (consumerRecord.topic().equalsIgnoreCase(
				propertiesManager.getRejectPropertyAlteration())) {
			
			notificationService.rejectPropertyAlteration(consumerRecord.value().getProperties());
		}
    }
}
