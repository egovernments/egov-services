package org.egov.web.indexer.enricher;

import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;

public interface ServiceRequestDocumentEnricher {
    boolean matches(ServiceType serviceType, SevaRequest sevaRequest);
    void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document);
}
