package org.egov.works.estimate.domain.service;

import com.google.gson.JsonObject;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.exception.InvalidDataException;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.config.WorksEstimateServiceConstants;
import org.egov.works.estimate.domain.repository.DetailedEstimateRepository;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.works.estimate.config.WorksEstimateServiceConstants.*;

@Service
@Transactional(readOnly= true)
public class DetailedEstimateService {

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

    public List<DetailedEstimate> search(DetailedEstimateSearchContract detailedEstimateSearchContract) {
        return detailedEstimateRepository.search(detailedEstimateSearchContract);
    }

    public List<DetailedEstimate> create(DetailedEstimateRequest detailedEstimateRequest) {
        AuditDetails auditDetails = setAuditDetails(detailedEstimateRequest.getRequestInfo().getUserInfo().getUsername(), false);
        for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            detailedEstimate.setId(commonUtils.getUUID());
            detailedEstimate.setAuditDetails(auditDetails);
            detailedEstimate.setTotalIncludingRE(detailedEstimate.getWorkValue());

            if(detailedEstimate.getAbstractEstimateDetail() != null) {
                detailedEstimate.setEstimateNumber(detailedEstimate.getAbstractEstimateDetail().getEstimateNumber());
            } else {
                String estimateNumber = idGenerationRepository
                        .generateDetailedEstimateNumber(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
                detailedEstimate.setEstimateNumber(propertiesManager.getDetailedEstimateNumberPrefix() + estimateNumber);
            }

            for(final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets()) {
                assetsForEstimate.setId(commonUtils.getUUID());
                assetsForEstimate.setAuditDetails(auditDetails);
            }

            for(final MultiYearEstimate multiYearEstimate : detailedEstimate.getMultiYearEstimates()) {
                multiYearEstimate.setId(commonUtils.getUUID());
                multiYearEstimate.setAuditDetails(auditDetails);
            }

            for(final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {
                estimateOverhead.setId(commonUtils.getUUID());
                estimateOverhead.setAuditDetails(auditDetails);
            }

            for(final DetailedEstimateDeduction detailedEstimateDeduction : detailedEstimate.getDetailedEstimateDeductions()) {
                detailedEstimateDeduction.setId(commonUtils.getUUID());
                detailedEstimateDeduction.setAuditDetails(auditDetails);
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
        }
        kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateCreateTopic(), detailedEstimateRequest);
        return detailedEstimateRequest.getDetailedEstimates();
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

    public void validateOverheads(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String,String> messages) {
        for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()){

            JSONArray responseJSONArray = null;
            if(estimateOverhead != null) {
                if (estimateOverhead.getOverhead() != null && StringUtils.isNotBlank(estimateOverhead.getOverhead().getCode())) {
                    responseJSONArray = estimateUtils.getMDMSData(OVERHEAD_OBJECT,
                            estimateOverhead.getOverhead().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                            WORKS_MODULE_CODE);
                    if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                        messages.put(KEY_ESTIMATE_OVERHEAD_CODE_INVALID, MESSAGE_ESTIMATE_OVERHEAD_CODE_INVALID);
                    }
                }

                if (estimateOverhead.getOverhead().getId() == null) {
                    messages.put(KEY_ESIMATE_OVERHEAD_ID, MESSAGE_ESIMATE_OVERHEAD_ID);
                }
                if (estimateOverhead.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    messages.put(KEY_ESIMATE_OVERHEAD_AMOUNT, MESSAGE_ESIMATE_OVERHEAD_AMOUNT);
                }

            }
        }
    }

    public void validateMultiYearEstimates(final DetailedEstimate detailedEstimate, Map<String,String> messages) {
        FinancialYear financialYear = null;
        Double totalPercentage = 0d;
        for (final MultiYearEstimate multiYearEstimate : detailedEstimate.getMultiYearEstimates()) {
            totalPercentage = totalPercentage + multiYearEstimate.getPercentage();
            if (financialYear != null && financialYear.equals(multiYearEstimate.getFinancialYear()))
                messages.put(KEY_DUPLICATE_MULTIYEAR_ESTIMATE, MESSAGE_DUPLICATE_MULTIYEAR_ESTIMATE);
            if (totalPercentage > 100)
                messages.put(KEY_PERCENTAGE_MULTIYEAR_ESTIMATE, MESSAGE_PERCENTAGE_MULTIYEAR_ESTIMATE);
            financialYear = multiYearEstimate.getFinancialYear();
        }

    }

