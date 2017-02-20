package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ComplaintRepository {

    private String topicName;
    private KafkaTemplate<String, Object> kafkaTemplate;

    public ComplaintRepository(@Value("${kafka.topics.pgr.employee_enriched.name}")
                                      String topicName,
                               KafkaTemplate<String, Object> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void save(SevaRequest sevaRequest) {
        kafkaTemplate.send(topicName, sevaRequest.getRequestMap());
    }
}
