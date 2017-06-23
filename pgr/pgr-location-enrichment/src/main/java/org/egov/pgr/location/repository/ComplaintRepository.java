package org.egov.pgr.location.repository;

import org.egov.pgr.location.model.SevaRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ComplaintRepository {

    private String topicName;
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public ComplaintRepository(@Value("${kafka.topics.pgr.boundary_enriched.name}") String topicName,
                               LogAwareKafkaTemplate<String, Object> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void save(SevaRequest sevaRequest) {
        kafkaTemplate.send(topicName, sevaRequest.getRequestMap());
    }
}
