package org.egov.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class LogAwareKafkaTemplate<T,Q> {

    private static final String PRE_SEND_MESSAGE = "Sending message to topic: {} with body {}";
    private static final String SEND_FAILURE_MESSAGE = "Sending of message failed";
    private static final String SEND_SUCCESS_MESSAGE = "Sending of message to topic: {} is successful";
    private static final String JSON_SERIALIZATION_FAILURE_MESSAGE = "Serializing of message failed";
    private KafkaTemplate<T, Q> kafkaTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public LogAwareKafkaTemplate(KafkaTemplate<T, Q> kafkaTemplate,
                                 ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public SendResult<T, Q> send(String topicName, Q message) {
        try {
            if (log.isInfoEnabled()) {
                log.info(PRE_SEND_MESSAGE, topicName, objectMapper.writeValueAsString(message));
            }
            final SendResult<T, Q> result = kafkaTemplate.send(topicName, message).get();
            log.info(SEND_SUCCESS_MESSAGE, topicName);
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error(SEND_FAILURE_MESSAGE, e);
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            log.error(JSON_SERIALIZATION_FAILURE_MESSAGE, e);
            throw new RuntimeException(e);
        }
    }

}
