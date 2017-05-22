package org.egov.domain.service;

import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;
import org.egov.persistence.repository.ServiceTypeRepository;
import org.egov.persistence.repository.TenantRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private EmailService emailService;
    private SMSService smsService;
    private ServiceTypeRepository serviceTypeRepository;
    private TenantRepository tenantRepository;

    public NotificationService(EmailService emailService,
                               SMSService smsService,
                               ServiceTypeRepository serviceTypeRepository,
                               TenantRepository tenantRepository) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.serviceTypeRepository = serviceTypeRepository;
        this.tenantRepository = tenantRepository;
    }

    public void notify(SevaRequest sevaRequest) {
        final ServiceType serviceType = this.serviceTypeRepository
            .getServiceTypeByCode(sevaRequest.getServiceTypeCode(), sevaRequest.getTenantId());
        final Tenant tenant = this.tenantRepository.fetchTenantById(sevaRequest.getTenantId());
        this.smsService.send(sevaRequest, serviceType);
        this.emailService.send(sevaRequest, serviceType, tenant);
    }
}
