package org.egov.tracer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.egov.tracer.config.ObjectMapperFactory;
import org.egov.tracer.config.TracerProperties;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.stream.Stream;

@Aspect
@Slf4j
public class KafkaListenerLoggingAspect {

    private static final String RECEIVED_MESSAGE_WITH_BODY = "Received message from topic: {} with body {}";
    private static final String RECEIVED_MESSAGE = "Received message from topic: {}";
    private static final String PROCESSED_SUCCESS_MESSAGE = "Processed message successfully";
    private static final String EXCEPTION_MESSAGE = "Exception processing message";
    private static final String NOT_AVAILABLE = "<NOT-AVAILABLE>";
    private static final String CONVERSION_FAILED_MESSAGE = "Failed to convert String to HashMap";
    private ObjectMapper objectMapper;
    private TracerProperties tracerProperties;

    public KafkaListenerLoggingAspect(TracerProperties tracerProperties,
                                      ObjectMapperFactory objectMapperFactory) {
        this.tracerProperties = tracerProperties;
        this.objectMapper = objectMapperFactory.getObjectMapper();
    }

    @Pointcut(value = " within(org.egov..*) && @annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void anyKafkaConsumer() {
    }

    @Around("anyKafkaConsumer() ")
    public Object logAction(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        try {
            final String receivedTopicName = getParameterWithTopicAnnotation(parameterAnnotations, args);
            if (tracerProperties.isDetailedTracingEnabled()) {
                final String messageBodyAsString = getMessageBodyAsString(args, parameterAnnotations);
                log.debug(RECEIVED_MESSAGE_WITH_BODY, receivedTopicName, messageBodyAsString);
            } else {
                log.debug(RECEIVED_MESSAGE, receivedTopicName);
            }

            final Object result = joinPoint.proceed();
            log.debug(PROCESSED_SUCCESS_MESSAGE);
            return result;
        } catch (Exception e) {
            log.error(EXCEPTION_MESSAGE, e);
            throw e;
        }
    }

    private String getMessageBodyAsString(Object[] args, Annotation[][] parameterAnnotations)
        throws JsonProcessingException {
        final Object parameterWithPayloadAnnotation = getParameterWithPayloadAnnotation(parameterAnnotations, args);
        if (parameterWithPayloadAnnotation != null) {
            if(parameterWithPayloadAnnotation instanceof String) {
                return (String) parameterWithPayloadAnnotation;
            }
            return objectMapper.writeValueAsString(parameterWithPayloadAnnotation);
        }
        return NOT_AVAILABLE;
    }

    private Object getParameterWithPayloadAnnotation(Annotation[][] parameterAnnotations, Object[] args) {
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            final boolean isPayloadAnnotationPresent = isPayloadAnnotationPresent(annotations);
            if(isPayloadAnnotationPresent) return args[i];
        }
        return null;
    }

    private boolean isPayloadAnnotationPresent(Annotation[] annotations) {
        if (annotations == null) {
            return false;
        }
        return Stream.of(annotations)
            .anyMatch(annotation -> annotation.annotationType().getAnnotationsByType(Payload.class) != null);
    }

    private String getParameterWithTopicAnnotation(Annotation[][] parameterAnnotations, Object[] args) {
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            if(isTopicAnnotationPresent(annotations)) {
                return (String) args[i];
            }
        }
        return NOT_AVAILABLE;
    }

    private boolean isTopicAnnotationPresent(Annotation[] annotations) {
        if (annotations == null) {
            return false;
        }
        return Stream.of(annotations)
            .anyMatch(this::isTopicNameHeaderAnnotationPresent);
    }

    private boolean isTopicNameHeaderAnnotationPresent(Annotation annotation) {
        return annotation instanceof Header && KafkaHeaders.RECEIVED_TOPIC.equals(((Header)annotation).value());
    }

    private HashMap<String, Object> getMessageBody(Annotation[][] parameterAnnotations, Object[] args) {
        final Object parameterWithPayloadAnnotation = getParameterWithPayloadAnnotation(parameterAnnotations, args);
        if (parameterWithPayloadAnnotation != null) {
            return convertToMap(parameterWithPayloadAnnotation);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> convertToMap(Object parameterObject) {
        if(parameterObject instanceof String ) {
            try {
                return (HashMap<String, Object>) objectMapper.readValue((String) parameterObject, HashMap.class);
            } catch (IOException e) {
                log.error(CONVERSION_FAILED_MESSAGE, e);
                return null;
            }
        } else{
            return (HashMap<String, Object>) objectMapper.convertValue(parameterObject, HashMap.class);
        }
    }

}
