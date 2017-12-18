package org.egov.works.masters.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ramki on 31/10/17.
 */
@Configuration
@Getter
@ToString
public class PropertiesManager {

    @Value("${app.timezone}")
    private String appTimeZone;

    @Value("${egov.services.works.masters.pageSize.default}")
    private String pageSize;

    @Value("${egov.services.works.masters.pageNumber.default}")
    private String pageNumber;

    @Value("${egov.services.works.masters.pagesize.max}")
    private String pageSizeMax;

    @Value("${egov.services.works.masters.sorrate.saveorupdate.validated}")
    private String worksMasterSorrateSaveOrUpdateValidatedTopic;

    @Value("${egov.services.works.masters.estimatetemplate.saveorupdate.validated}")
    private String worksMasterEstimateTemplateSaveOrUpdateValidatedTopic;

    @Value("${egov.services.egov_mdms.hostname}")
    private String mdmsServiceHostname;

    @Value("${egov.services.egov_mdms.searchpath}")
    private String mdmsSearchPath;

    @Value("${egov.services.works.masters.contractor.create.validated}")
    private String worksMasterContractorCreateValidatedTopic;

    @Value("${egov.services.works.masters.contractor.update.validated}")
    private String worksMasterContractorUpdateValidatedTopic;

    @Value("${egov.services.works.masters.milestonetemplate.saveorupdate.validated}")
    private String worksMasterMilestoneTemplateSaveOrUpdateValidatedTopic;

}
