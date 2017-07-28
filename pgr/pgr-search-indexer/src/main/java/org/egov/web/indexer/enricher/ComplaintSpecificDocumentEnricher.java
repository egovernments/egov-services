package org.egov.web.indexer.enricher;

import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

@Service
public class ComplaintSpecificDocumentEnricher implements ServiceRequestDocumentEnricher {

    private static final String RECEIVING_MODE = "systemReceivingMode";

    @Override
    public boolean matches(ServiceType serviceType, SevaRequest sevaRequest) {
        return serviceType.isComplaintType();
    }

    @Override
    public void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document) {
        final ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
        document.setReceivingMode(serviceRequest.getDynamicSingleValue(RECEIVING_MODE));
    }

}


