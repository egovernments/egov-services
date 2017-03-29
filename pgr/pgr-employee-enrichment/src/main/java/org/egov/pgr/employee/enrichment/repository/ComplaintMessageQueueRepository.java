package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ComplaintMessageQueueRepository {

    private String topicName;
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    public ComplaintMessageQueueRepository(@Value("${kafka.topics.pgr.employee_enriched.name}")
                                               String topicName,
                                           LogAwareKafkaTemplate<String, Object> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void save(SevaRequest sevaRequest) {
        kafkaTemplate.send(topicName, sevaRequest.getRequestMap());
    }
}
