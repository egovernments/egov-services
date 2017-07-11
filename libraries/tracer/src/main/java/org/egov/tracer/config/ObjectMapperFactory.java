package org.egov.tracer.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.TimeZone;

public class ObjectMapperFactory {

    private TracerProperties tracerProperties;

    public ObjectMapperFactory(TracerProperties tracerProperties) {
        this.tracerProperties = tracerProperties;
    }

    public ObjectMapper create() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(this.tracerProperties.getTimeZone()));
        return objectMapper;
    }
}
