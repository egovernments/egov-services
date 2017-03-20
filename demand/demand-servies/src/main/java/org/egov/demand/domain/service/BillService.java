package org.egov.demand.domain.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.demand.persistence.entity.EgBill;
import org.egov.demand.persistence.entity.EgDemand;
import org.egov.demand.persistence.repository.BillRepository;
import org.egov.demand.persistence.repository.BillTypeRepository;
import org.egov.demand.persistence.repository.DemandRepository;
import org.egov.demand.web.contract.BillAddlInfo;
import org.egov.demand.web.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {
	private static final Logger LOGGER = Logger.getLogger(BillService.class);
	@Autowired
	private BillRepository billRepository;
	@Autowired
	private DemandRepository demandRepository;
	@Autowired
	private ModuleRepository moduleRepository;
	@Autowired
	private BillTypeRepository billTypeRepository;

	public BillAddlInfo createBill(Long demandId, BillAddlInfo billAddlInfo) throws Exception {
		EgBill egBill = new EgBill(billAddlInfo);
		EgDemand egDemand = demandRepository.findOne(demandId);
		egBill.setEgDemand(egDemand);
		egBill.setModule(moduleRepository.fetchModuleByName(billAddlInfo.getModuleName()).getId());
		egBill.setEgBillType(billTypeRepository.findByName(billAddlInfo.getBillType()));
		egBill.setCreateDate(new Date());
		egBill.setModifiedDate(new Date());
		return billRepository.save(egBill).toDomain(egBill);
	}
}
