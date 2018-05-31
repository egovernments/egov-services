package org.egov.pt.calculator.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class Configurations {

	//PERSISTER
	@Value("${kafka.topics.billing-slab.save.service}")
	public String billingSlabSavePersisterTopic;
	
	@Value("${kafka.topics.billing-slab.update.service}")
	public String billingSlabUpdatePersisterTopic;
	
	//MDMS
	@Value("${egov.mdms.host}")
	private String mdmsHost;
	
	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;
}
