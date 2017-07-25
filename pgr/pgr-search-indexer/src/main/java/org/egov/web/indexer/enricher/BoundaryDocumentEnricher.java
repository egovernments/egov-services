package org.egov.web.indexer.enricher;

import org.egov.web.indexer.contract.Boundary;
import org.egov.web.indexer.contract.ServiceRequest;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.SevaRequest;
import org.egov.web.indexer.repository.BoundaryRepository;
import org.egov.web.indexer.repository.contract.GeoPoint;
import org.egov.web.indexer.repository.contract.ServiceRequestDocument;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isEmpty;


@Service
public class BoundaryDocumentEnricher implements ServiceRequestDocumentEnricher {

    private static final String LOCATION_ID = "systemLocationId";
    private static final String CHILD_LOCATION_ID = "systemChildLocationId";
    private BoundaryRepository boundaryRepository;

    public BoundaryDocumentEnricher(BoundaryRepository boundaryRepository) {
        this.boundaryRepository = boundaryRepository;
    }

    @Override
    public boolean matches(ServiceType serviceType, SevaRequest sevaRequest) {
        return true;
    }

    @Override
    public void enrich(ServiceType serviceType, SevaRequest sevaRequest, ServiceRequestDocument document) {
        final ServiceRequest serviceRequest = sevaRequest.getServiceRequest();
        setWardFields(document, serviceRequest);
        setLocalityFields(document, serviceRequest);
    }

    private void setLocalityFields(ServiceRequestDocument document, ServiceRequest serviceRequest) {
        final String childLocationId = serviceRequest.getDynamicSingleValue(CHILD_LOCATION_ID);
        if (isEmpty(childLocationId)) {
            return;
        }
        Boundary boundary = boundaryRepository
			.fetchBoundaryById(Long.valueOf(childLocationId), serviceRequest.getTenantId());
        if (boundary == null) {
            return;
        }
        document.setLocalityName(boundary.getName());
        document.setLocalityNo(boundary.getBoundaryNum().toString());
        if (boundary.getLongitude() != null && boundary.getLatitude() != null) {
            final GeoPoint geoPoint = new GeoPoint(boundary.getLatitude(), boundary.getLongitude());
            document.setLocalityGeo(geoPoint.toString());
        }
    }

    private void setWardFields(ServiceRequestDocument document, ServiceRequest serviceRequest) {
        final String locationId = serviceRequest.getDynamicSingleValue(LOCATION_ID);
        if (isEmpty(locationId)) {
            return;
        }
        Boundary boundary = boundaryRepository
			.fetchBoundaryById(Long.valueOf(locationId), serviceRequest.getTenantId());
        if (boundary == null) {
            return;
        }
        document.setWardName(boundary.getName());
        document.setWardNo(boundary.getId().toString());
        if (boundary.getLongitude() != null && boundary.getLatitude() != null) {
            final GeoPoint geoPoint = new GeoPoint(boundary.getLatitude(), boundary.getLongitude());
            document.setWardGeo(geoPoint.toString());
        }
    }

}

