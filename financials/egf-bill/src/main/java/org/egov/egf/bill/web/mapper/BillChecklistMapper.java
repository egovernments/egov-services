package org.egov.egf.bill.web.mapper;

import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillChecklistSearch;
import org.egov.egf.bill.web.contract.BillChecklistContract;
import org.egov.egf.bill.web.contract.BillChecklistSearchContract;


public class BillChecklistMapper {
	
	public BillChecklist toDomain(BillChecklistContract contract) {
		
		BillChecklist billChecklist = new BillChecklist();
		
		billChecklist.setId(contract.getId());
		billChecklist.setBill(contract.getBill());
		billChecklist.setChecklist(contract.getChecklist());
		billChecklist.setChecklistValue(contract.getChecklistValue());
		
		return billChecklist;
	}

	public BillChecklistContract toContract(BillChecklist billChecklist) {
		
		BillChecklistContract contract = new BillChecklistContract();
		
		contract.setId(billChecklist.getId());
		contract.setBill(billChecklist.getBill());
		contract.setChecklist(billChecklist.getChecklist());
		contract.setChecklistValue(billChecklist.getChecklistValue());
		
		return contract;
	}
	
	public BillChecklistSearch toSearchDomain(BillChecklistSearchContract contract) {
		
		BillChecklistSearch billChecklistSearch = new BillChecklistSearch();
		
		billChecklistSearch.setId(contract.getId());
		billChecklistSearch.setBill(contract.getBill());
		billChecklistSearch.setChecklist(contract.getChecklist());
		billChecklistSearch.setChecklistValue(contract.getChecklistValue());
		
		return billChecklistSearch;
	}
	
	public BillChecklistSearchContract toSearchContract(BillChecklistSearch billChecklistSearch) {
		
		BillChecklistSearchContract contract = new BillChecklistSearchContract();
		
		contract.setId(billChecklistSearch.getId());
		contract.setBill(billChecklistSearch.getBill());
		contract.setChecklist(billChecklistSearch.getChecklist());
		contract.setChecklistValue(billChecklistSearch.getChecklistValue());
		
		return null;
	}
}
