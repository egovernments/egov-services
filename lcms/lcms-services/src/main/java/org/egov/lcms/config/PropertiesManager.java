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
	
	@Value("${ulb.name}")
	private String ulbName;
	
	@Value("${ulb.format}")
	private String ulbFormat;

	@Value("${opinion.ulb.name}")
	private String opinionUlbName;
	
	@Value("${opinion.ulb.format}")
	private String opinionUlbFormat;
	
	@Value("${egov.lcms.opinion.create}")
	private String opinionCreateValidated;

	@Value("${egov.lcms.opinion.update}")
	private String opinionUpdateValidated;
	
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

}
