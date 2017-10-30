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

	
	@Value("${egov.lcms.create.summon.validated}")
	private String createSummonvalidated;

	@Value("${egov.lcms.search.opinion.error.code}")
	private String opinionSearchErrorCode;
	
	@Value("${egov.lcms.code}")
	private String sortCode;
	
	@Value("${egov.lcms.json.error}")
	private String jsonStringError;
	
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
	private String  caseReferenceFormat;
	
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
	
	@Value("${case.mandatory.department.person.message}")
	private String isVakalatNamaRequiredCode;
	
	@Value("${case.mandatory.department.person.message}")
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
	
    @Value("${egov.lcms.case.load.key}")
    private String loadLegacyData;
	
}
