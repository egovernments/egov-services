package org.egov.works.measurementbook.config;

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

    @Value("${egov.services.works.measurementsheet.pageSize.default}")
    private String pageSize;

    @Value("${egov.services.works.measurementsheet.pageNumber.default}")
    private String pageNumber;

    @Value("${egov.services.works.measurementsheet.pagesize.max}")
    private String pageSizeMax;
    
    @Value("${egov.services.works.measurementbook.create.update.topic}")
    private String worksMBCreateUpdateTopic;
    
    @Value("${egov.services.works.contractorbill.create.update.topic}")
    private String contractorBillCreateUpdateTopic;
    
    @Value("${works.billnumber.format}")
    private String billNumberFormat;
    
    @Value("${works.billnumber}")
    private String billNumber;
    
    @Value("${works.billnumberprefix}")
    private String billNumberPrefix;
    
}
