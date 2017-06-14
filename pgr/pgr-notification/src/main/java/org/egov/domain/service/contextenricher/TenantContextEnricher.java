package org.egov.domain.service.contextenricher;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.TenantRepository;
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

