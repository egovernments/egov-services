package org.egov.works.estimate.domain.service;

import static org.egov.works.estimate.config.WorksEstimateServiceConstants.APPCONFIGURATION_OBJECT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.ASSET_DETAILES_REQUIRED_APPCONFIG;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.BUDGETGROUP_OBJECT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.COMMON_MASTERS_MODULE_CODE;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.DEPARTMENT_OBJECT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.FUNCTION_OBJECT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.FUND_OBJECT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.GIS_INTEGRATION_APPCONFIG;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_DUPLICATE_ESTIMATE_ASSET_DETAILS;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_DUPLICATE_MULTIYEAR_ESTIMATE;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESIMATE_OVERHEAD_AMOUNT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESIMATE_OVERHEAD_ID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_QUANTITY;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_UNIT_RATE_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_UOM_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ACTIVITY_UOM_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ASSET_DETAILS_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_ASSET_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_BUDGETGROUP_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_DEPARTMENT_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_FUNCTION_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_FUND_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_LOCATION_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_OVERHEAD_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_SCHEME_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_SUBSCHEME_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_SUBTYPEOFWORK_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_ESTIMATE_TYPEOFWORK_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.KEY_PERCENTAGE_MULTIYEAR_ESTIMATE;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_DUPLICATE_ESTIMATE_ASSET_DETAILS;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_DUPLICATE_MULTIYEAR_ESTIMATE;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESIMATE_OVERHEAD_AMOUNT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESIMATE_OVERHEAD_ID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_QUANTITY;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_UNIT_RATE_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_UOM_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ACTIVITY_UOM_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ASSET_DETAILS_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_ASSET_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_BUDGETGROUP_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_DEPARTMENT_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_FUNCTION_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_FUND_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_LOCATION_REQUIRED;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_OVERHEAD_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_SCHEME_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_SUBSCHEME_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_SUBTYPEOFWORK_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_ESTIMATE_TYPEOFWORK_CODE_INVALID;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.MESSAGE_PERCENTAGE_MULTIYEAR_ESTIMATE;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.OVERHEAD_OBJECT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.SCHEME_OBJECT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.SUBSCHEME_OBJECT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.TYPEOFWORK_OBJECT;
import static org.egov.works.estimate.config.WorksEstimateServiceConstants.WORKS_MODULE_CODE;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.DetailedEstimateRepository;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.Asset;
import org.egov.works.estimate.web.contract.AssetsForEstimate;
import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.DetailedEstimateDeduction;
import org.egov.works.estimate.web.contract.DetailedEstimateRequest;
import org.egov.works.estimate.web.contract.DetailedEstimateSearchContract;
import org.egov.works.estimate.web.contract.DetailedEstimateStatus;
import org.egov.works.estimate.web.contract.EstimateActivity;
import org.egov.works.estimate.web.contract.EstimateMeasurementSheet;
import org.egov.works.estimate.web.contract.EstimateOverhead;
import org.egov.works.estimate.web.contract.FinancialYear;
import org.egov.works.estimate.web.contract.MultiYearEstimate;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.WorkFlowDetails;
import org.egov.works.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.minidev.json.JSONArray;

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
            if (detailedEstimate.getSpillOverFlag())
            	detailedEstimate.setStatus(DetailedEstimateStatus.APPROVED);
			else {
				populateWorkFlowDetails(detailedEstimate, detailedEstimateRequest.getRequestInfo());
				detailedEstimate.setStateId(workflowService.enrichWorkflow(detailedEstimate.getWorkFlowDetails(),
						detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()));
				detailedEstimate.setStatus(DetailedEstimateStatus.CREATED);
			}
        }
        kafkaTemplate.send(propertiesManager.getWorksDetailedEstimateCreateTopic(), detailedEstimateRequest);
        return detailedEstimateRequest.getDetailedEstimates();
    }
    
    public List<DetailedEstimate> update(DetailedEstimateRequest detailedEstimateRequest) {
        AuditDetails updateDetails = setAuditDetails(detailedEstimateRequest.getRequestInfo().getUserInfo().getUsername(), true);
        AuditDetails createDetails = setAuditDetails(detailedEstimateRequest.getRequestInfo().getUserInfo().getUsername(), false);
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

            if (activity.getEstimateRate() == null)
                messages.put(KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED, MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE_REQUIRED);
            else if(activity.getEstimateRate().compareTo(BigDecimal.ZERO) <= 0)
               messages.put(KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE, MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE);

            if(activity.getUom() == null)
                messages.put(KEY_ESTIMATE_ACTIVITY_UOM_REQUIRED, MESSAGE_ESTIMATE_ACTIVITY_UOM_REQUIRED);
            else if(StringUtils.isBlank(activity.getUom().getCode()))
                messages.put(KEY_ESTIMATE_ACTIVITY_UOM_CODE_INVALID, MESSAGE_ESTIMATE_ACTIVITY_UOM_CODE_INVALID);

            if(activity.getUnitRate() == null)
                messages.put(KEY_ESTIMATE_ACTIVITY_UNIT_RATE_REQUIRED, MESSAGE_ESTIMATE_ACTIVITY_UNIT_RATE_REQUIRED);
            else if(activity.getUnitRate().compareTo(BigDecimal.ZERO) <= 0)
                messages.put(KEY_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID, MESSAGE_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID);

            if(activity.getEstimateMeasurementSheets() != null)
                for(final EstimateMeasurementSheet estimateMeasurementSheet : activity.getEstimateMeasurementSheets()) {
                    if(estimateMeasurementSheet.getQuantity() == null)
                        messages.put(KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED, MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED);
                    else if(estimateMeasurementSheet.getQuantity().compareTo(BigDecimal.ZERO) <= 0)
                        messages.put(KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID, MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID);
                }

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
    
    private void populateWorkFlowDetails(DetailedEstimate detailedEstimate, RequestInfo requestInfo) {

		if (null != detailedEstimate && null != detailedEstimate.getWorkFlowDetails()) {

			WorkFlowDetails workFlowDetails = detailedEstimate.getWorkFlowDetails();

			workFlowDetails.setType(DETAILED_ESTIMATE_WF_TYPE);
			workFlowDetails.setBusinessKey(DETAILED_ESTIMATE_BUSINESSKEY);
			workFlowDetails.setStateId(detailedEstimate.getStateId());
			if (detailedEstimate.getStatus() != null)
				workFlowDetails.setStatus(detailedEstimate.getStatus().toString());

			if (null != requestInfo && null != requestInfo.getUserInfo()) {
				workFlowDetails.setSenderName(requestInfo.getUserInfo().getUsername());
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
	

