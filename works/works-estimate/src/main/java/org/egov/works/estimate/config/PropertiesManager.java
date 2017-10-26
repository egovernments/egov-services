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
	
}