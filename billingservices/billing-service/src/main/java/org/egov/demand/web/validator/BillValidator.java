package org.egov.demand.web.validator;

import java.util.List;

import org.egov.demand.model.Bill;
import org.egov.demand.model.GenerateBillCriteria;
import org.egov.demand.web.contract.BillRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class BillValidator {
	
	public void validateBillRequest(BillRequest billRequest) {
		validateBill(billRequest);
	}
	
	public void validateBillGenRequest(GenerateBillCriteria generateBillCriteria,Errors errors){
		if(generateBillCriteria.getMobileNumber() == null && generateBillCriteria.getEmail() == null && 
				generateBillCriteria.getBusinessService() == null && generateBillCriteria.getConsumerCode() == null){
			errors.rejectValue("businessService","BILL_GEN_MANDATORY_FIELDS_MISSING",
					"all the criteria fields missing, please give some valid criteria like "
					+ "mobileNumber,email,businessService,consumerCode");
		}else if((generateBillCriteria.getMobileNumber() == null && generateBillCriteria.getEmail() == null) 
				&& ((generateBillCriteria.getConsumerCode() != null && generateBillCriteria.getBusinessService() == null
					|| generateBillCriteria.getBusinessService() != null && generateBillCriteria.getConsumerCode() == null)))
					errors.rejectValue("consumerCode", "BILL_GEN_CONSUMERCODE_BUSINESSSERVICE",
							"the consumerCode & BusinessService values should be given together");
		
	}
	
	private void validateBill(BillRequest billRequest) {
		List<Bill> bills = billRequest.getBills();
		
		for(Bill bill : bills){
			
			if(bill.getIsActive()==null){
				bill.setIsActive(true);
			}
			if(bill.getIsCancelled()==null){
				bill.setIsCancelled(false);
			}
		}
		
	}
	
	private void validateBillDetails(BillRequest billRequest) {
		
		List<Bill> bills = billRequest.getBills();
		
		for(Bill bill : bills){
			
			if(bill.getIsActive()==null){
				
			}
		}
		
	}

}
