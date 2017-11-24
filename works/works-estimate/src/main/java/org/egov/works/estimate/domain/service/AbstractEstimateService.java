package org.egov.works.estimate.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.config.PropertiesManager;
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
import org.egov.works.estimate.web.contract.DocumentDetail;
import org.egov.works.estimate.web.contract.EstimateAppropriation;
import org.egov.works.estimate.web.contract.EstimateAppropriationRequest;
import org.egov.works.estimate.web.contract.EstimateAppropriationResponse;
import org.egov.works.estimate.web.contract.ProjectCode;
import org.egov.works.estimate.web.contract.ProjectCodeRequest;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.WorkFlowDetails;
import org.egov.works.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONArray;

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
		Boolean isSpilloverWFReq = false;
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			estimate.setId(commonUtils.getUUID());
			estimate.setAuditDetails(estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), false));
			for (final AbstractEstimateDetails details : estimate.getAbstractEstimateDetails()) {
				details.setId(commonUtils.getUUID());
				details.setAuditDetails(estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), false));
			}
			for (final AbstractEstimateSanctionDetail sanctionDetail : estimate.getSanctionDetails())
				sanctionDetail.setId(commonUtils.getUUID());
			for (final AbstractEstimateAssetDetail assetDetail : estimate.getAssetDetails()) {
				assetDetail.setId(commonUtils.getUUID());
				assetDetail.setAuditDetails(
						estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), false));
			}

			if (!estimate.getSpillOverFlag()) {
				String abstractEstimateNumber = idGenerationRepository.generateAbstractEstimateNumber(
						estimate.getTenantId(), abstractEstimateRequest.getRequestInfo());
				// TODO: check idgen to accept values to generate
				estimate.setAbstractEstimateNumber(propertiesManager.getEstimateNumberPrefix() + "/"
						+ estimate.getDepartment().getCode() + abstractEstimateNumber);
			}
			for (final DocumentDetail documentDetail : estimate.getDocumentDetails()) {
				documentDetail.setId(commonUtils.getUUID());
				documentDetail.setObjectId(estimate.getAbstractEstimateNumber());
				documentDetail.setObjectType(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
				documentDetail.setAuditDetails(
						estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), false));
			}
			if(estimate.getSpillOverFlag()) {
				for (AbstractEstimateDetails abstractEstimateDetails : estimate.getAbstractEstimateDetails()) {
					projectCode.setCode(setProjectCode(abstractEstimateDetails, estimate.getSpillOverFlag(),
							abstractEstimateRequest.getRequestInfo()));
					abstractEstimateDetails.setProjectCode(projectCode);

				}
			}
			if (estimate.getSpillOverFlag())
				isSpilloverWFReq = isConfigRequired(CommonConstants.SPILLOVER_WORKFLOW_MANDATORY,
						abstractEstimateRequest.getRequestInfo(), estimate.getTenantId());
			if (!isSpilloverWFReq && estimate.getSpillOverFlag())
				estimate.setStatus(AbstractEstimateStatus.ADMIN_SANCTIONED);
			else {
				populateWorkFlowDetails(estimate, abstractEstimateRequest.getRequestInfo());
				Map<String, String> workFlowResponse = workflowService.enrichWorkflow(estimate.getWorkFlowDetails(),
						estimate.getTenantId(), abstractEstimateRequest.getRequestInfo());
				estimate.setStateId(workFlowResponse.get("id"));
				estimate.setStatus(AbstractEstimateStatus.valueOf(workFlowResponse.get("status")));
			}

		}
		kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateCreateTopic(), abstractEstimateRequest);
		final AbstractEstimateResponse response = new AbstractEstimateResponse();
		response.setAbstractEstimates(abstractEstimateRequest.getAbstractEstimates());
		response.setResponseInfo(estimateUtils.getResponseInfo(abstractEstimateRequest.getRequestInfo()));
		return response;
	}

	private Boolean isConfigRequired(String keyName, RequestInfo requestInfo, final String tenantId) {
		Boolean isSpilloverWFReq = false;
		JSONArray responseJSONArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
				keyName, tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
		if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
			Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
			if (jsonMap.get("value").equals("Yes"))
				isSpilloverWFReq = true;
		}
		return isSpilloverWFReq;
	}

	public AbstractEstimateResponse update(AbstractEstimateRequest abstractEstimateRequest) {
		validator.validateEstimates(abstractEstimateRequest, false);
		for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
			for (final DocumentDetail documentDetail : estimate.getDocumentDetails()) {
				if (documentDetail.getId().isEmpty())
					documentDetail.setId(commonUtils.getUUID());
				documentDetail.setObjectId(estimate.getAbstractEstimateNumber());
				documentDetail.setObjectType(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
				documentDetail
						.setAuditDetails(estimateUtils.setAuditDetails(abstractEstimateRequest.getRequestInfo(), true));
			}
			populateAuditDetails(abstractEstimateRequest.getRequestInfo(), estimate);
			populateWorkFlowDetails(estimate, abstractEstimateRequest.getRequestInfo());
			Map<String, String> workFlowResponse = workflowService.enrichWorkflow(estimate.getWorkFlowDetails(),
					estimate.getTenantId(), abstractEstimateRequest.getRequestInfo());
			estimate.setStateId(workFlowResponse.get("id"));
			estimate.setStatus(AbstractEstimateStatus.valueOf(workFlowResponse.get("status")));

			Boolean isFinIntReq = isConfigRequired(CommonConstants.FINANCIAL_INTEGRATION_KEY,
					abstractEstimateRequest.getRequestInfo(), estimate.getTenantId());

			if (isFinIntReq && estimate.getStatus().toString()
					.equalsIgnoreCase(AbstractEstimateStatus.ADMIN_SANCTIONED.toString())) {
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

	private void populateAuditDetails(final RequestInfo requestInfo, final AbstractEstimate estimate) {
		for (final AbstractEstimateDetails details : estimate.getAbstractEstimateDetails())
			details.setAuditDetails(estimateUtils.setAuditDetails(requestInfo, true));
		for (final AbstractEstimateAssetDetail assetDetail : estimate.getAssetDetails())
			assetDetail.setAuditDetails(estimateUtils.setAuditDetails(requestInfo, true));
		for (final DocumentDetail documentDetail : estimate.getDocumentDetails())
			documentDetail.setAuditDetails(estimateUtils.setAuditDetails(requestInfo, true));
		estimate.setAuditDetails(estimateUtils.setAuditDetails(requestInfo, true));
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

	public AbstractEstimateResponse search(AbstractEstimateSearchContract abstractEstimateSearchContract,
			RequestInfo requestInfo) {
		List<AbstractEstimate> abstractEstimates = abstractEstimateRepository.search(abstractEstimateSearchContract);
		final AbstractEstimateResponse response = new AbstractEstimateResponse();
		response.setAbstractEstimates(abstractEstimates);
		response.setResponseInfo(estimateUtils.getResponseInfo(requestInfo));
		return response;
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

		if (!messages.isEmpty())
			throw new CustomException(messages);

		projectCode.setName(abstractEstimateDetails.getNameOfWork());
		projectCode.setDescription(abstractEstimateDetails.getNameOfWork());
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
		if (abstractEstimateDetails.getProjectCode() != null
				&& abstractEstimateDetails.getProjectCode().getCode() != null)
			estimateAppropriation.setObjectNumber(abstractEstimateDetails.getProjectCode().getCode());
		estimateAppropriation.setObjectType(CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
		estimateAppropriation.setTenantId(abstractEstimateDetails.getTenantId());
		estimateAppropriationRequest.setEstimateAppropriations(appropriations);
		estimateAppropriationRequest.setRequestInfo(requestInfo);

		final String url = propertiesManager.getWorksSeviceHostName() + propertiesManager.getEstimateAppropriationURL();
		restTemplate.postForObject(url, estimateAppropriationRequest, EstimateAppropriationResponse.class);
	}

}
