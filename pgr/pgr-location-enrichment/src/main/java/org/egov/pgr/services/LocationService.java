package org.egov.pgr.services;

import org.egov.pgr.contract.BoundaryResponse;
import org.egov.pgr.contract.CrossHierarchyResponse;
import org.egov.pgr.model.SevaRequest;
import org.egov.pgr.repository.BoundaryRepository;
import org.egov.pgr.repository.CrossHierarchyRepository;
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
                    .findBoundary(sevaRequest.getLatitude(), sevaRequest.getLongitude(),"ap.kurnool");
            sevaRequest.update(response);
        } else if (sevaRequest.isCrossHierarchyIdPresent()) {
            CrossHierarchyResponse response = crossHierarchyRepository
                    .getCrossHierarchy(sevaRequest.getCrossHierarchyId());
            sevaRequest.update(response);
        }
        return sevaRequest;
    }
}
