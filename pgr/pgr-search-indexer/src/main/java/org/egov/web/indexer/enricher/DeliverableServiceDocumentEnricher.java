package org.egov.web.indexer.enricher;

import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang.StringUtils.isEmpty;

@Service
public class DeliverableServiceDocumentEnricher implements ServiceRequestDocumentEnricher {

    private static final String PROCESSING_FEE = "PROCESSINGFEE";

    @Override
    public boolean matches(ServiceType serviceType, SevaRequest sevaRequest) {
        return serviceType.isDeliverableServiceType();
    }

    @Override
    public void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document) {
        final ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
        final String processFee = serviceRequest.getDynamicSingleValue(PROCESSING_FEE);
        if (isEmpty(processFee)) {
            return;
        }
        document.setProcessingFee(Double.parseDouble(processFee));
    }

}
