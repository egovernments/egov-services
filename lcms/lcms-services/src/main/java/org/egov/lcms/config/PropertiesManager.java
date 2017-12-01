package org.egov.lcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

/**
 * 
 * @author Prasad
 *
 */
@Configuration
@Getter
public class PropertiesManager {

	@Value("${egov.services.tenant.hostname}")
	private String tenantHostName;

	@Value("${egov.services.tenant.basepath}")
	private String tenantBasePath;

	@Value("${egov.services.tenant.searchpath}")
	private String tenantSearchPath;

	@Value("${egov.services.egov_idgen.hostname}")
	private String idHostName;

	@Value("${egov.services.egov_idgen.createpath}")
	private String idCreatepath;

	@Value("${summon.code.name}")
	private String summonName;

	@Value("${opinion.ulb.name}")
	private String opinionUlbName;

	@Value("${opinion.ulb.format}")
	private String opinionUlbFormat;

	@Value("${egov.lcms.opinion.create}")
	private String opinionCreateValidated;

	@Value("${egov.lcms.opinion.update}")
	private String opinionUpdateValidated;

	@Value("${summom.code.format}")
	private String summonCodeFormat;

	@Value("${voucher.code.format}")
	private String voucherCodeFormat;

	@Value("${voucher.code.name}")
	private String voucherCodeFormatName;

	@Value("${egov.lcms.create.summon.validated}")
	private String createSummonvalidated;
	
	@Value("${egov.lcms.update.summon.validated}")
	private String updateSummonvalidated;

	@Value("${egov.lcms.search.opinion.error.code}")
	private String opinionSearchErrorCode;

	@Value("${egov.lcms.code}")
	private String sortCode;

	@Value("${egov.lcms.tenant.code}")
	private String tenantCode;

	@Value("${egov.lcms.tenant.mandatory.code}")
	private String tenantMandatoryCode;

	@Value("${egov.lcms.tenant.mandatory.message}")
	private String tenantMandatoryMessage;

	@Value("${egov.lcms.tenant.service.error}")
	private String tenantServiceErrorMsg;

	@Value("${egov.lcms.parawisecomment.create}")
	private String paraWiseCreateValidated;

	@Value("${egov.lcms.parawisecomment.update}")
	private String paraWiseUpdateValidated;

	@Value("${parawisecomment.ulb.name}")
	private String paraWiseCommentsUlbName;

	@Value("${parawisecomment.ulb.format}")
	private String paraWiseCommentsUlbFormat;

	@Value("${egov.lcms.search.payment.error.code}")
	private String paymentSearchErrorCode;

	@Value("${egov.lcms.advocate.payment.create.key}")
	private String advocatePaymentCreate;

	@Value("${egov.lcms.advocate.payment.update.key}")
	private String advocatePaymentUpdate;

	@Value("${advocate.payment.ulb.format}")
	private String advocatePaymentUlbFormat;

	@Value("${advocate.payment.ulb.name}")
	private String AdvocatePaymentUlbName;

	@Value("${egov.lcms.update.summon}")
	private String updateSummonValidate;

	@Value("${egov.lcms.create.vakalatnama}")
	private String createVakalatnama;

	@Value("${summon.ref.name}")
	private String summonReferenceGenName;

	@Value("${summon.ref.format}")
	private String summonRefrenceFormat;

	@Value("${egov.lcms.assign.advocate}")
	private String assignAdvocate;

	@Value("${case.ref.name}")
	private String caseReferenceGenName;

	@Value("${case.ref.format}")
	private String caseReferenceFormat;

	@Value("${case.ref.name}")
	private String caseCodeName;

	@Value("${case.ref.format}")
	private String caseCodeFormat;

	@Value("${egov.lcms.object.parse.exception}")
	private String parsingError;

	@Value("${case.mandatory.reg.date.message}")
	private String requiedCaseGenerationDateMessage;

	@Value("${case.mandatory.reg.date.code}")
	private String requiredCaseGenerationCode;

	@Value("${case.mandatory.department.person.code}")
	private String requiredDepartmentPersonCode;

	@Value("${case.mandatory.department.person.message}")
	private String requiredDepartmentPersonMessage;

	@Value("${vakalatnama.isgenerate.code}")
	private String isVakalatNamaRequiredCode;

	@Value("${vakalatnama.isgenerate.message}")
	private String isVakalatNamaRequiredMessage;

	@Value("${advocate.ulb.name}")
	private String advocateUlbName;

	@Value("${advocate.ulb.format}")
	private String advocateUlbFormat;

	@Value("${egov.lcms.create.advocate}")
	private String createAdvocateTopic;

	@Value("${egov.lcms.update.advocate}")
	private String updateAdvocateTopic;

	@Value("${invalid.tenant.code}")
	private String invalidTenantCode;

	@Value("${exception.message}")
	private String exceptionMessage;

