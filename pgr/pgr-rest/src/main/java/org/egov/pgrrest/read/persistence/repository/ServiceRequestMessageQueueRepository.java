package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.common.contract.web.SevaRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceRequestMessageQueueRepository {

    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private String topicName;

    @Autowired
    public ServiceRequestMessageQueueRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
                                                @Value("${outgoing.queue.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void save(SevaRequest sevaRequest) {
        kafkaTemplate.send(topicName, sevaRequest);
    }
}
