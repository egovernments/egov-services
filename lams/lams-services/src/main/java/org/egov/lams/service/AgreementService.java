package org.egov.lams.service;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.lams.model.*;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.PaymentCycle;
import org.egov.lams.model.enums.Source;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.*;
import org.egov.lams.util.AcknowledgementNumberUtil;
import org.egov.lams.util.AgreementNumberUtil;
import org.egov.lams.web.contract.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgreementService {
	public static final Logger logger = LoggerFactory.getLogger(AgreementService.class);
	public static final String WF_ACTION_APPROVE = "Approve";
	public static final String WF_ACTION_REJECT = "Reject";
	public static final String WF_ACTION_CANCEL = "Cancel";
	public static final String WF_ACTION_PRINT_NOTICE = "Print Notice";
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
	private AllotteeRepository allotteeRepository;
	private AgreementMessageQueueRepository agreementMessageQueueRepository;
	private PositionRestRepository positionRestRepository;
	private DemandService demandService;

	public AgreementService(AgreementRepository agreementRepository, LamsConfigurationService lamsConfigurationService,
							DemandRepository demandRepository, AcknowledgementNumberUtil acknowledgementNumberService,
							AgreementNumberUtil agreementNumberService, AllotteeRepository allotteeRepository,
							AgreementMessageQueueRepository agreementMessageQueueRepository, PositionRestRepository positionRestRepository,
							DemandService demandService, @Value("${app.timezone}") String timeZone) {
		this.agreementRepository = agreementRepository;
		this.lamsConfigurationService = lamsConfigurationService;
		this.demandRepository = demandRepository;
		this.acknowledgementNumberService = acknowledgementNumberService;
		this.agreementNumberService = agreementNumberService;
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
	
	public List<Agreement> getAgreementsForAssetIdAndFloor(Agreement agreement,Long assetId) {

		AgreementCriteria agreementCriteria = new AgreementCriteria();
		Set<Long> assets = new HashSet<>();
		assets.add(assetId);
		agreementCriteria.setAsset(assets);
		if(agreement.getReferenceNumber()!=null){
		agreementCriteria.setReferenceNumber(agreement.getReferenceNumber());
		}
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
			agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
			logger.info("The closeDate calculated is " + agreement.getExpiryDate() + "from commencementDate of "
					+ agreement.getCommencementDate() + "by adding with no of years " + agreement.getTimePeriod());
			agreement.setId(agreementRepository.getAgreementID());
			if (agreement.getSource().equals(Source.DATA_ENTRY)) {
				agreement.setStatus(Status.ACTIVE);
				agreement.setIsUnderWorkflow(Boolean.FALSE);
				List<Demand> demands = demandService.prepareDemands(agreementRequest);

				DemandResponse demandResponse = demandRepository.createDemand(demands,
						agreementRequest.getRequestInfo());
				List<String> demandList = demandResponse.getDemands().stream().map(Demand::getId)
						.collect(Collectors.toList());
				agreement.setDemands(demandList);
				agreement.setAgreementNumber(agreementNumberService.generateAgrementNumber(agreement.getTenantId()));
				agreement.setAgreementDate(agreement.getCommencementDate());
				agreementMessageQueueRepository.save(agreementRequest, SAVE);
			} else {
				agreement.setStatus(Status.WORKFLOW);
				agreement.setIsUnderWorkflow(Boolean.TRUE);
				setInitiatorPosition(agreementRequest);

				List<Demand> demands = demandService.prepareDemands(agreementRequest);

				DemandResponse demandResponse = demandRepository.createDemand(demands,
						agreementRequest.getRequestInfo());
				List<String> demandIdList = demandResponse.getDemands().stream().map(Demand::getId)
						.collect(Collectors.toList());
				agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
				logger.info(agreement.getAcknowledgementNumber());
				agreement.setDemands(demandIdList);
				agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);
			}

		}
		return agreement;
	}

	public Agreement modifyAgreement(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		updateAuditDetails(agreement, agreementRequest.getRequestInfo());
		agreement.setExpiryDate(getExpiryDate(agreement));
		agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
		Agreement oldAgreement = agreementRepository.findByAgreementId(agreement.getId()).get(0);
		if (isDemandChangeRequired(agreement, oldAgreement)) {
			agreement.setDemands(null);
			List<Demand> demands = demandService.prepareDemands(agreementRequest);

			DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
			List<String> demandList = demandResponse.getDemands().stream().map(Demand::getId)
					.collect(Collectors.toList());
			agreement.setDemands(demandList);
		}

		agreement.setStatus(Status.ACTIVE);
		agreement.setIsUnderWorkflow(Boolean.FALSE);
		agreement.setAgreementDate(agreement.getCommencementDate());
		agreementRepository.modifyAgreement(agreementRequest);
		return agreement;
	}
	
	private Boolean isDemandChangeRequired(Agreement newAgreement, Agreement oldAgreement) {

		if ((newAgreement.getCommencementDate().compareTo(oldAgreement.getCommencementDate()) != 0)
				|| !newAgreement.getPaymentCycle().equals(oldAgreement.getPaymentCycle())) {

			return Boolean.TRUE;
		} else
			return Boolean.FALSE;

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
		//Renewal date = existing expiry date + 1
		Date renewalStartDate = DateUtils.addDays(agreement.getExpiryDate(), 1);
		agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
		agreement.setRenewalDate(renewalStartDate);
		agreement.setParent(agreement.getParent() != null ? agreement.getParent() : agreement.getId());
		List<Demand> demands = demandService.prepareDemandsForRenewal(agreementRequest, false);
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(Demand::getId)
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);
		agreement.setExpiryDate(getExpiryDate(agreement));

		agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);
		return agreement;
	}

	public Agreement createObjection(AgreementRequest agreementRequest) {
		logger.info("createObjection on agreement::" + agreementRequest.getAgreement());
		Agreement agreement = enrichAgreement(agreementRequest);
		List<Demand> demands = demandService.prepareDemandsForClone(agreement.getDemands().get(0),agreementRequest.getRequestInfo());
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(Demand::getId)
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);

		agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);
		return agreement;
	}

	public Agreement createJudgement(AgreementRequest agreementRequest) {
		logger.info("create judgement on agreement::" + agreementRequest.getAgreement());
		Agreement agreement = enrichAgreement(agreementRequest);
		List<Demand> demands = demandService.prepareDemandsForClone(agreement.getDemands().get(0),agreementRequest.getRequestInfo());
		DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandIdList = demandResponse.getDemands().stream().map(Demand::getId)
				.collect(Collectors.toList());
		agreement.setDemands(demandIdList);

		agreementMessageQueueRepository.save(agreementRequest, START_WORKFLOW);
		return agreement;
	}

	private Agreement enrichAgreement(AgreementRequest agreementRequest){
		Agreement agreement = agreementRequest.getAgreement();
		setAuditDetails(agreement, agreementRequest.getRequestInfo());
		agreement.setStatus(Status.WORKFLOW);
		agreement.setIsUnderWorkflow(Boolean.TRUE);
		setInitiatorPosition(agreementRequest);
		agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
		agreement.setId(agreementRepository.getAgreementID());

		return agreement;
	}

	public Agreement saveRemission(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		agreement.setId(agreementRepository.getAgreementID());
		List<Demand> demands = demandService.updateDemandOnRemission(agreement, agreementRequest.getRequestInfo());
		DemandResponse demandResponse = demandRepository.updateDemand(demands, agreementRequest.getRequestInfo());
		List<String> demandList = demandResponse.getDemands().stream().map(Demand::getId)
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

		if (Action.RENEWAL.equals(agreement.getAction())
				&& (Status.WORKFLOW.equals(agreement.getStatus()) || Status.REJECTED.equals(agreement.getStatus()))) {
			agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);
		} else if (agreement.getSource().equals(Source.DATA_ENTRY)) {
			Demand demand = agreement.getLegacyDemands().get(0);
			List<DemandDetails> demandDetailList = new ArrayList<>();
			for(DemandDetails demandDetail : demand.getDemandDetails()){
				if(demandDetail.getTaxAmount().compareTo(BigDecimal.ZERO) > 0)
					demandDetailList.add(demandDetail);
			}
			demand.getDemandDetails().clear();
			demand.setDemandDetails(demandDetailList);
			agreement.setDemands(updateDemand(agreement.getDemands(), agreement.getLegacyDemands(),
					agreementRequest.getRequestInfo()));
			agreementMessageQueueRepository.save(agreementRequest, UPDATE);

		} else if (agreement.getSource().equals(Source.SYSTEM)) {
			if (workFlowDetails != null) {
				if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.ACTIVE);
					agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
					agreement.setAgreementDate(new Date());
					if (agreement.getAgreementNumber() == null) {
						agreement.setAgreementNumber(
								agreementNumberService.generateAgrementNumber(agreement.getTenantId()));
					}
					List<Demand> demands =demandService.prepareDemands(agreementRequest);
					updateDemand(agreement.getDemands(),demands,
							agreementRequest.getRequestInfo());
				} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.REJECTED);
				} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.CANCELLED);
					agreement.setIsUnderWorkflow(Boolean.FALSE);
				} else if (WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setIsUnderWorkflow(Boolean.FALSE);
				}
			}
			agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);

		}

		return agreement;
	}

	public Agreement updateCancellation(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update cancellation agreement ::" + agreement);
		enrichAgreementWithWorkflowDetails(agreement, agreementRequest.getRequestInfo(), Status.INACTIVE);
		agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);
		return agreement;
	}

	public Agreement updateEviction(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update eviction agreement ::" + agreement);
		enrichAgreementWithWorkflowDetails(agreement, agreementRequest.getRequestInfo(), Status.EVICTED);
		agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);
		return agreement;
	}

	private void enrichAgreementWithWorkflowDetails(Agreement agreement, RequestInfo requestInfo, Status status){
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		updateAuditDetails(agreement, requestInfo);
		String userId = requestInfo.getUserInfo().getId().toString();
		if (workFlowDetails != null) {
			if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreementRepository.updateExistingAgreementAsHistory(agreement,userId);
				agreement.setStatus(status);
				agreement.setAgreementDate(new Date());
			} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.REJECTED);
			} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.CANCELLED);
				agreement.setIsUnderWorkflow(Boolean.FALSE);
			} else if (WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setIsUnderWorkflow(Boolean.FALSE);
			}
		}
	}

	public Agreement updateObjectionAndJudgement(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		String userId = requestInfo.getUserInfo().getId().toString();
		logger.info("update objection/judgement agreement ::" + agreement);
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		updateAuditDetails(agreement, requestInfo);

		if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreementRepository.updateExistingAgreementAsHistory(agreement,userId);
			agreement.setStatus(Status.ACTIVE);
			List<Demand> demands = demandService.prepareDemandsByApprove(agreementRequest);
			updateDemand(agreement.getDemands(), demands,
					agreementRequest.getRequestInfo());

		} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreement.setStatus(Status.REJECTED);
		} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreement.setStatus(Status.CANCELLED);
			agreement.setIsUnderWorkflow(Boolean.FALSE);
		}else if (WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workFlowDetails.getAction())) {
			agreement.setIsUnderWorkflow(Boolean.FALSE);
		}
		agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);
		return agreement;
	}

	public Agreement updateRenewal(AgreementRequest agreementRequest) {
		Agreement agreement = agreementRequest.getAgreement();
		logger.info("update renewal agreement ::" + agreement);
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		String userId = requestInfo.getUserInfo().getId().toString();
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		updateAuditDetails(agreement, requestInfo);
		if (workFlowDetails != null) {
			if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreementRepository.updateExistingAgreementAsHistory(agreement, userId);
				agreement.setAgreementNumber(agreementNumberService.generateAgrementNumber(agreement.getTenantId()));
				agreement.setStatus(Status.ACTIVE);
				agreement.setAgreementDate(new Date());
				agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
				List<Demand> demands = prepareDemandsForRenewalApproval(agreementRequest);
				updateDemand(agreement.getDemands(), demands,
						agreementRequest.getRequestInfo());
			} else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.REJECTED);
			} else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setStatus(Status.CANCELLED);
				agreement.setIsUnderWorkflow(Boolean.FALSE);
			}else if (WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workFlowDetails.getAction())) {
				agreement.setIsUnderWorkflow(Boolean.FALSE);
			}
		}
		agreementMessageQueueRepository.save(agreementRequest, UPDATE_WORKFLOW);
		return agreement;
	}

	private List<Demand> prepareDemandsForRenewalApproval(AgreementRequest agreementRequest) {
		String demandId = agreementRequest.getAgreement().getDemands().get(0);
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		List<Demand> demands = new ArrayList<>();
		if (demandId != null) {
			demandSearchCriteria.setDemandId(Long.valueOf(demandId));
			DemandResponse demandResponse = demandRepository
					.getDemandBySearch(demandSearchCriteria, agreementRequest.getRequestInfo());
			if(demandResponse != null){
				Demand demand = demandResponse.getDemands().get(0);
				if (demand != null) {
					demand.getDemandDetails().addAll(demandService.prepareDemandDetailsForRenewal(agreementRequest,
							demandRepository.getDemandReasonForRenewal(agreementRequest, true)));
					demands.add(demand);
				}
			}
		}
		return demands;
	}

	public List<Agreement> searchAgreement(AgreementCriteria agreementCriteria, RequestInfo requestInfo) {

		if (agreementCriteria.getToDate() != null) {
			agreementCriteria.setToDate(setToTime(agreementCriteria.getToDate()));
		}

		if (agreementCriteria.isAgreementAndAssetAndAllotteeNotNull()) {
			logger.info("agreementRepository.findByAllotee");
			return agreementRepository.findByAllotee(agreementCriteria, requestInfo);

		} else if (agreementCriteria.isAssetOnlyNull()) {
			logger.info("agreementRepository.findByAllotee");
			return agreementRepository.findByAgreementAndAllotee(agreementCriteria, requestInfo);

		} else if (agreementCriteria.isAllotteeOnlyNull()) {
			logger.info("agreementRepository.findByAgreementAndAsset : both agreement and ");
			return agreementRepository.findByAgreementAndAsset(agreementCriteria, requestInfo);

		} else if (agreementCriteria.isAgreementAndAssetNull()
				|| agreementCriteria.isAgreementOnlyNull()) {
			logger.info("agreementRepository.findByAllotee : only allottee || allotte and asset");
			return agreementRepository.findByAllotee(agreementCriteria, requestInfo);

		} else if (agreementCriteria.isAgreementAndAllotteeNull()) {
			logger.info("agreementRepository.findByAsset : only asset");
			return agreementRepository.findByAsset(agreementCriteria, requestInfo);

		} else if (agreementCriteria.isAssetAndAllotteeNull()) {
			logger.info("agreementRepository.findByAgreement : only agreement");
			return agreementRepository.findByAgreement(agreementCriteria, requestInfo);
		} else {
			// if no values are given for all the three criteria objects
			// (isAgreementNull && isAssetNull && isAllotteeNull)
			logger.info("agreementRepository.findByAgreement : all values null");
			return agreementRepository.findByAgreement(agreementCriteria, requestInfo);
		}
	}
	
	public List<Agreement> getAgreementsByAgreementNumber(AgreementCriteria agreementCriteria, RequestInfo requestInfo){
		return agreementRepository.findByAgreementNumber(agreementCriteria, requestInfo);
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
	
	public List<Demand> getDemands(AgreementRequest agreementRequest) {

		List<Demand> agreementDemands = null;
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		Agreement agreement = agreementRequest.getAgreement();
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();

		demandSearchCriteria.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
		agreementDemands = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands();
		return agreementDemands;
	}

	private List<String> updateDemand(List<String> demands, List<Demand> legacydemands, RequestInfo requestInfo) {
		DemandResponse demandResponse = null;
		if (demands == null)
			demandResponse = demandRepository.createDemand(legacydemands, requestInfo);
		else
			demandResponse = demandRepository.updateDemand(legacydemands, requestInfo);
		return demandResponse.getDemands().stream().map(Demand::getId).collect(Collectors.toList());
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

	public Date getExpiryDate(Agreement agreement) {
		Date commencementDate;
		if(Action.RENEWAL.equals(agreement.getAction()))
			commencementDate = agreement.getRenewalDate();
		else
			commencementDate = agreement.getCommencementDate();
		Instant instant = Instant.ofEpochMilli(commencementDate.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate expiryDate = localDateTime.toLocalDate();
		expiryDate = expiryDate.plusYears(agreement.getTimePeriod());
		expiryDate = expiryDate.minusDays(1);
		return Date.from(expiryDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	private Date getAdjustmentDate(Agreement agreement) {

		Long monthsToReduce = (long) (agreement.getSecurityDeposit() / agreement.getRent());
		if (PaymentCycle.QUARTER.equals(agreement.getPaymentCycle())) {
			monthsToReduce = monthsToReduce * 3;
		} else if (PaymentCycle.HALFYEAR.equals(agreement.getPaymentCycle())) {
			monthsToReduce = monthsToReduce * 6;
		} else if (PaymentCycle.ANNUAL.equals(agreement.getPaymentCycle())) {
			monthsToReduce = monthsToReduce * 12;
		}
		Date expiryDate = agreement.getExpiryDate();
		Instant instant = Instant.ofEpochMilli(expiryDate.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate adjustmentDate = localDateTime.toLocalDate();
		adjustmentDate = adjustmentDate.minusMonths(monthsToReduce);
		adjustmentDate = adjustmentDate.plusDays(1);
		return Date.from(adjustmentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

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
