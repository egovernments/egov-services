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

	public DemandReasonCriteria getDemandReasonUrlParams(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		DemandReasonCriteria demandReasonCriteria = new DemandReasonCriteria();

		logger.info("the criteria for demandReasonSearch are ::: " + demandReasonCriteria);

		demandReasonCriteria.setModuleName(propertiesManager.getDemandModuleName());
		demandReasonCriteria.setTaxPeriod(agreement.getTimePeriod().toString());
		demandReasonCriteria.setFromDate(agreement.getCommencementDate());
		demandReasonCriteria.setToDate(agreement.getExpiryDate());
		demandReasonCriteria.setInstallmentType(agreement.getPaymentCycle().toString());
		demandReasonCriteria.setTaxCategory(propertiesManager.getTaxCategoryName());
		demandReasonCriteria.setTenantId(agreement.getTenantId());
		if (agreement.getSource().equals(Source.DATA_ENTRY))
			demandReasonCriteria.setTaxReason(propertiesManager.getTaxReasonName());

		return demandReasonCriteria;
	}
}
