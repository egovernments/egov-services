package org.egov.poc.repository;

import org.egov.poc.contract.ThirdPartyRecord;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ThirdPartyRepository {

    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    private String topicName;

    @Autowired
    public ThirdPartyRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
                                             @Value("${pgr.ougoing.queue.name}") String topicName){
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void save(ThirdPartyRecord record) {
        kafkaTemplate.send(topicName, record);
    }
}
