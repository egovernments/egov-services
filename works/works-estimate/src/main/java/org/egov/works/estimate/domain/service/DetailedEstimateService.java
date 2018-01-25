package org.egov.works.estimate.domain.service;

import java.util.*;

import net.minidev.json.JSONArray;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.DetailedEstimateRepository;
import org.egov.works.estimate.domain.validator.EstimateValidator;
import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.egov.works.workflow.service.WorkflowService;
import org.joda.time.LocalDate;
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
        List<DetailedEstimate> detailedEstimateList = new ArrayList<>();
        for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            detailedEstimate.setId(commonUtils.getUUID());
            detailedEstimate.setAuditDetails(auditDetails);
            detailedEstimate.setTotalIncludingRE(detailedEstimate.getEstimateValue());
            if (detailedEstimate.getSpillOverFlag()) {
                detailedEstimate.setApprovedDate(detailedEstimate.getEstimateDate());
            }
            AbstractEstimate abstactEstimate = null;
            if (detailedEstimate.getAbstractEstimateDetail() != null || (isRevision != null && isRevision)) {
                abstactEstimate = validator.searchAbstractEstimate(detailedEstimate);
                detailedEstimate.setAbstractEstimateDetail(abstactEstimate.getAbstractEstimateDetails().get(0));
                detailedEstimate.setProjectCode(abstactEstimate.getAbstractEstimateDetails().get(0).getProjectCode());
                if ((abstactEstimate != null && !abstactEstimate.getDetailedEstimateCreated())
                        || (isRevision != null && isRevision)) {
                    String estimateNumber = idGenerationRepository.generateDetailedEstimateNumber(
                            detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
                    detailedEstimate.setEstimateNumber(
                            estimateUtils.getCityCode(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo())
                                    + "/" + propertiesManager.getDetailedEstimateNumberPrefix() + '/'
                                    + detailedEstimate.getDepartment().getCode() + estimateNumber);
                }
            }

            if (validator.checkAbstractEstimateRequired(detailedEstimate, detailedEstimateRequest.getRequestInfo())) {
                if (abstactEstimate != null) {
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
                    if (abstactEstimate.getAssetDetails() != null && !abstactEstimate.getAssetDetails().isEmpty()) {
                        AssetsForEstimate assetsForEstimate = null;
                        for (AbstractEstimateAssetDetail assets : abstactEstimate.getAssetDetails()) {
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
                MultiYearEstimate multiYearEstimate = new MultiYearEstimate();
                multiYearEstimate.setId(commonUtils.getUUID());
                multiYearEstimate.setFinancialYear(
                        getCurrentFinancialYear(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()));
                multiYearEstimate.setTenantId(detailedEstimate.getTenantId());
                multiYearEstimate.setPercentage(100d);
                multiYearEstimate.setAuditDetails(auditDetails);
                detailedEstimate.setMultiYearEstimates(Arrays.asList(multiYearEstimate));

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
                        estimateTechnicalSanction.setDetailedEstimate(detailedEstimate.getId());
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
                if (estimateActivity.getNonSor() != null) {
                    estimateActivity.getNonSor().setId(commonUtils.getUUID());
                }
            }

            List<String> filetsNamesList = null;
            List<String> filetsValuesList = null;
            if (validator.workflowRequired(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()) &&
                    (isRevision == null || (isRevision != null && !isRevision))) {
                populateWorkFlowDetails(detailedEstimate, detailedEstimateRequest.getRequestInfo(), abstactEstimate);
                Map<String, String> workFlowResponse = workflowService.enrichWorkflow(detailedEstimate.getWorkFlowDetails(),
                        detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
                detailedEstimate.setStateId(workFlowResponse.get("id"));
                WorksStatus status = new WorksStatus();
                status.code(workFlowResponse.get("status"));
                detailedEstimate.setStatus(status);
            } else if (detailedEstimate.getSpillOverFlag()) {
                filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE, CommonConstants.MODULE_TYPE));
                filetsValuesList = new ArrayList<>(
                        Arrays.asList(Constants.DETAILEDESTIMATE_STATUS_TECH_SANCTIONED, CommonConstants.DETAILEDESTIMATE));
                JSONArray dBStatusArray = estimateUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                        filetsValuesList, detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo(),
                        CommonConstants.MODULENAME_WORKS);
                if (dBStatusArray != null && !dBStatusArray.isEmpty()) {
                    WorksStatus status = new WorksStatus();
                    status.code(Constants.DETAILEDESTIMATE_STATUS_TECH_SANCTIONED);
                    detailedEstimate.setStatus(status);
                }
            } else {
                filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE, CommonConstants.MODULE_TYPE));
                filetsValuesList = new ArrayList<>(
                        Arrays.asList(Constants.ESTIMATE_STATUS_CREATED, CommonConstants.DETAILEDESTIMATE));
                JSONArray dBStatusArray = estimateUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                        filetsValuesList, detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo(),
                        CommonConstants.MODULENAME_WORKS);
                if (dBStatusArray != null && !dBStatusArray.isEmpty()) {
                    WorksStatus status = new WorksStatus();
                    status.code(Constants.ESTIMATE_STATUS_CREATED);
                    detailedEstimate.setStatus(status);
                }
            }
            DetailedEstimate de = null;
            if(detailedEstimate.getStatus().getCode().equalsIgnoreCase(Constants.ESTIMATE_STATUS_NEW) ||
                    detailedEstimate.getStatus().getCode().equalsIgnoreCase(Constants.DETAILEDESTIMATE_STATUS_TECH_SANCTIONED)) {
                de = detailedEstimate;
                de.setBackUpdateAE(true);
                detailedEstimateList.add(de);
            }

            if(detailedEstimate.getStatus().getCode().equalsIgnoreCase(Constants.ESTIMATE_STATUS_CANCELLED)) {
                de = detailedEstimate;
                de.setBackUpdateAE(false);
                detailedEstimateList.add(de);
            }
        }

        if (isRevision == null || (isRevision != null && !isRevision))
            kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateCreateAndUpdateTopic(), detailedEstimateRequest);
        else
            kafkaTemplate.send(propertiesManager.getWorksRECreateUpdateTopic(), detailedEstimateRequest);

        if(detailedEstimateList != null && !detailedEstimateList.isEmpty()) {
            DetailedEstimateRequest backUpdateRequest = new DetailedEstimateRequest();
            backUpdateRequest.setDetailedEstimates(detailedEstimateList);
            kafkaTemplate.send(propertiesManager.getWorksAbstractEstimateBackupdateTopic(), backUpdateRequest);
        }
        final DetailedEstimateResponse response = new DetailedEstimateResponse();
        response.setDetailedEstimates(detailedEstimateRequest.getDetailedEstimates());
        response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
        return response;
    }

    private FinancialYear getCurrentFinancialYear(final String tenantId, final RequestInfo requestInfo) {
        JSONArray jsonArray = estimateUtils.getMDMSData(CommonConstants.FINANCIALYEAR_OBJECT, "tenantId", tenantId,
                tenantId, requestInfo, Constants.EGF_MODULE_CODE);
        FinancialYear financialYear = null;
        if (jsonArray != null && !jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                Map<String, Object> jsonMap = (Map<String, Object>) jsonArray.get(i);
                long fromDate = (long) jsonMap.get("startingDate");
                long toDate = (long) jsonMap.get("endingDate");
                long currentDateTime = new Date().getTime();
                if (fromDate <= currentDateTime && toDate >= currentDateTime) {
                    financialYear = new FinancialYear();
                    financialYear.setId((String) jsonMap.get("id"));
                    financialYear.setFinYearRange((String) jsonMap.get("finYearRange"));
                    break;
                }
            }
        }
        return financialYear;
    }

    public DetailedEstimateResponse update(DetailedEstimateRequest detailedEstimateRequest, Boolean isRevision) {
        validator.validateDetailedEstimates(detailedEstimateRequest, isRevision);
        AuditDetails updateDetails = estimateUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), true);
        AuditDetails createDetails = estimateUtils.setAuditDetails(detailedEstimateRequest.getRequestInfo(), false);
        AbstractEstimate abstactEstimate = null;
        for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            abstactEstimate = validator.searchAbstractEstimate(detailedEstimate);

            if (detailedEstimate.getSpillOverFlag()) {
                detailedEstimate.setApprovedDate(detailedEstimate.getEstimateDate());
            }
            detailedEstimate.setAuditDetails(updateDetails);

            if (detailedEstimate.getAssets() != null) {
                for (final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets()) {
                    if (assetsForEstimate.getId() == null) {
                        assetsForEstimate.setId(commonUtils.getUUID());
                        assetsForEstimate.setAuditDetails(createDetails);
                    }
                    assetsForEstimate.setAuditDetails(updateDetails);
                }
            }

            if (detailedEstimate.getEstimateOverheads() != null) {
                for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {
                    if (estimateOverhead.getId() == null) {
                        estimateOverhead.setId(commonUtils.getUUID());
                        estimateOverhead.setAuditDetails(createDetails);
                    }
                    estimateOverhead.setAuditDetails(updateDetails);
                }
            }

            if (detailedEstimate.getDetailedEstimateDeductions() != null) {
                for (final DetailedEstimateDeduction detailedEstimateDeduction : detailedEstimate
                        .getDetailedEstimateDeductions()) {
                    if (detailedEstimateDeduction.getId() == null) {
                        detailedEstimateDeduction.setId(commonUtils.getUUID());
                        detailedEstimateDeduction.setAuditDetails(createDetails);
                    }
                    detailedEstimateDeduction.setAuditDetails(updateDetails);
                }
            }

            if (detailedEstimate.getEstimateActivities() != null) {
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
            }

            if (validator.workflowRequired(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo())) {
                populateWorkFlowDetails(detailedEstimate, detailedEstimateRequest.getRequestInfo(), abstactEstimate);
                Map<String, String> workFlowResponse = workflowService.enrichWorkflow(detailedEstimate.getWorkFlowDetails(),
                        detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
                detailedEstimate.setStateId(workFlowResponse.get("id"));
                WorksStatus status = new WorksStatus();
                status.setCode(workFlowResponse.get("status"));
                detailedEstimate.setStatus(status);
            }

            if (!detailedEstimate.getSpillOverFlag() && detailedEstimate.getStatus().getCode()
                    .equalsIgnoreCase(DetailedEstimateStatus.TECHNICAL_SANCTIONED.toString())) {
                detailedEstimate.setApprovedDate(new Date().getTime());

                setTechnicalSanctionDetails(detailedEstimateRequest, createDetails, detailedEstimate);
            }

        }
        kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateCreateAndUpdateTopic(), detailedEstimateRequest);
        final DetailedEstimateResponse response = new DetailedEstimateResponse();
        response.setDetailedEstimates(detailedEstimateRequest.getDetailedEstimates());
        response.setResponseInfo(estimateUtils.getResponseInfo(detailedEstimateRequest.getRequestInfo()));
        return response;
    }

    private void setTechnicalSanctionDetails(DetailedEstimateRequest detailedEstimateRequest, AuditDetails createDetails,
            final DetailedEstimate detailedEstimate) {
        List<EstimateTechnicalSanction> estimateTechnicalSanctions = new ArrayList<>();
        EstimateTechnicalSanction estimateTechnicalSanction = new EstimateTechnicalSanction();
        estimateTechnicalSanction.setId(commonUtils.getUUID());
        estimateTechnicalSanction.setTenantId(detailedEstimate.getTenantId());
        estimateTechnicalSanction.setDetailedEstimate(detailedEstimate.getId());
        estimateTechnicalSanction.setAuditDetails(createDetails);
        estimateTechnicalSanction.setTechnicalSanctionDate(new Date().getTime());
        User user = new User();
        user.setUserName(detailedEstimateRequest.getRequestInfo().getUserInfo().getUserName());
        estimateTechnicalSanction.setTechnicalSanctionBy(user);

        StringBuilder technicalSanctionNumber = new StringBuilder();
        String tsn = idGenerationRepository.generateTechnicalSanctionNumber(
                detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
        technicalSanctionNumber
                .append(estimateUtils.getCityCode(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()))
                .append("/")
                .append(propertiesManager.getTechnicalSanctionNumberPrefix())
                .append("/").append(detailedEstimate.getDepartment().getCode())
                .append("/").append(tsn);
        estimateTechnicalSanction.setTechnicalSanctionNumber(technicalSanctionNumber.toString());
        estimateTechnicalSanctions.add(estimateTechnicalSanction);
        detailedEstimate.setEstimateTechnicalSanctions(estimateTechnicalSanctions);
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
                workFlowDetails.setStatus(detailedEstimate.getStatus().getCode());

            if (null != requestInfo && null != requestInfo.getUserInfo()) {
                workFlowDetails.setSenderName(requestInfo.getUserInfo().getUserName());
            }

            if (detailedEstimate.getStateId() != null) {
                workFlowDetails.setStateId(detailedEstimate.getStateId());
            }
        }
    }
}
