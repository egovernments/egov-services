package org.egov.pgr.persistence.repository;


import org.egov.pgr.domain.model.ServiceTypeConfiguration;
import org.egov.pgr.producers.PGRProducer;
import org.springframework.beans.factory.annotation.Value;

public class ServiceTypeConfigurationMessageQueueRepository {

    private PGRProducer producer;

    private String topicName;

    private String key;

    public ServiceTypeConfigurationMessageQueueRepository(PGRProducer producer,
                                                          @Value("${kafka.topics.servicetypeconfiguration.create.name}") String topicName,
                                                          @Value("${kafka.topics.servicetypeconfiguration.create.key}") String key) {
        this.producer = producer;
        this.topicName = topicName;
        this.key = key;
    }

    public void save(ServiceTypeConfiguration serviceTypeConfiguration){

    }
}
