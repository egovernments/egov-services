package org.egov.pgr.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ComplaintRepository {

    private String topicName;
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public ComplaintRepository(@Value("${kafka.topics.pgr.boundary_enriched.name}") String topicName,
                               KafkaTemplate<String, Object> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void save(Object message) {
        kafkaTemplate.send(topicName, message);
    }
}
