package org.egov.pgr.notification.domain.service.contextenricher;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SevaRequest;
import org.egov.pgr.notification.domain.model.Tenant;
import org.egov.pgr.notification.persistence.repository.TenantRepository;
import org.springframework.stereotype.Service;

@Service
public class TenantContextEnricher implements NotificationContextEnricher {
    private TenantRepository tenantRepository;

    public TenantContextEnricher(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public void enrich(SevaRequest sevaRequest, NotificationContext context) {
        final Tenant tenant = this.tenantRepository.fetchTenantById(sevaRequest.getTenantId());
        context.setTenant(tenant);
    }
}


