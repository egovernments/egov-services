package org.egov.works.estimate.domain.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.estimate.config.Constants;
import org.egov.works.estimate.config.PropertiesManager;
import org.egov.works.estimate.domain.repository.AbstractEstimateRepository;
import org.egov.works.estimate.domain.service.AbstractEstimateService;
import org.egov.works.estimate.persistence.helper.DetailedEstimateHelper;
import org.egov.works.estimate.persistence.repository.AssetRepository;
import org.egov.works.estimate.persistence.repository.BoundaryRepository;
import org.egov.works.estimate.persistence.repository.DetailedEstimateJdbcRepository;
import org.egov.works.estimate.persistence.repository.EstimateTechnicalSanctionRepository;
import org.egov.works.estimate.persistence.repository.FileStoreRepository;
import org.egov.works.estimate.persistence.repository.WorksMastersRepository;
import org.egov.works.estimate.utils.EstimateUtils;
import org.egov.works.estimate.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;

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
    private AbstractEstimateRepository abstractEstimateRepository;

    @Autowired
    private EstimateTechnicalSanctionRepository estimateTechnicalSanctionRepository;

    @Autowired
    private DetailedEstimateJdbcRepository detailedEstimateJdbcRepository;

    @Autowired
    private FileStoreRepository fileStoreRepository;

    @Autowired
    private BoundaryRepository boundaryRepository;

    public void validateEstimates(AbstractEstimateRequest abstractEstimateRequest, Boolean isNew) {
        Map<String, String> messages = new HashMap<>();
        if (abstractEstimateRequest.getAbstractEstimates().size() > 1) {
            validateDuplicateAbstractEstimateNumber(messages, abstractEstimateRequest);
        }
        for (final AbstractEstimate estimate : abstractEstimateRequest.getAbstractEstimates()) {
            validateEstimateDetails(estimate, messages);
            validatePMCData(messages, estimate, abstractEstimateRequest.getRequestInfo());
            if (!messages.isEmpty())
                throw new CustomException(messages);
            validateWardAndLocalityMandatory(messages, estimate);
            if (!estimate.getSpillOverFlag())
               validateDpRemarks(messages, estimate);
            if (estimate.getSpillOverFlag())
                validateSpillOverData(estimate, messages);
            validateMasterData(estimate, abstractEstimateRequest.getRequestInfo(), messages, isNew);
            if (!messages.isEmpty())
                throw new CustomException(messages);

            if (estimate.getSpillOverFlag())
                validateDuplicateWINCode(messages, estimate);

            if (!messages.isEmpty())
                throw new CustomException(messages);
            if (StringUtils.isNotBlank(estimate.getAbstractEstimateNumber()))
                validateAbstractEstimateNumber(abstractEstimateRequest.getRequestInfo(), isNew, messages, estimate);
            if (StringUtils.isNotBlank(estimate.getAdminSanctionNumber()))
                validateAdminSanctionDetails(abstractEstimateRequest.getRequestInfo(), isNew, messages, estimate);
            validateEstimateAssetDetails(estimate, abstractEstimateRequest.getRequestInfo(), messages);
            validateCouncilSanctionDetails(abstractEstimateRequest.getRequestInfo(), isNew, messages, estimate);
            if (estimate.getId() != null) {
                validateIsModified(estimate, abstractEstimateRequest.getRequestInfo(), messages);
                validateStatus(estimate.getStatus(), estimate.getTenantId(), abstractEstimateRequest.getRequestInfo(), messages, CommonConstants.ABSTRACT_ESTIMATE_BUSINESSKEY);
            }
            if (!messages.isEmpty())
                throw new CustomException(messages);
        }
    }


    private void validateDpRemarks(Map<String, String> messages, AbstractEstimate estimate) {
        if(StringUtils.isBlank(estimate.getDpRemarks()))
            messages.put(Constants.KEY_WORKS_ESTIMATE_DPREMARKS_REQUIRED,
                    Constants.MESSAGE_WORKS_ESTIMATE_DPREMARKS_REQUIRED);
        if(!estimate.getWorkProposedAsPerDP())
            messages.put(Constants.KEY_WORKS_ESTIMATE_WORKPROPOSED_AS_PERDPREMARKS_REQUIRED,
                    Constants.MESSAGE_WORKS_ESTIMATE_WORKPROPOSED_AS_PERDPREMARKS_REQUIRED);
    }

    private void validateDuplicateWINCode(Map<String, String> messages, final AbstractEstimate estimate) {
        int size = estimate.getAbstractEstimateDetails().size();
        boolean validProjectCodes = true;
        for (int i = 0; i <= size - 1; i++) {
            for (int j = i + 1; j <= size - 1; j++) {
                if (estimate.getAbstractEstimateDetails().get(i).getProjectCode().getCode()
                        .equalsIgnoreCase(estimate.getAbstractEstimateDetails().get(j).getProjectCode().getCode())) {
                    validProjectCodes = false;
                    break;
                }
            }
        }

        if (!validProjectCodes) {
            messages.put(Constants.KEY_DUPLICATE_WINCODES,
                    Constants.MESSAGE_DUPLICATE_WINCODES);
        }
    }

    private void validateDuplicateAbstractEstimateNumber(Map<String, String> messages,
            final AbstractEstimateRequest abstractEstimateRequest) {
        int size = abstractEstimateRequest.getAbstractEstimates().size();
        boolean validAbstractEstimateNumbers = true;
        for (int i = 0; i <= size - 1; i++) {
            for (int j = i + 1; j <= size - 1; j++) {
                if (abstractEstimateRequest.getAbstractEstimates().get(i).getAbstractEstimateNumber()
                        .equalsIgnoreCase(abstractEstimateRequest.getAbstractEstimates().get(j).getAbstractEstimateNumber())) {
                    validAbstractEstimateNumbers = false;
                    break;
                }
            }
        }

        if (!validAbstractEstimateNumbers) {
            messages.put(Constants.KEY_DUPLICATE_ABSTRACTESTIMATENUMBERS,
                    Constants.MESSAGE_DUPLICATE_ABSTRACTESTIMATENUMBERS);
        }
    }

    private void validateWardAndLocalityMandatory(Map<String, String> messages, final AbstractEstimate estimate) {
        if (estimate.getWard() == null || (estimate.getWard() != null && StringUtils.isBlank(estimate.getWard().getCode()))) {
            messages.put(Constants.KEY_WARDCODE_INVALID, Constants.MESSAGE_WARDCODE_INVALID);
        }
        if (estimate.getLocality() == null
                || (estimate.getLocality() != null && StringUtils.isBlank(estimate.getLocality().getCode()))) {
            messages.put(Constants.KEY_LOCALITYCODE_INVALID, Constants.MESSAGE_LOCALITYCODE_INVALID);
        }
    }

    private void validatePMCData(Map<String, String> messages, final AbstractEstimate estimate, final RequestInfo requestInfo) {
        if (estimate.getPmcRequired() && estimate.getPmcType() == null) {
            messages.put(Constants.KEY_PMCTYPE_INVALID, Constants.MESSAGE_PMCTYPE_INVALID);
        }

        if (estimate.getPmcRequired() && StringUtils.isNotBlank(estimate.getPmcType())
                && estimate.getPmcType().equalsIgnoreCase("Panel")
                && (estimate.getPmcName() == null || (estimate.getPmcName() != null && StringUtils.isBlank(estimate.getPmcName().getCode())))) {
            messages.put(Constants.KEY_PMCNAME_INVALID, Constants.MESSAGE_PMCNAME_INVALID);
        }
        
        if(estimate.getPmcRequired() && StringUtils.isNotBlank(estimate.getPmcType())
                && estimate.getPmcType().equalsIgnoreCase("Panel")
                && estimate.getPmcName() != null && StringUtils.isNotBlank(estimate.getPmcName().getCode())) {
            List<Contractor> contractors = worksMastersRepository.searchContractorByCode(estimate.getTenantId(), estimate.getPmcName().getCode(), requestInfo);
            if(contractors.isEmpty()) {
                messages.put(Constants.KEY_PMC_CONTRACTOR_INVALID, Constants.MESSAGE_PMC_CONTRACTOR_INVALID);
            }
        }
    }

    private void validateAdminSanctionDetails(RequestInfo requestInfo, Boolean isNew, Map<String, String> messages,
            final AbstractEstimate estimate) {
        AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
        searchContract.setAdminSanctionNumbers(Arrays.asList(estimate.getAdminSanctionNumber()));
        List<AbstractEstimate> oldEstimates = abstractEstimateService.search(searchContract, requestInfo)
                .getAbstractEstimates();
        if (!estimate.getSpillOverFlag() && !isNew && estimate.getWorkFlowDetails() != null
                && Constants.APPROVE.equalsIgnoreCase(estimate.getWorkFlowDetails().getAction())) {
            if (StringUtils.isBlank(estimate.getAdminSanctionNumber()))
                messages.put(Constants.KEY_NULL_ADMINSANCTIONNUMBER, Constants.MESSAGE_NULL_ADMINSANCTIONNUMBER);
            if (!oldEstimates.isEmpty() && !estimate.getId().equalsIgnoreCase(oldEstimates.get(0).getId()))
                messages.put(Constants.KEY_UNIQUE_ADMINSANCTIONNUMBER, Constants.MESSAGE_UNIQUE_ADMINSANCTIONNUMBER);
        } else if (estimate.getSpillOverFlag()) {
            if (StringUtils.isBlank(estimate.getAdminSanctionNumber()))
                messages.put(Constants.KEY_NULL_ADMINSANCTIONNUMBER, Constants.MESSAGE_NULL_ADMINSANCTIONNUMBER);
            if (estimate.getAdminSanctionDate() == null)
                messages.put(Constants.KEY_NULL_ADMINSANCTIONDATE, Constants.MESSAGE_NULL_ADMINSANCTIONDATE);
            if (estimate.getAdminSanctionDate() > new Date().getTime())
                messages.put(Constants.KEY_FUTUREDATE_ADMINSANCTIONDATE,
                        Constants.MESSAGE_FUTUREDATE_ADMINSANCTIONDATE);
            if (estimate.getAdminSanctionDate() < estimate.getDateOfProposal())
                messages.put(Constants.KEY_ADMINSANCTION_PROPOSAL_DATE, Constants.MESSAGE_ADMINSANCTION_PROPOSAL_DATE);
            if ((isNew && !oldEstimates.isEmpty()) || (!isNew && !oldEstimates.isEmpty()
                    && !estimate.getId().equalsIgnoreCase(oldEstimates.get(0).getId())))
                messages.put(Constants.KEY_UNIQUE_ADMINSANCTIONNUMBER, Constants.MESSAGE_UNIQUE_ADMINSANCTIONNUMBER);
        }
    }

    private void validateCouncilSanctionDetails(RequestInfo requestInfo, Boolean isNew, Map<String, String> messages,
            final AbstractEstimate estimate) {
        if (estimate.getCouncilResolutionDate() != null && estimate.getCouncilResolutionDate() > new Date().getTime())
            messages.put(Constants.KEY_FUTUREDATE_COUNCILRESOLUTIONDATE,
                    Constants.MESSAGE_FUTUREDATE_COUNCILRESOLUTIONDATE);
        if (estimate.getCouncilResolutionDate() != null && estimate.getCouncilResolutionDate() < estimate.getDateOfProposal())
            messages.put(Constants.KEY_COUNCILRESOLUTION_PROPOSAL_DATE, Constants.MESSAGE_COUNCILRESOLUTION_PROPOSAL_DATE);
        if (estimate.getCouncilResolutionNumber() != null) {
            AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
            searchContract.setCouncilResolutionNumbers(Arrays.asList(estimate.getCouncilResolutionNumber()));

            List<AbstractEstimate> oldEstimates = abstractEstimateService.search(searchContract, requestInfo)
                    .getAbstractEstimates();
            if ((isNew && !oldEstimates.isEmpty())
                    || (!isNew && !oldEstimates.isEmpty()
                            && !estimate.getId().equalsIgnoreCase(oldEstimates.get(0).getId())))
                messages.put(Constants.KEY_UNIQUE_COUNCILRESOLUTIONNUMBER, Constants.MESSAGE_UNIQUE_COUNCILRESOLUTIONNUMBER);
        }

    }

    private void validateAbstractEstimateNumber(RequestInfo requestInfo, Boolean isNew, Map<String, String> messages,
            final AbstractEstimate estimate) {
        AbstractEstimateSearchContract searchContract = new AbstractEstimateSearchContract();
        if (estimate.getId() != null)
            searchContract.setIds(Arrays.asList(estimate.getId()));
        searchContract.setAbstractEstimateNumbers(Arrays.asList(estimate.getAbstractEstimateNumber()));
        searchContract.setTenantId(estimate.getTenantId());
        List<AbstractEstimate> oldEstimates = abstractEstimateService.search(searchContract, requestInfo)
                .getAbstractEstimates();

        if (isNew && !oldEstimates.isEmpty())
            for (AbstractEstimate abstractEstimate : oldEstimates) {
                if (!abstractEstimate.getStatus().getCode().toString().equals(Constants.ESTIMATE_STATUS_CANCELLED))
                    messages.put(Constants.KEY_UNIQUE_ABSTRACTESTIMATENUMBER, Constants.MESSAGE_UNIQUE_ABSTRACTESTIMATENUMBER);
            }
    }

    private void validateSpillOverData(AbstractEstimate estimate, Map<String, String> messages) {
        if (estimate.getAbstractEstimateNumber() == null)
            messages.put(Constants.KEY_NULL_ABSTRACTESTIMATE_NUMBER, Constants.MESSAGE_NULL_ABSTRACTESTIMATE_NUMBER);
        for (AbstractEstimateDetails details : estimate.getAbstractEstimateDetails())
            if (details.getProjectCode() == null
                    || (details.getProjectCode() != null && StringUtils.isBlank(details.getProjectCode().getCode())))
                messages.put(Constants.KEY_NULL_WIN_NUMBER, Constants.MESSAGE_NULL_WIN_NUMBER);

        if (estimate.getDateOfProposal() != null && estimate.getDateOfProposal() > new Date().getTime())
            messages.put(Constants.KEY_FUTUREDATE_DATEOFPROPOSAL, Constants.MESSAGE_FUTUREDATE_DATEOFPROPOSAL);

    }

    private void validateEstimateDetails(AbstractEstimate estimate, Map<String, String> messages) {
        BigDecimal estimateAmount = BigDecimal.ZERO;
        if (estimate.getAbstractEstimateDetails().isEmpty())
            messages.put(Constants.KEY_ABSTRACTESTIMATE_DETAILS_REQUIRED,
                    Constants.MESSAGE_ABSTRACTESTIMATE_DETAILS_REQUIRED);

        for (final AbstractEstimateDetails aed : estimate.getAbstractEstimateDetails()) {
            estimateAmount = estimateAmount.add(aed.getEstimateAmount());
            if (estimate.getBillsCreated() && aed.getGrossAmountBilled() == null) {
                messages.put(Constants.KEY_ABSTRACTESTIMATE_DETAILS_GROSSBILLEDAMOUNT_REQUIRED,
                        Constants.MESSAGE_ABSTRACTESTIMATE_DETAILS_GROSSBILLEDAMOUNT_REQUIRED);
            }

            if (estimate.getBillsCreated() && aed.getGrossAmountBilled() != null && aed.getGrossAmountBilled() <= 0) {
                messages.put(Constants.KEY_ABSTRACTESTIMATE_DETAILS_GROSSBILLEDAMOUNT_REQUIRED,
                        Constants.MESSAGE_ABSTRACTESTIMATE_DETAILS_GROSSBILLEDAMOUNT_REQUIRED);
            }

            if (estimate.getBillsCreated() && aed.getGrossAmountBilled() != null
                    && aed.getGrossAmountBilled().compareTo(BigDecimal.ZERO.doubleValue()) == -1)
                messages.put(Constants.KEY_INVALID_GROSSBILLEDAMOUNT, Constants.MESSAGE_INVALID_GROSSBILLEDAMOUNT);
            
            if (!estimate.getBillsCreated() && aed.getGrossAmountBilled() != null && aed.getGrossAmountBilled() != 0)
                messages.put(Constants.KEY_AE_INVALID_GROSSBILLEDAMOUNT, Constants.MESSAGE_AE_INVALID_GROSSBILLEDAMOUNT);

        }

        if (estimate.getBillsCreated() && !(estimate.getDetailedEstimateCreated() && estimate.getWorkOrderCreated())) {
            messages.put(Constants.KEY_INVALID_BILLSCREATED_FLAG, Constants.MESSAGE_INVALID_BILLSCREATED_FLAG);
        }

        if (estimate.getWorkOrderCreated() && !estimate.getDetailedEstimateCreated()) {
            messages.put(Constants.KEY_INVALID_WORKORDER_FLAG, Constants.MESSAGE_INVALID_WORKORDER_FLAG);
        }

        if (estimateAmount.compareTo(BigDecimal.ZERO) == -1)
            messages.put(Constants.KEY_INVALID_ESTIMATEAMOUNT, Constants.MESSAGE_INVALID_ESTIMATEAMOUNT);

    }

    public void validateMasterData(AbstractEstimate abstractEstimate, RequestInfo requestInfo,
            Map<String, String> messages, Boolean isNew) {

        Boolean isFinIntReq = false;
        JSONArray responseJSONArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                CommonConstants.FINANCIAL_INTEGRATION_KEY, abstractEstimate.getTenantId(), requestInfo,
                CommonConstants.MODULENAME_WORKS);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                isFinIntReq = true;
        }
        // TODO:
        if (abstractEstimate.getSpillOverFlag()
                || (!isNew && Constants.ESTIMATE_STATUS_CHECKED.equals(abstractEstimate.getStatus().getCode())
                        && Constants.FORWARD.equalsIgnoreCase(abstractEstimate.getWorkFlowDetails().getAction())))
            validateFinancialDetails(abstractEstimate, isFinIntReq, requestInfo, messages);
        validateTypeOfWork(abstractEstimate.getTypeOfWork(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateSubTypeOfWork(abstractEstimate.getSubTypeOfWork(), abstractEstimate.getTenantId(), requestInfo,
                messages);
        validateDepartment(abstractEstimate.getDepartment(), abstractEstimate.getTenantId(), requestInfo, messages);
        // TODO:
        // validateWard(abstractEstimate.getWard(),
        // abstractEstimate.getTenantId(), requestInfo, messages);
        // validateLocality(abstractEstimate.getLocality(),
        // abstractEstimate.getTenantId(), requestInfo, messages);
        validateNatureOfWork(abstractEstimate.getNatureOfWork(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateReferenceType(abstractEstimate.getReferenceType(), abstractEstimate.getTenantId(), requestInfo,
                messages);
        validateModeOfAllotment(abstractEstimate.getModeOfAllotment(), abstractEstimate.getTenantId(), requestInfo,
                messages);
    }

    private void validateModeOfAllotment(ModeOfAllotment modeOfAllotment, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (modeOfAllotment != null && modeOfAllotment.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.MODEOFALLOTMENT_OBJECT, CommonConstants.CODE,
                    modeOfAllotment.getCode(), tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_MODEOFALLOTMENT_INVALID, Constants.MESSAGE_MODEOFALLOTMENT_INVALID);
            }
        } else
            messages.put(Constants.KEY_MODEOFALLOTMENT_REQUIRED, Constants.MESSAGE_MODEOFALLOTMENT_REQUIRED);
    }

    private void validateReferenceType(ReferenceType referenceType, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (referenceType != null && referenceType.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.REFERENCETYPE_OBJECT, CommonConstants.CODE,
                    referenceType.getCode(), tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_REFERENCETYPE_INVALID, Constants.MESSAGE_REFERENCETYPE_INVALID);
            }
        } else
            messages.put(Constants.KEY_REFERENCETYPE_REQUIRED, Constants.MESSAGE_REFERENCETYPE_REQUIRED);
    }

    private void validateFinancialDetails(AbstractEstimate abstractEstimate, Boolean isFinIntReq,
            RequestInfo requestInfo, Map<String, String> messages) {
        validateFund(abstractEstimate.getFund(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateFunction(abstractEstimate.getFunction(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateScheme(abstractEstimate.getScheme(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateSubScheme(abstractEstimate.getSubScheme(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateBudgetGroup(abstractEstimate.getBudgetGroup(), abstractEstimate.getTenantId(), requestInfo, messages);
        validateAccountCode(abstractEstimate.getAccountCode(), isFinIntReq, messages);
    }

    public void validateFund(Fund fund, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (fund != null && StringUtils.isNotBlank(fund.getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.FUND_OBJECT, CommonConstants.CODE, fund.getCode(),
                    tenantId, requestInfo, Constants.EGF_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_FUND_INVALID, Constants.MESSAGE_FUND_INVALID);
            }
        } else
            messages.put(Constants.KEY_FUND_REQUIRED, Constants.MESSAGE_FUND_REQUIRED);
    }

    public void validateFunction(Function function, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (function != null && StringUtils.isNotBlank(function.getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.FUNCTION_OBJECT, CommonConstants.CODE,
                    function.getCode(), tenantId, requestInfo, Constants.EGF_MODULE_CODE);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_FUNCTION_INVALID, Constants.MESSAGE_FUNCTION_INVALID);
            }
        } else
            messages.put(Constants.KEY_FUNCTION_REQUIRED, Constants.MESSAGE_FUNCTION_REQUIRED);
    }

    public void validateScheme(Scheme scheme, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (scheme != null && StringUtils.isNotBlank(scheme.getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.SCHEME_OBJECT, CommonConstants.CODE,
                    scheme.getCode(), tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_SCHEME_INVALID, Constants.MESSAGE_SCHEME_INVALID);
            }
        }
    }

    public void validateSubScheme(SubScheme subScheme, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (subScheme != null && StringUtils.isNotBlank(subScheme.getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.SUBSCHEME_OBJECT, CommonConstants.CODE,
                    subScheme.getCode(), tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_SUBSCHEME_INVALID, Constants.MESSAGE_SUBSCHEME_INVALID);
            }
        }
    }

    public void validateBudgetGroup(BudgetGroup budgetGroup, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (budgetGroup != null && StringUtils.isNotBlank(budgetGroup.getName())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.BUDGETGROUP_OBJECT, CommonConstants.NAME,
                    budgetGroup.getName(), tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_BUDGETGROUP_INVALID, Constants.MESSAGE_BUDGETGROUP_INVALID);
            }
        } else
            messages.put(Constants.KEY_BUDGETGROUP_NAME_REQUIRED, Constants.MESSAGE_BUDGETGROUP_NAME_REQUIRED);
    }

    private void validateAccountCode(String accountCode, Boolean isFinIntReq, Map<String, String> messages) {
        // TODO: Validate once we get clarity on MDMS or fin integration
        if (isFinIntReq && accountCode == null)
            messages.put(Constants.KEY_ACCOUNTCODE_REQUIRED, Constants.MESSAGE_ACCOUNTCODE_REQUIRED);
    }

    public void validateTypeOfWork(TypeOfWork typeOfWork, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (typeOfWork != null && StringUtils.isNotBlank(typeOfWork.getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.TYPEOFWORK_OBJECT, CommonConstants.CODE,
                    typeOfWork.getCode(), tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_TYPEOFWORK_INVALID, Constants.MESSAGE_TYPEOFWORK_INVALID);
            }
        } else
            messages.put(Constants.KEY_TYPEOFWORK_REQUIRED, Constants.MESSAGE_TYPEOFWORK_REQUIRED);
    }

    public void validateSubTypeOfWork(TypeOfWork subTypeOfWork, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (subTypeOfWork != null && StringUtils.isNotBlank(subTypeOfWork.getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.TYPEOFWORK_OBJECT, CommonConstants.CODE,
                    subTypeOfWork.getCode(), tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_SUBTYPEOFWORK_INVALID, Constants.MESSAGE_SUBTYPEOFWORK_INVALID);
            }
        } else
            messages.put(Constants.KEY_SUBTYPEOFWORK_INVALID, Constants.MESSAGE_SUBTYPEOFWORK_INVALID);
    }

    public void validateDepartment(Department department, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (department != null && StringUtils.isNotBlank(department.getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.DEPARTMENT_OBJECT, CommonConstants.CODE,
                    department.getCode(), tenantId, requestInfo, CommonConstants.MODULENAME_COMMON);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_DEPARTMENT_INVALID, Constants.MESSAGE_DEPARTMENT_INVALID);
            }
        } else
            messages.put(Constants.KEY_DEPARTMENT_CODE_REQUIRED, Constants.MESSAGE_DEPARTMENT_CODE_REQUIRED);
    }

    public void validateWard(Boundary ward, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (ward != null && ward.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.WARD_OBJECT, CommonConstants.CODE, ward.getCode(),
                    tenantId, requestInfo, CommonConstants.MODULENAME_COMMON);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_WARD_INVALID, Constants.MESSAGE_WARD_INVALID);
            }
        } else
            messages.put(Constants.KEY_WARD_CODE_REQUIRED, Constants.MESSAGE_WARD_CODE_REQUIRED);
    }

    public void validateLocality(Boundary locality, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (locality != null && locality.getCode() != null) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.LOCALITY_OBJECT, CommonConstants.CODE,
                    locality.getCode(), tenantId, requestInfo, CommonConstants.MODULENAME_COMMON);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_LOCALITY_INVALID, Constants.MESSAGE_LOCALITY_INVALID);
            }
        } else
            messages.put(Constants.KEY_LOCALITY_CODE_REQUIRED, Constants.MESSAGE_LOCALITY_CODE_REQUIRED);
    }

    public void validateDetailedEstimates(DetailedEstimateRequest detailedEstimateRequest, Boolean isRevision) {
        final RequestInfo requestInfo = detailedEstimateRequest.getRequestInfo();
        Map<String, String> messages = new HashMap<>();
        AbstractEstimate abstactEstimate = null;
        for (DetailedEstimate detailedEstimate : detailedEstimateRequest.getDetailedEstimates()) {
            if (isRevision == null || (isRevision != null && !isRevision)) {
                abstactEstimate = searchAbstractEstimate(detailedEstimate);
                if (abstactEstimate == null)
                    messages.put(Constants.KEY_INVALID_ABSTRACTESTIMATE_DETAILS,
                            Constants.MESSAGE_INVALID_ABSTRACTESTIMATE_DETAILS);
                validateDetailedEstimateExists(detailedEstimate, abstactEstimate, messages, detailedEstimateRequest.getRequestInfo());
                if (!checkAbstractEstimateRequired(detailedEstimate, requestInfo)) {
                    validateMasterData(detailedEstimate, requestInfo, messages);
                    validateAssetDetails(detailedEstimate, requestInfo, messages, abstactEstimate);
                }
                validateEstimateAdminSanction(detailedEstimate, messages, abstactEstimate);
                validateSpillOverEstimate(detailedEstimate, messages, abstactEstimate, detailedEstimateRequest.getRequestInfo());
                validateLocationDetails(detailedEstimate, requestInfo, messages, abstactEstimate);
                if(detailedEstimate.getEstimateOverheads() != null && !detailedEstimate.getEstimateOverheads().isEmpty())
                    validateOverheads(detailedEstimate, requestInfo, messages);
                validateDocuments(detailedEstimate, requestInfo, messages);
                if(!detailedEstimate.getStatus().getCode().equalsIgnoreCase(Constants.ESTIMATE_STATUS_CANCELLED))
                   validateUpdateStatus(detailedEstimate, requestInfo, messages);
                if(detailedEstimate.getDetailedEstimateDeductions() != null && !detailedEstimate.getDetailedEstimateDeductions().isEmpty())
                    validateDeductions(detailedEstimate, requestInfo, messages);

            }
            validateActivities(detailedEstimate, messages, requestInfo);
            if (StringUtils.isNotBlank(detailedEstimate.getId())) {
                validateIsModified(detailedEstimate, requestInfo, messages);
                validateStatus(detailedEstimate.getStatus(), detailedEstimate.getTenantId(), requestInfo, messages, CommonConstants.DETAILEDESTIMATE);
            }
            if (detailedEstimate.getEstimateDate() != null && detailedEstimate.getEstimateDate() > new Date().getTime())
                messages.put(Constants.KEY_FUTUREDATE_ESTIMATEDATE,
                        Constants.MESSAGE_FUTUREDATE_ESTIMATEDATE);

            if (detailedEstimate.getWorkValue() != null && detailedEstimate.getWorkValue().compareTo(BigDecimal.ZERO) <= 0)
                messages.put(Constants.KEY_WORK_VALUE_INVALID,
                        Constants.MESSAGE_WORK_VALUE_INVALID);

            if (detailedEstimate.getEstimateValue() != null
                    && detailedEstimate.getEstimateValue().compareTo(BigDecimal.ZERO) <= 0)
                messages.put(Constants.KEY_ESTIMATE_VALUE_INVALID,
                        Constants.MESSAGE_ESTIMATE_VALUE_INVALID);

            if (detailedEstimate.getWorkValue() != null && detailedEstimate.getEstimateValue() != null &&
                    detailedEstimate.getEstimateValue().compareTo(detailedEstimate.getWorkValue()) < 0)
                messages.put(Constants.KEY_WORK_VALUE_GREATERTHAN_ESTIMATE_VALUE,
                        Constants.MESSAGE_WORK_VALUE_GREATERTHAN_ESTIMATE_VALUE);

            if(!workflowRequired(detailedEstimate.getTenantId(), detailedEstimateRequest.getRequestInfo()) &&
                    detailedEstimate.getStatus() != null && detailedEstimate.getStatus().getCode().equalsIgnoreCase(Constants.DETAILEDESTIMATE_STATUS_TECH_SANCTIONED) &&
                    !abstactEstimate.getDetailedEstimateCreated()) {
                validateTechnicalSanctionDetail(detailedEstimate, messages, abstactEstimate.getDetailedEstimateCreated(), requestInfo);
            }

        }
        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateDeductions(DetailedEstimate detailedEstimate, RequestInfo requestInfo, Map<String, String> messages) {
        if(detailedEstimate.getDetailedEstimateDeductions() != null) {
            for(DetailedEstimateDeduction deduction : detailedEstimate.getDetailedEstimateDeductions()) {

                if(StringUtils.isBlank(deduction.getTenantId()))
                    messages.put(Constants.KEY_ESIMATE_DEDUCTIONS_TENANTID_REQUIRED,
                            Constants.MESSAGE_ESIMATE_DEDUCTIONS_TENANTID_REQUIRED);

                if(deduction.getChartOfAccounts() != null && StringUtils.isBlank(deduction.getChartOfAccounts().getGlcode()))
                    messages.put(Constants.KEY_ESTIMATE_DEDUCTIONS_CHARTOFACCOUNTS_INVALID,
                            Constants.MESSAGE_ESTIMATE_DEDUCTIONS_CHARTOFACCOUNTS_INVALID);

                if(deduction.getAmount() == null)
                    messages.put(Constants.KEY_ESTIMATE_DEDUCTIONS_AMOUNT_REQUIRED,
                            Constants.MESSAGE_ESTIMATE_DEDUCTIONS_AMOUNT_REQUIRED);
                else if(deduction.getAmount().compareTo(BigDecimal.ZERO) <= 0)
                    messages.put(Constants.KEY_ESTIMATE_DEDUCTIONS_AMOUNT_INVALID,
                            Constants.MESSAGE_ESTIMATE_DEDUCTIONS_AMOUNT_INVALID);
            }
        }
    }

    private void validateStatus(WorksStatus status, String tenantId, RequestInfo requestInfo, Map<String, String> messages, String object) {

        if(status != null && StringUtils.isNotBlank(status.getCode())) {
            List<String> filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE,CommonConstants.MODULE_TYPE));
            List<String> filetsValuesList = new ArrayList<>(Arrays.asList(status.getCode().toUpperCase(), object));
            JSONArray dBStatusArray = estimateUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                    filetsValuesList, tenantId, requestInfo,
                    CommonConstants.MODULENAME_WORKS);
            if(dBStatusArray != null && dBStatusArray.isEmpty())
                messages.put(Constants.KEY_WORKS_ESTIMATE_STATUS_INVALID,
                        Constants.MESSAGE_WORKS_ESTIMATE_STATUS_INVALID);
        } else
            messages.put(Constants.KEY_WORKS_ESTIMATE_STATUS_REQUIRED,
                    Constants.MESSAGE_WORKS_ESTIMATE_STATUS_REQUIRED);
    }

    private void validateUpdateStatus(DetailedEstimate detailedEstimate, RequestInfo requestInfo, Map<String, String> messages) {
        if(detailedEstimate.getId() != null) {
            List<DetailedEstimateHelper> lists = searchDetailedEstimatesById(detailedEstimate, requestInfo);
            List<String> filetsNamesList = null;
            List<String> filetsValuesList = null;
            if(lists != null && !lists.isEmpty()) {
                String status = lists.get(0).getStatus();
                if (status.equals(Constants.ESTIMATE_STATUS_CANCELLED) || status.equals(Constants.DETAILEDESTIMATE_STATUS_TECH_SANCTIONED)) {
                    messages.put(Constants.KEY_CANNOT_UPDATE_STATUS_FOR_DETAILED_ESTIMATE, Constants.MESSAGE_CANNOT_UPDATE_STATUS_FOR_DETAILED_ESTIMATE);
                } else if((status.equals(Constants.ESTIMATE_STATUS_REJECTED) && !detailedEstimate.getStatus().getCode().equals(Constants.ESTIMATE_STATUS_RESUBMITTED)) ||
                        (status.equals(Constants.ESTIMATE_STATUS_RESUBMITTED) && !(detailedEstimate.getStatus().getCode().equals(Constants.ESTIMATE_STATUS_CHECKED) ||
                                detailedEstimate.getStatus().getCode().equals(Constants.ESTIMATE_STATUS_CANCELLED)) )) {
                    messages.put(Constants.KEY_INVALID_STATUS_UPDATE_FOR_DETAILED_ESTIMATE, Constants.MESSAGE_INVALID_STATUS_UPDATE_FOR_DETAILED_ESTIMATE);
                } else if (!detailedEstimate.getStatus().getCode().equals(Constants.ESTIMATE_STATUS_REJECTED)) {
                    filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE,CommonConstants.MODULE_TYPE));
                    filetsValuesList = new ArrayList<>(Arrays.asList(detailedEstimate.getStatus().getCode().toUpperCase(), CommonConstants.DETAILEDESTIMATE));
                    JSONArray statusRequestArray = estimateUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                            filetsValuesList, detailedEstimate.getTenantId(), requestInfo,
                            CommonConstants.MODULENAME_WORKS);
                    filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE,CommonConstants.MODULE_TYPE));
                    filetsValuesList = new ArrayList<>(Arrays.asList(status.toUpperCase(),CommonConstants.DETAILEDESTIMATE));
                    JSONArray dBStatusArray = estimateUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                            filetsValuesList, detailedEstimate.getTenantId(), requestInfo,
                            CommonConstants.MODULENAME_WORKS);
                    if (statusRequestArray != null && !statusRequestArray.isEmpty() && dBStatusArray != null && !dBStatusArray.isEmpty()) {
                        Map<String, Object> jsonMapRequest = (Map<String, Object>) statusRequestArray.get(0);
                        Map<String, Object> jsonMapDB = (Map<String, Object>) dBStatusArray.get(0);
                        Integer requestStatusOrderNumber = (Integer) jsonMapRequest.get("orderNumber");
                        Integer dbtStatusOrderNumber = (Integer) jsonMapDB.get("orderNumber");
                        if (requestStatusOrderNumber - dbtStatusOrderNumber != 1) {
                            messages.put(Constants.KEY_INVALID_STATUS_UPDATE_FOR_DETAILED_ESTIMATE, Constants.MESSAGE_INVALID_STATUS_UPDATE_FOR_DETAILED_ESTIMATE);
                        }
                    }
                }
            }

        }

    }

    private void validateDetailedEstimateExists(DetailedEstimate detailedEstimate, AbstractEstimate abstractEstimate,
            Map<String, String> messages, final RequestInfo requestInfo) {
        if (abstractEstimate != null && StringUtils.isBlank(detailedEstimate.getId())) {
            String projectCode = abstractEstimate.getAbstractEstimateDetails().get(0).getProjectCode().getCode();
            DetailedEstimateSearchContract detailedEstimateSearchContract = DetailedEstimateSearchContract.builder()
                    .tenantId(detailedEstimate.getTenantId())
                    .workIdentificationNumbers(Arrays.asList(projectCode)).build();
            List<DetailedEstimateHelper> lists = detailedEstimateJdbcRepository.search(detailedEstimateSearchContract, requestInfo);
            for (DetailedEstimateHelper detailedEstimateHelper : lists) {
                if (!detailedEstimateHelper.getStatus().equals(Constants.ESTIMATE_STATUS_CANCELLED))
                    messages.put(Constants.KEY_DE_EXISTS_FOR_AE, Constants.MESSAGE_DE_EXISTS_FOR_AE);
            }
        }
    }

    private void validateIsModified(DetailedEstimate detailedEstimate, RequestInfo requestInfo,
            Map<String, String> messages) {
        DetailedEstimateSearchContract detailedEstimateSearchContract = DetailedEstimateSearchContract.builder()
                .tenantId(detailedEstimate.getTenantId()).ids(Arrays.asList(detailedEstimate.getId())).build();
        List<DetailedEstimateHelper> lists = detailedEstimateJdbcRepository.search(detailedEstimateSearchContract, requestInfo);
        if (lists.isEmpty())
            messages.put(Constants.KEY_ESTIMATE_NOT_EXISTS, Constants.MESSAGE_ESTIMATE_NOT_EXISTS);
        else {
            DetailedEstimateHelper estimateFromDb = lists.get(0);
            if (!estimateFromDb.getEstimateNumber().equals(detailedEstimate.getEstimateNumber()))
                messages.put(Constants.KEY_ESTIMATE_NUMBER_MODIFIED, Constants.MESSAGE_ESTIMATE_NUMBER_MODIFIED);
            if (!estimateFromDb.getAbstractEstimateDetail()
                    .equals(detailedEstimate.getAbstractEstimateDetail().getId()))
                messages.put(Constants.KEY_ABSTRACT_ESTIMATE_DETAIL_MODIFIED,
                        Constants.KEY_ABSTRACT_ESTIMATE_DETAIL_MODIFIED);
        }
    }

    private void validateIsModified(AbstractEstimate abstractEstimate, RequestInfo requestInfo,
            Map<String, String> messages) {
        AbstractEstimateSearchContract abstractEstimateSearchContract = AbstractEstimateSearchContract.builder()
                .tenantId(abstractEstimate.getTenantId()).ids(Arrays.asList(abstractEstimate.getId())).build();
        List<AbstractEstimate> lists = abstractEstimateRepository.search(abstractEstimateSearchContract);
        if (lists.isEmpty())
            messages.put(Constants.KEY_ESTIMATE_NOT_EXISTS, Constants.MESSAGE_ESTIMATE_NOT_EXISTS);
        else {
            AbstractEstimate estimateFromDb = lists.get(0);
            if (!estimateFromDb.getAbstractEstimateNumber().equals(abstractEstimate.getAbstractEstimateNumber()))
                messages.put(Constants.KEY_ABSTRACT_ESTIMATE_NUMBER_MODIFIED,
                        Constants.MESSAGE_ABSTRACT_ESTIMATE_NUMBER_MODIFIED);
        }
    }

    private void validateDocuments(DetailedEstimate detailedEstimate, RequestInfo requestInfo,
            Map<String, String> messages) {
        if (detailedEstimate.getDocumentDetails() != null) {
            for (DocumentDetail documentDetail : detailedEstimate.getDocumentDetails()) {
                if(StringUtils.isBlank(documentDetail.getTenantId()))
                    messages.put(Constants.KEY_ESIMATE_DOCUMENTDETAILS_TENANTID_REQUIRED, Constants.MESSAGE_ESIMATE_DOCUMENTDETAILS_TENANTID_REQUIRED);

                boolean fileExists = fileStoreRepository.searchFileStore(detailedEstimate.getTenantId(),
                        documentDetail.getFileStore(), requestInfo);
                if (!fileExists)
                    messages.put(Constants.KEY_INVALID_FILESTORE_ID, Constants.MESSAGE_INVALID_FILESTORE_ID);
            }
        }
    }

    private void validateEstimateAdminSanction(DetailedEstimate detailedEstimate, Map<String, String> messages,
            AbstractEstimate abstractEstimate) {
        if (abstractEstimate != null && detailedEstimate.getEstimateDate() != null
                && detailedEstimate.getEstimateDate() < abstractEstimate.getAdminSanctionDate()) {
            messages.put(Constants.KEY_INVALID_ADMINSANCTION_DATE, Constants.MESSAGE_INVALID_ADMINSANCTION_DATE);
        }

    }

    public AbstractEstimate searchAbstractEstimate(DetailedEstimate detailedEstimate) {
        AbstractEstimate abstractEstimate = null;
        if (detailedEstimate.getAbstractEstimateDetail() != null) {
            String workIdentificationNumber = detailedEstimate.getAbstractEstimateDetail().getProjectCode().getCode();
            AbstractEstimateSearchContract abstractEstimateSearchContract = AbstractEstimateSearchContract.builder()
                    .tenantId(detailedEstimate.getTenantId())
                    .workIdentificationNumbers(
                            Arrays.asList(detailedEstimate.getAbstractEstimateDetail().getProjectCode().getCode()))
                    .statuses(Arrays.asList(Constants.ABSTRACTESTIMATE_STATUS_ADMIN_SANCTIONED))
                    .build();
            List<AbstractEstimate> abstractEstimates = abstractEstimateRepository
                    .search(abstractEstimateSearchContract);
            if (!abstractEstimates.isEmpty()) {
                abstractEstimate = abstractEstimates.get(0);
                for (AbstractEstimateDetails abstractEstimateDetails : abstractEstimate.getAbstractEstimateDetails()) {
                    if (abstractEstimateDetails.getProjectCode() != null && abstractEstimateDetails.getProjectCode()
                            .getCode().equalsIgnoreCase(workIdentificationNumber))
                        abstractEstimate.setAbstractEstimateDetails(Arrays.asList(abstractEstimateDetails));
                    return abstractEstimate;

                }
            }
        }
        return null;
    }

    public void validateSpillOverEstimate(final DetailedEstimate detailedEstimate, Map<String, String> messages,
            AbstractEstimate abstractEstimate, final RequestInfo requestInfo) {
        if (abstractEstimate != null && abstractEstimate.getDetailedEstimateCreated()) {
            if (StringUtils.isBlank(detailedEstimate.getEstimateNumber()))
                messages.put(Constants.KEY_NULL_DETAILEDESTIMATE_NUMBER,
                        Constants.MESSAGE_NULL_DETAILEDESTIMATE_NUMBER);
            else
                validateEstimateNumberUnique(detailedEstimate, messages, requestInfo);

            validateTechnicalSanctionDetail(detailedEstimate, messages, abstractEstimate.getDetailedEstimateCreated(), requestInfo);
        }
    }

    private void validateEstimateNumberUnique(DetailedEstimate detailedEstimate, Map<String, String> messages, RequestInfo requestInfo) {
        if (detailedEstimate.getId() == null) {
            DetailedEstimateSearchContract detailedEstimateSearchContract = DetailedEstimateSearchContract.builder()
                    .tenantId(detailedEstimate.getTenantId())
                    .detailedEstimateNumbers(Arrays.asList(detailedEstimate.getEstimateNumber())).build();
            List<DetailedEstimateHelper> lists = detailedEstimateJdbcRepository.search(detailedEstimateSearchContract, requestInfo);
            for (DetailedEstimateHelper detailedEstimateHelper : lists) {
                if (!detailedEstimateHelper.getStatus().equals(Constants.ESTIMATE_STATUS_CANCELLED))
                    messages.put(Constants.KEY_INVALID_ESTIMATNUMBER_SPILLOVER,
                            Constants.MESSAGE_INVALID_ESTIMATNUMBER_SPILLOVER);
            }
        }
    }

    public void validateOverheads(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo,
            Map<String, String> messages) {
        Overhead overhead = null;
        BigDecimal totalOverAmount = BigDecimal.ZERO;
        if (detailedEstimate.getEstimateOverheads() != null) {
            for (final EstimateOverhead estimateOverhead : detailedEstimate.getEstimateOverheads()) {

                if (estimateOverhead != null) {
                    validateEstimateOverHead(estimateOverhead.getOverhead(), requestInfo, messages);

                    if (StringUtils.isBlank(estimateOverhead.getTenantId())) {
                        messages.put(Constants.KEY_ESIMATE_OVERHEAD_TENANTID_REQUIRED, Constants.MESSAGE_ESIMATE_OVERHEAD_TENANTID_REQUIRED);
                    }
                    if (StringUtils.isBlank(estimateOverhead.getOverhead().getCode())) {
                        messages.put(Constants.KEY_ESIMATE_OVERHEAD_CODE, Constants.MESSAGE_ESIMATE_OVERHEAD_CODE);
                    }
                    if (estimateOverhead.getAmount() != null && estimateOverhead.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        messages.put(Constants.KEY_ESIMATE_OVERHEAD_AMOUNT, Constants.MESSAGE_ESIMATE_OVERHEAD_AMOUNT);
                    } else
                        totalOverAmount = totalOverAmount.add(estimateOverhead.getAmount());

                    if (overhead != null && overhead.getCode().equals(estimateOverhead.getOverhead().getCode())) {
                        messages.put(Constants.KEY_ESIMATE_OVERHEAD_UNIQUE, Constants.MESSAGE_ESIMATE_OVERHEAD_UNIQUE);
                    }
                    overhead = estimateOverhead.getOverhead();
                }
            }

            if (detailedEstimate.getWorkValue() != null && detailedEstimate.getEstimateValue() != null &&
                    totalOverAmount != null
                    && totalOverAmount.add(detailedEstimate.getWorkValue()).compareTo(detailedEstimate.getEstimateValue()) != 0) {
                messages.put(Constants.KEY_ESIMATE_OVERHEAD_WORKVALUE_AMOUNT, Constants.MESSAGE_ESIMATE_OVERHEAD_WORKVALUE_AMOUNT);
            }
        }
    }

    private void validateEstimateOverHead(Overhead overhead, RequestInfo requestInfo, Map<String, String> messages) {
        if (overhead != null && StringUtils.isNotBlank(overhead.getCode())) {
            JSONArray responseJSONArray = estimateUtils.getMDMSData(Constants.OVERHEAD_OBJECT, CommonConstants.CODE,
                    overhead.getCode(), overhead.getTenantId(), requestInfo, CommonConstants.MODULENAME_WORKS);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_ESTIMATE_OVERHEAD_CODE_INVALID,
                        Constants.MESSAGE_ESTIMATE_OVERHEAD_CODE_INVALID);
            }
        }
    }

    public void validateActivities(final DetailedEstimate detailedEstimate, Map<String, String> messages,
            final RequestInfo requestInfo) {

        if (detailedEstimate.getWorkFlowDetails() != null &&  detailedEstimate.getWorkFlowDetails().getAction() != null && (detailedEstimate.getWorkFlowDetails().getAction()
                .equalsIgnoreCase(CommonConstants.WORKFLOW_ACTION_CREATE) || detailedEstimate.getWorkFlowDetails().getAction()
                        .equalsIgnoreCase(CommonConstants.WORKFLOW_ACTION_FORWARD)
                || detailedEstimate.getWorkFlowDetails().getAction()
                        .equalsIgnoreCase(CommonConstants.WORKFLOW_ACTION_REJECTED)))
            if (detailedEstimate.getEstimateActivities() == null || detailedEstimate.getEstimateActivities().isEmpty())
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_REQUIRED, Constants.MESSAGE_ESTIMATE_ACTIVITY_REQUIRED);

        ScheduleOfRate sor = null;
        for (final EstimateActivity activity : detailedEstimate.getEstimateActivities()) {

            if (activity.getScheduleOfRate() != null && (StringUtils.isBlank(activity.getScheduleOfRate().getId()) &&
                    (StringUtils.isBlank(activity.getScheduleOfRate().getCode())
                            && activity.getScheduleOfRate().getScheduleCategory() == null)) ||
                    (activity.getScheduleOfRate() != null && activity.getScheduleOfRate().getScheduleCategory() != null &&
                            StringUtils.isBlank(activity.getScheduleOfRate().getScheduleCategory().getCode()))
                    || (activity.getNonSor() != null && (activity.getNonSor().getUom() == null || (activity.getNonSor().getUom() != null &&
                      StringUtils.isBlank(activity.getNonSor().getUom().getCode())) || StringUtils.isBlank(activity.getNonSor().getDescription()))
                    || activity.getScheduleOfRate() == null && activity.getNonSor() == null))
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_REQUIRED, Constants.MESSAGE_ESTIMATE_ACTIVITY_REQUIRED);

            // validate at pattern level
            if (activity.getQuantity() != null && activity.getQuantity() <= 0)
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_QUANTITY, Constants.MESSAGE_ESTIMATE_ACTIVITY_QUANTITY);

            if (activity.getEstimateRate().compareTo(BigDecimal.ZERO) <= 0)
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_ESTIMATE_RATE,
                        Constants.MESSAGE_ESTIMATE_ACTIVITY_ESTIMATE_RATE);

            if(StringUtils.isBlank(activity.getTenantId()))
                messages.put(Constants.KEY_ESIMATE_ACTIVITY_TENANTID_REQUIRED,
                        Constants.MESSAGE_ESIMATE_ACTIVITY_TENANTID_REQUIRED);

            validateUOM(activity.getUom(), detailedEstimate.getTenantId(), requestInfo, messages);

            if (activity.getUnitRate().compareTo(BigDecimal.ZERO) <= 0)
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID,
                        Constants.MESSAGE_ESTIMATE_ACTIVITY_UNIT_RATE_INVALID);

            if (activity.getScheduleOfRate() != null) {
                List<ScheduleOfRate> scheduleOfRates = new ArrayList<>();
                if (StringUtils.isNotBlank(activity.getScheduleOfRate().getId())) {
                    scheduleOfRates = worksMastersRepository.searchScheduleOfRatesById(
                            activity.getTenantId(), Arrays.asList(activity.getScheduleOfRate().getId()), requestInfo);
                } else if (StringUtils.isNotBlank(activity.getScheduleOfRate().getCode())
                        && activity.getScheduleOfRate().getScheduleCategory() != null &&
                        StringUtils.isNotBlank(activity.getScheduleOfRate().getScheduleCategory().getCode()))
                    scheduleOfRates = worksMastersRepository.searchScheduleOfRatesByCodeAndCategory(
                            activity.getTenantId(), Arrays.asList(activity.getScheduleOfRate().getCode()),
                            Arrays.asList(activity.getScheduleOfRate().getScheduleCategory().getCode()), requestInfo);

                if (scheduleOfRates != null && scheduleOfRates.isEmpty())
                    messages.put(Constants.KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATES_INVALID,
                            Constants.MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATES_INVALID);
                else if (sor != null && sor.getId().equals(activity.getScheduleOfRate().getId()))
                    messages.put(Constants.KEY_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE,
                            Constants.MESSAGE_ESTIMATE_ACTIVITY_SCHEDULEOFRATE_DUPLICATE);

                if(!validateSorRates(scheduleOfRates, detailedEstimate))
                    messages.put(Constants.KEY_INVALID_SOR_RATES,
                            Constants.MESSAGE_INVALID_SOR_RATES);

                if (scheduleOfRates != null && !scheduleOfRates.isEmpty())
                    activity.setScheduleOfRate(scheduleOfRates.get(0));
            }

            if(activity.getNonSor() != null) {
                if(StringUtils.isBlank(activity.getNonSor().getDescription()))
                    messages.put(Constants.KEY_NONSOR_DESCRIPTION_REQUIRED,
                            Constants.MESSAGE_NONSOR_DESCRIPTION_REQUIRED);

                if(activity.getNonSor().getUom() != null && StringUtils.isBlank(activity.getNonSor().getUom().getCode()))
                    messages.put(Constants.KEY_NONSOR_UOM_CODE_REQUIRED,
                            Constants.MESSAGE_NONSOR_UOM_CODE_REQUIRED);
                else
                    validateUOM(activity.getNonSor().getUom(), detailedEstimate.getTenantId(), requestInfo, messages);
            }

            BigDecimal measurementQuantitySum = BigDecimal.ZERO;
            if (activity.getEstimateMeasurementSheets() != null) {
                for (final EstimateMeasurementSheet estimateMeasurementSheet : activity
                        .getEstimateMeasurementSheets()) {

                    if (StringUtils.isBlank(estimateMeasurementSheet.getIdentifier()))
                        messages.put(Constants.KEY_ESTIMATE_ACTIVITY_MEASUREMENT_IDENTIFIER_REQUIRED,
                                Constants.MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_IDENTIFIER_REQUIRED);
                    else {
                        if (estimateMeasurementSheet.getIdentifier().equals("A"))
                            measurementQuantitySum = measurementQuantitySum.add(estimateMeasurementSheet.getQuantity());
                        else
                            measurementQuantitySum = measurementQuantitySum.subtract(estimateMeasurementSheet.getQuantity());
                    }

                    if (estimateMeasurementSheet.getQuantity() == null)
                        messages.put(Constants.KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED,
                                Constants.MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_REQUIRED);
                    else if (estimateMeasurementSheet.getQuantity().compareTo(BigDecimal.ZERO) <= 0)
                        messages.put(Constants.KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID,
                                Constants.MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_INVALID);
                }
            }

            if (measurementQuantitySum.compareTo(BigDecimal.valueOf(activity.getQuantity())) != 0) {
                messages.put(Constants.KEY_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_GREATER,
                        Constants.MESSAGE_ESTIMATE_ACTIVITY_MEASUREMENT_QUANTITY_GREATER);
            }

        }

        BigDecimal totalActivityAmount = getTotalActivityAmount(detailedEstimate.getEstimateActivities());
        if (detailedEstimate.getWorkValue() != null && totalActivityAmount != null &&
                detailedEstimate.getWorkValue().compareTo(totalActivityAmount) != 0)
            messages.put(Constants.KEY_ACTIVITY_AMOUNT_TOTAL_NOTEQUALSTO_WORKVALUE,
                    Constants.MESSAGE_ACTIVITY_AMOUNT_TOTAL_NOTEQUALSTO_WORKVALUE);

    }

    private boolean validateSorRates(List<ScheduleOfRate> scheduleOfRates, DetailedEstimate detailedEstimate) {
        boolean validRatesExists = false;
        for (ScheduleOfRate scheduleOfRate : scheduleOfRates) {
            for (SORRate sorRate : scheduleOfRate.getSorRates())
                if (sorRate.getFromDate() <= detailedEstimate.getEstimateDate() &&
                        sorRate.getToDate() >= detailedEstimate.getEstimateDate()) {
                    validRatesExists = true;
                    break;
                }
        }
        return validRatesExists;
    }

    private void validateUOM(final UOM uom, String tenantId, RequestInfo requestInfo, Map<String, String> messages) {
        JSONArray responseJSONArray;

        if (uom != null && StringUtils.isNotBlank(uom.getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.UOM_OBJECT, CommonConstants.CODE, uom.getCode(),
                    tenantId, requestInfo, CommonConstants.MODULENAME_COMMON);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_UOM_INVALID, Constants.MESSAGE_UOM_INVALID);
            }
        } else
            messages.put(Constants.KEY_UOM_REQUIRED, Constants.MESSAGE_UOM_REQUIRED);
    }

    public void validateLocationDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo,
            Map<String, String> messages, final AbstractEstimate abstractEstimate) {
        if (propertiesManager.getLocationRequiredForEstimate().toString().equalsIgnoreCase("Yes")) {
            JSONArray mdmsArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                    Constants.GIS_INTEGRATION_APPCONFIG, detailedEstimate.getTenantId(), requestInfo,
                    CommonConstants.MODULENAME_WORKS);
            if (mdmsArray != null && !mdmsArray.isEmpty()) {
                Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
                if (jsonMap.get("value").equals("Yes") && !abstractEstimate.getDetailedEstimateCreated() && (StringUtils.isBlank(detailedEstimate.getLocation())
                        || detailedEstimate.getLatitude() == null || detailedEstimate.getLongitude() == null))
                    messages.put(Constants.KEY_ESTIMATE_LOCATION_REQUIRED,
                            Constants.MESSAGE_ESTIMATE_LOCATION_REQUIRED);
            }
        }
    }

    public void validateAssetDetails(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo,
            Map<String, String> messages, AbstractEstimate abstractEstimate) {

        JSONArray mdmsArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                CommonConstants.ASSET_DETAILES_REQUIRED_APPCONFIG, detailedEstimate.getTenantId(), requestInfo,
                CommonConstants.MODULENAME_WORKS);
        boolean assetsAdded = false;
        if (mdmsArray != null && !mdmsArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
            if (jsonMap.get("value").equals("Yes")) {
                if (detailedEstimate.getAssets() != null && !detailedEstimate.getAssets().isEmpty())
                    assetsAdded = true;

                for (AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets())
                    if (!assetsAdded && StringUtils.isBlank(assetsForEstimate.getLandAsset()))
                        messages.put(Constants.KEY_ESTIMATE_ASSET_DETAILS_REQUIRED,
                                Constants.MESSAGE_ESTIMATE_ASSET_DETAILS_REQUIRED);
            }
        }

        Asset asset = null;
        for (final AssetsForEstimate assetsForEstimate : detailedEstimate.getAssets())
            if (assetsForEstimate != null) {
                if (detailedEstimate.getNatureOfWork() != null
                        && detailedEstimate.getNatureOfWork().getExpenditureType() != null) {
                    mdmsArray = estimateUtils.getMDMSData(Constants.NATUREOFWORK_OBJECT,
                            CommonConstants.EXPENDITURETYPE,
                            detailedEstimate.getNatureOfWork().getExpenditureType().toString(),
                            detailedEstimate.getTenantId(), requestInfo, CommonConstants.MODULENAME_WORKS);
                    if (mdmsArray != null && !mdmsArray.isEmpty()) {
                        Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
                        if (jsonMap.get(CommonConstants.EXPENDITURETYPE).equals(ExpenditureType.REVENUE.toString())
                                && assetsForEstimate.getAsset() == null)
                            messages.put(Constants.KEY_ESTIMATE_ASSET_REQUIRED,
                                    Constants.MESSAGE_ESTIMATE_ASSET_REQUIRED);

                        else if (jsonMap.get(CommonConstants.EXPENDITURETYPE).equals(ExpenditureType.CAPITAL.toString())
                                && StringUtils.isBlank(assetsForEstimate.getLandAsset()) && abstractEstimate != null
                                && abstractEstimate.getLandAssetRequired() != null
                                && abstractEstimate.getLandAssetRequired())
                            messages.put(Constants.KEY_ESTIMATE_LAND_ASSET_REQUIRED,
                                    Constants.MESSAGE_ESTIMATE_LAND_ASSET_REQUIRED);
                    }

                }

                // TODO FIX aset code validation getting deserialization error
                // for AttributeDefinition["columns"]
                if (assetsForEstimate.getAsset() != null && StringUtils.isNotBlank(assetsForEstimate.getAsset().getCode())) {
                    List<Asset> assets = assetRepository.searchAssets(assetsForEstimate.getTenantId(),
                            assetsForEstimate.getAsset().getCode(), requestInfo);
                    if (assets != null && assets.isEmpty())
                        messages.put(Constants.KEY_WORKS_ESTIMATE_ASSET_CODE_INVALID,
                                Constants.MESSAGE_WORKS_ESTIMATE_ASSET_CODE_INVALID);
                }
                if (asset != null && asset.getCode().equals(assetsForEstimate.getAsset().getCode()))
                    messages.put(Constants.KEY_DUPLICATE_ESTIMATE_ASSET_DETAILS,
                            Constants.MESSAGE_DUPLICATE_ESTIMATE_ASSET_DETAILS);
                asset = assetsForEstimate.getAsset();
            }
    }

    public void validateEstimateAssetDetails(final AbstractEstimate abstractEstimate, final RequestInfo requestInfo,
            Map<String, String> messages) {

        JSONArray mdmsArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                CommonConstants.ASSET_DETAILES_REQUIRED_APPCONFIG, abstractEstimate.getTenantId(), requestInfo,
                CommonConstants.MODULENAME_WORKS);
        boolean assetsAdded = false;
        if (mdmsArray != null && !mdmsArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
            if (jsonMap.get("value").equals("Yes") && !abstractEstimate.getSpillOverFlag()) {
                if (abstractEstimate.getAssetDetails() != null && !abstractEstimate.getAssetDetails().isEmpty())
                    assetsAdded = true;

                for (AbstractEstimateAssetDetail abstractEstimateAssetDetail : abstractEstimate.getAssetDetails())
                    if (!assetsAdded && StringUtils.isBlank(abstractEstimateAssetDetail.getLandAsset()))
                        messages.put(Constants.KEY_ESTIMATE_ASSET_DETAILS_REQUIRED,
                                Constants.MESSAGE_ESTIMATE_ASSET_DETAILS_REQUIRED);
            }
        }

        Asset asset = null;
        if (abstractEstimate.getAssetDetails() != null) {
            for (final AbstractEstimateAssetDetail abstractEstimateAssetDetail : abstractEstimate.getAssetDetails())
                if (abstractEstimateAssetDetail != null) {
                    if (abstractEstimate.getNatureOfWork() != null
                            && abstractEstimate.getNatureOfWork().getExpenditureType() != null) {
                        mdmsArray = estimateUtils.getMDMSData(Constants.NATUREOFWORK_OBJECT,
                                CommonConstants.EXPENDITURETYPE,
                                abstractEstimate.getNatureOfWork().getExpenditureType().toString(),
                                abstractEstimate.getTenantId(), requestInfo, CommonConstants.MODULENAME_WORKS);
                        if (mdmsArray != null && !mdmsArray.isEmpty()) {
                            Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
                            if (jsonMap.get(CommonConstants.EXPENDITURETYPE).equals(ExpenditureType.REVENUE.toString())
                                    && abstractEstimateAssetDetail.getAsset() == null)
                                messages.put(Constants.KEY_ESTIMATE_ASSET_REQUIRED,
                                        Constants.MESSAGE_ESTIMATE_ASSET_REQUIRED);

                            else if (jsonMap.get(CommonConstants.EXPENDITURETYPE).equals(ExpenditureType.CAPITAL.toString())
                                    && StringUtils.isBlank(abstractEstimateAssetDetail.getLandAsset())
                                    && abstractEstimate != null && abstractEstimate.getLandAssetRequired() != null
                                    && abstractEstimate.getLandAssetRequired())
                                messages.put(Constants.KEY_ESTIMATE_LAND_ASSET_REQUIRED,
                                        Constants.MESSAGE_ESTIMATE_LAND_ASSET_REQUIRED);
                        }

                    }

                    // TODO FIX aset code validation getting deserialization error
                    // for AttributeDefinition["columns"]
                   /*  if( abstractEstimateAssetDetail.getAsset() != null && StringUtils.isNotBlank(abstractEstimateAssetDetail.getAsset().getCode())) {
                         List<Asset> assets = assetRepository.searchAssets(abstractEstimateAssetDetail.getTenantId(),
                             abstractEstimateAssetDetail.getAsset().getCode(),requestInfo);
                         if(assets != null && assets.isEmpty())
                          messages.put(Constants.KEY_WORKS_ESTIMATE_ASSET_CODE_INVALID,Constants.MESSAGE_WORKS_ESTIMATE_ASSET_CODE_INVALID); }*/
                    if (asset != null && asset.getCode().equals(abstractEstimateAssetDetail.getAsset().getCode()))
                        messages.put(Constants.KEY_DUPLICATE_ESTIMATE_ASSET_DETAILS,
                                Constants.MESSAGE_DUPLICATE_ESTIMATE_ASSET_DETAILS);
                        asset = abstractEstimateAssetDetail.getAsset();
                }
        }

    }

    public void validateTechnicalSanctionDetail(final DetailedEstimate detailedEstimate, Map<String, String> messages, boolean detailedEstimateCreated, RequestInfo requestInfo) {
        if (detailedEstimate.getEstimateTechnicalSanctions() == null
                || detailedEstimate.getEstimateTechnicalSanctions() != null
                        && detailedEstimate.getEstimateTechnicalSanctions().isEmpty()) {
            messages.put(Constants.KEY_ESTIMATE_TECHNICALSANCTION_DETAILS_REQUIRED,
                    Constants.MESSAGE_ESTIMATE_TECHNICALSANCTION_DETAILS_REQUIRED);
        }

        if (detailedEstimate.getEstimateTechnicalSanctions() != null)
            for (EstimateTechnicalSanction estimateTechnicalSanction : detailedEstimate
                    .getEstimateTechnicalSanctions()) {

                if(StringUtils.isBlank(estimateTechnicalSanction.getTenantId()))
                    messages.put(Constants.KEY_ESIMATE_TECHNICALSANCTION_TENANTID_REQUIRED,
                            Constants.MESSAGE_ESIMATE_TECHNICALSANCTION_TENANTID_REQUIRED);

                if (StringUtils.isNotBlank(estimateTechnicalSanction.getTechnicalSanctionNumber()))
                    validateUniqueTechnicalSanctionForDetailedEstimate(detailedEstimate, estimateTechnicalSanction,
                            messages, requestInfo);

                if(detailedEstimateCreated && StringUtils.isBlank(estimateTechnicalSanction.getTechnicalSanctionNumber()))
                    messages.put(Constants.KEY_ESTIMATE_TECHNICALSANCTION_NUMBER_REQUIRED,
                            Constants.MESSAGE_ESTIMATE_TECHNICALSANCTION_NUMBER_REQUIRED);

                if(detailedEstimateCreated && estimateTechnicalSanction.getTechnicalSanctionDate() == null)
                    messages.put(Constants.KEY_ESTIMATE_TECHNICALSANCTION_DATE_REQUIRED,
                            Constants.MESSAGE_ESTIMATE_TECHNICALSANCTION_DATE_REQUIRED);

                if (estimateTechnicalSanction.getTechnicalSanctionDate() != null
                        && estimateTechnicalSanction.getTechnicalSanctionDate() > new Date().getTime())
                    messages.put(Constants.KEY_TECHNICAL_SANCTION_DATE_FUTUREDATE,
                            Constants.MESSAGE_TECHNICAL_SANCTION_DATE_FUTUREDATE);

                if (detailedEstimate.getEstimateDate() != null
                        && estimateTechnicalSanction.getTechnicalSanctionDate() != null
                        && estimateTechnicalSanction.getTechnicalSanctionDate() < detailedEstimate.getEstimateDate())
                    messages.put(Constants.KEY_INVALID_TECHNICALSANCTION_DATE,
                            Constants.MESSAGE_INVALID_TECHNICALSANCTION_DATE);
            }

    }

    private void validateUniqueTechnicalSanctionForDetailedEstimate(DetailedEstimate detailedEstimate,
            EstimateTechnicalSanction estimateTechnicalSanction, Map<String, String> messages, final RequestInfo requestInfo) {
        DetailedEstimateSearchContract detailedEstimateSearchContract = DetailedEstimateSearchContract.builder()
                .tenantId(detailedEstimate.getTenantId())
                .technicalSanctionNumbers(Arrays.asList(estimateTechnicalSanction.getTechnicalSanctionNumber()))
                .statuses(Arrays.asList(Constants.DETAILEDESTIMATE_STATUS_TECH_SANCTIONED))
                .build();
        if (StringUtils.isNotBlank(detailedEstimate.getId())) {
            detailedEstimateSearchContract.setIds(Arrays.asList(detailedEstimate.getId()));
        }
        List<DetailedEstimateHelper> detailedEstimates = detailedEstimateJdbcRepository.search(detailedEstimateSearchContract, requestInfo);
        if (detailedEstimates != null && !detailedEstimates.isEmpty())
            messages.put(Constants.KEY_INVALID_TECHNICALSANCTION_NUMBER, Constants.MESSAGE_INVALID_TECHNICALSANCTION_NUMBER);
    }

    private void validateMasterData(DetailedEstimate detailedEstimate, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray = null;

        responseJSONArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                CommonConstants.FINANCIAL_INTEGRATION_KEY, detailedEstimate.getTenantId(), requestInfo,
                CommonConstants.MODULENAME_WORKS);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes"))
                validateFinancialDetailsForDetailedEstmate(detailedEstimate, requestInfo, messages);
        }

        validateModeOfAllotment(detailedEstimate.getModeOfAllotment(), detailedEstimate.getTenantId(), requestInfo,
                messages);
        validateNatureOfWork(detailedEstimate.getNatureOfWork(), detailedEstimate.getTenantId(), requestInfo, messages);
        // validateWardBoundary(detailedEstimate.getWard(),
        // detailedEstimate.getTenantId(), requestInfo, messages);
        validateLocalityBoundary(detailedEstimate.getLocality(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateTypeOfWork(detailedEstimate.getWorksType(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateSubTypeOfWork(detailedEstimate.getWorksSubtype(), detailedEstimate.getTenantId(), requestInfo,
                messages);
        validateDepartment(detailedEstimate.getDepartment(), detailedEstimate.getTenantId(), requestInfo, messages);
    }

    private void validateLocalityBoundary(Boundary locality, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {

        if (locality != null && StringUtils.isNotBlank(locality.getCode())) {
            List<Boundary> boundaries = boundaryRepository.searchBoundaries(tenantId, locality.getCode());
            if (boundaries != null && boundaries.isEmpty())
                messages.put(Constants.KEY_LOCALITY_CODE_INVALID, Constants.MESSAGE_LOCALITY_CODE_INVALID);
        }

    }

    private void validateWardBoundary(Boundary ward, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        if (ward != null && StringUtils.isNotBlank(ward.getCode())) {
            List<Boundary> boundaries = boundaryRepository.searchBoundaries(tenantId, ward.getCode());
            if (boundaries != null && boundaries.isEmpty())
                messages.put(Constants.KEY_WARD_INVALID, Constants.MESSAGE_WARD_INVALID);
        } else
            messages.put(Constants.KEY_WARD_CODE_REQUIRED, Constants.MESSAGE_WARD_CODE_REQUIRED);
    }

    private void validateNatureOfWork(NatureOfWork natureOfWork, String tenantId, RequestInfo requestInfo,
            Map<String, String> messages) {
        JSONArray responseJSONArray;
        if (natureOfWork != null && StringUtils.isNotBlank(natureOfWork.getCode())) {
            responseJSONArray = estimateUtils.getMDMSData(Constants.NATUREOFWORK_OBJECT, CommonConstants.CODE,
                    natureOfWork.getCode(), tenantId, requestInfo, CommonConstants.MODULENAME_WORKS);
            if (responseJSONArray != null && responseJSONArray.isEmpty()) {
                messages.put(Constants.KEY_NATUREOFWORK_INVALID, Constants.MESSAGE_NATUREOFWORK_INVALID);
            } else {
                Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
                if (jsonMap.get("expenditureType").equals(ExpenditureType.CAPITAL.toString()))
                    natureOfWork.setExpenditureType(ExpenditureType.CAPITAL);
                else
                    natureOfWork.setExpenditureType(ExpenditureType.REVENUE);
            }
        } else
            messages.put(Constants.KEY_NATUREOFWORK_REQUIRED, Constants.MESSAGE_NATUREOFWORK_REQUIRED);
    }

    private void validateFinancialDetailsForDetailedEstmate(DetailedEstimate detailedEstimate, RequestInfo requestInfo,
            Map<String, String> messages) {
        validateFund(detailedEstimate.getFund(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateFunction(detailedEstimate.getFunction(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateScheme(detailedEstimate.getScheme(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateSubScheme(detailedEstimate.getSubScheme(), detailedEstimate.getTenantId(), requestInfo, messages);
        validateBudgetGroup(detailedEstimate.getBudgetGroup(), detailedEstimate.getTenantId(), requestInfo, messages);
    }

    public boolean checkAbstractEstimateRequired(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo) {
        JSONArray responseJSONArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                Constants.ABSTRACT_ESTIMATE_REQUIRED_APPCONFIG, detailedEstimate.getTenantId(), requestInfo,
                CommonConstants.MODULENAME_WORKS);
        boolean abstractEstimateRequired = false;
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
            if (jsonMap.get("value").equals("Yes")) {
                abstractEstimateRequired = true;
            } else
                abstractEstimateRequired = false;
        }
        return abstractEstimateRequired;
    }

    public BigDecimal getTotalActivityAmount(List<EstimateActivity> activities) {
        BigDecimal totalActivityAmount = BigDecimal.ZERO;
        for (EstimateActivity activity : activities) {
            if (activity.getUnitRate() != null && activity.getQuantity() != null)
                totalActivityAmount = totalActivityAmount
                        .add(activity.getUnitRate().multiply(BigDecimal.valueOf(activity.getQuantity())));
        }
        return totalActivityAmount;
    }

    public boolean workflowRequired(final String tenantId, final RequestInfo requestInfo) {
        JSONArray mdmsArray = estimateUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
                CommonConstants.WORKFLOW_REQUIRED_APPCONFIG, tenantId, requestInfo,
                CommonConstants.MODULENAME_WORKS);
        boolean workflowRequired = false;
        if (mdmsArray != null && !mdmsArray.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) mdmsArray.get(0);
            if (jsonMap.get("value").equals("Yes")) {
                workflowRequired = true;
            }
        }
        return workflowRequired;
    }

    public List<DetailedEstimateHelper> searchDetailedEstimatesById(final DetailedEstimate detailedEstimate, final RequestInfo requestInfo) {
        DetailedEstimateSearchContract detailedEstimateSearchContract = DetailedEstimateSearchContract.builder()
                .ids(Arrays.asList(detailedEstimate.getId()))
                .tenantId(detailedEstimate.getTenantId()).build();
        return detailedEstimateJdbcRepository.search(detailedEstimateSearchContract, requestInfo);
    }
}
