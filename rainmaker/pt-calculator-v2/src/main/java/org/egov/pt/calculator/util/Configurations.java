package org.egov.pt.calculator.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
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
	
	
	/*
	 * Calculator Configs
	 */
	
	// billing service
	@Value("${egov.billingservice.host}")
	private String billingServiceHost;
	
	@Value("${egov.taxhead.search.endpoint}")
	private String	taxheadsSearchEndpoint;
	
	// billing slab configs
	
	@Value("${billingslab.value.all}")
	private String slabValueAll;
	
	@Value("${billingslab.value.usagemajor.nonresidential}")
	private String usageMajorNonResidential;
	
	@Value("${billingslab.value.occupancytype.rented}")
	private String occupancyTypeRented;
	
	@Value("${billingslab.value.arv.percent}")
	private Double arvPercent;
	
	@Value("${billingslab.value.taxheadcode.pttax}")
	private String ptTaxCode;
	
	
}
