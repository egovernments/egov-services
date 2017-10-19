package org.egov.web.indexer.enricher;

import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.egov.web.indexer.repository.contract.ServiceRequestDocument.NO;
import static org.egov.web.indexer.repository.contract.ServiceRequestDocument.YES;

@Service
public class ServiceStatusFlagsDocumentEnricher implements ServiceRequestDocumentEnricher {

    private static final String SERVICE_STATUS = "systemStatus";
    private static final String COMPLAINT_RESOLVED = "COMPLETED";
    private static final String COMPLAINT_WITHDRAWN = "WITHDRAWN";
    private static final String COMPLAINT_REJECTED = "REJECTED";
    private static final String COMPLAINT_PROCESSING = "PROCESSING";
    private static final String COMPLAINT_FORWARDED = "FORWARDED";
    private static final String COMPLAINT_REGISTERED = "REGISTERED";
    private static final String COMPLAINT_REOPENED = "REOPENED";
    private static final String COMPLAINT_ONHOLD = "ONHOLD";
    private static final String DELIVERY_SERVICE_NEW = "DSNEW";
    private static final String DELIVERY_SERVICE_IN_PROGRESS = "DSPROGRESS";
    private static final String DELIVERY_SERVICE_APPROVED = "DSAPPROVED";
    private static final String DELIVERY_SERVICE_REJECTED = "DSREJECTED";
    private static final List<String> IN_PROGRESS_STATUSES =
        Arrays.asList(COMPLAINT_PROCESSING, COMPLAINT_FORWARDED, DELIVERY_SERVICE_IN_PROGRESS, COMPLAINT_ONHOLD);
    private static final List<String> REJECTED_STATUSES =
        Arrays.asList(COMPLAINT_REJECTED, DELIVERY_SERVICE_REJECTED);
    private static final List<String> SUCCESSFULLY_COMPLETED_STATUSES =
        Arrays.asList(COMPLAINT_RESOLVED, COMPLAINT_WITHDRAWN, DELIVERY_SERVICE_APPROVED);
    private static final List<String> REGISTERED_STATUSES =
        Arrays.asList(COMPLAINT_REGISTERED, DELIVERY_SERVICE_NEW);

    @Override
    public boolean matches(ServiceType serviceType, SevaRequest sevaRequest) {
        return sevaRequest.isUpdateRequest();
    }

    @Override
    public void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document) {
        String currentStatus = sevaRequest.getServiceRequest().getDynamicSingleValue(SERVICE_STATUS);
        setRegisteredStatusFlags(document, currentStatus);
        setInProgressStatusFlags(document, currentStatus);
        setSuccessfullyCompletedStatusFlags(document, currentStatus);
        setRejectedStatusFlags(document, currentStatus);
        setReopenedStatusFlags(document, currentStatus);
    }

    private void setReopenedStatusFlags(ServiceRequestDocument document, String currentStatus) {
        if (COMPLAINT_REOPENED.equalsIgnoreCase(currentStatus)) {
            document.setIsInProcess(YES);
            document.setIsAddressed(NO);
            document.setRejected(NO);
            document.setReOpened(YES);
            document.setIsRegistered(NO);
        }
    }

    private void setRejectedStatusFlags(ServiceRequestDocument document, String currentStatus) {
        if (isRejected(currentStatus)) {
            document.setIsInProcess(NO);
            document.setIsAddressed(NO);
            document.setRejected(YES);
            document.setIsRegistered(NO);
        }
    }

    private void setSuccessfullyCompletedStatusFlags(ServiceRequestDocument document, String currentStatus) {
        if (isSuccessfullyCompleted(currentStatus)) {
            document.setIsInProcess(NO);
            document.setIsAddressed(YES);
            document.setRejected(NO);
            document.setIsRegistered(NO);
        }
    }

    private void setInProgressStatusFlags(ServiceRequestDocument document, String currentStatus) {
        if (isInProgress(currentStatus)) {
            document.setIsInProcess(YES);
            document.setIsAddressed(NO);
            document.setRejected(NO);
            document.setIsRegistered(NO);
        }
    }

    private void setRegisteredStatusFlags(ServiceRequestDocument document, String currentStatus) {
        if(isRegistered(currentStatus)) {
            document.setIsRegistered(YES);
            document.setIsInProcess(NO);
            document.setIsAddressed(NO);
            document.setRejected(NO);
            document.setReOpened(NO);
        }
    }

    private boolean isRegistered(String currentStatus) {
        return currentStatusMatches(currentStatus, REGISTERED_STATUSES);
    }

    private boolean isRejected(String currentStatus) {
        return currentStatusMatches(currentStatus, REJECTED_STATUSES);
    }

    private boolean isSuccessfullyCompleted(String currentStatus) {
        return currentStatusMatches(currentStatus, SUCCESSFULLY_COMPLETED_STATUSES);
    }

    private boolean isInProgress(String currentStatus) {
        return currentStatusMatches(currentStatus, IN_PROGRESS_STATUSES);
    }

    private boolean currentStatusMatches(String currentStatus, List<String> statuses) {
        return statuses.stream().anyMatch(status -> status.equalsIgnoreCase(currentStatus));
    }

}
