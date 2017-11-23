package org.egov.works.estimate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@ToString
public class PropertiesManager {

	@Value("${app.timezone}")
	private String appTimeZone;

	@Value("${egov.services.works.estimate.pageSize.default}")
	private String pageSize;

	@Value("${egov.services.works.estimate.pageNumber.default}")
	private String pageNumber;

	@Value("${egov.services.works.estimate.pagesize.max}")
	private String pageSizeMax;
	
	@Value("${egov.services.works.abstract.estimate.create.topic}")
	private String worksAbstractEstimateCreateTopic;
	
	@Value("${egov.services.works.abstract.estimate.update.topic}")
	private String worksAbstractEstimateUpdateTopic;
	
	@Value("${egov.services.works.projectcode.create.topic}")
	private String worksProjectCodeCreateTopic;
	
	@Value("${egov.services.works.projectcode.update.topic}")
	private String worksProjectCodeUpdateTopic;

    @Value("${egov.services.works.detailed.estimate.create.topic}")
    private String worksDetailedEstimateCreateTopic;

    @Value("${egov.services.works.detailed.estimate.update.topic}")
    private String worksDetailedEstimateUpdateTopic;

    @Value("${works.abstractestimatenumber}")
    private String worksAbstractEstimateNumber;

    @Value("${works.abstractestimatenumber.format}")
    private String worksAbstractEstimateNumberFormat;

    @Value("${works.detailedestimatenumber}")
    private String worksDetailedEstimateNumber;

    @Value("${works.detailedestimatenumber.format}")
    private String worksDetailedEstimateNumberFormat;
    
    @Value("${works.projectcode}")
    private String worksWorkIdentificationNumber;

    @Value("${works.projectcode.format}")
    private String worksWorkIdentificationNumberFormat;

    @Value("${location.required}")
    private String locationRequiredForEstimate;

    @Value("${estimateNumber.prefix}")
    private String estimateNumberPrefix;

    @Value("${detailedEstimateNumber.prefix}")
    private String detailedEstimateNumberPrefix;
    
    @Value("${egov.works.services.hostname}")
    private String worksSeviceHostName;
    
    @Value("${egov.works.services.estimateappropriation.create}")
    private String estimateAppropriationURL;
    
	@Value("${egov.services.works.accountdetailkey.create.topic}")
	private String createAccountDetailKeyTopic;
	
    @Value("${projectcode.prefix}")
    private String workIdentificationNumberPrefix;

}