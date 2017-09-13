package org.egov.egf.bill.web.mapper;

import org.egov.egf.bill.domain.model.BillDetail;
import org.egov.egf.bill.domain.model.BillDetailSearch;
import org.egov.egf.bill.web.contract.BillDetailContract;
import org.egov.egf.bill.web.contract.BillDetailSearchContract;

public class BillDetailMapper {

	public BillDetail toDomain(BillDetailContract contract) {
		
		BillDetail billDetail = new BillDetail();
		
		billDetail.setId(contract.getId());
		billDetail.setOrderId(contract.getOrderId());
		billDetail.setChartOfAccount(contract.getChartOfAccount());
		billDetail.setGlcode(contract.getGlcode());
		billDetail.setDebitAmount(contract.getDebitAmount());
		billDetail.setCreditAmount(contract.getCreditAmount());
		billDetail.setFunction(contract.getFunction());
		
		return billDetail;
	}
	
	public BillDetailContract toContract(BillDetail billDetail) {
		
		BillDetailContract contract = new BillDetailContract();
		
		contract.setId(billDetail.getId());
		contract.setOrderId(billDetail.getOrderId());
		contract.setChartOfAccount(billDetail.getChartOfAccount());
		contract.setGlcode(billDetail.getGlcode());
		contract.setDebitAmount(billDetail.getDebitAmount());
		contract.setCreditAmount(billDetail.getCreditAmount());
		contract.setFunction(billDetail.getFunction());
		
		return contract;
	}
	
	public BillDetailSearch toSearchDomain(BillDetailSearchContract contract) {
		
		BillDetailSearch billDetailSearch = new BillDetailSearch();
		
		billDetailSearch.setId(contract.getId());
		billDetailSearch.setOrderId(contract.getOrderId());
		billDetailSearch.setChartOfAccount(contract.getChartOfAccount());
		billDetailSearch.setGlcode(contract.getGlcode());
		billDetailSearch.setDebitAmount(contract.getDebitAmount());
		billDetailSearch.setCreditAmount(contract.getCreditAmount());
		billDetailSearch.setFunction(contract.getFunction());
		
		return billDetailSearch;
	}
	
	public BillDetailSearchContract toSearchContract(BillDetailSearch billDetailSearch) {
		
		BillDetailSearchContract contract = new BillDetailSearchContract();
		
		contract.setId(billDetailSearch.getId());
		contract.setOrderId(billDetailSearch.getOrderId());
		contract.setChartOfAccount(billDetailSearch.getChartOfAccount());
		contract.setGlcode(billDetailSearch.getGlcode());
		contract.setDebitAmount(billDetailSearch.getDebitAmount());
		contract.setCreditAmount(billDetailSearch.getCreditAmount());
		contract.setFunction(billDetailSearch.getFunction());
		
		return contract;
	}
}
