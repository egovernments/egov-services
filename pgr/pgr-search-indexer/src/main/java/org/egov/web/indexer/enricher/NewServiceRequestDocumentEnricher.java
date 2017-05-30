package org.egov.web.indexer.enricher;

import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

import static org.egov.web.indexer.repository.contract.ServiceRequestDocument.NO;
import static org.egov.web.indexer.repository.contract.ServiceRequestDocument.YES;

@Service
public class NewServiceRequestDocumentEnricher implements ServiceRequestDocumentEnricher {

    @Override
    public boolean matches(ServiceType serviceType, SevaRequest sevaRequest) {
        return sevaRequest.isNewRequest();
    }

    @Override
    public void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document) {
        document.setIsClosed(NO);
        document.setIsWithinSLA(YES);
        document.setServiceStatusAgeingDaysFromDue(0);
        document.setServiceDuration(0);
        document.setServiceStatusPeriod(0);
        document.setDurationRange("");
    }
}

