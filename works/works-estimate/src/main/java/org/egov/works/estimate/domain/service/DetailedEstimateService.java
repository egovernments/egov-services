package org.egov.works.estimate.domain.service;

import java.util.Date;
import java.util.List;

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
@Transactional(readOnly= true)
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

    public DetailedEstimateResponse create(DetailedEstimateRequest detailedEstimateRequest) {
        validator.validateDetailedEstimates(detailedEstimateRequest);
        AuditDetails auditDetails = setAuditDetails(detailedEstimateRequest.getRequestInfo().getUserInfo().getUserName(), false);
        for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            detailedEstimate.setId(commonUtils.getUUID());
            detailedEstimate.setAuditDetails(auditDetails);
            detailedEstimate.setTotalIncludingRE(detailedEstimate.getWorkValue());

            if(detailedEstimate.getAbstractEstimateDetail() != null) {
                        if(!validator.checkDetailedEstimateCreatedFlag(detailedEstimate)) {
                            String estimateNumber = idGenerationRepository
                                    .generateDetailedEstimateNumber(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
                            detailedEstimate.setEstimateNumber(propertiesManager.getDetailedEstimateNumberPrefix() + estimateNumber);
                        }
                }

            for(final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets()) {
                assetsForEstimate.setId(commonUtils.getUUID());
                assetsForEstimate.setAuditDetails(auditDetails);
            }

            if(detailedEstimate.getMultiYearEstimates() != null) {
                for (final MultiYearEstimate multiYearEstimate : detailedEstimate.getMultiYearEstimates()) {
                    multiYearEstimate.setId(commonUtils.getUUID());
                    //Set from financials
                    multiYearEstimate.setFinancialYear(new FinancialYear());
                    multiYearEstimate.setPercentage(100d);
                    multiYearEstimate.setAuditDetails(auditDetails);
                }
            }

            for(final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {
                estimateOverhead.setId(commonUtils.getUUID());
                estimateOverhead.setAuditDetails(auditDetails);
            }

            for(final DetailedEstimateDeduction detailedEstimateDeduction : detailedEstimate.getDetailedEstimateDeductions()) {
                detailedEstimateDeduction.setId(commonUtils.getUUID());
                detailedEstimateDeduction.setAuditDetails(auditDetails);
            }

            if(detailedEstimate.getEstimateTechnicalSanctions() != null) {
                for(final EstimateTechnicalSanction estimateTechnicalSanction : detailedEstimate.getEstimateTechnicalSanctions()) {
                    estimateTechnicalSanction.setId(commonUtils.getUUID());
                    estimateTechnicalSanction.setAuditDetails(auditDetails);
                }
            }

            for(final EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
                estimateActivity.setId(commonUtils.getUUID());
                estimateActivity.setAuditDetails(auditDetails);
                if(estimateActivity.getEstimateMeasurementSheets() != null) {
                    for (final EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity.getEstimateMeasurementSheets()) {
                        estimateMeasurementSheet.setId(commonUtils.getUUID());
                        estimateMeasurementSheet.setAuditDetails(auditDetails);
                    }
                }
            }
				populateWorkFlowDetails(detailedEstimate, detailedEstimateRequest.getRequestInfo());
				detailedEstimate.setStateId(workflowService.enrichWorkflow(detailedEstimate.getWorkFlowDetails(),
                        detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()));
				detailedEstimate.setStatus(DetailedEstimateStatus.CREATED);
        }
        kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateCreateTopic(), detailedEstimateRequest);
        final DetailedEstimateResponse response = new DetailedEstimateResponse();
        response.setDetailedEstimates(detailedEstimateRequest.getDetailedEstimates());
        response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
        return response;
    }
    
    public DetailedEstimateResponse update(DetailedEstimateRequest detailedEstimateRequest) {
        AuditDetails updateDetails = setAuditDetails(detailedEstimateRequest.getRequestInfo().getUserInfo().getUserName(), true);
        AuditDetails createDetails = setAuditDetails(detailedEstimateRequest.getRequestInfo().getUserInfo().getUserName(), false);
        for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
        	populateWorkFlowDetails(detailedEstimate, detailedEstimateRequest.getRequestInfo());
            detailedEstimate.setAuditDetails(updateDetails);

            for(final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets()) {
            	if (assetsForEstimate.getId() == null){
            		assetsForEstimate.setId(commonUtils.getUUID());
            		assetsForEstimate.setAuditDetails(createDetails);
            	}
                assetsForEstimate.setAuditDetails(updateDetails);
            }

            for(final MultiYearEstimate multiYearEstimate : detailedEstimate.getMultiYearEstimates()) {
            	if (multiYearEstimate.getId() == null) {
            		multiYearEstimate.setId(commonUtils.getUUID());
            		multiYearEstimate.setAuditDetails(createDetails);
            	}
                multiYearEstimate.setAuditDetails(updateDetails);
            }

            for(final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {
            	if (estimateOverhead.getId() == null) {
            		estimateOverhead.setId(commonUtils.getUUID());
            		estimateOverhead.setAuditDetails(createDetails);
            	}
                estimateOverhead.setAuditDetails(updateDetails);
            }

            for(final DetailedEstimateDeduction detailedEstimateDeduction : detailedEstimate.getDetailedEstimateDeductions()) {
            	if (detailedEstimateDeduction.getId() == null) {
            		detailedEstimateDeduction.setId(commonUtils.getUUID());
                    detailedEstimateDeduction.setAuditDetails(createDetails);
            	}
                detailedEstimateDeduction.setAuditDetails(updateDetails);
            }

            for(final EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
            	if (estimateActivity.getId() == null) {
            		estimateActivity.setId(commonUtils.getUUID());
                    estimateActivity.setAuditDetails(createDetails);
            	}
                estimateActivity.setAuditDetails(updateDetails);
                if(estimateActivity.getEstimateMeasurementSheets() != null) {
                    for (final EstimateMeasurementSheet estimateMeasurementSheet : estimateActivity.getEstimateMeasurementSheets()) {
                    	if (estimateMeasurementSheet.getId() == null) {
                    		estimateMeasurementSheet.setId(commonUtils.getUUID());
                            estimateMeasurementSheet.setAuditDetails(createDetails);
                    	}
                        estimateMeasurementSheet.setAuditDetails(updateDetails);
                    }
                }
            }
            detailedEstimate.setStateId(workflowService.enrichWorkflow(detailedEstimate.getWorkFlowDetails(), detailedEstimate.getTenantId(),
            		detailedEstimateRequest.getRequestInfo()));

			populateNextStatus(detailedEstimate);
        }
        kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateUpdateTopic(), detailedEstimateRequest);
        final DetailedEstimateResponse response = new DetailedEstimateResponse();
        response.setDetailedEstimates(detailedEstimateRequest.getDetailedEstimates());
        response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
        return response;
    }

    public AuditDetails setAuditDetails(final String userName, final Boolean isUpdate) {
        AuditDetails auditDetails = new AuditDetails();
        if (isUpdate) {
            auditDetails.setLastModifiedBy(userName);
            auditDetails.setLastModifiedTime(new Date().getTime());
        } else {
            auditDetails.setCreatedBy(userName);
            auditDetails.setCreatedTime(new Date().getTime());
            auditDetails.setLastModifiedBy(userName);
            auditDetails.setLastModifiedTime(new Date().getTime());
        }

        return auditDetails;
    }


    
    private void populateWorkFlowDetails(DetailedEstimate detailedEstimate, RequestInfo requestInfo) {

		if (null != detailedEstimate && null != detailedEstimate.getWorkFlowDetails()) {

			WorkFlowDetails workFlowDetails = detailedEstimate.getWorkFlowDetails();

            if (validator.checkDetailedEstimateCreatedFlag(detailedEstimate)) {
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
	
	private void populateNextStatus(DetailedEstimate detailedEstimate) {
		WorkFlowDetails workFlowDetails = null;
		String currentStatus = null;

		if (null != detailedEstimate && null != detailedEstimate.getStatus()) {
			workFlowDetails = detailedEstimate.getWorkFlowDetails();
			currentStatus = detailedEstimate.getStatus().toString();
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Submit")
				&& (currentStatus.equalsIgnoreCase(DetailedEstimateStatus.CREATED.toString())
				|| currentStatus.equalsIgnoreCase(DetailedEstimateStatus.RESUBMITTED.toString()))) {
			detailedEstimate.setStatus(DetailedEstimateStatus.CHECKED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Approve")
				&& currentStatus.equalsIgnoreCase(DetailedEstimateStatus.CHECKED.toString())) {
			detailedEstimate.setStatus(DetailedEstimateStatus.APPROVED);
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Reject")) {
			detailedEstimate.setStatus(DetailedEstimateStatus.REJECTED);
		}
		
		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Forward")
				&& currentStatus.equalsIgnoreCase(DetailedEstimateStatus.REJECTED.toString())) {
			detailedEstimate.setStatus(DetailedEstimateStatus.RESUBMITTED);
		}
		
		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& workFlowDetails.getAction().equalsIgnoreCase("Cancel")
				&& currentStatus.equalsIgnoreCase(DetailedEstimateStatus.REJECTED.toString())) {
			detailedEstimate.setStatus(DetailedEstimateStatus.CANCELLED);
		}
	}
}
	

