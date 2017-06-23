package org.egov.pgr.notification.domain.service.contextenricher;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SevaRequest;
import org.egov.pgr.common.model.Location;
import org.egov.pgr.common.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationContextEnricher implements NotificationContextEnricher {
    private LocationRepository locationRepository;

    public LocationContextEnricher(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void enrich(SevaRequest sevaRequest, NotificationContext context) {
        final Location location = locationRepository
            .getLocationById(sevaRequest.getLocationId(), sevaRequest.getTenantId());
        context.setLocation(location);
    }
}