	@Value("${invalid.idgeneration.code}")
	private String invalidIdGenerationCode;

	@Value("${idgeneration.exception.message}")
	private String idGenerationExceptionMessage;

	@Value("${invalid.organization.code}")
	private String invalidOrganizationCode;

	@Value("${organization.exception.message}")
	private String organizationExceptionMessage;

	@Value("${register.ulb.name}")
	private String registerUlbName;

	@Value("${register.ulb.format}")
	private String registerUlbFormat;

	@Value("${egov.lcms.create.register}")
	private String createRegisterTopic;

	@Value("${egov.lcms.update.register}")
	private String updateRegisterTopic;

	@Value("${default.page.size}")
	private String defaultPageSize;

	@Value("${default.page.number}")
	private String defaultPageNumber;

	@Value("${default.offset}")
	private String defaultOffset;

	@Value("${egov.lcms.hearingdetails.create}")
	private String hearingCreateValidated;

	@Value("${egov.lcms.hearingdetails.update}")
	private String hearingUpdateValidated;

	@Value("${hearingdetails.ulb.name}")
	private String hearingDetailsUlbName;

	@Value("${hearingdetails.ulb.format}")
	private String hearingDetailsUlbFormat;

	@Value("${notice.ulb.name}")
	private String noticeUlbName;

	@Value("${notice.ulb.format}")
	private String noticeUlbFormat;

	@Value("${egov.lcms.create.notice}")
	private String createNoticeTopic;

	@Value("${advocate.assign.details.code}")
	private String advocateDetailsCodeName;

	@Value("${advocate.assign.details.code.format}")
	private String advocateDetailsCodeFormat;

	@Value("${egov.lcms.legacy.case.create}")
	private String createLegacyCase;

	@Value("${egov.lcms.legacy.hearing.create}")
	private String createLegacyHearing;

	@Value("${egov.lcms.legacy.case.voucher.create}")
	private String createLegacyCaseVoucher;

	@Value("${egov.lcms.legacy.case.advocate.create}")
	private String createLegacyCaseAdvocate;

	@Value("${egov.lcms.update.notice}")
	private String updateNoticeTopic;

	@Value("${invalid.tenant.ulb.code}")
	private String invalidTenantUlbCode;

	@Value("${tenant.ulb.exception.message}")
	private String tenantUlbExceptionMessage;

	@Value("${egov.lcms.courtname.mandatory.code}")
	private String courtnameCode;

	@Value("${egov.lcms.courtname.mandatory.message}")
	private String courtnameMessage;

	@Value("${egov.lcms.defendant.mandatory.code}")
	private String defendentCode;

	@Value("${egov.lcms.defendant.mandatory.message}")
	private String defendentMessage;

	@Value("${egov.lcms.ward.mandatory.code}")
	private String wardCode;

	@Value("${egov.lcms.ward.mandatory.message}")
	private String wardMessage;

	@Value("${egov.lcms.bench.mandatory.code}")
	private String benchCode;

	@Value("${egov.lcms.bench.mandatory.message}")
	private String benchMessage;

	@Value("${egov.lcms.caseno.mandatory.code}")
	private String casenoCode;

	@Value("${egov.lcms.caseno.mandatory.message}")
	private String casenoMessage;

	@Value("${egov.lcms.casetype.mandatory.code}")
	private String casetypeCode;

	@Value("${egov.lcms.casetype.mandatory.message}")
	private String casetypeMessage;

	@Value("${egov.lcms.casedetails.mandatory.code}")
	private String casedetailsCode;

	@Value("${egov.lcms.casedetails.mandatory.message}")
	private String casedetailsMessage;

	@Value("${egov.lcms.plaintiff.mandatory.code}")
	private String plaintiffCode;

	@Value("${egov.lcms.plaintiff.mandatory.message}")
	private String plaintiffMessage;

	@Value("${egov.lcms.plaintiffaddress.mandatory.code}")
	private String plaintiffaddressCode;

	@Value("${egov.lcms.plaintiffaddress.mandatory.message}")
	private String plaintiffaddressMessage;

	@Value("${egov.lcms.year.mandatory.code}")
	private String yearCode;

	@Value("${egov.lcms.year.mandatory.message}")
	private String yearMessage;

	@Value("${egov.lcms.side.mandatory.code}")
	private String sideCode;

	@Value("${egov.lcms.side.mandatory.message}")
	private String sideMessage;

	@Value("${egov.lcms.hearingdate.mandatory.code}")
	private String hearingdateCode;

	@Value("${egov.lcms.hearingdate.mandatory.message}")
	private String hearingdateMessage;

	@Value("${egov.lcms.hearingtime.mandatory.code}")
	private String hearingtimeCode;

	@Value("${egov.lcms.hearingtime.mandatory.message}")
	private String hearingtimeMessage;

