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
	private String assetServiceBasePath;

	@Value("${egov.services.asset_service.searchpath}")
	private String assetServiceSearchPath;

	@Value("${egov.services.allottee_service.hostname}")
	private String allotteeServiceHostName;

	@Value("${egov.services.allottee_service.basepath}")
	private String allotteeServiceBasePath;

	@Value("${egov.services.allottee_service.searchpath}")
	private String allotteeServiceSearchPath;

	@Value("${egov.services.allottee_service.createpath}")
	private String allotteeServiceCreatePath;

	@Value("${egov.services.lams.agreementnumber_sequence}")
	private String agreementNumberSequence;

	@Value("${egov.services.lams.agreementnumber_prefix}")
	private String lamsPrefix;

	@Value("${egov.services.lams.acknowledgementnumber_sequence}")
	private String acknowledgementNumberSequence;
	
	@Value("${egov.services.demand_service.hostname}")
	private String demandServiceHostName;
	
	@Value("${egov.services.demand_reason_service.searchpath}")
	private String demandReasonSearchPath;
	
	@Value("${egov.services.demand_service.createdemand}")
	private String createDemandSevice;
	
	@Value("${egov.services.demand_service.moduleName}")
	private String demandModuleName;
	
	@Value("${egov.services.demand_service.taxCategoryName}")
	private String taxCategoryName;
	
	@Value("${egov.services.demand_service.taxreasonrent}")
	private String taxReasonRent;
	
	@Value("${egov.services.demand_service.taxreasonadvancetax}")
	private String taxReasonAdvanceTax;
	
	@Value("${egov.services.demand_service.taxreasongoodwillamount}")
	private String taxReasonGoodWillAmount;

	@Value("${egov.services.demand_service.taxreasoncentralgst}")
	private String taxReasonCentralGst;

	@Value("${egov.services.demand_service.taxreasonstategst}")
	private String taxReasonStateGst;

	@Value("${egov.services.demand_service.taxreasonservicetax}")
	private String taxReasonServiceTax;

	@Value("${egov.services.demand_service.taxreasoncgstonadvance}")
	private String taxReasonCGSTOnAdvance;

	@Value("${egov.services.demand_service.taxreasonsgstonadvance}")
	private String taxReasonSGSTOnAdvance;
	
	@Value("${egov.services.demand_service.taxreasoncgstongoodwill}")
	private String taxReasonCGSTOnGoodwill;
	
	@Value("${egov.services.demand_service.taxreasonsgstongoodwill}")
	private String taxReasonSGSTOnGoodwill;
	
	@Value("${kafka.topics.start.workflow}")
	private String startWorkflowTopic;
	
	@Value("${kafka.topics.update.workflow}")
	private String updateWorkflowTopic;
	
	@Value("${egov.services.demand_service.searchpath}")
	private String demandSearchServicepath;
	
	@Value("${egov.services.demand_service.bill.create}")
	private String demandBillCreateService;
	
	@Value("${egov.services.lams.billnumber_sequence}")
	private String billNumberSequence;
	
	@Value("${egov.services.lams.billnumber_prefix}")
	private String lamsBillNumberPrefix;

	@Value("${egov.services.demand_service.updatedemandbasepath}")
	private String updateDemandBasePath;
	
	@Value("${egov.services.demand_service.updatedemand}")
	private String updateDemandService;
	
	@Value("${egov.services.demand_service.bill.search}")
	private String demandBillSearchService;

	@Value("${egov.services.collection_service.purpose}")
	public String purposeService;
	
	@Value("${egov.services.financial.hostname}")
	public String financialServiceHostName;

	@Value("${egov.services.financial.chartofaccounts}")
	public String financialGetChartOfAccountsService;
	
	@Value("${egov.services.financial.finyear.searchpath}")
	public String financialYearSearchPath;
	
	@Value("${egov.services.boundary_service.hostname}")
	public String boundaryServiceHostName;
	
	@Value("${egov.services.boundary_service.searchpath}")
	public String boundaryServiceSearchPath;
	
	@Value("${egov.services.employee_service.hostname}")
	public String employeeServiceHostName;
	
	@Value("${egov.services.employee_service.searchpath}")
	public String employeeServiceSearchPath;
	
	@Value("${egov.services.employee_service.searchpath.pathvariable}")
	public String employeeServiceSearchPathVariable;
	
	@Value("${egov.services.lams.workflow_initiator_position_key}")
	public String workflowInitiatorPositionkey;
	
	@Value("${egov.services.lams.rentincrement_assetcategories}")
	public String rentIncrementAssetCategoryKey;
	
	@Value("${egov.services.lams.eviction_assetcategories}")
	public String evictionAssetCategoryKey;

	@Value("${egov.services.lams.remission_assetcategories}")
	public String remissionAssetCategoryKey;

	@Value("${egov.services.lams.securitydeposit_factor}")
	public String securityDepositFactor;
	
	@Value("${egov.services.lams.renewal_time_before_expiry}")
	public String renewalTimeBeforeExpiry;
	
	@Value("${egov.services.lams.renewal_time_after_expiry}")
	public String renewalTimeAfterExpiry;
	
	@Value("${kafka.topics.save.agreement}")
	private String saveAgreementTopic;
	
	@Value("${kafka.topics.update.agreement}")
	private String updateAgreementTopic;
	
	@Value("${egov.services.egov-common-workflows_service.hostname}")
	public String commonWorkFlowServiceHostName;
	
	@Value("${egov.services.egov-common-workflows_service.historypath}")
	public String commonWorkFlowServiceHistoryPath;
	
	@Value("${egov.services.egov-common-workflows_service.searchpath}")
	public String commonWorkFlowServiceSearchPath;
	
	@Value("${commons.workflow.cancel.action}")
	public String commonsWorkflowCancelAction;
	
	@Value("${egov.services.tenant.host}")
	public String tenantServiceHostName;
	
	@Value("${app.timezone}")
	public String timeZone;
	
	@Value("${egov.services.lams.assetcategory.market}")
	public String assetCategoryMarket;
	
	@Value("${egov.services.demand_service.taxreason.penalty}")
	public String taxReasonPenalty;
	
	
	@Value("${egov.services.demand_service.penaltyCategoryName}")
	private String penaltyCategoryName;
	
	@Value("${egov.services.lams.gst.effective.date}")
	private String gstEffectiveDate;
	
	@Value("${egov.services.lams.gst.rate}")
	private String gstRate;
}