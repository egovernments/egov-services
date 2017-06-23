package org.egov.pgr.location.services;

import org.egov.pgr.location.contract.BoundaryResponse;
import org.egov.pgr.location.contract.CrossHierarchyResponse;
import org.egov.pgr.location.model.SevaRequest;
import org.egov.pgr.location.repository.BoundaryRepository;
import org.egov.pgr.location.repository.CrossHierarchyRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private BoundaryRepository boundaryRepository;
    private CrossHierarchyRepository crossHierarchyRepository;

    public LocationService(BoundaryRepository boundaryRepository,
                           CrossHierarchyRepository crossHierarchyRepository) {
        this.boundaryRepository = boundaryRepository;
        this.crossHierarchyRepository = crossHierarchyRepository;
    }

    public SevaRequest enrich(SevaRequest sevaRequest) {
        return sevaRequest.isLocationPresent() ? sevaRequest : enrichLocation(sevaRequest);
    }

    private SevaRequest enrichLocation(SevaRequest sevaRequest) {
        if (sevaRequest.isLocationCoordinatesPresent()) {
            BoundaryResponse response = boundaryRepository
                    .findBoundary(sevaRequest.getLatitude(), sevaRequest.getLongitude(),sevaRequest.getTenantId());

            sevaRequest.update(response);
        } else if (sevaRequest.isCrossHierarchyIdPresent()) {
            CrossHierarchyResponse response = crossHierarchyRepository
                    .getCrossHierarchy(sevaRequest.getCrossHierarchyId(),sevaRequest.getTenantId());
            sevaRequest.update(response);
        }
        return sevaRequest;
    }
}
