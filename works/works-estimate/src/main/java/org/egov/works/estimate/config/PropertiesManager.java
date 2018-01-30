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

    @Value("${egov.services.works.abstract.estimate.createandupdate.topic}")
    private String worksAbstractEstimateCreateAndUpdateTopic;

    @Value("${egov.services.works.projectcode.create.topic}")
    private String worksProjectCodeCreateTopic;

    @Value("${egov.services.works.projectcode.update.topic}")
    private String worksProjectCodeUpdateTopic;

    @Value("${egov.services.works.detailed.estimate.createandupdate.topic}")
    private String worksDetailedEstimateCreateAndUpdateTopic;

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
    private String createEstimateAppropriationURL;
    
    @Value("${egov.works.services.estimateappropriation.update}")
    private String updateEstimateAppropriationURL;

    @Value("${egov.services.works.accountdetailkey.create.topic}")
    private String createAccountDetailKeyTopic;

    @Value("${projectcode.prefix}")
    private String workIdentificationNumberPrefix;

    @Value("${egov.services.works.revision.detailedestimate.topic}")
    private String worksRECreateUpdateTopic;
    
    @Value("${technicalsanctionnumber.prefix}")
    private String technicalSanctionNumberPrefix;
    
    @Value("${works.technicalsanctionnumber}")
    private String worksTechnicalSanctionNumber;

    @Value("${works.technicalsanctionnumber.format}")
    private String worksTechnicalSanctionNumberFormat;

    @Value("${egov.services.works.abstract.estimate.backupdate.oncreatede.topic}")
    private String worksAbstractEstimateBackupdateOnCreateDETopic;

    @Value("${egov.services.works.abstract.estimate.backupdate.oncancelde.topic}")
    private String worksAbstractEstimateBackupdateOnCancelDETopic;


}