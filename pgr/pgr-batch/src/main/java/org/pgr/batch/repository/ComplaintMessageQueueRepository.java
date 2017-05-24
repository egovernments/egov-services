package org.pgr.batch.repository;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.pgr.batch.service.model.SevaRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ComplaintMessageQueueRepository {

    private String topicName;
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    public ComplaintMessageQueueRepository(@Value("${kafka.topics.escalation.update.name}")
                                               String topicName,
                                           LogAwareKafkaTemplate<String, Object> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void save(SevaRequest sevaRequest) {
        kafkaTemplate.send(topicName, sevaRequest);
    }
}
