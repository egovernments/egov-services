package org.egov.works.workorder.domain.validator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.web.contract.LOAStatus;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.repository.EmployeeRepository;
import org.egov.works.workorder.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.workorder.domain.repository.WorksMastersRepository;
import org.egov.works.workorder.domain.service.EstimateService;
import org.egov.works.workorder.domain.service.OfflineStatusService;
import org.egov.works.workorder.web.contract.Contractor;
import org.egov.works.workorder.web.contract.DetailedEstimate;
import org.egov.works.workorder.web.contract.DetailedEstimateStatus;
import org.egov.works.workorder.web.contract.EmployeeResponse;
import org.egov.works.workorder.web.contract.LOAActivity;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceEstimate;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceRequest;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceSearchContract;
import org.egov.works.workorder.web.contract.OfflineStatus;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ramki on 11/11/17.
 */
@Service
public class LetterOfAcceptanceValidator {

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private OfflineStatusService offlineStatusService;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    @Autowired
    private WorksMastersRepository worksMastersRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static void validateUniqueLOANumber(LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            HashMap<String, String> messages, LetterOfAcceptance letterOfAcceptance,
            LetterOfAcceptanceRepository letterOfAcceptanceRepository) {

        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();

        letterOfAcceptanceSearchContract.setTenantId(letterOfAcceptance.getTenantId());
        letterOfAcceptanceSearchContract.setLoaNumbers(Arrays.asList(letterOfAcceptance.getLoaNumber()));

        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOAs(letterOfAcceptanceSearchContract,
                letterOfAcceptanceRequest.getRequestInfo());

        for (LetterOfAcceptance acceptance : letterOfAcceptances) {
            if (!acceptance.getStatus().getCode().equalsIgnoreCase(LOAStatus.CANCELLED.toString())) {
                messages.put(Constants.KEY_INVALID_LOA_EXISTS, Constants.MESSAGE_INVALID_LOA_EXISTS);
                break;
            }
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    public void validateLetterOfAcceptance(final LetterOfAcceptanceRequest letterOfAcceptanceRequest, Boolean isUpdate,
            Boolean isRevision) {
        DetailedEstimate detailedEstimate = null;
        HashMap<String, String> messages = new HashMap<>();
        for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptanceRequest.getLetterOfAcceptances()) {

            for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance
                    .getLetterOfAcceptanceEstimates()) {

                if (!isUpdate) {
                    checkLoaExistForDE(letterOfAcceptanceRequest, messages, letterOfAcceptanceEstimate);
                    if (messages != null && !messages.isEmpty())
                        throw new CustomException(messages);
                }

                List<DetailedEstimate> detailedEstimates = estimateService
                        .getDetailedEstimate(letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber(),
                                letterOfAcceptanceEstimate.getTenantId(), letterOfAcceptanceRequest.getRequestInfo())
                        .getDetailedEstimates();

                if (!detailedEstimates.isEmpty())
                    detailedEstimate = detailedEstimates.get(0);

                validateDetailedEstimate(detailedEstimate, messages, letterOfAcceptance);

                if (messages != null && !messages.isEmpty())
                    throw new CustomException(messages);

                if (!detailedEstimate.getWorkOrderCreated() && (isRevision == null || (isRevision != null && !isRevision))) {
                    validateOfflineStatus(letterOfAcceptanceRequest, messages, letterOfAcceptance,
                            letterOfAcceptanceEstimate);

                    if (messages != null && !messages.isEmpty())
                        throw new CustomException(messages);
                }
            }

            if (!isUpdate && letterOfAcceptance.getLoaNumber() != null && !letterOfAcceptance.getLoaNumber().isEmpty()) {
                validateUniqueLOANumber(letterOfAcceptanceRequest, messages, letterOfAcceptance, letterOfAcceptanceRepository);
                validateLOACreated(letterOfAcceptanceRequest, messages, letterOfAcceptance, letterOfAcceptanceRepository);
            }
            if (isUpdate) {
                checkLOAExists(letterOfAcceptanceRequest, messages, letterOfAcceptance);
            }

            validateLOA(messages, letterOfAcceptance, letterOfAcceptanceRequest.getRequestInfo(), detailedEstimate);
            validateEngineerIncharge(letterOfAcceptanceRequest, messages, letterOfAcceptance);
            if (letterOfAcceptance.getSpillOverFlag()
                    || letterOfAcceptance.getWorkFlowDetails().getAction().equalsIgnoreCase("APPROVE"))
                validateCouncilDetails(messages, letterOfAcceptance);

            if (messages != null && !messages.isEmpty())
                throw new CustomException(messages);

        }

    }

