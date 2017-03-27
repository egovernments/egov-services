package org.egov.tracer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.egov.tracer.config.TracerProperties;
import org.egov.tracer.model.RequestContext;
import org.egov.tracer.model.RequestCorrelationId;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Slf4j
public class KafkaListenerLoggingAspect {

    private static final String RECEIVED_MESSAGE_WITH_BODY = "Received message from topics: {} with body {}";
    private static final String RECEIVED_MESSAGE = "Received message from topics: {}";
    private static final String PROCESSED_SUCCESS_MESSAGE = "Processed message successfully";
    private static final String EXCEPTION_MESSAGE = "Exception processing message";
    private static final String JOIN_DELIMITER = ", ";
    private ObjectMapper objectMapper;
    private TracerProperties tracerProperties;

    public KafkaListenerLoggingAspect(TracerProperties tracerProperties) {
        this.tracerProperties = tracerProperties;
        this.objectMapper = new ObjectMapper();
    }

    @Pointcut(value=" within(org.egov..*) && @annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void anyKafkaConsumer() {
    }

    @Around("anyKafkaConsumer() ")
    public Object logAction(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        KafkaListener myAnnotation = method.getAnnotation(KafkaListener.class);
        try {
            setCorrelationId(args);
            if (tracerProperties.isDetailedTracingEnabled()) {
                final String topics = getListeningTopics(myAnnotation);
                final String messageBodyAsString = getMessageBodyAsString(args);
                log.info(RECEIVED_MESSAGE_WITH_BODY, topics, messageBodyAsString);
            } else {
                final String topics = getListeningTopics(myAnnotation);
                log.info(RECEIVED_MESSAGE, topics);
            }

            final Object result = joinPoint.proceed();
            log.info(PROCESSED_SUCCESS_MESSAGE);
            return result;
        }
        catch (Exception e) {
            log.error(EXCEPTION_MESSAGE, e);
            throw e;
        }
    }

    private void setCorrelationId(Object[] args) {
        final String correlationId = getCorrelationId(args);
        RequestContext.setId(correlationId);
    }

    private String getCorrelationId(Object[] args) {
        @SuppressWarnings("unchecked")
        final HashMap<String, Object> requestMap = getMessageBody(args);
        final String correlationIdFromRequest = new RequestCorrelationId(requestMap).get();
        return  correlationIdFromRequest == null ? getRandomCorrelationId() : correlationIdFromRequest;
    }

    private String getRandomCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private String getMessageBodyAsString(Object[] args) throws JsonProcessingException {
        return objectMapper.writeValueAsString(getMessageBody(args));
    }

    private HashMap<String, Object> getMessageBody(Object[] args) {
        return Stream.of(args)
                .filter(parameterObject -> isNotAcknowledgmentParameter(parameterObject) && isNotString(parameterObject))
                .findFirst()
                .map(this::convertToMap)
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> convertToMap(Object parameterObject) {
        return (HashMap<String, Object>) objectMapper.convertValue(parameterObject, HashMap.class);
    }

    private boolean isNotString(Object o) {
        return !(o instanceof String);
    }

    private boolean isNotAcknowledgmentParameter(Object o) {
        return !(o instanceof Acknowledgment);
    }

    private String getListeningTopics(KafkaListener myAnnotation) {
        return Stream.of(myAnnotation.topics())
            .map(topic -> tracerProperties.getResolvedPropertyValue(topic))
            .collect(Collectors.joining(JOIN_DELIMITER));
    }
}
