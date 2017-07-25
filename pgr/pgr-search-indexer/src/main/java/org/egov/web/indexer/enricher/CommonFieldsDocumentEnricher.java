package org.egov.web.indexer.enricher;

import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.ESDateTimeFormatter;
import org.egov.web.indexer.repository.contract.GeoPoint;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

@Service
public class CommonFieldsDocumentEnricher implements ServiceRequestDocumentEnricher {

    private static final String SERVICE_STATUS = "systemStatus";
    private ESDateTimeFormatter esDateTimeFormatter;

    public CommonFieldsDocumentEnricher(ESDateTimeFormatter esDateTimeFormatter) {
        this.esDateTimeFormatter = esDateTimeFormatter;
    }

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
        document.setCreatedDate(esDateTimeFormatter.toESDateTimeString(serviceRequest.getCreatedDate()));
        document.setLastModifiedDate(esDateTimeFormatter.toESDateTimeString(serviceRequest.getLastModifiedDate()));
        document.setEscalationDate(esDateTimeFormatter.toESDateTimeString(serviceRequest.getEscalationDate()));
        document.setDetails(serviceRequest.getDetails());
        document.setLandmarkDetails(serviceRequest.getLandmarkDetails());
        document.setRequesterName(serviceRequest.getFirstName());
        document.setRequesterMobile(serviceRequest.getPhone());
        document.setRequesterEmail(serviceRequest.getEmail());
        document.setRequesterId(serviceRequest.getCitizenUserId());
        document.setServiceTypeName(serviceType.getServiceName());
        document.setServiceTypeCode(serviceRequest.getServiceCode());
        document.setServiceStatusCode(serviceRequest.getDynamicSingleValue(SERVICE_STATUS));
        document.setComplaintSLADays(serviceType.getSlaHours());
        document.setServiceCategoryId(serviceType.getGroupId());
        document.setServiceCategoryName(serviceType.getGroups());
        setServiceLatitudeAndLongitude(document, serviceRequest);
    }

    private void setServiceLatitudeAndLongitude(ServiceRequestDocument document, ServiceRequest serviceRequest) {
        if (serviceRequest.getLat() != null && serviceRequest.getLng() != null) {
            final GeoPoint geoPoint = new GeoPoint(serviceRequest.getLat(), serviceRequest.getLng());
            document.setServiceGeo(geoPoint.toString());
        }
    }
}