    private void validateEngineerIncharge(final LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            HashMap<String, String> messages, LetterOfAcceptance letterOfAcceptance) {
        if (letterOfAcceptance.getEngineerIncharge() == null || (letterOfAcceptance.getEngineerIncharge() != null
                && (StringUtils.isBlank(letterOfAcceptance.getEngineerIncharge().getCode())))) {
            messages.put(Constants.KEY_LOA_ENGINEERINCHARGE_NULL, Constants.MESSAGE_LOA_ENGINEERINCHARGE_NULL);
        }

        if (letterOfAcceptance.getEngineerIncharge() != null
                && StringUtils.isNotBlank(letterOfAcceptance.getEngineerIncharge().getCode())) {
            EmployeeResponse employeeResponse = employeeRepository.getEmployeeByCode(
                    letterOfAcceptance.getEngineerIncharge().getCode(), letterOfAcceptance.getTenantId(),
                    letterOfAcceptanceRequest.getRequestInfo());
            if (employeeResponse.getEmployees().isEmpty()) {
                messages.put(Constants.KEY_LOA_ENGINEERINCHARGE_INVALID, Constants.MESSAGE_LOA_ENGINEERINCHARGE_INVALID);
            }
        }
    }

    private void checkLoaExistForDE(final LetterOfAcceptanceRequest letterOfAcceptanceRequest, HashMap<String, String> messages,
            LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();
        letterOfAcceptanceSearchContract
                .setDetailedEstimateNumberLike(letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber());
        letterOfAcceptanceSearchContract.setTenantId(letterOfAcceptanceEstimate.getTenantId());
        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOAs(letterOfAcceptanceSearchContract,
                letterOfAcceptanceRequest.getRequestInfo());

        for (LetterOfAcceptance acceptance : letterOfAcceptances) {
            if (acceptance.getStatus() != null
                    && !acceptance.getStatus().getCode().equalsIgnoreCase(LOAStatus.CANCELLED.toString())) {
                messages.put(Constants.KEY_WORKS_LOA_DE_EXISTS, Constants.MESSAGE_WORKS_LOA_DE_EXISTS);
                break;
            }
        }
    }

    private void validateCouncilDetails(HashMap<String, String> messages, LetterOfAcceptance letterOfAcceptance) {
        if (letterOfAcceptance.getSpillOverFlag() && letterOfAcceptance.getCouncilResolutionDate() == null) {
            messages.put(Constants.KEY_NULL_COUNCILRESOLUTIONDATE, Constants.MESSAGE_NULL_COUNCILRESOLUTIONDATE);
        }

        if (letterOfAcceptance.getSpillOverFlag() && StringUtils.isBlank(letterOfAcceptance.getCouncilResolutionNumber())) {
            messages.put(Constants.KEY_NULL_COUNCILRESOLUTIONNUMBER, Constants.MESSAGE_NULL_COUNCILRESOLUTIONNUMBER);
        }
    }

