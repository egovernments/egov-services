package org.egov.works.estimate.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.domain.repository.AbstractEstimateRepository;
import org.egov.works.estimate.domain.validator.EstimateValidator;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.AbstractEstimate;
import org.egov.works.estimate.web.contract.AbstractEstimateAssetDetail;
import org.egov.works.estimate.web.contract.AbstractEstimateDetails;
import org.egov.works.estimate.web.contract.AbstractEstimateRequest;
import org.egov.works.estimate.web.contract.AbstractEstimateResponse;
import org.egov.works.estimate.web.contract.AbstractEstimateSanctionDetail;
import org.egov.works.estimate.web.contract.AbstractEstimateSearchContract;
import org.egov.works.estimate.web.contract.AbstractEstimateStatus;
import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.DocumentDetail;
import org.egov.works.estimate.web.contract.EstimateAppropriation;
import org.egov.works.estimate.web.contract.EstimateAppropriationRequest;
import org.egov.works.estimate.web.contract.EstimateAppropriationResponse;
import org.egov.works.estimate.web.contract.ProjectCode;
import org.egov.works.estimate.web.contract.ProjectCodeRequest;
import org.egov.works.estimate.web.contract.ProjectCodeStatus;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.WorkFlowDetails;
import org.egov.works.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
	
	@Autowired
	private EstimateValidator validator;

	@Transactional
	public AbstractEstimateResponse create(AbstractEstimateRequest abstractEstimateRequest) {
		validator.validateEstimates(abstractEstimateRequest, true);
		ProjectCode projectCode = new ProjectCode();
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			estimate.setId(commonUtils.getUUID());
			estimate.setAuditDetails(
					getAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUserName(), false));
			for (final AbstractEstimateDetails details : estimate.getAbstractEstimateDetails()) {
				details.setId(commonUtils.getUUID());
				details.setAuditDetails(
						getAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUserName(), false));
			}
			for (final AbstractEstimateSanctionDetail sanctionDetail : estimate.getSanctionDetails())
				sanctionDetail.setId(commonUtils.getUUID());
			for (final AbstractEstimateAssetDetail assetDetail : estimate.getAssetDetails()) {
				assetDetail.setId(commonUtils.getUUID());
				assetDetail.setAuditDetails(
						getAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUserName(), false));
			}
			for (final DocumentDetail documentDetail : estimate.getDocumentDetails()) {
				documentDetail.setId(commonUtils.getUUID());
				documentDetail.setObjectType(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
				documentDetail.setAuditDetails(
						getAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUserName(), false));
			}
			if (!estimate.getSpillOverFlag()) {
				String abstractEstimateNumber = idGenerationRepository
						.generateAbstractEstimateNumber(estimate.getTenantId(), abstractEstimateRequest.getRequestInfo());
				// TODO: check idgen to accept values to generate
				estimate.setAbstractEstimateNumber(propertiesManager.getEstimateNumberPrefix() + "/"
						+ estimate.getDepartment().getCode() + abstractEstimateNumber);
				for (AbstractEstimateDetails abstractEstimateDetails : estimate.getAbstractEstimateDetails()) {
					projectCode.setCode(setProjectCode(abstractEstimateDetails, estimate.getSpillOverFlag(),
							abstractEstimateRequest.getRequestInfo()));
					abstractEstimateDetails.setProjectCode(projectCode);

				}
			}
			populateWorkFlowDetails(estimate, abstractEstimateRequest.getRequestInfo());
			estimate.setStateId(workflowService.enrichWorkflow(estimate.getWorkFlowDetails(),
					estimate.getTenantId(), abstractEstimateRequest.getRequestInfo()));
			estimate.setStatus(AbstractEstimateStatus.CREATED);
		}
		kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateCreateTopic(), abstractEstimateRequest);
		final AbstractEstimateResponse response = new AbstractEstimateResponse();
		response.setAbstractEstimates(abstractEstimateRequest.getAbstractEstimates());
		response.setResponseInfo(estimateUtils.getResponseInfo(abstractEstimateRequest.getRequestInfo()));
		return response;
	}

	public AbstractEstimateResponse update(AbstractEstimateRequest abstractEstimateRequest) {
		validator.validateEstimates(abstractEstimateRequest, false);
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			for (final AbstractEstimateDetails details : estimate.getAbstractEstimateDetails())
				details.setAuditDetails(
						getAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUserName(), true));
			for (final AbstractEstimateAssetDetail assetDetail : estimate.getAssetDetails())
				assetDetail.setAuditDetails(
						getAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUserName(), true));
			for (final DocumentDetail documentDetail : estimate.getDocumentDetails())
				documentDetail.setAuditDetails(
						getAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUserName(), true));
			populateWorkFlowDetails(estimate, abstractEstimateRequest.getRequestInfo());
			estimate.setAuditDetails(
					getAuditDetails(abstractEstimateRequest.getRequestInfo().getUserInfo().getUserName(), true));
			estimate.setStateId(workflowService.enrichWorkflow(estimate.getWorkFlowDetails(), estimate.getTenantId(),
					abstractEstimateRequest.getRequestInfo()));

			populateNextStatus(estimate);

			if (estimate.getStatus().toString().equalsIgnoreCase(AbstractEstimateStatus.APPROVED.toString())) {
				for (AbstractEstimateDetails abstractEstimateDetails : estimate.getAbstractEstimateDetails()) {
					setEstimateAppropriation(abstractEstimateDetails, abstractEstimateRequest.getRequestInfo());

				}
			}
		}
		kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateUpdateTopic(), abstractEstimateRequest);
		final AbstractEstimateResponse response = new AbstractEstimateResponse();
		response.setAbstractEstimates(abstractEstimateRequest.getAbstractEstimates());
		response.setResponseInfo(estimateUtils.getResponseInfo(abstractEstimateRequest.getRequestInfo()));
		return response;
	}

	private void populateWorkFlowDetails(AbstractEstimate abstractEstimate, RequestInfo requestInfo) {

		if (null != abstractEstimate && null != abstractEstimate.getWorkFlowDetails()) {

			WorkFlowDetails workFlowDetails = abstractEstimate.getWorkFlowDetails();

			if (abstractEstimate.getSpillOverFlag()) {
				workFlowDetails.setType(CommonConstants.SPILLOVER_ABSTRACT_ESTIMATE_WF_TYPE);
				workFlowDetails.setBusinessKey(CommonConstants.SPILLOVER_ABSTRACT_ESTIMATE_BUSINESSKEY);
			} else {
				workFlowDetails.setType(CommonConstants.ABSTRACT_ESTIMATE_WF_TYPE);
				workFlowDetails.setBusinessKey(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
			}
			workFlowDetails.setStateId(abstractEstimate.getStateId());
			if (abstractEstimate.getStatus() != null)
				workFlowDetails.setStatus(abstractEstimate.getStatus().toString());

			if (null != requestInfo && null != requestInfo.getUserInfo()) {
				workFlowDetails.setSenderName(requestInfo.getUserInfo().getUserName());
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
				&& Constants.SUBMIT.equalsIgnoreCase(workFlowDetails.getAction())
				&& (currentStatus.equalsIgnoreCase(AbstractEstimateStatus.CREATED.toString())
						|| currentStatus.equalsIgnoreCase(AbstractEstimateStatus.RESUBMITTED.toString()))) {
			abstractEstimate.setStatus(AbstractEstimateStatus.CHECKED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& Constants.APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
			abstractEstimate.setStatus(AbstractEstimateStatus.APPROVED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& Constants.REJECT.equalsIgnoreCase(workFlowDetails.getAction())) {
			abstractEstimate.setStatus(AbstractEstimateStatus.REJECTED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& Constants.FORWARD.equalsIgnoreCase(workFlowDetails.getAction())
				&& currentStatus.equalsIgnoreCase(AbstractEstimateStatus.REJECTED.toString())) {
			abstractEstimate.setStatus(AbstractEstimateStatus.RESUBMITTED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& Constants.CANCEL.equalsIgnoreCase(workFlowDetails.getAction())
				&& currentStatus.equalsIgnoreCase(AbstractEstimateStatus.REJECTED.toString())) {
			abstractEstimate.setStatus(AbstractEstimateStatus.CANCELLED);
		}
	}

	public AbstractEstimateResponse search(AbstractEstimateSearchContract abstractEstimateSearchContract, RequestInfo requestInfo) {
		List<AbstractEstimate> abstractEstimates = abstractEstimateRepository.search(abstractEstimateSearchContract);
		final AbstractEstimateResponse response = new AbstractEstimateResponse();
		response.setAbstractEstimates(abstractEstimates);
		response.setResponseInfo(estimateUtils.getResponseInfo(requestInfo));
		return response;
	}

	public AuditDetails getAuditDetails(final String userName, final Boolean isUpdate) {
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
				messages.put(Constants.KEY_UNIQUE_WORKIDENTIFICATIONNUMBER,
						Constants.MESSAGE_UNIQUE_WORKIDENTIFICATIONNUMBER);
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

	private void setEstimateAppropriation(AbstractEstimateDetails abstractEstimateDetails,
			final RequestInfo requestInfo) {
		EstimateAppropriationRequest estimateAppropriationRequest = new EstimateAppropriationRequest();
		EstimateAppropriation estimateAppropriation = new EstimateAppropriation();
		List<EstimateAppropriation> appropriations = new ArrayList<>();
		if(abstractEstimateDetails.getProjectCode() != null && abstractEstimateDetails.getProjectCode().getCode() != null)
			estimateAppropriation
					.setObjectNumber(abstractEstimateDetails.getProjectCode().getCode());
		estimateAppropriation.setObjectType(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
		estimateAppropriation.setTenantId(abstractEstimateDetails.getTenantId());
		estimateAppropriationRequest.setEstimateAppropriations(appropriations);
		estimateAppropriationRequest.setRequestInfo(requestInfo);

		final String url = propertiesManager.getWorksSeviceHostName() + propertiesManager.getEstimateAppropriationURL();
		restTemplate.postForObject(url, estimateAppropriationRequest, EstimateAppropriationResponse.class);
	}

}
