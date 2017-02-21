package org.egov.pgr.employee.enrichment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.employee.enrichment.http.CorrelationIdAwareRestTemplate;
import org.egov.pgr.employee.enrichment.json.ObjectMapperFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PgrEmployeeEnrichmentApplication {

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
    public RestTemplate restTemplate() {
        return new CorrelationIdAwareRestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(PgrEmployeeEnrichmentApplication.class, args);
    }
}
