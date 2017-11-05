package org.egov.works.estimate.domain.service;

import com.google.gson.JsonObject;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
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
import java.util.List;

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
            String estimateNumber = idGenerationRepository
                    .generateAbstractEstimateNumber(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
            detailedEstimate.setEstimateNumber(propertiesManager.getDetailedEstimateNumberPrefix() + "/" + estimateNumber);

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

    public void validateOverheads(final DetailedEstimate detailedEstimate) {
        for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()){
            if (estimateOverhead.getOverhead().getId() == null) {
                throw new InvalidDataException("OverHead", "Activity overhead is required",
                        estimateOverhead.getOverhead().getId());
            }
        if (estimateOverhead.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("OverHead", "Activity overhead amount required",
                    estimateOverhead.getOverhead().getId());
        }
      }
    }

    public void validateMultiYearEstimates(final DetailedEstimate detailedEstimate) {
        FinancialYear financialYear = null;
        Double totalPercentage = 0d;
        Integer index = 0;
        for (final MultiYearEstimate multiYearEstimate : detailedEstimate.getMultiYearEstimates()) {
            totalPercentage = totalPercentage + multiYearEstimate.getPercentage();

            if (multiYearEstimate.getFinancialYear() == null)
                throw new InvalidDataException("multiYearEstimates[" + index + "].financialYear", "Financial year is required",
                    multiYearEstimate.getFinancialYear().getFinYearRange());
            if (multiYearEstimate.getPercentage() == 0)
                throw new InvalidDataException("multiYearEstimates[" + index + "].percentage", "Percentage is required",
                    multiYearEstimate.getFinancialYear().getFinYearRange());
            if (financialYear != null && financialYear.equals(multiYearEstimate.getFinancialYear()))
                throw new InvalidDataException("multiYearEstimates[" + index + "].financialYear", "Duplicate financial year",
                    multiYearEstimate.getFinancialYear().getFinYearRange());
            if (totalPercentage > 100)
                  throw new InvalidDataException("multiYearEstimates[" + index + "].percentage", "Percentage should not  be greater than 100",
                    multiYearEstimate.getFinancialYear().getFinYearRange());
            financialYear = multiYearEstimate.getFinancialYear();
            index++;
        }

    }

    public void validateActivities(final DetailedEstimate detailedEstimate) {
        for (int i = 0; i < detailedEstimate.getEstimateActivities().size() - 1; i++)
            for (int j = i + 1; j < detailedEstimate.getEstimateActivities().size(); j++)
                if (detailedEstimate.getEstimateActivities().get(i).getScheduleOfRate() != null
                        && detailedEstimate.getEstimateActivities().get(i).getScheduleOfRate().getId()
                        .equals(detailedEstimate.getEstimateActivities().get(j).getScheduleOfRate().getId())) {
                    throw new InvalidDataException("ScheduleOfRate", "Duplicate data for activity schedule of rates",
                            detailedEstimate.getEstimateActivities().get(i).getScheduleOfRate().getCode());
                }

        for (final EstimateActivity activity : detailedEstimate.getEstimateActivities()) {
            if (activity.getQuantity() <= 0)
            throw new InvalidDataException("Quantity", "Activity quantity should be greater than zero",
                    activity.getScheduleOfRate().getCode());
            if (activity.getEstimateRate().compareTo(BigDecimal.ZERO) <= 0)
                throw new InvalidDataException("EstimateRate", "Activity estimate rate should be greater than zero",
                        activity.getEstimateRate().toString());
        }

    }

    public void validateLocationDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo) {
        if (propertiesManager.getLocationRequiredForEstimate().toString().equalsIgnoreCase("Yes")) {
            JSONArray mdmsArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.APPCONFIGURATION_OBJECT, WorksEstimateServiceConstants.GIS_INTEGRATION_APPCONFIG, null,
                    detailedEstimate.getTenantId(), requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (mdmsArray != null && !mdmsArray.isEmpty() && mdmsArray.get(0).equals("Yes") && (StringUtils.isBlank(detailedEstimate.getLocation())
                    || detailedEstimate.getLatitude() == null || detailedEstimate.getLongitude() == null))
            throw new InvalidDataException("Location", "Estimate location detailed required",
                    detailedEstimate.getLocation());
        }
    }

    public void validateAssetDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo) {

        JSONArray mdmsArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.APPCONFIGURATION_OBJECT, WorksEstimateServiceConstants.ASSET_DETAILES_REQUIRED_APPCONFIG, null,
                detailedEstimate.getTenantId(), requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (mdmsArray != null && !mdmsArray.isEmpty() && mdmsArray.get(0).equals("Yes") && detailedEstimate.getAssets() != null
                    && detailedEstimate.getAssets().isEmpty())
                throw new InvalidDataException("Asset detailes", "Asset detailes required for estimate",
                        null);

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
        for(DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            validateActivities(detailedEstimate);
           // validateLocationDetails(detailedEstimate,requestInfo);
           // validateAssetDetails(detailedEstimate, requestInfo);
            validateMultiYearEstimates(detailedEstimate);
            validateOverheads(detailedEstimate);
            validateMasterData(detailedEstimate, requestInfo);
        }
    }

    private void validateMasterData(DetailedEstimate detailedEstimate, RequestInfo requestInfo) {
        JSONArray responseJSONArray = null;

        if (detailedEstimate.getFund() != null && StringUtils.isNotBlank(detailedEstimate.getFund().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.FUND_OBJECT,
                    detailedEstimate.getFund().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("Fund", "Invalid data for fund code",
                        detailedEstimate.getFund().getCode());
            }
        }
        if (detailedEstimate.getFunction() != null
                && StringUtils.isNotBlank(detailedEstimate.getFunction().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.FUNCTION_OBJECT,
                    detailedEstimate.getFunction().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("Function", "Invalid data for function code",
                        detailedEstimate.getFunction().getCode());
            }
        }

        if (detailedEstimate.getWorksType() != null
                && StringUtils.isNotBlank(detailedEstimate.getWorksType().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.TYPEOFWORK_OBJECT,
                    detailedEstimate.getWorksType().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("TypeOfWork", "Invalid data for estimate type of work",
                        detailedEstimate.getWorksType().getCode());
            }
        }
        if (detailedEstimate.getWorksSubtype() != null
                && StringUtils.isNotBlank(detailedEstimate.getWorksSubtype().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.SUBTYPEOFWORK_OBJECT,
                    detailedEstimate.getWorksSubtype().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("SubTypeOfWork", "Invalid data for estimate subtype of work",
                        detailedEstimate.getWorksType().getCode());
            }
        }

        if (detailedEstimate.getDepartment() != null
                & StringUtils.isNotBlank(detailedEstimate.getDepartment().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.DEPARTMENT_OBJECT,
                    detailedEstimate.getDepartment().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.COMMON_MASTERS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("Department", "Invalid data for estimate Department",
                        detailedEstimate.getDepartment().getCode());
            }
        }
        if (detailedEstimate.getScheme() != null & StringUtils.isNotBlank(detailedEstimate.getScheme().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.SCHEME_OBJECT,
                    detailedEstimate.getScheme().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("Scheme", "Invalid data for estimate scheme",
                        detailedEstimate.getScheme().getCode());
            }
        }

        if (detailedEstimate.getSubScheme() != null
                & StringUtils.isNotBlank(detailedEstimate.getSubScheme().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.SUBSCHEME_OBJECT,
                    detailedEstimate.getSubScheme().getCode(), null, detailedEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("SubScheme", "Invalid data for estimate SubScheme",
                        detailedEstimate.getSubScheme().getCode());
            }
        }

        if (detailedEstimate.getBudgetGroup() != null
                & StringUtils.isNotBlank(detailedEstimate.getBudgetGroup().getName())) {
            responseJSONArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.BUDGETGROUP_OBJECT, null,
                    detailedEstimate.getBudgetGroup().getName(), detailedEstimate.getTenantId(), requestInfo,
                    WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                throw new InvalidDataException("BudgetGroup", "Invalid data for estimate Budget Group",
                        detailedEstimate.getBudgetGroup().getName());
            }
        }
    }
}
	

