package org.egov.tracer.config;

import org.egov.tracer.kafka.LogAwareProducerListener;
import org.springframework.kafka.support.ProducerListener;

public class LoggingListenerFactory {

    private TracerProperties tracerProperties;

    public LoggingListenerFactory(TracerProperties tracerProperties) {
        this.tracerProperties = tracerProperties;
    }

    public <K, V> ProducerListener<K, V> build() {
        return new LogAwareProducerListener<>(tracerProperties);
    }
}

