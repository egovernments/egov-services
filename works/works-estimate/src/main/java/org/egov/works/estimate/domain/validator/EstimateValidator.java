package org.egov.works.estimate.domain.validator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.domain.repository.AbstractEstimateRepository;
import org.egov.works.estimate.domain.service.AbstractEstimateService;
import org.egov.works.estimate.domain.service.DetailedEstimateService;
import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.persistence.repository.*;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;

import static org.egov.works.estimate.config.Constants.*;
import static org.egov.works.estimate.config.Constants.KEY_DUPLICATE_ESTIMATE_ASSET_DETAILS;
import static org.egov.works.estimate.config.Constants.MESSAGE_DUPLICATE_ESTIMATE_ASSET_DETAILS;

@Service
public class EstimateValidator {

    @Autowired
    private EstimateUtils estimateUtils;

    @Autowired
    private AbstractEstimateService abstractEstimateService;

    @Autowired
    private WorksMastersRepository worksMastersRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private AbstractEstimateDetailsJdbcRepository abstractEstimateDetailsJdbcRepository;

    @Autowired
    private AbstractEstimateRepository abstractEstimateRepository;

    @Autowired
    private EstimateTechnicalSanctionRepository estimateTechnicalSanctionRepository;

    @Autowired
    private DetailedEstimateJdbcRepository detailedEstimateJdbcRepository;

