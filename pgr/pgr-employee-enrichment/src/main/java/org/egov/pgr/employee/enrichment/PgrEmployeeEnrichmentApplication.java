package org.egov.pgr.employee.enrichment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.employee.enrichment.json.ObjectMapperFactory;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.TimeZone;

@SpringBootApplication
@Import(TracerConfiguration.class)
public class PgrEmployeeEnrichmentApplication {
    private static final String IST = "Asia/Calcutta";

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

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(IST));
        SpringApplication.run(PgrEmployeeEnrichmentApplication.class, args);
    }
}
