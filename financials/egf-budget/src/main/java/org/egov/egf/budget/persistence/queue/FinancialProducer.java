package org.egov.egf.budget.persistence.queue;

import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinancialProducer {

    private final LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public FinancialProducer(final LogAwareKafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(final String topic, final String key, final Map<String, Object> message) {
        kafkaTemplate.send(topic, key, message);
    }

}
