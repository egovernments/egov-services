package org.egov.lams.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class PropertiesManager {
	
	 @Value("${kafka.topics.save.agreement}")
	 private String kafkaSaveAgreementTopic;
	
	 @Value("${kafka.topics.save.agreement}")
	 private String kafkaUpdateAgreementTopic; 

}
