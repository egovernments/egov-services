package org.egov.demand.web.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.demand.model.BillSearchCriteria;
import org.egov.demand.model.GenerateBillCriteria;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class BillValidator {
	
	/**
	 * validtes the bill gen criteria
	 * 
	 * @param generateBillCriteria
	 */
	public void validateBillGenRequest(GenerateBillCriteria generateBillCriteria) {

		Map<String, String> errorMap = new HashMap<>();

		if (generateBillCriteria.getMobileNumber() == null && generateBillCriteria.getEmail() == null
				&& generateBillCriteria.getBusinessService() == null
				&& generateBillCriteria.getConsumerCode() == null) {

			errorMap.put("BILL_GEN_MANDATORY_FIELDS_MISSING",
					"all the criteria fields missing, please give some valid criteria like "
							+ "mobileNumber,email,businessService,consumerCode");

		} else if ((generateBillCriteria.getMobileNumber() == null && generateBillCriteria.getEmail() == null)
				&& ((generateBillCriteria.getConsumerCode() != null && generateBillCriteria.getBusinessService() == null)
				|| (generateBillCriteria.getBusinessService() != null && generateBillCriteria.getConsumerCode() == null))) {
			errorMap.put("BILL_GEN_CONSUMERCODE_BUSINESSSERVICE",
					"the consumerCode & BusinessService values should be given together");
		}

	}
	
	public void validateBillSearchCriteria(BillSearchCriteria billCriteria,Errors errors){
		
		if(billCriteria.getBillId() == null && billCriteria.getBillType() == null && billCriteria.getIsActive() == null 
				&& billCriteria.getIsCancelled() == null && billCriteria.getService() == null
				&& billCriteria.getConsumerCode() == null){
			errors.rejectValue("service","BILL_SEARCH_MANDATORY_FIELDS_MISSING",
					"Any one of the fields additional to tenantId is mandatory like service,"
					+ "consumerCode,billId,isActiv,isCanceled");
		}else if((billCriteria.getConsumerCode() != null && billCriteria.getService() == null)
					|| (billCriteria.getService() != null && billCriteria.getConsumerCode() == null))
					errors.rejectValue("consumerCode", "BILL_SEARCH_CONSUMERCODE_BUSINESSSERVICE",
							"the consumerCode & Service values should be given together");
	}
	
}
