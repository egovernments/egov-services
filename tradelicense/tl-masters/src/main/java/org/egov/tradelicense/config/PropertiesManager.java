package org.egov.tradelicense.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@ToString
public class PropertiesManager {

	@Value("${spring.kafka.bootstrap.servers}")
	private String kafkaServerConfig;

	@Value("${spring.embedded.kafka.brokers}")
	private String kafkaEmbededBootServer;

	@Value("${auto.offset.reset.config}")
	private String kafkaOffsetConfig;

	@Value("${app.timezone}")
	private String appTimeZone;

	@Value("${invalid.input}")
	private String invalidInput;

	@Value("${invalid.category.validityYears.msg}")
	private String invalidValidityYears;

	@Value("${duplicate.code}")
	private String duplicateCode;

	@Value("${duplicate.name}")
	private String duplicateName;

	@Value("${invalid.range.code}")
	private String invalidRangeCode;

	@Value("${success.status}")
	private String successStatus;

	@Value("${failed.status}")
	private String failedStatus;

	@Value("${default.page.size}")
	private String defaultPageSize;

	@Value("${default.page.number}")
	private String defaultPageNumber;

	@Value("${default.offset}")
	private String defaultOffset;

	@Value("${duplicate.documentType.name}")
	private String documentTypeCustomMsg;

	@Value("${duplicate.uom.code}")
	private String uomCustomMsg;

	@Value("${duplicate.uom.name}")
	private String uomDuplicateNameErrorMsg;

	@Value("${duplicate.category.code}")
	private String categoryCustomMsg;

	@Value("${duplicate.category.name}")
	private String categoryNameDuplicate;
	
	@Value("${duplicate.subcategory.name}")
	private String subCategoryNameDuplicate;

	@Value("${duplicate.feematrix.code}")
	private String feeMatrixCustomMsg;

	@Value("${invalid.category.id.msg}")
	private String invalidCategoryIdMsg;

	@Value("${invalid.documenttype.id.msg}")
	private String invalidDocumentTypeIdMsg;

	@Value("${invalid.license.status.id.msg}")
	private String invalidLicenseStatusIdMsg;

	@Value("${invalid.uom.id.msg}")
	private String invalidUomIdMsg;
	
	@Value("${invalid.uom.msg}")
	private String invalidUomMsg;

	@Value("${invalid.parent.id.msg}")
	private String invalidParentIdMsg;

	@Value("${invalid.sequence.range.msg}")
	private String invalidSequenceRangeMsg;

	@Value("${invalid.applicationtype.msg}")
	private String invalidApplicationTypeMsg;

	@Value("${invalid.financialyear.msg}")
	private String invalidFinancialYearMsg;

	@Value("${duplicate.subcategory.code}")
	private String subCategoryCustomMsg;

	@Value("${duplicate.subcategory.detail}")
	private String duplicateSubCategoryDetail;

	@Value("${duplicate.businessnature.code}")
	private String businessNatureCustomMsg;

	@Value("${egov.services.egf-masters.hostname}")
	private String financialServiceHostName;

	@Value("${egov.services.egf-masters.basepath}")
	private String financialServiceBasePath;

	@Value("${egov.services.egf-masters.searchpath}")
	private String financialServiceSearchPath;

	@Value("${duplicate.license.status}")
	private String licenseStatusCustomMsg;

	@Value("${egov.tradelicense.category.create.validated}")
	private String createCategoryValidated;

	@Value("${egov.tradelicense.category.update.validated}")
	private String updateCategoryValidated;

	@Value("${egov.tradelicense.uom.create.validated}")
	private String createUomValidated;

	@Value("${egov.tradelicense.uom.update.validated}")
	private String updateUomValidated;

	@Value("${egov.tradelicense.documenttype.create.validated}")
	private String createDocumentTypeValidated;

	@Value("${egov.tradelicense.documenttype.update.validated}")
	private String updateDocumentTypeValidated;

	@Value("${egov.tradelicense.penaltyrate.create.validated}")
	private String createPenaltyRateValidated;

	@Value("${egov.tradelicense.penaltyrate.update.validated}")
	private String updatePenaltyRateValidated;

	@Value("${egov.tradelicense.licensestatus.create.validated}")
	private String createLicenseStatusValidated;

	@Value("${egov.tradelicense.licensestatus.update.validated}")
	private String updateLicenseStatusValidated;

	@Value("${egov.tradelicense.documenttypev2.create.validated}")
	private String createDocumentTypeV2Validated;

	@Value("${egov.tradelicense.documenttypev2.update.validated}")
	private String updateDocumentTypeV2Validated;

	@Value("${error.document.types.notfound}")
	private String documentTypeNotFoundMsg;

	@Value("${error.document.types.empty}")
	private String documentTypeEmptyMsg;

	@Value("${error.documenttype.category}")
	private String categoryErrorMsg;

	@Value("${error.documenttype.subcategory}")
	private String subCategoryErrorMsg;

	@Value("${document.create.success.status.message}")
	private String documentTypeCreateSuccessMessage;

	@Value("${document.update.success.status.message}")
	private String documentTypeUpdateSuccessMessage;

	@Value("${invalid.from.range.code}")
	private String invalidfromRangeCode;

	@Value("${invalid.rate.application.type}")
	private String invalidRateWithapp;

	@Value("${duplicate.penalty.rate}")
	private String duplicatePenaltyRate;

	@Value("${invalid.category.id.message}")
	private String categoryIdValidationMsg;

	@Value("${invalid.input.id.msg}")
	private String invalidIdMsg;

	@Value("${invalid.input.id.tenantid.msg}")
	private String invalidIdAndTenantIdMsg;

	@Value("${invalid.financialyear.error.msg}")
	private String financialYearErrorMsg;

	@Value("${duplicate.feematrix.record}")
	private String uniquenessErrorMsg;

	@Value("${egov.tradelicense.feematrix.create.validated.key}")
	private String feeMatrixCreateValidated;
	
	@Value("${error.feematrix.details.id.notfound.msg}")
	private String feeMatrixDetailsIdNotFoundMsg;
	
	@Value("${egov.tradelicense.feematrix.update.validated.key}")
	private String feeMatrixUpdateValidated;
	
	@Value("${egov.tradelicense.invalid.feematrix.msg}")
	private String invalidFeeMatrixMsg;
	
	@Value("${egov.tradelicense.feematrixdetail.delete.validated.key}")
	private String deleteFeeMatrixDetailsKey;
	
	@Value("${egov.tradelicense.endpoint.exception.msg}")
	private String endpointExceptionMsg;
	
	@Value("${egov.tradelicense.subcategory.inactive.msg}")
	private String subCategoryInactiveMsg;
	
	@Value("${invalid.category.type}")
	private String invalidCategoryTypeMessage;
	
	@Value("${invlaid.duplicate.categorydetail}")
	private String duplicateCategoryDetail;
	
	@Value("${egov.category.validityYears.nullcheck.msg}")
	private String validityYearNullCheckMsg;
	
	@Value("${already.exists.uom.code}")
	private String uomDuplicateCode;
	
	@Value("${already.exists.uom.name}")
	private String uomDuplicateName;

	@Value("${already.exists.category.code}")
	private String categoryDuplicateCode;
	
	@Value("${already.exists.category.name}")
	private String categoryDuplicateName;
	
	@Value("${already.exists.subcategory.code}")
	private String subCategoryDuplicateCode;
	
	@Value("${already.exists.subcategory.name}")
	private String subCategoryDuplicateName;
}