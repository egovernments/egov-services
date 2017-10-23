package org.egov.property.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author Yosadhara
 *
 */
@Configuration
@ToString
@NoArgsConstructor
@SuppressWarnings("unused")
@Service
public class PropertiesManager {

	@Autowired
	Environment environment;

	// Inavlid input
	private String invalidInput;

	// #property producer topics
	private String createValidatedProperty;

	private String updateValidatedProperty;

	private String createTaxCalculated;

	private String updateTaxCalculated;

	// #property consumer topics
	private String createValidatedUser;

	private String updateValidatedUser;

	private String createWorkflow;

	private String updateWorkflow;

	private String approveWorkflow;

	// #TitleTransfer topics
	private String createTitleTransferUserValidator;

	private String updateTitleTransferUserValidator;

	// #Kafka configuration
	private String bootstrapServer;

	private String autoOffsetReset;

	// #database configuration
	private String dbUrl;

	private String driverClassName;

	private String userName;

	private String password;

	// #User search
	private String userHostname;

	private String userBasepath;

	private String userSearchpath;

	// #application properties
	private String isWriteDatesAsTimestamps;

	private String attributeNotfound;

	private String success;

	private String failed;

	private String acknowledgementNotfound;

	private String workflowActionNotfound;

	private String workflowAssigneeNotfound;

	private String workflowDepartmentNotfound;

	private String workflowDesignationNotfound;

	private String workflowStatusNotfound;

	// #application properties
	private String idName;

	private String idFormat;

	private String idHostName;

	private String idCreatepath;

	private String invalidIdServiceUrl;

	private String upicNumberFormat;

	// ####---Boundary properties
	private String invalidBoundaryValidationUrl;

	private String invalidPropertyBoundary;

	private String invalidBoundaryMessage;

	private String locationHostName;

	private String locationSearchpath;

	private String revenueBoundary;

	private String locationBoundary;

	private String adminBoundary;

	private String guidanceValueBoundary;

	private String invalidGuidanceValueBoundary;

	private String invalidGuidanceValueBoundaryId;

	private String invalidAppConfigKey;

	// ####---Pagination place holders ---###########
	private String defaultPageSize;

	private String defaultPageNumber;

	private String defaultOffset;

	// #Set context root
	private String serverContextpath;

	// #Unit
	private String unitType;

	private String recieveProperty;

	private String sendProperty;

	private String calculatorHostName;

	private String calculatorPath;

	private String invalidPropertyStatus;

	private String invalidTitleTransfer;

	// #property chanel type
	private String channelType;

	// #tenantCode
	private String tenantHostName;

	private String tenantBasepath;

	private String tenantSearchpath;

	private String duplicateCode;

	private String generatePropertyTax;

	// ###---Code Validation Proprties---#####
	private String invalidPropertyTypeCode;

	private String invalidPropertyUsageCode;

	private String invalidOldUpicCode;

	private String invalidPropertySubUsageCode;

	private String invalidPropertyOccupancyCode;

	private String invalidPropertyAgeCode;

	private String invalidPropertyStructureCode;

	private String vacantLand;

	// #workflow topics
	private String createPropertyTitletransferWorkflow;

	private String approveTitletransfer;

	private String updatePropertyTitletransferWorkflow;

	private String savePropertyTitletransfer;

	// #code validation messages
	private String invalidDocumentTypeCode;

	private String invalidDepartmentCode;

	private String invalidFloorTypeCode;

	private String invalidRoofTypeCode;

	private String invalidWoodTypeCode;

	private String invalidWallTypeCode;

	private String invalidPropertyFloor;

	private String invalidPropertyVacantland;

	private String simpleDateFormat;

	private String dateAndTimeFormat;

	private String ulbName;

	private String ulbFormat;

	private String calculatorTaxperiodsSearch;

	private String defaultUlbName;

	private String defaultUlbImageUrl;

	// #guidance and factor validation messages

	private String calculatorGuidanceSearchPath;

	private String invalidGuidanceSearchValidationUrl;

	private String invalidGuidanceVal;

	private String invalidGuidanceValueMsg;

	private String calculatorFactorSearchPath;

	private String factorsearchValidationUrl;

	private String invalidFactorValue;

	private String invalidFactorValueMsg;

	private String invalidInputBoundary;

	private String invalidInputOccupancydate;

	private String propertyFactorOccupancy;

	private String propertyFactorUsage;

