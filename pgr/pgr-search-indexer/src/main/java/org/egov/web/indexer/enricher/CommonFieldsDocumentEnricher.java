package org.egov.web.indexer.enricher;

import lombok.extern.slf4j.Slf4j;
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.contract.GeoPoint;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.TimeZone;

import static org.egov.pgr.common.date.DateFormatter.toDate;

@Service
@Slf4j
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
        final Date createdDate = toDate(serviceRequest.getCreatedDate());
        log.info("Default Timezone: {}", TimeZone.getDefault());
        log.info("Created date : {}", createdDate);
        document.setCreatedDate(createdDate);
        final Date lastModifiedDate = toDate(serviceRequest.getLastModifiedDate());
        log.info("Last Modified date : {}", lastModifiedDate);
        document.setLastModifiedDate(lastModifiedDate);
        final Date escalationDate = toDate(serviceRequest.getEscalationDate());
        log.info("Escalation date : {}", escalationDate);
        document.setEscalationDate(escalationDate);
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

