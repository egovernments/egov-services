package org.egov.tracer.config;

import org.springframework.core.env.Environment;

public class TracerProperties {

    private static final String DETAILED_TRACING_FLAG = "tracer.detailed.tracing.enabled";
    private static final String PROPERTY_PLACEHOLDER_PREFIX = "${";
    private static final String PROPERTY_PLACEHOLDER_SUFFIX = "}";
    private static final String REPLACEMENT_STRING = "";
    private static final String UTC = "UTC";
    private static final String TIME_ZONE_PROPERTY = "app.timezone";

    private Environment environment;

    public TracerProperties(Environment environment) {
        this.environment = environment;
    }

    public boolean isDetailedTracingEnabled() {
        return environment.getProperty(DETAILED_TRACING_FLAG, Boolean.class, true);
    }

    public String getTimeZone() {
        return environment.getProperty(TIME_ZONE_PROPERTY, UTC);
    }

    public boolean isDetailedTracingDisabled() {
        return !isDetailedTracingEnabled();
    }

    public String getResolvedPropertyValue(String propertyPlaceholder) {
        final String propertyName = propertyPlaceholder
            .replace(PROPERTY_PLACEHOLDER_PREFIX, REPLACEMENT_STRING)
            .replace(PROPERTY_PLACEHOLDER_SUFFIX, REPLACEMENT_STRING);
        return this.environment.getProperty(propertyName);
    }
}
