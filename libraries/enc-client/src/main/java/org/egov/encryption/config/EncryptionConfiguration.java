package org.egov.encryption.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"org.egov.encryption"})
@PropertySource("classpath:enc.properties")
public class EncryptionConfiguration {


}