	@Value("${egov.lcms.stamp.mandatory.code}")
	private String stampCode;

	@Value("${egov.lcms.stamp.mandatory.message}")
	private String stampMessage;

	@Value("${egov.lcms.summondate.mandatory.code}")
	private String summondateCode;

	@Value("${egov.lcms.summondate.mandatory.message}")
	private String summondateMessage;

	@Value("${egov.lcms.departmentname.mandatory.code}")
	private String departmentNameCode;

	@Value("${egov.lcms.departmentname.mandatory.message}")
	private String departmentNameMessage;

	@Value("${egov.lcms.sectionapplied.mandatory.code}")
	private String sectionappliedCode;

	@Value("${egov.lcms.sectionapplied.mandatory.message}")
	private String sectionappliedMessage;

	@Value("${egov.lcms.refrenceno.mandatory.code}")
	private String refrencenoCode;

	@Value("${egov.lcms.refrenceno.mandatory.message}")
	private String refrencenoMessage;

	@Value("${egov.lcms.summonrefrenceno.mandatory.code}")
	private String summonrefrencenoCode;

	@Value("${egov.lcms.summonrefrenceno.mandatory.message}")
	private String summonrefrencenoMessage;

	@Value("${egov.lcms.chiefofficerdetails.mandatory.code}")
	private String chiefofficerdetailsCode;

	@Value("${egov.lcms.chiefofficerdetails.mandatory.message}")
	private String chiefofficerdetailsMessage;

	@Value("${egov.lcms.refrencecaseno.mandatory.code}")
	private String refrencecasenoCode;

	@Value("${egov.lcms.refrencecaseno.mandatory.message}")
	private String refrencecasenoMessage;

	@Value("${egov.lcms.witness.mandatory.code}")
	private String witnessCode;

	@Value("${egov.lcms.witness.mandatory.message}")
	private String witnessMessage;

	@Value("${egov.lcms.advocatename.mandatory.code}")
	private String advocatenameCode;

	@Value("${egov.lcms.advocatename.mandatory.message}")
	private String advocatenameMessage;

	@Value("${egov.lcms.department.mandatory.code}")
	private String departmenteCode;

	@Value("${egov.lcms.department.mandatory.message}")
	private String departmentMessage;

	@Value("${egov.lcms.days.mandatory.code}")
	private String daysCode;

	@Value("${egov.lcms.days.mandatory.message}")
	private String daysMessage;

	@Value("${egov.lcms.assign.advocate.mandatory.code}")
	private String advocateDetailsMandatorycode;

	@Value("${egov.lcms.assign.advocate.mandatory.message}")
	private String advocateDetailsMandatoryMessage;

	@Value("${egov.lcms.assign.advocatedetails.advocate.code}")
	private String advocateMandatoryCode;

	@Value("${egov.lcms.assign.advocatedetails.advocate.message}")
	private String advocateMandatoryMessage;

	@Value("${egov.lcms.assign.advocatedetails.assigndate.code}")
	private String advocateAssignDateCode;

	@Value("${egov.lcms.assign.advocatedetails.assigndate.message}")
	private String advocateAssignDateMessage;

	@Value("${lcms.cases.error.code}")
	private String caseResponseErrorCode;

	@Value("${lcms.parawisecomments.error.code}")
	private String paraWiseResponseErrorCode;

	@Value("${lcms.hearingdetails.error.code}")
	private String hearingDetailsResponseErrorCode;

	@Value("${lcms.advocatedetails.error.code}")
	private String advocateDetailsResponseErrorCode;

	@Value("${lcms.casevoucher.error.code}")
	private String caseVoucherResponseErrorCode;

	@Value("${lcms.advocatedetails.error.msg}")
	private String advocateDetailsResponseErrorMsg;

	@Value("${lcms.hearingdetails.error.msg}")
	private String hearingDetailsResponseErrorMsg;

	@Value("${lcms.parawisecomments.error.msg}")
	private String paraWiseCommentsResponseErrorMsg;

	@Value("${lcms.casevoucher.error.msg}")
	private String caseVoucherResponseErrorMsg;

	@Value("${lcms.opinion.error.msg}")
	private String opinionSearchErrorMsg;

	@Value("${lcms.advocate.error.code}")
	private String advocateErrorCode;

	@Value("${lcms.advocate.error.msg}")
	private String advocateErrorMsg;

	@Value("${lcms.notice.error.code}")
	private String noticeErrorMsg;

	@Value("${lcms.notice.error.msg}")
	private String noticeErrorCode;

	@Value("${lcms.register.error.code}")
	private String registerErrorCode;

	@Value("${lcms.register.error.msg}")
	private String registerErrorMsg;

	@Value("${egov.services.common-masters.hostname}")
	private String commonServiceBasepath;

	@Value("${egov.services.common.masters.base.path}")
	private String commonServiceSearchPath;

