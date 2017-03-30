package org.egov.lams.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@ToString
public class PropertiesManager {

	@Value("${egov.services.asset_service.hostname}")
	private String assetServiceHostName;

	@Value("${egov.services.asset_service.basepath}")
	private String assetServiceBasePAth;

	@Value("${egov.services.asset_service.searchpath}")
	private String assetServiceSearchPath;

	@Value("${egov.services.allottee_service.hostname}")
	private String allotteeServiceHostName;

	@Value("${egov.services.allottee_service.basepath}")
	private String allotteeServiceBasePAth;

	@Value("${egov.services.allottee_service.searchpath}")
	private String allotteeServiceSearchPath;

	@Value("${egov.services.allottee_service.createpath}")
	private String allotteeServiceCreatePAth;
	
	@Value("${egov.services.lams.ulb_number}")
	private String ulbNumber;

	@Value("${egov.services.lams.agreementnumber_sequence}")
	private String agreementNumberSequence;

	@Value("${egov.services.lams.agreementnumber_prefix}")
	private String lamsPrefix;

	@Value("${egov.services.lams.acknowledgementnumber_sequence}")
	private String acknowledgementNumberSequence;
	
	@Value("${egov.services.demand_service.hostname}")
	private String demandServiceHostName;
	
	@Value("${egov.services.demand_reason_service.searchpath}")
	private String demandReasonSearchService;
	
	@Value("${egov.services.demand_service.createdemand}")
	private String createDemandSevice;
	
	@Value("${egov.services.demand_service.moduleName}")
	private String getDemandModuleName;
	
	@Value("${egov.services.demand_service.TaxName}")
	private String getDemandModuleTax;

	@Value("${kafka.topics.start.workflow}")
	private String startWorkflowTopic;
	
	@Value("${kafka.topics.update.workflow}")
	private String updateWorkflowTopic;
	@Value("${egov.services.demand_service.searchpath}")
	private String demandSearchService;
	
	@Value("${egov.services.demand_service.bill.create}")
	private String demandBillCreateService;
	
	@Value("${egov.services.lams.billnumber_sequence}")
	private String billNumberSequence;
	
	@Value("${egov.services.lams.billnumber_prefix}")
	private String lamsBillNumberPrefix;

	@Value("${egov.services.demand_service.updatedemand}")
	private String updateDemandSevice;
	
	@Value("${egov.services.demand_service.bill.search}")
	private String demandBillSearchService;

	@Value("${egov.services.collection_service.hostname}")
	public String purposeHostName;

	@Value("${egov.services.collection_service.purpose}")
	public String purposeService;

}