    public void validateActivities(final DetailedEstimate detailedEstimate, Map<String,String> messages) {
        for (int i = 0; i < detailedEstimate.getEstimateActivities().size() ; i++)
            for (int j = i + 1; j < detailedEstimate.getEstimateActivities().size(); j++)
                if (detailedEstimate.getEstimateActivities().get(i).getScheduleOfRate() != null
                        && detailedEstimate.getEstimateActivities().get(i).getScheduleOfRate().getId()
                        .equals(detailedEstimate.getEstimateActivities().get(j).getScheduleOfRate().getId())) {
                    messages.put(KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE, MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE);
                }
        if(detailedEstimate.getEstimateActivities() == null || detailedEstimate.getEstimateActivities().isEmpty())
            messages.put(KEY_ESTIMATE_ACTIVITY_REQUIRED, MESSAGE_ESTIMATE_ACTIVITY_REQUIRED);

        for (final EstimateActivity activity : detailedEstimate.getEstimateActivities()) {
            if((activity.getScheduleOfRate() != null && activity.getScheduleOfRate().getId() == null) || (activity.getNonSor() != null && activity.getNonSor().getId() == null))
                messages.put(KEY_ESTIMATE_ACTIVITY_REQUIRED, MESSAGE_ESTIMATE_ACTIVITY_REQUIRED);

            if(activity.getScheduleOfRate() == null && activity.getNonSor() == null )
                messages.put(KEY_ESTIMATE_ACTIVITY_INVALID, MESSAGE_ESTIMATE_ACTIVITY_INVALID);

            if (activity.getQuantity() != null && activity.getQuantity() <= 0)
               messages.put(KEY_ESTIMATE_ACTIVITY_QUANTITY, MESSAGE_ESTIMATE_ACTIVITY_QUANTITY);

            if (activity.getEstimateRate() != null && activity.getEstimateRate().compareTo(BigDecimal.ZERO) <= 0)
               messages.put(KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE, MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE);


        }

    }

