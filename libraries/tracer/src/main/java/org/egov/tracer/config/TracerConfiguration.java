package org.egov.tracer.config;

import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.kafka.KafkaListenerLoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class TracerConfiguration {

    @Bean
    public TracerProperties tracerProperties(Environment environment) {
        return new TracerProperties(environment);
    }

    @Bean
    public KafkaListenerLoggingAspect kafkaListenerLoggingAspect(TracerProperties tracerProperties) {
        return new KafkaListenerLoggingAspect(tracerProperties);
    }

    @Bean
    public LoggingListenerFactory loggingListenerFactory(TracerProperties tracerProperties) {
        return new LoggingListenerFactory(tracerProperties);
    }

    @Bean(name = "logAwareRestTemplate")
    public LogAwareRestTemplate logAwareRestTemplate(TracerProperties tracerProperties) {
        return new LogAwareRestTemplate(tracerProperties);
    }

}


