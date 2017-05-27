package org.egov.web.indexer.service;

import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.enricher.ServiceRequestDocumentEnricher;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.ServiceTypeRepository;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {
    private List<ServiceRequestDocumentEnricher> documentEnrichers;
    private ServiceTypeRepository serviceTypeRepository;

    public DocumentService(List<ServiceRequestDocumentEnricher> documentEnrichers,
                           ServiceTypeRepository serviceTypeRepository) {
        this.documentEnrichers = documentEnrichers;
        this.serviceTypeRepository = serviceTypeRepository;
    }

    public ServiceRequestDocument enrich(SevaRequest sevaRequest) {
        final ServiceRequestDocument document = new ServiceRequestDocument();
        final ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
        final ServiceType serviceType = serviceTypeRepository
            .fetchServiceTypeByCode(serviceRequest.getServiceCode(), serviceRequest.getTenantId());
        documentEnrichers.stream()
            .filter(enricher -> enricher.matches(serviceType, sevaRequest))
            .forEach(documentEnricher -> documentEnricher.enrich(serviceType, sevaRequest, document));
        return document;
    }
}