	private String propertyFactorStructure;

	private String propertyFactorPropertytype;

	private String propertyFactorAge;

	private String billingServiceHostname;

	private String billingServiceSearchdemand;

	private String billingServiceUpdateDemand;

	private String invalidTaxMessage;

	private String invalidDemandValidation;

	// # Workflow URL's

	private String workflowHostname;

	private String workflowBasepath;

	private String workflowStartPath;

	private String workflowUpdatepath;

	private String workflowValidation;

	private String workflowBusinessKey;

	private String invalidParentMsg;

	// #Apartment validation message

	private String invalidFloorNo;

	private String invalidUsage;

	private String invalidOccupancytype;

	private String invalidStructure;

	private String invalidAge;

	// #Billing services
	private String workflowBusinessService;

	private String billingServiceUpdateMisPath;

	private String usageMasterService;

	private String propertyUnitAge;

	private String invalidCategory;

	private String date;

	private String approveProperty;

	// modify
	private String modifyValidatedProperty;

	private String modifyTaxCalculated;

	private String modifyValidatedUser;

	private String modifyWorkflow;

	private String modifyaprroveWorkflow;

	private String propertytax;

	private String documentValue;

	private String titleTransferCalculateSearchPath;

	private String titleTransferTaxCalculation;

	private String feeFactorFromAppConfiguration;

	private String invalidPropertyTax;

	private String createTitleTransferTaxCalculated;

	private String updateTitleTransferTaxCalculated;

	private String titleTransferFeeFactorKeyName;

	private String createTitleTransferValidated;

	private String updateTitleTransferValidated;

	private String titleTransferPenalty;

	private String invalidTitleTransferFeeFactor;

	private String invalidTaxValueTransferFee;

	private String getInvalidGuidanceValueBoundary1;

	private String taxRates;

	private String taxPeriods;

	private String invalidOwners;

	private String emptyDemandsError;

	private String createNotice;

	private String createPropertyUserValidator;

	private String defaultUserPassword;

	private String specialNoticeAction;

	private String demandSearchType;

	private String boundarySearch;

	private String revenueBoundaryType;

	private String locationBoundaryType;

	private String adminBoundaryType;

	private String locationHierarchyType;

	private String adminHierarchyType;

	private String appConfigKey;

	private String heirarchyTypeError;

	private String vacancyRemissionPropertyDueException;

	private String vacancyRemissionInvalidPeriodException;

	private String createValidatedVacancyRemission;

	private String updateValidatedVacancyRemission;

	private String vacancyRemissionCreateWorkflow;

	private String vacancyRemissionUpdateWorkflow;

	private String vacancyRemissionApproveWorkflow;

	private String invalidPropertyTypeException;

	private String invalidTodateGreaterthanFromDate;

	private String fromOrToDateLessthanCurrentDate;

	private String invalidDateValidation;

	private String invalidSearchParameterException;

	private String cancelAction;

	private String invalidTaxCalculation;

	private String invalidTaxHead;

	private String invalidTenent;

	private String userCreatepath;

	private String userUpdatepath;

	private String rejectAction;

	private String createValidatedDemolition;

	private String updateValidatedDemolition;

	private String approveDemolitionWorkflow;

	private String createDemolitionWorkflow;

	private String rejectDemolition;

	private String updateDemolitionWorkflow;

	private String createValidatedTaxExemption;

	private String updateValidatedTaxExemption;

	private String taxExemptionCreateWorkflow;

	private String taxExemptionUpdateWorkflow;

	private String taxExemptionApproveWorkflow;

	public String getInvalidInput() {
		return environment.getProperty("invalid.input");
	}

	// #property producer topics
	public String getCreateValidatedProperty() {
		return environment.getProperty("egov.propertytax.property.create.validated");
	}

	public String getUpdateValidatedProperty() {
		return environment.getProperty("egov.propertytax.property.update.validated");
	}

	public String getCreateTaxCalculated() {
		return environment.getProperty("egov.propertytax.property.create.tax.calculated");
	}

	public String getUpdateTaxCalculated() {
		return environment.getProperty("egov.propertytax.property.update.tax.calculated");
	}

	// #property consumer topics
	public String getCreateValidatedUser() {
		return environment.getProperty("egov.propertytax.property.create.user.validated");
	}

	public String getUpdateValidatedUser() {
		return environment.getProperty("egov.propertytax.property.update.user.validated");
	}

