package org.egov.tracer.config;

import org.egov.tracer.http.CorrelationIdFilter;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.http.UnhandledExceptionControllerAdvice;
import org.egov.tracer.kafka.KafkaListenerLoggingAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"org.egov.tracer"})
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

    @Bean
    public UnhandledExceptionControllerAdvice unhandledExceptionControllerAdvice() {
        return new UnhandledExceptionControllerAdvice();
    }

    @Bean
    @ConditionalOnProperty(name = "org.egov.correlation.body.filter.disabled",
        havingValue = "false", matchIfMissing = true)
    public FilterRegistrationBean correlationIdFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorrelationIdFilter());
        registration.addUrlPatterns("/*");
        registration.setName("correlationIdFilter");
        registration.setOrder(1);
        return registration;
    }

}


