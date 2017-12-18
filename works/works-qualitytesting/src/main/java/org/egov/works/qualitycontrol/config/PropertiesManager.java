package org.egov.works.qualitycontrol.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

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

    @Value("${egov.services.works.qualitytesting.createandupdate.topic}")
    private String qualityTestingCreateAndUpdateTopic;
}
