package org.egov.lams.repository.helper;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.enums.Source;
import org.egov.lams.service.AgreementService;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.DemandReasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemandHelper {

	public static final Logger logger = LoggerFactory.getLogger(AgreementService.class);

	@Autowired
	private PropertiesManager propertiesManager;

	public String getDemandReasonUrlParams(AgreementRequest agreementRequest) {
	
		Agreement agreement = agreementRequest.getAgreement();
	
		logger.info("the criteria for demandReasonSearch are ::: " + "?moduleName="
				+ propertiesManager.getDemandModuleName() + "&taxPeriod=" + agreement.getTimePeriod() + "&fromDate="
				+ agreement.getCommencementDate() + "&toDate=" + agreement.getExpiryDate() + "&installmentType="
				+ agreement.getPaymentCycle().toString() + "&taxCategory=" + propertiesManager.getTaxCategoryName());
	
		StringBuilder urlParams = new StringBuilder();
		urlParams.append("?moduleName=" + propertiesManager.getDemandModuleName());
		urlParams.append("&taxPeriod=" + agreement.getTimePeriod());
		urlParams.append("&fromDate=" + agreement.getCommencementDate());
		urlParams.append("&toDate=" + agreement.getExpiryDate());
		urlParams.append("&installmentType=" + agreement.getPaymentCycle().toString());
		urlParams.append("&taxCategory=" + propertiesManager.getTaxCategoryName());
		urlParams.append("&tenantId=" + agreement.getTenantId());
		if (agreement.getSource().equals(Source.DATA_ENTRY))
			urlParams.append("&taxReason=" + propertiesManager.getTaxReasonName());
		return urlParams.toString();
	}
}
