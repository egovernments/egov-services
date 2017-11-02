package org.egov.works.estimate.domain.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.domain.model.AuditDetails;
import org.egov.works.commons.web.contract.RequestInfo;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.config.WorksEstimateServiceConstants;
import org.egov.works.estimate.domain.repository.DetailedEstimateRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.DetailedEstimateRequest;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
import org.egov.works.estimate.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

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

	public List<DetailedEstimate> search(DetailedEstimateSearchContract detailedEstimateSearchContract) {
		return detailedEstimateRepository.search(detailedEstimateSearchContract);
	}

    public List<DetailedEstimate> create(DetailedEstimateRequest detailedEstimateRequest) {
        AuditDetails auditDetails = setAuditDetails(detailedEstimateRequest.getRequestInfo().getUserInfo().getUsername(), false);
        for (final DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            detailedEstimate.setId(UUID.randomUUID().toString().replace("-", ""));
            detailedEstimate.setAuditDetails(auditDetails);

            for(final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets()) {
                assetsForEstimate.setId(UUID.randomUUID().toString().replace("-", ""));
                detailedEstimate.setAuditDetails(auditDetails);
            }

            for(final MultiYearEstimate multiYearEstimate : detailedEstimate.getMultiYearEstimates()) {
                multiYearEstimate.setId(UUID.randomUUID().toString().replace("-", ""));
                multiYearEstimate.setAuditDetails(auditDetails);
            }

            for(final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {
                estimateOverhead.setId(UUID.randomUUID().toString().replace("-", ""));
                estimateOverhead.setAuditDetails(auditDetails);
            }

            for(final DetailedEstimateDeduction detailedEstimateDeduction : detailedEstimate.getDetailedEstimateDeductions()) {
                detailedEstimateDeduction.setId(UUID.randomUUID().toString().replace("-", ""));
                detailedEstimateDeduction.setAuditDetails(auditDetails);
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

    public void validateOverheads(final DetailedEstimate detailedEstimate, final BindingResult errors) {
        for (final EstimateOverhead value : detailedEstimate.getEstimateOverheads())
            if (value.getOverhead().getId() == null) {
                errors.reject("error.overhead.null", "error.overhead.null");
                break;
            }
        for (final EstimateOverhead value : detailedEstimate.getEstimateOverheads())
            if (value.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                errors.reject("error.overhead.amount", "error.overhead.amount");
                break;
            }
    }

    public void validateMultiYearEstimates(final DetailedEstimate detailedEstimate, final BindingResult bindErrors) {
        FinancialYear financialYear = null;
        Double totalPercentage = 0d;
        Integer index = 0;
        for (final MultiYearEstimate multiYearEstimate : detailedEstimate.getMultiYearEstimates()) {
            totalPercentage = totalPercentage + multiYearEstimate.getPercentage();

            if (multiYearEstimate.getFinancialYear() == null)
                bindErrors.rejectValue("multiYearEstimates[" + index + "].financialYear", "error.finyear.required");
            if (multiYearEstimate.getPercentage() == 0)
                bindErrors.rejectValue("multiYearEstimates[" + index + "].percentage", "error.percentage.required");
            if (financialYear != null && financialYear.equals(multiYearEstimate.getFinancialYear()))
                bindErrors.rejectValue("multiYearEstimates[" + index + "].financialYear", "error.financialYear.unique");
            if (totalPercentage > 100)
                bindErrors.rejectValue("multiYearEstimates[" + index + "].percentage", "error.percentage.greater");
            financialYear = multiYearEstimate.getFinancialYear();
            index++;
        }

    }

/*    public void validateActivities(final DetailedEstimate detailedEstimate, final BindingResult errors) {
        for (int i = 0; i < detailedEstimate.gets.getSorActivities().size() - 1; i++)
            for (int j = i + 1; j < abstractEstimate.getSorActivities().size(); j++)
                if (abstractEstimate.getSorActivities().get(i).getSchedule() != null
                        && abstractEstimate.getSorActivities().get(i).getSchedule().getId()
                        .equals(abstractEstimate.getSorActivities().get(j).getSchedule().getId())) {
                    errors.reject("error.sor.duplicate", "error.sor.duplicate");
                    break;
                }

        for (final Activity activity : abstractEstimate.getSorActivities()) {
            if (activity.getQuantity() <= 0)
                errors.reject("error.quantity.zero", "error.quantity.zero");
            if (activity.getRate() <= 0)
                errors.reject("error.rates.zero", "error.rates.zero");
        }

        for (final Activity activity : abstractEstimate.getNonSorActivities()) {
            if (activity.getQuantity() <= 0)
                errors.reject("error.quantity.zero", "error.quantity.zero");
            if (activity.getRate() <= 0)
                errors.reject("error.rates.zero", "error.rates.zero");
        }
    }*/

    public void validateLocationDetails(final DetailedEstimate detailedEstimate, final BindingResult bindErrors, final RequestInfo requestInfo) {
        if (propertiesManager.getLocationRequiredForEstimate().toString().equalsIgnoreCase("Yes")) {
            JSONArray mdmsArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.APPCONFIGURATION_OBJECT, WorksEstimateServiceConstants.GIS_INTEGRATION_APPCONFIG,
                    detailedEstimate.getTenantId(), requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (mdmsArray != null && !mdmsArray.isEmpty() && mdmsArray.get(0).equals("Yes") && (StringUtils.isBlank(detailedEstimate.getLocation())
                    || detailedEstimate.getLatitude() == null || detailedEstimate.getLongitude() == null))
                bindErrors.reject("error.locationdetails.required", "error.locationdetails.required");
        }
    }

    public void validateAssetDetails(final DetailedEstimate detailedEstimate, final BindingResult bindErrors, final RequestInfo requestInfo) {

        JSONArray mdmsArray = estimateUtils.getMDMSData(WorksEstimateServiceConstants.APPCONFIGURATION_OBJECT, WorksEstimateServiceConstants.ASSET_DETAILES_REQUIRED_APPCONFIG,
                detailedEstimate.getTenantId(), requestInfo, WorksEstimateServiceConstants.WORKS_MODULE_CODE);
            if (mdmsArray != null && !mdmsArray.isEmpty() && mdmsArray.get(0).equals("Yes") && detailedEstimate.getAssets() != null
                    && detailedEstimate.getAssets().isEmpty())
                bindErrors.reject("error.assetdetails.required", "error.assetdetails.required");

            Asset asset = null;
            Integer index = 0;
            for (final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets())
                if (assetsForEstimate != null) {
                    if (StringUtils.isBlank(assetsForEstimate.getAsset().getCode()))
                        bindErrors.rejectValue("tempAssetValues[" + index + "].asset.code", "error.assetcode.required");
                    if (StringUtils.isBlank(assetsForEstimate.getAsset().getName()))
                        bindErrors.rejectValue("tempAssetValues[" + index + "].asset.name", "error.assetname.required");
                    if (asset != null && asset.getCode().equals(assetsForEstimate.getAsset().getCode()))
                        bindErrors.rejectValue("tempAssetValues[" + index + "].asset.code", "error.asset.not.unique");
                    asset = assetsForEstimate.getAsset();
                    index++;
                }
        }
    }
	