    public void validateEstimates(AbstractEstimateRequest abstractEstimateRequest, Boolean isNew) {
        Map<String, String> messages = new HashMap<>();
        for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
            validateSpillOverData(estimate, messages);
            validateMasterData(estimate, abstractEstimateRequest.getRequestInfo(), messages);

            AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
            if (estimate.getId() != null)
                searchContract.setIds(Arrays.asList(estimate.getId()));
            searchContract.setAbstractEstimateNumbers(Arrays.asList(estimate.getAbstractEstimateNumber()));
            searchContract.setTenantId(estimate.getTenantId());
            List<AbstractEstimate> oldEstimates = abstractEstimateService
                    .search(searchContract, abstractEstimateRequest.getRequestInfo()).getAbstractEstimates();
            if (isNew && !oldEstimates.isEmpty())
                messages.put(Constants.KEY_UNIQUE_ABSTRACTESTIMATENUMBER,
                        Constants.MESSAGE_UNIQUE_ABSTRACTESTIMATENUMBER);
            searchContract.setAbstractEstimateNumbers(null);
            if (!isNew && estimate.getWorkFlowDetails() != null && Constants.APPROVE
                    .equalsIgnoreCase(estimate.getWorkFlowDetails().getAction())) {
                if (StringUtils.isBlank(estimate.getAdminSanctionNumber()))
                    messages.put(Constants.KEY_NULL_ADMINSANCTIONNUMBER,
                            Constants.MESSAGE_NULL_ADMINSANCTIONNUMBER);
                searchContract.setAdminSanctionNumbers(Arrays.asList(estimate.getAdminSanctionNumber()));
                oldEstimates = abstractEstimateService.search(searchContract, abstractEstimateRequest.getRequestInfo())
                        .getAbstractEstimates();
                if (!oldEstimates.isEmpty() && !estimate.getId().equalsIgnoreCase(oldEstimates.get(0).getId()))
                    messages.put(Constants.KEY_UNIQUE_ADMINSANCTIONNUMBER,
                            Constants.MESSAGE_UNIQUE_ADMINSANCTIONNUMBER);
            }
            validateEstimateDetails(estimate, messages);

            if (messages != null && !messages.isEmpty())
                throw new CustomException(messages);
        }
    }

    private void validateSpillOverData(AbstractEstimate estimate, Map<String, String> messages) {
        if (estimate.getSpillOverFlag() && estimate.getAbstractEstimateNumber() == null)
            messages.put(Constants.KEY_NULL_ABSTRACTESTIMATE_NUMBER,
                    Constants.MESSAGE_NULL_ABSTRACTESTIMATE_NUMBER);
        if (!estimate.getSpillOverFlag() && estimate.getDateOfProposal() != null
                && estimate.getDateOfProposal() > new Date().getTime())
            messages.put(Constants.KEY_FUTUREDATE_DATEOFPROPOSAL,
                    Constants.MESSAGE_FUTUREDATE_DATEOFPROPOSAL);
    }

    private void validateEstimateDetails(AbstractEstimate estimate, Map<String, String> messages) {
        BigDecimal estimateAmount = BigDecimal.ZERO;
        for (final AbstractEstimateDetails aed : estimate.getAbstractEstimateDetails())
            estimateAmount = estimateAmount.add(aed.getEstimateAmount());
        if (estimateAmount.compareTo(BigDecimal.ZERO) == -1)
            messages.put(Constants.KEY_INVALID_ESTIMATEAMOUNT,
                    Constants.MESSAGE_INVALID_ESTIMATEAMOUNT);
    }

    public void validateMasterData(AbstractEstimate abstractEstimate, RequestInfo requestInfo,
                                   Map<String, String> messages) {

        Boolean isFinIntReq = false;
        JSONArray responseJSONArray = estimateUtils.getMDMSData(Constants.APPCONFIGURATION_OBJECT,
                CommonConstants.CODE, Constants.FINANCIAL_INTEGRATION_KEY,
                abstractEstimate.getTenantId(), requestInfo, Constants.WORKS_MODULE_CODE);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                isFinIntReq = true;
        }

        if (isFinIntReq)
            validateFinancialDetails(abstractEstimate, requestInfo, messages);

        validateTypeOfWork(abstractEstimate.getTypeOfWork(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateSubTypeOfWork(abstractEstimate.getSubTypeOfWork(), abstractEstimate.getTenantId(), requestInfo,
                messages);
        validateDepartment(abstractEstimate.getDepartment(), abstractEstimate.getTenantId(), requestInfo, messages);

		/*
		 * * if(abstractEstimate.getWard() != null &
		 * StringUtils.isNotBlank(abstractEstimate.getWard().getCode())) {
		 * responseJSONArray =
		 * estimateUtils.getAppConfigurationData(WorksEstimateServiceConstants.
		 * DEPARTMENT_OBJECT,abstractEstimate.getWard().getCode(),
		 * abstractEstimate.getTenantId(),requestInfo); if(responseJSONArray !=
		 * null && responseJSONArray.isEmpty()) { throw new
		 * InvalidDataException("Boundary",
		 * "Invalid data for estimate Ward boundary",
		 * abstractEstimate.getWard().getCode()); } }
		 * if(abstractEstimate.getLocality() != null &
		 * StringUtils.isNotBlank(abstractEstimate.getLocality().getCode())) {
		 * responseJSONArray =
		 * estimateUtils.getAppConfigurationData(WorksEstimateServiceConstants.
		 * DEPARTMENT_OBJECT,abstractEstimate.getLocality().getCode(),
		 * abstractEstimate.getTenantId(),requestInfo); if(responseJSONArray !=
		 * null && responseJSONArray.isEmpty()) { throw new
		 * InvalidDataException("Boundary",
		 * "Invalid data for estimate locality boundary",
		 * abstractEstimate.getLocality().getCode()); } }
		 */

    }


    private void validateFinancialDetails(AbstractEstimate abstractEstimate, RequestInfo requestInfo,
                                          Map<String, String> messages) {
        validateFund(abstractEstimate.getFund(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateFunction(abstractEstimate.getFunction(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateScheme(abstractEstimate.getScheme(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateSubScheme(abstractEstimate.getSubScheme(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateBudgetGroup(abstractEstimate.getBudgetGroup(), abstractEstimate.getTenantId(), requestInfo, messages);
    }

    public void validateFund(Fund fund, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (fund != null && fund.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.FUND_OBJECT,
                    CommonConstants.CODE, fund.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_FUND_INVALID,
                        Constants.MESSAGE_FUND_INVALID);
            }
        } else
            messages.put(Constants.KEY_FUND_REQUIRED,
                    Constants.MESSAGE_FUND_REQUIRED);

    }

    public void validateFunction(Function function, String tenantId, RequestInfo requestInfo,
                                 Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (function != null && function.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.FUNCTION_OBJECT,
                    CommonConstants.CODE, function.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_FUNCTION_INVALID,
                        Constants.MESSAGE_FUNCTION_INVALID);
            } else
               messages.put(Constants.KEY_FUNCTION_REQUIRED,
                    Constants.MESSAGE_FUNCTION_REQUIRED);
        }
    }

    public void validateScheme(Scheme scheme, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (scheme != null && scheme.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.SCHEME_OBJECT,
                    CommonConstants.CODE, scheme.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_SCHEME_INVALID,
                        Constants.MESSAGE_SCHEME_INVALID);
            } else
                messages.put(Constants.KEY_SCHEME_REQUIRED,
                        Constants.MESSAGE_SCHEME_REQUIRED);
        }
    }

    public void validateSubScheme(SubScheme subScheme, String tenantId, RequestInfo requestInfo,
                                  Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (subScheme != null && subScheme.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.SUBSCHEME_OBJECT,
                    CommonConstants.CODE, subScheme.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_SUBSCHEME_INVALID,
                        Constants.MESSAGE_SUBSCHEME_INVALID);
            } else
                messages.put(Constants.KEY_SUBSCHEME_REQUIRED,
                        Constants.MESSAGE_SUBSCHEME_REQUIRED);
        }
    }

    public void validateBudgetGroup(BudgetGroup budgetGroup, String tenantId, RequestInfo requestInfo,
                                    Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (budgetGroup != null && budgetGroup.getName() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.BUDGETGROUP_OBJECT,
                    CommonConstants.NAME, budgetGroup.getName(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_BUDGETGROUP_INVALID,
                        Constants.MESSAGE_BUDGETGROUP_INVALID);
            } else
                messages.put(Constants.KEY_BUDGETGROUP_NAME_REQUIRED,
                        Constants.MESSAGE_UDGETGROUP_NAME_REQUIRED);
        }
    }

    public void validateTypeOfWork(TypeOfWork typeOfWork, String tenantId, RequestInfo requestInfo,
                                   Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (typeOfWork != null && typeOfWork.getName() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.TYPEOFWORK_OBJECT,
                    CommonConstants.CODE, typeOfWork.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_TYPEOFWORK_INVALID,
                        Constants.MESSAGE_TYPEOFWORK_INVALID);
            } else
                messages.put(Constants.KEY_TYPEOFWORK_REQUIRED,
                        Constants.MESSAGE_TYPEOFWORK_REQUIRED);
        }
    }

    public void validateSubTypeOfWork(TypeOfWork subTypeOfWork, String tenantId, RequestInfo requestInfo,
                                      Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (subTypeOfWork != null && subTypeOfWork.getName() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.TYPEOFWORK_OBJECT,
                    CommonConstants.CODE, subTypeOfWork.getCode(), tenantId, requestInfo,
                    Constants.WORKS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_SUBTYPEOFWORK_INVALID,
                        Constants.MESSAGE_SUBTYPEOFWORK_INVALID);
            } else
                messages.put(Constants.KEY_DEPARTMENT_CODE_REQUIRED,
                        Constants.MESSAGE_DEPARTMENT_CODE_REQUIRED);
        }
    }

    public void validateDepartment(Department department, String tenantId, RequestInfo requestInfo,
                                   Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (department != null && department.getName() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.DEPARTMENT_OBJECT,
                    CommonConstants.CODE, department.getCode(), tenantId, requestInfo,
                    Constants.COMMON_MASTERS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_DEPARTMENT_INVALID,
                        Constants.MESSAGE_DEPARTMENT_INVALID);
            } else
                messages.put(Constants.KEY_SUBTYPEOFWORK_REQUIRED,
                        Constants.MESSAGE_SUBTYPEOFWORK_REQUIRED);
        }
    }

    public void validateDetailedEstimates(DetailedEstimateRequest detailedEstimateRequest) {
        final RequestInfo requestInfo = detailedEstimateRequest.getRequestInfo();
        Map<String, String> messages = new HashMap<>();
        for (DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            validateSpillOverEstimate(detailedEstimate, messages);
            validateActivities(detailedEstimate, messages, requestInfo);
            validateLocationDetails(detailedEstimate, requestInfo, messages);
            validateAssetDetails(detailedEstimate, requestInfo, messages);
            validateOverheads(detailedEstimate, requestInfo, messages);
            validateMasterData(detailedEstimate, requestInfo, messages);
        }
        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    public void validateSpillOverEstimate(final DetailedEstimate detailedEstimate, Map<String, String> messages) {
        if(checkDetailedEstimateCreatedFlag(detailedEstimate)) {
            if(StringUtils.isBlank(detailedEstimate.getEstimateNumber()))
                messages.put(Constants.KEY_NULL_DETAILEDESTIMATE_NUMBER,
                        Constants.MESSAGE_NULL_DETAILEDESTIMATE_NUMBER);
            else
               validateEstimateNumberUnique(detailedEstimate, messages);
            if(detailedEstimate.getEstimateDate() == null)
                messages.put(Constants.KEY_NULL_DETAILEDESTIMATE_DATE,
                        Constants.MESSAGE_NULL_DETAILEDESTIMATE_DATE);
            else if(detailedEstimate.getEstimateDate() > new Date().getTime())
                messages.put(Constants.KEY_FUTUREDATE_ESTIMATEDATE_SPILLOVER,
                        Constants.MESSAGE_FUTUREDATE_ESTIMATEDATE_SPILLOVER);
            validateTechnicalSanctionDetail(detailedEstimate, messages);
        }
    }

    private void validateEstimateNumberUnique(DetailedEstimate detailedEstimate, Map<String, String> messages) {
        DetailedEstimateSearchContract detailedEstimateSearchContract = DetailedEstimateSearchContract.builder()
                .tenantId(detailedEstimate.getTenantId()).estimateAmount(detailedEstimate.getEstimateNumber()).build();
        List<DetailedEstimateHelper> lists= detailedEstimateJdbcRepository.search(detailedEstimateSearchContract);
        if(lists != null && !lists.isEmpty())
            messages.put(Constants.KEY_INVALID_ESTIMATNUMBER_SPILLOVER,
                    Constants.MESSAGE_INVALID_ESTIMATNUMBER_SPILLOVER);
    }

    public void validateOverheads(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String, String> messages) {
        for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {

            JSONArray responseJSONArray = null;
            if (estimateOverhead != null) {
                if (estimateOverhead.getOverhead() != null && StringUtils.isNotBlank(estimateOverhead.getOverhead().getCode())) {
                    responseJSONArray = estimateUtils.getMDMSData(OVERHEAD_OBJECT,
                            CommonConstants.CODE, estimateOverhead.getOverhead().getCode(), detailedEstimate.getTenantId(), requestInfo,
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

    public void validateActivities(final DetailedEstimate detailedEstimate, Map<String, String> messages, final RequestInfo requestInfo) {
        for (int i = 0; i < detailedEstimate.getEstimateActivities().size(); i++)
            for (int j = i + 1; j < detailedEstimate.getEstimateActivities().size(); j++)
                if (detailedEstimate.getEstimateActivities().get(i).getScheduleOfRate() != null
                        && detailedEstimate.getEstimateActivities().get(i).getScheduleOfRate().getId()
                        .equals(detailedEstimate.getEstimateActivities().get(j).getScheduleOfRate().getId())) {
                    messages.put(KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE, MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE);
                }
        if (detailedEstimate.getEstimateActivities() == null || detailedEstimate.getEstimateActivities().isEmpty())
            messages.put(KEY_ESTIMATE_ACTIVITY_REQUIRED, MESSAGE_ESTIMATE_ACTIVITY_REQUIRED);

        for (final EstimateActivity activity : detailedEstimate.getEstimateActivities()) {
            if ((activity.getScheduleOfRate() != null && activity.getScheduleOfRate().getId() == null) || (activity.getNonSor() != null && activity.getNonSor().getId() == null))
                messages.put(KEY_ESTIMATE_ACTIVITY_REQUIRED, MESSAGE_ESTIMATE_ACTIVITY_REQUIRED);

            if (activity.getScheduleOfRate() == null && activity.getNonSor() == null)
                messages.put(KEY_ESTIMATE_ACTIVITY_INVALID, MESSAGE_ESTIMATE_ACTIVITY_INVALID);

            if (activity.getQuantity() != null && activity.getQuantity() <= 0)
                messages.put(KEY_ESTIMATE_ACTIVITY_QUANTITY, MESSAGE_ESTIMATE_ACTIVITY_QUANTITY);

             if (activity.getEstimateRate().compareTo(BigDecimal.ZERO) <= 0)
                messages.put(KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE, MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE);

            validateUOM(activity.getUom(), detailedEstimate.getTenantId(), requestInfo,messages);

           if (activity.getUnitRate().compareTo(BigDecimal.ZERO) <= 0)
                messages.put(KEY_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID, MESSAGE_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID);

            if (activity.getEstimateMeasurementSheets() != null)
                for (final EstimateMeasurementSheet estimateMeasurementSheet : activity.getEstimateMeasurementSheets()) {
                    if (estimateMeasurementSheet.getQuantity() == null)
                        messages.put(KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED, MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED);
                    else if (estimateMeasurementSheet.getQuantity().compareTo(BigDecimal.ZERO) <= 0)
                        messages.put(KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID, MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID);
                }

            if (activity.getScheduleOfRate() != null && StringUtils.isNotBlank(activity.getScheduleOfRate().getId())) {
                List<ScheduleOfRate> scheduleOfRates = worksMastersRepository.searchScheduleOfRates(activity.getTenantId(), Arrays.asList(activity.getScheduleOfRate().getId()), requestInfo);
                if (scheduleOfRates != null && scheduleOfRates.isEmpty())
                    messages.put(KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_CODE_INVALID, MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_CODE_INVALID);
            }

        }

    }

    private void validateUOM(final UOM uom, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (uom != null && uom.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.UOM_OBJECT,
                    CommonConstants.CODE, uom.getCode(), tenantId, requestInfo,
                    Constants.COMMON_MASTERS_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_UOM_INVALID,
                        Constants.MESSAGE_UOM_INVALID);
            }
        }
    }

    public void validateLocationDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String, String> messages) {
        if (propertiesManager.getLocationRequiredForEstimate().toString().equalsIgnoreCase("Yes")) {
            JSONArray mdmsArray = estimateUtils.getMDMSData(APPCONFIGURATION_OBJECT, CommonConstants.CODE, GIS_INTEGRATION_APPCONFIG,
                    detailedEstimate.getTenantId(), requestInfo, WORKS_MODULE_CODE);
            if (mdmsArray != null && !mdmsArray.isEmpty()) {
                Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
                if (jsonMap.get("value").equals("Yes") && (StringUtils.isBlank(detailedEstimate.getLocation())
                        || detailedEstimate.getLatitude() == null || detailedEstimate.getLongitude() == null))
                    messages.put(KEY_ESTIMATE_LOCATION_REQUIRED, MESSAGE_ESTIMATE_LOCATION_REQUIRED);
            }
        }
    }

    public void validateAssetDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo, Map<String, String> messages) {

        JSONArray mdmsArray = estimateUtils.getMDMSData(APPCONFIGURATION_OBJECT, CommonConstants.CODE, ASSET_DETAILES_REQUIRED_APPCONFIG,
                detailedEstimate.getTenantId(), requestInfo, WORKS_MODULE_CODE);
        boolean assetsAdded = false;
        if (mdmsArray != null && !mdmsArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                if(detailedEstimate.getAssets() != null && detailedEstimate.getAssets().isEmpty())
                    assetsAdded = true;

                for(AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets())
                  if(!assetsAdded && StringUtils.isBlank(assetsForEstimate.getLandAsset()))
                    messages.put(KEY_ESTIMATE_ASSET_DETAILS_REQUIRED, MESSAGE_ESTIMATE_ASSET_DETAILS_REQUIRED);
        }

        Asset asset = null;
        for (final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets())
            if (assetsForEstimate != null) {
                if (StringUtils.isBlank(detailedEstimate.getNameOfWork()) &&
                        (detailedEstimate.getNameOfWork().equalsIgnoreCase(Constants.ESTIMATE_NAMEOFWORK_ADDITION) ||
                                detailedEstimate.getNameOfWork().equalsIgnoreCase(Constants.ESTIMATE_NAMEOFWORK_REPAIRS))
                        && StringUtils.isBlank(assetsForEstimate.getAsset().getCode()))
                    messages.put(KEY_ESTIMATE_ASSET_REQUIRED, MESSAGE_ESTIMATE_ASSET_REQUIRED);

                if(StringUtils.isBlank(detailedEstimate.getNameOfWork()) && detailedEstimate.getNameOfWork().equalsIgnoreCase(Constants.ESTIMATE_NAMEOFWORK_NEW)
                  && StringUtils.isBlank(assetsForEstimate.getLandAsset()))
                    messages.put(KEY_ESTIMATE_LAND_ASSET_REQUIRED, MESSAGE_ESTIMATE_LAND_ASSET_REQUIRED);
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


    public void validateTechnicalSanctionDetail(final DetailedEstimate detailedEstimate, Map<String,String> messages) {
        if(detailedEstimate.getEstimateTechnicalSanctions() == null ||
                detailedEstimate.getEstimateTechnicalSanctions() != null && detailedEstimate.getEstimateTechnicalSanctions().isEmpty()) {
            messages.put(KEY_ESTIMATE_TECHNICALSANCTION_DETAILS_REQUIRED, MESSAGE_ESTIMATE_TECHNICALSANCTION_DETAILS_REQUIRED);
        }

        if(detailedEstimate.getEstimateTechnicalSanctions() != null )
            for(EstimateTechnicalSanction estimateTechnicalSanction : detailedEstimate.getEstimateTechnicalSanctions()) {

                if(StringUtils.isNotBlank(estimateTechnicalSanction.getTechnicalSanctionNumber()))
                   validateUniqueTechnicalSanctionForDetailedEstimate(detailedEstimate, estimateTechnicalSanction, messages);

                if(estimateTechnicalSanction.getTechnicalSanctionDate() != null && estimateTechnicalSanction.getTechnicalSanctionDate() > new Date().getTime())
                    messages.put(KEY_TECHNICAL_SANCTION_DATE_FUTUREDATE, MESSAGE_TECHNICAL_SANCTION_DATE_FUTUREDATE);

                if(detailedEstimate.getEstimateDate() != null && estimateTechnicalSanction.getTechnicalSanctionDate() < detailedEstimate.getEstimateDate())
                    messages.put(KEY_INVALID_TECHNICALSANCTION_DATE, MESSAGE_INVALID_TECHNICALSANCTION_DATE);

                if(detailedEstimate.getAbstractEstimateDetail() != null && detailedEstimate.getEstimateDate() != null) {
                    AbstractEstimateDetailsSearchContract searchContract = AbstractEstimateDetailsSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                            .ids(Arrays.asList(detailedEstimate.getAbstractEstimateDetail().getId())).build();
                    List<AbstractEstimateDetails> abstractEstimateDetails = abstractEstimateDetailsJdbcRepository.search(searchContract);
                    if (!abstractEstimateDetails.isEmpty()) {
                        String abstractEstimateId = abstractEstimateDetails.get(0).getAbstractEstimate();
                        AbstractEstimateSearchContract abstractEstimateSearchContract = AbstractEstimateSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                                .ids(Arrays.asList(abstractEstimateId)).build();
                        List<AbstractEstimate> abstractEstimates = abstractEstimateRepository.search(abstractEstimateSearchContract);
                        if (!abstractEstimates.isEmpty() && abstractEstimates.get(0).getAdminSanctionDate() != null &&
                                detailedEstimate.getEstimateDate() < abstractEstimates.get(0).getAdminSanctionDate()) {
                            messages.put(KEY_INVALID_ADMINSANCTION_DATE, MESSAGE_INVALID_ADMINSANCTION_DATE);
                        }

                    }
                }
            }

    }

    private void validateUniqueTechnicalSanctionForDetailedEstimate(DetailedEstimate detailedEstimate, EstimateTechnicalSanction estimateTechnicalSanction, Map<String, String> messages) {
        TechnicalSanctionSearchContract technicalSanctionSearchContract = TechnicalSanctionSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                .technicalSanctionNumbers(Arrays.asList(estimateTechnicalSanction.getTechnicalSanctionNumber())).build();
        List<EstimateTechnicalSanction> technicalSanctions = estimateTechnicalSanctionRepository.search(technicalSanctionSearchContract);
        if(technicalSanctions != null && !technicalSanctions.isEmpty())
            messages.put(KEY_INVALID_ADMINSANCTION_DATE, MESSAGE_INVALID_ADMINSANCTION_DATE);
    }

    private void validateMasterData(DetailedEstimate detailedEstimate, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray = null;

        responseJSONArray = estimateUtils.getMDMSData(Constants.APPCONFIGURATION_OBJECT,
                CommonConstants.CODE, Constants.FINANCIAL_INTEGRATION_KEY,
                detailedEstimate.getTenantId(), requestInfo, Constants.WORKS_MODULE_CODE);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                validateFinancialDetailsForDetailedEstmate(detailedEstimate, requestInfo, messages);
        }

        validateTypeOfWork(detailedEstimate.getWorksType(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateSubTypeOfWork(detailedEstimate.getWorksSubtype(), detailedEstimate.getTenantId(), requestInfo,
                messages);
        validateDepartment(detailedEstimate.getDepartment(), detailedEstimate.getTenantId(), requestInfo, messages);
    }

    private void validateFinancialDetailsForDetailedEstmate(DetailedEstimate detailedEstimate, RequestInfo requestInfo,
                                                            Map<String, String> messages) {
        validateFund(detailedEstimate.getFund(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateFunction(detailedEstimate.getFunction(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateScheme(detailedEstimate.getScheme(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateSubScheme(detailedEstimate.getSubScheme(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateBudgetGroup(detailedEstimate.getBudgetGroup(), detailedEstimate.getTenantId(), requestInfo, messages);
    }

    public boolean checkDetailedEstimateCreatedFlag(final DetailedEstimate detailedEstimate) {
        boolean isDetailedEstimateCreated = false;
        AbstractEstimateDetailsSearchContract searchContract = AbstractEstimateDetailsSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                .ids(Arrays.asList(detailedEstimate.getAbstractEstimateDetail().getId())).build();
        List<AbstractEstimateDetails> abstractEstimateDetails = abstractEstimateDetailsJdbcRepository.search(searchContract);
        if (!abstractEstimateDetails.isEmpty()) {
            String abstractEstimateId = abstractEstimateDetails.get(0).getAbstractEstimate();
            AbstractEstimateSearchContract abstractEstimateSearchContract = AbstractEstimateSearchContract.builder().tenantId(detailedEstimate.getTenantId())
                    .ids(Arrays.asList(abstractEstimateId)).build();
            List<AbstractEstimate> abstractEstimates = abstractEstimateRepository.search(abstractEstimateSearchContract);
            if (!abstractEstimates.isEmpty()) {
                AbstractEstimate abstractEstimate = abstractEstimates.get(0);
                isDetailedEstimateCreated = abstractEstimate.getDetailedEstimateCreated();
            }

        }
        return isDetailedEstimateCreated;
    }
}

