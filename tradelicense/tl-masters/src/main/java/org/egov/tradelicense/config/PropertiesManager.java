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

	@Value("${egov.tradelicense.feematrix.create.validated}")
	private String createFeeMatrixValidated;

	@Value("${egov.tradelicense.feematrix.update.validated}")
	private String updateFeeMatrixValidated;

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

}