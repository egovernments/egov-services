package org.egov.lams.service;

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
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Source;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.AgreementMessageQueueRepository;
import org.egov.lams.repository.AgreementRepository;
import org.egov.lams.repository.AllotteeRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.repository.PositionRestRepository;
import org.egov.lams.util.AcknowledgementNumberUtil;
import org.egov.lams.util.AgreementNumberUtil;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AllotteeResponse;
import org.egov.lams.web.contract.DemandResponse;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.Position;
import org.egov.lams.web.contract.PositionResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgreementService {
	public static final Logger logger = LoggerFactory.getLogger(AgreementService.class);
	public static final String WF_ACTION_APPROVE = "Approve";
	public static final String WF_ACTION_REJECT = "Reject";
	public static final String WF_ACTION_CANCEL = "Cancel";
	public static final String START_WORKFLOW = "START_WORKFLOW";
	public static final String UPDATE_WORKFLOW = "UPDATE_WORKFLOW";
	public static final String SAVE = "SAVE";
	public static final String UPDATE = "UPDATE";
	public static final String LAMS_WORKFLOW_INITIATOR_DESIGNATION = "lams_workflow_initiator_designation";

	private AgreementRepository agreementRepository;
	private LamsConfigurationService lamsConfigurationService;
	private DemandRepository demandRepository;
	private AcknowledgementNumberUtil acknowledgementNumberService;
	private AgreementNumberUtil agreementNumberService;
	private PropertiesManager propertiesManager;
	private AllotteeRepository allotteeRepository;
	private AgreementMessageQueueRepository agreementMessageQueueRepository;
	private PositionRestRepository positionRestRepository;
	private DemandService demandService;

	public AgreementService(AgreementRepository agreementRepository, LamsConfigurationService lamsConfigurationService,
							DemandRepository demandRepository, AcknowledgementNumberUtil acknowledgementNumberService,
							AgreementNumberUtil agreementNumberService, PropertiesManager propertiesManager,
							AllotteeRepository allotteeRepository, AgreementMessageQueueRepository agreementMessageQueueRepository,
							PositionRestRepository positionRestRepository, DemandService demandService) {
		this.agreementRepository = agreementRepository;
		this.lamsConfigurationService = lamsConfigurationService;
		this.demandRepository = demandRepository;
		this.acknowledgementNumberService = acknowledgementNumberService;
		this.agreementNumberService = agreementNumberService;
		this.propertiesManager = propertiesManager;
		this.allotteeRepository = allotteeRepository;
		this.agreementMessageQueueRepository = agreementMessageQueueRepository;
		this.positionRestRepository = positionRestRepository;
		this.demandService = demandService;
	}

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
		setAuditDetails(agreement, agreementRequest.getRequestInfo());

		if (agreement.getAction().equals(Action.CREATE)) {

			agreement.setExpiryDate(getExpiryDate(agreement));
			logger.info("The closeDate calculated is " + agreement.getExpiryDate() + "from commencementDate of "
					+ agreement.getCommencementDate() + "by adding with no of years " + agreement.getTimePeriod());
			agreement.setId(agreementRepository.getAgreementID());
			if (agreement.getSource().equals(Source.DATA_ENTRY)) {
				agreement.setStatus(Status.ACTIVE);
				List<Demand> demands = demandService.prepareDemands(agreementRequest);

				DemandResponse demandResponse = demandRepository.createDemand(demands,
						agreementRequest.getRequestInfo());
				List<String> demandList = demandResponse.getDemands().stream().map(demand -> demand.getId())
						.collect(Collectors.toList());
				agreement.setDemands(demandList);
				agreement.setAgreementNumber(agreementNumberService.generateAgrementNumber(agreement.getTenantId()));
				agreement.setAgreementDate(agreement.getCommencementDate());
				agreementMessageQueueRepository.save(agreementRequest, SAVE);
			} else {
				agreement.setStatus(Status.WORKFLOW);
				setInitiatorPosition(agreementRequest);

				List<Demand> demands = demandService.prepareDemands(agreementRequest);

				DemandResponse demandResponse = demandRepository.createDemand(demands,
						agreementRequest.getRequestInfo());
				List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
						.collect(Collectors.toList());
				agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
				logger.info(agreement.getAcknowledgementNumber());
				agreement.setDemands(demandIdList);
				agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);
			}

		}
		return agreement;
	}

	public Agreement createEviction(AgreementRequest agreementRequest) {
		logger.info("create Eviction of agreement::" + agreementRequest.getAgreement());
		Agreement agreement = enrichAgreement(agreementRequest);
		agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);

		return agreement;
	}

	public Agreement createCancellation(AgreementRequest agreementRequest) {
		logger.info("create Cancellation of agreement::" + agreementRequest.getAgreement());
		Agreement agreement = enrichAgreement(agreementRequest);
		agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);

		return agreement;
	}

	public Agreement createRenewal(AgreementRequest agreementRequest) {
		logger.info("create Renewal of agreement::" + agreementRequest.getAgreement());
		Agreement agreement = enrichAgreement(agreementRequest);
		agreement.setExpiryDate(getExpiryDate(agreement));
		List<Demand> demands = demandService.prepareDemandsForClone(agreement.getLegacyDemands());
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);

		agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);
		return agreement;
	}

	public Agreement createObjection(AgreementRequest agreementRequest) {
		logger.info("createObjection on agreement::" + agreementRequest.getAgreement());
		Agreement agreement = enrichAgreement(agreementRequest);
		List<Demand> demands = demandService.prepareDemandsForClone(agreement.getLegacyDemands());
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);

		agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);
		return agreement;
	}

	public Agreement createJudgement(AgreementRequest agreementRequest) {
		logger.info("create judgement on agreement::" + agreementRequest.getAgreement());
		Agreement agreement = enrichAgreement(agreementRequest);
		List<Demand> demands = demandService.prepareDemandsForClone(agreement.getLegacyDemands());
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);

		agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);
		return agreement;
	}

	private Agreement enrichAgreement(AgreementRequest agreementRequest){
		Agreement agreement = agreementRequest.getAgreement();
		setAuditDetails(agreement, agreementRequest.getRequestInfo());
		agreement.setStatus(Status.WORKFLOW);
		setInitiatorPosition(agreementRequest);
		agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
		agreement.setId(agreementRepository.getAgreementID());

		return agreement;
	}

	public Agreement saveRemission(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		agreement.setId(agreementRepository.getAgreementID());
		List<Demand> demands = demandService.updateDemandOnRemission(agreement, agreement.getLegacyDemands());
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandList = demandResponse.getDemands().stream().map(demand -> demand.getId())
				.collect(Collectors.toList());
		agreement.setDemands(demandList);
		agreementMessageQueueRepository.save(agreementRequest, SAVE);
		return agreement;
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
		updateAuditDetails(agreement, agreementRequest.getRequestInfo());

		if (agreement.getSource().equals(Source.DATA_ENTRY)) {
			agreement.setDemands(updateDemand(agreement.getDemands(), agreement.getLegacyDemands(),
					agreementRequest.getRequestInfo()));
			agreementMessageQueueRepository.save(agreementRequest, UPDATE);

		} else if (agreement.getSource().equals(Source.SYSTEM)) {
			if (workFlowDetails != null) {
				if ("Approve".equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.ACTIVE);
					agreement.setAgreementDate(new Date());
					if (agreement.getAgreementNumber() == null) {
						agreement.setAgreementNumber(
								agreementNumberService.generateAgrementNumber(agreement.getTenantId()));
					}
					List<Demand> demands =demandService.prepareDemands(agreementRequest);
					updateDemand(agreement.getDemands(),demands,
							agreementRequest.getRequestInfo());
				} else if ("Reject".equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.REJECTED);
				} else if ("Cancel".equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.CANCELLED);
				} else if ("Print Notice".equalsIgnoreCase(workFlowDetails.getAction())) {
					// no action for print notice
				}
			}
			agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);

		}

		return agreement;
	}

	public Agreement updateRenewal(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update renewal agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		updateAuditDetails(agreement, agreementRequest.getRequestInfo());
		if (workFlowDetails != null) {
			if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.ACTIVE);
				agreement.setAgreementDate(new Date());
				List<Demand> demands = demandService.prepareDemands(agreementRequest);
				updateDemand(agreement.getDemands(), demands,
						agreementRequest.getRequestInfo());
			} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.REJECTED);
			} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.CANCELLED);
			}
		}
		agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);
		return agreement;
	}

	public Agreement updateCancellation(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update cancellation agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
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
		agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);
		return agreement;
	}

	public Agreement updateEviction(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update eviction agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
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
		agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);
		return agreement;
	}

	public Agreement updateObjectionAndJudgement(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update objection/judgement agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		updateAuditDetails(agreement, agreementRequest.getRequestInfo());

		if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreement.setStatus(Status.ACTIVE);
			List<Demand> demands = demandService.prepareDemandsByApprove(agreementRequest);
			updateDemand(agreement.getDemands(), demands,
					agreementRequest.getRequestInfo());

		} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreement.setStatus(Status.REJECTED);
		} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreement.setStatus(Status.CANCELLED);
		}
		agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);
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
	
	/*
	 * calling to prepare the demands for Data entry agreements in Add/Edit
	 * demand
	 */
	public List<Demand> prepareLegacyDemands(AgreementRequest agreementRequest) {

		return demandService.prepareLegacyDemands(agreementRequest);
	}

	public List<Demand> prepareDemands(AgreementRequest agreementRequest) {

		return demandService.prepareDemands(agreementRequest);

	}

	private List<String> updateDemand(List<String> demands, List<Demand> legacydemands, RequestInfo requestInfo) {

		DemandResponse demandResponse = null;
		if (demands == null)
			demandResponse = demandRepository.createDemand(legacydemands, requestInfo);
		else
			demandResponse = demandRepository.updateDemand(legacydemands, requestInfo);
		return demandResponse.getDemands().stream().map(demand -> demand.getId()).collect(Collectors.toList());
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

		PositionResponse positionResponse = positionRestRepository.getPositions(allottee.getId().toString(), tenantId,
				requestInfoWrapper);

		List<Position> positionList = positionResponse.getPosition();
		if (positionList == null || positionList.isEmpty())
			throw new RuntimeException("the respective user has no positions");
		Map<String, Long> positionMap = new HashMap<>();

		for (Position position : positionList) {
			positionMap.put(position.getDeptdesig().getDesignation().getName(), position.getId());
		}

		LamsConfigurationGetRequest lamsConfigurationGetRequest = new LamsConfigurationGetRequest();
		lamsConfigurationGetRequest.setName(LAMS_WORKFLOW_INITIATOR_DESIGNATION);
		List<String> assistantDesignations = lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest)
				.get(LAMS_WORKFLOW_INITIATOR_DESIGNATION);

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

}
