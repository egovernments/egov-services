package org.egov.lams.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.DemandReason;
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Source;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.AgreementRepository;
import org.egov.lams.repository.AllotteeRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.util.AcknowledgementNumberUtil;
import org.egov.lams.util.AgreementNumberUtil;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AllotteeResponse;
import org.egov.lams.web.contract.DemandResponse;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.Position;
import org.egov.lams.web.contract.PositionResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AgreementService {
	public static final Logger logger = LoggerFactory.getLogger(AgreementService.class);
	public static final String WF_ACTION_APPROVE = "Approve";
	public static final String WF_ACTION_REJECT = "Reject";
	public static final String WF_ACTION_CANCEL = "Cancel";

	@Autowired
	private AgreementRepository agreementRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private LamsConfigurationService lamsConfigurationService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private DemandRepository demandRepository;

	@Autowired
	private AcknowledgementNumberUtil acknowledgementNumberService;

	@Autowired
	private AgreementNumberUtil agreementNumberService;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private AllotteeRepository allotteeRepository;

	/**
	 * service call to single agreement based on acknowledgementNumber
	 * 
	 * @param code
	 * @return
	 */
	public boolean isAgreementExist(String code) {
		return agreementRepository.isAgreementExist(code);
	}

	public List<Agreement> getAgreementsForAssetId(Long assetId) {

		AgreementCriteria agreementCriteria = new AgreementCriteria();
		Set<Long> assets = new HashSet<>();
		assets.add(assetId);
		agreementCriteria.setAsset(assets);
		return agreementRepository.getAgreementForCriteria(agreementCriteria);
	}

	/**
	 * This method is used to create new agreement
	 * 
	 * @return Agreement, return the agreement details with current status
	 * 
	 * @param agreement,
	 *            hold agreement details
	 * 
	 */
	public Agreement createAgreement(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		logger.info("createAgreement service::" + agreement);
		String kafkaTopic = null;
		setAuditDetails(agreement, agreementRequest.getRequestInfo());

		if (agreement.getAction().equals(Action.CREATE)) {

			agreement.setExpiryDate(getExpiryDate(agreement));
			logger.info("The closeDate calculated is " + agreement.getExpiryDate() + "from commencementDate of "
					+ agreement.getCommencementDate() + "by adding with no of years " + agreement.getTimePeriod());

			if (agreement.getSource().equals(Source.DATA_ENTRY)) {
				kafkaTopic = propertiesManager.getSaveAgreementTopic();
				agreement.setStatus(Status.ACTIVE);
				List<Demand> demands = prepareDemands(agreementRequest);

				DemandResponse demandResponse = demandRepository.createDemand(demands,
						agreementRequest.getRequestInfo());
				List<String> demandList = demandResponse.getDemands().stream().map(demand -> demand.getId())
						.collect(Collectors.toList());
				agreement.setDemands(demandList);
				agreement.setAgreementNumber(agreementNumberService.generateAgrementNumber(agreement.getTenantId()));
				agreement.setAgreementDate(agreement.getCommencementDate());
			} else {
				kafkaTopic = propertiesManager.getStartWorkflowTopic();
				agreement.setStatus(Status.WORKFLOW);
				setInitiatorPosition(agreementRequest);

				List<Demand> demands = prepareDemands(agreementRequest);

				DemandResponse demandResponse = demandRepository.createDemand(demands,
						agreementRequest.getRequestInfo());
				List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
						.collect(Collectors.toList());
				agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
				logger.info(agreement.getAcknowledgementNumber());
				agreement.setDemands(demandIdList);
			}
			agreement.setId(agreementRepository.getAgreementID());
		}
		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}

	public Agreement createEviction(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("create Eviction of agreement::" + agreement);
		setAuditDetails(agreement, agreementRequest.getRequestInfo());

		String kafkaTopic = propertiesManager.getStartWorkflowTopic();
		agreement.setStatus(Status.WORKFLOW);
		setInitiatorPosition(agreementRequest);
		agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
		agreement.setId(agreementRepository.getAgreementID());

		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}

	public Agreement createCancellation(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();

		logger.info("create Cancellation of agreement::" + agreement);
		String kafkaTopic = propertiesManager.getStartWorkflowTopic();
		setAuditDetails(agreement, agreementRequest.getRequestInfo());

		agreement.setStatus(Status.WORKFLOW);
		setInitiatorPosition(agreementRequest);
		agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
		agreement.setId(agreementRepository.getAgreementID());

		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}

	public Agreement createRenewal(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("create Renewal of agreement::" + agreement);
		String kafkaTopic = propertiesManager.getStartWorkflowTopic();
		setAuditDetails(agreement, agreementRequest.getRequestInfo());

		agreement.setStatus(Status.WORKFLOW);
		agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
		agreement.setExpiryDate(getExpiryDate(agreement));
		agreement.setId(agreementRepository.getAgreementID());
		setInitiatorPosition(agreementRequest);
		List<Demand> demands = prepareDemandsForClone(agreement.getLegacyDemands());
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);
		agreement.setId(agreementRepository.getAgreementID());

		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}

	public Agreement createObjection(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("createObjection on agreement::" + agreement);
		String kafkaTopic = propertiesManager.getStartWorkflowTopic();
		setAuditDetails(agreement, agreementRequest.getRequestInfo());
		agreement.setStatus(Status.WORKFLOW);
		agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
		agreement.setId(agreementRepository.getAgreementID());
		setInitiatorPosition(agreementRequest);
		List<Demand> demands = prepareDemandsForClone(agreement.getLegacyDemands());
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);

		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}

	public Agreement createJudgement(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("create judgement on agreement::" + agreement);
		String kafkaTopic = propertiesManager.getStartWorkflowTopic();
		setAuditDetails(agreement, agreementRequest.getRequestInfo());
		agreement.setStatus(Status.WORKFLOW);
		agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
		agreement.setId(agreementRepository.getAgreementID());
		setInitiatorPosition(agreementRequest);
		List<Demand> demands = prepareDemandsForClone(agreement.getLegacyDemands());
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);
		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}

	private void sendAgreementToKafka(String kafkaTopic, AgreementRequest agreementRequest) {
		try {
			Agreement agreement = agreementRequest.getAgreement();
			logger.info("agreement before sending" + agreement);
			kafkaTemplate.send(kafkaTopic, "save-agreement", agreementRequest);
		} catch (Exception exception) {
			logger.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
	}

	/***
	 * method to update agreementNumber using acknowledgeNumber
	 * 
	 * @param agreement
	 * @return
	 */
	public Agreement updateAgreement(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update create agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		String kafkaTopic = propertiesManager.getUpdateWorkflowTopic();
		updateAuditDetails(agreement, agreementRequest.getRequestInfo());

		if (agreement.getSource().equals(Source.DATA_ENTRY)) {
			kafkaTopic = propertiesManager.getUpdateAgreementTopic();
			agreement.setDemands(updateDemand(agreement.getDemands(), agreement.getLegacyDemands(),
					agreementRequest.getRequestInfo()));
		} else if (agreement.getSource().equals(Source.SYSTEM)) {
			kafkaTopic = propertiesManager.getUpdateWorkflowTopic();
			if (workFlowDetails != null) {
				if ("Approve".equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.ACTIVE);
					agreement.setAgreementDate(new Date());
					if (agreement.getAgreementNumber() == null) {
						agreement.setAgreementNumber(
								agreementNumberService.generateAgrementNumber(agreement.getTenantId()));
					}
					updateDemand(agreement.getDemands(), prepareDemands(agreementRequest),
							agreementRequest.getRequestInfo());
				} else if ("Reject".equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.REJECTED);
				} else if ("Cancel".equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.CANCELLED);
				} else if ("Print Notice".equalsIgnoreCase(workFlowDetails.getAction())) {
					// no action for print notice
				}
			}
		}

		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}

	public Agreement updateRenewal(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update renewal agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		String kafkaTopic = propertiesManager.getUpdateWorkflowTopic();
		updateAuditDetails(agreement, agreementRequest.getRequestInfo());
		if (workFlowDetails != null) {
			if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.ACTIVE);
				agreement.setAgreementDate(new Date());
				updateDemand(agreement.getDemands(), prepareDemands(agreementRequest),
						agreementRequest.getRequestInfo());
			} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.REJECTED);
			} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.CANCELLED);
			}
		}
		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}
	
	public Agreement updateCancellation(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update cancellation agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		String kafkaTopic = propertiesManager.getUpdateWorkflowTopic();
		updateAuditDetails(agreement, agreementRequest.getRequestInfo());
		if (workFlowDetails != null) {
			if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.CANCELLED);// this has to be
														// fixed (status)
				agreement.setAgreementDate(new Date());

			} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.REJECTED);
			} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.CANCELLED);
			}
		}
		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}
	
	public Agreement updateEviction(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update eviction agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		String kafkaTopic = propertiesManager.getUpdateWorkflowTopic();
		updateAuditDetails(agreement, agreementRequest.getRequestInfo());
		if (workFlowDetails != null) {
			if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.EVICTED);
				agreement.setAgreementDate(new Date());

			} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.REJECTED);
			} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.CANCELLED);
			}
		}
		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}
	
	public Agreement updateObjectionAndJudgement(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update objection/judgement agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		String kafkaTopic = propertiesManager.getUpdateWorkflowTopic();
		updateAuditDetails(agreement, agreementRequest.getRequestInfo());

		if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreement.setStatus(Status.ACTIVE);
			updateDemand(agreement.getDemands(), prepareDemandsByApprove(agreementRequest),
					agreementRequest.getRequestInfo());

		} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreement.setStatus(Status.REJECTED);
		} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreement.setStatus(Status.CANCELLED);
		}
		sendAgreementToKafka(kafkaTopic, agreementRequest);
		return agreement;
	}
	
	
	public List<Agreement> searchAgreement(AgreementCriteria agreementCriteria, RequestInfo requestInfo) {
		/*
		 * three boolean variables isAgreementNull,isAssetNull and
		 * isAllotteeNull declared to indicate whether criteria arguments for
		 * each of the Agreement,Asset and Allottee objects are given or not.
		 */

		if (agreementCriteria.getToDate() != null) {
			agreementCriteria.setToDate(setToTime(agreementCriteria.getToDate()));
		}

		boolean isAgreementNull = agreementCriteria.getAgreementId() == null
				&& agreementCriteria.getAgreementNumber() == null && agreementCriteria.getStatus() == null
				&& (agreementCriteria.getFromDate() == null && agreementCriteria.getToDate() == null)
				&& agreementCriteria.getTenderNumber() == null && agreementCriteria.getTinNumber() == null
				&& agreementCriteria.getTradelicenseNumber() == null && agreementCriteria.getAsset() == null
				&& agreementCriteria.getAllottee() == null;

		boolean isAllotteeNull = agreementCriteria.getAllotteeName() == null
				&& agreementCriteria.getMobileNumber() == null;

		boolean isAssetNull = agreementCriteria.getAssetCategory() == null
				&& agreementCriteria.getShoppingComplexNo() == null && agreementCriteria.getAssetCode() == null
				&& agreementCriteria.getLocality() == null && agreementCriteria.getRevenueWard() == null
				&& agreementCriteria.getElectionWard() == null && agreementCriteria.getDoorNo() == null;

		if (!isAgreementNull && !isAssetNull && !isAllotteeNull) {
			logger.info("agreementRepository.findByAllotee");
			return agreementRepository.findByAllotee(agreementCriteria, requestInfo);

		} else if (!isAgreementNull && isAssetNull && !isAllotteeNull) {
			logger.info("agreementRepository.findByAllotee");
			return agreementRepository.findByAgreementAndAllotee(agreementCriteria, requestInfo);

		} else if (!isAgreementNull && !isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findByAgreementAndAsset : both agreement and ");
			return agreementRepository.findByAgreementAndAsset(agreementCriteria, requestInfo);

		} else if ((isAgreementNull && isAssetNull && !isAllotteeNull)
				|| (isAgreementNull && !isAssetNull && !isAllotteeNull)) {
			logger.info("agreementRepository.findByAllotee : only allottee || allotte and asset");
			return agreementRepository.findByAllotee(agreementCriteria, requestInfo);

		} else if (isAgreementNull && !isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findByAsset : only asset");
			return agreementRepository.findByAsset(agreementCriteria, requestInfo);

		} else if (!isAgreementNull && isAssetNull && isAllotteeNull) {
			logger.info("agreementRepository.findByAgreement : only agreement");
			return agreementRepository.findByAgreement(agreementCriteria, requestInfo);
		} else {
			// if no values are given for all the three criteria objects
			// (isAgreementNull && isAssetNull && isAllotteeNull)
			logger.info("agreementRepository.findByAgreement : all values null");
			return agreementRepository.findByAgreement(agreementCriteria, requestInfo);
		}
	}

	private static Date setToTime(Date toDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(toDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	private List<String> updateDemand(List<String> demands, List<Demand> legacydemands, RequestInfo requestInfo) {

		DemandResponse demandResponse = null;
		if (demands == null)
			demandResponse = demandRepository.createDemand(legacydemands, requestInfo);
		else
			demandResponse = demandRepository.updateDemand(legacydemands, requestInfo);
		return demandResponse.getDemands().stream().map(demand -> demand.getId()).collect(Collectors.toList());
	}

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

	private List<DemandReason> getDemandReasons(AgreementRequest agreementRequest) {
		List<DemandReason> demandReasons = demandRepository.getDemandReason(agreementRequest);
		if (demandReasons.isEmpty())
			throw new RuntimeException("No demand reason found for given criteria");
		logger.info("the size of demand reasons obtained from reason search api call : " + demandReasons.size());
		return demandReasons;
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
						&& existingDetail.getTaxReason().equalsIgnoreCase(demandReason.getName())) {
					isDemandDetailsExist = Boolean.TRUE;
				}
			}

			if (!isDemandDetailsExist && "RENT".equalsIgnoreCase(demandReason.getName())) {
				demandDetail = new DemandDetails();
				demandDetail.setCollectionAmount(BigDecimal.ZERO);
				demandDetail.setRebateAmount(BigDecimal.ZERO);
				demandDetail.setTaxReason(demandReason.getName());
				demandDetail.setTaxReasonCode(demandReason.getName());
				demandDetail.setTaxPeriod(demandReason.getTaxPeriod());
				demandDetail.setTenantId(agreement.getTenantId());
				demandDetail.setTaxAmount(BigDecimal.valueOf(agreement.getRent()));

				legacyDetails.add(demandDetail);
			}

		}
		logger.info("legacy demand details to add to existing:" + legacyDetails);
		demands.get(0).getDemandDetails().addAll(legacyDetails);
		return demands;
	}

	private List<DemandReason> getLegacyDemandReasons(AgreementRequest agreementRequest) {
		List<DemandReason> legacrDemandReasons = demandRepository.getLegacyDemandReason(agreementRequest);
		if (legacrDemandReasons.isEmpty())
			throw new RuntimeException("No demand reason found for given criteria");
		logger.info("the size of demand reasons from reason search api call : " + legacrDemandReasons.size());
		return legacrDemandReasons;
	}

	public List<Demand> prepareDemandsByApprove(AgreementRequest agreementRequest) {
		List<Demand> demands = null;
		List<DemandDetails> demandDetails;
		Agreement agreement = agreementRequest.getAgreement();
		String tenatId = agreement.getTenantId();
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
		demandDetails = updateRentAndCollection(legacyDetails, revisedRent, effectiveCollection, effectiveDate,
				tenatId);
		demands.get(0).setDemandDetails(demandDetails);

		return demands;
	}

	private List<DemandDetails> updateRentAndCollection(List<DemandDetails> legacyDetails, BigDecimal revisedRent,
			BigDecimal effectiveCollection, Date effectiveDate, String tenatId) {
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

	private void setInitiatorPosition(AgreementRequest agreementRequest) {

		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		Agreement agreement = agreementRequest.getAgreement();
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(agreementRequest.getRequestInfo());
		String tenantId = requestInfo.getUserInfo().getTenantId();

		Allottee allottee = new Allottee();
		allottee.setUserName(requestInfo.getUserInfo().getUserName());
		allottee.setTenantId(tenantId);
		AllotteeResponse allotteeResponse = allotteeRepository.getAllottees(allottee,
				requestInfoWrapper.getRequestInfo());
		allottee = allotteeResponse.getAllottee().get(0);

		PositionResponse positionResponse = null;
		String positionUrl = propertiesManager.getEmployeeServiceHostName()
				+ propertiesManager.getEmployeeServiceSearchPath()
						.replace(propertiesManager.getEmployeeServiceSearchPathVariable(), allottee.getId().toString())
				+ "?tenantId=" + tenantId;

		logger.info("the request url to position get call :: " + positionUrl);
		logger.info("the request body to position get call :: " + requestInfoWrapper);

		// FIXME move the resttemplate to positionrepository later
		try {
			positionResponse = restTemplate.postForObject(positionUrl, requestInfoWrapper, PositionResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("the exception from poisition search :: " + e);
			throw e;
		}

		logger.info("the response form position get call :: " + positionResponse);

		List<Position> positionList = positionResponse.getPosition();
		if (positionList == null || positionList.isEmpty())
			throw new RuntimeException("the respective user has no positions");
		Map<String, Long> positionMap = new HashMap<>();

		for (Position position : positionList) {
			positionMap.put(position.getDeptdesig().getDesignation().getName(), position.getId());
		}

		LamsConfigurationGetRequest lamsConfigurationGetRequest = new LamsConfigurationGetRequest();
		String keyName = propertiesManager.getWorkflowInitiatorPositionkey();
		lamsConfigurationGetRequest.setName(keyName);
		List<String> assistantDesignations = lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest)
				.get(keyName);

		for (String desginationName : assistantDesignations) {
			logger.info("desg name" + desginationName);
			if (positionMap.containsKey(desginationName)) {
				workFlowDetails.setInitiatorPosition(positionMap.get(desginationName));
				logger.info(" the initiator name  :: " + desginationName + "the value for key"
						+ workFlowDetails.getInitiatorPosition());
			}
		}
	}

	public void updateAdvanceFlag(Agreement agreement) {
		if (agreement.getAcknowledgementNumber() != null)
			agreementRepository.updateAgreementAdvance(agreement.getAcknowledgementNumber());

	}

	private void setAuditDetails(Agreement agreement, RequestInfo requestInfo) {
		String requesterId = requestInfo.getUserInfo().getId().toString();
		agreement.setCreatedBy(requesterId);
		agreement.setCreatedDate(new Date());
		agreement.setLastmodifiedBy(requesterId);
		agreement.setLastmodifiedDate(new Date());
	}

	private void updateAuditDetails(Agreement agreement, RequestInfo requestInfo) {
		String requesterId = requestInfo.getUserInfo().getId().toString();
		agreement.setLastmodifiedBy(requesterId);
		agreement.setLastmodifiedDate(new Date());
	}
	private Date getExpiryDate(Agreement agreement) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(agreement.getCommencementDate());
		calendar.setTimeZone(TimeZone.getTimeZone(propertiesManager.getTimeZone()));
		calendar.add(Calendar.YEAR, agreement.getTimePeriod().intValue());
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	public String checkRenewalStatus(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		return agreementRepository.getRenewalStatus(agreement.getAgreementNumber(), agreement.getTenantId());

	}

	public String checkObjectionStatus(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		return agreementRepository.getObjectionStatus(agreement.getAgreementNumber(), agreement.getTenantId());

	}
	private List<Demand> prepareDemandsForClone(List<Demand> demands) {
		List<DemandDetails> clonedDemandDetails = new ArrayList<>();
		for (DemandDetails demandDetail : demands.get(0).getDemandDetails()) {
			demandDetail.setId(null);
			clonedDemandDetails.add(demandDetail);
		}
		demands.get(0).getDemandDetails().clear();
		demands.get(0).setDemandDetails(clonedDemandDetails);
		return demands;
	}
}
