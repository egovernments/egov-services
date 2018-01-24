package org.egov.works.services.domain.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.web.contract.DetailedEstimateOfflineStatus;
import org.egov.works.commons.web.contract.DetailedEstimateStatus;
import org.egov.works.commons.web.contract.LOAOfflineStatuses;
import org.egov.works.commons.web.contract.LOAStatus;
import org.egov.works.commons.web.contract.WorkOrderOfflineStatus;
import org.egov.works.commons.web.contract.WorkOrderStatus;
import org.egov.works.services.config.Constants;
import org.egov.works.services.domain.repository.EstimateRepository;
import org.egov.works.services.domain.repository.FileStoreRepository;
import org.egov.works.services.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.services.domain.repository.WorkOrderRepository;
import org.egov.works.services.domain.service.OfflineStatusService;
import org.egov.works.services.utils.ServiceUtils;
import org.egov.works.services.web.contract.DetailedEstimate;
import org.egov.works.services.web.contract.DetailedEstimateResponse;
import org.egov.works.services.web.contract.DetailedEstimateSearchContract;
import org.egov.works.services.web.contract.DocumentDetail;
import org.egov.works.services.web.contract.DocumentDetailRequest;
import org.egov.works.services.web.contract.DocumentDetailSearchCriteria;
import org.egov.works.services.web.contract.EstimateAppropriationSearchContract;
import org.egov.works.services.web.contract.LetterOfAcceptance;
import org.egov.works.services.web.contract.LetterOfAcceptanceResponse;
import org.egov.works.services.web.contract.LetterOfAcceptanceSearchContract;
import org.egov.works.services.web.contract.OfflineStatus;
import org.egov.works.services.web.contract.OfflineStatusRequest;
import org.egov.works.services.web.contract.OfflineStatusResponse;
import org.egov.works.services.web.contract.OfflineStatusSearchContract;
import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.contract.WorkOrder;
import org.egov.works.services.web.contract.WorkOrderResponse;
import org.egov.works.services.web.contract.WorkOrderSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;

@Service
public class RequestValidator {

    private static final String SHOULD_BE_GREATER_THEN = " should be greater then";
    private static final String STATUS_DATE_FOR = "Status Date for ";
    private static final String SHOULD_BE_SET_AFTER_STATUS = " should be set After status ";
    public static final String KEY_OFFLINESTATUS_STATUS_ORDER_INCORRECT = "works.offlinestatus.order.incorrect";
    public static final String KEY_OFFLINESTATUS_DATE_ORDER_INCORRECT = "works.offlinestatus.order.incorrect";
    @Autowired
    private OfflineStatusService offlineStatusService;

    @Autowired
    private FileStoreRepository fileStoreRepository;

    @Autowired
    private ServiceUtils serviceUtils;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    public void validateAppropriationSearchContract(EstimateAppropriationSearchContract estimateAppropriationSearchContract) {
        Map<String, String> messages = new HashMap<>();
        if ((estimateAppropriationSearchContract.getObjectNumber() != null
                || estimateAppropriationSearchContract.getAbstractEstimateNumbers() != null
                || estimateAppropriationSearchContract.getDetailedEstimateNumbers() != null)
                && estimateAppropriationSearchContract.getObjectType() == null) {
            messages.put(Constants.KEY_OBJECTTYPE_INVALID,
                    Constants.MESSAGE_OBJECTTYPE_INVALID);
            throw new CustomException(messages);
        }
    }

