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
                detailedEstimate.setAuditDetails(auditDetails);
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

    public void validateOverheads(final DetailedEstimate detailedEstimate,Map<String,String> messages) {
        for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()){
            if (estimateOverhead.getOverhead().getId() == null) {
                messages.put(KEY_ESIMATE_OVERHEAD_ID, MESSAGE_ESIMATE_OVERHEAD_ID);
            }
            if (estimateOverhead.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                messages.put(KEY_ESIMATE_OVERHEAD_AMOUNT, MESSAGE_ESIMATE_OVERHEAD_AMOUNT);
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
        for (int i = 0; i < detailedEstimate.getEstimateActivities().size() - 1; i++)
            for (int j = i + 1; j < detailedEstimate.getEstimateActivities().size(); j++)
                if (detailedEstimate.getEstimateActivities().get(i).getScheduleOfRate() != null
                        && detailedEstimate.getEstimateActivities().get(i).getScheduleOfRate().getId()
                        .equals(detailedEstimate.getEstimateActivities().get(j).getScheduleOfRate().getId())) {
                    messages.put("ScheduleOfRate", "Duplicate data for activity schedule of rates");
                }

        for (final EstimateActivity activity : detailedEstimate.getEstimateActivities()) {
            if (activity.getQuantity() != null && activity.getQuantity() <= 0)
               messages.put("Quantity", "Activity quantity should be greater than zero");
            if (activity.getEstimateRate() != null && activity.getEstimateRate().compareTo(BigDecimal.ZERO) <= 0)
               messages.put("EstimateRate", "Activity estimate rate should be greater than zero");
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
                    throw new InvalidDataException("Location", "Estimate location detailed required",
                            detailedEstimate.getLocation());
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
                throw new InvalidDataException("Asset detailes", "Asset detailes required for estimate",
                        null);
        }

        Asset asset = null;
        Integer index = 0;
        for (final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets())
            if (assetsForEstimate != null) {
                if (StringUtils.isBlank(assetsForEstimate.getAsset().getCode()))
                    throw new InvalidDataException("assets[" + index + "].asset.code", "Asset code is required",
                            assetsForEstimate.getAsset().getCode());
                if (StringUtils.isBlank(assetsForEstimate.getAsset().getName()))
                    throw new InvalidDataException("assets[" + index + "].asset.name", "Asset code is required",
                            assetsForEstimate.getAsset().getName());
                if (asset != null && asset.getCode().equals(assetsForEstimate.getAsset().getCode()))
                    throw new InvalidDataException("assets[" + index + "].asset.code", "Duplicate asset code",
                            assetsForEstimate.getAsset().getCode());
                asset = assetsForEstimate.getAsset();
                index++;
            }
    }

    public void validateDetailedEstimates(DetailedEstimateRequest detailedEstimateRequest) {
        final RequestInfo requestInfo = detailedEstimateRequest.getRequestInfo();
        Map<String, String> messages = new HashMap<>();
        for(DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            validateActivities(detailedEstimate, messages);
            //validateLocationDetails(detailedEstimate,requestInfo);
            //validateAssetDetails(detailedEstimate, requestInfo);
            validateMultiYearEstimates(detailedEstimate, messages);
            validateOverheads(detailedEstimate, messages);
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
                throw new InvalidDataException("Fund", "Invalid data for fund code",
                        detailedEstimate.getFund().getCode());
            }
        }
        if (detailedEstimate.getFunction() != null
                && StringUtils.isNotBlank(detailedEstimate.getFunction().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(FUNCTION_OBJECT,
                    detailedEstimate.getFunction().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("Function", "Invalid data for function code",
                        detailedEstimate.getFunction().getCode());
            }
        }

        if (detailedEstimate.getWorksType() != null
                && StringUtils.isNotBlank(detailedEstimate.getWorksType().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(TYPEOFWORK_OBJECT,
                    detailedEstimate.getWorksType().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("TypeOfWork", "Invalid data for estimate type of work",
                        detailedEstimate.getWorksType().getCode());
            }
        }
        if (detailedEstimate.getWorksSubtype() != null
                && StringUtils.isNotBlank(detailedEstimate.getWorksSubtype().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(SUBTYPEOFWORK_OBJECT,
                    detailedEstimate.getWorksSubtype().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("SubTypeOfWork", "Invalid data for estimate subtype of work",
                        detailedEstimate.getWorksType().getCode());
            }
        }

        if (detailedEstimate.getDepartment() != null
                & StringUtils.isNotBlank(detailedEstimate.getDepartment().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(DEPARTMENT_OBJECT,
                    detailedEstimate.getDepartment().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    COMMON_MASTERS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("Department", "Invalid data for estimate Department",
                        detailedEstimate.getDepartment().getCode());
            }
        }
        if (detailedEstimate.getScheme() != null & StringUtils.isNotBlank(detailedEstimate.getScheme().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(SCHEME_OBJECT,
                    detailedEstimate.getScheme().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("Scheme", "Invalid data for estimate scheme",
                        detailedEstimate.getScheme().getCode());
            }
        }

        if (detailedEstimate.getSubScheme() != null
                & StringUtils.isNotBlank(detailedEstimate.getSubScheme().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(SUBSCHEME_OBJECT,
                    detailedEstimate.getSubScheme().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("SubScheme", "Invalid data for estimate SubScheme",
                        detailedEstimate.getSubScheme().getCode());
            }
        }

        if (detailedEstimate.getBudgetGroup() != null
                & StringUtils.isNotBlank(detailedEstimate.getBudgetGroup().getName())) {
            responseJSONArray = estimateUtils.getMDMSData(BUDGETGROUP_OBJECT, null,
                    detailedEstimate.getBudgetGroup().getName(), detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("BudgetGroup", "Invalid data for estimate Budget Group",
                        detailedEstimate.getBudgetGroup().getName());
            }
        }
    }
}
	

