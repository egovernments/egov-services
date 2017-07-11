package org.egov.tracer.config;

import org.egov.tracer.kafka.LogAwareProducerListener;
import org.springframework.kafka.support.ProducerListener;

public class LoggingListenerFactory {

    private TracerProperties tracerProperties;
    private ObjectMapperFactory objectMapperFactory;

    public LoggingListenerFactory(TracerProperties tracerProperties,
                                  ObjectMapperFactory objectMapperFactory) {
        this.tracerProperties = tracerProperties;
        this.objectMapperFactory = objectMapperFactory;
    }

    public <K, V> ProducerListener<K, V> build() {
        return new LogAwareProducerListener<>(tracerProperties, objectMapperFactory);
    }
}

