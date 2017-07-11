package org.egov.lams.repository.helper;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.model.Agreement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstallmetHelper {
	public static final Logger logger = LoggerFactory.getLogger(InstallmetHelper.class);

	@Autowired
	private PropertiesManager propertiesManager;

	public String getInstallmentUrlParams(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();

		logger.info("the criteria for current installment search are ::: " + "?module=" + propertiesManager.getModuleName()
				+ "&installmentType=" + agreement.getPaymentCycle().toString() + "&tenantId" + agreement.getTenantId()
				+ "&currentDate=" + new Date());

		StringBuilder urlParams = new StringBuilder();
		urlParams.append("?module=" + propertiesManager.getModuleName());
		urlParams.append("&installmentType=" + agreement.getPaymentCycle().toString());
		urlParams.append("&tenantId=" + agreement.getTenantId());
		urlParams.append("&currentDate=" + DateFormatUtils.format(new Date(), "dd/MM/yyyy"));
		return urlParams.toString();
	}

}