    public void validateLocationDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String,String> messages) {
        if (propertiesManager.getLocationRequiredForEstimate().toString().equalsIgnoreCase("Yes")) {
            JSONArray mdmsArray = estimateUtils.getMDMSData(APPCONFIGURATION_OBJECT, GIS_INTEGRATION_APPCONFIG, null,
                    detailedEstimate.getTenantId(), requestInfo, WORKS_MODULE_CODE);
            if(mdmsArray != null && !mdmsArray.isEmpty()) {
                Map<String,Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
                if (jsonMap.get("value").equals("Yes") && (StringUtils.isBlank(detailedEstimate.getLocation())
                        || detailedEstimate.getLatitude() == null || detailedEstimate.getLongitude() == null))
                messages.put(KEY_ESTIMATE_LOCATION_REQUIRED, MESSAGE_ESTIMATE_LOCATION_REQUIRED);
            }
        }
    }

    public void validateAssetDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String,String> messages) {

        JSONArray mdmsArray = estimateUtils.getMDMSData(APPCONFIGURATION_OBJECT, ASSET_DETAILES_REQUIRED_APPCONFIG, null,
                detailedEstimate.getTenantId(), requestInfo, WORKS_MODULE_CODE);
        if(mdmsArray != null && !mdmsArray.isEmpty()) {
            Map<String,Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
            if (jsonMap.get("value").equals("Yes") && detailedEstimate.getAssets() != null
                    && detailedEstimate.getAssets().isEmpty())
            messages.put(KEY_ESTIMATE_ASSET_DETAILS_REQUIRED, MESSAGE_ESTIMATE_ASSET_DETAILS_REQUIRED);
        }

        Asset asset = null;
        for (final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets())
            if (assetsForEstimate != null) {
                if(StringUtils.isBlank(assetsForEstimate.getAsset().getCode()))
                    messages.put(KEY_ESTIMATE_ASSET_REQUIRED, MESSAGE_ESTIMATE_ASSET_REQUIRED);
                if (asset != null && asset.getCode().equals(assetsForEstimate.getAsset().getCode()))
                  messages.put(KEY_DUPLICATE_ESTIMATE_ASSET_DETAILS, MESSAGE_DUPLICATE_ESTIMATE_ASSET_DETAILS);
            }
    }

    public void validateDetailedEstimates(DetailedEstimateRequest detailedEstimateRequest) {
        final RequestInfo requestInfo = detailedEstimateRequest.getRequestInfo();
        Map<String, String> messages = new HashMap<>();
        for(DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            validateActivities(detailedEstimate, messages);
            validateLocationDetails(detailedEstimate,requestInfo, messages);
            validateAssetDetails(detailedEstimate, requestInfo, messages);
            validateMultiYearEstimates(detailedEstimate, messages);
            validateOverheads(detailedEstimate, requestInfo, messages);
            validateMasterData(detailedEstimate, requestInfo, messages);
        }
        if(messages != null && !messages.isEmpty())
             throw new CustomException(messages);
    }

    private void validateMasterData(DetailedEstimate detailedEstimate, RequestInfo requestInfo, Map<String,String> messages) {
        JSONArray responseJSONArray = null;

        if (detailedEstimate.getFund() != null && StringUtils.isNotBlank(detailedEstimate.getFund().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(FUND_OBJECT,
                    detailedEstimate.getFund().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_FUND_CODE_INVALID, MESSAGE_ESTIMATE_FUND_CODE_INVALID);
            }
        }
        if (detailedEstimate.getFunction() != null
                && StringUtils.isNotBlank(detailedEstimate.getFunction().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(FUNCTION_OBJECT,
                    detailedEstimate.getFunction().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_FUNCTION_CODE_INVALID, MESSAGE_ESTIMATE_FUNCTION_CODE_INVALID);
            }
        }

        if (detailedEstimate.getWorksType() != null
                && StringUtils.isNotBlank(detailedEstimate.getWorksType().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(TYPEOFWORK_OBJECT,
                    detailedEstimate.getWorksType().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_TYPEOFWORK_CODE_INVALID, MESSAGE_ESTIMATE_TYPEOFWORK_CODE_INVALID);
            }
        }
        if (detailedEstimate.getWorksSubtype() != null
                && StringUtils.isNotBlank(detailedEstimate.getWorksSubtype().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(TYPEOFWORK_OBJECT,
                    detailedEstimate.getWorksSubtype().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_SUBTYPEOFWORK_CODE_INVALID, MESSAGE_ESTIMATE_SUBTYPEOFWORK_CODE_INVALID);
            }
        }

        if (detailedEstimate.getDepartment() != null
                & StringUtils.isNotBlank(detailedEstimate.getDepartment().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(DEPARTMENT_OBJECT,
                    detailedEstimate.getDepartment().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    COMMON_MASTERS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_DEPARTMENT_CODE_INVALID, MESSAGE_ESTIMATE_DEPARTMENT_CODE_INVALID);
            }
        }
        if (detailedEstimate.getScheme() != null & StringUtils.isNotBlank(detailedEstimate.getScheme().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(SCHEME_OBJECT,
                    detailedEstimate.getScheme().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_SCHEME_CODE_INVALID, MESSAGE_ESTIMATE_SCHEME_CODE_INVALID);
            }
        }

        if (detailedEstimate.getSubScheme() != null
                & StringUtils.isNotBlank(detailedEstimate.getSubScheme().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(SUBSCHEME_OBJECT,
                    detailedEstimate.getSubScheme().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_SUBSCHEME_CODE_INVALID, MESSAGE_ESTIMATE_SUBSCHEME_CODE_INVALID);
            }
        }

        if (detailedEstimate.getBudgetGroup() != null
                & StringUtils.isNotBlank(detailedEstimate.getBudgetGroup().getName())) {
            responseJSONArray = estimateUtils.getMDMSData(BUDGETGROUP_OBJECT, null,
                    detailedEstimate.getBudgetGroup().getName(), detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_BUDGETGROUP_INVALID, MESSAGE_ESTIMATE_BUDGETGROUP_INVALID);
            }
        }
    }
}
	