    public void validateOfflineStatus(OfflineStatusRequest offlineStatusRequest, Boolean isUpdate) {

        Map<String, String> messages = new HashMap<>();

        validateObjectDateAndStatusDate(offlineStatusRequest, messages);
        if (isUpdate)
            validateObjectTypeAndObjectNumber(offlineStatusRequest, messages);

        List<String> OffStatuses;
        final String ObjectType = offlineStatusRequest.getOfflineStatuses().get(0).getObjectType();
        OffStatuses = getListOfStatusForObjectType(ObjectType);

        validateStatusNameOrder(OffStatuses, offlineStatusRequest, messages);
        validateStatusDate(offlineStatusRequest, messages);
        validateStatus(offlineStatusRequest, messages, offlineStatusRequest.getRequestInfo());

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);

    }

    private void validateStatus(OfflineStatusRequest offlineStatusRequest, Map<String, String> messages,
            RequestInfo requestInfo) {

        for (OfflineStatus offlineStatus : offlineStatusRequest.getOfflineStatuses()) {
            if (offlineStatus.getStatus() != null && StringUtils.isNotBlank(offlineStatus.getStatus().getCode())) {
                List<String> filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE, CommonConstants.MODULE_TYPE));
                List<String> filetsValuesList = new ArrayList<>(
                        Arrays.asList(offlineStatus.getStatus().getCode().toUpperCase(), offlineStatus.getObjectType()));
                JSONArray dBStatusArray = serviceUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                        filetsValuesList, offlineStatus.getTenantId(), requestInfo,
                        CommonConstants.MODULENAME_WORKS);
                if (dBStatusArray != null && dBStatusArray.isEmpty())
                    messages.put(Constants.KEY_OFFLINE_STATUS_VALUE_INVALID,
                            Constants.MESSAGE_OFFLINE_STATUS_VALUE_INVALID);
            }
        }

    }

    private void validateObjectDateAndStatusDate(OfflineStatusRequest offlineStatusRequest, Map<String, String> messages) {
        for (OfflineStatus offlineStatus : offlineStatusRequest.getOfflineStatuses()) {
            if (offlineStatus.getObjectType().equalsIgnoreCase(Constants.WORKORDER)
                    && offlineStatus.getObjectDate() > offlineStatus.getStatusDate()) {
                messages.put(Constants.KEY_OBJECTDATE_STATUSDATE_WO_INVALID,
                        Constants.MESSAGE_OBJECTDATE_STATUSDATE_WO_INVALID);
            } else if (offlineStatus.getObjectType().equalsIgnoreCase(Constants.LETTEROFACCEPTANCE)
                    && offlineStatus.getObjectDate() > offlineStatus.getStatusDate()) {
                messages.put(Constants.KEY_OBJECTDATE_STATUSDATE_LOA_INVALID,
                        Constants.MESSAGE_OBJECTDATE_STATUSDATE_LOA_INVALID);
            } else if (offlineStatus.getObjectType().equalsIgnoreCase(Constants.DETAILEDESTIMATE)
                    && offlineStatus.getObjectDate() > offlineStatus.getStatusDate()) {
                messages.put(Constants.KEY_OBJECTDATE_STATUSDATE_DE_INVALID,
                        Constants.MESSAGE_OBJECTDATE_STATUSDATE_DE_INVALID);
            }

            if (offlineStatus.getObjectType().equalsIgnoreCase(CommonConstants.DETAILEDESTIMATE)) {
                DetailedEstimateSearchContract detailedEstimateSearchContract = new DetailedEstimateSearchContract();
                detailedEstimateSearchContract.setDetailedEstimateNumbers(Arrays.asList(offlineStatus.getObjectNumber()));
                detailedEstimateSearchContract.setStatuses(Arrays.asList(DetailedEstimateStatus.TECHNICAL_SANCTIONED.toString()));
                DetailedEstimateResponse detailedEstimateResponse = estimateRepository.getDetailedEstimateByEstimateNumber(
                        detailedEstimateSearchContract, offlineStatus.getTenantId(), offlineStatusRequest.getRequestInfo());

                if (detailedEstimateResponse.getDetailedEstimates().isEmpty()) {
                    messages.put(Constants.KEY_OFFLINESTATUS_DE_NOT_EXIST, Constants.MESSAGE_OFFLINESTATUS_DE_NOT_EXIST);

                } else if (detailedEstimateResponse != null && detailedEstimateResponse.getDetailedEstimates() != null
                        && !detailedEstimateResponse.getDetailedEstimates().isEmpty()) {

                    DetailedEstimate detailedEstimate = detailedEstimateResponse.getDetailedEstimates().get(0);

                    if (detailedEstimate.getApprovedDate() > offlineStatus.getStatusDate()) {
                        messages.put(Constants.KEY_OBJECTDATE_STATUSDATE_DE_INVALID,
                                Constants.MESSAGE_OBJECTDATE_STATUSDATE_DE_INVALID);
                    }
                }
            }

            if (offlineStatus.getObjectType().equalsIgnoreCase(CommonConstants.LETTEROFACCEPTANCE)) {
                LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();
                letterOfAcceptanceSearchContract.setLoaNumbers(Arrays.asList(offlineStatus.getObjectNumber()));
                letterOfAcceptanceSearchContract.setStatuses(Arrays.asList(LOAStatus.APPROVED.toString()));
                LetterOfAcceptanceResponse letterOfAcceptanceResponse = letterOfAcceptanceRepository.searchLOAByLOANumber(
                        letterOfAcceptanceSearchContract, offlineStatus.getTenantId(), offlineStatusRequest.getRequestInfo());

                if (letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty()) {
                    messages.put(Constants.KEY_OFFLINESTATUS_LOA_NOT_EXIST, Constants.MESSAGE_OFFLINESTATUS_LOA_NOT_EXIST);
                } else if (letterOfAcceptanceResponse != null && letterOfAcceptanceResponse.getLetterOfAcceptances() != null
                        && !letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty()) {

                    LetterOfAcceptance letterOfAcceptance = letterOfAcceptanceResponse.getLetterOfAcceptances().get(0);

                    if (letterOfAcceptance.getApprovedDate() > offlineStatus.getStatusDate()) {
                        messages.put(Constants.KEY_OBJECTDATE_STATUSDATE_LOA_INVALID,
                                Constants.MESSAGE_OBJECTDATE_STATUSDATE_LOA_INVALID);
                    }
                }
            }

            if (offlineStatus.getObjectType().equalsIgnoreCase(CommonConstants.WORKORDER)) {

                WorkOrderSearchContract workOrderSearchContract = new WorkOrderSearchContract();
                workOrderSearchContract.setWorkOrderNumbers(Arrays.asList(offlineStatus.getObjectNumber()));
                workOrderSearchContract.setStatuses(Arrays.asList(WorkOrderStatus.APPROVED.toString()));

                WorkOrderResponse workOrderResponse = workOrderRepository.searchWorkOrder(workOrderSearchContract,
                        offlineStatus.getTenantId(), offlineStatusRequest.getRequestInfo());

                if (workOrderResponse.getWorkOrders().isEmpty()) {
                    messages.put(Constants.KEY_OFFLINESTATUS_WO_NOT_EXIST, Constants.MESSAGE_OFFLINESTATUS_WO_NOT_EXIST);
                } else if (workOrderResponse != null && workOrderResponse.getWorkOrders() != null
                        && !workOrderResponse.getWorkOrders().isEmpty()) {

                    WorkOrder workOrder = workOrderResponse.getWorkOrders().get(0);

                    if (workOrder.getWorkOrderDate() > offlineStatus.getStatusDate()) {
                        messages.put(Constants.KEY_OBJECTDATE_STATUSDATE_WO_INVALID,
                                Constants.MESSAGE_OBJECTDATE_STATUSDATE_WO_INVALID);
                    }
                }
            }

        }
    }

    private void validateObjectTypeAndObjectNumber(OfflineStatusRequest offlineStatusRequest, Map<String, String> messages) {
        for (OfflineStatus offlineStatus : offlineStatusRequest.getOfflineStatuses()) {
            OfflineStatusSearchContract offlineStatusSearchContract = new OfflineStatusSearchContract();
            offlineStatusSearchContract.setIds(Arrays.asList(offlineStatus.getId()));
            offlineStatusSearchContract.setTenantId(offlineStatus.getTenantId());
            OfflineStatusResponse offlineStatusResponse = offlineStatusService.search(offlineStatusSearchContract,
                    offlineStatusRequest.getRequestInfo());
            if (offlineStatusResponse.getOfflineStatuses() == null && offlineStatusResponse.getOfflineStatuses().isEmpty()) {

                messages.put(Constants.KEY_OFFLINESTATUS_INVALID,
                        Constants.MESSAGE_OFFLINESTATUS_INVALID);
            }
            if (offlineStatusResponse.getOfflineStatuses() != null && !offlineStatusResponse.getOfflineStatuses().isEmpty()) {
                OfflineStatus savedOfflineStatus = offlineStatusResponse.getOfflineStatuses().get(0);
                if (!savedOfflineStatus.getObjectType().equalsIgnoreCase(offlineStatus.getObjectType())) {
                    messages.put(Constants.KEY_OFFLINESTATUS_INVALID_OBJECTTYPE,
                            Constants.MESSAGE_OFFLINESTATUS_INVALID_OBJECTTYPE);
                }
                if (!savedOfflineStatus.getObjectNumber().equalsIgnoreCase(offlineStatus.getObjectNumber())) {
                    messages.put(Constants.KEY_OFFLINESTATUS_INVALID_OBJECTNUMBER,
                            Constants.MESSAGE_OFFLINESTATUS_INVALID_OBJECTNUMBER);
                }
            }
        }
    }

    private void validateStatusDate(OfflineStatusRequest offlineStatusRequest, Map<String, String> messages) {
        final List<OfflineStatus> newOfflineStatus = offlineStatusRequest.getOfflineStatuses();
        StringBuilder s = new StringBuilder();
        for (int a = 0; a < newOfflineStatus.size() - 1; a++)
            if (newOfflineStatus.get(a).getStatusDate() > newOfflineStatus.get(a + 1).getStatusDate()) {
                messages.put(KEY_OFFLINESTATUS_DATE_ORDER_INCORRECT,
                        s.append(STATUS_DATE_FOR).append(newOfflineStatus.get(a + 1).getStatus())
                                .append(SHOULD_BE_GREATER_THEN).append(newOfflineStatus.get(a).getStatus()).toString());
                break;
            }
    }

    private void validateStatusNameOrder(List<String> OffStatuses, final OfflineStatusRequest offlineStatusRequest,
            Map<String, String> messages) {
        int b = 0;
        final String[] selectedStatusArr = new String[offlineStatusRequest.getOfflineStatuses().size()];
        for (int i = 0; i < offlineStatusRequest.getOfflineStatuses().size(); i++) {
            selectedStatusArr[i] = offlineStatusRequest.getOfflineStatuses().get(i).getStatus().getCode();
        }

        for (final String statName : offlineStatusService.getStatusNameDetails(selectedStatusArr)) {
            if (!OffStatuses.isEmpty() && !statName.equals(OffStatuses.get(b))) {

                StringBuilder s = new StringBuilder();
                s.append(statName).append(SHOULD_BE_SET_AFTER_STATUS).append(OffStatuses.get(OffStatuses.indexOf(statName) - 1));
                messages.put(KEY_OFFLINESTATUS_STATUS_ORDER_INCORRECT, s.toString());
                break;
            }
            b++;
        }
    }

    private List<String> getListOfStatusForObjectType(final String ObjectType) {

        String[] statusName;
        List<String> OffStatuses = null;
        if (ObjectType.equalsIgnoreCase(Constants.WORKORDER)) {
            final WorkOrderOfflineStatus[] workOrderOfflineStatus = WorkOrderOfflineStatus.values();
            statusName = new String[workOrderOfflineStatus.length];
            for (int j = 0; j < workOrderOfflineStatus.length; j++)
                statusName[j] = workOrderOfflineStatus[j].name();
            OffStatuses = Arrays.asList(statusName);
        } else if (ObjectType.equalsIgnoreCase(Constants.LETTEROFACCEPTANCE)) {
            final LOAOfflineStatuses[] loaOfflineStatuses = LOAOfflineStatuses.values();
            statusName = new String[loaOfflineStatuses.length];
            for (int j = 0; j < loaOfflineStatuses.length; j++)
                statusName[j] = loaOfflineStatuses[j].name();
            OffStatuses = Arrays.asList(statusName);
        } else if (ObjectType.equalsIgnoreCase(Constants.DETAILEDESTIMATE)) {
            final DetailedEstimateOfflineStatus[] detailedEstimateOfflineStatus = DetailedEstimateOfflineStatus.values();
            statusName = new String[detailedEstimateOfflineStatus.length];
            for (int j = 0; j < detailedEstimateOfflineStatus.length; j++)
                statusName[j] = detailedEstimateOfflineStatus[j].name();
            OffStatuses = Arrays.asList(statusName);
        }

        return OffStatuses;
    }

    public void validateDocuments(final DocumentDetailRequest documentDetailRequest) {

        Map<String, String> messages = new HashMap<>();
        for (DocumentDetail documentDetail : documentDetailRequest.getDocumentDetails()) {
            if (StringUtils.isNotBlank(documentDetail.getFileStore())) {
                boolean fileExists = fileStoreRepository.searchFileStore(documentDetail.getTenantId(),
                        documentDetail.getFileStore(), documentDetailRequest.getRequestInfo());
                if (!fileExists) {
                    messages.put(Constants.KEY_FILESTORE_INVALID, Constants.MESSAGE_FILESTORE_INVALID);
                }
            }
        }
        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    public void validateSearchDocuments(final DocumentDetailSearchCriteria documentDetailSearchCriteria) {
        Map<String, String> messages = new HashMap<>();
        if (StringUtils.isBlank(documentDetailSearchCriteria.getTenantId())) {
            messages.put(Constants.KEY_TENANTID_INVALID, Constants.MESSAGE_TENANTID_INVALID);
        }
        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);

    }
}
