package org.egov.works.workorder.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.config.PropertiesManager;
import org.egov.works.workorder.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.workorder.domain.repository.builder.IdGenerationRepository;
import org.egov.works.workorder.domain.validator.LetterOfAcceptanceValidator;
import org.egov.works.workorder.utils.WorkOrderUtils;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.minidev.json.JSONArray;

/**
 * Created by ramki on 11/11/17.
 */
@Service
@Transactional(readOnly = true)
public class LetterOfAcceptanceService {

    @Autowired
    private WorkOrderUtils workOrderUtils;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private IdGenerationRepository idGenerationRepository;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    @Autowired
    LetterOfAcceptanceValidator letterOfAcceptanceValidator;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    public LetterOfAcceptanceResponse create(final LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            final Boolean isRevision) {
        letterOfAcceptanceValidator.validateLetterOfAcceptance(letterOfAcceptanceRequest, Boolean.FALSE, isRevision);
        List<LetterOfAcceptanceEstimate> letterOfAcceptanceEstimates = new ArrayList<>();
        List<LetterOfAcceptance> letterOfAcceptanceList = new ArrayList<>();
        for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptanceRequest.getLetterOfAcceptances()) {

            letterOfAcceptance.setId(commonUtils.getUUID());
            Boolean isSpilloverWFReq = false;
            if (letterOfAcceptance.getSpillOverFlag())
                isSpilloverWFReq = isConfigRequired(CommonConstants.SPILLOVER_WORKFLOW_MANDATORY,
                        letterOfAcceptanceRequest.getRequestInfo(), letterOfAcceptance.getTenantId());
            if (!isSpilloverWFReq && letterOfAcceptance.getSpillOverFlag()) {
                List<String> filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE, CommonConstants.MODULE_TYPE));
                List<String> filetsValuesList = new ArrayList<>(Arrays.asList(Constants.STATUS_APPROVED,Constants.LETTEROFACCEPTANCE_OBJECT));
                JSONArray dBStatusArray = workOrderUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                        filetsValuesList, letterOfAcceptance.getTenantId(), letterOfAcceptanceRequest.getRequestInfo(),
                        CommonConstants.MODULENAME_WORKS);
                if(dBStatusArray != null && !dBStatusArray.isEmpty()) {
                    WorksStatus status = new WorksStatus();
                    status.code(Constants.STATUS_APPROVED);
                    letterOfAcceptance.setStatus(status);
                }
            }
            
            if(letterOfAcceptance.getSpillOverFlag()) {
                letterOfAcceptance.setApprovedDate(letterOfAcceptance.getLoaDate());
            }

            for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance
                    .getLetterOfAcceptanceEstimates()) {
                letterOfAcceptanceEstimate.setId(commonUtils.getUUID());
                letterOfAcceptanceEstimate.setAuditDetails(
                        workOrderUtils.setAuditDetails(letterOfAcceptanceRequest.getRequestInfo(), false));
                DetailedEstimate detailedEstimate = estimateService
                        .getDetailedEstimate(letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber(),
                                letterOfAcceptanceEstimate.getTenantId(), letterOfAcceptanceRequest.getRequestInfo())
                        .getDetailedEstimates().get(0);

                List<LOAActivity> loaActivities = new ArrayList<>();

                for (EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
                    prepareLOAActivity(letterOfAcceptance, letterOfAcceptanceEstimate, loaActivities, estimateActivity,
                            letterOfAcceptanceRequest.getRequestInfo(), false);
                }
                letterOfAcceptanceEstimate.setLetterOfAcceptance(letterOfAcceptance.getId());
                letterOfAcceptanceEstimate.setDetailedEstimate(detailedEstimate);
                letterOfAcceptanceEstimate.setLoaActivities(loaActivities);
                
                
                letterOfAcceptanceValidator.validateLOAAmount(letterOfAcceptance, detailedEstimate);

                if (!detailedEstimate.getWorkOrderCreated()) {
                    String loaNumber = idGenerationRepository.generateLOANumber(letterOfAcceptance.getTenantId(),
                            letterOfAcceptanceRequest.getRequestInfo());
                    // TODO: check idgen to accept values to generate
                    letterOfAcceptance.setLoaNumber(workOrderUtils.getCityCode(letterOfAcceptance.getTenantId(),
                            letterOfAcceptanceRequest.getRequestInfo()) + "/" + propertiesManager.getLoaNumberPrefix() + "/"
                            + detailedEstimate.getDepartment().getCode() + loaNumber);
                    letterOfAcceptance.setLoaDate(new Date().getTime());
                    
                }

            }

            LetterOfAcceptance loa = null;
            if(letterOfAcceptance.getStatus().getCode().equalsIgnoreCase(Constants.STATUS_APPROVED)) {
                loa = letterOfAcceptance;
                letterOfAcceptanceList.add(loa);
            }

            if ((isRevision != null && !isRevision) && letterOfAcceptance.getSecurityDeposits() != null)
                for (SecurityDeposit securityDeposit : letterOfAcceptance.getSecurityDeposits()) {

                    securityDeposit.setId(commonUtils.getUUID());
                    securityDeposit.setTenantId(letterOfAcceptance.getTenantId());
                    securityDeposit.setLetterOfAcceptance(letterOfAcceptance.getId());
                    securityDeposit.setAuditDetails(
                            workOrderUtils.setAuditDetails(letterOfAcceptanceRequest.getRequestInfo(), false));
                }

            if (letterOfAcceptance.getDocumentDetails() != null)
                for (final DocumentDetail documentDetail : letterOfAcceptance.getDocumentDetails()) {
                    documentDetail.setId(commonUtils.getUUID());
                    documentDetail.setObjectId(letterOfAcceptance.getLoaNumber());
                    documentDetail.setObjectType(CommonConstants.LETTEROFACCEPTANCE);
                    documentDetail.setAuditDetails(
                            workOrderUtils.setAuditDetails(letterOfAcceptanceRequest.getRequestInfo(), false));
                }

            letterOfAcceptance
                    .setAuditDetails(workOrderUtils.setAuditDetails(letterOfAcceptanceRequest.getRequestInfo(), false));
            
            
            if(letterOfAcceptance.getStatus() != null && letterOfAcceptance.getStatus().getCode().equalsIgnoreCase(Constants.STATUS_APPROVED)) {
                letterOfAcceptance.setApprovedDate(new Date().getTime());
                User approvedBy = new User();
                approvedBy.setUserName(letterOfAcceptanceRequest.getRequestInfo().getUserInfo().getUserName());
                letterOfAcceptance.setApprovedBy(approvedBy);
            }

        }

        if (isRevision == null || (isRevision != null && !isRevision))
            kafkaTemplate.send(propertiesManager.getWorksLOACreateTopic(), letterOfAcceptanceRequest);
        else
            kafkaTemplate.send(propertiesManager.getWorksRevisionLOACreateUpdateTopic(), letterOfAcceptanceRequest);

        if(letterOfAcceptanceList != null && !letterOfAcceptanceList.isEmpty()) {
            LetterOfAcceptanceRequest backUpdateRequest = new LetterOfAcceptanceRequest();
            backUpdateRequest.setLetterOfAcceptances(letterOfAcceptanceList);
            kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateBackupdateOnCreateLoaTopic(), backUpdateRequest);
        }

        LetterOfAcceptanceResponse letterOfAcceptanceResponse = new LetterOfAcceptanceResponse();
        letterOfAcceptanceResponse.setLetterOfAcceptances(letterOfAcceptanceRequest.getLetterOfAcceptances());
        letterOfAcceptanceResponse
                .setResponseInfo(workOrderUtils.getResponseInfo(letterOfAcceptanceRequest.getRequestInfo()));
        return letterOfAcceptanceResponse;
    }

    private void prepareLOAActivity(LetterOfAcceptance letterOfAcceptance,
            LetterOfAcceptanceEstimate letterOfAcceptanceEstimate, List<LOAActivity> loaActivities,
            EstimateActivity estimateActivity, final RequestInfo requestInfo, final Boolean isUpdate) {
        LOAActivity activity = new LOAActivity();
        activity.setEstimateActivity(estimateActivity);
        activity.setApprovedRate(estimateActivity.getEstimateRate());
        activity.setApprovedQuantity(new BigDecimal(estimateActivity.getQuantity()));
        activity.setApprovedAmount(
                BigDecimal.valueOf(estimateActivity.getUnitRate().doubleValue() * estimateActivity.getQuantity()));
        if (activity.getId() == null || activity.getId().isEmpty())
            activity.setId(commonUtils.getUUID());
        activity.setTenantId(letterOfAcceptanceEstimate.getTenantId());
        activity.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimate.getId());
        activity.setAuditDetails(workOrderUtils.setAuditDetails(requestInfo, isUpdate));
        activity.setParent(estimateActivity.getParent());
        createLOAMSheet(activity, estimateActivity, requestInfo, isUpdate);
        loaActivities.add(activity);
    }

    public LetterOfAcceptanceResponse search(final LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria,
            final RequestInfo requestInfo) {
        LetterOfAcceptanceResponse letterOfAcceptanceResponse = new LetterOfAcceptanceResponse();
        letterOfAcceptanceResponse.setLetterOfAcceptances(
                letterOfAcceptanceRepository.searchLOAs(letterOfAcceptanceSearchCriteria, requestInfo));
        return letterOfAcceptanceResponse;
    }

    private void createLOAMSheet(final LOAActivity loaActivity, final EstimateActivity estimateActivity,
            final RequestInfo requestInfo, final Boolean isUpdate) {
        final List<LOAMeasurementSheet> loaSheetList = new ArrayList<LOAMeasurementSheet>();
        LOAMeasurementSheet loaSheet = null;
        for (final EstimateMeasurementSheet estimatesheet : estimateActivity.getEstimateMeasurementSheets()) {
            loaSheet = new LOAMeasurementSheet();
            if (loaSheet.getId() == null || loaSheet.getId().isEmpty())
                loaSheet.setId(commonUtils.getUUID());
            loaSheet.setNumber(estimatesheet.getNumber());
            loaSheet.setMultiplier(estimatesheet.getMultiplier());
            loaSheet.setLength(estimatesheet.getLength());
            loaSheet.setWidth(estimatesheet.getWidth());
            loaSheet.setDepthOrHeight(estimatesheet.getDepthOrHeight());
            loaSheet.setLoaActivity(loaActivity.getId());
            loaSheet.setQuantity(estimatesheet.getQuantity());
            loaSheet.setEstimateMeasurementSheet(estimatesheet.getId());
            loaSheet.setAuditDetails(workOrderUtils.setAuditDetails(requestInfo, isUpdate));
            loaSheet.setTenantId(loaActivity.getTenantId());
            loaSheetList.add(loaSheet);

        }
        loaActivity.setLoaMeasurements(loaSheetList);
    }

    public LetterOfAcceptanceResponse update(final LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            final Boolean isRevision) {
        letterOfAcceptanceValidator.validateLetterOfAcceptance(letterOfAcceptanceRequest, Boolean.TRUE, isRevision);
        List<LetterOfAcceptanceEstimate> letterOfAcceptanceEstimates = new ArrayList<>();
        List<LetterOfAcceptance> letterOfAcceptanceList = new ArrayList<>();
        for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptanceRequest.getLetterOfAcceptances()) {

            if (letterOfAcceptance.getId() == null || letterOfAcceptance.getId().isEmpty())
                letterOfAcceptance.setId(commonUtils.getUUID());

            for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance
                    .getLetterOfAcceptanceEstimates()) {
                if (letterOfAcceptanceEstimate.getId() == null || letterOfAcceptanceEstimate.getId().isEmpty())
                    letterOfAcceptanceEstimate.setId(commonUtils.getUUID());
                letterOfAcceptanceEstimate.setAuditDetails(
                        workOrderUtils.setAuditDetails(letterOfAcceptanceRequest.getRequestInfo(), true));
                DetailedEstimate detailedEstimate = estimateService
                        .getDetailedEstimate(letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber(),
                                letterOfAcceptanceEstimate.getTenantId(), letterOfAcceptanceRequest.getRequestInfo())
                        .getDetailedEstimates().get(0);

                List<LOAActivity> loaActivities = new ArrayList<>();

                for (EstimateActivity estimateActivity : detailedEstimate.getEstimateActivities()) {
                    prepareLOAActivity(letterOfAcceptance, letterOfAcceptanceEstimate, loaActivities, estimateActivity,
                            letterOfAcceptanceRequest.getRequestInfo(), true);
                }
                letterOfAcceptanceEstimate.setLetterOfAcceptance(letterOfAcceptance.getId());
                letterOfAcceptanceEstimate.setDetailedEstimate(detailedEstimate);
                letterOfAcceptanceEstimate.setLoaActivities(loaActivities);
            }

            if(letterOfAcceptance.getSecurityDeposits() != null)
            for (SecurityDeposit securityDeposit : letterOfAcceptance.getSecurityDeposits()) {

                if (securityDeposit.getId() == null || securityDeposit.getId().isEmpty())
                    securityDeposit.setId(commonUtils.getUUID());
                securityDeposit.setTenantId(letterOfAcceptance.getTenantId());
                securityDeposit.setLetterOfAcceptance(letterOfAcceptance.getId());
                securityDeposit.setAuditDetails(
                        workOrderUtils.setAuditDetails(letterOfAcceptanceRequest.getRequestInfo(), true));
            }

            if (letterOfAcceptance.getDocumentDetails() != null && !letterOfAcceptance.getDocumentDetails().isEmpty())
                for (final DocumentDetail documentDetail : letterOfAcceptance.getDocumentDetails()) {
                    if (documentDetail.getId() == null || documentDetail.getId().isEmpty())
                        documentDetail.setId(commonUtils.getUUID());
                    documentDetail.setObjectId(letterOfAcceptance.getLoaNumber());
                    documentDetail.setObjectType(CommonConstants.LETTEROFACCEPTANCE);
                    documentDetail.setAuditDetails(
                            workOrderUtils.setAuditDetails(letterOfAcceptanceRequest.getRequestInfo(), true));
                }

            letterOfAcceptance
                    .setAuditDetails(workOrderUtils.setAuditDetails(letterOfAcceptanceRequest.getRequestInfo(), true));
            
            if(letterOfAcceptance.getStatus() != null && letterOfAcceptance.getStatus().getCode().equalsIgnoreCase(Constants.STATUS_APPROVED)) {
                letterOfAcceptance.setApprovedDate(new Date().getTime());
                User approvedBy = new User();
                approvedBy.setUserName(letterOfAcceptanceRequest.getRequestInfo().getUserInfo().getUserName());
                letterOfAcceptance.setApprovedBy(approvedBy);
            }

            LetterOfAcceptance loa = null;
            if(letterOfAcceptance.getStatus().getCode().equalsIgnoreCase(Constants.STATUS_CANCELLED)) {
                loa = letterOfAcceptance;
                letterOfAcceptanceList.add(loa);
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksLOACreateTopic(), letterOfAcceptanceRequest);
        if(letterOfAcceptanceList != null && !letterOfAcceptanceList.isEmpty()) {
            LetterOfAcceptanceRequest backUpdateRequest = new LetterOfAcceptanceRequest();
            backUpdateRequest.setLetterOfAcceptances(letterOfAcceptanceList);
            kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateBackupdateOnCancelLoaTopic(), backUpdateRequest);
        }
        LetterOfAcceptanceResponse letterOfAcceptanceResponse = new LetterOfAcceptanceResponse();
        letterOfAcceptanceResponse.setLetterOfAcceptances(letterOfAcceptanceRequest.getLetterOfAcceptances());
        letterOfAcceptanceResponse
                .setResponseInfo(workOrderUtils.getResponseInfo(letterOfAcceptanceRequest.getRequestInfo()));
        return letterOfAcceptanceResponse;
    }
    
    private Boolean isConfigRequired(String keyName, RequestInfo requestInfo, final String tenantId) {
        Boolean isSpilloverWFReq = false;
        JSONArray responseJSONArray = workOrderUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                keyName, tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                isSpilloverWFReq = true;
        }
        return isSpilloverWFReq;
    }

}
