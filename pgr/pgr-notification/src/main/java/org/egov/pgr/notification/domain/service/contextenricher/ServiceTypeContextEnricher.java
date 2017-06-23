package org.egov.pgr.notification.domain.service.contextenricher;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.ServiceType;
import org.egov.pgr.notification.domain.model.SevaRequest;
import org.egov.pgr.notification.persistence.repository.ServiceTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ServiceTypeContextEnricher implements NotificationContextEnricher {

    private ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeContextEnricher(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Override
    public void enrich(SevaRequest sevaRequest, NotificationContext context) {
        final ServiceType serviceType = this.serviceTypeRepository
            .getServiceTypeByCode(sevaRequest.getServiceTypeCode(), sevaRequest.getTenantId());
        context.setServiceType(serviceType);
    }
}
