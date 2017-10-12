package org.egov.mr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.Page;
import org.egov.mr.model.Position;
import org.egov.mr.model.enums.Action;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.Source;
import org.egov.mr.repository.MarriageCertRepository;
import org.egov.mr.repository.MarriageRegnRepository;
import org.egov.mr.repository.MarryingPersonRepository;
import org.egov.mr.repository.UserPositionRepository;
import org.egov.mr.util.SequenceIdGenService;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.mr.web.contract.MarriageRegnResponse;
import org.egov.mr.web.contract.PositionResponse;
import org.egov.mr.web.contract.RequestInfoWrapper;
import org.egov.mr.web.contract.ResponseInfoFactory;
import org.egov.mr.web.contract.User;
import org.egov.mr.web.contract.UserResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarriageRegnService {

	@Autowired
	private MarriageRegnRepository marriageRegnRepository;

	@Autowired
	private MarryingPersonRepository marryingPersonRepository;

	@Autowired
	private MarriageCertRepository marriageCertRepository;

	@Autowired
	private RegnNumberService regnNumberService;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private SequenceIdGenService sequenceGenService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserPositionRepository userRepository;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public MarriageRegnResponse getMarriageRegns(MarriageRegnCriteria marriageRegnCriteria, RequestInfo requestInfo) {
		return getSuccessResponseForSearch(marriageRegnRepository.findForCriteria(marriageRegnCriteria),requestInfo);
	}

	private void populateAuditDetailsForMarriageRegnCreate(MarriageRegnRequest marriageRegnRequest) {
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();

		marriageRegn.setIsActive(false);

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(requestInfo.getDid());
		auditDetails.setCreatedTime(new Date().getTime());
		auditDetails.setLastModifiedBy(requestInfo.getDid());
		auditDetails.setLastModifiedTime(new Date().getTime());
		marriageRegn.setAuditDetails(auditDetails);
	}

	@Transactional
	public void create(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		marriageRegnRepository.save(marriageRegn);
		marryingPersonRepository.save(marriageRegn.getBridegroom(), marriageRegn.getTenantId());
		marryingPersonRepository.save(marriageRegn.getBride(), marriageRegn.getTenantId());

	}

	public MarriageRegnResponse createAsync(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		marriageRegn.setId(sequenceGenService.getIds(1, "seq_egmr_regn_number").get(0));
		populateAuditDetailsForMarriageRegnCreate(marriageRegnRequest);

		//setInitiatorPosition(marriageRegnRequest);
		marriageRegn.setApplicationNumber(marriageRegnRepository.generateApplicationNumber());

		log.info("marriageRegnRequest::" + marriageRegnRequest);
			kafkaTemplate.send(propertiesManager.getCreateMarriageFeeGenerated(), marriageRegnRequest);
		return getSuccessResponseForCreate(marriageRegn,marriageRegnRequest.getRequestInfo());
	}

	public MarriageRegnResponse updateAsync(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		ApprovalDetails workFlowDetails = marriageRegn.getApprovalDetails();
		populateAuditDetailsForMarriageRegnUpdate(marriageRegnRequest);
		populateDefaultDetailsForMarriageRegnUpdate(marriageRegnRequest);
		String kafkaTopic = null;
		if (marriageRegn.getSource().equals(Source.DATA_ENTRY)) {
			kafkaTopic = propertiesManager.getUpdateMarriageRegnTopicName();
		} else if (marriageRegn.getSource().equals(Source.SYSTEM)) {
			kafkaTopic = propertiesManager.getKafkaUpdateworkflowTopic();
			if (workFlowDetails != null) {
				if ("Approve".equalsIgnoreCase(workFlowDetails.getAction())
						&& (marriageRegn.getActions().equals(Action.CREATE))) {
					marriageRegn.setStatus(ApplicationStatus.ACTIVE);
					marriageRegn.setRegnDate(new Date().getTime());
					if (marriageRegn.getRegnNumber() == null) {
						marriageRegn
								.setRegnNumber(sequenceGenService.getIds(1, "seq_egmr_marriageregn_reg_number").get(0));
					}
				} else if ("Reject".equalsIgnoreCase(workFlowDetails.getAction())) {
					marriageRegn.setStatus(ApplicationStatus.REJECTED);
				} else if ("Cancel".equalsIgnoreCase(workFlowDetails.getAction())) {
					marriageRegn.setStatus(ApplicationStatus.CANCELLED);
				}
			}
		}
		log.info("marriageRegnRequest::" + marriageRegnRequest);
		kafkaTemplate.send(kafkaTopic, marriageRegnRequest);
		return getSuccessResponseForCreate(marriageRegn,marriageRegnRequest.getRequestInfo());
	}

	private void populateDefaultDetailsForMarriageRegnUpdate(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		if (marriageRegn.getStatus().toString().equals("REGISTERED")) {
			marriageRegn.setRegnNumber(regnNumberService.generateRegnNumber());
			marriageRegn.setRegnDate(new Date().getTime());
			marriageRegn.setIsActive(true);
		}
	}

	private void populateAuditDetailsForMarriageRegnUpdate(MarriageRegnRequest marriageRegnRequest) {
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();

		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setLastModifiedBy(requestInfo.getDid());
		auditDetails.setLastModifiedTime(new Date().getTime());
		marriageRegn.setAuditDetails(auditDetails);
	}

	@Transactional
	public void update(MarriageRegnRequest marriageRegnRequest) {
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		marriageRegnRepository.update(marriageRegn);
		marryingPersonRepository.update(marriageRegn.getBridegroom(), marriageRegn.getTenantId());
		marryingPersonRepository.update(marriageRegn.getBride(), marriageRegn.getTenantId());
		// List<String> applicationNosList =
		// marriageCertRepository.getApplicationNos(marriageRegn.getTenantId());
		if (marriageRegn.getStatus().toString().equals("REGISTERED"))
			marriageCertRepository.insert(marriageRegn);

	}

	private void setInitiatorPosition(MarriageRegnRequest marriageRegnRequest) {

		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		ApprovalDetails workFlowDetails = marriageRegn.getApprovalDetails();

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(marriageRegnRequest.getRequestInfo());
		String tenantId = marriageRegn.getTenantId();

		User user = new User();
		user.setUserName(requestInfo.getUserInfo().getUserName());
		user.setTenantId(tenantId);
//		user.setMobileNumber(requestInfo.getUserInfo().getMobileNumber());
//		user.setEmailId(requestInfo.getUserInfo().getEmailId());
		UserResponse userResponse = userRepository.getUser(user,
				requestInfoWrapper.getRequestInfo());
		user = userResponse.getUsers().get(0);

		PositionResponse positionResponse = null;
		String positionUrl = propertiesManager.getEmployeeServiceHostName()
				+ propertiesManager.getEmployeeServiceSearchPath()
						.replace(propertiesManager.getEmployeeServiceSearchPathVariable(), user.getId().toString())
				+ "?tenantId=" + tenantId;

		log.info("the request url to position get call :: " + positionUrl);
		log.info("the request body to position get call :: " + requestInfoWrapper);

		try {
			positionResponse = restTemplate.postForObject(positionUrl, requestInfoWrapper, PositionResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("the exception from poisition search :: " + e);
			throw e;
		}

		log.info("the response form position get call :: " + positionResponse);

		List<Position> positionList = positionResponse.getPosition();
		if (positionList == null || positionList.isEmpty())
			throw new RuntimeException("the respective user has no positions");
		Map<String, Long> positionMap = new HashMap<>();

		for (Position position : positionList) {
			positionMap.put(position.getDeptdesig().getDesignation().getName(), position.getId());
		}

		/*
		 * ServiceConfigurationSearchCriteria config = new
		 * ServiceConfigurationSearchCriteria(); String keyName =
		 * propertiesManager.getWorkflowInitiatorPositionkey(); List<String>
		 * names = new ArrayList<>(); names.add(keyName);
		 * config.setNames(names); List<String> assistantDesignations =
		 * serviceConfigurationService.search(config).get(keyName); for (String
		 * desginationName : assistantDesignations) { log.info("desg name" +
		 * desginationName); if (positionMap.containsKey(desginationName)) {
		 * workFlowDetails.setInitiatorPosition(positionMap.get(desginationName)
		 * ); log.info(" the initiator name  :: " + desginationName +
		 * "the value for key" + workFlowDetails.getInitiatorPosition()); } }
		 */

	}
	
	private MarriageRegnResponse getSuccessResponseForCreate(MarriageRegn marriageRegn, RequestInfo requestInfo) {
		MarriageRegnResponse marriageRegnResponse = new MarriageRegnResponse();
		List<MarriageRegn> marriageRegns = new ArrayList<MarriageRegn>();
		marriageRegns.add(marriageRegn);
		marriageRegnResponse.setMarriageRegns(marriageRegns);
		Page page=new Page();
		marriageRegnResponse.setPage(page);
	
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		marriageRegnResponse.setResponseInfo(responseInfo);
		return marriageRegnResponse;
	}
	
	private MarriageRegnResponse getSuccessResponseForSearch(List<MarriageRegn> marriageRegnList, RequestInfo requestInfo) {
		MarriageRegnResponse marriageRegnResponse = new MarriageRegnResponse();
		marriageRegnResponse.setMarriageRegns(marriageRegnList);
		System.out.println("marriageRegnList=" + marriageRegnList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		marriageRegnResponse.setResponseInfo(responseInfo);
		return marriageRegnResponse;
	}

}