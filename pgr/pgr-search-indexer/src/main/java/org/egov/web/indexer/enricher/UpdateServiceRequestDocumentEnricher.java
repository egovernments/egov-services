package org.egov.web.indexer.enricher;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.egov.pgr.common.date.DateFormatter.toDate;
import static org.egov.web.indexer.repository.contract.ServiceRequestDocument.NO;
import static org.egov.web.indexer.repository.contract.ServiceRequestDocument.YES;

@Service
public class UpdateServiceRequestDocumentEnricher implements ServiceRequestDocumentEnricher {

    private static final String SERVICE_STATUS = "systemStatus";
    private static final String COMPLETED = "COMPLETED";
    private static final String WITHDRAWN = "WITHDRAWN";
    private static final String REJECTED = "REJECTED";
    private static final int DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;
    private List<String> END_STATUSES = Arrays.asList(COMPLETED, WITHDRAWN, REJECTED);

    @Override
    public boolean matches(ServiceType serviceType, SevaRequest sevaRequest) {
        return sevaRequest.isUpdateRequest();
    }

    @Override
    public void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document) {
        final ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
        String currentStatus = serviceRequest.getDynamicSingleValue(SERVICE_STATUS);
        final Date createdDate = toDate(serviceRequest.getCreatedDate());
        setServiceRequestClosedDetails(document, currentStatus, createdDate);
        setSLADetails(document, createdDate, serviceType);
    }

    private void setServiceRequestClosedDetails(ServiceRequestDocument document, String currentStatus, Date createdDate) {
        if (isServiceRequestClosed(currentStatus)) {
            setClosedServiceRequestDetails(document, createdDate);
        } else {
            document.setIsClosed(NO);
            document.setServiceDuration(0);
            document.setDurationRange("");
        }
    }

    private void setSLADetails(ServiceRequestDocument document, Date createdDate, ServiceType serviceType) {
        if (document.getCreatedDate() == null) {
            return;
        }
        final long days = getServiceDurationInDays(createdDate);
        document.setServiceStatusPeriod(days);
        final Date currentDate = new Date();
        Date lastDateToResolve = DateUtils.addHours(createdDate, serviceType.getSlaHours());
        final boolean isWithinSLA = lastDateToResolve.getTime() - currentDate.getTime() >= 0;
        if (isWithinSLA) {
			document.setServiceStatusAgeingDaysFromDue(0);
			document.setIsWithinSLA(YES);
		} else {
			final long daysCrossedSLA =
				Math.abs(currentDate.getTime() - lastDateToResolve.getTime()) / DAY_IN_MILLISECONDS;
			document.setServiceStatusAgeingDaysFromDue(daysCrossedSLA);
			document.setIsWithinSLA(NO);
		}
    }

    private void setClosedServiceRequestDetails(ServiceRequestDocument document, Date createdDate) {
        document.setIsClosed(YES);
        if (createdDate == null) {
            return;
        }
        final long duration = getServiceDurationInDays(createdDate);
        document.setServiceDuration(duration);
        if (duration < 3)
            document.setDurationRange("(<3 days)");
        else if (duration < 7)
            document.setDurationRange("(3-7 days)");
        else if (duration < 15)
            document.setDurationRange("(8-15 days)");
        else if (duration < 30)
            document.setDurationRange("(16-30 days)");
        else
            document.setDurationRange("(>30 days)");
    }

    private boolean isServiceRequestClosed(String currentStatus) {
        return END_STATUSES.stream().anyMatch(status -> status.equalsIgnoreCase(currentStatus));
    }

    private long getServiceDurationInDays(Date createdDate) {
        final long differenceInMilliSeconds = Math.abs(new Date().getTime() - createdDate.getTime());
        return differenceInMilliSeconds / DAY_IN_MILLISECONDS;
    }

}

