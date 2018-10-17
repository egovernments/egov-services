package org.egov.tracer.config;

import io.opentracing.noop.NoopTracerFactory;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.kafka.KafkaListenerLoggingAspect;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"org.egov.tracer"})
public class TracerConfiguration {

    @Bean
    public TracerProperties tracerProperties(Environment environment) {
        return new TracerProperties(environment);
    }

    @Bean
    public KafkaListenerLoggingAspect kafkaListenerLoggingAspect(TracerProperties tracerProperties,
                                                                 ObjectMapperFactory objectMapperFactory) {
        return new KafkaListenerLoggingAspect(tracerProperties, objectMapperFactory);
    }

    @Bean
    public LoggingListenerFactory loggingListenerFactory(TracerProperties tracerProperties,
                                                         ObjectMapperFactory objectMapperFactory) {
        return new LoggingListenerFactory(tracerProperties, objectMapperFactory);
    }

    @Bean
    public ObjectMapperFactory objectMapperFactory(TracerProperties tracerProperties) {
        return new ObjectMapperFactory(tracerProperties);
    }

    @Bean
    public LoggingKafkaTemplateFactory loggingKafkaTemplateFactory(TracerProperties tracerProperties,
                                                                   ObjectMapperFactory objectMapperFactory) {
        return new LoggingKafkaTemplateFactory(tracerProperties, objectMapperFactory);
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
            = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }

    @Bean(name = "logAwareRestTemplate")
    public LogAwareRestTemplate logAwareRestTemplate(TracerProperties tracerProperties) {
        return new LogAwareRestTemplate(getClientHttpRequestFactory() ,tracerProperties);
    }

    @Bean
    @Profile("monitoring")
    public io.opentracing.Tracer jaegerTracer() {
        return io.jaegertracing.Configuration.fromEnv()
            .getTracer();
    }

    @Bean
    @Profile("!monitoring")
    public io.opentracing.Tracer tracer() {
        return NoopTracerFactory.create();
    }

}


