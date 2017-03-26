package org.egov.pgr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgr.json.ObjectMapperFactory;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@SpringBootApplication
@Import({TracerConfiguration.class})
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

    public static void main(String[] args) {
        SpringApplication.run(PgrLocationEnrichmentApplication.class, args);
    }
}
