package org.egov.demand.web.validator;

import java.util.List;

import org.egov.demand.model.Bill;
import org.egov.demand.web.contract.BillRequest;
import org.springframework.stereotype.Component;

@Component
public class BillValidator {
	
	public void validateBillRequest(BillRequest billRequest) {
		validateBill(billRequest);
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
