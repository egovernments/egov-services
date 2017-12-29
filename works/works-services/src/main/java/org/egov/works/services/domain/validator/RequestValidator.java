package org.egov.works.services.domain.validator;

import java.util.*;

import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.web.contract.DetailedEstimateOfflineStatus;
import org.egov.works.commons.web.contract.LOAOfflineStatuses;
import org.egov.works.commons.web.contract.WorkOrderOfflineStatus;
import org.egov.works.services.config.Constants;
import org.egov.works.services.domain.repository.FileStoreRepository;
import org.egov.works.services.domain.service.OfflineStatusService;
import org.egov.works.services.utils.ServiceUtils;
import org.egov.works.services.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestValidator {

    @Autowired
    private OfflineStatusService offlineStatusService;

    @Autowired
    private FileStoreRepository fileStoreRepository;

    @Autowired
    private ServiceUtils serviceUtils;

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
        if(isUpdate)
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

    private void validateStatus(OfflineStatusRequest offlineStatusRequest, Map<String, String> messages, RequestInfo requestInfo) {

        for(OfflineStatus offlineStatus : offlineStatusRequest.getOfflineStatuses()) {
            if (offlineStatus.getStatus() != null && StringUtils.isNotBlank(offlineStatus.getStatus().getCode())) {
                List<String> filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE, CommonConstants.MODULE_TYPE));
                List<String> filetsValuesList = new ArrayList<>(Arrays.asList(offlineStatus.getStatus().getCode().toUpperCase(), offlineStatus.getObjectType()));
                JSONArray dBStatusArray = serviceUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                        filetsValuesList, offlineStatus.getStatus().getTenantId(), requestInfo,
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
                messages.put(Constants.KEY_OBJECTDATE_STATUSDATE_DE_INVALID,
                        Constants.MESSAGE_OBJECTDATE_STATUSDATE_DE_INVALID);
            } else if (offlineStatus.getObjectType().equalsIgnoreCase(Constants.LETTEROFACCEPTANCE)
                    && offlineStatus.getObjectDate() > offlineStatus.getStatusDate()) {
                messages.put(Constants.KEY_OBJECTDATE_STATUSDATE_LOA_INVALID,
                        Constants.MESSAGE_OBJECTDATE_STATUSDATE_LOA_INVALID);
            } else if (offlineStatus.getObjectType().equalsIgnoreCase(Constants.DETAILEDESTIMATE)
                    && offlineStatus.getObjectDate() > offlineStatus.getStatusDate()) {
                messages.put(Constants.KEY_OBJECTDATE_STATUSDATE_WO_INVALID,
                        Constants.MESSAGE_OBJECTDATE_STATUSDATE_WO_INVALID);
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
        for (int a = 0; a < newOfflineStatus.size() - 1; a++)
            if (newOfflineStatus.get(a).getStatusDate() > newOfflineStatus.get(a + 1).getStatusDate()) {
                messages.put("Incorrect Date for Order", "Status Date for " + newOfflineStatus.get(a + 1).getStatus()
                        + " should be greater then" + newOfflineStatus.get(a).getStatus());
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

                String s = statName + " should be set After status " + OffStatuses.get(OffStatuses.indexOf(statName) - 1);

                messages.put("Please Send Proper order", s);
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
