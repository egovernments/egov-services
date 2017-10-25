package org.egov.works.services.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;

@Configuration
@Data
@ToString
public class PropertiesManager {
	
    @Value("${kafka.topics.works.estimateappropriation.create.name}")
    private String estimateAppropriationsCreateTopic;

    @Value("${kafka.topics.works.estimateappropriation.update.name}")
    private String estimateAppropriationsUpdateTopic;

}
