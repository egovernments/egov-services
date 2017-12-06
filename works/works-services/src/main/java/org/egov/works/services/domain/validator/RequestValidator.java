package org.egov.works.services.domain.validator;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.web.contract.DetailedEstimateOfflineStatus;
import org.egov.works.commons.web.contract.LOAOfflineStatuses;
import org.egov.works.commons.web.contract.WorkOrderOfflineStatus;
import org.egov.works.services.config.Constants;
import org.egov.works.services.domain.repository.FileStoreRepository;
import org.egov.works.services.domain.service.OfflineStatusService;
import org.egov.works.services.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestValidator {

    @Autowired
    private OfflineStatusService offlineStatusService;

    @Autowired
    private FileStoreRepository fileStoreRepository;

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

    public void validateOfflineStatus(OfflineStatusRequest offlineStatusRequest) {

        List<String> OffStatuses;
        final String[] selectedStatusArr = new String[offlineStatusRequest.getOfflineStatuses().size()];;
        int b = 0;
        Map<String, String> messages = new HashMap<>();

        final String ObjectType = offlineStatusRequest.getOfflineStatuses().get(0).getObjectType();
        OffStatuses = getListOfStatusForObjectType(ObjectType);

        for (int i = 0; i < offlineStatusRequest.getOfflineStatuses().size(); i++) {
            selectedStatusArr[i] = offlineStatusRequest.getOfflineStatuses().get(i).getStatus();
        }

        for (final String statName : offlineStatusService.getStatusNameDetails(selectedStatusArr)) {
            if (!OffStatuses.isEmpty() && !statName.equals(OffStatuses.get(b))) {

                String s = statName + " should be set After status " + OffStatuses.get(OffStatuses.indexOf(statName) - 1);

                messages.put("Please Send Proper order", s);
                break;
            }
            b++;
        }

        final List<OfflineStatus> newOfflineStatus = offlineStatusRequest.getOfflineStatuses();
        for (int a = 0; a < newOfflineStatus.size() - 1; a++)
            if (newOfflineStatus.get(a).getStatusDate() > newOfflineStatus.get(a + 1).getStatusDate()) {
                messages.put("Incorrect Date for Order", "Status Date for " + newOfflineStatus.get(a+1).getStatus()
                        + " should be greater then" + newOfflineStatus.get(a).getStatus());
                break;
            }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);

    }

    private List<String> getListOfStatusForObjectType(final String ObjectType) {

        String[] statusName;
        List<String> OffStatuses = null;
        if(ObjectType.equalsIgnoreCase(CommonConstants.WORKORDER)) {
            final WorkOrderOfflineStatus[] workOrderOfflineStatus = WorkOrderOfflineStatus.values();
            statusName = new String[workOrderOfflineStatus.length];
            for (int j = 0; j < workOrderOfflineStatus.length; j++)
                statusName[j] = workOrderOfflineStatus[j].name();
            OffStatuses = Arrays.asList(statusName);
        } else if(ObjectType.equalsIgnoreCase(CommonConstants.LETTEROFACCEPTANCE)){
            final LOAOfflineStatuses[] loaOfflineStatuses = LOAOfflineStatuses.values();
            statusName = new String[loaOfflineStatuses.length];
            for (int j = 0; j < loaOfflineStatuses.length; j++)
                statusName[j] = loaOfflineStatuses[j].name();
            OffStatuses = Arrays.asList(statusName);
        } else if(ObjectType.equalsIgnoreCase(CommonConstants.DETAILEDESTIMATE)){
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
