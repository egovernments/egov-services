package org.egov.lams.repository.helper;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.web.contract.AgreementRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemandHelper {
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	
	public String getDemandReasonUrlParams(AgreementRequest agreementRequest){
		
		Agreement agreement = agreementRequest.getAgreement();
	
		StringBuilder urlParams =  new StringBuilder();
		urlParams.append("?moduleName=" + propertiesManager.getDemandModuleName());
		urlParams.append("&taxPeriod=" + agreement.getTimePeriod());
		urlParams.append("&fromDate=" + agreement.getCommencementDate());
		urlParams.append("&toDate=" + agreement.getCloseDate());
		urlParams.append("&installmentType=" + agreement.getPaymentCycle().toString());
		urlParams.append("&taxCategory=" + propertiesManager.getTaxCategoryName());
		
		return urlParams.toString();
	}
}
