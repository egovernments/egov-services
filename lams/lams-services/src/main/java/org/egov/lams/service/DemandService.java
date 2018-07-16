package org.egov.lams.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.DemandReason;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Source;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandService {
	public static final Logger logger = LoggerFactory.getLogger(DemandService.class);
	
	private static final List<String> DEMAND_REASONS = Arrays.asList("RENT","PENALTY","STATE_GST","CENTRAL_GST","SERVICE_TAX");
	private static final String CENTRAL_GST = "CENTRAL_GST";
	private static final String STATE_GST = "STATE_GST";

	@Autowired
	private DemandRepository demandRepository;

	@Autowired
	private PropertiesManager propertiesManager;

	public List<Demand> prepareDemands(AgreementRequest agreementRequest) {

		List<Demand> demands = null;
		List<DemandDetails> oldDetails = new ArrayList<>();
		Agreement agreement = agreementRequest.getAgreement();
		List<String> demandIds = agreement.getDemands();

		if (demandIds == null) {
			demands = demandRepository.getDemandList(agreementRequest, getDemandReasons(agreementRequest));
		} else if (agreement.getSource().equals(Source.SYSTEM)) {
			DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
			demandSearchCriteria.setDemandId(Long.parseLong(demandIds.get(0)));
			demands = demandRepository.getDemandBySearch(demandSearchCriteria, agreementRequest.getRequestInfo())
					.getDemands();
			if (agreement.getAction().equals(Action.RENEWAL)) {
				for (DemandDetails demandDetails : demands.get(0).getDemandDetails()) {
					if (!demandDetails.getTaxAmount().equals(demandDetails.getCollectionAmount()))
						oldDetails.add(demandDetails);
				}
			}

			logger.info("the demand list after getting demandsearch result : " + demands);
			demands = demandRepository.getDemandList(agreementRequest, getDemandReasons(agreementRequest));
			demands.get(0).getDemandDetails().addAll(oldDetails);

		} else if (agreement.getSource().equals(Source.DATA_ENTRY)) {
			DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
			demandSearchCriteria.setDemandId(Long.parseLong(demandIds.get(0)));
			demands = demandRepository.getDemandBySearch(demandSearchCriteria, agreementRequest.getRequestInfo())
					.getDemands();
		}
		return demands;
	}
	
	public List<DemandDetails> prepareDemandDetailsForRenewal(AgreementRequest agreementRequest, List<DemandReason> demandReasons){
		List<DemandDetails> demandDetails = new ArrayList<>();
		DemandDetails demandDetail;
		for (DemandReason demandReason : demandReasons) {
			demandDetail = new DemandDetails();
			demandDetail.setCollectionAmount(BigDecimal.ZERO);
			demandDetail.setRebateAmount(BigDecimal.ZERO);
			demandDetail.setTaxReason(demandReason.getName());
			demandDetail.setTaxReasonCode(demandReason.getName());
			demandDetail.setTaxPeriod(demandReason.getTaxPeriod());
			demandDetail.setTaxAmount(BigDecimal.valueOf(agreementRequest.getAgreement().getRent()));
			demandDetails.add(demandDetail);
		}
		return demandDetails;
	}

	public List<DemandReason> getDemandReasons(AgreementRequest agreementRequest) {
		List<DemandReason> demandReasons = demandRepository.getDemandReason(agreementRequest);
		if (demandReasons.isEmpty())
			throw new RuntimeException("No demand reason found for given criteria");
		logger.info("the size of demand reasons obtained from reason search api call : " + demandReasons.size());
		return demandReasons;
	}

	public List<Demand> prepareDemandsByApprove(AgreementRequest agreementRequest) {
		List<Demand> demands = null;
		List<DemandDetails> demandDetails;
		Agreement agreement = agreementRequest.getAgreement();
		List<String> demandIds = agreement.getDemands();
		Date effectiveDate = null;
		BigDecimal revisedRent = BigDecimal.ZERO;
		BigDecimal effectiveCollection = BigDecimal.ZERO;
		if (Action.OBJECTION.equals(agreement.getAction())) {
			effectiveDate = agreement.getObjection().getEffectiveDate();
			revisedRent = BigDecimal.valueOf(agreement.getObjection().getCourtFixedRent());
		} else if (Action.JUDGEMENT.equals(agreement.getAction())) {
			effectiveDate = agreement.getJudgement().getEffectiveDate();
			revisedRent = BigDecimal.valueOf(agreement.getJudgement().getJudgementRent());
		}

		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		demandSearchCriteria.setDemandId(Long.parseLong(demandIds.get(0)));
		demands = demandRepository.getDemandBySearch(demandSearchCriteria, agreementRequest.getRequestInfo())
				.getDemands();
		List<DemandDetails> legacyDetails = demands.get(0).getDemandDetails();
		for (DemandDetails demandDetail : demands.get(0).getDemandDetails()) {
			if (demandDetail.getPeriodEndDate().after(effectiveDate)) {
				effectiveCollection = demandDetail.getCollectionAmount();
			}
		}
		demandDetails = updateRentAndCollection(legacyDetails, revisedRent, effectiveCollection, effectiveDate);
		demands.get(0).setDemandDetails(demandDetails);

		return demands;
	}

	private List<DemandDetails> updateRentAndCollection(List<DemandDetails> legacyDetails, BigDecimal revisedRent,
			BigDecimal effectiveCollection, Date effectiveDate) {
		List<DemandDetails> demandDetails = new ArrayList<>();
		for (DemandDetails demandDetail : legacyDetails) {
			if (demandDetail.getPeriodEndDate().after(effectiveDate)) {
				demandDetail.setTaxAmount(revisedRent);
				demandDetail.setCollectionAmount(BigDecimal.ZERO);
				if (effectiveCollection.compareTo(revisedRent) >= 0) {
					demandDetail.setCollectionAmount(revisedRent);
					effectiveCollection = effectiveCollection.subtract(revisedRent);

				} else {
					demandDetail.setCollectionAmount(effectiveCollection);
					effectiveCollection = BigDecimal.ZERO;

				}
			}
			demandDetails.add(demandDetail);
		}
		if (effectiveCollection.compareTo(BigDecimal.ZERO) > 0) {

			addExcessCollectionToAdvance(legacyDetails, effectiveCollection);

		}
		return demandDetails;

	}

	private void addExcessCollectionToAdvance(List<DemandDetails> legacyDetails, BigDecimal excessCollection) {
		for (DemandDetails demandDetail : legacyDetails) {
			if ("Advance Tax".equals(demandDetail.getTaxReason())
					|| propertiesManager.getTaxReasonAdvanceTax().equals(demandDetail.getTaxReasonCode())) {
				demandDetail.setCollectionAmount(demandDetail.getCollectionAmount().add(excessCollection));
			}
		}
	}

	public List<Demand> prepareDemandsForClone(String demandId,RequestInfo requestInfo) {
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		List<Demand> demands= new ArrayList<>();
		if(demandId!=null){
			demandSearchCriteria.setDemandId(Long.valueOf(demandId));
		
		Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands().get(0);
		List<DemandDetails> clonedDemandDetails = new ArrayList<>();
		for (DemandDetails demandDetail : demand.getDemandDetails()) {
			demandDetail.setId(null);
			clonedDemandDetails.add(demandDetail);
		}
		demand.getDemandDetails().clear();
		demand.setDemandDetails(clonedDemandDetails);
		demands.add(demand);
		}
		return demands;
	}

	/*
	 * calling to prepare the demands for Data entry agreements in Add/Edit
	 * demand
	 */
	public List<Demand> prepareLegacyDemands(AgreementRequest agreementRequest) {
		List<Demand> demands = null;
		List<DemandDetails> legacyDetails = new ArrayList<>();
		DemandDetails demandDetail = null;
		List<DemandReason> demandReasons = null;
		Agreement agreement = agreementRequest.getAgreement();
		List<String> demandIds = agreement.getDemands();
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		if (demandIds == null) {
			demands = demandRepository.getDemandList(agreementRequest, getLegacyDemandReasons(agreementRequest));
			return demands;
		}
		demandSearchCriteria.setDemandId(Long.parseLong(demandIds.get(0)));
		demands = demandRepository.getDemandBySearch(demandSearchCriteria, agreementRequest.getRequestInfo())
				.getDemands();

		demandReasons = getLegacyDemandReasons(agreementRequest);
		for (DemandReason demandReason : demandReasons) {
			Boolean isDemandDetailsExist = Boolean.FALSE;
			for (DemandDetails existingDetail : demands.get(0).getDemandDetails()) {

				if (existingDetail.getTaxPeriod().equalsIgnoreCase(demandReason.getTaxPeriod())
						&& (existingDetail.getTaxReason().equalsIgnoreCase(demandReason.getName())
								|| existingDetail.getTaxReasonCode().equalsIgnoreCase(demandReason.getName()))) {
					isDemandDetailsExist = Boolean.TRUE;
				}
				existingDetail.setIsCollected(existingDetail.getCollectionAmount().compareTo(BigDecimal.ZERO) > 0
						? Boolean.TRUE : Boolean.FALSE);
			}

			if (!isDemandDetailsExist
					&& DEMAND_REASONS.stream().anyMatch(reason -> reason.equalsIgnoreCase(demandReason.getName()))) {
				demandDetail = new DemandDetails();
				demandDetail.setCollectionAmount(BigDecimal.ZERO);
				demandDetail.setRebateAmount(BigDecimal.ZERO);
				demandDetail.setTaxReason(demandReason.getName());
				demandDetail.setTaxReasonCode(demandReason.getName());
				demandDetail.setTaxPeriod(demandReason.getTaxPeriod());
				demandDetail.setTenantId(agreement.getTenantId());
				if(propertiesManager.getTaxReasonRent().equalsIgnoreCase(demandReason.getName()))
					demandDetail.setTaxAmount(BigDecimal.valueOf(agreement.getRent()));
				else
					demandDetail.setTaxAmount(BigDecimal.ZERO);
				demandDetail.setIsCollected(Boolean.FALSE);
				legacyDetails.add(demandDetail);
			}

		}
		logger.info("legacy demand details to add to existing:" + legacyDetails);
		demands.get(0).getDemandDetails().addAll(legacyDetails);
		return demands;
	}

	private List<DemandReason> getLegacyDemandReasons(AgreementRequest agreementRequest) {
		List<DemandReason> legacyDemandReasons = demandRepository.getLegacyDemandReason(agreementRequest);
		if (legacyDemandReasons.isEmpty())
			throw new RuntimeException("No demand reason found for given criteria");
		logger.info("the size of demand reasons from reason search api call : " + legacyDemandReasons.size());
		return legacyDemandReasons;
	}

	public List<Demand> updateDemandOnRemission(Agreement agreement, RequestInfo requestInfo) {
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		demandSearchCriteria.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
		List<Demand> demands = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands();
		BigDecimal totalExcessCollection = BigDecimal.ZERO;
		BigDecimal revisedRent = BigDecimal.valueOf(agreement.getRemission().getRemissionRent());
		Date fromDate = agreement.getRemission().getRemissionFromDate();
		Date toDate = agreement.getRemission().getRemissionToDate();
		for (DemandDetails demandDetail : demands.get(0).getDemandDetails()) {
			BigDecimal excessCollection ;
			if (propertiesManager.getTaxReasonRent().equalsIgnoreCase(demandDetail.getTaxReasonCode())
					|| CENTRAL_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())
					|| STATE_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
				excessCollection = updateDemadDetails(demandDetail, revisedRent, fromDate, toDate);
				totalExcessCollection = totalExcessCollection.add(excessCollection);
			}	
			
		}
		if (totalExcessCollection.compareTo(BigDecimal.ZERO) > 0) {
			demands = adjustCollection(demands, totalExcessCollection);
		}
		return demands;
	}

	private BigDecimal updateDemadDetails(DemandDetails demandDetail, BigDecimal rent, Date fromDate, Date toDate) {
		BigDecimal excessCollection = BigDecimal.ZERO;
		if (demandDetail.getPeriodEndDate().compareTo(fromDate) >= 0
				&& demandDetail.getPeriodStartDate().compareTo(toDate) <= 0) {
			Double gstAmount = (rent.doubleValue() * 18) / 100;

			if (CENTRAL_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())
					|| STATE_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
				demandDetail.setTaxAmount(BigDecimal.valueOf(Math.round(gstAmount / 2)));

			} else {

				if (demandDetail.getTaxAmount().compareTo(rent) > 0) {
					excessCollection = demandDetail.getCollectionAmount();
					demandDetail.setTaxAmount(rent);
				}

				if (demandDetail.getCollectionAmount().compareTo(rent) >= 0) {
					excessCollection = demandDetail.getCollectionAmount().subtract(rent);
					demandDetail.setCollectionAmount(rent);
				}
			}
		}
		return excessCollection;
	}


	/*
	 * Adjusting excess collection in remission , currently we are 
	 * doing this adjustment for the agreements for which already collection is done
	 * and for same installments remission is applying (DATA_ENTRY agreements use case)
	 */
	private List<Demand> adjustCollection(List<Demand> demands, BigDecimal collection) {

		for (DemandDetails demandDetail : demands.get(0).getDemandDetails()) {

			if (demandDetail.getTaxAmount().compareTo(demandDetail.getCollectionAmount()) > 0) {
				BigDecimal balance = demandDetail.getTaxAmount().subtract(demandDetail.getCollectionAmount());
				if (collection.compareTo(balance) > 0) {

					collection = collection.subtract(balance);
					demandDetail.setCollectionAmount(demandDetail.getCollectionAmount().add(balance));

				} else
					demandDetail.setCollectionAmount(demandDetail.getCollectionAmount().add(balance));
			}
		}
		if (collection.compareTo(BigDecimal.ZERO) > 0) {
			for (DemandDetails demandDetail : demands.get(0).getDemandDetails()) {
				if ("ADVANCE_TAX".equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
					demandDetail.setCollectionAmount(demandDetail.getCollectionAmount().add(collection));
				}
			}
		}

		return demands;
	}
	
	public List<Demand> prepareDemandsForRenewal(AgreementRequest agreementRequest, boolean isForApproval) {
		List<Demand> demands;
		List<DemandReason> demandReasons = demandRepository.getDemandReasonForRenewal(agreementRequest, isForApproval);
		if (!demandReasons.isEmpty()) {
			demands = demandRepository.getDemandList(agreementRequest, demandReasons);
			return demands;
		} else
			throw new RuntimeException("No demand reason found for given criteria in Renewal");
	}
	
	public List<DemandDetails> getExistingDemandDetails(String demandId,RequestInfo requestInfo) {
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		List<DemandDetails> clonedDemandDetails = new ArrayList<>();
		if (demandId != null) {
			demandSearchCriteria.setDemandId(Long.valueOf(demandId));
			Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands().get(0);
			for (DemandDetails demandDetail : demand.getDemandDetails()) {
				demandDetail.setId(null);
				clonedDemandDetails.add(demandDetail);
			}
		}
		return clonedDemandDetails;
	}
}
