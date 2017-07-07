package org.egov.eis;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.eis.indexer.http.CorrelationIdAwareRestTemplate;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@Import(TracerConfiguration.class)
public class HREmployeeIndexerApplication {
    
    @Value("${app.timezone}")
    private String timeZone;
    
    @PostConstruct
    public void initialize() {
            TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        return objectMapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
        return converter;
    }

    public static void main(String[] args) {
        SpringApplication.run(HREmployeeIndexerApplication.class, args);
    }

}