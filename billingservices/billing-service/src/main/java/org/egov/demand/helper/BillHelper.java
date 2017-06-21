package org.egov.demand.helper;

import java.util.List;

import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.Bill;
import org.egov.demand.model.BillAccountDetail;
import org.egov.demand.model.BillDetail;
import org.egov.demand.repository.GenIdServiceUtil;
import org.egov.demand.web.contract.BillRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BillHelper {
	
	@Autowired
	private GenIdServiceUtil genIdServiceUtil;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public void getBillRequestWithIds(BillRequest billRequest){
		List<Bill> bills = billRequest.getBillInfos();
		
		Integer totalNoOfBills = bills.size();
		Integer totalNoOfBillDetails = 0;
		Integer totalNoOfAccountDetails = 0;
		
		for(Bill bill:bills){
			List<BillDetail> billDetails = bill.getBillDetails();
			totalNoOfBillDetails = totalNoOfBillDetails + billDetails.size();
			for(BillDetail billDetail : billDetails){
				List<BillAccountDetail> accountDetails = billDetail.getBillAccountDetails();
				totalNoOfAccountDetails = totalNoOfAccountDetails + accountDetails.size();
			}
		}
		
		List<String> billIds = genIdServiceUtil.getNextValue(applicationProperties.getBillSeqName(), totalNoOfBills);
		List<String> billDetailIds = genIdServiceUtil.getNextValue(applicationProperties.getBillDetailSeqName(), totalNoOfBillDetails);
		List<String> billAccIds = genIdServiceUtil.getNextValue(applicationProperties.getBillAccDetailSeqName(), totalNoOfAccountDetails);
		List<String> billNumber = genIdServiceUtil.getNextValue(applicationProperties.getBillNumSeqName(), totalNoOfBillDetails);
		
		int billIndex = 0;
		int billDetailIndex = 0;
		int billAccIndex = 0;
		
		for(Bill bill:bills) {
			bill.setId(billIds.get(billIndex++));
			List<BillDetail> billDetails = bill.getBillDetails();
		
			for(BillDetail billDetail : billDetails){
				billDetail.setId(billDetailIds.get(billDetailIndex));
				billDetail.setBill(bill.getId());
				billDetail.setBillNumber(billNumber.get(billDetailIndex++));
				billDetail.setTenantId(bill.getTenantId());
				List<BillAccountDetail> accountDetails = billDetail.getBillAccountDetails();
				
				for(BillAccountDetail billAccountDetail : accountDetails){
					billAccountDetail.setId(billAccIds.get(billAccIndex++));
					billAccountDetail.setBillDetail(billDetail.getId());
					billAccountDetail.setTenantId(billDetail.getTenantId());
				}
				
			}
		}
		log.info("BillHelper bill:"+billIds);
		log.info("BillHelper billRequest:"+billRequest);
	}

	
}
