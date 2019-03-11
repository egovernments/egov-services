package org.egov.encryption.producer;

import com.fasterxml.jackson.databind.JsonNode;
import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    @Autowired
    private CustomKafkaTemplate<String, Object> kafkaTemplate;

    public void push(String topic, String key, JsonNode data) {
        kafkaTemplate.send(topic, key, data);
    }

}
