package org.egov.tracer.config;

import org.springframework.core.env.Environment;

public class TracerProperties {

    private static final String DETAILED_TRACING_FLAG = "tracer.detailed.tracing.enabled";
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

}
