package org.egov;

import java.net.UnknownHostException;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Import({ TracerConfiguration.class })
@SpringBootApplication
public class CollectionReceiptVoucherConsumerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CollectionReceiptVoucherConsumerApplication.class, args);
    }

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void init() throws UnknownHostException {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
