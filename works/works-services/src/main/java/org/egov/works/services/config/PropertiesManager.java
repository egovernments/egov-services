package org.egov.works.services.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;

@Configuration
@Data
@ToString
public class PropertiesManager {
	
    @Value("${kafka.topics.works.estimateappropriation.create}")
    private String estimateAppropriationsCreateTopic;

    @Value("${kafka.topics.works.estimateappropriation.update}")
    private String estimateAppropriationsUpdateTopic;

    @Value("${egov.services.works-services.pageSize.default}")
    private String worksSearchPageSizeDefault;

    @Value("${kafka.topics.works.documentdetails.create.name}")
    private String documentDetailsCreateTopic;

    @Value("${kafka.topics.works.documentdetails.update.name}")
    private String documentDetailsUpdateTopic;
  	
    @Value("${works.appropriationnumber}")
    private String worksAppropriationNumber;

    @Value("${works.appropriationnumber.format}")
    private String worksAppropriationNumberFormat;

}
