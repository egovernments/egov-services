package org.egov.works.masters.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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

    @Value("${egov.services.works.masters.sorrate.create.validated}")
    private String worksMasterSorrateCreateValidatedTopic;

    @Value("${egov.services.works.masters.sorrate.update.validated}")
    private String worksMasterSorrateUpdateValidatedTopic;

    @Value("${egov.services.works.masters.estimatetemplate.create.validated}")
    private String worksMasterEstimateTemplateCreateValidatedTopic;

    @Value("${egov.services.works.masters.estimatetemplate.update.validated}")
    private String worksMasterEstimateTemplateUpdateValidatedTopic;

    @Value("${egov.services.egov_mdms.hostname}")
    private String mdmsServiceHostname;

    @Value("${egov.services.egov_mdms.searchpath}")
    private String mdmsSearchPath;

    @Value("${egov.services.works.masters.contractor.create.validated}")
    private String worksMasterContractorCreateValidatedTopic;

    @Value("${egov.services.works.masters.contractor.update.validated}")
    private String worksMasterContractorUpdateValidatedTopic;

}
