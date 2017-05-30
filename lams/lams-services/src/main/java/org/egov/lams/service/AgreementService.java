package org.egov.lams.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.lams.brokers.producer.AgreementProducer;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandReason;
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.model.enums.Source;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.AgreementRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.util.AcknowledgementNumberUtil;
import org.egov.lams.util.AgreementNumberUtil;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.DemandResponse;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.Position;
import org.egov.lams.web.contract.PositionResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AgreementService {
	public static final Logger logger = LoggerFactory.getLogger(AgreementService.class);

	@Autowired
	private AgreementRepository agreementRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private LamsConfigurationService lamsConfigurationService;

	@Autowired
	private AgreementProducer agreementProducer;

	@Autowired
	private DemandRepository demandRepository;

	@Autowired
	private AcknowledgementNumberUtil acknowledgementNumberService;

	@Autowired
	private AgreementNumberUtil agreementNumberService;

	@Autowired
	private PropertiesManager propertiesManager;

	/**
	 * service call to single agreement based on acknowledgementNumber
	 * 
	 * @param code
	 * @return
	 */
	public boolean isAgreementExist(String code) {

		return agreementRepository.isAgreementExist(code);
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
		ObjectMapper mapper = new ObjectMapper();
		String agreementValue = null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(agreement.getCommencementDate());
			calendar.add(Calendar.YEAR, 1);
			calendar.add(Calendar.DATE, -1);
		Date expiryDate = calendar.getTime();
		agreement.setExpiryDate(expiryDate);
		logger.info("The closeDate calculated is " + expiryDate + "from commencementDate of "
				+ agreement.getCommencementDate() + "by adding with no of years " + agreement.getTimePeriod());

		if (agreement.getSource().equals(Source.DATA_ENTRY)) {
			kafkaTopic = propertiesManager.getSaveAgreementTopic();
			agreement.setStatus(Status.ACTIVE);
			agreement.setAgreementNumber(agreementNumberService.generateAgrementNumber());
		} else {
			kafkaTopic = propertiesManager.getStartWorkflowTopic();
			agreement.setStatus(Status.WORKFLOW);

			setInitiatorPosition(agreementRequest);
			List<Demand> demands = prepareDemands(agreementRequest);

			DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
			List<String> demandIdList = demandResponse.getDemands().stream().map(demand -> demand.getId())
					.collect(Collectors.toList());
			agreement.setDemands(demandIdList);
			agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
			logger.info(agreement.getAcknowledgementNumber());
		}
		agreement.setId(agreementRepository.getAgreementID());
		try {

			agreementValue = mapper.writeValueAsString(agreementRequest);
			logger.info("agreementValue::" + agreementValue);
		} catch (JsonProcessingException JsonProcessingException) {
			logger.info("AgreementService : " + JsonProcessingException.getMessage(), JsonProcessingException);
			throw new RuntimeException(JsonProcessingException.getMessage());
		}

		try {
			agreementProducer.sendMessage(kafkaTopic, "save-agreement", agreementValue);
		} catch (Exception exception) {
			logger.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
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
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		logger.info("updateagreement service :: " + workFlowDetails);
		ObjectMapper mapper = new ObjectMapper();
		String agreementValue = null;
		String kafkaTopic = null;

		if (agreement.getSource().equals(Source.DATA_ENTRY)) {
			logger.info("updateagreementservice Source.DATA_ENTRY");
			kafkaTopic = propertiesManager.getUpdateAgreementTopic();
			agreement.setDemands(updateDemnad(agreement.getDemands(), agreement.getLegacyDemands(),
					agreementRequest.getRequestInfo()));
			logger.info("the id from demand save call :: " + agreement.getDemands());
		} else if (agreement.getSource().equals(Source.SYSTEM)) {

			kafkaTopic = propertiesManager.getUpdateWorkflowTopic();

			if (workFlowDetails != null) {
				logger.info("updateagreementservice Source.SYSTEM");
				logger.info("the workflow details status :: " + workFlowDetails.getAction());
				if ("Approve".equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setAgreementNumber(agreementNumberService.generateAgrementNumber());
					logger.info("createAgreement service Agreement_No::" + agreement.getAgreementNumber());
					agreement.setAgreementDate(new Date());
					logger.info("createAgreement service Agreement_No::" + agreement.getStatus());
				} else if ("Reject".equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.CANCELLED);
					logger.info("createAgreement service Agreement_No::" + agreement.getStatus());
				} else if ("print notice".equalsIgnoreCase(workFlowDetails.getAction())) {
					agreement.setStatus(Status.ACTIVE);
					logger.info("createAgreement service Agreement_No::" + agreement.getStatus());
				}
			}
		}
		logger.info("kafkatopic value :: " + kafkaTopic);
		try {
			agreementValue = mapper.writeValueAsString(agreementRequest);
			logger.info("agreementValue::" + agreementValue);
		} catch (JsonProcessingException jsonProcessingException) {
			logger.info("AgreementService : " + jsonProcessingException.getMessage(), jsonProcessingException);
			throw new RuntimeException(jsonProcessingException.getMessage());
		}

		try {
			agreementProducer.sendMessage(kafkaTopic, "save-agreement", agreementValue);
		} catch (Exception exception) {
			logger.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
		return agreement;
	}

	private List<String> updateDemnad(List<String> demands, List<Demand> legacydemands, RequestInfo requestInfo) {

		DemandResponse demandResponse = null;
		if (demands == null)
			demandResponse = demandRepository.createDemand(legacydemands, requestInfo);
		else
			demandResponse = demandRepository.updateDemand(legacydemands, requestInfo);
		return demandResponse.getDemands().stream().map(demand -> demand.getId()).collect(Collectors.toList());
	}

	public List<Demand> prepareDemands(AgreementRequest agreementRequest) {

		List<Demand> legacyDemands = null;
		Agreement agreement = agreementRequest.getAgreement();
		List<String> demandIds = agreement.getDemands();
		if (demandIds == null) {

			List<DemandReason> demandReasons = demandRepository.getDemandReason(agreementRequest);
			if (demandReasons.isEmpty())
				throw new RuntimeException("No demand reason found for given criteria");
			legacyDemands = demandRepository.getDemandList(agreementRequest, demandReasons);
		} else {
			DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
			demandSearchCriteria.setDemandId(Long.parseLong(demandIds.get(0)));
			legacyDemands = demandRepository.getDemandBySearch(demandSearchCriteria, agreementRequest.getRequestInfo())
					.getDemands();
		}
		return legacyDemands;
	}

	public List<Agreement> searchAgreement(AgreementCriteria agreementCriteria, RequestInfo requestInfo) {
		/*
		 * three boolean variables isAgreementNull,isAssetNull and
		 * isAllotteeNull declared to indicate whether criteria arguments for
		 * each of the Agreement,Asset and Allottee objects are given or not.
		 */
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
				&& agreementCriteria.getElectionWard() == null && agreementCriteria.getDoorno() == null;

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

	private void setInitiatorPosition(AgreementRequest agreementRequest) {

		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		Agreement agreement = agreementRequest.getAgreement();
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(agreementRequest.getRequestInfo());
		String tenantId = requestInfoWrapper.getRequestInfo().getUserInfo().getTenantId();

		PositionResponse positionResponse = null;
		String positionUrl = propertiesManager.getEmployeeServiceHostName() + propertiesManager
				.getEmployeeServiceSearchPath().replace(propertiesManager.getEmployeeServiceSearchPathVariable(),
						requestInfo.getUserInfo().getId().toString())
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
			positionMap.put(position.getDeptdesig().getDesignation().getName(),
					position.getDeptdesig().getDesignation().getId());
		}

		LamsConfigurationGetRequest lamsConfigurationGetRequest = new LamsConfigurationGetRequest();
		String keyName = propertiesManager.getWorkflowInitiatorPositionkey();
		lamsConfigurationGetRequest.setName(keyName);
		List<String> assistantDesignations = lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest)
				.get(keyName);

		for (String desginationName : assistantDesignations) {
			if (positionMap.containsKey(desginationName)) {
				workFlowDetails.setInitiatorPosition(positionMap.get(desginationName));
				logger.info(" the initiator name  :: " + desginationName + "the value for key"
						+ workFlowDetails.getInitiatorPosition());
			}
		}
	}
}
