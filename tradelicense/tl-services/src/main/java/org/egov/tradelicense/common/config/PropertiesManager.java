package org.egov.tradelicense.common.config;

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

	@Value("${error.license.licenses.notfound}")
	private String tradeLicensesNotFoundMsg;

	@Value("${error.license.licenses.size.not.empty}")
	private String tradeLicensesNotEmptyMsg;

	@Value("${egov.services.tl-masters_v1.hostname}")
	private String tradeLicenseMasterServiceHostName;

	@Value("${egov.services.tl-masters_v1.basepath}")
	private String tradeLicenseMasterServiceBasePath;

	@Value("${egov.services.tl-indexer_v1.basepath}")
	private String tradeLicenseIndexerServiceBasePath;

	@Value("${egov.services.tl-indexer_v1.license.searchpath}")
	private String tradeLicenseIndexerLicenseSearchPath;

	@Value("${aadhaar.validation.required}")
	private Boolean adhaarValidation;

	@Value("${egov.services.tl-indexer_v1.hostname}")
	private String tradeLicenseIndexerServiceHostName;

	@Value("${ptis.validation.required}")
	private Boolean ptisValidation;

	@Value("${egov.services.pt_property.hostname}")
	private String propertyHostname;

	@Value("${egov.services.pt_property.basepath}")
	private String propertyBasepath;

	@Value("${egov.services.pt_property.searchpath}")
	private String propertySearchpath;

	@Value("${egov.services.tl-masters_v1.documenttype.searchpath}")
	private String documentServiceSearchPath;

	@Value("${egov.services.tl-masters_v2.documenttype.searchpath}")
	private String documentServiceV2SearchPath;

	@Value("${egov.services.tl-masters_v1.category.searchpath}")
	private String categoryServiceSearchPath;

	@Value("${egov.services.tl-masters_v1.uom.searchpath}")
	private String uomServiceSearchPath;

	@Value("${egov.services.tl-masters_v1.status.searchpath}")
	private String statusServiceSearchPath;

	@Value("${egov.services.tl-masters_v1.feematrix.searchpath}")
	private String feeMatrixServiceSearchPath;

	@Value("${egov.services.egov-location.hostname}")
	private String locationServiceHostName;

	@Value("${egov.services.egov-location.basepath}")
	private String locationServiceBasePath;

	@Value("${egov.services.egov-location.searchpath}")
	private String locationServiceSearchPath;

	@Value("${egov.services.egf-masters.hostname}")
	private String financialYearServiceHostName;

	@Value("${egov.services.egf-masters.basepath}")
	private String financialYearServiceBasePath;

	@Value("${egov.services.egf-masters.searchpath}")
	private String financialYearServiceSearchPath;

	@Value("${egov.services.tl-services.pageSize.default}")
	private String pageSize;

	@Value("${egov.services.tl-services.pageNumber.default}")
	private String pageNumber;

	@Value("${egov.services.id_service.hostname}")
	private String idGenServiceBasePathTopic;

	@Value("${egov.services.id_service.createpath}")
	private String idGenServiceCreatePathTopic;

	@Value("${id.tlnName}")
	private String idTLNumberGenNameServiceTopic;

	@Value("${id.tlnFormat}")
	private String idTLNumberGenFormatServiceTopic;

	@Value("${id.anName}")
	private String idApplicationNumberGenNameServiceTopic;

	@Value("${id.anFormat}")
	private String idApplicationNumberGenFormatServiceTopic;

	@Value("${error.license.locationendpoint}")
	private String locationEndPointError;

	@Value("${error.oldLicense.duplicate}")
	private String duplicateOldTradeLicenseMsg;

	@Value("${error.applicationnumber.duplicate}")
	private String duplicateTradeApplicationNumberMsg;

	@Value("${error.license.categoryendpoint}")
	private String catEndPointError;

	@Value("${error.license.documentendpoint}")
	private String documentEndPointErrormsg;

	@Value("${error.license.propertyendpoint}")
	private String propertyEndPointErrormsg;

	@Value("${error.license.oldLicenseNumber}")
	private String oldLicenseNumberErrorMsg;

	@Value("${error.license.agreementdate}")
	private String agreementDateErrorMsg;

	@Value("${error.license.agreementno.notfound}")
	private String agreementNotFoundErrorMsg;

	@Value("${error.license.agreementno}")
	private String agreementNoErrorMsg;

	@Value("${error.license.propertyAssesmentNo.notvalid}")
	private String propertyAssesmentNoInvalidErrorMsg;

	@Value("${error.license.propertyAssesmentNo.notfound}")
	private String propertyAssesmentNotFoundMsg;

	@Value("${error.license.aadhaarnumber}")
	private String aadhaarNumberErrorMsg;

	@Value("${error.license.locality}")
	private String localityErrorMsg;

	@Value("${error.license.revenueward}")
	private String revenueWardErrorMsg;

	@Value("${error.license.adminward}")
	private String adminWardErrorMsg;

	@Value("${error.license.category}")
	private String categoryErrorMsg;

	@Value("${error.license.subcategory}")
	private String subCategoryErrorMsg;

	@Value("${error.license.uom}")
	private String uomErrorMsg;

	@Value("${error.license.documenttype}")
	private String documentTypeErrorMsg;

	@Value("${error.license.validityyears}")
	private String validtyYearsErrorMsg;

	@Value("${error.license.validityyearsMatch}")
	private String validatiyYearsMatch;

	@Value("${error.license.feedetails}")
	private String feeDetailsErrorMsg;

	@Value("${error.license.legacy.feedetails.notfound}")
	private String legacyFeeDetailsNotFoundMsg;

	@Value("${error.license.feeDetailYearNotFound}")
	private String feeDetailYearNotFound;

	@Value("${legacy.trade.create.success.status.message}")
	private String legacyCreateSuccessMessage;

	@Value("${new.trade.create.success.status.message}")
	private String newTradeLicenseCreateSuccessMessage;

	// IdNotFound Exception Related
	@Value("${id.notFound.field}")
	private String idField;

	@Value("${oldLicense.id.notFound.customMsg}")
	private String oldLicenseIdNotFoundCustomMsg;

	@Value("${oldLicense.id.notValid.customMsg}")
	private String oldLicenseIdNotValidCustomMsg;

	@Value("${supporting.document.id.notFound.customMsg}")
	private String supportDocumentIdNotFoundCustomMsg;

	@Value("${supporting.document.id.notValid.customMsg}")
	private String supportDocumentIdNotValidCustomMsg;

	@Value("${feedetail.id.notFound.customMsg}")
	private String feeDetailIdNotFoundCustomMsg;

	@Value("${feedetail.id.notValid.customMsg}")
	private String feeDetailIdNotValidCustomMsg;

	@Value("${applicationfee.applicable}")
	private String applicatonFeeApplicable;

	// mandatory document exception message
	@Value("${mandatory.document.notfound.customMsg}")
	private String mandatoryDocumentNotFoundCustomMsg;

	@Value("${nonlegacy.update.customMsg}")
	private String nonLegacyUpdateCustomMsg;

	// kafka topics and keys
	@Value("${egov.services.tl-services.tradelicense.validated.topic}")
	private String tradeLicenseValidatedTopic;

	@Value("${egov.services.tl-services.tradelicense.legacy.validated.key}")
	private String legacyTradeLicenseValidatedKey;

	@Value("${egov.services.tl-services.tradelicense.new.validated.key}")
	private String newTradeLicenseValidatedKey;

	@Value("${egov.services.tl-services.tradelicense.workflow.populated.topic}")
	private String tradeLicenseWorkFlowPopulatedTopic;

	@Value("${egov.services.tl-services.tradelicense.persisted.topic}")
	private String tradeLicensePersistedTopic;

	@Value("${egov.services.tl-services.tradelicense.persisted.key}")
	private String tradeLicensePersistedKey;

	@Value("${egov.services.billing_service.hostname}")
	private String billingServiceHostName;

	@Value("${egov.services.billing_service.createbill}")
	private String billingServiceCreatedBill;
	
	@Value("${egov.services.billing_service.updatebill}")
	private String billingServiceUpdateBill;
	
	@Value("${egov.services.billing_service.searchbill}")
	private String billingServiceSearchBill;

	@Value("${businessService}")
	private String billBusinessService;
	
	@Value("${application.businessService}")
	private String applicationBusinessService;

	@Value("${tl.tax.head.master.code}")
	private String taxHeadMasterCode;
	
	@Value("${egov.services.egov_user.hostname}")
	private String userServiceHostName;

	@Value("${egov.services.egov_user.createpath}")
	private String userServiceCreatePath;
	
	@Value("${id.usernameFormat}")
    private String userNameFormat;
    
    @Value("${id.userName}")
    private String userNameService;
    
    @Value("${default.password}")
    private String defaultPassword;

	// tenant service
	@Value("${egov.services.tenant.service.hostname}")
	private String tenantServiceHostName;

	@Value("${egov.services.tenant.service.basepath}")
	private String tenantServiceBasePath;

	@Value("${egov.services.tenant.service.searchpath}")
	private String tenantServiceSearchPath;

	@Value("${egov.services.application.detail.missing.error}")
	private String applicationMissingErr;

	@Value("${egov.services.type.application.detail.missing.error}")
	private String applicationTypeMissingErr;

	// Error codes and messages
	@Value("${tl.error.licensevalidfromdate.notnull.code}")
	private String licenseValidFromDateNotNullCode;

	@Value("${tl.error.agreementdate.notvalid.code}")
	private String agreementDateNotValidCode;

	@Value("${tl.error.tradecommencementdate.notvalid.code}")
	private String tradeCommencementDateNotValidCode;

	@Value("${tl.error.licensevalidfromdate.notnull}")
	private String licenseValidFromDateNotNullMsg;

	@Value("${tl.error.agreementdate.notvalid}")
	private String agreementDateNotValidMsg;

	@Value("${tl.error.tradecommencementdate.notvalid}")
	private String tradeCommencementDateNotValidMsg;

	@Value("${kafka.topics.demandBill.update.name}")
	private String updateDemandBillTopicName;

	@Value("${egov.services.tl.search.pagesize.default}")
	private String tlSearchPageSizeDefault;

	@Value("${egov.services.tl.search.pageno.max}")
	private String tlSearchPageNumberDefault;

	@Value("${egov.services.tl.search.pagesize.max}")
	private String tlSearchPageSizeMax;

	@Value("${kafka.topics.noticedocument.create.name}")
	private String noticeDocumentCreateTopic;

	@Value("${kafka.topics.noticedocument.update.name}")
	private String noticeDocumentUpdateTopic;

	@Value("${tl.error.financialyear.notfound.code}")
	private String financialYearNotFoundCode;

	@Value("${tl.error.financialyear.notfound}")
	private String financialYearNotFoundMsg;

	@Value("${tl.error.feematrix.rates.notdefined.code}")
	private String feeMatrixRatesNotDefinedCode;

	@Value("${tl.error.feematrix.rates.notdefined}")
	private String feeMatrixRatesNotDefinedErrorMsg;
	
	@Value("${tl.error.feematrix.notdefined.code}")
	private String feeMatrixNotDefinedCode;

	@Value("${tl.error.feematrix.notdefined}")
	private String feeMatrixNotDefinedErrorMsg;
	
	@Value("${tl.error.fieldinspectionreport.notdefined.code}")
	private String fieldInspectionReportNotDefinedCode;
	
	@Value("${tl.error.fieldinspectionreport.notdefined}")
	private String fieldInspectionReportNotDefinedErrorMsg;
	
	@Value("${tl.error.uom.quantity.notdefined.code}")
	private String uomQuanityNotDefinedCode;
	
	@Value("${tl.error.uom.quantity.notdefined}")
	private String uomQuanityNotDefinedErrorMsg;

	@Value("${tl.error.feematrix.license.feetype.notdefined.code}")
	private String feeMatrixlicenseFeeTypeNotDefinedCode;
	
	@Value("${tl.error.feematrix.license.feetype.notdefined}")
	private String feeMatrixlicenseFeeTypeNotDefinedErrorMsg;
	
	@Value("${tl.error.licensefee.notzero.code}")
	private String licenseFeeNotZeroCode;
	
	@Value("${tl.error.licensefee.notzero}")
	private String licenseFeeNotZeroErrorMsg;
	
	@Value("${egov.services.tl.admin.hierarchy.key}")
	private String adminBoundryHierarchyKey;
	
	@Value("${egov.services.tl.location.hierarchy.key}")
	private String locationBoundryHierarchyKey;
	
	@Value("${egov.services.tl.revenue.hierarchy.key}")
	private String revenueBoundryHierarchyKey;
	
	@Value("${tl.application.fee.master.code}")
	private String applicationFeeMasterCode;
	
	@Value("${tl.application.fee.amount}")
	private String applicationFeeAmount;
	
	@Value("${tl.application.fee.enabled}")
	private String applicationFeeEnabled;
	
}