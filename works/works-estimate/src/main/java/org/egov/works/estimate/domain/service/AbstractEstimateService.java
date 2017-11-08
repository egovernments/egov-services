package org.egov.works.estimate.domain.service;

import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.config.WorksEstimateServiceConstants;
import org.egov.works.estimate.domain.repository.AbstractEstimateRepository;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.egov.works.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class AbstractEstimateService {

	@Autowired
	private AbstractEstimateRepository abstractEstimateRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private IdGenerationRepository idGenerationRepository;

	@Autowired
	private EstimateUtils estimateUtils;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProjectCodeService projectCodeService;

	@Transactional
	public List<AbstractEstimate> create(AbstractEstimateRequest abstractEstimateRequest) {
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			estimate.setId(commonUtils.getUUID());
			estimate.setAuditDetails(
					setAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUsername(), false));
			String abstractEstimateNumber = idGenerationRepository
					.generateAbstractEstimateNumber(estimate.getTenantId(), abstractEstimateRequest.getRequestInfo());
			estimate.setAbstractEstimateNumber(propertiesManager.getEstimateNumberPrefix() + "/"
					+ estimate.getDepartment().getCode() + abstractEstimateNumber);
			for (final AbstractEstimateDetails details : estimate.getAbstractEstimateDetails()) {
				details.setId(commonUtils.getUUID());
				details.setAuditDetails(
						setAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUsername(), false));
			}
			if (estimate.getSpillOverFlag())
				estimate.setStatus(AbstractEstimateStatus.APPROVED);
			else {
				populateWorkFlowDetails(estimate, abstractEstimateRequest.getRequestInfo());
				estimate.setStateId(workflowService.enrichWorkflow(estimate.getWorkFlowDetails(),
						estimate.getTenantId(), abstractEstimateRequest.getRequestInfo()));
				estimate.setStatus(AbstractEstimateStatus.CREATED);
			}
		}
		kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateCreateTopic(), abstractEstimateRequest);
		return abstractEstimateRequest.getAbstractEstimates();
	}

	public List<AbstractEstimate> update(AbstractEstimateRequest abstractEstimateRequest) {
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			populateWorkFlowDetails(estimate, abstractEstimateRequest.getRequestInfo());
			estimate.setAuditDetails(
					setAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUsername(), true));
			estimate.setStateId(workflowService.enrichWorkflow(estimate.getWorkFlowDetails(), estimate.getTenantId(),
					abstractEstimateRequest.getRequestInfo()));

			populateNextStatus(estimate);

			ProjectCode projectCode = new ProjectCode();
			if (estimate.getStatus().toString().equalsIgnoreCase(AbstractEstimateStatus.APPROVED.toString())) {
				for (AbstractEstimateDetails abstractEstimateDetails : estimate.getAbstractEstimateDetails()) {
					projectCode.setCode(setProjectCode(abstractEstimateDetails, estimate.getSpillOverFlag(),
							abstractEstimateRequest.getRequestInfo()));
					abstractEstimateDetails.setProjectCode(projectCode);
					setEstimateAppropriation(estimate, abstractEstimateRequest.getRequestInfo());

				}
			}
		}
		kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateUpdateTopic(), abstractEstimateRequest);
		return abstractEstimateRequest.getAbstractEstimates();
	}

	private void populateWorkFlowDetails(AbstractEstimate abstractEstimate, RequestInfo requestInfo) {

		if (null != abstractEstimate && null != abstractEstimate.getWorkFlowDetails()) {

			WorkFlowDetails workFlowDetails = abstractEstimate.getWorkFlowDetails();

			workFlowDetails.setType(CommonConstants.ABSTRACT_ESTIMATE_WF_TYPE);
			workFlowDetails.setBusinessKey(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
			workFlowDetails.setStateId(abstractEstimate.getStateId());
			if (abstractEstimate.getStatus() != null)
				workFlowDetails.setStatus(abstractEstimate.getStatus().toString());

			if (null != requestInfo && null != requestInfo.getUserInfo()) {
				workFlowDetails.setSenderName(requestInfo.getUserInfo().getUsername());
			}

			if (abstractEstimate.getStateId() != null) {
				workFlowDetails.setStateId(abstractEstimate.getStateId());
			}
		}
	}

	private void populateNextStatus(AbstractEstimate abstractEstimate) {
		WorkFlowDetails workFlowDetails = null;
		String currentStatus = null;

		if (null != abstractEstimate && null != abstractEstimate.getStatus()) {
			workFlowDetails = abstractEstimate.getWorkFlowDetails();
			currentStatus = abstractEstimate.getStatus().toString();
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Submit")
				&& (currentStatus.equalsIgnoreCase(AbstractEstimateStatus.CREATED.toString())
						|| currentStatus.equalsIgnoreCase(AbstractEstimateStatus.RESUBMITTED.toString()))) {
			abstractEstimate.setStatus(AbstractEstimateStatus.CHECKED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Approve")
				&& currentStatus.equalsIgnoreCase(AbstractEstimateStatus.CHECKED.toString())) {
			abstractEstimate.setStatus(AbstractEstimateStatus.APPROVED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Reject")) {
			abstractEstimate.setStatus(AbstractEstimateStatus.REJECTED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Forward")
				&& currentStatus.equalsIgnoreCase(AbstractEstimateStatus.REJECTED.toString())) {
			abstractEstimate.setStatus(AbstractEstimateStatus.RESUBMITTED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Cancel")
				&& currentStatus.equalsIgnoreCase(AbstractEstimateStatus.REJECTED.toString())) {
			abstractEstimate.setStatus(AbstractEstimateStatus.CANCELLED);
		}
	}

	public List<AbstractEstimate> search(AbstractEstimateSearchContract abstractEstimateSearchContract) {
		return abstractEstimateRepository.search(abstractEstimateSearchContract);
	}

	public void validateEstimates(AbstractEstimateRequest abstractEstimateRequest, BindingResult errors,
			Boolean isNew) {
		Map<String, String> messages = new HashMap<>();
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			if (estimate.getDateOfProposal() == null)
				messages.put(WorksEstimateServiceConstants.KEY_NULL_DATEOFPROPOSAL,
						WorksEstimateServiceConstants.MESSAGE_NULL_DATEOFPROPOSAL);
			if (!estimate.getSpillOverFlag() && estimate.getDateOfProposal() != null
					&& new Date(estimate.getDateOfProposal()).after(new Date()))
				messages.put(WorksEstimateServiceConstants.KEY_FUTUREDATE_DATEOFPROPOSAL,
						WorksEstimateServiceConstants.MESSAGE_FUTUREDATE_DATEOFPROPOSAL);
			if (estimate.getTenantId() == null)
				messages.put(WorksEstimateServiceConstants.KEY_NULL_TENANTID,
						WorksEstimateServiceConstants.MESSAGE_NULL_TENANTID);
			validateMasterData(estimate, errors, abstractEstimateRequest.getRequestInfo(), messages);

			AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
			if (estimate.getId() != null)
				searchContract.setIds(Arrays.asList(estimate.getId()));
			searchContract.setAbstractEstimateNumbers(Arrays.asList(estimate.getAbstractEstimateNumber()));
			searchContract.setTenantId(estimate.getTenantId());
			List<AbstractEstimate> oldEstimates = search(searchContract);
			if (isNew && !oldEstimates.isEmpty())
				messages.put(WorksEstimateServiceConstants.KEY_UNIQUE_ABSTRACTESTIMATENUMBER,
						WorksEstimateServiceConstants.MESSAGE_UNIQUE_ABSTRACTESTIMATENUMBER);
			searchContract.setAbstractEstimateNumbers(null);
			if (!isNew && estimate.getWorkFlowDetails() != null
					&& "Approve".equalsIgnoreCase(estimate.getWorkFlowDetails().getAction())) {
				if (StringUtils.isBlank(estimate.getAdminSanctionNumber()))
					messages.put(WorksEstimateServiceConstants.KEY_NULL_ADMINSANCTIONNUMBER,
							WorksEstimateServiceConstants.MESSAGE_NULL_ADMINSANCTIONNUMBER);
				searchContract.setAdminSanctionNumbers(Arrays.asList(estimate.getAdminSanctionNumber()));
				oldEstimates = search(searchContract);
				if (!oldEstimates.isEmpty() && !estimate.getId().equalsIgnoreCase(oldEstimates.get(0).getId()))
					messages.put(WorksEstimateServiceConstants.KEY_UNIQUE_ADMINSANCTIONNUMBER,
							WorksEstimateServiceConstants.MESSAGE_UNIQUE_ADMINSANCTIONNUMBER);
			}
			validateEstimateDetails(estimate, errors, isNew, messages);

			if (messages != null && !messages.isEmpty())
				throw new CustomException(messages);
		}
	}

	private void validateEstimateDetails(AbstractEstimate estimate, BindingResult errors, Boolean isNew,
			Map<String, String> messages) {
		BigDecimal estimateAmount = BigDecimal.ZERO;
		for (final AbstractEstimateDetails aed : estimate.getAbstractEstimateDetails()) {
			estimateAmount = estimateAmount.add(aed.getEstimateAmount());
			AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
			if (estimate.getId() != null)
				searchContract.setIds(Arrays.asList(estimate.getId()));
			searchContract.setEstimateNumbers(Arrays.asList(aed.getEstimateNumber()));
			searchContract.setTenantId(estimate.getTenantId());
			List<AbstractEstimate> oldEstimates = search(searchContract);
			if (isNew && !oldEstimates.isEmpty())
				messages.put(WorksEstimateServiceConstants.KEY_UNIQUE_ESTIMATENUMBER,
						WorksEstimateServiceConstants.MESSAGE_UNIQUE_ESTIMATENUMBER);
		}
		if (Double.parseDouble(estimateAmount.toString()) <= 0)
			messages.put(WorksEstimateServiceConstants.KEY_INVALID_ESTIMATEAMOUNT,
					WorksEstimateServiceConstants.MESSAGE_INVALID_ESTIMATEAMOUNT);
	}

	public void validateMasterData(AbstractEstimate abstractEstimate, BindingResult errors, RequestInfo requestInfo,
			Map<String, String> messages) {

		JSONArray responseJSONArray = null;

		if (abstractEstimate.getFund() != null && StringUtils.isNotBlank(abstractEstimate.getFund().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.FUND_OBJECT,
					CommonConstants.CODE, abstractEstimate.getFund().getCode(), abstractEstimate.getTenantId(), requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_FUND_INVALID,
						WorksEstimateServiceConstants.MESSAGE_FUND_INVALID);
			}
		}
		if (abstractEstimate.getFunction() != null
				&& StringUtils.isNotBlank(abstractEstimate.getFunction().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.FUNCTION_OBJECT,
					CommonConstants.CODE,abstractEstimate.getFunction().getCode(), abstractEstimate.getTenantId(), requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_FUNCTION_INVALID,
						WorksEstimateServiceConstants.MESSAGE_FUNCTION_INVALID);
			}
		}

		if (abstractEstimate.getTypeOfWork() != null
				&& StringUtils.isNotBlank(abstractEstimate.getTypeOfWork().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.TYPEOFWORK_OBJECT,
					CommonConstants.CODE ,abstractEstimate.getTypeOfWork().getCode(), abstractEstimate.getTenantId(), requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_TYPEOFWORK_INVALID,
						WorksEstimateServiceConstants.MESSAGE_TYPEOFWORK_INVALID);
			}
		}
		if (abstractEstimate.getSubTypeOfWork() != null
				&& StringUtils.isNotBlank(abstractEstimate.getSubTypeOfWork().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.TYPEOFWORK_OBJECT,
					CommonConstants.CODE,abstractEstimate.getSubTypeOfWork().getCode(), abstractEstimate.getTenantId(), requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_SUBTYPEOFWORK_INVALID,
						WorksEstimateServiceConstants.MESSAGE_SUBTYPEOFWORK_INVALID);
			}
		}
		if (abstractEstimate.getDepartment() != null
				& StringUtils.isNotBlank(abstractEstimate.getDepartment().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.DEPARTMENT_OBJECT,
					CommonConstants.CODE,abstractEstimate.getDepartment().getCode(), abstractEstimate.getTenantId(), requestInfo,
					WorksEstimateServiceConstants.COMMON_MASTERS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_DEPARTMENT_INVALID,
						WorksEstimateServiceConstants.MESSAGE_DEPARTMENT_INVALID);
			}
		}
		if (abstractEstimate.getScheme() != null & StringUtils.isNotBlank(abstractEstimate.getScheme().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.SCHEME_OBJECT,
					CommonConstants.CODE,abstractEstimate.getScheme().getCode(), abstractEstimate.getTenantId(), requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_SCHEME_INVALID,
						WorksEstimateServiceConstants.MESSAGE_SCHEME_INVALID);
			}
		}

		if (abstractEstimate.getSubScheme() != null
				& StringUtils.isNotBlank(abstractEstimate.getSubScheme().getCode())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.SUBSCHEME_OBJECT,
					CommonConstants.CODE,abstractEstimate.getSubScheme().getCode(), abstractEstimate.getTenantId(), requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_SUBSCHEME_INVALID,
						WorksEstimateServiceConstants.MESSAGE_SUBSCHEME_INVALID);
			}
		}

		if (abstractEstimate.getBudgetGroup() != null
				& StringUtils.isNotBlank(abstractEstimate.getBudgetGroup().getName())) {
			responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.BUDGETGROUP_OBJECT, CommonConstants.NAME,
					abstractEstimate.getBudgetGroup().getName(), abstractEstimate.getTenantId(), requestInfo,
					WorksEstimateServiceConstants.WORKS_MODULE_CODE);
			if (responseJSONArray != null && responseJSONArray.isEmpty()) {
				messages.put(WorksEstimateServiceConstants.KEY_BUDGETGROUP_INVALID,
						WorksEstimateServiceConstants.MESSAGE_BUDGETGROUP_INVALID);
			}
		}

		/*
		 * * if(abstractEstimate.getWard() != null &
		 * StringUtils.isNotBlank(abstractEstimate.getWard().getCode())) {
		 * responseJSONArray =
		 * estimateUtils.getAppConfigurationData(WorksEstimateServiceConstants.
		 * DEPARTMENT_OBJECT,abstractEstimate.getWard().getCode(),
		 * abstractEstimate.getTenantId(),requestInfo); if(responseJSONArray !=
		 * null && responseJSONArray.isEmpty()) { throw new
		 * InvalidDataException("Boundary",
		 * "Invalid data for estimate Ward boundary",
		 * abstractEstimate.getWard().getCode()); } }
		 * if(abstractEstimate.getLocality() != null &
		 * StringUtils.isNotBlank(abstractEstimate.getLocality().getCode())) {
		 * responseJSONArray =
		 * estimateUtils.getAppConfigurationData(WorksEstimateServiceConstants.
		 * DEPARTMENT_OBJECT,abstractEstimate.getLocality().getCode(),
		 * abstractEstimate.getTenantId(),requestInfo); if(responseJSONArray !=
		 * null && responseJSONArray.isEmpty()) { throw new
		 * InvalidDataException("Boundary",
		 * "Invalid data for estimate locality boundary",
		 * abstractEstimate.getLocality().getCode()); } }
		 */

	}

	public AuditDetails setAuditDetails(final String userName, final Boolean isUpdate) {
		AuditDetails auditDetails = new AuditDetails();
		if (!isUpdate) {
			auditDetails.setCreatedBy(userName);
			auditDetails.setCreatedTime(new Date().getTime());
		}
		auditDetails.setLastModifiedBy(userName);
		auditDetails.setLastModifiedTime(new Date().getTime());

		return auditDetails;
	}

	public String setProjectCode(final AbstractEstimateDetails abstractEstimateDetails, boolean spillOverFlag,
			final RequestInfo requestInfo) {
		Map<String, String> messages = new HashMap<>();
		ProjectCode projectCode = new ProjectCode();
		if (spillOverFlag) {
			if (abstractEstimateDetails.getProjectCode() != null
					&& abstractEstimateDetails.getProjectCode().getCode() != null)
				projectCode.setCode(abstractEstimateDetails.getProjectCode().getCode());
			else
				messages.put(WorksEstimateServiceConstants.KEY_UNIQUE_WORKIDENTIFICATIONNUMBER,
						WorksEstimateServiceConstants.MESSAGE_UNIQUE_WORKIDENTIFICATIONNUMBER);
		}

		if (messages != null && !messages.isEmpty())
			throw new CustomException(messages);

		projectCode.setName(abstractEstimateDetails.getNameOfWork());
		projectCode.setDescription(abstractEstimateDetails.getNameOfWork());
		projectCode.setActive(true);
		projectCode.setStatus(ProjectCodeStatus.CREATED);
		projectCode.setTenantId(abstractEstimateDetails.getTenantId());

		ProjectCodeRequest projectCodeRequest = new ProjectCodeRequest();
		projectCodeRequest.setRequestInfo(requestInfo);
		List<ProjectCode> projectCodes = new ArrayList<>();
		projectCodes.add(projectCode);
		projectCodeRequest.setProjectCodes(projectCodes);
		List<ProjectCode> savedCodes = projectCodeService.create(projectCodeRequest);
		return savedCodes.get(0).getCode();
	}

	private void setEstimateAppropriation(AbstractEstimate abstractEstimate,
			final RequestInfo requestInfo) {
		EstimateAppropriationRequest estimateAppropriationRequest = new EstimateAppropriationRequest();
		EstimateAppropriation estimateAppropriation = new EstimateAppropriation();
		List<EstimateAppropriation> appropriations = new ArrayList<>();
		estimateAppropriation
				.setObjectNumber(abstractEstimate.getAbstractEstimateNumber());
		estimateAppropriation.setObjectType(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
		estimateAppropriation.setTenantId(abstractEstimate.getTenantId());
		estimateAppropriationRequest.setEstimateAppropriations(appropriations);
		estimateAppropriationRequest.setRequestInfo(requestInfo);

		final String url = propertiesManager.getWorksSeviceHostName() + propertiesManager.getEstimateAppropriationURL();
		restTemplate.postForObject(url, estimateAppropriationRequest, EstimateAppropriationResponse.class);
	}

}
