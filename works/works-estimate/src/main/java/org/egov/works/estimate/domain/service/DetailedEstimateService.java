package org.egov.works.estimate.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.DetailedEstimateRepository;
import org.egov.works.estimate.domain.validator.EstimateValidator;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.egov.works.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DetailedEstimateService {

	public static final String DETAILED_ESTIMATE_WF_TYPE = "DetailedEstimate";

	public static final String DETAILED_ESTIMATE_BUSINESSKEY = "DetailedEstimate";

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private DetailedEstimateRepository detailedEstimateRepository;

	@Autowired
	private EstimateUtils estimateUtils;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private IdGenerationRepository idGenerationRepository;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private EstimateValidator validator;

	public List<DetailedEstimate> search(DetailedEstimateSearchContract detailedEstimateSearchContract) {
		return detailedEstimateRepository.search(detailedEstimateSearchContract);
	}

	public DetailedEstimateResponse create(DetailedEstimateRequest detailedEstimateRequest, Boolean isRevision) {
		validator.validateDetailedEstimates(detailedEstimateRequest, isRevision);
		AuditDetails auditDetails = estimateUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), false);
		for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
			detailedEstimate.setId(commonUtils.getUUID());
			detailedEstimate.setAuditDetails(auditDetails);
			detailedEstimate.setTotalIncludingRE(detailedEstimate.getEstimateValue());
			AbstractEstimate abstactEstimate = null;
			if (detailedEstimate.getAbstractEstimateDetail() != null || (isRevision != null && isRevision)) {
				abstactEstimate = validator.searchAbstractEstimate(detailedEstimate);
                detailedEstimate.setAbstractEstimateDetail(abstactEstimate.getAbstractEstimateDetails().get(0));
                detailedEstimate.setProjectCode(abstactEstimate.getAbstractEstimateDetails().get(0).getProjectCode());
				if ((abstactEstimate != null && !abstactEstimate.getDetailedEstimateCreated())
						 || (isRevision != null && isRevision)) {
					String estimateNumber = idGenerationRepository.generateDetailedEstimateNumber(
							detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
					detailedEstimate.setEstimateNumber(estimateUtils.getCityCode(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()) + "/" + propertiesManager.getDetailedEstimateNumberPrefix() + '/'
							+ detailedEstimate.getDepartment().getCode() + estimateNumber);
				}
			}

            if(validator.checkAbstractEstimateRequired(detailedEstimate,detailedEstimateRequest.getRequestInfo())) {
                if(abstactEstimate != null) {
                    detailedEstimate.setNameOfWork(abstactEstimate.getNatureOfWork().getCode());
                    detailedEstimate.setWard(abstactEstimate.getWard());
                    detailedEstimate.setLocality(abstactEstimate.getLocality());
                    detailedEstimate.setBillsCreated(abstactEstimate.getBillsCreated());
                    detailedEstimate.setSpillOverFlag(abstactEstimate.getSpillOverFlag());
                    detailedEstimate.setBeneficiary(abstactEstimate.getBeneficiary());
                    detailedEstimate.setFunction(abstactEstimate.getFunction());
                    detailedEstimate.setFund(abstactEstimate.getFund());
                    detailedEstimate.setBudgetGroup(abstactEstimate.getBudgetGroup());
                    detailedEstimate.setDepartment(abstactEstimate.getDepartment());
                    detailedEstimate.setModeOfAllotment(abstactEstimate.getModeOfAllotment());
                    detailedEstimate.setWorksType(abstactEstimate.getTypeOfWork());
                    detailedEstimate.setWorksSubtype(abstactEstimate.getSubTypeOfWork());
                    List<AssetsForEstimate> assetsForEstimates = new ArrayList<>();
                    if(abstactEstimate.getAssetDetails() != null && !abstactEstimate.getAssetDetails().isEmpty()) {
                        AssetsForEstimate assetsForEstimate = null;
                        for(AbstractEstimateAssetDetail assets : abstactEstimate.getAssetDetails()) {
                            assetsForEstimate = new AssetsForEstimate();
                            assetsForEstimate.setLandAsset(assets.getLandAsset());
                            assetsForEstimate.setAsset(assets.getAsset());
                            assetsForEstimate.setAuditDetails(auditDetails);
                            assetsForEstimate.setId(commonUtils.getUUID());
                            assetsForEstimates.add(assetsForEstimate);
                        }
                    }
                    detailedEstimate.setAssets(assetsForEstimates);
                }

            }

			if (isRevision == null || (isRevision != null && !isRevision)) {
				if (detailedEstimate.getMultiYearEstimates() != null) {
					for (final MultiYearEstimate multiYearEstimate : detailedEstimate.getMultiYearEstimates()) {
						multiYearEstimate.setId(commonUtils.getUUID());
						//TODO Set from financials
						multiYearEstimate.setFinancialYear(new FinancialYear());
						multiYearEstimate.setPercentage(100d);
						multiYearEstimate.setAuditDetails(auditDetails);
					}
				}

				for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {
					estimateOverhead.setId(commonUtils.getUUID());
					estimateOverhead.setAuditDetails(auditDetails);
				}

				for (final DetailedEstimateDeduction detailedEstimateDeduction : detailedEstimate
						.getDetailedEstimateDeductions()) {
					detailedEstimateDeduction.setId(commonUtils.getUUID());
					detailedEstimateDeduction.setAuditDetails(auditDetails);
				}

				if (detailedEstimate.getEstimateTechnicalSanctions() != null) {
					for (final EstimateTechnicalSanction estimateTechnicalSanction : detailedEstimate
							.getEstimateTechnicalSanctions()) {
						estimateTechnicalSanction.setId(commonUtils.getUUID());
						estimateTechnicalSanction.setAuditDetails(auditDetails);
					}
				}
				
				if (detailedEstimate.getDocumentDetails() != null) {
					for (DocumentDetail documentDetail : detailedEstimate.getDocumentDetails()) {
						documentDetail.setObjectId(detailedEstimate.getEstimateNumber());
						documentDetail.setObjectType(CommonConstants.DETAILEDESTIMATE);
						documentDetail.setAuditDetails(auditDetails);
					}
				}
			}

			for (final EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
				estimateActivity.setId(commonUtils.getUUID());
				estimateActivity.setAuditDetails(auditDetails);
				if (estimateActivity.getEstimateMeasurementSheets() != null) {
					for (final EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity
							.getEstimateMeasurementSheets()) {
						estimateMeasurementSheet.setId(commonUtils.getUUID());
						estimateMeasurementSheet.setAuditDetails(auditDetails);
                        estimateMeasurementSheet.setEstimateActivity(estimateActivity.getId());
					}
				}
			}

            if(validator.workflowRequired(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()) &&
                 isRevision == null || (isRevision != null && !isRevision)) {
				populateWorkFlowDetails(detailedEstimate, detailedEstimateRequest.getRequestInfo(), abstactEstimate);
				Map<String, String> workFlowResponse = workflowService.enrichWorkflow(detailedEstimate.getWorkFlowDetails(),
						detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
				detailedEstimate.setStateId(workFlowResponse.get("id"));
				detailedEstimate.setStatus(DetailedEstimateStatus.valueOf(workFlowResponse.get("status")));
			} else
				detailedEstimate.setStatus(DetailedEstimateStatus.TECHNICAL_SANCTIONED);
		}
		if (isRevision == null || (isRevision != null && !isRevision))
			kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateCreateAndUpdateTopic(), detailedEstimateRequest);
		else
			kafkaTemplate.send(propertiesManager.getWorksRECreateUpdateTopic(), detailedEstimateRequest);
		final DetailedEstimateResponse response = new DetailedEstimateResponse();
		response.setDetailedEstimates(detailedEstimateRequest.getDetailedEstimates());
		response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
		return response;
	}

	public DetailedEstimateResponse update(DetailedEstimateRequest detailedEstimateRequest, Boolean isRevision) {
		validator.validateDetailedEstimates(detailedEstimateRequest, isRevision);
		AuditDetails updateDetails = estimateUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), true);
		AuditDetails createDetails = estimateUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), false);
		AbstractEstimate abstactEstimate = null;
		for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
			abstactEstimate = validator.searchAbstractEstimate(detailedEstimate);
			populateWorkFlowDetails(detailedEstimate, detailedEstimateRequest.getRequestInfo(), abstactEstimate);
			detailedEstimate.setAuditDetails(updateDetails);

			for (final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets()) {
				if (assetsForEstimate.getId() == null) {
					assetsForEstimate.setId(commonUtils.getUUID());
					assetsForEstimate.setAuditDetails(createDetails);
				}
				assetsForEstimate.setAuditDetails(updateDetails);
			}

			for (final MultiYearEstimate multiYearEstimate : detailedEstimate.getMultiYearEstimates()) {
				if (multiYearEstimate.getId() == null) {
					multiYearEstimate.setId(commonUtils.getUUID());
					multiYearEstimate.setAuditDetails(createDetails);
				}
				multiYearEstimate.setAuditDetails(updateDetails);
			}

			for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {
				if (estimateOverhead.getId() == null) {
					estimateOverhead.setId(commonUtils.getUUID());
					estimateOverhead.setAuditDetails(createDetails);
				}
				estimateOverhead.setAuditDetails(updateDetails);
			}

			for (final DetailedEstimateDeduction detailedEstimateDeduction : detailedEstimate
					.getDetailedEstimateDeductions()) {
				if (detailedEstimateDeduction.getId() == null) {
					detailedEstimateDeduction.setId(commonUtils.getUUID());
					detailedEstimateDeduction.setAuditDetails(createDetails);
				}
				detailedEstimateDeduction.setAuditDetails(updateDetails);
			}

			for (final EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
				if (estimateActivity.getId() == null) {
					estimateActivity.setId(commonUtils.getUUID());
					estimateActivity.setAuditDetails(createDetails);
				}
				estimateActivity.setAuditDetails(updateDetails);
				if (estimateActivity.getEstimateMeasurementSheets() != null) {
					for (final EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity
							.getEstimateMeasurementSheets()) {
						if (estimateMeasurementSheet.getId() == null) {
							estimateMeasurementSheet.setId(commonUtils.getUUID());
							estimateMeasurementSheet.setAuditDetails(createDetails);
						}
						estimateMeasurementSheet.setAuditDetails(updateDetails);
					}
				}
			}
			Map<String, String> workFlowResponse = workflowService.enrichWorkflow(detailedEstimate.getWorkFlowDetails(),
					detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
			detailedEstimate.setStateId(workFlowResponse.get("id"));
			detailedEstimate.setStatus(DetailedEstimateStatus.valueOf(workFlowResponse.get("status")));
		}
		kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateCreateAndUpdateTopic(), detailedEstimateRequest);
		final DetailedEstimateResponse response = new DetailedEstimateResponse();
		response.setDetailedEstimates(detailedEstimateRequest.getDetailedEstimates());
		response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
		return response;
	}

	private void populateWorkFlowDetails(DetailedEstimate detailedEstimate, RequestInfo requestInfo,
			AbstractEstimate abstactEstimate) {

		if (null != detailedEstimate && null != detailedEstimate.getWorkFlowDetails()) {

			WorkFlowDetails workFlowDetails = detailedEstimate.getWorkFlowDetails();
			if (abstactEstimate != null && abstactEstimate.getDetailedEstimateCreated()) {
				workFlowDetails.setType(CommonConstants.SPILLOVER_DETAILED_ESTIMATE_WF_TYPE);
				workFlowDetails.setBusinessKey(CommonConstants.SPILLOVER_DETAILED_ESTIMATE_WF_TYPE);
			} else {
				workFlowDetails.setType(DETAILED_ESTIMATE_WF_TYPE);
				workFlowDetails.setBusinessKey(DETAILED_ESTIMATE_BUSINESSKEY);
			}
			workFlowDetails.setStateId(detailedEstimate.getStateId());
			if (detailedEstimate.getStatus() != null)
				workFlowDetails.setStatus(detailedEstimate.getStatus().toString());

			if (null != requestInfo && null != requestInfo.getUserInfo()) {
				workFlowDetails.setSenderName(requestInfo.getUserInfo().getUserName());
			}

			if (detailedEstimate.getStateId() != null) {
				workFlowDetails.setStateId(detailedEstimate.getStateId());
			}
		}
	}
}