	@Value("${egov.services.mdms-services.hostname}")
	private String mdmsBasePath;

	@Value("${egov.services.mdms.searchpath}")
	private String mdmsSearhPath;

	@Value("${egov.lcms.create.summon.indexer}")
	private String pushSummonCreateToIndexer;

	@Value("${egov.lcms.legacy.case.create.indexer}")
	private String createLegacyCaseIndexer;

	@Value("${egov.services.department.search.code}")
	private String searchDeparatmentErrorCode;

	@Value("${egov.services.department.search.message}")
	private String searchDepartmentErrorMessage;

	@Value("${egov.lcms.evidence.ulb.format}")
	private String evidenceUlbFormat;

	@Value("${egov.lcms.evidence.ulb.name}")
	private String evidenceUlbName;

	@Value("${egov.lcms.evidence.create}")
	private String evidenceCreateTopic;

	@Value("${egov.lcms.evidence.update}")
	private String evidenceUpdateTopic;

	@Value("${egov.lcms.evidence.error.code}")
	private String evidenceResponseErrorCode;

	@Value("${egov.lcms.evidence.error.msg}")
	private String evidenceResponseErrorMsg;

	@Value("${egov.lcms.lastmodifiedtime}")
	private String lastModifiedTime;

	@Value("${egov.lcms.update.assign.advocate}")
	private String updateAssignAdvocate;

	@Value("${egov.lcms.assign.advocatedetails.size}")
	private String advocateDetailsSize;

	@Value("${egov.lcms.assign.advocatedetails.size.message}")
	private String advocateDetailsSizeMessage;

	@Value("${egov.lcms.casedetails.error.code}")
	private String caseDetailsResponseErrorCode;

	@Value("${egov.lcms.casedetails.error.msg}")
	private String caseDetailsResponseErrorMsg;

	@Value("${egov.lcms.caseno.error.code}")
	private String caseNoErrorCode;

	@Value("${egov.lcms.caseno.error.msg}")
	private String caseNoErrorMsg;

	@Value("${egov.agency.ulb.format}")
	private String agencyUlbFormat;

	@Value("${egov.agency.ulb.name}")
	private String agencyUlbName;

	@Value("${egov.personDetails.ulb.format}")
	private String personDetailsUlbFormat;

	@Value("${egov.personDetails.ulb.name}")
	private String personDetailsUlbName;

	@Value("${egov.agency.created}")
	private String agencyCreated;

	@Value("${egov.agency.updated}")
	private String agencyUpdated;

	@Value("${egov.personaldetails.create}")
	private String createPersonalDetailsTopic;

	@Value("${egov.agencyaddress.error.code}")
	private String agencyAddressErrorCode;

	@Value("${egov.agencyaddress.error.msg}")
	private String agencyAddressErrorMsg;

	@Value("${egov.agencyname.error.code}")
	private String agencyNameErrorCode;

	@Value("${egov.agencyname.error.msg}")
	private String agencyNameErrorMsg;

	@Value("${egov.lcms.empanelment.error.code}")
	private String empanelmentErrorCode;

	@Value("${egov.lcms.empanelment.error.msg}")
	private String empanelmentErrorMsg;

	@Value("${egov.lcms.bankdetails.error.code}")
	private String bankDetailsErrorCode;

	@Value("${egov.lcms.bankdetails.error.msg}")
	private String bankDetailsErrorMsg;

	@Value("${egov.lcms.agency.substring.code}")
	private String agencySubStringCode;

	@Value("${egov.lcms.advocate.substring.code}")
	private String advocateSubStringCode;

	@Value("${egov.lcms.event.ulb.format}")
	private String eventUlbFormat;

	@Value("${egov.lcms.event.ulb.name}")
	private String eventUlbName;

	@Value("${egov.lcms.module.name}")
	private String lcmsModuleName;

	@Value("${egov.lcms.case.entity.name}")
	private String caseEntityName;

	@Value("${egov.lcms.event.created}")
	private String eventCreateValidated;

	@Value("${egov.lcms.event.error.code}")
	private String eventResponseErrorCode;

	@Value("${egov.lcms.event.error.msg}")
	private String eventResponseErrorMsg;

	@Value("${default.title}")
	private String defaultTitle;

	@Value("${egov.lcms.agency.status}")
	private String agencyStatus;

	@Value("${egov.lcms.module.name}")
	private String lCMSModuleName;

	@Value("${egov.common.master.module.name}")
	private String commonMasterModuleName;

	@Value("${summon.type}")
	private String summonType;

	@Value("${warrant.type}")
	private String warrantType;

	@Value("${egov.lcms.assignadvocate.duplicate.code}")
	private String duplicateAdvocate;

	@Value("${egov.lcms.assignadvocate.duplicate.message}")
	private String duplicateAdvocateMessage;
}
