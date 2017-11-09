package org.egov.works.estimate.domain.service;

import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.AbstractEstimateRepository;
import org.egov.works.estimate.domain.repository.DetailedEstimateRepository;
import org.egov.works.estimate.persistence.repository.AbstractEstimateDetailsJdbcRepository;
import org.egov.works.estimate.persistence.repository.AssetRepository;
import org.egov.works.estimate.persistence.repository.IdGenerationRepository;
import org.egov.works.estimate.persistence.repository.WorksMastersRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.egov.works.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.minidev.json.JSONArray;
import org.springframework.validation.BindingResult;

import static org.egov.works.estimate.config.WorksEstimateServiceConstants.*;

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
    private WorksMastersRepository worksMastersRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AbstractEstimateDetailsJdbcRepository abstractEstimateDetailsJdbcRepository;

    @Autowired
    private AbstractEstimateRepository abstractEstimateRepository;

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
                AbstractEstimateDetailsSearchContract searchContract = AbstractEstimateDetailsSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                        .ids(Arrays.asList(detailedEstimate.getAbstractEstimateDetail().getId())).build();
                List<AbstractEstimateDetails> abstractEstimateDetails = abstractEstimateDetailsJdbcRepository.search(searchContract);
                if(!abstractEstimateDetails.isEmpty()) {
                    String abstractEstimateId = abstractEstimateDetails.get(0).getAbstractEstimate().getId();
                    AbstractEstimateSearchContract abstractEstimateSearchContract = AbstractEstimateSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                            .ids(Arrays.asList(abstractEstimateId)).build();
                    List<AbstractEstimate> abstractEstimates = abstractEstimateRepository.search(abstractEstimateSearchContract);
                    if(!abstractEstimates.isEmpty()) {
                        AbstractEstimate abstractEstimate = abstractEstimates.get(0);
                        if(!abstractEstimate.getDetailedEstimateCreated()) {
                            String estimateNumber = idGenerationRepository
                                    .generateDetailedEstimateNumber(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo());
                            detailedEstimate.setEstimateNumber(propertiesManager.getDetailedEstimateNumberPrefix() + estimateNumber);
                        }
                    }
                }

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
            if (detailedEstimate.getSpillOverFlag() != null && detailedEstimate.getSpillOverFlag())
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
                    		CommonConstants.CODE,estimateOverhead.getOverhead().getCode(), detailedEstimate.getTenantId(), requestInfo,
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

    public void validateActivities(final DetailedEstimate detailedEstimate, Map<String,String> messages, final RequestInfo requestInfo) {
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

            if(activity.getScheduleOfRate() != null && StringUtils.isNotBlank(activity.getScheduleOfRate().getId())) {
                List<ScheduleOfRate> scheduleOfRates =  worksMastersRepository.searchScheduleOfRates(activity.getTenantId(), Arrays.asList(activity.getScheduleOfRate().getId()), requestInfo);
                if(scheduleOfRates != null && scheduleOfRates.isEmpty())
                    messages.put(KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_CODE_INVALID, MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_CODE_INVALID);
            }

        }

    }

    public void validateLocationDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String,String> messages) {
        if (propertiesManager.getLocationRequiredForEstimate().toString().equalsIgnoreCase("Yes")) {
            JSONArray mdmsArray = estimateUtils.getMDMSData(APPCONFIGURATION_OBJECT, CommonConstants.CODE, GIS_INTEGRATION_APPCONFIG,
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

        JSONArray mdmsArray = estimateUtils.getMDMSData(APPCONFIGURATION_OBJECT, CommonConstants.CODE,ASSET_DETAILES_REQUIRED_APPCONFIG,
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
                //TODO FIX aset code validation
               /* else {
                    List<Asset> assets = assetRepository.searchAssets(assetsForEstimate.getTenantId(),assetsForEstimate.getAsset().getCode(),requestInfo);
                    if(assets != null && assets.isEmpty())
                        messages.put(KEY_WORKS_ESTIMATE_ASSET_CODE_INVALID, MESSAGE_WORKS_ESTIMATE_ASSET_CODE_INVALID);
                }*/
                if (asset != null && asset.getCode().equals(assetsForEstimate.getAsset().getCode()))
                  messages.put(KEY_DUPLICATE_ESTIMATE_ASSET_DETAILS, MESSAGE_DUPLICATE_ESTIMATE_ASSET_DETAILS);
            }
    }

    public void validateDetailedEstimates(DetailedEstimateRequest detailedEstimateRequest) {
        final RequestInfo requestInfo = detailedEstimateRequest.getRequestInfo();
        Map<String, String> messages = new HashMap<>();
        for(DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            //validateSpillOverEstimate(detailedEstimate,messages);
            validateActivities(detailedEstimate, messages, requestInfo);
            validateLocationDetails(detailedEstimate,requestInfo, messages);
            validateAssetDetails(detailedEstimate, requestInfo, messages);
            validateMultiYearEstimates(detailedEstimate, messages);
            validateOverheads(detailedEstimate, requestInfo, messages);
            validateMasterData(detailedEstimate, requestInfo, messages);
        }
        if(messages != null && !messages.isEmpty())
             throw new CustomException(messages);
    }

    /*public void validateTechnicalSanctionDetail(final DetailedEstimate detailedEstimate) {
        if (abstractEstimate.getEstimateTechnicalSanctions() != null
                && abstractEstimate.getEstimateTechnicalSanctions().get(0).getTechnicalSanctionDate() == null)
            errors.reject("error.techdate.notnull", "error.techdate.notnull");
        if (abstractEstimate.getEstimateTechnicalSanctions() != null
                && abstractEstimate.getEstimateTechnicalSanctions().get(0).getTechnicalSanctionDate() != null
                && abstractEstimate.getEstimateTechnicalSanctions().get(0).getTechnicalSanctionDate()
                .before(abstractEstimate.getEstimateDate()))
            errors.reject("error.abstracttechnicalsanctiondate", "error.abstracttechnicalsanctiondate");
        if (abstractEstimate.getEstimateTechnicalSanctions() != null
                && abstractEstimate.getEstimateTechnicalSanctions().get(0).getTechnicalSanctionNumber() == null)
            errors.reject("error.technumber.notnull", "error.technumber.notnull");
        if (abstractEstimate.getEstimateTechnicalSanctions() != null
                && abstractEstimate.getEstimateTechnicalSanctions().get(0).getTechnicalSanctionNumber() != null) {
            final AbstractEstimate esistingAbstractEstimate = abstractEstimateRepository
                    .findByEstimateTechnicalSanctionsIgnoreCase_TechnicalSanctionNumberAndEgwStatus_CodeNot(
                            abstractEstimate.getEstimateTechnicalSanctions().get(0).getTechnicalSanctionNumber(),
                            EstimateStatus.CANCELLED.toString());
            if (esistingAbstractEstimate != null)
                errors.reject("error.technumber.unique", "error.technumber.unique");
        }
        if (abstractEstimate.getEstimateDate() == null)
            errors.reject("errors.abbstractestimate.estimatedate", "errors.abbstractestimate.estimatedate");
        if (abstractEstimate.getLineEstimateDetails() != null && abstractEstimate.getEstimateDate() != null
                && abstractEstimate.getEstimateDate()
                .before(abstractEstimate.getLineEstimateDetails().getLineEstimate().getAdminSanctionDate()))
            errors.reject("error.abstractadminsanctiondatele", "error.abstractadminsanctiondatele");
    }*/

    private void validateMasterData(DetailedEstimate detailedEstimate, RequestInfo requestInfo, Map<String,String> messages) {
        JSONArray responseJSONArray = null;

        if (detailedEstimate.getFund() != null && StringUtils.isNotBlank(detailedEstimate.getFund().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(FUND_OBJECT,
            		CommonConstants.CODE,detailedEstimate.getFund().getCode(), detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_FUND_CODE_INVALID, MESSAGE_ESTIMATE_FUND_CODE_INVALID);
            }
        }
        if (detailedEstimate.getFunction() != null
                && StringUtils.isNotBlank(detailedEstimate.getFunction().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(FUNCTION_OBJECT,
            		CommonConstants.CODE,detailedEstimate.getFunction().getCode(), detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_FUNCTION_CODE_INVALID, MESSAGE_ESTIMATE_FUNCTION_CODE_INVALID);
            }
        }

        if (detailedEstimate.getWorksType() != null
                && StringUtils.isNotBlank(detailedEstimate.getWorksType().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(TYPEOFWORK_OBJECT,
            		CommonConstants.CODE,detailedEstimate.getWorksType().getCode(), detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_TYPEOFWORK_CODE_INVALID, MESSAGE_ESTIMATE_TYPEOFWORK_CODE_INVALID);
            }
        }
        if (detailedEstimate.getWorksSubtype() != null
                && StringUtils.isNotBlank(detailedEstimate.getWorksSubtype().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(TYPEOFWORK_OBJECT,
            		CommonConstants.CODE,detailedEstimate.getWorksSubtype().getCode(), detailedEstimate.getTenantId(), requestInfo,
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
            		CommonConstants.CODE,detailedEstimate.getScheme().getCode(), detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_SCHEME_CODE_INVALID, MESSAGE_ESTIMATE_SCHEME_CODE_INVALID);
            }
        }

        if (detailedEstimate.getSubScheme() != null
                & StringUtils.isNotBlank(detailedEstimate.getSubScheme().getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(SUBSCHEME_OBJECT,
            		CommonConstants.CODE,detailedEstimate.getSubScheme().getCode(),  detailedEstimate.getTenantId(), requestInfo,
                    WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(KEY_ESTIMATE_SUBSCHEME_CODE_INVALID, MESSAGE_ESTIMATE_SUBSCHEME_CODE_INVALID);
            }
        }

        if (detailedEstimate.getBudgetGroup() != null
                & StringUtils.isNotBlank(detailedEstimate.getBudgetGroup().getName())) {
            responseJSONArray = estimateUtils.getMDMSData(BUDGETGROUP_OBJECT,CommonConstants.NAME,
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
	

