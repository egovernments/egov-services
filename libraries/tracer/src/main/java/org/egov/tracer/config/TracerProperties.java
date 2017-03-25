package org.egov.tracer.config;

import org.springframework.core.env.Environment;

public class TracerProperties {

    private static final String DETAILED_TRACING_FLAG = "org.egov.detailed.tracing.enabled";
    private Environment environment;

    public TracerProperties(Environment environment) {
        this.environment = environment;
    }

    public boolean isDetailedTracingEnabled() {
        return environment.getProperty(DETAILED_TRACING_FLAG, Boolean.class, true);
    }

    public boolean isDetailedTracingDisabled() {
        return !isDetailedTracingEnabled();
    }
}
