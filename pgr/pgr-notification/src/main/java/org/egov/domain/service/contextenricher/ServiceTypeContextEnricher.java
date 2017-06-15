package org.egov.domain.service.contextenricher;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.persistence.repository.ServiceTypeRepository;
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
