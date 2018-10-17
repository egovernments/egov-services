package org.egov.tracer.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.TimeZone;

public class ObjectMapperFactory {

    private TracerProperties tracerProperties;

    private ObjectMapper objectMapper;

    public ObjectMapperFactory(TracerProperties tracerProperties) {
        this.tracerProperties = tracerProperties;
        this.objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(this.tracerProperties.getTimeZone()));
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
