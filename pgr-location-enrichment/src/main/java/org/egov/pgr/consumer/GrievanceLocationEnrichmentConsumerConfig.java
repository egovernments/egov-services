package org.egov.pgr.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.egov.pgr.config.PropertiesManager;
import org.egov.pgr.model.SevaRequest;
import org.egov.pgr.producer.GrievanceAssignmentProducer;
import org.egov.pgr.services.BoundaryService;
import org.egov.pgr.services.CrossHierarchyService;
import org.egov.pgr.contract.SevaRequestDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class GrievanceLocationEnrichmentConsumerConfig {

    @Value("${kafka.config.bootstrap_server_config}")
    private String serverConfig;

    @Value("${kafka.consumer.config.auto_commit}")
    private Boolean enableAutoCommit;

    @Value("${kafka.consumer.config.auto_commit_interval}")
    private String autoCommitInterval;

    @Value("${kafka.consumer.config.session_timeout}")
    private String sessionTimeout;

    @Value("${kafka.consumer.config.group_id}")
    private String groupId;

    @Value("${kafka.consumer.config.auto_offset_reset}")
    private String autoOffsetReset;

    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private CrossHierarchyService crossHierarchyService;
    @Autowired
    private GrievanceAssignmentProducer kafkaProducer;
    @Autowired
    private PropertiesManager propertiesManager;


    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, SevaRequest>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SevaRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        //TODO - Tweak params with some real values.
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, SevaRequest> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        //TODO - Load configs from env vars
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverConfig);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SevaRequestDeserializer.class);
        return propsMap;
    }

    @Bean
    public GrievanceLocationEnrichmentListener listener() {
        return new GrievanceLocationEnrichmentListener(boundaryService, crossHierarchyService, kafkaProducer, propertiesManager);
    }

}
