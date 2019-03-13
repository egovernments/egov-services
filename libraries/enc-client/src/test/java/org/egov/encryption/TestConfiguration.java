package org.egov.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfiguration {

    @Bean
    public EncryptionService encryptionService() {
        return new EncryptionService();
    }

}