	public String getCreateWorkflow() {
		return environment.getProperty("egov.propertytax.property.create.workflow.started");
	}

	public String getUpdateWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.workflow.started");
	}

	public String getApproveWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.workflow.approved");
	}

	// #TitleTransfer topics
	public String getCreateTitleTransferUserValidator() {
		return environment.getProperty("egov.propertytax.create.property.titletransfer.user.validated");
	}

	public String getUpdateTitleTransferUserValidator() {
		return environment.getProperty("egov.propertytax.update.property.titletransfer.user.validated");
	}

	// #Kafka configuration
	public String getBootstrapServer() {
		return environment.getProperty("spring.kafka.bootstrap.servers");
	}

	public String getAutoOffsetReset() {
		return environment.getProperty("auto.offset.reset.config");
	}

	// #database configuration
	public String getDbUrl() {
		return environment.getProperty("spring.datasource.url");
	}

	public String getDriverClassName() {
		return environment.getProperty("spring.datasource.driver-class-name");
	}

	public String getUserName() {
		return environment.getProperty("spring.datasource.username");
	}

	public String getPassword() {
		return environment.getProperty("spring.datasource.password");
	}

	// #User search
	public String getUserHostname() {
		return environment.getProperty("egov.services.egov_user.hostname");
	}

	public String getUserBasepath() {
		return environment.getProperty("egov.services.egov_user.basepath");
	}

	public String getUserSearchpath() {
		return environment.getProperty("egov.services.egov_user.searchpath");
	}

	public String getUserCreatepath() {
		return environment.getProperty("egov.services.egov_user.createpath");
	}

	public String getUserUpdatepath() {
		return environment.getProperty("egov.services.egov_user.updatepath");
	}

	// #application properties
	public String getIsWriteDatesAsTimestamps() {
		return environment.getProperty("spring.jackson.serialization.write-dates-as-timestamps");
	}

	public String getAttributeNotfound() {
		return environment.getProperty("attribute.notfound");
	}

	public String getSuccess() {
		return environment.getProperty("success");
	}

	public String getFailed() {
		return environment.getProperty("failed");
	}

	public String getAcknowledgementNotfound() {
		return environment.getProperty("acknowledgement.message");
	}

	public String getWorkflowActionNotfound() {
		return environment.getProperty("workflow.action.message");
	}

	public String getWorkflowAssigneeNotfound() {
		return environment.getProperty("workflow.assignee.message");
	}

	public String getWorkflowDepartmentNotfound() {
		return environment.getProperty("workflow.department.message");
	}

	public String getWorkflowDesignationNotfound() {
		return environment.getProperty("workflow.designation.message");
	}

	public String getWorkflowStatusNotfound() {
		return environment.getProperty("workflow.status.message");
	}

	// ####---Idgeneration properties
	public String getIdName() {
		return environment.getProperty("id.idName");
	}

	public String getIdFormat() {
		return environment.getProperty("id.format");
	}

	public String getIdHostName() {
		return environment.getProperty("egov.services.egov_idgen.hostname");
	}

	public String getIdCreatepath() {
		return environment.getProperty("egov.services.egov_idgen.createpath");
	}

	public String getInvalidIdServiceUrl() {
		return environment.getProperty("invalid.id.service.url");
	}

	public String getUpicNumberFormat() {
		return environment.getProperty("upic.number.format");
	}

	// ####---Boundary properties
	public String getInvalidBoundaryValidationUrl() {
		return environment.getProperty("invalid.property.boundary.validation.url");
	}

	public String getInvalidPropertyBoundary() {
		return environment.getProperty("invalid.property.boundary");
	}

	public String getInvalidBoundaryMessage() {
		return environment.getProperty("invalid.property.boundary.message");
	}

	public String getLocationHostName() {
		return environment.getProperty("egov.services.egov_location.hostname");
	}

	public String getLocationSearchpath() {
		return environment.getProperty("egov.services.egov_location.searchpath");
	}

	public String getRevenueBoundary() {
		return environment.getProperty("revenue.boundary");
	}

	public String getLocationBoundary() {
		return environment.getProperty("location.boundary");
	}

	public String getAdminBoundary() {
		return environment.getProperty("admin.boundary");
	}

	public String getGuidanceValueBoundary() {
		return environment.getProperty("guidance.boundary");
	}

	public String getInvalidGuidanceValueBoundary() {
		return environment.getProperty("invalid.property.boundary.guidance");
	}

	public String getInvalidGuidanceValueBoundaryId() {
		return environment.getProperty("invalid.property.boundary.guidance.id");
	}

	public String getInvalidAppConfigKey() {
		return environment.getProperty("property.APPCONFIG_KEY_ALREADY_EXIST");
	}

	// ####---Pagination place holders ---------------#######
	public String getDefaultPageSize() {
		return environment.getProperty("default.page.size");
	}

	public String getDefaultPageNumber() {
		return environment.getProperty("default.page.number");
	}

	public String getDefaultOffset() {
		return environment.getProperty("default.offset");
	}

	// #Set context root
	public String getServerContextpath() {
		return environment.getProperty("server.contextPath");
	}

	// #Unit
	public String getUnitType() {
		return environment.getProperty("unit.type");
	}

	public String getRecieveProperty() {
		return environment.getProperty("propety.recieve");
	}

	public String getSendProperty() {
		return environment.getProperty("property.send");
	}

	public String getCalculatorHostName() {
		return environment.getProperty("egov.services.pt_calculator.hostname");
	}

	public String getCalculatorPath() {
		return environment.getProperty("egov.services.pt_calculator.calculatorpath");
	}

	public String getInvalidPropertyStatus() {
		return environment.getProperty("invalid.property.status");
	}

	public String getInvalidTitleTransfer() {
		return environment.getProperty("invalid.title.transfer");
	}

	// #property chanel type
	public String getChannelType() {
		return environment.getProperty("egov.property.channel.type");
	}

	// #tenantCode
	public String getTenantHostName() {
		return environment.getProperty("egov.services.tenant.hostname");
	}

	public String getTenantBasepath() {
		return environment.getProperty("egov.services.tenant.basepath");
	}

	public String getTenantSearchpath() {
		return environment.getProperty("egov.services.tenant.searchpath");
	}

	public String getDuplicateCode() {
		return environment.getProperty("duplicate.code");
	}

	public String getGeneratePropertyTax() {
		return environment.getProperty("egov.propertytax.create.demand");
	}

	// ###---Code Validation Proprties---#####
	public String getInvalidPropertyTypeCode() {
		return environment.getProperty("invalid.input.propertytype");
	}

	public String getInvalidPropertyUsageCode() {
		return environment.getProperty("invalid.input.usage");
	}

	public String getInvalidOldUpicCode() {
		return environment.getProperty("invalid.oldupicno");
	}

	public String getInvalidPropertySubUsageCode() {
		return environment.getProperty("invalid.input.subusage");
	}

	public String getInvalidPropertyOccupancyCode() {
		return environment.getProperty("invalid.input.occupancy");
	}

	public String getInvalidPropertyAgeCode() {
		return environment.getProperty("invalid.input.age");
	}

	public String getInvalidPropertyStructureCode() {
		return environment.getProperty("invalid.input.structure");
	}

	public String getVacantLand() {
		return environment.getProperty("egov.property.type.vacantLand");
	}

	// #Workflow topics
	public String getCreatePropertyTitletransferWorkflow() {
		return environment.getProperty("egov.propertytax.property.titletransfer.create.workflow.started");
	}

	public String getApproveTitletransfer() {
		return environment.getProperty("egov.propertytax.property.titletransfer.approved");
	}

	public String getUpdatePropertyTitletransferWorkflow() {
		return environment.getProperty("egov.propertytax.property.titletransfer.workflow.updated");
	}

	public String getSavePropertyTitletransfer() {
		return environment.getProperty("egov.propertytax.property.titletransfer.db.saved");
	}

	// #code validation messages
	public String getInvalidDocumentTypeCode() {
		return environment.getProperty("invalid.input.documenttype");
	}

	public String getInvalidDepartmentCode() {
		return environment.getProperty("invalid.input.department");
	}

	public String getInvalidFloorTypeCode() {
		return environment.getProperty("invalid.input.floortype");
	}

	public String getInvalidRoofTypeCode() {
		return environment.getProperty("invalid.input.rooftype");
	}

	public String getInvalidWoodTypeCode() {
		return environment.getProperty("invalid.input.woodtype");
	}

	public String getInvalidWallTypeCode() {
		return environment.getProperty("invalid.input.walltype");
	}

	public String getInvalidPropertyFloor() {
		return environment.getProperty("invalid.property.floor");
	}

	public String getInvalidPropertyVacantland() {
		return environment.getProperty("invalid.property.vacantland");
	}

	public String getSimpleDateFormat() {
		return environment.getProperty("egov.property.simple.dateformat");
	}

	public String getDateAndTimeFormat() {
		return environment.getProperty("egov.property.date.and.timeformat");
	}

	public String getUlbName() {
		return environment.getProperty("ulb.name");
	}

	public String getUlbFormat() {
		return environment.getProperty("ulb.format");
	}

	public String getCalculatorTaxperiodsSearch() {
		return environment.getProperty("egov.services.pt_calculator.taxperiods.search");
	}

	public String getDefaultUlbName() {
		return environment.getProperty("default.ulb.name");
	}

	public String getDefaultUlbImageUrl() {
		return environment.getProperty("default.ulb.image.url");
	}

	// #guidance and factor validation messages

	public String getCalculatorGuidanceSearchPath() {
		return environment.getProperty("egov.services.pt_calculator.guidancesearchpath");
	}

	public String getInvalidGuidanceSearchValidationUrl() {
		return environment.getProperty("invalid.property.guidancesearch.validation.url");
	}

	public String getInvalidGuidanceVal() {
		return environment.getProperty("invalid.property.guidancevalue");
	}

	public String getInvalidGuidanceValMsg() {
		return environment.getProperty("invalid.property.guidancevalue.message");
	}

	public String getCalculatorFactorSearchPath() {
		return environment.getProperty("egov.services.pt_calculator.factorsearchpath");
	}

	public String getFactorsearchValidationUrl() {
		return environment.getProperty("invalid.property.factorsearch.validation.url");
	}

	public String getInvalidFactorValue() {
		return environment.getProperty("invalid.property.factorvalue");
	}

	public String getInvalidFactorValueMsg() {
		return environment.getProperty("invalid.property.factorvalue.message");
	}

	public String getInvalidInputBoundary() {
		return environment.getProperty("invalid.input.boundary");
	}

	public String getInvalidInputOccupancydate() {
		return environment.getProperty("invalid.input.occupancydate");
	}

	public String getPropertyFactorOccupancy() {
		return environment.getProperty("egov.property.factor.occupancy");
	}

	public String getPropertyFactorUsage() {
		return environment.getProperty("egov.property.factor.usage");
	}

	public String getPropertyFactorStructure() {
		return environment.getProperty("egov.property.factor.structure");
	}

	public String getPropertyFactorPropertytype() {
		return environment.getProperty("egov.property.factor.propertytype");
	}

	public String getPropertyFactorAge() {
		return environment.getProperty("egov.property.factor.age");
	}

	public String getBillingServiceHostname() {
		return environment.getProperty("egov.services.billing_service.hostname");
	}

	public String getBillingServiceSearchdemand() {
		return environment.getProperty("egov.services.billing_service.searchdemand");
	}

	public String getBillingServiceUpdatedemand() {
		return environment.getProperty("egov.services.billing_service.updatedemand");
	}

	public String getBillingServiceSearchTaxHeads() {
		return environment.getProperty("egov.services.billing_service.searchtaxheads");
	}

	public String getInvalidTaxMessage() {
		return environment.getProperty("invalid.titletransfer.tax.message");
	}

	public String getInvalidDemandValidation() {
		return environment.getProperty("invalid.titletransfer.demand.validation");
	}

	// # Workflow URL's

	public String getWorkflowHostname() {
		return environment.getProperty("egov.services.egov-common-workflows.hostname");
	}

	public String getWorkflowBasepath() {
		return environment.getProperty("egov.services.egov-common-workflows.basepath");
	}

	public String getWorkflowStartPath() {
		return environment.getProperty("egov.services.egov-common-workflows.startpath");
	}

	public String getWorkflowUpdatepath() {
		return environment.getProperty("egov.services.egov-common-workflows.updatepath");
	}

	public String getWorkflowValidation() {
		return environment.getProperty("invalid.update.workflow.validation");
	}

	public String getWorkflowBusinessKey() {
		return environment.getProperty("businessKey");
	}

	public String getInvalidParentMsg() {
		return environment.getProperty("invalid.property.parent.code");
	}

	public String getBusinessService() {
		return environment.getProperty("business.service");
	}

	public String getBillingServiceUpdateMisPath() {
		return environment.getProperty("egov.services.billing_service.updatemisdemand");
	}

	public String getInvalidFloorNo() {
		return environment.getProperty("invalid.input.floorNo");
	}

	public String getInvalidUsage() {
		return environment.getProperty("invalid.input.unit.usage");
	}

	public String getInvalidOccupancytype() {
		return environment.getProperty("invalid.input.unit.occupancytype");
	}

	public String getInvalidStructure() {
		return environment.getProperty("invalid.input.unit.structure");
	}

	public String getInvalidAge() {
		return environment.getProperty("invalid.input.unit.age");
	}

	public String getUsageMasterDefaultService() {
		return environment.getProperty("usage.master.default.service");
	}

	public String getPropertyUnitAge() {
		return environment.getProperty("egov.property.unit.age");
	}

	public String getInvalidCategory() {
		return environment.getProperty("invalid.property.category");
	}

	public String getDate() {
		return environment.getProperty("egov.property.date");
	}

	public String getApproveProperty() {
		return environment.getProperty("property.approved");
	}

	public String getModifyValidatedProperty() {
		return environment.getProperty("egov.propertytax.property.modify.validated");
	}

	public String getModifyValidatedUser() {
		return environment.getProperty("egov.propertytax.property.modify.user.validated");
	}

	public String getModifyTaxCalculated() {
		return environment.getProperty("egov.propertytax.property.modify.tax.calculated");
	}

	public String getModifyWorkflow() {
		return environment.getProperty("egov.propertytax.property.modify.workflow.started");
	}

	public String getModifyaprroveWorkflow() {
		return environment.getProperty("egov.propertytax.property.modify.workflow.approved");
	}

	public String getPropertyTax() {
		return environment.getProperty("titletransfer.feefactor.propertytax");
	}

	public String getDocumentValue() {
		return environment.getProperty("titletransfer.feefactor.documentvalue");
	}

	public String getTitleTransferCalculateSearchPath() {
		return environment.getProperty("egov.services.pt_calculator.titletransfersearchpath");
	}

	public String getTitleTransferTaxCalculate() {
		return environment.getProperty("egov.property.titletransfer.taxcalculation");
	}

	public String getFeeFactorFromAppConfiguration() {
		return environment.getProperty("egov.property.appconfiguration.feefactor");
	}

	public String getTitleTransferFeeFactorKeyName() {
		return environment.getProperty("egov.property.titletransfer.feefactor.keyname");
	}

	public String getInvalidPropertyTax() {
		return environment.getProperty("egov.property.titletransfer.propertytax.tax");
	}

	public String getCreateTitleTransferTaxCalculated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.create.tax.calculated");
	}

	public String getUpdateTitleTransferTaxCalculated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.update.tax.calculated");
	}

	public String getCreateTitleTransferValidated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.create.validated");
	}

	public String getUpdateTitleTransferValidated() {
		return environment.getProperty("egov.propertytax.property.titletransfer.update.validated");
	}

	public String getTitleTransferPenalty() {
		return environment.getProperty("egov.property.titletransfer.penalty");
	}

	public String getInvalidTitleTransferFeeFactor() {
		return environment.getProperty("invalid.titletranfer.feefactor");
	}

	public String getInvalidTaxValueTransferFee() {
		return environment.getProperty("invalid.taxvalue.transferfee");
	}

	public String getInvalidGuidanceValueBoundary1() {
		return environment.getProperty("invalid.guidanceValueBoundary1");
	}

	public String getTaxRates() {
		return environment.getProperty("egov.services.pt_calculator.taxRatesSearchPath");
	}

	public String getTaxPeriods() {
		return environment.getProperty("egov.services.pt_calculator.taxPeriodsSearchPath");
	}

	public String getInvalidTaxRates() {
		// TODO Auto-generated method stub
		return environment.getProperty("egov.propertytax.property.invalidtaxrates");
	}

	public String getInvalidTaxPeriods() {
		// TODO Auto-generated method stub
		return environment.getProperty("egov.propertytax.property.invalidtaxperiods");
	}

	public String getInvalidOwners() {
		// TODO Auto-generated method stub
		return environment.getProperty("invalid.owners");
	}

	public String getCreateNotice() {
		return environment.getProperty("property-notice-create");
	}

	public String getEmptyDemandsError() {
		return environment.getProperty("invalid.demands");
	}

	public String getCreatePropertyUserValidator() {
		return environment.getProperty("egov.propertytax.create.property.user.validated");
	}

	public String getDefaultUserPassword() {
		return environment.getProperty("default.user.password");
	}

	public String getVacancyRemissionPropertyDueException() {
		return environment.getProperty("vacancy.remission.propertydue.exception");
	}

	public String getVacancyRemissionInvalidPeriodException() {
		return environment.getProperty("vacancy.remission.invalid.period.exception");
	}

	public String getSpecialNoticeAction() {
		return environment.getProperty("special.notice.action");
	}

	public String getBoundarySearch() {
		return environment.getProperty("egov.services.egov_location.search");
	}

	public String getRevenueBoundaryType() {
		return environment.getProperty("revenue.boundarytype");
	}

	public String getLocationBoundaryType() {
		return environment.getProperty("location.boundarytype");
	}

	public String getAdminBoundaryType() {
		return environment.getProperty("admin.boundarytype");
	}

	public String getLocationHierarchyType() {
		return environment.getProperty("location.hierarchytype");
	}

	public String getAdminHierarchyType() {
		return environment.getProperty("admin.hierarchytype");
	}

	public String getAppConfigKey() {
		return environment.getProperty("appconfiguration.key");
	}

	public String getHeirarchyTypeError() {
		return environment.getProperty("invalid.property.appconfiguration.hierarchy");
	}

	public String getDemandSearchType() {
		return environment.getProperty("demand.search.type");
	}

	public String getCreateValidatedVacancyRemission() {
		return environment.getProperty("egov.propertytax.property.vacancyremission.create.validated");
	}

	public String getUpdateValidatedVacancyRemission() {
		return environment.getProperty("egov.propertytax.property.vacancyremission.update.validated");
	}

	public String getVacancyRemissionCreateWorkflow() {
		return environment.getProperty("egov.propertytax.property.create.vacancyremission.workflow.started");
	}

	public String getVacancyRemissionUpdateWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.vacancyremission.workflow.started");
	}

	public String getVacancyRemissionApproveWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.vacancyremission.workflow.approved");
	}

	public String getInvalidPropertyTypeException() {
		return environment.getProperty("invalid.propertytype.exception");
	}

	public String getInvalidTodateGreaterthanFromDate() {
		return environment.getProperty("invalid.todate.greaterthan.fromdate");
	}

	public String getFromOrToDateLessthanCurrentDate() {
		return environment.getProperty("fromortodate.lessthan.currentdate");
	}

	public String getInvalidDateValidation() {
		return environment.getProperty("invalid.date.validation");
	}

	public String getInvalidSearchParameterException() {
		return environment.getProperty("invalid.search.parameter.exception");
	}

	public String getCancelAction() {
		return environment.getProperty("cancel");
	}

	public String getRejectAction() {
		return environment.getProperty("reject");
	}

	public String getInvalidTaxCalculation() {
		return environment.getProperty("egov.property.taxcalculation");
	}

	public String getInvalidTaxHead() {
		return environment.getProperty("egov.property.taxhead");
	}

	public String getInvalidTenent() {
		return environment.getProperty("egov.property.tenant");
	}

	public String getCreateValidatedDemolition() {
		return environment.getProperty("egov.propertytax.create.demolition.validated");
	}

	public String getUpdateValidatedDemolition() {
		return environment.getProperty("egov.propertytax.update.demolition.validated");
	}

	public String getApproveDemolition() {
		return environment.getProperty("egov.propertytax.demolition.approved");
	}

	public String getCreateDemolitionWorkflow() {
		return environment.getProperty("egov.propertytax.create.demolition.workflow.started");
	}

	public String getRejectDemolition() {
		return environment.getProperty("egov.propertytax.demolition.rejected");
	}

	public String getUpdateDemolitionWorkflow() {
		return environment.getProperty("egov.propertytax.demolition.update.workflow.started");
	}

	public String getCreateValidatedTaxExemption() {
		return environment.getProperty("egov.propertytax.property.taxexemption.create.validated");
	}

	public String getUpdateValidatedTaxExemption() {
		return environment.getProperty("egov.propertytax.property.taxexemption.update.validated");
	}

	public String getTaxExemptionCreateWorkflow() {
		return environment.getProperty("egov.propertytax.property.create.taxexemption.workflow.started");
	}

	public String getTaxExemptionUpdateWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.taxexemption.workflow.started");
	}

	public String getTaxExemptionApproveWorkflow() {
		return environment.getProperty("egov.propertytax.property.update.taxexemption.workflow.approved");
	}

}