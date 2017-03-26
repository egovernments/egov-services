package org.egov.pgr.producer;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrievanceProducer {

    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public GrievanceProducer(LogAwareKafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}
