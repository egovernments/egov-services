package org.egov.demand.domain.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.demand.persistence.entity.EgDemand;
import org.egov.demand.persistence.entity.EgDemandDetails;
import org.egov.demand.persistence.entity.EgDemandReason;
import org.egov.demand.persistence.entity.Installment;
import org.egov.demand.persistence.repository.DemandRepository;
import org.egov.demand.web.contract.Demand;
import org.egov.demand.web.contract.DemandDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandService {
	private static final Logger LOGGER = Logger.getLogger(DemandService.class);
	@Autowired
	private DemandRepository demandRepository;
	@Autowired
	private DemandReasonService demandReasonService;
	@Autowired
	private InstallmentService installmentService;

	public EgDemand createDemand(Demand demand) throws Exception {
		LOGGER.info("createDemand - demand - " + demand);
		EgDemand egDemand = new EgDemand();
		Installment demandInstallment = null;
		EgDemandDetails egDemandDetails = null;
		Set<EgDemandDetails> demandDetailsList = new HashSet<EgDemandDetails>();
		EgDemandReason demandReason;
		if (!demand.getModuleName().isEmpty() && !demand.getInstallment().isEmpty()) {
			demandInstallment = installmentService.findByDescriptionAndModuleAndTenantId(demand.getInstallment(),
					demand.getModuleName(), demand.getTenantId());
		}
		if (demandInstallment == null) {
			throw new Exception("Not a valid module or installment description");
		}
		egDemand.setEgInstallmentMaster(demandInstallment);
		egDemand.setIsHistory("N");
		egDemand.setCreateDate(new Date());
		egDemand.setModifiedDate(new Date());
		egDemand.setTenantId(demand.getTenantId());
		for (DemandDetails demandDetail : demand.getDemandDetails()) {
			if (demandDetail.getTaxAmount() != null && demandDetail.getTaxReason() != null
					&& !demandDetail.getTaxPeriod().isEmpty()) {
				demandReason = demandReasonService.findByCodeInstModule(demandDetail.getTaxReason(),
						demandDetail.getTaxPeriod(), demand.getModuleName(), demand.getTenantId());
				if (demandReason != null) {
					egDemandDetails = EgDemandDetails.fromReasonAndAmounts(demandDetail.getTaxAmount(), demandReason,
							BigDecimal.ZERO);
					egDemandDetails.setTenantId(demand.getTenantId());
					egDemandDetails.setEgDemand(egDemand);
					demandDetailsList.add(egDemandDetails);
				} else
					throw new Exception("Not a valid amount or demand reason details");
			}
		}
		egDemand.setEgDemandDetails(demandDetailsList);
		demandRepository.save(egDemand);
		LOGGER.info("createDemand - egDemand - " + egDemand);
		return egDemand;
	}

	public EgDemand updateDemandForCollection(Demand demand) throws Exception {
		EgDemand egDemand = demandRepository.findOne(demand.getId());
		for (DemandDetails demandDetails : demand.getDemandDetails()) {
			for (EgDemandDetails egDemandDetail : egDemand.getEgDemandDetails()) {
				if (egDemandDetail.getId().equals(demandDetails.getId())) {
					LOGGER.info("match is occuring in update service");
					egDemandDetail.addCollected(demandDetails.getCollectionAmount());
				}
			}
		}
		egDemand.addCollected(demand.getCollectionAmount());
		return demandRepository.save(egDemand);
	}

	public EgDemand updateDemand(Demand demand) throws Exception {
		EgDemand egDemand = demandRepository.findOne(demand.getId());
		for (DemandDetails demandDetails : demand.getDemandDetails()) {
			for (EgDemandDetails egDemandDetail : egDemand.getEgDemandDetails()) {
				if (egDemandDetail.getId().equals(demandDetails.getId())) {
					LOGGER.info("match is occuring in update service");
					egDemandDetail.setAmount(demandDetails.getTaxAmount());
					egDemandDetail.setAmtCollected(demandDetails.getCollectionAmount());
					egDemandDetail.setAmtRebate(demandDetails.getRebateAmount());
				}
			}
		}
		egDemand.addCollected(demand.getCollectionAmount());
		return demandRepository.save(egDemand);
	}
}
