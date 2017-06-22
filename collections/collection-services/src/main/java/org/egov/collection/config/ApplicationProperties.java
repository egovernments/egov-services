package org.egov.collection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

@Configuration
@PropertySource(value = { "classpath:config/application-config.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class ApplicationProperties {
	
    
    @Value("${kafka.topics.receipt.create.name}")
    private String createReceiptTopicName;
    
    @Value("${kafka.topics.receipt.create.key}")
    private String createReceiptTopicKey;

	public String getCreateReceiptTopicName() {
		return createReceiptTopicName;
	}

	public String getCreateReceiptTopicKey() {
		return createReceiptTopicKey;
	}
    


}
