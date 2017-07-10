package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.write.consumer.contracts.request.SevaRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IntegrationMessageQueueRepository {

    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private String topicName;

    @Autowired
    public IntegrationMessageQueueRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
                                             @Value("${external.queue.name}") String topicName){
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void save(SevaRequest sevaRequest) {
        kafkaTemplate.send(topicName, sevaRequest);
    }
}
