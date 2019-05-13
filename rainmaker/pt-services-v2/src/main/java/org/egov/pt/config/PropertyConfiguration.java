package org.egov.pt.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.egov.tracer.kafka.deserializer.HashMapDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.kafka.listener.AbstractMessageListenerContainer.AckMode;

@Import({TracerConfiguration.class})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class PropertyConfiguration {

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
    @Autowired
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper);
    return converter;
    }



    @Configuration
    public class KafkaConfig {

        @Autowired
        private KafkaProperties kafkaProperties;

        @Bean
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>(
                    kafkaProperties.buildConsumerProperties()
            );
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    HashMapDeserializer.class);
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 50);
            props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 10000);

            return props;
        }

        @Bean
        public ConsumerFactory<String, Object> consumerFactory() {
            return new DefaultKafkaConsumerFactory<>(consumerConfigs());
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            factory.setBatchListener(true);
            factory.getContainerProperties().setAckMode(AckMode.BATCH);

            return factory;
        }

    }




    //PERSISTER
    @Value("${persister.save.property.topic}")
    private String savePropertyTopic;

    @Value("${persister.update.property.topic}")
    private String updatePropertyTopic;
    
    @Value("${persister.save.drafts.topic}")
    private String saveDraftsTopic;

    @Value("${persister.update.drafts.topic}")
    private String updateDraftsTopic;

    @Value("${persister.demand.based.topic}")
    private String demandBasedPTTopic;

    @Value("${persister.demand.based.dead.letter.topic.batch}")
    private String deadLetterTopicBatch;

    @Value("${persister.demand.based.dead.letter.topic.single}")
    private String deadLetterTopicSingle;



    //IDGEN
    @Value("${egov.idgen.ack.name}")
    private String acknowldgementIdGenName;

    @Value("${egov.idgen.ack.format}")
    private String acknowldgementIdGenFormat;

    @Value("${egov.idgen.assm.name}")
    private String assessmentIdGenName;

    @Value("${egov.idgen.assm.format}")
    private String assessmentIdGenFormat;

    @Value("${egov.idgen.ptid.name}")
    private String propertyIdGenName;

    @Value("${egov.idgen.ptid.format}")
    private String propertyIdGenFormat;


    //NOTIFICATION TOPICS
    @Value("${kafka.topics.notification.sms}")
    private String smsNotifTopic;

    @Value("${kafka.topics.notification.fullpayment}")
    private String receiptTopic;

    @Value("${kafka.topics.notification.pg.save.txns}")
    private String pgTopic;

    @Value("${egov.localization.statelevel}")
    private Boolean isStateLevel;

    @Value("${notification.sms.enabled}")
    private Boolean isSMSNotificationEnabled;


    //Property Search Params
    @Value("${citizen.allowed.search.params}")
    private String citizenSearchParams;

    @Value("${employee.allowed.search.params}")
    private String employeeSearchParams;

    @Value("${notification.url}")
    private String notificationURL;
    
    @Value("${pt.search.pagination.default.limit}")
    private Long defaultLimit;

    @Value("${pt.search.pagination.default.offset}")
    private Long defaultOffset;
    
    @Value("${pt.search.pagination.max.search.limit}")
    private Long maxSearchLimit;

    //Localization
    @Value("${egov.localization.host}")
    private String localizationHost;

    @Value("${egov.localization.context.path}")
    private String localizationContextPath;

    @Value("${egov.localization.search.endpoint}")
    private String localizationSearchEndpoint;


    // Demand based System
    @Value("${egov.pt.demand.based.searcher.host}")
    private String demandBasedSearcherHost;

    @Value("${egov.pt.demand.based.searcher.endpoint}")
    private String demandBasedSearcherEndpoint;


}