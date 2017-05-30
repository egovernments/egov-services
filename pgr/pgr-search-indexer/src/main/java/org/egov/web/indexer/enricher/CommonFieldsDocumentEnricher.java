package org.egov.web.indexer.enricher;

import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

import static org.egov.pgr.common.date.DateFormatter.toDate;

@Service
public class CommonFieldsDocumentEnricher implements ServiceRequestDocumentEnricher {

    private static final String SERVICE_STATUS = "status";

    @Override
    public boolean matches(ServiceType serviceType, SevaRequest sevaRequest) {
        return true;
    }

    @Override
    public void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document) {
        final ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
        document.setTenantId(serviceRequest.getTenantId());
        document.setKeywords(serviceType.getKeywords());
        document.setCrn(serviceRequest.getCrn());
        document.setId(serviceRequest.getCrn());
        document.setCreatedDate(toDate(serviceRequest.getCreatedDate()));
        document.setLastModifiedDate(toDate(serviceRequest.getLastModifiedDate()));
        document.setEscalationDate(toDate(serviceRequest.getEscalationDate()));
        document.setDetails(serviceRequest.getDetails());
        document.setLandmarkDetails(serviceRequest.getLandmarkDetails());
        document.setRequesterName(serviceRequest.getFirstName());
        document.setRequesterMobile(serviceRequest.getPhone());
        document.setRequesterEmail(serviceRequest.getEmail());
        document.setServiceTypeName(serviceRequest.getServiceName());
        document.setServiceTypeCode(serviceRequest.getServiceCode());
        document.setServiceStatusName(serviceRequest.getDynamicSingleValue(SERVICE_STATUS));
        document.setComplaintSLADays(serviceType.getSlaHours());
//        setServiceLatitudeAndLongitude(document, serviceRequest);
    }

//    private void setServiceLatitudeAndLongitude(ServiceRequestDocument document, ServiceRequest serviceRequest) {
//        if (serviceRequest.getLat() != null && serviceRequest.getLng() != null) {
//            document.setServiceGeo(new GeoPoint(serviceRequest.getLat(), serviceRequest.getLng()));
//        }
//    }
}

