package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ComplaintMessageQueueRepository {

    private KafkaTemplate<String, Object> kafkaTemplate;

    private String queueNameSuffix;

    @Autowired
    public ComplaintMessageQueueRepository(KafkaTemplate<String, Object> kafkaTemplate,
                                           @Value("${outgoing.queue.name.suffix}") String queueNameSuffix) {
        this.kafkaTemplate = kafkaTemplate;
        this.queueNameSuffix = queueNameSuffix;
    }

    public void save(SevaRequest sevaRequest, String jurisdictionId) {
        String topicName = queueNameSuffix;
        kafkaTemplate.send(topicName, sevaRequest);
    }
}
