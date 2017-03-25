package org.egov.tracer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.config.TracerProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class LogAwareKafkaTemplate<T,Q> {

    private static final String EMPTY_BODY = "<EMPTY BODY>";
    private static final String SEND_SUCCESS_MESSAGE =
        "Sending of message to topic: {}, partition: {} with key: {} succeeded.";
    private static final String BODY_JSON_SERIALIZATION_ERROR = "Serialization of body failed";
    private static final String SEND_SUCCESS_MESSAGE_WITH_BODY =
        "Sending of message to topic: {}, partition: {}, body: {} with key: {} succeeded.";
    private static final String SEND_FAILURE_MESSAGE_WITH_TOPIC =
        "Sending of message to topic: %s failed.";
    private static final String SEND_FAILURE_MESSAGE_WITH_TOPIC_KEY =
        "Sending of message to topic: %s, key: %s failed.";
    private static final String SEND_FAILURE_MESSAGE_WITH_TOPIC_KEY_PARTITION =
        "Sending of message to topic: %s, partition: %s, key: %s failed.";
    private TracerProperties tracerProperties;
    private KafkaTemplate<T, Q> kafkaTemplate;
    private ObjectMapper objectMapper;

    public LogAwareKafkaTemplate(TracerProperties tracerProperties,
                                 KafkaTemplate<T, Q> kafkaTemplate) {
        this.tracerProperties = tracerProperties;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public SendResult<T, Q> send(String topic, Q value) {
        try {
            final SendResult<T, Q> result = kafkaTemplate.send(topic, value).get();
            logSuccessMessage(value, result);
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error(String.format(SEND_FAILURE_MESSAGE_WITH_TOPIC, topic), e);
            throw new RuntimeException(e);
        }
    }

    public SendResult<T, Q> send(String topic, T key, Q value) {
        try {
            final SendResult<T, Q> result = kafkaTemplate.send(topic, key, value).get();
            logSuccessMessage(value, result);
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error(String.format(SEND_FAILURE_MESSAGE_WITH_TOPIC_KEY, topic, key), e);
            throw new RuntimeException(e);
        }
    }

    public SendResult<T, Q> send(String topic, T key, int partition, Q value) {
        try {
            final SendResult<T, Q> result = kafkaTemplate.send(topic, partition, key, value).get();
            logSuccessMessage(value, result);
            return result;
        } catch (InterruptedException | ExecutionException e) {
            log.error(String.format(SEND_FAILURE_MESSAGE_WITH_TOPIC_KEY_PARTITION, topic, key, partition), e);
            throw new RuntimeException(e);
        }
    }

    private void logSuccessMessage(Q message, SendResult<T, Q> result) {
        final String topic = result.getProducerRecord().topic();
        final Integer partition = result.getProducerRecord().partition();
        final String key = ObjectUtils.nullSafeToString(result.getProducerRecord().key());
        if (tracerProperties.isDetailedTracingEnabled()) {
			final String bodyAsJsonString = getMessageBodyAsJsonString(message);
			log.info(SEND_SUCCESS_MESSAGE_WITH_BODY, topic, partition, bodyAsJsonString, key);
		} else {
			log.info(SEND_SUCCESS_MESSAGE, topic, partition, key);
		}
    }

    private String getMessageBodyAsJsonString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error(BODY_JSON_SERIALIZATION_ERROR);
            return EMPTY_BODY;
        }
    }

}