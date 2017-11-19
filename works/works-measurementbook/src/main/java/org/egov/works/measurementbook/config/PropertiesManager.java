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

    @Value("${egov.services.works.workorder.pageSize.default}")
    private String pageSize;

    @Value("${egov.services.works.workorder.pageNumber.default}")
    private String pageNumber;

    @Value("${egov.services.works.workorder.pagesize.max}")
    private String pageSizeMax;
    
    @Value("${works.loanumber.format}")
    private String loaNumberFormat;
    
    @Value("${works.loanumber}")
    private String worksLOANumber;

}
