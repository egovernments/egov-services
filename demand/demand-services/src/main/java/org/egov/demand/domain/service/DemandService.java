package org.egov.demand.domain.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.demand.persistence.entity.EgDemand;
import org.egov.demand.persistence.entity.EgDemandDetails;
import org.egov.demand.persistence.entity.EgDemandReason;
import org.egov.demand.persistence.entity.EgdmCollectedReceipt;
import org.egov.demand.persistence.entity.Installment;
import org.egov.demand.persistence.repository.DemandRepository;
import org.egov.demand.web.contract.Demand;
import org.egov.demand.web.contract.DemandDetails;
import org.egov.demand.web.contract.PaymentInfo;
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
		BigDecimal totalDemandCollection = BigDecimal.ZERO;
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
				demandReason = demandReasonService.findByCodeInstModule(demandDetail.getTaxReasonCode(),
						demandDetail.getTaxPeriod(), demand.getModuleName(), demand.getTenantId());
				if (demandReason != null) {
					egDemandDetails = EgDemandDetails.fromReasonAndAmounts(demandDetail.getTaxAmount(), demandReason,
							demandDetail.getCollectionAmount()!=null ? demandDetail.getCollectionAmount() : BigDecimal.ZERO);
					egDemandDetails.setTenantId(demand.getTenantId());
					egDemandDetails.setEgDemand(egDemand);
					totalDemandCollection=totalDemandCollection.add(demandDetail.getCollectionAmount());
					demandDetailsList.add(egDemandDetails);
				} else
					throw new Exception("Not a valid amount or demand reason details");
			}
		}
		LOGGER.info("total collection to update in demand by edit demand" + totalDemandCollection);
		egDemand.setAmtCollected(totalDemandCollection);
		egDemand.setEgDemandDetails(demandDetailsList);
		demandRepository.save(egDemand);
		LOGGER.info("createDemand - egDemand - " + egDemand);
		return egDemand;
	}

	public EgDemand updateDemandForCollection(Demand demand) throws Exception {
		EgDemand egDemand = demandRepository.findOne(demand.getId());
		LOGGER.info("demandDetails :" + demand.getDemandDetails().toString());
		for (DemandDetails demandDetails : demand.getDemandDetails()) {
			for (EgDemandDetails egDemandDetail : egDemand.getEgDemandDetails()) {
				if (egDemandDetail.getId().equals(demandDetails.getId())) {
					LOGGER.info("match is occuring in update service");
					LOGGER.info("collection :" + demandDetails.getCollectionAmount());
					egDemandDetail.addCollected(demandDetails.getCollectionAmount());
					LOGGER.info("payment info to update receipts" + demand.getPaymentInfos());
					if (!demand.getPaymentInfos().isEmpty()) {
						egDemandDetail.setEgdmCollectedReceipts(getCollectedReceipts(demand, egDemandDetail));
						LOGGER.info("back end receipt details" + egDemandDetail.getEgdmCollectedReceipts());
					}
				}
			}
		}
		egDemand.addCollected(demand.getCollectionAmount());
		return demandRepository.save(egDemand);
	}

	public EgDemand updateDemand(Demand demand) throws Exception {
		EgDemand egDemand = demandRepository.findOne(demand.getId());
		EgDemandDetails egDemandDetails = null;
		EgDemandReason demandReason;
		Boolean isDemandDetailExists = Boolean.FALSE;
		for (DemandDetails demandDetails : demand.getDemandDetails()) {
			isDemandDetailExists = Boolean.FALSE;
			for (EgDemandDetails egDemandDetail : egDemand.getEgDemandDetails()) {
				if (egDemandDetail.getId()!=null && egDemandDetail.getId().equals(demandDetails.getId())) {
					isDemandDetailExists = Boolean.TRUE;
					LOGGER.info("match is occuring in update service");
					egDemandDetail.setAmount(demandDetails.getTaxAmount());
					egDemandDetail.setAmtCollected(demandDetails.getCollectionAmount());
					egDemandDetail.setAmtRebate(demandDetails.getRebateAmount());
					LOGGER.info("demand details updated");
				}
			}
			// adding to demand if demanddetails does not exists
			if (!isDemandDetailExists) {
				demandReason = demandReasonService.findByCodeInstModule(demandDetails.getTaxReasonCode(),
						demandDetails.getTaxPeriod(), demand.getModuleName(), demand.getTenantId());
				
				LOGGER.info("new demand reason :" + demandReason);
				egDemandDetails = EgDemandDetails.fromReasonAndAmounts(demandDetails.getTaxAmount(), demandReason,
						demandDetails.getCollectionAmount()!=null ? demandDetails.getCollectionAmount() : BigDecimal.ZERO);
				LOGGER.info("new demand details :" + egDemandDetails);
				egDemand.addEgDemandDetails(egDemandDetails);
				egDemandDetails.setTenantId(demand.getTenantId());
				egDemandDetails.setEgDemand(egDemand);
			}
		}
		egDemand.addCollected(demand.getCollectionAmount());
		return demandRepository.save(egDemand);
	}

	private Set<EgdmCollectedReceipt> getCollectedReceipts(Demand demand, EgDemandDetails egDemandDetails) {
		Set<EgdmCollectedReceipt> egdmCollectedReceipts = new HashSet<>();

		List<PaymentInfo> paymentInfo = demand.getPaymentInfos();

		for (PaymentInfo info : paymentInfo) {
			EgdmCollectedReceipt receipt = new EgdmCollectedReceipt();
			for (DemandDetails demandsDetails : demand.getDemandDetails()) {

				if (info.getTaxPeriod().equalsIgnoreCase(demandsDetails.getTaxPeriod())
						&& info.getPurpose().equalsIgnoreCase(demandsDetails.getTaxReason())) {
					receipt.setEgdemandDetail(egDemandDetails);
					receipt.setReceiptNumber(info.getReceiptNumber());
					receipt.setReasonAmount(egDemandDetails.getAmtCollected());
					receipt.setReceiptDate(info.getReceiptDate());
					receipt.setUpdatedTime(new Date());
					receipt.setAmount(info.getCreditedAmount());
					receipt.setStatus(info.getStatus().charAt(0));
					receipt.setTenantId(egDemandDetails.getTenantId());
					egdmCollectedReceipts.add(receipt);
					LOGGER.info("adding receipt details " + receipt);
				}
			}
		}
		return egdmCollectedReceipts;
	}

}
