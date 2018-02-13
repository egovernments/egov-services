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
    
    @Value("${appropriationNumber.prefix}")
    private String appropriationNumberPrefix;
    
    @Value("${egov.services.works.service.offlinestatus.create.validated}")
    private String worksServiceOfflineStatusCreateValidatedTopic;

    @Value("${egov.services.works.service.offlinestatus.update.validated}")
    private String worksServiceOfflineStatusUpdateValidatedTopic;

    @Value("${egov.services.works.loa.backupdate.onupdateofflinestatus.topic}")
    private String worksBackUpdateLOAWithOfflineStatusTopic;

    @Value("${egov.services.works.loa.backupdate.withallofflinestatus.topic}")
    private String worksBackUpdateLOAWithAllOfflineStatusTopic;

    @Value("${egov.services.works.workorder.backupdate.onupdateofflinestatus.topic}")
    private String worksBackUpdateWorkOrderWithOfflineStatusTopic;

    @Value("${egov.services.works.workorder.backupdate.withallofflinestatus.topic}")
    private String worksBackUpdateWorkOrderWithAllOfflineStatusTopic;

}
