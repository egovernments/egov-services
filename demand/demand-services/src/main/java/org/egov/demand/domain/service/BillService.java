package org.egov.demand.domain.service;

import org.apache.log4j.Logger;
import org.egov.demand.persistence.entity.EgBill;
import org.egov.demand.persistence.entity.EgBillDetails;
import org.egov.demand.persistence.entity.EgBillType;
import org.egov.demand.persistence.repository.BillRepository;
import org.egov.demand.persistence.repository.BillTypeRepository;
import org.egov.demand.web.contract.BillDetailInfo;
import org.egov.demand.web.contract.BillInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {
	private static final Logger LOGGER = Logger.getLogger(BillService.class);
	@Autowired
	private BillRepository billRepository;
	@Autowired
	private BillTypeRepository billTypeRepository;

	public EgBill createBill(Long demandId, BillInfo billInfo) {
		EgBill egBill = new EgBill(billInfo);
		List<EgBillDetails> billDetails = new ArrayList<>();
		egBill.setEgDemand(demandId);
		egBill.setModule(billInfo.getModuleName());
		LOGGER.info("BillService before findbnamemethod call getBillType ::"+ billInfo.getBillType()+"billInfo.getTenantId()" + billInfo.getTenantId());
		EgBillType egBillType = billTypeRepository.findByNameAndTenantId(billInfo.getBillType(), billInfo.getTenantId());
		LOGGER.info("BillService  the egbilltype response object from findbnamemethod call ::"+egBillType);
		egBill.setEgBillType(egBillType);
		egBill.setUserId(1l);
		egBill.setTenantId(billInfo.getTenantId());
		for (BillDetailInfo billDetailInfo : billInfo.getBillDetailInfos()) {
			EgBillDetails egBillDetail = new EgBillDetails(billDetailInfo);
			egBillDetail.setEgBill(egBill);
			egBillDetail.setTenantId(billInfo.getTenantId());
			billDetails.add(egBillDetail);
		}
		egBill.setEgBillDetails(billDetails);
		return billRepository.save(egBill);
	}

	public EgBill updateBill( EgBill bill, BillInfo billInfo) {
		bill.setIs_Cancelled(billInfo.getCancelled());
		return billRepository.save(bill);
	}
}