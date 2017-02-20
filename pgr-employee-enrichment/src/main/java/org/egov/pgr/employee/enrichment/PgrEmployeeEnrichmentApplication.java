package org.egov.pgr.employee.enrichment;

import org.egov.pgr.employee.enrichment.http.CorrelationIdAwareRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PgrEmployeeEnrichmentApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new CorrelationIdAwareRestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(PgrEmployeeEnrichmentApplication.class, args);
    }
}
