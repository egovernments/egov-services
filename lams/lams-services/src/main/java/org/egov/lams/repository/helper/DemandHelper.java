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

	
	public String DemandReasonParamsBuilder(AgreementRequest agreementRequest){
		
		Agreement agreement = agreementRequest.getAgreement();
		StringBuilder demandReasonquery = new StringBuilder();
		
		demandReasonquery.append("moduleName="+propertiesManager.getGetDemandModuleName());
		demandReasonquery.append("&taxCategory="+propertiesManager.getGetDemandModuleTax());
		if(agreement.getPaymentCycle()!=null)
			demandReasonquery.append("&installmentType="+agreement.getPaymentCycle());
		//FIXME tax period should be calculated using the payment cylce
		//Fromdate should come from commencementdate of agreement FIXME
 		/* private String taxPeriod;
		@JsonFormat(pattern = "dd/MM/yyyy")
		private Date fromDate;
		@JsonFormat(pattern = "dd/MM/yyyy")
		private Date toDate;
		*/
		return demandReasonquery.toString();
	}
	

}
