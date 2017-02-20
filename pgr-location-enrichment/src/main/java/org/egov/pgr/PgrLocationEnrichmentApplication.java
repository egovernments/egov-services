package org.egov.pgr;

import org.egov.pgr.interceptor.CorrelationIdAwareRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PgrLocationEnrichmentApplication {

    @Bean
    public RestTemplate getRestTemplate() {
        return new CorrelationIdAwareRestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(PgrLocationEnrichmentApplication.class, args);
    }
}
