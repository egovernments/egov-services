package org.egov.tracer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.egov.tracer.config.ObjectMapperFactory;
import org.egov.tracer.config.TracerProperties;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.util.ObjectUtils;

@Slf4j
public class LogAwareProducerListener<K, V> implements ProducerListener<K, V> {

    private static final String EMPTY_BODY = "<EMPTY BODY>";
    private final ObjectMapper objectMapper;
    private static final String SEND_SUCCESS_MESSAGE =
        "Sending of message to topic: {}, partition: {} with key: {} succeeded.";
    private static final String BODY_JSON_SERIALIZATION_ERROR = "Serialization of body failed";
    private static final String SEND_SUCCESS_MESSAGE_WITH_BODY =
        "Sending of message to topic: {}, partition: {}, body: {} with key: {} succeeded.";
    private static final String SEND_FAILURE_MESSAGE =
        "Sending of message to topic: %s, partition: %s with key: %s failed.";
    private static final String SEND_FAILURE_MESSAGE_WITH_BODY =
        "Sending of message to topic: %s, partition: %s, body: %s with key: %s failed.";
    private TracerProperties tracerProperties;

    public LogAwareProducerListener(TracerProperties tracerProperties,
                                    ObjectMapperFactory objectMapperFactory) {
        this.tracerProperties = tracerProperties;
        this.objectMapper = objectMapperFactory.getObjectMapper();
    }

    @Override
    public void onSuccess(String topic, Integer partition, K key, V value, RecordMetadata recordMetadata) {
        final String keyAsString = ObjectUtils.nullSafeToString(key);
        if (tracerProperties.isDetailedTracingEnabled()) {
            final String bodyAsJsonString = getMessageBodyAsJsonString(value);
            log.info(SEND_SUCCESS_MESSAGE_WITH_BODY, topic, partition, bodyAsJsonString, keyAsString);
        } else {
            log.info(SEND_SUCCESS_MESSAGE, topic, partition, keyAsString);
        }
    }

    @Override
    public void onError(String topic, Integer partition, K key, V value, Exception exception) {
        final String keyAsString = ObjectUtils.nullSafeToString(key);
        if (tracerProperties.isDetailedTracingEnabled()) {
            final String bodyAsJsonString = getMessageBodyAsJsonString(value);
            final String message =
                String.format(SEND_FAILURE_MESSAGE_WITH_BODY, topic, partition, bodyAsJsonString, keyAsString);
            log.error(message, exception);
        } else {
            final String message = String.format(SEND_FAILURE_MESSAGE, topic, partition, keyAsString);
            log.error(message, exception);
        }
    }

    @Override
    public boolean isInterestedInSuccess() {
        return true;
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
