package org.egov.tracer.kafka;

import org.apache.kafka.common.errors.SerializationException;
import org.egov.tracer.model.ErrorQueueContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ErrorQueueProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value(("${error.queue.topic:egov.error}"))
    private String topic;

    public void sendMessage(ErrorQueueContract errorQueueContract) {
        try {
            kafkaTemplate.send(topic, errorQueueContract);
        } catch (SerializationException serializationException) {
            log.info("SerializationException exception occured while sending exception to error queue");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                kafkaTemplate.send(topic, objectMapper.writeValueAsString(errorQueueContract));
            } catch (JsonProcessingException e) {
                log.info("exception occured while converting ErrorQueueContract to json string");
                e.printStackTrace();
            }
        } catch (Exception ex) {
            log.info("exception occured while sending exception to error queue");
            ex.printStackTrace();
        }
    }

}
