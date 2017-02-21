package org.egov.pgr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.http.CorrelationIdAwareRestTemplate;
import org.egov.pgr.json.ObjectMapperFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PgrLocationEnrichmentApplication {

    @Bean
    public ObjectMapper getObjectMapper() {
        return ObjectMapperFactory.create();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new CorrelationIdAwareRestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(PgrLocationEnrichmentApplication.class, args);
    }
}