    private static void validateLOACreated(LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            HashMap<String, String> messages, LetterOfAcceptance letterOfAcceptance,
            LetterOfAcceptanceRepository letterOfAcceptanceRepository) {

        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();

        letterOfAcceptanceSearchContract.setTenantId(letterOfAcceptance.getTenantId());
        letterOfAcceptanceSearchContract.setDetailedEstimateNumbers(Arrays
                .asList(letterOfAcceptance.getLetterOfAcceptanceEstimates().get(0).getDetailedEstimate().getEstimateNumber()));

        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOAs(letterOfAcceptanceSearchContract,
                letterOfAcceptanceRequest.getRequestInfo());

        for (LetterOfAcceptance acceptance : letterOfAcceptances) {
            if (!acceptance.getStatus().getCode().equalsIgnoreCase(LOAStatus.CANCELLED.toString())) {
                messages.put(Constants.KEY_INVALID_LOA_DE_EXISTS, Constants.MESSAGE_INVALID_LOA_DE_EXISTS);
            }
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateOfflineStatus(final LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            HashMap<String, String> messages, LetterOfAcceptance letterOfAcceptance,
            LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
        OfflineStatus offlineStatus = null;
        List<OfflineStatus> offlineStatuses = offlineStatusService
                .getOfflineStatus(letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber(),
                        letterOfAcceptance.getTenantId(), letterOfAcceptanceRequest.getRequestInfo())
                .getOfflineStatuses();
        if (!offlineStatuses.isEmpty())
            offlineStatus = offlineStatuses.get(0);

        if (offlineStatus == null) {
            messages.put(Constants.KEY_DETAILEDESTIMATE_OFFLINE_STATUS,
                    Constants.MESSAGE_DETAILEDESTIMATE_OFFLINE_STATUS);
        }

        if (offlineStatus != null && letterOfAcceptance.getLoaDate() != null && offlineStatus.getStatusDate() > letterOfAcceptance.getLoaDate())
            messages.put(Constants.KEY_FUTUREDATE_LOADATE_OFFLINESTATUS,
                    Constants.MESSAGE_FUTUREDATE_LOADATE_OFFLINESTATUS);

    }

    private void checkLOAExists(LetterOfAcceptanceRequest letterOfAcceptanceRequest, HashMap<String, String> messages,
            LetterOfAcceptance letterOfAcceptance) {
        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();

        letterOfAcceptanceSearchContract.setTenantId(letterOfAcceptance.getTenantId());
        if (letterOfAcceptance.getId() != null && letterOfAcceptance.getId().isEmpty())
            letterOfAcceptanceSearchContract.setIds(Arrays.asList(letterOfAcceptance.getId()));
        letterOfAcceptanceSearchContract.setLoaNumbers(Arrays.asList(letterOfAcceptance.getLoaNumber()));

        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOAs(letterOfAcceptanceSearchContract,
                letterOfAcceptanceRequest.getRequestInfo());

        if (letterOfAcceptances.isEmpty()) {
            messages.put(Constants.KEY_INVALID_LOA, Constants.MESSAGE_INVALID_LOA);
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateLOA(HashMap<String, String> messages,
            LetterOfAcceptance letterOfAcceptance, final RequestInfo requestInfo, final DetailedEstimate detailedEstimate) {
        if (letterOfAcceptance.getLoaDate() != null
                && letterOfAcceptance.getLoaDate() > new Date().getTime()) {
            messages.put(Constants.KEY_FUTUREDATE_LOADATE, Constants.MESSAGE_FUTUREDATE_LOADATE);
        }

        if (letterOfAcceptance.getFileDate() > new Date().getTime()) {
            messages.put(Constants.KEY_FUTUREDATE_FILEDATE, Constants.MESSAGE_FUTUREDATE_FILEDATE);
        }

        if (letterOfAcceptance.getContractor() == null || (letterOfAcceptance.getContractor() != null
                && StringUtils.isBlank(letterOfAcceptance.getContractor().getCode()))) {
            messages.put(Constants.KEY_LOA_CONRACTOR_REQUIRED, Constants.MESSAGE_LOA_CONRACTOR_REQUIRED);
        }

        if (letterOfAcceptance.getContractor() != null && StringUtils.isNotBlank(letterOfAcceptance.getContractor().getCode())) {
            List<Contractor> contractors = worksMastersRepository.searchContractorsByCodes(letterOfAcceptance.getTenantId(),
                    letterOfAcceptance.getContractor().getCode(), requestInfo);
            if (contractors.isEmpty()) {
                messages.put(Constants.KEY_LOA_CONRACTOR_INACTIVE, Constants.MESSAGE_LOA_CONRACTOR_INACTIVE);
            }
        }

        if (detailedEstimate.getWorkOrderCreated()
                && (letterOfAcceptance.getLoaNumber() == null || letterOfAcceptance.getLoaNumber().isEmpty())) {
            messages.put(Constants.KEY_WORKORDER_LOANUMBER_REQUIRED, Constants.MESSAGE_WORKORDER_LOANUMBER_REQUIRED);
        }

        if (letterOfAcceptance.getDefectLiabilityPeriod() <= 0) {
            messages.put(Constants.KEY_WORKORDER_DLP_ZERO, Constants.MESSAGE_WORKORDER_DLP_ZERO);
        }

        if (letterOfAcceptance.getContractPeriod().compareTo(BigDecimal.ZERO) <= 0) {
            messages.put(Constants.KEY_WORKORDER_CP_ZERO, Constants.MESSAGE_WORKORDER_CP_ZERO);
        }

    }

    public void validateLOAAmount(LetterOfAcceptance letterOfAcceptance,
            final DetailedEstimate detailedEstimate) {
        BigDecimal approvedAmount = BigDecimal.ZERO;
        HashMap<String, String> messages = new HashMap<>();
        for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance.getLetterOfAcceptanceEstimates()) {
            for (LOAActivity loaActivity : letterOfAcceptanceEstimate.getLoaActivities()) {
                approvedAmount = approvedAmount.add(loaActivity.getApprovedAmount());
            }
        }

        if (detailedEstimate.getApprovedDate() != null && detailedEstimate.getApprovedDate() > letterOfAcceptance.getLoaDate()) {
            messages.put(Constants.KEY_FUTUREDATE_LOADATE_DETAILEDESTIMATE,
                    Constants.MESSAGE_FUTUREDATE_LOADATE_DETAILEDESTIMATE);
        }

        approvedAmount = approvedAmount.add(approvedAmount
                .multiply(BigDecimal.valueOf(letterOfAcceptance.getTenderFinalizedPercentage())).divide(new BigDecimal(100)));

        if (letterOfAcceptance.getLoaAmount().compareTo(approvedAmount) != 0) {
            messages.put(Constants.KEY_WORKORDER_LOAAMOUNT_LOAACTIVITYAMOUNT_INVALID,
                    Constants.MESSAGE_WORKORDER_LOAAMOUNT_LOAACTIVITYAMOUNT_INVALID);
        }

        BigDecimal workValue = detailedEstimate.getWorkValue().add(detailedEstimate.getWorkValue()
                .multiply(BigDecimal.valueOf(letterOfAcceptance.getTenderFinalizedPercentage())).divide(new BigDecimal(100)));
        if (letterOfAcceptance.getLoaAmount().compareTo(workValue) != 0) {
            messages.put(Constants.KEY_WORKORDER_LOAAMOUNT_WORKVALUE_INPROPER,
                    Constants.MESSAGE_WORKORDER_LOAAMOUNT_WORKVALUE_INPROPER);
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateDetailedEstimate(DetailedEstimate detailedEstimate, HashMap<String, String> messages,
            LetterOfAcceptance letterOfAcceptance) {

        if (detailedEstimate == null)
            messages.put(Constants.KEY_DETAILEDESTIMATE_EXIST, Constants.MESSAGE_DETAILEDESTIMATE_EXIST);

        if (detailedEstimate != null && detailedEstimate.getStatus() != null && !detailedEstimate.getStatus().getCode().toString()
                .equalsIgnoreCase(DetailedEstimateStatus.TECHNICAL_SANCTIONED.toString())) {
            messages.put(Constants.KEY_DETAILEDESTIMATE_STATUS, Constants.MESSAGE_DETAILEDESTIMATE_STATUS);
        }

        if (detailedEstimate != null && detailedEstimate.getApprovedDate() != null
                && letterOfAcceptance.getLoaDate() != null && detailedEstimate.getApprovedDate() > letterOfAcceptance.getLoaDate()) {
            messages.put(Constants.KEY_INVALID_LOADATE_DATE, Constants.MESSAGE_INVALID_LOADATE_DATE);
        }

    }

    @SuppressWarnings("static-access")
    public LetterOfAcceptance searchAbstractEstimate(LetterOfAcceptance letterOfAcceptance,
            final RequestInfo requestInfo) {

        @SuppressWarnings("unused")
        LetterOfAcceptance savedLetterOfAcceptance = new LetterOfAcceptance();

        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria = new LetterOfAcceptanceSearchContract();
        letterOfAcceptanceSearchCriteria.builder().tenantId(letterOfAcceptance.getTenantId())
                .loaNumbers(Arrays.asList(letterOfAcceptance.getLoaNumber())).build();

        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository
                .searchLOAs(letterOfAcceptanceSearchCriteria, requestInfo);

        if (!letterOfAcceptances.isEmpty())
            savedLetterOfAcceptance = letterOfAcceptances.get(0);

        return null;
    }

}
